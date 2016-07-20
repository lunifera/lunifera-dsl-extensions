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
import org.lunifera.dsl.semantic.common.types.LTypedPackage
import org.lunifera.dsl.semantic.common.types.LEnum

class CppManagerGenerator {

	@Inject extension CppExtensions
	@Inject extension ManagerExtensions

	def String toFileName(LTypedPackage pkg) {
		"DataManager.cpp"
	}

	def CharSequence toContent(LTypedPackage pkg) '''
#include <QObject>

#include "DataManager.hpp"

«IF pkg.hasTargetOS»
#include <QtQml>
#include <QJsonObject>
#include <QFile>
«ELSE»
#include <bb/cascades/Application>
#include <bb/cascades/AbstractPane>
#include <bb/data/JsonDataAccess>
#include <bb/cascades/GroupDataModel>
«ENDIF»

«IF pkg.hasSqlCache»
	«IF !pkg.has2PhaseInit»
#include <QtSql/QSqlQuery>
	«ENDIF»
#include <QtSql/QSqlRecord>

static QString dbName = "sqlcache.db";
«ENDIF»

static const QString PRODUCTION_ENVIRONMENT = "prod/";
static const QString TEST_ENVIRONMENT = "test/";
static bool isProductionEnvironment = true;

«IF pkg.hasTargetOS»
«ELSE»
static QString dataAssetsPath(const QString& fileName)
{
    return QDir::currentPath() + "/app/native/assets/datamodel/" + (isProductionEnvironment?PRODUCTION_ENVIRONMENT:TEST_ENVIRONMENT) + fileName;
}
static QString dataPath(const QString& fileName)
{
    return QDir::currentPath() + "/data/" + (isProductionEnvironment?PRODUCTION_ENVIRONMENT:TEST_ENVIRONMENT) + fileName;
}
static QString settingsAssetsPath(const QString& fileName)
{
    return QDir::currentPath() + "/app/native/assets/datamodel/" + fileName;
}
static QString settingsPath(const QString& fileName)
{
    return QDir::currentPath() + "/data/" + fileName;
}
«ENDIF»

    «FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
    	«IF dto.isTree»
// cache«dto.toName» is tree of  «dto.toName»
// there's also a flat list (in memory only) useful for easy filtering
		«ENDIF»
    	«IF dto.isRootDataObject»
static const QString cache«dto.toName» = "cache«dto.toName».json";
		«ENDIF»
	«ENDFOR»

«IF pkg.hasTargetOS»
«ELSE»
using namespace bb::cascades;
using namespace bb::data;
«ENDIF»

DataManager::DataManager(QObject *parent) :
        QObject(parent)
{
    // ApplicationUI is parent of DataManager
    // DataManager is parent of all root DataObjects
    // ROOT DataObjects are parent of contained DataObjects
    // ROOT:
    «FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
    	«IF dto.isRootDataObject»
    	// «dto.toName»
		«ENDIF»
	«ENDFOR»

    // register all DataObjects to get access to properties from QML:
	«FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
		qmlRegisterType<«dto.toName»>("org.ekkescorner.data", 1, 0, "«dto.toName»");
	«ENDFOR»
	«IF pkg.hasGeo»
	// QGeo... classes wrapped as QObject* to be able to access via Q_PROPERTY
	«IF pkg.hasGeoCoordinate»
		qmlRegisterType<GeoCoordinate>("org.ekkescorner.data", 1, 0, "GeoCoordinate");
	«ENDIF»
	«IF pkg.hasGeoAddress»
		qmlRegisterType<GeoAddress>("org.ekkescorner.data", 1, 0, "GeoAddress");
	«ENDIF»
	«ENDIF»
	// register all ENUMs to get access from QML
	«FOR en : pkg.types.filter[it instanceof LEnum].map[it as LEnum]»
		qmlRegisterType<«en.toName»>("org.ekkescorner.enums", 1, 0, "«en.toName»");
	«ENDFOR»
	// useful Types for all APPs dealing with data
	// QTimer
	qmlRegisterType<QTimer>("org.ekkescorner.common", 1, 0, "QTimer");

	// no auto exit: we must persist the cache before
    bb::Application::instance()->setAutoExit(false);
    bool res = QObject::connect(bb::Application::instance(), SIGNAL(manualExit()), this, SLOT(onManualExit()));
    Q_ASSERT(res);

	«IF pkg.hasSqlCache && pkg.has2PhaseInit»
	m2PhaseInitDone = false;
    «FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
    	«IF dto.isRootDataObject && dto.is2PhaseInit»
	m«dto.toName»Init2Done = false;
		«ENDIF»
	«ENDFOR»
	// we cannot deal with QThread or QtConcurrent because we create QObject*
	// and must set DataManager as parent what's not possible from another Thread
	mPhase2Timer = new QTimer(this);
	// setting interval to zero: QTimer gets timeout if nothing in event queue
	mPhase2Timer->setInterval(0);
	res = QObject::connect(mPhase2Timer, SIGNAL(timeout()), this, SLOT(onPhase2TimerTimeout()));
	Q_ASSERT(res);
	«ENDIF»

    Q_UNUSED(res);
}

/*
 * loads all data from cache.
 * called from main.qml with delay using QTimer
 * Data with 2PhaseInit Caching Policy will only
 * load priority records needed to resolve from others
 */
void DataManager::init()
{
    // check directories
    QDir dir;
    bool exists;
    exists = dir.exists(QDir::currentPath() + "/data/"+PRODUCTION_ENVIRONMENT);
    if (!exists) {
        dir.mkpath(QDir::currentPath() + "/data/"+PRODUCTION_ENVIRONMENT);
    }
    exists = dir.exists(QDir::currentPath() + "/data/"+TEST_ENVIRONMENT);
        if (!exists) {
        dir.mkpath(QDir::currentPath() + "/data/"+TEST_ENVIRONMENT);
    }
    // get all from cache
	«IF pkg.hasSqlCache»
	// SQL init the sqlite database
	mDatabaseAvailable = initDatabase();
	qDebug() << "SQLite created or opened ? " << mDatabaseAvailable;
	«ENDIF»

    «FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
    	«IF dto.isRootDataObject»
    		«IF dto.hasSqlCachePropertyName»
    		«IF !dto.is2PhaseInit»
    		init«dto.toName»FromSqlCache();
    		«ENDIF»
    		«ELSE»
    		init«dto.toName»FromCache();
    		«ENDIF»
    		«IF dto.toName.equals("SettingsData")»
    		SettingsData* theSettings;
    		theSettings = (SettingsData*) mAllSettingsData.first();
    		isProductionEnvironment = theSettings->isProductionEnvironment();
			«ENDIF»
		«ENDIF»
	«ENDFOR»
	«IF pkg.has2PhaseInit && pkg.hasSqlCache»
	// first step: load priority rows from SQLite
    	«FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
    		«IF dto.isRootDataObject && dto.is2PhaseInit»
    		init«dto.toName»FromSqlCache1();
			«ENDIF»
		«ENDFOR»
	«ENDIF»
}

«IF pkg.has2PhaseInit && pkg.hasSqlCache»
/*
 * STEP 2 of 2PaseInit Caching Policy
 * some priority records already loaded from SQLite
 * 
 * now loads all remaining data from cache.
 * called from main.qml with delay using QTimer
 */
void DataManager::init2()
{
    «FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
    	«IF dto.isRootDataObject && dto.is2PhaseInit»
    		if (!m«dto.toName»Init2Done) {
    			init«dto.toName»FromSqlCache2();
    			return;
    		}
		«ENDIF»
	«ENDFOR»
	m2PhaseInitDone = true;
	emit finished2PhaseInit();
}
bool DataManager::is2PhaseInitDone()
{
    return m2PhaseInitDone;
}
// SLOT
void DataManager::onPhase2TimerTimeout()
{
    «FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
    	«IF dto.isRootDataObject && dto.is2PhaseInit»
    		if (!m«dto.toName»Init2Done) {
    			process«dto.toName»Query2();
    			return;
    		}
		«ENDIF»
	«ENDFOR»
    qWarning() << "nothing more to process from onPhase2TimerTimeout";
    mPhase2Timer->stop();
}
«ENDIF»

«IF pkg.hasSqlCache»
//  S Q L
/**
 * OPENs the DATABASE FILE
 *
 * Also initialized the DATABASE CONNECTION (SqlDataAccess),
 * we're reusing for all SQL commands on this database
 */
bool DataManager::initDatabase()
{
    mChunkSize = 10000;
    QString pathname;
    pathname = dataPath(dbName);
    QFile dataFile(pathname);
    if (!dataFile.exists()) {
        // of there's a default at assets we copy it - otherwise a new db will be created later
        QFile assetDataFile(dataAssetsPath(dbName));
        if (assetDataFile.exists()) {
            // copy file from assets to data
            bool copyOk = assetDataFile.copy(pathname);
            if (!copyOk) {
                qDebug() << "cannot copy dataAssetsPath(fileName) to dataPath(fileName)";
            }
        }
    }
    //
    mDatabase = QSqlDatabase::addDatabase("QSQLITE");
    mDatabase.setDatabaseName(dataPath(dbName));
    if (mDatabase.open() == false) {
        const QSqlError error = mDatabase.lastError();
        // you should notify the user !
        qWarning() << "Cannot open " << dbName << ":" << error.text();
        return false;
    }
    qDebug() << "Database opened: " << dbName;
	«IF pkg.hasSqlCache && pkg.has2PhaseInit»
	mPhase2Query = QSqlQuery (mDatabase);
	«ENDIF»
    return true;
}

void DataManager::setChunkSize(const int& newChunkSize)
{
    mChunkSize = newChunkSize;
}

/**
 * tune PRAGMA synchronous and journal_mode for better speed with bulk import
 * see https://www.sqlite.org/pragma.html
 * and http://stackoverflow.com/questions/1711631/improve-insert-per-second-performance-of-sqlite
 *
 * PRAGMA database.synchronous = 0 | OFF | 1 | NORMAL | 2 | FULL;
 * default: FULL
 *
 * PRAGMA database.journal_mode = DELETE | TRUNCATE | PERSIST | MEMORY | WAL | OFF
 * default: DELETE
 */
void DataManager::bulkImport(const bool& tuneJournalAndSync)
{
    QSqlQuery query (mDatabase);
    bool success;
    QString journalMode;
    QString syncMode;
    query.prepare("PRAGMA journal_mode");
    success = query.exec();
    if(!success) {
        qWarning() << "NO SUCCESS PRAGMA journal_mode";
        return;
    }
    success = query.first();
    if(!success) {
        qWarning() << "NO RESULT PRAGMA journal_mode";
        return;
    }
    journalMode = query.value(0).toString();
    //
    query.clear();
    query.prepare("PRAGMA synchronous");
    success = query.exec();
    if(!success) {
        qWarning() << "NO SUCCESS PRAGMA synchronous";
        return;
    }
        success = query.first();
    if(!success) {
        qWarning() << "NO RESULT PRAGMA synchronous";
        return;
    }
    switch (query.value(0).toInt()) {
        case 0:
            syncMode = "OFF";
            break;
        case 1:
            syncMode = "NORMAL";
            break;
        case 2:
            syncMode = "FULL";
            break;
        default:
            syncMode = query.value(0).toString();
            break;
    }
    qDebug() << "PRAGMA current values - " << "journal: " << journalMode << " synchronous: " << syncMode;
    //
    query.clear();
    if (tuneJournalAndSync) {
        query.prepare("PRAGMA journal_mode = MEMORY");
    } else {
        query.prepare("PRAGMA journal_mode = DELETE");
    }
    success = query.exec();
    if(!success) {
        qWarning() << "NO SUCCESS PRAGMA set journal_mode";
        return;
    }
    //
    query.prepare("PRAGMA journal_mode");
    success = query.exec();
    if(!success) {
        qWarning() << "NO SUCCESS PRAGMA journal_mode";
        return;
    }
    success = query.first();
    if(!success) {
        qWarning() << "NO RESULT PRAGMA journal_mode";
        return;
    }
    qDebug() << "PRAGMA NEW VALUE journal_mode: " << query.value(0).toString();
    //
    query.clear();
    if (tuneJournalAndSync) {
        query.prepare("PRAGMA synchronous = OFF");
    } else {
        query.prepare("PRAGMA synchronous = FULL");
    }
    success = query.exec();
    if(!success) {
        qWarning() << "NO SUCCESS PRAGMA set synchronous";
        return;
    }
    //
    query.prepare("PRAGMA synchronous");
    success = query.exec();
    if(!success) {
        qWarning() << "NO SUCCESS PRAGMA synchronous";
        return;
    }
    success = query.first();
    if(!success) {
        qWarning() << "NO RESULT PRAGMA synchronous";
        return;
    }
    switch (query.value(0).toInt()) {
        case 0:
            syncMode = "OFF";
            break;
        case 1:
            syncMode = "NORMAL";
            break;
        case 2:
            syncMode = "FULL";
            break;
        default:
            syncMode = query.value(0).toString();
            break;
    }
    qDebug() << "PRAGMA synchronous NEW VALUE: " << syncMode;
}
«ENDIF»

void DataManager::finish()
{
    «FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
    	«IF dto.isRootDataObject»
    		«IF dto.isReadOnlyCache»
    		// «dto.toName» is read-only - not saved to cache
    		«ELSE»
    		save«dto.toName»ToCache();
    		«ENDIF»
		«ENDIF»
	«ENDFOR»
}

    «FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
    	«IF dto.isRootDataObject»
/*
 * reads Maps of «dto.toName» in from JSON cache
 * creates List of «dto.toName»*  from QVariantList
 * List declared as list of QObject* - only way to use in GroupDataModel
 */
void DataManager::init«dto.toName»FromCache()
{
	qDebug() << "start init«dto.toName»FromCache";
    mAll«dto.toName».clear();
    «IF dto.isTree»
    mAll«dto.toName»Flat.clear();
    «ENDIF»
    QVariantList cacheList;
    cacheList = readFromCache(cache«dto.toName»);
    qDebug() << "read «dto.toName» from cache #" << cacheList.size();
    for (int i = 0; i < cacheList.size(); ++i) {
        QVariantMap cacheMap;
        cacheMap = cacheList.at(i).toMap();
        «dto.toName»* «dto.toName.toFirstLower» = new «dto.toName»();
        // Important: DataManager must be parent of all root DTOs
        «dto.toName.toFirstLower»->setParent(this);
        «dto.toName.toFirstLower»->fillFromCacheMap(cacheMap);
        mAll«dto.toName».append(«dto.toName.toFirstLower»);
        «IF dto.isTree»
        mAll«dto.toName»Flat.append(«dto.toName.toFirstLower»);
        mAll«dto.toName»Flat.append(«dto.toName.toFirstLower»->all«dto.toName»Children());
        «ENDIF»
        «IF pkg.has2PhaseInit»
        	«FOR feature : dto.allFeatures»
        		«IF feature.referenceIs2PhaseInit»
        		if(«dto.toName.toFirstLower»->has«feature.toName.toFirstUpper»()) {
        			m«feature.toTypeOrQObjectName»2PhaseInit.insert(«dto.toName.toFirstLower»->«feature.toName.toFirstLower»(), "");
        		}
        		«ENDIF»
        	«ENDFOR»
        «ENDIF»
    }
    «IF dto.isTree»
    qDebug() << "created Tree of «dto.toName»* #" << mAll«dto.toName».size();
    qDebug() << "created Flat list of «dto.toName»* #" << mAll«dto.toName»Flat.size();
    «ELSE»
    qDebug() << "created «dto.toName»* #" << mAll«dto.toName».size();
    «ENDIF»
}

«IF dto.hasSqlCachePropertyName»
	«IF dto.is2PhaseInit»
/*
 * queries SELECT * FROM «dto.toName» (SQLite cache)
 * for all keys collected in Map m«dto.toName»2PhaseInit while doing the init()
 * creates List of «dto.toName»*  from QSqlQuery
 */
void DataManager::init«dto.toName»FromSqlCache1()
{
	qDebug() << "start init«dto.toName» STEP ONE From S Q L Cache";
	mAll«dto.toName».clear();
    «IF dto.isTree»
    mAll«dto.toName»Flat.clear();
    «ENDIF»
	QStringList keys = m«dto.toName»2PhaseInit.keys();
	if (keys.size() == 0) {
		qDebug() << "no priority rows collected from init()";
		return;
	}
	QString sqlQuery = "SELECT * FROM «dto.toName.toFirstLower» WHERE «dto.domainKey» IN (";
    for (int i = 0; i < keys.size(); ++i) {
    	sqlQuery += "'";
    	sqlQuery += keys.at(i);
    	sqlQuery += "', ";
	}
	sqlQuery = sqlQuery.left(sqlQuery.length() - 2);
	sqlQuery += ")";
	qDebug() << sqlQuery;
    QSqlQuery query (mDatabase);
    query.setForwardOnly(true);
    query.prepare(sqlQuery);
    bool success = query.exec();
    if(!success) {
    	qDebug() << "NO SUCCESS query step ONE «dto.toName.toFirstLower»";
    	return;
    }
    QSqlRecord record = query.record();
    «dto.toName»::fillSqlQueryPos(record);
    while (query.next())
    	{
    		«dto.toName»* «dto.toName.toFirstLower» = new «dto.toName»();
    		// Important: DataManager must be parent of all root DTOs
    		«dto.toName.toFirstLower»->setParent(this);
    		«dto.toName.toFirstLower»->fillFromSqlQuery(query);
    		mAll«dto.toName».append(«dto.toName.toFirstLower»);
    		«IF dto.isTree»
    		mAll«dto.toName»Flat.append(«dto.toName.toFirstLower»);
    		mAll«dto.toName»Flat.append(«dto.toName.toFirstLower»->all«dto.toName»Children());
    		«ENDIF»
    	}
    «IF dto.isTree»
    qDebug() << "read priority rows from SQLite and created Tree of «dto.toName»* #" << mAll«dto.toName».size();
    qDebug() << "read priority rows from SQLite and created Flat list of «dto.toName»* #" << mAll«dto.toName»Flat.size();
    «ELSE»
    qDebug() << "read priority rows from SQLite and created «dto.toName»* #" << mAll«dto.toName».size();
    «ENDIF»
}
	«ENDIF»
/*
 * queries SELECT * FROM «dto.toName» (SQLite cache)
«IF dto.is2PhaseInit»
 * appends to List of «dto.toName»*  from QSqlQuery
«ELSE»
 * creates List of «dto.toName»*  from QSqlQuery
«ENDIF»
 */
void DataManager::init«dto.toName»FromSqlCache«IF dto.is2PhaseInit»2«ENDIF»()
{
	qDebug() << "start init«dto.toName» «IF dto.is2PhaseInit»step TWO «ENDIF»From S Q L Cache";
	«IF !dto.is2PhaseInit»
		mAll«dto.toName».clear();
    	«IF dto.isTree»
    	mAll«dto.toName»Flat.clear();
    	«ENDIF»
    «ELSE»
	// 2 Phase INIT: don't clear mAll«dto.toName» - we must append
	qDebug() << "already read from SQLite «dto.toName»* priority rows #" << mAll«dto.toName».size();
    «ENDIF»
    QString sqlQuery = "SELECT * FROM «dto.toName.toFirstLower»";
    «IF !dto.is2PhaseInit»
    QSqlQuery query (mDatabase);
    «ELSE»
    mPhase2Query.clear();
    «ENDIF»
    «IF dto.is2PhaseInit»mPhase2Query«ELSE»query«ENDIF».setForwardOnly(true);
    «IF dto.is2PhaseInit»mPhase2Query«ELSE»query«ENDIF».prepare(sqlQuery);
    bool success = «IF dto.is2PhaseInit»mPhase2Query«ELSE»query«ENDIF».exec();
    if(!success) {
    	qDebug() << "NO SUCCESS query «IF dto.is2PhaseInit»step TWO «ENDIF»«dto.toName.toFirstLower»";
    	«IF dto.is2PhaseInit»
    	m«dto.toName»2PhaseInit.clear();
    	m«dto.toName»Init2Done = true;
    	init2();
    	«ENDIF»
    	return;
    }
    QSqlRecord record = «IF dto.is2PhaseInit»mPhase2Query«ELSE»query«ENDIF».record();
    «dto.toName»::fillSqlQueryPos(record);
    «IF dto.is2PhaseInit»
    mPhase2Timer->start();
    «ELSE»
    while (query.next())
    	{
    		«dto.toName»* «dto.toName.toFirstLower» = new «dto.toName»();
    		// Important: DataManager must be parent of all root DTOs
    		«dto.toName.toFirstLower»->setParent(this);
    		«dto.toName.toFirstLower»->fillFromSqlQuery(query);
    		mAll«dto.toName».append(«dto.toName.toFirstLower»);
    		«IF dto.isTree»
    		mAll«dto.toName»Flat.append(«dto.toName.toFirstLower»);
    		mAll«dto.toName»Flat.append(«dto.toName.toFirstLower»->all«dto.toName»Children());
    		«ENDIF»
    	}
    «IF dto.isTree»
    qDebug() << "read from SQLite and created Tree of «dto.toName»* #" << mAll«dto.toName».size();
    qDebug() << "read from SQLite and created Flat list of «dto.toName»* #" << mAll«dto.toName»Flat.size();
    «ELSE»
    qDebug() << "read from SQLite and created «dto.toName»* #" << mAll«dto.toName».size();
    «ENDIF»
    «ENDIF»
}
	«IF dto.is2PhaseInit»
void DataManager::process«dto.toName»Query2()
{
    if (mPhase2Query.next()) {
    	if («dto.toName»::isPreloaded(mPhase2Query, m«dto.toName»2PhaseInit)) {
    		return;
    	}
    	«dto.toName»* «dto.toName.toFirstLower» = new «dto.toName»();
    	// Important: DataManager must be parent of all root DTOs
    	«dto.toName.toFirstLower»->setParent(this);
    	«dto.toName.toFirstLower»->fillFromSqlQuery(mPhase2Query);
    	mAll«dto.toName».append(«dto.toName.toFirstLower»);
    	«IF dto.isTree»
    	mAll«dto.toName»Flat.append(«dto.toName.toFirstLower»);
    	mAll«dto.toName»Flat.append(«dto.toName.toFirstLower»->all«dto.toName»Children());
    	«ENDIF»
    } else {
    	mPhase2Timer->stop();
    	«IF dto.isTree»
    	qDebug() << "read from SQLite and created Tree of «dto.toName»* #" << mAll«dto.toName».size();
    	qDebug() << "read from SQLite and created Flat list of «dto.toName»* #" << mAll«dto.toName»Flat.size();
    	«ELSE»
    	qDebug() << "read from SQLite and created «dto.toName»* #" << mAll«dto.toName».size();
    	«ENDIF»
    	m«dto.toName»2PhaseInit.clear();
    	m«dto.toName»Init2Done = true;
    	mPhase2Query.clear();
    	init2();
    }
}
	«ENDIF»
«ENDIF»

/*
 * save List of «dto.toName»* to JSON cache
 * convert list of «dto.toName»* to QVariantList
 * toCacheMap stores all properties without transient values
«IF dto.isReadOnlyCache» * «dto.toName» is read-only Cache - so it's not saved automatically at exit«ENDIF»
 */
void DataManager::save«dto.toName»ToCache()
{
    QVariantList cacheList;
    qDebug() << "now caching «dto.toName»* #" << mAll«dto.toName».size();
    for (int i = 0; i < mAll«dto.toName».size(); ++i) {
        «dto.toName»* «dto.toName.toFirstLower»;
        «dto.toName.toFirstLower» = («dto.toName»*)mAll«dto.toName».at(i);
        QVariantMap cacheMap;
        cacheMap = «dto.toName.toFirstLower»->toCacheMap();
        cacheList.append(cacheMap);
    }
    qDebug() << "«dto.toName»* converted to JSON cache #" << cacheList.size();
    writeToCache(cache«dto.toName», cacheList);
}

«IF dto.hasSqlCachePropertyName»
/*
 * save List of «dto.toName»* to SQLite cache
 * convert list of «dto.toName»* to QVariantLists for each COLUMN
 * tune PRAGMA journal_mode and synchronous for bulk import into SQLite
 * INSERT chunks of data into SQLite (default: 10k rows at once)
 * 
 * «dto.toName» is read-only Cache - so it's not saved automatically at exit
 */
void DataManager::save«dto.toName»ToSqlCache()
{
    qDebug() << "now caching «dto.toName»* #" << mAll«dto.toName».size();
    bulkImport(true);
    bool success = false;
    QSqlQuery query (mDatabase);
    query.prepare("DROP TABLE IF EXISTS «dto.toName.toFirstLower»");
    success = query.exec();
    if(!success) {
        qWarning() << "NO SUCCESS DROP «dto.toName.toFirstLower»";
        bulkImport(false);
        return;
    }
    qDebug() << "table DROPPED «dto.toName.toFirstLower»";
    // create table
    query.clear();
    query.prepare(«dto.toName»::createTableCommand());
    success = query.exec();
    if(!success) {
        qWarning() << "NO SUCCESS CREATE «dto.toName.toFirstLower»";
        bulkImport(false);
        return;
    }
    qDebug() << "table CREATED «dto.toName.toFirstLower»";

	qDebug() << "BEGIN INSERT chunks of «dto.toName.toFirstLower»";
    //
    QVariantList «FOR feature : dto.features SEPARATOR", "»«feature.toName»List«ENDFOR»;
    QString insertSQL = «dto.toName»::createParameterizedInsertPosBinding();
    int laps = mAll«dto.toName».size()/mChunkSize;
    if(mAll«dto.toName».size()%mChunkSize) {
        laps ++;
    }
    int count = 0;
    qDebug() << "chunks of " << mChunkSize << " laps: " << laps;
    int fromPos = 0;
    int toPos = mChunkSize;
    if(toPos > mAll«dto.toName».size()) {
        toPos = mAll«dto.toName».size();
    }
    while (count < laps) {
    	//
    	query.clear();
    	query.prepare("BEGIN TRANSACTION");
    	success = query.exec();
    	if(!success) {
        	qWarning() << "NO SUCCESS BEGIN TRANSACTION";
        	bulkImport(false);
        	return;
    	}
    	// do it
		«FOR feature : dto.features»
		«feature.toName»List.clear();
		«ENDFOR»
    	for (int i = fromPos; i < toPos; ++i) {
        	«dto.toName»* «dto.toName.toFirstLower»;
        	«dto.toName.toFirstLower» = («dto.toName»*)mAll«dto.toName».at(i);
        	«dto.toName.toFirstLower»->toSqlCache(«FOR feature : dto.features SEPARATOR", "»«feature.toName»List«ENDFOR»);
    	}
        //
    	query.clear();
    	query.prepare(insertSQL);
    	«FOR feature : dto.features»
    	query.addBindValue(«feature.toName»List);
    	«ENDFOR»
    	success = query.execBatch();
    	if(!success) {
        	qWarning() << "NO SUCCESS INSERT batch «dto.toName.toFirstLower»";
        	bulkImport(false);
        	return;
    	}
    	query.clear();
    	query.prepare("END TRANSACTION");
    	success = query.exec();
    	if(!success) {
        	qWarning() << "NO SUCCESS END TRANSACTION";
        	bulkImport(false);
        	return;
    	}
        //
        count ++;
        fromPos += mChunkSize;
        toPos += mChunkSize;
        if(toPos > mAll«dto.toName».size()) {
            toPos = mAll«dto.toName».size();
        }
    }
    qDebug() << "END INSERT chunks of «dto.toName.toFirstLower»";
    bulkImport(false);
}
«ENDIF»
«IF dto.existsLazy»
	«FOR feature : dto.allFeatures.filter[isLazy]»
	«IF isHierarchy(dto, feature)»
// isHierarchy of: «dto.toName» FEATURE: «feature.toName»
void DataManager::init«feature.toName.toFirstUpper»HierarchyList(«dto.toName»* «feature.toName»«dto.toName»)
{
	if (!«feature.toName»«dto.toName») {
		qDebug() << "cannot init«feature.toName.toFirstUpper»HierarchyList with «dto.toName.toFirstLower» NULL";
		return;
	}
	QVariantList keyList;
	QList<«dto.toName»*> «feature.toName»PropertyList;
	bool more = true;
    «dto.toName»* «dto.toName.toFirstLower»;
    «dto.toName.toFirstLower» = «feature.toName»«dto.toName»;
	while (more) {
		if («dto.toName.toFirstLower»->has«feature.toName.toFirstUpper»() && !«dto.toName.toFirstLower»->is«feature.toName.toFirstUpper»ResolvedAsDataObject()){
			qDebug() << "RESOLVE REFERENCES " << «dto.toName.toFirstLower»->«dto.domainKey»();
			resolve«dto.toName»References(«dto.toName.toFirstLower»);
		}
		if («dto.toName.toFirstLower»->is«feature.toName.toFirstUpper»ResolvedAsDataObject()) {
			if (keyList.contains(«dto.toName.toFirstLower»->«dto.domainKey»())) {
				// uups - avoid recursion - stop iteration
				qWarning() << "Attention: recursive hierarchy - already got " << «dto.toName.toFirstLower»->«dto.domainKey»();
				more = false;
			} else {
				keyList.append(«dto.toName.toFirstLower»->«dto.domainKey»());
            	«feature.toName»PropertyList.append(«dto.toName.toFirstLower»->«feature.toName»AsDataObject());
            	«dto.toName.toFirstLower» = «dto.toName.toFirstLower»->«feature.toName»AsDataObject();
            }
		} else {
			more = false;
		}
	}
    «dto.toName.toFirstLower» = 0;
	qDebug() << "init«feature.toName.toFirstUpper»HierarchyList DONE with #" << «feature.toName»PropertyList.size();
    «feature.toName»«dto.toName»->init«feature.toName.toFirstUpper»PropertyList(«feature.toName»PropertyList);
}

	«ENDIF»
	«ENDFOR»

void DataManager::resolve«dto.toName»References(«dto.toName»* «dto.toName.toFirstLower»)
{
	if (!«dto.toName.toFirstLower») {
        qDebug() << "cannot resolve«dto.toName»References with «dto.toName.toFirstLower» NULL";
        return;
    }
    if(«dto.toName.toFirstLower»->isAllResolved()) {
	    qDebug() << "nothing to do: all is resolved";
	    return;
	}
    «FOR feature : dto.allFeatures.filter[isLazy]»
    if («dto.toName.toFirstLower»->has«feature.toName.toFirstUpper»() && !«dto.toName.toFirstLower»->is«feature.toName.toFirstUpper»ResolvedAsDataObject()) {
    	«feature.toTypeName»* «feature.toName.toFirstLower»;
   		«feature.toName.toFirstLower» = find«feature.toTypeName»By«feature.referenceDomainKey.toFirstUpper»(«dto.toName.toFirstLower»->«feature.toName.toFirstLower»());
    	if («feature.toName.toFirstLower») {
    		«dto.toName.toFirstLower»->resolve«feature.toName.toFirstUpper»AsDataObject(«feature.toName.toFirstLower»);
    	} else {
    		qDebug() << "mark«feature.toName.toFirstUpper»AsInvalid: " << «dto.toName.toFirstLower»->«feature.toName.toFirstLower»();
    		«dto.toName.toFirstLower»->mark«feature.toName.toFirstUpper»AsInvalid();
    	}
    }
    «ENDFOR»
    «FOR feature : dto.allFeatures.filter[isLazyArray]»
    if (!«dto.toName.toFirstLower»->are«feature.toName.toFirstUpper»KeysResolved()) {
        «dto.toName.toFirstLower»->resolve«feature.toName.toFirstUpper»Keys(
                listOf«feature.toTypeName.toFirstUpper»ForKeys(«dto.toName.toFirstLower»->«feature.toName»Keys()));
    }
    «ENDFOR»
}
void DataManager::resolveReferencesForAll«dto.toName»()
{
    for (int i = 0; i < mAll«dto.toName».size(); ++i) {
        «dto.toName»* «dto.toName.toFirstLower»;
        «dto.toName.toFirstLower» = («dto.toName»*)mAll«dto.toName».at(i);
    	resolve«dto.toName»References(«dto.toName.toFirstLower»);
    }
}
«ENDIF»
/**
* converts a list of keys in to a list of DataObjects
* per ex. used to resolve lazy arrays
*/
QList<«dto.toName»*> DataManager::listOf«dto.toName»ForKeys(
        QStringList keyList)
{
    QList<«dto.toName»*> listOfData;
    keyList.removeDuplicates();
    if (keyList.isEmpty()) {
        return listOfData;
    }
    for (int i = 0; i < mAll«dto.toName».size(); ++i) {
        «dto.toName»* «dto.toName.toFirstLower»;
        «dto.toName.toFirstLower» = («dto.toName»*) mAll«dto.toName».at(i);
        «IF dto.domainKeyType == "int"»
        if (keyList.contains(QString::number(«dto.toName.toFirstLower»->«dto.domainKey»()))) {
            listOfData.append(«dto.toName.toFirstLower»);
            keyList.removeOne(QString::number(«dto.toName.toFirstLower»->«dto.domainKey»()));
            if(keyList.isEmpty()){
                break;
            }
        }
        «ELSE»
        if (keyList.contains(«dto.toName.toFirstLower»->«dto.domainKey»())) {
            listOfData.append(«dto.toName.toFirstLower»);
            keyList.removeOne(«dto.toName.toFirstLower»->«dto.domainKey»());
            if(keyList.isEmpty()){
                break;
            }
        }
        «ENDIF»
    }
    if (keyList.isEmpty()) {
        return listOfData;
    }
    qWarning() << "not all keys found for «dto.toName»: " << keyList.join(", ");
    return listOfData;
}

QVariantList DataManager::«dto.toName.toFirstLower»AsQVariantList()
{
    QVariantList «dto.toName.toFirstLower»List;
    for (int i = 0; i < mAll«dto.toName».size(); ++i) {
        «dto.toName.toFirstLower»List.append(((«dto.toName»*) (mAll«dto.toName».at(i)))->toMap());
    }
    return «dto.toName.toFirstLower»List;
}

QList<QObject*> DataManager::all«dto.toName»()
{
    return mAll«dto.toName»;
}

QDeclarativeListProperty<«dto.toName»> DataManager::«dto.toName.toFirstLower»PropertyList()
{
    return QDeclarativeListProperty<«dto.toName»>(this, 0,
            &DataManager::appendTo«dto.toName»Property, &DataManager::«dto.toName.toFirstLower»PropertyCount,
            &DataManager::at«dto.toName»Property, &DataManager::clear«dto.toName»Property);
}

// implementation for QDeclarativeListProperty to use
// QML functions for List of «dto.toName»*
void DataManager::appendTo«dto.toName»Property(
        QDeclarativeListProperty<«dto.toName»> *«dto.toName.toFirstLower»List,
        «dto.toName»* «dto.toName.toFirstLower»)
{
    DataManager *dataManagerObject = qobject_cast<DataManager *>(«dto.toName.toFirstLower»List->object);
    if (dataManagerObject) {
        «dto.toName.toFirstLower»->setParent(dataManagerObject);
        dataManagerObject->mAll«dto.toName».append(«dto.toName.toFirstLower»);
        emit dataManagerObject->addedToAll«dto.toName»(«dto.toName.toFirstLower»);
    } else {
        qWarning() << "cannot append «dto.toName»* to mAll«dto.toName» "
                << "Object is not of type DataManager*";
    }
}
int DataManager::«dto.toName.toFirstLower»PropertyCount(
        QDeclarativeListProperty<«dto.toName»> *«dto.toName.toFirstLower»List)
{
    DataManager *dataManager = qobject_cast<DataManager *>(«dto.toName.toFirstLower»List->object);
    if (dataManager) {
        return dataManager->mAll«dto.toName».size();
    } else {
        qWarning() << "cannot get size mAll«dto.toName» " << "Object is not of type DataManager*";
    }
    return 0;
}
«dto.toName»* DataManager::at«dto.toName»Property(
        QDeclarativeListProperty<«dto.toName»> *«dto.toName.toFirstLower»List, int pos)
{
    DataManager *dataManager = qobject_cast<DataManager *>(«dto.toName.toFirstLower»List->object);
    if (dataManager) {
        if (dataManager->mAll«dto.toName».size() > pos) {
            return («dto.toName»*) dataManager->mAll«dto.toName».at(pos);
        }
        qWarning() << "cannot get «dto.toName»* at pos " << pos << " size is "
                << dataManager->mAll«dto.toName».size();
    } else {
        qWarning() << "cannot get «dto.toName»* at pos " << pos
                << "Object is not of type DataManager*";
    }
    return 0;
}
void DataManager::clear«dto.toName»Property(
        QDeclarativeListProperty<«dto.toName»> *«dto.toName.toFirstLower»List)
{
    DataManager *dataManager = qobject_cast<DataManager *>(«dto.toName.toFirstLower»List->object);
    if (dataManager) {
        for (int i = 0; i < dataManager->mAll«dto.toName».size(); ++i) {
            «dto.toName»* «dto.toName.toFirstLower»;
            «dto.toName.toFirstLower» = («dto.toName»*) dataManager->mAll«dto.toName».at(i);
			«IF dto.hasUuid»
			emit dataManager->deletedFromAll«dto.toName»ByUuid(«dto.toName.toFirstLower»->uuid());
			«ELSEIF dto.hasDomainKey»
			emit dataManager->deletedFromAll«dto.toName»By«dto.domainKey.toFirstUpper»(«dto.toName.toFirstLower»->«dto.domainKey»());
			«ENDIF»
			emit dataManager->deletedFromAll«dto.toName»(«dto.toName.toFirstLower»);
            «dto.toName.toFirstLower»->deleteLater();
            «dto.toName.toFirstLower» = 0;
        }
        dataManager->mAll«dto.toName».clear();
    } else {
        qWarning() << "cannot clear mAll«dto.toName» " << "Object is not of type DataManager*";
    }
}

/**
 * deletes all «dto.toName»
 * and clears the list
 */
void DataManager::delete«dto.toName»()
{
    for (int i = 0; i < mAll«dto.toName».size(); ++i) {
        «dto.toName»* «dto.toName.toFirstLower»;
        «dto.toName.toFirstLower» = («dto.toName»*) mAll«dto.toName».at(i);
        «IF dto.hasUuid»
        emit deletedFromAll«dto.toName»ByUuid(«dto.toName.toFirstLower»->uuid());
		«ELSEIF dto.hasDomainKey»
        emit deletedFromAll«dto.toName»By«dto.domainKey.toFirstUpper»(«dto.toName.toFirstLower»->«dto.domainKey»());
		«ENDIF»
		emit deletedFromAll«dto.toName»(«dto.toName.toFirstLower»);
        «dto.toName.toFirstLower»->deleteLater();
        «dto.toName.toFirstLower» = 0;
     }
     mAll«dto.toName».clear();
}

/**
 * creates a new «dto.toName»
 * parent is DataManager
 * if data is successfully entered you must insert«dto.toName»
 * if edit was canceled you must undoCreate«dto.toName» to free up memory
 */
«dto.toName»* DataManager::create«dto.toName»()
{
    «dto.toName»* «dto.toName.toFirstLower»;
    «dto.toName.toFirstLower» = new «dto.toName»();
    «dto.toName.toFirstLower»->setParent(this);
    «dto.toName.toFirstLower»->prepareNew();
    return «dto.toName.toFirstLower»;
}

/**
 * deletes «dto.toName»
 * if create«dto.toName» was canceled from UI
 * to delete a previous successfully inserted «dto.toName»
 * use delete«dto.toName»
 */
void DataManager::undoCreate«dto.toName»(«dto.toName»* «dto.toName.toFirstLower»)
{
    if («dto.toName.toFirstLower») {
        qDebug() << "undoCreate«dto.toName» " << «dto.toName.toFirstLower»->«dto.domainKey»();
        «dto.toName.toFirstLower»->deleteLater();
        «dto.toName.toFirstLower» = 0;
    }
}

void DataManager::insert«dto.toName»(«dto.toName»* «dto.toName.toFirstLower»)
{
    // Important: DataManager must be parent of all root DTOs
    «dto.toName.toFirstLower»->setParent(this);
    mAll«dto.toName».append(«dto.toName.toFirstLower»);
    «IF dto.isTree»
    mAll«dto.toName»Flat.append(«dto.toName.toFirstLower»);
    «ENDIF»
    emit addedToAll«dto.toName»(«dto.toName.toFirstLower»);
}

void DataManager::insert«dto.toName»FromMap(const QVariantMap& «dto.toName.toFirstLower»Map,
        const bool& useForeignProperties)
{
    «dto.toName»* «dto.toName.toFirstLower» = new «dto.toName»();
    «dto.toName.toFirstLower»->setParent(this);
    if (useForeignProperties) {
        «dto.toName.toFirstLower»->fillFromForeignMap(«dto.toName.toFirstLower»Map);
    } else {
        «dto.toName.toFirstLower»->fillFromMap(«dto.toName.toFirstLower»Map);
    }
    mAll«dto.toName».append(«dto.toName.toFirstLower»);
    «IF dto.isTree»
    mAll«dto.toName»Flat.append(«dto.toName.toFirstLower»);
    «ENDIF»
    emit addedToAll«dto.toName»(«dto.toName.toFirstLower»);
}

bool DataManager::delete«dto.toName»(«dto.toName»* «dto.toName.toFirstLower»)
{
    bool ok = false;
    ok = mAll«dto.toName».removeOne(«dto.toName.toFirstLower»);
    if (!ok) {
        return ok;
    }
    «IF dto.isTree»
    mAll«dto.toName»Flat.removeOne(«dto.toName.toFirstLower»);
    «ENDIF»
    «IF dto.hasUuid»
    emit deletedFromAll«dto.toName»ByUuid(«dto.toName.toFirstLower»->uuid());
    «ELSEIF dto.hasDomainKey»
    emit deletedFromAll«dto.toName»By«dto.domainKey.toFirstUpper»(«dto.toName.toFirstLower»->«dto.domainKey»());
    «ENDIF»
    emit deletedFromAll«dto.toName»(«dto.toName.toFirstLower»);
    «dto.toName.toFirstLower»->deleteLater();
    «dto.toName.toFirstLower» = 0;
    return ok;
}

«IF dto.hasUuid»
bool DataManager::delete«dto.toName»ByUuid(const QString& uuid)
{
    if (uuid.isNull() || uuid.isEmpty()) {
        qDebug() << "cannot delete «dto.toName» from empty uuid";
        return false;
    }
    for (int i = 0; i < mAll«dto.toName».size(); ++i) {
        «dto.toName»* «dto.toName.toFirstLower»;
        «dto.toName.toFirstLower» = («dto.toName»*) mAll«dto.toName».at(i);
        if («dto.toName.toFirstLower»->uuid() == uuid) {
            mAll«dto.toName».removeAt(i);
            «IF dto.isTree»
            mAll«dto.toName»Flat.removeOne(«dto.toName.toFirstLower»);
            «ENDIF»
            emit deletedFromAll«dto.toName»ByUuid(uuid);
            emit deletedFromAll«dto.toName»(«dto.toName.toFirstLower»);
            «dto.toName.toFirstLower»->deleteLater();
            «dto.toName.toFirstLower» = 0;
            return true;
        }
    }
    return false;
}
«ENDIF»

«IF dto.hasDomainKey && dto.domainKey != "uuid"»
bool DataManager::delete«dto.toName»By«dto.domainKey.toFirstUpper»(const «dto.domainKeyType»& «dto.domainKey»)
{
    for (int i = 0; i < mAll«dto.toName».size(); ++i) {
        «dto.toName»* «dto.toName.toFirstLower»;
        «dto.toName.toFirstLower» = («dto.toName»*) mAll«dto.toName».at(i);
        if («dto.toName.toFirstLower»->«dto.domainKey»() == «dto.domainKey») {
            mAll«dto.toName».removeAt(i);
            «IF dto.isTree»
            mAll«dto.toName»Flat.removeOne(«dto.toName.toFirstLower»);
            «ENDIF»
            emit deletedFromAll«dto.toName»By«dto.domainKey.toFirstUpper»(«dto.domainKey»);
            emit deletedFromAll«dto.toName»(«dto.toName.toFirstLower»);
            «dto.toName.toFirstLower»->deleteLater();
            «dto.toName.toFirstLower» = 0;
            return true;
        }
    }
    return false;
}
«ENDIF»

«IF dto.isTree»
void DataManager::fill«dto.toName»TreeDataModel(QString objectName)
{
    // using dynamic created Pages / Lists it's a good idea to use findChildren ... last()
    // probably there are GroupDataModels not deleted yet from previous destroyed Pages
    QList<GroupDataModel*> dataModelList = Application::instance()->scene()->findChildren<
            GroupDataModel*>(objectName);
    if (dataModelList.size() > 0) {
    	GroupDataModel* dataModel = dataModelList.last();
    	if (dataModel) {
        	QList<QObject*> theList;
        	for (int i = 0; i < mAll«dto.toName».size(); ++i) {
            	theList.append(mAll«dto.toName».at(i));
        	}
        	dataModel->clear();
        	dataModel->insertList(theList);
        	return;
    	}
    }
    qDebug() << "NO GRP DATA FOUND «dto.toName» for " << objectName;
}
void DataManager::fill«dto.toName»FlatDataModel(QString objectName)
{
    // using dynamic created Pages / Lists it's a good idea to use findChildren ... last()
    // probably there are GroupDataModels not deleted yet from previous destroyed Pages
    QList<GroupDataModel*> dataModelList = Application::instance()->scene()->findChildren<
            GroupDataModel*>(objectName);
    if (dataModelList.size() > 0) {
    	GroupDataModel* dataModel = dataModelList.last();
    	if (dataModel) {
        	QList<QObject*> theList;
        	for (int i = 0; i < mAll«dto.toName»Flat.size(); ++i) {
            	theList.append(mAll«dto.toName»Flat.at(i));
        	}
        	dataModel->clear();
        	dataModel->insertList(theList);
        	return;
    	}
    }
    qDebug() << "NO GRP DATA FOUND «dto.toName» for " << objectName;
}
«ELSE»
void DataManager::fill«dto.toName»DataModel(QString objectName)
{
    // using dynamic created Pages / Lists it's a good idea to use findChildren ... last()
    // probably there are GroupDataModels not deleted yet from previous destroyed Pages
    QList<GroupDataModel*> dataModelList = Application::instance()->scene()->findChildren<
            GroupDataModel*>(objectName);
    if (dataModelList.size() > 0) {
    	GroupDataModel* dataModel = dataModelList.last();
    	if (dataModel) {
        	QList<QObject*> theList;
        	for (int i = 0; i < mAll«dto.toName».size(); ++i) {
            	theList.append(mAll«dto.toName».at(i));
        	}
        	dataModel->clear();
        	dataModel->insertList(theList);
        	return;
    	}
    }
    qDebug() << "NO GRP DATA FOUND «dto.toName» for " << objectName;
}
«ENDIF»
/**
 * removing and re-inserting a single item of a DataModel
 * this will cause the ListView to redraw or recalculate all values for this ListItem
 * we do this, because only changing properties won't call List functions
 */
void DataManager::replaceItemIn«dto.toName»DataModel(QString objectName,
        «dto.toName»* listItem)
{
    // using dynamic created Pages / Lists it's a good idea to use findChildren ... last()
    // probably there are GroupDataModels not deleted yet from previous destroyed Pages
    QList<GroupDataModel*> dataModelList = Application::instance()->scene()->findChildren<
            GroupDataModel*>(objectName);
    if (dataModelList.size() > 0) {
        GroupDataModel* dataModel = dataModelList.last();
        if (dataModel) {
            bool exists = dataModel->remove(listItem);
            if (exists) {
                dataModel->insert(listItem);
                return;
            }
            qDebug() << "«dto.toName» Object not found and not replaced in " << objectName;
        }
        return;
    }
    qDebug() << "no DataModel found for " << objectName;
}

void DataManager::removeItemFrom«dto.toName»DataModel(QString objectName, «dto.toName»* listItem)
{
    // using dynamic created Pages / Lists it's a good idea to use findChildren ... last()
    // probably there are GroupDataModels not deleted yet from previous destroyed Pages
    QList<GroupDataModel*> dataModelList = Application::instance()->scene()->findChildren<
            GroupDataModel*>(objectName);
    if (dataModelList.size() > 0) {
        GroupDataModel* dataModel = dataModelList.last();
        if (dataModel) {
            bool exists = dataModel->remove(listItem);
            if (exists) {
                return;
            }
            qDebug() << "«dto.toName» Object not found and not removed from " << objectName;
        }
        return;
    }
    qDebug() << "no DataModel found for " << objectName;
}

void DataManager::insertItemInto«dto.toName»DataModel(QString objectName, «dto.toName»* listItem)
{
    // using dynamic created Pages / Lists it's a good idea to use findChildren ... last()
    // probably there are GroupDataModels not deleted yet from previous destroyed Pages
    QList<GroupDataModel*> dataModelList = Application::instance()->scene()->findChildren<
            GroupDataModel*>(objectName);
    if (dataModelList.size() > 0) {
        GroupDataModel* dataModel = dataModelList.last();
        if (dataModel) {
            dataModel->insert(listItem);
        }
        return;
    }
    qDebug() << "no DataModel found for " << objectName;
}
«FOR feature : dto.allFeatures.filter[hasIndex]»

«IF feature.isLazy»
void DataManager::fill«dto.toName»DataModelBy«feature.toName.toFirstUpper»(QString objectName, const «feature.referenceDomainKeyType»& «feature.toName»)
«ELSE»
void DataManager::fill«dto.toName»DataModelBy«feature.toName.toFirstUpper»(QString objectName, const «feature.toTypeName»& «feature.toName»)
«ENDIF»
{
    // using dynamic created Pages / Lists it's a good idea to use findChildren ... last()
    // probably there are GroupDataModels not deleted yet from previous destroyed Pages
    QList<GroupDataModel*> dataModelList = Application::instance()->scene()->findChildren<
            GroupDataModel*>(objectName);
    if (dataModelList.size() > 0) {
        GroupDataModel* dataModel = dataModelList.last();
        if (dataModel) {
            QList<QObject*> theList;
            for (int i = 0; i < mAll«dto.toName».size(); ++i) {
                «dto.toName»* «dto.toName.toFirstLower»;
                «dto.toName.toFirstLower» = («dto.toName»*) mAll«dto.toName».at(i);
                if («dto.toName.toFirstLower»->«feature.toName»() == «feature.toName») {
                    theList.append(mAll«dto.toName».at(i));
                }
            }
            dataModel->clear();
            dataModel->insertList(theList);
            qDebug() << "fill«dto.toName»DataModelBy«feature.toName.toFirstUpper» " << «feature.toName» << " (" << objectName << ") #"
                    << theList.size();
            return;
        }
    }
    qDebug() << "NO GRP DATA FOUND «dto.toName» for " << objectName;
}
«ENDFOR»
«IF dto.hasUuid»
«dto.toName»* DataManager::find«dto.toName»ByUuid(const QString& uuid){
    if (uuid.isNull() || uuid.isEmpty()) {
        qDebug() << "cannot find «dto.toName» from empty uuid";
        return 0;
    }
    for (int i = 0; i < mAll«dto.toName»«IF dto.isTree»Flat«ENDIF».size(); ++i) {
        «dto.toName»* «dto.toName.toFirstLower»;
        «dto.toName.toFirstLower» = («dto.toName»*)mAll«dto.toName»«IF dto.isTree»Flat«ENDIF».at(i);
        if(«dto.toName.toFirstLower»->uuid() == uuid){
            return «dto.toName.toFirstLower»;
        }
    }
    qDebug() << "no «dto.toName» found for uuid " << uuid;
    return 0;
}
«ENDIF»

«IF dto.hasDomainKey && dto.domainKey != "uuid"»
// nr is DomainKey
«dto.toName»* DataManager::find«dto.toName»By«dto.domainKey.toFirstUpper»(const «dto.domainKeyType»& «dto.domainKey»){
    for (int i = 0; i < mAll«dto.toName»«IF dto.isTree»Flat«ENDIF».size(); ++i) {
        «dto.toName»* «dto.toName.toFirstLower»;
        «dto.toName.toFirstLower» = («dto.toName»*)mAll«dto.toName»«IF dto.isTree»Flat«ENDIF».at(i);
        if(«dto.toName.toFirstLower»->«dto.domainKey»() == «dto.domainKey»){
            return «dto.toName.toFirstLower»;
        }
    }
    qDebug() << "no «dto.toName» found for «dto.domainKey» " << «dto.domainKey»;
    return 0;
}
«ENDIF»
		«ENDIF»
	«ENDFOR»

/*
 * reads data in from stored cache
 * if no cache found tries to get data from assets/datamodel
 */
QVariantList DataManager::readFromCache(const QString& fileName)
{
    JsonDataAccess jda;
    QVariantList cacheList;
    QString cacheFilePath;
    QString assetsFilePath;
    if(fileName == cacheSettingsData) {
        cacheFilePath = settingsPath(fileName);
        assetsFilePath = settingsAssetsPath(fileName);
    } else {
        cacheFilePath = dataPath(fileName);
        assetsFilePath = dataAssetsPath(fileName);
    }
    QFile dataFile(cacheFilePath);
    if (!dataFile.exists()) {
        QFile assetDataFile(assetsFilePath);
        if (assetDataFile.exists()) {
            // copy file from assets to data
            bool copyOk = assetDataFile.copy(cacheFilePath);
            if (!copyOk) {
                qDebug() << "cannot copy dataAssetsPath(fileName) to dataPath(fileName)";
                // no cache, no assets - empty list
                return cacheList;
            }
        } else {
            // no cache, no assets - empty list
            return cacheList;
        }
    }
    cacheList = jda.load(cacheFilePath).toList();
    return cacheList;
}

void DataManager::writeToCache(const QString& fileName, QVariantList& data)
{
    QString filePath;
    if(fileName == cacheSettingsData) {
        filePath = settingsPath(fileName);
    } else {
        filePath = dataPath(fileName);
    }
    JsonDataAccess jda;
    jda.save(data, filePath);
}

void DataManager::onManualExit()
{
    qDebug() << "## DataManager ## MANUAL EXIT";
    finish();
    emit shuttingDown();
    bb::Application::instance()->exit(0);
}

DataManager::~DataManager()
{
    // clean up
}
	'''
}
