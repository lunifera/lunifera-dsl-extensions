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

«FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
#include "«dto.toName».hpp"
«ENDFOR»
«IF pkg.hasGeoCoordinate»
#include  "GeoCoordinate.hpp"
«ENDIF»
«IF pkg.hasGeoAddress»
#include  "GeoAddress.hpp"
«ENDIF»

class DataManager: public QObject
{
Q_OBJECT

public:
    DataManager(QObject *parent = 0);
    virtual ~DataManager();
    Q_INVOKABLE
    void init();

	«FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
	«IF dto.isRootDataObject»
	
	«IF dto.isTree»
	Q_INVOKABLE
	void fill«dto.toName»TreeDataModel(QString objectName);

	Q_INVOKABLE
	void fill«dto.toName»FlatDataModel(QString objectName);
	«ELSE»
	Q_INVOKABLE
	void fill«dto.toName»DataModel(QString objectName);
	«ENDIF»
	«IF dto.existsLazy»

	Q_INVOKABLE
	void resolve«dto.toName»References(«dto.toName»* «dto.toName.toFirstLower»);

	Q_INVOKABLE
	void resolveReferencesForAll«dto.toName»();
	«ENDIF»

	Q_INVOKABLE
	void insert«dto.toName»(«dto.toName»* «dto.toName.toFirstLower»);

	Q_INVOKABLE
	void insert«dto.toName»FromMap(const QVariantMap& «dto.toName.toFirstLower»Map, const bool& useForeignProperties);

	Q_INVOKABLE
	bool delete«dto.toName»(«dto.toName»* «dto.toName.toFirstLower»);
	«IF dto.hasUuid»

	Q_INVOKABLE
	bool delete«dto.toName»ByUuid(const QString& uuid);

	Q_INVOKABLE
	«dto.toName»* find«dto.toName»ByUuid(const QString& uuid);
	«ENDIF»
	«IF dto.hasDomainKey && dto.domainKey != "uuid"»

	Q_INVOKABLE
	bool delete«dto.toName»By«dto.domainKey.toFirstUpper»(const «dto.domainKeyType»& «dto.domainKey»);

	Q_INVOKABLE
    «dto.toName»* find«dto.toName»By«dto.domainKey.toFirstUpper»(const «dto.domainKeyType»& «dto.domainKey»);
    «ENDIF»
	«ENDIF»
	«ENDFOR»

Q_SIGNALS:

	«FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
	«IF dto.isRootDataObject»
	void addedToAll«dto.toName»(«dto.toName»* orderData);
	«IF dto.hasUuid»
	void deletedFromAll«dto.toName»ByUuid(QString uuid);
	«ENDIF»
	«IF dto.hasDomainKey && dto.domainKey != "uuid"»
	void deletedFromAll«dto.toName»By«dto.domainKey.toFirstUpper»(«dto.domainKeyType» «dto.domainKey»);
	«ENDIF»
	«ENDIF»
    «ENDFOR»
    
public slots:
    void onManualExit();

private:

	// DataObject stored in List of QObject*
	// GroupDataModel only supports QObject*
    «FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
    	«IF dto.isRootDataObject»
    	QList<QObject*> mAll«dto.toName»;
		«ENDIF»
    	«IF dto.isTree»
    	QList<QObject*> mAll«dto.toName»Flat;
		«ENDIF»
	«ENDFOR»

    «FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
    	«IF dto.isRootDataObject»
    	void init«dto.toName»FromCache();
    	void save«dto.toName»ToCache();
		«ENDIF»
	«ENDFOR»

	QVariantList readFromCache(QString& fileName);
	void writeToCache(QString& fileName, QVariantList& data);
	void finish();
};

#endif /* DATAMANAGER_HPP_ */
		'''
}
