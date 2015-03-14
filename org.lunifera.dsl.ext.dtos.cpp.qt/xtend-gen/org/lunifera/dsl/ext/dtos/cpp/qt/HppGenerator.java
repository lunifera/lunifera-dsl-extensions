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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.lunifera.dsl.ext.dtos.cpp.qt.CppExtensions;
import org.lunifera.dsl.semantic.common.types.LFeature;
import org.lunifera.dsl.semantic.common.types.LPackage;
import org.lunifera.dsl.semantic.dto.LDto;
import org.lunifera.dsl.semantic.dto.LDtoAbstractReference;

@SuppressWarnings("all")
public class HppGenerator {
  @Inject
  @Extension
  private CppExtensions _cppExtensions;
  
  public String toFileName(final LDto dto) {
    String _name = this._cppExtensions.toName(dto);
    return (_name + ".hpp");
  }
  
  public CharSequence toContent(final LDto dto) {
    StringConcatenation _builder = new StringConcatenation();
    EObject _eContainer = dto.eContainer();
    String _copyRight = this._cppExtensions.toCopyRight(((LPackage) _eContainer));
    _builder.append(_copyRight, "");
    _builder.newLineIfNotEmpty();
    _builder.append("#ifndef ");
    String _name = this._cppExtensions.toName(dto);
    String _upperCase = _name.toUpperCase();
    _builder.append(_upperCase, "");
    _builder.append("_HPP_");
    _builder.newLineIfNotEmpty();
    _builder.append("#define ");
    String _name_1 = this._cppExtensions.toName(dto);
    String _upperCase_1 = _name_1.toUpperCase();
    _builder.append(_upperCase_1, "");
    _builder.append("_HPP_");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("#include <QObject>");
    _builder.newLine();
    _builder.append("#include <qvariant.h>");
    _builder.newLine();
    {
      List<LDtoAbstractReference> _references = dto.getReferences();
      for(final LDtoAbstractReference reference : _references) {
        _builder.append("#include \"");
        String _typeName = this._cppExtensions.toTypeName(reference);
        _builder.append(_typeName, "");
        _builder.append(".hpp\"");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("class ");
    String _name_2 = this._cppExtensions.toName(dto);
    _builder.append(_name_2, "");
    _builder.append(": public QObject");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Q_OBJECT");
    _builder.newLine();
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _isToMany = HppGenerator.this._cppExtensions.isToMany(it);
          return Boolean.valueOf((!_isToMany));
        }
      };
      Iterable<? extends LFeature> _filter = IterableExtensions.filter(_allFeatures, _function);
      for(final LFeature feature : _filter) {
        _builder.append("\t");
        _builder.append("Q_PROPERTY(");
        String _typeOrQObject = this._cppExtensions.toTypeOrQObject(feature);
        _builder.append(_typeOrQObject, "\t");
        _builder.append(" ");
        String _name_3 = this._cppExtensions.toName(feature);
        _builder.append(_name_3, "\t");
        _builder.append(" READ ");
        String _name_4 = this._cppExtensions.toName(feature);
        _builder.append(_name_4, "\t");
        _builder.append(" WRITE set");
        String _name_5 = this._cppExtensions.toName(feature);
        String _firstUpper = StringExtensions.toFirstUpper(_name_5);
        _builder.append(_firstUpper, "\t");
        _builder.append(" NOTIFY ");
        String _name_6 = this._cppExtensions.toName(feature);
        _builder.append(_name_6, "\t");
        _builder.append("Changed FINAL)");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_1 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_1 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          return Boolean.valueOf(HppGenerator.this._cppExtensions.isToMany(it));
        }
      };
      Iterable<? extends LFeature> _filter_1 = IterableExtensions.filter(_allFeatures_1, _function_1);
      for(final LFeature feature_1 : _filter_1) {
        _builder.append("\t");
        _builder.append("Q_PROPERTY(QVariantList ");
        String _name_7 = this._cppExtensions.toName(feature_1);
        _builder.append(_name_7, "\t");
        _builder.append(" READ ");
        String _name_8 = this._cppExtensions.toName(feature_1);
        _builder.append(_name_8, "\t");
        _builder.append(" WRITE set");
        String _name_9 = this._cppExtensions.toName(feature_1);
        String _firstUpper_1 = StringExtensions.toFirstUpper(_name_9);
        _builder.append(_firstUpper_1, "\t");
        _builder.append(" NOTIFY ");
        String _name_10 = this._cppExtensions.toName(feature_1);
        _builder.append(_name_10, "\t");
        _builder.append("Changed FINAL)");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("public:");
    _builder.newLine();
    _builder.append("\t");
    String _name_11 = this._cppExtensions.toName(dto);
    _builder.append(_name_11, "\t");
    _builder.append("(QObject *parent = 0);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("void initFromMap(QVariantMap ");
    String _name_12 = this._cppExtensions.toName(dto);
    String _firstLower = StringExtensions.toFirstLower(_name_12);
    _builder.append(_firstLower, "\t");
    _builder.append("Map);");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("QVariantMap toMap();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("QVariantMap toForeignMap();");
    _builder.newLine();
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_2 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_2 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _isToMany = HppGenerator.this._cppExtensions.isToMany(it);
          return Boolean.valueOf((!_isToMany));
        }
      };
      Iterable<? extends LFeature> _filter_2 = IterableExtensions.filter(_allFeatures_2, _function_2);
      for(final LFeature feature_2 : _filter_2) {
        _builder.append("\t");
        String _typeOrQObject_1 = this._cppExtensions.toTypeOrQObject(feature_2);
        _builder.append(_typeOrQObject_1, "\t");
        _builder.append(" ");
        String _name_13 = this._cppExtensions.toName(feature_2);
        _builder.append(_name_13, "\t");
        _builder.append("() const;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("void set");
        String _name_14 = this._cppExtensions.toName(feature_2);
        String _firstUpper_2 = StringExtensions.toFirstUpper(_name_14);
        _builder.append(_firstUpper_2, "\t");
        _builder.append("(");
        String _typeOrQObject_2 = this._cppExtensions.toTypeOrQObject(feature_2);
        _builder.append(_typeOrQObject_2, "\t");
        _builder.append(" ");
        String _name_15 = this._cppExtensions.toName(feature_2);
        _builder.append(_name_15, "\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_3 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_3 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          return Boolean.valueOf(HppGenerator.this._cppExtensions.isToMany(it));
        }
      };
      Iterable<? extends LFeature> _filter_3 = IterableExtensions.filter(_allFeatures_3, _function_3);
      for(final LFeature feature_3 : _filter_3) {
        _builder.append("\t");
        _builder.append("QVariantList ");
        String _name_16 = this._cppExtensions.toName(feature_3);
        _builder.append(_name_16, "\t");
        _builder.append("() const;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("void set");
        String _name_17 = this._cppExtensions.toName(feature_3);
        String _firstUpper_3 = StringExtensions.toFirstUpper(_name_17);
        _builder.append(_firstUpper_3, "\t");
        _builder.append("(QVariantList ");
        String _name_18 = this._cppExtensions.toName(feature_3);
        _builder.append(_name_18, "\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("\t");
    _builder.append("virtual ~");
    String _name_19 = this._cppExtensions.toName(dto);
    String _firstUpper_4 = StringExtensions.toFirstUpper(_name_19);
    _builder.append(_firstUpper_4, "\t");
    _builder.append("();");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Q_SIGNALS:");
    _builder.newLine();
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_4 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_4 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _isToMany = HppGenerator.this._cppExtensions.isToMany(it);
          return Boolean.valueOf((!_isToMany));
        }
      };
      Iterable<? extends LFeature> _filter_4 = IterableExtensions.filter(_allFeatures_4, _function_4);
      for(final LFeature feature_4 : _filter_4) {
        _builder.append("\t");
        _builder.append("void ");
        String _name_20 = this._cppExtensions.toName(feature_4);
        _builder.append(_name_20, "\t");
        _builder.append("Changed(");
        String _typeOrQObject_3 = this._cppExtensions.toTypeOrQObject(feature_4);
        _builder.append(_typeOrQObject_3, "\t");
        _builder.append(" ");
        String _name_21 = this._cppExtensions.toName(feature_4);
        _builder.append(_name_21, "\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      List<? extends LFeature> _allFeatures_5 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_5 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          return Boolean.valueOf(HppGenerator.this._cppExtensions.isToMany(it));
        }
      };
      Iterable<? extends LFeature> _filter_5 = IterableExtensions.filter(_allFeatures_5, _function_5);
      for(final LFeature feature_5 : _filter_5) {
        _builder.append("\t");
        _builder.append("void ");
        String _name_22 = this._cppExtensions.toName(feature_5);
        _builder.append(_name_22, "\t");
        _builder.append("Changed(QVariantList ");
        String _name_23 = this._cppExtensions.toName(feature_5);
        _builder.append(_name_23, "\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("private:");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("QVariantMap m");
    String _name_24 = this._cppExtensions.toName(dto);
    String _firstUpper_5 = StringExtensions.toFirstUpper(_name_24);
    _builder.append(_firstUpper_5, "\t");
    _builder.append("Map;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_6 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_6 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _isToMany = HppGenerator.this._cppExtensions.isToMany(it);
          return Boolean.valueOf((!_isToMany));
        }
      };
      Iterable<? extends LFeature> _filter_6 = IterableExtensions.filter(_allFeatures_6, _function_6);
      for(final LFeature feature_6 : _filter_6) {
        _builder.append("\t");
        String _typeOrQObject_4 = this._cppExtensions.toTypeOrQObject(feature_6);
        _builder.append(_typeOrQObject_4, "\t");
        _builder.append(" m");
        String _name_25 = this._cppExtensions.toName(feature_6);
        String _firstUpper_6 = StringExtensions.toFirstUpper(_name_25);
        _builder.append(_firstUpper_6, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      List<? extends LFeature> _allFeatures_7 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_7 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          return Boolean.valueOf(HppGenerator.this._cppExtensions.isToMany(it));
        }
      };
      Iterable<? extends LFeature> _filter_7 = IterableExtensions.filter(_allFeatures_7, _function_7);
      for(final LFeature feature_7 : _filter_7) {
        _builder.append("\t");
        _builder.append("QVariantList m");
        String _name_26 = this._cppExtensions.toName(feature_7);
        String _firstUpper_7 = StringExtensions.toFirstUpper(_name_26);
        _builder.append(_firstUpper_7, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Q_DISABLE_COPY (");
    String _name_27 = this._cppExtensions.toName(dto);
    String _firstUpper_8 = StringExtensions.toFirstUpper(_name_27);
    _builder.append(_firstUpper_8, "\t");
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    _builder.append("};");
    _builder.newLine();
    _builder.append("Q_DECLARE_METATYPE(");
    String _name_28 = this._cppExtensions.toName(dto);
    String _firstUpper_9 = StringExtensions.toFirstUpper(_name_28);
    _builder.append(_firstUpper_9, "");
    _builder.append("*)");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("#endif /* ");
    String _name_29 = this._cppExtensions.toName(dto);
    String _upperCase_2 = _name_29.toUpperCase();
    _builder.append(_upperCase_2, "");
    _builder.append("_HPP_ */");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    return _builder;
  }
}
