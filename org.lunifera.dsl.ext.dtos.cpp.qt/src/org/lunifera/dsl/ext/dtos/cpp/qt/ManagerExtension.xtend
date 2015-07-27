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
 * 		Florian Pirchner - initial work.
 * 		ekke (Ekkehard Gentz), Rosenheim (Germany)
 */
package org.lunifera.dsl.ext.dtos.cpp.qt

import com.google.inject.Inject
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.common.types.JvmCustomAnnotationValue
import org.eclipse.xtext.xbase.XStringLiteral
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder
import org.lunifera.dsl.dto.xtext.extensions.AnnotationExtension
import org.lunifera.dsl.dto.xtext.extensions.DtoModelExtensions
import org.lunifera.dsl.semantic.common.helper.Bounds
import org.lunifera.dsl.semantic.common.types.LAnnotationTarget
import org.lunifera.dsl.semantic.common.types.LAttribute
import org.lunifera.dsl.semantic.common.types.LFeature
import org.lunifera.dsl.semantic.common.types.LReference
import org.lunifera.dsl.semantic.dto.LDto
import org.lunifera.dsl.semantic.dto.LDtoAbstractAttribute
import org.lunifera.dsl.semantic.dto.LDtoAbstractReference
import org.lunifera.dsl.semantic.dto.LDtoReference
import org.lunifera.dsl.ext.cpp.qt.lib.types.annotation.ForeignPropertyName
import org.lunifera.dsl.semantic.common.types.LTypedPackage

class ManagerExtensions {

	@Inject extension JvmTypesBuilder
	@Inject DtoModelExtensions modelExtension
	@Inject extension AnnotationExtension
	@Inject extension CppExtensions

	def boolean hasSqlCache(LTypedPackage pkg) {
		for (lt : pkg.types) {
			if (lt instanceof LDto) {
				if (lt.hasSqlCachePropertyName) {
					return true
				}
			}
		}
		return false
	}
	
	def boolean has2PhaseInit(LTypedPackage pkg) {
		for (lt : pkg.types) {
			if (lt instanceof LDto) {
				if (lt.is2PhaseInit) {
					return true
				}
			}
		}
		return false
	}

	def boolean hasGeoCoordinate(LTypedPackage pkg) {
		for (lt : pkg.types) {
			if (lt instanceof LDto) {
				if (lt.existsGeoCoordinate) {
					return true
				}
			}
		}
		return false
	}

	def boolean hasGeoAddress(LTypedPackage pkg) {
		for (lt : pkg.types) {
			if (lt instanceof LDto) {
				if (lt.existsGeoAddress) {
					return true
				}
			}
		}
		return false
	}

	def boolean hasGeo(LTypedPackage pkg) {
		if (pkg.hasGeoCoordinate || pkg.hasGeoAddress) {
			return true
		}
		return false
	}

}
