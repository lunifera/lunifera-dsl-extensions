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

#include <bb/cascades/Application>
#include <bb/cascades/AbstractPane>
#include <bb/data/JsonDataAccess>
#include <bb/cascades/GroupDataModel>

«IF pkg.hasSqlCache»
#include <QtSql/QtSql>

static QString dbName = "sqlcache.db";
«ENDIF»

static QString dataAssetsPath(const QString& fileName)
{
    return QDir::currentPath() + "/app/native/assets/datamodel/" + fileName;
}
static QString dataPath(const QString& fileName)
{
    return QDir::currentPath() + "/data/" + fileName;
}
    «FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
    	«IF dto.isTree»
// cache«dto.toName» is tree of  «dto.toName»
// there's also a flat list (in memory only) useful for easy filtering
		«ENDIF»
    	«IF dto.isRootDataObject»
static QString cache«dto.toName» = "cache«dto.toName».json";
		«ENDIF»
	«ENDFOR»

using namespace bb::cascades;
using namespace bb::data;

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

    Q_UNUSED(res);
}

/*
 * loads all data from cache.
 * called from main.qml with delay using QTimer
 */
void DataManager::init()
{
	«IF pkg.hasSqlCache»
	// SQL init the sqlite database
	mDatabaseAvailable = initDatabase();
	qDebug() << "SQLite created or opened ? " << mDatabaseAvailable;
	«ENDIF»

    «FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
    	«IF dto.isRootDataObject»
    	init«dto.toName»FromCache();
		«ENDIF»
	«ENDFOR»
}

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
    QSqlDatabase database = QSqlDatabase::addDatabase("QSQLITE");
    database.setDatabaseName(dataPath(dbName));
    if (database.open() == false) {
        const QSqlError error = database.lastError();
        // you should notify the user !
        qWarning() << "Cannot open " << dbName << ":" << error.text();
        return false;
    }
    qDebug() << "Database opened: " << dbName;
    // create the Connection
    mSQLda = new SqlDataAccess(dataPath(dbName), this);
    return true;
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
    }
    «IF dto.isTree»
    qDebug() << "created Tree of «dto.toName»* #" << mAll«dto.toName».size();
    qDebug() << "created Flat list of «dto.toName»* #" << mAll«dto.toName»Flat.size();
    «ELSE»
    qDebug() << "created «dto.toName»* #" << mAll«dto.toName».size();
    «ENDIF»
}

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
 * convert list of «dto.toName»* to QVariantList
 * toCacheMap stores all properties without transient values
 * «dto.toName» is read-only Cache - so it's not saved automatically at exit
 */
void DataManager::save«dto.toName»ToSqlCache()
{
    qDebug() << "now caching «dto.toName»* #" << mAll«dto.toName».size();
    // to be safe we drop an existing table
    mSQLda->execute("DROP TABLE IF EXISTS «dto.toName.toFirstLower»");
    // create table
    mSQLda->execute(«dto.toName»::createTableCommand());
    if (mSQLda->hasError()) {
        qWarning() << "Create table «dto.toName.toFirstLower» Error " << mSQLda->error().errorMessage();
        return;
    }
    QString insertSQL = «dto.toName»::createParameterizedInsertCommand();
    //
    QVariantList cacheList;
    for (int i = 0; i < mAll«dto.toName».size(); ++i) {
        «dto.toName»* «dto.toName.toFirstLower»;
        «dto.toName.toFirstLower» = («dto.toName»*)mAll«dto.toName».at(i);
        QVariantMap cacheMap;
        cacheMap = «dto.toName.toFirstLower»->toSqlCacheMap();
        cacheList.append(cacheMap);
    }
    qDebug() << "«dto.toName»* converted to SQL cache #" << cacheList.size();
    // HINT: executeBatch command automatically runs in a TRANSACTION to get best results
    mSQLda->executeBatch(insertSQL, cacheList);
    if (mSQLda->hasError()) {
        qWarning() << "Insert into table «dto.toName.toFirstLower» Error " << mSQLda->error().errorMessage();
    } else {
        qDebug() << "«dto.toName»* inserted into SQLite";
    }
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
                listOf«dto.toName.toFirstUpper»ForKeys(«dto.toName.toFirstLower»->«feature.toName»Keys()));
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
QVariantList DataManager::readFromCache(QString& fileName)
{
    JsonDataAccess jda;
    QVariantList cacheList;
    QFile dataFile(dataPath(fileName));
    if (!dataFile.exists()) {
        QFile assetDataFile(dataAssetsPath(fileName));
        if (assetDataFile.exists()) {
            // copy file from assets to data
            bool copyOk = assetDataFile.copy(dataPath(fileName));
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
    cacheList = jda.load(dataPath(fileName)).toList();
    return cacheList;
}

void DataManager::writeToCache(QString& fileName, QVariantList& data)
{
    QString filePath;
    filePath = dataPath(fileName);
    JsonDataAccess jda;
    jda.save(data, filePath);
}

void DataManager::onManualExit()
{
    qDebug() << "## DataManager ## MANUAL EXIT";
    finish();
    bb::Application::instance()->exit(0);
}

DataManager::~DataManager()
{
    // clean up
}
	'''
}
