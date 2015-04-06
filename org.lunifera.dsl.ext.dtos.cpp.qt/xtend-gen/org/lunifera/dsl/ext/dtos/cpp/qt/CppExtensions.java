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
import org.lunifera.dsl.ext.cpp.qt.lib.types.annotation.CachePolicy;
import org.lunifera.dsl.ext.cpp.qt.lib.types.annotation.DateFormatString;
import org.lunifera.dsl.ext.cpp.qt.lib.types.annotation.EnumValues;
import org.lunifera.dsl.ext.cpp.qt.lib.types.annotation.ForeignPropertyName;
import org.lunifera.dsl.semantic.common.helper.Bounds;
import org.lunifera.dsl.semantic.common.types.LAnnotationDef;
import org.lunifera.dsl.semantic.common.types.LAnnotationTarget;
import org.lunifera.dsl.semantic.common.types.LAttribute;
import org.lunifera.dsl.semantic.common.types.LEnum;
import org.lunifera.dsl.semantic.common.types.LEnumLiteral;
import org.lunifera.dsl.semantic.common.types.LFeature;
import org.lunifera.dsl.semantic.common.types.LReference;
import org.lunifera.dsl.semantic.common.types.LScalarType;
import org.lunifera.dsl.semantic.dto.LDto;
import org.lunifera.dsl.semantic.dto.LDtoAbstractAttribute;
import org.lunifera.dsl.semantic.dto.LDtoAbstractReference;
import org.lunifera.dsl.semantic.dto.LDtoReference;

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
  
  protected String _toForeignPropertyName(final LAnnotationTarget target) {
    return this.modelExtension.toName(target);
  }
  
  protected String _toForeignPropertyName(final LAttribute target) {
    String _xblockexpression = null;
    {
      final String value = this.getForeignPropertyNameValue(target);
      boolean _notEquals = (!Objects.equal(value, null));
      if (_notEquals) {
        return value;
      }
      _xblockexpression = this.modelExtension.toName(target);
    }
    return _xblockexpression;
  }
  
  protected String _toForeignPropertyName(final LReference target) {
    String _xblockexpression = null;
    {
      final String value = this.getForeignPropertyNameValue(target);
      boolean _notEquals = (!Objects.equal(value, null));
      if (_notEquals) {
        return value;
      }
      _xblockexpression = this.modelExtension.toName(target);
    }
    return _xblockexpression;
  }
  
  protected String _toDateFormatString(final LAnnotationTarget target) {
    return "";
  }
  
  protected String _toDateFormatString(final LAttribute target) {
    final String value = this.getDateFormatStrngValue(target);
    boolean _notEquals = (!Objects.equal(value, null));
    if (_notEquals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("\"");
      _builder.append(value, "");
      _builder.append("\"");
      return _builder.toString();
    }
    return "Qt::ISODate";
  }
  
  protected String _toDateFormatString(final LReference target) {
    return "";
  }
  
  protected String _toEnumValues(final LAttribute target) {
    return "";
  }
  
  protected String _toEnumValues(final LEnum target) {
    final String values = this.getEnumValues(target);
    boolean _notEquals = (!Objects.equal(values, null));
    if (_notEquals) {
      return values;
    }
    return "";
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
  
  protected boolean _isContained(final LDtoReference target) {
    return target.isCascading();
  }
  
  protected boolean _hasOpposite(final LDtoAbstractAttribute att) {
    return false;
  }
  
  protected boolean _hasOpposite(final LDtoAbstractReference ref) {
    return false;
  }
  
  protected boolean _hasOpposite(final LDtoReference ref) {
    LDtoReference _opposite = ref.getOpposite();
    return (!Objects.equal(_opposite, null));
  }
  
  protected boolean _hasOpposite(final LFeature feature) {
    return false;
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
  
  protected boolean _isLazy(final LAnnotationTarget target) {
    return false;
  }
  
  protected boolean _isLazy(final LAttribute target) {
    return false;
  }
  
  protected boolean _isLazy(final LReference target) {
    return target.isLazy();
  }
  
  protected String _toTypeName(final LAttribute att) {
    String _typeName = this.modelExtension.toTypeName(((LDtoAbstractAttribute) att));
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_typeName, "Date")) {
        _matched=true;
        return "QDate";
      }
    }
    if (!_matched) {
      if (Objects.equal(_typeName, "Time")) {
        _matched=true;
        return "QTime";
      }
    }
    if (!_matched) {
      if (Objects.equal(_typeName, "Timestamp")) {
        _matched=true;
        return "QDateTime";
      }
    }
    return this.modelExtension.toTypeName(((LDtoAbstractAttribute) att));
  }
  
  protected String _toTypeName(final LReference ref) {
    return this.modelExtension.toTypeName(((LDtoAbstractReference) ref));
  }
  
  protected boolean _isTypeOfDataObject(final LDtoAbstractAttribute att) {
    LScalarType _type = att.getType();
    if ((_type instanceof LDto)) {
      return true;
    }
    return false;
  }
  
  protected boolean _isTypeOfDataObject(final LDtoAbstractReference ref) {
    LDto _type = ref.getType();
    if ((_type instanceof LDto)) {
      return true;
    }
    return false;
  }
  
  protected boolean _isTypeOfDataObject(final LFeature feature) {
    return false;
  }
  
  public boolean isTypeOfDates(final LFeature feature) {
    boolean _or = false;
    boolean _or_1 = false;
    String _typeName = this.toTypeName(feature);
    boolean _equals = Objects.equal(_typeName, "QDate");
    if (_equals) {
      _or_1 = true;
    } else {
      String _typeName_1 = this.toTypeName(feature);
      boolean _equals_1 = Objects.equal(_typeName_1, "QTime");
      _or_1 = _equals_1;
    }
    if (_or_1) {
      _or = true;
    } else {
      String _typeName_2 = this.toTypeName(feature);
      boolean _equals_2 = Objects.equal(_typeName_2, "QDateTime");
      _or = _equals_2;
    }
    if (_or) {
      return true;
    }
    return false;
  }
  
  protected boolean _isEnum(final LDtoAbstractAttribute att) {
    boolean _typeIsEnum = this.modelExtension.typeIsEnum(att);
    if (_typeIsEnum) {
      return true;
    }
    return false;
  }
  
  protected boolean _isEnum(final LFeature feature) {
    return false;
  }
  
  protected LEnum _enumFromAttributeType(final LDtoAbstractAttribute att) {
    LScalarType _type = att.getType();
    return ((LEnum) _type);
  }
  
  protected LEnum _enumFromAttributeType(final LFeature feature) {
    return null;
  }
  
  public int enumIndex(final LEnum en, final LEnumLiteral lit) {
    for (int i = 0; (i < en.getLiterals().size()); i++) {
      EList<LEnumLiteral> _literals = en.getLiterals();
      LEnumLiteral _get = _literals.get(i);
      boolean _equals = Objects.equal(lit, _get);
      if (_equals) {
        return i;
      }
    }
    return (-1);
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
        boolean _isToMany = this.isToMany(feature);
        if (_isToMany) {
          return "List";
        } else {
          return "Int";
        }
      }
    }
    if (!_matched) {
      if (Objects.equal(_typeName, "double")) {
        _matched=true;
        boolean _isToMany_1 = this.isToMany(feature);
        if (_isToMany_1) {
          return "List";
        } else {
          return "Double";
        }
      }
    }
    if (!_matched) {
      if (Objects.equal(_typeName, "QString")) {
        _matched=true;
        boolean _isToMany_2 = this.isToMany(feature);
        if (_isToMany_2) {
          return "StringList";
        } else {
          return "String";
        }
      }
    }
    if (!_matched) {
      if (Objects.equal(_typeName, "QDate")) {
        _matched=true;
        boolean _isToMany_3 = this.isToMany(feature);
        if (_isToMany_3) {
          return "List";
        } else {
          return "Date";
        }
      }
    }
    if (!_matched) {
      if (Objects.equal(_typeName, "QTime")) {
        _matched=true;
        boolean _isToMany_4 = this.isToMany(feature);
        if (_isToMany_4) {
          return "List";
        } else {
          return "Time";
        }
      }
    }
    if (!_matched) {
      if (Objects.equal(_typeName, "QDateTime")) {
        _matched=true;
        boolean _isToMany_5 = this.isToMany(feature);
        if (_isToMany_5) {
          return "List";
        } else {
          return "DateTime";
        }
      }
    }
    boolean _isToMany_6 = this.isToMany(feature);
    if (_isToMany_6) {
      return "List";
    }
    boolean _isEnum = this.isEnum(feature);
    if (_isEnum) {
      return "Int";
    }
    return "Map";
  }
  
  public String mapToSingleType(final LFeature feature) {
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
      if (Objects.equal(_typeName, "double")) {
        _matched=true;
        return "Double";
      }
    }
    if (!_matched) {
      if (Objects.equal(_typeName, "QString")) {
        _matched=true;
        return "String";
      }
    }
    if (!_matched) {
      if (Objects.equal(_typeName, "QDate")) {
        _matched=true;
        return "Date";
      }
    }
    if (!_matched) {
      if (Objects.equal(_typeName, "QTime")) {
        _matched=true;
        return "Time";
      }
    }
    if (!_matched) {
      if (Objects.equal(_typeName, "QDateTime")) {
        _matched=true;
        return "DateTime";
      }
    }
    return "Map";
  }
  
  public String mapToLazyTypeName(final String typeName) {
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(typeName, "int")) {
        _matched=true;
        return "Int";
      }
    }
    if (!_matched) {
      if (Objects.equal(typeName, "QString")) {
        _matched=true;
        return "String";
      }
    }
    return "String";
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
      if (Objects.equal(_typeName, "double")) {
        _matched=true;
        boolean _or_1 = false;
        boolean _isMandatory_1 = this.isMandatory(feature);
        if (_isMandatory_1) {
          _or_1 = true;
        } else {
          boolean _isDomainKey_1 = this.isDomainKey(feature);
          _or_1 = _isDomainKey_1;
        }
        if (_or_1) {
          return "-1.0";
        } else {
          return "0.0";
        }
      }
    }
    if (!_matched) {
      if (Objects.equal(_typeName, "QString")) {
        _matched=true;
        return "\"\"";
      }
    }
    boolean _isEnum = this.isEnum(feature);
    if (_isEnum) {
      return "0";
    }
    return "";
  }
  
  public String defaultForLazyTypeName(final String typeName) {
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(typeName, "int")) {
        _matched=true;
        return "-1";
      }
    }
    if (!_matched) {
      if (Objects.equal(typeName, "QString")) {
        _matched=true;
        return "\"\"";
      }
    }
    return "";
  }
  
  public boolean isArrayList(final LFeature feature) {
    boolean _isToMany = this.isToMany(feature);
    if (_isToMany) {
      String _typeName = this.toTypeName(feature);
      boolean _matched = false;
      if (!_matched) {
        if (Objects.equal(_typeName, "int")) {
          _matched=true;
          return true;
        }
      }
      if (!_matched) {
        if (Objects.equal(_typeName, "double")) {
          _matched=true;
          return true;
        }
      }
      if (!_matched) {
        if (Objects.equal(_typeName, "bool")) {
          _matched=true;
          return true;
        }
      }
      if (!_matched) {
        if (Objects.equal(_typeName, "QString")) {
          _matched=true;
          return true;
        }
      }
    }
    return false;
  }
  
  public boolean isToMany(final LFeature feature) {
    return this.modelExtension.isToMany(feature);
  }
  
  public Bounds getBounds(final LFeature feature) {
    return this.modelExtension.getBounds(feature);
  }
  
  public boolean isOptional(final LFeature feature) {
    boolean _and = false;
    Bounds _bounds = this.getBounds(feature);
    boolean _isOptional = _bounds.isOptional();
    if (!_isOptional) {
      _and = false;
    } else {
      boolean _isDomainKey = this.isDomainKey(feature);
      boolean _not = (!_isDomainKey);
      _and = _not;
    }
    return _and;
  }
  
  public boolean isMandatory(final LFeature feature) {
    boolean _or = false;
    Bounds _bounds = this.getBounds(feature);
    boolean _isRequired = _bounds.isRequired();
    if (_isRequired) {
      _or = true;
    } else {
      boolean _isDomainKey = this.isDomainKey(feature);
      _or = _isDomainKey;
    }
    return _or;
  }
  
  public String toValidate(final LFeature feature) {
    String _typeName = this.toTypeName(feature);
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_typeName, "int")) {
        _matched=true;
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("if (m");
        String _name = this.toName(feature);
        String _firstUpper = StringExtensions.toFirstUpper(_name);
        _builder.append(_firstUpper, "");
        _builder.append(" == -1) {");
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
      if (Objects.equal(_typeName, "double")) {
        _matched=true;
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("if (m");
        String _name_1 = this.toName(feature);
        String _firstUpper_1 = StringExtensions.toFirstUpper(_name_1);
        _builder_1.append(_firstUpper_1, "");
        _builder_1.append(" == -1.0) {");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("\t");
        _builder_1.append("return false;");
        _builder_1.newLine();
        _builder_1.append("}");
        _builder_1.newLine();
        return _builder_1.toString();
      }
    }
    if (!_matched) {
      if (Objects.equal(_typeName, "QString")) {
        _matched=true;
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("if (m");
        String _name_2 = this.toName(feature);
        String _firstUpper_2 = StringExtensions.toFirstUpper(_name_2);
        _builder_2.append(_firstUpper_2, "");
        _builder_2.append(".isNull() || m");
        String _name_3 = this.toName(feature);
        String _firstUpper_3 = StringExtensions.toFirstUpper(_name_3);
        _builder_2.append(_firstUpper_3, "");
        _builder_2.append(".isEmpty()) {");
        _builder_2.newLineIfNotEmpty();
        _builder_2.append("\t");
        _builder_2.append("return false;");
        _builder_2.newLine();
        _builder_2.append("}");
        _builder_2.newLine();
        return _builder_2.toString();
      }
    }
    if (!_matched) {
      if (Objects.equal(_typeName, "QDate")) {
        _matched=true;
        StringConcatenation _builder_3 = new StringConcatenation();
        _builder_3.append("if (m");
        String _name_4 = this.toName(feature);
        String _firstUpper_4 = StringExtensions.toFirstUpper(_name_4);
        _builder_3.append(_firstUpper_4, "");
        _builder_3.append(".isNull() || !m");
        String _name_5 = this.toName(feature);
        String _firstUpper_5 = StringExtensions.toFirstUpper(_name_5);
        _builder_3.append(_firstUpper_5, "");
        _builder_3.append(".isValid()) {");
        _builder_3.newLineIfNotEmpty();
        _builder_3.append("\t");
        _builder_3.append("return false;");
        _builder_3.newLine();
        _builder_3.append("}");
        _builder_3.newLine();
        return _builder_3.toString();
      }
    }
    if (!_matched) {
      if (Objects.equal(_typeName, "QTime")) {
        _matched=true;
        StringConcatenation _builder_4 = new StringConcatenation();
        _builder_4.append("if (m");
        String _name_6 = this.toName(feature);
        String _firstUpper_6 = StringExtensions.toFirstUpper(_name_6);
        _builder_4.append(_firstUpper_6, "");
        _builder_4.append(".isNull() || !m");
        String _name_7 = this.toName(feature);
        String _firstUpper_7 = StringExtensions.toFirstUpper(_name_7);
        _builder_4.append(_firstUpper_7, "");
        _builder_4.append(".isValid()) {");
        _builder_4.newLineIfNotEmpty();
        _builder_4.append("\t");
        _builder_4.append("return false;");
        _builder_4.newLine();
        _builder_4.append("}");
        _builder_4.newLine();
        return _builder_4.toString();
      }
    }
    if (!_matched) {
      if (Objects.equal(_typeName, "QDateTime")) {
        _matched=true;
        StringConcatenation _builder_5 = new StringConcatenation();
        _builder_5.append("if (m");
        String _name_8 = this.toName(feature);
        String _firstUpper_8 = StringExtensions.toFirstUpper(_name_8);
        _builder_5.append(_firstUpper_8, "");
        _builder_5.append(".isNull() || !m");
        String _name_9 = this.toName(feature);
        String _firstUpper_9 = StringExtensions.toFirstUpper(_name_9);
        _builder_5.append(_firstUpper_9, "");
        _builder_5.append(".isValid()) {");
        _builder_5.newLineIfNotEmpty();
        _builder_5.append("\t");
        _builder_5.append("return false;");
        _builder_5.newLine();
        _builder_5.append("}");
        _builder_5.newLine();
        return _builder_5.toString();
      }
    }
    StringConcatenation _builder_6 = new StringConcatenation();
    _builder_6.append("// missing validation for m");
    String _name_10 = this.toName(feature);
    String _firstUpper_10 = StringExtensions.toFirstUpper(_name_10);
    _builder_6.append(_firstUpper_10, "");
    _builder_6.newLineIfNotEmpty();
    return _builder_6.toString();
  }
  
  public String toValidateReference(final String referenceTypeName, final String featureName) {
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(referenceTypeName, "int")) {
        _matched=true;
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("if (m");
        String _firstUpper = StringExtensions.toFirstUpper(featureName);
        _builder.append(_firstUpper, "");
        _builder.append(" == -1) {");
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
      if (Objects.equal(referenceTypeName, "double")) {
        _matched=true;
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("if (m");
        String _firstUpper_1 = StringExtensions.toFirstUpper(featureName);
        _builder_1.append(_firstUpper_1, "");
        _builder_1.append(" == -1.0) {");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("\t");
        _builder_1.append("return false;");
        _builder_1.newLine();
        _builder_1.append("}");
        _builder_1.newLine();
        return _builder_1.toString();
      }
    }
    if (!_matched) {
      if (Objects.equal(referenceTypeName, "QString")) {
        _matched=true;
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("if (m");
        String _firstUpper_2 = StringExtensions.toFirstUpper(featureName);
        _builder_2.append(_firstUpper_2, "");
        _builder_2.append(".isNull() || m");
        String _firstUpper_3 = StringExtensions.toFirstUpper(featureName);
        _builder_2.append(_firstUpper_3, "");
        _builder_2.append(".isEmpty()) {");
        _builder_2.newLineIfNotEmpty();
        _builder_2.append("\t");
        _builder_2.append("return false;");
        _builder_2.newLine();
        _builder_2.append("}");
        _builder_2.newLine();
        return _builder_2.toString();
      }
    }
    StringConcatenation _builder_3 = new StringConcatenation();
    _builder_3.append("// missing validation for m");
    String _firstUpper_4 = StringExtensions.toFirstUpper(featureName);
    _builder_3.append(_firstUpper_4, "");
    _builder_3.newLineIfNotEmpty();
    return _builder_3.toString();
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
  
  public boolean hasForeignPropertyName(final LFeature feature) {
    String _foreignPropertyNameValue = this.getForeignPropertyNameValue(feature);
    boolean _equals = Objects.equal(_foreignPropertyNameValue, null);
    if (_equals) {
      return false;
    }
    return true;
  }
  
  public boolean hasEnumValues(final LEnum en) {
    String _enumValues = this.getEnumValues(en);
    boolean _equals = Objects.equal(_enumValues, null);
    if (_equals) {
      return false;
    }
    return true;
  }
  
  protected LFeature _referenceDomainKeyFeature(final LFeature feature) {
    return feature;
  }
  
  protected LFeature _referenceDomainKeyFeature(final LDtoReference reference) {
    LDto _type = reference.getType();
    return this.domainKeyFeature(((LDto) _type));
  }
  
  public LFeature domainKeyFeature(final LDto dto) {
    List<? extends LFeature> _allFeatures = dto.getAllFeatures();
    for (final LFeature feature : _allFeatures) {
      boolean _isDomainKey = this.isDomainKey(feature);
      if (_isDomainKey) {
        return feature;
      }
    }
    return null;
  }
  
  protected String _referenceDomainKey(final LFeature feature) {
    return "";
  }
  
  protected String _referenceDomainKey(final LDtoReference reference) {
    LDto _type = reference.getType();
    return this.domainKey(((LDto) _type));
  }
  
  public String domainKey(final LDto dto) {
    List<? extends LFeature> _allFeatures = dto.getAllFeatures();
    for (final LFeature feature : _allFeatures) {
      boolean _isDomainKey = this.isDomainKey(feature);
      if (_isDomainKey) {
        return this.toName(feature);
      }
    }
    return "uuid";
  }
  
  protected String _referenceDomainKeyType(final LFeature feature) {
    return "";
  }
  
  protected String _referenceDomainKeyType(final LDtoReference reference) {
    LDto _type = reference.getType();
    return this.domainKeyType(((LDto) _type));
  }
  
  public String domainKeyType(final LDto dto) {
    List<? extends LFeature> _allFeatures = dto.getAllFeatures();
    for (final LFeature feature : _allFeatures) {
      boolean _isDomainKey = this.isDomainKey(feature);
      if (_isDomainKey) {
        return this.toTypeName(feature);
      }
    }
    return "QString";
  }
  
  public boolean isReadOnlyCache(final LDto dto) {
    String _cachePolicyValue = this.getCachePolicyValue(dto);
    boolean _notEquals = (!Objects.equal(_cachePolicyValue, null));
    if (_notEquals) {
      String _cachePolicyValue_1 = this.getCachePolicyValue(dto);
      boolean _equals = Objects.equal(_cachePolicyValue_1, "R");
      if (_equals) {
        return true;
      }
    }
    return false;
  }
  
  public boolean existsForeignPropertyName(final LDto dto) {
    List<? extends LFeature> _allFeatures = dto.getAllFeatures();
    for (final LFeature feature : _allFeatures) {
      boolean _hasForeignPropertyName = this.hasForeignPropertyName(feature);
      if (_hasForeignPropertyName) {
        return true;
      }
    }
    return false;
  }
  
  public boolean existsEnum(final LDto dto) {
    List<? extends LFeature> _allFeatures = dto.getAllFeatures();
    for (final LFeature feature : _allFeatures) {
      boolean _isEnum = this.isEnum(feature);
      if (_isEnum) {
        return true;
      }
    }
    return false;
  }
  
  public boolean existsDates(final LDto dto) {
    List<? extends LFeature> _allFeatures = dto.getAllFeatures();
    for (final LFeature feature : _allFeatures) {
      boolean _isTypeOfDates = this.isTypeOfDates(feature);
      if (_isTypeOfDates) {
        return true;
      }
    }
    return false;
  }
  
  public boolean existsTypeOfDataObject(final LDto dto) {
    List<? extends LFeature> _allFeatures = dto.getAllFeatures();
    for (final LFeature feature : _allFeatures) {
      boolean _and = false;
      boolean _and_1 = false;
      boolean _and_2 = false;
      boolean _isTypeOfDataObject = this.isTypeOfDataObject(feature);
      if (!_isTypeOfDataObject) {
        _and_2 = false;
      } else {
        boolean _isToMany = this.isToMany(feature);
        boolean _not = (!_isToMany);
        _and_2 = _not;
      }
      if (!_and_2) {
        _and_1 = false;
      } else {
        boolean _isLazy = this.isLazy(feature);
        boolean _not_1 = (!_isLazy);
        _and_1 = _not_1;
      }
      if (!_and_1) {
        _and = false;
      } else {
        boolean _isContained = this.isContained(feature);
        boolean _not_2 = (!_isContained);
        _and = _not_2;
      }
      if (_and) {
        return true;
      }
    }
    return false;
  }
  
  public boolean existsLazy(final LDto dto) {
    List<? extends LFeature> _allFeatures = dto.getAllFeatures();
    for (final LFeature feature : _allFeatures) {
      boolean _isLazy = this.isLazy(feature);
      if (_isLazy) {
        return true;
      }
    }
    return false;
  }
  
  public boolean existsTransient(final LDto dto) {
    List<? extends LFeature> _allFeatures = dto.getAllFeatures();
    for (final LFeature feature : _allFeatures) {
      boolean _isTransient = this.isTransient(feature);
      if (_isTransient) {
        return true;
      }
    }
    return false;
  }
  
  public boolean hasUuid(final LDto dto) {
    List<? extends LFeature> _allFeatures = dto.getAllFeatures();
    for (final LFeature feature : _allFeatures) {
      String _name = this.toName(feature);
      boolean _equals = Objects.equal(_name, "uuid");
      if (_equals) {
        return true;
      }
    }
    return false;
  }
  
  public boolean hasDomainKey(final LDto dto) {
    List<? extends LFeature> _allFeatures = dto.getAllFeatures();
    for (final LFeature feature : _allFeatures) {
      boolean _isDomainKey = this.isDomainKey(feature);
      if (_isDomainKey) {
        return true;
      }
    }
    return false;
  }
  
  public String toTypeOrQObject(final LFeature feature) {
    boolean _isTypeOfDataObject = this.isTypeOfDataObject(feature);
    if (_isTypeOfDataObject) {
      StringConcatenation _builder = new StringConcatenation();
      String _typeName = this.toTypeName(feature);
      _builder.append(_typeName, "");
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
  
  public String getCachePolicyValue(final LDto member) {
    EList<LAnnotationDef> _resolvedAnnotations = member.getResolvedAnnotations();
    final LAnnotationDef annoDef = this._annotationExtension.getRedefined(CachePolicy.class, _resolvedAnnotations);
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
  
  public String getDateFormatStrngValue(final LFeature member) {
    EList<LAnnotationDef> _resolvedAnnotations = member.getResolvedAnnotations();
    final LAnnotationDef annoDef = this._annotationExtension.getRedefined(DateFormatString.class, _resolvedAnnotations);
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
  
  public String getForeignPropertyNameValue(final LFeature member) {
    EList<LAnnotationDef> _resolvedAnnotations = member.getResolvedAnnotations();
    final LAnnotationDef annoDef = this._annotationExtension.getRedefined(ForeignPropertyName.class, _resolvedAnnotations);
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
  
  public String getEnumValues(final LEnum member) {
    EList<LAnnotationDef> _resolvedAnnotations = member.getResolvedAnnotations();
    final LAnnotationDef annoDef = this._annotationExtension.getRedefined(EnumValues.class, _resolvedAnnotations);
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
  
  public String toForeignPropertyName(final LAnnotationTarget target) {
    if (target instanceof LAttribute) {
      return _toForeignPropertyName((LAttribute)target);
    } else if (target instanceof LReference) {
      return _toForeignPropertyName((LReference)target);
    } else if (target != null) {
      return _toForeignPropertyName(target);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(target).toString());
    }
  }
  
  public String toDateFormatString(final LAnnotationTarget target) {
    if (target instanceof LAttribute) {
      return _toDateFormatString((LAttribute)target);
    } else if (target instanceof LReference) {
      return _toDateFormatString((LReference)target);
    } else if (target != null) {
      return _toDateFormatString(target);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(target).toString());
    }
  }
  
  public String toEnumValues(final LAnnotationTarget target) {
    if (target instanceof LEnum) {
      return _toEnumValues((LEnum)target);
    } else if (target instanceof LAttribute) {
      return _toEnumValues((LAttribute)target);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(target).toString());
    }
  }
  
  public boolean isContained(final LAnnotationTarget target) {
    if (target instanceof LDtoReference) {
      return _isContained((LDtoReference)target);
    } else if (target instanceof LAttribute) {
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
  
  public boolean hasOpposite(final LFeature ref) {
    if (ref instanceof LDtoReference) {
      return _hasOpposite((LDtoReference)ref);
    } else if (ref instanceof LDtoAbstractAttribute) {
      return _hasOpposite((LDtoAbstractAttribute)ref);
    } else if (ref instanceof LDtoAbstractReference) {
      return _hasOpposite((LDtoAbstractReference)ref);
    } else if (ref != null) {
      return _hasOpposite(ref);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(ref).toString());
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
  
  public boolean isLazy(final LAnnotationTarget target) {
    if (target instanceof LAttribute) {
      return _isLazy((LAttribute)target);
    } else if (target instanceof LReference) {
      return _isLazy((LReference)target);
    } else if (target != null) {
      return _isLazy(target);
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
  
  public boolean isTypeOfDataObject(final LFeature att) {
    if (att instanceof LDtoAbstractAttribute) {
      return _isTypeOfDataObject((LDtoAbstractAttribute)att);
    } else if (att instanceof LDtoAbstractReference) {
      return _isTypeOfDataObject((LDtoAbstractReference)att);
    } else if (att != null) {
      return _isTypeOfDataObject(att);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(att).toString());
    }
  }
  
  public boolean isEnum(final LFeature att) {
    if (att instanceof LDtoAbstractAttribute) {
      return _isEnum((LDtoAbstractAttribute)att);
    } else if (att != null) {
      return _isEnum(att);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(att).toString());
    }
  }
  
  public LEnum enumFromAttributeType(final LFeature att) {
    if (att instanceof LDtoAbstractAttribute) {
      return _enumFromAttributeType((LDtoAbstractAttribute)att);
    } else if (att != null) {
      return _enumFromAttributeType(att);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(att).toString());
    }
  }
  
  public LFeature referenceDomainKeyFeature(final LFeature reference) {
    if (reference instanceof LDtoReference) {
      return _referenceDomainKeyFeature((LDtoReference)reference);
    } else if (reference != null) {
      return _referenceDomainKeyFeature(reference);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(reference).toString());
    }
  }
  
  public String referenceDomainKey(final LFeature reference) {
    if (reference instanceof LDtoReference) {
      return _referenceDomainKey((LDtoReference)reference);
    } else if (reference != null) {
      return _referenceDomainKey(reference);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(reference).toString());
    }
  }
  
  public String referenceDomainKeyType(final LFeature reference) {
    if (reference instanceof LDtoReference) {
      return _referenceDomainKeyType((LDtoReference)reference);
    } else if (reference != null) {
      return _referenceDomainKeyType(reference);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(reference).toString());
    }
  }
}
