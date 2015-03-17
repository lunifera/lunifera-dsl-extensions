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
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.lunifera.dsl.ext.dtos.cpp.qt.CppExtensions;
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
          boolean _isToMany = CppGenerator.this._cppExtensions.isToMany(it);
          boolean _not = (!_isToMany);
          if (!_not) {
            _and_1 = false;
          } else {
            boolean _isTypeOfDTO = CppGenerator.this._cppExtensions.isTypeOfDTO(it);
            boolean _not_1 = (!_isTypeOfDTO);
            _and_1 = _not_1;
          }
          if (!_and_1) {
            _and = false;
          } else {
            boolean _isContained = CppGenerator.this._cppExtensions.isContained(it);
            boolean _not_2 = (!_isContained);
            _and = _not_2;
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
    _builder.append("\t");
    _builder.append("//");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* initialize ");
    String _name_11 = this._cppExtensions.toName(dto);
    _builder.append(_name_11, " ");
    _builder.append(" from QVariantMap");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("* Map got from JsonDataAccess or so");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("void ");
    String _name_12 = this._cppExtensions.toName(dto);
    _builder.append(_name_12, "");
    _builder.append("::fillFromMap(const QVariantMap& ");
    String _name_13 = this._cppExtensions.toName(dto);
    String _firstLower = StringExtensions.toFirstLower(_name_13);
    _builder.append(_firstLower, "");
    _builder.append("Map)");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("m");
    String _name_14 = this._cppExtensions.toName(dto);
    String _firstUpper_2 = StringExtensions.toFirstUpper(_name_14);
    _builder.append(_firstUpper_2, "\t");
    _builder.append("Map = ");
    String _name_15 = this._cppExtensions.toName(dto);
    String _firstLower_1 = StringExtensions.toFirstLower(_name_15);
    _builder.append(_firstLower_1, "\t");
    _builder.append("Map;");
    _builder.newLineIfNotEmpty();
    {
      List<? extends LFeature> _allFeatures_4 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_2 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _isToMany = CppGenerator.this._cppExtensions.isToMany(it);
          return Boolean.valueOf((!_isToMany));
        }
      };
      Iterable<? extends LFeature> _filter_2 = IterableExtensions.filter(_allFeatures_4, _function_2);
      for(final LFeature feature_4 : _filter_2) {
        {
          boolean _isTypeOfDTO_2 = this._cppExtensions.isTypeOfDTO(feature_4);
          if (_isTypeOfDTO_2) {
            {
              boolean _isContained_3 = this._cppExtensions.isContained(feature_4);
              if (_isContained_3) {
                _builder.append("\t");
                _builder.append("// m");
                String _name_16 = this._cppExtensions.toName(feature_4);
                String _firstUpper_3 = StringExtensions.toFirstUpper(_name_16);
                _builder.append(_firstUpper_3, "\t");
                _builder.append(" is parent (");
                String _typeName_1 = this._cppExtensions.toTypeName(feature_4);
                _builder.append(_typeName_1, "\t");
                _builder.append("* containing ");
                String _name_17 = this._cppExtensions.toName(dto);
                _builder.append(_name_17, "\t");
                _builder.append(")");
                _builder.newLineIfNotEmpty();
              } else {
                boolean _isLazy = this._cppExtensions.isLazy(feature_4);
                if (_isLazy) {
                  _builder.append("\t");
                  _builder.append("// ");
                  String _name_18 = this._cppExtensions.toName(feature_4);
                  _builder.append(_name_18, "\t");
                  _builder.append(" lazy pointing to ");
                  String _typeOrQObject = this._cppExtensions.toTypeOrQObject(feature_4);
                  _builder.append(_typeOrQObject, "\t");
                  _builder.append(" (domainKey: ");
                  String _referenceDomainKey = this._cppExtensions.referenceDomainKey(feature_4);
                  _builder.append(_referenceDomainKey, "\t");
                  _builder.append(")");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("if(m");
                  String _name_19 = this._cppExtensions.toName(dto);
                  String _firstUpper_4 = StringExtensions.toFirstUpper(_name_19);
                  _builder.append(_firstUpper_4, "\t");
                  _builder.append("Map.contains(");
                  String _name_20 = this._cppExtensions.toName(feature_4);
                  _builder.append(_name_20, "\t");
                  _builder.append("Key)){");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("m");
                  String _name_21 = this._cppExtensions.toName(feature_4);
                  String _firstUpper_5 = StringExtensions.toFirstUpper(_name_21);
                  _builder.append(_firstUpper_5, "\t\t");
                  _builder.append(" = m");
                  String _name_22 = this._cppExtensions.toName(dto);
                  String _firstUpper_6 = StringExtensions.toFirstUpper(_name_22);
                  _builder.append(_firstUpper_6, "\t\t");
                  _builder.append("Map.value(");
                  String _name_23 = this._cppExtensions.toName(feature_4);
                  _builder.append(_name_23, "\t\t");
                  _builder.append("Key).to");
                  String _referenceDomainKeyType_1 = this._cppExtensions.referenceDomainKeyType(feature_4);
                  String _mapToLazyTypeName = this._cppExtensions.mapToLazyTypeName(_referenceDomainKeyType_1);
                  _builder.append(_mapToLazyTypeName, "\t\t");
                  _builder.append("();");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("if(m");
                  String _name_24 = this._cppExtensions.toName(feature_4);
                  String _firstUpper_7 = StringExtensions.toFirstUpper(_name_24);
                  _builder.append(_firstUpper_7, "\t\t");
                  _builder.append(" != ");
                  String _referenceDomainKeyType_2 = this._cppExtensions.referenceDomainKeyType(feature_4);
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
                  String _name_25 = this._cppExtensions.toName(feature_4);
                  String _firstUpper_8 = StringExtensions.toFirstUpper(_name_25);
                  _builder.append(_firstUpper_8, "\t\t\t");
                  _builder.append("AsDTO(m");
                  String _name_26 = this._cppExtensions.toName(feature_4);
                  String _firstUpper_9 = StringExtensions.toFirstUpper(_name_26);
                  _builder.append(_firstUpper_9, "\t\t\t");
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
                  String _name_27 = this._cppExtensions.toName(feature_4);
                  String _firstUpper_10 = StringExtensions.toFirstUpper(_name_27);
                  _builder.append(_firstUpper_10, "\t");
                  _builder.append(" points to ");
                  String _typeName_2 = this._cppExtensions.toTypeName(feature_4);
                  _builder.append(_typeName_2, "\t");
                  _builder.append("*");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("if(m");
                  String _name_28 = this._cppExtensions.toName(dto);
                  String _firstUpper_11 = StringExtensions.toFirstUpper(_name_28);
                  _builder.append(_firstUpper_11, "\t");
                  _builder.append("Map.contains(");
                  String _name_29 = this._cppExtensions.toName(feature_4);
                  _builder.append(_name_29, "\t");
                  _builder.append("Key)){");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("QVariantMap ");
                  String _name_30 = this._cppExtensions.toName(feature_4);
                  _builder.append(_name_30, "\t\t");
                  _builder.append("Map;");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  String _name_31 = this._cppExtensions.toName(feature_4);
                  _builder.append(_name_31, "\t\t");
                  _builder.append("Map = m");
                  String _name_32 = this._cppExtensions.toName(dto);
                  String _firstUpper_12 = StringExtensions.toFirstUpper(_name_32);
                  _builder.append(_firstUpper_12, "\t\t");
                  _builder.append("Map.value(");
                  String _name_33 = this._cppExtensions.toName(feature_4);
                  _builder.append(_name_33, "\t\t");
                  _builder.append("Key).toMap();");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("if(!");
                  String _name_34 = this._cppExtensions.toName(feature_4);
                  _builder.append(_name_34, "\t\t");
                  _builder.append("Map.isEmpty()){");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_35 = this._cppExtensions.toName(feature_4);
                  String _firstUpper_13 = StringExtensions.toFirstUpper(_name_35);
                  _builder.append(_firstUpper_13, "\t\t\t");
                  _builder.append(" = new ");
                  String _typeName_3 = this._cppExtensions.toTypeName(feature_4);
                  _builder.append(_typeName_3, "\t\t\t");
                  _builder.append("();");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_36 = this._cppExtensions.toName(feature_4);
                  String _firstUpper_14 = StringExtensions.toFirstUpper(_name_36);
                  _builder.append(_firstUpper_14, "\t\t\t");
                  _builder.append("->setParent(this);");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_37 = this._cppExtensions.toName(feature_4);
                  String _firstUpper_15 = StringExtensions.toFirstUpper(_name_37);
                  _builder.append(_firstUpper_15, "\t\t\t");
                  _builder.append("->fillFromMap(");
                  String _name_38 = this._cppExtensions.toName(feature_4);
                  _builder.append(_name_38, "\t\t\t");
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
              boolean _isTransient = this._cppExtensions.isTransient(feature_4);
              if (_isTransient) {
                _builder.append("\t");
                _builder.append("// m");
                String _name_39 = this._cppExtensions.toName(feature_4);
                String _firstUpper_16 = StringExtensions.toFirstUpper(_name_39);
                _builder.append(_firstUpper_16, "\t");
                _builder.append(" is transient - perhaps not included");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("if(m");
                String _name_40 = this._cppExtensions.toName(dto);
                String _firstUpper_17 = StringExtensions.toFirstUpper(_name_40);
                _builder.append(_firstUpper_17, "\t");
                _builder.append("Map.contains(");
                String _name_41 = this._cppExtensions.toName(feature_4);
                String _firstLower_2 = StringExtensions.toFirstLower(_name_41);
                _builder.append(_firstLower_2, "\t");
                _builder.append("Key)){");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("m");
                String _name_42 = this._cppExtensions.toName(feature_4);
                String _firstUpper_18 = StringExtensions.toFirstUpper(_name_42);
                _builder.append(_firstUpper_18, "\t\t");
                _builder.append(" = m");
                String _name_43 = this._cppExtensions.toName(dto);
                String _firstUpper_19 = StringExtensions.toFirstUpper(_name_43);
                _builder.append(_firstUpper_19, "\t\t");
                _builder.append("Map.value(");
                String _name_44 = this._cppExtensions.toName(feature_4);
                _builder.append(_name_44, "\t\t");
                _builder.append("Key).to");
                String _mapToType = this._cppExtensions.mapToType(feature_4);
                _builder.append(_mapToType, "\t\t");
                _builder.append("();");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("}");
                _builder.newLine();
              } else {
                _builder.append("\t");
                _builder.append("m");
                String _name_45 = this._cppExtensions.toName(feature_4);
                String _firstUpper_20 = StringExtensions.toFirstUpper(_name_45);
                _builder.append(_firstUpper_20, "\t");
                _builder.append(" = m");
                String _name_46 = this._cppExtensions.toName(dto);
                String _firstUpper_21 = StringExtensions.toFirstUpper(_name_46);
                _builder.append(_firstUpper_21, "\t");
                _builder.append("Map.value(");
                String _name_47 = this._cppExtensions.toName(feature_4);
                _builder.append(_name_47, "\t");
                _builder.append("Key).to");
                String _mapToType_1 = this._cppExtensions.mapToType(feature_4);
                _builder.append(_mapToType_1, "\t");
                _builder.append("();");
                _builder.newLineIfNotEmpty();
              }
            }
            {
              String _name_48 = this._cppExtensions.toName(feature_4);
              boolean _equals = Objects.equal(_name_48, "uuid");
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
      List<? extends LFeature> _allFeatures_5 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_3 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          return Boolean.valueOf(CppGenerator.this._cppExtensions.isToMany(it));
        }
      };
      Iterable<? extends LFeature> _filter_3 = IterableExtensions.filter(_allFeatures_5, _function_3);
      for(final LFeature feature_5 : _filter_3) {
        _builder.append("\t");
        _builder.append("// m");
        String _name_49 = this._cppExtensions.toName(feature_5);
        String _firstUpper_22 = StringExtensions.toFirstUpper(_name_49);
        _builder.append(_firstUpper_22, "\t");
        _builder.append(" is List of ");
        String _typeName_4 = this._cppExtensions.toTypeName(feature_5);
        _builder.append(_typeName_4, "\t");
        _builder.append("*");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("QVariantList ");
        String _name_50 = this._cppExtensions.toName(feature_5);
        String _firstLower_3 = StringExtensions.toFirstLower(_name_50);
        _builder.append(_firstLower_3, "\t");
        _builder.append("List;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        String _name_51 = this._cppExtensions.toName(feature_5);
        String _firstLower_4 = StringExtensions.toFirstLower(_name_51);
        _builder.append(_firstLower_4, "\t");
        _builder.append("List = m");
        String _name_52 = this._cppExtensions.toName(dto);
        String _firstUpper_23 = StringExtensions.toFirstUpper(_name_52);
        _builder.append(_firstUpper_23, "\t");
        _builder.append("Map.value(");
        String _name_53 = this._cppExtensions.toName(feature_5);
        String _firstLower_5 = StringExtensions.toFirstLower(_name_53);
        _builder.append(_firstLower_5, "\t");
        _builder.append("Key).toList();");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("m");
        String _name_54 = this._cppExtensions.toName(feature_5);
        String _firstUpper_24 = StringExtensions.toFirstUpper(_name_54);
        _builder.append(_firstUpper_24, "\t");
        _builder.append(".clear();");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("for (int i = 0; i < ");
        String _name_55 = this._cppExtensions.toName(feature_5);
        String _firstLower_6 = StringExtensions.toFirstLower(_name_55);
        _builder.append(_firstLower_6, "\t");
        _builder.append("List.size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("QVariantMap ");
        String _name_56 = this._cppExtensions.toName(feature_5);
        String _firstLower_7 = StringExtensions.toFirstLower(_name_56);
        _builder.append(_firstLower_7, "\t\t");
        _builder.append("Map;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        String _typeName_5 = this._cppExtensions.toTypeName(feature_5);
        _builder.append(_typeName_5, "\t\t");
        _builder.append("* ");
        String _typeName_6 = this._cppExtensions.toTypeName(feature_5);
        String _firstLower_8 = StringExtensions.toFirstLower(_typeName_6);
        _builder.append(_firstLower_8, "\t\t");
        _builder.append(" = new ");
        String _typeName_7 = this._cppExtensions.toTypeName(feature_5);
        _builder.append(_typeName_7, "\t\t");
        _builder.append("();");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        String _typeName_8 = this._cppExtensions.toTypeName(feature_5);
        String _firstLower_9 = StringExtensions.toFirstLower(_typeName_8);
        _builder.append(_firstLower_9, "\t\t");
        _builder.append("->setParent(this);");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        String _typeName_9 = this._cppExtensions.toTypeName(feature_5);
        String _firstLower_10 = StringExtensions.toFirstLower(_typeName_9);
        _builder.append(_firstLower_10, "\t\t");
        _builder.append("->fillFromMap(");
        String _name_57 = this._cppExtensions.toName(feature_5);
        String _firstLower_11 = StringExtensions.toFirstLower(_name_57);
        _builder.append(_firstLower_11, "\t\t");
        _builder.append("Map);");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("m");
        String _name_58 = this._cppExtensions.toName(feature_5);
        String _firstUpper_25 = StringExtensions.toFirstUpper(_name_58);
        _builder.append(_firstUpper_25, "\t\t");
        _builder.append(".append(");
        String _typeName_10 = this._cppExtensions.toTypeName(feature_5);
        String _firstLower_12 = StringExtensions.toFirstLower(_typeName_10);
        _builder.append(_firstLower_12, "\t\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("void ");
    String _name_59 = this._cppExtensions.toName(dto);
    _builder.append(_name_59, "");
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
    String _name_60 = this._cppExtensions.toName(dto);
    _builder.append(_name_60, "");
    _builder.append("::isValid()");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_6 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_4 = new Function1<LFeature, Boolean>() {
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
      Iterable<? extends LFeature> _filter_4 = IterableExtensions.filter(_allFeatures_6, _function_4);
      for(final LFeature feature_6 : _filter_4) {
        {
          boolean _isToMany = this._cppExtensions.isToMany(feature_6);
          if (_isToMany) {
            _builder.append("\t");
            _builder.append("if(m");
            String _name_61 = this._cppExtensions.toName(feature_6);
            String _firstUpper_26 = StringExtensions.toFirstUpper(_name_61);
            _builder.append(_firstUpper_26, "\t");
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
            boolean _isLazy_1 = this._cppExtensions.isLazy(feature_6);
            if (_isLazy_1) {
              _builder.append("\t");
              _builder.append("// ");
              String _name_62 = this._cppExtensions.toName(feature_6);
              _builder.append(_name_62, "\t");
              _builder.append(" lazy pointing to ");
              String _typeOrQObject_1 = this._cppExtensions.toTypeOrQObject(feature_6);
              _builder.append(_typeOrQObject_1, "\t");
              _builder.append(" (domainKey: ");
              String _referenceDomainKey_1 = this._cppExtensions.referenceDomainKey(feature_6);
              _builder.append(_referenceDomainKey_1, "\t");
              _builder.append(")");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              LFeature _referenceDomainKeyFeature = this._cppExtensions.referenceDomainKeyFeature(feature_6);
              String _typeName_11 = this._cppExtensions.toTypeName(_referenceDomainKeyFeature);
              String _name_63 = this._cppExtensions.toName(feature_6);
              String _validateReference = this._cppExtensions.toValidateReference(_typeName_11, _name_63);
              _builder.append(_validateReference, "\t");
              _builder.newLineIfNotEmpty();
            } else {
              boolean _isTypeOfDTO_3 = this._cppExtensions.isTypeOfDTO(feature_6);
              if (_isTypeOfDTO_3) {
                _builder.append("\t");
                _builder.append("if(!m");
                String _name_64 = this._cppExtensions.toName(feature_6);
                String _firstUpper_27 = StringExtensions.toFirstUpper(_name_64);
                _builder.append(_firstUpper_27, "\t");
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
                String _validate = this._cppExtensions.toValidate(feature_6);
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
    String _name_65 = this._cppExtensions.toName(dto);
    _builder.append(_name_65, " ");
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
    String _name_66 = this._cppExtensions.toName(dto);
    _builder.append(_name_66, "");
    _builder.append("::toMap()");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_7 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_5 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          return Boolean.valueOf(CppGenerator.this._cppExtensions.isLazy(it));
        }
      };
      Iterable<? extends LFeature> _filter_5 = IterableExtensions.filter(_allFeatures_7, _function_5);
      for(final LFeature feature_7 : _filter_5) {
        _builder.append("\t");
        _builder.append("// ");
        String _name_67 = this._cppExtensions.toName(feature_7);
        _builder.append(_name_67, "\t");
        _builder.append(" lazy pointing to ");
        String _typeOrQObject_2 = this._cppExtensions.toTypeOrQObject(feature_7);
        _builder.append(_typeOrQObject_2, "\t");
        _builder.append(" (domainKey: ");
        String _referenceDomainKey_2 = this._cppExtensions.referenceDomainKey(feature_7);
        _builder.append(_referenceDomainKey_2, "\t");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("m");
        String _name_68 = this._cppExtensions.toName(dto);
        String _firstUpper_28 = StringExtensions.toFirstUpper(_name_68);
        _builder.append(_firstUpper_28, "\t");
        _builder.append("Map.insert(");
        String _name_69 = this._cppExtensions.toName(feature_7);
        _builder.append(_name_69, "\t");
        _builder.append("Key, m");
        String _name_70 = this._cppExtensions.toName(feature_7);
        String _firstUpper_29 = StringExtensions.toFirstUpper(_name_70);
        _builder.append(_firstUpper_29, "\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      List<? extends LFeature> _allFeatures_8 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_6 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _isLazy = CppGenerator.this._cppExtensions.isLazy(it);
          return Boolean.valueOf((!_isLazy));
        }
      };
      Iterable<? extends LFeature> _filter_6 = IterableExtensions.filter(_allFeatures_8, _function_6);
      for(final LFeature feature_8 : _filter_6) {
        {
          boolean _isTypeOfDTO_4 = this._cppExtensions.isTypeOfDTO(feature_8);
          if (_isTypeOfDTO_4) {
            {
              boolean _isContained_4 = this._cppExtensions.isContained(feature_8);
              boolean _not = (!_isContained_4);
              if (_not) {
                _builder.append("\t");
                _builder.append("// m");
                String _name_71 = this._cppExtensions.toName(feature_8);
                String _firstUpper_30 = StringExtensions.toFirstUpper(_name_71);
                _builder.append(_firstUpper_30, "\t");
                _builder.append(" points to ");
                String _typeName_12 = this._cppExtensions.toTypeName(feature_8);
                _builder.append(_typeName_12, "\t");
                _builder.append("*");
                _builder.newLineIfNotEmpty();
                {
                  boolean _isToMany_1 = this._cppExtensions.isToMany(feature_8);
                  if (_isToMany_1) {
                    _builder.append("\t");
                    _builder.append("m");
                    String _name_72 = this._cppExtensions.toName(dto);
                    String _firstUpper_31 = StringExtensions.toFirstUpper(_name_72);
                    _builder.append(_firstUpper_31, "\t");
                    _builder.append("Map.insert(");
                    String _name_73 = this._cppExtensions.toName(feature_8);
                    _builder.append(_name_73, "\t");
                    _builder.append("Key, ");
                    String _name_74 = this._cppExtensions.toName(feature_8);
                    _builder.append(_name_74, "\t");
                    _builder.append("AsQVariantList());");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t");
                    _builder.append("if(m");
                    String _name_75 = this._cppExtensions.toName(feature_8);
                    String _firstUpper_32 = StringExtensions.toFirstUpper(_name_75);
                    _builder.append(_firstUpper_32, "\t");
                    _builder.append("){");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("m");
                    String _name_76 = this._cppExtensions.toName(dto);
                    String _firstUpper_33 = StringExtensions.toFirstUpper(_name_76);
                    _builder.append(_firstUpper_33, "\t\t");
                    _builder.append("Map.insert(");
                    String _name_77 = this._cppExtensions.toName(feature_8);
                    _builder.append(_name_77, "\t\t");
                    _builder.append("Key, m");
                    String _name_78 = this._cppExtensions.toName(feature_8);
                    String _firstUpper_34 = StringExtensions.toFirstUpper(_name_78);
                    _builder.append(_firstUpper_34, "\t\t");
                    _builder.append("->to");
                    String _mapOrList = this._cppExtensions.toMapOrList(feature_8);
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
                String _name_79 = this._cppExtensions.toName(feature_8);
                String _firstUpper_35 = StringExtensions.toFirstUpper(_name_79);
                _builder.append(_firstUpper_35, "\t");
                _builder.append(" points to ");
                String _typeName_13 = this._cppExtensions.toTypeName(feature_8);
                _builder.append(_typeName_13, "\t");
                _builder.append("* containing ");
                String _name_80 = this._cppExtensions.toName(dto);
                _builder.append(_name_80, "\t");
                _builder.newLineIfNotEmpty();
              }
            }
          } else {
            _builder.append("\t");
            _builder.append("m");
            String _name_81 = this._cppExtensions.toName(dto);
            String _firstUpper_36 = StringExtensions.toFirstUpper(_name_81);
            _builder.append(_firstUpper_36, "\t");
            _builder.append("Map.insert(");
            String _name_82 = this._cppExtensions.toName(feature_8);
            _builder.append(_name_82, "\t");
            _builder.append("Key, m");
            String _name_83 = this._cppExtensions.toName(feature_8);
            String _firstUpper_37 = StringExtensions.toFirstUpper(_name_83);
            _builder.append(_firstUpper_37, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("return m");
    String _name_84 = this._cppExtensions.toName(dto);
    String _firstUpper_38 = StringExtensions.toFirstUpper(_name_84);
    _builder.append(_firstUpper_38, "\t");
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
        String _name_85 = this._cppExtensions.toName(dto);
        _builder.append(_name_85, " ");
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
        String _name_86 = this._cppExtensions.toName(dto);
        _builder.append(_name_86, "");
        _builder.append("::toForeignMap()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("QVariantMap foreignMap;");
        _builder.newLine();
        {
          List<? extends LFeature> _allFeatures_9 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_7 = new Function1<LFeature, Boolean>() {
            public Boolean apply(final LFeature it) {
              return Boolean.valueOf(CppGenerator.this._cppExtensions.isLazy(it));
            }
          };
          Iterable<? extends LFeature> _filter_7 = IterableExtensions.filter(_allFeatures_9, _function_7);
          for(final LFeature feature_9 : _filter_7) {
            _builder.append("\t");
            _builder.append("// ");
            String _name_87 = this._cppExtensions.toName(feature_9);
            _builder.append(_name_87, "\t");
            _builder.append(" lazy pointing to ");
            String _typeOrQObject_3 = this._cppExtensions.toTypeOrQObject(feature_9);
            _builder.append(_typeOrQObject_3, "\t");
            _builder.append(" (domainKey: ");
            String _referenceDomainKey_3 = this._cppExtensions.referenceDomainKey(feature_9);
            _builder.append(_referenceDomainKey_3, "\t");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("foreignMap.insert(");
            String _name_88 = this._cppExtensions.toName(feature_9);
            _builder.append(_name_88, "\t");
            _builder.append("ForeignKey, m");
            String _name_89 = this._cppExtensions.toName(feature_9);
            String _firstUpper_39 = StringExtensions.toFirstUpper(_name_89);
            _builder.append(_firstUpper_39, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
          }
        }
        {
          List<? extends LFeature> _allFeatures_10 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_8 = new Function1<LFeature, Boolean>() {
            public Boolean apply(final LFeature it) {
              boolean _isLazy = CppGenerator.this._cppExtensions.isLazy(it);
              return Boolean.valueOf((!_isLazy));
            }
          };
          Iterable<? extends LFeature> _filter_8 = IterableExtensions.filter(_allFeatures_10, _function_8);
          for(final LFeature feature_10 : _filter_8) {
            {
              boolean _isTypeOfDTO_5 = this._cppExtensions.isTypeOfDTO(feature_10);
              if (_isTypeOfDTO_5) {
                {
                  boolean _isContained_5 = this._cppExtensions.isContained(feature_10);
                  boolean _not_1 = (!_isContained_5);
                  if (_not_1) {
                    _builder.append("\t");
                    _builder.append("// m");
                    String _name_90 = this._cppExtensions.toName(feature_10);
                    String _firstUpper_40 = StringExtensions.toFirstUpper(_name_90);
                    _builder.append(_firstUpper_40, "\t");
                    _builder.append(" points to ");
                    String _typeName_14 = this._cppExtensions.toTypeName(feature_10);
                    _builder.append(_typeName_14, "\t");
                    _builder.append("*");
                    _builder.newLineIfNotEmpty();
                    {
                      boolean _isToMany_2 = this._cppExtensions.isToMany(feature_10);
                      if (_isToMany_2) {
                        _builder.append("\t");
                        _builder.append("foreignMap.insert(");
                        String _name_91 = this._cppExtensions.toName(feature_10);
                        _builder.append(_name_91, "\t");
                        _builder.append("ForeignKey, ");
                        String _name_92 = this._cppExtensions.toName(feature_10);
                        _builder.append(_name_92, "\t");
                        _builder.append("AsQVariantList());");
                        _builder.newLineIfNotEmpty();
                      } else {
                        _builder.append("\t");
                        _builder.append("if(m");
                        String _name_93 = this._cppExtensions.toName(feature_10);
                        String _firstUpper_41 = StringExtensions.toFirstUpper(_name_93);
                        _builder.append(_firstUpper_41, "\t");
                        _builder.append("){");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("foreignMap.insert(");
                        String _name_94 = this._cppExtensions.toName(feature_10);
                        _builder.append(_name_94, "\t\t");
                        _builder.append("ForeignKey, m");
                        String _name_95 = this._cppExtensions.toName(feature_10);
                        String _firstUpper_42 = StringExtensions.toFirstUpper(_name_95);
                        _builder.append(_firstUpper_42, "\t\t");
                        _builder.append("->to");
                        String _mapOrList_1 = this._cppExtensions.toMapOrList(feature_10);
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
                    String _name_96 = this._cppExtensions.toName(feature_10);
                    String _firstUpper_43 = StringExtensions.toFirstUpper(_name_96);
                    _builder.append(_firstUpper_43, "\t");
                    _builder.append(" points to ");
                    String _typeName_15 = this._cppExtensions.toTypeName(feature_10);
                    _builder.append(_typeName_15, "\t");
                    _builder.append("* containing ");
                    String _name_97 = this._cppExtensions.toName(dto);
                    _builder.append(_name_97, "\t");
                    _builder.newLineIfNotEmpty();
                  }
                }
              } else {
                _builder.append("\t");
                _builder.append("foreignMap.insert(");
                String _name_98 = this._cppExtensions.toName(feature_10);
                _builder.append(_name_98, "\t");
                _builder.append("ForeignKey, m");
                String _name_99 = this._cppExtensions.toName(feature_10);
                String _firstUpper_44 = StringExtensions.toFirstUpper(_name_99);
                _builder.append(_firstUpper_44, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
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
    String _name_100 = this._cppExtensions.toName(dto);
    _builder.append(_name_100, " ");
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
    String _name_101 = this._cppExtensions.toName(dto);
    _builder.append(_name_101, "");
    _builder.append("::dataToPersist()");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("QVariantMap persistMap;");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_11 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_9 = new Function1<LFeature, Boolean>() {
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
      Iterable<? extends LFeature> _filter_9 = IterableExtensions.filter(_allFeatures_11, _function_9);
      for(final LFeature feature_11 : _filter_9) {
        _builder.append("\t");
        _builder.append("// ");
        String _name_102 = this._cppExtensions.toName(feature_11);
        _builder.append(_name_102, "\t");
        _builder.append(" lazy pointing to ");
        String _typeOrQObject_4 = this._cppExtensions.toTypeOrQObject(feature_11);
        _builder.append(_typeOrQObject_4, "\t");
        _builder.append(" (domainKey: ");
        String _referenceDomainKey_4 = this._cppExtensions.referenceDomainKey(feature_11);
        _builder.append(_referenceDomainKey_4, "\t");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("persistMap.insert(");
        String _name_103 = this._cppExtensions.toName(feature_11);
        _builder.append(_name_103, "\t");
        _builder.append("Key, m");
        String _name_104 = this._cppExtensions.toName(feature_11);
        String _firstUpper_45 = StringExtensions.toFirstUpper(_name_104);
        _builder.append(_firstUpper_45, "\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      List<? extends LFeature> _allFeatures_12 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_10 = new Function1<LFeature, Boolean>() {
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
      Iterable<? extends LFeature> _filter_10 = IterableExtensions.filter(_allFeatures_12, _function_10);
      for(final LFeature feature_12 : _filter_10) {
        {
          boolean _isTypeOfDTO_6 = this._cppExtensions.isTypeOfDTO(feature_12);
          if (_isTypeOfDTO_6) {
            {
              boolean _isContained_6 = this._cppExtensions.isContained(feature_12);
              boolean _not_2 = (!_isContained_6);
              if (_not_2) {
                _builder.append("\t");
                _builder.append("// m");
                String _name_105 = this._cppExtensions.toName(feature_12);
                String _firstUpper_46 = StringExtensions.toFirstUpper(_name_105);
                _builder.append(_firstUpper_46, "\t");
                _builder.append(" points to ");
                String _typeName_16 = this._cppExtensions.toTypeName(feature_12);
                _builder.append(_typeName_16, "\t");
                _builder.append("*");
                _builder.newLineIfNotEmpty();
                {
                  boolean _isToMany_3 = this._cppExtensions.isToMany(feature_12);
                  if (_isToMany_3) {
                    _builder.append("\t");
                    _builder.append("persistMap.insert(");
                    String _name_106 = this._cppExtensions.toName(feature_12);
                    _builder.append(_name_106, "\t");
                    _builder.append("Key, ");
                    String _name_107 = this._cppExtensions.toName(feature_12);
                    _builder.append(_name_107, "\t");
                    _builder.append("AsQVariantList());");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t");
                    _builder.append("if(m");
                    String _name_108 = this._cppExtensions.toName(feature_12);
                    String _firstUpper_47 = StringExtensions.toFirstUpper(_name_108);
                    _builder.append(_firstUpper_47, "\t");
                    _builder.append("){");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("persistMap.insert(");
                    String _name_109 = this._cppExtensions.toName(feature_12);
                    _builder.append(_name_109, "\t\t");
                    _builder.append("Key, m");
                    String _name_110 = this._cppExtensions.toName(feature_12);
                    String _firstUpper_48 = StringExtensions.toFirstUpper(_name_110);
                    _builder.append(_firstUpper_48, "\t\t");
                    _builder.append("->to");
                    String _mapOrList_2 = this._cppExtensions.toMapOrList(feature_12);
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
                String _name_111 = this._cppExtensions.toName(feature_12);
                String _firstUpper_49 = StringExtensions.toFirstUpper(_name_111);
                _builder.append(_firstUpper_49, "\t");
                _builder.append(" points to ");
                String _typeName_17 = this._cppExtensions.toTypeName(feature_12);
                _builder.append(_typeName_17, "\t");
                _builder.append("* containing ");
                String _name_112 = this._cppExtensions.toName(dto);
                _builder.append(_name_112, "\t");
                _builder.newLineIfNotEmpty();
              }
            }
          } else {
            _builder.append("\t");
            _builder.append("persistMap.insert(");
            String _name_113 = this._cppExtensions.toName(feature_12);
            _builder.append(_name_113, "\t");
            _builder.append("Key, m");
            String _name_114 = this._cppExtensions.toName(feature_12);
            String _firstUpper_50 = StringExtensions.toFirstUpper(_name_114);
            _builder.append(_firstUpper_50, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      List<? extends LFeature> _allFeatures_13 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_11 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          return Boolean.valueOf(CppGenerator.this._cppExtensions.isTransient(it));
        }
      };
      Iterable<? extends LFeature> _filter_11 = IterableExtensions.filter(_allFeatures_13, _function_11);
      for(final LFeature feature_13 : _filter_11) {
        _builder.append("\t");
        _builder.append("// excluded: m");
        String _name_115 = this._cppExtensions.toName(feature_13);
        String _firstUpper_51 = StringExtensions.toFirstUpper(_name_115);
        _builder.append(_firstUpper_51, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("return persistMap;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_14 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_12 = new Function1<LFeature, Boolean>() {
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
      Iterable<? extends LFeature> _filter_12 = IterableExtensions.filter(_allFeatures_14, _function_12);
      for(final LFeature feature_14 : _filter_12) {
        CharSequence _foo = this.foo(feature_14);
        _builder.append(_foo, "");
        _builder.newLineIfNotEmpty();
        _builder.append("// ");
        String _name_116 = this._cppExtensions.toName(feature_14);
        _builder.append(_name_116, "");
        _builder.append(" lazy pointing to ");
        String _typeOrQObject_5 = this._cppExtensions.toTypeOrQObject(feature_14);
        _builder.append(_typeOrQObject_5, "");
        _builder.append(" (domainKey: ");
        String _referenceDomainKey_5 = this._cppExtensions.referenceDomainKey(feature_14);
        _builder.append(_referenceDomainKey_5, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        String _referenceDomainKeyType_3 = this._cppExtensions.referenceDomainKeyType(feature_14);
        _builder.append(_referenceDomainKeyType_3, "");
        _builder.append(" ");
        String _name_117 = this._cppExtensions.toName(dto);
        _builder.append(_name_117, "");
        _builder.append("::");
        String _name_118 = this._cppExtensions.toName(feature_14);
        _builder.append(_name_118, "");
        _builder.append("() const");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return m");
        String _name_119 = this._cppExtensions.toName(feature_14);
        String _firstUpper_52 = StringExtensions.toFirstUpper(_name_119);
        _builder.append(_firstUpper_52, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        String _typeOrQObject_6 = this._cppExtensions.toTypeOrQObject(feature_14);
        _builder.append(_typeOrQObject_6, "");
        _builder.append(" ");
        String _name_120 = this._cppExtensions.toName(dto);
        _builder.append(_name_120, "");
        _builder.append("::");
        String _name_121 = this._cppExtensions.toName(feature_14);
        _builder.append(_name_121, "");
        _builder.append("AsDTO() const");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return m");
        String _name_122 = this._cppExtensions.toName(feature_14);
        String _firstUpper_53 = StringExtensions.toFirstUpper(_name_122);
        _builder.append(_firstUpper_53, "\t");
        _builder.append("AsDTO;");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_123 = this._cppExtensions.toName(dto);
        _builder.append(_name_123, "");
        _builder.append("::set");
        String _name_124 = this._cppExtensions.toName(feature_14);
        String _firstUpper_54 = StringExtensions.toFirstUpper(_name_124);
        _builder.append(_firstUpper_54, "");
        _builder.append("(");
        String _referenceDomainKeyType_4 = this._cppExtensions.referenceDomainKeyType(feature_14);
        _builder.append(_referenceDomainKeyType_4, "");
        _builder.append(" ");
        String _name_125 = this._cppExtensions.toName(feature_14);
        _builder.append(_name_125, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (");
        String _name_126 = this._cppExtensions.toName(feature_14);
        _builder.append(_name_126, "\t");
        _builder.append(" != m");
        String _name_127 = this._cppExtensions.toName(feature_14);
        String _firstUpper_55 = StringExtensions.toFirstUpper(_name_127);
        _builder.append(_firstUpper_55, "\t");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("if (");
        String _name_128 = this._cppExtensions.toName(feature_14);
        _builder.append(_name_128, "\t\t");
        _builder.append(" != ");
        String _referenceDomainKeyType_5 = this._cppExtensions.referenceDomainKeyType(feature_14);
        String _defaultForLazyTypeName_2 = this._cppExtensions.defaultForLazyTypeName(_referenceDomainKeyType_5);
        _builder.append(_defaultForLazyTypeName_2, "\t\t");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("// connected from DTOManager to lookup for DTO");
        _builder.newLine();
        _builder.append("            ");
        _builder.append("emit request");
        String _name_129 = this._cppExtensions.toName(feature_14);
        String _firstUpper_56 = StringExtensions.toFirstUpper(_name_129);
        _builder.append(_firstUpper_56, "            ");
        _builder.append("AsDTO(");
        String _name_130 = this._cppExtensions.toName(feature_14);
        _builder.append(_name_130, "            ");
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
        String _name_131 = this._cppExtensions.toName(feature_14);
        String _firstUpper_57 = StringExtensions.toFirstUpper(_name_131);
        _builder.append(_firstUpper_57, "            ");
        _builder.append("AsDTO = 0;");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("emit ");
        String _name_132 = this._cppExtensions.toName(feature_14);
        _builder.append(_name_132, "            ");
        _builder.append("Removed(m");
        String _name_133 = this._cppExtensions.toName(feature_14);
        String _firstUpper_58 = StringExtensions.toFirstUpper(_name_133);
        _builder.append(_firstUpper_58, "            ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("m");
        String _name_134 = this._cppExtensions.toName(feature_14);
        String _firstUpper_59 = StringExtensions.toFirstUpper(_name_134);
        _builder.append(_firstUpper_59, "        ");
        _builder.append(" = ");
        String _name_135 = this._cppExtensions.toName(feature_14);
        _builder.append(_name_135, "        ");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("emit ");
        String _name_136 = this._cppExtensions.toName(feature_14);
        _builder.append(_name_136, "        ");
        _builder.append("Changed(");
        String _name_137 = this._cppExtensions.toName(feature_14);
        _builder.append(_name_137, "        ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_138 = this._cppExtensions.toName(dto);
        _builder.append(_name_138, "");
        _builder.append("::remove");
        String _name_139 = this._cppExtensions.toName(feature_14);
        String _firstUpper_60 = StringExtensions.toFirstUpper(_name_139);
        _builder.append(_firstUpper_60, "");
        _builder.append("()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("set");
        String _name_140 = this._cppExtensions.toName(feature_14);
        String _firstUpper_61 = StringExtensions.toFirstUpper(_name_140);
        _builder.append(_firstUpper_61, "\t");
        _builder.append("(");
        String _referenceDomainKeyType_6 = this._cppExtensions.referenceDomainKeyType(feature_14);
        String _defaultForLazyTypeName_3 = this._cppExtensions.defaultForLazyTypeName(_referenceDomainKeyType_6);
        _builder.append(_defaultForLazyTypeName_3, "\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("bool ");
        String _name_141 = this._cppExtensions.toName(dto);
        _builder.append(_name_141, "");
        _builder.append("::has");
        String _name_142 = this._cppExtensions.toName(feature_14);
        String _firstUpper_62 = StringExtensions.toFirstUpper(_name_142);
        _builder.append(_firstUpper_62, "");
        _builder.append("(){");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("if(m");
        String _name_143 = this._cppExtensions.toName(feature_14);
        String _firstUpper_63 = StringExtensions.toFirstUpper(_name_143);
        _builder.append(_firstUpper_63, "    ");
        _builder.append(" != ");
        String _referenceDomainKeyType_7 = this._cppExtensions.referenceDomainKeyType(feature_14);
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
        String _name_144 = this._cppExtensions.toName(dto);
        _builder.append(_name_144, "");
        _builder.append("::has");
        String _name_145 = this._cppExtensions.toName(feature_14);
        String _firstUpper_64 = StringExtensions.toFirstUpper(_name_145);
        _builder.append(_firstUpper_64, "");
        _builder.append("AsDTO(){");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("if(m");
        String _name_146 = this._cppExtensions.toName(feature_14);
        String _firstUpper_65 = StringExtensions.toFirstUpper(_name_146);
        _builder.append(_firstUpper_65, "    ");
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
        String _name_147 = this._cppExtensions.toName(dto);
        _builder.append(_name_147, "");
        _builder.append("::onRequested");
        String _name_148 = this._cppExtensions.toName(feature_14);
        String _firstUpper_66 = StringExtensions.toFirstUpper(_name_148);
        _builder.append(_firstUpper_66, "");
        _builder.append("AsDTO(");
        String _typeOrQObject_7 = this._cppExtensions.toTypeOrQObject(feature_14);
        _builder.append(_typeOrQObject_7, "");
        _builder.append(" ");
        String _typeName_18 = this._cppExtensions.toTypeName(feature_14);
        String _firstLower_13 = StringExtensions.toFirstLower(_typeName_18);
        _builder.append(_firstLower_13, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("if (");
        String _typeName_19 = this._cppExtensions.toTypeName(feature_14);
        String _firstLower_14 = StringExtensions.toFirstLower(_typeName_19);
        _builder.append(_firstLower_14, "    ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("if (");
        String _typeName_20 = this._cppExtensions.toTypeName(feature_14);
        String _firstLower_15 = StringExtensions.toFirstLower(_typeName_20);
        _builder.append(_firstLower_15, "        ");
        _builder.append("->");
        String _referenceDomainKey_6 = this._cppExtensions.referenceDomainKey(feature_14);
        _builder.append(_referenceDomainKey_6, "        ");
        _builder.append("() == m");
        String _name_149 = this._cppExtensions.toName(feature_14);
        String _firstUpper_67 = StringExtensions.toFirstUpper(_name_149);
        _builder.append(_firstUpper_67, "        ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("m");
        String _name_150 = this._cppExtensions.toName(feature_14);
        String _firstUpper_68 = StringExtensions.toFirstUpper(_name_150);
        _builder.append(_firstUpper_68, "            ");
        _builder.append("AsDTO = ");
        String _typeName_21 = this._cppExtensions.toTypeName(feature_14);
        String _firstLower_16 = StringExtensions.toFirstLower(_typeName_21);
        _builder.append(_firstLower_16, "            ");
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
      List<? extends LFeature> _allFeatures_15 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_13 = new Function1<LFeature, Boolean>() {
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
      Iterable<? extends LFeature> _filter_13 = IterableExtensions.filter(_allFeatures_15, _function_13);
      for(final LFeature feature_15 : _filter_13) {
        CharSequence _foo_1 = this.foo(feature_15);
        _builder.append(_foo_1, "");
        _builder.newLineIfNotEmpty();
        {
          boolean _and_2 = false;
          boolean _isTypeOfDTO_7 = this._cppExtensions.isTypeOfDTO(feature_15);
          if (!_isTypeOfDTO_7) {
            _and_2 = false;
          } else {
            boolean _isContained_7 = this._cppExtensions.isContained(feature_15);
            _and_2 = _isContained_7;
          }
          if (_and_2) {
            _builder.append("// No SETTER for ");
            String _name_151 = this._cppExtensions.toName(feature_15);
            String _firstUpper_69 = StringExtensions.toFirstUpper(_name_151);
            _builder.append(_firstUpper_69, "");
            _builder.append(" - it\'s the parent");
            _builder.newLineIfNotEmpty();
            String _typeOrQObject_8 = this._cppExtensions.toTypeOrQObject(feature_15);
            _builder.append(_typeOrQObject_8, "");
            _builder.append(" ");
            String _name_152 = this._cppExtensions.toName(dto);
            _builder.append(_name_152, "");
            _builder.append("::");
            String _name_153 = this._cppExtensions.toName(feature_15);
            _builder.append(_name_153, "");
            _builder.append("() const");
            _builder.newLineIfNotEmpty();
            _builder.append("{");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("return qobject_cast<");
            String _typeOrQObject_9 = this._cppExtensions.toTypeOrQObject(feature_15);
            _builder.append(_typeOrQObject_9, "\t");
            _builder.append(">(parent());");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
          } else {
            String _typeOrQObject_10 = this._cppExtensions.toTypeOrQObject(feature_15);
            _builder.append(_typeOrQObject_10, "");
            _builder.append(" ");
            String _name_154 = this._cppExtensions.toName(dto);
            _builder.append(_name_154, "");
            _builder.append("::");
            String _name_155 = this._cppExtensions.toName(feature_15);
            _builder.append(_name_155, "");
            _builder.append("() const");
            _builder.newLineIfNotEmpty();
            _builder.append("{");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("return m");
            String _name_156 = this._cppExtensions.toName(feature_15);
            String _firstUpper_70 = StringExtensions.toFirstUpper(_name_156);
            _builder.append(_firstUpper_70, "\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
            _builder.append("void ");
            String _name_157 = this._cppExtensions.toName(dto);
            _builder.append(_name_157, "");
            _builder.append("::set");
            String _name_158 = this._cppExtensions.toName(feature_15);
            String _firstUpper_71 = StringExtensions.toFirstUpper(_name_158);
            _builder.append(_firstUpper_71, "");
            _builder.append("(");
            String _typeOrQObject_11 = this._cppExtensions.toTypeOrQObject(feature_15);
            _builder.append(_typeOrQObject_11, "");
            _builder.append(" ");
            String _name_159 = this._cppExtensions.toName(feature_15);
            _builder.append(_name_159, "");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
            _builder.append("{");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("if (");
            String _name_160 = this._cppExtensions.toName(feature_15);
            _builder.append(_name_160, "\t");
            _builder.append(" != m");
            String _name_161 = this._cppExtensions.toName(feature_15);
            String _firstUpper_72 = StringExtensions.toFirstUpper(_name_161);
            _builder.append(_firstUpper_72, "\t");
            _builder.append(") {");
            _builder.newLineIfNotEmpty();
            {
              boolean _isTypeOfDTO_8 = this._cppExtensions.isTypeOfDTO(feature_15);
              if (_isTypeOfDTO_8) {
                _builder.append("\t\t");
                _builder.append("if (m");
                String _name_162 = this._cppExtensions.toName(feature_15);
                String _firstUpper_73 = StringExtensions.toFirstUpper(_name_162);
                _builder.append(_firstUpper_73, "\t\t");
                _builder.append("){");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t");
                _builder.append("\t");
                _builder.append("m");
                String _name_163 = this._cppExtensions.toName(feature_15);
                String _firstUpper_74 = StringExtensions.toFirstUpper(_name_163);
                _builder.append(_firstUpper_74, "\t\t\t");
                _builder.append("->deleteLater();");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t");
                _builder.append("}");
                _builder.newLine();
                _builder.append("\t\t");
                _builder.append("m");
                String _name_164 = this._cppExtensions.toName(feature_15);
                String _firstUpper_75 = StringExtensions.toFirstUpper(_name_164);
                _builder.append(_firstUpper_75, "\t\t");
                _builder.append(" = ");
                String _name_165 = this._cppExtensions.toName(feature_15);
                _builder.append(_name_165, "\t\t");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t");
                _builder.append("m");
                String _name_166 = this._cppExtensions.toName(feature_15);
                String _firstUpper_76 = StringExtensions.toFirstUpper(_name_166);
                _builder.append(_firstUpper_76, "\t\t");
                _builder.append("->setParent(this);");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t\t");
                _builder.append("m");
                String _name_167 = this._cppExtensions.toName(feature_15);
                String _firstUpper_77 = StringExtensions.toFirstUpper(_name_167);
                _builder.append(_firstUpper_77, "\t\t");
                _builder.append(" = ");
                String _name_168 = this._cppExtensions.toName(feature_15);
                _builder.append(_name_168, "\t\t");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t\t");
            _builder.append("emit ");
            String _name_169 = this._cppExtensions.toName(feature_15);
            _builder.append(_name_169, "\t\t");
            _builder.append("Changed(");
            String _name_170 = this._cppExtensions.toName(feature_15);
            _builder.append(_name_170, "\t\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("}");
            _builder.newLine();
            {
              boolean _isTypeOfDTO_9 = this._cppExtensions.isTypeOfDTO(feature_15);
              if (_isTypeOfDTO_9) {
                _builder.append("void ");
                String _name_171 = this._cppExtensions.toName(dto);
                _builder.append(_name_171, "");
                _builder.append("::delete");
                String _name_172 = this._cppExtensions.toName(feature_15);
                String _firstUpper_78 = StringExtensions.toFirstUpper(_name_172);
                _builder.append(_firstUpper_78, "");
                _builder.append("()");
                _builder.newLineIfNotEmpty();
                _builder.append("{");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("if (m");
                String _name_173 = this._cppExtensions.toName(feature_15);
                String _firstUpper_79 = StringExtensions.toFirstUpper(_name_173);
                _builder.append(_firstUpper_79, "\t");
                _builder.append("){");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t");
                _builder.append("emit ");
                String _name_174 = this._cppExtensions.toName(feature_15);
                String _firstLower_17 = StringExtensions.toFirstLower(_name_174);
                _builder.append(_firstLower_17, "\t\t");
                _builder.append("Deleted(m");
                String _name_175 = this._cppExtensions.toName(feature_15);
                String _firstUpper_80 = StringExtensions.toFirstUpper(_name_175);
                _builder.append(_firstUpper_80, "\t\t");
                _builder.append("->uuid());");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t");
                _builder.append("m");
                String _name_176 = this._cppExtensions.toName(feature_15);
                String _firstUpper_81 = StringExtensions.toFirstUpper(_name_176);
                _builder.append(_firstUpper_81, "\t\t");
                _builder.append("->deleteLater();");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t");
                _builder.append("m");
                String _name_177 = this._cppExtensions.toName(feature_15);
                String _firstUpper_82 = StringExtensions.toFirstUpper(_name_177);
                _builder.append(_firstUpper_82, "\t\t");
                _builder.append(" = 0;");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("}");
                _builder.newLine();
                _builder.append("}");
                _builder.newLine();
                _builder.append("bool ");
                String _name_178 = this._cppExtensions.toName(dto);
                _builder.append(_name_178, "");
                _builder.append("::has");
                String _name_179 = this._cppExtensions.toName(feature_15);
                String _firstUpper_83 = StringExtensions.toFirstUpper(_name_179);
                _builder.append(_firstUpper_83, "");
                _builder.append("(){");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("if(m");
                String _name_180 = this._cppExtensions.toName(feature_15);
                String _firstUpper_84 = StringExtensions.toFirstUpper(_name_180);
                _builder.append(_firstUpper_84, "    ");
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
    }
    {
      List<? extends LFeature> _allFeatures_16 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_14 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          return Boolean.valueOf(CppGenerator.this._cppExtensions.isToMany(it));
        }
      };
      Iterable<? extends LFeature> _filter_14 = IterableExtensions.filter(_allFeatures_16, _function_14);
      for(final LFeature feature_16 : _filter_14) {
        CharSequence _foo_2 = this.foo(feature_16);
        _builder.append(_foo_2, "");
        _builder.append(" ");
        _builder.newLineIfNotEmpty();
        _builder.append("QVariantList ");
        String _name_181 = this._cppExtensions.toName(dto);
        _builder.append(_name_181, "");
        _builder.append("::");
        String _name_182 = this._cppExtensions.toName(feature_16);
        _builder.append(_name_182, "");
        _builder.append("AsQVariantList()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("QVariantList ");
        String _name_183 = this._cppExtensions.toName(feature_16);
        _builder.append(_name_183, "\t");
        _builder.append("List;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("for (int i = 0; i < m");
        String _name_184 = this._cppExtensions.toName(feature_16);
        String _firstUpper_85 = StringExtensions.toFirstUpper(_name_184);
        _builder.append(_firstUpper_85, "\t");
        _builder.append(".size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        String _name_185 = this._cppExtensions.toName(feature_16);
        _builder.append(_name_185, "        ");
        _builder.append("List.append(qobject_cast<");
        String _typeName_22 = this._cppExtensions.toTypeName(feature_16);
        _builder.append(_typeName_22, "        ");
        _builder.append("*>(m");
        String _name_186 = this._cppExtensions.toName(feature_16);
        String _firstUpper_86 = StringExtensions.toFirstUpper(_name_186);
        _builder.append(_firstUpper_86, "        ");
        _builder.append(".at(i))->toMap());");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return ");
        String _name_187 = this._cppExtensions.toName(feature_16);
        _builder.append(_name_187, "\t");
        _builder.append("List;");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_188 = this._cppExtensions.toName(dto);
        _builder.append(_name_188, "");
        _builder.append("::addTo");
        String _name_189 = this._cppExtensions.toName(feature_16);
        String _firstUpper_87 = StringExtensions.toFirstUpper(_name_189);
        _builder.append(_firstUpper_87, "");
        _builder.append("(");
        String _typeName_23 = this._cppExtensions.toTypeName(feature_16);
        _builder.append(_typeName_23, "");
        _builder.append("* ");
        String _typeName_24 = this._cppExtensions.toTypeName(feature_16);
        String _firstLower_18 = StringExtensions.toFirstLower(_typeName_24);
        _builder.append(_firstLower_18, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("m");
        String _name_190 = this._cppExtensions.toName(feature_16);
        String _firstUpper_88 = StringExtensions.toFirstUpper(_name_190);
        _builder.append(_firstUpper_88, "    ");
        _builder.append(".append(");
        String _typeName_25 = this._cppExtensions.toTypeName(feature_16);
        String _firstLower_19 = StringExtensions.toFirstLower(_typeName_25);
        _builder.append(_firstLower_19, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("void ");
        String _name_191 = this._cppExtensions.toName(dto);
        _builder.append(_name_191, "");
        _builder.append("::removeFrom");
        String _name_192 = this._cppExtensions.toName(feature_16);
        String _firstUpper_89 = StringExtensions.toFirstUpper(_name_192);
        _builder.append(_firstUpper_89, "");
        _builder.append("(");
        String _typeName_26 = this._cppExtensions.toTypeName(feature_16);
        _builder.append(_typeName_26, "");
        _builder.append("* ");
        String _typeName_27 = this._cppExtensions.toTypeName(feature_16);
        String _firstLower_20 = StringExtensions.toFirstLower(_typeName_27);
        _builder.append(_firstLower_20, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("for (int i = 0; i < m");
        String _name_193 = this._cppExtensions.toName(feature_16);
        String _firstUpper_90 = StringExtensions.toFirstUpper(_name_193);
        _builder.append(_firstUpper_90, "    ");
        _builder.append(".size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("if (m");
        String _name_194 = this._cppExtensions.toName(feature_16);
        String _firstUpper_91 = StringExtensions.toFirstUpper(_name_194);
        _builder.append(_firstUpper_91, "        ");
        _builder.append(".at(i) == ");
        String _typeName_28 = this._cppExtensions.toTypeName(feature_16);
        String _firstLower_21 = StringExtensions.toFirstLower(_typeName_28);
        _builder.append(_firstLower_21, "        ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("m");
        String _name_195 = this._cppExtensions.toName(feature_16);
        String _firstUpper_92 = StringExtensions.toFirstUpper(_name_195);
        _builder.append(_firstUpper_92, "            ");
        _builder.append(".removeAt(i);");
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
        String _typeName_29 = this._cppExtensions.toTypeName(feature_16);
        _builder.append(_typeName_29, "    ");
        _builder.append("* not found in ");
        String _name_196 = this._cppExtensions.toName(feature_16);
        String _firstLower_22 = StringExtensions.toFirstLower(_name_196);
        _builder.append(_firstLower_22, "    ");
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("// TODO signal error");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("void ");
        String _name_197 = this._cppExtensions.toName(dto);
        _builder.append(_name_197, "");
        _builder.append("::addTo");
        String _name_198 = this._cppExtensions.toName(feature_16);
        String _firstUpper_93 = StringExtensions.toFirstUpper(_name_198);
        _builder.append(_firstUpper_93, "");
        _builder.append("FromMap(const QVariantMap& ");
        String _typeName_30 = this._cppExtensions.toTypeName(feature_16);
        String _firstLower_23 = StringExtensions.toFirstLower(_typeName_30);
        _builder.append(_firstLower_23, "");
        _builder.append("Map)");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        String _typeName_31 = this._cppExtensions.toTypeName(feature_16);
        _builder.append(_typeName_31, "    ");
        _builder.append("* ");
        String _typeName_32 = this._cppExtensions.toTypeName(feature_16);
        String _firstLower_24 = StringExtensions.toFirstLower(_typeName_32);
        _builder.append(_firstLower_24, "    ");
        _builder.append(" = new ");
        String _typeName_33 = this._cppExtensions.toTypeName(feature_16);
        _builder.append(_typeName_33, "    ");
        _builder.append("();");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        String _typeName_34 = this._cppExtensions.toTypeName(feature_16);
        String _firstLower_25 = StringExtensions.toFirstLower(_typeName_34);
        _builder.append(_firstLower_25, "    ");
        _builder.append("->setParent(this);");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        String _typeName_35 = this._cppExtensions.toTypeName(feature_16);
        String _firstLower_26 = StringExtensions.toFirstLower(_typeName_35);
        _builder.append(_firstLower_26, "    ");
        _builder.append("->fillFromMap(");
        String _typeName_36 = this._cppExtensions.toTypeName(feature_16);
        String _firstLower_27 = StringExtensions.toFirstLower(_typeName_36);
        _builder.append(_firstLower_27, "    ");
        _builder.append("Map);");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("m");
        String _name_199 = this._cppExtensions.toName(feature_16);
        String _firstUpper_94 = StringExtensions.toFirstUpper(_name_199);
        _builder.append(_firstUpper_94, "    ");
        _builder.append(".append(");
        String _typeName_37 = this._cppExtensions.toTypeName(feature_16);
        String _firstLower_28 = StringExtensions.toFirstLower(_typeName_37);
        _builder.append(_firstLower_28, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("void ");
        String _name_200 = this._cppExtensions.toName(dto);
        _builder.append(_name_200, "");
        _builder.append("::removeFrom");
        String _name_201 = this._cppExtensions.toName(feature_16);
        String _firstUpper_95 = StringExtensions.toFirstUpper(_name_201);
        _builder.append(_firstUpper_95, "");
        _builder.append("ByKey(const QString& uuid)");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("for (int i = 0; i < m");
        String _name_202 = this._cppExtensions.toName(feature_16);
        String _firstUpper_96 = StringExtensions.toFirstUpper(_name_202);
        _builder.append(_firstUpper_96, "    ");
        _builder.append(".size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("if (qobject_cast<");
        String _typeName_38 = this._cppExtensions.toTypeName(feature_16);
        _builder.append(_typeName_38, "        ");
        _builder.append("*>(m");
        String _name_203 = this._cppExtensions.toName(feature_16);
        String _firstUpper_97 = StringExtensions.toFirstUpper(_name_203);
        _builder.append(_firstUpper_97, "        ");
        _builder.append(".at(i))->toMap().value(uuidKey).toString() == uuid) {");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("m");
        String _name_204 = this._cppExtensions.toName(feature_16);
        String _firstUpper_98 = StringExtensions.toFirstUpper(_name_204);
        _builder.append(_firstUpper_98, "            ");
        _builder.append(".removeAt(i);");
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
        _builder.append("qDebug() << \"uuid not found in ");
        String _name_205 = this._cppExtensions.toName(feature_16);
        String _firstLower_29 = StringExtensions.toFirstLower(_name_205);
        _builder.append(_firstLower_29, "    ");
        _builder.append(": \" << uuid;");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("// TODO signal error");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("int ");
        String _name_206 = this._cppExtensions.toName(dto);
        _builder.append(_name_206, "");
        _builder.append("::");
        String _name_207 = this._cppExtensions.toName(feature_16);
        String _firstLower_30 = StringExtensions.toFirstLower(_name_207);
        _builder.append(_firstLower_30, "");
        _builder.append("Count(){");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("return m");
        String _name_208 = this._cppExtensions.toName(feature_16);
        String _firstUpper_99 = StringExtensions.toFirstUpper(_name_208);
        _builder.append(_firstUpper_99, "    ");
        _builder.append(".size();");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("QList<QObject*> ");
        String _name_209 = this._cppExtensions.toName(dto);
        _builder.append(_name_209, "");
        _builder.append("::");
        String _name_210 = this._cppExtensions.toName(feature_16);
        _builder.append(_name_210, "");
        _builder.append("()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return m");
        String _name_211 = this._cppExtensions.toName(feature_16);
        String _firstUpper_100 = StringExtensions.toFirstUpper(_name_211);
        _builder.append(_firstUpper_100, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_212 = this._cppExtensions.toName(dto);
        _builder.append(_name_212, "");
        _builder.append("::set");
        String _name_213 = this._cppExtensions.toName(feature_16);
        String _firstUpper_101 = StringExtensions.toFirstUpper(_name_213);
        _builder.append(_firstUpper_101, "");
        _builder.append("(QList<QObject*> ");
        String _name_214 = this._cppExtensions.toName(feature_16);
        _builder.append(_name_214, "");
        _builder.append(") ");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (");
        String _name_215 = this._cppExtensions.toName(feature_16);
        _builder.append(_name_215, "\t");
        _builder.append(" != m");
        String _name_216 = this._cppExtensions.toName(feature_16);
        String _firstUpper_102 = StringExtensions.toFirstUpper(_name_216);
        _builder.append(_firstUpper_102, "\t");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("m");
        String _name_217 = this._cppExtensions.toName(feature_16);
        String _firstUpper_103 = StringExtensions.toFirstUpper(_name_217);
        _builder.append(_firstUpper_103, "\t\t");
        _builder.append(" = ");
        String _name_218 = this._cppExtensions.toName(feature_16);
        _builder.append(_name_218, "\t\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("emit ");
        String _name_219 = this._cppExtensions.toName(feature_16);
        _builder.append(_name_219, "\t\t");
        _builder.append("Changed(");
        String _name_220 = this._cppExtensions.toName(feature_16);
        _builder.append(_name_220, "\t\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    String _name_221 = this._cppExtensions.toName(dto);
    _builder.append(_name_221, "");
    _builder.append("::~");
    String _name_222 = this._cppExtensions.toName(dto);
    _builder.append(_name_222, "");
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
