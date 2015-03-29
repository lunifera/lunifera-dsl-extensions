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
	#include <QDeclarativeListProperty>
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
		«ELSEIF feature.isLazy»
		// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
		Q_PROPERTY(«feature.referenceDomainKeyType» «feature.toName» READ «feature.toName» WRITE set«feature.toName.toFirstUpper» NOTIFY «feature.toName»Changed FINAL)
		Q_PROPERTY(«feature.toTypeOrQObject» «feature.toName»AsDTO READ «feature.toName»AsDTO)
		«ELSE»
		Q_PROPERTY(«feature.toTypeOrQObject» «feature.toName» READ «feature.toName» WRITE set«feature.toName.toFirstUpper» NOTIFY «feature.toName»Changed FINAL)
		«ENDIF»
		«ENDFOR»

		«FOR feature : dto.allFeatures.filter[isToMany]»
		// QDeclarativeListProperty to get easy access from QML
		Q_PROPERTY(QDeclarativeListProperty<ItemDTO> «feature.toName»PropertyList READ «feature.toName»PropertyList CONSTANT)
		«ENDFOR»	

	public:
		«dto.toName»(QObject *parent = 0);
		
		void fillFromMap(const QVariantMap& «dto.toName.toFirstLower»Map);
		void prepareNew();
		bool isValid();
	
		Q_INVOKABLE
		QVariantMap toMap();
		
		«IF dto.existsServerName»
		QVariantMap toForeignMap();
		«ENDIF»
		QVariantMap dataToPersist();

		«FOR feature : dto.allFeatures.filter[!isToMany]»
		«IF feature.isLazy»
		// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
		«feature.referenceDomainKeyType» «feature.toName»() const;
		void set«feature.toName.toFirstUpper»(«feature.referenceDomainKeyType» «feature.toName»);
		«feature.toTypeOrQObject» «feature.toName»AsDTO() const;
		Q_INVOKABLE
		void remove«feature.toName.toFirstUpper»();
		
		Q_INVOKABLE
		bool has«feature.toName.toFirstUpper»();
		
		Q_INVOKABLE
		bool has«feature.toName.toFirstUpper»AsDTO();
		
		«ELSE»
		«feature.toTypeOrQObject» «feature.toName»() const;
		«IF feature.isTypeOfDTO && feature.isContained»
		// no SETTER «feature.toName»() is only convenience method to get the parent
		«ELSE»
		void set«feature.toName.toFirstUpper»(«feature.toTypeOrQObject» «feature.toName»);
		«IF feature.isTypeOfDTO»
		Q_INVOKABLE
		void delete«feature.toName.toFirstUpper»();
		
		Q_INVOKABLE
		bool has«feature.toName.toFirstUpper»();
		
		«ENDIF»
		«ENDIF»
		«ENDIF»
		«ENDFOR»
	
		«FOR feature : dto.allFeatures.filter[isToMany]»
		Q_INVOKABLE
		QVariantList «feature.toName»AsQVariantList();
		
		Q_INVOKABLE
		void addTo«feature.toName.toFirstUpper»(«feature.toTypeName»* «feature.toTypeName.toFirstLower»);
		
		Q_INVOKABLE
		void removeFrom«feature.toName.toFirstUpper»(«feature.toTypeName»* «feature.toTypeName.toFirstLower»);
		
		Q_INVOKABLE
		void addTo«feature.toName.toFirstUpper»FromMap(const QVariantMap& «feature.toTypeName.toFirstLower»Map);
		
		Q_INVOKABLE
		void removeFrom«feature.toName.toFirstUpper»ByKey(const QString& uuid);
		
		Q_INVOKABLE
		int «feature.toName.toFirstLower»Count();
		
		 // access from C++ to positions
		QList<«feature.toTypeName»*> «feature.toName»();
		void set«feature.toName.toFirstUpper»(QList<«feature.toTypeName»*> «feature.toName»);
		// access from QML to positions
		 QDeclarativeListProperty<«feature.toTypeName»> «feature.toName»PropertyList();
		«ENDFOR»
	
		virtual ~«dto.toName.toFirstUpper»();
	
		Q_SIGNALS:
	
		«FOR feature : dto.allFeatures.filter[!isToMany]»
		«IF feature.isLazy»
		// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
		void «feature.toName»Changed(«feature.referenceDomainKeyType» «feature.toName»);
		void «feature.toName»Removed(«feature.referenceDomainKeyType» «feature.toName»);
		void request«feature.toName.toFirstUpper»AsDTO(«feature.referenceDomainKeyType» «feature.toName»);
		«ELSE»
		«IF feature.isTypeOfDTO && feature.isContained»
		// no SIGNAL «feature.toName» is only convenience way to get the parent
		«ELSE»
		void «feature.toName»Changed(«feature.toTypeOrQObject» «feature.toName»);
		«IF feature.isTypeOfDTO»
		void «feature.toName.toFirstLower»Deleted(QString uuid);
		«ENDIF»
		«ENDIF»
		«ENDIF»
		«ENDFOR»
		«FOR feature : dto.allFeatures.filter[isToMany]»
		void «feature.toName»Changed(QList<«feature.toTypeName»*> «feature.toName»);
		void addedTo«feature.toName.toFirstUpper»(«feature.toTypeName»* «feature.toTypeName.toFirstLower»);
		void removedFrom«feature.toName.toFirstUpper»(QString uuid);
		«ENDFOR»
		
	«IF dto.existsLazy»
	public slots:
		«FOR feature : dto.allFeatures.filter[!isToMany]»
		«IF feature.isLazy»
		void onRequested«feature.toName.toFirstUpper»AsDTO(«feature.toTypeOrQObject» «feature.toTypeName.toFirstLower»);
		«ENDIF»
		«ENDFOR»
	«ENDIF»
	
	private:
	
		QVariantMap m«dto.toName.toFirstUpper»Map;
		
		«FOR feature : dto.allFeatures.filter[!isToMany]»
		«IF feature.isTypeOfDTO && feature.isContained»
		// no MEMBER m«feature.toName.toFirstUpper» it's the parent
		«ELSEIF feature.isLazy»
		«feature.referenceDomainKeyType» m«feature.toName.toFirstUpper»;
		«feature.toTypeOrQObject» m«feature.toName.toFirstUpper»AsDTO;
		«ELSE»
		«feature.toTypeOrQObject» m«feature.toName.toFirstUpper»;
		«ENDIF»
		«ENDFOR»
		«FOR feature : dto.allFeatures.filter[isToMany]»
		QList<«feature.toTypeName»*> m«feature.toName.toFirstUpper»;
		// implementation for QDeclarativeListProperty to use
		// QML functions for List of «feature.toTypeName»*
		static void appendTo«feature.toName.toFirstUpper»Property(QDeclarativeListProperty<«feature.toTypeName»> *«feature.toName»List,
			«feature.toTypeName»* «feature.toTypeName.toFirstLower»);
		static int «feature.toName»PropertyCount(QDeclarativeListProperty<«feature.toTypeName»> *«feature.toName»List);
		static «feature.toTypeName»* at«feature.toName.toFirstUpper»Property(QDeclarativeListProperty<«feature.toTypeName»> *«feature.toName»List, int pos);
		static void clear«feature.toName.toFirstUpper»Property(QDeclarativeListProperty<«feature.toTypeName»> *«feature.toName»List);
		«ENDFOR»
	
		Q_DISABLE_COPY («dto.toName.toFirstUpper»)
	};
	Q_DECLARE_METATYPE(«dto.toName.toFirstUpper»*)
	
	#endif /* «dto.toName.toUpperCase»_HPP_ */
	
'''

}
