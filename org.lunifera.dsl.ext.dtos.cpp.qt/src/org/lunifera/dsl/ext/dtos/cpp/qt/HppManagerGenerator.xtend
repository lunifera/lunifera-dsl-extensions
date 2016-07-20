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

import com.google.inject.Inject
import org.lunifera.dsl.semantic.common.types.LTypedPackage
import org.lunifera.dsl.semantic.dto.LDto

class HppManagerGenerator {

	@Inject extension CppExtensions
	@Inject extension ManagerExtensions

	def String toFileName(LTypedPackage pkg) {
		"DataManager.hpp"
	}

	def CharSequence toContent(LTypedPackage pkg) '''
#ifndef DATAMANAGER_HPP_
#define DATAMANAGER_HPP_

#include <qobject.h>
«IF pkg.hasTargetOS»
#include <QQmlListProperty>
«ELSE»
#include <QtDeclarative>
«ENDIF»
#include <QStringList>
«IF pkg.hasSqlCache»
#include <QtSql/QtSql>
	«IF pkg.has2PhaseInit»
#include <QtSql/QSqlQuery>
#include <QTimer>
	«ENDIF»
«ENDIF»

«FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
#include "«dto.toName».hpp"
«ENDFOR»
«IF pkg.hasGeoCoordinate»
#include "../GeoCoordinate.hpp"
«ENDIF»
«IF pkg.hasGeoAddress»
#include  "../GeoAddress.hpp"
«ENDIF»

class DataManager: public QObject
{
Q_OBJECT

«IF pkg.hasTargetOS»
// QQmlListProperty to get easy access from QML
«ELSE»
// QDeclarativeListProperty to get easy access from QML
«ENDIF»
«FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
«IF dto.isRootDataObject»
«IF pkg.hasTargetOS»
«IF dto.name != "SettingsData"»
Q_PROPERTY(QQmlListProperty<«dto.toName»> «dto.toName.toFirstLower»PropertyList READ «dto.toName.toFirstLower»PropertyList NOTIFY «dto.toName.toFirstLower»PropertyListChanged)
«ENDIF»
«ELSE»
Q_PROPERTY(QDeclarativeListProperty<«dto.toName»> «dto.toName.toFirstLower»PropertyList READ «dto.toName.toFirstLower»PropertyList NOTIFY «dto.toName.toFirstLower»PropertyListChanged)
«ENDIF»
«ENDIF»
«ENDFOR»

public:
    DataManager(QObject *parent = 0);
	«FOR dto : pkg.types.filter[it instanceof LDto && it.name == "SettingsData"].map[it as LDto]»
		«IF dto.hasFriendsClassPropertyName»
		
		«FOR friend : dto.friendsClassValue.split(",")»
		friend class «friend»;
		«ENDFOR»
		
		«ENDIF»
	«ENDFOR»
    virtual ~DataManager();
    Q_INVOKABLE
    void init();
	«IF pkg.has2PhaseInit && pkg.hasSqlCache»
	Q_INVOKABLE
	void init2();
	«ENDIF»
	«IF pkg.hasTargetOS»
	bool checkDirs();
	«ENDIF»

	«FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
	«IF dto.isRootDataObject»
	«IF !pkg.hasTargetOS»
	«IF dto.isTree»
	Q_INVOKABLE
	void fill«dto.toName»TreeDataModel(QString objectName);

	Q_INVOKABLE
	void fill«dto.toName»FlatDataModel(QString objectName);
	«ELSE»
	Q_INVOKABLE
	void fill«dto.toName»DataModel(QString objectName);
	«ENDIF»

	Q_INVOKABLE
	void replaceItemIn«dto.toName»DataModel(QString objectName, «dto.toName»* listItem);

	Q_INVOKABLE
	void removeItemFrom«dto.toName»DataModel(QString objectName, «dto.toName»* listItem);

	Q_INVOKABLE
	void insertItemInto«dto.toName»DataModel(QString objectName, «dto.toName»* listItem);
	«FOR feature : dto.allFeatures.filter[hasIndex]»

	Q_INVOKABLE
	«IF feature.isLazy»
	void fill«dto.toName»DataModelBy«feature.toName.toFirstUpper»(QString objectName, const «feature.referenceDomainKeyType»& «feature.toName»);
	«ELSE»
	void fill«dto.toName»DataModelBy«feature.toName.toFirstUpper»(QString objectName, const «feature.toTypeName»& «feature.toName»);
	«ENDIF»
	«ENDFOR»
	«ENDIF»
	
	«IF dto.existsLazy»
	«FOR feature : dto.allFeatures.filter[isLazy]»
	«IF isHierarchy(dto, feature)»

	// isHierarchy of: «dto.toName» FEATURE: «feature.toName»
	Q_INVOKABLE
	void init«feature.toName.toFirstUpper»HierarchyList(«dto.toName»* «feature.toName»«dto.toName»);
	«ENDIF»
	«ENDFOR»

	Q_INVOKABLE
	void resolve«dto.toName»References(«dto.toName»* «dto.toName.toFirstLower»);

	Q_INVOKABLE
	void resolveReferencesForAll«dto.toName»();
	«ENDIF»
	
	«IF !pkg.hasTargetOS || dto.name != "SettingsData"»
	Q_INVOKABLE
	QList<«dto.toName»*> listOf«dto.toName»ForKeys(QStringList keyList);

	Q_INVOKABLE
	QVariantList «dto.toName.toFirstLower»AsQVariantList();

	Q_INVOKABLE
	QList<QObject*> all«dto.toName»();

	«IF !pkg.hasTargetOS || dto.name != "SettingsData"»
	Q_INVOKABLE
	void delete«dto.toName»();
	«ENDIF»

	// access from QML to list of all «dto.toName»
	«IF pkg.hasTargetOS»
	QQmlListProperty<«dto.toName»> «dto.toName.toFirstLower»PropertyList();
	«ELSE»
	QDeclarativeListProperty<«dto.toName»> «dto.toName.toFirstLower»PropertyList();
	«ENDIF»
	«ENDIF»

	Q_INVOKABLE
	«dto.toName»* create«dto.toName»();

	Q_INVOKABLE
	void undoCreate«dto.toName»(«dto.toName»* «dto.toName.toFirstLower»);

	«IF !pkg.hasTargetOS || dto.name != "SettingsData"»
	Q_INVOKABLE
	void insert«dto.toName»(«dto.toName»* «dto.toName.toFirstLower»);

	Q_INVOKABLE
	void insert«dto.toName»FromMap(const QVariantMap& «dto.toName.toFirstLower»Map, const bool& useForeignProperties);
	«ENDIF»

	«IF !pkg.hasTargetOS || dto.name != "SettingsData"»
	Q_INVOKABLE
	bool delete«dto.toName»(«dto.toName»* «dto.toName.toFirstLower»);
	«ENDIF»	
	
	«IF dto.hasUuid»

	Q_INVOKABLE
	bool delete«dto.toName»ByUuid(const QString& uuid);

	Q_INVOKABLE
	«dto.toName»* find«dto.toName»ByUuid(const QString& uuid);
	«ENDIF»
	«IF dto.hasDomainKey && dto.domainKey != "uuid"»

	«IF !pkg.hasTargetOS || dto.name != "SettingsData"»
	Q_INVOKABLE
	bool delete«dto.toName»By«dto.domainKey.toFirstUpper»(const «dto.domainKeyType»& «dto.domainKey»);

	Q_INVOKABLE
    «dto.toName»* find«dto.toName»By«dto.domainKey.toFirstUpper»(const «dto.domainKeyType»& «dto.domainKey»);
	«ENDIF»

    «ENDIF»
	«ENDIF»
	«ENDFOR»

	«IF pkg.hasSqlCache»
	Q_INVOKABLE
	void setChunkSize(const int& newChunkSize);
		«IF pkg.has2PhaseInit»
		Q_INVOKABLE
		bool is2PhaseInitDone();
		«ENDIF»
    «ENDIF»

    «FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
    	«IF dto.isRootDataObject»
    	«IF !pkg.hasTargetOS || dto.name != "SettingsData"»
    	void init«dto.toName»FromCache();
    	«IF dto.hasSqlCachePropertyName»
    		«IF dto.is2PhaseInit»
    		void init«dto.toName»FromSqlCache1();
    		«ELSE»
    		void init«dto.toName»FromSqlCache();
    		«ENDIF»
    	«ENDIF»
    	«ENDIF»
		«ENDIF»
	«ENDFOR»
	«IF pkg.hasTargetOS»
	Q_INVOKABLE
	SettingsData* settingsData();
	
	void finish();
	«ENDIF»

Q_SIGNALS:

	void shuttingDown();
	«FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
	«IF dto.isRootDataObject»
	«IF !pkg.hasTargetOS || dto.name != "SettingsData"»
	void addedToAll«dto.toName»(«dto.toName»* «dto.toName.toFirstLower»);
	«IF dto.hasUuid»
	void deletedFromAll«dto.toName»ByUuid(QString uuid);
	«ENDIF»
	«IF dto.hasDomainKey && dto.domainKey != "uuid"»
	void deletedFromAll«dto.toName»By«dto.domainKey.toFirstUpper»(«dto.domainKeyType» «dto.domainKey»);
	«ENDIF»
	void deletedFromAll«dto.toName»(«dto.toName»* «dto.toName.toFirstLower»);
	void «dto.toName.toFirstLower»PropertyListChanged();
	«ENDIF»
	«ENDIF»
    «ENDFOR»
    «IF pkg.hasSqlCache && pkg.has2PhaseInit»
    void finished2PhaseInit();
    «ENDIF»

«IF !pkg.hasTargetOS»    
public slots:
    void onManualExit();
«ENDIF»

«IF pkg.hasSqlCache && pkg.has2PhaseInit»
private Q_SLOTS:
	void onPhase2TimerTimeout();

«ENDIF»
private:
	«IF pkg.hasTargetOS»
	QString mDataRoot;
	QString mDataPath;
	QString mSettingsPath;
	QString mDataAssetsPath;
	QString dataAssetsPath(const QString& fileName);
	QString dataPath(const QString& fileName);

	SettingsData* mSettingsData;
	void readSettings();
	void saveSettings();
	bool mCompactJson;
	«ENDIF»
	// DataObject stored in List of QObject*
	// GroupDataModel only supports QObject*
    «FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
    	«IF dto.isRootDataObject»
    	«IF !pkg.hasTargetOS || dto.name != "SettingsData"»
    	QList<QObject*> mAll«dto.toName»;
    	«ENDIF»
    	«IF dto.is2PhaseInit»
    	// collects data for priority loading
    	QVariantMap m«dto.toName»2PhaseInit;
    	bool m«dto.toName»Init2Done;
    	«ENDIF»
    	«IF pkg.hasTargetOS»
    	«IF dto.name != "SettingsData"»
    	// implementation for QQmlListProperty to use
    	// QML functions for List of All «dto.toName»*
    	static void appendTo«dto.toName»Property(
    		QQmlListProperty<«dto.toName»> *«dto.toName.toFirstLower»List,
    		«dto.toName»* «dto.toName.toFirstLower»);
    	static int «dto.toName.toFirstLower»PropertyCount(
    		QQmlListProperty<«dto.toName»> *«dto.toName.toFirstLower»List);
    	static «dto.toName»* at«dto.toName»Property(
    		QQmlListProperty<«dto.toName»> *«dto.toName.toFirstLower»List, int pos);
    	static void clear«dto.toName»Property(
    		QQmlListProperty<«dto.toName»> *«dto.toName.toFirstLower»List);
    	«ENDIF»
    	«ELSE»
    	// implementation for QDeclarativeListProperty to use
    	// QML functions for List of All «dto.toName»*
    	static void appendTo«dto.toName»Property(
    		QDeclarativeListProperty<«dto.toName»> *«dto.toName.toFirstLower»List,
    		«dto.toName»* «dto.toName.toFirstLower»);
    	static int «dto.toName.toFirstLower»PropertyCount(
    		QDeclarativeListProperty<«dto.toName»> *«dto.toName.toFirstLower»List);
    	static «dto.toName»* at«dto.toName»Property(
    		QDeclarativeListProperty<«dto.toName»> *«dto.toName.toFirstLower»List, int pos);
    	static void clear«dto.toName»Property(
    		QDeclarativeListProperty<«dto.toName»> *«dto.toName.toFirstLower»List);
    	«ENDIF»
    		
		«ENDIF»
    	«IF dto.isTree»
    	QList<QObject*> mAll«dto.toName»Flat;
		«ENDIF»
	«ENDFOR»

    «FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
    	«IF dto.isRootDataObject»
    	«IF !pkg.hasTargetOS || dto.name != "SettingsData"»
    	void save«dto.toName»ToCache();
    		«IF dto.hasSqlCachePropertyName»
    		void save«dto.toName»ToSqlCache();
    			«IF dto.is2PhaseInit»
    			void init«dto.toName»FromSqlCache2();
    			void process«dto.toName»Query2();
    			«ENDIF»
			«ENDIF»
			«ENDIF»
		«ENDIF»
	«ENDFOR»

«IF pkg.hasSqlCache»
// S Q L
	QSqlDatabase mDatabase;
    bool mDatabaseAvailable;
    bool initDatabase();
    void bulkImport(const bool& tuneJournalAndSync);
    int mChunkSize;
    «IF pkg.has2PhaseInit»
    bool m2PhaseInitDone;
    QSqlQuery mPhase2Query;
    QTimer *mPhase2Timer;
	«ENDIF»
«ENDIF»

	QVariantList readFromCache(const QString& fileName);
	void writeToCache(const QString& fileName, QVariantList& data);
	«IF !pkg.hasTargetOS»
	void finish();
	«ENDIF»
};

#endif /* DATAMANAGER_HPP_ */
		'''
}
