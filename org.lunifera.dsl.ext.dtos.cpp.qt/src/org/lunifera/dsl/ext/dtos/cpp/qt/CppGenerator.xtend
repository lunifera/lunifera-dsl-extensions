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
import org.lunifera.dsl.semantic.common.types.LFeature

class CppGenerator {

	@Inject extension CppExtensions

	def String toFileName(LDto dto) {
		dto.name.toFirstUpper + ".cpp"
	}

	def CharSequence toContent(LDto dto) '''
#include "«dto.toName».hpp"
#include <QDebug>

// keys of QVariantMap used in this APP
«FOR feature : dto.allFeatures»
static const QString «feature.toName»Key = "«feature.toName»";
«ENDFOR»

// keys used from Server API etc
«FOR feature : dto.allFeatures»
static const QString «feature.toName»ForeignKey = "«feature.toServerName»";
«ENDFOR»

/*
 * Default Constructor if «dto.toName» not initialized from QVariantMap
 */
«dto.toName»::«dto.toName»(QObject *parent) :
        QObject(parent)«FOR feature : dto.allFeatures.filter[!isToMany]», m«feature.toName.toFirstUpper»(«feature.
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
	«IF feature.toTypeName.endsWith("DTO")»
	m«feature.toName.toFirstUpper» = «feature.toTypeName»(m«dto.toName.toFirstUpper»Map.value(«feature.toName»Key).to«feature.
		mapToType»());
	«ELSE» 
	m«feature.toName.toFirstUpper» = m«dto.toName.toFirstUpper»Map.value(«feature.toName»Key).to«feature.mapToType»();
	«ENDIF»
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[isToMany]»
	m«feature.toName.toFirstUpper» = m«dto.toName.toFirstUpper»Map.value(«feature.toName»Key).toList();
	«ENDFOR»	
}
	
/*
 * Exports Properties from «dto.toName» as QVariantMap
 * To store Data in JsonDataAccess or so
 */
QVariantMap «dto.toName»::toMap()
{
	«FOR feature : dto.allFeatures»
	m«dto.toName.toFirstUpper»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»);
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
	foreignMap.insert(«feature.toName»ForeignKey, m«feature.toName.toFirstUpper»);
	«ENDFOR»
	return foreignMap;
}
	
«FOR feature : dto.allFeatures.filter[!isToMany]»
«IF feature.toTypeName.endsWith("DTO")»QObject*«ELSE»«feature.toTypeName»«ENDIF» «dto.toName»::«feature.toName»() const
{
	return m«feature.toName.toFirstUpper»;
}
void «dto.toName»::set«feature.toName.toFirstUpper»(«IF feature.toTypeName.endsWith("DTO")»QObject*«ELSE»«feature.toTypeName»«ENDIF» «feature.toName»)
{
	if («feature.toName» != m«feature.toName.toFirstUpper») {
		m«feature.toName.toFirstUpper» = «feature.toName»;
		emit «feature.toName»Changed(«feature.toName»);
	}
}
«ENDFOR»
«FOR feature : dto.allFeatures.filter[isToMany]»
«feature.foo» 
QVariantList «dto.toName»::«feature.toName»() const 
{
	return m«feature.toName.toFirstUpper»;
}
void «dto.toName»::set«feature.toName.toFirstUpper»(QVariantList «feature.toName») 
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
	'''
		

	def dispatch foo(LDtoAbstractReference ref) '''
	// do ref 
	'''

	def dispatch foo(LFeature feature) '''
	// just a helper for max superclass 
	'''
}
