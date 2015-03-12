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

import com.google.common.base.Objects;
import com.google.inject.Inject;
import java.util.Arrays;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.lunifera.dsl.dto.xtext.extensions.AnnotationExtension;
import org.lunifera.dsl.dto.xtext.extensions.DtoModelExtensions;
import org.lunifera.dsl.semantic.common.types.LAnnotationTarget;
import org.lunifera.dsl.semantic.common.types.LAttribute;
import org.lunifera.dsl.semantic.common.types.LFeature;
import org.lunifera.dsl.semantic.common.types.LReference;
import org.lunifera.dsl.semantic.dto.LDtoAbstractAttribute;
import org.lunifera.dsl.semantic.dto.LDtoAbstractReference;

@SuppressWarnings("all")
public class CppExtensions {
  @Inject
  @Extension
  private JvmTypesBuilder _jvmTypesBuilder;
  
  @Inject
  private DtoModelExtensions modelExtension;
  
  @Inject
  @Extension
  private AnnotationExtension _annotationExtension;
  
  protected String _toName(final LAnnotationTarget target) {
    return this.modelExtension.toName(target);
  }
  
  protected String _toName(final LAttribute target) {
    return this.modelExtension.toName(target);
  }
  
  protected String _toName(final LReference target) {
    return this.modelExtension.toName(target);
  }
  
  protected String _toTypeName(final LAttribute att) {
    return this.modelExtension.toTypeName(((LDtoAbstractAttribute) att));
  }
  
  protected String _toTypeName(final LReference ref) {
    return this.modelExtension.toTypeName(((LDtoAbstractReference) ref));
  }
  
  public String mapToType(final LFeature feature) {
    String _typeName = this.toTypeName(feature);
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_typeName, "bool")) {
        _matched=true;
        return "Bool";
      }
    }
    if (!_matched) {
      if (Objects.equal(_typeName, "int")) {
        _matched=true;
        return "Int";
      }
    }
    if (!_matched) {
      if (Objects.equal(_typeName, "QString")) {
        _matched=true;
        return "String";
      }
    }
    boolean _isToMany = this.isToMany(feature);
    if (_isToMany) {
      return "List";
    }
    return "Map";
  }
  
  public String defaultForType(final LFeature feature) {
    String _typeName = this.toTypeName(feature);
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_typeName, "bool")) {
        _matched=true;
        return "false";
      }
    }
    if (!_matched) {
      if (Objects.equal(_typeName, "int")) {
        _matched=true;
        return "0";
      }
    }
    if (!_matched) {
      if (Objects.equal(_typeName, "QString")) {
        _matched=true;
        return "\"\"";
      }
    }
    return "";
  }
  
  public boolean isToMany(final LFeature feature) {
    return this.modelExtension.isToMany(feature);
  }
  
  public String toCopyRight(final EObject element) {
    String docu = this._jvmTypesBuilder.getDocumentation(element);
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(docu);
    boolean _not = (!_isNullOrEmpty);
    if (_not) {
      String[] docus = docu.split("\n");
      int _length = docus.length;
      boolean _greaterThan = (_length > 1);
      if (_greaterThan) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("/**");
        _builder.newLine();
        {
          for(final String line : docus) {
            _builder.append(" * ", "");
            _builder.append(line, "");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append(" ");
        _builder.append("*/");
        return _builder.toString();
      } else {
        int _length_1 = docus.length;
        boolean _equals = (_length_1 == 1);
        if (_equals) {
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("/** ");
          _builder_1.append(docu, "");
          _builder_1.append(" */");
          return _builder_1.toString();
        }
      }
    }
    return "";
  }
  
  public String toName(final LAnnotationTarget target) {
    if (target instanceof LAttribute) {
      return _toName((LAttribute)target);
    } else if (target instanceof LReference) {
      return _toName((LReference)target);
    } else if (target != null) {
      return _toName(target);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(target).toString());
    }
  }
  
  public String toTypeName(final LFeature att) {
    if (att instanceof LAttribute) {
      return _toTypeName((LAttribute)att);
    } else if (att instanceof LReference) {
      return _toTypeName((LReference)att);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(att).toString());
    }
  }
}
