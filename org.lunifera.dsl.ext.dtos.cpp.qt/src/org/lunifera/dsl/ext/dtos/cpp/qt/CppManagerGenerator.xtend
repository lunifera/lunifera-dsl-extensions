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

class CppManagerGenerator {

	@Inject extension CppExtensions
	@Inject extension ManagerExtensions

	def String toFileName(LTypedPackage pkg) {
		"DTOManager.cpp"
	}

	def CharSequence toContent(LTypedPackage pkg) '''
#include <QObject>

#include "DTOManager.hpp"

#include <bb/cascades/Application>
#include <bb/cascades/AbstractPane>
#include <bb/data/JsonDataAccess>
#include  <bb/cascades/GroupDataModel>

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
// there's also a plain list (in memory only) useful for easy filtering
		«ENDIF»
    	«IF dto.isRootDTO»
static QString cache«dto.toName» = "cache«dto.toName».json";
		«ENDIF»
	«ENDFOR»

using namespace bb::cascades;
using namespace bb::data;

DTOManager::DTOManager(QObject *parent) :
        QObject(parent)
{
    // ApplicationUI is parent of DTOManager
    // DTOManager is parent of all root DTOs
    // ROOT DTOs are parent of contained DTOs
    // ROOT:
    «FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
    	«IF dto.isRootDTO»
    	// «dto.toName»
		«ENDIF»
	«ENDFOR»

    // register all DTOs to get access to properties from QML:	
	«FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
		qmlRegisterType<«dto.toName»>("org.ekkescorner", 1, 0, "«dto.toName»");
	«ENDFOR»
}

/*
 * loads all data from cache.
 * tip: call from main.qml with delay using QTimer
 */
void DTOManager::init()
{
    «FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
    	«IF dto.isRootDTO»
    	init«dto.toName»();
		«ENDIF»
	«ENDFOR»
}

    «FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
    	«IF dto.isRootDTO»
/*
 * reads «dto.toName»'s in from stored cache
 * creates List of QObject* containing «dto.toName»'s
 */
void DTOManager::init«dto.toName»()
{
    mAll«dto.toName».clear();
    QVariantList cacheList;
    cacheList = readCache(cache«dto.toName»);
    qDebug() << "read «dto.toName» from cache #" << cacheList.size();
    for (int i = 0; i < cacheList.size(); ++i) {
        QVariantMap cacheMap;
        cacheMap = cacheList.at(i).toMap();
        «dto.toName»* «dto.toName.toFirstLower» = new «dto.toName»();
        // Important: DTOManager must be parent of all root DTOs
        «dto.toName.toFirstLower»->setParent(this);
        «dto.toName.toFirstLower»->fillFromMap(cacheMap);
        mAll«dto.toName».append(«dto.toName.toFirstLower»);
    }
    qDebug() << "created «dto.toName»* #" << mAll«dto.toName».size();
}

void DTOManager::fill«dto.toName»DataModel(QString objectName)
{
    GroupDataModel* dataModel = Application::instance()->scene()->findChild<GroupDataModel*>(
            objectName);
    if(dataModel){
        QList<QObject*> theList;
        for (int i = 0; i < mAll«dto.toName».size(); ++i) {
            theList.append(mAll«dto.toName».at(i));
        }
        dataModel->clear();
        dataModel->insertList(theList);
    } else {
        qDebug() << "NO GRP DATA FOUND «dto.toName» for " << objectName;
    }
}
		«ENDIF»
	«ENDFOR»

/*
 * reads data in from stored cache
 * if no cache found tries to get data from assets/datamodel
 */
QVariantList DTOManager::readCache(QString& fileName)
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

DTOManager::~DTOManager()
{
    // clean up
}
	'''
}
