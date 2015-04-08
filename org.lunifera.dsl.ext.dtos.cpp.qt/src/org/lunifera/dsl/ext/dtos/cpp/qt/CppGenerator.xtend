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

// keys of QVariantMap used in this APP
«FOR feature : dto.allFeatures»
	«IF feature.isTypeOfDataObject && feature.isContained»
	// no key for «feature.toName»
	«ELSE»
	static const QString «feature.toName»Key = "«feature.toName»";
	«ENDIF»
«ENDFOR»

«IF dto.existsForeignPropertyName»
// keys used from Server API etc
«FOR feature : dto.allFeatures»
	«IF feature.isTypeOfDataObject && feature.isContained»
	// no key for «feature.toName»
	«ELSE»
	static const QString «feature.toName»ForeignKey = "«feature.toForeignPropertyName»";
	«ENDIF»
«ENDFOR»
«ENDIF»

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
		«ENDFOR»
	«ENDIF»
	«IF dto.existsLazy»
		// lazy references:
		«FOR feature : dto.allFeatures.filter[isLazy]»
		m«feature.toName.toFirstUpper» = «feature.referenceDomainKeyType.defaultForLazyTypeName»;
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
}

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
	«FOR feature : dto.allFeatures.filter[isToMany && !(isArrayList)]»
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
	«IF dto.existsForeignPropertyName»
	«FOR feature : dto.allFeatures.filter[!isToMany]»
		«IF feature.isTypeOfDataObject»
			«IF feature.isContained»
			// m«feature.toName.toFirstUpper» is parent («feature.toTypeName»* containing «dto.toName»)
			«ELSEIF feature.isLazy»
			// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
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
					m«feature.toName.toFirstUpper» = new «feature.toTypeName»();
					m«feature.toName.toFirstUpper»->setParent(this);
					m«feature.toName.toFirstUpper»->fillFromMap(«feature.toName»Map);
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
	«FOR feature : dto.allFeatures.filter[isToMany && !(isArrayList)]»
		// m«feature.toName.toFirstUpper» is List of «feature.toTypeName»*
		QVariantList «feature.toName.toFirstLower»List;
		«feature.toName.toFirstLower»List = «dto.toName.toFirstLower»Map.value(«feature.toName.toFirstLower»ForeignKey).toList();
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
	«ELSE»
	// no Foreign Properties found in data model
	// use default method
	fillFromMap(«dto.toName.toFirstLower»Map);
	«ENDIF»
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
	«IF dto.existsTransient»
	«FOR feature : dto.allFeatures.filter[!isToMany]»
		«IF feature.isTypeOfDataObject»
			«IF feature.isContained»
			// m«feature.toName.toFirstUpper» is parent («feature.toTypeName»* containing «dto.toName»)
			«ELSEIF feature.isLazy»
			// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
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
					m«feature.toName.toFirstUpper»->fillFromMap(«feature.toName»Map);
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
	«FOR feature : dto.allFeatures.filter[isToMany && !(isArrayList)]»
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
	«ELSE»
	// no Transient Properties found in data model
	// use default method for cache
	fillFromMap(«dto.toName.toFirstLower»Map);
	«ENDIF»
}

void «dto.toName»::prepareNew()
{
	mUuid = QUuid::createUuid().toString();
	mUuid = mUuid.right(mUuid.length() - 1);
	mUuid = mUuid.left(mUuid.length() - 1);
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
		if (has«feature.toName.toFirstUpper»()) {
			«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»);
		}
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[!isLazy]»
		«IF feature.isTypeOfDataObject»
			«IF !feature.isContained»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»*
			«IF feature.isToMany»
				«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, «feature.toName»AsQVariantList());
			«ELSE»
				if (m«feature.toName.toFirstUpper») {
					«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»->to«feature.toMapOrList»());
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
	«IF dto.existsForeignPropertyName»
	QVariantMap «dto.toName.toFirstLower»Map;
	«FOR feature : dto.allFeatures.filter[isLazy]»
		// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
		if (has«feature.toName.toFirstUpper»()) {
			«dto.toName.toFirstLower»Map.insert(«feature.toName»ForeignKey, m«feature.toName.toFirstUpper»);
		}
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[!isLazy]»
		«IF feature.isTypeOfDataObject»
			«IF !feature.isContained»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»*
			«IF feature.isToMany»
			«dto.toName.toFirstLower»Map.insert(«feature.toName»ForeignKey, «feature.toName»AsQVariantList());
			«ELSE»
			if (m«feature.toName.toFirstUpper») {
				«dto.toName.toFirstLower»Map.insert(«feature.toName»ForeignKey, m«feature.toName.toFirstUpper»->to«feature.
		toMapOrList»());
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
	«ELSE»
	// no Foreign Properties found in data model
	return toMap();
	«ENDIF»
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
		if (has«feature.toName.toFirstUpper»()) {
			«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»);
		}
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[!isTransient && !isLazy]»
		«IF feature.isTypeOfDataObject»
			«IF !feature.isContained»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»*
			«IF feature.isToMany»
				«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, «feature.toName»AsQVariantList());
			«ELSE»
				if (m«feature.toName.toFirstUpper») {
					«dto.toName.toFirstLower»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»->to«feature.toMapOrList»());
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
        // set the new lazy reference
        m«feature.toName.toFirstUpper» = «feature.toName»;
        emit «feature.toName»Changed(«feature.toName»);
        if («feature.toName» != «feature.referenceDomainKeyType.defaultForLazyTypeName») {
            // resolve the corresponding Data Object on demand from DataManager
        }
    }
}
void «dto.toName»::remove«feature.toName.toFirstUpper»()
{
	if (m«feature.toName.toFirstUpper» != -1) {
		set«feature.toName.toFirstUpper»(«feature.referenceDomainKeyType.defaultForLazyTypeName»);
	}
}
bool «dto.toName»::has«feature.toName.toFirstUpper»()
{
    if (m«feature.toName.toFirstUpper» != «feature.referenceDomainKeyType.defaultForLazyTypeName») {
        return true;
    } else {
        return false;
    }
}
bool «dto.toName»::is«feature.toName.toFirstUpper»ResolvedAsDataObject()
{
    if (m«feature.toName.toFirstUpper»AsDataObject) {
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
    }
}
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
«feature.toTypeOrQObject» «dto.toName»::«feature.toName»() const
{
	return m«feature.toName.toFirstUpper»;
}
void «dto.toName»::set«feature.toName.toFirstUpper»(«feature.toTypeOrQObject» «feature.toName»)
{
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
    if (m«feature.toName.toFirstUpper») {
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

void «dto.toName»::addTo«feature.toName.toFirstUpper»FromMap(const QVariantMap& «feature.toTypeName.toFirstLower»Map)
{
    «feature.toTypeName»* «feature.toTypeName.toFirstLower» = new «feature.toTypeName»();
    «feature.toTypeName.toFirstLower»->setParent(this);
    «feature.toTypeName.toFirstLower»->fillFromMap(«feature.toTypeName.toFirstLower»Map);
    m«feature.toName.toFirstUpper».append(«feature.toTypeName.toFirstLower»);
    emit addedTo«feature.toName.toFirstUpper»(«feature.toTypeName.toFirstLower»);
}

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
        «feature.toTypeName.toFirstLower»->setParent(«dto.toName.toFirstLower»Object);
        «dto.toName.toFirstLower»Object->m«feature.toName.toFirstUpper».append(«feature.toTypeName.toFirstLower»);
        emit «dto.toName.toFirstLower»Object->addedTo«feature.toName.toFirstUpper»(«feature.toTypeName.toFirstLower»);
    } else {
        qWarning() << "cannot append «feature.toTypeName»* to «feature.toName» " << "Object is not of type «dto.toName»*";
    }
}
int «dto.toName»::«feature.toName»PropertyCount(QDeclarativeListProperty<«feature.toTypeName»> *«feature.toName»List)
{
    qDebug() << "«feature.toName»PropertyCount";
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
    qDebug() << "at«feature.toName.toFirstUpper»Property #" << pos;
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
