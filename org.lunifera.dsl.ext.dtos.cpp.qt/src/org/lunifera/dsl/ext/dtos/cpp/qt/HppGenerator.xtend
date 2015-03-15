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
	«FOR reference : dto.references»
	«IF !reference.isContained»
	#include "«reference.toTypeName».hpp"
	«ELSE»
	// forward declaration to avoid circular dependencies
	class «reference.toTypeName.toFirstUpper»;
	«ENDIF»
	«ENDFOR»

	class «dto.toName»: public QObject
	{
		Q_OBJECT

		«FOR feature : dto.allFeatures.filter[!isToMany]»
		«IF feature.isTypeOfDTO && feature.isContained»
		Q_PROPERTY(«feature.toTypeOrQObject» «feature.toName» READ «feature.toName»)
		«ELSE»
		Q_PROPERTY(«feature.toTypeOrQObject» «feature.toName» READ «feature.toName» WRITE set«feature.toName.toFirstUpper» NOTIFY «feature.
		toName»Changed FINAL)
		«ENDIF»
		«ENDFOR»

		«FOR feature : dto.allFeatures.filter[isToMany]»
		Q_PROPERTY(QList<QObject*> «feature.toName» READ «feature.toName» WRITE set«feature.toName.toFirstUpper» NOTIFY «feature.toName»Changed FINAL)
		«ENDFOR»	

	public:
		«dto.toName»(QObject *parent = 0);
		
		void fillFromMap(QVariantMap «dto.toName.toFirstLower»Map);
		void prepareNew();
		bool isValid();
	
		QVariantMap toMap();
		QVariantMap toForeignMap();
	
		«FOR feature : dto.allFeatures.filter[!isToMany]»
		«feature.toTypeOrQObject» «feature.toName»() const;
		«IF feature.isTypeOfDTO && feature.isContained»
		// no SETTER «feature.toName»() is only convenience method to get the parent
		«ELSE»
		void set«feature.toName.toFirstUpper»(«feature.toTypeOrQObject» «feature.toName»);
		«ENDIF»
		«ENDFOR»
	
		«FOR feature : dto.allFeatures.filter[isToMany]»
		Q_INVOKABLE
		QVariantList «feature.toName»AsQVariantList();
		QList<QObject*> «feature.toName»();
		void set«feature.toName.toFirstUpper»(QList<QObject*> «feature.toName»);
		«ENDFOR»
	
		virtual ~«dto.toName.toFirstUpper»();
	
		Q_SIGNALS:
	
		«FOR feature : dto.allFeatures.filter[!isToMany]»
		«IF feature.isTypeOfDTO && feature.isContained»
		// no SIGNAL «feature.toName» is only convenience way to get the parent
		«ELSE»
		void «feature.toName»Changed(«feature.toTypeOrQObject» «feature.toName»);
		«ENDIF»
		«ENDFOR»
		«FOR feature : dto.allFeatures.filter[isToMany]»
		void «feature.toName»Changed(QList<QObject*> «feature.toName»);
		«ENDFOR»
	
	private:
	
		QVariantMap m«dto.toName.toFirstUpper»Map;
		
		«FOR feature : dto.allFeatures.filter[!isToMany]»
		«IF feature.isTypeOfDTO && feature.isContained»
		// no MEMBER m«feature.toName.toFirstUpper» it's the parent
		«ELSE»
		«feature.toTypeOrQObject» m«feature.toName.toFirstUpper»;
		«ENDIF»
		«ENDFOR»
		«FOR feature : dto.allFeatures.filter[isToMany]»
		QList<QObject*> m«feature.toName.toFirstUpper»;
		«ENDFOR»
	
		Q_DISABLE_COPY («dto.toName.toFirstUpper»)
	};
	Q_DECLARE_METATYPE(«dto.toName.toFirstUpper»*)
	
	#endif /* «dto.toName.toUpperCase»_HPP_ */
	
'''

}
