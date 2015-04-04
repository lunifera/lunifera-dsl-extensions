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
import org.lunifera.dsl.semantic.common.types.LEnum
import org.lunifera.dsl.semantic.common.types.LEnumLiteral
import org.lunifera.dsl.ext.cpp.qt.lib.types.annotation.EnumValues
import org.lunifera.dsl.ext.cpp.qt.lib.types.annotation.ForeignPropertyName
import org.lunifera.dsl.ext.cpp.qt.lib.types.annotation.DateFormatString

class CppExtensions {

	@Inject extension JvmTypesBuilder
	@Inject DtoModelExtensions modelExtension
	@Inject extension AnnotationExtension

	def String toName(LAnnotationTarget target) {
		modelExtension.toName(target)
	}

	def dispatch String toForeignPropertyName(LAnnotationTarget target) {
		modelExtension.toName(target)
	}

	def dispatch String toForeignPropertyName(LAttribute target) {
		val value = target.foreignPropertyNameValue
		if (value != null) {
			return value
		}
		modelExtension.toName(target)
	}

	def dispatch String toForeignPropertyName(LReference target) {
		val value = target.foreignPropertyNameValue
		if (value != null) {
			return value
		}
		modelExtension.toName(target)
	}
	
		def dispatch String toDateFormatString(LAnnotationTarget target) {
		return ""
	}

	def dispatch String toDateFormatString(LAttribute target) {
		val value = target.dateFormatStrngValue
		if (value != null) {
			return '''"«value»"'''.toString
		}
		return "Qt::ISODate"
	}

	def dispatch String toDateFormatString(LReference target) {
		return ""
	}

	def dispatch String toEnumValues(LAttribute target) {

		return ""
	}

	def dispatch String toEnumValues(LEnum target) {
		val values = target.enumValues
		if (values != null) {
			return values
		}
		return ""
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

	def dispatch boolean isContained(LDtoReference target) {
		return target.isCascading
	}

	def dispatch boolean hasOpposite(LDtoAbstractAttribute att) {
		return false
	}

	def dispatch boolean hasOpposite(LDtoAbstractReference ref) {
		return false
	}

	def dispatch boolean hasOpposite(LDtoReference ref) {
		return ref.opposite != null
	}

	def dispatch boolean hasOpposite(LFeature feature) {
		return false
	}

	def dispatch boolean isDomainKey(LAnnotationTarget target) {
		return false
	}

	def dispatch boolean isDomainKey(LAttribute target) {
		return target.isDomainKey
	}

	def dispatch boolean isDomainKey(LReference target) {
		return false
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

	def dispatch boolean isLazy(LAnnotationTarget target) {
		return false
	}

	def dispatch boolean isLazy(LAttribute target) {
		return false
	}

	def dispatch boolean isLazy(LReference target) {
		return target.isLazy
	}

	def dispatch String toTypeName(LAttribute att) {
		switch (modelExtension.toTypeName(att as LDtoAbstractAttribute)) {
			case "Date":
				return "QDate"
			case "Time":
				return "QTime"
			case "Timestamp":
				return "QDateTime"
		}
		return modelExtension.toTypeName(att as LDtoAbstractAttribute)
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
	
	def boolean isTypeOfDates(LFeature feature) {
		if (feature.toTypeName == "QDate" || feature.toTypeName == "QTime" || feature.toTypeName == "QDateTime") {
			return true
		}
		return false
	}

	def dispatch boolean isEnum(LDtoAbstractAttribute att) {
		if (modelExtension.typeIsEnum(att)) {
			return true
		}
		return false
	}

	def dispatch boolean isEnum(LFeature feature) {
		return false
	}

	def dispatch LEnum enumFromAttributeType(LDtoAbstractAttribute att) {
		return att.getType() as LEnum
	}

	def dispatch LEnum enumFromAttributeType(LFeature feature) {
		return null
	}

	def int enumIndex(LEnum en, LEnumLiteral lit) {
		for (var i = 0; i < en.literals.size; i++) {
			if(lit == en.literals.get(i)){
				return i;
			}
		}
		return -1;
	}

	def String mapToType(LFeature feature) {
		switch (feature.toTypeName) {
			case "bool":
				return "Bool"
			case "int":
				if (feature.isToMany) {
					return "List"
				} else {
					return "Int"
				}
			case "double":
				if (feature.isToMany) {
					return "List"
				} else {
					return "Double"
				}
			case "QString":
				if (feature.isToMany) {
					return "StringList"
				} else {
					return "String"
				}
			case "QDate":
				if (feature.isToMany) {
					return "List"
				} else {
					return "Date"
				}
			case "QTime":
				if (feature.isToMany) {
					return "List"
				} else {
					return "Time"
				}
			case "QDateTime":
				if (feature.isToMany) {
					return "List"
				} else {
					return "DateTime"
				}
		}
		if (feature.isToMany) {
			return "List"
		}
		if (feature.isEnum) {
			return "Int"
		}
		return "Map"
	}

	def String mapToSingleType(LFeature feature) {
		switch (feature.toTypeName) {
			case "bool":
				return "Bool"
			case "int":
				return "Int"
			case "double":
				return "Double"
			case "QString":
				return "String"
			case "QDate":
				return "Date"
			case "QTime":
				return "Time"
			case "QDateTime":
				return "DateTime"
		}
		return "Map"
	}

	def String mapToLazyTypeName(String typeName) {
		switch (typeName) {
			case "int":
				return "Int"
			case "QString":
				return "String"
		}
		return "String"
	}

	def String defaultForType(LFeature feature) {
		switch (feature.toTypeName) {
			case "bool":
				return "false"
			case "int":
				if (feature.isMandatory || feature.isDomainKey) {
					return "-1"
				} else {
					return "0"
				}
			case "double":
				if (feature.isMandatory || feature.isDomainKey) {
					return "-1.0"
				} else {
					return "0.0"
				}
			case "QString":
				return "\"\""
		}
		if (feature.isEnum) {
			return "0"
		}
		return ""
	}

	def String defaultForLazyTypeName(String typeName) {
		switch (typeName) {
			case "int":
				return "-1"
			case "QString":
				return "\"\""
		}
		return ""
	}

	def boolean isArrayList(LFeature feature) {
		if (feature.isToMany) {
			switch (feature.toTypeName) {
				case "int":
					return true
				case "double":
					return true
				case "bool":
					return true
				case "QString":
					return true
			}
		}

		return false
	}

	def boolean isToMany(LFeature feature) {
		modelExtension.isToMany(feature)
	}

	def Bounds getBounds(LFeature feature) {
		modelExtension.getBounds(feature)
	}

	def boolean isOptional(LFeature feature) {
		return feature.bounds.isOptional;
	}

	def boolean isMandatory(LFeature feature) {
		return feature.bounds.isRequired;
	}

	def toValidate(LFeature feature) {
		switch (feature.toTypeName) {
			case "int":
				return '''
					if(m«feature.toName.toFirstUpper» == -1){
						return false;
					}
				'''.toString
			case "double":
				return '''
					if(m«feature.toName.toFirstUpper» == -1.0){
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
			case "QDate":
			return '''
					if(m«feature.toName.toFirstUpper».isNull() || !m«feature.toName.toFirstUpper».isValid())
					{
						return false;
					}
				'''.toString
			case "QTime":
			return '''
					if(m«feature.toName.toFirstUpper».isNull() || !m«feature.toName.toFirstUpper».isValid())
					{
						return false;
					}
				'''.toString
			case "QDateTime":
				return '''
					if(m«feature.toName.toFirstUpper».isNull() || !m«feature.toName.toFirstUpper».isValid())
					{
						return false;
					}
				'''.toString
		}
		return '''
			// missing validation for m«feature.toName.toFirstUpper»
		'''.toString
	}

	def toValidateReference(String referenceTypeName, String featureName) {
		switch (referenceTypeName) {
			case "int":
				return '''
					if(m«featureName.toFirstUpper» == -1){
						return false;
					}
				'''.toString
			case "double":
				return '''
					if(m«featureName.toFirstUpper» == -1.0){
						return false;
					}
				'''.toString
			case "QString":
				return '''
					if(m«featureName.toFirstUpper».isNull() || m«featureName.toFirstUpper».isEmpty())
					{
						return false;
					}
				'''.toString
		}
		return '''
			// missing validation for m«featureName.toFirstUpper»
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

	def boolean hasForeignPropertyName(LFeature feature) {
		if (feature.foreignPropertyNameValue == null) {
			return false
		}
		return true
	}

	def boolean hasEnumValues(LEnum en) {
		if (en.enumValues == null) {
			return false
		}
		return true
	}

	def dispatch LFeature referenceDomainKeyFeature(LFeature feature) {
		return feature
	}

	def dispatch LFeature referenceDomainKeyFeature(LDtoReference reference) {
		return (reference.type as LDto).domainKeyFeature
	}

	def LFeature domainKeyFeature(LDto dto) {
		for (feature : dto.allFeatures) {
			if (feature.isDomainKey) {
				return feature
			}
		}
	}

	def dispatch String referenceDomainKey(LFeature feature) {
		return ""
	}

	def dispatch String referenceDomainKey(LDtoReference reference) {
		return (reference.type as LDto).domainKey
	}

	def String domainKey(LDto dto) {
		for (feature : dto.allFeatures) {
			if (feature.isDomainKey) {
				return feature.toName
			}
		}
		return "uuid"
	}

	def dispatch String referenceDomainKeyType(LFeature feature) {
		return ""
	}

	def dispatch String referenceDomainKeyType(LDtoReference reference) {
		return (reference.type as LDto).domainKeyType
	}

	def String domainKeyType(LDto dto) {
		for (feature : dto.allFeatures) {
			if (feature.isDomainKey) {
				return feature.toTypeName
			}
		}
		return "QString"
	}

	def boolean existsForeignPropertyName(LDto dto) {
		for (feature : dto.allFeatures) {
			if (feature.hasForeignPropertyName) {
				return true
			}
		}
		return false
	}

	def boolean existsEnum(LDto dto) {
		for (feature : dto.allFeatures) {
			if (feature.isEnum) {
				return true
			}
		}
		return false
	}
	
	def boolean existsDates(LDto dto) {
		for (feature : dto.allFeatures) {
			if (feature.isTypeOfDates) {
				return true
			}
		}
		return false
	}

	def boolean existsLazy(LDto dto) {
		for (feature : dto.allFeatures) {
			if (feature.isLazy) {
				return true
			}
		}
		return false
	}
	
	def boolean existsTransient(LDto dto) {
		for (feature : dto.allFeatures) {
			if (feature.isTransient) {
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
	
	def String getDateFormatStrngValue(LFeature member) {
		val annoDef = typeof(DateFormatString).getRedefined(member.resolvedAnnotations)
		if (annoDef != null) {
			val JvmCustomAnnotationValue annotationValue = toJvmAnnotationValue(annoDef.annotation.value) as JvmCustomAnnotationValue;
			val XStringLiteral lit = annotationValue.values.get(0) as XStringLiteral
			return lit.value
		} else {
			return null
		}
	}

	def String getForeignPropertyNameValue(LFeature member) {
		val annoDef = typeof(ForeignPropertyName).getRedefined(member.resolvedAnnotations)
		if (annoDef != null) {
			val JvmCustomAnnotationValue annotationValue = toJvmAnnotationValue(annoDef.annotation.value) as JvmCustomAnnotationValue;
			val XStringLiteral lit = annotationValue.values.get(0) as XStringLiteral
			return lit.value
		} else {
			return null
		}
	}

	def String getEnumValues(LEnum member) {
		val annoDef = typeof(EnumValues).getRedefined(member.resolvedAnnotations)
		if (annoDef != null) {
			val JvmCustomAnnotationValue annotationValue = toJvmAnnotationValue(annoDef.annotation.value) as JvmCustomAnnotationValue;
			val XStringLiteral lit = annotationValue.values.get(0) as XStringLiteral
			return lit.value
		} else {
			return null
		}
	}

}
