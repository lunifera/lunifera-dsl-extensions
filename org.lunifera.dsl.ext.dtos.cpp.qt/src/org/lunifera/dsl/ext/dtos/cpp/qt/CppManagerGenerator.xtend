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

using namespace bb::cascades;
using namespace bb::data;

DTOManager::DTOManager(QObject *parent) :
        QObject(parent)
{
    // ApplicationUI is parent of DTOManager
    // DTOManager is parent of all root DTOs
    // root DTOs are parent of contained DTOs
    «FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
    	«IF dto.isRootDTO»
			// ROOT: «dto.toName»
		«ENDIF»
    	«IF dto.isTree»
			// TREE: «dto.toName»
		«ENDIF»
	«ENDFOR»

    // register all DTOs to get access to properties from QML:	
	«FOR dto : pkg.types.filter[it instanceof LDto].map[it as LDto]»
		qmlRegisterType<«dto.toName»>("org.ekkescorner", 1, 0, "«dto.toName»");
	«ENDFOR»
}

DTOManager::~DTOManager()
{
    // clean up
}
	'''
}
