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

import com.google.inject.Inject;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.lunifera.dsl.ext.dtos.cpp.qt.CppExtensions;
import org.lunifera.dsl.semantic.common.types.LFeature;
import org.lunifera.dsl.semantic.dto.LDto;

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
    _builder.newLine();
    _builder.append("// keys of QVariantMap used in this APP");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures = dto.getAllFeatures();
      for(final LFeature feature : _allFeatures) {
        _builder.append("static const QString ");
        String _name_1 = this._cppExtensions.toName(feature);
        _builder.append(_name_1, "");
        _builder.append("Key = \"");
        String _name_2 = this._cppExtensions.toName(feature);
        _builder.append(_name_2, "");
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("// keys used from Server API etc");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_1 = dto.getAllFeatures();
      for(final LFeature feature_1 : _allFeatures_1) {
        _builder.append("static const QString ");
        String _name_3 = this._cppExtensions.toName(feature_1);
        _builder.append(_name_3, "");
        _builder.append("ForeignKey = \"");
        String _name_4 = this._cppExtensions.toName(feature_1);
        _builder.append(_name_4, "");
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Default Constructor if ");
    String _name_5 = this._cppExtensions.toName(dto);
    _builder.append(_name_5, " ");
    _builder.append(" not initialized from QVariantMap");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    String _name_6 = this._cppExtensions.toName(dto);
    _builder.append(_name_6, "");
    _builder.append("::");
    String _name_7 = this._cppExtensions.toName(dto);
    _builder.append(_name_7, "");
    _builder.append("(QObject *parent) :");
    _builder.newLineIfNotEmpty();
    _builder.append("        ");
    _builder.append("QObject()");
    {
      List<? extends LFeature> _allFeatures_2 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _isToMany = CppGenerator.this._cppExtensions.isToMany(it);
          return Boolean.valueOf((!_isToMany));
        }
      };
      Iterable<? extends LFeature> _filter = IterableExtensions.filter(_allFeatures_2, _function);
      for(final LFeature feature_2 : _filter) {
        _builder.append(", m");
        String _name_8 = this._cppExtensions.toName(feature_2);
        String _firstUpper = StringExtensions.toFirstUpper(_name_8);
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
    _builder.append("* Special Constructor to initialize ");
    String _name_9 = this._cppExtensions.toName(dto);
    _builder.append(_name_9, " ");
    _builder.append(" from QVariantMap");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("* Map got from JsonDataAccess or so");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    String _name_10 = this._cppExtensions.toName(dto);
    _builder.append(_name_10, "");
    _builder.append("::");
    String _name_11 = this._cppExtensions.toName(dto);
    _builder.append(_name_11, "");
    _builder.append("(QVariantMap ");
    String _name_12 = this._cppExtensions.toName(dto);
    String _firstLower = StringExtensions.toFirstLower(_name_12);
    _builder.append(_firstLower, "");
    _builder.append("Map) :");
    _builder.newLineIfNotEmpty();
    _builder.append("        ");
    _builder.append("QObject(), m");
    String _name_13 = this._cppExtensions.toName(dto);
    String _firstUpper_1 = StringExtensions.toFirstUpper(_name_13);
    _builder.append(_firstUpper_1, "        ");
    _builder.append("Map(");
    String _name_14 = this._cppExtensions.toName(dto);
    String _firstLower_1 = StringExtensions.toFirstLower(_name_14);
    _builder.append(_firstLower_1, "        ");
    _builder.append("Map)");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
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
          String _name_15 = this._cppExtensions.toName(dto);
          boolean _endsWith = _name_15.endsWith("DTO");
          if (_endsWith) {
            _builder.append("\t");
            _builder.append("m");
            String _name_16 = this._cppExtensions.toName(feature_3);
            String _firstUpper_2 = StringExtensions.toFirstUpper(_name_16);
            _builder.append(_firstUpper_2, "\t");
            _builder.append(" = ");
            String _typeName = this._cppExtensions.toTypeName(feature_3);
            _builder.append(_typeName, "\t");
            _builder.append("(m");
            String _name_17 = this._cppExtensions.toName(dto);
            String _firstUpper_3 = StringExtensions.toFirstUpper(_name_17);
            _builder.append(_firstUpper_3, "\t");
            _builder.append("Map.value(");
            String _name_18 = this._cppExtensions.toName(feature_3);
            _builder.append(_name_18, "\t");
            _builder.append("Key).to");
            String _mapToType = this._cppExtensions.mapToType(feature_3);
            _builder.append(_mapToType, "\t");
            _builder.append("());");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t");
            _builder.append("m");
            String _name_19 = this._cppExtensions.toName(feature_3);
            String _firstUpper_4 = StringExtensions.toFirstUpper(_name_19);
            _builder.append(_firstUpper_4, "\t");
            _builder.append(" = m");
            String _name_20 = this._cppExtensions.toName(dto);
            String _firstUpper_5 = StringExtensions.toFirstUpper(_name_20);
            _builder.append(_firstUpper_5, "\t");
            _builder.append("Map.value(");
            String _name_21 = this._cppExtensions.toName(feature_3);
            _builder.append(_name_21, "\t");
            _builder.append("Key).to");
            String _mapToType_1 = this._cppExtensions.mapToType(feature_3);
            _builder.append(_mapToType_1, "\t");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
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
        _builder.append("m");
        String _name_22 = this._cppExtensions.toName(feature_4);
        String _firstUpper_6 = StringExtensions.toFirstUpper(_name_22);
        _builder.append(_firstUpper_6, "\t");
        _builder.append(" = m");
        String _name_23 = this._cppExtensions.toName(dto);
        String _firstUpper_7 = StringExtensions.toFirstUpper(_name_23);
        _builder.append(_firstUpper_7, "\t");
        _builder.append("Map.value(");
        String _name_24 = this._cppExtensions.toName(feature_4);
        _builder.append(_name_24, "\t");
        _builder.append("Key).toList();");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Exports Properties from ");
    String _name_25 = this._cppExtensions.toName(dto);
    _builder.append(_name_25, " ");
    _builder.append(" as QVariantMap");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("* To store Data in JsonDataAccess or so");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("QVariantMap ");
    String _name_26 = this._cppExtensions.toName(dto);
    _builder.append(_name_26, "");
    _builder.append("::toMap()");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_5 = dto.getAllFeatures();
      for(final LFeature feature_5 : _allFeatures_5) {
        _builder.append("\t");
        _builder.append("m");
        String _name_27 = this._cppExtensions.toName(dto);
        String _firstUpper_8 = StringExtensions.toFirstUpper(_name_27);
        _builder.append(_firstUpper_8, "\t");
        _builder.append("Map.insert(");
        String _name_28 = this._cppExtensions.toName(feature_5);
        _builder.append(_name_28, "\t");
        _builder.append("Key, m");
        String _name_29 = this._cppExtensions.toName(feature_5);
        String _firstUpper_9 = StringExtensions.toFirstUpper(_name_29);
        _builder.append(_firstUpper_9, "\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("return m");
    String _name_30 = this._cppExtensions.toName(dto);
    String _firstUpper_10 = StringExtensions.toFirstUpper(_name_30);
    _builder.append(_firstUpper_10, "\t");
    _builder.append("Map;");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Exports Properties from ");
    String _name_31 = this._cppExtensions.toName(dto);
    _builder.append(_name_31, " ");
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
    String _name_32 = this._cppExtensions.toName(dto);
    _builder.append(_name_32, "");
    _builder.append("::toForeignMap()");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("QVariantMap foreignMap;");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_6 = dto.getAllFeatures();
      for(final LFeature feature_6 : _allFeatures_6) {
        _builder.append("\t");
        _builder.append("foreignMap.insert(");
        String _name_33 = this._cppExtensions.toName(feature_6);
        _builder.append(_name_33, "\t");
        _builder.append("ForeignKey, m");
        String _name_34 = this._cppExtensions.toName(feature_6);
        String _firstUpper_11 = StringExtensions.toFirstUpper(_name_34);
        _builder.append(_firstUpper_11, "\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("return foreignMap;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_7 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_3 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _isToMany = CppGenerator.this._cppExtensions.isToMany(it);
          return Boolean.valueOf((!_isToMany));
        }
      };
      Iterable<? extends LFeature> _filter_3 = IterableExtensions.filter(_allFeatures_7, _function_3);
      for(final LFeature feature_7 : _filter_3) {
        String _typeName_1 = this._cppExtensions.toTypeName(feature_7);
        _builder.append(_typeName_1, "");
        _builder.append(" ");
        String _name_35 = this._cppExtensions.toName(dto);
        _builder.append(_name_35, "");
        _builder.append("::");
        String _name_36 = this._cppExtensions.toName(feature_7);
        _builder.append(_name_36, "");
        _builder.append("() const");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return m");
        String _name_37 = this._cppExtensions.toName(feature_7);
        String _firstUpper_12 = StringExtensions.toFirstUpper(_name_37);
        _builder.append(_firstUpper_12, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_38 = this._cppExtensions.toName(dto);
        _builder.append(_name_38, "");
        _builder.append("::set");
        String _name_39 = this._cppExtensions.toName(feature_7);
        String _firstUpper_13 = StringExtensions.toFirstUpper(_name_39);
        _builder.append(_firstUpper_13, "");
        _builder.append("(");
        String _typeName_2 = this._cppExtensions.toTypeName(feature_7);
        _builder.append(_typeName_2, "");
        _builder.append(" ");
        String _name_40 = this._cppExtensions.toName(feature_7);
        _builder.append(_name_40, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (");
        String _name_41 = this._cppExtensions.toName(feature_7);
        _builder.append(_name_41, "\t");
        _builder.append(" != m");
        String _name_42 = this._cppExtensions.toName(feature_7);
        String _firstUpper_14 = StringExtensions.toFirstUpper(_name_42);
        _builder.append(_firstUpper_14, "\t");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("m");
        String _name_43 = this._cppExtensions.toName(feature_7);
        String _firstUpper_15 = StringExtensions.toFirstUpper(_name_43);
        _builder.append(_firstUpper_15, "\t\t");
        _builder.append(" = ");
        String _name_44 = this._cppExtensions.toName(feature_7);
        _builder.append(_name_44, "\t\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("emit ");
        String _name_45 = this._cppExtensions.toName(feature_7);
        _builder.append(_name_45, "\t\t");
        _builder.append("Changed(");
        String _name_46 = this._cppExtensions.toName(feature_7);
        _builder.append(_name_46, "\t\t");
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
      List<? extends LFeature> _allFeatures_8 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_4 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          return Boolean.valueOf(CppGenerator.this._cppExtensions.isToMany(it));
        }
      };
      Iterable<? extends LFeature> _filter_4 = IterableExtensions.filter(_allFeatures_8, _function_4);
      for(final LFeature feature_8 : _filter_4) {
        _builder.append("QVariantList ");
        String _name_47 = this._cppExtensions.toName(dto);
        _builder.append(_name_47, "");
        _builder.append("::");
        String _name_48 = this._cppExtensions.toName(feature_8);
        _builder.append(_name_48, "");
        _builder.append("() const ");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return m");
        String _name_49 = this._cppExtensions.toName(feature_8);
        String _firstUpper_16 = StringExtensions.toFirstUpper(_name_49);
        _builder.append(_firstUpper_16, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void ");
        String _name_50 = this._cppExtensions.toName(dto);
        _builder.append(_name_50, "");
        _builder.append("::set");
        String _name_51 = this._cppExtensions.toName(feature_8);
        String _firstUpper_17 = StringExtensions.toFirstUpper(_name_51);
        _builder.append(_firstUpper_17, "");
        _builder.append("(QVariantList ");
        String _name_52 = this._cppExtensions.toName(feature_8);
        _builder.append(_name_52, "");
        _builder.append(") ");
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (");
        String _name_53 = this._cppExtensions.toName(feature_8);
        _builder.append(_name_53, "\t");
        _builder.append(" != m");
        String _name_54 = this._cppExtensions.toName(feature_8);
        String _firstUpper_18 = StringExtensions.toFirstUpper(_name_54);
        _builder.append(_firstUpper_18, "\t");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("m");
        String _name_55 = this._cppExtensions.toName(feature_8);
        String _firstUpper_19 = StringExtensions.toFirstUpper(_name_55);
        _builder.append(_firstUpper_19, "\t\t");
        _builder.append(" = ");
        String _name_56 = this._cppExtensions.toName(feature_8);
        _builder.append(_name_56, "\t\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("emit ");
        String _name_57 = this._cppExtensions.toName(feature_8);
        _builder.append(_name_57, "\t\t");
        _builder.append("Changed(");
        String _name_58 = this._cppExtensions.toName(feature_8);
        _builder.append(_name_58, "\t\t");
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
    String _name_59 = this._cppExtensions.toName(dto);
    _builder.append(_name_59, "");
    _builder.append("::~");
    String _name_60 = this._cppExtensions.toName(dto);
    _builder.append(_name_60, "");
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
}
