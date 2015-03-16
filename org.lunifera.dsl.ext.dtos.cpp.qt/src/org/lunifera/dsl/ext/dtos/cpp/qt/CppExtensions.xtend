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
import org.lunifera.dsl.ext.cpp.qt.lib.types.annotation.ServerName
import org.lunifera.dsl.semantic.common.helper.Bounds
import org.lunifera.dsl.semantic.common.types.LAnnotationTarget
import org.lunifera.dsl.semantic.common.types.LAttribute
import org.lunifera.dsl.semantic.common.types.LFeature
import org.lunifera.dsl.semantic.common.types.LReference
import org.lunifera.dsl.semantic.dto.LDto
import org.lunifera.dsl.semantic.dto.LDtoAbstractAttribute
import org.lunifera.dsl.semantic.dto.LDtoAbstractReference

class CppExtensions {

	@Inject extension JvmTypesBuilder
	@Inject DtoModelExtensions modelExtension
	@Inject extension AnnotationExtension

	def String toName(LAnnotationTarget target) {
		modelExtension.toName(target)
	}

	def dispatch String toServerName(LAnnotationTarget target) {
		modelExtension.toName(target)
	}

	def dispatch String toServerName(LAttribute target) {
		val value = target.serverNameValue
		if (value != null) {
			return value
		}
		modelExtension.toName(target)
	}

	def dispatch String toServerName(LReference target) {
		val value = target.serverNameValue
		if (value != null) {
			return value
		}
		modelExtension.toName(target)
	}

	def dispatch boolean isContained(LAnnotationTarget target) {
		return false
	}

	def dispatch boolean isContained(LAttribute target) {
		return false
	}

	def dispatch boolean isContained(LReference target) {
		return target.isCascading
	}
	
	def dispatch boolean isTransient(LAnnotationTarget target) {
		return false
	}

	def dispatch boolean isTransient(LAttribute target) {
		return target.isTransient
	}

	def dispatch boolean isTransient(LReference target) {
		return false
	}

	def dispatch String toTypeName(LAttribute att) {
		modelExtension.toTypeName(att as LDtoAbstractAttribute)
	}

	def dispatch String toTypeName(LReference ref) {
		modelExtension.toTypeName(ref as LDtoAbstractReference)
	}

	def boolean isTypeOfDTO(LFeature feature) {
		if (feature.toTypeName.endsWith("DTO")) {
			return true
		}
		return false
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
				if(feature.isMandatory){
					return "-1"
				} else {
					return "0"
				}
			case "QString":
				return "\"\""
		}
		return ""
	}

	def boolean isToMany(LFeature feature) {
		modelExtension.isToMany(feature)
	}

	def Bounds getBounds(LFeature feature) {
		modelExtension.getBounds(feature)
	}
	
	def boolean isOptional(LFeature feature){
		return feature.bounds.isOptional;
	}
	
	def boolean isMandatory(LFeature feature){
		return feature.bounds.isRequired;
	}
	
	def toValidate(LFeature feature){
		switch (feature.toTypeName) {
			case "int":
				return '''
				if(m«feature.toName.toFirstUpper» == -1){
					return false;
				}
				'''.toString
			case "QString":
				return '''
				if(m«feature.toName.toFirstUpper».isNull() || m«feature.toName.toFirstUpper».isEmpty())
				{
					return false;
				}
				'''.toString
		}
		return '''
		// missing validation for m«feature.toName.toFirstUpper»
		'''.toString
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

	def boolean isQueueObject(LDto element) {
		var docu = element.documentation
		if (!docu.nullOrEmpty) {
			if (docu == "QUEUE") {
				return true
			}
		}
		return false
	}

	def toDtoDocu(LDto element) {
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

	def boolean hasServerName(LFeature feature) {
		if (feature.serverNameValue == null) {
			return false
		}
		return true
	}
	
	def boolean existsServerName(LDto dto){
		for (feature : dto.allFeatures){
			if(feature.hasServerName){
				return true
			}
		}
		return false
	}

	def toTypeOrQObject(LFeature feature) {
		if (feature.toTypeName.endsWith("DTO")) {
			return '''«feature.toTypeName»*'''.toString
		}
		return feature.toTypeName
	}

	def toMapOrList(LFeature feature) {
		if (feature.toMany) {
			return '''List'''.toString
		}
		return '''Map'''.toString
	}

	def String getServerNameValue(LFeature member) {
		val annoDef = typeof(ServerName).getRedefined(member.resolvedAnnotations)
		if (annoDef != null) {
			val JvmCustomAnnotationValue annotationValue = toJvmAnnotationValue(annoDef.annotation.value) as JvmCustomAnnotationValue;
			val XStringLiteral lit = annotationValue.values.get(0) as XStringLiteral
			return lit.value
		} else {
			return null
		}
	}

}
