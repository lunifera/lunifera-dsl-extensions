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
import org.lunifera.dsl.ext.cpp.qt.lib.types.annotation.SimpleName
import org.lunifera.dsl.semantic.common.types.LAnnotationTarget
import org.lunifera.dsl.semantic.common.types.LAttribute
import org.lunifera.dsl.semantic.common.types.LFeature
import org.lunifera.dsl.semantic.common.types.LReference
import org.lunifera.dsl.semantic.dto.LDtoAbstractAttribute
import org.lunifera.dsl.semantic.dto.LDtoAbstractReference

class CppExtensions {

	@Inject extension JvmTypesBuilder
	@Inject DtoModelExtensions modelExtension
	@Inject extension AnnotationExtension

	def String toName(LAnnotationTarget target) {
		modelExtension.toName(target)
	}
	
	def dispatch String toAliasOrName(LAnnotationTarget target) {
		modelExtension.toName(target)
	}

	def dispatch String toAliasOrName(LAttribute target) {
		val value = target.aliasValue
		if (value != null) {
			return value
		}
		modelExtension.toName(target)
	}

	def dispatch String toAliasOrName(LReference target) {
		val value = target.aliasValue
		if (value != null) {
			return value
		}
		modelExtension.toName(target)
	}

	def dispatch String toTypeName(LAttribute att) {
		modelExtension.toTypeName(att as LDtoAbstractAttribute)
	}

	def dispatch String toTypeName(LReference ref) {
		modelExtension.toTypeName(ref as LDtoAbstractReference)
	}

	def String mapToType(LFeature feature) {
		switch (feature.toTypeName) {
			case "bool":
				return "Bool"
			case "int":
				return "Int"
			case "QString":
				return "String"
		}
		if (feature.isToMany) {
			return "List"
		}
		return "Map"
	}

	def String defaultForType(LFeature feature) {
		switch (feature.toTypeName) {
			case "bool":
				return "false"
			case "int":
				return "0"
			case "QString":
				return "\"\""
		}
		return ""
	}

	def boolean isToMany(LFeature feature) {
		modelExtension.isToMany(feature)
	}

	def toCopyRight(EObject element) {
		var docu = element.documentation
		if (!docu.nullOrEmpty) {
			var docus = docu.split('\n')
			if (docus.length > 1) {
				return '''
				/**
				«FOR line : docus»
					«" * "»«line»
				«ENDFOR»
				 */'''.toString
			} else if (docus.length == 1) {
				return '''/** «docu» */'''.toString
			}
		}
		return ""
	}

	def boolean hasAlias(LFeature feature) {
		if (feature.aliasValue == null){
			return false
		}
		return true
	}

	def String getAliasValue(LFeature member) {
		val annoDef = typeof(SimpleName).getRedefined(member.resolvedAnnotations)
		if (annoDef != null) {
			val JvmCustomAnnotationValue annotationValue = toJvmAnnotationValue(annoDef.annotation.value) as JvmCustomAnnotationValue;
			val XStringLiteral lit = annotationValue.values.get(0) as XStringLiteral
			return lit.value
		} else {
			return null
		}
	}

}
