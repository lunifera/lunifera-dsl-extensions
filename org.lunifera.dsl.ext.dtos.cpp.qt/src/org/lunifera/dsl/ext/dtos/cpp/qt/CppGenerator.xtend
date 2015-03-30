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
	«IF feature.isTypeOfDTO && feature.isContained»
	// no key for «feature.toName»
	«ELSE»
	static const QString «feature.toName»Key = "«feature.toName»";	
	«ENDIF»
«ENDFOR»

«IF dto.existsServerName»
// keys used from Server API etc
«FOR feature : dto.allFeatures»
	«IF feature.isTypeOfDTO && feature.isContained»
	// no key for «feature.toName»
	«ELSE»
	static const QString «feature.toName»ForeignKey = "«feature.toServerName»";	
	«ENDIF»
«ENDFOR»
«ENDIF»

/*
 * Default Constructor if «dto.toName» not initialized from QVariantMap
 */
«dto.toName»::«dto.toName»(QObject *parent) :
        QObject(parent)«FOR feature : dto.allFeatures.filter [
		!isToMany && !isTypeOfDTO && !isContained
	]», m«feature.toName.toFirstUpper»(«feature.defaultForType»)«ENDFOR»
{
	«IF dto.existsLazy»
		// lazy references:
		«FOR feature : dto.allFeatures.filter[isLazy]»
		m«feature.toName.toFirstUpper» = «feature.referenceDomainKeyType.defaultForLazyTypeName»;
		«ENDFOR»
	«ENDIF»
	//
}

/*
 * initialize «dto.toName» from QVariantMap
 * Map got from JsonDataAccess or so
 */
void «dto.toName»::fillFromMap(const QVariantMap& «dto.toName.toFirstLower»Map)
{
	m«dto.toName.toFirstUpper»Map = «dto.toName.toFirstLower»Map;
	«FOR feature : dto.allFeatures.filter[!isToMany]»
		«IF feature.isTypeOfDTO»
			«IF feature.isContained»
			// m«feature.toName.toFirstUpper» is parent («feature.toTypeName»* containing «dto.toName»)
			«ELSEIF feature.isLazy»
			// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
			if(m«dto.toName.toFirstUpper»Map.contains(«feature.toName»Key)){
				m«feature.toName.toFirstUpper» = m«dto.toName.toFirstUpper»Map.value(«feature.toName»Key).to«feature.referenceDomainKeyType.mapToLazyTypeName»();
				if(m«feature.toName.toFirstUpper» != «feature.referenceDomainKeyType.defaultForLazyTypeName»){
					// SIGNAL to request a pointer to the corresponding DTO
					emit request«feature.toName.toFirstUpper»AsDTO(m«feature.toName.toFirstUpper»);
				}
			}
			«ELSE»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»*
			if(m«dto.toName.toFirstUpper»Map.contains(«feature.toName»Key)){
				QVariantMap «feature.toName»Map;
				«feature.toName»Map = m«dto.toName.toFirstUpper»Map.value(«feature.toName»Key).toMap();
				if(!«feature.toName»Map.isEmpty()){
					m«feature.toName.toFirstUpper» = new «feature.toTypeName»();
					m«feature.toName.toFirstUpper»->setParent(this);
					m«feature.toName.toFirstUpper»->fillFromMap(«feature.toName»Map);
				}
			}
			«ENDIF»
		«ELSE» 
			«IF feature.isTransient»
			// m«feature.toName.toFirstUpper» is transient - perhaps not included
			if(m«dto.toName.toFirstUpper»Map.contains(«feature.toName.toFirstLower»Key)){
				m«feature.toName.toFirstUpper» = m«dto.toName.toFirstUpper»Map.value(«feature.toName»Key).to«feature.mapToType»();
			}
			«ELSE»
			m«feature.toName.toFirstUpper» = m«dto.toName.toFirstUpper»Map.value(«feature.toName»Key).to«feature.mapToType»();
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
	«FOR feature : dto.allFeatures.filter[isToMany]»
		// m«feature.toName.toFirstUpper» is List of «feature.toTypeName»*
		QVariantList «feature.toName.toFirstLower»List;
		«feature.toName.toFirstLower»List = m«dto.toName.toFirstUpper»Map.value(«feature.toName.toFirstLower»Key).toList();
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
		if(m«feature.toName.toFirstUpper».size() == 0){
			return false;
		}
		«ELSEIF feature.isLazy»
		// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
		«toValidateReference(feature.referenceDomainKeyFeature.toTypeName, feature.toName)»
		«ELSEIF feature.typeOfDTO»
		if(!m«feature.toName.toFirstUpper») {
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
 * To store persistent Data in JsonDataAccess use dataToPersist()
 */
QVariantMap «dto.toName»::toMap()
{
	«FOR feature : dto.allFeatures.filter[isLazy]»
		// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
		m«dto.toName.toFirstUpper»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»);
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[!isLazy]»
		«IF feature.isTypeOfDTO»
			«IF !feature.isContained»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»*
			«IF feature.isToMany»
				m«dto.toName.toFirstUpper»Map.insert(«feature.toName»Key, «feature.toName»AsQVariantList());
			«ELSE»
				if(m«feature.toName.toFirstUpper»){
					m«dto.toName.toFirstUpper»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»->to«feature.toMapOrList»());
				}
			«ENDIF»
			«ELSE»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»* containing «dto.toName»
			«ENDIF»
		«ELSE» 
			m«dto.toName.toFirstUpper»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»);
		«ENDIF»
	«ENDFOR»
	return m«dto.toName.toFirstUpper»Map;
}

«IF dto.existsServerName»
/*
 * Exports Properties from «dto.toName» as QVariantMap
 * To send data as payload to Server
 * Makes it possible to use defferent naming conditions
 */
QVariantMap «dto.toName»::toForeignMap()
{
	QVariantMap foreignMap;
	«FOR feature : dto.allFeatures.filter[isLazy]»
		// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
		foreignMap.insert(«feature.toName»ForeignKey, m«feature.toName.toFirstUpper»);
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[!isLazy]»
		«IF feature.isTypeOfDTO»
			«IF !feature.isContained»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»*
			«IF feature.isToMany»
			foreignMap.insert(«feature.toName»ForeignKey, «feature.toName»AsQVariantList());
			«ELSE»
			if(m«feature.toName.toFirstUpper»){
				foreignMap.insert(«feature.toName»ForeignKey, m«feature.toName.toFirstUpper»->to«feature.toMapOrList»());
			}
			«ENDIF»
			«ELSE»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»* containing «dto.toName»
			«ENDIF»
		«ELSE» 
			foreignMap.insert(«feature.toName»ForeignKey, m«feature.toName.toFirstUpper»);
		«ENDIF»	
	«ENDFOR»
	return foreignMap;
}
«ENDIF»

/*
 * Exports Properties from «dto.toName» as QVariantMap
 * transient properties are excluded
 * To export ALL data use toMap()
 */
QVariantMap «dto.toName»::dataToPersist()
{
	QVariantMap persistMap;
	«FOR feature : dto.allFeatures.filter[!isTransient && isLazy]»
		// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
		persistMap.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»);
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[!isTransient && !isLazy]»
		«IF feature.isTypeOfDTO»
			«IF !feature.isContained»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»*
			«IF feature.isToMany»
				persistMap.insert(«feature.toName»Key, «feature.toName»AsQVariantList());
			«ELSE»
				if(m«feature.toName.toFirstUpper»){
					persistMap.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»->to«feature.toMapOrList»());
				}
			«ENDIF»
			«ELSE»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»* containing «dto.toName»
			«ENDIF»
		«ELSE» 
			persistMap.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»);
		«ENDIF»
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[isTransient]»
		// excluded: m«feature.toName.toFirstUpper»
	«ENDFOR»
	return persistMap;
}
«FOR feature : dto.allFeatures.filter[!isToMany && isLazy]»
«feature.foo»
// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
«feature.referenceDomainKeyType» «dto.toName»::«feature.toName»() const
{
	return m«feature.toName.toFirstUpper»;
}
«feature.toTypeOrQObject» «dto.toName»::«feature.toName»AsDTO() const
{
	return m«feature.toName.toFirstUpper»AsDTO;
}
void «dto.toName»::set«feature.toName.toFirstUpper»(«feature.referenceDomainKeyType» «feature.toName»)
{
	if («feature.toName» != m«feature.toName.toFirstUpper») {
		if («feature.toName» != «feature.referenceDomainKeyType.defaultForLazyTypeName») {
            // connected from DTOManager to lookup for DTO
            emit request«feature.toName.toFirstUpper»AsDTO(«feature.toName»);
        } else {
            // reset pointer, don't delete the independent object !
            m«feature.toName.toFirstUpper»AsDTO = 0;
            emit «feature.toName»Removed(m«feature.toName.toFirstUpper»);
        }
        m«feature.toName.toFirstUpper» = «feature.toName»;
        emit «feature.toName»Changed(«feature.toName»);
    }
}
void «dto.toName»::remove«feature.toName.toFirstUpper»()
{
	set«feature.toName.toFirstUpper»(«feature.referenceDomainKeyType.defaultForLazyTypeName»);
}
bool «dto.toName»::has«feature.toName.toFirstUpper»(){
    if(m«feature.toName.toFirstUpper» != «feature.referenceDomainKeyType.defaultForLazyTypeName»){
        return true;
    } else {
        return false;
    }
}
bool «dto.toName»::has«feature.toName.toFirstUpper»AsDTO(){
    if(m«feature.toName.toFirstUpper»AsDTO){
        return true;
    } else {
        return false;
    }
}
// SLOT
void «dto.toName»::onRequested«feature.toName.toFirstUpper»AsDTO(«feature.toTypeOrQObject» «feature.toTypeName.
		toFirstLower»)
{
    if («feature.toTypeName.toFirstLower») {
        if («feature.toTypeName.toFirstLower»->«feature.referenceDomainKey»() == m«feature.toName.toFirstUpper») {
            m«feature.toName.toFirstUpper»AsDTO = «feature.toTypeName.toFirstLower»;
        }
    }
}
«ENDFOR»
«FOR feature : dto.allFeatures.filter[!isToMany && !isLazy]»
«feature.foo»
«IF feature.isTypeOfDTO && feature.isContained»
// No SETTER for «feature.toName.toFirstUpper» - it's the parent
«feature.toTypeOrQObject» «dto.toName»::«feature.toName»() const
{
	return qobject_cast<«feature.toTypeOrQObject»>(parent());
}
«ELSE»
«feature.toTypeOrQObject» «dto.toName»::«feature.toName»() const
{
	return m«feature.toName.toFirstUpper»;
}
void «dto.toName»::set«feature.toName.toFirstUpper»(«feature.toTypeOrQObject» «feature.toName»)
{
	if («feature.toName» != m«feature.toName.toFirstUpper») {
		«IF feature.isTypeOfDTO»
		if (m«feature.toName.toFirstUpper»){
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
	«IF feature.isTypeOfDTO»
void «dto.toName»::delete«feature.toName.toFirstUpper»()
{
	if (m«feature.toName.toFirstUpper»){
		emit «feature.toName.toFirstLower»Deleted(m«feature.toName.toFirstUpper»->uuid());
		m«feature.toName.toFirstUpper»->deleteLater();
		m«feature.toName.toFirstUpper» = 0;
	}
}
bool «dto.toName»::has«feature.toName.toFirstUpper»(){
    if(m«feature.toName.toFirstUpper»){
        return true;
    } else {
        return false;
    }
}
	«ENDIF»
«ENDIF»
«ENDFOR»
«FOR feature : dto.allFeatures.filter[isToMany]»
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

void «dto.toName»::removeFrom«feature.toName.toFirstUpper»(«feature.toTypeName»* «feature.toTypeName.toFirstLower»)
{
    for (int i = 0; i < m«feature.toName.toFirstUpper».size(); ++i) {
        if (m«feature.toName.toFirstUpper».at(i) == «feature.toTypeName.toFirstLower») {
            m«feature.toName.toFirstUpper».removeAt(i);
            emit removedFrom«feature.toName.toFirstUpper»(«feature.toTypeName.toFirstLower»->uuid());
            «IF feature.hasOpposite»
            // «feature.toName» are contained - so we must delete them
            «feature.toTypeName.toFirstLower»->deleteLater();
            «ELSE»
            // «feature.toName» are independent - DON'T delete them
            «ENDIF»
            return;
        }
    }
    qDebug() << "«feature.toTypeName»* not found in «feature.toName.toFirstLower»";
    // TODO signal error
}

void «dto.toName»::addTo«feature.toName.toFirstUpper»FromMap(const QVariantMap& «feature.toTypeName.toFirstLower»Map)
{
    «feature.toTypeName»* «feature.toTypeName.toFirstLower» = new «feature.toTypeName»();
    «feature.toTypeName.toFirstLower»->setParent(this);
    «feature.toTypeName.toFirstLower»->fillFromMap(«feature.toTypeName.toFirstLower»Map);
    m«feature.toName.toFirstUpper».append(«feature.toTypeName.toFirstLower»);
    emit addedTo«feature.toName.toFirstUpper»(«feature.toTypeName.toFirstLower»);
}

void «dto.toName»::removeFrom«feature.toName.toFirstUpper»ByKey(const QString& uuid)
{
    for (int i = 0; i < m«feature.toName.toFirstUpper».size(); ++i) {
        if ((m«feature.toName.toFirstUpper».at(i))->toMap().value(uuidKey).toString() == uuid) {
            «IF feature.hasOpposite»
            «feature.toTypeName»* «feature.toTypeName.toFirstLower»;
            «feature.toTypeName.toFirstLower» = m«feature.toName.toFirstUpper».at(i);
            m«feature.toName.toFirstUpper».removeAt(i);
            emit removedFrom«feature.toName.toFirstUpper»(uuid);
            // «feature.toName» are contained - so we must delete them
            «feature.toTypeName.toFirstLower»->deleteLater();
            «ELSE»
            m«feature.toName.toFirstUpper».removeAt(i);
            emit removedFrom«feature.toName.toFirstUpper»(uuid);
            // «feature.toName» are independent - DON'T delete them
            «ENDIF»
            return;
        }
    }
    qDebug() << "uuid not found in «feature.toName.toFirstLower»: " << uuid;
    // TODO signal error
}
int «dto.toName»::«feature.toName.toFirstLower»Count(){
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
	'''

	def dispatch foo(LDtoAbstractReference ref) '''
		// do ref 
	'''

	def dispatch foo(LDtoReference ref) '''
		// do DTO ref 
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
