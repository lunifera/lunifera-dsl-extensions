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
 */
package org.lunifera.dsl.ext.dtos.cpp.qt

import org.lunifera.dsl.semantic.dto.LDto
import org.lunifera.dsl.semantic.common.types.LPackage
import com.google.inject.Inject

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
	static const QString «feature.toName»ForeignKey = "«feature.toName»";
	«ENDFOR»
	
	«dto.toName»::«dto.toName»(QObject *parent) {}
	
	«dto.toName»::«dto.toName»(QVariantMap «dto.toName.toFirstLower»Map) :
				QObject(), m«dto.toName.toFirstUpper»Map(«dto.toName.toFirstLower»Map) {
	«FOR feature : dto.allFeatures.filter[!isToMany]»
		m«feature.toName.toFirstUpper» = m«dto.toName.toFirstUpper»Map.Key(«feature.toName»Key).to«feature.mapToType»();
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[isToMany]»
		m«feature.toName.toFirstUpper» = m«dto.toName.toFirstUpper»Map.Key(«feature.toName»Key).toList();
	«ENDFOR»	
	}
	
	QVariantMap «dto.toName»::toMap(){
	«FOR feature : dto.allFeatures»
		m«dto.toName.toFirstUpper»Map.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»);
	«ENDFOR»
		return m«dto.toName.toFirstUpper»Map;
	}
	
	QVariantMap «dto.toName»::toForeignMap(){
		QVariantMap foreignMap;
		«FOR feature : dto.allFeatures»
		foreignMap.insert(«feature.toName»Key, m«feature.toName.toFirstUpper»);
		«ENDFOR»
		return foreignMap;
	}
	
	«FOR feature : dto.allFeatures.filter[!isToMany]»
	«feature.toTypeName» «dto.toName»::«feature.toName»() const {
		return m«feature.toName.toFirstUpper»;
	}
	void «dto.toName»::set«feature.toName.toFirstUpper»(«feature.toTypeName» «feature.toName») {
		if («feature.toName» != m«feature.toName.toFirstUpper») {
			m«feature.toName.toFirstUpper» = «feature.toName»;
			emit «feature.toName»Changed(«feature.toName»);
		}
	}
	«ENDFOR»
	«FOR feature : dto.allFeatures.filter[isToMany]»
	QVariantList «dto.toName»::«feature.toName»() const {
		return m«feature.toName.toFirstUpper»;
	}
	void «dto.toName»::set«feature.toName.toFirstUpper»(QVariantList «feature.toName») {
		if («feature.toName» != m«feature.toName.toFirstUpper») {
			m«feature.toName.toFirstUpper» = «feature.toName»;
			emit «feature.toName»Changed(«feature.toName»);
		}
	}
	«ENDFOR»
	
	«dto.toName»::~«dto.toName»() {
	// 
	}
	
	'''
	
}