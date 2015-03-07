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
 */
package org.lunifera.dsl.ext.dtos.cpp.qt

import com.google.inject.Inject
import java.util.Set
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.OutputConfiguration
import org.lunifera.dsl.semantic.dto.LDto
import org.lunifera.dsl.semantic.dto.LDtoModel
import org.lunifera.dsl.xtext.lazyresolver.api.hook.IGeneratorDelegate

class GeneratorDelegate implements IGeneratorDelegate {
	
	@Inject
	HppGenerator hppGenerator
	
	@Inject
	CppGenerator cppGenerator

	override generate(Resource input, IFileSystemAccess fsa) {
		val LDtoModel lModel = input.contents.get(0) as LDtoModel
		lModel.packages.forEach [
			types.map[it as LDto].forEach [
				it.generateHppFile(fsa)
				it.generateCppFile(fsa)
			]
		]
	}
	
	/**
	 * Generates the .hpp file for the given dto.
	 */
	def void generateHppFile(LDto lType, IFileSystemAccess fsa) {
		val fileName = lType.toHppFileName
		fsa.deleteFile(fileName)
		fsa.generateFile(fileName, "CppQt", lType.toHppContent)
	}

	def CharSequence toHppContent(LDto type) {
		hppGenerator.toContent(type)
	}

	def String toHppFileName(LDto type) {
		hppGenerator.toFileName(type)
	}

	/**
	 * Generates the .cpp file for the given dto.
	 */
	def void generateCppFile(LDto lType, IFileSystemAccess fsa) {
		val fileName = lType.toCppFileName
		fsa.deleteFile(fileName)
		fsa.generateFile(fileName, "CppQt", lType.toCppContent)
	}

	def CharSequence toCppContent(LDto type) {
		cppGenerator.toContent(type)
	}

	def String toCppFileName(LDto type) {
		cppGenerator.toFileName(type)
	}

	/** 
	 * Returns a new output configuration for the Cpp Qt files.
	 */
	override Set<OutputConfiguration> getOutputConfigurations() {
		val Set<OutputConfiguration> configs = newHashSet();

		val OutputConfiguration componentOutput = new OutputConfiguration("CppQt");
		componentOutput.description = "C++ Qt artifacts"
		componentOutput.outputDirectory = "./CppQt"
		componentOutput.overrideExistingResources = true
		componentOutput.createOutputDirectory = true
		componentOutput.cleanUpDerivedResources = true
		componentOutput.setDerivedProperty = true
		componentOutput.keepLocalHistory = true
		configs += componentOutput

		return configs
	}

}
