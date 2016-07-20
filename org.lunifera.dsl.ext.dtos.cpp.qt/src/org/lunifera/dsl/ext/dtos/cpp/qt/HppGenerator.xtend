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
	«IF dto.hasTargetOSPropertyName»
	#include <QQmlListProperty>
	«ELSE»
	#include <QDeclarativeListProperty>
	«ENDIF»
	«ELSEIF dto.existsHierarchy»
	«IF dto.hasTargetOSPropertyName»
	#include QQmlListProperty
	«ELSE»
	#include <QDeclarativeListProperty>
	«ENDIF»
	«ENDIF»
	«IF dto.allFeatures.filter[isToMany && (isLazyArray || toTypeName == "QString")].size > 0»
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
	«IF dto.hasSqlCachePropertyName»
	#include <QtSql/QSqlQuery>
	#include <QtSql/QSqlRecord>
	«ENDIF»
	«IF dto.existsGeoCoordinate»
	// #include <QtLocationSubset/QGeoCoordinate>
	«ENDIF»
	«IF dto.existsGeoAddress»
	// #include <QtLocationSubset/QGeoAddress>
	«ENDIF»
	
	«FOR en : dto.allFeatures.filter[isEnum]»
	#include "«en.toTypeName».hpp"
	«ENDFOR»
	
	«FOR reference : dto.allFeatures.filter[isTypeOfDataObject]»
	«IF !reference.isContained»
		«IF reference.toTypeName != dto.toName»
			«IF isReferencing(reference, dto)»
			// forward declaration (target references to this)
			class «reference.toTypeName.toFirstUpper»;
			«ELSE»
			«IF reference.toTypeName == "GeoCoordinate" || reference.toTypeName == "GeoAddress"»
			#include "../«reference.toTypeName».hpp"
			«ELSE»
			#include "«reference.toTypeName».hpp"
			«ENDIF»
			«ENDIF»
		«ENDIF»
	«ELSE»
	// forward declaration to avoid circular dependencies
	class «reference.toTypeName.toFirstUpper»;
	«ENDIF»
	«ENDFOR»
	
	«IF dto.existsGeo»
	// using namespace QtMobilitySubset;	
	«ENDIF»

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
		«IF isHierarchy(dto, feature)»
		«IF dto.hasTargetOSPropertyName»
		// QQmlListProperty to get easy access to hierarchy of «feature.toName» property from QML
		// Must always be initialized from DataManager before
		// DataManager::init«feature.toName.toFirstUpper»HierarchyList
		Q_PROPERTY(QQmlListProperty<«dto.name»> «feature.toName»PropertyList READ «feature.toName»PropertyList NOTIFY «feature.toName»PropertyListChanged)
		«ELSE»
		// QDeclarativeListProperty to get easy access to hierarchy of «feature.toName» property from QML
		// Must always be initialized from DataManager before
		// DataManager::init«feature.toName.toFirstUpper»HierarchyList
		Q_PROPERTY(QDeclarativeListProperty<«dto.name»> «feature.toName»PropertyList READ «feature.toName»PropertyList CONSTANT)
		«ENDIF»
		«ENDIF»
		«ELSEIF feature.isEnum»
		// int ENUM «feature.toTypeName»
		Q_PROPERTY(int «feature.toName» READ «feature.toName» WRITE set«feature.toName.toFirstUpper» NOTIFY «feature.toName»Changed FINAL)
		«ELSE»
		Q_PROPERTY(«feature.toTypeOrQObject» «feature.toName» READ «feature.toName» WRITE set«feature.toName.toFirstUpper» NOTIFY «feature.toName»Changed FINAL)
		«ENDIF»
		«ENDFOR»

		«FOR feature : dto.allFeatures.filter[isToMany && !isArrayList]»
		«IF dto.hasTargetOSPropertyName»
		// QQmlListProperty to get easy access from QML
		Q_PROPERTY(QQmlListProperty<«feature.toTypeName»> «feature.toName»PropertyList READ «feature.toName»PropertyList NOTIFY «feature.toName»PropertyListChanged)
		«ELSE»
		// QDeclarativeListProperty to get easy access from QML
		Q_PROPERTY(QDeclarativeListProperty<«feature.toTypeName»> «feature.toName»PropertyList READ «feature.toName»PropertyList NOTIFY «feature.toName»PropertyListChanged)
		«ENDIF»
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

		«IF dto.existsLazy || dto.existsLazyArray»
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
		«IF isHierarchy(dto, feature)»
		// access to the hierarchy - must be initialized from DataManager before
		// DataManager::init«feature.toName.toFirstUpper»HierarchyList
		void init«feature.toName.toFirstUpper»PropertyList(QList<«dto.name»*> «feature.toName»PropertyList);
		// if list should be cleared from C++ - from QML use QDeclarativeList function
		void clear«feature.toName.toFirstUpper»PropertyList();
		«IF dto.hasTargetOSPropertyName»
		QQmlListProperty<«dto.name»> «feature.toName»PropertyList();
		«ELSE»
		QDeclarativeListProperty<«dto.name»> «feature.toName»PropertyList();
		«ENDIF»
		«ENDIF»
		
		Q_INVOKABLE
		void resolve«feature.toName.toFirstUpper»AsDataObject(«feature.toTypeName.toFirstUpper»* «feature.toTypeName.toFirstLower»);
		
		Q_INVOKABLE
		void remove«feature.toName.toFirstUpper»();
		
		Q_INVOKABLE
		bool has«feature.toName.toFirstUpper»();
		
		Q_INVOKABLE
		bool is«feature.toName.toFirstUpper»ResolvedAsDataObject();
		
		Q_INVOKABLE
		void mark«feature.toName.toFirstUpper»AsInvalid();
		
		«ELSEIF feature.isEnum»
		int «feature.toName»() const;
		void set«feature.toName.toFirstUpper»(int «feature.toName»);
		void set«feature.toName.toFirstUpper»(QString «feature.toName»);
		«ELSE»
		«feature.toTypeOrQObject» «feature.toName»() const;
		«IF feature.isTypeOfDates»

		Q_INVOKABLE
		bool has«feature.toName.toFirstUpper»();
		«IF feature.toTypeName == "QTime"»

		Q_INVOKABLE
		void set«feature.toName.toFirstUpper»FromPickerValue(QString «feature.toName.toFirstLower»Value);
		«ENDIF»
		«ENDIF»
		«IF feature.isTypeOfDataObject && feature.isContained»
		// no SETTER «feature.toName»() is only convenience method to get the parent
		«ELSE»
		void set«feature.toName.toFirstUpper»(«feature.toTypeOrQObject» «feature.toName»);
		«IF feature.isTypeOfDataObject»
		«IF !(feature.toTypeName == "GeoAddress") && !(feature.toTypeName == "GeoCoordinate")»
		Q_INVOKABLE
		«feature.toTypeName»* create«feature.toName.toFirstUpper»();

		Q_INVOKABLE
		void undoCreate«feature.toName.toFirstUpper»(«feature.toTypeName»* «feature.toTypeName.toFirstLower»);
		
		«ENDIF»
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
		QVariantList «feature.toName»AsForeignQVariantList();

		«IF !feature.isTypeRootDataObject»
		Q_INVOKABLE
		«feature.toTypeName»* createElementOf«feature.toName.toFirstUpper»();

		Q_INVOKABLE
		void undoCreateElementOf«feature.toName.toFirstUpper»(«feature.toTypeName»* «feature.toTypeName.toFirstLower»);
		«ENDIF»
		
		Q_INVOKABLE
		void addTo«feature.toName.toFirstUpper»(«feature.toTypeName»* «feature.toTypeName.toFirstLower»);
		
		Q_INVOKABLE
		bool removeFrom«feature.toName.toFirstUpper»(«feature.toTypeName»* «feature.toTypeName.toFirstLower»);

		Q_INVOKABLE
		void clear«feature.toName.toFirstUpper»();

		«IF feature.isLazyArray»
		// lazy Array of independent Data Objects: only keys are persisted
		Q_INVOKABLE
		bool are«feature.toName.toFirstUpper»KeysResolved();

		Q_INVOKABLE
		QStringList «feature.toName.toFirstLower»Keys();

		Q_INVOKABLE
		void resolve«feature.toName.toFirstUpper»Keys(QList<«feature.toTypeName»*> «feature.toName.toFirstLower»);
		«ENDIF»
		«IF !feature.isTypeRootDataObject»
		Q_INVOKABLE
		void addTo«feature.toName.toFirstUpper»FromMap(const QVariantMap& «feature.toTypeName.toFirstLower»Map);
		«ENDIF»
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
		«IF dto.hasTargetOSPropertyName»
		QQmlListProperty<«feature.toTypeName»> «feature.toName»PropertyList();
		«ELSE»
		QDeclarativeListProperty<«feature.toTypeName»> «feature.toName»PropertyList();
		«ENDIF»
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
		«IF dto.hasSqlCachePropertyName»
		// SQL
		static const QString createTableCommand();
		static const QString createParameterizedInsertNameBinding();
		static const QString createParameterizedInsertPosBinding();
		void toSqlCache(«FOR feature : dto.features SEPARATOR", "»QVariantList& «feature.toName»List«ENDFOR»);
		void fillFromSqlQuery(const QSqlQuery& sqlQuery);
		static void fillSqlQueryPos(const QSqlRecord& record);
		«IF dto.is2PhaseInit»
		static bool isPreloaded(const QSqlQuery& sqlQuery, const QVariantMap& preloadMap);
		«ENDIF»
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
		void «feature.toName»PropertyListChanged();
		
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
		bool m«feature.toName.toFirstUpper»Invalid;
		«feature.toTypeOrQObject» m«feature.toName.toFirstUpper»AsDataObject;
		«IF isHierarchy(dto, feature)»
		// hierarchy of «dto.toName»*
		bool mIs«feature.toName.toFirstUpper»AsPropertyListInitialized;
		QList<«dto.toName»*> m«feature.toName.toFirstUpper»AsPropertyList;
		«IF dto.hasTargetOSPropertyName»
		// implementation for QQmlListProperty to use
		// QML functions for hierarchy of «dto.toName»*
		static void appendTo«feature.toName.toFirstUpper»Property(QQmlListProperty<«dto.toName»> *«feature.toName»List,
			«dto.toName»* «dto.toName.toFirstLower»);
		static int «feature.toName»PropertyCount(QQmlListProperty<«dto.toName»> *«feature.toName»List);
		static «dto.toName»* at«feature.toName.toFirstUpper»Property(QQmlListProperty<«dto.toName»> *«feature.toName»List,
		int pos);
		static void clear«feature.toName.toFirstUpper»Property(QQmlListProperty<«dto.toName»> *«feature.toName»List);
		«ELSE»
		// implementation for QDeclarativeListProperty to use
		// QML functions for hierarchy of «dto.toName»*
		static void appendTo«feature.toName.toFirstUpper»Property(QDeclarativeListProperty<«dto.toName»> *«feature.toName»List,
			«dto.toName»* «dto.toName.toFirstLower»);
		static int «feature.toName»PropertyCount(QDeclarativeListProperty<«dto.toName»> *«feature.toName»List);
		static «dto.toName»* at«feature.toName.toFirstUpper»Property(QDeclarativeListProperty<«dto.toName»> *«feature.toName»List,
		int pos);
		static void clear«feature.toName.toFirstUpper»Property(QDeclarativeListProperty<«dto.toName»> *«feature.toName»List);
		«ENDIF»
		
		«ENDIF»
		«ELSEIF feature.isEnum»
		int m«feature.toName.toFirstUpper»;
		int «feature.toName.toFirstLower»StringToInt(QString «feature.toName.toFirstLower»);
		«ELSE»
		«feature.toTypeOrQObject» m«feature.toName.toFirstUpper»;
		«ENDIF»
		«ENDFOR»
		«FOR feature : dto.allFeatures.filter[isToMany]»
		«IF feature.isLazyArray»
		// lazy Array of independent Data Objects: only keys are persisted
		QStringList m«feature.toName.toFirstUpper»Keys;
		bool m«feature.toName.toFirstUpper»KeysResolved;
		«ENDIF»
		«IF !(feature.isArrayList)»
		«IF dto.hasTargetOSPropertyName»
		QList<«feature.toTypeName»*> m«feature.toName.toFirstUpper»;
		// implementation for QQmlListProperty to use
		// QML functions for List of «feature.toTypeName»*
		static void appendTo«feature.toName.toFirstUpper»Property(QQmlListProperty<«feature.toTypeName»> *«feature.toName»List,
			«feature.toTypeName»* «feature.toTypeName.toFirstLower»);
		static int «feature.toName»PropertyCount(QQmlListProperty<«feature.toTypeName»> *«feature.toName»List);
		static «feature.toTypeName»* at«feature.toName.toFirstUpper»Property(QQmlListProperty<«feature.toTypeName»> *«feature.toName»List, int pos);
		static void clear«feature.toName.toFirstUpper»Property(QQmlListProperty<«feature.toTypeName»> *«feature.toName»List);
		«ELSE»
		QList<«feature.toTypeName»*> m«feature.toName.toFirstUpper»;
		// implementation for QDeclarativeListProperty to use
		// QML functions for List of «feature.toTypeName»*
		static void appendTo«feature.toName.toFirstUpper»Property(QDeclarativeListProperty<«feature.toTypeName»> *«feature.toName»List,
			«feature.toTypeName»* «feature.toTypeName.toFirstLower»);
		static int «feature.toName»PropertyCount(QDeclarativeListProperty<«feature.toTypeName»> *«feature.toName»List);
		static «feature.toTypeName»* at«feature.toName.toFirstUpper»Property(QDeclarativeListProperty<«feature.toTypeName»> *«feature.toName»List, int pos);
		static void clear«feature.toName.toFirstUpper»Property(QDeclarativeListProperty<«feature.toTypeName»> *«feature.toName»List);
		«ENDIF»
		
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
