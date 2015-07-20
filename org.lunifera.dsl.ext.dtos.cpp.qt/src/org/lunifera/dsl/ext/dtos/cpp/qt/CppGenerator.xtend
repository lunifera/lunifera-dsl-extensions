/**
 * Copyright (c) 2011 - 2014, Lunifera GmbH (Gross Enzersdorf)
 * All rights reserved. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Contributor:
 * 		Florian Pirchner - Copied filter and fixed them for lunifera usecase.
 * 		ekke (Ekkehard Gentz), Rosenheim (Germany)
 */
package org.lunifera.dsl.ext.dtos.cpp.qt

import org.lunifera.dsl.semantic.dto.LDto
import com.google.inject.Inject
import org.lunifera.dsl.semantic.dto.LDtoAbstractAttribute
import org.lunifera.dsl.semantic.dto.LDtoAbstractReference
import org.lunifera.dsl.semantic.dto.LDtoReference
import org.lunifera.dsl.semantic.common.types.LFeature

class CppGenerator {

	@Inject extension CppExtensions

	def String toFileName(LDto dto) {
		dto.name.toFirstUpper + ".cpp"
	}

	def CharSequence toContent(LDto dto) '''
#include "«dto.toName».hpp"
#include <QDebug>
#include <quuid.h>
«FOR reference : dto.references»
	«IF reference.isContained»
	#include "«reference.toTypeName».hpp"
	«ENDIF»
«ENDFOR»
«FOR reference : dto.allFeatures.filter[isTypeOfDataObject]»
	«IF !reference.isContained»
		«IF reference.toTypeName != dto.toName»
			«IF isReferencing(reference, dto)»
			// target also references to this
			#include "«reference.toTypeName».hpp"
			«ENDIF»
		«ENDIF»
	«ENDIF»
«ENDFOR»

// keys of QVariantMap used in this APP
«FOR feature : dto.allFeatures»
	«IF feature.isTypeOfDataObject && feature.isContained»
	// no key for «feature.toName»
	«ELSE»
	static const QString «feature.toName»Key = "«feature.toName»";
	«ENDIF»
«ENDFOR»

// keys used from Server API etc
«FOR feature : dto.allFeatures»
	«IF feature.isTypeOfDataObject && feature.isContained»
	// no key for «feature.toName»
	«ELSE»
	static const QString «feature.toName»ForeignKey = "«feature.toForeignPropertyName»";
	«ENDIF»
«ENDFOR»

/*
 * Default Constructor if «dto.toName» not initialized from QVariantMap
 */
«dto.toName»::«dto.toName»(QObject *parent) :
        QObject(parent)«FOR feature : dto.allFeatures.filter [
		!isToMany && !isTypeOfDataObject && !typeOfDates && !isContained && !isEnum
	]», m«feature.toName.toFirstUpper»(«feature.defaultForType»)«ENDFOR»
{
	«IF dto.existsTypeOfDataObject»
		// set Types of DataObject* to NULL:
		«FOR feature : dto.allFeatures.filter[!isLazy && isTypeOfDataObject && !toMany && !isContained]»
		m«feature.toName.toFirstUpper» = 0;
		«IF feature.toTypeName == "GeoCoordinate" || feature.toTypeName == "GeoAddress"»
		m«feature.toName.toFirstUpper» = new «feature.toTypeName»();
		«ENDIF»
		«ENDFOR»
	«ENDIF»
	«IF dto.existsLazy»
		// lazy references:
		«FOR feature : dto.allFeatures.filter[isLazy]»
		m«feature.toName.toFirstUpper» = «feature.referenceDomainKeyType.defaultForLazyTypeName»;
		m«feature.toName.toFirstUpper»AsDataObject = 0;
		m«feature.toName.toFirstUpper»Invalid = false;
		«IF isHierarchy(dto, feature)»
		// hierarchy of «dto.toName»*
		mIs«feature.toName.toFirstUpper»AsPropertyListInitialized = false;
		«ENDIF»
		«ENDFOR»
	«ENDIF»
	«IF dto.existsEnum»
		// ENUMs:
		«FOR feature : dto.allFeatures.filter[isEnum]»
		m«feature.toName.toFirstUpper» = «feature.toTypeName»::DEFAULT_VALUE;
		«ENDFOR»
	«ENDIF»
	«IF dto.existsDates»
		// Date, Time or Timestamp ? construct null value
		«FOR feature : dto.allFeatures.filter[isTypeOfDates]»
		m«feature.toName.toFirstUpper» = «feature.toTypeName»();
		«ENDFOR»
	«ENDIF»
	«IF dto.existsTransient»
		// transient values (not cached)
		«FOR feature : dto.allFeatures.filter[isTransient]»
		// «feature.toTypeName» m«feature.toName.toFirstUpper»
		«ENDFOR»
	«ENDIF»
		«IF dto.existsLazyArray»
		// lazy Arrays where only keys are persisted
		«FOR feature : dto.allFeatures.filter[isLazyArray]»
		m«feature.toName.toFirstUpper»KeysResolved = false;
		«ENDFOR»
	«ENDIF»
}
«IF dto.hasSqlCachePropertyName»
// S Q L
const QString «dto.toName»::createTableCommand()
{
	QString createSQL = "CREATE TABLE «dto.toName.toFirstLower» (";
	«FOR feature : dto.allFeatures.filter[!isToMany]»
	// «feature.toName»
	createSQL.append(«feature.toName»Key).append("«feature.toSqlColumnType»");
	«IF dto.hasDomainKey && feature.isDomainKey»
	createSQL.append(" PRIMARY KEY");
	«ELSEIF feature.toName == "uuid"»
	createSQL.append(" PRIMARY KEY");
	«ENDIF»
	createSQL.append(", ");
	«ENDFOR»
	//
    createSQL = createSQL.left(createSQL.length()-2);
    createSQL.append(");");
    return createSQL;
}
const QString «dto.toName»::createParameterizedInsertCommand()
{
	QString insertSQL;
    QString valueSQL;
    insertSQL = "INSERT INTO «dto.toName.toFirstLower» (";
    valueSQL = " VALUES (";
    «FOR feature : dto.allFeatures.filter[!isToMany]»
// «feature.toName» 
	insertSQL.append(«feature.toName»Key);
	insertSQL.append(", ");
	valueSQL.append(":");
	valueSQL.append(«feature.toName»Key);
	valueSQL.append(", ");
	«ENDFOR»
//
    insertSQL = insertSQL.left(insertSQL.length()-2);
    insertSQL.append(") ");
    valueSQL = valueSQL.left(valueSQL.length()-2);
    valueSQL.append(") ");
    insertSQL.append(valueSQL);
    return insertSQL;
}
/*
 * Exports Properties from «dto.toName» as QVariantMap
 * transient properties are excluded:
 * To cache as JSON use toCacheMap()
 */
QVariantMap «dto.toName»::toSqlCacheMap()
{
	QVariantMap «dto.toName.toFirstLower»Map;
	«FOR feature : dto.allFeatures.filter[!isTransient && isLazy]»
		// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
		if (m«feature.toName.toFirstUpper» != «feature.referenceDomainKeyType.defaultForLazyTypeName») {
			«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»);
		} else {
			«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, "");
		}
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[isLazyArray]»
		// m«feature.toName.toFirstUpper» points to «feature.toTypeName»*
		// lazy array: persist only keys
		if(m«feature.toName.toFirstUpper»KeysResolved || (m«feature.toName.toFirstUpper»Keys.size() == 0 && m«feature.toName.toFirstUpper».size() != 0)) {
			m«feature.toName.toFirstUpper»Keys.clear();
			for (int i = 0; i < m«feature.toName.toFirstUpper».size(); ++i) {
				«feature.toTypeName»* «feature.toTypeName.toFirstLower»;
				«feature.toTypeName.toFirstLower» = m«feature.toName.toFirstUpper».at(i);
				m«feature.toName.toFirstUpper»Keys << «feature.toTypeName.toFirstLower»->«feature.attributeDomainKey»();
			}
		}
		«dto.toName.toFirstLower»Map.insert(«feature.toName.toFirstLower»Key, m«feature.toName.toFirstUpper»Keys.join(";"));
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[!isTransient && !isLazy && !isLazyArray]»
		«IF feature.isTypeOfDataObject»
			«IF !feature.isContained»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»*
			«IF feature.isToMany»
				«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, «feature.toName»AsQVariantList());
			«ELSE»
				if (m«feature.toName.toFirstUpper») {
					«IF feature.toTypeName == "GeoCoordinate"»
					if (m«feature.toName.toFirstUpper»->isValid()) {
						«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»->asOneSqlColumn());
					} else {
						«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, "");
					}
					«ELSE»
					// TODO «dto.toName.toFirstLower»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»->to«feature.toMapOrList»());
					«ENDIF»
				}
			«ENDIF»
			«ELSE»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»* containing «dto.toName»
			«ENDIF»
		«ELSE» 
			«IF feature.isArrayList»
				// Array of «feature.toTypeName»
				«IF feature.toTypeName == "QString"»
				«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»StringList);
				«ELSE»
				«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, «feature.toName»List());
				«ENDIF»
			«ELSEIF feature.isTypeOfDates»
				if (has«feature.toName.toFirstUpper»()) {
					«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper».toString(«feature.toDateFormatString»));
				}
			«ELSE»
			«IF feature.isEnum»
			// ENUM always as  int
			«ENDIF»
			«IF feature.toTypeName == "bool"»
			// SQLite stores bool as INTEGER 0 or 1
			if (m«feature.toName.toFirstUpper») {
				«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, 1);
			} else {
				«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, 0);
			}
			«ELSE»
			«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»);
			«ENDIF»
			«ENDIF»
		«ENDIF»
	«ENDFOR»
	return «dto.toName.toFirstLower»Map;
}
«dto.sqlMethods»
«ENDIF»
«IF dto.existsLazy || dto.existsLazyArray»

bool «dto.toName»::isAllResolved()
{
	«FOR feature : dto.allFeatures.filter[isLazy]»
	if (has«feature.toName.toFirstUpper»() && !is«feature.toName.toFirstUpper»ResolvedAsDataObject()) {
		return false;
	}
    «ENDFOR»
    «FOR feature : dto.allFeatures.filter[isLazyArray]»
    if(!are«feature.toName.toFirstUpper»KeysResolved()) {
        return false;
    }
    «ENDFOR»
    return true;
}
«ENDIF»

/*
 * initialize «dto.toName» from QVariantMap
 * Map got from JsonDataAccess or so
 * includes also transient values
 * uses own property names
 * corresponding export method: toMap()
 */
void «dto.toName»::fillFromMap(const QVariantMap& «dto.toName.toFirstLower»Map)
{
	«FOR feature : dto.allFeatures.filter[!isToMany]»
		«IF feature.isTypeOfDataObject»
			«IF feature.isContained»
			// m«feature.toName.toFirstUpper» is parent («feature.toTypeName»* containing «dto.toName»)
			«ELSEIF feature.isLazy»
			// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
			«IF isHierarchy(dto, feature)»
			// reset hierarchy of «feature.toName»
			clear«feature.toName.toFirstUpper»PropertyList();
        	«ENDIF»
			if («dto.toName.toFirstLower»Map.contains(«feature.toName»Key)) {
				m«feature.toName.toFirstUpper» = «dto.toName.toFirstLower»Map.value(«feature.toName»Key).to«feature.referenceDomainKeyType.mapToLazyTypeName»();
				if (m«feature.toName.toFirstUpper» != «feature.referenceDomainKeyType.defaultForLazyTypeName») {
					// resolve the corresponding Data Object on demand from DataManager
				}
			}
			«ELSE»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»*
			if («dto.toName.toFirstLower»Map.contains(«feature.toName»Key)) {
				QVariantMap «feature.toName»Map;
				«feature.toName»Map = «dto.toName.toFirstLower»Map.value(«feature.toName»Key).toMap();
				if (!«feature.toName»Map.isEmpty()) {
					m«feature.toName.toFirstUpper» = 0;
					m«feature.toName.toFirstUpper» = new «feature.toTypeName»();
					m«feature.toName.toFirstUpper»->setParent(this);
					m«feature.toName.toFirstUpper»->fillFromMap(«feature.toName»Map);
				}
			}
			«ENDIF»
		«ELSE» 
			«IF feature.isTransient»
			// m«feature.toName.toFirstUpper» is transient
			if («dto.toName.toFirstLower»Map.contains(«feature.toName.toFirstLower»Key)) {
				m«feature.toName.toFirstUpper» = «dto.toName.toFirstLower»Map.value(«feature.toName»Key).to«feature.mapToType»();
			}
			«ELSEIF feature.isEnum»
			// ENUM
			if («dto.toName.toFirstLower»Map.contains(«feature.toName.toFirstLower»Key)) {
				bool* ok;
				ok = false;
				«dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»Key).toInt(ok);
				if (ok) {
					m«feature.toName.toFirstUpper» = «dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»Key).toInt();
				} else {
					m«feature.toName.toFirstUpper» = «feature.toName.toFirstLower»StringToInt(«dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»Key).toString());
				}
			} else {
				m«feature.toName.toFirstUpper» = «feature.toTypeName»::NO_VALUE;
			}
			«ELSEIF feature.isTypeOfDates»
			if («dto.toName.toFirstLower»Map.contains(«feature.toName.toFirstLower»Key)) {
				// always getting the Date as a String (from server or JSON)
				QString «feature.toName.toFirstLower»AsString = «dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»Key).toString();
				m«feature.toName.toFirstUpper» = «feature.toTypeName»::fromString(«feature.toName.toFirstLower»AsString, «feature.toDateFormatString»);
				if (!m«feature.toName.toFirstUpper».isValid()) {
					m«feature.toName.toFirstUpper» = «feature.toTypeName»();
					qDebug() << "m«feature.toName.toFirstUpper» is not valid for String: " << «feature.toName.toFirstLower»AsString;
				}
			}
			«ELSE»
			m«feature.toName.toFirstUpper» = «dto.toName.toFirstLower»Map.value(«feature.toName»Key).to«feature.mapToType»();
			«ENDIF»
			«IF feature.toName == "uuid"»
			if (mUuid.isEmpty()) {
				mUuid = QUuid::createUuid().toString();
				mUuid = mUuid.right(mUuid.length() - 1);
				mUuid = mUuid.left(mUuid.length() - 1);
			}	
			«ENDIF»
		«ENDIF»
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[isToMany && !(isArrayList) && !(isLazyArray)]»
		// m«feature.toName.toFirstUpper» is List of «feature.toTypeName»*
		QVariantList «feature.toName.toFirstLower»List;
		«feature.toName.toFirstLower»List = «dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»Key).toList();
		m«feature.toName.toFirstUpper».clear();
		for (int i = 0; i < «feature.toName.toFirstLower»List.size(); ++i) {
			QVariantMap «feature.toName.toFirstLower»Map;
			«feature.toName.toFirstLower»Map = «feature.toName.toFirstLower»List.at(i).toMap();
			«feature.toTypeName»* «feature.toTypeName.toFirstLower» = new «feature.toTypeName»();
			«feature.toTypeName.toFirstLower»->setParent(this);
			«feature.toTypeName.toFirstLower»->fillFromMap(«feature.toName.toFirstLower»Map);
			m«feature.toName.toFirstUpper».append(«feature.toTypeName.toFirstLower»);
		}
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[isToMany && isLazyArray]»
		// m«feature.toName.toFirstUpper» is (lazy loaded) Array of «feature.toTypeName»*
		m«feature.toName.toFirstUpper»Keys = «dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»Key).toStringList();
		// m«feature.toName.toFirstUpper» must be resolved later if there are keys
		m«feature.toName.toFirstUpper»KeysResolved = (m«feature.toName.toFirstUpper»Keys.size() == 0);
		m«feature.toName.toFirstUpper».clear();
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[isToMany && isArrayList]»
		«IF feature.toTypeName == "QString"»
		m«feature.toName.toFirstUpper»StringList = «dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»Key).toStringList();
		«ELSE»
		// m«feature.toName.toFirstUpper» is Array of «feature.toTypeName»
		QVariantList «feature.toName»List;
		«feature.toName»List = «dto.toName.toFirstLower»Map.value(«feature.toName»Key).toList();
		m«feature.toName.toFirstUpper».clear();
		for (int i = 0; i < «feature.toName»List.size(); ++i) {
			m«feature.toName.toFirstUpper».append(«feature.toName»List.at(i).to«feature.mapToSingleType»());
		}
		«ENDIF»
	«ENDFOR»
}
/*
 * initialize OrderData from QVariantMap
 * Map got from JsonDataAccess or so
 * includes also transient values
 * uses foreign property names - per ex. from Server API
 * corresponding export method: toForeignMap()
 */
void «dto.toName»::fillFromForeignMap(const QVariantMap& «dto.toName.toFirstLower»Map)
{
	«FOR feature : dto.allFeatures.filter[!isToMany]»
		«IF feature.isTypeOfDataObject»
			«IF feature.isContained»
			// m«feature.toName.toFirstUpper» is parent («feature.toTypeName»* containing «dto.toName»)
			«ELSEIF feature.isLazy»
			// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
			«IF isHierarchy(dto, feature)»
			// reset hierarchy of «feature.toName»
			clear«feature.toName.toFirstUpper»PropertyList();
        	«ENDIF»
			if («dto.toName.toFirstLower»Map.contains(«feature.toName»ForeignKey)) {
				m«feature.toName.toFirstUpper» = «dto.toName.toFirstLower»Map.value(«feature.toName»ForeignKey).to«feature.referenceDomainKeyType.mapToLazyTypeName»();
				if (m«feature.toName.toFirstUpper» != «feature.referenceDomainKeyType.defaultForLazyTypeName») {
					// resolve the corresponding Data Object on demand from DataManager
				}
			}
			«ELSE»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»*
			if («dto.toName.toFirstLower»Map.contains(«feature.toName»ForeignKey)) {
				QVariantMap «feature.toName»Map;
				«feature.toName»Map = «dto.toName.toFirstLower»Map.value(«feature.toName»ForeignKey).toMap();
				if (!«feature.toName»Map.isEmpty()) {
					m«feature.toName.toFirstUpper» = 0;
					m«feature.toName.toFirstUpper» = new «feature.toTypeName»();
					m«feature.toName.toFirstUpper»->setParent(this);
					m«feature.toName.toFirstUpper»->fillFromForeignMap(«feature.toName»Map);
				}
			}
			«ENDIF»
		«ELSE» 
			«IF feature.isTransient»
			// m«feature.toName.toFirstUpper» is transient
			if («dto.toName.toFirstLower»Map.contains(«feature.toName.toFirstLower»ForeignKey)) {
				m«feature.toName.toFirstUpper» = «dto.toName.toFirstLower»Map.value(«feature.toName»ForeignKey).to«feature.
		mapToType»();
			}
			«ELSEIF feature.isEnum»
			// ENUM
			if («dto.toName.toFirstLower»Map.contains(«feature.toName.toFirstLower»ForeignKey)) {
				bool* ok;
				ok = false;
				«dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»ForeignKey).toInt(ok);
				if (ok) {
					m«feature.toName.toFirstUpper» = «dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»ForeignKey).toInt();
				} else {
					m«feature.toName.toFirstUpper» = «feature.toName.toFirstLower»StringToInt(«dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»ForeignKey).toString());
				}
			} else {
				m«feature.toName.toFirstUpper» = «feature.toTypeName»::NO_VALUE;
			}
			«ELSEIF feature.isTypeOfDates»
			if («dto.toName.toFirstLower»Map.contains(«feature.toName.toFirstLower»ForeignKey)) {
				// always getting the Date as a String (from server or JSON)
				QString «feature.toName.toFirstLower»AsString = «dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»ForeignKey).toString();
				m«feature.toName.toFirstUpper» = «feature.toTypeName»::fromString(«feature.toName.toFirstLower»AsString, «feature.toDateFormatString»);
				if (!m«feature.toName.toFirstUpper».isValid()) {
					m«feature.toName.toFirstUpper» = «feature.toTypeName»();
					qDebug() << "m«feature.toName.toFirstUpper» is not valid for String: " << «feature.toName.toFirstLower»AsString;
				}
			}
			«ELSE»
			m«feature.toName.toFirstUpper» = «dto.toName.toFirstLower»Map.value(«feature.toName»ForeignKey).to«feature.mapToType»();
			«ENDIF»
			«IF feature.toName == "uuid"»
			if (mUuid.isEmpty()) {
				mUuid = QUuid::createUuid().toString();
				mUuid = mUuid.right(mUuid.length() - 1);
				mUuid = mUuid.left(mUuid.length() - 1);
			}	
			«ENDIF»
		«ENDIF»
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[isToMany && !(isArrayList) && !(isLazyArray)]»
		// m«feature.toName.toFirstUpper» is List of «feature.toTypeName»*
		QVariantList «feature.toName.toFirstLower»List;
		«feature.toName.toFirstLower»List = «dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»ForeignKey).toList();
		m«feature.toName.toFirstUpper».clear();
		for (int i = 0; i < «feature.toName.toFirstLower»List.size(); ++i) {
			QVariantMap «feature.toName.toFirstLower»Map;
			«feature.toName.toFirstLower»Map = «feature.toName.toFirstLower»List.at(i).toMap();
			«feature.toTypeName»* «feature.toTypeName.toFirstLower» = new «feature.toTypeName»();
			«feature.toTypeName.toFirstLower»->setParent(this);
			«feature.toTypeName.toFirstLower»->fillFromForeignMap(«feature.toName.toFirstLower»Map);
			m«feature.toName.toFirstUpper».append(«feature.toTypeName.toFirstLower»);
		}
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[isToMany && isLazyArray]»
		// m«feature.toName.toFirstUpper» is (lazy loaded) Array of «feature.toTypeName»*
		m«feature.toName.toFirstUpper»Keys = «dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»ForeignKey).toStringList();
		// m«feature.toName.toFirstUpper» must be resolved later if there are keys
		m«feature.toName.toFirstUpper»KeysResolved = (m«feature.toName.toFirstUpper»Keys.size() == 0);
		m«feature.toName.toFirstUpper».clear();
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[isToMany && isArrayList]»
		«IF feature.toTypeName == "QString"»
		m«feature.toName.toFirstUpper»StringList = «dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»ForeignKey).toStringList();
		«ELSE»
		// m«feature.toName.toFirstUpper» is Array of «feature.toTypeName»
		QVariantList «feature.toName»List;
		«feature.toName»List = «dto.toName.toFirstLower»Map.value(«feature.toName»ForeignKey).toList();
		m«feature.toName.toFirstUpper».clear();
		for (int i = 0; i < «feature.toName»List.size(); ++i) {
			m«feature.toName.toFirstUpper».append(«feature.toName»List.at(i).to«feature.mapToSingleType»());
		}
		«ENDIF»
	«ENDFOR»
}
/*
 * initialize OrderData from QVariantMap
 * Map got from JsonDataAccess or so
 * excludes transient values
 * uses own property names
 * corresponding export method: toCacheMap()
 */
void «dto.toName»::fillFromCacheMap(const QVariantMap& «dto.toName.toFirstLower»Map)
{
	«FOR feature : dto.allFeatures.filter[!isToMany]»
		«IF feature.isTypeOfDataObject»
			«IF feature.isContained»
			// m«feature.toName.toFirstUpper» is parent («feature.toTypeName»* containing «dto.toName»)
			«ELSEIF feature.isLazy»
			// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
			«IF isHierarchy(dto, feature)»
			// reset hierarchy of «feature.toName»
			clear«feature.toName.toFirstUpper»PropertyList();
        	«ENDIF»
			if («dto.toName.toFirstLower»Map.contains(«feature.toName»Key)) {
				m«feature.toName.toFirstUpper» = «dto.toName.toFirstLower»Map.value(«feature.toName»Key).to«feature.referenceDomainKeyType.mapToLazyTypeName»();
				if (m«feature.toName.toFirstUpper» != «feature.referenceDomainKeyType.defaultForLazyTypeName») {
					// resolve the corresponding Data Object on demand from DataManager
				}
			}
			«ELSE»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»*
			if («dto.toName.toFirstLower»Map.contains(«feature.toName»Key)) {
				QVariantMap «feature.toName»Map;
				«feature.toName»Map = «dto.toName.toFirstLower»Map.value(«feature.toName»Key).toMap();
				if (!«feature.toName»Map.isEmpty()) {
					m«feature.toName.toFirstUpper» = new «feature.toTypeName»();
					m«feature.toName.toFirstUpper»->setParent(this);
					m«feature.toName.toFirstUpper»->fillFromCacheMap(«feature.toName»Map);
				}
			}
			«ENDIF»
		«ELSE» 
			«IF feature.isTransient»
			// m«feature.toName.toFirstUpper» is transient - don't forget to initialize
			«ELSEIF feature.isEnum»
			// ENUM
			if («dto.toName.toFirstLower»Map.contains(«feature.toName.toFirstLower»Key)) {
				bool* ok;
				ok = false;
				«dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»Key).toInt(ok);
				if (ok) {
					m«feature.toName.toFirstUpper» = «dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»Key).toInt();
				} else {
					m«feature.toName.toFirstUpper» = «feature.toName.toFirstLower»StringToInt(«dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»Key).toString());
				}
			} else {
				m«feature.toName.toFirstUpper» = «feature.toTypeName»::NO_VALUE;
			}
			«ELSEIF feature.isTypeOfDates»
			if («dto.toName.toFirstLower»Map.contains(«feature.toName.toFirstLower»Key)) {
				// always getting the Date as a String (from server or JSON)
				QString «feature.toName.toFirstLower»AsString = «dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»Key).toString();
				m«feature.toName.toFirstUpper» = «feature.toTypeName»::fromString(«feature.toName.toFirstLower»AsString, «feature.toDateFormatString»);
				if (!m«feature.toName.toFirstUpper».isValid()) {
					m«feature.toName.toFirstUpper» = «feature.toTypeName»();
					qDebug() << "m«feature.toName.toFirstUpper» is not valid for String: " << «feature.toName.toFirstLower»AsString;
				}
			}
			«ELSE»
			m«feature.toName.toFirstUpper» = «dto.toName.toFirstLower»Map.value(«feature.toName»Key).to«feature.mapToType»();
			«ENDIF»
			«IF feature.toName == "uuid"»
			if (mUuid.isEmpty()) {
				mUuid = QUuid::createUuid().toString();
				mUuid = mUuid.right(mUuid.length() - 1);
				mUuid = mUuid.left(mUuid.length() - 1);
			}	
			«ENDIF»
		«ENDIF»
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[isToMany && !(isArrayList) && !(isLazyArray)]»
		// m«feature.toName.toFirstUpper» is List of «feature.toTypeName»*
		QVariantList «feature.toName.toFirstLower»List;
		«feature.toName.toFirstLower»List = «dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»Key).toList();
		m«feature.toName.toFirstUpper».clear();
		for (int i = 0; i < «feature.toName.toFirstLower»List.size(); ++i) {
			QVariantMap «feature.toName.toFirstLower»Map;
			«feature.toName.toFirstLower»Map = «feature.toName.toFirstLower»List.at(i).toMap();
			«feature.toTypeName»* «feature.toTypeName.toFirstLower» = new «feature.toTypeName»();
			«feature.toTypeName.toFirstLower»->setParent(this);
			«feature.toTypeName.toFirstLower»->fillFromCacheMap(«feature.toName.toFirstLower»Map);
			m«feature.toName.toFirstUpper».append(«feature.toTypeName.toFirstLower»);
		}
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[isToMany && isLazyArray]»
		// m«feature.toName.toFirstUpper» is (lazy loaded) Array of «feature.toTypeName»*
		m«feature.toName.toFirstUpper»Keys = «dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»Key).toStringList();
		// m«feature.toName.toFirstUpper» must be resolved later if there are keys
		m«feature.toName.toFirstUpper»KeysResolved = (m«feature.toName.toFirstUpper»Keys.size() == 0);
		m«feature.toName.toFirstUpper».clear();
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[isToMany && isArrayList]»
		«IF feature.toTypeName == "QString"»
		m«feature.toName.toFirstUpper»StringList = «dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»Key).toStringList();
		«ELSE»
		// m«feature.toName.toFirstUpper» is Array of «feature.toTypeName»
		QVariantList «feature.toName»List;
		«feature.toName»List = «dto.toName.toFirstLower»Map.value(«feature.toName»Key).toList();
		m«feature.toName.toFirstUpper».clear();
		for (int i = 0; i < «feature.toName»List.size(); ++i) {
			m«feature.toName.toFirstUpper».append(«feature.toName»List.at(i).to«feature.mapToSingleType»());
		}
		«ENDIF»
	«ENDFOR»
}

void «dto.toName»::prepareNew()
{
	«IF dto.hasUuid»
	mUuid = QUuid::createUuid().toString();
	mUuid = mUuid.right(mUuid.length() - 1);
	mUuid = mUuid.left(mUuid.length() - 1);
	«ENDIF»
}

/*
 * Checks if all mandatory attributes, all DomainKeys and uuid's are filled
 */
bool «dto.toName»::isValid()
{
	«FOR feature : dto.allFeatures.filter[isMandatory || toName == "uuid" || isDomainKey]»
		«IF feature.isToMany»
		if (m«feature.toName.toFirstUpper».size() == 0) {
			return false;
		}
		«ELSEIF feature.isLazy»
		// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
		«toValidateReference(feature.referenceDomainKeyFeature.toTypeName, feature.toName)»
		«ELSEIF feature.isTypeOfDataObject»
		if (!m«feature.toName.toFirstUpper») {
			return false;
		}
		«ELSE»
		«feature.toValidate»
		«ENDIF»
	«ENDFOR»
	return true;
}
	
/*
 * Exports Properties from «dto.toName» as QVariantMap
 * exports ALL data including transient properties
 * To store persistent Data in JsonDataAccess use toCacheMap()
 */
QVariantMap «dto.toName»::toMap()
{
	QVariantMap «dto.toName.toFirstLower»Map;
	«FOR feature : dto.allFeatures.filter[isLazy]»
		// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
		if (m«feature.toName.toFirstUpper» != «feature.referenceDomainKeyType.defaultForLazyTypeName») {
			«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»);
		}
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[isLazyArray]»
		// m«feature.toName.toFirstUpper» points to «feature.toTypeName»*
		// lazy array: persist only keys
		if(m«feature.toName.toFirstUpper»KeysResolved || (m«feature.toName.toFirstUpper»Keys.size() == 0 && m«feature.toName.toFirstUpper».size() != 0)) {
			m«feature.toName.toFirstUpper»Keys.clear();
			for (int i = 0; i < m«feature.toName.toFirstUpper».size(); ++i) {
				«feature.toTypeName»* «feature.toTypeName.toFirstLower»;
				«feature.toTypeName.toFirstLower» = m«feature.toName.toFirstUpper».at(i);
				m«feature.toName.toFirstUpper»Keys << «feature.toTypeName.toFirstLower»->«feature.attributeDomainKey»();
			}
		}
		«dto.toName.toFirstLower»Map.insert(«feature.toName.toFirstLower»Key, m«feature.toName.toFirstUpper»Keys);
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[!isLazy && !isLazyArray]»
		«IF feature.isTypeOfDataObject»
			«IF !feature.isContained»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»*
			«IF feature.isToMany»
				«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, «feature.toName»AsQVariantList());
			«ELSE»
				if (m«feature.toName.toFirstUpper») {
				«IF feature.toTypeName == "GeoCoordinate"»
					if (m«feature.toName.toFirstUpper»->isValid()) {
						«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»->to«feature.toMapOrList»());
					}
				«ELSE»
					«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»->to«feature.toMapOrList»());
				«ENDIF»
				}
			«ENDIF»
			«ELSE»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»* containing «dto.toName»
			«ENDIF»
		«ELSE» 
			«IF feature.isArrayList»
				// Array of «feature.toTypeName»
				«IF feature.toTypeName == "QString"»
				«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»StringList);
				«ELSE»
				«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, «feature.toName»List());
				«ENDIF»
			«ELSEIF feature.isTypeOfDates»
				if (has«feature.toName.toFirstUpper»()) {
					«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper».toString(«feature.toDateFormatString»));
				}
			«ELSE»
			«IF feature.isEnum»
			// ENUM always as  int
			«ENDIF»
			«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»);
			«ENDIF»
		«ENDIF»
	«ENDFOR»
	return «dto.toName.toFirstLower»Map;
}

/*
 * Exports Properties from «dto.toName» as QVariantMap
 * To send data as payload to Server
 * Makes it possible to use defferent naming conditions
 */
QVariantMap «dto.toName»::toForeignMap()
{
	QVariantMap «dto.toName.toFirstLower»Map;
	«FOR feature : dto.allFeatures.filter[isLazy]»
		// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
		if (m«feature.toName.toFirstUpper» != «feature.referenceDomainKeyType.defaultForLazyTypeName») {
			«dto.toName.toFirstLower»Map.insert(«feature.toName»ForeignKey, m«feature.toName.toFirstUpper»);
		}
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[isLazyArray]»
		// m«feature.toName.toFirstUpper» points to «feature.toTypeName»*
		// lazy array: persist only keys
		if(m«feature.toName.toFirstUpper»KeysResolved || (m«feature.toName.toFirstUpper»Keys.size() == 0 && m«feature.toName.toFirstUpper».size() != 0)) {
			m«feature.toName.toFirstUpper»Keys.clear();
			for (int i = 0; i < m«feature.toName.toFirstUpper».size(); ++i) {
				«feature.toTypeName»* «feature.toTypeName.toFirstLower»;
				«feature.toTypeName.toFirstLower» = m«feature.toName.toFirstUpper».at(i);
				m«feature.toName.toFirstUpper»Keys << «feature.toTypeName.toFirstLower»->«feature.attributeDomainKey»();
			}
		}
		«dto.toName.toFirstLower»Map.insert(«feature.toName.toFirstLower»Key, m«feature.toName.toFirstUpper»Keys);
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[!isLazy && !isLazyArray]»
		«IF feature.isTypeOfDataObject»
			«IF !feature.isContained»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»*
			«IF feature.isToMany»
			«dto.toName.toFirstLower»Map.insert(«feature.toName»ForeignKey, «feature.toName»AsQVariantList());
			«ELSE»
			if (m«feature.toName.toFirstUpper») {
				«IF feature.toTypeName == "GeoCoordinate"»
					if (m«feature.toName.toFirstUpper»->isValid()) {
						«dto.toName.toFirstLower»Map.insert(«feature.toName»ForeignKey, m«feature.toName.toFirstUpper»->to«feature.toMapOrList»());
					}
				«ELSE»
					«dto.toName.toFirstLower»Map.insert(«feature.toName»ForeignKey, m«feature.toName.toFirstUpper»->to«feature.toMapOrList»());
				«ENDIF»
			}
			«ENDIF»
			«ELSE»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»* containing «dto.toName»
			«ENDIF»
		«ELSE» 
			«IF feature.isArrayList»
				// Array of «feature.toTypeName»
				«IF feature.toTypeName == "QString"»
				«dto.toName.toFirstLower»Map.insert(«feature.toName»ForeignKey, m«feature.toName.toFirstUpper»StringList);
				«ELSE»
				«dto.toName.toFirstLower»Map.insert(«feature.toName»ForeignKey, «feature.toName»List());
				«ENDIF»
			«ELSEIF feature.isTypeOfDates»
				if (has«feature.toName.toFirstUpper»()) {
					«dto.toName.toFirstLower»Map.insert(«feature.toName»ForeignKey, m«feature.toName.toFirstUpper».toString(«feature.toDateFormatString»));
				}
			«ELSE»
			«IF feature.isEnum»
			// ENUM always as  int
			«ENDIF»
			«dto.toName.toFirstLower»Map.insert(«feature.toName»ForeignKey, m«feature.toName.toFirstUpper»);
			«ENDIF»
		«ENDIF»	
	«ENDFOR»
	return «dto.toName.toFirstLower»Map;
}


/*
 * Exports Properties from «dto.toName» as QVariantMap
 * transient properties are excluded:
 «IF dto.existsTransient»* «FOR feature : dto.allFeatures.filter[isTransient] SEPARATOR ", "»m«feature.toName.
		toFirstUpper»«ENDFOR»«ENDIF»
 * To export ALL data use toMap()
 */
QVariantMap «dto.toName»::toCacheMap()
{
	«IF dto.existsTransient»
	QVariantMap «dto.toName.toFirstLower»Map;
	«FOR feature : dto.allFeatures.filter[!isTransient && isLazy]»
		// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
		if (m«feature.toName.toFirstUpper» != «feature.referenceDomainKeyType.defaultForLazyTypeName») {
			«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»);
		}
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[isLazyArray]»
		// m«feature.toName.toFirstUpper» points to «feature.toTypeName»*
		// lazy array: persist only keys
		if(m«feature.toName.toFirstUpper»KeysResolved || (m«feature.toName.toFirstUpper»Keys.size() == 0 && m«feature.toName.toFirstUpper».size() != 0)) {
			m«feature.toName.toFirstUpper»Keys.clear();
			for (int i = 0; i < m«feature.toName.toFirstUpper».size(); ++i) {
				«feature.toTypeName»* «feature.toTypeName.toFirstLower»;
				«feature.toTypeName.toFirstLower» = m«feature.toName.toFirstUpper».at(i);
				m«feature.toName.toFirstUpper»Keys << «feature.toTypeName.toFirstLower»->«feature.attributeDomainKey»();
			}
		}
		«dto.toName.toFirstLower»Map.insert(«feature.toName.toFirstLower»Key, m«feature.toName.toFirstUpper»Keys);
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[!isTransient && !isLazy && !isLazyArray]»
		«IF feature.isTypeOfDataObject»
			«IF !feature.isContained»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»*
			«IF feature.isToMany»
				«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, «feature.toName»AsQVariantList());
			«ELSE»
				if (m«feature.toName.toFirstUpper») {
				«IF feature.toTypeName == "GeoCoordinate"»
					if (m«feature.toName.toFirstUpper»->isValid()) {
						«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»->to«feature.toMapOrList»());
					}
				«ELSE»
					«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»->to«feature.toMapOrList»());
				«ENDIF»
				}
			«ENDIF»
			«ELSE»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»* containing «dto.toName»
			«ENDIF»
		«ELSE» 
			«IF feature.isArrayList»
				// Array of «feature.toTypeName»
				«IF feature.toTypeName == "QString"»
				«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»StringList);
				«ELSE»
				«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, «feature.toName»List());
				«ENDIF»
			«ELSEIF feature.isTypeOfDates»
				if (has«feature.toName.toFirstUpper»()) {
					«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper».toString(«feature.toDateFormatString»));
				}
			«ELSE»
			«IF feature.isEnum»
			// ENUM always as  int
			«ENDIF»
			«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»);
			«ENDIF»
		«ENDIF»
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[isTransient]»
		// excluded: m«feature.toName.toFirstUpper»
	«ENDFOR»
	return «dto.toName.toFirstLower»Map;
	«ELSE»
	// no transient properties found from data model
	// use default toMao()
	return toMap();
	«ENDIF»
}
«FOR feature : dto.allFeatures.filter[!isToMany && isLazy]»
«feature.foo»
// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
«feature.referenceDomainKeyType» «dto.toName»::«feature.toName»() const
{
	return m«feature.toName.toFirstUpper»;
}
«feature.toTypeOrQObject» «dto.toName»::«feature.toName»AsDataObject() const
{
	return m«feature.toName.toFirstUpper»AsDataObject;
}
void «dto.toName»::set«feature.toName.toFirstUpper»(«feature.referenceDomainKeyType» «feature.toName»)
{
	if («feature.toName» != m«feature.toName.toFirstUpper») {
        // remove old Data Object if one was resolved
        if (m«feature.toName.toFirstUpper»AsDataObject) {
            // reset pointer, don't delete the independent object !
            m«feature.toName.toFirstUpper»AsDataObject = 0;
        }
        «IF isHierarchy(dto, feature)»
        // reset hierarchy of «feature.toName»
        clear«feature.toName.toFirstUpper»PropertyList();
        «ENDIF»
        // set the new lazy reference
        m«feature.toName.toFirstUpper» = «feature.toName»;
        m«feature.toName.toFirstUpper»Invalid = false;
        emit «feature.toName»Changed(«feature.toName»);
        if («feature.toName» != «feature.referenceDomainKeyType.defaultForLazyTypeName») {
            // resolve the corresponding Data Object on demand from DataManager
        }
    }
}
void «dto.toName»::remove«feature.toName.toFirstUpper»()
{
	if (m«feature.toName.toFirstUpper» != «feature.referenceDomainKeyFeature.defaultForType») {
		set«feature.toName.toFirstUpper»(«feature.referenceDomainKeyType.defaultForLazyTypeName»);
	}
}
bool «dto.toName»::has«feature.toName.toFirstUpper»()
{
    if (!m«feature.toName.toFirstUpper»Invalid && m«feature.toName.toFirstUpper» != «feature.referenceDomainKeyType.defaultForLazyTypeName») {
        return true;
    } else {
        return false;
    }
}
bool «dto.toName»::is«feature.toName.toFirstUpper»ResolvedAsDataObject()
{
    if (!m«feature.toName.toFirstUpper»Invalid && m«feature.toName.toFirstUpper»AsDataObject) {
        return true;
    } else {
        return false;
    }
}

// lazy bound Data Object was resolved. overwrite «feature.referenceDomainKey» if different
void «dto.toName»::resolve«feature.toName.toFirstUpper»AsDataObject(«feature.toTypeName»* «feature.toTypeName.
		toFirstLower»)
{
    if («feature.toTypeName.toFirstLower») {
        if («feature.toTypeName.toFirstLower»->«feature.referenceDomainKey»() != m«feature.toName.toFirstUpper») {
            set«feature.toName.toFirstUpper»(«feature.toTypeName.toFirstLower»->«feature.referenceDomainKey»());
        }
        m«feature.toName.toFirstUpper»AsDataObject = «feature.toTypeName.toFirstLower»;
        m«feature.toName.toFirstUpper»Invalid = false;
    }
}
void «dto.toName»::mark«feature.toName.toFirstUpper»AsInvalid()
{
    m«feature.toName.toFirstUpper»Invalid = true;
}
«IF isHierarchy(dto, feature)»
// hierarchy of «dto.toName»* (m«feature.toName.toFirstUpper»)
// must be initialized from DataManager before use
void «dto.toName»::init«feature.toName.toFirstUpper»PropertyList(QList<«dto.toName»*> «feature.toName»PropertyList)
{
    m«feature.toName.toFirstUpper»AsPropertyList = «feature.toName»PropertyList;
    mIs«feature.toName.toFirstUpper»AsPropertyListInitialized = true;
}
void «dto.toName»::clear«feature.toName.toFirstUpper»PropertyList()
{
    m«feature.toName.toFirstUpper»AsPropertyList.clear();
    mIs«feature.toName.toFirstUpper»AsPropertyListInitialized = false;
}
/**
 * to access lists from QML we're using QDeclarativeListProperty
 * and implement methods to count and clear
 * because it's a special list of a hierarchy there's no append function
 * now from QML we can use
 * «dto.toName.toFirstLower».«feature.toName»PropertyList.length to get the size
 * «dto.toName.toFirstLower».«feature.toName»PropertyList[2] to get «dto.toName»* at position 2
 * «dto.toName.toFirstLower».«feature.toName»PropertyList = [] to clear the list
 * or get easy access to properties like
 * «dto.toName.toFirstLower».«feature.toName»PropertyList[2].myPropertyName
 */
QDeclarativeListProperty<«dto.toName»> «dto.toName»::«feature.toName»PropertyList()
{
    return QDeclarativeListProperty<«dto.toName»>(this, 0,
            &«dto.toName»::appendTo«feature.toName.toFirstUpper»Property, &«dto.toName»::«feature.toName»PropertyCount,
            &«dto.toName»::at«feature.toName.toFirstUpper»Property, &«dto.toName»::clear«feature.toName.toFirstUpper»Property);
}
void «dto.toName»::appendTo«feature.toName.toFirstUpper»Property(
        QDeclarativeListProperty<«dto.toName»> *«feature.toName»List,
        «dto.toName»* «dto.toName.toFirstLower»)
{
    qWarning() << "Not allowed to APPEND to hierarchy of «feature.toName», m«feature.toName.toFirstUpper»AsPropertyList is only a mirror of existing structures";
}
// implementation for QDeclarativeListProperty to use
// QML functions for hierarchy of «dto.toName»*
int «dto.toName»::«feature.toName»PropertyCount(
        QDeclarativeListProperty<«dto.toName»> *«feature.toName»List)
{
    «dto.toName» *«dto.toName.toFirstLower» = qobject_cast<«dto.toName» *>(
            «feature.toName»List->object);
    if («dto.toName.toFirstLower») {
        return «dto.toName.toFirstLower»->m«feature.toName.toFirstUpper»AsPropertyList.size();
    } else {
        qWarning() << "cannot get size «feature.toName» hierarchy "
                << "Object is not of type «dto.toName»*";
    }
    return 0;
}
«dto.toName»* «dto.toName»::at«feature.toName.toFirstUpper»Property(
        QDeclarativeListProperty<«dto.toName»> *«feature.toName»List, int pos)
{
    «dto.toName» *«dto.toName.toFirstLower» = qobject_cast<«dto.toName» *>(
            «feature.toName»List->object);
    if («dto.toName.toFirstLower») {
        if («dto.toName.toFirstLower»->m«feature.toName.toFirstUpper»AsPropertyList.size() > pos) {
            return «dto.toName.toFirstLower»->m«feature.toName.toFirstUpper»AsPropertyList.at(pos);
        }
        qWarning() << "cannot get «dto.toName»* at pos " << pos << " size is "
                << «dto.toName.toFirstLower»->m«feature.toName.toFirstUpper»AsPropertyList.size();
    } else {
        qWarning() << "cannot get «dto.toName»* at pos " << pos
                << "Object is not of type «dto.toName»*";
    }
    return 0;
}
void «dto.toName»::clear«feature.toName.toFirstUpper»Property(
        QDeclarativeListProperty<«dto.toName»> *«feature.toName»List)
{
    «dto.toName» *«dto.toName.toFirstLower» = qobject_cast<«dto.toName» *>(
            «feature.toName»List->object);
    if («dto.toName.toFirstLower») {
        // nothing contained - so nothing must be deleted
        «dto.toName.toFirstLower»->m«feature.toName.toFirstUpper»AsPropertyList.clear();
    } else {
        qWarning() << "cannot clear «feature.toName» hierarchy "
                << "Object is not of type «dto.toName»*";
    }
}
«ENDIF»
«ENDFOR»
«FOR feature : dto.allFeatures.filter[!isToMany && !isLazy]»
«feature.foo»
«IF feature.isTypeOfDataObject && feature.isContained»
// No SETTER for «feature.toName.toFirstUpper» - it's the parent
«feature.toTypeOrQObject» «dto.toName»::«feature.toName»() const
{
	return qobject_cast<«feature.toTypeOrQObject»>(parent());
}
«ELSEIF feature.isEnum»
int «dto.toName»::«feature.toName»() const
{
	return m«feature.toName.toFirstUpper»;
}
void «dto.toName»::set«feature.toName.toFirstUpper»(int «feature.toName»)
{
	if («feature.toName» != m«feature.toName.toFirstUpper») {
		m«feature.toName.toFirstUpper» = «feature.toName»;
		emit «feature.toName»Changed(«feature.toName»);
	}
}
void «dto.toName»::set«feature.toName.toFirstUpper»(QString «feature.toName»)
{
    set«feature.toName.toFirstUpper»(«feature.toName»StringToInt(«feature.toName»));
}
int «dto.toName»::«feature.toName»StringToInt(QString «feature.toName»)
{
    if («feature.toName».isNull() || «feature.toName».isEmpty()) {
        return «feature.toTypeName»::NO_VALUE;
    }
    «FOR literal : feature.enumFromAttributeType.literals»
    if («feature.toName» == "«literal.name»") {
        return «feature.enumFromAttributeType.literals.indexOf(literal)»;
    }
    «ENDFOR»
    qWarning() << "«feature.toName» wrong enumValue as String: " << «feature.toName»;
    return «feature.toTypeName»::NO_VALUE;
}
«ELSE»
«IF feature.toTypeName == "QDate"»
/**
 * in QML set DateTimePicker value this way:
 * myPicker.value = new Date(«feature.toName»)
 */
«ENDIF»
«IF feature.toTypeName == "QTime"»
/**
 * in QML set DateTimePicker value this way:
 * myPicker.value = myPicker.dateFromTime(«feature.toName»)
 */
«ENDIF»
«feature.toTypeOrQObject» «dto.toName»::«feature.toName»() const
{
	return m«feature.toName.toFirstUpper»;
}
«IF feature.isTypeOfDataObject && !feature.isContained && !(feature.toTypeName == "GeoAddress") && !(feature.toTypeName == "GeoCoordinate")»
/**
 * creates a new «feature.toTypeName»
 * parent is this «dto.toName»
 * if data is successfully entered you must INVOKE set«feature.toName.toFirstUpper»()
 * if edit was canceled you must undoCreate«feature.toName.toFirstUpper» to free up memory
 */
«feature.toTypeName»* «dto.toName»::create«feature.toName.toFirstUpper»()
{
    «feature.toTypeName»* «feature.toTypeName.toFirstLower»;
    «feature.toTypeName.toFirstLower» = new «feature.toTypeName»();
    «feature.toTypeName.toFirstLower»->setParent(this);
    «feature.toTypeName.toFirstLower»->prepareNew();
    return «feature.toTypeName.toFirstLower»;
}

/**
 * if create«feature.toName.toFirstUpper» was canceled from UI
 * this method deletes the Object of Type «feature.toTypeName»
 * 
 * to delete a  «feature.toName» allready set to  «feature.toTypeName»
 * you must use delete«feature.toName.toFirstUpper»
 */
void «dto.toName»::undoCreate«feature.toName.toFirstUpper»(«feature.toTypeName»* «feature.toTypeName.toFirstLower»)
{
    if («feature.toTypeName.toFirstLower») {
        «feature.toTypeName.toFirstLower»->deleteLater();
        «feature.toTypeName.toFirstLower» = 0;
    }
}
«ENDIF»

«IF feature.toTypeName == "QDate"»
/**
 * from QML DateTime Picker use as parameter:
 * «feature.toName» = new Date(myPicker.value)
 */
«ENDIF»
void «dto.toName»::set«feature.toName.toFirstUpper»(«feature.toTypeOrQObject» «feature.toName»)
{
	«IF feature.isTypeOfDataObject»
	if (!«feature.toName») {
	    return;
	}
	«ENDIF»
	if («feature.toName» != m«feature.toName.toFirstUpper») {
		«IF feature.isTypeOfDataObject»
		if (m«feature.toName.toFirstUpper») {
			m«feature.toName.toFirstUpper»->deleteLater();
		}
		m«feature.toName.toFirstUpper» = «feature.toName»;
		m«feature.toName.toFirstUpper»->setParent(this);
		«ELSE»
		m«feature.toName.toFirstUpper» = «feature.toName»;
		«ENDIF»
		emit «feature.toName»Changed(«feature.toName»);
	}
}
«IF feature.toTypeName == "QTime"»
/**
 * INVOKABLE
 * Convenience method to make it easy to set the value from QML
 * use myPicker.value.toTimeString() as Parameter
 */
void «dto.toName»::set«feature.toName.toFirstUpper»FromPickerValue(QString «feature.toName.toFirstLower»Value)
{
    QTime «feature.toName.toFirstLower» = QTime::fromString(«feature.toName.toFirstLower»Value.left(8), "HH:mm:ss");
    if («feature.toName.toFirstLower» != m«feature.toName.toFirstUpper») {
        m«feature.toName.toFirstUpper» = «feature.toName.toFirstLower»;
        emit «feature.toName.toFirstLower»Changed(«feature.toName.toFirstLower»);
    }
}
«ENDIF»
	«IF feature.isTypeOfDataObject»
void «dto.toName»::delete«feature.toName.toFirstUpper»()
{
	if (m«feature.toName.toFirstUpper») {
		emit «feature.toName.toFirstLower»Deleted(m«feature.toName.toFirstUpper»->uuid());
		m«feature.toName.toFirstUpper»->deleteLater();
		m«feature.toName.toFirstUpper» = 0;
	}
}
bool «dto.toName»::has«feature.toName.toFirstUpper»()
{
	«IF feature.toTypeName == "GeoAddress"»
	if (m«feature.toName.toFirstUpper» && !m«feature.toName.toFirstUpper»->isEmpty()) {
	«ELSEIF feature.toTypeName == "GeoCoordinate"»
	if (m«feature.toName.toFirstUpper» && m«feature.toName.toFirstUpper»->isValid()) {
	«ELSE»
	if (m«feature.toName.toFirstUpper») {
	«ENDIF»
        return true;
    } else {
        return false;
    }
}
	«ENDIF»
«ENDIF»
«IF feature.isTypeOfDates»
bool «dto.toName»::has«feature.toName.toFirstUpper»()
{
	return !m«feature.toName.toFirstUpper».isNull() && m«feature.toName.toFirstUpper».isValid();
}
«ENDIF»
«ENDFOR»
«FOR feature : dto.allFeatures.filter[isArrayList && toTypeName == "QString"]»
«feature.foo»
void «dto.toName»::addTo«feature.toName.toFirstUpper»StringList(const «feature.toTypeName»& stringValue)
{
    m«feature.toName.toFirstUpper»StringList.append(stringValue);
    emit addedTo«feature.toName.toFirstUpper»StringList(stringValue);
}

bool «dto.toName»::removeFrom«feature.toName.toFirstUpper»StringList(const «feature.toTypeName»& stringValue)
{
    bool ok = false;
    ok = m«feature.toName.toFirstUpper»StringList.removeOne(stringValue);
    if (!ok) {
    	qDebug() << "«feature.toTypeName»& not found in m«feature.toName.toFirstUpper»StringList: " << stringValue;
    	return false;
    }
    emit removedFrom«feature.toName.toFirstUpper»StringList(stringValue);
    return true;
}
int «dto.toName»::«feature.toName.toFirstLower»Count()
{
    return m«feature.toName.toFirstUpper»StringList.size();
}
QStringList «dto.toName»::«feature.toName»StringList()
{
	return m«feature.toName.toFirstUpper»StringList;
}
void «dto.toName»::set«feature.toName.toFirstUpper»StringList(const QStringList& «feature.toName») 
{
	if («feature.toName» != m«feature.toName.toFirstUpper»StringList) {
		m«feature.toName.toFirstUpper»StringList = «feature.toName»;
		emit «feature.toName»StringListChanged(«feature.toName»);
	}
}
«ENDFOR»
«FOR feature : dto.allFeatures.filter[isArrayList && toTypeName != "QString"]»
«feature.foo»
void «dto.toName»::addTo«feature.toName.toFirstUpper»List(const «feature.toTypeName»& «feature.toTypeName.
		toFirstLower»Value)
{
    m«feature.toName.toFirstUpper».append(«feature.toTypeName.toFirstLower»Value);
    emit addedTo«feature.toName.toFirstUpper»List(«feature.toTypeName.toFirstLower»Value);
}

bool «dto.toName»::removeFrom«feature.toName.toFirstUpper»List(const «feature.toTypeName»& «feature.toTypeName.
		toFirstLower»Value)
{
    bool ok = false;
    ok = m«feature.toName.toFirstUpper».removeOne(«feature.toTypeName.toFirstLower»Value);
    if (!ok) {
    	qDebug() << "«feature.toTypeName»& not found in : m«feature.toName.toFirstUpper»" << «feature.toTypeName.toFirstLower»Value;
    	return false;
    }
    emit removedFrom«feature.toName.toFirstUpper»List(«feature.toTypeName.toFirstLower»Value);
    return true;
}
int «dto.toName»::«feature.toName.toFirstLower»Count()
{
    return m«feature.toName.toFirstUpper».size();
}
QList<«feature.toTypeName»> «dto.toName»::«feature.toName»()
{
    return m«feature.toName.toFirstUpper»;
}
void «dto.toName»::set«feature.toName.toFirstUpper»(QList<«feature.toTypeName»> «feature.toName»)
{
    if («feature.toName» != m«feature.toName.toFirstUpper») {
        m«feature.toName.toFirstUpper» = «feature.toName»;
        QVariantList variantList;
        for (int i = 0; i < «feature.toName».size(); ++i) {
            variantList.append(«feature.toName».at(i));
        }
        emit «feature.toName»ListChanged(variantList);
    }
}
// access from QML to values
QVariantList «dto.toName»::«feature.toName»List()
{
	QVariantList variantList;
    for (int i = 0; i < m«feature.toName.toFirstUpper».size(); ++i) {
        variantList.append(m«feature.toName.toFirstUpper».at(i));
    }
    return variantList;
}
void «dto.toName»::set«feature.toName.toFirstUpper»List(const QVariantList& «feature.toName») 
{
	m«feature.toName.toFirstUpper».clear();
    for (int i = 0; i < «feature.toName».size(); ++i) {
        m«feature.toName.toFirstUpper».append(«feature.toName».at(i).to«feature.mapToSingleType»());
    }
}
«ENDFOR»
«FOR feature : dto.allFeatures.filter[isToMany && !(isArrayList)]»
«feature.foo» 
QVariantList «dto.toName»::«feature.toName»AsQVariantList()
{
	QVariantList «feature.toName»List;
	for (int i = 0; i < m«feature.toName.toFirstUpper».size(); ++i) {
        «feature.toName»List.append((m«feature.toName.toFirstUpper».at(i))->toMap());
    }
	return «feature.toName»List;
}
«IF !feature.isTypeRootDataObject»
/**
 * creates a new «feature.toTypeName»
 * parent is this «dto.toName»
 * if data is successfully entered you must INVOKE addTo«feature.toName.toFirstUpper»()
 * if edit was canceled you must undoCreateElementOf«feature.toName.toFirstUpper» to free up memory
 */
«feature.toTypeName»* «dto.toName»::createElementOf«feature.toName.toFirstUpper»()
{
    «feature.toTypeName»* «feature.toTypeName.toFirstLower»;
    «feature.toTypeName.toFirstLower» = new «feature.toTypeName»();
    «feature.toTypeName.toFirstLower»->setParent(this);
    «feature.toTypeName.toFirstLower»->prepareNew();
    return «feature.toTypeName.toFirstLower»;
}

/**
 * if createElementOf«feature.toName.toFirstUpper» was canceled from UI
 * this method deletes the Object of Type «feature.toTypeName»
 * 
 * to delete a allready into «feature.toName» inserted  «feature.toTypeName»
 * you must use removeFrom«feature.toName.toFirstUpper»
 */
void «dto.toName»::undoCreateElementOf«feature.toName.toFirstUpper»(«feature.toTypeName»* «feature.toTypeName.toFirstLower»)
{
    if («feature.toTypeName.toFirstLower») {
        «feature.toTypeName.toFirstLower»->deleteLater();
        «feature.toTypeName.toFirstLower» = 0;
    }
}
«ELSE»
// no create() or undoCreate() because dto is root object
// see methods in DataManager
«ENDIF»
void «dto.toName»::addTo«feature.toName.toFirstUpper»(«feature.toTypeName»* «feature.toTypeName.toFirstLower»)
{
    m«feature.toName.toFirstUpper».append(«feature.toTypeName.toFirstLower»);
    emit addedTo«feature.toName.toFirstUpper»(«feature.toTypeName.toFirstLower»);
}

bool «dto.toName»::removeFrom«feature.toName.toFirstUpper»(«feature.toTypeName»* «feature.toTypeName.toFirstLower»)
{
    bool ok = false;
    ok = m«feature.toName.toFirstUpper».removeOne(«feature.toTypeName.toFirstLower»);
    if (!ok) {
    	qDebug() << "«feature.toTypeName»* not found in «feature.toName.toFirstLower»";
    	return false;
    }
    «IF feature.referenceHasUuid»
    emit removedFrom«feature.toName.toFirstUpper»ByUuid(«feature.toTypeName.toFirstLower»->uuid());
    «ELSEIF feature.referenceHasDomainKey»
    emit removedFrom«feature.toName.toFirstUpper»By«feature.referenceDomainKey.toFirstUpper»(«feature.toTypeName.toFirstLower»->«feature.referenceDomainKey»());
    «ENDIF»
    «IF feature.hasOpposite»
    // «feature.toName» are contained - so we must delete them
    «feature.toTypeName.toFirstLower»->deleteLater();
    «feature.toTypeName.toFirstLower» = 0;
    «ELSE»
    // «feature.toName» are independent - DON'T delete them
    «ENDIF»
    return true;
}
void «dto.toName»::clear«feature.toName.toFirstUpper»()
{
    for (int i = m«feature.toName.toFirstUpper».size(); i > 0; --i) {
        removeFrom«feature.toName.toFirstUpper»(m«feature.toName.toFirstUpper».last());
    }
}
«IF !feature.isTypeRootDataObject»
void «dto.toName»::addTo«feature.toName.toFirstUpper»FromMap(const QVariantMap& «feature.toTypeName.toFirstLower»Map)
{
    «feature.toTypeName»* «feature.toTypeName.toFirstLower» = new «feature.toTypeName»();
    «feature.toTypeName.toFirstLower»->setParent(this);
    «feature.toTypeName.toFirstLower»->fillFromMap(«feature.toTypeName.toFirstLower»Map);
    m«feature.toName.toFirstUpper».append(«feature.toTypeName.toFirstLower»);
    emit addedTo«feature.toName.toFirstUpper»(«feature.toTypeName.toFirstLower»);
}
«ENDIF»
«IF feature.referenceHasUuid»
bool «dto.toName»::removeFrom«feature.toName.toFirstUpper»ByUuid(const QString& uuid)
{
    for (int i = 0; i < m«feature.toName.toFirstUpper».size(); ++i) {
    	«feature.toTypeName»* «feature.toTypeName.toFirstLower»;
        «feature.toTypeName.toFirstLower» = m«feature.toName.toFirstUpper».at(i);
        if («feature.toTypeName.toFirstLower»->uuid() == uuid) {
        	m«feature.toName.toFirstUpper».removeAt(i);
        	emit removedFrom«feature.toName.toFirstUpper»ByUuid(uuid);
        	«IF feature.hasOpposite»
        	// «feature.toName» are contained - so we must delete them
        	«feature.toTypeName.toFirstLower»->deleteLater();
        	«feature.toTypeName.toFirstLower» = 0;
        	«ELSE»
        	// «feature.toName» are independent - DON'T delete them
        	«ENDIF»
        	return true;
        }
    }
    qDebug() << "uuid not found in «feature.toName.toFirstLower»: " << uuid;
    return false;
}
«ENDIF»

«IF feature.referenceHasDomainKey && feature.referenceDomainKey != "uuid"»
bool «dto.toName»::removeFrom«feature.toName.toFirstUpper»By«feature.referenceDomainKey.toFirstUpper»(const «feature.referenceDomainKeyType»& «feature.referenceDomainKey»)
{
    for (int i = 0; i < m«feature.toName.toFirstUpper».size(); ++i) {
    	«feature.toTypeName»* «feature.toTypeName.toFirstLower»;
        «feature.toTypeName.toFirstLower» = m«feature.toName.toFirstUpper».at(i);
        if («feature.toTypeName.toFirstLower»->«feature.referenceDomainKey»() == «feature.referenceDomainKey») {
        	m«feature.toName.toFirstUpper».removeAt(i);
        	emit removedFrom«feature.toName.toFirstUpper»By«feature.referenceDomainKey.toFirstUpper»(«feature.referenceDomainKey»);
        	«IF feature.hasOpposite»
        	// «feature.toName» are contained - so we must delete them
        	«feature.toTypeName.toFirstLower»->deleteLater();
        	«feature.toTypeName.toFirstLower» = 0;
        	«ELSE»
        	// «feature.toName» are independent - DON'T delete them
        	«ENDIF»
        	return true;
        }
    }
    qDebug() << "«feature.referenceDomainKey» not found in «feature.toName.toFirstLower»: " << «feature.referenceDomainKey»;
    return false;
}
«ENDIF»
«IF feature.isLazyArray»
/**
 * lazy Array of independent Data Objects: only keys are persited
 * so we get a list of keys (uuid or domain keys) from map
 * and we persist only the keys toMap()
 * after initializing the keys must be resolved:
 * - get the list of keys: «feature.toName»Keys()
 * - resolve them from DataManager
 * - then resolve«feature.toName.toFirstUpper»Keys()
 */
bool «dto.toName»::are«feature.toName.toFirstUpper»KeysResolved()
{
    return m«feature.toName.toFirstUpper»KeysResolved;
}

QStringList «dto.toName»::«feature.toName.toFirstLower»Keys()
{
    return m«feature.toName.toFirstUpper»Keys;
}

void «dto.toName»::resolve«feature.toName.toFirstUpper»Keys(QList<«feature.toTypeName»*> «feature.toName.toFirstLower»)
{
    if(m«feature.toName.toFirstUpper»KeysResolved){
        return;
    }
    m«feature.toName.toFirstUpper».clear();
    for (int i = 0; i < «feature.toName.toFirstLower».size(); ++i) {
        addTo«feature.toName.toFirstUpper»(«feature.toName.toFirstLower».at(i));
    }
    m«feature.toName.toFirstUpper»KeysResolved = true;
}

«ENDIF»
int «dto.toName»::«feature.toName.toFirstLower»Count()
{
    return m«feature.toName.toFirstUpper».size();
}
QList<«feature.toTypeName»*> «dto.toName»::«feature.toName»()
{
	return m«feature.toName.toFirstUpper»;
}
void «dto.toName»::set«feature.toName.toFirstUpper»(QList<«feature.toTypeName»*> «feature.toName») 
{
	if («feature.toName» != m«feature.toName.toFirstUpper») {
		m«feature.toName.toFirstUpper» = «feature.toName»;
		emit «feature.toName»Changed(«feature.toName»);
	}
}
/**
 * to access lists from QML we're using QDeclarativeListProperty
 * and implement methods to append, count and clear
 * now from QML we can use
 * «dto.toName.toFirstLower».«feature.toName»PropertyList.length to get the size
 * «dto.toName.toFirstLower».«feature.toName»PropertyList[2] to get «feature.toTypeName»* at position 2
 * «dto.toName.toFirstLower».«feature.toName»PropertyList = [] to clear the list
 * or get easy access to properties like
 * «dto.toName.toFirstLower».«feature.toName»PropertyList[2].myPropertyName
 */
QDeclarativeListProperty<«feature.toTypeName»> «dto.toName»::«feature.toName»PropertyList()
{
    return QDeclarativeListProperty<«feature.toTypeName»>(this, 0, &«dto.toName»::appendTo«feature.toName.toFirstUpper»Property,
            &«dto.toName»::«feature.toName»PropertyCount, &«dto.toName»::at«feature.toName.toFirstUpper»Property,
            &«dto.toName»::clear«feature.toName.toFirstUpper»Property);
}
void «dto.toName»::appendTo«feature.toName.toFirstUpper»Property(QDeclarativeListProperty<«feature.toTypeName»> *«feature.
		toName»List,
        «feature.toTypeName»* «feature.toTypeName.toFirstLower»)
{
    «dto.toName» *«dto.toName.toFirstLower»Object = qobject_cast<«dto.toName» *>(«feature.toName»List->object);
    if («dto.toName.toFirstLower»Object) {
		«IF !feature.isTypeRootDataObject»
		«feature.toTypeName.toFirstLower»->setParent(«dto.toName.toFirstLower»Object);
		«ENDIF»
        «dto.toName.toFirstLower»Object->m«feature.toName.toFirstUpper».append(«feature.toTypeName.toFirstLower»);
        emit «dto.toName.toFirstLower»Object->addedTo«feature.toName.toFirstUpper»(«feature.toTypeName.toFirstLower»);
    } else {
        qWarning() << "cannot append «feature.toTypeName»* to «feature.toName» " << "Object is not of type «dto.toName»*";
    }
}
int «dto.toName»::«feature.toName»PropertyCount(QDeclarativeListProperty<«feature.toTypeName»> *«feature.toName»List)
{
    «dto.toName» *«dto.toName.toFirstLower» = qobject_cast<«dto.toName» *>(«feature.toName»List->object);
    if («dto.toName.toFirstLower») {
        return «dto.toName.toFirstLower»->m«feature.toName.toFirstUpper».size();
    } else {
        qWarning() << "cannot get size «feature.toName» " << "Object is not of type «dto.toName»*";
    }
    return 0;
}
«feature.toTypeName»* «dto.toName»::at«feature.toName.toFirstUpper»Property(QDeclarativeListProperty<«feature.
		toTypeName»> *«feature.toName»List, int pos)
{
    «dto.toName» *«dto.toName.toFirstLower» = qobject_cast<«dto.toName» *>(«feature.toName»List->object);
    if («dto.toName.toFirstLower») {
        if («dto.toName.toFirstLower»->m«feature.toName.toFirstUpper».size() > pos) {
            return «dto.toName.toFirstLower»->m«feature.toName.toFirstUpper».at(pos);
        }
        qWarning() << "cannot get «feature.toTypeName»* at pos " << pos << " size is "
                << «dto.toName.toFirstLower»->m«feature.toName.toFirstUpper».size();
    } else {
        qWarning() << "cannot get «feature.toTypeName»* at pos " << pos << "Object is not of type «dto.toName»*";
    }
    return 0;
}
void «dto.toName»::clear«feature.toName.toFirstUpper»Property(QDeclarativeListProperty<«feature.toTypeName»> *«feature.
		toName»List)
{
    «dto.toName» *«dto.toName.toFirstLower» = qobject_cast<«dto.toName» *>(«feature.toName»List->object);
    if («dto.toName.toFirstLower») {
        «IF feature.hasOpposite»
        // «feature.toName» are contained - so we must delete them
        for (int i = 0; i < «dto.toName.toFirstLower»->m«feature.toName.toFirstUpper».size(); ++i) {
            «dto.toName.toFirstLower»->m«feature.toName.toFirstUpper».at(i)->deleteLater();
        }
        «ELSE»
        // «feature.toName» are independent - DON'T delete them
        «ENDIF»
        «dto.toName.toFirstLower»->m«feature.toName.toFirstUpper».clear();
    } else {
        qWarning() << "cannot clear «feature.toName» " << "Object is not of type «dto.toName»*";
    }
}
«ENDFOR»

«IF dto.isTree»
// it's a Tree of «dto.toName»*
// get a flat list of all children
QList<QObject*> «dto.toName»::all«dto.toName»Children(){
    QList<QObject*> allChildren;
    for (int i = 0; i < this->children().size(); ++i) {
        if (qobject_cast<«dto.toName»*>(this->children().at(i))) {
            allChildren.append(this->children().at(i));
            «dto.toName»* «dto.toName.toFirstLower» = («dto.toName»*)this->children().at(i);
            allChildren.append(«dto.toName.toFirstLower»->all«dto.toName»Children());
        }
    }
    return allChildren;
}
«ENDIF»

«dto.toName»::~«dto.toName»()
{
	// place cleanUp code here
}
	
	'''

// some separate content blocks because of Java 64K limit
def sqlMethods(LDto dto) '''
/*
 * initialize «dto.toName» from QVariantMap
 * Map got from SQlite
 * excludes transient values
 * uses own property names
 * corresponding export method: toSqlMap()
 */
void «dto.toName»::fillFromSql(const QVariantMap& «dto.toName.toFirstLower»Map)
{
	«FOR feature : dto.allFeatures.filter[!isToMany]»
		«IF feature.isTypeOfDataObject»
			«IF feature.isContained»
			// m«feature.toName.toFirstUpper» is parent («feature.toTypeName»* containing «dto.toName»)
			«ELSEIF feature.isLazy»
			// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
			«IF isHierarchy(dto, feature)»
			// reset hierarchy of «feature.toName»
			clear«feature.toName.toFirstUpper»PropertyList();
        	«ENDIF»
			if («dto.toName.toFirstLower»Map.contains(«feature.toName»Key)) {
				m«feature.toName.toFirstUpper» = «dto.toName.toFirstLower»Map.value(«feature.toName»Key).to«feature.referenceDomainKeyType.mapToLazyTypeName»();
				if (m«feature.toName.toFirstUpper» != «feature.referenceDomainKeyType.defaultForLazyTypeName») {
					// resolve the corresponding Data Object on demand from DataManager
				}
			}
			«ELSE»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»*
			if («dto.toName.toFirstLower»Map.contains(«feature.toName»Key)) {
				«IF feature.toTypeName == "GeoCoordinate"»
				QString «feature.toName»String;
				«feature.toName»String = «dto.toName.toFirstLower»Map.value(«feature.toName»Key).toString();
				if (!«feature.toName»String.isEmpty()) {
					m«feature.toName.toFirstUpper» = new «feature.toTypeName»();
					m«feature.toName.toFirstUpper»->setParent(this);
					m«feature.toName.toFirstUpper»->fillFromSql(«feature.toName»String);
				}
				«ELSE»
				QVariantMap «feature.toName»Map;
				«feature.toName»Map = «dto.toName.toFirstLower»Map.value(«feature.toName»Key).toMap();
				if (!«feature.toName»Map.isEmpty()) {
					m«feature.toName.toFirstUpper» = new «feature.toTypeName»();
					m«feature.toName.toFirstUpper»->setParent(this);
					m«feature.toName.toFirstUpper»->fillFromSql(«feature.toName»Map);
				}
				«ENDIF»
			}
			«ENDIF»
		«ELSE» 
			«IF feature.isTransient»
			// m«feature.toName.toFirstUpper» is transient - don't forget to initialize
			«ELSEIF feature.isEnum»
			// ENUM
			if («dto.toName.toFirstLower»Map.contains(«feature.toName.toFirstLower»Key)) {
				bool* ok;
				ok = false;
				«dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»Key).toInt(ok);
				if (ok) {
					m«feature.toName.toFirstUpper» = «dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»Key).toInt();
				} else {
					m«feature.toName.toFirstUpper» = «feature.toName.toFirstLower»StringToInt(«dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»Key).toString());
				}
			} else {
				m«feature.toName.toFirstUpper» = «feature.toTypeName»::NO_VALUE;
			}
			«ELSEIF feature.isTypeOfDates»
			if («dto.toName.toFirstLower»Map.contains(«feature.toName.toFirstLower»Key)) {
				// always getting the Date as a String (from server or JSON)
				QString «feature.toName.toFirstLower»AsString = «dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»Key).toString();
				m«feature.toName.toFirstUpper» = «feature.toTypeName»::fromString(«feature.toName.toFirstLower»AsString, «feature.toDateFormatString»);
				if (!m«feature.toName.toFirstUpper».isValid()) {
					m«feature.toName.toFirstUpper» = «feature.toTypeName»();
					qDebug() << "m«feature.toName.toFirstUpper» is not valid for String: " << «feature.toName.toFirstLower»AsString;
				}
			}
			«ELSE»
			m«feature.toName.toFirstUpper» = «dto.toName.toFirstLower»Map.value(«feature.toName»Key).to«feature.mapToType»();
			«ENDIF»
			«IF feature.toName == "uuid"»
			if (mUuid.isEmpty()) {
				mUuid = QUuid::createUuid().toString();
				mUuid = mUuid.right(mUuid.length() - 1);
				mUuid = mUuid.left(mUuid.length() - 1);
			}	
			«ENDIF»
		«ENDIF»
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[isToMany && !(isArrayList) && !(isLazyArray)]»
		// m«feature.toName.toFirstUpper» is List of «feature.toTypeName»*
		QVariantList «feature.toName.toFirstLower»List;
		«feature.toName.toFirstLower»List = «dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»Key).toList();
		m«feature.toName.toFirstUpper».clear();
		for (int i = 0; i < «feature.toName.toFirstLower»List.size(); ++i) {
			QVariantMap «feature.toName.toFirstLower»Map;
			«feature.toName.toFirstLower»Map = «feature.toName.toFirstLower»List.at(i).toMap();
			«feature.toTypeName»* «feature.toTypeName.toFirstLower» = new «feature.toTypeName»();
			«feature.toTypeName.toFirstLower»->setParent(this);
			«feature.toTypeName.toFirstLower»->fillFromSql(«feature.toName.toFirstLower»Map);
			m«feature.toName.toFirstUpper».append(«feature.toTypeName.toFirstLower»);
		}
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[isToMany && isLazyArray]»
		// m«feature.toName.toFirstUpper» is (lazy loaded) Array of «feature.toTypeName»*
		m«feature.toName.toFirstUpper»Keys = «dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»Key).toString().split(";");
		// m«feature.toName.toFirstUpper» must be resolved later if there are keys
		m«feature.toName.toFirstUpper»KeysResolved = (m«feature.toName.toFirstUpper»Keys.size() == 0);
		m«feature.toName.toFirstUpper».clear();
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[isToMany && isArrayList]»
		«IF feature.toTypeName == "QString"»
		m«feature.toName.toFirstUpper»StringList = «dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»Key).toStringList();
		«ELSE»
		// m«feature.toName.toFirstUpper» is Array of «feature.toTypeName»
		QVariantList «feature.toName»List;
		«feature.toName»List = «dto.toName.toFirstLower»Map.value(«feature.toName»Key).toList();
		m«feature.toName.toFirstUpper».clear();
		for (int i = 0; i < «feature.toName»List.size(); ++i) {
			m«feature.toName.toFirstUpper».append(«feature.toName»List.at(i).to«feature.mapToSingleType»());
		}
		«ENDIF»
	«ENDFOR»
}

'''

	def dispatch foo(LDtoAbstractAttribute att) '''
		// ATT 
		«IF att.isOptional»
			// Optional: «att.toName»
		«ENDIF»
		«IF att.isMandatory»
			// Mandatory: «att.toName»
		«ENDIF»
		«IF att.isDomainKey»
			// Domain KEY: «att.toName»
		«ENDIF»
		«IF att.isEnum»
			// ENUM: «att.toTypeName»::«att.toTypeName»Enum
		«ENDIF»
	'''

	def dispatch foo(LDtoAbstractReference ref) '''
		// do ref 
	'''

	def dispatch foo(LDtoReference ref) '''
		// REF
		«IF ref.opposite != null»
			// Opposite: «ref.opposite.toName»
		«ENDIF»
		«IF ref.isLazy»
			// Lazy: «ref.toName»
		«ENDIF»
		«IF ref.isOptional»
			// Optional: «ref.toName»
		«ENDIF»
		«IF ref.isMandatory»
			// Mandatory: «ref.toName»
		«ENDIF»
	'''

	def dispatch foo(LFeature feature) '''
		// just a helper for max superclass 
	'''
}
