package org.lunifera.dsl.ext.dtos.cpp.qt

import com.google.inject.Inject
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder
import org.lunifera.dsl.dto.xtext.extensions.AnnotationExtension
import org.lunifera.dsl.dto.xtext.extensions.DtoModelExtensions
import org.lunifera.dsl.semantic.common.types.LAnnotationTarget
import org.lunifera.dsl.semantic.common.types.LAttribute
import org.lunifera.dsl.semantic.common.types.LFeature
import org.lunifera.dsl.semantic.common.types.LReference
import org.lunifera.dsl.semantic.dto.LDtoAbstractAttribute
import org.lunifera.dsl.semantic.dto.LDtoAbstractReference
import org.lunifera.dsl.semantic.common.types.LAnnotationDef
import org.lunifera.dsl.ext.cpp.qt.lib.types.annotation.SimpleName

class CppExtensions {

	@Inject extension JvmTypesBuilder
	@Inject DtoModelExtensions modelExtension
	@Inject extension AnnotationExtension

	def dispatch String toName(LAnnotationTarget target) {
		modelExtension.toName(target)
	}
	
	def dispatch String toName(LAttribute target) {
//		val anno = target.simpleNameAnno
//		if(anno != null){
//			return anno.annotation.
//		}
		modelExtension.toName(target)
	}
	
	def dispatch String toName(LReference target) {
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
		return "String"
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
	
//	def LAnnotationDef getSimpleNameAnno(LFeature member) {
//		val annoDef = typeof(SimpleName).getRedefined(member.annotations)
//		annoDef.annotation.
//	}

}
