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
          boolean _isTypeOfDataObject = this._cppExtensions.isTypeOfDataObject(feature);
          if (!_isTypeOfDataObject) {
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
            _builder.append("\";");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.newLine();
    {
      boolean _existsForeignPropertyName = this._cppExtensions.existsForeignPropertyName(dto);
      if (_existsForeignPropertyName) {
        _builder.append("// keys used from Server API etc");
        _builder.newLine();
        {
          List<? extends LFeature> _allFeatures_1 = dto.getAllFeatures();
          for(final LFeature feature_1 : _allFeatures_1) {
            {
              boolean _and_1 = false;
              boolean _isTypeOfDataObject_1 = this._cppExtensions.isTypeOfDataObject(feature_1);
              if (!_isTypeOfDataObject_1) {
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
                String _foreignPropertyName = this._cppExtensions.toForeignPropertyName(feature_1);
                _builder.append(_foreignPropertyName, "");
                _builder.append("\";");
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
            boolean _isTypeOfDataObject = CppGenerator.this._cppExtensions.isTypeOfDataObject(it);
            boolean _not_1 = (!_isTypeOfDataObject);
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
      boolean _existsTypeOfDataObject = this._cppExtensions.existsTypeOfDataObject(dto);
      if (_existsTypeOfDataObject) {
        _builder.append("\t");
        _builder.append("// set Types of DataObject* to NULL:");
        _builder.newLine();
        {
          List<? extends LFeature> _allFeatures_3 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_1 = new Function1<LFeature, Boolean>() {
            public Boolean apply(final LFeature it) {
              boolean _and = false;
              boolean _and_1 = false;
              boolean _and_2 = false;
              boolean _isLazy = CppGenerator.this._cppExtensions.isLazy(it);
              boolean _not = (!_isLazy);
              if (!_not) {
                _and_2 = false;
              } else {
                boolean _isTypeOfDataObject = CppGenerator.this._cppExtensions.isTypeOfDataObject(it);
                _and_2 = _isTypeOfDataObject;
              }
              if (!_and_2) {
                _and_1 = false;
              } else {
                boolean _isToMany = CppGenerator.this._cppExtensions.isToMany(it);
                boolean _not_1 = (!_isToMany);
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
          Iterable<? extends LFeature> _filter_1 = IterableExtensions.filter(_allFeatures_3, _function_1);
          for(final LFeature feature_3 : _filter_1) {
            _builder.append("\t");
            _builder.append("m");
            String _name_10 = this._cppExtensions.toName(feature_3);
            String _firstUpper_1 = StringExtensions.toFirstUpper(_name_10);
            _builder.append(_firstUpper_1, "\t");
            _builder.append(" = 0;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      boolean _existsLazy = this._cppExtensions.existsLazy(dto);
      if (_existsLazy) {
        _builder.append("\t");
        _builder.append("// lazy references:");
        _builder.newLine();
        {
          List<? extends LFeature> _allFeatures_4 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_2 = new Function1<LFeature, Boolean>() {
            public Boolean apply(final LFeature it) {
              return Boolean.valueOf(CppGenerator.this._cppExtensions.isLazy(it));
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
            String _referenceDomainKeyType = this._cppExtensions.referenceDomainKeyType(feature_4);
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
          List<? extends LFeature> _allFeatures_5 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_3 = new Function1<LFeature, Boolean>() {
            public Boolean apply(final LFeature it) {
              return Boolean.valueOf(CppGenerator.this._cppExtensions.isEnum(it));
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
            String _typeName_1 = this._cppExtensions.toTypeName(feature_5);
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
          List<? extends LFeature> _allFeatures_6 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_4 = new Function1<LFeature, Boolean>() {
            public Boolean apply(final LFeature it) {
              return Boolean.valueOf(CppGenerator.this._cppExtensions.isTypeOfDates(it));
            }
          };
          Iterable<? extends LFeature> _filter_4 = IterableExtensions.filter(_allFeatures_6, _function_4);
          for(final LFeature feature_6 : _filter_4) {
            _builder.append("\t");
            _builder.append("m");
            String _name_13 = this._cppExtensions.toName(feature_6);
            String _firstUpper_4 = StringExtensions.toFirstUpper(_name_13);
            _builder.append(_firstUpper_4, "\t");
            _builder.append(" = ");
            String _typeName_2 = this._cppExtensions.toTypeName(feature_6);
            _builder.append(_typeName_2, "\t");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      boolean _existsTransient = this._cppExtensions.existsTransient(dto);
      if (_existsTransient) {
        _builder.append("\t");
        _builder.append("// transient values (not cached)");
        _builder.newLine();
        {
          List<? extends LFeature> _allFeatures_7 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_5 = new Function1<LFeature, Boolean>() {
            public Boolean apply(final LFeature it) {
              return Boolean.valueOf(CppGenerator.this._cppExtensions.isTransient(it));
            }
          };
          Iterable<? extends LFeature> _filter_5 = IterableExtensions.filter(_allFeatures_7, _function_5);
          for(final LFeature feature_7 : _filter_5) {
            _builder.append("\t");
            _builder.append("// ");
            String _typeName_3 = this._cppExtensions.toTypeName(feature_7);
            _builder.append(_typeName_3, "\t");
            _builder.append(" m");
            String _name_14 = this._cppExtensions.toName(feature_7);
            String _firstUpper_5 = StringExtensions.toFirstUpper(_name_14);
            _builder.append(_firstUpper_5, "\t");
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
    String _name_15 = this._cppExtensions.toName(dto);
    _builder.append(_name_15, " ");
    _builder.append(" from QVariantMap");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("* Map got from JsonDataAccess or so");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* includes also transient values");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* uses own property names");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* corresponding export method: toMap()");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("void ");
    String _name_16 = this._cppExtensions.toName(dto);
    _builder.append(_name_16, "");
    _builder.append("::fillFromMap(const QVariantMap& ");
    String _name_17 = this._cppExtensions.toName(dto);
    String _firstLower = StringExtensions.toFirstLower(_name_17);
    _builder.append(_firstLower, "");
    _builder.append("Map)");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_8 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_6 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _isToMany = CppGenerator.this._cppExtensions.isToMany(it);
          return Boolean.valueOf((!_isToMany));
        }
      };
      Iterable<? extends LFeature> _filter_6 = IterableExtensions.filter(_allFeatures_8, _function_6);
      for(final LFeature feature_8 : _filter_6) {
        {
          boolean _isTypeOfDataObject_2 = this._cppExtensions.isTypeOfDataObject(feature_8);
          if (_isTypeOfDataObject_2) {
            {
              boolean _isContained_3 = this._cppExtensions.isContained(feature_8);
              if (_isContained_3) {
                _builder.append("\t");
                _builder.append("// m");
                String _name_18 = this._cppExtensions.toName(feature_8);
                String _firstUpper_6 = StringExtensions.toFirstUpper(_name_18);
                _builder.append(_firstUpper_6, "\t");
                _builder.append(" is parent (");
                String _typeName_4 = this._cppExtensions.toTypeName(feature_8);
                _builder.append(_typeName_4, "\t");
                _builder.append("* containing ");
                String _name_19 = this._cppExtensions.toName(dto);
                _builder.append(_name_19, "\t");
                _builder.append(")");
                _builder.newLineIfNotEmpty();
              } else {
                boolean _isLazy = this._cppExtensions.isLazy(feature_8);
                if (_isLazy) {
                  _builder.append("\t");
                  _builder.append("// ");
                  String _name_20 = this._cppExtensions.toName(feature_8);
                  _builder.append(_name_20, "\t");
                  _builder.append(" lazy pointing to ");
                  String _typeOrQObject = this._cppExtensions.toTypeOrQObject(feature_8);
                  _builder.append(_typeOrQObject, "\t");
                  _builder.append(" (domainKey: ");
                  String _referenceDomainKey = this._cppExtensions.referenceDomainKey(feature_8);
                  _builder.append(_referenceDomainKey, "\t");
                  _builder.append(")");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("if (");
                  String _name_21 = this._cppExtensions.toName(dto);
                  String _firstLower_1 = StringExtensions.toFirstLower(_name_21);
                  _builder.append(_firstLower_1, "\t");
                  _builder.append("Map.contains(");
                  String _name_22 = this._cppExtensions.toName(feature_8);
                  _builder.append(_name_22, "\t");
                  _builder.append("Key)) {");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("m");
                  String _name_23 = this._cppExtensions.toName(feature_8);
                  String _firstUpper_7 = StringExtensions.toFirstUpper(_name_23);
                  _builder.append(_firstUpper_7, "\t\t");
                  _builder.append(" = ");
                  String _name_24 = this._cppExtensions.toName(dto);
                  String _firstLower_2 = StringExtensions.toFirstLower(_name_24);
                  _builder.append(_firstLower_2, "\t\t");
                  _builder.append("Map.value(");
                  String _name_25 = this._cppExtensions.toName(feature_8);
                  _builder.append(_name_25, "\t\t");
                  _builder.append("Key).to");
                  String _referenceDomainKeyType_1 = this._cppExtensions.referenceDomainKeyType(feature_8);
                  String _mapToLazyTypeName = this._cppExtensions.mapToLazyTypeName(_referenceDomainKeyType_1);
                  _builder.append(_mapToLazyTypeName, "\t\t");
                  _builder.append("();");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("if (m");
                  String _name_26 = this._cppExtensions.toName(feature_8);
                  String _firstUpper_8 = StringExtensions.toFirstUpper(_name_26);
                  _builder.append(_firstUpper_8, "\t\t");
                  _builder.append(" != ");
                  String _referenceDomainKeyType_2 = this._cppExtensions.referenceDomainKeyType(feature_8);
                  String _defaultForLazyTypeName_1 = this._cppExtensions.defaultForLazyTypeName(_referenceDomainKeyType_2);
                  _builder.append(_defaultForLazyTypeName_1, "\t\t");
                  _builder.append(") {");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t\t");
                  _builder.append("// resolve the corresponding Data Object on demand from DataManager");
                  _builder.newLine();
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
                  String _name_27 = this._cppExtensions.toName(feature_8);
                  String _firstUpper_9 = StringExtensions.toFirstUpper(_name_27);
                  _builder.append(_firstUpper_9, "\t");
                  _builder.append(" points to ");
                  String _typeName_5 = this._cppExtensions.toTypeName(feature_8);
                  _builder.append(_typeName_5, "\t");
                  _builder.append("*");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("if (");
                  String _name_28 = this._cppExtensions.toName(dto);
                  String _firstLower_3 = StringExtensions.toFirstLower(_name_28);
                  _builder.append(_firstLower_3, "\t");
                  _builder.append("Map.contains(");
                  String _name_29 = this._cppExtensions.toName(feature_8);
                  _builder.append(_name_29, "\t");
                  _builder.append("Key)) {");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("QVariantMap ");
                  String _name_30 = this._cppExtensions.toName(feature_8);
                  _builder.append(_name_30, "\t\t");
                  _builder.append("Map;");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  String _name_31 = this._cppExtensions.toName(feature_8);
                  _builder.append(_name_31, "\t\t");
                  _builder.append("Map = ");
                  String _name_32 = this._cppExtensions.toName(dto);
                  String _firstLower_4 = StringExtensions.toFirstLower(_name_32);
                  _builder.append(_firstLower_4, "\t\t");
                  _builder.append("Map.value(");
                  String _name_33 = this._cppExtensions.toName(feature_8);
                  _builder.append(_name_33, "\t\t");
                  _builder.append("Key).toMap();");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("if (!");
                  String _name_34 = this._cppExtensions.toName(feature_8);
                  _builder.append(_name_34, "\t\t");
                  _builder.append("Map.isEmpty()) {");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_35 = this._cppExtensions.toName(feature_8);
                  String _firstUpper_10 = StringExtensions.toFirstUpper(_name_35);
                  _builder.append(_firstUpper_10, "\t\t\t");
                  _builder.append(" = new ");
                  String _typeName_6 = this._cppExtensions.toTypeName(feature_8);
                  _builder.append(_typeName_6, "\t\t\t");
                  _builder.append("();");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_36 = this._cppExtensions.toName(feature_8);
                  String _firstUpper_11 = StringExtensions.toFirstUpper(_name_36);
                  _builder.append(_firstUpper_11, "\t\t\t");
                  _builder.append("->setParent(this);");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_37 = this._cppExtensions.toName(feature_8);
                  String _firstUpper_12 = StringExtensions.toFirstUpper(_name_37);
                  _builder.append(_firstUpper_12, "\t\t\t");
                  _builder.append("->fillFromMap(");
                  String _name_38 = this._cppExtensions.toName(feature_8);
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
              boolean _isTransient = this._cppExtensions.isTransient(feature_8);
              if (_isTransient) {
                _builder.append("\t");
                _builder.append("// m");
                String _name_39 = this._cppExtensions.toName(feature_8);
                String _firstUpper_13 = StringExtensions.toFirstUpper(_name_39);
                _builder.append(_firstUpper_13, "\t");
                _builder.append(" is transient");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("if (");
                String _name_40 = this._cppExtensions.toName(dto);
                String _firstLower_5 = StringExtensions.toFirstLower(_name_40);
                _builder.append(_firstLower_5, "\t");
                _builder.append("Map.contains(");
                String _name_41 = this._cppExtensions.toName(feature_8);
                String _firstLower_6 = StringExtensions.toFirstLower(_name_41);
                _builder.append(_firstLower_6, "\t");
                _builder.append("Key)) {");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("m");
                String _name_42 = this._cppExtensions.toName(feature_8);
                String _firstUpper_14 = StringExtensions.toFirstUpper(_name_42);
                _builder.append(_firstUpper_14, "\t\t");
                _builder.append(" = ");
                String _name_43 = this._cppExtensions.toName(dto);
                String _firstLower_7 = StringExtensions.toFirstLower(_name_43);
                _builder.append(_firstLower_7, "\t\t");
                _builder.append("Map.value(");
                String _name_44 = this._cppExtensions.toName(feature_8);
                _builder.append(_name_44, "\t\t");
                _builder.append("Key).to");
                String _mapToType = this._cppExtensions.mapToType(feature_8);
                _builder.append(_mapToType, "\t\t");
                _builder.append("();");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("}");
                _builder.newLine();
              } else {
                boolean _isEnum = this._cppExtensions.isEnum(feature_8);
                if (_isEnum) {
                  _builder.append("\t");
                  _builder.append("// ENUM");
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("if (");
                  String _name_45 = this._cppExtensions.toName(dto);
                  String _firstLower_8 = StringExtensions.toFirstLower(_name_45);
                  _builder.append(_firstLower_8, "\t");
                  _builder.append("Map.contains(");
                  String _name_46 = this._cppExtensions.toName(feature_8);
                  String _firstLower_9 = StringExtensions.toFirstLower(_name_46);
                  _builder.append(_firstLower_9, "\t");
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
                  String _name_47 = this._cppExtensions.toName(dto);
                  String _firstLower_10 = StringExtensions.toFirstLower(_name_47);
                  _builder.append(_firstLower_10, "\t\t");
                  _builder.append("Map.value(");
                  String _name_48 = this._cppExtensions.toName(feature_8);
                  String _firstLower_11 = StringExtensions.toFirstLower(_name_48);
                  _builder.append(_firstLower_11, "\t\t");
                  _builder.append("Key).toInt(ok);");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("if (ok) {");
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_49 = this._cppExtensions.toName(feature_8);
                  String _firstUpper_15 = StringExtensions.toFirstUpper(_name_49);
                  _builder.append(_firstUpper_15, "\t\t\t");
                  _builder.append(" = ");
                  String _name_50 = this._cppExtensions.toName(dto);
                  String _firstLower_12 = StringExtensions.toFirstLower(_name_50);
                  _builder.append(_firstLower_12, "\t\t\t");
                  _builder.append("Map.value(");
                  String _name_51 = this._cppExtensions.toName(feature_8);
                  String _firstLower_13 = StringExtensions.toFirstLower(_name_51);
                  _builder.append(_firstLower_13, "\t\t\t");
                  _builder.append("Key).toInt();");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  _builder.append("} else {");
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_52 = this._cppExtensions.toName(feature_8);
                  String _firstUpper_16 = StringExtensions.toFirstUpper(_name_52);
                  _builder.append(_firstUpper_16, "\t\t\t");
                  _builder.append(" = ");
                  String _name_53 = this._cppExtensions.toName(feature_8);
                  String _firstLower_14 = StringExtensions.toFirstLower(_name_53);
                  _builder.append(_firstLower_14, "\t\t\t");
                  _builder.append("StringToInt(");
                  String _name_54 = this._cppExtensions.toName(dto);
                  String _firstLower_15 = StringExtensions.toFirstLower(_name_54);
                  _builder.append(_firstLower_15, "\t\t\t");
                  _builder.append("Map.value(");
                  String _name_55 = this._cppExtensions.toName(feature_8);
                  String _firstLower_16 = StringExtensions.toFirstLower(_name_55);
                  _builder.append(_firstLower_16, "\t\t\t");
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
                  String _name_56 = this._cppExtensions.toName(feature_8);
                  String _firstUpper_17 = StringExtensions.toFirstUpper(_name_56);
                  _builder.append(_firstUpper_17, "\t\t");
                  _builder.append(" = ");
                  String _typeName_7 = this._cppExtensions.toTypeName(feature_8);
                  _builder.append(_typeName_7, "\t\t");
                  _builder.append("::NO_VALUE;");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("}");
                  _builder.newLine();
                } else {
                  boolean _isTypeOfDates = this._cppExtensions.isTypeOfDates(feature_8);
                  if (_isTypeOfDates) {
                    _builder.append("\t");
                    _builder.append("if (");
                    String _name_57 = this._cppExtensions.toName(dto);
                    String _firstLower_17 = StringExtensions.toFirstLower(_name_57);
                    _builder.append(_firstLower_17, "\t");
                    _builder.append("Map.contains(");
                    String _name_58 = this._cppExtensions.toName(feature_8);
                    String _firstLower_18 = StringExtensions.toFirstLower(_name_58);
                    _builder.append(_firstLower_18, "\t");
                    _builder.append("Key)) {");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("// always getting the Date as a String (from server or JSON)");
                    _builder.newLine();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("QString ");
                    String _name_59 = this._cppExtensions.toName(feature_8);
                    String _firstLower_19 = StringExtensions.toFirstLower(_name_59);
                    _builder.append(_firstLower_19, "\t\t");
                    _builder.append("AsString = ");
                    String _name_60 = this._cppExtensions.toName(dto);
                    String _firstLower_20 = StringExtensions.toFirstLower(_name_60);
                    _builder.append(_firstLower_20, "\t\t");
                    _builder.append("Map.value(");
                    String _name_61 = this._cppExtensions.toName(feature_8);
                    String _firstLower_21 = StringExtensions.toFirstLower(_name_61);
                    _builder.append(_firstLower_21, "\t\t");
                    _builder.append("Key).toString();");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("m");
                    String _name_62 = this._cppExtensions.toName(feature_8);
                    String _firstUpper_18 = StringExtensions.toFirstUpper(_name_62);
                    _builder.append(_firstUpper_18, "\t\t");
                    _builder.append(" = ");
                    String _typeName_8 = this._cppExtensions.toTypeName(feature_8);
                    _builder.append(_typeName_8, "\t\t");
                    _builder.append("::fromString(");
                    String _name_63 = this._cppExtensions.toName(feature_8);
                    String _firstLower_22 = StringExtensions.toFirstLower(_name_63);
                    _builder.append(_firstLower_22, "\t\t");
                    _builder.append("AsString, ");
                    String _dateFormatString = this._cppExtensions.toDateFormatString(feature_8);
                    _builder.append(_dateFormatString, "\t\t");
                    _builder.append(");");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("if (!m");
                    String _name_64 = this._cppExtensions.toName(feature_8);
                    String _firstUpper_19 = StringExtensions.toFirstUpper(_name_64);
                    _builder.append(_firstUpper_19, "\t\t");
                    _builder.append(".isValid()) {");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t\t");
                    _builder.append("m");
                    String _name_65 = this._cppExtensions.toName(feature_8);
                    String _firstUpper_20 = StringExtensions.toFirstUpper(_name_65);
                    _builder.append(_firstUpper_20, "\t\t\t");
                    _builder.append(" = ");
                    String _typeName_9 = this._cppExtensions.toTypeName(feature_8);
                    _builder.append(_typeName_9, "\t\t\t");
                    _builder.append("();");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t\t");
                    _builder.append("qDebug() << \"m");
                    String _name_66 = this._cppExtensions.toName(feature_8);
                    String _firstUpper_21 = StringExtensions.toFirstUpper(_name_66);
                    _builder.append(_firstUpper_21, "\t\t\t");
                    _builder.append(" is not valid for String: \" << ");
                    String _name_67 = this._cppExtensions.toName(feature_8);
                    String _firstLower_23 = StringExtensions.toFirstLower(_name_67);
                    _builder.append(_firstLower_23, "\t\t\t");
                    _builder.append("AsString;");
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
                    _builder.append("m");
                    String _name_68 = this._cppExtensions.toName(feature_8);
                    String _firstUpper_22 = StringExtensions.toFirstUpper(_name_68);
                    _builder.append(_firstUpper_22, "\t");
                    _builder.append(" = ");
                    String _name_69 = this._cppExtensions.toName(dto);
                    String _firstLower_24 = StringExtensions.toFirstLower(_name_69);
                    _builder.append(_firstLower_24, "\t");
                    _builder.append("Map.value(");
                    String _name_70 = this._cppExtensions.toName(feature_8);
                    _builder.append(_name_70, "\t");
                    _builder.append("Key).to");
                    String _mapToType_1 = this._cppExtensions.mapToType(feature_8);
                    _builder.append(_mapToType_1, "\t");
                    _builder.append("();");
                    _builder.newLineIfNotEmpty();
                  }
                }
              }
            }
            {
              String _name_71 = this._cppExtensions.toName(feature_8);
              boolean _equals = Objects.equal(_name_71, "uuid");
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
      List<? extends LFeature> _allFeatures_9 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_7 = new Function1<LFeature, Boolean>() {
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
      Iterable<? extends LFeature> _filter_7 = IterableExtensions.filter(_allFeatures_9, _function_7);
      for(final LFeature feature_9 : _filter_7) {
        _builder.append("\t");
        _builder.append("// m");
        String _name_72 = this._cppExtensions.toName(feature_9);
        String _firstUpper_23 = StringExtensions.toFirstUpper(_name_72);
        _builder.append(_firstUpper_23, "\t");
        _builder.append(" is List of ");
        String _typeName_10 = this._cppExtensions.toTypeName(feature_9);
        _builder.append(_typeName_10, "\t");
        _builder.append("*");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("QVariantList ");
        String _name_73 = this._cppExtensions.toName(feature_9);
        String _firstLower_25 = StringExtensions.toFirstLower(_name_73);
        _builder.append(_firstLower_25, "\t");
        _builder.append("List;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        String _name_74 = this._cppExtensions.toName(feature_9);
        String _firstLower_26 = StringExtensions.toFirstLower(_name_74);
        _builder.append(_firstLower_26, "\t");
        _builder.append("List = ");
        String _name_75 = this._cppExtensions.toName(dto);
        String _firstLower_27 = StringExtensions.toFirstLower(_name_75);
        _builder.append(_firstLower_27, "\t");
        _builder.append("Map.value(");
        String _name_76 = this._cppExtensions.toName(feature_9);
        String _firstLower_28 = StringExtensions.toFirstLower(_name_76);
        _builder.append(_firstLower_28, "\t");
        _builder.append("Key).toList();");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("m");
        String _name_77 = this._cppExtensions.toName(feature_9);
        String _firstUpper_24 = StringExtensions.toFirstUpper(_name_77);
        _builder.append(_firstUpper_24, "\t");
        _builder.append(".clear();");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("for (int i = 0; i < ");
        String _name_78 = this._cppExtensions.toName(feature_9);
        String _firstLower_29 = StringExtensions.toFirstLower(_name_78);
        _builder.append(_firstLower_29, "\t");
        _builder.append("List.size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("QVariantMap ");
        String _name_79 = this._cppExtensions.toName(feature_9);
        String _firstLower_30 = StringExtensions.toFirstLower(_name_79);
        _builder.append(_firstLower_30, "\t\t");
        _builder.append("Map;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        String _name_80 = this._cppExtensions.toName(feature_9);
        String _firstLower_31 = StringExtensions.toFirstLower(_name_80);
        _builder.append(_firstLower_31, "\t\t");
        _builder.append("Map = ");
        String _name_81 = this._cppExtensions.toName(feature_9);
        String _firstLower_32 = StringExtensions.toFirstLower(_name_81);
        _builder.append(_firstLower_32, "\t\t");
        _builder.append("List.at(i).toMap();");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        String _typeName_11 = this._cppExtensions.toTypeName(feature_9);
        _builder.append(_typeName_11, "\t\t");
        _builder.append("* ");
        String _typeName_12 = this._cppExtensions.toTypeName(feature_9);
        String _firstLower_33 = StringExtensions.toFirstLower(_typeName_12);
        _builder.append(_firstLower_33, "\t\t");
        _builder.append(" = new ");
        String _typeName_13 = this._cppExtensions.toTypeName(feature_9);
        _builder.append(_typeName_13, "\t\t");
        _builder.append("();");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        String _typeName_14 = this._cppExtensions.toTypeName(feature_9);
        String _firstLower_34 = StringExtensions.toFirstLower(_typeName_14);
        _builder.append(_firstLower_34, "\t\t");
        _builder.append("->setParent(this);");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        String _typeName_15 = this._cppExtensions.toTypeName(feature_9);
        String _firstLower_35 = StringExtensions.toFirstLower(_typeName_15);
        _builder.append(_firstLower_35, "\t\t");
        _builder.append("->fillFromMap(");
        String _name_82 = this._cppExtensions.toName(feature_9);
        String _firstLower_36 = StringExtensions.toFirstLower(_name_82);
        _builder.append(_firstLower_36, "\t\t");
        _builder.append("Map);");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("m");
        String _name_83 = this._cppExtensions.toName(feature_9);
        String _firstUpper_25 = StringExtensions.toFirstUpper(_name_83);
        _builder.append(_firstUpper_25, "\t\t");
        _builder.append(".append(");
        String _typeName_16 = this._cppExtensions.toTypeName(feature_9);
        String _firstLower_37 = StringExtensions.toFirstLower(_typeName_16);
        _builder.append(_firstLower_37, "\t\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    {
      List<? extends LFeature> _allFeatures_10 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_8 = new Function1<LFeature, Boolean>() {
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
      Iterable<? extends LFeature> _filter_8 = IterableExtensions.filter(_allFeatures_10, _function_8);
      for(final LFeature feature_10 : _filter_8) {
        {
          String _typeName_17 = this._cppExtensions.toTypeName(feature_10);
          boolean _equals_1 = Objects.equal(_typeName_17, "QString");
          if (_equals_1) {
            _builder.append("\t");
            _builder.append("m");
            String _name_84 = this._cppExtensions.toName(feature_10);
            String _firstUpper_26 = StringExtensions.toFirstUpper(_name_84);
            _builder.append(_firstUpper_26, "\t");
            _builder.append("StringList = ");
            String _name_85 = this._cppExtensions.toName(dto);
            String _firstLower_38 = StringExtensions.toFirstLower(_name_85);
            _builder.append(_firstLower_38, "\t");
            _builder.append("Map.value(");
            String _name_86 = this._cppExtensions.toName(feature_10);
            String _firstLower_39 = StringExtensions.toFirstLower(_name_86);
            _builder.append(_firstLower_39, "\t");
            _builder.append("Key).toStringList();");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t");
            _builder.append("// m");
            String _name_87 = this._cppExtensions.toName(feature_10);
            String _firstUpper_27 = StringExtensions.toFirstUpper(_name_87);
            _builder.append(_firstUpper_27, "\t");
            _builder.append(" is Array of ");
            String _typeName_18 = this._cppExtensions.toTypeName(feature_10);
            _builder.append(_typeName_18, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("QVariantList ");
            String _name_88 = this._cppExtensions.toName(feature_10);
            _builder.append(_name_88, "\t");
            _builder.append("List;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            String _name_89 = this._cppExtensions.toName(feature_10);
            _builder.append(_name_89, "\t");
            _builder.append("List = ");
            String _name_90 = this._cppExtensions.toName(dto);
            String _firstLower_40 = StringExtensions.toFirstLower(_name_90);
            _builder.append(_firstLower_40, "\t");
            _builder.append("Map.value(");
            String _name_91 = this._cppExtensions.toName(feature_10);
            _builder.append(_name_91, "\t");
            _builder.append("Key).toList();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("m");
            String _name_92 = this._cppExtensions.toName(feature_10);
            String _firstUpper_28 = StringExtensions.toFirstUpper(_name_92);
            _builder.append(_firstUpper_28, "\t");
            _builder.append(".clear();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("for (int i = 0; i < ");
            String _name_93 = this._cppExtensions.toName(feature_10);
            _builder.append(_name_93, "\t");
            _builder.append("List.size(); ++i) {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("m");
            String _name_94 = this._cppExtensions.toName(feature_10);
            String _firstUpper_29 = StringExtensions.toFirstUpper(_name_94);
            _builder.append(_firstUpper_29, "\t\t");
            _builder.append(".append(");
            String _name_95 = this._cppExtensions.toName(feature_10);
            _builder.append(_name_95, "\t\t");
            _builder.append("List.at(i).to");
            String _mapToSingleType = this._cppExtensions.mapToSingleType(feature_10);
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
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* initialize OrderData from QVariantMap");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Map got from JsonDataAccess or so");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* includes also transient values");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* uses foreign property names - per ex. from Server API");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* corresponding export method: toForeignMap()");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("void ");
    String _name_96 = this._cppExtensions.toName(dto);
    _builder.append(_name_96, "");
    _builder.append("::fillFromForeignMap(const QVariantMap& ");
    String _name_97 = this._cppExtensions.toName(dto);
    String _firstLower_41 = StringExtensions.toFirstLower(_name_97);
    _builder.append(_firstLower_41, "");
    _builder.append("Map)");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    {
      boolean _existsForeignPropertyName_1 = this._cppExtensions.existsForeignPropertyName(dto);
      if (_existsForeignPropertyName_1) {
        {
          List<? extends LFeature> _allFeatures_11 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_9 = new Function1<LFeature, Boolean>() {
            public Boolean apply(final LFeature it) {
              boolean _isToMany = CppGenerator.this._cppExtensions.isToMany(it);
              return Boolean.valueOf((!_isToMany));
            }
          };
          Iterable<? extends LFeature> _filter_9 = IterableExtensions.filter(_allFeatures_11, _function_9);
          for(final LFeature feature_11 : _filter_9) {
            {
              boolean _isTypeOfDataObject_3 = this._cppExtensions.isTypeOfDataObject(feature_11);
              if (_isTypeOfDataObject_3) {
                {
                  boolean _isContained_4 = this._cppExtensions.isContained(feature_11);
                  if (_isContained_4) {
                    _builder.append("\t");
                    _builder.append("// m");
                    String _name_98 = this._cppExtensions.toName(feature_11);
                    String _firstUpper_30 = StringExtensions.toFirstUpper(_name_98);
                    _builder.append(_firstUpper_30, "\t");
                    _builder.append(" is parent (");
                    String _typeName_19 = this._cppExtensions.toTypeName(feature_11);
                    _builder.append(_typeName_19, "\t");
                    _builder.append("* containing ");
                    String _name_99 = this._cppExtensions.toName(dto);
                    _builder.append(_name_99, "\t");
                    _builder.append(")");
                    _builder.newLineIfNotEmpty();
                  } else {
                    boolean _isLazy_1 = this._cppExtensions.isLazy(feature_11);
                    if (_isLazy_1) {
                      _builder.append("\t");
                      _builder.append("// ");
                      String _name_100 = this._cppExtensions.toName(feature_11);
                      _builder.append(_name_100, "\t");
                      _builder.append(" lazy pointing to ");
                      String _typeOrQObject_1 = this._cppExtensions.toTypeOrQObject(feature_11);
                      _builder.append(_typeOrQObject_1, "\t");
                      _builder.append(" (domainKey: ");
                      String _referenceDomainKey_1 = this._cppExtensions.referenceDomainKey(feature_11);
                      _builder.append(_referenceDomainKey_1, "\t");
                      _builder.append(")");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("if (");
                      String _name_101 = this._cppExtensions.toName(dto);
                      String _firstLower_42 = StringExtensions.toFirstLower(_name_101);
                      _builder.append(_firstLower_42, "\t");
                      _builder.append("Map.contains(");
                      String _name_102 = this._cppExtensions.toName(feature_11);
                      _builder.append(_name_102, "\t");
                      _builder.append("ForeignKey)) {");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t");
                      _builder.append("m");
                      String _name_103 = this._cppExtensions.toName(feature_11);
                      String _firstUpper_31 = StringExtensions.toFirstUpper(_name_103);
                      _builder.append(_firstUpper_31, "\t\t");
                      _builder.append(" = ");
                      String _name_104 = this._cppExtensions.toName(dto);
                      String _firstLower_43 = StringExtensions.toFirstLower(_name_104);
                      _builder.append(_firstLower_43, "\t\t");
                      _builder.append("Map.value(");
                      String _name_105 = this._cppExtensions.toName(feature_11);
                      _builder.append(_name_105, "\t\t");
                      _builder.append("ForeignKey).to");
                      String _referenceDomainKeyType_3 = this._cppExtensions.referenceDomainKeyType(feature_11);
                      String _mapToLazyTypeName_1 = this._cppExtensions.mapToLazyTypeName(_referenceDomainKeyType_3);
                      _builder.append(_mapToLazyTypeName_1, "\t\t");
                      _builder.append("();");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t");
                      _builder.append("if (m");
                      String _name_106 = this._cppExtensions.toName(feature_11);
                      String _firstUpper_32 = StringExtensions.toFirstUpper(_name_106);
                      _builder.append(_firstUpper_32, "\t\t");
                      _builder.append(" != ");
                      String _referenceDomainKeyType_4 = this._cppExtensions.referenceDomainKeyType(feature_11);
                      String _defaultForLazyTypeName_2 = this._cppExtensions.defaultForLazyTypeName(_referenceDomainKeyType_4);
                      _builder.append(_defaultForLazyTypeName_2, "\t\t");
                      _builder.append(") {");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t\t");
                      _builder.append("// resolve the corresponding Data Object on demand from DataManager");
                      _builder.newLine();
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
                      String _name_107 = this._cppExtensions.toName(feature_11);
                      String _firstUpper_33 = StringExtensions.toFirstUpper(_name_107);
                      _builder.append(_firstUpper_33, "\t");
                      _builder.append(" points to ");
                      String _typeName_20 = this._cppExtensions.toTypeName(feature_11);
                      _builder.append(_typeName_20, "\t");
                      _builder.append("*");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("if (");
                      String _name_108 = this._cppExtensions.toName(dto);
                      String _firstLower_44 = StringExtensions.toFirstLower(_name_108);
                      _builder.append(_firstLower_44, "\t");
                      _builder.append("Map.contains(");
                      String _name_109 = this._cppExtensions.toName(feature_11);
                      _builder.append(_name_109, "\t");
                      _builder.append("ForeignKey)) {");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t");
                      _builder.append("QVariantMap ");
                      String _name_110 = this._cppExtensions.toName(feature_11);
                      _builder.append(_name_110, "\t\t");
                      _builder.append("Map;");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t");
                      String _name_111 = this._cppExtensions.toName(feature_11);
                      _builder.append(_name_111, "\t\t");
                      _builder.append("Map = ");
                      String _name_112 = this._cppExtensions.toName(dto);
                      String _firstLower_45 = StringExtensions.toFirstLower(_name_112);
                      _builder.append(_firstLower_45, "\t\t");
                      _builder.append("Map.value(");
                      String _name_113 = this._cppExtensions.toName(feature_11);
                      _builder.append(_name_113, "\t\t");
                      _builder.append("ForeignKey).toMap();");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t");
                      _builder.append("if (!");
                      String _name_114 = this._cppExtensions.toName(feature_11);
                      _builder.append(_name_114, "\t\t");
                      _builder.append("Map.isEmpty()) {");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t\t");
                      _builder.append("m");
                      String _name_115 = this._cppExtensions.toName(feature_11);
                      String _firstUpper_34 = StringExtensions.toFirstUpper(_name_115);
                      _builder.append(_firstUpper_34, "\t\t\t");
                      _builder.append(" = new ");
                      String _typeName_21 = this._cppExtensions.toTypeName(feature_11);
                      _builder.append(_typeName_21, "\t\t\t");
                      _builder.append("();");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t\t");
                      _builder.append("m");
                      String _name_116 = this._cppExtensions.toName(feature_11);
                      String _firstUpper_35 = StringExtensions.toFirstUpper(_name_116);
                      _builder.append(_firstUpper_35, "\t\t\t");
                      _builder.append("->setParent(this);");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t\t");
                      _builder.append("m");
                      String _name_117 = this._cppExtensions.toName(feature_11);
                      String _firstUpper_36 = StringExtensions.toFirstUpper(_name_117);
                      _builder.append(_firstUpper_36, "\t\t\t");
                      _builder.append("->fillFromMap(");
                      String _name_118 = this._cppExtensions.toName(feature_11);
                      _builder.append(_name_118, "\t\t\t");
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
                  boolean _isTransient_1 = this._cppExtensions.isTransient(feature_11);
                  if (_isTransient_1) {
                    _builder.append("\t");
                    _builder.append("// m");
                    String _name_119 = this._cppExtensions.toName(feature_11);
                    String _firstUpper_37 = StringExtensions.toFirstUpper(_name_119);
                    _builder.append(_firstUpper_37, "\t");
                    _builder.append(" is transient");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("if (");
                    String _name_120 = this._cppExtensions.toName(dto);
                    String _firstLower_46 = StringExtensions.toFirstLower(_name_120);
                    _builder.append(_firstLower_46, "\t");
                    _builder.append("Map.contains(");
                    String _name_121 = this._cppExtensions.toName(feature_11);
                    String _firstLower_47 = StringExtensions.toFirstLower(_name_121);
                    _builder.append(_firstLower_47, "\t");
                    _builder.append("ForeignKey)) {");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("m");
                    String _name_122 = this._cppExtensions.toName(feature_11);
                    String _firstUpper_38 = StringExtensions.toFirstUpper(_name_122);
                    _builder.append(_firstUpper_38, "\t\t");
                    _builder.append(" = ");
                    String _name_123 = this._cppExtensions.toName(dto);
                    String _firstLower_48 = StringExtensions.toFirstLower(_name_123);
                    _builder.append(_firstLower_48, "\t\t");
                    _builder.append("Map.value(");
                    String _name_124 = this._cppExtensions.toName(feature_11);
                    _builder.append(_name_124, "\t\t");
                    _builder.append("ForeignKey).to");
                    String _mapToType_2 = this._cppExtensions.mapToType(feature_11);
                    _builder.append(_mapToType_2, "\t\t");
                    _builder.append("();");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("}");
                    _builder.newLine();
                  } else {
                    boolean _isEnum_1 = this._cppExtensions.isEnum(feature_11);
                    if (_isEnum_1) {
                      _builder.append("\t");
                      _builder.append("// ENUM");
                      _builder.newLine();
                      _builder.append("\t");
                      _builder.append("if (");
                      String _name_125 = this._cppExtensions.toName(dto);
                      String _firstLower_49 = StringExtensions.toFirstLower(_name_125);
                      _builder.append(_firstLower_49, "\t");
                      _builder.append("Map.contains(");
                      String _name_126 = this._cppExtensions.toName(feature_11);
                      String _firstLower_50 = StringExtensions.toFirstLower(_name_126);
                      _builder.append(_firstLower_50, "\t");
                      _builder.append("ForeignKey)) {");
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
                      String _name_127 = this._cppExtensions.toName(dto);
                      String _firstLower_51 = StringExtensions.toFirstLower(_name_127);
                      _builder.append(_firstLower_51, "\t\t");
                      _builder.append("Map.value(");
                      String _name_128 = this._cppExtensions.toName(feature_11);
                      String _firstLower_52 = StringExtensions.toFirstLower(_name_128);
                      _builder.append(_firstLower_52, "\t\t");
                      _builder.append("ForeignKey).toInt(ok);");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t");
                      _builder.append("if (ok) {");
                      _builder.newLine();
                      _builder.append("\t");
                      _builder.append("\t\t");
                      _builder.append("m");
                      String _name_129 = this._cppExtensions.toName(feature_11);
                      String _firstUpper_39 = StringExtensions.toFirstUpper(_name_129);
                      _builder.append(_firstUpper_39, "\t\t\t");
                      _builder.append(" = ");
                      String _name_130 = this._cppExtensions.toName(dto);
                      String _firstLower_53 = StringExtensions.toFirstLower(_name_130);
                      _builder.append(_firstLower_53, "\t\t\t");
                      _builder.append("Map.value(");
                      String _name_131 = this._cppExtensions.toName(feature_11);
                      String _firstLower_54 = StringExtensions.toFirstLower(_name_131);
                      _builder.append(_firstLower_54, "\t\t\t");
                      _builder.append("ForeignKey).toInt();");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t");
                      _builder.append("} else {");
                      _builder.newLine();
                      _builder.append("\t");
                      _builder.append("\t\t");
                      _builder.append("m");
                      String _name_132 = this._cppExtensions.toName(feature_11);
                      String _firstUpper_40 = StringExtensions.toFirstUpper(_name_132);
                      _builder.append(_firstUpper_40, "\t\t\t");
                      _builder.append(" = ");
                      String _name_133 = this._cppExtensions.toName(feature_11);
                      String _firstLower_55 = StringExtensions.toFirstLower(_name_133);
                      _builder.append(_firstLower_55, "\t\t\t");
                      _builder.append("StringToInt(");
                      String _name_134 = this._cppExtensions.toName(dto);
                      String _firstLower_56 = StringExtensions.toFirstLower(_name_134);
                      _builder.append(_firstLower_56, "\t\t\t");
                      _builder.append("Map.value(");
                      String _name_135 = this._cppExtensions.toName(feature_11);
                      String _firstLower_57 = StringExtensions.toFirstLower(_name_135);
                      _builder.append(_firstLower_57, "\t\t\t");
                      _builder.append("ForeignKey).toString());");
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
                      String _name_136 = this._cppExtensions.toName(feature_11);
                      String _firstUpper_41 = StringExtensions.toFirstUpper(_name_136);
                      _builder.append(_firstUpper_41, "\t\t");
                      _builder.append(" = ");
                      String _typeName_22 = this._cppExtensions.toTypeName(feature_11);
                      _builder.append(_typeName_22, "\t\t");
                      _builder.append("::NO_VALUE;");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("}");
                      _builder.newLine();
                    } else {
                      boolean _isTypeOfDates_1 = this._cppExtensions.isTypeOfDates(feature_11);
                      if (_isTypeOfDates_1) {
                        _builder.append("\t");
                        _builder.append("if (");
                        String _name_137 = this._cppExtensions.toName(dto);
                        String _firstLower_58 = StringExtensions.toFirstLower(_name_137);
                        _builder.append(_firstLower_58, "\t");
                        _builder.append("Map.contains(");
                        String _name_138 = this._cppExtensions.toName(feature_11);
                        String _firstLower_59 = StringExtensions.toFirstLower(_name_138);
                        _builder.append(_firstLower_59, "\t");
                        _builder.append("ForeignKey)) {");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("// always getting the Date as a String (from server or JSON)");
                        _builder.newLine();
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("QString ");
                        String _name_139 = this._cppExtensions.toName(feature_11);
                        String _firstLower_60 = StringExtensions.toFirstLower(_name_139);
                        _builder.append(_firstLower_60, "\t\t");
                        _builder.append("AsString = ");
                        String _name_140 = this._cppExtensions.toName(dto);
                        String _firstLower_61 = StringExtensions.toFirstLower(_name_140);
                        _builder.append(_firstLower_61, "\t\t");
                        _builder.append("Map.value(");
                        String _name_141 = this._cppExtensions.toName(feature_11);
                        String _firstLower_62 = StringExtensions.toFirstLower(_name_141);
                        _builder.append(_firstLower_62, "\t\t");
                        _builder.append("ForeignKey).toString();");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("m");
                        String _name_142 = this._cppExtensions.toName(feature_11);
                        String _firstUpper_42 = StringExtensions.toFirstUpper(_name_142);
                        _builder.append(_firstUpper_42, "\t\t");
                        _builder.append(" = ");
                        String _typeName_23 = this._cppExtensions.toTypeName(feature_11);
                        _builder.append(_typeName_23, "\t\t");
                        _builder.append("::fromString(");
                        String _name_143 = this._cppExtensions.toName(feature_11);
                        String _firstLower_63 = StringExtensions.toFirstLower(_name_143);
                        _builder.append(_firstLower_63, "\t\t");
                        _builder.append("AsString, ");
                        String _dateFormatString_1 = this._cppExtensions.toDateFormatString(feature_11);
                        _builder.append(_dateFormatString_1, "\t\t");
                        _builder.append(");");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("if (!m");
                        String _name_144 = this._cppExtensions.toName(feature_11);
                        String _firstUpper_43 = StringExtensions.toFirstUpper(_name_144);
                        _builder.append(_firstUpper_43, "\t\t");
                        _builder.append(".isValid()) {");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("\t\t");
                        _builder.append("m");
                        String _name_145 = this._cppExtensions.toName(feature_11);
                        String _firstUpper_44 = StringExtensions.toFirstUpper(_name_145);
                        _builder.append(_firstUpper_44, "\t\t\t");
                        _builder.append(" = ");
                        String _typeName_24 = this._cppExtensions.toTypeName(feature_11);
                        _builder.append(_typeName_24, "\t\t\t");
                        _builder.append("();");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("\t\t");
                        _builder.append("qDebug() << \"m");
                        String _name_146 = this._cppExtensions.toName(feature_11);
                        String _firstUpper_45 = StringExtensions.toFirstUpper(_name_146);
                        _builder.append(_firstUpper_45, "\t\t\t");
                        _builder.append(" is not valid for String: \" << ");
                        String _name_147 = this._cppExtensions.toName(feature_11);
                        String _firstLower_64 = StringExtensions.toFirstLower(_name_147);
                        _builder.append(_firstLower_64, "\t\t\t");
                        _builder.append("AsString;");
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
                        _builder.append("m");
                        String _name_148 = this._cppExtensions.toName(feature_11);
                        String _firstUpper_46 = StringExtensions.toFirstUpper(_name_148);
                        _builder.append(_firstUpper_46, "\t");
                        _builder.append(" = ");
                        String _name_149 = this._cppExtensions.toName(dto);
                        String _firstLower_65 = StringExtensions.toFirstLower(_name_149);
                        _builder.append(_firstLower_65, "\t");
                        _builder.append("Map.value(");
                        String _name_150 = this._cppExtensions.toName(feature_11);
                        _builder.append(_name_150, "\t");
                        _builder.append("ForeignKey).to");
                        String _mapToType_3 = this._cppExtensions.mapToType(feature_11);
                        _builder.append(_mapToType_3, "\t");
                        _builder.append("();");
                        _builder.newLineIfNotEmpty();
                      }
                    }
                  }
                }
                {
                  String _name_151 = this._cppExtensions.toName(feature_11);
                  boolean _equals_2 = Objects.equal(_name_151, "uuid");
                  if (_equals_2) {
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
          List<? extends LFeature> _allFeatures_12 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_10 = new Function1<LFeature, Boolean>() {
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
          Iterable<? extends LFeature> _filter_10 = IterableExtensions.filter(_allFeatures_12, _function_10);
          for(final LFeature feature_12 : _filter_10) {
            _builder.append("\t");
            _builder.append("// m");
            String _name_152 = this._cppExtensions.toName(feature_12);
            String _firstUpper_47 = StringExtensions.toFirstUpper(_name_152);
            _builder.append(_firstUpper_47, "\t");
            _builder.append(" is List of ");
            String _typeName_25 = this._cppExtensions.toTypeName(feature_12);
            _builder.append(_typeName_25, "\t");
            _builder.append("*");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("QVariantList ");
            String _name_153 = this._cppExtensions.toName(feature_12);
            String _firstLower_66 = StringExtensions.toFirstLower(_name_153);
            _builder.append(_firstLower_66, "\t");
            _builder.append("List;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            String _name_154 = this._cppExtensions.toName(feature_12);
            String _firstLower_67 = StringExtensions.toFirstLower(_name_154);
            _builder.append(_firstLower_67, "\t");
            _builder.append("List = ");
            String _name_155 = this._cppExtensions.toName(dto);
            String _firstLower_68 = StringExtensions.toFirstLower(_name_155);
            _builder.append(_firstLower_68, "\t");
            _builder.append("Map.value(");
            String _name_156 = this._cppExtensions.toName(feature_12);
            String _firstLower_69 = StringExtensions.toFirstLower(_name_156);
            _builder.append(_firstLower_69, "\t");
            _builder.append("ForeignKey).toList();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("m");
            String _name_157 = this._cppExtensions.toName(feature_12);
            String _firstUpper_48 = StringExtensions.toFirstUpper(_name_157);
            _builder.append(_firstUpper_48, "\t");
            _builder.append(".clear();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("for (int i = 0; i < ");
            String _name_158 = this._cppExtensions.toName(feature_12);
            String _firstLower_70 = StringExtensions.toFirstLower(_name_158);
            _builder.append(_firstLower_70, "\t");
            _builder.append("List.size(); ++i) {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("QVariantMap ");
            String _name_159 = this._cppExtensions.toName(feature_12);
            String _firstLower_71 = StringExtensions.toFirstLower(_name_159);
            _builder.append(_firstLower_71, "\t\t");
            _builder.append("Map;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            String _name_160 = this._cppExtensions.toName(feature_12);
            String _firstLower_72 = StringExtensions.toFirstLower(_name_160);
            _builder.append(_firstLower_72, "\t\t");
            _builder.append("Map = ");
            String _name_161 = this._cppExtensions.toName(feature_12);
            String _firstLower_73 = StringExtensions.toFirstLower(_name_161);
            _builder.append(_firstLower_73, "\t\t");
            _builder.append("List.at(i).toMap();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            String _typeName_26 = this._cppExtensions.toTypeName(feature_12);
            _builder.append(_typeName_26, "\t\t");
            _builder.append("* ");
            String _typeName_27 = this._cppExtensions.toTypeName(feature_12);
            String _firstLower_74 = StringExtensions.toFirstLower(_typeName_27);
            _builder.append(_firstLower_74, "\t\t");
            _builder.append(" = new ");
            String _typeName_28 = this._cppExtensions.toTypeName(feature_12);
            _builder.append(_typeName_28, "\t\t");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            String _typeName_29 = this._cppExtensions.toTypeName(feature_12);
            String _firstLower_75 = StringExtensions.toFirstLower(_typeName_29);
            _builder.append(_firstLower_75, "\t\t");
            _builder.append("->setParent(this);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            String _typeName_30 = this._cppExtensions.toTypeName(feature_12);
            String _firstLower_76 = StringExtensions.toFirstLower(_typeName_30);
            _builder.append(_firstLower_76, "\t\t");
            _builder.append("->fillFromMap(");
            String _name_162 = this._cppExtensions.toName(feature_12);
            String _firstLower_77 = StringExtensions.toFirstLower(_name_162);
            _builder.append(_firstLower_77, "\t\t");
            _builder.append("Map);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("m");
            String _name_163 = this._cppExtensions.toName(feature_12);
            String _firstUpper_49 = StringExtensions.toFirstUpper(_name_163);
            _builder.append(_firstUpper_49, "\t\t");
            _builder.append(".append(");
            String _typeName_31 = this._cppExtensions.toTypeName(feature_12);
            String _firstLower_78 = StringExtensions.toFirstLower(_typeName_31);
            _builder.append(_firstLower_78, "\t\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          }
        }
        {
          List<? extends LFeature> _allFeatures_13 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_11 = new Function1<LFeature, Boolean>() {
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
          Iterable<? extends LFeature> _filter_11 = IterableExtensions.filter(_allFeatures_13, _function_11);
          for(final LFeature feature_13 : _filter_11) {
            {
              String _typeName_32 = this._cppExtensions.toTypeName(feature_13);
              boolean _equals_3 = Objects.equal(_typeName_32, "QString");
              if (_equals_3) {
                _builder.append("\t");
                _builder.append("m");
                String _name_164 = this._cppExtensions.toName(feature_13);
                String _firstUpper_50 = StringExtensions.toFirstUpper(_name_164);
                _builder.append(_firstUpper_50, "\t");
                _builder.append("StringList = ");
                String _name_165 = this._cppExtensions.toName(dto);
                String _firstLower_79 = StringExtensions.toFirstLower(_name_165);
                _builder.append(_firstLower_79, "\t");
                _builder.append("Map.value(");
                String _name_166 = this._cppExtensions.toName(feature_13);
                String _firstLower_80 = StringExtensions.toFirstLower(_name_166);
                _builder.append(_firstLower_80, "\t");
                _builder.append("ForeignKey).toStringList();");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("// m");
                String _name_167 = this._cppExtensions.toName(feature_13);
                String _firstUpper_51 = StringExtensions.toFirstUpper(_name_167);
                _builder.append(_firstUpper_51, "\t");
                _builder.append(" is Array of ");
                String _typeName_33 = this._cppExtensions.toTypeName(feature_13);
                _builder.append(_typeName_33, "\t");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("QVariantList ");
                String _name_168 = this._cppExtensions.toName(feature_13);
                _builder.append(_name_168, "\t");
                _builder.append("List;");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                String _name_169 = this._cppExtensions.toName(feature_13);
                _builder.append(_name_169, "\t");
                _builder.append("List = ");
                String _name_170 = this._cppExtensions.toName(dto);
                String _firstLower_81 = StringExtensions.toFirstLower(_name_170);
                _builder.append(_firstLower_81, "\t");
                _builder.append("Map.value(");
                String _name_171 = this._cppExtensions.toName(feature_13);
                _builder.append(_name_171, "\t");
                _builder.append("ForeignKey).toList();");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("m");
                String _name_172 = this._cppExtensions.toName(feature_13);
                String _firstUpper_52 = StringExtensions.toFirstUpper(_name_172);
                _builder.append(_firstUpper_52, "\t");
                _builder.append(".clear();");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("for (int i = 0; i < ");
                String _name_173 = this._cppExtensions.toName(feature_13);
                _builder.append(_name_173, "\t");
                _builder.append("List.size(); ++i) {");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("m");
                String _name_174 = this._cppExtensions.toName(feature_13);
                String _firstUpper_53 = StringExtensions.toFirstUpper(_name_174);
                _builder.append(_firstUpper_53, "\t\t");
                _builder.append(".append(");
                String _name_175 = this._cppExtensions.toName(feature_13);
                _builder.append(_name_175, "\t\t");
                _builder.append("List.at(i).to");
                String _mapToSingleType_1 = this._cppExtensions.mapToSingleType(feature_13);
                _builder.append(_mapToSingleType_1, "\t\t");
                _builder.append("());");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("}");
                _builder.newLine();
              }
            }
          }
        }
      } else {
        _builder.append("\t");
        _builder.append("// no Foreign Properties found in data model");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("// use default method");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("fillFromMap(");
        String _name_176 = this._cppExtensions.toName(dto);
        String _firstLower_82 = StringExtensions.toFirstLower(_name_176);
        _builder.append(_firstLower_82, "\t");
        _builder.append("Map);");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* initialize OrderData from QVariantMap");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Map got from JsonDataAccess or so");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* excludes transient values");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* uses own property names");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* corresponding export method: toCacheMap()");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("void ");
    String _name_177 = this._cppExtensions.toName(dto);
    _builder.append(_name_177, "");
    _builder.append("::fillFromCacheMap(const QVariantMap& ");
    String _name_178 = this._cppExtensions.toName(dto);
    String _firstLower_83 = StringExtensions.toFirstLower(_name_178);
    _builder.append(_firstLower_83, "");
    _builder.append("Map)");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    {
      boolean _existsTransient_1 = this._cppExtensions.existsTransient(dto);
      if (_existsTransient_1) {
        {
          List<? extends LFeature> _allFeatures_14 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_12 = new Function1<LFeature, Boolean>() {
            public Boolean apply(final LFeature it) {
              boolean _isToMany = CppGenerator.this._cppExtensions.isToMany(it);
              return Boolean.valueOf((!_isToMany));
            }
          };
          Iterable<? extends LFeature> _filter_12 = IterableExtensions.filter(_allFeatures_14, _function_12);
          for(final LFeature feature_14 : _filter_12) {
            {
              boolean _isTypeOfDataObject_4 = this._cppExtensions.isTypeOfDataObject(feature_14);
              if (_isTypeOfDataObject_4) {
                {
                  boolean _isContained_5 = this._cppExtensions.isContained(feature_14);
                  if (_isContained_5) {
                    _builder.append("\t");
                    _builder.append("// m");
                    String _name_179 = this._cppExtensions.toName(feature_14);
                    String _firstUpper_54 = StringExtensions.toFirstUpper(_name_179);
                    _builder.append(_firstUpper_54, "\t");
                    _builder.append(" is parent (");
                    String _typeName_34 = this._cppExtensions.toTypeName(feature_14);
                    _builder.append(_typeName_34, "\t");
                    _builder.append("* containing ");
                    String _name_180 = this._cppExtensions.toName(dto);
                    _builder.append(_name_180, "\t");
                    _builder.append(")");
                    _builder.newLineIfNotEmpty();
                  } else {
                    boolean _isLazy_2 = this._cppExtensions.isLazy(feature_14);
                    if (_isLazy_2) {
                      _builder.append("\t");
                      _builder.append("// ");
                      String _name_181 = this._cppExtensions.toName(feature_14);
                      _builder.append(_name_181, "\t");
                      _builder.append(" lazy pointing to ");
                      String _typeOrQObject_2 = this._cppExtensions.toTypeOrQObject(feature_14);
                      _builder.append(_typeOrQObject_2, "\t");
                      _builder.append(" (domainKey: ");
                      String _referenceDomainKey_2 = this._cppExtensions.referenceDomainKey(feature_14);
                      _builder.append(_referenceDomainKey_2, "\t");
                      _builder.append(")");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("if (");
                      String _name_182 = this._cppExtensions.toName(dto);
                      String _firstLower_84 = StringExtensions.toFirstLower(_name_182);
                      _builder.append(_firstLower_84, "\t");
                      _builder.append("Map.contains(");
                      String _name_183 = this._cppExtensions.toName(feature_14);
                      _builder.append(_name_183, "\t");
                      _builder.append("Key)) {");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t");
                      _builder.append("m");
                      String _name_184 = this._cppExtensions.toName(feature_14);
                      String _firstUpper_55 = StringExtensions.toFirstUpper(_name_184);
                      _builder.append(_firstUpper_55, "\t\t");
                      _builder.append(" = ");
                      String _name_185 = this._cppExtensions.toName(dto);
                      String _firstLower_85 = StringExtensions.toFirstLower(_name_185);
                      _builder.append(_firstLower_85, "\t\t");
                      _builder.append("Map.value(");
                      String _name_186 = this._cppExtensions.toName(feature_14);
                      _builder.append(_name_186, "\t\t");
                      _builder.append("Key).to");
                      String _referenceDomainKeyType_5 = this._cppExtensions.referenceDomainKeyType(feature_14);
                      String _mapToLazyTypeName_2 = this._cppExtensions.mapToLazyTypeName(_referenceDomainKeyType_5);
                      _builder.append(_mapToLazyTypeName_2, "\t\t");
                      _builder.append("();");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t");
                      _builder.append("if (m");
                      String _name_187 = this._cppExtensions.toName(feature_14);
                      String _firstUpper_56 = StringExtensions.toFirstUpper(_name_187);
                      _builder.append(_firstUpper_56, "\t\t");
                      _builder.append(" != ");
                      String _referenceDomainKeyType_6 = this._cppExtensions.referenceDomainKeyType(feature_14);
                      String _defaultForLazyTypeName_3 = this._cppExtensions.defaultForLazyTypeName(_referenceDomainKeyType_6);
                      _builder.append(_defaultForLazyTypeName_3, "\t\t");
                      _builder.append(") {");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t\t");
                      _builder.append("// resolve the corresponding Data Object on demand from DataManager");
                      _builder.newLine();
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
                      String _name_188 = this._cppExtensions.toName(feature_14);
                      String _firstUpper_57 = StringExtensions.toFirstUpper(_name_188);
                      _builder.append(_firstUpper_57, "\t");
                      _builder.append(" points to ");
                      String _typeName_35 = this._cppExtensions.toTypeName(feature_14);
                      _builder.append(_typeName_35, "\t");
                      _builder.append("*");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("if (");
                      String _name_189 = this._cppExtensions.toName(dto);
                      String _firstLower_86 = StringExtensions.toFirstLower(_name_189);
                      _builder.append(_firstLower_86, "\t");
                      _builder.append("Map.contains(");
                      String _name_190 = this._cppExtensions.toName(feature_14);
                      _builder.append(_name_190, "\t");
                      _builder.append("Key)) {");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t");
                      _builder.append("QVariantMap ");
                      String _name_191 = this._cppExtensions.toName(feature_14);
                      _builder.append(_name_191, "\t\t");
                      _builder.append("Map;");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t");
                      String _name_192 = this._cppExtensions.toName(feature_14);
                      _builder.append(_name_192, "\t\t");
                      _builder.append("Map = ");
                      String _name_193 = this._cppExtensions.toName(dto);
                      String _firstLower_87 = StringExtensions.toFirstLower(_name_193);
                      _builder.append(_firstLower_87, "\t\t");
                      _builder.append("Map.value(");
                      String _name_194 = this._cppExtensions.toName(feature_14);
                      _builder.append(_name_194, "\t\t");
                      _builder.append("Key).toMap();");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t");
                      _builder.append("if (!");
                      String _name_195 = this._cppExtensions.toName(feature_14);
                      _builder.append(_name_195, "\t\t");
                      _builder.append("Map.isEmpty()) {");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t\t");
                      _builder.append("m");
                      String _name_196 = this._cppExtensions.toName(feature_14);
                      String _firstUpper_58 = StringExtensions.toFirstUpper(_name_196);
                      _builder.append(_firstUpper_58, "\t\t\t");
                      _builder.append(" = new ");
                      String _typeName_36 = this._cppExtensions.toTypeName(feature_14);
                      _builder.append(_typeName_36, "\t\t\t");
                      _builder.append("();");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t\t");
                      _builder.append("m");
                      String _name_197 = this._cppExtensions.toName(feature_14);
                      String _firstUpper_59 = StringExtensions.toFirstUpper(_name_197);
                      _builder.append(_firstUpper_59, "\t\t\t");
                      _builder.append("->setParent(this);");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t\t");
                      _builder.append("m");
                      String _name_198 = this._cppExtensions.toName(feature_14);
                      String _firstUpper_60 = StringExtensions.toFirstUpper(_name_198);
                      _builder.append(_firstUpper_60, "\t\t\t");
                      _builder.append("->fillFromMap(");
                      String _name_199 = this._cppExtensions.toName(feature_14);
                      _builder.append(_name_199, "\t\t\t");
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
                  boolean _isTransient_2 = this._cppExtensions.isTransient(feature_14);
                  if (_isTransient_2) {
                    _builder.append("\t");
                    _builder.append("// m");
                    String _name_200 = this._cppExtensions.toName(feature_14);
                    String _firstUpper_61 = StringExtensions.toFirstUpper(_name_200);
                    _builder.append(_firstUpper_61, "\t");
                    _builder.append(" is transient - don\'t forget to initialize");
                    _builder.newLineIfNotEmpty();
                  } else {
                    boolean _isEnum_2 = this._cppExtensions.isEnum(feature_14);
                    if (_isEnum_2) {
                      _builder.append("\t");
                      _builder.append("// ENUM");
                      _builder.newLine();
                      _builder.append("\t");
                      _builder.append("if (");
                      String _name_201 = this._cppExtensions.toName(dto);
                      String _firstLower_88 = StringExtensions.toFirstLower(_name_201);
                      _builder.append(_firstLower_88, "\t");
                      _builder.append("Map.contains(");
                      String _name_202 = this._cppExtensions.toName(feature_14);
                      String _firstLower_89 = StringExtensions.toFirstLower(_name_202);
                      _builder.append(_firstLower_89, "\t");
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
                      String _name_203 = this._cppExtensions.toName(dto);
                      String _firstLower_90 = StringExtensions.toFirstLower(_name_203);
                      _builder.append(_firstLower_90, "\t\t");
                      _builder.append("Map.value(");
                      String _name_204 = this._cppExtensions.toName(feature_14);
                      String _firstLower_91 = StringExtensions.toFirstLower(_name_204);
                      _builder.append(_firstLower_91, "\t\t");
                      _builder.append("Key).toInt(ok);");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t");
                      _builder.append("if (ok) {");
                      _builder.newLine();
                      _builder.append("\t");
                      _builder.append("\t\t");
                      _builder.append("m");
                      String _name_205 = this._cppExtensions.toName(feature_14);
                      String _firstUpper_62 = StringExtensions.toFirstUpper(_name_205);
                      _builder.append(_firstUpper_62, "\t\t\t");
                      _builder.append(" = ");
                      String _name_206 = this._cppExtensions.toName(dto);
                      String _firstLower_92 = StringExtensions.toFirstLower(_name_206);
                      _builder.append(_firstLower_92, "\t\t\t");
                      _builder.append("Map.value(");
                      String _name_207 = this._cppExtensions.toName(feature_14);
                      String _firstLower_93 = StringExtensions.toFirstLower(_name_207);
                      _builder.append(_firstLower_93, "\t\t\t");
                      _builder.append("Key).toInt();");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t");
                      _builder.append("} else {");
                      _builder.newLine();
                      _builder.append("\t");
                      _builder.append("\t\t");
                      _builder.append("m");
                      String _name_208 = this._cppExtensions.toName(feature_14);
                      String _firstUpper_63 = StringExtensions.toFirstUpper(_name_208);
                      _builder.append(_firstUpper_63, "\t\t\t");
                      _builder.append(" = ");
                      String _name_209 = this._cppExtensions.toName(feature_14);
                      String _firstLower_94 = StringExtensions.toFirstLower(_name_209);
                      _builder.append(_firstLower_94, "\t\t\t");
                      _builder.append("StringToInt(");
                      String _name_210 = this._cppExtensions.toName(dto);
                      String _firstLower_95 = StringExtensions.toFirstLower(_name_210);
                      _builder.append(_firstLower_95, "\t\t\t");
                      _builder.append("Map.value(");
                      String _name_211 = this._cppExtensions.toName(feature_14);
                      String _firstLower_96 = StringExtensions.toFirstLower(_name_211);
                      _builder.append(_firstLower_96, "\t\t\t");
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
                      String _name_212 = this._cppExtensions.toName(feature_14);
                      String _firstUpper_64 = StringExtensions.toFirstUpper(_name_212);
                      _builder.append(_firstUpper_64, "\t\t");
                      _builder.append(" = ");
                      String _typeName_37 = this._cppExtensions.toTypeName(feature_14);
                      _builder.append(_typeName_37, "\t\t");
                      _builder.append("::NO_VALUE;");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("}");
                      _builder.newLine();
                    } else {
                      boolean _isTypeOfDates_2 = this._cppExtensions.isTypeOfDates(feature_14);
                      if (_isTypeOfDates_2) {
                        _builder.append("\t");
                        _builder.append("if (");
                        String _name_213 = this._cppExtensions.toName(dto);
                        String _firstLower_97 = StringExtensions.toFirstLower(_name_213);
                        _builder.append(_firstLower_97, "\t");
                        _builder.append("Map.contains(");
                        String _name_214 = this._cppExtensions.toName(feature_14);
                        String _firstLower_98 = StringExtensions.toFirstLower(_name_214);
                        _builder.append(_firstLower_98, "\t");
                        _builder.append("Key)) {");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("// always getting the Date as a String (from server or JSON)");
                        _builder.newLine();
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("QString ");
                        String _name_215 = this._cppExtensions.toName(feature_14);
                        String _firstLower_99 = StringExtensions.toFirstLower(_name_215);
                        _builder.append(_firstLower_99, "\t\t");
                        _builder.append("AsString = ");
                        String _name_216 = this._cppExtensions.toName(dto);
                        String _firstLower_100 = StringExtensions.toFirstLower(_name_216);
                        _builder.append(_firstLower_100, "\t\t");
                        _builder.append("Map.value(");
                        String _name_217 = this._cppExtensions.toName(feature_14);
                        String _firstLower_101 = StringExtensions.toFirstLower(_name_217);
                        _builder.append(_firstLower_101, "\t\t");
                        _builder.append("Key).toString();");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("m");
                        String _name_218 = this._cppExtensions.toName(feature_14);
                        String _firstUpper_65 = StringExtensions.toFirstUpper(_name_218);
                        _builder.append(_firstUpper_65, "\t\t");
                        _builder.append(" = ");
                        String _typeName_38 = this._cppExtensions.toTypeName(feature_14);
                        _builder.append(_typeName_38, "\t\t");
                        _builder.append("::fromString(");
                        String _name_219 = this._cppExtensions.toName(feature_14);
                        String _firstLower_102 = StringExtensions.toFirstLower(_name_219);
                        _builder.append(_firstLower_102, "\t\t");
                        _builder.append("AsString, ");
                        String _dateFormatString_2 = this._cppExtensions.toDateFormatString(feature_14);
                        _builder.append(_dateFormatString_2, "\t\t");
                        _builder.append(");");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("if (!m");
                        String _name_220 = this._cppExtensions.toName(feature_14);
                        String _firstUpper_66 = StringExtensions.toFirstUpper(_name_220);
                        _builder.append(_firstUpper_66, "\t\t");
                        _builder.append(".isValid()) {");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("\t\t");
                        _builder.append("m");
                        String _name_221 = this._cppExtensions.toName(feature_14);
                        String _firstUpper_67 = StringExtensions.toFirstUpper(_name_221);
                        _builder.append(_firstUpper_67, "\t\t\t");
                        _builder.append(" = ");
                        String _typeName_39 = this._cppExtensions.toTypeName(feature_14);
                        _builder.append(_typeName_39, "\t\t\t");
                        _builder.append("();");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("\t\t");
                        _builder.append("qDebug() << \"m");
                        String _name_222 = this._cppExtensions.toName(feature_14);
                        String _firstUpper_68 = StringExtensions.toFirstUpper(_name_222);
                        _builder.append(_firstUpper_68, "\t\t\t");
                        _builder.append(" is not valid for String: \" << ");
                        String _name_223 = this._cppExtensions.toName(feature_14);
                        String _firstLower_103 = StringExtensions.toFirstLower(_name_223);
                        _builder.append(_firstLower_103, "\t\t\t");
                        _builder.append("AsString;");
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
                        _builder.append("m");
                        String _name_224 = this._cppExtensions.toName(feature_14);
                        String _firstUpper_69 = StringExtensions.toFirstUpper(_name_224);
                        _builder.append(_firstUpper_69, "\t");
                        _builder.append(" = ");
                        String _name_225 = this._cppExtensions.toName(dto);
                        String _firstLower_104 = StringExtensions.toFirstLower(_name_225);
                        _builder.append(_firstLower_104, "\t");
                        _builder.append("Map.value(");
                        String _name_226 = this._cppExtensions.toName(feature_14);
                        _builder.append(_name_226, "\t");
                        _builder.append("Key).to");
                        String _mapToType_4 = this._cppExtensions.mapToType(feature_14);
                        _builder.append(_mapToType_4, "\t");
                        _builder.append("();");
                        _builder.newLineIfNotEmpty();
                      }
                    }
                  }
                }
                {
                  String _name_227 = this._cppExtensions.toName(feature_14);
                  boolean _equals_4 = Objects.equal(_name_227, "uuid");
                  if (_equals_4) {
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
          List<? extends LFeature> _allFeatures_15 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_13 = new Function1<LFeature, Boolean>() {
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
          Iterable<? extends LFeature> _filter_13 = IterableExtensions.filter(_allFeatures_15, _function_13);
          for(final LFeature feature_15 : _filter_13) {
            _builder.append("\t");
            _builder.append("// m");
            String _name_228 = this._cppExtensions.toName(feature_15);
            String _firstUpper_70 = StringExtensions.toFirstUpper(_name_228);
            _builder.append(_firstUpper_70, "\t");
            _builder.append(" is List of ");
            String _typeName_40 = this._cppExtensions.toTypeName(feature_15);
            _builder.append(_typeName_40, "\t");
            _builder.append("*");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("QVariantList ");
            String _name_229 = this._cppExtensions.toName(feature_15);
            String _firstLower_105 = StringExtensions.toFirstLower(_name_229);
            _builder.append(_firstLower_105, "\t");
            _builder.append("List;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            String _name_230 = this._cppExtensions.toName(feature_15);
            String _firstLower_106 = StringExtensions.toFirstLower(_name_230);
            _builder.append(_firstLower_106, "\t");
            _builder.append("List = ");
            String _name_231 = this._cppExtensions.toName(dto);
            String _firstLower_107 = StringExtensions.toFirstLower(_name_231);
            _builder.append(_firstLower_107, "\t");
            _builder.append("Map.value(");
            String _name_232 = this._cppExtensions.toName(feature_15);
            String _firstLower_108 = StringExtensions.toFirstLower(_name_232);
            _builder.append(_firstLower_108, "\t");
            _builder.append("Key).toList();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("m");
            String _name_233 = this._cppExtensions.toName(feature_15);
            String _firstUpper_71 = StringExtensions.toFirstUpper(_name_233);
            _builder.append(_firstUpper_71, "\t");
            _builder.append(".clear();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("for (int i = 0; i < ");
            String _name_234 = this._cppExtensions.toName(feature_15);
            String _firstLower_109 = StringExtensions.toFirstLower(_name_234);
            _builder.append(_firstLower_109, "\t");
            _builder.append("List.size(); ++i) {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("QVariantMap ");
            String _name_235 = this._cppExtensions.toName(feature_15);
            String _firstLower_110 = StringExtensions.toFirstLower(_name_235);
            _builder.append(_firstLower_110, "\t\t");
            _builder.append("Map;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            String _name_236 = this._cppExtensions.toName(feature_15);
            String _firstLower_111 = StringExtensions.toFirstLower(_name_236);
            _builder.append(_firstLower_111, "\t\t");
            _builder.append("Map = ");
            String _name_237 = this._cppExtensions.toName(feature_15);
            String _firstLower_112 = StringExtensions.toFirstLower(_name_237);
            _builder.append(_firstLower_112, "\t\t");
            _builder.append("List.at(i).toMap();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            String _typeName_41 = this._cppExtensions.toTypeName(feature_15);
            _builder.append(_typeName_41, "\t\t");
            _builder.append("* ");
            String _typeName_42 = this._cppExtensions.toTypeName(feature_15);
            String _firstLower_113 = StringExtensions.toFirstLower(_typeName_42);
            _builder.append(_firstLower_113, "\t\t");
            _builder.append(" = new ");
            String _typeName_43 = this._cppExtensions.toTypeName(feature_15);
            _builder.append(_typeName_43, "\t\t");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            String _typeName_44 = this._cppExtensions.toTypeName(feature_15);
            String _firstLower_114 = StringExtensions.toFirstLower(_typeName_44);
            _builder.append(_firstLower_114, "\t\t");
            _builder.append("->setParent(this);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            String _typeName_45 = this._cppExtensions.toTypeName(feature_15);
            String _firstLower_115 = StringExtensions.toFirstLower(_typeName_45);
            _builder.append(_firstLower_115, "\t\t");
            _builder.append("->fillFromMap(");
            String _name_238 = this._cppExtensions.toName(feature_15);
            String _firstLower_116 = StringExtensions.toFirstLower(_name_238);
            _builder.append(_firstLower_116, "\t\t");
            _builder.append("Map);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("m");
            String _name_239 = this._cppExtensions.toName(feature_15);
            String _firstUpper_72 = StringExtensions.toFirstUpper(_name_239);
            _builder.append(_firstUpper_72, "\t\t");
            _builder.append(".append(");
            String _typeName_46 = this._cppExtensions.toTypeName(feature_15);
            String _firstLower_117 = StringExtensions.toFirstLower(_typeName_46);
            _builder.append(_firstLower_117, "\t\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          }
        }
        {
          List<? extends LFeature> _allFeatures_16 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_14 = new Function1<LFeature, Boolean>() {
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
          Iterable<? extends LFeature> _filter_14 = IterableExtensions.filter(_allFeatures_16, _function_14);
          for(final LFeature feature_16 : _filter_14) {
            {
              String _typeName_47 = this._cppExtensions.toTypeName(feature_16);
              boolean _equals_5 = Objects.equal(_typeName_47, "QString");
              if (_equals_5) {
                _builder.append("\t");
                _builder.append("m");
                String _name_240 = this._cppExtensions.toName(feature_16);
                String _firstUpper_73 = StringExtensions.toFirstUpper(_name_240);
                _builder.append(_firstUpper_73, "\t");
                _builder.append("StringList = ");
                String _name_241 = this._cppExtensions.toName(dto);
                String _firstLower_118 = StringExtensions.toFirstLower(_name_241);
                _builder.append(_firstLower_118, "\t");
                _builder.append("Map.value(");
                String _name_242 = this._cppExtensions.toName(feature_16);
                String _firstLower_119 = StringExtensions.toFirstLower(_name_242);
                _builder.append(_firstLower_119, "\t");
                _builder.append("Key).toStringList();");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("// m");
                String _name_243 = this._cppExtensions.toName(feature_16);
                String _firstUpper_74 = StringExtensions.toFirstUpper(_name_243);
                _builder.append(_firstUpper_74, "\t");
                _builder.append(" is Array of ");
                String _typeName_48 = this._cppExtensions.toTypeName(feature_16);
                _builder.append(_typeName_48, "\t");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("QVariantList ");
                String _name_244 = this._cppExtensions.toName(feature_16);
                _builder.append(_name_244, "\t");
                _builder.append("List;");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                String _name_245 = this._cppExtensions.toName(feature_16);
                _builder.append(_name_245, "\t");
                _builder.append("List = ");
                String _name_246 = this._cppExtensions.toName(dto);
                String _firstLower_120 = StringExtensions.toFirstLower(_name_246);
                _builder.append(_firstLower_120, "\t");
                _builder.append("Map.value(");
                String _name_247 = this._cppExtensions.toName(feature_16);
                _builder.append(_name_247, "\t");
                _builder.append("Key).toList();");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("m");
                String _name_248 = this._cppExtensions.toName(feature_16);
                String _firstUpper_75 = StringExtensions.toFirstUpper(_name_248);
                _builder.append(_firstUpper_75, "\t");
                _builder.append(".clear();");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("for (int i = 0; i < ");
                String _name_249 = this._cppExtensions.toName(feature_16);
                _builder.append(_name_249, "\t");
                _builder.append("List.size(); ++i) {");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("m");
                String _name_250 = this._cppExtensions.toName(feature_16);
                String _firstUpper_76 = StringExtensions.toFirstUpper(_name_250);
                _builder.append(_firstUpper_76, "\t\t");
                _builder.append(".append(");
                String _name_251 = this._cppExtensions.toName(feature_16);
                _builder.append(_name_251, "\t\t");
                _builder.append("List.at(i).to");
                String _mapToSingleType_2 = this._cppExtensions.mapToSingleType(feature_16);
                _builder.append(_mapToSingleType_2, "\t\t");
                _builder.append("());");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("}");
                _builder.newLine();
              }
            }
          }
        }
      } else {
        _builder.append("\t");
        _builder.append("// no Transient Properties found in data model");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("// use default method for cache");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("fillFromMap(");
        String _name_252 = this._cppExtensions.toName(dto);
        String _firstLower_121 = StringExtensions.toFirstLower(_name_252);
        _builder.append(_firstLower_121, "\t");
        _builder.append("Map);");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("void ");
    String _name_253 = this._cppExtensions.toName(dto);
    _builder.append(_name_253, "");
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
    String _name_254 = this._cppExtensions.toName(dto);
    _builder.append(_name_254, "");
    _builder.append("::isValid()");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_17 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_15 = new Function1<LFeature, Boolean>() {
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
      Iterable<? extends LFeature> _filter_15 = IterableExtensions.filter(_allFeatures_17, _function_15);
      for(final LFeature feature_17 : _filter_15) {
        {
          boolean _isToMany = this._cppExtensions.isToMany(feature_17);
          if (_isToMany) {
            _builder.append("\t");
            _builder.append("if (m");
            String _name_255 = this._cppExtensions.toName(feature_17);
            String _firstUpper_77 = StringExtensions.toFirstUpper(_name_255);
            _builder.append(_firstUpper_77, "\t");
            _builder.append(".size() == 0) {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("return false;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          } else {
            boolean _isLazy_3 = this._cppExtensions.isLazy(feature_17);
            if (_isLazy_3) {
              _builder.append("\t");
              _builder.append("// ");
              String _name_256 = this._cppExtensions.toName(feature_17);
              _builder.append(_name_256, "\t");
              _builder.append(" lazy pointing to ");
              String _typeOrQObject_3 = this._cppExtensions.toTypeOrQObject(feature_17);
              _builder.append(_typeOrQObject_3, "\t");
              _builder.append(" (domainKey: ");
              String _referenceDomainKey_3 = this._cppExtensions.referenceDomainKey(feature_17);
              _builder.append(_referenceDomainKey_3, "\t");
              _builder.append(")");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              LFeature _referenceDomainKeyFeature = this._cppExtensions.referenceDomainKeyFeature(feature_17);
              String _typeName_49 = this._cppExtensions.toTypeName(_referenceDomainKeyFeature);
              String _name_257 = this._cppExtensions.toName(feature_17);
              String _validateReference = this._cppExtensions.toValidateReference(_typeName_49, _name_257);
              _builder.append(_validateReference, "\t");
              _builder.newLineIfNotEmpty();
            } else {
              boolean _isTypeOfDataObject_5 = this._cppExtensions.isTypeOfDataObject(feature_17);
              if (_isTypeOfDataObject_5) {
                _builder.append("\t");
                _builder.append("if (!m");
                String _name_258 = this._cppExtensions.toName(feature_17);
                String _firstUpper_78 = StringExtensions.toFirstUpper(_name_258);
                _builder.append(_firstUpper_78, "\t");
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
                String _validate = this._cppExtensions.toValidate(feature_17);
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
    String _name_259 = this._cppExtensions.toName(dto);
    _builder.append(_name_259, " ");
    _builder.append(" as QVariantMap");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("* exports ALL data including transient properties");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* To store persistent Data in JsonDataAccess use toCacheMap()");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("QVariantMap ");
    String _name_260 = this._cppExtensions.toName(dto);
    _builder.append(_name_260, "");
    _builder.append("::toMap()");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("QVariantMap ");
    String _name_261 = this._cppExtensions.toName(dto);
    String _firstLower_122 = StringExtensions.toFirstLower(_name_261);
    _builder.append(_firstLower_122, "\t");
    _builder.append("Map;");
    _builder.newLineIfNotEmpty();
    {
      List<? extends LFeature> _allFeatures_18 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_16 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          return Boolean.valueOf(CppGenerator.this._cppExtensions.isLazy(it));
        }
      };
      Iterable<? extends LFeature> _filter_16 = IterableExtensions.filter(_allFeatures_18, _function_16);
      for(final LFeature feature_18 : _filter_16) {
        _builder.append("\t");
        _builder.append("// ");
        String _name_262 = this._cppExtensions.toName(feature_18);
        _builder.append(_name_262, "\t");
        _builder.append(" lazy pointing to ");
        String _typeOrQObject_4 = this._cppExtensions.toTypeOrQObject(feature_18);
        _builder.append(_typeOrQObject_4, "\t");
        _builder.append(" (domainKey: ");
        String _referenceDomainKey_4 = this._cppExtensions.referenceDomainKey(feature_18);
        _builder.append(_referenceDomainKey_4, "\t");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("if (has");
        String _name_263 = this._cppExtensions.toName(feature_18);
        String _firstUpper_79 = StringExtensions.toFirstUpper(_name_263);
        _builder.append(_firstUpper_79, "\t");
        _builder.append("()) {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        String _name_264 = this._cppExtensions.toName(dto);
        String _firstLower_123 = StringExtensions.toFirstLower(_name_264);
        _builder.append(_firstLower_123, "\t\t");
        _builder.append("Map.insert(");
        String _name_265 = this._cppExtensions.toName(feature_18);
        _builder.append(_name_265, "\t\t");
        _builder.append("Key, m");
        String _name_266 = this._cppExtensions.toName(feature_18);
        String _firstUpper_80 = StringExtensions.toFirstUpper(_name_266);
        _builder.append(_firstUpper_80, "\t\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    {
      List<? extends LFeature> _allFeatures_19 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_17 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _isLazy = CppGenerator.this._cppExtensions.isLazy(it);
          return Boolean.valueOf((!_isLazy));
        }
      };
      Iterable<? extends LFeature> _filter_17 = IterableExtensions.filter(_allFeatures_19, _function_17);
      for(final LFeature feature_19 : _filter_17) {
        {
          boolean _isTypeOfDataObject_6 = this._cppExtensions.isTypeOfDataObject(feature_19);
          if (_isTypeOfDataObject_6) {
            {
              boolean _isContained_6 = this._cppExtensions.isContained(feature_19);
              boolean _not = (!_isContained_6);
              if (_not) {
                _builder.append("\t");
                _builder.append("// m");
                String _name_267 = this._cppExtensions.toName(feature_19);
                String _firstUpper_81 = StringExtensions.toFirstUpper(_name_267);
                _builder.append(_firstUpper_81, "\t");
                _builder.append(" points to ");
                String _typeName_50 = this._cppExtensions.toTypeName(feature_19);
                _builder.append(_typeName_50, "\t");
                _builder.append("*");
                _builder.newLineIfNotEmpty();
                {
                  boolean _isToMany_1 = this._cppExtensions.isToMany(feature_19);
                  if (_isToMany_1) {
                    _builder.append("\t");
                    String _name_268 = this._cppExtensions.toName(dto);
                    String _firstLower_124 = StringExtensions.toFirstLower(_name_268);
                    _builder.append(_firstLower_124, "\t");
                    _builder.append("Map.insert(");
                    String _name_269 = this._cppExtensions.toName(feature_19);
                    _builder.append(_name_269, "\t");
                    _builder.append("Key, ");
                    String _name_270 = this._cppExtensions.toName(feature_19);
                    _builder.append(_name_270, "\t");
                    _builder.append("AsQVariantList());");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t");
                    _builder.append("if (m");
                    String _name_271 = this._cppExtensions.toName(feature_19);
                    String _firstUpper_82 = StringExtensions.toFirstUpper(_name_271);
                    _builder.append(_firstUpper_82, "\t");
                    _builder.append(") {");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t");
                    String _name_272 = this._cppExtensions.toName(dto);
                    String _firstLower_125 = StringExtensions.toFirstLower(_name_272);
                    _builder.append(_firstLower_125, "\t\t");
                    _builder.append("Map.insert(");
                    String _name_273 = this._cppExtensions.toName(feature_19);
                    _builder.append(_name_273, "\t\t");
                    _builder.append("Key, m");
                    String _name_274 = this._cppExtensions.toName(feature_19);
                    String _firstUpper_83 = StringExtensions.toFirstUpper(_name_274);
                    _builder.append(_firstUpper_83, "\t\t");
                    _builder.append("->to");
                    String _mapOrList = this._cppExtensions.toMapOrList(feature_19);
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
                String _name_275 = this._cppExtensions.toName(feature_19);
                String _firstUpper_84 = StringExtensions.toFirstUpper(_name_275);
                _builder.append(_firstUpper_84, "\t");
                _builder.append(" points to ");
                String _typeName_51 = this._cppExtensions.toTypeName(feature_19);
                _builder.append(_typeName_51, "\t");
                _builder.append("* containing ");
                String _name_276 = this._cppExtensions.toName(dto);
                _builder.append(_name_276, "\t");
                _builder.newLineIfNotEmpty();
              }
            }
          } else {
            {
              boolean _isArrayList = this._cppExtensions.isArrayList(feature_19);
              if (_isArrayList) {
                _builder.append("\t");
                _builder.append("// Array of ");
                String _typeName_52 = this._cppExtensions.toTypeName(feature_19);
                _builder.append(_typeName_52, "\t");
                _builder.newLineIfNotEmpty();
                {
                  String _typeName_53 = this._cppExtensions.toTypeName(feature_19);
                  boolean _equals_6 = Objects.equal(_typeName_53, "QString");
                  if (_equals_6) {
                    _builder.append("\t");
                    String _name_277 = this._cppExtensions.toName(dto);
                    String _firstLower_126 = StringExtensions.toFirstLower(_name_277);
                    _builder.append(_firstLower_126, "\t");
                    _builder.append("Map.insert(");
                    String _name_278 = this._cppExtensions.toName(feature_19);
                    _builder.append(_name_278, "\t");
                    _builder.append("Key, m");
                    String _name_279 = this._cppExtensions.toName(feature_19);
                    String _firstUpper_85 = StringExtensions.toFirstUpper(_name_279);
                    _builder.append(_firstUpper_85, "\t");
                    _builder.append("StringList);");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t");
                    String _name_280 = this._cppExtensions.toName(dto);
                    String _firstLower_127 = StringExtensions.toFirstLower(_name_280);
                    _builder.append(_firstLower_127, "\t");
                    _builder.append("Map.insert(");
                    String _name_281 = this._cppExtensions.toName(feature_19);
                    _builder.append(_name_281, "\t");
                    _builder.append("Key, ");
                    String _name_282 = this._cppExtensions.toName(feature_19);
                    _builder.append(_name_282, "\t");
                    _builder.append("List());");
                    _builder.newLineIfNotEmpty();
                  }
                }
              } else {
                boolean _isTypeOfDates_3 = this._cppExtensions.isTypeOfDates(feature_19);
                if (_isTypeOfDates_3) {
                  _builder.append("\t");
                  _builder.append("if (has");
                  String _name_283 = this._cppExtensions.toName(feature_19);
                  String _firstUpper_86 = StringExtensions.toFirstUpper(_name_283);
                  _builder.append(_firstUpper_86, "\t");
                  _builder.append("()) {");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  String _name_284 = this._cppExtensions.toName(dto);
                  String _firstLower_128 = StringExtensions.toFirstLower(_name_284);
                  _builder.append(_firstLower_128, "\t\t");
                  _builder.append("Map.insert(");
                  String _name_285 = this._cppExtensions.toName(feature_19);
                  _builder.append(_name_285, "\t\t");
                  _builder.append("Key, m");
                  String _name_286 = this._cppExtensions.toName(feature_19);
                  String _firstUpper_87 = StringExtensions.toFirstUpper(_name_286);
                  _builder.append(_firstUpper_87, "\t\t");
                  _builder.append(".toString(");
                  String _dateFormatString_3 = this._cppExtensions.toDateFormatString(feature_19);
                  _builder.append(_dateFormatString_3, "\t\t");
                  _builder.append("));");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("}");
                  _builder.newLine();
                } else {
                  {
                    boolean _isEnum_3 = this._cppExtensions.isEnum(feature_19);
                    if (_isEnum_3) {
                      _builder.append("\t");
                      _builder.append("// ENUM always as  int");
                      _builder.newLine();
                    }
                  }
                  _builder.append("\t");
                  String _name_287 = this._cppExtensions.toName(dto);
                  String _firstLower_129 = StringExtensions.toFirstLower(_name_287);
                  _builder.append(_firstLower_129, "\t");
                  _builder.append("Map.insert(");
                  String _name_288 = this._cppExtensions.toName(feature_19);
                  _builder.append(_name_288, "\t");
                  _builder.append("Key, m");
                  String _name_289 = this._cppExtensions.toName(feature_19);
                  String _firstUpper_88 = StringExtensions.toFirstUpper(_name_289);
                  _builder.append(_firstUpper_88, "\t");
                  _builder.append(");");
                  _builder.newLineIfNotEmpty();
                }
              }
            }
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("return ");
    String _name_290 = this._cppExtensions.toName(dto);
    String _firstLower_130 = StringExtensions.toFirstLower(_name_290);
    _builder.append(_firstLower_130, "\t");
    _builder.append("Map;");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Exports Properties from ");
    String _name_291 = this._cppExtensions.toName(dto);
    _builder.append(_name_291, " ");
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
    String _name_292 = this._cppExtensions.toName(dto);
    _builder.append(_name_292, "");
    _builder.append("::toForeignMap()");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    {
      boolean _existsForeignPropertyName_2 = this._cppExtensions.existsForeignPropertyName(dto);
      if (_existsForeignPropertyName_2) {
        _builder.append("\t");
        _builder.append("QVariantMap ");
        String _name_293 = this._cppExtensions.toName(dto);
        String _firstLower_131 = StringExtensions.toFirstLower(_name_293);
        _builder.append(_firstLower_131, "\t");
        _builder.append("Map;");
        _builder.newLineIfNotEmpty();
        {
          List<? extends LFeature> _allFeatures_20 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_18 = new Function1<LFeature, Boolean>() {
            public Boolean apply(final LFeature it) {
              return Boolean.valueOf(CppGenerator.this._cppExtensions.isLazy(it));
            }
          };
          Iterable<? extends LFeature> _filter_18 = IterableExtensions.filter(_allFeatures_20, _function_18);
          for(final LFeature feature_20 : _filter_18) {
            _builder.append("\t");
            _builder.append("// ");
            String _name_294 = this._cppExtensions.toName(feature_20);
            _builder.append(_name_294, "\t");
            _builder.append(" lazy pointing to ");
            String _typeOrQObject_5 = this._cppExtensions.toTypeOrQObject(feature_20);
            _builder.append(_typeOrQObject_5, "\t");
            _builder.append(" (domainKey: ");
            String _referenceDomainKey_5 = this._cppExtensions.referenceDomainKey(feature_20);
            _builder.append(_referenceDomainKey_5, "\t");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("if (has");
            String _name_295 = this._cppExtensions.toName(feature_20);
            String _firstUpper_89 = StringExtensions.toFirstUpper(_name_295);
            _builder.append(_firstUpper_89, "\t");
            _builder.append("()) {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            String _name_296 = this._cppExtensions.toName(dto);
            String _firstLower_132 = StringExtensions.toFirstLower(_name_296);
            _builder.append(_firstLower_132, "\t\t");
            _builder.append("Map.insert(");
            String _name_297 = this._cppExtensions.toName(feature_20);
            _builder.append(_name_297, "\t\t");
            _builder.append("ForeignKey, m");
            String _name_298 = this._cppExtensions.toName(feature_20);
            String _firstUpper_90 = StringExtensions.toFirstUpper(_name_298);
            _builder.append(_firstUpper_90, "\t\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          }
        }
        {
          List<? extends LFeature> _allFeatures_21 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_19 = new Function1<LFeature, Boolean>() {
            public Boolean apply(final LFeature it) {
              boolean _isLazy = CppGenerator.this._cppExtensions.isLazy(it);
              return Boolean.valueOf((!_isLazy));
            }
          };
          Iterable<? extends LFeature> _filter_19 = IterableExtensions.filter(_allFeatures_21, _function_19);
          for(final LFeature feature_21 : _filter_19) {
            {
              boolean _isTypeOfDataObject_7 = this._cppExtensions.isTypeOfDataObject(feature_21);
              if (_isTypeOfDataObject_7) {
                {
                  boolean _isContained_7 = this._cppExtensions.isContained(feature_21);
                  boolean _not_1 = (!_isContained_7);
                  if (_not_1) {
                    _builder.append("\t");
                    _builder.append("// m");
                    String _name_299 = this._cppExtensions.toName(feature_21);
                    String _firstUpper_91 = StringExtensions.toFirstUpper(_name_299);
                    _builder.append(_firstUpper_91, "\t");
                    _builder.append(" points to ");
                    String _typeName_54 = this._cppExtensions.toTypeName(feature_21);
                    _builder.append(_typeName_54, "\t");
                    _builder.append("*");
                    _builder.newLineIfNotEmpty();
                    {
                      boolean _isToMany_2 = this._cppExtensions.isToMany(feature_21);
                      if (_isToMany_2) {
                        _builder.append("\t");
                        String _name_300 = this._cppExtensions.toName(dto);
                        String _firstLower_133 = StringExtensions.toFirstLower(_name_300);
                        _builder.append(_firstLower_133, "\t");
                        _builder.append("Map.insert(");
                        String _name_301 = this._cppExtensions.toName(feature_21);
                        _builder.append(_name_301, "\t");
                        _builder.append("ForeignKey, ");
                        String _name_302 = this._cppExtensions.toName(feature_21);
                        _builder.append(_name_302, "\t");
                        _builder.append("AsQVariantList());");
                        _builder.newLineIfNotEmpty();
                      } else {
                        _builder.append("\t");
                        _builder.append("if (m");
                        String _name_303 = this._cppExtensions.toName(feature_21);
                        String _firstUpper_92 = StringExtensions.toFirstUpper(_name_303);
                        _builder.append(_firstUpper_92, "\t");
                        _builder.append(") {");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("\t");
                        String _name_304 = this._cppExtensions.toName(dto);
                        String _firstLower_134 = StringExtensions.toFirstLower(_name_304);
                        _builder.append(_firstLower_134, "\t\t");
                        _builder.append("Map.insert(");
                        String _name_305 = this._cppExtensions.toName(feature_21);
                        _builder.append(_name_305, "\t\t");
                        _builder.append("ForeignKey, m");
                        String _name_306 = this._cppExtensions.toName(feature_21);
                        String _firstUpper_93 = StringExtensions.toFirstUpper(_name_306);
                        _builder.append(_firstUpper_93, "\t\t");
                        _builder.append("->to");
                        String _mapOrList_1 = this._cppExtensions.toMapOrList(feature_21);
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
                    String _name_307 = this._cppExtensions.toName(feature_21);
                    String _firstUpper_94 = StringExtensions.toFirstUpper(_name_307);
                    _builder.append(_firstUpper_94, "\t");
                    _builder.append(" points to ");
                    String _typeName_55 = this._cppExtensions.toTypeName(feature_21);
                    _builder.append(_typeName_55, "\t");
                    _builder.append("* containing ");
                    String _name_308 = this._cppExtensions.toName(dto);
                    _builder.append(_name_308, "\t");
                    _builder.newLineIfNotEmpty();
                  }
                }
              } else {
                {
                  boolean _isArrayList_1 = this._cppExtensions.isArrayList(feature_21);
                  if (_isArrayList_1) {
                    _builder.append("\t");
                    _builder.append("// Array of ");
                    String _typeName_56 = this._cppExtensions.toTypeName(feature_21);
                    _builder.append(_typeName_56, "\t");
                    _builder.newLineIfNotEmpty();
                    {
                      String _typeName_57 = this._cppExtensions.toTypeName(feature_21);
                      boolean _equals_7 = Objects.equal(_typeName_57, "QString");
                      if (_equals_7) {
                        _builder.append("\t");
                        String _name_309 = this._cppExtensions.toName(dto);
                        String _firstLower_135 = StringExtensions.toFirstLower(_name_309);
                        _builder.append(_firstLower_135, "\t");
                        _builder.append("Map.insert(");
                        String _name_310 = this._cppExtensions.toName(feature_21);
                        _builder.append(_name_310, "\t");
                        _builder.append("ForeignKey, m");
                        String _name_311 = this._cppExtensions.toName(feature_21);
                        String _firstUpper_95 = StringExtensions.toFirstUpper(_name_311);
                        _builder.append(_firstUpper_95, "\t");
                        _builder.append("StringList);");
                        _builder.newLineIfNotEmpty();
                      } else {
                        _builder.append("\t");
                        String _name_312 = this._cppExtensions.toName(dto);
                        String _firstLower_136 = StringExtensions.toFirstLower(_name_312);
                        _builder.append(_firstLower_136, "\t");
                        _builder.append("Map.insert(");
                        String _name_313 = this._cppExtensions.toName(feature_21);
                        _builder.append(_name_313, "\t");
                        _builder.append("ForeignKey, ");
                        String _name_314 = this._cppExtensions.toName(feature_21);
                        _builder.append(_name_314, "\t");
                        _builder.append("List());");
                        _builder.newLineIfNotEmpty();
                      }
                    }
                  } else {
                    boolean _isTypeOfDates_4 = this._cppExtensions.isTypeOfDates(feature_21);
                    if (_isTypeOfDates_4) {
                      _builder.append("\t");
                      _builder.append("if (has");
                      String _name_315 = this._cppExtensions.toName(feature_21);
                      String _firstUpper_96 = StringExtensions.toFirstUpper(_name_315);
                      _builder.append(_firstUpper_96, "\t");
                      _builder.append("()) {");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t");
                      String _name_316 = this._cppExtensions.toName(dto);
                      String _firstLower_137 = StringExtensions.toFirstLower(_name_316);
                      _builder.append(_firstLower_137, "\t\t");
                      _builder.append("Map.insert(");
                      String _name_317 = this._cppExtensions.toName(feature_21);
                      _builder.append(_name_317, "\t\t");
                      _builder.append("ForeignKey, m");
                      String _name_318 = this._cppExtensions.toName(feature_21);
                      String _firstUpper_97 = StringExtensions.toFirstUpper(_name_318);
                      _builder.append(_firstUpper_97, "\t\t");
                      _builder.append(".toString(");
                      String _dateFormatString_4 = this._cppExtensions.toDateFormatString(feature_21);
                      _builder.append(_dateFormatString_4, "\t\t");
                      _builder.append("));");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("}");
                      _builder.newLine();
                    } else {
                      {
                        boolean _isEnum_4 = this._cppExtensions.isEnum(feature_21);
                        if (_isEnum_4) {
                          _builder.append("\t");
                          _builder.append("// ENUM always as  int");
                          _builder.newLine();
                        }
                      }
                      _builder.append("\t");
                      String _name_319 = this._cppExtensions.toName(dto);
                      String _firstLower_138 = StringExtensions.toFirstLower(_name_319);
                      _builder.append(_firstLower_138, "\t");
                      _builder.append("Map.insert(");
                      String _name_320 = this._cppExtensions.toName(feature_21);
                      _builder.append(_name_320, "\t");
                      _builder.append("ForeignKey, m");
                      String _name_321 = this._cppExtensions.toName(feature_21);
                      String _firstUpper_98 = StringExtensions.toFirstUpper(_name_321);
                      _builder.append(_firstUpper_98, "\t");
                      _builder.append(");");
                      _builder.newLineIfNotEmpty();
                    }
                  }
                }
              }
            }
          }
        }
        _builder.append("\t");
        _builder.append("return ");
        String _name_322 = this._cppExtensions.toName(dto);
        String _firstLower_139 = StringExtensions.toFirstLower(_name_322);
        _builder.append(_firstLower_139, "\t");
        _builder.append("Map;");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("\t");
        _builder.append("// no Foreign Properties found in data model");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return toMap();");
        _builder.newLine();
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.newLine();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Exports Properties from ");
    String _name_323 = this._cppExtensions.toName(dto);
    _builder.append(_name_323, " ");
    _builder.append(" as QVariantMap");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("* transient properties are excluded:");
    _builder.newLine();
    _builder.append(" ");
    {
      boolean _existsTransient_2 = this._cppExtensions.existsTransient(dto);
      if (_existsTransient_2) {
        _builder.append("* ");
        {
          List<? extends LFeature> _allFeatures_22 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_20 = new Function1<LFeature, Boolean>() {
            public Boolean apply(final LFeature it) {
              return Boolean.valueOf(CppGenerator.this._cppExtensions.isTransient(it));
            }
          };
          Iterable<? extends LFeature> _filter_20 = IterableExtensions.filter(_allFeatures_22, _function_20);
          boolean _hasElements = false;
          for(final LFeature feature_22 : _filter_20) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(", ", " ");
            }
            _builder.append("m");
            String _name_324 = this._cppExtensions.toName(feature_22);
            String _firstUpper_99 = StringExtensions.toFirstUpper(_name_324);
            _builder.append(_firstUpper_99, " ");
          }
        }
      }
    }
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("* To export ALL data use toMap()");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("QVariantMap ");
    String _name_325 = this._cppExtensions.toName(dto);
    _builder.append(_name_325, "");
    _builder.append("::toCacheMap()");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    {
      boolean _existsTransient_3 = this._cppExtensions.existsTransient(dto);
      if (_existsTransient_3) {
        _builder.append("\t");
        _builder.append("QVariantMap ");
        String _name_326 = this._cppExtensions.toName(dto);
        String _firstLower_140 = StringExtensions.toFirstLower(_name_326);
        _builder.append(_firstLower_140, "\t");
        _builder.append("Map;");
        _builder.newLineIfNotEmpty();
        {
          List<? extends LFeature> _allFeatures_23 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_21 = new Function1<LFeature, Boolean>() {
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
          Iterable<? extends LFeature> _filter_21 = IterableExtensions.filter(_allFeatures_23, _function_21);
          for(final LFeature feature_23 : _filter_21) {
            _builder.append("\t");
            _builder.append("// ");
            String _name_327 = this._cppExtensions.toName(feature_23);
            _builder.append(_name_327, "\t");
            _builder.append(" lazy pointing to ");
            String _typeOrQObject_6 = this._cppExtensions.toTypeOrQObject(feature_23);
            _builder.append(_typeOrQObject_6, "\t");
            _builder.append(" (domainKey: ");
            String _referenceDomainKey_6 = this._cppExtensions.referenceDomainKey(feature_23);
            _builder.append(_referenceDomainKey_6, "\t");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("if (has");
            String _name_328 = this._cppExtensions.toName(feature_23);
            String _firstUpper_100 = StringExtensions.toFirstUpper(_name_328);
            _builder.append(_firstUpper_100, "\t");
            _builder.append("()) {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            String _name_329 = this._cppExtensions.toName(dto);
            String _firstLower_141 = StringExtensions.toFirstLower(_name_329);
            _builder.append(_firstLower_141, "\t\t");
            _builder.append("Map.insert(");
            String _name_330 = this._cppExtensions.toName(feature_23);
            _builder.append(_name_330, "\t\t");
            _builder.append("Key, m");
            String _name_331 = this._cppExtensions.toName(feature_23);
            String _firstUpper_101 = StringExtensions.toFirstUpper(_name_331);
            _builder.append(_firstUpper_101, "\t\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          }
        }
        {
          List<? extends LFeature> _allFeatures_24 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_22 = new Function1<LFeature, Boolean>() {
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
          Iterable<? extends LFeature> _filter_22 = IterableExtensions.filter(_allFeatures_24, _function_22);
          for(final LFeature feature_24 : _filter_22) {
            {
              boolean _isTypeOfDataObject_8 = this._cppExtensions.isTypeOfDataObject(feature_24);
              if (_isTypeOfDataObject_8) {
                {
                  boolean _isContained_8 = this._cppExtensions.isContained(feature_24);
                  boolean _not_2 = (!_isContained_8);
                  if (_not_2) {
                    _builder.append("\t");
                    _builder.append("// m");
                    String _name_332 = this._cppExtensions.toName(feature_24);
                    String _firstUpper_102 = StringExtensions.toFirstUpper(_name_332);
                    _builder.append(_firstUpper_102, "\t");
                    _builder.append(" points to ");
                    String _typeName_58 = this._cppExtensions.toTypeName(feature_24);
                    _builder.append(_typeName_58, "\t");
                    _builder.append("*");
                    _builder.newLineIfNotEmpty();
                    {
                      boolean _isToMany_3 = this._cppExtensions.isToMany(feature_24);
                      if (_isToMany_3) {
                        _builder.append("\t");
                        String _name_333 = this._cppExtensions.toName(dto);
                        String _firstLower_142 = StringExtensions.toFirstLower(_name_333);
                        _builder.append(_firstLower_142, "\t");
                        _builder.append("Map.insert(");
                        String _name_334 = this._cppExtensions.toName(feature_24);
                        _builder.append(_name_334, "\t");
                        _builder.append("Key, ");
                        String _name_335 = this._cppExtensions.toName(feature_24);
                        _builder.append(_name_335, "\t");
                        _builder.append("AsQVariantList());");
                        _builder.newLineIfNotEmpty();
                      } else {
                        _builder.append("\t");
                        _builder.append("if (m");
                        String _name_336 = this._cppExtensions.toName(feature_24);
                        String _firstUpper_103 = StringExtensions.toFirstUpper(_name_336);
                        _builder.append(_firstUpper_103, "\t");
                        _builder.append(") {");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("\t");
                        String _name_337 = this._cppExtensions.toName(dto);
                        String _firstLower_143 = StringExtensions.toFirstLower(_name_337);
                        _builder.append(_firstLower_143, "\t\t");
                        _builder.append("Map.insert(");
                        String _name_338 = this._cppExtensions.toName(feature_24);
                        _builder.append(_name_338, "\t\t");
                        _builder.append("Key, m");
                        String _name_339 = this._cppExtensions.toName(feature_24);
                        String _firstUpper_104 = StringExtensions.toFirstUpper(_name_339);
                        _builder.append(_firstUpper_104, "\t\t");
                        _builder.append("->to");
                        String _mapOrList_2 = this._cppExtensions.toMapOrList(feature_24);
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
                    String _name_340 = this._cppExtensions.toName(feature_24);
                    String _firstUpper_105 = StringExtensions.toFirstUpper(_name_340);
                    _builder.append(_firstUpper_105, "\t");
                    _builder.append(" points to ");
                    String _typeName_59 = this._cppExtensions.toTypeName(feature_24);
                    _builder.append(_typeName_59, "\t");
                    _builder.append("* containing ");
                    String _name_341 = this._cppExtensions.toName(dto);
                    _builder.append(_name_341, "\t");
                    _builder.newLineIfNotEmpty();
                  }
                }
              } else {
                {
                  boolean _isArrayList_2 = this._cppExtensions.isArrayList(feature_24);
                  if (_isArrayList_2) {
                    _builder.append("\t");
                    _builder.append("// Array of ");
                    String _typeName_60 = this._cppExtensions.toTypeName(feature_24);
                    _builder.append(_typeName_60, "\t");
                    _builder.newLineIfNotEmpty();
                    {
                      String _typeName_61 = this._cppExtensions.toTypeName(feature_24);
                      boolean _equals_8 = Objects.equal(_typeName_61, "QString");
                      if (_equals_8) {
                        _builder.append("\t");
                        String _name_342 = this._cppExtensions.toName(dto);
                        String _firstLower_144 = StringExtensions.toFirstLower(_name_342);
                        _builder.append(_firstLower_144, "\t");
                        _builder.append("Map.insert(");
                        String _name_343 = this._cppExtensions.toName(feature_24);
                        _builder.append(_name_343, "\t");
                        _builder.append("Key, m");
                        String _name_344 = this._cppExtensions.toName(feature_24);
                        String _firstUpper_106 = StringExtensions.toFirstUpper(_name_344);
                        _builder.append(_firstUpper_106, "\t");
                        _builder.append("StringList);");
                        _builder.newLineIfNotEmpty();
                      } else {
                        _builder.append("\t");
                        String _name_345 = this._cppExtensions.toName(dto);
                        String _firstLower_145 = StringExtensions.toFirstLower(_name_345);
                        _builder.append(_firstLower_145, "\t");
                        _builder.append("Map.insert(");
                        String _name_346 = this._cppExtensions.toName(feature_24);
                        _builder.append(_name_346, "\t");
                        _builder.append("Key, ");
                        String _name_347 = this._cppExtensions.toName(feature_24);
                        _builder.append(_name_347, "\t");
                        _builder.append("List());");
                        _builder.newLineIfNotEmpty();
                      }
                    }
                  } else {
                    boolean _isTypeOfDates_5 = this._cppExtensions.isTypeOfDates(feature_24);
                    if (_isTypeOfDates_5) {
                      _builder.append("\t");
                      _builder.append("if (has");
                      String _name_348 = this._cppExtensions.toName(feature_24);
                      String _firstUpper_107 = StringExtensions.toFirstUpper(_name_348);
                      _builder.append(_firstUpper_107, "\t");
                      _builder.append("()) {");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("\t");
                      String _name_349 = this._cppExtensions.toName(dto);
                      String _firstLower_146 = StringExtensions.toFirstLower(_name_349);
                      _builder.append(_firstLower_146, "\t\t");
                      _builder.append("Map.insert(");
                      String _name_350 = this._cppExtensions.toName(feature_24);
                      _builder.append(_name_350, "\t\t");
                      _builder.append("Key, m");
                      String _name_351 = this._cppExtensions.toName(feature_24);
                      String _firstUpper_108 = StringExtensions.toFirstUpper(_name_351);
                      _builder.append(_firstUpper_108, "\t\t");
                      _builder.append(".toString(");
                      String _dateFormatString_5 = this._cppExtensions.toDateFormatString(feature_24);
                      _builder.append(_dateFormatString_5, "\t\t");
                      _builder.append("));");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("}");
                      _builder.newLine();
                    } else {
                      {
                        boolean _isEnum_5 = this._cppExtensions.isEnum(feature_24);
                        if (_isEnum_5) {
                          _builder.append("\t");
                          _builder.append("// ENUM always as  int");
                          _builder.newLine();
                        }
                      }
                      _builder.append("\t");
                      String _name_352 = this._cppExtensions.toName(dto);
                      String _firstLower_147 = StringExtensions.toFirstLower(_name_352);
                      _builder.append(_firstLower_147, "\t");
                      _builder.append("Map.insert(");
                      String _name_353 = this._cppExtensions.toName(feature_24);
                      _builder.append(_name_353, "\t");
                      _builder.append("Key, m");
                      String _name_354 = this._cppExtensions.toName(feature_24);
                      String _firstUpper_109 = StringExtensions.toFirstUpper(_name_354);
                      _builder.append(_firstUpper_109, "\t");
                      _builder.append(");");
                      _builder.newLineIfNotEmpty();
                    }
                  }
                }
              }
            }
          }
        }
        {
          List<? extends LFeature> _allFeatures_25 = dto.getAllFeatures();
          final Function1<LFeature, Boolean> _function_23 = new Function1<LFeature, Boolean>() {
            public Boolean apply(final LFeature it) {
              return Boolean.valueOf(CppGenerator.this._cppExtensions.isTransient(it));
            }
          };
          Iterable<? extends LFeature> _filter_23 = IterableExtensions.filter(_allFeatures_25, _function_23);
          for(final LFeature feature_25 : _filter_23) {
            _builder.append("\t");
            _builder.append("// excluded: m");
            String _name_355 = this._cppExtensions.toName(feature_25);
            String _firstUpper_110 = StringExtensions.toFirstUpper(_name_355);
            _builder.append(_firstUpper_110, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.append("return ");
        String _name_356 = this._cppExtensions.toName(dto);
        String _firstLower_148 = StringExtensions.toFirstLower(_name_356);
        _builder.append(_firstLower_148, "\t");
        _builder.append("Map;");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("\t");
        _builder.append("// no transient properties found from data model");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("// use default toMao()");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return toMap();");
        _builder.newLine();
      }
    }
    _builder.append("}");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_26 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_24 = new Function1<LFeature, Boolean>() {
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
      Iterable<? extends LFeature> _filter_24 = IterableExtensions.filter(_allFeatures_26, _function_24);
      for(final LFeature feature_26 : _filter_24) {
        CharSequence _foo = this.foo(feature_26);
        _builder.append(_foo, "");
        _builder.newLineIfNotEmpty();
        _builder.append("// ");
        String _name_357 = this._cppExtensions.toName(feature_26);
        _builder.append(_name_357, "");
        _builder.append(" lazy pointing to ");
        String _typeOrQObject_7 = this._cppExtensions.toTypeOrQObject(feature_26);
        _builder.append(_typeOrQObject_7, "");
        _builder.append(" (domainKey: ");
        String _referenceDomainKey_7 = this._cppExtensions.referenceDomainKey(feature_26);
        _builder.append(_referenceDomainKey_7, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        String _referenceDomainKeyType_7 = this._cppExtensions.referenceDomainKeyType(feature_26);
        _builder.append(_referenceDomainKeyType_7, "");
        _builder.append(" ");
        String _name_358 = this._cppExtensions.toName(dto);
        _builder.append(_name_358, "");
        _builder.append("::");
        String _name_359 = this._cppExtensions.toName(feature_26);
        _builder.append(_name_359, "");
        _builder.append("() const");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return m");
        String _name_360 = this._cppExtensions.toName(feature_26);
        String _firstUpper_111 = StringExtensions.toFirstUpper(_name_360);
        _builder.append(_firstUpper_111, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        String _typeOrQObject_8 = this._cppExtensions.toTypeOrQObject(feature_26);
        _builder.append(_typeOrQObject_8, "");
        _builder.append(" ");
        String _name_361 = this._cppExtensions.toName(dto);
        _builder.append(_name_361, "");
        _builder.append("::");
        String _name_362 = this._cppExtensions.toName(feature_26);
        _builder.append(_name_362, "");
        _builder.append("AsDataObject() const");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return m");
        String _name_363 = this._cppExtensions.toName(feature_26);
        String _firstUpper_112 = StringExtensions.toFirstUpper(_name_363);
        _builder.append(_firstUpper_112, "\t");
        _builder.append("AsDataObject;");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_364 = this._cppExtensions.toName(dto);
        _builder.append(_name_364, "");
        _builder.append("::set");
        String _name_365 = this._cppExtensions.toName(feature_26);
        String _firstUpper_113 = StringExtensions.toFirstUpper(_name_365);
        _builder.append(_firstUpper_113, "");
        _builder.append("(");
        String _referenceDomainKeyType_8 = this._cppExtensions.referenceDomainKeyType(feature_26);
        _builder.append(_referenceDomainKeyType_8, "");
        _builder.append(" ");
        String _name_366 = this._cppExtensions.toName(feature_26);
        _builder.append(_name_366, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (");
        String _name_367 = this._cppExtensions.toName(feature_26);
        _builder.append(_name_367, "\t");
        _builder.append(" != m");
        String _name_368 = this._cppExtensions.toName(feature_26);
        String _firstUpper_114 = StringExtensions.toFirstUpper(_name_368);
        _builder.append(_firstUpper_114, "\t");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("if (");
        String _name_369 = this._cppExtensions.toName(feature_26);
        _builder.append(_name_369, "\t\t");
        _builder.append(" != ");
        String _referenceDomainKeyType_9 = this._cppExtensions.referenceDomainKeyType(feature_26);
        String _defaultForLazyTypeName_4 = this._cppExtensions.defaultForLazyTypeName(_referenceDomainKeyType_9);
        _builder.append(_defaultForLazyTypeName_4, "\t\t");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("// resolve the corresponding Data Object on demand from DataManager");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("} else {");
        _builder.newLine();
        _builder.append("        \t");
        _builder.append("// remove old Data Object");
        _builder.newLine();
        _builder.append("        \t");
        _builder.append("if (mTopicIdAsDataObject) {");
        _builder.newLine();
        _builder.append("            \t");
        _builder.append("// reset pointer, don\'t delete the independent object !");
        _builder.newLine();
        _builder.append("            \t");
        _builder.append("m");
        String _name_370 = this._cppExtensions.toName(feature_26);
        String _firstUpper_115 = StringExtensions.toFirstUpper(_name_370);
        _builder.append(_firstUpper_115, "            \t");
        _builder.append("AsDataObject = 0;");
        _builder.newLineIfNotEmpty();
        _builder.append("            \t");
        _builder.append("emit ");
        String _name_371 = this._cppExtensions.toName(feature_26);
        _builder.append(_name_371, "            \t");
        _builder.append("Removed(m");
        String _name_372 = this._cppExtensions.toName(feature_26);
        String _firstUpper_116 = StringExtensions.toFirstUpper(_name_372);
        _builder.append(_firstUpper_116, "            \t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("m");
        String _name_373 = this._cppExtensions.toName(feature_26);
        String _firstUpper_117 = StringExtensions.toFirstUpper(_name_373);
        _builder.append(_firstUpper_117, "        ");
        _builder.append(" = ");
        String _name_374 = this._cppExtensions.toName(feature_26);
        _builder.append(_name_374, "        ");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("emit ");
        String _name_375 = this._cppExtensions.toName(feature_26);
        _builder.append(_name_375, "        ");
        _builder.append("Changed(");
        String _name_376 = this._cppExtensions.toName(feature_26);
        _builder.append(_name_376, "        ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_377 = this._cppExtensions.toName(dto);
        _builder.append(_name_377, "");
        _builder.append("::remove");
        String _name_378 = this._cppExtensions.toName(feature_26);
        String _firstUpper_118 = StringExtensions.toFirstUpper(_name_378);
        _builder.append(_firstUpper_118, "");
        _builder.append("()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (m");
        String _name_379 = this._cppExtensions.toName(feature_26);
        String _firstUpper_119 = StringExtensions.toFirstUpper(_name_379);
        _builder.append(_firstUpper_119, "\t");
        _builder.append(" != -1) {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("set");
        String _name_380 = this._cppExtensions.toName(feature_26);
        String _firstUpper_120 = StringExtensions.toFirstUpper(_name_380);
        _builder.append(_firstUpper_120, "\t\t");
        _builder.append("(");
        String _referenceDomainKeyType_10 = this._cppExtensions.referenceDomainKeyType(feature_26);
        String _defaultForLazyTypeName_5 = this._cppExtensions.defaultForLazyTypeName(_referenceDomainKeyType_10);
        _builder.append(_defaultForLazyTypeName_5, "\t\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("bool ");
        String _name_381 = this._cppExtensions.toName(dto);
        _builder.append(_name_381, "");
        _builder.append("::has");
        String _name_382 = this._cppExtensions.toName(feature_26);
        String _firstUpper_121 = StringExtensions.toFirstUpper(_name_382);
        _builder.append(_firstUpper_121, "");
        _builder.append("()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("if (m");
        String _name_383 = this._cppExtensions.toName(feature_26);
        String _firstUpper_122 = StringExtensions.toFirstUpper(_name_383);
        _builder.append(_firstUpper_122, "    ");
        _builder.append(" != ");
        String _referenceDomainKeyType_11 = this._cppExtensions.referenceDomainKeyType(feature_26);
        String _defaultForLazyTypeName_6 = this._cppExtensions.defaultForLazyTypeName(_referenceDomainKeyType_11);
        _builder.append(_defaultForLazyTypeName_6, "    ");
        _builder.append(") {");
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
        String _name_384 = this._cppExtensions.toName(dto);
        _builder.append(_name_384, "");
        _builder.append("::is");
        String _name_385 = this._cppExtensions.toName(feature_26);
        String _firstUpper_123 = StringExtensions.toFirstUpper(_name_385);
        _builder.append(_firstUpper_123, "");
        _builder.append("ResolvedAsDataObject()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("if (m");
        String _name_386 = this._cppExtensions.toName(feature_26);
        String _firstUpper_124 = StringExtensions.toFirstUpper(_name_386);
        _builder.append(_firstUpper_124, "    ");
        _builder.append("AsDataObject) {");
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
        _builder.newLine();
        _builder.append("// lazy bound Data Object was resolved. overwrite ");
        String _referenceDomainKey_8 = this._cppExtensions.referenceDomainKey(feature_26);
        _builder.append(_referenceDomainKey_8, "");
        _builder.append(" if different");
        _builder.newLineIfNotEmpty();
        _builder.append("void ");
        String _name_387 = this._cppExtensions.toName(dto);
        _builder.append(_name_387, "");
        _builder.append("::resolve");
        String _name_388 = this._cppExtensions.toName(feature_26);
        String _firstUpper_125 = StringExtensions.toFirstUpper(_name_388);
        _builder.append(_firstUpper_125, "");
        _builder.append("AsDataObject(");
        String _typeName_62 = this._cppExtensions.toTypeName(feature_26);
        _builder.append(_typeName_62, "");
        _builder.append("* ");
        String _typeName_63 = this._cppExtensions.toTypeName(feature_26);
        String _firstLower_149 = StringExtensions.toFirstLower(_typeName_63);
        _builder.append(_firstLower_149, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("if (");
        String _typeName_64 = this._cppExtensions.toTypeName(feature_26);
        String _firstLower_150 = StringExtensions.toFirstLower(_typeName_64);
        _builder.append(_firstLower_150, "    ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("    \t");
        _builder.append("m");
        String _name_389 = this._cppExtensions.toName(feature_26);
        String _firstUpper_126 = StringExtensions.toFirstUpper(_name_389);
        _builder.append(_firstUpper_126, "    \t");
        _builder.append("AsDataObject = ");
        String _typeName_65 = this._cppExtensions.toTypeName(feature_26);
        String _firstLower_151 = StringExtensions.toFirstLower(_typeName_65);
        _builder.append(_firstLower_151, "    \t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("if (");
        String _typeName_66 = this._cppExtensions.toTypeName(feature_26);
        String _firstLower_152 = StringExtensions.toFirstLower(_typeName_66);
        _builder.append(_firstLower_152, "        ");
        _builder.append("->");
        String _referenceDomainKey_9 = this._cppExtensions.referenceDomainKey(feature_26);
        _builder.append(_referenceDomainKey_9, "        ");
        _builder.append("() != m");
        String _name_390 = this._cppExtensions.toName(feature_26);
        String _firstUpper_127 = StringExtensions.toFirstUpper(_name_390);
        _builder.append(_firstUpper_127, "        ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("m");
        String _name_391 = this._cppExtensions.toName(feature_26);
        String _firstUpper_128 = StringExtensions.toFirstUpper(_name_391);
        _builder.append(_firstUpper_128, "            ");
        _builder.append(" = ");
        String _typeName_67 = this._cppExtensions.toTypeName(feature_26);
        String _firstLower_153 = StringExtensions.toFirstLower(_typeName_67);
        _builder.append(_firstLower_153, "            ");
        _builder.append("->");
        String _referenceDomainKey_10 = this._cppExtensions.referenceDomainKey(feature_26);
        _builder.append(_referenceDomainKey_10, "            ");
        _builder.append("();");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("emit ");
        String _name_392 = this._cppExtensions.toName(feature_26);
        String _firstLower_154 = StringExtensions.toFirstLower(_name_392);
        _builder.append(_firstLower_154, "            ");
        _builder.append("Changed(m");
        String _name_393 = this._cppExtensions.toName(feature_26);
        String _firstUpper_129 = StringExtensions.toFirstUpper(_name_393);
        _builder.append(_firstUpper_129, "            ");
        _builder.append(");");
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
      List<? extends LFeature> _allFeatures_27 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_25 = new Function1<LFeature, Boolean>() {
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
      Iterable<? extends LFeature> _filter_25 = IterableExtensions.filter(_allFeatures_27, _function_25);
      for(final LFeature feature_27 : _filter_25) {
        CharSequence _foo_1 = this.foo(feature_27);
        _builder.append(_foo_1, "");
        _builder.newLineIfNotEmpty();
        {
          boolean _and_2 = false;
          boolean _isTypeOfDataObject_9 = this._cppExtensions.isTypeOfDataObject(feature_27);
          if (!_isTypeOfDataObject_9) {
            _and_2 = false;
          } else {
            boolean _isContained_9 = this._cppExtensions.isContained(feature_27);
            _and_2 = _isContained_9;
          }
          if (_and_2) {
            _builder.append("// No SETTER for ");
            String _name_394 = this._cppExtensions.toName(feature_27);
            String _firstUpper_130 = StringExtensions.toFirstUpper(_name_394);
            _builder.append(_firstUpper_130, "");
            _builder.append(" - it\'s the parent");
            _builder.newLineIfNotEmpty();
            String _typeOrQObject_9 = this._cppExtensions.toTypeOrQObject(feature_27);
            _builder.append(_typeOrQObject_9, "");
            _builder.append(" ");
            String _name_395 = this._cppExtensions.toName(dto);
            _builder.append(_name_395, "");
            _builder.append("::");
            String _name_396 = this._cppExtensions.toName(feature_27);
            _builder.append(_name_396, "");
            _builder.append("() const");
            _builder.newLineIfNotEmpty();
            _builder.append("{");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("return qobject_cast<");
            String _typeOrQObject_10 = this._cppExtensions.toTypeOrQObject(feature_27);
            _builder.append(_typeOrQObject_10, "\t");
            _builder.append(">(parent());");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
          } else {
            boolean _isEnum_6 = this._cppExtensions.isEnum(feature_27);
            if (_isEnum_6) {
              _builder.append("int ");
              String _name_397 = this._cppExtensions.toName(dto);
              _builder.append(_name_397, "");
              _builder.append("::");
              String _name_398 = this._cppExtensions.toName(feature_27);
              _builder.append(_name_398, "");
              _builder.append("() const");
              _builder.newLineIfNotEmpty();
              _builder.append("{");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("return m");
              String _name_399 = this._cppExtensions.toName(feature_27);
              String _firstUpper_131 = StringExtensions.toFirstUpper(_name_399);
              _builder.append(_firstUpper_131, "\t");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
              _builder.append("}");
              _builder.newLine();
              _builder.append("void ");
              String _name_400 = this._cppExtensions.toName(dto);
              _builder.append(_name_400, "");
              _builder.append("::set");
              String _name_401 = this._cppExtensions.toName(feature_27);
              String _firstUpper_132 = StringExtensions.toFirstUpper(_name_401);
              _builder.append(_firstUpper_132, "");
              _builder.append("(int ");
              String _name_402 = this._cppExtensions.toName(feature_27);
              _builder.append(_name_402, "");
              _builder.append(")");
              _builder.newLineIfNotEmpty();
              _builder.append("{");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("if (");
              String _name_403 = this._cppExtensions.toName(feature_27);
              _builder.append(_name_403, "\t");
              _builder.append(" != m");
              String _name_404 = this._cppExtensions.toName(feature_27);
              String _firstUpper_133 = StringExtensions.toFirstUpper(_name_404);
              _builder.append(_firstUpper_133, "\t");
              _builder.append(") {");
              _builder.newLineIfNotEmpty();
              _builder.append("\t\t");
              _builder.append("m");
              String _name_405 = this._cppExtensions.toName(feature_27);
              String _firstUpper_134 = StringExtensions.toFirstUpper(_name_405);
              _builder.append(_firstUpper_134, "\t\t");
              _builder.append(" = ");
              String _name_406 = this._cppExtensions.toName(feature_27);
              _builder.append(_name_406, "\t\t");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
              _builder.append("\t\t");
              _builder.append("emit ");
              String _name_407 = this._cppExtensions.toName(feature_27);
              _builder.append(_name_407, "\t\t");
              _builder.append("Changed(");
              String _name_408 = this._cppExtensions.toName(feature_27);
              _builder.append(_name_408, "\t\t");
              _builder.append(");");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("}");
              _builder.newLine();
              _builder.append("}");
              _builder.newLine();
              _builder.append("void ");
              String _name_409 = this._cppExtensions.toName(dto);
              _builder.append(_name_409, "");
              _builder.append("::set");
              String _name_410 = this._cppExtensions.toName(feature_27);
              String _firstUpper_135 = StringExtensions.toFirstUpper(_name_410);
              _builder.append(_firstUpper_135, "");
              _builder.append("(QString ");
              String _name_411 = this._cppExtensions.toName(feature_27);
              _builder.append(_name_411, "");
              _builder.append(")");
              _builder.newLineIfNotEmpty();
              _builder.append("{");
              _builder.newLine();
              _builder.append("    ");
              _builder.append("set");
              String _name_412 = this._cppExtensions.toName(feature_27);
              String _firstUpper_136 = StringExtensions.toFirstUpper(_name_412);
              _builder.append(_firstUpper_136, "    ");
              _builder.append("(");
              String _name_413 = this._cppExtensions.toName(feature_27);
              _builder.append(_name_413, "    ");
              _builder.append("StringToInt(");
              String _name_414 = this._cppExtensions.toName(feature_27);
              _builder.append(_name_414, "    ");
              _builder.append("));");
              _builder.newLineIfNotEmpty();
              _builder.append("}");
              _builder.newLine();
              _builder.append("int ");
              String _name_415 = this._cppExtensions.toName(dto);
              _builder.append(_name_415, "");
              _builder.append("::");
              String _name_416 = this._cppExtensions.toName(feature_27);
              _builder.append(_name_416, "");
              _builder.append("StringToInt(QString ");
              String _name_417 = this._cppExtensions.toName(feature_27);
              _builder.append(_name_417, "");
              _builder.append(")");
              _builder.newLineIfNotEmpty();
              _builder.append("{");
              _builder.newLine();
              _builder.append("    ");
              _builder.append("if (");
              String _name_418 = this._cppExtensions.toName(feature_27);
              _builder.append(_name_418, "    ");
              _builder.append(".isNull() || ");
              String _name_419 = this._cppExtensions.toName(feature_27);
              _builder.append(_name_419, "    ");
              _builder.append(".isEmpty()) {");
              _builder.newLineIfNotEmpty();
              _builder.append("        ");
              _builder.append("return ");
              String _typeName_68 = this._cppExtensions.toTypeName(feature_27);
              _builder.append(_typeName_68, "        ");
              _builder.append("::NO_VALUE;");
              _builder.newLineIfNotEmpty();
              _builder.append("    ");
              _builder.append("}");
              _builder.newLine();
              {
                LEnum _enumFromAttributeType = this._cppExtensions.enumFromAttributeType(feature_27);
                EList<LEnumLiteral> _literals = _enumFromAttributeType.getLiterals();
                for(final LEnumLiteral literal : _literals) {
                  _builder.append("    ");
                  _builder.append("if (");
                  String _name_420 = this._cppExtensions.toName(feature_27);
                  _builder.append(_name_420, "    ");
                  _builder.append(" == \"");
                  String _name_421 = literal.getName();
                  _builder.append(_name_421, "    ");
                  _builder.append("\") {");
                  _builder.newLineIfNotEmpty();
                  _builder.append("    ");
                  _builder.append("    ");
                  _builder.append("return ");
                  LEnum _enumFromAttributeType_1 = this._cppExtensions.enumFromAttributeType(feature_27);
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
              String _name_422 = this._cppExtensions.toName(feature_27);
              _builder.append(_name_422, "    ");
              _builder.append(" wrong enumValue as String: \" << ");
              String _name_423 = this._cppExtensions.toName(feature_27);
              _builder.append(_name_423, "    ");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
              _builder.append("    ");
              _builder.append("return ");
              String _typeName_69 = this._cppExtensions.toTypeName(feature_27);
              _builder.append(_typeName_69, "    ");
              _builder.append("::NO_VALUE;");
              _builder.newLineIfNotEmpty();
              _builder.append("}");
              _builder.newLine();
            } else {
              String _typeOrQObject_11 = this._cppExtensions.toTypeOrQObject(feature_27);
              _builder.append(_typeOrQObject_11, "");
              _builder.append(" ");
              String _name_424 = this._cppExtensions.toName(dto);
              _builder.append(_name_424, "");
              _builder.append("::");
              String _name_425 = this._cppExtensions.toName(feature_27);
              _builder.append(_name_425, "");
              _builder.append("() const");
              _builder.newLineIfNotEmpty();
              _builder.append("{");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("return m");
              String _name_426 = this._cppExtensions.toName(feature_27);
              String _firstUpper_137 = StringExtensions.toFirstUpper(_name_426);
              _builder.append(_firstUpper_137, "\t");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
              _builder.append("}");
              _builder.newLine();
              _builder.append("void ");
              String _name_427 = this._cppExtensions.toName(dto);
              _builder.append(_name_427, "");
              _builder.append("::set");
              String _name_428 = this._cppExtensions.toName(feature_27);
              String _firstUpper_138 = StringExtensions.toFirstUpper(_name_428);
              _builder.append(_firstUpper_138, "");
              _builder.append("(");
              String _typeOrQObject_12 = this._cppExtensions.toTypeOrQObject(feature_27);
              _builder.append(_typeOrQObject_12, "");
              _builder.append(" ");
              String _name_429 = this._cppExtensions.toName(feature_27);
              _builder.append(_name_429, "");
              _builder.append(")");
              _builder.newLineIfNotEmpty();
              _builder.append("{");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("if (");
              String _name_430 = this._cppExtensions.toName(feature_27);
              _builder.append(_name_430, "\t");
              _builder.append(" != m");
              String _name_431 = this._cppExtensions.toName(feature_27);
              String _firstUpper_139 = StringExtensions.toFirstUpper(_name_431);
              _builder.append(_firstUpper_139, "\t");
              _builder.append(") {");
              _builder.newLineIfNotEmpty();
              {
                boolean _isTypeOfDataObject_10 = this._cppExtensions.isTypeOfDataObject(feature_27);
                if (_isTypeOfDataObject_10) {
                  _builder.append("\t\t");
                  _builder.append("if (m");
                  String _name_432 = this._cppExtensions.toName(feature_27);
                  String _firstUpper_140 = StringExtensions.toFirstUpper(_name_432);
                  _builder.append(_firstUpper_140, "\t\t");
                  _builder.append(") {");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t\t");
                  _builder.append("\t");
                  _builder.append("m");
                  String _name_433 = this._cppExtensions.toName(feature_27);
                  String _firstUpper_141 = StringExtensions.toFirstUpper(_name_433);
                  _builder.append(_firstUpper_141, "\t\t\t");
                  _builder.append("->deleteLater();");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t\t");
                  _builder.append("}");
                  _builder.newLine();
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_434 = this._cppExtensions.toName(feature_27);
                  String _firstUpper_142 = StringExtensions.toFirstUpper(_name_434);
                  _builder.append(_firstUpper_142, "\t\t");
                  _builder.append(" = ");
                  String _name_435 = this._cppExtensions.toName(feature_27);
                  _builder.append(_name_435, "\t\t");
                  _builder.append(";");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_436 = this._cppExtensions.toName(feature_27);
                  String _firstUpper_143 = StringExtensions.toFirstUpper(_name_436);
                  _builder.append(_firstUpper_143, "\t\t");
                  _builder.append("->setParent(this);");
                  _builder.newLineIfNotEmpty();
                } else {
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_437 = this._cppExtensions.toName(feature_27);
                  String _firstUpper_144 = StringExtensions.toFirstUpper(_name_437);
                  _builder.append(_firstUpper_144, "\t\t");
                  _builder.append(" = ");
                  String _name_438 = this._cppExtensions.toName(feature_27);
                  _builder.append(_name_438, "\t\t");
                  _builder.append(";");
                  _builder.newLineIfNotEmpty();
                }
              }
              _builder.append("\t\t");
              _builder.append("emit ");
              String _name_439 = this._cppExtensions.toName(feature_27);
              _builder.append(_name_439, "\t\t");
              _builder.append("Changed(");
              String _name_440 = this._cppExtensions.toName(feature_27);
              _builder.append(_name_440, "\t\t");
              _builder.append(");");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("}");
              _builder.newLine();
              _builder.append("}");
              _builder.newLine();
              {
                boolean _isTypeOfDataObject_11 = this._cppExtensions.isTypeOfDataObject(feature_27);
                if (_isTypeOfDataObject_11) {
                  _builder.append("void ");
                  String _name_441 = this._cppExtensions.toName(dto);
                  _builder.append(_name_441, "");
                  _builder.append("::delete");
                  String _name_442 = this._cppExtensions.toName(feature_27);
                  String _firstUpper_145 = StringExtensions.toFirstUpper(_name_442);
                  _builder.append(_firstUpper_145, "");
                  _builder.append("()");
                  _builder.newLineIfNotEmpty();
                  _builder.append("{");
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("if (m");
                  String _name_443 = this._cppExtensions.toName(feature_27);
                  String _firstUpper_146 = StringExtensions.toFirstUpper(_name_443);
                  _builder.append(_firstUpper_146, "\t");
                  _builder.append(") {");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t\t");
                  _builder.append("emit ");
                  String _name_444 = this._cppExtensions.toName(feature_27);
                  String _firstLower_155 = StringExtensions.toFirstLower(_name_444);
                  _builder.append(_firstLower_155, "\t\t");
                  _builder.append("Deleted(m");
                  String _name_445 = this._cppExtensions.toName(feature_27);
                  String _firstUpper_147 = StringExtensions.toFirstUpper(_name_445);
                  _builder.append(_firstUpper_147, "\t\t");
                  _builder.append("->uuid());");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_446 = this._cppExtensions.toName(feature_27);
                  String _firstUpper_148 = StringExtensions.toFirstUpper(_name_446);
                  _builder.append(_firstUpper_148, "\t\t");
                  _builder.append("->deleteLater();");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t\t");
                  _builder.append("m");
                  String _name_447 = this._cppExtensions.toName(feature_27);
                  String _firstUpper_149 = StringExtensions.toFirstUpper(_name_447);
                  _builder.append(_firstUpper_149, "\t\t");
                  _builder.append(" = 0;");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("}");
                  _builder.newLine();
                  _builder.append("}");
                  _builder.newLine();
                  _builder.append("bool ");
                  String _name_448 = this._cppExtensions.toName(dto);
                  _builder.append(_name_448, "");
                  _builder.append("::has");
                  String _name_449 = this._cppExtensions.toName(feature_27);
                  String _firstUpper_150 = StringExtensions.toFirstUpper(_name_449);
                  _builder.append(_firstUpper_150, "");
                  _builder.append("()");
                  _builder.newLineIfNotEmpty();
                  _builder.append("{");
                  _builder.newLine();
                  _builder.append("    ");
                  _builder.append("if (m");
                  String _name_450 = this._cppExtensions.toName(feature_27);
                  String _firstUpper_151 = StringExtensions.toFirstUpper(_name_450);
                  _builder.append(_firstUpper_151, "    ");
                  _builder.append(") {");
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
          boolean _isTypeOfDates_6 = this._cppExtensions.isTypeOfDates(feature_27);
          if (_isTypeOfDates_6) {
            _builder.append("bool ");
            String _name_451 = this._cppExtensions.toName(dto);
            _builder.append(_name_451, "");
            _builder.append("::has");
            String _name_452 = this._cppExtensions.toName(feature_27);
            String _firstUpper_152 = StringExtensions.toFirstUpper(_name_452);
            _builder.append(_firstUpper_152, "");
            _builder.append("()");
            _builder.newLineIfNotEmpty();
            _builder.append("{");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("return !m");
            String _name_453 = this._cppExtensions.toName(feature_27);
            String _firstUpper_153 = StringExtensions.toFirstUpper(_name_453);
            _builder.append(_firstUpper_153, "\t");
            _builder.append(".isNull() && m");
            String _name_454 = this._cppExtensions.toName(feature_27);
            String _firstUpper_154 = StringExtensions.toFirstUpper(_name_454);
            _builder.append(_firstUpper_154, "\t");
            _builder.append(".isValid();");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    {
      List<? extends LFeature> _allFeatures_28 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_26 = new Function1<LFeature, Boolean>() {
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
      Iterable<? extends LFeature> _filter_26 = IterableExtensions.filter(_allFeatures_28, _function_26);
      for(final LFeature feature_28 : _filter_26) {
        CharSequence _foo_2 = this.foo(feature_28);
        _builder.append(_foo_2, "");
        _builder.newLineIfNotEmpty();
        _builder.append("void ");
        String _name_455 = this._cppExtensions.toName(dto);
        _builder.append(_name_455, "");
        _builder.append("::addTo");
        String _name_456 = this._cppExtensions.toName(feature_28);
        String _firstUpper_155 = StringExtensions.toFirstUpper(_name_456);
        _builder.append(_firstUpper_155, "");
        _builder.append("StringList(const ");
        String _typeName_70 = this._cppExtensions.toTypeName(feature_28);
        _builder.append(_typeName_70, "");
        _builder.append("& ");
        String _typeName_71 = this._cppExtensions.toTypeName(feature_28);
        String _firstLower_156 = StringExtensions.toFirstLower(_typeName_71);
        _builder.append(_firstLower_156, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("m");
        String _name_457 = this._cppExtensions.toName(feature_28);
        String _firstUpper_156 = StringExtensions.toFirstUpper(_name_457);
        _builder.append(_firstUpper_156, "    ");
        _builder.append("StringList.append(");
        String _typeName_72 = this._cppExtensions.toTypeName(feature_28);
        String _firstLower_157 = StringExtensions.toFirstLower(_typeName_72);
        _builder.append(_firstLower_157, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("emit addedTo");
        String _name_458 = this._cppExtensions.toName(feature_28);
        String _firstUpper_157 = StringExtensions.toFirstUpper(_name_458);
        _builder.append(_firstUpper_157, "    ");
        _builder.append("StringList(");
        String _typeName_73 = this._cppExtensions.toTypeName(feature_28);
        String _firstLower_158 = StringExtensions.toFirstLower(_typeName_73);
        _builder.append(_firstLower_158, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("void ");
        String _name_459 = this._cppExtensions.toName(dto);
        _builder.append(_name_459, "");
        _builder.append("::removeFrom");
        String _name_460 = this._cppExtensions.toName(feature_28);
        String _firstUpper_158 = StringExtensions.toFirstUpper(_name_460);
        _builder.append(_firstUpper_158, "");
        _builder.append("StringList(const ");
        String _typeName_74 = this._cppExtensions.toTypeName(feature_28);
        _builder.append(_typeName_74, "");
        _builder.append("& ");
        String _typeName_75 = this._cppExtensions.toTypeName(feature_28);
        String _firstLower_159 = StringExtensions.toFirstLower(_typeName_75);
        _builder.append(_firstLower_159, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("for (int i = 0; i < m");
        String _name_461 = this._cppExtensions.toName(feature_28);
        String _firstUpper_159 = StringExtensions.toFirstUpper(_name_461);
        _builder.append(_firstUpper_159, "    ");
        _builder.append("StringList.size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("if (m");
        String _name_462 = this._cppExtensions.toName(feature_28);
        String _firstUpper_160 = StringExtensions.toFirstUpper(_name_462);
        _builder.append(_firstUpper_160, "        ");
        _builder.append("StringList.at(i) == ");
        String _typeName_76 = this._cppExtensions.toTypeName(feature_28);
        String _firstLower_160 = StringExtensions.toFirstLower(_typeName_76);
        _builder.append(_firstLower_160, "        ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("m");
        String _name_463 = this._cppExtensions.toName(feature_28);
        String _firstUpper_161 = StringExtensions.toFirstUpper(_name_463);
        _builder.append(_firstUpper_161, "            ");
        _builder.append("StringList.removeAt(i);");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("emit removedFrom");
        String _name_464 = this._cppExtensions.toName(feature_28);
        String _firstUpper_162 = StringExtensions.toFirstUpper(_name_464);
        _builder.append(_firstUpper_162, "            ");
        _builder.append("StringList(");
        String _typeName_77 = this._cppExtensions.toTypeName(feature_28);
        String _firstLower_161 = StringExtensions.toFirstLower(_typeName_77);
        _builder.append(_firstLower_161, "            ");
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
        String _typeName_78 = this._cppExtensions.toTypeName(feature_28);
        _builder.append(_typeName_78, "    ");
        _builder.append("& not found in ");
        String _name_465 = this._cppExtensions.toName(feature_28);
        String _firstLower_162 = StringExtensions.toFirstLower(_name_465);
        _builder.append(_firstLower_162, "    ");
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("// TODO signal error");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("int ");
        String _name_466 = this._cppExtensions.toName(dto);
        _builder.append(_name_466, "");
        _builder.append("::");
        String _name_467 = this._cppExtensions.toName(feature_28);
        String _firstLower_163 = StringExtensions.toFirstLower(_name_467);
        _builder.append(_firstLower_163, "");
        _builder.append("Count()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("return m");
        String _name_468 = this._cppExtensions.toName(feature_28);
        String _firstUpper_163 = StringExtensions.toFirstUpper(_name_468);
        _builder.append(_firstUpper_163, "    ");
        _builder.append("StringList.size();");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("QStringList ");
        String _name_469 = this._cppExtensions.toName(dto);
        _builder.append(_name_469, "");
        _builder.append("::");
        String _name_470 = this._cppExtensions.toName(feature_28);
        _builder.append(_name_470, "");
        _builder.append("StringList()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return m");
        String _name_471 = this._cppExtensions.toName(feature_28);
        String _firstUpper_164 = StringExtensions.toFirstUpper(_name_471);
        _builder.append(_firstUpper_164, "\t");
        _builder.append("StringList;");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_472 = this._cppExtensions.toName(dto);
        _builder.append(_name_472, "");
        _builder.append("::set");
        String _name_473 = this._cppExtensions.toName(feature_28);
        String _firstUpper_165 = StringExtensions.toFirstUpper(_name_473);
        _builder.append(_firstUpper_165, "");
        _builder.append("StringList(const QStringList& ");
        String _name_474 = this._cppExtensions.toName(feature_28);
        _builder.append(_name_474, "");
        _builder.append(") ");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (");
        String _name_475 = this._cppExtensions.toName(feature_28);
        _builder.append(_name_475, "\t");
        _builder.append(" != m");
        String _name_476 = this._cppExtensions.toName(feature_28);
        String _firstUpper_166 = StringExtensions.toFirstUpper(_name_476);
        _builder.append(_firstUpper_166, "\t");
        _builder.append("StringList) {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("m");
        String _name_477 = this._cppExtensions.toName(feature_28);
        String _firstUpper_167 = StringExtensions.toFirstUpper(_name_477);
        _builder.append(_firstUpper_167, "\t\t");
        _builder.append("StringList = ");
        String _name_478 = this._cppExtensions.toName(feature_28);
        _builder.append(_name_478, "\t\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("emit ");
        String _name_479 = this._cppExtensions.toName(feature_28);
        _builder.append(_name_479, "\t\t");
        _builder.append("StringListChanged(");
        String _name_480 = this._cppExtensions.toName(feature_28);
        _builder.append(_name_480, "\t\t");
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
      List<? extends LFeature> _allFeatures_29 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_27 = new Function1<LFeature, Boolean>() {
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
      Iterable<? extends LFeature> _filter_27 = IterableExtensions.filter(_allFeatures_29, _function_27);
      for(final LFeature feature_29 : _filter_27) {
        CharSequence _foo_3 = this.foo(feature_29);
        _builder.append(_foo_3, "");
        _builder.newLineIfNotEmpty();
        _builder.append("void ");
        String _name_481 = this._cppExtensions.toName(dto);
        _builder.append(_name_481, "");
        _builder.append("::addTo");
        String _name_482 = this._cppExtensions.toName(feature_29);
        String _firstUpper_168 = StringExtensions.toFirstUpper(_name_482);
        _builder.append(_firstUpper_168, "");
        _builder.append("List(const ");
        String _typeName_79 = this._cppExtensions.toTypeName(feature_29);
        _builder.append(_typeName_79, "");
        _builder.append("& the");
        String _typeName_80 = this._cppExtensions.toTypeName(feature_29);
        String _firstUpper_169 = StringExtensions.toFirstUpper(_typeName_80);
        _builder.append(_firstUpper_169, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("m");
        String _name_483 = this._cppExtensions.toName(feature_29);
        String _firstUpper_170 = StringExtensions.toFirstUpper(_name_483);
        _builder.append(_firstUpper_170, "    ");
        _builder.append(".append(the");
        String _typeName_81 = this._cppExtensions.toTypeName(feature_29);
        String _firstUpper_171 = StringExtensions.toFirstUpper(_typeName_81);
        _builder.append(_firstUpper_171, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("emit addedTo");
        String _name_484 = this._cppExtensions.toName(feature_29);
        String _firstUpper_172 = StringExtensions.toFirstUpper(_name_484);
        _builder.append(_firstUpper_172, "    ");
        _builder.append("List(the");
        String _typeName_82 = this._cppExtensions.toTypeName(feature_29);
        String _firstUpper_173 = StringExtensions.toFirstUpper(_typeName_82);
        _builder.append(_firstUpper_173, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("void ");
        String _name_485 = this._cppExtensions.toName(dto);
        _builder.append(_name_485, "");
        _builder.append("::removeFrom");
        String _name_486 = this._cppExtensions.toName(feature_29);
        String _firstUpper_174 = StringExtensions.toFirstUpper(_name_486);
        _builder.append(_firstUpper_174, "");
        _builder.append("List(const ");
        String _typeName_83 = this._cppExtensions.toTypeName(feature_29);
        _builder.append(_typeName_83, "");
        _builder.append("& the");
        String _typeName_84 = this._cppExtensions.toTypeName(feature_29);
        String _firstUpper_175 = StringExtensions.toFirstUpper(_typeName_84);
        _builder.append(_firstUpper_175, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("for (int i = 0; i < m");
        String _name_487 = this._cppExtensions.toName(feature_29);
        String _firstUpper_176 = StringExtensions.toFirstUpper(_name_487);
        _builder.append(_firstUpper_176, "    ");
        _builder.append(".size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("if (m");
        String _name_488 = this._cppExtensions.toName(feature_29);
        String _firstUpper_177 = StringExtensions.toFirstUpper(_name_488);
        _builder.append(_firstUpper_177, "        ");
        _builder.append(".at(i) == the");
        String _typeName_85 = this._cppExtensions.toTypeName(feature_29);
        String _firstUpper_178 = StringExtensions.toFirstUpper(_typeName_85);
        _builder.append(_firstUpper_178, "        ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("m");
        String _name_489 = this._cppExtensions.toName(feature_29);
        String _firstUpper_179 = StringExtensions.toFirstUpper(_name_489);
        _builder.append(_firstUpper_179, "            ");
        _builder.append(".removeAt(i);");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("emit removedFrom");
        String _name_490 = this._cppExtensions.toName(feature_29);
        String _firstUpper_180 = StringExtensions.toFirstUpper(_name_490);
        _builder.append(_firstUpper_180, "            ");
        _builder.append("List(the");
        String _typeName_86 = this._cppExtensions.toTypeName(feature_29);
        String _firstUpper_181 = StringExtensions.toFirstUpper(_typeName_86);
        _builder.append(_firstUpper_181, "            ");
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
        String _typeName_87 = this._cppExtensions.toTypeName(feature_29);
        _builder.append(_typeName_87, "    ");
        _builder.append("& not found in ");
        String _name_491 = this._cppExtensions.toName(feature_29);
        String _firstLower_164 = StringExtensions.toFirstLower(_name_491);
        _builder.append(_firstLower_164, "    ");
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("// TODO signal error");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("int ");
        String _name_492 = this._cppExtensions.toName(dto);
        _builder.append(_name_492, "");
        _builder.append("::");
        String _name_493 = this._cppExtensions.toName(feature_29);
        String _firstLower_165 = StringExtensions.toFirstLower(_name_493);
        _builder.append(_firstLower_165, "");
        _builder.append("Count()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("return m");
        String _name_494 = this._cppExtensions.toName(feature_29);
        String _firstUpper_182 = StringExtensions.toFirstUpper(_name_494);
        _builder.append(_firstUpper_182, "    ");
        _builder.append(".size();");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("QList<");
        String _typeName_88 = this._cppExtensions.toTypeName(feature_29);
        _builder.append(_typeName_88, "");
        _builder.append("> ");
        String _name_495 = this._cppExtensions.toName(dto);
        _builder.append(_name_495, "");
        _builder.append("::");
        String _name_496 = this._cppExtensions.toName(feature_29);
        _builder.append(_name_496, "");
        _builder.append("()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("return m");
        String _name_497 = this._cppExtensions.toName(feature_29);
        String _firstUpper_183 = StringExtensions.toFirstUpper(_name_497);
        _builder.append(_firstUpper_183, "    ");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_498 = this._cppExtensions.toName(dto);
        _builder.append(_name_498, "");
        _builder.append("::set");
        String _name_499 = this._cppExtensions.toName(feature_29);
        String _firstUpper_184 = StringExtensions.toFirstUpper(_name_499);
        _builder.append(_firstUpper_184, "");
        _builder.append("(QList<");
        String _typeName_89 = this._cppExtensions.toTypeName(feature_29);
        _builder.append(_typeName_89, "");
        _builder.append("> ");
        String _name_500 = this._cppExtensions.toName(feature_29);
        _builder.append(_name_500, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("if (");
        String _name_501 = this._cppExtensions.toName(feature_29);
        _builder.append(_name_501, "    ");
        _builder.append(" != m");
        String _name_502 = this._cppExtensions.toName(feature_29);
        String _firstUpper_185 = StringExtensions.toFirstUpper(_name_502);
        _builder.append(_firstUpper_185, "    ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("m");
        String _name_503 = this._cppExtensions.toName(feature_29);
        String _firstUpper_186 = StringExtensions.toFirstUpper(_name_503);
        _builder.append(_firstUpper_186, "        ");
        _builder.append(" = ");
        String _name_504 = this._cppExtensions.toName(feature_29);
        _builder.append(_name_504, "        ");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("QVariantList variantList;");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("for (int i = 0; i < ");
        String _name_505 = this._cppExtensions.toName(feature_29);
        _builder.append(_name_505, "        ");
        _builder.append(".size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("variantList.append(");
        String _name_506 = this._cppExtensions.toName(feature_29);
        _builder.append(_name_506, "            ");
        _builder.append(".at(i));");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("emit ");
        String _name_507 = this._cppExtensions.toName(feature_29);
        _builder.append(_name_507, "        ");
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
        String _name_508 = this._cppExtensions.toName(dto);
        _builder.append(_name_508, "");
        _builder.append("::");
        String _name_509 = this._cppExtensions.toName(feature_29);
        _builder.append(_name_509, "");
        _builder.append("List()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("QVariantList variantList;");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("for (int i = 0; i < m");
        String _name_510 = this._cppExtensions.toName(feature_29);
        String _firstUpper_187 = StringExtensions.toFirstUpper(_name_510);
        _builder.append(_firstUpper_187, "    ");
        _builder.append(".size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("variantList.append(m");
        String _name_511 = this._cppExtensions.toName(feature_29);
        String _firstUpper_188 = StringExtensions.toFirstUpper(_name_511);
        _builder.append(_firstUpper_188, "        ");
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
        String _name_512 = this._cppExtensions.toName(dto);
        _builder.append(_name_512, "");
        _builder.append("::set");
        String _name_513 = this._cppExtensions.toName(feature_29);
        String _firstUpper_189 = StringExtensions.toFirstUpper(_name_513);
        _builder.append(_firstUpper_189, "");
        _builder.append("List(const QVariantList& ");
        String _name_514 = this._cppExtensions.toName(feature_29);
        _builder.append(_name_514, "");
        _builder.append(") ");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("m");
        String _name_515 = this._cppExtensions.toName(feature_29);
        String _firstUpper_190 = StringExtensions.toFirstUpper(_name_515);
        _builder.append(_firstUpper_190, "\t");
        _builder.append(".clear();");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("for (int i = 0; i < ");
        String _name_516 = this._cppExtensions.toName(feature_29);
        _builder.append(_name_516, "    ");
        _builder.append(".size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("m");
        String _name_517 = this._cppExtensions.toName(feature_29);
        String _firstUpper_191 = StringExtensions.toFirstUpper(_name_517);
        _builder.append(_firstUpper_191, "        ");
        _builder.append(".append(");
        String _name_518 = this._cppExtensions.toName(feature_29);
        _builder.append(_name_518, "        ");
        _builder.append(".at(i).to");
        String _mapToSingleType_3 = this._cppExtensions.mapToSingleType(feature_29);
        _builder.append(_mapToSingleType_3, "        ");
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
      List<? extends LFeature> _allFeatures_30 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_28 = new Function1<LFeature, Boolean>() {
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
      Iterable<? extends LFeature> _filter_28 = IterableExtensions.filter(_allFeatures_30, _function_28);
      for(final LFeature feature_30 : _filter_28) {
        CharSequence _foo_4 = this.foo(feature_30);
        _builder.append(_foo_4, "");
        _builder.append(" ");
        _builder.newLineIfNotEmpty();
        _builder.append("QVariantList ");
        String _name_519 = this._cppExtensions.toName(dto);
        _builder.append(_name_519, "");
        _builder.append("::");
        String _name_520 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_520, "");
        _builder.append("AsQVariantList()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("QVariantList ");
        String _name_521 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_521, "\t");
        _builder.append("List;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("for (int i = 0; i < m");
        String _name_522 = this._cppExtensions.toName(feature_30);
        String _firstUpper_192 = StringExtensions.toFirstUpper(_name_522);
        _builder.append(_firstUpper_192, "\t");
        _builder.append(".size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        String _name_523 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_523, "        ");
        _builder.append("List.append((m");
        String _name_524 = this._cppExtensions.toName(feature_30);
        String _firstUpper_193 = StringExtensions.toFirstUpper(_name_524);
        _builder.append(_firstUpper_193, "        ");
        _builder.append(".at(i))->toMap());");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return ");
        String _name_525 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_525, "\t");
        _builder.append("List;");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_526 = this._cppExtensions.toName(dto);
        _builder.append(_name_526, "");
        _builder.append("::addTo");
        String _name_527 = this._cppExtensions.toName(feature_30);
        String _firstUpper_194 = StringExtensions.toFirstUpper(_name_527);
        _builder.append(_firstUpper_194, "");
        _builder.append("(");
        String _typeName_90 = this._cppExtensions.toTypeName(feature_30);
        _builder.append(_typeName_90, "");
        _builder.append("* ");
        String _typeName_91 = this._cppExtensions.toTypeName(feature_30);
        String _firstLower_166 = StringExtensions.toFirstLower(_typeName_91);
        _builder.append(_firstLower_166, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("m");
        String _name_528 = this._cppExtensions.toName(feature_30);
        String _firstUpper_195 = StringExtensions.toFirstUpper(_name_528);
        _builder.append(_firstUpper_195, "    ");
        _builder.append(".append(");
        String _typeName_92 = this._cppExtensions.toTypeName(feature_30);
        String _firstLower_167 = StringExtensions.toFirstLower(_typeName_92);
        _builder.append(_firstLower_167, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("emit addedTo");
        String _name_529 = this._cppExtensions.toName(feature_30);
        String _firstUpper_196 = StringExtensions.toFirstUpper(_name_529);
        _builder.append(_firstUpper_196, "    ");
        _builder.append("(");
        String _typeName_93 = this._cppExtensions.toTypeName(feature_30);
        String _firstLower_168 = StringExtensions.toFirstLower(_typeName_93);
        _builder.append(_firstLower_168, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("void ");
        String _name_530 = this._cppExtensions.toName(dto);
        _builder.append(_name_530, "");
        _builder.append("::removeFrom");
        String _name_531 = this._cppExtensions.toName(feature_30);
        String _firstUpper_197 = StringExtensions.toFirstUpper(_name_531);
        _builder.append(_firstUpper_197, "");
        _builder.append("(");
        String _typeName_94 = this._cppExtensions.toTypeName(feature_30);
        _builder.append(_typeName_94, "");
        _builder.append("* ");
        String _typeName_95 = this._cppExtensions.toTypeName(feature_30);
        String _firstLower_169 = StringExtensions.toFirstLower(_typeName_95);
        _builder.append(_firstLower_169, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("for (int i = 0; i < m");
        String _name_532 = this._cppExtensions.toName(feature_30);
        String _firstUpper_198 = StringExtensions.toFirstUpper(_name_532);
        _builder.append(_firstUpper_198, "    ");
        _builder.append(".size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("if (m");
        String _name_533 = this._cppExtensions.toName(feature_30);
        String _firstUpper_199 = StringExtensions.toFirstUpper(_name_533);
        _builder.append(_firstUpper_199, "        ");
        _builder.append(".at(i) == ");
        String _typeName_96 = this._cppExtensions.toTypeName(feature_30);
        String _firstLower_170 = StringExtensions.toFirstLower(_typeName_96);
        _builder.append(_firstLower_170, "        ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("m");
        String _name_534 = this._cppExtensions.toName(feature_30);
        String _firstUpper_200 = StringExtensions.toFirstUpper(_name_534);
        _builder.append(_firstUpper_200, "            ");
        _builder.append(".removeAt(i);");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("emit removedFrom");
        String _name_535 = this._cppExtensions.toName(feature_30);
        String _firstUpper_201 = StringExtensions.toFirstUpper(_name_535);
        _builder.append(_firstUpper_201, "            ");
        _builder.append("(");
        String _typeName_97 = this._cppExtensions.toTypeName(feature_30);
        String _firstLower_171 = StringExtensions.toFirstLower(_typeName_97);
        _builder.append(_firstLower_171, "            ");
        _builder.append("->uuid());");
        _builder.newLineIfNotEmpty();
        {
          boolean _hasOpposite = this._cppExtensions.hasOpposite(feature_30);
          if (_hasOpposite) {
            _builder.append("            ");
            _builder.append("// ");
            String _name_536 = this._cppExtensions.toName(feature_30);
            _builder.append(_name_536, "            ");
            _builder.append(" are contained - so we must delete them");
            _builder.newLineIfNotEmpty();
            _builder.append("            ");
            String _typeName_98 = this._cppExtensions.toTypeName(feature_30);
            String _firstLower_172 = StringExtensions.toFirstLower(_typeName_98);
            _builder.append(_firstLower_172, "            ");
            _builder.append("->deleteLater();");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("            ");
            _builder.append("// ");
            String _name_537 = this._cppExtensions.toName(feature_30);
            _builder.append(_name_537, "            ");
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
        String _typeName_99 = this._cppExtensions.toTypeName(feature_30);
        _builder.append(_typeName_99, "    ");
        _builder.append("* not found in ");
        String _name_538 = this._cppExtensions.toName(feature_30);
        String _firstLower_173 = StringExtensions.toFirstLower(_name_538);
        _builder.append(_firstLower_173, "    ");
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("// TODO signal error");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("void ");
        String _name_539 = this._cppExtensions.toName(dto);
        _builder.append(_name_539, "");
        _builder.append("::addTo");
        String _name_540 = this._cppExtensions.toName(feature_30);
        String _firstUpper_202 = StringExtensions.toFirstUpper(_name_540);
        _builder.append(_firstUpper_202, "");
        _builder.append("FromMap(const QVariantMap& ");
        String _typeName_100 = this._cppExtensions.toTypeName(feature_30);
        String _firstLower_174 = StringExtensions.toFirstLower(_typeName_100);
        _builder.append(_firstLower_174, "");
        _builder.append("Map)");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        String _typeName_101 = this._cppExtensions.toTypeName(feature_30);
        _builder.append(_typeName_101, "    ");
        _builder.append("* ");
        String _typeName_102 = this._cppExtensions.toTypeName(feature_30);
        String _firstLower_175 = StringExtensions.toFirstLower(_typeName_102);
        _builder.append(_firstLower_175, "    ");
        _builder.append(" = new ");
        String _typeName_103 = this._cppExtensions.toTypeName(feature_30);
        _builder.append(_typeName_103, "    ");
        _builder.append("();");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        String _typeName_104 = this._cppExtensions.toTypeName(feature_30);
        String _firstLower_176 = StringExtensions.toFirstLower(_typeName_104);
        _builder.append(_firstLower_176, "    ");
        _builder.append("->setParent(this);");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        String _typeName_105 = this._cppExtensions.toTypeName(feature_30);
        String _firstLower_177 = StringExtensions.toFirstLower(_typeName_105);
        _builder.append(_firstLower_177, "    ");
        _builder.append("->fillFromMap(");
        String _typeName_106 = this._cppExtensions.toTypeName(feature_30);
        String _firstLower_178 = StringExtensions.toFirstLower(_typeName_106);
        _builder.append(_firstLower_178, "    ");
        _builder.append("Map);");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("m");
        String _name_541 = this._cppExtensions.toName(feature_30);
        String _firstUpper_203 = StringExtensions.toFirstUpper(_name_541);
        _builder.append(_firstUpper_203, "    ");
        _builder.append(".append(");
        String _typeName_107 = this._cppExtensions.toTypeName(feature_30);
        String _firstLower_179 = StringExtensions.toFirstLower(_typeName_107);
        _builder.append(_firstLower_179, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("emit addedTo");
        String _name_542 = this._cppExtensions.toName(feature_30);
        String _firstUpper_204 = StringExtensions.toFirstUpper(_name_542);
        _builder.append(_firstUpper_204, "    ");
        _builder.append("(");
        String _typeName_108 = this._cppExtensions.toTypeName(feature_30);
        String _firstLower_180 = StringExtensions.toFirstLower(_typeName_108);
        _builder.append(_firstLower_180, "    ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("void ");
        String _name_543 = this._cppExtensions.toName(dto);
        _builder.append(_name_543, "");
        _builder.append("::removeFrom");
        String _name_544 = this._cppExtensions.toName(feature_30);
        String _firstUpper_205 = StringExtensions.toFirstUpper(_name_544);
        _builder.append(_firstUpper_205, "");
        _builder.append("ByKey(const QString& uuid)");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("for (int i = 0; i < m");
        String _name_545 = this._cppExtensions.toName(feature_30);
        String _firstUpper_206 = StringExtensions.toFirstUpper(_name_545);
        _builder.append(_firstUpper_206, "    ");
        _builder.append(".size(); ++i) {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("if ((m");
        String _name_546 = this._cppExtensions.toName(feature_30);
        String _firstUpper_207 = StringExtensions.toFirstUpper(_name_546);
        _builder.append(_firstUpper_207, "        ");
        _builder.append(".at(i))->toMap().value(uuidKey).toString() == uuid) {");
        _builder.newLineIfNotEmpty();
        {
          boolean _hasOpposite_1 = this._cppExtensions.hasOpposite(feature_30);
          if (_hasOpposite_1) {
            _builder.append("            ");
            String _typeName_109 = this._cppExtensions.toTypeName(feature_30);
            _builder.append(_typeName_109, "            ");
            _builder.append("* ");
            String _typeName_110 = this._cppExtensions.toTypeName(feature_30);
            String _firstLower_181 = StringExtensions.toFirstLower(_typeName_110);
            _builder.append(_firstLower_181, "            ");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            _builder.append("            ");
            String _typeName_111 = this._cppExtensions.toTypeName(feature_30);
            String _firstLower_182 = StringExtensions.toFirstLower(_typeName_111);
            _builder.append(_firstLower_182, "            ");
            _builder.append(" = m");
            String _name_547 = this._cppExtensions.toName(feature_30);
            String _firstUpper_208 = StringExtensions.toFirstUpper(_name_547);
            _builder.append(_firstUpper_208, "            ");
            _builder.append(".at(i);");
            _builder.newLineIfNotEmpty();
            _builder.append("            ");
            _builder.append("m");
            String _name_548 = this._cppExtensions.toName(feature_30);
            String _firstUpper_209 = StringExtensions.toFirstUpper(_name_548);
            _builder.append(_firstUpper_209, "            ");
            _builder.append(".removeAt(i);");
            _builder.newLineIfNotEmpty();
            _builder.append("            ");
            _builder.append("emit removedFrom");
            String _name_549 = this._cppExtensions.toName(feature_30);
            String _firstUpper_210 = StringExtensions.toFirstUpper(_name_549);
            _builder.append(_firstUpper_210, "            ");
            _builder.append("(uuid);");
            _builder.newLineIfNotEmpty();
            _builder.append("            ");
            _builder.append("// ");
            String _name_550 = this._cppExtensions.toName(feature_30);
            _builder.append(_name_550, "            ");
            _builder.append(" are contained - so we must delete them");
            _builder.newLineIfNotEmpty();
            _builder.append("            ");
            String _typeName_112 = this._cppExtensions.toTypeName(feature_30);
            String _firstLower_183 = StringExtensions.toFirstLower(_typeName_112);
            _builder.append(_firstLower_183, "            ");
            _builder.append("->deleteLater();");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("            ");
            _builder.append("m");
            String _name_551 = this._cppExtensions.toName(feature_30);
            String _firstUpper_211 = StringExtensions.toFirstUpper(_name_551);
            _builder.append(_firstUpper_211, "            ");
            _builder.append(".removeAt(i);");
            _builder.newLineIfNotEmpty();
            _builder.append("            ");
            _builder.append("emit removedFrom");
            String _name_552 = this._cppExtensions.toName(feature_30);
            String _firstUpper_212 = StringExtensions.toFirstUpper(_name_552);
            _builder.append(_firstUpper_212, "            ");
            _builder.append("(uuid);");
            _builder.newLineIfNotEmpty();
            _builder.append("            ");
            _builder.append("// ");
            String _name_553 = this._cppExtensions.toName(feature_30);
            _builder.append(_name_553, "            ");
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
        String _name_554 = this._cppExtensions.toName(feature_30);
        String _firstLower_184 = StringExtensions.toFirstLower(_name_554);
        _builder.append(_firstLower_184, "    ");
        _builder.append(": \" << uuid;");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("// TODO signal error");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("int ");
        String _name_555 = this._cppExtensions.toName(dto);
        _builder.append(_name_555, "");
        _builder.append("::");
        String _name_556 = this._cppExtensions.toName(feature_30);
        String _firstLower_185 = StringExtensions.toFirstLower(_name_556);
        _builder.append(_firstLower_185, "");
        _builder.append("Count()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("return m");
        String _name_557 = this._cppExtensions.toName(feature_30);
        String _firstUpper_213 = StringExtensions.toFirstUpper(_name_557);
        _builder.append(_firstUpper_213, "    ");
        _builder.append(".size();");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("QList<");
        String _typeName_113 = this._cppExtensions.toTypeName(feature_30);
        _builder.append(_typeName_113, "");
        _builder.append("*> ");
        String _name_558 = this._cppExtensions.toName(dto);
        _builder.append(_name_558, "");
        _builder.append("::");
        String _name_559 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_559, "");
        _builder.append("()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return m");
        String _name_560 = this._cppExtensions.toName(feature_30);
        String _firstUpper_214 = StringExtensions.toFirstUpper(_name_560);
        _builder.append(_firstUpper_214, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_561 = this._cppExtensions.toName(dto);
        _builder.append(_name_561, "");
        _builder.append("::set");
        String _name_562 = this._cppExtensions.toName(feature_30);
        String _firstUpper_215 = StringExtensions.toFirstUpper(_name_562);
        _builder.append(_firstUpper_215, "");
        _builder.append("(QList<");
        String _typeName_114 = this._cppExtensions.toTypeName(feature_30);
        _builder.append(_typeName_114, "");
        _builder.append("*> ");
        String _name_563 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_563, "");
        _builder.append(") ");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (");
        String _name_564 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_564, "\t");
        _builder.append(" != m");
        String _name_565 = this._cppExtensions.toName(feature_30);
        String _firstUpper_216 = StringExtensions.toFirstUpper(_name_565);
        _builder.append(_firstUpper_216, "\t");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("m");
        String _name_566 = this._cppExtensions.toName(feature_30);
        String _firstUpper_217 = StringExtensions.toFirstUpper(_name_566);
        _builder.append(_firstUpper_217, "\t\t");
        _builder.append(" = ");
        String _name_567 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_567, "\t\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("emit ");
        String _name_568 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_568, "\t\t");
        _builder.append("Changed(");
        String _name_569 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_569, "\t\t");
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
        String _name_570 = this._cppExtensions.toName(dto);
        String _firstLower_186 = StringExtensions.toFirstLower(_name_570);
        _builder.append(_firstLower_186, " ");
        _builder.append(".");
        String _name_571 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_571, " ");
        _builder.append("PropertyList.length to get the size");
        _builder.newLineIfNotEmpty();
        _builder.append(" ");
        _builder.append("* ");
        String _name_572 = this._cppExtensions.toName(dto);
        String _firstLower_187 = StringExtensions.toFirstLower(_name_572);
        _builder.append(_firstLower_187, " ");
        _builder.append(".");
        String _name_573 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_573, " ");
        _builder.append("PropertyList[2] to get ");
        String _typeName_115 = this._cppExtensions.toTypeName(feature_30);
        _builder.append(_typeName_115, " ");
        _builder.append("* at position 2");
        _builder.newLineIfNotEmpty();
        _builder.append(" ");
        _builder.append("* ");
        String _name_574 = this._cppExtensions.toName(dto);
        String _firstLower_188 = StringExtensions.toFirstLower(_name_574);
        _builder.append(_firstLower_188, " ");
        _builder.append(".");
        String _name_575 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_575, " ");
        _builder.append("PropertyList = [] to clear the list");
        _builder.newLineIfNotEmpty();
        _builder.append(" ");
        _builder.append("* or get easy access to properties like");
        _builder.newLine();
        _builder.append(" ");
        _builder.append("* ");
        String _name_576 = this._cppExtensions.toName(dto);
        String _firstLower_189 = StringExtensions.toFirstLower(_name_576);
        _builder.append(_firstLower_189, " ");
        _builder.append(".");
        String _name_577 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_577, " ");
        _builder.append("PropertyList[2].myPropertyName");
        _builder.newLineIfNotEmpty();
        _builder.append(" ");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("QDeclarativeListProperty<");
        String _typeName_116 = this._cppExtensions.toTypeName(feature_30);
        _builder.append(_typeName_116, "");
        _builder.append("> ");
        String _name_578 = this._cppExtensions.toName(dto);
        _builder.append(_name_578, "");
        _builder.append("::");
        String _name_579 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_579, "");
        _builder.append("PropertyList()");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("return QDeclarativeListProperty<");
        String _typeName_117 = this._cppExtensions.toTypeName(feature_30);
        _builder.append(_typeName_117, "    ");
        _builder.append(">(this, 0, &");
        String _name_580 = this._cppExtensions.toName(dto);
        _builder.append(_name_580, "    ");
        _builder.append("::appendTo");
        String _name_581 = this._cppExtensions.toName(feature_30);
        String _firstUpper_218 = StringExtensions.toFirstUpper(_name_581);
        _builder.append(_firstUpper_218, "    ");
        _builder.append("Property,");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("&");
        String _name_582 = this._cppExtensions.toName(dto);
        _builder.append(_name_582, "            ");
        _builder.append("::");
        String _name_583 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_583, "            ");
        _builder.append("PropertyCount, &");
        String _name_584 = this._cppExtensions.toName(dto);
        _builder.append(_name_584, "            ");
        _builder.append("::at");
        String _name_585 = this._cppExtensions.toName(feature_30);
        String _firstUpper_219 = StringExtensions.toFirstUpper(_name_585);
        _builder.append(_firstUpper_219, "            ");
        _builder.append("Property,");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("&");
        String _name_586 = this._cppExtensions.toName(dto);
        _builder.append(_name_586, "            ");
        _builder.append("::clear");
        String _name_587 = this._cppExtensions.toName(feature_30);
        String _firstUpper_220 = StringExtensions.toFirstUpper(_name_587);
        _builder.append(_firstUpper_220, "            ");
        _builder.append("Property);");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_588 = this._cppExtensions.toName(dto);
        _builder.append(_name_588, "");
        _builder.append("::appendTo");
        String _name_589 = this._cppExtensions.toName(feature_30);
        String _firstUpper_221 = StringExtensions.toFirstUpper(_name_589);
        _builder.append(_firstUpper_221, "");
        _builder.append("Property(QDeclarativeListProperty<");
        String _typeName_118 = this._cppExtensions.toTypeName(feature_30);
        _builder.append(_typeName_118, "");
        _builder.append("> *");
        String _name_590 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_590, "");
        _builder.append("List,");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        String _typeName_119 = this._cppExtensions.toTypeName(feature_30);
        _builder.append(_typeName_119, "        ");
        _builder.append("* ");
        String _typeName_120 = this._cppExtensions.toTypeName(feature_30);
        String _firstLower_190 = StringExtensions.toFirstLower(_typeName_120);
        _builder.append(_firstLower_190, "        ");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        String _name_591 = this._cppExtensions.toName(dto);
        _builder.append(_name_591, "    ");
        _builder.append(" *");
        String _name_592 = this._cppExtensions.toName(dto);
        String _firstLower_191 = StringExtensions.toFirstLower(_name_592);
        _builder.append(_firstLower_191, "    ");
        _builder.append("Object = qobject_cast<");
        String _name_593 = this._cppExtensions.toName(dto);
        _builder.append(_name_593, "    ");
        _builder.append(" *>(");
        String _name_594 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_594, "    ");
        _builder.append("List->object);");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("if (");
        String _name_595 = this._cppExtensions.toName(dto);
        String _firstLower_192 = StringExtensions.toFirstLower(_name_595);
        _builder.append(_firstLower_192, "    ");
        _builder.append("Object) {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        String _typeName_121 = this._cppExtensions.toTypeName(feature_30);
        String _firstLower_193 = StringExtensions.toFirstLower(_typeName_121);
        _builder.append(_firstLower_193, "        ");
        _builder.append("->setParent(");
        String _name_596 = this._cppExtensions.toName(dto);
        String _firstLower_194 = StringExtensions.toFirstLower(_name_596);
        _builder.append(_firstLower_194, "        ");
        _builder.append("Object);");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        String _name_597 = this._cppExtensions.toName(dto);
        String _firstLower_195 = StringExtensions.toFirstLower(_name_597);
        _builder.append(_firstLower_195, "        ");
        _builder.append("Object->m");
        String _name_598 = this._cppExtensions.toName(feature_30);
        String _firstUpper_222 = StringExtensions.toFirstUpper(_name_598);
        _builder.append(_firstUpper_222, "        ");
        _builder.append(".append(");
        String _typeName_122 = this._cppExtensions.toTypeName(feature_30);
        String _firstLower_196 = StringExtensions.toFirstLower(_typeName_122);
        _builder.append(_firstLower_196, "        ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("emit ");
        String _name_599 = this._cppExtensions.toName(dto);
        String _firstLower_197 = StringExtensions.toFirstLower(_name_599);
        _builder.append(_firstLower_197, "        ");
        _builder.append("Object->addedTo");
        String _name_600 = this._cppExtensions.toName(feature_30);
        String _firstUpper_223 = StringExtensions.toFirstUpper(_name_600);
        _builder.append(_firstUpper_223, "        ");
        _builder.append("(");
        String _typeName_123 = this._cppExtensions.toTypeName(feature_30);
        String _firstLower_198 = StringExtensions.toFirstLower(_typeName_123);
        _builder.append(_firstLower_198, "        ");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("} else {");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("qWarning() << \"cannot append ");
        String _typeName_124 = this._cppExtensions.toTypeName(feature_30);
        _builder.append(_typeName_124, "        ");
        _builder.append("* to ");
        String _name_601 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_601, "        ");
        _builder.append(" \" << \"Object is not of type ");
        String _name_602 = this._cppExtensions.toName(dto);
        _builder.append(_name_602, "        ");
        _builder.append("*\";");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("int ");
        String _name_603 = this._cppExtensions.toName(dto);
        _builder.append(_name_603, "");
        _builder.append("::");
        String _name_604 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_604, "");
        _builder.append("PropertyCount(QDeclarativeListProperty<");
        String _typeName_125 = this._cppExtensions.toTypeName(feature_30);
        _builder.append(_typeName_125, "");
        _builder.append("> *");
        String _name_605 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_605, "");
        _builder.append("List)");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("qDebug() << \"");
        String _name_606 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_606, "    ");
        _builder.append("PropertyCount\";");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        String _name_607 = this._cppExtensions.toName(dto);
        _builder.append(_name_607, "    ");
        _builder.append(" *");
        String _name_608 = this._cppExtensions.toName(dto);
        String _firstLower_199 = StringExtensions.toFirstLower(_name_608);
        _builder.append(_firstLower_199, "    ");
        _builder.append(" = qobject_cast<");
        String _name_609 = this._cppExtensions.toName(dto);
        _builder.append(_name_609, "    ");
        _builder.append(" *>(");
        String _name_610 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_610, "    ");
        _builder.append("List->object);");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("if (");
        String _name_611 = this._cppExtensions.toName(dto);
        String _firstLower_200 = StringExtensions.toFirstLower(_name_611);
        _builder.append(_firstLower_200, "    ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("return ");
        String _name_612 = this._cppExtensions.toName(dto);
        String _firstLower_201 = StringExtensions.toFirstLower(_name_612);
        _builder.append(_firstLower_201, "        ");
        _builder.append("->m");
        String _name_613 = this._cppExtensions.toName(feature_30);
        String _firstUpper_224 = StringExtensions.toFirstUpper(_name_613);
        _builder.append(_firstUpper_224, "        ");
        _builder.append(".size();");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("} else {");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("qWarning() << \"cannot get size ");
        String _name_614 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_614, "        ");
        _builder.append(" \" << \"Object is not of type ");
        String _name_615 = this._cppExtensions.toName(dto);
        _builder.append(_name_615, "        ");
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
        String _typeName_126 = this._cppExtensions.toTypeName(feature_30);
        _builder.append(_typeName_126, "");
        _builder.append("* ");
        String _name_616 = this._cppExtensions.toName(dto);
        _builder.append(_name_616, "");
        _builder.append("::at");
        String _name_617 = this._cppExtensions.toName(feature_30);
        String _firstUpper_225 = StringExtensions.toFirstUpper(_name_617);
        _builder.append(_firstUpper_225, "");
        _builder.append("Property(QDeclarativeListProperty<");
        String _typeName_127 = this._cppExtensions.toTypeName(feature_30);
        _builder.append(_typeName_127, "");
        _builder.append("> *");
        String _name_618 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_618, "");
        _builder.append("List, int pos)");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("qDebug() << \"at");
        String _name_619 = this._cppExtensions.toName(feature_30);
        String _firstUpper_226 = StringExtensions.toFirstUpper(_name_619);
        _builder.append(_firstUpper_226, "    ");
        _builder.append("Property #\" << pos;");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        String _name_620 = this._cppExtensions.toName(dto);
        _builder.append(_name_620, "    ");
        _builder.append(" *");
        String _name_621 = this._cppExtensions.toName(dto);
        String _firstLower_202 = StringExtensions.toFirstLower(_name_621);
        _builder.append(_firstLower_202, "    ");
        _builder.append(" = qobject_cast<");
        String _name_622 = this._cppExtensions.toName(dto);
        _builder.append(_name_622, "    ");
        _builder.append(" *>(");
        String _name_623 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_623, "    ");
        _builder.append("List->object);");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("if (");
        String _name_624 = this._cppExtensions.toName(dto);
        String _firstLower_203 = StringExtensions.toFirstLower(_name_624);
        _builder.append(_firstLower_203, "    ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("if (");
        String _name_625 = this._cppExtensions.toName(dto);
        String _firstLower_204 = StringExtensions.toFirstLower(_name_625);
        _builder.append(_firstLower_204, "        ");
        _builder.append("->m");
        String _name_626 = this._cppExtensions.toName(feature_30);
        String _firstUpper_227 = StringExtensions.toFirstUpper(_name_626);
        _builder.append(_firstUpper_227, "        ");
        _builder.append(".size() > pos) {");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("return ");
        String _name_627 = this._cppExtensions.toName(dto);
        String _firstLower_205 = StringExtensions.toFirstLower(_name_627);
        _builder.append(_firstLower_205, "            ");
        _builder.append("->m");
        String _name_628 = this._cppExtensions.toName(feature_30);
        String _firstUpper_228 = StringExtensions.toFirstUpper(_name_628);
        _builder.append(_firstUpper_228, "            ");
        _builder.append(".at(pos);");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("qWarning() << \"cannot get ");
        String _typeName_128 = this._cppExtensions.toTypeName(feature_30);
        _builder.append(_typeName_128, "        ");
        _builder.append("* at pos \" << pos << \" size is \"");
        _builder.newLineIfNotEmpty();
        _builder.append("                ");
        _builder.append("<< ");
        String _name_629 = this._cppExtensions.toName(dto);
        String _firstLower_206 = StringExtensions.toFirstLower(_name_629);
        _builder.append(_firstLower_206, "                ");
        _builder.append("->m");
        String _name_630 = this._cppExtensions.toName(feature_30);
        String _firstUpper_229 = StringExtensions.toFirstUpper(_name_630);
        _builder.append(_firstUpper_229, "                ");
        _builder.append(".size();");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("} else {");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("qWarning() << \"cannot get ");
        String _typeName_129 = this._cppExtensions.toTypeName(feature_30);
        _builder.append(_typeName_129, "        ");
        _builder.append("* at pos \" << pos << \"Object is not of type ");
        String _name_631 = this._cppExtensions.toName(dto);
        _builder.append(_name_631, "        ");
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
        String _name_632 = this._cppExtensions.toName(dto);
        _builder.append(_name_632, "");
        _builder.append("::clear");
        String _name_633 = this._cppExtensions.toName(feature_30);
        String _firstUpper_230 = StringExtensions.toFirstUpper(_name_633);
        _builder.append(_firstUpper_230, "");
        _builder.append("Property(QDeclarativeListProperty<");
        String _typeName_130 = this._cppExtensions.toTypeName(feature_30);
        _builder.append(_typeName_130, "");
        _builder.append("> *");
        String _name_634 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_634, "");
        _builder.append("List)");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("    ");
        String _name_635 = this._cppExtensions.toName(dto);
        _builder.append(_name_635, "    ");
        _builder.append(" *");
        String _name_636 = this._cppExtensions.toName(dto);
        String _firstLower_207 = StringExtensions.toFirstLower(_name_636);
        _builder.append(_firstLower_207, "    ");
        _builder.append(" = qobject_cast<");
        String _name_637 = this._cppExtensions.toName(dto);
        _builder.append(_name_637, "    ");
        _builder.append(" *>(");
        String _name_638 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_638, "    ");
        _builder.append("List->object);");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("if (");
        String _name_639 = this._cppExtensions.toName(dto);
        String _firstLower_208 = StringExtensions.toFirstLower(_name_639);
        _builder.append(_firstLower_208, "    ");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        {
          boolean _hasOpposite_2 = this._cppExtensions.hasOpposite(feature_30);
          if (_hasOpposite_2) {
            _builder.append("        ");
            _builder.append("// ");
            String _name_640 = this._cppExtensions.toName(feature_30);
            _builder.append(_name_640, "        ");
            _builder.append(" are contained - so we must delete them");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            _builder.append("for (int i = 0; i < ");
            String _name_641 = this._cppExtensions.toName(dto);
            String _firstLower_209 = StringExtensions.toFirstLower(_name_641);
            _builder.append(_firstLower_209, "        ");
            _builder.append("->m");
            String _name_642 = this._cppExtensions.toName(feature_30);
            String _firstUpper_231 = StringExtensions.toFirstUpper(_name_642);
            _builder.append(_firstUpper_231, "        ");
            _builder.append(".size(); ++i) {");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            _builder.append("    ");
            String _name_643 = this._cppExtensions.toName(dto);
            String _firstLower_210 = StringExtensions.toFirstLower(_name_643);
            _builder.append(_firstLower_210, "            ");
            _builder.append("->m");
            String _name_644 = this._cppExtensions.toName(feature_30);
            String _firstUpper_232 = StringExtensions.toFirstUpper(_name_644);
            _builder.append(_firstUpper_232, "            ");
            _builder.append(".at(i)->deleteLater();");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            _builder.append("}");
            _builder.newLine();
          } else {
            _builder.append("        ");
            _builder.append("// ");
            String _name_645 = this._cppExtensions.toName(feature_30);
            _builder.append(_name_645, "        ");
            _builder.append(" are independent - DON\'T delete them");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("        ");
        String _name_646 = this._cppExtensions.toName(dto);
        String _firstLower_211 = StringExtensions.toFirstLower(_name_646);
        _builder.append(_firstLower_211, "        ");
        _builder.append("->m");
        String _name_647 = this._cppExtensions.toName(feature_30);
        String _firstUpper_233 = StringExtensions.toFirstUpper(_name_647);
        _builder.append(_firstUpper_233, "        ");
        _builder.append(".clear();");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("} else {");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("qWarning() << \"cannot clear ");
        String _name_648 = this._cppExtensions.toName(feature_30);
        _builder.append(_name_648, "        ");
        _builder.append(" \" << \"Object is not of type ");
        String _name_649 = this._cppExtensions.toName(dto);
        _builder.append(_name_649, "        ");
        _builder.append("*\";");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.newLine();
    {
      boolean _isTree = this._cppExtensions.isTree(dto);
      if (_isTree) {
        _builder.append("// it\'s a Tree of ");
        String _name_650 = this._cppExtensions.toName(dto);
        _builder.append(_name_650, "");
        _builder.append("*");
        _builder.newLineIfNotEmpty();
        _builder.append("// get a flat list of all children");
        _builder.newLine();
        _builder.append("QList<QObject*> ");
        String _name_651 = this._cppExtensions.toName(dto);
        _builder.append(_name_651, "");
        _builder.append("::all");
        String _name_652 = this._cppExtensions.toName(dto);
        _builder.append(_name_652, "");
        _builder.append("Children(){");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("QList<QObject*> allChildren;");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("for (int i = 0; i < this->children().size(); ++i) {");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("if (qobject_cast<");
        String _name_653 = this._cppExtensions.toName(dto);
        _builder.append(_name_653, "        ");
        _builder.append("*>(this->children().at(i))) {");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("allChildren.append(this->children().at(i));");
        _builder.newLine();
        _builder.append("            ");
        String _name_654 = this._cppExtensions.toName(dto);
        _builder.append(_name_654, "            ");
        _builder.append("* ");
        String _name_655 = this._cppExtensions.toName(dto);
        String _firstLower_212 = StringExtensions.toFirstLower(_name_655);
        _builder.append(_firstLower_212, "            ");
        _builder.append(" = (");
        String _name_656 = this._cppExtensions.toName(dto);
        _builder.append(_name_656, "            ");
        _builder.append("*)this->children().at(i);");
        _builder.newLineIfNotEmpty();
        _builder.append("            ");
        _builder.append("allChildren.append(");
        String _name_657 = this._cppExtensions.toName(dto);
        String _firstLower_213 = StringExtensions.toFirstLower(_name_657);
        _builder.append(_firstLower_213, "            ");
        _builder.append("->all");
        String _name_658 = this._cppExtensions.toName(dto);
        _builder.append(_name_658, "            ");
        _builder.append("Children());");
        _builder.newLineIfNotEmpty();
        _builder.append("        ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("return allChildren;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.newLine();
    String _name_659 = this._cppExtensions.toName(dto);
    _builder.append(_name_659, "");
    _builder.append("::~");
    String _name_660 = this._cppExtensions.toName(dto);
    _builder.append(_name_660, "");
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
    _builder.append("// REF");
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
