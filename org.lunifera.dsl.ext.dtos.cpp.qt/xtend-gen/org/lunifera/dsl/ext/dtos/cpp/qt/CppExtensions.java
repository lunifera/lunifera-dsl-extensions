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
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmAnnotationValue;
import org.eclipse.xtext.common.types.JvmCustomAnnotationValue;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.XStringLiteral;
import org.eclipse.xtext.xbase.annotations.xAnnotations.XAnnotation;
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.lunifera.dsl.dto.xtext.extensions.AnnotationExtension;
import org.lunifera.dsl.dto.xtext.extensions.DtoModelExtensions;
import org.lunifera.dsl.ext.cpp.qt.lib.types.annotation.ServerName;
import org.lunifera.dsl.semantic.common.helper.Bounds;
import org.lunifera.dsl.semantic.common.types.LAnnotationDef;
import org.lunifera.dsl.semantic.common.types.LAnnotationTarget;
import org.lunifera.dsl.semantic.common.types.LAttribute;
import org.lunifera.dsl.semantic.common.types.LFeature;
import org.lunifera.dsl.semantic.common.types.LReference;
import org.lunifera.dsl.semantic.dto.LDto;
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
  
  public String toName(final LAnnotationTarget target) {
    return this.modelExtension.toName(target);
  }
  
  protected String _toServerName(final LAnnotationTarget target) {
    return this.modelExtension.toName(target);
  }
  
  protected String _toServerName(final LAttribute target) {
    String _xblockexpression = null;
    {
      final String value = this.getServerNameValue(target);
      boolean _notEquals = (!Objects.equal(value, null));
      if (_notEquals) {
        return value;
      }
      _xblockexpression = this.modelExtension.toName(target);
    }
    return _xblockexpression;
  }
  
  protected String _toServerName(final LReference target) {
    String _xblockexpression = null;
    {
      final String value = this.getServerNameValue(target);
      boolean _notEquals = (!Objects.equal(value, null));
      if (_notEquals) {
        return value;
      }
      _xblockexpression = this.modelExtension.toName(target);
    }
    return _xblockexpression;
  }
  
  protected boolean _isContained(final LAnnotationTarget target) {
    return false;
  }
  
  protected boolean _isContained(final LAttribute target) {
    return false;
  }
  
  protected boolean _isContained(final LReference target) {
    return target.isCascading();
  }
  
  protected boolean _isDomainKey(final LAnnotationTarget target) {
    return false;
  }
  
  protected boolean _isDomainKey(final LAttribute target) {
    return target.isDomainKey();
  }
  
  protected boolean _isDomainKey(final LReference target) {
    return false;
  }
  
  protected boolean _isTransient(final LAnnotationTarget target) {
    return false;
  }
  
  protected boolean _isTransient(final LAttribute target) {
    return target.isTransient();
  }
  
  protected boolean _isTransient(final LReference target) {
    return false;
  }
  
  protected String _toTypeName(final LAttribute att) {
    return this.modelExtension.toTypeName(((LDtoAbstractAttribute) att));
  }
  
  protected String _toTypeName(final LReference ref) {
    return this.modelExtension.toTypeName(((LDtoAbstractReference) ref));
  }
  
  public boolean isTypeOfDTO(final LFeature feature) {
    String _typeName = this.toTypeName(feature);
    boolean _endsWith = _typeName.endsWith("DTO");
    if (_endsWith) {
      return true;
    }
    return false;
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
        boolean _or = false;
        boolean _isMandatory = this.isMandatory(feature);
        if (_isMandatory) {
          _or = true;
        } else {
          boolean _isDomainKey = this.isDomainKey(feature);
          _or = _isDomainKey;
        }
        if (_or) {
          return "-1";
        } else {
          return "0";
        }
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
  
  public Bounds getBounds(final LFeature feature) {
    return this.modelExtension.getBounds(feature);
  }
  
  public boolean isOptional(final LFeature feature) {
    Bounds _bounds = this.getBounds(feature);
    return _bounds.isOptional();
  }
  
  public boolean isMandatory(final LFeature feature) {
    Bounds _bounds = this.getBounds(feature);
    return _bounds.isRequired();
  }
  
  public String toValidate(final LFeature feature) {
    String _typeName = this.toTypeName(feature);
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_typeName, "int")) {
        _matched=true;
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("if(m");
        String _name = this.toName(feature);
        String _firstUpper = StringExtensions.toFirstUpper(_name);
        _builder.append(_firstUpper, "");
        _builder.append(" == -1){");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("return false;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        return _builder.toString();
      }
    }
    if (!_matched) {
      if (Objects.equal(_typeName, "QString")) {
        _matched=true;
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("if(m");
        String _name_1 = this.toName(feature);
        String _firstUpper_1 = StringExtensions.toFirstUpper(_name_1);
        _builder_1.append(_firstUpper_1, "");
        _builder_1.append(".isNull() || m");
        String _name_2 = this.toName(feature);
        String _firstUpper_2 = StringExtensions.toFirstUpper(_name_2);
        _builder_1.append(_firstUpper_2, "");
        _builder_1.append(".isEmpty())");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("{");
        _builder_1.newLine();
        _builder_1.append("\t");
        _builder_1.append("return false;");
        _builder_1.newLine();
        _builder_1.append("}");
        _builder_1.newLine();
        return _builder_1.toString();
      }
    }
    StringConcatenation _builder_2 = new StringConcatenation();
    _builder_2.append("// missing validation for m");
    String _name_3 = this.toName(feature);
    String _firstUpper_3 = StringExtensions.toFirstUpper(_name_3);
    _builder_2.append(_firstUpper_3, "");
    _builder_2.newLineIfNotEmpty();
    return _builder_2.toString();
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
  
  public boolean isQueueObject(final LDto element) {
    String docu = this._jvmTypesBuilder.getDocumentation(element);
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(docu);
    boolean _not = (!_isNullOrEmpty);
    if (_not) {
      boolean _equals = Objects.equal(docu, "QUEUE");
      if (_equals) {
        return true;
      }
    }
    return false;
  }
  
  public String toDtoDocu(final LDto element) {
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
  
  public boolean hasServerName(final LFeature feature) {
    String _serverNameValue = this.getServerNameValue(feature);
    boolean _equals = Objects.equal(_serverNameValue, null);
    if (_equals) {
      return false;
    }
    return true;
  }
  
  public boolean existsServerName(final LDto dto) {
    List<? extends LFeature> _allFeatures = dto.getAllFeatures();
    for (final LFeature feature : _allFeatures) {
      boolean _hasServerName = this.hasServerName(feature);
      if (_hasServerName) {
        return true;
      }
    }
    return false;
  }
  
  public String toTypeOrQObject(final LFeature feature) {
    String _typeName = this.toTypeName(feature);
    boolean _endsWith = _typeName.endsWith("DTO");
    if (_endsWith) {
      StringConcatenation _builder = new StringConcatenation();
      String _typeName_1 = this.toTypeName(feature);
      _builder.append(_typeName_1, "");
      _builder.append("*");
      return _builder.toString();
    }
    return this.toTypeName(feature);
  }
  
  public String toMapOrList(final LFeature feature) {
    boolean _isToMany = this.isToMany(feature);
    if (_isToMany) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("List");
      return _builder.toString();
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("Map");
    return _builder_1.toString();
  }
  
  public String getServerNameValue(final LFeature member) {
    EList<LAnnotationDef> _resolvedAnnotations = member.getResolvedAnnotations();
    final LAnnotationDef annoDef = this._annotationExtension.getRedefined(ServerName.class, _resolvedAnnotations);
    boolean _notEquals = (!Objects.equal(annoDef, null));
    if (_notEquals) {
      XAnnotation _annotation = annoDef.getAnnotation();
      XExpression _value = _annotation.getValue();
      JvmAnnotationValue _jvmAnnotationValue = this._jvmTypesBuilder.toJvmAnnotationValue(_value);
      final JvmCustomAnnotationValue annotationValue = ((JvmCustomAnnotationValue) _jvmAnnotationValue);
      EList<Object> _values = annotationValue.getValues();
      Object _get = _values.get(0);
      final XStringLiteral lit = ((XStringLiteral) _get);
      return lit.getValue();
    } else {
      return null;
    }
  }
  
  public String toServerName(final LAnnotationTarget target) {
    if (target instanceof LAttribute) {
      return _toServerName((LAttribute)target);
    } else if (target instanceof LReference) {
      return _toServerName((LReference)target);
    } else if (target != null) {
      return _toServerName(target);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(target).toString());
    }
  }
  
  public boolean isContained(final LAnnotationTarget target) {
    if (target instanceof LAttribute) {
      return _isContained((LAttribute)target);
    } else if (target instanceof LReference) {
      return _isContained((LReference)target);
    } else if (target != null) {
      return _isContained(target);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(target).toString());
    }
  }
  
  public boolean isDomainKey(final LAnnotationTarget target) {
    if (target instanceof LAttribute) {
      return _isDomainKey((LAttribute)target);
    } else if (target instanceof LReference) {
      return _isDomainKey((LReference)target);
    } else if (target != null) {
      return _isDomainKey(target);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(target).toString());
    }
  }
  
  public boolean isTransient(final LAnnotationTarget target) {
    if (target instanceof LAttribute) {
      return _isTransient((LAttribute)target);
    } else if (target instanceof LReference) {
      return _isTransient((LReference)target);
    } else if (target != null) {
      return _isTransient(target);
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
