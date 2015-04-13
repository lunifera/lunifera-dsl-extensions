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
	«IF dto.allFeatures.filter[isToMany && toTypeName != "QString"].size > 0»
	#include <QDeclarativeListProperty>
	«ENDIF»
	«IF dto.allFeatures.filter[isToMany && toTypeName == "QString"].size > 0»
	#include <QStringList>
	«ENDIF»
	«IF dto.allFeatures.filter[toTypeName == "QDate"].size > 0»
	#include <QDate>
	«ENDIF»
	«IF dto.allFeatures.filter[toTypeName == "QTime"].size > 0»
	#include <QTime>
	«ENDIF»
	«IF dto.allFeatures.filter[toTypeName == "QDateTime"].size > 0»
	#include <QDateTime>
	«ENDIF»
	
	«FOR en : dto.allFeatures.filter[isEnum]»
	#include "«en.toTypeName».hpp"
	«ENDFOR»
	
	«FOR reference : dto.allFeatures.filter[isTypeOfDataObject]»
	«IF !reference.isContained»
		«IF reference.toTypeName != dto.toName»
		#include "«reference.toTypeName».hpp"
		«ENDIF»
	«ELSE»
	// forward declaration to avoid circular dependencies
	class «reference.toTypeName.toFirstUpper»;
	«ENDIF»
	«ENDFOR»

	class «dto.toName»: public QObject
	{
		Q_OBJECT

		«FOR feature : dto.allFeatures.filter[!isToMany]»
		«IF feature.isTypeOfDataObject && feature.isContained»
		Q_PROPERTY(«feature.toTypeOrQObject» «feature.toName» READ «feature.toName»)
		«ELSEIF feature.isLazy»
		// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
		Q_PROPERTY(«feature.referenceDomainKeyType» «feature.toName» READ «feature.toName» WRITE set«feature.toName.toFirstUpper» NOTIFY «feature.toName»Changed FINAL)
		Q_PROPERTY(«feature.toTypeOrQObject» «feature.toName»AsDataObject READ «feature.toName»AsDataObject WRITE resolve«feature.toName.toFirstUpper»AsDataObject NOTIFY «feature.toName»AsDataObjectChanged FINAL)
		«ELSEIF feature.isEnum»
		// int ENUM «feature.toTypeName»
		Q_PROPERTY(int «feature.toName» READ «feature.toName» WRITE set«feature.toName.toFirstUpper» NOTIFY «feature.toName»Changed FINAL)
		«ELSE»
		Q_PROPERTY(«feature.toTypeOrQObject» «feature.toName» READ «feature.toName» WRITE set«feature.toName.toFirstUpper» NOTIFY «feature.toName»Changed FINAL)
		«ENDIF»
		«ENDFOR»

		«FOR feature : dto.allFeatures.filter[isToMany && !isArrayList]»
		// QDeclarativeListProperty to get easy access from QML
		Q_PROPERTY(QDeclarativeListProperty<«feature.toTypeName»> «feature.toName»PropertyList READ «feature.toName»PropertyList CONSTANT)
		«ENDFOR»
		«FOR feature : dto.allFeatures.filter[isToMany && isArrayList]»
		«IF feature.toTypeName == "QString"»
		Q_PROPERTY(QStringList «feature.toName»StringList READ «feature.toName»StringList  WRITE set«feature.toName.toFirstUpper»StringList NOTIFY «feature.toName»StringListChanged FINAL)
		«ELSE»
		// QVariantList to get easy access from QML to «feature.toTypeName» Array
		Q_PROPERTY(QVariantList «feature.toName»List READ «feature.toName»List  WRITE set«feature.toName.toFirstUpper»List NOTIFY «feature.toName»ListChanged FINAL)
		«ENDIF»
		«ENDFOR»	

	public:
		«dto.toName»(QObject *parent = 0);

		«IF dto.existsLazy»
		Q_INVOKABLE
		bool isAllResolved();
		«ENDIF»
		
		void fillFromMap(const QVariantMap& «dto.toName.toFirstLower»Map);
		void fillFromForeignMap(const QVariantMap& «dto.toName.toFirstLower»Map);
		void fillFromCacheMap(const QVariantMap& «dto.toName.toFirstLower»Map);
		
		void prepareNew();
		
		bool isValid();
	
		Q_INVOKABLE
		QVariantMap toMap();
		QVariantMap toForeignMap();
		QVariantMap toCacheMap();

		«FOR feature : dto.allFeatures.filter[!isToMany]»
		«IF feature.isLazy»
		// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
		«feature.referenceDomainKeyType» «feature.toName»() const;
		void set«feature.toName.toFirstUpper»(«feature.referenceDomainKeyType» «feature.toName»);
		«feature.toTypeOrQObject» «feature.toName»AsDataObject() const;
		
		Q_INVOKABLE
		void resolve«feature.toName.toFirstUpper»AsDataObject(«feature.toTypeName.toFirstUpper»* «feature.toTypeName.toFirstLower»);
		
		Q_INVOKABLE
		void remove«feature.toName.toFirstUpper»();
		
		Q_INVOKABLE
		bool has«feature.toName.toFirstUpper»();
		
		Q_INVOKABLE
		bool is«feature.toName.toFirstUpper»ResolvedAsDataObject();
		
		«ELSEIF feature.isEnum»
		int «feature.toName»() const;
		void set«feature.toName.toFirstUpper»(int «feature.toName»);
		void set«feature.toName.toFirstUpper»(QString «feature.toName»);
		«ELSE»
		«feature.toTypeOrQObject» «feature.toName»() const;
		«IF feature.isTypeOfDates»
		
		Q_INVOKABLE
		bool has«feature.toName.toFirstUpper»();
		«ENDIF»
		«IF feature.isTypeOfDataObject && feature.isContained»
		// no SETTER «feature.toName»() is only convenience method to get the parent
		«ELSE»
		void set«feature.toName.toFirstUpper»(«feature.toTypeOrQObject» «feature.toName»);
		«IF feature.isTypeOfDataObject»
		
		Q_INVOKABLE
		void delete«feature.toName.toFirstUpper»();
		
		Q_INVOKABLE
		bool has«feature.toName.toFirstUpper»();
		
		«ENDIF»
		«ENDIF»
		«ENDIF»
		«ENDFOR»
	
		«FOR feature : dto.allFeatures.filter[isToMany]»
		«IF !(feature.isArrayList)»
		
		Q_INVOKABLE
		QVariantList «feature.toName»AsQVariantList();
		
		Q_INVOKABLE
		void addTo«feature.toName.toFirstUpper»(«feature.toTypeName»* «feature.toTypeName.toFirstLower»);
		
		Q_INVOKABLE
		bool removeFrom«feature.toName.toFirstUpper»(«feature.toTypeName»* «feature.toTypeName.toFirstLower»);
		
		Q_INVOKABLE
		void addTo«feature.toName.toFirstUpper»FromMap(const QVariantMap& «feature.toTypeName.toFirstLower»Map);
		«IF feature.referenceHasUuid»
		
		Q_INVOKABLE
		bool removeFrom«feature.toName.toFirstUpper»ByUuid(const QString& uuid);
		«ENDIF»
		«IF feature.referenceHasDomainKey && feature.referenceDomainKey != "uuid"»
		
		Q_INVOKABLE
		bool removeFrom«feature.toName.toFirstUpper»By«feature.referenceDomainKey.toFirstUpper»(const «feature.referenceDomainKeyType»& «feature.referenceDomainKey»);
		«ENDIF»
		«ELSE»
			
			«IF feature.toTypeName == "QString"»
			Q_INVOKABLE
			void addTo«feature.toName.toFirstUpper»StringList(const «feature.toTypeName»& stringValue);
			
			Q_INVOKABLE
			bool removeFrom«feature.toName.toFirstUpper»StringList(const «feature.toTypeName»& stringValue);
			«ELSE»
			Q_INVOKABLE
			void addTo«feature.toName.toFirstUpper»List(const «feature.toTypeName»& «feature.toTypeName.toFirstLower»Value);
			
			Q_INVOKABLE
			bool removeFrom«feature.toName.toFirstUpper»List(const «feature.toTypeName»& «feature.toTypeName.toFirstLower»Value);
			«ENDIF»
		«ENDIF»
		
		Q_INVOKABLE
		int «feature.toName.toFirstLower»Count();
		
		«IF !(feature.isArrayList)»
		 // access from C++ to «feature.toName»
		QList<«feature.toTypeName»*> «feature.toName»();
		void set«feature.toName.toFirstUpper»(QList<«feature.toTypeName»*> «feature.toName»);
		// access from QML to «feature.toName»
		QDeclarativeListProperty<«feature.toTypeName»> «feature.toName»PropertyList();
		 «ELSE»
		 	«IF feature.toTypeName == "QString"»
		 	QStringList «feature.toName»StringList();
		 	void set«feature.toName.toFirstUpper»StringList(const QStringList& «feature.toName»);
		 	«ELSE»
		 	// access from C++ to «feature.toName»
		 	QList<«feature.toTypeName»> «feature.toName»();
		 	void set«feature.toName.toFirstUpper»(QList<«feature.toTypeName»> «feature.toName»);
		 	// access from QML to «feature.toName» (array of «feature.toTypeName»)
		 	QVariantList «feature.toName»List();
		 	void set«feature.toName.toFirstUpper»List(const QVariantList& «feature.toName»);
		 	«ENDIF»
		 «ENDIF»
		«ENDFOR»

		«IF dto.isTree»
			// tree with children of same type - get all as flat list
			QList<QObject*> all«dto.toName.toFirstUpper»Children();
		«ENDIF»
	
		virtual ~«dto.toName.toFirstUpper»();
	
		Q_SIGNALS:
	
		«FOR feature : dto.allFeatures.filter[!isToMany]»
		«IF feature.isLazy»
		// «feature.toName» lazy pointing to «feature.toTypeOrQObject» (domainKey: «feature.referenceDomainKey»)
		void «feature.toName»Changed(«feature.referenceDomainKeyType» «feature.toName»);
		void «feature.toName»AsDataObjectChanged(«feature.toTypeName»* «feature.toTypeName.toFirstLower»);
		«ELSEIF feature.isEnum»
		void «feature.toName»Changed(int «feature.toName»);
		«ELSE»
		«IF feature.isTypeOfDataObject && feature.isContained»
		// no SIGNAL «feature.toName» is only convenience way to get the parent
		«ELSE»
		void «feature.toName»Changed(«feature.toTypeOrQObject» «feature.toName»);
		«IF feature.isTypeOfDataObject»
		void «feature.toName.toFirstLower»Deleted(QString uuid);
		«ENDIF»
		«ENDIF»
		«ENDIF»
		«ENDFOR»
		«FOR feature : dto.allFeatures.filter[isToMany]»
		«IF !(feature.isArrayList)»
		void «feature.toName»Changed(QList<«feature.toTypeName»*> «feature.toName»);
		void addedTo«feature.toName.toFirstUpper»(«feature.toTypeName»* «feature.toTypeName.toFirstLower»);
		«IF feature.referenceHasUuid»
		void removedFrom«feature.toName.toFirstUpper»ByUuid(QString uuid);
		«ENDIF»
		«IF feature.referenceHasDomainKey && feature.referenceDomainKey != "uuid"»
		void removedFrom«feature.toName.toFirstUpper»By«feature.referenceDomainKey.toFirstUpper»(«feature.referenceDomainKeyType» «feature.referenceDomainKey»);
		«ENDIF»
		
		«ELSE»
			«IF feature.toTypeName == "QString"»
			void «feature.toName»StringListChanged(QStringList «feature.toName»);
			void addedTo«feature.toName.toFirstUpper»StringList(«feature.toTypeName» stringValue);
			void removedFrom«feature.toName.toFirstUpper»StringList(«feature.toTypeName» stringValue);
			«ELSE»
			void «feature.toName»ListChanged(QVariantList «feature.toName»);
			void addedTo«feature.toName.toFirstUpper»List(«feature.toTypeName» «feature.toTypeName.toFirstLower»Value);
			void removedFrom«feature.toName.toFirstUpper»List(«feature.toTypeName» «feature.toTypeName.toFirstLower»Value);
			«ENDIF»
		«ENDIF»
		«ENDFOR»
		
	
	private:
	
		«FOR feature : dto.allFeatures.filter[!isToMany]»
		«IF feature.isTypeOfDataObject && feature.isContained»
		// no MEMBER m«feature.toName.toFirstUpper» it's the parent
		«ELSEIF feature.isLazy»
		«feature.referenceDomainKeyType» m«feature.toName.toFirstUpper»;
		«feature.toTypeOrQObject» m«feature.toName.toFirstUpper»AsDataObject;
		«ELSEIF feature.isEnum»
		int m«feature.toName.toFirstUpper»;
		int «feature.toName.toFirstLower»StringToInt(QString «feature.toName.toFirstLower»);
		«ELSE»
		«feature.toTypeOrQObject» m«feature.toName.toFirstUpper»;
		«ENDIF»
		«ENDFOR»
		«FOR feature : dto.allFeatures.filter[isToMany]»
		«IF !(feature.isArrayList)»
		QList<«feature.toTypeName»*> m«feature.toName.toFirstUpper»;
		// implementation for QDeclarativeListProperty to use
		// QML functions for List of «feature.toTypeName»*
		static void appendTo«feature.toName.toFirstUpper»Property(QDeclarativeListProperty<«feature.toTypeName»> *«feature.toName»List,
			«feature.toTypeName»* «feature.toTypeName.toFirstLower»);
		static int «feature.toName»PropertyCount(QDeclarativeListProperty<«feature.toTypeName»> *«feature.toName»List);
		static «feature.toTypeName»* at«feature.toName.toFirstUpper»Property(QDeclarativeListProperty<«feature.toTypeName»> *«feature.toName»List, int pos);
		static void clear«feature.toName.toFirstUpper»Property(QDeclarativeListProperty<«feature.toTypeName»> *«feature.toName»List);
		«ELSE»
			«IF feature.toTypeName == "QString"»
			QStringList m«feature.toName.toFirstUpper»StringList;
			«ELSE»
			QList<«feature.toTypeName»> m«feature.toName.toFirstUpper»;
			«ENDIF»
		«ENDIF»
		«ENDFOR»
	
		Q_DISABLE_COPY («dto.toName.toFirstUpper»)
	};
	Q_DECLARE_METATYPE(«dto.toName.toFirstUpper»*)
	
	#endif /* «dto.toName.toUpperCase»_HPP_ */
	
'''

}
