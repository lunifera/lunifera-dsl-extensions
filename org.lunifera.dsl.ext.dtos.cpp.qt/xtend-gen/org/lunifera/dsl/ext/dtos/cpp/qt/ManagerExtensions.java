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
package org.lunifera.dsl.ext.dtos.cpp.qt;

import com.google.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder;
import org.eclipse.xtext.xbase.lib.Extension;
import org.lunifera.dsl.dto.xtext.extensions.AnnotationExtension;
import org.lunifera.dsl.dto.xtext.extensions.DtoModelExtensions;
import org.lunifera.dsl.ext.dtos.cpp.qt.CppExtensions;
import org.lunifera.dsl.semantic.common.types.LType;
import org.lunifera.dsl.semantic.common.types.LTypedPackage;
import org.lunifera.dsl.semantic.dto.LDto;

@SuppressWarnings("all")
public class ManagerExtensions {
  @Inject
  @Extension
  private JvmTypesBuilder _jvmTypesBuilder;
  
  @Inject
  private DtoModelExtensions modelExtension;
  
  @Inject
  @Extension
  private AnnotationExtension _annotationExtension;
  
  @Inject
  @Extension
  private CppExtensions _cppExtensions;
  
  public boolean hasGeoCoordinate(final LTypedPackage pkg) {
    EList<LType> _types = pkg.getTypes();
    for (final LType lt : _types) {
      if ((lt instanceof LDto)) {
        boolean _existsGeoCoordinate = this._cppExtensions.existsGeoCoordinate(((LDto)lt));
        if (_existsGeoCoordinate) {
          return true;
        }
      }
    }
    return false;
  }
  
  public boolean hasGeoAddress(final LTypedPackage pkg) {
    EList<LType> _types = pkg.getTypes();
    for (final LType lt : _types) {
      if ((lt instanceof LDto)) {
        boolean _existsGeoAddress = this._cppExtensions.existsGeoAddress(((LDto)lt));
        if (_existsGeoAddress) {
          return true;
        }
      }
    }
    return false;
  }
  
  public boolean hasGeo(final LTypedPackage pkg) {
    boolean _or = false;
    boolean _hasGeoCoordinate = this.hasGeoCoordinate(pkg);
    if (_hasGeoCoordinate) {
      _or = true;
    } else {
      boolean _hasGeoAddress = this.hasGeoAddress(pkg);
      _or = _hasGeoAddress;
    }
    if (_or) {
      return true;
    }
    return false;
  }
}
