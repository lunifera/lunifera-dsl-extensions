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
    String _name_10 = this._cppExtensions.toName(dto);
    _builder.append(_name_10, " ");
    _builder.append(" from QVariantMap");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("* Map got from JsonDataAccess or so");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("void ");
    String _name_11 = this._cppExtensions.toName(dto);
    _builder.append(_name_11, "");
    _builder.append("::fillFromMap(const QVariantMap& ");
    String _name_12 = this._cppExtensions.toName(dto);
    String _firstLower = StringExtensions.toFirstLower(_name_12);
    _builder.append(_firstLower, "");
    _builder.append("Map)");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("m");
    String _name_13 = this._cppExtensions.toName(dto);
    String _firstUpper_1 = StringExtensions.toFirstUpper(_name_13);
    _builder.append(_firstUpper_1, "\t");
    _builder.append("Map = ");
    String _name_14 = this._cppExtensions.toName(dto);
    String _firstLower_1 = StringExtensions.toFirstLower(_name_14);
    _builder.append(_firstLower_1, "\t");
    _builder.append("Map;");
    _builder.newLineIfNotEmpty();
    {
      List<? extends LFeature> _allFeatures_3 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_1 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _isToMany = CppGenerator.this._cppExtensions.isToMany(it);
          return Boolean.valueOf((!_isToMany));
        }
      };
      Iterable<? extends LFeature> _filter_1 = IterableExtensions.filter(_allFeatures_3, _function_1);
      for(final LFeature feature_3 : _filter_1) {
        {
          boolean _isTypeOfDTO_2 = this._cppExtensions.isTypeOfDTO(feature_3);
          if (_isTypeOfDTO_2) {
            {
              boolean _isContained_3 = this._cppExtensions.isContained(feature_3);
              if (_isContained_3) {
                _builder.append("\t");
                _builder.append("// m");
                String _name_15 = this._cppExtensions.toName(feature_3);
                String _firstUpper_2 = StringExtensions.toFirstUpper(_name_15);
                _builder.append(_firstUpper_2, "\t");
                _builder.append(" is parent (");
                String _typeName_1 = this._cppExtensions.toTypeName(feature_3);
                _builder.append(_typeName_1, "\t");
                _builder.append("* containing ");
                String _name_16 = this._cppExtensions.toName(dto);
                _builder.append(_name_16, "\t");
                _builder.append(")");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("// m");
                String _name_17 = this._cppExtensions.toName(feature_3);
                String _firstUpper_3 = StringExtensions.toFirstUpper(_name_17);
                _builder.append(_firstUpper_3, "\t");
                _builder.append(" points to ");
                String _typeName_2 = this._cppExtensions.toTypeName(feature_3);
                _builder.append(_typeName_2, "\t");
                _builder.append("*");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("if(m");
                String _name_18 = this._cppExtensions.toName(dto);
                String _firstUpper_4 = StringExtensions.toFirstUpper(_name_18);
                _builder.append(_firstUpper_4, "\t");
                _builder.append("Map.contains(");
                String _name_19 = this._cppExtensions.toName(feature_3);
                _builder.append(_name_19, "\t");
                _builder.append("Key)){");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("QVariantMap ");
                String _name_20 = this._cppExtensions.toName(feature_3);
                _builder.append(_name_20, "\t\t");
                _builder.append("Map;");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                String _name_21 = this._cppExtensions.toName(feature_3);
                _builder.append(_name_21, "\t\t");
                _builder.append("Map = m");
                String _name_22 = this._cppExtensions.toName(dto);
                String _firstUpper_5 = StringExtensions.toFirstUpper(_name_22);
                _builder.append(_firstUpper_5, "\t\t");
                _builder.append("Map.value(");
                String _name_23 = this._cppExtensions.toName(feature_3);
                _builder.append(_name_23, "\t\t");
                _builder.append("Key).toMap();");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("if(!");
                String _name_24 = this._cppExtensions.toName(feature_3);
                _builder.append(_name_24, "\t\t");
                _builder.append("Map.isEmpty()){");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t\t");
                _builder.append("m");
                String _name_25 = this._cppExtensions.toName(feature_3);
                String _firstUpper_6 = StringExtensions.toFirstUpper(_name_25);
                _builder.append(_firstUpper_6, "\t\t\t");
                _builder.append(" = new ");
                String _typeName_3 = this._cppExtensions.toTypeName(feature_3);
                _builder.append(_typeName_3, "\t\t\t");
                _builder.append("();");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t\t");
                _builder.append("m");
                String _name_26 = this._cppExtensions.toName(feature_3);
                String _firstUpper_7 = StringExtensions.toFirstUpper(_name_26);
                _builder.append(_firstUpper_7, "\t\t\t");
                _builder.append("->setParent(this);");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t\t");
                _builder.append("m");
                String _name_27 = this._cppExtensions.toName(feature_3);
                String _firstUpper_8 = StringExtensions.toFirstUpper(_name_27);
                _builder.append(_firstUpper_8, "\t\t\t");
                _builder.append("->fillFromMap(");
                String _name_28 = this._cppExtensions.toName(feature_3);
                _builder.append(_name_28, "\t\t\t");
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
          } else {
            {
              boolean _isTransient = this._cppExtensions.isTransient(feature_3);
              if (_isTransient) {
                _builder.append("\t");
                _builder.append("// m");
                String _name_29 = this._cppExtensions.toName(feature_3);
                String _firstUpper_9 = StringExtensions.toFirstUpper(_name_29);
                _builder.append(_firstUpper_9, "\t");
                _builder.append(" is transient - perhaps not included");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("if(m");
                String _name_30 = this._cppExtensions.toName(dto);
                String _firstUpper_10 = StringExtensions.toFirstUpper(_name_30);
                _builder.append(_firstUpper_10, "\t");
                _builder.append("Map.contains(");
                String _name_31 = this._cppExtensions.toName(feature_3);
                String _firstLower_2 = StringExtensions.toFirstLower(_name_31);
                _builder.append(_firstLower_2, "\t");
                _builder.append("Key)){");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("m");
                String _name_32 = this._cppExtensions.toName(feature_3);
                String _firstUpper_11 = StringExtensions.toFirstUpper(_name_32);
                _builder.append(_firstUpper_11, "\t\t");
                _builder.append(" = m");
                String _name_33 = this._cppExtensions.toName(dto);
                String _firstUpper_12 = StringExtensions.toFirstUpper(_name_33);
                _builder.append(_firstUpper_12, "\t\t");
                _builder.append("Map.value(");
                String _name_34 = this._cppExtensions.toName(feature_3);
                _builder.append(_name_34, "\t\t");
                _builder.append("Key).to");
                String _mapToType = this._cppExtensions.mapToType(feature_3);
                _builder.append(_mapToType, "\t\t");
                _builder.append("();");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("}");
                _builder.newLine();
              } else {
                _builder.append("\t");
                _builder.append("m");
                String _name_35 = this._cppExtensions.toName(feature_3);
                String _firstUpper_13 = StringExtensions.toFirstUpper(_name_35);
                _builder.append(_firstUpper_13, "\t");
                _builder.append(" = m");
                String _name_36 = this._cppExtensions.toName(dto);
                String _firstUpper_14 = StringExtensions.toFirstUpper(_name_36);
                _builder.append(_firstUpper_14, "\t");
                _builder.append("Map.value(");
                String _name_37 = this._cppExtensions.toName(feature_3);
                _builder.append(_name_37, "\t");
                _builder.append("Key).to");
                String _mapToType_1 = this._cppExtensions.mapToType(feature_3);
                _builder.append(_mapToType_1, "\t");
                _builder.append("();");
                _builder.newLineIfNotEmpty();
              }
            }
            {
              String _name_38 = this._cppExtensions.toName(feature_3);
              boolean _equals = Objects.equal(_name_38, "uuid");
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
      List<? extends LFeature> _allFeatures_4 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_2 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          return Boolean.valueOf(CppGenerator.this._cppExtensions.isToMany(it));
        }
      };
      Iterable<? extends LFeature> _filter_2 = IterableExtensions.filter(_allFeatures_4, _function_2);
      for(final LFeature feature_4 : _filter_2) {
        _builder.append("\t");
        _builder.append("// m");
        String _name_39 = this._cppExtensions.toName(feature_4);
        String _firstUpper_15 = StringExtensions.toFirstUpper(_name_39);
        _builder.append(_firstUpper_15, "\t");
        _builder.append(" is List of ");
        String _typeName_4 = this._cppExtensions.toTypeName(feature_4);
        _builder.append(_typeName_4, "\t");
        _builder.append("*");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("QVariantList ");
        String _name_40 = this._cppExtensions.toName(feature_4);
        String _firstLower_3 = StringExtensions.toFirstLower(_name_40);
        _builder.append(_firstLower_3, "\t");
        _builder.append("List;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        String _name_41 = this._cppExtensions.toName(feature_4);
        String _firstLower_4 = StringExtensions.toFirstLower(_name_41);
        _builder.append(_firstLower_4, "\t");
        _builder.append("List = m");
        String _name_42 = this._cppExtensions.toName(dto);
        String _firstUpper_16 = StringExtensions.toFirstUpper(_name_42);
        _builder.append(_firstUpper_16, "\t");
        _builder.append("Map.value(");
        String _name_43 = this._cppExtensions.toName(feature_4);
        String _firstLower_5 = StringExtensions.toFirstLower(_name_43);
        _builder.append(_firstLower_5, "\t");
        _builder.append("Key).toList();");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("m");
        String _name_44 = this._cppExtensions.toName(feature_4);
        String _firstUpper_17 = StringExtensions.toFirstUpper(_name_44);
        _builder.append(_firstUpper_17, "\t");
        _builder.append(".clear();");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("for (int i = 0; i < ");
        String _name_45 = this._cppExtensions.toName(feature_4);
        String _firstLower_6 = StringExtensions.toFirstLower(_name_45);
        _builder.append(_firstLower_6, "\t");
        _builder.append("List.size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("QVariantMap ");
        String _name_46 = this._cppExtensions.toName(feature_4);
        String _firstLower_7 = StringExtensions.toFirstLower(_name_46);
        _builder.append(_firstLower_7, "\t\t");
        _builder.append("Map;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        String _typeName_5 = this._cppExtensions.toTypeName(feature_4);
        _builder.append(_typeName_5, "\t\t");
        _builder.append("* ");
        String _typeName_6 = this._cppExtensions.toTypeName(feature_4);
        String _firstLower_8 = StringExtensions.toFirstLower(_typeName_6);
        _builder.append(_firstLower_8, "\t\t");
        _builder.append(" = new ");
        String _typeName_7 = this._cppExtensions.toTypeName(feature_4);
        _builder.append(_typeName_7, "\t\t");
        _builder.append("();");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        String _typeName_8 = this._cppExtensions.toTypeName(feature_4);
        String _firstLower_9 = StringExtensions.toFirstLower(_typeName_8);
        _builder.append(_firstLower_9, "\t\t");
        _builder.append("->setParent(this);");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        String _typeName_9 = this._cppExtensions.toTypeName(feature_4);
        String _firstLower_10 = StringExtensions.toFirstLower(_typeName_9);
        _builder.append(_firstLower_10, "\t\t");
        _builder.append("->fillFromMap(");
        String _name_47 = this._cppExtensions.toName(feature_4);
        String _firstLower_11 = StringExtensions.toFirstLower(_name_47);
        _builder.append(_firstLower_11, "\t\t");
        _builder.append("Map);");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("m");
        String _name_48 = this._cppExtensions.toName(feature_4);
        String _firstUpper_18 = StringExtensions.toFirstUpper(_name_48);
        _builder.append(_firstUpper_18, "\t\t");
        _builder.append(".append(");
        String _typeName_10 = this._cppExtensions.toTypeName(feature_4);
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
    String _name_49 = this._cppExtensions.toName(dto);
    _builder.append(_name_49, "");
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
    _builder.append("* Checks if all mandatory attributes are filled");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("bool ");
    String _name_50 = this._cppExtensions.toName(dto);
    _builder.append(_name_50, "");
    _builder.append("::isValid()");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_5 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_3 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          return Boolean.valueOf(CppGenerator.this._cppExtensions.isMandatory(it));
        }
      };
      Iterable<? extends LFeature> _filter_3 = IterableExtensions.filter(_allFeatures_5, _function_3);
      for(final LFeature feature_5 : _filter_3) {
        {
          boolean _isToMany = this._cppExtensions.isToMany(feature_5);
          if (_isToMany) {
            _builder.append("\t");
            _builder.append("if(m");
            String _name_51 = this._cppExtensions.toName(feature_5);
            String _firstUpper_19 = StringExtensions.toFirstUpper(_name_51);
            _builder.append(_firstUpper_19, "\t");
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
            boolean _isTypeOfDTO_3 = this._cppExtensions.isTypeOfDTO(feature_5);
            if (_isTypeOfDTO_3) {
              _builder.append("\t");
              _builder.append("if(!m");
              String _name_52 = this._cppExtensions.toName(feature_5);
              String _firstUpper_20 = StringExtensions.toFirstUpper(_name_52);
              _builder.append(_firstUpper_20, "\t");
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
              String _validate = this._cppExtensions.toValidate(feature_5);
              _builder.append(_validate, "\t");
              _builder.newLineIfNotEmpty();
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
    String _name_53 = this._cppExtensions.toName(dto);
    _builder.append(_name_53, " ");
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
    String _name_54 = this._cppExtensions.toName(dto);
    _builder.append(_name_54, "");
    _builder.append("::toMap()");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_6 = dto.getAllFeatures();
      for(final LFeature feature_6 : _allFeatures_6) {
        {
          boolean _isTypeOfDTO_4 = this._cppExtensions.isTypeOfDTO(feature_6);
          if (_isTypeOfDTO_4) {
            {
              boolean _isContained_4 = this._cppExtensions.isContained(feature_6);
              boolean _not = (!_isContained_4);
              if (_not) {
                _builder.append("\t");
                _builder.append("// m");
                String _name_55 = this._cppExtensions.toName(feature_6);
                String _firstUpper_21 = StringExtensions.toFirstUpper(_name_55);
                _builder.append(_firstUpper_21, "\t");
                _builder.append(" points to ");
                String _typeName_11 = this._cppExtensions.toTypeName(feature_6);
                _builder.append(_typeName_11, "\t");
                _builder.append("*");
                _builder.newLineIfNotEmpty();
                {
                  boolean _isToMany_1 = this._cppExtensions.isToMany(feature_6);
                  if (_isToMany_1) {
                    _builder.append("\t");
                    _builder.append("m");
                    String _name_56 = this._cppExtensions.toName(dto);
                    String _firstUpper_22 = StringExtensions.toFirstUpper(_name_56);
                    _builder.append(_firstUpper_22, "\t");
                    _builder.append("Map.insert(");
                    String _name_57 = this._cppExtensions.toName(feature_6);
                    _builder.append(_name_57, "\t");
                    _builder.append("Key, ");
                    String _name_58 = this._cppExtensions.toName(feature_6);
                    _builder.append(_name_58, "\t");
                    _builder.append("AsQVariantList());");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t");
                    _builder.append("if(m");
                    String _name_59 = this._cppExtensions.toName(feature_6);
                    String _firstUpper_23 = StringExtensions.toFirstUpper(_name_59);
                    _builder.append(_firstUpper_23, "\t");
                    _builder.append("){");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("m");
                    String _name_60 = this._cppExtensions.toName(dto);
                    String _firstUpper_24 = StringExtensions.toFirstUpper(_name_60);
                    _builder.append(_firstUpper_24, "\t\t");
                    _builder.append("Map.insert(");
                    String _name_61 = this._cppExtensions.toName(feature_6);
                    _builder.append(_name_61, "\t\t");
                    _builder.append("Key, m");
                    String _name_62 = this._cppExtensions.toName(feature_6);
                    String _firstUpper_25 = StringExtensions.toFirstUpper(_name_62);
                    _builder.append(_firstUpper_25, "\t\t");
                    _builder.append("->to");
                    String _mapOrList = this._cppExtensions.toMapOrList(feature_6);
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
                String _name_63 = this._cppExtensions.toName(feature_6);
                String _firstUpper_26 = StringExtensions.toFirstUpper(_name_63);
                _builder.append(_firstUpper_26, "\t");
                _builder.append(" points to ");
                String _typeName_12 = this._cppExtensions.toTypeName(feature_6);
                _builder.append(_typeName_12, "\t");
                _builder.append("* containing ");
                String _name_64 = this._cppExtensions.toName(dto);
                _builder.append(_name_64, "\t");
                _builder.newLineIfNotEmpty();
              }
            }
          } else {
            _builder.append("\t");
            _builder.append("m");
            String _name_65 = this._cppExtensions.toName(dto);
            String _firstUpper_27 = StringExtensions.toFirstUpper(_name_65);
            _builder.append(_firstUpper_27, "\t");
            _builder.append("Map.insert(");
            String _name_66 = this._cppExtensions.toName(feature_6);
            _builder.append(_name_66, "\t");
            _builder.append("Key, m");
            String _name_67 = this._cppExtensions.toName(feature_6);
            String _firstUpper_28 = StringExtensions.toFirstUpper(_name_67);
            _builder.append(_firstUpper_28, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("return m");
    String _name_68 = this._cppExtensions.toName(dto);
    String _firstUpper_29 = StringExtensions.toFirstUpper(_name_68);
    _builder.append(_firstUpper_29, "\t");
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
        String _name_69 = this._cppExtensions.toName(dto);
        _builder.append(_name_69, " ");
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
        String _name_70 = this._cppExtensions.toName(dto);
        _builder.append(_name_70, "");
        _builder.append("::toForeignMap()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("QVariantMap foreignMap;");
        _builder.newLine();
        {
          List<? extends LFeature> _allFeatures_7 = dto.getAllFeatures();
          for(final LFeature feature_7 : _allFeatures_7) {
            {
              boolean _isTypeOfDTO_5 = this._cppExtensions.isTypeOfDTO(feature_7);
              if (_isTypeOfDTO_5) {
                {
                  boolean _isContained_5 = this._cppExtensions.isContained(feature_7);
                  boolean _not_1 = (!_isContained_5);
                  if (_not_1) {
                    _builder.append("\t");
                    _builder.append("// m");
                    String _name_71 = this._cppExtensions.toName(feature_7);
                    String _firstUpper_30 = StringExtensions.toFirstUpper(_name_71);
                    _builder.append(_firstUpper_30, "\t");
                    _builder.append(" points to ");
                    String _typeName_13 = this._cppExtensions.toTypeName(feature_7);
                    _builder.append(_typeName_13, "\t");
                    _builder.append("*");
                    _builder.newLineIfNotEmpty();
                    {
                      boolean _isToMany_2 = this._cppExtensions.isToMany(feature_7);
                      if (_isToMany_2) {
                        _builder.append("\t");
                        _builder.append("foreignMap.insert(");
                        String _name_72 = this._cppExtensions.toName(feature_7);
                        _builder.append(_name_72, "\t");
                        _builder.append("ForeignKey, ");
                        String _name_73 = this._cppExtensions.toName(feature_7);
                        _builder.append(_name_73, "\t");
                        _builder.append("AsQVariantList());");
                        _builder.newLineIfNotEmpty();
                      } else {
                        _builder.append("\t");
                        _builder.append("if(m");
                        String _name_74 = this._cppExtensions.toName(feature_7);
                        String _firstUpper_31 = StringExtensions.toFirstUpper(_name_74);
                        _builder.append(_firstUpper_31, "\t");
                        _builder.append("){");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("foreignMap.insert(");
                        String _name_75 = this._cppExtensions.toName(feature_7);
                        _builder.append(_name_75, "\t\t");
                        _builder.append("ForeignKey, m");
                        String _name_76 = this._cppExtensions.toName(feature_7);
                        String _firstUpper_32 = StringExtensions.toFirstUpper(_name_76);
                        _builder.append(_firstUpper_32, "\t\t");
                        _builder.append("->to");
                        String _mapOrList_1 = this._cppExtensions.toMapOrList(feature_7);
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
                    String _name_77 = this._cppExtensions.toName(feature_7);
                    String _firstUpper_33 = StringExtensions.toFirstUpper(_name_77);
                    _builder.append(_firstUpper_33, "\t");
                    _builder.append(" points to ");
                    String _typeName_14 = this._cppExtensions.toTypeName(feature_7);
                    _builder.append(_typeName_14, "\t");
                    _builder.append("* containing ");
                    String _name_78 = this._cppExtensions.toName(dto);
                    _builder.append(_name_78, "\t");
                    _builder.newLineIfNotEmpty();
                  }
                }
              } else {
                _builder.append("\t");
                _builder.append("foreignMap.insert(");
                String _name_79 = this._cppExtensions.toName(feature_7);
                _builder.append(_name_79, "\t");
                _builder.append("ForeignKey, m");
                String _name_80 = this._cppExtensions.toName(feature_7);
                String _firstUpper_34 = StringExtensions.toFirstUpper(_name_80);
                _builder.append(_firstUpper_34, "\t");
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
    String _name_81 = this._cppExtensions.toName(dto);
    _builder.append(_name_81, " ");
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
    String _name_82 = this._cppExtensions.toName(dto);
    _builder.append(_name_82, "");
    _builder.append("::dataToPersist()");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_8 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_4 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _isTransient = CppGenerator.this._cppExtensions.isTransient(it);
          return Boolean.valueOf((!_isTransient));
        }
      };
      Iterable<? extends LFeature> _filter_4 = IterableExtensions.filter(_allFeatures_8, _function_4);
      for(final LFeature feature_8 : _filter_4) {
        {
          boolean _isTypeOfDTO_6 = this._cppExtensions.isTypeOfDTO(feature_8);
          if (_isTypeOfDTO_6) {
            {
              boolean _isContained_6 = this._cppExtensions.isContained(feature_8);
              boolean _not_2 = (!_isContained_6);
              if (_not_2) {
                _builder.append("\t");
                _builder.append("// m");
                String _name_83 = this._cppExtensions.toName(feature_8);
                String _firstUpper_35 = StringExtensions.toFirstUpper(_name_83);
                _builder.append(_firstUpper_35, "\t");
                _builder.append(" points to ");
                String _typeName_15 = this._cppExtensions.toTypeName(feature_8);
                _builder.append(_typeName_15, "\t");
                _builder.append("*");
                _builder.newLineIfNotEmpty();
                {
                  boolean _isToMany_3 = this._cppExtensions.isToMany(feature_8);
                  if (_isToMany_3) {
                    _builder.append("\t");
                    _builder.append("m");
                    String _name_84 = this._cppExtensions.toName(dto);
                    String _firstUpper_36 = StringExtensions.toFirstUpper(_name_84);
                    _builder.append(_firstUpper_36, "\t");
                    _builder.append("Map.insert(");
                    String _name_85 = this._cppExtensions.toName(feature_8);
                    _builder.append(_name_85, "\t");
                    _builder.append("Key, ");
                    String _name_86 = this._cppExtensions.toName(feature_8);
                    _builder.append(_name_86, "\t");
                    _builder.append("AsQVariantList());");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t");
                    _builder.append("if(m");
                    String _name_87 = this._cppExtensions.toName(feature_8);
                    String _firstUpper_37 = StringExtensions.toFirstUpper(_name_87);
                    _builder.append(_firstUpper_37, "\t");
                    _builder.append("){");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("m");
                    String _name_88 = this._cppExtensions.toName(dto);
                    String _firstUpper_38 = StringExtensions.toFirstUpper(_name_88);
                    _builder.append(_firstUpper_38, "\t\t");
                    _builder.append("Map.insert(");
                    String _name_89 = this._cppExtensions.toName(feature_8);
                    _builder.append(_name_89, "\t\t");
                    _builder.append("Key, m");
                    String _name_90 = this._cppExtensions.toName(feature_8);
                    String _firstUpper_39 = StringExtensions.toFirstUpper(_name_90);
                    _builder.append(_firstUpper_39, "\t\t");
                    _builder.append("->to");
                    String _mapOrList_2 = this._cppExtensions.toMapOrList(feature_8);
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
                String _name_91 = this._cppExtensions.toName(feature_8);
                String _firstUpper_40 = StringExtensions.toFirstUpper(_name_91);
                _builder.append(_firstUpper_40, "\t");
                _builder.append(" points to ");
                String _typeName_16 = this._cppExtensions.toTypeName(feature_8);
                _builder.append(_typeName_16, "\t");
                _builder.append("* containing ");
                String _name_92 = this._cppExtensions.toName(dto);
                _builder.append(_name_92, "\t");
                _builder.newLineIfNotEmpty();
              }
            }
          } else {
            _builder.append("\t");
            _builder.append("m");
            String _name_93 = this._cppExtensions.toName(dto);
            String _firstUpper_41 = StringExtensions.toFirstUpper(_name_93);
            _builder.append(_firstUpper_41, "\t");
            _builder.append("Map.insert(");
            String _name_94 = this._cppExtensions.toName(feature_8);
            _builder.append(_name_94, "\t");
            _builder.append("Key, m");
            String _name_95 = this._cppExtensions.toName(feature_8);
            String _firstUpper_42 = StringExtensions.toFirstUpper(_name_95);
            _builder.append(_firstUpper_42, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      List<? extends LFeature> _allFeatures_9 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_5 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          return Boolean.valueOf(CppGenerator.this._cppExtensions.isTransient(it));
        }
      };
      Iterable<? extends LFeature> _filter_5 = IterableExtensions.filter(_allFeatures_9, _function_5);
      for(final LFeature feature_9 : _filter_5) {
        _builder.append("\t");
        _builder.append("// excluded: m");
        String _name_96 = this._cppExtensions.toName(feature_9);
        String _firstUpper_43 = StringExtensions.toFirstUpper(_name_96);
        _builder.append(_firstUpper_43, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("return m");
    String _name_97 = this._cppExtensions.toName(dto);
    String _firstUpper_44 = StringExtensions.toFirstUpper(_name_97);
    _builder.append(_firstUpper_44, "\t");
    _builder.append("Map;");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_10 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_6 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _isToMany = CppGenerator.this._cppExtensions.isToMany(it);
          return Boolean.valueOf((!_isToMany));
        }
      };
      Iterable<? extends LFeature> _filter_6 = IterableExtensions.filter(_allFeatures_10, _function_6);
      for(final LFeature feature_10 : _filter_6) {
        CharSequence _foo = this.foo(feature_10);
        _builder.append(_foo, "");
        _builder.newLineIfNotEmpty();
        {
          boolean _and_2 = false;
          boolean _isTypeOfDTO_7 = this._cppExtensions.isTypeOfDTO(feature_10);
          if (!_isTypeOfDTO_7) {
            _and_2 = false;
          } else {
            boolean _isContained_7 = this._cppExtensions.isContained(feature_10);
            _and_2 = _isContained_7;
          }
          if (_and_2) {
            _builder.append("// No SETTER for ");
            String _name_98 = this._cppExtensions.toName(feature_10);
            String _firstUpper_45 = StringExtensions.toFirstUpper(_name_98);
            _builder.append(_firstUpper_45, "");
            _builder.append(" - it\'s the parent");
            _builder.newLineIfNotEmpty();
            String _typeOrQObject = this._cppExtensions.toTypeOrQObject(feature_10);
            _builder.append(_typeOrQObject, "");
            _builder.append(" ");
            String _name_99 = this._cppExtensions.toName(dto);
            _builder.append(_name_99, "");
            _builder.append("::");
            String _name_100 = this._cppExtensions.toName(feature_10);
            _builder.append(_name_100, "");
            _builder.append("() const");
            _builder.newLineIfNotEmpty();
            _builder.append("{");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("return qobject_cast<");
            String _typeOrQObject_1 = this._cppExtensions.toTypeOrQObject(feature_10);
            _builder.append(_typeOrQObject_1, "\t");
            _builder.append(">(parent());");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
          } else {
            String _typeOrQObject_2 = this._cppExtensions.toTypeOrQObject(feature_10);
            _builder.append(_typeOrQObject_2, "");
            _builder.append(" ");
            String _name_101 = this._cppExtensions.toName(dto);
            _builder.append(_name_101, "");
            _builder.append("::");
            String _name_102 = this._cppExtensions.toName(feature_10);
            _builder.append(_name_102, "");
            _builder.append("() const");
            _builder.newLineIfNotEmpty();
            _builder.append("{");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("return m");
            String _name_103 = this._cppExtensions.toName(feature_10);
            String _firstUpper_46 = StringExtensions.toFirstUpper(_name_103);
            _builder.append(_firstUpper_46, "\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
            _builder.append("void ");
            String _name_104 = this._cppExtensions.toName(dto);
            _builder.append(_name_104, "");
            _builder.append("::set");
            String _name_105 = this._cppExtensions.toName(feature_10);
            String _firstUpper_47 = StringExtensions.toFirstUpper(_name_105);
            _builder.append(_firstUpper_47, "");
            _builder.append("(");
            String _typeOrQObject_3 = this._cppExtensions.toTypeOrQObject(feature_10);
            _builder.append(_typeOrQObject_3, "");
            _builder.append(" ");
            String _name_106 = this._cppExtensions.toName(feature_10);
            _builder.append(_name_106, "");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
            _builder.append("{");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("if (");
            String _name_107 = this._cppExtensions.toName(feature_10);
            _builder.append(_name_107, "\t");
            _builder.append(" != m");
            String _name_108 = this._cppExtensions.toName(feature_10);
            String _firstUpper_48 = StringExtensions.toFirstUpper(_name_108);
            _builder.append(_firstUpper_48, "\t");
            _builder.append(") {");
            _builder.newLineIfNotEmpty();
            {
              boolean _isTypeOfDTO_8 = this._cppExtensions.isTypeOfDTO(feature_10);
              if (_isTypeOfDTO_8) {
                _builder.append("\t\t");
                _builder.append("if (m");
                String _name_109 = this._cppExtensions.toName(feature_10);
                String _firstUpper_49 = StringExtensions.toFirstUpper(_name_109);
                _builder.append(_firstUpper_49, "\t\t");
                _builder.append("){");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t");
                _builder.append("\t");
                _builder.append("m");
                String _name_110 = this._cppExtensions.toName(feature_10);
                String _firstUpper_50 = StringExtensions.toFirstUpper(_name_110);
                _builder.append(_firstUpper_50, "\t\t\t");
                _builder.append("->deleteLater();");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t");
                _builder.append("}");
                _builder.newLine();
                _builder.append("\t\t");
                _builder.append("m");
                String _name_111 = this._cppExtensions.toName(feature_10);
                String _firstUpper_51 = StringExtensions.toFirstUpper(_name_111);
                _builder.append(_firstUpper_51, "\t\t");
                _builder.append(" = ");
                String _name_112 = this._cppExtensions.toName(feature_10);
                _builder.append(_name_112, "\t\t");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t");
                _builder.append("m");
                String _name_113 = this._cppExtensions.toName(feature_10);
                String _firstUpper_52 = StringExtensions.toFirstUpper(_name_113);
                _builder.append(_firstUpper_52, "\t\t");
                _builder.append("->setParent(this);");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t\t");
                _builder.append("m");
                String _name_114 = this._cppExtensions.toName(feature_10);
                String _firstUpper_53 = StringExtensions.toFirstUpper(_name_114);
                _builder.append(_firstUpper_53, "\t\t");
                _builder.append(" = ");
                String _name_115 = this._cppExtensions.toName(feature_10);
                _builder.append(_name_115, "\t\t");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t\t");
            _builder.append("emit ");
            String _name_116 = this._cppExtensions.toName(feature_10);
            _builder.append(_name_116, "\t\t");
            _builder.append("Changed(");
            String _name_117 = this._cppExtensions.toName(feature_10);
            _builder.append(_name_117, "\t\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("}");
            _builder.newLine();
            {
              boolean _isTypeOfDTO_9 = this._cppExtensions.isTypeOfDTO(feature_10);
              if (_isTypeOfDTO_9) {
                _builder.append("void ");
                String _name_118 = this._cppExtensions.toName(dto);
                _builder.append(_name_118, "");
                _builder.append("::delete");
                String _name_119 = this._cppExtensions.toName(feature_10);
                String _firstUpper_54 = StringExtensions.toFirstUpper(_name_119);
                _builder.append(_firstUpper_54, "");
                _builder.append("()");
                _builder.newLineIfNotEmpty();
                _builder.append("{");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("if (m");
                String _name_120 = this._cppExtensions.toName(feature_10);
                String _firstUpper_55 = StringExtensions.toFirstUpper(_name_120);
                _builder.append(_firstUpper_55, "\t");
                _builder.append("){");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t");
                _builder.append("emit ");
                String _name_121 = this._cppExtensions.toName(feature_10);
                String _firstLower_13 = StringExtensions.toFirstLower(_name_121);
                _builder.append(_firstLower_13, "\t\t");
                _builder.append("Deleted(m");
                String _name_122 = this._cppExtensions.toName(feature_10);
                String _firstUpper_56 = StringExtensions.toFirstUpper(_name_122);
                _builder.append(_firstUpper_56, "\t\t");
                _builder.append("->uuid());");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t");
                _builder.append("m");
                String _name_123 = this._cppExtensions.toName(feature_10);
                String _firstUpper_57 = StringExtensions.toFirstUpper(_name_123);
                _builder.append(_firstUpper_57, "\t\t");
                _builder.append("->deleteLater();");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t");
                _builder.append("m");
                String _name_124 = this._cppExtensions.toName(feature_10);
                String _firstUpper_58 = StringExtensions.toFirstUpper(_name_124);
                _builder.append(_firstUpper_58, "\t\t");
                _builder.append(" = 0;");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("}");
                _builder.newLine();
                _builder.append("}");
                _builder.newLine();
                _builder.append("bool ");
                String _name_125 = this._cppExtensions.toName(dto);
                _builder.append(_name_125, "");
                _builder.append("::has");
                String _name_126 = this._cppExtensions.toName(feature_10);
                String _firstUpper_59 = StringExtensions.toFirstUpper(_name_126);
                _builder.append(_firstUpper_59, "");
                _builder.append("(){");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("if(m");
                String _name_127 = this._cppExtensions.toName(feature_10);
                String _firstUpper_60 = StringExtensions.toFirstUpper(_name_127);
                _builder.append(_firstUpper_60, "    ");
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
      List<? extends LFeature> _allFeatures_11 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_7 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          return Boolean.valueOf(CppGenerator.this._cppExtensions.isToMany(it));
        }
      };
      Iterable<? extends LFeature> _filter_7 = IterableExtensions.filter(_allFeatures_11, _function_7);
      for(final LFeature feature_11 : _filter_7) {
        CharSequence _foo_1 = this.foo(feature_11);
        _builder.append(_foo_1, "");
        _builder.append(" ");
        _builder.newLineIfNotEmpty();
        _builder.append("QVariantList ");
        String _name_128 = this._cppExtensions.toName(dto);
        _builder.append(_name_128, "");
        _builder.append("::");
        String _name_129 = this._cppExtensions.toName(feature_11);
        _builder.append(_name_129, "");
        _builder.append("AsQVariantList()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("QVariantList ");
        String _name_130 = this._cppExtensions.toName(feature_11);
        _builder.append(_name_130, "\t");
        _builder.append("List;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("for (int i = 0; i < m");
        String _name_131 = this._cppExtensions.toName(feature_11);
        String _firstUpper_61 = StringExtensions.toFirstUpper(_name_131);
        _builder.append(_firstUpper_61, "\t");
        _builder.append(".size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        String _name_132 = this._cppExtensions.toName(feature_11);
        _builder.append(_name_132, "        ");
        _builder.append("List.append(qobject_cast<");
        String _typeName_17 = this._cppExtensions.toTypeName(feature_11);
        _builder.append(_typeName_17, "        ");
        _builder.append("*>(m");
        String _name_133 = this._cppExtensions.toName(feature_11);
        String _firstUpper_62 = StringExtensions.toFirstUpper(_name_133);
        _builder.append(_firstUpper_62, "        ");
        _builder.append(".at(i))->toMap());");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return ");
        String _name_134 = this._cppExtensions.toName(feature_11);
        _builder.append(_name_134, "\t");
        _builder.append("List;");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_135 = this._cppExtensions.toName(dto);
        _builder.append(_name_135, "");
        _builder.append("::addTo");
        String _name_136 = this._cppExtensions.toName(feature_11);
        String _firstUpper_63 = StringExtensions.toFirstUpper(_name_136);
        _builder.append(_firstUpper_63, "");
        _builder.append("(");
        String _typeName_18 = this._cppExtensions.toTypeName(feature_11);
        _builder.append(_typeName_18, "");
        _builder.append("* ");
        String _typeName_19 = this._cppExtensions.toTypeName(feature_11);
        String _firstLower_14 = StringExtensions.toFirstLower(_typeName_19);
        _builder.append(_firstLower_14, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("m");
        String _name_137 = this._cppExtensions.toName(feature_11);
        String _firstUpper_64 = StringExtensions.toFirstUpper(_name_137);
        _builder.append(_firstUpper_64, "    ");
        _builder.append(".append(");
        String _typeName_20 = this._cppExtensions.toTypeName(feature_11);
        String _firstLower_15 = StringExtensions.toFirstLower(_typeName_20);
        _builder.append(_firstLower_15, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("void ");
        String _name_138 = this._cppExtensions.toName(dto);
        _builder.append(_name_138, "");
        _builder.append("::removeFrom");
        String _name_139 = this._cppExtensions.toName(feature_11);
        String _firstUpper_65 = StringExtensions.toFirstUpper(_name_139);
        _builder.append(_firstUpper_65, "");
        _builder.append("(");
        String _typeName_21 = this._cppExtensions.toTypeName(feature_11);
        _builder.append(_typeName_21, "");
        _builder.append("* ");
        String _typeName_22 = this._cppExtensions.toTypeName(feature_11);
        String _firstLower_16 = StringExtensions.toFirstLower(_typeName_22);
        _builder.append(_firstLower_16, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("for (int i = 0; i < m");
        String _name_140 = this._cppExtensions.toName(feature_11);
        String _firstUpper_66 = StringExtensions.toFirstUpper(_name_140);
        _builder.append(_firstUpper_66, "    ");
        _builder.append(".size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("if (m");
        String _name_141 = this._cppExtensions.toName(feature_11);
        String _firstUpper_67 = StringExtensions.toFirstUpper(_name_141);
        _builder.append(_firstUpper_67, "        ");
        _builder.append(".at(i) == ");
        String _typeName_23 = this._cppExtensions.toTypeName(feature_11);
        String _firstLower_17 = StringExtensions.toFirstLower(_typeName_23);
        _builder.append(_firstLower_17, "        ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("m");
        String _name_142 = this._cppExtensions.toName(feature_11);
        String _firstUpper_68 = StringExtensions.toFirstUpper(_name_142);
        _builder.append(_firstUpper_68, "            ");
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
        String _typeName_24 = this._cppExtensions.toTypeName(feature_11);
        _builder.append(_typeName_24, "    ");
        _builder.append("* not found in ");
        String _name_143 = this._cppExtensions.toName(feature_11);
        String _firstLower_18 = StringExtensions.toFirstLower(_name_143);
        _builder.append(_firstLower_18, "    ");
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("// TODO signal error");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("void ");
        String _name_144 = this._cppExtensions.toName(dto);
        _builder.append(_name_144, "");
        _builder.append("::addTo");
        String _name_145 = this._cppExtensions.toName(feature_11);
        String _firstUpper_69 = StringExtensions.toFirstUpper(_name_145);
        _builder.append(_firstUpper_69, "");
        _builder.append("FromMap(const QVariantMap& ");
        String _typeName_25 = this._cppExtensions.toTypeName(feature_11);
        String _firstLower_19 = StringExtensions.toFirstLower(_typeName_25);
        _builder.append(_firstLower_19, "");
        _builder.append("Map)");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        String _typeName_26 = this._cppExtensions.toTypeName(feature_11);
        _builder.append(_typeName_26, "    ");
        _builder.append("* ");
        String _typeName_27 = this._cppExtensions.toTypeName(feature_11);
        String _firstLower_20 = StringExtensions.toFirstLower(_typeName_27);
        _builder.append(_firstLower_20, "    ");
        _builder.append(" = new ");
        String _typeName_28 = this._cppExtensions.toTypeName(feature_11);
        _builder.append(_typeName_28, "    ");
        _builder.append("();");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        String _typeName_29 = this._cppExtensions.toTypeName(feature_11);
        String _firstLower_21 = StringExtensions.toFirstLower(_typeName_29);
        _builder.append(_firstLower_21, "    ");
        _builder.append("->setParent(this);");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        String _typeName_30 = this._cppExtensions.toTypeName(feature_11);
        String _firstLower_22 = StringExtensions.toFirstLower(_typeName_30);
        _builder.append(_firstLower_22, "    ");
        _builder.append("->fillFromMap(");
        String _typeName_31 = this._cppExtensions.toTypeName(feature_11);
        String _firstLower_23 = StringExtensions.toFirstLower(_typeName_31);
        _builder.append(_firstLower_23, "    ");
        _builder.append("Map);");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("m");
        String _name_146 = this._cppExtensions.toName(feature_11);
        String _firstUpper_70 = StringExtensions.toFirstUpper(_name_146);
        _builder.append(_firstUpper_70, "    ");
        _builder.append(".append(");
        String _typeName_32 = this._cppExtensions.toTypeName(feature_11);
        String _firstLower_24 = StringExtensions.toFirstLower(_typeName_32);
        _builder.append(_firstLower_24, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("void ");
        String _name_147 = this._cppExtensions.toName(dto);
        _builder.append(_name_147, "");
        _builder.append("::removeFrom");
        String _name_148 = this._cppExtensions.toName(feature_11);
        String _firstUpper_71 = StringExtensions.toFirstUpper(_name_148);
        _builder.append(_firstUpper_71, "");
        _builder.append("ByKey(const QString& uuid)");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("for (int i = 0; i < m");
        String _name_149 = this._cppExtensions.toName(feature_11);
        String _firstUpper_72 = StringExtensions.toFirstUpper(_name_149);
        _builder.append(_firstUpper_72, "    ");
        _builder.append(".size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("if (qobject_cast<");
        String _typeName_33 = this._cppExtensions.toTypeName(feature_11);
        _builder.append(_typeName_33, "        ");
        _builder.append("*>(m");
        String _name_150 = this._cppExtensions.toName(feature_11);
        String _firstUpper_73 = StringExtensions.toFirstUpper(_name_150);
        _builder.append(_firstUpper_73, "        ");
        _builder.append(".at(i))->toMap().value(uuidKey).toString() == uuid) {");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("m");
        String _name_151 = this._cppExtensions.toName(feature_11);
        String _firstUpper_74 = StringExtensions.toFirstUpper(_name_151);
        _builder.append(_firstUpper_74, "            ");
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
        String _name_152 = this._cppExtensions.toName(feature_11);
        String _firstLower_25 = StringExtensions.toFirstLower(_name_152);
        _builder.append(_firstLower_25, "    ");
        _builder.append(": \" << uuid;");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("// TODO signal error");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("int ");
        String _name_153 = this._cppExtensions.toName(dto);
        _builder.append(_name_153, "");
        _builder.append("::");
        String _name_154 = this._cppExtensions.toName(feature_11);
        String _firstLower_26 = StringExtensions.toFirstLower(_name_154);
        _builder.append(_firstLower_26, "");
        _builder.append("Count(){");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("return m");
        String _name_155 = this._cppExtensions.toName(feature_11);
        String _firstUpper_75 = StringExtensions.toFirstUpper(_name_155);
        _builder.append(_firstUpper_75, "    ");
        _builder.append(".size();");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("QList<QObject*> ");
        String _name_156 = this._cppExtensions.toName(dto);
        _builder.append(_name_156, "");
        _builder.append("::");
        String _name_157 = this._cppExtensions.toName(feature_11);
        _builder.append(_name_157, "");
        _builder.append("()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return m");
        String _name_158 = this._cppExtensions.toName(feature_11);
        String _firstUpper_76 = StringExtensions.toFirstUpper(_name_158);
        _builder.append(_firstUpper_76, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_159 = this._cppExtensions.toName(dto);
        _builder.append(_name_159, "");
        _builder.append("::set");
        String _name_160 = this._cppExtensions.toName(feature_11);
        String _firstUpper_77 = StringExtensions.toFirstUpper(_name_160);
        _builder.append(_firstUpper_77, "");
        _builder.append("(QList<QObject*> ");
        String _name_161 = this._cppExtensions.toName(feature_11);
        _builder.append(_name_161, "");
        _builder.append(") ");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (");
        String _name_162 = this._cppExtensions.toName(feature_11);
        _builder.append(_name_162, "\t");
        _builder.append(" != m");
        String _name_163 = this._cppExtensions.toName(feature_11);
        String _firstUpper_78 = StringExtensions.toFirstUpper(_name_163);
        _builder.append(_firstUpper_78, "\t");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("m");
        String _name_164 = this._cppExtensions.toName(feature_11);
        String _firstUpper_79 = StringExtensions.toFirstUpper(_name_164);
        _builder.append(_firstUpper_79, "\t\t");
        _builder.append(" = ");
        String _name_165 = this._cppExtensions.toName(feature_11);
        _builder.append(_name_165, "\t\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("emit ");
        String _name_166 = this._cppExtensions.toName(feature_11);
        _builder.append(_name_166, "\t\t");
        _builder.append("Changed(");
        String _name_167 = this._cppExtensions.toName(feature_11);
        _builder.append(_name_167, "\t\t");
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
    String _name_168 = this._cppExtensions.toName(dto);
    _builder.append(_name_168, "");
    _builder.append("::~");
    String _name_169 = this._cppExtensions.toName(dto);
    _builder.append(_name_169, "");
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
