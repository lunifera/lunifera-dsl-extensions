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
import org.lunifera.dsl.semantic.common.types.LPackage
import com.google.inject.Inject

class HppGenerator {

	@Inject extension CppExtensions

	def String toFileName(LDto dto) {
		dto.toName + ".hpp"
	}

	def CharSequence toContent(LDto dto) '''
	«(dto.eContainer as LPackage).toCopyRight»
	#ifndef «dto.toName.toUpperCase»_HPP_
	#define «dto.toName.toUpperCase»_HPP_

	#include <QObject>
	#include <qvariant.h>

	class «dto.toName»: public QObject
	{
		Q_OBJECT

		«FOR feature : dto.allFeatures.filter[!isToMany]»
		Q_PROPERTY(«feature.toTypeName» «feature.toName» READ «feature.toName» WRITE set«feature.toName.toFirstUpper» NOTIFY «feature.
		toName»Changed FINAL)
		«ENDFOR»

		«FOR feature : dto.allFeatures.filter[isToMany]»
		Q_PROPERTY(QVariantList «feature.toName» READ «feature.toName» NOTIFY «feature.toName»Changed FINAL)
		«ENDFOR»	

	public:
		MyEntity(QObject *parent = 0);
		MyEntity(QVariantMap «dto.toName.toFirstLower»Map);
	
		QVariantMap toMap();
		QVariantMap toForeignMap();
	
		«FOR feature : dto.allFeatures.filter[!isToMany]»
		«feature.toTypeName» «feature.toName»() const;
		void set«feature.toName.toFirstUpper»(«feature.toTypeName» «feature.toName»);
		«ENDFOR»
	
		«FOR feature : dto.allFeatures.filter[isToMany]»
		QVariantList «feature.toName»() const;
		«ENDFOR»
	
		virtual ~«dto.toName.toFirstUpper»();
	
		Q_SIGNALS:
	
		«FOR feature : dto.allFeatures.filter[!isToMany]»
		void «feature.toName»Changed(«feature.toTypeName» «feature.toName»);
		«ENDFOR»
		«FOR feature : dto.allFeatures.filter[isToMany]»
		void «feature.toName»Changed(QVariantList «feature.toName»);
		«ENDFOR»
	
	private:
	
		QVariantMap m«dto.toName.toFirstUpper»Map;
		
		«FOR feature : dto.allFeatures»
		«feature.toTypeName» m«feature.toName.toFirstUpper»;
		«ENDFOR»
	
		Q_DISABLE_COPY («dto.toName.toFirstUpper»)
	};
	Q_DECLARE_METATYPE(«dto.toName.toFirstUpper»*)
	
	#endif /* «dto.toName.toUpperCase»_HPP_ */
	
'''

}
