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

// keys of QVariantMap used in this APP
«FOR feature : dto.allFeatures»
	«IF feature.isTypeOfDTO && feature.isContained»
	// no key for «feature.toName»
	«ELSE»
	static const QString «feature.toName»Key = "«feature.toName»";	
	«ENDIF»
«ENDFOR»

// keys used from Server API etc
«FOR feature : dto.allFeatures»
	«IF feature.isTypeOfDTO && feature.isContained»
	// no key for «feature.toName»
	«ELSE»
	static const QString «feature.toName»ForeignKey = "«feature.toServerName»";	
	«ENDIF»
«ENDFOR»

/*
 * Default Constructor if «dto.toName» not initialized from QVariantMap
 */
«dto.toName»::«dto.toName»(QObject *parent) :
        QObject(parent)«FOR feature : dto.allFeatures.filter[!isToMany && !isTypeOfDTO && !isContained
        ]», m«feature.toName.toFirstUpper»(«feature.
		defaultForType»)«ENDFOR»
{
	//
}

/*
 * initialize «dto.toName» from QVariantMap
 * Map got from JsonDataAccess or so
 */
void «dto.toName»::initFromMap(QVariantMap «dto.toName.toFirstLower»Map)
{
	m«dto.toName.toFirstUpper»Map = «dto.toName.toFirstLower»Map;
	«FOR feature : dto.allFeatures.filter[!isToMany]»
		«IF feature.isTypeOfDTO»
			«IF feature.isContained»
			// m«feature.toName.toFirstUpper» is parent («feature.toTypeName»* containing «dto.toName»)
			«ELSE»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»*
			«ENDIF»
		«ELSE» 
			m«feature.toName.toFirstUpper» = m«dto.toName.toFirstUpper»Map.value(«feature.toName»Key).to«feature.mapToType»();
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
			«feature.toTypeName»* «feature.toTypeName.toFirstLower» = new «feature.toTypeName»();
			«feature.toTypeName.toFirstLower»->setParent(this);
			«feature.toTypeName.toFirstLower»->initFromMap(«feature.toName.toFirstLower»Map);
			m«feature.toName.toFirstUpper».append(«feature.toTypeName.toFirstLower»);
		}
	«ENDFOR»	
}
	
/*
 * Exports Properties from «dto.toName» as QVariantMap
 * To store Data in JsonDataAccess or so
 */
QVariantMap «dto.toName»::toMap()
{
	«FOR feature : dto.allFeatures»
		«IF feature.isTypeOfDTO»
			«IF !feature.isContained»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»*
			«IF feature.isToMany»
				m«dto.toName.toFirstUpper»Map.insert(«feature.toName»Key, «feature.toName»AsQVariantList());
				«ELSE»
				m«dto.toName.toFirstUpper»Map.insert(«feature.toName»Key, qobject_cast<«feature.toTypeName»*>(m«feature.toName.
		toFirstUpper»)->to«feature.toMapOrList»());
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

/*
 * Exports Properties from «dto.toName» as QVariantMap
 * To send data as payload to Server
 * Makes it possible to use defferent naming conditions
 */
QVariantMap «dto.toName»::toForeignMap()
{
	QVariantMap foreignMap;
	«FOR feature : dto.allFeatures»
		«IF feature.isTypeOfDTO»
			«IF !feature.isContained»
			// m«feature.toName.toFirstUpper» points to «feature.toTypeName»*
			«IF feature.isToMany»
			foreignMap.insert(«feature.toName»ForeignKey, «feature.toName»AsQVariantList());
				«ELSE»
			foreignMap.insert(«feature.toName»ForeignKey, qobject_cast<«feature.toTypeName»*>(m«feature.toName.toFirstUpper»)->to«feature.
		toMapOrList»());
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
	
«FOR feature : dto.allFeatures.filter[!isToMany]»
«feature.foo»
«IF feature.isTypeOfDTO && feature.isContained»
// No SETTER for «feature.toName.toFirstUpper» - it's the parent
«feature.toTypeOrQObject» «dto.toName»::«feature.toName»() const
{
	return this->parent();
}
«ELSE»
«feature.toTypeOrQObject» «dto.toName»::«feature.toName»() const
{
	return m«feature.toName.toFirstUpper»;
}
void «dto.toName»::set«feature.toName.toFirstUpper»(«feature.toTypeOrQObject» «feature.toName»)
{
	if («feature.toName» != m«feature.toName.toFirstUpper») {
		m«feature.toName.toFirstUpper» = «feature.toName»;
		emit «feature.toName»Changed(«feature.toName»);
	}
}
«ENDIF»
«ENDFOR»
«FOR feature : dto.allFeatures.filter[isToMany]»
«feature.foo» 
QVariantList «dto.toName»::«feature.toName»AsQVariantList()
{
	QVariantList «feature.toName»List;
	for (int i = 0; i < m«feature.toName.toFirstUpper».size(); ++i) {
        «feature.toName»List.append(qobject_cast<«feature.toTypeName»*>(m«feature.toName.toFirstUpper».at(i))->toMap());
    }
	return «feature.toName»List;
}
QList<QObject*> «dto.toName»::«feature.toName»()
{
	return m«feature.toName.toFirstUpper»;
}
void «dto.toName»::set«feature.toName.toFirstUpper»(QList<QObject*> «feature.toName») 
{
	if («feature.toName» != m«feature.toName.toFirstUpper») {
		m«feature.toName.toFirstUpper» = «feature.toName»;
		emit «feature.toName»Changed(«feature.toName»);
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
