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
 * 		ekke (Ekkehard Gentz), Rosenheim (Germany)
 */
package org.lunifera.dsl.ext.dtos.cpp.qt;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import java.util.Arrays;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.lunifera.dsl.ext.dtos.cpp.qt.CppExtensions;
import org.lunifera.dsl.semantic.common.types.LEnum;
import org.lunifera.dsl.semantic.common.types.LEnumLiteral;
import org.lunifera.dsl.semantic.common.types.LFeature;
import org.lunifera.dsl.semantic.dto.LDto;
import org.lunifera.dsl.semantic.dto.LDtoAbstractAttribute;
import org.lunifera.dsl.semantic.dto.LDtoAbstractReference;
import org.lunifera.dsl.semantic.dto.LDtoReference;

@SuppressWarnings("all")
public class CppGenerator {
  @Inject
  @Extension
  private CppExtensions _cppExtensions;
  
  public String toFileName(final LDto dto) {
    String _name = dto.getName();
    String _firstUpper = StringExtensions.toFirstUpper(_name);
    return (_firstUpper + ".cpp");
  }
  
  public CharSequence toContent(final LDto dto) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("#include \"");
    String _name = this._cppExtensions.toName(dto);
    _builder.append(_name, "");
    _builder.append(".hpp\"");
    _builder.newLineIfNotEmpty();
    _builder.append("#include <QDebug>");
    _builder.newLine();
    _builder.append("#include <quuid.h>");
    _builder.newLine();
    {
      List<LDtoAbstractReference> _references = dto.getReferences();
      for(final LDtoAbstractReference reference : _references) {
        {
          boolean _isContained = this._cppExtensions.isContained(reference);
          if (_isContained) {
            _builder.append("#include \"");
            String _typeName = this._cppExtensions.toTypeName(reference);
            _builder.append(_typeName, "");
            _builder.append(".hpp\"");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.newLine();
    _builder.append("// keys of QVariantMap used in this APP");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures = dto.getAllFeatures();
      for(final LFeature feature : _allFeatures) {
        {
          boolean _and = false;
          boolean _isTypeOfDTO = this._cppExtensions.isTypeOfDTO(feature);
          if (!_isTypeOfDTO) {
            _and = false;
          } else {
            boolean _isContained_1 = this._cppExtensions.isContained(feature);
            _and = _isContained_1;
          }
          if (_and) {
            _builder.append("// no key for ");
            String _name_1 = this._cppExtensions.toName(feature);
            _builder.append(_name_1, "");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("static const QString ");
            String _name_2 = this._cppExtensions.toName(feature);
            _builder.append(_name_2, "");
            _builder.append("Key = \"");
            String _name_3 = this._cppExtensions.toName(feature);
            _builder.append(_name_3, "");
            _builder.append("\";\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.newLine();
    {
      boolean _existsServerName = this._cppExtensions.existsServerName(dto);
      if (_existsServerName) {
        _builder.append("// keys used from Server API etc");
        _builder.newLine();
        {
          List<? extends LFeature> _allFeatures_1 = dto.getAllFeatures();
          for(final LFeature feature_1 : _allFeatures_1) {
            {
              boolean _and_1 = false;
              boolean _isTypeOfDTO_1 = this._cppExtensions.isTypeOfDTO(feature_1);
              if (!_isTypeOfDTO_1) {
                _and_1 = false;
              } else {
                boolean _isContained_2 = this._cppExtensions.isContained(feature_1);
                _and_1 = _isContained_2;
              }
              if (_and_1) {
                _builder.append("// no key for ");
                String _name_4 = this._cppExtensions.toName(feature_1);
                _builder.append(_name_4, "");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("static const QString ");
                String _name_5 = this._cppExtensions.toName(feature_1);
                _builder.append(_name_5, "");
                _builder.append("ForeignKey = \"");
                String _serverName = this._cppExtensions.toServerName(feature_1);
                _builder.append(_serverName, "");
                _builder.append("\";\t");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    _builder.newLine();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Default Constructor if ");
    String _name_6 = this._cppExtensions.toName(dto);
    _builder.append(_name_6, " ");
    _builder.append(" not initialized from QVariantMap");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    String _name_7 = this._cppExtensions.toName(dto);
    _builder.append(_name_7, "");
    _builder.append("::");
    String _name_8 = this._cppExtensions.toName(dto);
    _builder.append(_name_8, "");
    _builder.append("(QObject *parent) :");
    _builder.newLineIfNotEmpty();
    _builder.append("        ");
    _builder.append("QObject(parent)");
    {
      List<? extends LFeature> _allFeatures_2 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _and = false;
          boolean _and_1 = false;
          boolean _and_2 = false;
          boolean _and_3 = false;
          boolean _isToMany = CppGenerator.this._cppExtensions.isToMany(it);
          boolean _not = (!_isToMany);
          if (!_not) {
            _and_3 = false;
          } else {
            boolean _isTypeOfDTO = CppGenerator.this._cppExtensions.isTypeOfDTO(it);
            boolean _not_1 = (!_isTypeOfDTO);
            _and_3 = _not_1;
          }
          if (!_and_3) {
            _and_2 = false;
          } else {
            boolean _isTypeOfDates = CppGenerator.this._cppExtensions.isTypeOfDates(it);
            boolean _not_2 = (!_isTypeOfDates);
            _and_2 = _not_2;
          }
          if (!_and_2) {
            _and_1 = false;
          } else {
            boolean _isContained = CppGenerator.this._cppExtensions.isContained(it);
            boolean _not_3 = (!_isContained);
            _and_1 = _not_3;
          }
          if (!_and_1) {
            _and = false;
          } else {
            boolean _isEnum = CppGenerator.this._cppExtensions.isEnum(it);
            boolean _not_4 = (!_isEnum);
            _and = _not_4;
          }
          return Boolean.valueOf(_and);
        }
      };
      Iterable<? extends LFeature> _filter = IterableExtensions.filter(_allFeatures_2, _function);
      for(final LFeature feature_2 : _filter) {
        _builder.append(", m");
        String _name_9 = this._cppExtensions.toName(feature_2);
        String _firstUpper = StringExtensions.toFirstUpper(_name_9);
        _builder.append(_firstUpper, "        ");
        _builder.append("(");
        String _defaultForType = this._cppExtensions.defaultForType(feature_2);
        _builder.append(_defaultForType, "        ");
        _builder.append(")");
      }
    }
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    {
      boolean _existsLazy = this._cppExtensions.existsLazy(dto);
      if (_existsLazy) {
        _builder.append("\t");
        _builder.append("// lazy references:");
        _builder.newLine();
        {
          List<? extends LFeature> _allFeatures_3 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_1 = new Function1<LFeature, Boolean>() {
            public Boolean apply(final LFeature it) {
              return Boolean.valueOf(CppGenerator.this._cppExtensions.isLazy(it));
            }
          };
          Iterable<? extends LFeature> _filter_1 = IterableExtensions.filter(_allFeatures_3, _function_1);
          for(final LFeature feature_3 : _filter_1) {
            _builder.append("\t");
            _builder.append("m");
            String _name_10 = this._cppExtensions.toName(feature_3);
            String _firstUpper_1 = StringExtensions.toFirstUpper(_name_10);
            _builder.append(_firstUpper_1, "\t");
            _builder.append(" = ");
            String _referenceDomainKeyType = this._cppExtensions.referenceDomainKeyType(feature_3);
            String _defaultForLazyTypeName = this._cppExtensions.defaultForLazyTypeName(_referenceDomainKeyType);
            _builder.append(_defaultForLazyTypeName, "\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      boolean _existsEnum = this._cppExtensions.existsEnum(dto);
      if (_existsEnum) {
        _builder.append("\t");
        _builder.append("// ENUMs:");
        _builder.newLine();
        {
          List<? extends LFeature> _allFeatures_4 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_2 = new Function1<LFeature, Boolean>() {
            public Boolean apply(final LFeature it) {
              return Boolean.valueOf(CppGenerator.this._cppExtensions.isEnum(it));
            }
          };
          Iterable<? extends LFeature> _filter_2 = IterableExtensions.filter(_allFeatures_4, _function_2);
          for(final LFeature feature_4 : _filter_2) {
            _builder.append("\t");
            _builder.append("m");
            String _name_11 = this._cppExtensions.toName(feature_4);
            String _firstUpper_2 = StringExtensions.toFirstUpper(_name_11);
            _builder.append(_firstUpper_2, "\t");
            _builder.append(" = ");
            String _typeName_1 = this._cppExtensions.toTypeName(feature_4);
            _builder.append(_typeName_1, "\t");
            _builder.append("::DEFAULT_VALUE;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      boolean _existsDates = this._cppExtensions.existsDates(dto);
      if (_existsDates) {
        _builder.append("\t");
        _builder.append("// Date, Time or Timestamp ? construct null value");
        _builder.newLine();
        {
          List<? extends LFeature> _allFeatures_5 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_3 = new Function1<LFeature, Boolean>() {
            public Boolean apply(final LFeature it) {
              return Boolean.valueOf(CppGenerator.this._cppExtensions.isTypeOfDates(it));
            }
          };
          Iterable<? extends LFeature> _filter_3 = IterableExtensions.filter(_allFeatures_5, _function_3);
          for(final LFeature feature_5 : _filter_3) {
            _builder.append("\t");
            _builder.append("m");
            String _name_12 = this._cppExtensions.toName(feature_5);
            String _firstUpper_3 = StringExtensions.toFirstUpper(_name_12);
            _builder.append(_firstUpper_3, "\t");
            _builder.append(" = ");
            String _typeName_2 = this._cppExtensions.toTypeName(feature_5);
            _builder.append(_typeName_2, "\t");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* initialize ");
    String _name_13 = this._cppExtensions.toName(dto);
    _builder.append(_name_13, " ");
    _builder.append(" from QVariantMap");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("* Map got from JsonDataAccess or so");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("void ");
    String _name_14 = this._cppExtensions.toName(dto);
    _builder.append(_name_14, "");
    _builder.append("::fillFromMap(const QVariantMap& ");
    String _name_15 = this._cppExtensions.toName(dto);
    String _firstLower = StringExtensions.toFirstLower(_name_15);
    _builder.append(_firstLower, "");
    _builder.append("Map)");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("m");
    String _name_16 = this._cppExtensions.toName(dto);
    String _firstUpper_4 = StringExtensions.toFirstUpper(_name_16);
    _builder.append(_firstUpper_4, "\t");
    _builder.append("Map = ");
    String _name_17 = this._cppExtensions.toName(dto);
    String _firstLower_1 = StringExtensions.toFirstLower(_name_17);
    _builder.append(_firstLower_1, "\t");
    _builder.append("Map;");
    _builder.newLineIfNotEmpty();
    {
      List<? extends LFeature> _allFeatures_6 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_4 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _isToMany = CppGenerator.this._cppExtensions.isToMany(it);
          return Boolean.valueOf((!_isToMany));
        }
      };
      Iterable<? extends LFeature> _filter_4 = IterableExtensions.filter(_allFeatures_6, _function_4);
      for(final LFeature feature_6 : _filter_4) {
        {
          boolean _isTypeOfDTO_2 = this._cppExtensions.isTypeOfDTO(feature_6);
          if (_isTypeOfDTO_2) {
            {
              boolean _isContained_3 = this._cppExtensions.isContained(feature_6);
              if (_isContained_3) {
                _builder.append("\t");
                _builder.append("// m");
                String _name_18 = this._cppExtensions.toName(feature_6);
                String _firstUpper_5 = StringExtensions.toFirstUpper(_name_18);
                _builder.append(_firstUpper_5, "\t");
                _builder.append(" is parent (");
                String _typeName_3 = this._cppExtensions.toTypeName(feature_6);
                _builder.append(_typeName_3, "\t");
                _builder.append("* containing ");
                String _name_19 = this._cppExtensions.toName(dto);
                _builder.append(_name_19, "\t");
                _builder.append(")");
                _builder.newLineIfNotEmpty();
              } else {
                boolean _isLazy = this._cppExtensions.isLazy(feature_6);
                if (_isLazy) {
                  _builder.append("\t");
                  _builder.append("// ");
                  String _name_20 = this._cppExtensions.toName(feature_6);
                  _builder.append(_name_20, "\t");
                  _builder.append(" lazy pointing to ");
                  String _typeOrQObject = this._cppExtensions.toTypeOrQObject(feature_6);
                  _builder.append(_typeOrQObject, "\t");
                  _builder.append(" (domainKey: ");
                  String _referenceDomainKey = this._cppExtensions.referenceDomainKey(feature_6);
                  _builder.append(_referenceDomainKey, "\t");
                  _builder.append(")");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("if(m");
                  String _name_21 = this._cppExtensions.toName(dto);
                  String _firstUpper_6 = StringExtensions.toFirstUpper(_name_21);
                  _builder.append(_firstUpper_6, "\t");
                  _builder.append("Map.contains(");
                  String _name_22 = this._cppExtensions.toName(feature_6);
                  _builder.append(_name_22, "\t");
                  _builder.append("Key)){");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("m");
                  String _name_23 = this._cppExtensions.toName(feature_6);
                  String _firstUpper_7 = StringExtensions.toFirstUpper(_name_23);
                  _builder.append(_firstUpper_7, "\t\t");
                  _builder.append(" = m");
                  String _name_24 = this._cppExtensions.toName(dto);
                  String _firstUpper_8 = StringExtensions.toFirstUpper(_name_24);
                  _builder.append(_firstUpper_8, "\t\t");
                  _builder.append("Map.value(");
                  String _name_25 = this._cppExtensions.toName(feature_6);
                  _builder.append(_name_25, "\t\t");
                  _builder.append("Key).to");
                  String _referenceDomainKeyType_1 = this._cppExtensions.referenceDomainKeyType(feature_6);
                  String _mapToLazyTypeName = this._cppExtensions.mapToLazyTypeName(_referenceDomainKeyType_1);
                  _builder.append(_mapToLazyTypeName, "\t\t");
                  _builder.append("();");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("if(m");
                  String _name_26 = this._cppExtensions.toName(feature_6);
                  String _firstUpper_9 = StringExtensions.toFirstUpper(_name_26);
                  _builder.append(_firstUpper_9, "\t\t");
                  _builder.append(" != ");
                  String _referenceDomainKeyType_2 = this._cppExtensions.referenceDomainKeyType(feature_6);
                  String _defaultForLazyTypeName_1 = this._cppExtensions.defaultForLazyTypeName(_referenceDomainKeyType_2);
                  _builder.append(_defaultForLazyTypeName_1, "\t\t");
                  _builder.append("){");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t\t");
                  _builder.append("// SIGNAL to request a pointer to the corresponding DTO");
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("\t\t");
                  _builder.append("emit request");
                  String _name_27 = this._cppExtensions.toName(feature_6);
                  String _firstUpper_10 = StringExtensions.toFirstUpper(_name_27);
                  _builder.append(_firstUpper_10, "\t\t\t");
                  _builder.append("AsDTO(m");
                  String _name_28 = this._cppExtensions.toName(feature_6);
                  String _firstUpper_11 = StringExtensions.toFirstUpper(_name_28);
                  _builder.append(_firstUpper_11, "\t\t\t");
                  _builder.append(");");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("}");
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("}");
                  _builder.newLine();
                } else {
                  _builder.append("\t");
                  _builder.append("// m");
                  String _name_29 = this._cppExtensions.toName(feature_6);
                  String _firstUpper_12 = StringExtensions.toFirstUpper(_name_29);
                  _builder.append(_firstUpper_12, "\t");
                  _builder.append(" points to ");
                  String _typeName_4 = this._cppExtensions.toTypeName(feature_6);
                  _builder.append(_typeName_4, "\t");
                  _builder.append("*");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("if(m");
                  String _name_30 = this._cppExtensions.toName(dto);
                  String _firstUpper_13 = StringExtensions.toFirstUpper(_name_30);
                  _builder.append(_firstUpper_13, "\t");
                  _builder.append("Map.contains(");
                  String _name_31 = this._cppExtensions.toName(feature_6);
                  _builder.append(_name_31, "\t");
                  _builder.append("Key)){");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("QVariantMap ");
                  String _name_32 = this._cppExtensions.toName(feature_6);
                  _builder.append(_name_32, "\t\t");
                  _builder.append("Map;");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  String _name_33 = this._cppExtensions.toName(feature_6);
                  _builder.append(_name_33, "\t\t");
                  _builder.append("Map = m");
                  String _name_34 = this._cppExtensions.toName(dto);
                  String _firstUpper_14 = StringExtensions.toFirstUpper(_name_34);
                  _builder.append(_firstUpper_14, "\t\t");
                  _builder.append("Map.value(");
                  String _name_35 = this._cppExtensions.toName(feature_6);
                  _builder.append(_name_35, "\t\t");
                  _builder.append("Key).toMap();");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("if(!");
                  String _name_36 = this._cppExtensions.toName(feature_6);
                  _builder.append(_name_36, "\t\t");
                  _builder.append("Map.isEmpty()){");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_37 = this._cppExtensions.toName(feature_6);
                  String _firstUpper_15 = StringExtensions.toFirstUpper(_name_37);
                  _builder.append(_firstUpper_15, "\t\t\t");
                  _builder.append(" = new ");
                  String _typeName_5 = this._cppExtensions.toTypeName(feature_6);
                  _builder.append(_typeName_5, "\t\t\t");
                  _builder.append("();");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_38 = this._cppExtensions.toName(feature_6);
                  String _firstUpper_16 = StringExtensions.toFirstUpper(_name_38);
                  _builder.append(_firstUpper_16, "\t\t\t");
                  _builder.append("->setParent(this);");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_39 = this._cppExtensions.toName(feature_6);
                  String _firstUpper_17 = StringExtensions.toFirstUpper(_name_39);
                  _builder.append(_firstUpper_17, "\t\t\t");
                  _builder.append("->fillFromMap(");
                  String _name_40 = this._cppExtensions.toName(feature_6);
                  _builder.append(_name_40, "\t\t\t");
                  _builder.append("Map);");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("}");
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("}");
                  _builder.newLine();
                }
              }
            }
          } else {
            {
              boolean _isTransient = this._cppExtensions.isTransient(feature_6);
              if (_isTransient) {
                _builder.append("\t");
                _builder.append("// m");
                String _name_41 = this._cppExtensions.toName(feature_6);
                String _firstUpper_18 = StringExtensions.toFirstUpper(_name_41);
                _builder.append(_firstUpper_18, "\t");
                _builder.append(" is transient - perhaps not included");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("if(m");
                String _name_42 = this._cppExtensions.toName(dto);
                String _firstUpper_19 = StringExtensions.toFirstUpper(_name_42);
                _builder.append(_firstUpper_19, "\t");
                _builder.append("Map.contains(");
                String _name_43 = this._cppExtensions.toName(feature_6);
                String _firstLower_2 = StringExtensions.toFirstLower(_name_43);
                _builder.append(_firstLower_2, "\t");
                _builder.append("Key)){");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("m");
                String _name_44 = this._cppExtensions.toName(feature_6);
                String _firstUpper_20 = StringExtensions.toFirstUpper(_name_44);
                _builder.append(_firstUpper_20, "\t\t");
                _builder.append(" = m");
                String _name_45 = this._cppExtensions.toName(dto);
                String _firstUpper_21 = StringExtensions.toFirstUpper(_name_45);
                _builder.append(_firstUpper_21, "\t\t");
                _builder.append("Map.value(");
                String _name_46 = this._cppExtensions.toName(feature_6);
                _builder.append(_name_46, "\t\t");
                _builder.append("Key).to");
                String _mapToType = this._cppExtensions.mapToType(feature_6);
                _builder.append(_mapToType, "\t\t");
                _builder.append("();");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("}");
                _builder.newLine();
              } else {
                boolean _isEnum = this._cppExtensions.isEnum(feature_6);
                if (_isEnum) {
                  _builder.append("\t");
                  _builder.append("// ENUM");
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("if (m");
                  String _name_47 = this._cppExtensions.toName(dto);
                  String _firstUpper_22 = StringExtensions.toFirstUpper(_name_47);
                  _builder.append(_firstUpper_22, "\t");
                  _builder.append("Map.contains(");
                  String _name_48 = this._cppExtensions.toName(feature_6);
                  String _firstLower_3 = StringExtensions.toFirstLower(_name_48);
                  _builder.append(_firstLower_3, "\t");
                  _builder.append("Key)) {");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("bool* ok;");
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("ok = false;");
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("m");
                  String _name_49 = this._cppExtensions.toName(dto);
                  String _firstUpper_23 = StringExtensions.toFirstUpper(_name_49);
                  _builder.append(_firstUpper_23, "\t\t");
                  _builder.append("Map.value(");
                  String _name_50 = this._cppExtensions.toName(feature_6);
                  String _firstLower_4 = StringExtensions.toFirstLower(_name_50);
                  _builder.append(_firstLower_4, "\t\t");
                  _builder.append("Key).toInt(ok);");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("if (ok) {");
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_51 = this._cppExtensions.toName(feature_6);
                  String _firstUpper_24 = StringExtensions.toFirstUpper(_name_51);
                  _builder.append(_firstUpper_24, "\t\t\t");
                  _builder.append(" = m");
                  String _name_52 = this._cppExtensions.toName(dto);
                  String _firstUpper_25 = StringExtensions.toFirstUpper(_name_52);
                  _builder.append(_firstUpper_25, "\t\t\t");
                  _builder.append("Map.value(");
                  String _name_53 = this._cppExtensions.toName(feature_6);
                  String _firstLower_5 = StringExtensions.toFirstLower(_name_53);
                  _builder.append(_firstLower_5, "\t\t\t");
                  _builder.append("Key).toInt();");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("} else {");
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_54 = this._cppExtensions.toName(feature_6);
                  String _firstUpper_26 = StringExtensions.toFirstUpper(_name_54);
                  _builder.append(_firstUpper_26, "\t\t\t");
                  _builder.append(" = ");
                  String _name_55 = this._cppExtensions.toName(feature_6);
                  String _firstLower_6 = StringExtensions.toFirstLower(_name_55);
                  _builder.append(_firstLower_6, "\t\t\t");
                  _builder.append("StringToInt(m");
                  String _name_56 = this._cppExtensions.toName(dto);
                  String _firstUpper_27 = StringExtensions.toFirstUpper(_name_56);
                  _builder.append(_firstUpper_27, "\t\t\t");
                  _builder.append("Map.value(");
                  String _name_57 = this._cppExtensions.toName(feature_6);
                  String _firstLower_7 = StringExtensions.toFirstLower(_name_57);
                  _builder.append(_firstLower_7, "\t\t\t");
                  _builder.append("Key).toString());");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("}");
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("} else {");
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("m");
                  String _name_58 = this._cppExtensions.toName(feature_6);
                  String _firstUpper_28 = StringExtensions.toFirstUpper(_name_58);
                  _builder.append(_firstUpper_28, "\t\t");
                  _builder.append(" = ");
                  String _typeName_6 = this._cppExtensions.toTypeName(feature_6);
                  _builder.append(_typeName_6, "\t\t");
                  _builder.append("::NO_VALUE;");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("}");
                  _builder.newLine();
                } else {
                  _builder.append("\t");
                  _builder.append("m");
                  String _name_59 = this._cppExtensions.toName(feature_6);
                  String _firstUpper_29 = StringExtensions.toFirstUpper(_name_59);
                  _builder.append(_firstUpper_29, "\t");
                  _builder.append(" = m");
                  String _name_60 = this._cppExtensions.toName(dto);
                  String _firstUpper_30 = StringExtensions.toFirstUpper(_name_60);
                  _builder.append(_firstUpper_30, "\t");
                  _builder.append("Map.value(");
                  String _name_61 = this._cppExtensions.toName(feature_6);
                  _builder.append(_name_61, "\t");
                  _builder.append("Key).to");
                  String _mapToType_1 = this._cppExtensions.mapToType(feature_6);
                  _builder.append(_mapToType_1, "\t");
                  _builder.append("();");
                  _builder.newLineIfNotEmpty();
                }
              }
            }
            {
              String _name_62 = this._cppExtensions.toName(feature_6);
              boolean _equals = Objects.equal(_name_62, "uuid");
              if (_equals) {
                _builder.append("\t");
                _builder.append("if (mUuid.isEmpty()) {");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("mUuid = QUuid::createUuid().toString();");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("mUuid = mUuid.right(mUuid.length() - 1);");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("mUuid = mUuid.left(mUuid.length() - 1);");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("}\t");
                _builder.newLine();
              }
            }
          }
        }
      }
    }
    {
      List<? extends LFeature> _allFeatures_7 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_5 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _and = false;
          boolean _isToMany = CppGenerator.this._cppExtensions.isToMany(it);
          if (!_isToMany) {
            _and = false;
          } else {
            boolean _isArrayList = CppGenerator.this._cppExtensions.isArrayList(it);
            boolean _not = (!_isArrayList);
            _and = _not;
          }
          return Boolean.valueOf(_and);
        }
      };
      Iterable<? extends LFeature> _filter_5 = IterableExtensions.filter(_allFeatures_7, _function_5);
      for(final LFeature feature_7 : _filter_5) {
        _builder.append("\t");
        _builder.append("// m");
        String _name_63 = this._cppExtensions.toName(feature_7);
        String _firstUpper_31 = StringExtensions.toFirstUpper(_name_63);
        _builder.append(_firstUpper_31, "\t");
        _builder.append(" is List of ");
        String _typeName_7 = this._cppExtensions.toTypeName(feature_7);
        _builder.append(_typeName_7, "\t");
        _builder.append("*");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("QVariantList ");
        String _name_64 = this._cppExtensions.toName(feature_7);
        String _firstLower_8 = StringExtensions.toFirstLower(_name_64);
        _builder.append(_firstLower_8, "\t");
        _builder.append("List;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        String _name_65 = this._cppExtensions.toName(feature_7);
        String _firstLower_9 = StringExtensions.toFirstLower(_name_65);
        _builder.append(_firstLower_9, "\t");
        _builder.append("List = m");
        String _name_66 = this._cppExtensions.toName(dto);
        String _firstUpper_32 = StringExtensions.toFirstUpper(_name_66);
        _builder.append(_firstUpper_32, "\t");
        _builder.append("Map.value(");
        String _name_67 = this._cppExtensions.toName(feature_7);
        String _firstLower_10 = StringExtensions.toFirstLower(_name_67);
        _builder.append(_firstLower_10, "\t");
        _builder.append("Key).toList();");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("m");
        String _name_68 = this._cppExtensions.toName(feature_7);
        String _firstUpper_33 = StringExtensions.toFirstUpper(_name_68);
        _builder.append(_firstUpper_33, "\t");
        _builder.append(".clear();");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("for (int i = 0; i < ");
        String _name_69 = this._cppExtensions.toName(feature_7);
        String _firstLower_11 = StringExtensions.toFirstLower(_name_69);
        _builder.append(_firstLower_11, "\t");
        _builder.append("List.size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("QVariantMap ");
        String _name_70 = this._cppExtensions.toName(feature_7);
        String _firstLower_12 = StringExtensions.toFirstLower(_name_70);
        _builder.append(_firstLower_12, "\t\t");
        _builder.append("Map;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        String _name_71 = this._cppExtensions.toName(feature_7);
        String _firstLower_13 = StringExtensions.toFirstLower(_name_71);
        _builder.append(_firstLower_13, "\t\t");
        _builder.append("Map = ");
        String _name_72 = this._cppExtensions.toName(feature_7);
        String _firstLower_14 = StringExtensions.toFirstLower(_name_72);
        _builder.append(_firstLower_14, "\t\t");
        _builder.append("List.at(i).toMap();");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        String _typeName_8 = this._cppExtensions.toTypeName(feature_7);
        _builder.append(_typeName_8, "\t\t");
        _builder.append("* ");
        String _typeName_9 = this._cppExtensions.toTypeName(feature_7);
        String _firstLower_15 = StringExtensions.toFirstLower(_typeName_9);
        _builder.append(_firstLower_15, "\t\t");
        _builder.append(" = new ");
        String _typeName_10 = this._cppExtensions.toTypeName(feature_7);
        _builder.append(_typeName_10, "\t\t");
        _builder.append("();");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        String _typeName_11 = this._cppExtensions.toTypeName(feature_7);
        String _firstLower_16 = StringExtensions.toFirstLower(_typeName_11);
        _builder.append(_firstLower_16, "\t\t");
        _builder.append("->setParent(this);");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        String _typeName_12 = this._cppExtensions.toTypeName(feature_7);
        String _firstLower_17 = StringExtensions.toFirstLower(_typeName_12);
        _builder.append(_firstLower_17, "\t\t");
        _builder.append("->fillFromMap(");
        String _name_73 = this._cppExtensions.toName(feature_7);
        String _firstLower_18 = StringExtensions.toFirstLower(_name_73);
        _builder.append(_firstLower_18, "\t\t");
        _builder.append("Map);");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("m");
        String _name_74 = this._cppExtensions.toName(feature_7);
        String _firstUpper_34 = StringExtensions.toFirstUpper(_name_74);
        _builder.append(_firstUpper_34, "\t\t");
        _builder.append(".append(");
        String _typeName_13 = this._cppExtensions.toTypeName(feature_7);
        String _firstLower_19 = StringExtensions.toFirstLower(_typeName_13);
        _builder.append(_firstLower_19, "\t\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    {
      List<? extends LFeature> _allFeatures_8 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_6 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _and = false;
          boolean _isToMany = CppGenerator.this._cppExtensions.isToMany(it);
          if (!_isToMany) {
            _and = false;
          } else {
            boolean _isArrayList = CppGenerator.this._cppExtensions.isArrayList(it);
            _and = _isArrayList;
          }
          return Boolean.valueOf(_and);
        }
      };
      Iterable<? extends LFeature> _filter_6 = IterableExtensions.filter(_allFeatures_8, _function_6);
      for(final LFeature feature_8 : _filter_6) {
        {
          String _typeName_14 = this._cppExtensions.toTypeName(feature_8);
          boolean _equals_1 = Objects.equal(_typeName_14, "QString");
          if (_equals_1) {
            _builder.append("\t");
            _builder.append("m");
            String _name_75 = this._cppExtensions.toName(feature_8);
            String _firstUpper_35 = StringExtensions.toFirstUpper(_name_75);
            _builder.append(_firstUpper_35, "\t");
            _builder.append("StringList = m");
            String _name_76 = this._cppExtensions.toName(dto);
            String _firstUpper_36 = StringExtensions.toFirstUpper(_name_76);
            _builder.append(_firstUpper_36, "\t");
            _builder.append("Map.value(");
            String _name_77 = this._cppExtensions.toName(feature_8);
            String _firstLower_20 = StringExtensions.toFirstLower(_name_77);
            _builder.append(_firstLower_20, "\t");
            _builder.append("Key).toStringList();");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t");
            _builder.append("// m");
            String _name_78 = this._cppExtensions.toName(feature_8);
            String _firstUpper_37 = StringExtensions.toFirstUpper(_name_78);
            _builder.append(_firstUpper_37, "\t");
            _builder.append(" is Array of ");
            String _typeName_15 = this._cppExtensions.toTypeName(feature_8);
            _builder.append(_typeName_15, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("QVariantList ");
            String _name_79 = this._cppExtensions.toName(feature_8);
            _builder.append(_name_79, "\t");
            _builder.append("List;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            String _name_80 = this._cppExtensions.toName(feature_8);
            _builder.append(_name_80, "\t");
            _builder.append("List = m");
            String _name_81 = this._cppExtensions.toName(dto);
            String _firstUpper_38 = StringExtensions.toFirstUpper(_name_81);
            _builder.append(_firstUpper_38, "\t");
            _builder.append("Map.value(");
            String _name_82 = this._cppExtensions.toName(feature_8);
            _builder.append(_name_82, "\t");
            _builder.append("Key).toList();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("m");
            String _name_83 = this._cppExtensions.toName(feature_8);
            String _firstUpper_39 = StringExtensions.toFirstUpper(_name_83);
            _builder.append(_firstUpper_39, "\t");
            _builder.append(".clear();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("for (int i = 0; i < ");
            String _name_84 = this._cppExtensions.toName(feature_8);
            _builder.append(_name_84, "\t");
            _builder.append("List.size(); ++i) {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("m");
            String _name_85 = this._cppExtensions.toName(feature_8);
            String _firstUpper_40 = StringExtensions.toFirstUpper(_name_85);
            _builder.append(_firstUpper_40, "\t\t");
            _builder.append(".append(");
            String _name_86 = this._cppExtensions.toName(feature_8);
            _builder.append(_name_86, "\t\t");
            _builder.append("List.at(i).to");
            String _mapToSingleType = this._cppExtensions.mapToSingleType(feature_8);
            _builder.append(_mapToSingleType, "\t\t");
            _builder.append("());");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("void ");
    String _name_87 = this._cppExtensions.toName(dto);
    _builder.append(_name_87, "");
    _builder.append("::prepareNew()");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("mUuid = QUuid::createUuid().toString();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("mUuid = mUuid.right(mUuid.length() - 1);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("mUuid = mUuid.left(mUuid.length() - 1);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Checks if all mandatory attributes, all DomainKeys and uuid\'s are filled");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("bool ");
    String _name_88 = this._cppExtensions.toName(dto);
    _builder.append(_name_88, "");
    _builder.append("::isValid()");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_9 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_7 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _or = false;
          boolean _or_1 = false;
          boolean _isMandatory = CppGenerator.this._cppExtensions.isMandatory(it);
          if (_isMandatory) {
            _or_1 = true;
          } else {
            String _name = CppGenerator.this._cppExtensions.toName(it);
            boolean _equals = Objects.equal(_name, "uuid");
            _or_1 = _equals;
          }
          if (_or_1) {
            _or = true;
          } else {
            boolean _isDomainKey = CppGenerator.this._cppExtensions.isDomainKey(it);
            _or = _isDomainKey;
          }
          return Boolean.valueOf(_or);
        }
      };
      Iterable<? extends LFeature> _filter_7 = IterableExtensions.filter(_allFeatures_9, _function_7);
      for(final LFeature feature_9 : _filter_7) {
        {
          boolean _isToMany = this._cppExtensions.isToMany(feature_9);
          if (_isToMany) {
            _builder.append("\t");
            _builder.append("if(m");
            String _name_89 = this._cppExtensions.toName(feature_9);
            String _firstUpper_41 = StringExtensions.toFirstUpper(_name_89);
            _builder.append(_firstUpper_41, "\t");
            _builder.append(".size() == 0){");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("return false;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          } else {
            boolean _isLazy_1 = this._cppExtensions.isLazy(feature_9);
            if (_isLazy_1) {
              _builder.append("\t");
              _builder.append("// ");
              String _name_90 = this._cppExtensions.toName(feature_9);
              _builder.append(_name_90, "\t");
              _builder.append(" lazy pointing to ");
              String _typeOrQObject_1 = this._cppExtensions.toTypeOrQObject(feature_9);
              _builder.append(_typeOrQObject_1, "\t");
              _builder.append(" (domainKey: ");
              String _referenceDomainKey_1 = this._cppExtensions.referenceDomainKey(feature_9);
              _builder.append(_referenceDomainKey_1, "\t");
              _builder.append(")");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              LFeature _referenceDomainKeyFeature = this._cppExtensions.referenceDomainKeyFeature(feature_9);
              String _typeName_16 = this._cppExtensions.toTypeName(_referenceDomainKeyFeature);
              String _name_91 = this._cppExtensions.toName(feature_9);
              String _validateReference = this._cppExtensions.toValidateReference(_typeName_16, _name_91);
              _builder.append(_validateReference, "\t");
              _builder.newLineIfNotEmpty();
            } else {
              boolean _isTypeOfDTO_3 = this._cppExtensions.isTypeOfDTO(feature_9);
              if (_isTypeOfDTO_3) {
                _builder.append("\t");
                _builder.append("if(!m");
                String _name_92 = this._cppExtensions.toName(feature_9);
                String _firstUpper_42 = StringExtensions.toFirstUpper(_name_92);
                _builder.append(_firstUpper_42, "\t");
                _builder.append(") {");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("return false;");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("}");
                _builder.newLine();
              } else {
                _builder.append("\t");
                String _validate = this._cppExtensions.toValidate(feature_9);
                _builder.append(_validate, "\t");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("return true;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Exports Properties from ");
    String _name_93 = this._cppExtensions.toName(dto);
    _builder.append(_name_93, " ");
    _builder.append(" as QVariantMap");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("* exports ALL data including transient properties");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* To store persistent Data in JsonDataAccess use dataToPersist()");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("QVariantMap ");
    String _name_94 = this._cppExtensions.toName(dto);
    _builder.append(_name_94, "");
    _builder.append("::toMap()");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_10 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_8 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          return Boolean.valueOf(CppGenerator.this._cppExtensions.isLazy(it));
        }
      };
      Iterable<? extends LFeature> _filter_8 = IterableExtensions.filter(_allFeatures_10, _function_8);
      for(final LFeature feature_10 : _filter_8) {
        _builder.append("\t");
        _builder.append("// ");
        String _name_95 = this._cppExtensions.toName(feature_10);
        _builder.append(_name_95, "\t");
        _builder.append(" lazy pointing to ");
        String _typeOrQObject_2 = this._cppExtensions.toTypeOrQObject(feature_10);
        _builder.append(_typeOrQObject_2, "\t");
        _builder.append(" (domainKey: ");
        String _referenceDomainKey_2 = this._cppExtensions.referenceDomainKey(feature_10);
        _builder.append(_referenceDomainKey_2, "\t");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("m");
        String _name_96 = this._cppExtensions.toName(dto);
        String _firstUpper_43 = StringExtensions.toFirstUpper(_name_96);
        _builder.append(_firstUpper_43, "\t");
        _builder.append("Map.insert(");
        String _name_97 = this._cppExtensions.toName(feature_10);
        _builder.append(_name_97, "\t");
        _builder.append("Key, m");
        String _name_98 = this._cppExtensions.toName(feature_10);
        String _firstUpper_44 = StringExtensions.toFirstUpper(_name_98);
        _builder.append(_firstUpper_44, "\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      List<? extends LFeature> _allFeatures_11 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_9 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _isLazy = CppGenerator.this._cppExtensions.isLazy(it);
          return Boolean.valueOf((!_isLazy));
        }
      };
      Iterable<? extends LFeature> _filter_9 = IterableExtensions.filter(_allFeatures_11, _function_9);
      for(final LFeature feature_11 : _filter_9) {
        {
          boolean _isTypeOfDTO_4 = this._cppExtensions.isTypeOfDTO(feature_11);
          if (_isTypeOfDTO_4) {
            {
              boolean _isContained_4 = this._cppExtensions.isContained(feature_11);
              boolean _not = (!_isContained_4);
              if (_not) {
                _builder.append("\t");
                _builder.append("// m");
                String _name_99 = this._cppExtensions.toName(feature_11);
                String _firstUpper_45 = StringExtensions.toFirstUpper(_name_99);
                _builder.append(_firstUpper_45, "\t");
                _builder.append(" points to ");
                String _typeName_17 = this._cppExtensions.toTypeName(feature_11);
                _builder.append(_typeName_17, "\t");
                _builder.append("*");
                _builder.newLineIfNotEmpty();
                {
                  boolean _isToMany_1 = this._cppExtensions.isToMany(feature_11);
                  if (_isToMany_1) {
                    _builder.append("\t");
                    _builder.append("m");
                    String _name_100 = this._cppExtensions.toName(dto);
                    String _firstUpper_46 = StringExtensions.toFirstUpper(_name_100);
                    _builder.append(_firstUpper_46, "\t");
                    _builder.append("Map.insert(");
                    String _name_101 = this._cppExtensions.toName(feature_11);
                    _builder.append(_name_101, "\t");
                    _builder.append("Key, ");
                    String _name_102 = this._cppExtensions.toName(feature_11);
                    _builder.append(_name_102, "\t");
                    _builder.append("AsQVariantList());");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t");
                    _builder.append("if(m");
                    String _name_103 = this._cppExtensions.toName(feature_11);
                    String _firstUpper_47 = StringExtensions.toFirstUpper(_name_103);
                    _builder.append(_firstUpper_47, "\t");
                    _builder.append("){");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("m");
                    String _name_104 = this._cppExtensions.toName(dto);
                    String _firstUpper_48 = StringExtensions.toFirstUpper(_name_104);
                    _builder.append(_firstUpper_48, "\t\t");
                    _builder.append("Map.insert(");
                    String _name_105 = this._cppExtensions.toName(feature_11);
                    _builder.append(_name_105, "\t\t");
                    _builder.append("Key, m");
                    String _name_106 = this._cppExtensions.toName(feature_11);
                    String _firstUpper_49 = StringExtensions.toFirstUpper(_name_106);
                    _builder.append(_firstUpper_49, "\t\t");
                    _builder.append("->to");
                    String _mapOrList = this._cppExtensions.toMapOrList(feature_11);
                    _builder.append(_mapOrList, "\t\t");
                    _builder.append("());");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("}");
                    _builder.newLine();
                  }
                }
              } else {
                _builder.append("\t");
                _builder.append("// m");
                String _name_107 = this._cppExtensions.toName(feature_11);
                String _firstUpper_50 = StringExtensions.toFirstUpper(_name_107);
                _builder.append(_firstUpper_50, "\t");
                _builder.append(" points to ");
                String _typeName_18 = this._cppExtensions.toTypeName(feature_11);
                _builder.append(_typeName_18, "\t");
                _builder.append("* containing ");
                String _name_108 = this._cppExtensions.toName(dto);
                _builder.append(_name_108, "\t");
                _builder.newLineIfNotEmpty();
              }
            }
          } else {
            {
              boolean _isArrayList = this._cppExtensions.isArrayList(feature_11);
              if (_isArrayList) {
                {
                  String _typeName_19 = this._cppExtensions.toTypeName(feature_11);
                  boolean _equals_2 = Objects.equal(_typeName_19, "QString");
                  if (_equals_2) {
                    _builder.append("\t");
                    _builder.append("m");
                    String _name_109 = this._cppExtensions.toName(dto);
                    String _firstUpper_51 = StringExtensions.toFirstUpper(_name_109);
                    _builder.append(_firstUpper_51, "\t");
                    _builder.append("Map.insert(");
                    String _name_110 = this._cppExtensions.toName(feature_11);
                    _builder.append(_name_110, "\t");
                    _builder.append("Key, m");
                    String _name_111 = this._cppExtensions.toName(feature_11);
                    String _firstUpper_52 = StringExtensions.toFirstUpper(_name_111);
                    _builder.append(_firstUpper_52, "\t");
                    _builder.append("StringList);");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t");
                    _builder.append("m");
                    String _name_112 = this._cppExtensions.toName(dto);
                    String _firstUpper_53 = StringExtensions.toFirstUpper(_name_112);
                    _builder.append(_firstUpper_53, "\t");
                    _builder.append("Map.insert(");
                    String _name_113 = this._cppExtensions.toName(feature_11);
                    _builder.append(_name_113, "\t");
                    _builder.append("Key, ");
                    String _name_114 = this._cppExtensions.toName(feature_11);
                    _builder.append(_name_114, "\t");
                    _builder.append("List());");
                    _builder.newLineIfNotEmpty();
                  }
                }
              } else {
                _builder.append("\t");
                _builder.append("m");
                String _name_115 = this._cppExtensions.toName(dto);
                String _firstUpper_54 = StringExtensions.toFirstUpper(_name_115);
                _builder.append(_firstUpper_54, "\t");
                _builder.append("Map.insert(");
                String _name_116 = this._cppExtensions.toName(feature_11);
                _builder.append(_name_116, "\t");
                _builder.append("Key, m");
                String _name_117 = this._cppExtensions.toName(feature_11);
                String _firstUpper_55 = StringExtensions.toFirstUpper(_name_117);
                _builder.append(_firstUpper_55, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("return m");
    String _name_118 = this._cppExtensions.toName(dto);
    String _firstUpper_56 = StringExtensions.toFirstUpper(_name_118);
    _builder.append(_firstUpper_56, "\t");
    _builder.append("Map;");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    {
      boolean _existsServerName_1 = this._cppExtensions.existsServerName(dto);
      if (_existsServerName_1) {
        _builder.append("/*");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* Exports Properties from ");
        String _name_119 = this._cppExtensions.toName(dto);
        _builder.append(_name_119, " ");
        _builder.append(" as QVariantMap");
        _builder.newLineIfNotEmpty();
        _builder.append(" ");
        _builder.append("* To send data as payload to Server");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* Makes it possible to use defferent naming conditions");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("QVariantMap ");
        String _name_120 = this._cppExtensions.toName(dto);
        _builder.append(_name_120, "");
        _builder.append("::toForeignMap()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("QVariantMap foreignMap;");
        _builder.newLine();
        {
          List<? extends LFeature> _allFeatures_12 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_10 = new Function1<LFeature, Boolean>() {
            public Boolean apply(final LFeature it) {
              return Boolean.valueOf(CppGenerator.this._cppExtensions.isLazy(it));
            }
          };
          Iterable<? extends LFeature> _filter_10 = IterableExtensions.filter(_allFeatures_12, _function_10);
          for(final LFeature feature_12 : _filter_10) {
            _builder.append("\t");
            _builder.append("// ");
            String _name_121 = this._cppExtensions.toName(feature_12);
            _builder.append(_name_121, "\t");
            _builder.append(" lazy pointing to ");
            String _typeOrQObject_3 = this._cppExtensions.toTypeOrQObject(feature_12);
            _builder.append(_typeOrQObject_3, "\t");
            _builder.append(" (domainKey: ");
            String _referenceDomainKey_3 = this._cppExtensions.referenceDomainKey(feature_12);
            _builder.append(_referenceDomainKey_3, "\t");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("foreignMap.insert(");
            String _name_122 = this._cppExtensions.toName(feature_12);
            _builder.append(_name_122, "\t");
            _builder.append("ForeignKey, m");
            String _name_123 = this._cppExtensions.toName(feature_12);
            String _firstUpper_57 = StringExtensions.toFirstUpper(_name_123);
            _builder.append(_firstUpper_57, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
          }
        }
        {
          List<? extends LFeature> _allFeatures_13 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_11 = new Function1<LFeature, Boolean>() {
            public Boolean apply(final LFeature it) {
              boolean _isLazy = CppGenerator.this._cppExtensions.isLazy(it);
              return Boolean.valueOf((!_isLazy));
            }
          };
          Iterable<? extends LFeature> _filter_11 = IterableExtensions.filter(_allFeatures_13, _function_11);
          for(final LFeature feature_13 : _filter_11) {
            {
              boolean _isTypeOfDTO_5 = this._cppExtensions.isTypeOfDTO(feature_13);
              if (_isTypeOfDTO_5) {
                {
                  boolean _isContained_5 = this._cppExtensions.isContained(feature_13);
                  boolean _not_1 = (!_isContained_5);
                  if (_not_1) {
                    _builder.append("\t");
                    _builder.append("// m");
                    String _name_124 = this._cppExtensions.toName(feature_13);
                    String _firstUpper_58 = StringExtensions.toFirstUpper(_name_124);
                    _builder.append(_firstUpper_58, "\t");
                    _builder.append(" points to ");
                    String _typeName_20 = this._cppExtensions.toTypeName(feature_13);
                    _builder.append(_typeName_20, "\t");
                    _builder.append("*");
                    _builder.newLineIfNotEmpty();
                    {
                      boolean _isToMany_2 = this._cppExtensions.isToMany(feature_13);
                      if (_isToMany_2) {
                        _builder.append("\t");
                        _builder.append("foreignMap.insert(");
                        String _name_125 = this._cppExtensions.toName(feature_13);
                        _builder.append(_name_125, "\t");
                        _builder.append("ForeignKey, ");
                        String _name_126 = this._cppExtensions.toName(feature_13);
                        _builder.append(_name_126, "\t");
                        _builder.append("AsQVariantList());");
                        _builder.newLineIfNotEmpty();
                      } else {
                        _builder.append("\t");
                        _builder.append("if(m");
                        String _name_127 = this._cppExtensions.toName(feature_13);
                        String _firstUpper_59 = StringExtensions.toFirstUpper(_name_127);
                        _builder.append(_firstUpper_59, "\t");
                        _builder.append("){");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("foreignMap.insert(");
                        String _name_128 = this._cppExtensions.toName(feature_13);
                        _builder.append(_name_128, "\t\t");
                        _builder.append("ForeignKey, m");
                        String _name_129 = this._cppExtensions.toName(feature_13);
                        String _firstUpper_60 = StringExtensions.toFirstUpper(_name_129);
                        _builder.append(_firstUpper_60, "\t\t");
                        _builder.append("->to");
                        String _mapOrList_1 = this._cppExtensions.toMapOrList(feature_13);
                        _builder.append(_mapOrList_1, "\t\t");
                        _builder.append("());");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("}");
                        _builder.newLine();
                      }
                    }
                  } else {
                    _builder.append("\t");
                    _builder.append("// m");
                    String _name_130 = this._cppExtensions.toName(feature_13);
                    String _firstUpper_61 = StringExtensions.toFirstUpper(_name_130);
                    _builder.append(_firstUpper_61, "\t");
                    _builder.append(" points to ");
                    String _typeName_21 = this._cppExtensions.toTypeName(feature_13);
                    _builder.append(_typeName_21, "\t");
                    _builder.append("* containing ");
                    String _name_131 = this._cppExtensions.toName(dto);
                    _builder.append(_name_131, "\t");
                    _builder.newLineIfNotEmpty();
                  }
                }
              } else {
                {
                  boolean _isArrayList_1 = this._cppExtensions.isArrayList(feature_13);
                  if (_isArrayList_1) {
                    {
                      String _typeName_22 = this._cppExtensions.toTypeName(feature_13);
                      boolean _equals_3 = Objects.equal(_typeName_22, "QString");
                      if (_equals_3) {
                        _builder.append("\t");
                        _builder.append("foreignMap.insert(");
                        String _name_132 = this._cppExtensions.toName(feature_13);
                        _builder.append(_name_132, "\t");
                        _builder.append("ForeignKey, m");
                        String _name_133 = this._cppExtensions.toName(feature_13);
                        String _firstUpper_62 = StringExtensions.toFirstUpper(_name_133);
                        _builder.append(_firstUpper_62, "\t");
                        _builder.append("StringList);");
                        _builder.newLineIfNotEmpty();
                      } else {
                        _builder.append("\t");
                        _builder.append("foreignMap.insert(");
                        String _name_134 = this._cppExtensions.toName(feature_13);
                        _builder.append(_name_134, "\t");
                        _builder.append("ForeignKey, ");
                        String _name_135 = this._cppExtensions.toName(feature_13);
                        _builder.append(_name_135, "\t");
                        _builder.append("List());");
                        _builder.newLineIfNotEmpty();
                      }
                    }
                  } else {
                    _builder.append("\t");
                    _builder.append("foreignMap.insert(");
                    String _name_136 = this._cppExtensions.toName(feature_13);
                    _builder.append(_name_136, "\t");
                    _builder.append("ForeignKey, m");
                    String _name_137 = this._cppExtensions.toName(feature_13);
                    String _firstUpper_63 = StringExtensions.toFirstUpper(_name_137);
                    _builder.append(_firstUpper_63, "\t");
                    _builder.append(");");
                    _builder.newLineIfNotEmpty();
                  }
                }
              }
            }
          }
        }
        _builder.append("\t");
        _builder.append("return foreignMap;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.newLine();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Exports Properties from ");
    String _name_138 = this._cppExtensions.toName(dto);
    _builder.append(_name_138, " ");
    _builder.append(" as QVariantMap");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("* transient properties are excluded");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* To export ALL data use toMap()");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("QVariantMap ");
    String _name_139 = this._cppExtensions.toName(dto);
    _builder.append(_name_139, "");
    _builder.append("::dataToPersist()");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("QVariantMap persistMap;");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_14 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_12 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _and = false;
          boolean _isTransient = CppGenerator.this._cppExtensions.isTransient(it);
          boolean _not = (!_isTransient);
          if (!_not) {
            _and = false;
          } else {
            boolean _isLazy = CppGenerator.this._cppExtensions.isLazy(it);
            _and = _isLazy;
          }
          return Boolean.valueOf(_and);
        }
      };
      Iterable<? extends LFeature> _filter_12 = IterableExtensions.filter(_allFeatures_14, _function_12);
      for(final LFeature feature_14 : _filter_12) {
        _builder.append("\t");
        _builder.append("// ");
        String _name_140 = this._cppExtensions.toName(feature_14);
        _builder.append(_name_140, "\t");
        _builder.append(" lazy pointing to ");
        String _typeOrQObject_4 = this._cppExtensions.toTypeOrQObject(feature_14);
        _builder.append(_typeOrQObject_4, "\t");
        _builder.append(" (domainKey: ");
        String _referenceDomainKey_4 = this._cppExtensions.referenceDomainKey(feature_14);
        _builder.append(_referenceDomainKey_4, "\t");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("persistMap.insert(");
        String _name_141 = this._cppExtensions.toName(feature_14);
        _builder.append(_name_141, "\t");
        _builder.append("Key, m");
        String _name_142 = this._cppExtensions.toName(feature_14);
        String _firstUpper_64 = StringExtensions.toFirstUpper(_name_142);
        _builder.append(_firstUpper_64, "\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      List<? extends LFeature> _allFeatures_15 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_13 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _and = false;
          boolean _isTransient = CppGenerator.this._cppExtensions.isTransient(it);
          boolean _not = (!_isTransient);
          if (!_not) {
            _and = false;
          } else {
            boolean _isLazy = CppGenerator.this._cppExtensions.isLazy(it);
            boolean _not_1 = (!_isLazy);
            _and = _not_1;
          }
          return Boolean.valueOf(_and);
        }
      };
      Iterable<? extends LFeature> _filter_13 = IterableExtensions.filter(_allFeatures_15, _function_13);
      for(final LFeature feature_15 : _filter_13) {
        {
          boolean _isTypeOfDTO_6 = this._cppExtensions.isTypeOfDTO(feature_15);
          if (_isTypeOfDTO_6) {
            {
              boolean _isContained_6 = this._cppExtensions.isContained(feature_15);
              boolean _not_2 = (!_isContained_6);
              if (_not_2) {
                _builder.append("\t");
                _builder.append("// m");
                String _name_143 = this._cppExtensions.toName(feature_15);
                String _firstUpper_65 = StringExtensions.toFirstUpper(_name_143);
                _builder.append(_firstUpper_65, "\t");
                _builder.append(" points to ");
                String _typeName_23 = this._cppExtensions.toTypeName(feature_15);
                _builder.append(_typeName_23, "\t");
                _builder.append("*");
                _builder.newLineIfNotEmpty();
                {
                  boolean _isToMany_3 = this._cppExtensions.isToMany(feature_15);
                  if (_isToMany_3) {
                    _builder.append("\t");
                    _builder.append("persistMap.insert(");
                    String _name_144 = this._cppExtensions.toName(feature_15);
                    _builder.append(_name_144, "\t");
                    _builder.append("Key, ");
                    String _name_145 = this._cppExtensions.toName(feature_15);
                    _builder.append(_name_145, "\t");
                    _builder.append("AsQVariantList());");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t");
                    _builder.append("if(m");
                    String _name_146 = this._cppExtensions.toName(feature_15);
                    String _firstUpper_66 = StringExtensions.toFirstUpper(_name_146);
                    _builder.append(_firstUpper_66, "\t");
                    _builder.append("){");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("persistMap.insert(");
                    String _name_147 = this._cppExtensions.toName(feature_15);
                    _builder.append(_name_147, "\t\t");
                    _builder.append("Key, m");
                    String _name_148 = this._cppExtensions.toName(feature_15);
                    String _firstUpper_67 = StringExtensions.toFirstUpper(_name_148);
                    _builder.append(_firstUpper_67, "\t\t");
                    _builder.append("->to");
                    String _mapOrList_2 = this._cppExtensions.toMapOrList(feature_15);
                    _builder.append(_mapOrList_2, "\t\t");
                    _builder.append("());");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("}");
                    _builder.newLine();
                  }
                }
              } else {
                _builder.append("\t");
                _builder.append("// m");
                String _name_149 = this._cppExtensions.toName(feature_15);
                String _firstUpper_68 = StringExtensions.toFirstUpper(_name_149);
                _builder.append(_firstUpper_68, "\t");
                _builder.append(" points to ");
                String _typeName_24 = this._cppExtensions.toTypeName(feature_15);
                _builder.append(_typeName_24, "\t");
                _builder.append("* containing ");
                String _name_150 = this._cppExtensions.toName(dto);
                _builder.append(_name_150, "\t");
                _builder.newLineIfNotEmpty();
              }
            }
          } else {
            {
              boolean _isArrayList_2 = this._cppExtensions.isArrayList(feature_15);
              if (_isArrayList_2) {
                {
                  String _typeName_25 = this._cppExtensions.toTypeName(feature_15);
                  boolean _equals_4 = Objects.equal(_typeName_25, "QString");
                  if (_equals_4) {
                    _builder.append("\t");
                    _builder.append("persistMap.insert(");
                    String _name_151 = this._cppExtensions.toName(feature_15);
                    _builder.append(_name_151, "\t");
                    _builder.append("Key, m");
                    String _name_152 = this._cppExtensions.toName(feature_15);
                    String _firstUpper_69 = StringExtensions.toFirstUpper(_name_152);
                    _builder.append(_firstUpper_69, "\t");
                    _builder.append("StringList);");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t");
                    _builder.append("persistMap.insert(");
                    String _name_153 = this._cppExtensions.toName(feature_15);
                    _builder.append(_name_153, "\t");
                    _builder.append("Key, ");
                    String _name_154 = this._cppExtensions.toName(feature_15);
                    _builder.append(_name_154, "\t");
                    _builder.append("List());");
                    _builder.newLineIfNotEmpty();
                  }
                }
              } else {
                _builder.append("\t");
                _builder.append("persistMap.insert(");
                String _name_155 = this._cppExtensions.toName(feature_15);
                _builder.append(_name_155, "\t");
                _builder.append("Key, m");
                String _name_156 = this._cppExtensions.toName(feature_15);
                String _firstUpper_70 = StringExtensions.toFirstUpper(_name_156);
                _builder.append(_firstUpper_70, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    {
      List<? extends LFeature> _allFeatures_16 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_14 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          return Boolean.valueOf(CppGenerator.this._cppExtensions.isTransient(it));
        }
      };
      Iterable<? extends LFeature> _filter_14 = IterableExtensions.filter(_allFeatures_16, _function_14);
      for(final LFeature feature_16 : _filter_14) {
        _builder.append("\t");
        _builder.append("// excluded: m");
        String _name_157 = this._cppExtensions.toName(feature_16);
        String _firstUpper_71 = StringExtensions.toFirstUpper(_name_157);
        _builder.append(_firstUpper_71, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("return persistMap;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_17 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_15 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _and = false;
          boolean _isToMany = CppGenerator.this._cppExtensions.isToMany(it);
          boolean _not = (!_isToMany);
          if (!_not) {
            _and = false;
          } else {
            boolean _isLazy = CppGenerator.this._cppExtensions.isLazy(it);
            _and = _isLazy;
          }
          return Boolean.valueOf(_and);
        }
      };
      Iterable<? extends LFeature> _filter_15 = IterableExtensions.filter(_allFeatures_17, _function_15);
      for(final LFeature feature_17 : _filter_15) {
        CharSequence _foo = this.foo(feature_17);
        _builder.append(_foo, "");
        _builder.newLineIfNotEmpty();
        _builder.append("// ");
        String _name_158 = this._cppExtensions.toName(feature_17);
        _builder.append(_name_158, "");
        _builder.append(" lazy pointing to ");
        String _typeOrQObject_5 = this._cppExtensions.toTypeOrQObject(feature_17);
        _builder.append(_typeOrQObject_5, "");
        _builder.append(" (domainKey: ");
        String _referenceDomainKey_5 = this._cppExtensions.referenceDomainKey(feature_17);
        _builder.append(_referenceDomainKey_5, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        String _referenceDomainKeyType_3 = this._cppExtensions.referenceDomainKeyType(feature_17);
        _builder.append(_referenceDomainKeyType_3, "");
        _builder.append(" ");
        String _name_159 = this._cppExtensions.toName(dto);
        _builder.append(_name_159, "");
        _builder.append("::");
        String _name_160 = this._cppExtensions.toName(feature_17);
        _builder.append(_name_160, "");
        _builder.append("() const");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return m");
        String _name_161 = this._cppExtensions.toName(feature_17);
        String _firstUpper_72 = StringExtensions.toFirstUpper(_name_161);
        _builder.append(_firstUpper_72, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        String _typeOrQObject_6 = this._cppExtensions.toTypeOrQObject(feature_17);
        _builder.append(_typeOrQObject_6, "");
        _builder.append(" ");
        String _name_162 = this._cppExtensions.toName(dto);
        _builder.append(_name_162, "");
        _builder.append("::");
        String _name_163 = this._cppExtensions.toName(feature_17);
        _builder.append(_name_163, "");
        _builder.append("AsDTO() const");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return m");
        String _name_164 = this._cppExtensions.toName(feature_17);
        String _firstUpper_73 = StringExtensions.toFirstUpper(_name_164);
        _builder.append(_firstUpper_73, "\t");
        _builder.append("AsDTO;");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_165 = this._cppExtensions.toName(dto);
        _builder.append(_name_165, "");
        _builder.append("::set");
        String _name_166 = this._cppExtensions.toName(feature_17);
        String _firstUpper_74 = StringExtensions.toFirstUpper(_name_166);
        _builder.append(_firstUpper_74, "");
        _builder.append("(");
        String _referenceDomainKeyType_4 = this._cppExtensions.referenceDomainKeyType(feature_17);
        _builder.append(_referenceDomainKeyType_4, "");
        _builder.append(" ");
        String _name_167 = this._cppExtensions.toName(feature_17);
        _builder.append(_name_167, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (");
        String _name_168 = this._cppExtensions.toName(feature_17);
        _builder.append(_name_168, "\t");
        _builder.append(" != m");
        String _name_169 = this._cppExtensions.toName(feature_17);
        String _firstUpper_75 = StringExtensions.toFirstUpper(_name_169);
        _builder.append(_firstUpper_75, "\t");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("if (");
        String _name_170 = this._cppExtensions.toName(feature_17);
        _builder.append(_name_170, "\t\t");
        _builder.append(" != ");
        String _referenceDomainKeyType_5 = this._cppExtensions.referenceDomainKeyType(feature_17);
        String _defaultForLazyTypeName_2 = this._cppExtensions.defaultForLazyTypeName(_referenceDomainKeyType_5);
        _builder.append(_defaultForLazyTypeName_2, "\t\t");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("// connected from DTOManager to lookup for DTO");
        _builder.newLine();
        _builder.append("            ");
        _builder.append("emit request");
        String _name_171 = this._cppExtensions.toName(feature_17);
        String _firstUpper_76 = StringExtensions.toFirstUpper(_name_171);
        _builder.append(_firstUpper_76, "            ");
        _builder.append("AsDTO(");
        String _name_172 = this._cppExtensions.toName(feature_17);
        _builder.append(_name_172, "            ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("} else {");
        _builder.newLine();
        _builder.append("            ");
        _builder.append("// reset pointer, don\'t delete the independent object !");
        _builder.newLine();
        _builder.append("            ");
        _builder.append("m");
        String _name_173 = this._cppExtensions.toName(feature_17);
        String _firstUpper_77 = StringExtensions.toFirstUpper(_name_173);
        _builder.append(_firstUpper_77, "            ");
        _builder.append("AsDTO = 0;");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("emit ");
        String _name_174 = this._cppExtensions.toName(feature_17);
        _builder.append(_name_174, "            ");
        _builder.append("Removed(m");
        String _name_175 = this._cppExtensions.toName(feature_17);
        String _firstUpper_78 = StringExtensions.toFirstUpper(_name_175);
        _builder.append(_firstUpper_78, "            ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("m");
        String _name_176 = this._cppExtensions.toName(feature_17);
        String _firstUpper_79 = StringExtensions.toFirstUpper(_name_176);
        _builder.append(_firstUpper_79, "        ");
        _builder.append(" = ");
        String _name_177 = this._cppExtensions.toName(feature_17);
        _builder.append(_name_177, "        ");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("emit ");
        String _name_178 = this._cppExtensions.toName(feature_17);
        _builder.append(_name_178, "        ");
        _builder.append("Changed(");
        String _name_179 = this._cppExtensions.toName(feature_17);
        _builder.append(_name_179, "        ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_180 = this._cppExtensions.toName(dto);
        _builder.append(_name_180, "");
        _builder.append("::remove");
        String _name_181 = this._cppExtensions.toName(feature_17);
        String _firstUpper_80 = StringExtensions.toFirstUpper(_name_181);
        _builder.append(_firstUpper_80, "");
        _builder.append("()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("set");
        String _name_182 = this._cppExtensions.toName(feature_17);
        String _firstUpper_81 = StringExtensions.toFirstUpper(_name_182);
        _builder.append(_firstUpper_81, "\t");
        _builder.append("(");
        String _referenceDomainKeyType_6 = this._cppExtensions.referenceDomainKeyType(feature_17);
        String _defaultForLazyTypeName_3 = this._cppExtensions.defaultForLazyTypeName(_referenceDomainKeyType_6);
        _builder.append(_defaultForLazyTypeName_3, "\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("bool ");
        String _name_183 = this._cppExtensions.toName(dto);
        _builder.append(_name_183, "");
        _builder.append("::has");
        String _name_184 = this._cppExtensions.toName(feature_17);
        String _firstUpper_82 = StringExtensions.toFirstUpper(_name_184);
        _builder.append(_firstUpper_82, "");
        _builder.append("(){");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("if(m");
        String _name_185 = this._cppExtensions.toName(feature_17);
        String _firstUpper_83 = StringExtensions.toFirstUpper(_name_185);
        _builder.append(_firstUpper_83, "    ");
        _builder.append(" != ");
        String _referenceDomainKeyType_7 = this._cppExtensions.referenceDomainKeyType(feature_17);
        String _defaultForLazyTypeName_4 = this._cppExtensions.defaultForLazyTypeName(_referenceDomainKeyType_7);
        _builder.append(_defaultForLazyTypeName_4, "    ");
        _builder.append("){");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("return true;");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("} else {");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("return false;");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("bool ");
        String _name_186 = this._cppExtensions.toName(dto);
        _builder.append(_name_186, "");
        _builder.append("::has");
        String _name_187 = this._cppExtensions.toName(feature_17);
        String _firstUpper_84 = StringExtensions.toFirstUpper(_name_187);
        _builder.append(_firstUpper_84, "");
        _builder.append("AsDTO(){");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("if(m");
        String _name_188 = this._cppExtensions.toName(feature_17);
        String _firstUpper_85 = StringExtensions.toFirstUpper(_name_188);
        _builder.append(_firstUpper_85, "    ");
        _builder.append("AsDTO){");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("return true;");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("} else {");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("return false;");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("// SLOT");
        _builder.newLine();
        _builder.append("void ");
        String _name_189 = this._cppExtensions.toName(dto);
        _builder.append(_name_189, "");
        _builder.append("::onRequested");
        String _name_190 = this._cppExtensions.toName(feature_17);
        String _firstUpper_86 = StringExtensions.toFirstUpper(_name_190);
        _builder.append(_firstUpper_86, "");
        _builder.append("AsDTO(");
        String _typeOrQObject_7 = this._cppExtensions.toTypeOrQObject(feature_17);
        _builder.append(_typeOrQObject_7, "");
        _builder.append(" ");
        String _typeName_26 = this._cppExtensions.toTypeName(feature_17);
        String _firstLower_21 = StringExtensions.toFirstLower(_typeName_26);
        _builder.append(_firstLower_21, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("if (");
        String _typeName_27 = this._cppExtensions.toTypeName(feature_17);
        String _firstLower_22 = StringExtensions.toFirstLower(_typeName_27);
        _builder.append(_firstLower_22, "    ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("if (");
        String _typeName_28 = this._cppExtensions.toTypeName(feature_17);
        String _firstLower_23 = StringExtensions.toFirstLower(_typeName_28);
        _builder.append(_firstLower_23, "        ");
        _builder.append("->");
        String _referenceDomainKey_6 = this._cppExtensions.referenceDomainKey(feature_17);
        _builder.append(_referenceDomainKey_6, "        ");
        _builder.append("() == m");
        String _name_191 = this._cppExtensions.toName(feature_17);
        String _firstUpper_87 = StringExtensions.toFirstUpper(_name_191);
        _builder.append(_firstUpper_87, "        ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("m");
        String _name_192 = this._cppExtensions.toName(feature_17);
        String _firstUpper_88 = StringExtensions.toFirstUpper(_name_192);
        _builder.append(_firstUpper_88, "            ");
        _builder.append("AsDTO = ");
        String _typeName_29 = this._cppExtensions.toTypeName(feature_17);
        String _firstLower_24 = StringExtensions.toFirstLower(_typeName_29);
        _builder.append(_firstLower_24, "            ");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    {
      List<? extends LFeature> _allFeatures_18 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_16 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _and = false;
          boolean _isToMany = CppGenerator.this._cppExtensions.isToMany(it);
          boolean _not = (!_isToMany);
          if (!_not) {
            _and = false;
          } else {
            boolean _isLazy = CppGenerator.this._cppExtensions.isLazy(it);
            boolean _not_1 = (!_isLazy);
            _and = _not_1;
          }
          return Boolean.valueOf(_and);
        }
      };
      Iterable<? extends LFeature> _filter_16 = IterableExtensions.filter(_allFeatures_18, _function_16);
      for(final LFeature feature_18 : _filter_16) {
        CharSequence _foo_1 = this.foo(feature_18);
        _builder.append(_foo_1, "");
        _builder.newLineIfNotEmpty();
        {
          boolean _and_2 = false;
          boolean _isTypeOfDTO_7 = this._cppExtensions.isTypeOfDTO(feature_18);
          if (!_isTypeOfDTO_7) {
            _and_2 = false;
          } else {
            boolean _isContained_7 = this._cppExtensions.isContained(feature_18);
            _and_2 = _isContained_7;
          }
          if (_and_2) {
            _builder.append("// No SETTER for ");
            String _name_193 = this._cppExtensions.toName(feature_18);
            String _firstUpper_89 = StringExtensions.toFirstUpper(_name_193);
            _builder.append(_firstUpper_89, "");
            _builder.append(" - it\'s the parent");
            _builder.newLineIfNotEmpty();
            String _typeOrQObject_8 = this._cppExtensions.toTypeOrQObject(feature_18);
            _builder.append(_typeOrQObject_8, "");
            _builder.append(" ");
            String _name_194 = this._cppExtensions.toName(dto);
            _builder.append(_name_194, "");
            _builder.append("::");
            String _name_195 = this._cppExtensions.toName(feature_18);
            _builder.append(_name_195, "");
            _builder.append("() const");
            _builder.newLineIfNotEmpty();
            _builder.append("{");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("return qobject_cast<");
            String _typeOrQObject_9 = this._cppExtensions.toTypeOrQObject(feature_18);
            _builder.append(_typeOrQObject_9, "\t");
            _builder.append(">(parent());");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
          } else {
            boolean _isEnum_1 = this._cppExtensions.isEnum(feature_18);
            if (_isEnum_1) {
              _builder.append("int ");
              String _name_196 = this._cppExtensions.toName(dto);
              _builder.append(_name_196, "");
              _builder.append("::");
              String _name_197 = this._cppExtensions.toName(feature_18);
              _builder.append(_name_197, "");
              _builder.append("() const");
              _builder.newLineIfNotEmpty();
              _builder.append("{");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("return m");
              String _name_198 = this._cppExtensions.toName(feature_18);
              String _firstUpper_90 = StringExtensions.toFirstUpper(_name_198);
              _builder.append(_firstUpper_90, "\t");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
              _builder.append("}");
              _builder.newLine();
              _builder.append("void ");
              String _name_199 = this._cppExtensions.toName(dto);
              _builder.append(_name_199, "");
              _builder.append("::set");
              String _name_200 = this._cppExtensions.toName(feature_18);
              String _firstUpper_91 = StringExtensions.toFirstUpper(_name_200);
              _builder.append(_firstUpper_91, "");
              _builder.append("(int ");
              String _name_201 = this._cppExtensions.toName(feature_18);
              _builder.append(_name_201, "");
              _builder.append(")");
              _builder.newLineIfNotEmpty();
              _builder.append("{");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("if (");
              String _name_202 = this._cppExtensions.toName(feature_18);
              _builder.append(_name_202, "\t");
              _builder.append(" != m");
              String _name_203 = this._cppExtensions.toName(feature_18);
              String _firstUpper_92 = StringExtensions.toFirstUpper(_name_203);
              _builder.append(_firstUpper_92, "\t");
              _builder.append(") {");
              _builder.newLineIfNotEmpty();
              _builder.append("\t\t");
              _builder.append("m");
              String _name_204 = this._cppExtensions.toName(feature_18);
              String _firstUpper_93 = StringExtensions.toFirstUpper(_name_204);
              _builder.append(_firstUpper_93, "\t\t");
              _builder.append(" = ");
              String _name_205 = this._cppExtensions.toName(feature_18);
              _builder.append(_name_205, "\t\t");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
              _builder.append("\t\t");
              _builder.append("emit ");
              String _name_206 = this._cppExtensions.toName(feature_18);
              _builder.append(_name_206, "\t\t");
              _builder.append("Changed(");
              String _name_207 = this._cppExtensions.toName(feature_18);
              _builder.append(_name_207, "\t\t");
              _builder.append(");");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("}");
              _builder.newLine();
              _builder.append("}");
              _builder.newLine();
              _builder.append("void ");
              String _name_208 = this._cppExtensions.toName(dto);
              _builder.append(_name_208, "");
              _builder.append("::set");
              String _name_209 = this._cppExtensions.toName(feature_18);
              String _firstUpper_94 = StringExtensions.toFirstUpper(_name_209);
              _builder.append(_firstUpper_94, "");
              _builder.append("(QString ");
              String _name_210 = this._cppExtensions.toName(feature_18);
              _builder.append(_name_210, "");
              _builder.append(")");
              _builder.newLineIfNotEmpty();
              _builder.append("{");
              _builder.newLine();
              _builder.append("    ");
              _builder.append("set");
              String _name_211 = this._cppExtensions.toName(feature_18);
              String _firstUpper_95 = StringExtensions.toFirstUpper(_name_211);
              _builder.append(_firstUpper_95, "    ");
              _builder.append("(");
              String _name_212 = this._cppExtensions.toName(feature_18);
              _builder.append(_name_212, "    ");
              _builder.append("StringToInt(");
              String _name_213 = this._cppExtensions.toName(feature_18);
              _builder.append(_name_213, "    ");
              _builder.append("));");
              _builder.newLineIfNotEmpty();
              _builder.append("}");
              _builder.newLine();
              _builder.append("int ");
              String _name_214 = this._cppExtensions.toName(dto);
              _builder.append(_name_214, "");
              _builder.append("::");
              String _name_215 = this._cppExtensions.toName(feature_18);
              _builder.append(_name_215, "");
              _builder.append("StringToInt(QString ");
              String _name_216 = this._cppExtensions.toName(feature_18);
              _builder.append(_name_216, "");
              _builder.append(")");
              _builder.newLineIfNotEmpty();
              _builder.append("{");
              _builder.newLine();
              _builder.append("    ");
              _builder.append("if (");
              String _name_217 = this._cppExtensions.toName(feature_18);
              _builder.append(_name_217, "    ");
              _builder.append(".isNull() || ");
              String _name_218 = this._cppExtensions.toName(feature_18);
              _builder.append(_name_218, "    ");
              _builder.append(".isEmpty()) {");
              _builder.newLineIfNotEmpty();
              _builder.append("        ");
              _builder.append("return ");
              String _typeName_30 = this._cppExtensions.toTypeName(feature_18);
              _builder.append(_typeName_30, "        ");
              _builder.append("::NO_VALUE;");
              _builder.newLineIfNotEmpty();
              _builder.append("    ");
              _builder.append("}");
              _builder.newLine();
              {
                LEnum _enumFromAttributeType = this._cppExtensions.enumFromAttributeType(feature_18);
                EList<LEnumLiteral> _literals = _enumFromAttributeType.getLiterals();
                for(final LEnumLiteral literal : _literals) {
                  _builder.append("    ");
                  _builder.append("if (");
                  String _name_219 = this._cppExtensions.toName(feature_18);
                  _builder.append(_name_219, "    ");
                  _builder.append(" == \"");
                  String _name_220 = literal.getName();
                  _builder.append(_name_220, "    ");
                  _builder.append("\") {");
                  _builder.newLineIfNotEmpty();
                  _builder.append("    ");
                  _builder.append("    ");
                  _builder.append("return ");
                  LEnum _enumFromAttributeType_1 = this._cppExtensions.enumFromAttributeType(feature_18);
                  EList<LEnumLiteral> _literals_1 = _enumFromAttributeType_1.getLiterals();
                  int _indexOf = _literals_1.indexOf(literal);
                  _builder.append(_indexOf, "        ");
                  _builder.append(";");
                  _builder.newLineIfNotEmpty();
                  _builder.append("    ");
                  _builder.append("}");
                  _builder.newLine();
                }
              }
              _builder.append("    ");
              _builder.append("qWarning() << \"");
              String _name_221 = this._cppExtensions.toName(feature_18);
              _builder.append(_name_221, "    ");
              _builder.append(" wrong enumValue as String: \" << ");
              String _name_222 = this._cppExtensions.toName(feature_18);
              _builder.append(_name_222, "    ");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
              _builder.append("    ");
              _builder.append("return ");
              String _typeName_31 = this._cppExtensions.toTypeName(feature_18);
              _builder.append(_typeName_31, "    ");
              _builder.append("::NO_VALUE;");
              _builder.newLineIfNotEmpty();
              _builder.append("}");
              _builder.newLine();
            } else {
              String _typeOrQObject_10 = this._cppExtensions.toTypeOrQObject(feature_18);
              _builder.append(_typeOrQObject_10, "");
              _builder.append(" ");
              String _name_223 = this._cppExtensions.toName(dto);
              _builder.append(_name_223, "");
              _builder.append("::");
              String _name_224 = this._cppExtensions.toName(feature_18);
              _builder.append(_name_224, "");
              _builder.append("() const");
              _builder.newLineIfNotEmpty();
              _builder.append("{");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("return m");
              String _name_225 = this._cppExtensions.toName(feature_18);
              String _firstUpper_96 = StringExtensions.toFirstUpper(_name_225);
              _builder.append(_firstUpper_96, "\t");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
              _builder.append("}");
              _builder.newLine();
              _builder.append("void ");
              String _name_226 = this._cppExtensions.toName(dto);
              _builder.append(_name_226, "");
              _builder.append("::set");
              String _name_227 = this._cppExtensions.toName(feature_18);
              String _firstUpper_97 = StringExtensions.toFirstUpper(_name_227);
              _builder.append(_firstUpper_97, "");
              _builder.append("(");
              String _typeOrQObject_11 = this._cppExtensions.toTypeOrQObject(feature_18);
              _builder.append(_typeOrQObject_11, "");
              _builder.append(" ");
              String _name_228 = this._cppExtensions.toName(feature_18);
              _builder.append(_name_228, "");
              _builder.append(")");
              _builder.newLineIfNotEmpty();
              _builder.append("{");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("if (");
              String _name_229 = this._cppExtensions.toName(feature_18);
              _builder.append(_name_229, "\t");
              _builder.append(" != m");
              String _name_230 = this._cppExtensions.toName(feature_18);
              String _firstUpper_98 = StringExtensions.toFirstUpper(_name_230);
              _builder.append(_firstUpper_98, "\t");
              _builder.append(") {");
              _builder.newLineIfNotEmpty();
              {
                boolean _isTypeOfDTO_8 = this._cppExtensions.isTypeOfDTO(feature_18);
                if (_isTypeOfDTO_8) {
                  _builder.append("\t\t");
                  _builder.append("if (m");
                  String _name_231 = this._cppExtensions.toName(feature_18);
                  String _firstUpper_99 = StringExtensions.toFirstUpper(_name_231);
                  _builder.append(_firstUpper_99, "\t\t");
                  _builder.append("){");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t\t");
                  _builder.append("\t");
                  _builder.append("m");
                  String _name_232 = this._cppExtensions.toName(feature_18);
                  String _firstUpper_100 = StringExtensions.toFirstUpper(_name_232);
                  _builder.append(_firstUpper_100, "\t\t\t");
                  _builder.append("->deleteLater();");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t\t");
                  _builder.append("}");
                  _builder.newLine();
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_233 = this._cppExtensions.toName(feature_18);
                  String _firstUpper_101 = StringExtensions.toFirstUpper(_name_233);
                  _builder.append(_firstUpper_101, "\t\t");
                  _builder.append(" = ");
                  String _name_234 = this._cppExtensions.toName(feature_18);
                  _builder.append(_name_234, "\t\t");
                  _builder.append(";");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_235 = this._cppExtensions.toName(feature_18);
                  String _firstUpper_102 = StringExtensions.toFirstUpper(_name_235);
                  _builder.append(_firstUpper_102, "\t\t");
                  _builder.append("->setParent(this);");
                  _builder.newLineIfNotEmpty();
                } else {
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_236 = this._cppExtensions.toName(feature_18);
                  String _firstUpper_103 = StringExtensions.toFirstUpper(_name_236);
                  _builder.append(_firstUpper_103, "\t\t");
                  _builder.append(" = ");
                  String _name_237 = this._cppExtensions.toName(feature_18);
                  _builder.append(_name_237, "\t\t");
                  _builder.append(";");
                  _builder.newLineIfNotEmpty();
                }
              }
              _builder.append("\t\t");
              _builder.append("emit ");
              String _name_238 = this._cppExtensions.toName(feature_18);
              _builder.append(_name_238, "\t\t");
              _builder.append("Changed(");
              String _name_239 = this._cppExtensions.toName(feature_18);
              _builder.append(_name_239, "\t\t");
              _builder.append(");");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("}");
              _builder.newLine();
              _builder.append("}");
              _builder.newLine();
              {
                boolean _isTypeOfDTO_9 = this._cppExtensions.isTypeOfDTO(feature_18);
                if (_isTypeOfDTO_9) {
                  _builder.append("void ");
                  String _name_240 = this._cppExtensions.toName(dto);
                  _builder.append(_name_240, "");
                  _builder.append("::delete");
                  String _name_241 = this._cppExtensions.toName(feature_18);
                  String _firstUpper_104 = StringExtensions.toFirstUpper(_name_241);
                  _builder.append(_firstUpper_104, "");
                  _builder.append("()");
                  _builder.newLineIfNotEmpty();
                  _builder.append("{");
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("if (m");
                  String _name_242 = this._cppExtensions.toName(feature_18);
                  String _firstUpper_105 = StringExtensions.toFirstUpper(_name_242);
                  _builder.append(_firstUpper_105, "\t");
                  _builder.append("){");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t\t");
                  _builder.append("emit ");
                  String _name_243 = this._cppExtensions.toName(feature_18);
                  String _firstLower_25 = StringExtensions.toFirstLower(_name_243);
                  _builder.append(_firstLower_25, "\t\t");
                  _builder.append("Deleted(m");
                  String _name_244 = this._cppExtensions.toName(feature_18);
                  String _firstUpper_106 = StringExtensions.toFirstUpper(_name_244);
                  _builder.append(_firstUpper_106, "\t\t");
                  _builder.append("->uuid());");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_245 = this._cppExtensions.toName(feature_18);
                  String _firstUpper_107 = StringExtensions.toFirstUpper(_name_245);
                  _builder.append(_firstUpper_107, "\t\t");
                  _builder.append("->deleteLater();");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_246 = this._cppExtensions.toName(feature_18);
                  String _firstUpper_108 = StringExtensions.toFirstUpper(_name_246);
                  _builder.append(_firstUpper_108, "\t\t");
                  _builder.append(" = 0;");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("}");
                  _builder.newLine();
                  _builder.append("}");
                  _builder.newLine();
                  _builder.append("bool ");
                  String _name_247 = this._cppExtensions.toName(dto);
                  _builder.append(_name_247, "");
                  _builder.append("::has");
                  String _name_248 = this._cppExtensions.toName(feature_18);
                  String _firstUpper_109 = StringExtensions.toFirstUpper(_name_248);
                  _builder.append(_firstUpper_109, "");
                  _builder.append("(){");
                  _builder.newLineIfNotEmpty();
                  _builder.append("    ");
                  _builder.append("if(m");
                  String _name_249 = this._cppExtensions.toName(feature_18);
                  String _firstUpper_110 = StringExtensions.toFirstUpper(_name_249);
                  _builder.append(_firstUpper_110, "    ");
                  _builder.append("){");
                  _builder.newLineIfNotEmpty();
                  _builder.append("        ");
                  _builder.append("return true;");
                  _builder.newLine();
                  _builder.append("    ");
                  _builder.append("} else {");
                  _builder.newLine();
                  _builder.append("        ");
                  _builder.append("return false;");
                  _builder.newLine();
                  _builder.append("    ");
                  _builder.append("}");
                  _builder.newLine();
                  _builder.append("}");
                  _builder.newLine();
                }
              }
            }
          }
        }
        {
          boolean _isTypeOfDates = this._cppExtensions.isTypeOfDates(feature_18);
          if (_isTypeOfDates) {
            _builder.append("bool ");
            String _name_250 = this._cppExtensions.toName(dto);
            _builder.append(_name_250, "");
            _builder.append("::has");
            String _name_251 = this._cppExtensions.toName(feature_18);
            String _firstUpper_111 = StringExtensions.toFirstUpper(_name_251);
            _builder.append(_firstUpper_111, "");
            _builder.append("(){");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("return !m");
            String _name_252 = this._cppExtensions.toName(feature_18);
            String _firstUpper_112 = StringExtensions.toFirstUpper(_name_252);
            _builder.append(_firstUpper_112, "\t");
            _builder.append(".isNull() && m");
            String _name_253 = this._cppExtensions.toName(feature_18);
            String _firstUpper_113 = StringExtensions.toFirstUpper(_name_253);
            _builder.append(_firstUpper_113, "\t");
            _builder.append(".isValid();");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    {
      List<? extends LFeature> _allFeatures_19 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_17 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _and = false;
          boolean _isArrayList = CppGenerator.this._cppExtensions.isArrayList(it);
          if (!_isArrayList) {
            _and = false;
          } else {
            String _typeName = CppGenerator.this._cppExtensions.toTypeName(it);
            boolean _equals = Objects.equal(_typeName, "QString");
            _and = _equals;
          }
          return Boolean.valueOf(_and);
        }
      };
      Iterable<? extends LFeature> _filter_17 = IterableExtensions.filter(_allFeatures_19, _function_17);
      for(final LFeature feature_19 : _filter_17) {
        CharSequence _foo_2 = this.foo(feature_19);
        _builder.append(_foo_2, "");
        _builder.newLineIfNotEmpty();
        _builder.append("void ");
        String _name_254 = this._cppExtensions.toName(dto);
        _builder.append(_name_254, "");
        _builder.append("::addTo");
        String _name_255 = this._cppExtensions.toName(feature_19);
        String _firstUpper_114 = StringExtensions.toFirstUpper(_name_255);
        _builder.append(_firstUpper_114, "");
        _builder.append("StringList(const ");
        String _typeName_32 = this._cppExtensions.toTypeName(feature_19);
        _builder.append(_typeName_32, "");
        _builder.append("& ");
        String _typeName_33 = this._cppExtensions.toTypeName(feature_19);
        String _firstLower_26 = StringExtensions.toFirstLower(_typeName_33);
        _builder.append(_firstLower_26, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("m");
        String _name_256 = this._cppExtensions.toName(feature_19);
        String _firstUpper_115 = StringExtensions.toFirstUpper(_name_256);
        _builder.append(_firstUpper_115, "    ");
        _builder.append("StringList.append(");
        String _typeName_34 = this._cppExtensions.toTypeName(feature_19);
        String _firstLower_27 = StringExtensions.toFirstLower(_typeName_34);
        _builder.append(_firstLower_27, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("emit addedTo");
        String _name_257 = this._cppExtensions.toName(feature_19);
        String _firstUpper_116 = StringExtensions.toFirstUpper(_name_257);
        _builder.append(_firstUpper_116, "    ");
        _builder.append("StringList(");
        String _typeName_35 = this._cppExtensions.toTypeName(feature_19);
        String _firstLower_28 = StringExtensions.toFirstLower(_typeName_35);
        _builder.append(_firstLower_28, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("void ");
        String _name_258 = this._cppExtensions.toName(dto);
        _builder.append(_name_258, "");
        _builder.append("::removeFrom");
        String _name_259 = this._cppExtensions.toName(feature_19);
        String _firstUpper_117 = StringExtensions.toFirstUpper(_name_259);
        _builder.append(_firstUpper_117, "");
        _builder.append("StringList(const ");
        String _typeName_36 = this._cppExtensions.toTypeName(feature_19);
        _builder.append(_typeName_36, "");
        _builder.append("& ");
        String _typeName_37 = this._cppExtensions.toTypeName(feature_19);
        String _firstLower_29 = StringExtensions.toFirstLower(_typeName_37);
        _builder.append(_firstLower_29, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("for (int i = 0; i < m");
        String _name_260 = this._cppExtensions.toName(feature_19);
        String _firstUpper_118 = StringExtensions.toFirstUpper(_name_260);
        _builder.append(_firstUpper_118, "    ");
        _builder.append("StringList.size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("if (m");
        String _name_261 = this._cppExtensions.toName(feature_19);
        String _firstUpper_119 = StringExtensions.toFirstUpper(_name_261);
        _builder.append(_firstUpper_119, "        ");
        _builder.append("StringList.at(i) == ");
        String _typeName_38 = this._cppExtensions.toTypeName(feature_19);
        String _firstLower_30 = StringExtensions.toFirstLower(_typeName_38);
        _builder.append(_firstLower_30, "        ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("m");
        String _name_262 = this._cppExtensions.toName(feature_19);
        String _firstUpper_120 = StringExtensions.toFirstUpper(_name_262);
        _builder.append(_firstUpper_120, "            ");
        _builder.append("StringList.removeAt(i);");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("emit removedFrom");
        String _name_263 = this._cppExtensions.toName(feature_19);
        String _firstUpper_121 = StringExtensions.toFirstUpper(_name_263);
        _builder.append(_firstUpper_121, "            ");
        _builder.append("StringList(");
        String _typeName_39 = this._cppExtensions.toTypeName(feature_19);
        String _firstLower_31 = StringExtensions.toFirstLower(_typeName_39);
        _builder.append(_firstLower_31, "            ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("return;");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("qDebug() << \"");
        String _typeName_40 = this._cppExtensions.toTypeName(feature_19);
        _builder.append(_typeName_40, "    ");
        _builder.append("& not found in ");
        String _name_264 = this._cppExtensions.toName(feature_19);
        String _firstLower_32 = StringExtensions.toFirstLower(_name_264);
        _builder.append(_firstLower_32, "    ");
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("// TODO signal error");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("int ");
        String _name_265 = this._cppExtensions.toName(dto);
        _builder.append(_name_265, "");
        _builder.append("::");
        String _name_266 = this._cppExtensions.toName(feature_19);
        String _firstLower_33 = StringExtensions.toFirstLower(_name_266);
        _builder.append(_firstLower_33, "");
        _builder.append("Count(){");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("return m");
        String _name_267 = this._cppExtensions.toName(feature_19);
        String _firstUpper_122 = StringExtensions.toFirstUpper(_name_267);
        _builder.append(_firstUpper_122, "    ");
        _builder.append("StringList.size();");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("QStringList ");
        String _name_268 = this._cppExtensions.toName(dto);
        _builder.append(_name_268, "");
        _builder.append("::");
        String _name_269 = this._cppExtensions.toName(feature_19);
        _builder.append(_name_269, "");
        _builder.append("StringList()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return m");
        String _name_270 = this._cppExtensions.toName(feature_19);
        String _firstUpper_123 = StringExtensions.toFirstUpper(_name_270);
        _builder.append(_firstUpper_123, "\t");
        _builder.append("StringList;");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_271 = this._cppExtensions.toName(dto);
        _builder.append(_name_271, "");
        _builder.append("::set");
        String _name_272 = this._cppExtensions.toName(feature_19);
        String _firstUpper_124 = StringExtensions.toFirstUpper(_name_272);
        _builder.append(_firstUpper_124, "");
        _builder.append("StringList(const QStringList& ");
        String _name_273 = this._cppExtensions.toName(feature_19);
        _builder.append(_name_273, "");
        _builder.append(") ");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (");
        String _name_274 = this._cppExtensions.toName(feature_19);
        _builder.append(_name_274, "\t");
        _builder.append(" != m");
        String _name_275 = this._cppExtensions.toName(feature_19);
        String _firstUpper_125 = StringExtensions.toFirstUpper(_name_275);
        _builder.append(_firstUpper_125, "\t");
        _builder.append("StringList) {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("m");
        String _name_276 = this._cppExtensions.toName(feature_19);
        String _firstUpper_126 = StringExtensions.toFirstUpper(_name_276);
        _builder.append(_firstUpper_126, "\t\t");
        _builder.append("StringList = ");
        String _name_277 = this._cppExtensions.toName(feature_19);
        _builder.append(_name_277, "\t\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("emit ");
        String _name_278 = this._cppExtensions.toName(feature_19);
        _builder.append(_name_278, "\t\t");
        _builder.append("StringListChanged(");
        String _name_279 = this._cppExtensions.toName(feature_19);
        _builder.append(_name_279, "\t\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    {
      List<? extends LFeature> _allFeatures_20 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_18 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _and = false;
          boolean _isArrayList = CppGenerator.this._cppExtensions.isArrayList(it);
          if (!_isArrayList) {
            _and = false;
          } else {
            String _typeName = CppGenerator.this._cppExtensions.toTypeName(it);
            boolean _notEquals = (!Objects.equal(_typeName, "QString"));
            _and = _notEquals;
          }
          return Boolean.valueOf(_and);
        }
      };
      Iterable<? extends LFeature> _filter_18 = IterableExtensions.filter(_allFeatures_20, _function_18);
      for(final LFeature feature_20 : _filter_18) {
        CharSequence _foo_3 = this.foo(feature_20);
        _builder.append(_foo_3, "");
        _builder.newLineIfNotEmpty();
        _builder.append("void ");
        String _name_280 = this._cppExtensions.toName(dto);
        _builder.append(_name_280, "");
        _builder.append("::addTo");
        String _name_281 = this._cppExtensions.toName(feature_20);
        String _firstUpper_127 = StringExtensions.toFirstUpper(_name_281);
        _builder.append(_firstUpper_127, "");
        _builder.append("List(const ");
        String _typeName_41 = this._cppExtensions.toTypeName(feature_20);
        _builder.append(_typeName_41, "");
        _builder.append("& the");
        String _typeName_42 = this._cppExtensions.toTypeName(feature_20);
        String _firstUpper_128 = StringExtensions.toFirstUpper(_typeName_42);
        _builder.append(_firstUpper_128, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("m");
        String _name_282 = this._cppExtensions.toName(feature_20);
        String _firstUpper_129 = StringExtensions.toFirstUpper(_name_282);
        _builder.append(_firstUpper_129, "    ");
        _builder.append(".append(the");
        String _typeName_43 = this._cppExtensions.toTypeName(feature_20);
        String _firstUpper_130 = StringExtensions.toFirstUpper(_typeName_43);
        _builder.append(_firstUpper_130, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("emit addedTo");
        String _name_283 = this._cppExtensions.toName(feature_20);
        String _firstUpper_131 = StringExtensions.toFirstUpper(_name_283);
        _builder.append(_firstUpper_131, "    ");
        _builder.append("List(the");
        String _typeName_44 = this._cppExtensions.toTypeName(feature_20);
        String _firstUpper_132 = StringExtensions.toFirstUpper(_typeName_44);
        _builder.append(_firstUpper_132, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("void ");
        String _name_284 = this._cppExtensions.toName(dto);
        _builder.append(_name_284, "");
        _builder.append("::removeFrom");
        String _name_285 = this._cppExtensions.toName(feature_20);
        String _firstUpper_133 = StringExtensions.toFirstUpper(_name_285);
        _builder.append(_firstUpper_133, "");
        _builder.append("List(const ");
        String _typeName_45 = this._cppExtensions.toTypeName(feature_20);
        _builder.append(_typeName_45, "");
        _builder.append("& the");
        String _typeName_46 = this._cppExtensions.toTypeName(feature_20);
        String _firstUpper_134 = StringExtensions.toFirstUpper(_typeName_46);
        _builder.append(_firstUpper_134, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("for (int i = 0; i < m");
        String _name_286 = this._cppExtensions.toName(feature_20);
        String _firstUpper_135 = StringExtensions.toFirstUpper(_name_286);
        _builder.append(_firstUpper_135, "    ");
        _builder.append(".size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("if (m");
        String _name_287 = this._cppExtensions.toName(feature_20);
        String _firstUpper_136 = StringExtensions.toFirstUpper(_name_287);
        _builder.append(_firstUpper_136, "        ");
        _builder.append(".at(i) == the");
        String _typeName_47 = this._cppExtensions.toTypeName(feature_20);
        String _firstUpper_137 = StringExtensions.toFirstUpper(_typeName_47);
        _builder.append(_firstUpper_137, "        ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("m");
        String _name_288 = this._cppExtensions.toName(feature_20);
        String _firstUpper_138 = StringExtensions.toFirstUpper(_name_288);
        _builder.append(_firstUpper_138, "            ");
        _builder.append(".removeAt(i);");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("emit removedFrom");
        String _name_289 = this._cppExtensions.toName(feature_20);
        String _firstUpper_139 = StringExtensions.toFirstUpper(_name_289);
        _builder.append(_firstUpper_139, "            ");
        _builder.append("List(the");
        String _typeName_48 = this._cppExtensions.toTypeName(feature_20);
        String _firstUpper_140 = StringExtensions.toFirstUpper(_typeName_48);
        _builder.append(_firstUpper_140, "            ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("return;");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("qDebug() << \"");
        String _typeName_49 = this._cppExtensions.toTypeName(feature_20);
        _builder.append(_typeName_49, "    ");
        _builder.append("& not found in ");
        String _name_290 = this._cppExtensions.toName(feature_20);
        String _firstLower_34 = StringExtensions.toFirstLower(_name_290);
        _builder.append(_firstLower_34, "    ");
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("// TODO signal error");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("int ");
        String _name_291 = this._cppExtensions.toName(dto);
        _builder.append(_name_291, "");
        _builder.append("::");
        String _name_292 = this._cppExtensions.toName(feature_20);
        String _firstLower_35 = StringExtensions.toFirstLower(_name_292);
        _builder.append(_firstLower_35, "");
        _builder.append("Count(){");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("return m");
        String _name_293 = this._cppExtensions.toName(feature_20);
        String _firstUpper_141 = StringExtensions.toFirstUpper(_name_293);
        _builder.append(_firstUpper_141, "    ");
        _builder.append(".size();");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("QList<");
        String _typeName_50 = this._cppExtensions.toTypeName(feature_20);
        _builder.append(_typeName_50, "");
        _builder.append("> ");
        String _name_294 = this._cppExtensions.toName(dto);
        _builder.append(_name_294, "");
        _builder.append("::");
        String _name_295 = this._cppExtensions.toName(feature_20);
        _builder.append(_name_295, "");
        _builder.append("()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("return m");
        String _name_296 = this._cppExtensions.toName(feature_20);
        String _firstUpper_142 = StringExtensions.toFirstUpper(_name_296);
        _builder.append(_firstUpper_142, "    ");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_297 = this._cppExtensions.toName(dto);
        _builder.append(_name_297, "");
        _builder.append("::set");
        String _name_298 = this._cppExtensions.toName(feature_20);
        String _firstUpper_143 = StringExtensions.toFirstUpper(_name_298);
        _builder.append(_firstUpper_143, "");
        _builder.append("(QList<");
        String _typeName_51 = this._cppExtensions.toTypeName(feature_20);
        _builder.append(_typeName_51, "");
        _builder.append("> ");
        String _name_299 = this._cppExtensions.toName(feature_20);
        _builder.append(_name_299, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("if (");
        String _name_300 = this._cppExtensions.toName(feature_20);
        _builder.append(_name_300, "    ");
        _builder.append(" != m");
        String _name_301 = this._cppExtensions.toName(feature_20);
        String _firstUpper_144 = StringExtensions.toFirstUpper(_name_301);
        _builder.append(_firstUpper_144, "    ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("m");
        String _name_302 = this._cppExtensions.toName(feature_20);
        String _firstUpper_145 = StringExtensions.toFirstUpper(_name_302);
        _builder.append(_firstUpper_145, "        ");
        _builder.append(" = ");
        String _name_303 = this._cppExtensions.toName(feature_20);
        _builder.append(_name_303, "        ");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("QVariantList variantList;");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("for (int i = 0; i < ");
        String _name_304 = this._cppExtensions.toName(feature_20);
        _builder.append(_name_304, "        ");
        _builder.append(".size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("variantList.append(");
        String _name_305 = this._cppExtensions.toName(feature_20);
        _builder.append(_name_305, "            ");
        _builder.append(".at(i));");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("emit ");
        String _name_306 = this._cppExtensions.toName(feature_20);
        _builder.append(_name_306, "        ");
        _builder.append("ListChanged(variantList);");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("// access from QML to values");
        _builder.newLine();
        _builder.append("QVariantList ");
        String _name_307 = this._cppExtensions.toName(dto);
        _builder.append(_name_307, "");
        _builder.append("::");
        String _name_308 = this._cppExtensions.toName(feature_20);
        _builder.append(_name_308, "");
        _builder.append("List()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("QVariantList variantList;");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("for (int i = 0; i < m");
        String _name_309 = this._cppExtensions.toName(feature_20);
        String _firstUpper_146 = StringExtensions.toFirstUpper(_name_309);
        _builder.append(_firstUpper_146, "    ");
        _builder.append(".size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("variantList.append(m");
        String _name_310 = this._cppExtensions.toName(feature_20);
        String _firstUpper_147 = StringExtensions.toFirstUpper(_name_310);
        _builder.append(_firstUpper_147, "        ");
        _builder.append(".at(i));");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("return variantList;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_311 = this._cppExtensions.toName(dto);
        _builder.append(_name_311, "");
        _builder.append("::set");
        String _name_312 = this._cppExtensions.toName(feature_20);
        String _firstUpper_148 = StringExtensions.toFirstUpper(_name_312);
        _builder.append(_firstUpper_148, "");
        _builder.append("List(const QVariantList& ");
        String _name_313 = this._cppExtensions.toName(feature_20);
        _builder.append(_name_313, "");
        _builder.append(") ");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("m");
        String _name_314 = this._cppExtensions.toName(feature_20);
        String _firstUpper_149 = StringExtensions.toFirstUpper(_name_314);
        _builder.append(_firstUpper_149, "\t");
        _builder.append(".clear();");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("for (int i = 0; i < ");
        String _name_315 = this._cppExtensions.toName(feature_20);
        _builder.append(_name_315, "    ");
        _builder.append(".size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("m");
        String _name_316 = this._cppExtensions.toName(feature_20);
        String _firstUpper_150 = StringExtensions.toFirstUpper(_name_316);
        _builder.append(_firstUpper_150, "        ");
        _builder.append(".append(");
        String _name_317 = this._cppExtensions.toName(feature_20);
        _builder.append(_name_317, "        ");
        _builder.append(".at(i).to");
        String _mapToSingleType_1 = this._cppExtensions.mapToSingleType(feature_20);
        _builder.append(_mapToSingleType_1, "        ");
        _builder.append("());");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    {
      List<? extends LFeature> _allFeatures_21 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_19 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _and = false;
          boolean _isToMany = CppGenerator.this._cppExtensions.isToMany(it);
          if (!_isToMany) {
            _and = false;
          } else {
            boolean _isArrayList = CppGenerator.this._cppExtensions.isArrayList(it);
            boolean _not = (!_isArrayList);
            _and = _not;
          }
          return Boolean.valueOf(_and);
        }
      };
      Iterable<? extends LFeature> _filter_19 = IterableExtensions.filter(_allFeatures_21, _function_19);
      for(final LFeature feature_21 : _filter_19) {
        CharSequence _foo_4 = this.foo(feature_21);
        _builder.append(_foo_4, "");
        _builder.append(" ");
        _builder.newLineIfNotEmpty();
        _builder.append("QVariantList ");
        String _name_318 = this._cppExtensions.toName(dto);
        _builder.append(_name_318, "");
        _builder.append("::");
        String _name_319 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_319, "");
        _builder.append("AsQVariantList()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("QVariantList ");
        String _name_320 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_320, "\t");
        _builder.append("List;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("for (int i = 0; i < m");
        String _name_321 = this._cppExtensions.toName(feature_21);
        String _firstUpper_151 = StringExtensions.toFirstUpper(_name_321);
        _builder.append(_firstUpper_151, "\t");
        _builder.append(".size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        String _name_322 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_322, "        ");
        _builder.append("List.append((m");
        String _name_323 = this._cppExtensions.toName(feature_21);
        String _firstUpper_152 = StringExtensions.toFirstUpper(_name_323);
        _builder.append(_firstUpper_152, "        ");
        _builder.append(".at(i))->toMap());");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return ");
        String _name_324 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_324, "\t");
        _builder.append("List;");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_325 = this._cppExtensions.toName(dto);
        _builder.append(_name_325, "");
        _builder.append("::addTo");
        String _name_326 = this._cppExtensions.toName(feature_21);
        String _firstUpper_153 = StringExtensions.toFirstUpper(_name_326);
        _builder.append(_firstUpper_153, "");
        _builder.append("(");
        String _typeName_52 = this._cppExtensions.toTypeName(feature_21);
        _builder.append(_typeName_52, "");
        _builder.append("* ");
        String _typeName_53 = this._cppExtensions.toTypeName(feature_21);
        String _firstLower_36 = StringExtensions.toFirstLower(_typeName_53);
        _builder.append(_firstLower_36, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("m");
        String _name_327 = this._cppExtensions.toName(feature_21);
        String _firstUpper_154 = StringExtensions.toFirstUpper(_name_327);
        _builder.append(_firstUpper_154, "    ");
        _builder.append(".append(");
        String _typeName_54 = this._cppExtensions.toTypeName(feature_21);
        String _firstLower_37 = StringExtensions.toFirstLower(_typeName_54);
        _builder.append(_firstLower_37, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("emit addedTo");
        String _name_328 = this._cppExtensions.toName(feature_21);
        String _firstUpper_155 = StringExtensions.toFirstUpper(_name_328);
        _builder.append(_firstUpper_155, "    ");
        _builder.append("(");
        String _typeName_55 = this._cppExtensions.toTypeName(feature_21);
        String _firstLower_38 = StringExtensions.toFirstLower(_typeName_55);
        _builder.append(_firstLower_38, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("void ");
        String _name_329 = this._cppExtensions.toName(dto);
        _builder.append(_name_329, "");
        _builder.append("::removeFrom");
        String _name_330 = this._cppExtensions.toName(feature_21);
        String _firstUpper_156 = StringExtensions.toFirstUpper(_name_330);
        _builder.append(_firstUpper_156, "");
        _builder.append("(");
        String _typeName_56 = this._cppExtensions.toTypeName(feature_21);
        _builder.append(_typeName_56, "");
        _builder.append("* ");
        String _typeName_57 = this._cppExtensions.toTypeName(feature_21);
        String _firstLower_39 = StringExtensions.toFirstLower(_typeName_57);
        _builder.append(_firstLower_39, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("for (int i = 0; i < m");
        String _name_331 = this._cppExtensions.toName(feature_21);
        String _firstUpper_157 = StringExtensions.toFirstUpper(_name_331);
        _builder.append(_firstUpper_157, "    ");
        _builder.append(".size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("if (m");
        String _name_332 = this._cppExtensions.toName(feature_21);
        String _firstUpper_158 = StringExtensions.toFirstUpper(_name_332);
        _builder.append(_firstUpper_158, "        ");
        _builder.append(".at(i) == ");
        String _typeName_58 = this._cppExtensions.toTypeName(feature_21);
        String _firstLower_40 = StringExtensions.toFirstLower(_typeName_58);
        _builder.append(_firstLower_40, "        ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("m");
        String _name_333 = this._cppExtensions.toName(feature_21);
        String _firstUpper_159 = StringExtensions.toFirstUpper(_name_333);
        _builder.append(_firstUpper_159, "            ");
        _builder.append(".removeAt(i);");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("emit removedFrom");
        String _name_334 = this._cppExtensions.toName(feature_21);
        String _firstUpper_160 = StringExtensions.toFirstUpper(_name_334);
        _builder.append(_firstUpper_160, "            ");
        _builder.append("(");
        String _typeName_59 = this._cppExtensions.toTypeName(feature_21);
        String _firstLower_41 = StringExtensions.toFirstLower(_typeName_59);
        _builder.append(_firstLower_41, "            ");
        _builder.append("->uuid());");
        _builder.newLineIfNotEmpty();
        {
          boolean _hasOpposite = this._cppExtensions.hasOpposite(feature_21);
          if (_hasOpposite) {
            _builder.append("            ");
            _builder.append("// ");
            String _name_335 = this._cppExtensions.toName(feature_21);
            _builder.append(_name_335, "            ");
            _builder.append(" are contained - so we must delete them");
            _builder.newLineIfNotEmpty();
            _builder.append("            ");
            String _typeName_60 = this._cppExtensions.toTypeName(feature_21);
            String _firstLower_42 = StringExtensions.toFirstLower(_typeName_60);
            _builder.append(_firstLower_42, "            ");
            _builder.append("->deleteLater();");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("            ");
            _builder.append("// ");
            String _name_336 = this._cppExtensions.toName(feature_21);
            _builder.append(_name_336, "            ");
            _builder.append(" are independent - DON\'T delete them");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("            ");
        _builder.append("return;");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("qDebug() << \"");
        String _typeName_61 = this._cppExtensions.toTypeName(feature_21);
        _builder.append(_typeName_61, "    ");
        _builder.append("* not found in ");
        String _name_337 = this._cppExtensions.toName(feature_21);
        String _firstLower_43 = StringExtensions.toFirstLower(_name_337);
        _builder.append(_firstLower_43, "    ");
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("// TODO signal error");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("void ");
        String _name_338 = this._cppExtensions.toName(dto);
        _builder.append(_name_338, "");
        _builder.append("::addTo");
        String _name_339 = this._cppExtensions.toName(feature_21);
        String _firstUpper_161 = StringExtensions.toFirstUpper(_name_339);
        _builder.append(_firstUpper_161, "");
        _builder.append("FromMap(const QVariantMap& ");
        String _typeName_62 = this._cppExtensions.toTypeName(feature_21);
        String _firstLower_44 = StringExtensions.toFirstLower(_typeName_62);
        _builder.append(_firstLower_44, "");
        _builder.append("Map)");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        String _typeName_63 = this._cppExtensions.toTypeName(feature_21);
        _builder.append(_typeName_63, "    ");
        _builder.append("* ");
        String _typeName_64 = this._cppExtensions.toTypeName(feature_21);
        String _firstLower_45 = StringExtensions.toFirstLower(_typeName_64);
        _builder.append(_firstLower_45, "    ");
        _builder.append(" = new ");
        String _typeName_65 = this._cppExtensions.toTypeName(feature_21);
        _builder.append(_typeName_65, "    ");
        _builder.append("();");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        String _typeName_66 = this._cppExtensions.toTypeName(feature_21);
        String _firstLower_46 = StringExtensions.toFirstLower(_typeName_66);
        _builder.append(_firstLower_46, "    ");
        _builder.append("->setParent(this);");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        String _typeName_67 = this._cppExtensions.toTypeName(feature_21);
        String _firstLower_47 = StringExtensions.toFirstLower(_typeName_67);
        _builder.append(_firstLower_47, "    ");
        _builder.append("->fillFromMap(");
        String _typeName_68 = this._cppExtensions.toTypeName(feature_21);
        String _firstLower_48 = StringExtensions.toFirstLower(_typeName_68);
        _builder.append(_firstLower_48, "    ");
        _builder.append("Map);");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("m");
        String _name_340 = this._cppExtensions.toName(feature_21);
        String _firstUpper_162 = StringExtensions.toFirstUpper(_name_340);
        _builder.append(_firstUpper_162, "    ");
        _builder.append(".append(");
        String _typeName_69 = this._cppExtensions.toTypeName(feature_21);
        String _firstLower_49 = StringExtensions.toFirstLower(_typeName_69);
        _builder.append(_firstLower_49, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("emit addedTo");
        String _name_341 = this._cppExtensions.toName(feature_21);
        String _firstUpper_163 = StringExtensions.toFirstUpper(_name_341);
        _builder.append(_firstUpper_163, "    ");
        _builder.append("(");
        String _typeName_70 = this._cppExtensions.toTypeName(feature_21);
        String _firstLower_50 = StringExtensions.toFirstLower(_typeName_70);
        _builder.append(_firstLower_50, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("void ");
        String _name_342 = this._cppExtensions.toName(dto);
        _builder.append(_name_342, "");
        _builder.append("::removeFrom");
        String _name_343 = this._cppExtensions.toName(feature_21);
        String _firstUpper_164 = StringExtensions.toFirstUpper(_name_343);
        _builder.append(_firstUpper_164, "");
        _builder.append("ByKey(const QString& uuid)");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("for (int i = 0; i < m");
        String _name_344 = this._cppExtensions.toName(feature_21);
        String _firstUpper_165 = StringExtensions.toFirstUpper(_name_344);
        _builder.append(_firstUpper_165, "    ");
        _builder.append(".size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("if ((m");
        String _name_345 = this._cppExtensions.toName(feature_21);
        String _firstUpper_166 = StringExtensions.toFirstUpper(_name_345);
        _builder.append(_firstUpper_166, "        ");
        _builder.append(".at(i))->toMap().value(uuidKey).toString() == uuid) {");
        _builder.newLineIfNotEmpty();
        {
          boolean _hasOpposite_1 = this._cppExtensions.hasOpposite(feature_21);
          if (_hasOpposite_1) {
            _builder.append("            ");
            String _typeName_71 = this._cppExtensions.toTypeName(feature_21);
            _builder.append(_typeName_71, "            ");
            _builder.append("* ");
            String _typeName_72 = this._cppExtensions.toTypeName(feature_21);
            String _firstLower_51 = StringExtensions.toFirstLower(_typeName_72);
            _builder.append(_firstLower_51, "            ");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            _builder.append("            ");
            String _typeName_73 = this._cppExtensions.toTypeName(feature_21);
            String _firstLower_52 = StringExtensions.toFirstLower(_typeName_73);
            _builder.append(_firstLower_52, "            ");
            _builder.append(" = m");
            String _name_346 = this._cppExtensions.toName(feature_21);
            String _firstUpper_167 = StringExtensions.toFirstUpper(_name_346);
            _builder.append(_firstUpper_167, "            ");
            _builder.append(".at(i);");
            _builder.newLineIfNotEmpty();
            _builder.append("            ");
            _builder.append("m");
            String _name_347 = this._cppExtensions.toName(feature_21);
            String _firstUpper_168 = StringExtensions.toFirstUpper(_name_347);
            _builder.append(_firstUpper_168, "            ");
            _builder.append(".removeAt(i);");
            _builder.newLineIfNotEmpty();
            _builder.append("            ");
            _builder.append("emit removedFrom");
            String _name_348 = this._cppExtensions.toName(feature_21);
            String _firstUpper_169 = StringExtensions.toFirstUpper(_name_348);
            _builder.append(_firstUpper_169, "            ");
            _builder.append("(uuid);");
            _builder.newLineIfNotEmpty();
            _builder.append("            ");
            _builder.append("// ");
            String _name_349 = this._cppExtensions.toName(feature_21);
            _builder.append(_name_349, "            ");
            _builder.append(" are contained - so we must delete them");
            _builder.newLineIfNotEmpty();
            _builder.append("            ");
            String _typeName_74 = this._cppExtensions.toTypeName(feature_21);
            String _firstLower_53 = StringExtensions.toFirstLower(_typeName_74);
            _builder.append(_firstLower_53, "            ");
            _builder.append("->deleteLater();");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("            ");
            _builder.append("m");
            String _name_350 = this._cppExtensions.toName(feature_21);
            String _firstUpper_170 = StringExtensions.toFirstUpper(_name_350);
            _builder.append(_firstUpper_170, "            ");
            _builder.append(".removeAt(i);");
            _builder.newLineIfNotEmpty();
            _builder.append("            ");
            _builder.append("emit removedFrom");
            String _name_351 = this._cppExtensions.toName(feature_21);
            String _firstUpper_171 = StringExtensions.toFirstUpper(_name_351);
            _builder.append(_firstUpper_171, "            ");
            _builder.append("(uuid);");
            _builder.newLineIfNotEmpty();
            _builder.append("            ");
            _builder.append("// ");
            String _name_352 = this._cppExtensions.toName(feature_21);
            _builder.append(_name_352, "            ");
            _builder.append(" are independent - DON\'T delete them");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("            ");
        _builder.append("return;");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("qDebug() << \"uuid not found in ");
        String _name_353 = this._cppExtensions.toName(feature_21);
        String _firstLower_54 = StringExtensions.toFirstLower(_name_353);
        _builder.append(_firstLower_54, "    ");
        _builder.append(": \" << uuid;");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("// TODO signal error");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("int ");
        String _name_354 = this._cppExtensions.toName(dto);
        _builder.append(_name_354, "");
        _builder.append("::");
        String _name_355 = this._cppExtensions.toName(feature_21);
        String _firstLower_55 = StringExtensions.toFirstLower(_name_355);
        _builder.append(_firstLower_55, "");
        _builder.append("Count(){");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("return m");
        String _name_356 = this._cppExtensions.toName(feature_21);
        String _firstUpper_172 = StringExtensions.toFirstUpper(_name_356);
        _builder.append(_firstUpper_172, "    ");
        _builder.append(".size();");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("QList<");
        String _typeName_75 = this._cppExtensions.toTypeName(feature_21);
        _builder.append(_typeName_75, "");
        _builder.append("*> ");
        String _name_357 = this._cppExtensions.toName(dto);
        _builder.append(_name_357, "");
        _builder.append("::");
        String _name_358 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_358, "");
        _builder.append("()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return m");
        String _name_359 = this._cppExtensions.toName(feature_21);
        String _firstUpper_173 = StringExtensions.toFirstUpper(_name_359);
        _builder.append(_firstUpper_173, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_360 = this._cppExtensions.toName(dto);
        _builder.append(_name_360, "");
        _builder.append("::set");
        String _name_361 = this._cppExtensions.toName(feature_21);
        String _firstUpper_174 = StringExtensions.toFirstUpper(_name_361);
        _builder.append(_firstUpper_174, "");
        _builder.append("(QList<");
        String _typeName_76 = this._cppExtensions.toTypeName(feature_21);
        _builder.append(_typeName_76, "");
        _builder.append("*> ");
        String _name_362 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_362, "");
        _builder.append(") ");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (");
        String _name_363 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_363, "\t");
        _builder.append(" != m");
        String _name_364 = this._cppExtensions.toName(feature_21);
        String _firstUpper_175 = StringExtensions.toFirstUpper(_name_364);
        _builder.append(_firstUpper_175, "\t");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("m");
        String _name_365 = this._cppExtensions.toName(feature_21);
        String _firstUpper_176 = StringExtensions.toFirstUpper(_name_365);
        _builder.append(_firstUpper_176, "\t\t");
        _builder.append(" = ");
        String _name_366 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_366, "\t\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("emit ");
        String _name_367 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_367, "\t\t");
        _builder.append("Changed(");
        String _name_368 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_368, "\t\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("/**");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* to access lists from QML we\'re using QDeclarativeListProperty");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* and implement methods to append, count and clear");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* now from QML we can use");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* ");
        String _name_369 = this._cppExtensions.toName(dto);
        String _firstLower_56 = StringExtensions.toFirstLower(_name_369);
        _builder.append(_firstLower_56, " ");
        _builder.append(".");
        String _name_370 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_370, " ");
        _builder.append("PropertyList.length to get the size");
        _builder.newLineIfNotEmpty();
        _builder.append(" ");
        _builder.append("* ");
        String _name_371 = this._cppExtensions.toName(dto);
        String _firstLower_57 = StringExtensions.toFirstLower(_name_371);
        _builder.append(_firstLower_57, " ");
        _builder.append(".");
        String _name_372 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_372, " ");
        _builder.append("PropertyList[2] to get ");
        String _typeName_77 = this._cppExtensions.toTypeName(feature_21);
        _builder.append(_typeName_77, " ");
        _builder.append("* at position 2");
        _builder.newLineIfNotEmpty();
        _builder.append(" ");
        _builder.append("* ");
        String _name_373 = this._cppExtensions.toName(dto);
        String _firstLower_58 = StringExtensions.toFirstLower(_name_373);
        _builder.append(_firstLower_58, " ");
        _builder.append(".");
        String _name_374 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_374, " ");
        _builder.append("PropertyList = [] to clear the list");
        _builder.newLineIfNotEmpty();
        _builder.append(" ");
        _builder.append("* or get easy access to properties like");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* ");
        String _name_375 = this._cppExtensions.toName(dto);
        String _firstLower_59 = StringExtensions.toFirstLower(_name_375);
        _builder.append(_firstLower_59, " ");
        _builder.append(".");
        String _name_376 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_376, " ");
        _builder.append("PropertyList[2].myPropertyName");
        _builder.newLineIfNotEmpty();
        _builder.append(" ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("QDeclarativeListProperty<");
        String _typeName_78 = this._cppExtensions.toTypeName(feature_21);
        _builder.append(_typeName_78, "");
        _builder.append("> ");
        String _name_377 = this._cppExtensions.toName(dto);
        _builder.append(_name_377, "");
        _builder.append("::");
        String _name_378 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_378, "");
        _builder.append("PropertyList()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("return QDeclarativeListProperty<");
        String _typeName_79 = this._cppExtensions.toTypeName(feature_21);
        _builder.append(_typeName_79, "    ");
        _builder.append(">(this, 0, &");
        String _name_379 = this._cppExtensions.toName(dto);
        _builder.append(_name_379, "    ");
        _builder.append("::appendTo");
        String _name_380 = this._cppExtensions.toName(feature_21);
        String _firstUpper_177 = StringExtensions.toFirstUpper(_name_380);
        _builder.append(_firstUpper_177, "    ");
        _builder.append("Property,");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("&");
        String _name_381 = this._cppExtensions.toName(dto);
        _builder.append(_name_381, "            ");
        _builder.append("::");
        String _name_382 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_382, "            ");
        _builder.append("PropertyCount, &");
        String _name_383 = this._cppExtensions.toName(dto);
        _builder.append(_name_383, "            ");
        _builder.append("::at");
        String _name_384 = this._cppExtensions.toName(feature_21);
        String _firstUpper_178 = StringExtensions.toFirstUpper(_name_384);
        _builder.append(_firstUpper_178, "            ");
        _builder.append("Property,");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("&");
        String _name_385 = this._cppExtensions.toName(dto);
        _builder.append(_name_385, "            ");
        _builder.append("::clear");
        String _name_386 = this._cppExtensions.toName(feature_21);
        String _firstUpper_179 = StringExtensions.toFirstUpper(_name_386);
        _builder.append(_firstUpper_179, "            ");
        _builder.append("Property);");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_387 = this._cppExtensions.toName(dto);
        _builder.append(_name_387, "");
        _builder.append("::appendTo");
        String _name_388 = this._cppExtensions.toName(feature_21);
        String _firstUpper_180 = StringExtensions.toFirstUpper(_name_388);
        _builder.append(_firstUpper_180, "");
        _builder.append("Property(QDeclarativeListProperty<");
        String _typeName_80 = this._cppExtensions.toTypeName(feature_21);
        _builder.append(_typeName_80, "");
        _builder.append("> *");
        String _name_389 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_389, "");
        _builder.append("List,");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        String _typeName_81 = this._cppExtensions.toTypeName(feature_21);
        _builder.append(_typeName_81, "        ");
        _builder.append("* ");
        String _typeName_82 = this._cppExtensions.toTypeName(feature_21);
        String _firstLower_60 = StringExtensions.toFirstLower(_typeName_82);
        _builder.append(_firstLower_60, "        ");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        String _name_390 = this._cppExtensions.toName(dto);
        _builder.append(_name_390, "    ");
        _builder.append(" *");
        String _name_391 = this._cppExtensions.toName(dto);
        String _firstLower_61 = StringExtensions.toFirstLower(_name_391);
        _builder.append(_firstLower_61, "    ");
        _builder.append("Object = qobject_cast<");
        String _name_392 = this._cppExtensions.toName(dto);
        _builder.append(_name_392, "    ");
        _builder.append(" *>(");
        String _name_393 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_393, "    ");
        _builder.append("List->object);");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("if (");
        String _name_394 = this._cppExtensions.toName(dto);
        String _firstLower_62 = StringExtensions.toFirstLower(_name_394);
        _builder.append(_firstLower_62, "    ");
        _builder.append("Object) {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        String _typeName_83 = this._cppExtensions.toTypeName(feature_21);
        String _firstLower_63 = StringExtensions.toFirstLower(_typeName_83);
        _builder.append(_firstLower_63, "        ");
        _builder.append("->setParent(");
        String _name_395 = this._cppExtensions.toName(dto);
        String _firstLower_64 = StringExtensions.toFirstLower(_name_395);
        _builder.append(_firstLower_64, "        ");
        _builder.append("Object);");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        String _name_396 = this._cppExtensions.toName(dto);
        String _firstLower_65 = StringExtensions.toFirstLower(_name_396);
        _builder.append(_firstLower_65, "        ");
        _builder.append("Object->m");
        String _name_397 = this._cppExtensions.toName(feature_21);
        String _firstUpper_181 = StringExtensions.toFirstUpper(_name_397);
        _builder.append(_firstUpper_181, "        ");
        _builder.append(".append(");
        String _typeName_84 = this._cppExtensions.toTypeName(feature_21);
        String _firstLower_66 = StringExtensions.toFirstLower(_typeName_84);
        _builder.append(_firstLower_66, "        ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("emit ");
        String _name_398 = this._cppExtensions.toName(dto);
        String _firstLower_67 = StringExtensions.toFirstLower(_name_398);
        _builder.append(_firstLower_67, "        ");
        _builder.append("Object->addedTo");
        String _name_399 = this._cppExtensions.toName(feature_21);
        String _firstUpper_182 = StringExtensions.toFirstUpper(_name_399);
        _builder.append(_firstUpper_182, "        ");
        _builder.append("(");
        String _typeName_85 = this._cppExtensions.toTypeName(feature_21);
        String _firstLower_68 = StringExtensions.toFirstLower(_typeName_85);
        _builder.append(_firstLower_68, "        ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("} else {");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("qWarning() << \"cannot append ");
        String _typeName_86 = this._cppExtensions.toTypeName(feature_21);
        _builder.append(_typeName_86, "        ");
        _builder.append("* to ");
        String _name_400 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_400, "        ");
        _builder.append(" \" << \"Object is not of type ");
        String _name_401 = this._cppExtensions.toName(dto);
        _builder.append(_name_401, "        ");
        _builder.append("*\";");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("int ");
        String _name_402 = this._cppExtensions.toName(dto);
        _builder.append(_name_402, "");
        _builder.append("::");
        String _name_403 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_403, "");
        _builder.append("PropertyCount(QDeclarativeListProperty<");
        String _typeName_87 = this._cppExtensions.toTypeName(feature_21);
        _builder.append(_typeName_87, "");
        _builder.append("> *");
        String _name_404 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_404, "");
        _builder.append("List)");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("qDebug() << \"");
        String _name_405 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_405, "    ");
        _builder.append("PropertyCount\";");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        String _name_406 = this._cppExtensions.toName(dto);
        _builder.append(_name_406, "    ");
        _builder.append(" *");
        String _name_407 = this._cppExtensions.toName(dto);
        String _firstLower_69 = StringExtensions.toFirstLower(_name_407);
        _builder.append(_firstLower_69, "    ");
        _builder.append(" = qobject_cast<");
        String _name_408 = this._cppExtensions.toName(dto);
        _builder.append(_name_408, "    ");
        _builder.append(" *>(");
        String _name_409 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_409, "    ");
        _builder.append("List->object);");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("if (");
        String _name_410 = this._cppExtensions.toName(dto);
        String _firstLower_70 = StringExtensions.toFirstLower(_name_410);
        _builder.append(_firstLower_70, "    ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("return ");
        String _name_411 = this._cppExtensions.toName(dto);
        String _firstLower_71 = StringExtensions.toFirstLower(_name_411);
        _builder.append(_firstLower_71, "        ");
        _builder.append("->m");
        String _name_412 = this._cppExtensions.toName(feature_21);
        String _firstUpper_183 = StringExtensions.toFirstUpper(_name_412);
        _builder.append(_firstUpper_183, "        ");
        _builder.append(".size();");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("} else {");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("qWarning() << \"cannot get size ");
        String _name_413 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_413, "        ");
        _builder.append(" \" << \"Object is not of type ");
        String _name_414 = this._cppExtensions.toName(dto);
        _builder.append(_name_414, "        ");
        _builder.append("*\";");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("return 0;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        String _typeName_88 = this._cppExtensions.toTypeName(feature_21);
        _builder.append(_typeName_88, "");
        _builder.append("* ");
        String _name_415 = this._cppExtensions.toName(dto);
        _builder.append(_name_415, "");
        _builder.append("::at");
        String _name_416 = this._cppExtensions.toName(feature_21);
        String _firstUpper_184 = StringExtensions.toFirstUpper(_name_416);
        _builder.append(_firstUpper_184, "");
        _builder.append("Property(QDeclarativeListProperty<");
        String _typeName_89 = this._cppExtensions.toTypeName(feature_21);
        _builder.append(_typeName_89, "");
        _builder.append("> *");
        String _name_417 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_417, "");
        _builder.append("List, int pos)");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("qDebug() << \"at");
        String _name_418 = this._cppExtensions.toName(feature_21);
        String _firstUpper_185 = StringExtensions.toFirstUpper(_name_418);
        _builder.append(_firstUpper_185, "    ");
        _builder.append("Property #\" << pos;");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        String _name_419 = this._cppExtensions.toName(dto);
        _builder.append(_name_419, "    ");
        _builder.append(" *");
        String _name_420 = this._cppExtensions.toName(dto);
        String _firstLower_72 = StringExtensions.toFirstLower(_name_420);
        _builder.append(_firstLower_72, "    ");
        _builder.append(" = qobject_cast<");
        String _name_421 = this._cppExtensions.toName(dto);
        _builder.append(_name_421, "    ");
        _builder.append(" *>(");
        String _name_422 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_422, "    ");
        _builder.append("List->object);");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("if (");
        String _name_423 = this._cppExtensions.toName(dto);
        String _firstLower_73 = StringExtensions.toFirstLower(_name_423);
        _builder.append(_firstLower_73, "    ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("if (");
        String _name_424 = this._cppExtensions.toName(dto);
        String _firstLower_74 = StringExtensions.toFirstLower(_name_424);
        _builder.append(_firstLower_74, "        ");
        _builder.append("->m");
        String _name_425 = this._cppExtensions.toName(feature_21);
        String _firstUpper_186 = StringExtensions.toFirstUpper(_name_425);
        _builder.append(_firstUpper_186, "        ");
        _builder.append(".size() > pos) {");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("return ");
        String _name_426 = this._cppExtensions.toName(dto);
        String _firstLower_75 = StringExtensions.toFirstLower(_name_426);
        _builder.append(_firstLower_75, "            ");
        _builder.append("->m");
        String _name_427 = this._cppExtensions.toName(feature_21);
        String _firstUpper_187 = StringExtensions.toFirstUpper(_name_427);
        _builder.append(_firstUpper_187, "            ");
        _builder.append(".at(pos);");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("qWarning() << \"cannot get ");
        String _typeName_90 = this._cppExtensions.toTypeName(feature_21);
        _builder.append(_typeName_90, "        ");
        _builder.append("* at pos \" << pos << \" size is \"");
        _builder.newLineIfNotEmpty();
        _builder.append("                ");
        _builder.append("<< ");
        String _name_428 = this._cppExtensions.toName(dto);
        String _firstLower_76 = StringExtensions.toFirstLower(_name_428);
        _builder.append(_firstLower_76, "                ");
        _builder.append("->m");
        String _name_429 = this._cppExtensions.toName(feature_21);
        String _firstUpper_188 = StringExtensions.toFirstUpper(_name_429);
        _builder.append(_firstUpper_188, "                ");
        _builder.append(".size();");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("} else {");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("qWarning() << \"cannot get ");
        String _typeName_91 = this._cppExtensions.toTypeName(feature_21);
        _builder.append(_typeName_91, "        ");
        _builder.append("* at pos \" << pos << \"Object is not of type ");
        String _name_430 = this._cppExtensions.toName(dto);
        _builder.append(_name_430, "        ");
        _builder.append("*\";");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("return 0;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_431 = this._cppExtensions.toName(dto);
        _builder.append(_name_431, "");
        _builder.append("::clear");
        String _name_432 = this._cppExtensions.toName(feature_21);
        String _firstUpper_189 = StringExtensions.toFirstUpper(_name_432);
        _builder.append(_firstUpper_189, "");
        _builder.append("Property(QDeclarativeListProperty<");
        String _typeName_92 = this._cppExtensions.toTypeName(feature_21);
        _builder.append(_typeName_92, "");
        _builder.append("> *");
        String _name_433 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_433, "");
        _builder.append("List)");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        String _name_434 = this._cppExtensions.toName(dto);
        _builder.append(_name_434, "    ");
        _builder.append(" *");
        String _name_435 = this._cppExtensions.toName(dto);
        String _firstLower_77 = StringExtensions.toFirstLower(_name_435);
        _builder.append(_firstLower_77, "    ");
        _builder.append(" = qobject_cast<");
        String _name_436 = this._cppExtensions.toName(dto);
        _builder.append(_name_436, "    ");
        _builder.append(" *>(");
        String _name_437 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_437, "    ");
        _builder.append("List->object);");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("if (");
        String _name_438 = this._cppExtensions.toName(dto);
        String _firstLower_78 = StringExtensions.toFirstLower(_name_438);
        _builder.append(_firstLower_78, "    ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        {
          boolean _hasOpposite_2 = this._cppExtensions.hasOpposite(feature_21);
          if (_hasOpposite_2) {
            _builder.append("        ");
            _builder.append("// ");
            String _name_439 = this._cppExtensions.toName(feature_21);
            _builder.append(_name_439, "        ");
            _builder.append(" are contained - so we must delete them");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            _builder.append("for (int i = 0; i < ");
            String _name_440 = this._cppExtensions.toName(dto);
            String _firstLower_79 = StringExtensions.toFirstLower(_name_440);
            _builder.append(_firstLower_79, "        ");
            _builder.append("->m");
            String _name_441 = this._cppExtensions.toName(feature_21);
            String _firstUpper_190 = StringExtensions.toFirstUpper(_name_441);
            _builder.append(_firstUpper_190, "        ");
            _builder.append(".size(); ++i) {");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            _builder.append("    ");
            String _name_442 = this._cppExtensions.toName(dto);
            String _firstLower_80 = StringExtensions.toFirstLower(_name_442);
            _builder.append(_firstLower_80, "            ");
            _builder.append("->m");
            String _name_443 = this._cppExtensions.toName(feature_21);
            String _firstUpper_191 = StringExtensions.toFirstUpper(_name_443);
            _builder.append(_firstUpper_191, "            ");
            _builder.append(".at(i)->deleteLater();");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            _builder.append("}");
            _builder.newLine();
          } else {
            _builder.append("        ");
            _builder.append("// ");
            String _name_444 = this._cppExtensions.toName(feature_21);
            _builder.append(_name_444, "        ");
            _builder.append(" are independent - DON\'T delete them");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("        ");
        String _name_445 = this._cppExtensions.toName(dto);
        String _firstLower_81 = StringExtensions.toFirstLower(_name_445);
        _builder.append(_firstLower_81, "        ");
        _builder.append("->m");
        String _name_446 = this._cppExtensions.toName(feature_21);
        String _firstUpper_192 = StringExtensions.toFirstUpper(_name_446);
        _builder.append(_firstUpper_192, "        ");
        _builder.append(".clear();");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("} else {");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("qWarning() << \"cannot clear ");
        String _name_447 = this._cppExtensions.toName(feature_21);
        _builder.append(_name_447, "        ");
        _builder.append(" \" << \"Object is not of type ");
        String _name_448 = this._cppExtensions.toName(dto);
        _builder.append(_name_448, "        ");
        _builder.append("*\";");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    String _name_449 = this._cppExtensions.toName(dto);
    _builder.append(_name_449, "");
    _builder.append("::~");
    String _name_450 = this._cppExtensions.toName(dto);
    _builder.append(_name_450, "");
    _builder.append("()");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// place cleanUp code here");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _foo(final LDtoAbstractAttribute att) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// ATT ");
    _builder.newLine();
    {
      boolean _isOptional = this._cppExtensions.isOptional(att);
      if (_isOptional) {
        _builder.append("// Optional: ");
        String _name = this._cppExtensions.toName(att);
        _builder.append(_name, "");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      boolean _isMandatory = this._cppExtensions.isMandatory(att);
      if (_isMandatory) {
        _builder.append("// Mandatory: ");
        String _name_1 = this._cppExtensions.toName(att);
        _builder.append(_name_1, "");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      boolean _isDomainKey = att.isDomainKey();
      if (_isDomainKey) {
        _builder.append("// Domain KEY: ");
        String _name_2 = this._cppExtensions.toName(att);
        _builder.append(_name_2, "");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      boolean _isEnum = this._cppExtensions.isEnum(att);
      if (_isEnum) {
        _builder.append("// ENUM: ");
        String _typeName = this._cppExtensions.toTypeName(att);
        _builder.append(_typeName, "");
        _builder.append("::");
        String _typeName_1 = this._cppExtensions.toTypeName(att);
        _builder.append(_typeName_1, "");
        _builder.append("Enum");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  protected CharSequence _foo(final LDtoAbstractReference ref) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// do ref ");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _foo(final LDtoReference ref) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// do DTO ref ");
    _builder.newLine();
    {
      LDtoReference _opposite = ref.getOpposite();
      boolean _notEquals = (!Objects.equal(_opposite, null));
      if (_notEquals) {
        _builder.append("// Opposite: ");
        LDtoReference _opposite_1 = ref.getOpposite();
        String _name = this._cppExtensions.toName(_opposite_1);
        _builder.append(_name, "");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      boolean _isLazy = ref.isLazy();
      if (_isLazy) {
        _builder.append("// Lazy: ");
        String _name_1 = this._cppExtensions.toName(ref);
        _builder.append(_name_1, "");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      boolean _isOptional = this._cppExtensions.isOptional(ref);
      if (_isOptional) {
        _builder.append("// Optional: ");
        String _name_2 = this._cppExtensions.toName(ref);
        _builder.append(_name_2, "");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      boolean _isMandatory = this._cppExtensions.isMandatory(ref);
      if (_isMandatory) {
        _builder.append("// Mandatory: ");
        String _name_3 = this._cppExtensions.toName(ref);
        _builder.append(_name_3, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  protected CharSequence _foo(final LFeature feature) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// just a helper for max superclass ");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence foo(final LFeature ref) {
    if (ref instanceof LDtoReference) {
      return _foo((LDtoReference)ref);
    } else if (ref instanceof LDtoAbstractAttribute) {
      return _foo((LDtoAbstractAttribute)ref);
    } else if (ref instanceof LDtoAbstractReference) {
      return _foo((LDtoAbstractReference)ref);
    } else if (ref != null) {
      return _foo(ref);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(ref).toString());
    }
  }
}
