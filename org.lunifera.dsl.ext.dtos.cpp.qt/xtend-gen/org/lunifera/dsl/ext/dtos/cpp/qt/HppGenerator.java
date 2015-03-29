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
    _builder.append("#include <QDeclarativeListProperty>");
    _builder.newLine();
    {
      List<LDtoAbstractReference> _references = dto.getReferences();
      for(final LDtoAbstractReference reference : _references) {
        {
          boolean _isContained = this._cppExtensions.isContained(reference);
          boolean _not = (!_isContained);
          if (_not) {
            _builder.append("#include \"");
            String _typeName = this._cppExtensions.toTypeName(reference);
            _builder.append(_typeName, "");
            _builder.append(".hpp\"");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("// forward declaration to avoid circular dependencies");
            _builder.newLine();
            _builder.append("class ");
            String _typeName_1 = this._cppExtensions.toTypeName(reference);
            String _firstUpper = StringExtensions.toFirstUpper(_typeName_1);
            _builder.append(_firstUpper, "");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
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
            _builder.append(")");
            _builder.newLineIfNotEmpty();
          } else {
            boolean _isLazy = this._cppExtensions.isLazy(feature);
            if (_isLazy) {
              _builder.append("\t");
              _builder.append("// ");
              String _name_5 = this._cppExtensions.toName(feature);
              _builder.append(_name_5, "\t");
              _builder.append(" lazy pointing to ");
              String _typeOrQObject_1 = this._cppExtensions.toTypeOrQObject(feature);
              _builder.append(_typeOrQObject_1, "\t");
              _builder.append(" (domainKey: ");
              String _referenceDomainKey = this._cppExtensions.referenceDomainKey(feature);
              _builder.append(_referenceDomainKey, "\t");
              _builder.append(")");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("Q_PROPERTY(");
              String _referenceDomainKeyType = this._cppExtensions.referenceDomainKeyType(feature);
              _builder.append(_referenceDomainKeyType, "\t");
              _builder.append(" ");
              String _name_6 = this._cppExtensions.toName(feature);
              _builder.append(_name_6, "\t");
              _builder.append(" READ ");
              String _name_7 = this._cppExtensions.toName(feature);
              _builder.append(_name_7, "\t");
              _builder.append(" WRITE set");
              String _name_8 = this._cppExtensions.toName(feature);
              String _firstUpper_1 = StringExtensions.toFirstUpper(_name_8);
              _builder.append(_firstUpper_1, "\t");
              _builder.append(" NOTIFY ");
              String _name_9 = this._cppExtensions.toName(feature);
              _builder.append(_name_9, "\t");
              _builder.append("Changed FINAL)");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("Q_PROPERTY(");
              String _typeOrQObject_2 = this._cppExtensions.toTypeOrQObject(feature);
              _builder.append(_typeOrQObject_2, "\t");
              _builder.append(" ");
              String _name_10 = this._cppExtensions.toName(feature);
              _builder.append(_name_10, "\t");
              _builder.append("AsDTO READ ");
              String _name_11 = this._cppExtensions.toName(feature);
              _builder.append(_name_11, "\t");
              _builder.append("AsDTO)");
              _builder.newLineIfNotEmpty();
            } else {
              _builder.append("\t");
              _builder.append("Q_PROPERTY(");
              String _typeOrQObject_3 = this._cppExtensions.toTypeOrQObject(feature);
              _builder.append(_typeOrQObject_3, "\t");
              _builder.append(" ");
              String _name_12 = this._cppExtensions.toName(feature);
              _builder.append(_name_12, "\t");
              _builder.append(" READ ");
              String _name_13 = this._cppExtensions.toName(feature);
              _builder.append(_name_13, "\t");
              _builder.append(" WRITE set");
              String _name_14 = this._cppExtensions.toName(feature);
              String _firstUpper_2 = StringExtensions.toFirstUpper(_name_14);
              _builder.append(_firstUpper_2, "\t");
              _builder.append(" NOTIFY ");
              String _name_15 = this._cppExtensions.toName(feature);
              _builder.append(_name_15, "\t");
              _builder.append("Changed FINAL)");
              _builder.newLineIfNotEmpty();
            }
          }
        }
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
        _builder.append("// QDeclarativeListProperty to get easy access from QML");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("Q_PROPERTY(QDeclarativeListProperty<ItemDTO> ");
        String _name_16 = this._cppExtensions.toName(feature_1);
        _builder.append(_name_16, "\t");
        _builder.append("PropertyList READ ");
        String _name_17 = this._cppExtensions.toName(feature_1);
        _builder.append(_name_17, "\t");
        _builder.append("PropertyList CONSTANT)");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("public:");
    _builder.newLine();
    _builder.append("\t");
    String _name_18 = this._cppExtensions.toName(dto);
    _builder.append(_name_18, "\t");
    _builder.append("(QObject *parent = 0);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("void fillFromMap(const QVariantMap& ");
    String _name_19 = this._cppExtensions.toName(dto);
    String _firstLower = StringExtensions.toFirstLower(_name_19);
    _builder.append(_firstLower, "\t");
    _builder.append("Map);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("void prepareNew();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("bool isValid();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Q_INVOKABLE");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("QVariantMap toMap();");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    {
      boolean _existsServerName = this._cppExtensions.existsServerName(dto);
      if (_existsServerName) {
        _builder.append("\t");
        _builder.append("QVariantMap toForeignMap();");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("QVariantMap dataToPersist();");
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
        {
          boolean _isLazy_1 = this._cppExtensions.isLazy(feature_2);
          if (_isLazy_1) {
            _builder.append("\t");
            _builder.append("// ");
            String _name_20 = this._cppExtensions.toName(feature_2);
            _builder.append(_name_20, "\t");
            _builder.append(" lazy pointing to ");
            String _typeOrQObject_4 = this._cppExtensions.toTypeOrQObject(feature_2);
            _builder.append(_typeOrQObject_4, "\t");
            _builder.append(" (domainKey: ");
            String _referenceDomainKey_1 = this._cppExtensions.referenceDomainKey(feature_2);
            _builder.append(_referenceDomainKey_1, "\t");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            String _referenceDomainKeyType_1 = this._cppExtensions.referenceDomainKeyType(feature_2);
            _builder.append(_referenceDomainKeyType_1, "\t");
            _builder.append(" ");
            String _name_21 = this._cppExtensions.toName(feature_2);
            _builder.append(_name_21, "\t");
            _builder.append("() const;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("void set");
            String _name_22 = this._cppExtensions.toName(feature_2);
            String _firstUpper_3 = StringExtensions.toFirstUpper(_name_22);
            _builder.append(_firstUpper_3, "\t");
            _builder.append("(");
            String _referenceDomainKeyType_2 = this._cppExtensions.referenceDomainKeyType(feature_2);
            _builder.append(_referenceDomainKeyType_2, "\t");
            _builder.append(" ");
            String _name_23 = this._cppExtensions.toName(feature_2);
            _builder.append(_name_23, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            String _typeOrQObject_5 = this._cppExtensions.toTypeOrQObject(feature_2);
            _builder.append(_typeOrQObject_5, "\t");
            _builder.append(" ");
            String _name_24 = this._cppExtensions.toName(feature_2);
            _builder.append(_name_24, "\t");
            _builder.append("AsDTO() const;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("void remove");
            String _name_25 = this._cppExtensions.toName(feature_2);
            String _firstUpper_4 = StringExtensions.toFirstUpper(_name_25);
            _builder.append(_firstUpper_4, "\t");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("bool has");
            String _name_26 = this._cppExtensions.toName(feature_2);
            String _firstUpper_5 = StringExtensions.toFirstUpper(_name_26);
            _builder.append(_firstUpper_5, "\t");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("bool has");
            String _name_27 = this._cppExtensions.toName(feature_2);
            String _firstUpper_6 = StringExtensions.toFirstUpper(_name_27);
            _builder.append(_firstUpper_6, "\t");
            _builder.append("AsDTO();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.newLine();
          } else {
            _builder.append("\t");
            String _typeOrQObject_6 = this._cppExtensions.toTypeOrQObject(feature_2);
            _builder.append(_typeOrQObject_6, "\t");
            _builder.append(" ");
            String _name_28 = this._cppExtensions.toName(feature_2);
            _builder.append(_name_28, "\t");
            _builder.append("() const;");
            _builder.newLineIfNotEmpty();
            {
              boolean _and_1 = false;
              boolean _isTypeOfDTO_1 = this._cppExtensions.isTypeOfDTO(feature_2);
              if (!_isTypeOfDTO_1) {
                _and_1 = false;
              } else {
                boolean _isContained_2 = this._cppExtensions.isContained(feature_2);
                _and_1 = _isContained_2;
              }
              if (_and_1) {
                _builder.append("\t");
                _builder.append("// no SETTER ");
                String _name_29 = this._cppExtensions.toName(feature_2);
                _builder.append(_name_29, "\t");
                _builder.append("() is only convenience method to get the parent");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("void set");
                String _name_30 = this._cppExtensions.toName(feature_2);
                String _firstUpper_7 = StringExtensions.toFirstUpper(_name_30);
                _builder.append(_firstUpper_7, "\t");
                _builder.append("(");
                String _typeOrQObject_7 = this._cppExtensions.toTypeOrQObject(feature_2);
                _builder.append(_typeOrQObject_7, "\t");
                _builder.append(" ");
                String _name_31 = this._cppExtensions.toName(feature_2);
                _builder.append(_name_31, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                {
                  boolean _isTypeOfDTO_2 = this._cppExtensions.isTypeOfDTO(feature_2);
                  if (_isTypeOfDTO_2) {
                    _builder.append("\t");
                    _builder.append("Q_INVOKABLE");
                    _builder.newLine();
                    _builder.append("\t");
                    _builder.append("void delete");
                    String _name_32 = this._cppExtensions.toName(feature_2);
                    String _firstUpper_8 = StringExtensions.toFirstUpper(_name_32);
                    _builder.append(_firstUpper_8, "\t");
                    _builder.append("();");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.newLine();
                    _builder.append("\t");
                    _builder.append("Q_INVOKABLE");
                    _builder.newLine();
                    _builder.append("\t");
                    _builder.append("bool has");
                    String _name_33 = this._cppExtensions.toName(feature_2);
                    String _firstUpper_9 = StringExtensions.toFirstUpper(_name_33);
                    _builder.append(_firstUpper_9, "\t");
                    _builder.append("();");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.newLine();
                  }
                }
              }
            }
          }
        }
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
        _builder.append("Q_INVOKABLE");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("QVariantList ");
        String _name_34 = this._cppExtensions.toName(feature_3);
        _builder.append(_name_34, "\t");
        _builder.append("AsQVariantList();");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("Q_INVOKABLE");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("void addTo");
        String _name_35 = this._cppExtensions.toName(feature_3);
        String _firstUpper_10 = StringExtensions.toFirstUpper(_name_35);
        _builder.append(_firstUpper_10, "\t");
        _builder.append("(");
        String _typeName_2 = this._cppExtensions.toTypeName(feature_3);
        _builder.append(_typeName_2, "\t");
        _builder.append("* ");
        String _typeName_3 = this._cppExtensions.toTypeName(feature_3);
        String _firstLower_1 = StringExtensions.toFirstLower(_typeName_3);
        _builder.append(_firstLower_1, "\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("Q_INVOKABLE");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("void removeFrom");
        String _name_36 = this._cppExtensions.toName(feature_3);
        String _firstUpper_11 = StringExtensions.toFirstUpper(_name_36);
        _builder.append(_firstUpper_11, "\t");
        _builder.append("(");
        String _typeName_4 = this._cppExtensions.toTypeName(feature_3);
        _builder.append(_typeName_4, "\t");
        _builder.append("* ");
        String _typeName_5 = this._cppExtensions.toTypeName(feature_3);
        String _firstLower_2 = StringExtensions.toFirstLower(_typeName_5);
        _builder.append(_firstLower_2, "\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("Q_INVOKABLE");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("void addTo");
        String _name_37 = this._cppExtensions.toName(feature_3);
        String _firstUpper_12 = StringExtensions.toFirstUpper(_name_37);
        _builder.append(_firstUpper_12, "\t");
        _builder.append("FromMap(const QVariantMap& ");
        String _typeName_6 = this._cppExtensions.toTypeName(feature_3);
        String _firstLower_3 = StringExtensions.toFirstLower(_typeName_6);
        _builder.append(_firstLower_3, "\t");
        _builder.append("Map);");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("Q_INVOKABLE");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("void removeFrom");
        String _name_38 = this._cppExtensions.toName(feature_3);
        String _firstUpper_13 = StringExtensions.toFirstUpper(_name_38);
        _builder.append(_firstUpper_13, "\t");
        _builder.append("ByKey(const QString& uuid);");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("Q_INVOKABLE");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("int ");
        String _name_39 = this._cppExtensions.toName(feature_3);
        String _firstLower_4 = StringExtensions.toFirstLower(_name_39);
        _builder.append(_firstLower_4, "\t");
        _builder.append("Count();");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append(" ");
        _builder.append("// access from C++ to positions");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("QList<");
        String _typeName_7 = this._cppExtensions.toTypeName(feature_3);
        _builder.append(_typeName_7, "\t");
        _builder.append("*> ");
        String _name_40 = this._cppExtensions.toName(feature_3);
        _builder.append(_name_40, "\t");
        _builder.append("();");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("void set");
        String _name_41 = this._cppExtensions.toName(feature_3);
        String _firstUpper_14 = StringExtensions.toFirstUpper(_name_41);
        _builder.append(_firstUpper_14, "\t");
        _builder.append("(QList<");
        String _typeName_8 = this._cppExtensions.toTypeName(feature_3);
        _builder.append(_typeName_8, "\t");
        _builder.append("*> ");
        String _name_42 = this._cppExtensions.toName(feature_3);
        _builder.append(_name_42, "\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("// access from QML to positions");
        _builder.newLine();
        _builder.append("\t");
        _builder.append(" ");
        _builder.append("QDeclarativeListProperty<");
        String _typeName_9 = this._cppExtensions.toTypeName(feature_3);
        _builder.append(_typeName_9, "\t ");
        _builder.append("> ");
        String _name_43 = this._cppExtensions.toName(feature_3);
        _builder.append(_name_43, "\t ");
        _builder.append("PropertyList();");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("\t");
    _builder.append("virtual ~");
    String _name_44 = this._cppExtensions.toName(dto);
    String _firstUpper_15 = StringExtensions.toFirstUpper(_name_44);
    _builder.append(_firstUpper_15, "\t");
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
        {
          boolean _isLazy_2 = this._cppExtensions.isLazy(feature_4);
          if (_isLazy_2) {
            _builder.append("\t");
            _builder.append("// ");
            String _name_45 = this._cppExtensions.toName(feature_4);
            _builder.append(_name_45, "\t");
            _builder.append(" lazy pointing to ");
            String _typeOrQObject_8 = this._cppExtensions.toTypeOrQObject(feature_4);
            _builder.append(_typeOrQObject_8, "\t");
            _builder.append(" (domainKey: ");
            String _referenceDomainKey_2 = this._cppExtensions.referenceDomainKey(feature_4);
            _builder.append(_referenceDomainKey_2, "\t");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("void ");
            String _name_46 = this._cppExtensions.toName(feature_4);
            _builder.append(_name_46, "\t");
            _builder.append("Changed(");
            String _referenceDomainKeyType_3 = this._cppExtensions.referenceDomainKeyType(feature_4);
            _builder.append(_referenceDomainKeyType_3, "\t");
            _builder.append(" ");
            String _name_47 = this._cppExtensions.toName(feature_4);
            _builder.append(_name_47, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("void ");
            String _name_48 = this._cppExtensions.toName(feature_4);
            _builder.append(_name_48, "\t");
            _builder.append("Removed(");
            String _referenceDomainKeyType_4 = this._cppExtensions.referenceDomainKeyType(feature_4);
            _builder.append(_referenceDomainKeyType_4, "\t");
            _builder.append(" ");
            String _name_49 = this._cppExtensions.toName(feature_4);
            _builder.append(_name_49, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("void request");
            String _name_50 = this._cppExtensions.toName(feature_4);
            String _firstUpper_16 = StringExtensions.toFirstUpper(_name_50);
            _builder.append(_firstUpper_16, "\t");
            _builder.append("AsDTO(");
            String _referenceDomainKeyType_5 = this._cppExtensions.referenceDomainKeyType(feature_4);
            _builder.append(_referenceDomainKeyType_5, "\t");
            _builder.append(" ");
            String _name_51 = this._cppExtensions.toName(feature_4);
            _builder.append(_name_51, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
          } else {
            {
              boolean _and_2 = false;
              boolean _isTypeOfDTO_3 = this._cppExtensions.isTypeOfDTO(feature_4);
              if (!_isTypeOfDTO_3) {
                _and_2 = false;
              } else {
                boolean _isContained_3 = this._cppExtensions.isContained(feature_4);
                _and_2 = _isContained_3;
              }
              if (_and_2) {
                _builder.append("\t");
                _builder.append("// no SIGNAL ");
                String _name_52 = this._cppExtensions.toName(feature_4);
                _builder.append(_name_52, "\t");
                _builder.append(" is only convenience way to get the parent");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("void ");
                String _name_53 = this._cppExtensions.toName(feature_4);
                _builder.append(_name_53, "\t");
                _builder.append("Changed(");
                String _typeOrQObject_9 = this._cppExtensions.toTypeOrQObject(feature_4);
                _builder.append(_typeOrQObject_9, "\t");
                _builder.append(" ");
                String _name_54 = this._cppExtensions.toName(feature_4);
                _builder.append(_name_54, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                {
                  boolean _isTypeOfDTO_4 = this._cppExtensions.isTypeOfDTO(feature_4);
                  if (_isTypeOfDTO_4) {
                    _builder.append("\t");
                    _builder.append("void ");
                    String _name_55 = this._cppExtensions.toName(feature_4);
                    String _firstLower_5 = StringExtensions.toFirstLower(_name_55);
                    _builder.append(_firstLower_5, "\t");
                    _builder.append("Deleted(QString uuid);");
                    _builder.newLineIfNotEmpty();
                  }
                }
              }
            }
          }
        }
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
        String _name_56 = this._cppExtensions.toName(feature_5);
        _builder.append(_name_56, "\t");
        _builder.append("Changed(QList<");
        String _typeName_10 = this._cppExtensions.toTypeName(feature_5);
        _builder.append(_typeName_10, "\t");
        _builder.append("*> ");
        String _name_57 = this._cppExtensions.toName(feature_5);
        _builder.append(_name_57, "\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("void addedTo");
        String _name_58 = this._cppExtensions.toName(feature_5);
        String _firstUpper_17 = StringExtensions.toFirstUpper(_name_58);
        _builder.append(_firstUpper_17, "\t");
        _builder.append("(");
        String _typeName_11 = this._cppExtensions.toTypeName(feature_5);
        _builder.append(_typeName_11, "\t");
        _builder.append("* ");
        String _typeName_12 = this._cppExtensions.toTypeName(feature_5);
        String _firstLower_6 = StringExtensions.toFirstLower(_typeName_12);
        _builder.append(_firstLower_6, "\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("void removedFrom");
        String _name_59 = this._cppExtensions.toName(feature_5);
        String _firstUpper_18 = StringExtensions.toFirstUpper(_name_59);
        _builder.append(_firstUpper_18, "\t");
        _builder.append("(QString uuid);");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    {
      boolean _existsLazy = this._cppExtensions.existsLazy(dto);
      if (_existsLazy) {
        _builder.append("public slots:");
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
            {
              boolean _isLazy_3 = this._cppExtensions.isLazy(feature_6);
              if (_isLazy_3) {
                _builder.append("\t");
                _builder.append("void onRequested");
                String _name_60 = this._cppExtensions.toName(feature_6);
                String _firstUpper_19 = StringExtensions.toFirstUpper(_name_60);
                _builder.append(_firstUpper_19, "\t");
                _builder.append("AsDTO(");
                String _typeOrQObject_10 = this._cppExtensions.toTypeOrQObject(feature_6);
                _builder.append(_typeOrQObject_10, "\t");
                _builder.append(" ");
                String _typeName_13 = this._cppExtensions.toTypeName(feature_6);
                String _firstLower_7 = StringExtensions.toFirstLower(_typeName_13);
                _builder.append(_firstLower_7, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    _builder.newLine();
    _builder.append("private:");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("QVariantMap m");
    String _name_61 = this._cppExtensions.toName(dto);
    String _firstUpper_20 = StringExtensions.toFirstUpper(_name_61);
    _builder.append(_firstUpper_20, "\t");
    _builder.append("Map;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_7 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_7 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _isToMany = HppGenerator.this._cppExtensions.isToMany(it);
          return Boolean.valueOf((!_isToMany));
        }
      };
      Iterable<? extends LFeature> _filter_7 = IterableExtensions.filter(_allFeatures_7, _function_7);
      for(final LFeature feature_7 : _filter_7) {
        {
          boolean _and_3 = false;
          boolean _isTypeOfDTO_5 = this._cppExtensions.isTypeOfDTO(feature_7);
          if (!_isTypeOfDTO_5) {
            _and_3 = false;
          } else {
            boolean _isContained_4 = this._cppExtensions.isContained(feature_7);
            _and_3 = _isContained_4;
          }
          if (_and_3) {
            _builder.append("\t");
            _builder.append("// no MEMBER m");
            String _name_62 = this._cppExtensions.toName(feature_7);
            String _firstUpper_21 = StringExtensions.toFirstUpper(_name_62);
            _builder.append(_firstUpper_21, "\t");
            _builder.append(" it\'s the parent");
            _builder.newLineIfNotEmpty();
          } else {
            boolean _isLazy_4 = this._cppExtensions.isLazy(feature_7);
            if (_isLazy_4) {
              _builder.append("\t");
              String _referenceDomainKeyType_6 = this._cppExtensions.referenceDomainKeyType(feature_7);
              _builder.append(_referenceDomainKeyType_6, "\t");
              _builder.append(" m");
              String _name_63 = this._cppExtensions.toName(feature_7);
              String _firstUpper_22 = StringExtensions.toFirstUpper(_name_63);
              _builder.append(_firstUpper_22, "\t");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              String _typeOrQObject_11 = this._cppExtensions.toTypeOrQObject(feature_7);
              _builder.append(_typeOrQObject_11, "\t");
              _builder.append(" m");
              String _name_64 = this._cppExtensions.toName(feature_7);
              String _firstUpper_23 = StringExtensions.toFirstUpper(_name_64);
              _builder.append(_firstUpper_23, "\t");
              _builder.append("AsDTO;");
              _builder.newLineIfNotEmpty();
            } else {
              _builder.append("\t");
              String _typeOrQObject_12 = this._cppExtensions.toTypeOrQObject(feature_7);
              _builder.append(_typeOrQObject_12, "\t");
              _builder.append(" m");
              String _name_65 = this._cppExtensions.toName(feature_7);
              String _firstUpper_24 = StringExtensions.toFirstUpper(_name_65);
              _builder.append(_firstUpper_24, "\t");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
    }
    {
      List<? extends LFeature> _allFeatures_8 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_8 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          return Boolean.valueOf(HppGenerator.this._cppExtensions.isToMany(it));
        }
      };
      Iterable<? extends LFeature> _filter_8 = IterableExtensions.filter(_allFeatures_8, _function_8);
      for(final LFeature feature_8 : _filter_8) {
        _builder.append("\t");
        _builder.append("QList<");
        String _typeName_14 = this._cppExtensions.toTypeName(feature_8);
        _builder.append(_typeName_14, "\t");
        _builder.append("*> m");
        String _name_66 = this._cppExtensions.toName(feature_8);
        String _firstUpper_25 = StringExtensions.toFirstUpper(_name_66);
        _builder.append(_firstUpper_25, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("// implementation for QDeclarativeListProperty to use");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("// QML functions for List of ");
        String _typeName_15 = this._cppExtensions.toTypeName(feature_8);
        _builder.append(_typeName_15, "\t");
        _builder.append("*");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("static void appendTo");
        String _name_67 = this._cppExtensions.toName(feature_8);
        String _firstUpper_26 = StringExtensions.toFirstUpper(_name_67);
        _builder.append(_firstUpper_26, "\t");
        _builder.append("Property(QDeclarativeListProperty<");
        String _typeName_16 = this._cppExtensions.toTypeName(feature_8);
        _builder.append(_typeName_16, "\t");
        _builder.append("> *");
        String _name_68 = this._cppExtensions.toName(feature_8);
        _builder.append(_name_68, "\t");
        _builder.append("List,");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        String _typeName_17 = this._cppExtensions.toTypeName(feature_8);
        _builder.append(_typeName_17, "\t\t");
        _builder.append("* ");
        String _typeName_18 = this._cppExtensions.toTypeName(feature_8);
        String _firstLower_8 = StringExtensions.toFirstLower(_typeName_18);
        _builder.append(_firstLower_8, "\t\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("static int ");
        String _name_69 = this._cppExtensions.toName(feature_8);
        _builder.append(_name_69, "\t");
        _builder.append("PropertyCount(QDeclarativeListProperty<");
        String _typeName_19 = this._cppExtensions.toTypeName(feature_8);
        _builder.append(_typeName_19, "\t");
        _builder.append("> *");
        String _name_70 = this._cppExtensions.toName(feature_8);
        _builder.append(_name_70, "\t");
        _builder.append("List);");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("static ");
        String _typeName_20 = this._cppExtensions.toTypeName(feature_8);
        _builder.append(_typeName_20, "\t");
        _builder.append("* at");
        String _name_71 = this._cppExtensions.toName(feature_8);
        String _firstUpper_27 = StringExtensions.toFirstUpper(_name_71);
        _builder.append(_firstUpper_27, "\t");
        _builder.append("Property(QDeclarativeListProperty<");
        String _typeName_21 = this._cppExtensions.toTypeName(feature_8);
        _builder.append(_typeName_21, "\t");
        _builder.append("> *");
        String _name_72 = this._cppExtensions.toName(feature_8);
        _builder.append(_name_72, "\t");
        _builder.append("List, int pos);");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("static void clear");
        String _name_73 = this._cppExtensions.toName(feature_8);
        String _firstUpper_28 = StringExtensions.toFirstUpper(_name_73);
        _builder.append(_firstUpper_28, "\t");
        _builder.append("Property(QDeclarativeListProperty<");
        String _typeName_22 = this._cppExtensions.toTypeName(feature_8);
        _builder.append(_typeName_22, "\t");
        _builder.append("> *");
        String _name_74 = this._cppExtensions.toName(feature_8);
        _builder.append(_name_74, "\t");
        _builder.append("List);");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Q_DISABLE_COPY (");
    String _name_75 = this._cppExtensions.toName(dto);
    String _firstUpper_29 = StringExtensions.toFirstUpper(_name_75);
    _builder.append(_firstUpper_29, "\t");
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    _builder.append("};");
    _builder.newLine();
    _builder.append("Q_DECLARE_METATYPE(");
    String _name_76 = this._cppExtensions.toName(dto);
    String _firstUpper_30 = StringExtensions.toFirstUpper(_name_76);
    _builder.append(_firstUpper_30, "");
    _builder.append("*)");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("#endif /* ");
    String _name_77 = this._cppExtensions.toName(dto);
    String _upperCase_2 = _name_77.toUpperCase();
    _builder.append(_upperCase_2, "");
    _builder.append("_HPP_ */");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    return _builder;
  }
}
