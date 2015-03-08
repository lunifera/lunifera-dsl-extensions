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
package org.lunifera.dsl.ext.dtos.cpp.qt;

import com.google.inject.Inject;
import java.util.Set;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.OutputConfiguration;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.lunifera.dsl.ext.dtos.cpp.qt.CppGenerator;
import org.lunifera.dsl.ext.dtos.cpp.qt.HppGenerator;
import org.lunifera.dsl.semantic.common.types.LType;
import org.lunifera.dsl.semantic.common.types.LTypedPackage;
import org.lunifera.dsl.semantic.dto.LDto;
import org.lunifera.dsl.semantic.dto.LDtoModel;
import org.lunifera.dsl.xtext.lazyresolver.api.hook.IGeneratorDelegate;

@SuppressWarnings("all")
public class GeneratorDelegate implements IGeneratorDelegate {
  @Inject
  private HppGenerator hppGenerator;
  
  @Inject
  private CppGenerator cppGenerator;
  
  public void generate(final Resource input, final IFileSystemAccess fsa) {
    EList<EObject> _contents = input.getContents();
    boolean _isEmpty = _contents.isEmpty();
    if (_isEmpty) {
      return;
    }
    EList<EObject> _contents_1 = input.getContents();
    EObject _get = _contents_1.get(0);
    final LDtoModel lModel = ((LDtoModel) _get);
    EList<LTypedPackage> _packages = lModel.getPackages();
    final Procedure1<LTypedPackage> _function = new Procedure1<LTypedPackage>() {
      public void apply(final LTypedPackage it) {
        EList<LType> _types = it.getTypes();
        final Function1<LType, Boolean> _function = new Function1<LType, Boolean>() {
          public Boolean apply(final LType it) {
            return Boolean.valueOf((it instanceof LDto));
          }
        };
        Iterable<LType> _filter = IterableExtensions.<LType>filter(_types, _function);
        final Function1<LType, LDto> _function_1 = new Function1<LType, LDto>() {
          public LDto apply(final LType it) {
            return ((LDto) it);
          }
        };
        Iterable<LDto> _map = IterableExtensions.<LType, LDto>map(_filter, _function_1);
        final Procedure1<LDto> _function_2 = new Procedure1<LDto>() {
          public void apply(final LDto it) {
            GeneratorDelegate.this.generateHppFile(it, fsa);
            GeneratorDelegate.this.generateCppFile(it, fsa);
          }
        };
        IterableExtensions.<LDto>forEach(_map, _function_2);
      }
    };
    IterableExtensions.<LTypedPackage>forEach(_packages, _function);
  }
  
  /**
   * Generates the .hpp file for the given dto.
   */
  public void generateHppFile(final LDto lType, final IFileSystemAccess fsa) {
    final String fileName = this.toHppFileName(lType);
    fsa.deleteFile(fileName);
    CharSequence _hppContent = this.toHppContent(lType);
    fsa.generateFile(fileName, "CppQt", _hppContent);
  }
  
  public CharSequence toHppContent(final LDto type) {
    return this.hppGenerator.toContent(type);
  }
  
  public String toHppFileName(final LDto type) {
    return this.hppGenerator.toFileName(type);
  }
  
  /**
   * Generates the .cpp file for the given dto.
   */
  public void generateCppFile(final LDto lType, final IFileSystemAccess fsa) {
    final String fileName = this.toCppFileName(lType);
    fsa.deleteFile(fileName);
    CharSequence _cppContent = this.toCppContent(lType);
    fsa.generateFile(fileName, "CppQt", _cppContent);
  }
  
  public CharSequence toCppContent(final LDto type) {
    return this.cppGenerator.toContent(type);
  }
  
  public String toCppFileName(final LDto type) {
    return this.cppGenerator.toFileName(type);
  }
  
  /**
   * Returns a new output configuration for the Cpp Qt files.
   */
  public Set<OutputConfiguration> getOutputConfigurations() {
    final Set<OutputConfiguration> configs = CollectionLiterals.<OutputConfiguration>newHashSet();
    final OutputConfiguration componentOutput = new OutputConfiguration("CppQt");
    componentOutput.setDescription("C++ Qt artifacts");
    componentOutput.setOutputDirectory("./CppQt");
    componentOutput.setOverrideExistingResources(true);
    componentOutput.setCreateOutputDirectory(true);
    componentOutput.setCleanUpDerivedResources(true);
    componentOutput.setSetDerivedProperty(true);
    componentOutput.setKeepLocalHistory(Boolean.valueOf(true));
    configs.add(componentOutput);
    return configs;
  }
}
