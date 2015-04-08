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
      List<? extends LFeature> _allFeatures = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _and = false;
          boolean _isToMany = HppGenerator.this._cppExtensions.isToMany(it);
          if (!_isToMany) {
            _and = false;
          } else {
            String _typeName = HppGenerator.this._cppExtensions.toTypeName(it);
            boolean _notEquals = (!Objects.equal(_typeName, "QString"));
            _and = _notEquals;
          }
          return Boolean.valueOf(_and);
        }
      };
      Iterable<? extends LFeature> _filter = IterableExtensions.filter(_allFeatures, _function);
      int _size = IterableExtensions.size(_filter);
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        _builder.append("#include <QDeclarativeListProperty>");
        _builder.newLine();
      }
    }
    {
      List<? extends LFeature> _allFeatures_1 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_1 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _and = false;
          boolean _isToMany = HppGenerator.this._cppExtensions.isToMany(it);
          if (!_isToMany) {
            _and = false;
          } else {
            String _typeName = HppGenerator.this._cppExtensions.toTypeName(it);
            boolean _equals = Objects.equal(_typeName, "QString");
            _and = _equals;
          }
          return Boolean.valueOf(_and);
        }
      };
      Iterable<? extends LFeature> _filter_1 = IterableExtensions.filter(_allFeatures_1, _function_1);
      int _size_1 = IterableExtensions.size(_filter_1);
      boolean _greaterThan_1 = (_size_1 > 0);
      if (_greaterThan_1) {
        _builder.append("#include <QStringList>");
        _builder.newLine();
      }
    }
    {
      List<? extends LFeature> _allFeatures_2 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_2 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          String _typeName = HppGenerator.this._cppExtensions.toTypeName(it);
          return Boolean.valueOf(Objects.equal(_typeName, "QDate"));
        }
      };
      Iterable<? extends LFeature> _filter_2 = IterableExtensions.filter(_allFeatures_2, _function_2);
      int _size_2 = IterableExtensions.size(_filter_2);
      boolean _greaterThan_2 = (_size_2 > 0);
      if (_greaterThan_2) {
        _builder.append("#include <QDate>");
        _builder.newLine();
      }
    }
    {
      List<? extends LFeature> _allFeatures_3 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_3 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          String _typeName = HppGenerator.this._cppExtensions.toTypeName(it);
          return Boolean.valueOf(Objects.equal(_typeName, "QTime"));
        }
      };
      Iterable<? extends LFeature> _filter_3 = IterableExtensions.filter(_allFeatures_3, _function_3);
      int _size_3 = IterableExtensions.size(_filter_3);
      boolean _greaterThan_3 = (_size_3 > 0);
      if (_greaterThan_3) {
        _builder.append("#include <QTime>");
        _builder.newLine();
      }
    }
    {
      List<? extends LFeature> _allFeatures_4 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_4 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          String _typeName = HppGenerator.this._cppExtensions.toTypeName(it);
          return Boolean.valueOf(Objects.equal(_typeName, "QDateTime"));
        }
      };
      Iterable<? extends LFeature> _filter_4 = IterableExtensions.filter(_allFeatures_4, _function_4);
      int _size_4 = IterableExtensions.size(_filter_4);
      boolean _greaterThan_4 = (_size_4 > 0);
      if (_greaterThan_4) {
        _builder.append("#include <QDateTime>");
        _builder.newLine();
      }
    }
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_5 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_5 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          return Boolean.valueOf(HppGenerator.this._cppExtensions.isEnum(it));
        }
      };
      Iterable<? extends LFeature> _filter_5 = IterableExtensions.filter(_allFeatures_5, _function_5);
      for(final LFeature en : _filter_5) {
        _builder.append("#include \"");
        String _typeName = this._cppExtensions.toTypeName(en);
        _builder.append(_typeName, "");
        _builder.append(".hpp\"");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_6 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_6 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          return Boolean.valueOf(HppGenerator.this._cppExtensions.isTypeOfDataObject(it));
        }
      };
      Iterable<? extends LFeature> _filter_6 = IterableExtensions.filter(_allFeatures_6, _function_6);
      for(final LFeature reference : _filter_6) {
        {
          boolean _isContained = this._cppExtensions.isContained(reference);
          boolean _not = (!_isContained);
          if (_not) {
            {
              String _typeName_1 = this._cppExtensions.toTypeName(reference);
              String _name_2 = this._cppExtensions.toName(dto);
              boolean _notEquals = (!Objects.equal(_typeName_1, _name_2));
              if (_notEquals) {
                _builder.append("#include \"");
                String _typeName_2 = this._cppExtensions.toTypeName(reference);
                _builder.append(_typeName_2, "");
                _builder.append(".hpp\"");
                _builder.newLineIfNotEmpty();
              }
            }
          } else {
            _builder.append("// forward declaration to avoid circular dependencies");
            _builder.newLine();
            _builder.append("class ");
            String _typeName_3 = this._cppExtensions.toTypeName(reference);
            String _firstUpper = StringExtensions.toFirstUpper(_typeName_3);
            _builder.append(_firstUpper, "");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.newLine();
    _builder.append("class ");
    String _name_3 = this._cppExtensions.toName(dto);
    _builder.append(_name_3, "");
    _builder.append(": public QObject");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Q_OBJECT");
    _builder.newLine();
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
      for(final LFeature feature : _filter_7) {
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
            _builder.append("\t");
            _builder.append("Q_PROPERTY(");
            String _typeOrQObject = this._cppExtensions.toTypeOrQObject(feature);
            _builder.append(_typeOrQObject, "\t");
            _builder.append(" ");
            String _name_4 = this._cppExtensions.toName(feature);
            _builder.append(_name_4, "\t");
            _builder.append(" READ ");
            String _name_5 = this._cppExtensions.toName(feature);
            _builder.append(_name_5, "\t");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
          } else {
            boolean _isLazy = this._cppExtensions.isLazy(feature);
            if (_isLazy) {
              _builder.append("\t");
              _builder.append("// ");
              String _name_6 = this._cppExtensions.toName(feature);
              _builder.append(_name_6, "\t");
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
              String _name_7 = this._cppExtensions.toName(feature);
              _builder.append(_name_7, "\t");
              _builder.append(" READ ");
              String _name_8 = this._cppExtensions.toName(feature);
              _builder.append(_name_8, "\t");
              _builder.append(" WRITE set");
              String _name_9 = this._cppExtensions.toName(feature);
              String _firstUpper_1 = StringExtensions.toFirstUpper(_name_9);
              _builder.append(_firstUpper_1, "\t");
              _builder.append(" NOTIFY ");
              String _name_10 = this._cppExtensions.toName(feature);
              _builder.append(_name_10, "\t");
              _builder.append("Changed FINAL)");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("Q_PROPERTY(");
              String _typeOrQObject_2 = this._cppExtensions.toTypeOrQObject(feature);
              _builder.append(_typeOrQObject_2, "\t");
              _builder.append(" ");
              String _name_11 = this._cppExtensions.toName(feature);
              _builder.append(_name_11, "\t");
              _builder.append("AsDataObject READ ");
              String _name_12 = this._cppExtensions.toName(feature);
              _builder.append(_name_12, "\t");
              _builder.append("AsDataObject WRITE resolve");
              String _name_13 = this._cppExtensions.toName(feature);
              String _firstUpper_2 = StringExtensions.toFirstUpper(_name_13);
              _builder.append(_firstUpper_2, "\t");
              _builder.append("AsDataObject)");
              _builder.newLineIfNotEmpty();
            } else {
              boolean _isEnum = this._cppExtensions.isEnum(feature);
              if (_isEnum) {
                _builder.append("\t");
                _builder.append("// int ENUM ");
                String _typeName_4 = this._cppExtensions.toTypeName(feature);
                _builder.append(_typeName_4, "\t");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("Q_PROPERTY(int ");
                String _name_14 = this._cppExtensions.toName(feature);
                _builder.append(_name_14, "\t");
                _builder.append(" READ ");
                String _name_15 = this._cppExtensions.toName(feature);
                _builder.append(_name_15, "\t");
                _builder.append(" WRITE set");
                String _name_16 = this._cppExtensions.toName(feature);
                String _firstUpper_3 = StringExtensions.toFirstUpper(_name_16);
                _builder.append(_firstUpper_3, "\t");
                _builder.append(" NOTIFY ");
                String _name_17 = this._cppExtensions.toName(feature);
                _builder.append(_name_17, "\t");
                _builder.append("Changed FINAL)");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("Q_PROPERTY(");
                String _typeOrQObject_3 = this._cppExtensions.toTypeOrQObject(feature);
                _builder.append(_typeOrQObject_3, "\t");
                _builder.append(" ");
                String _name_18 = this._cppExtensions.toName(feature);
                _builder.append(_name_18, "\t");
                _builder.append(" READ ");
                String _name_19 = this._cppExtensions.toName(feature);
                _builder.append(_name_19, "\t");
                _builder.append(" WRITE set");
                String _name_20 = this._cppExtensions.toName(feature);
                String _firstUpper_4 = StringExtensions.toFirstUpper(_name_20);
                _builder.append(_firstUpper_4, "\t");
                _builder.append(" NOTIFY ");
                String _name_21 = this._cppExtensions.toName(feature);
                _builder.append(_name_21, "\t");
                _builder.append("Changed FINAL)");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_8 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_8 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _and = false;
          boolean _isToMany = HppGenerator.this._cppExtensions.isToMany(it);
          if (!_isToMany) {
            _and = false;
          } else {
            boolean _isArrayList = HppGenerator.this._cppExtensions.isArrayList(it);
            boolean _not = (!_isArrayList);
            _and = _not;
          }
          return Boolean.valueOf(_and);
        }
      };
      Iterable<? extends LFeature> _filter_8 = IterableExtensions.filter(_allFeatures_8, _function_8);
      for(final LFeature feature_1 : _filter_8) {
        _builder.append("\t");
        _builder.append("// QDeclarativeListProperty to get easy access from QML");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("Q_PROPERTY(QDeclarativeListProperty<");
        String _typeName_5 = this._cppExtensions.toTypeName(feature_1);
        _builder.append(_typeName_5, "\t");
        _builder.append("> ");
        String _name_22 = this._cppExtensions.toName(feature_1);
        _builder.append(_name_22, "\t");
        _builder.append("PropertyList READ ");
        String _name_23 = this._cppExtensions.toName(feature_1);
        _builder.append(_name_23, "\t");
        _builder.append("PropertyList CONSTANT)");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      List<? extends LFeature> _allFeatures_9 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_9 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _and = false;
          boolean _isToMany = HppGenerator.this._cppExtensions.isToMany(it);
          if (!_isToMany) {
            _and = false;
          } else {
            boolean _isArrayList = HppGenerator.this._cppExtensions.isArrayList(it);
            _and = _isArrayList;
          }
          return Boolean.valueOf(_and);
        }
      };
      Iterable<? extends LFeature> _filter_9 = IterableExtensions.filter(_allFeatures_9, _function_9);
      for(final LFeature feature_2 : _filter_9) {
        {
          String _typeName_6 = this._cppExtensions.toTypeName(feature_2);
          boolean _equals = Objects.equal(_typeName_6, "QString");
          if (_equals) {
            _builder.append("\t");
            _builder.append("Q_PROPERTY(QStringList ");
            String _name_24 = this._cppExtensions.toName(feature_2);
            _builder.append(_name_24, "\t");
            _builder.append("StringList READ ");
            String _name_25 = this._cppExtensions.toName(feature_2);
            _builder.append(_name_25, "\t");
            _builder.append("StringList  WRITE set");
            String _name_26 = this._cppExtensions.toName(feature_2);
            String _firstUpper_5 = StringExtensions.toFirstUpper(_name_26);
            _builder.append(_firstUpper_5, "\t");
            _builder.append("StringList NOTIFY ");
            String _name_27 = this._cppExtensions.toName(feature_2);
            _builder.append(_name_27, "\t");
            _builder.append("StringListChanged FINAL)");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t");
            _builder.append("// QVariantList to get easy access from QML to ");
            String _typeName_7 = this._cppExtensions.toTypeName(feature_2);
            _builder.append(_typeName_7, "\t");
            _builder.append(" Array");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("Q_PROPERTY(QVariantList ");
            String _name_28 = this._cppExtensions.toName(feature_2);
            _builder.append(_name_28, "\t");
            _builder.append("List READ ");
            String _name_29 = this._cppExtensions.toName(feature_2);
            _builder.append(_name_29, "\t");
            _builder.append("List  WRITE set");
            String _name_30 = this._cppExtensions.toName(feature_2);
            String _firstUpper_6 = StringExtensions.toFirstUpper(_name_30);
            _builder.append(_firstUpper_6, "\t");
            _builder.append("List NOTIFY ");
            String _name_31 = this._cppExtensions.toName(feature_2);
            _builder.append(_name_31, "\t");
            _builder.append("ListChanged FINAL)");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.newLine();
    _builder.append("public:");
    _builder.newLine();
    _builder.append("\t");
    String _name_32 = this._cppExtensions.toName(dto);
    _builder.append(_name_32, "\t");
    _builder.append("(QObject *parent = 0);");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    {
      boolean _existsLazy = this._cppExtensions.existsLazy(dto);
      if (_existsLazy) {
        _builder.append("\t");
        _builder.append("Q_INVOKABLE");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("bool isAllResolved();");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("void fillFromMap(const QVariantMap& ");
    String _name_33 = this._cppExtensions.toName(dto);
    String _firstLower = StringExtensions.toFirstLower(_name_33);
    _builder.append(_firstLower, "\t");
    _builder.append("Map);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("void fillFromForeignMap(const QVariantMap& ");
    String _name_34 = this._cppExtensions.toName(dto);
    String _firstLower_1 = StringExtensions.toFirstLower(_name_34);
    _builder.append(_firstLower_1, "\t");
    _builder.append("Map);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("void fillFromCacheMap(const QVariantMap& ");
    String _name_35 = this._cppExtensions.toName(dto);
    String _firstLower_2 = StringExtensions.toFirstLower(_name_35);
    _builder.append(_firstLower_2, "\t");
    _builder.append("Map);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("void prepareNew();");
    _builder.newLine();
    _builder.append("\t");
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
    _builder.append("QVariantMap toForeignMap();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("QVariantMap toCacheMap();");
    _builder.newLine();
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_10 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_10 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _isToMany = HppGenerator.this._cppExtensions.isToMany(it);
          return Boolean.valueOf((!_isToMany));
        }
      };
      Iterable<? extends LFeature> _filter_10 = IterableExtensions.filter(_allFeatures_10, _function_10);
      for(final LFeature feature_3 : _filter_10) {
        {
          boolean _isLazy_1 = this._cppExtensions.isLazy(feature_3);
          if (_isLazy_1) {
            _builder.append("\t");
            _builder.append("// ");
            String _name_36 = this._cppExtensions.toName(feature_3);
            _builder.append(_name_36, "\t");
            _builder.append(" lazy pointing to ");
            String _typeOrQObject_4 = this._cppExtensions.toTypeOrQObject(feature_3);
            _builder.append(_typeOrQObject_4, "\t");
            _builder.append(" (domainKey: ");
            String _referenceDomainKey_1 = this._cppExtensions.referenceDomainKey(feature_3);
            _builder.append(_referenceDomainKey_1, "\t");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            String _referenceDomainKeyType_1 = this._cppExtensions.referenceDomainKeyType(feature_3);
            _builder.append(_referenceDomainKeyType_1, "\t");
            _builder.append(" ");
            String _name_37 = this._cppExtensions.toName(feature_3);
            _builder.append(_name_37, "\t");
            _builder.append("() const;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("void set");
            String _name_38 = this._cppExtensions.toName(feature_3);
            String _firstUpper_7 = StringExtensions.toFirstUpper(_name_38);
            _builder.append(_firstUpper_7, "\t");
            _builder.append("(");
            String _referenceDomainKeyType_2 = this._cppExtensions.referenceDomainKeyType(feature_3);
            _builder.append(_referenceDomainKeyType_2, "\t");
            _builder.append(" ");
            String _name_39 = this._cppExtensions.toName(feature_3);
            _builder.append(_name_39, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            String _typeOrQObject_5 = this._cppExtensions.toTypeOrQObject(feature_3);
            _builder.append(_typeOrQObject_5, "\t");
            _builder.append(" ");
            String _name_40 = this._cppExtensions.toName(feature_3);
            _builder.append(_name_40, "\t");
            _builder.append("AsDataObject() const;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("void resolve");
            String _name_41 = this._cppExtensions.toName(feature_3);
            String _firstUpper_8 = StringExtensions.toFirstUpper(_name_41);
            _builder.append(_firstUpper_8, "\t");
            _builder.append("AsDataObject(");
            String _typeName_8 = this._cppExtensions.toTypeName(feature_3);
            String _firstUpper_9 = StringExtensions.toFirstUpper(_typeName_8);
            _builder.append(_firstUpper_9, "\t");
            _builder.append("* ");
            String _typeName_9 = this._cppExtensions.toTypeName(feature_3);
            String _firstLower_3 = StringExtensions.toFirstLower(_typeName_9);
            _builder.append(_firstLower_3, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("void remove");
            String _name_42 = this._cppExtensions.toName(feature_3);
            String _firstUpper_10 = StringExtensions.toFirstUpper(_name_42);
            _builder.append(_firstUpper_10, "\t");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("bool has");
            String _name_43 = this._cppExtensions.toName(feature_3);
            String _firstUpper_11 = StringExtensions.toFirstUpper(_name_43);
            _builder.append(_firstUpper_11, "\t");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("bool is");
            String _name_44 = this._cppExtensions.toName(feature_3);
            String _firstUpper_12 = StringExtensions.toFirstUpper(_name_44);
            _builder.append(_firstUpper_12, "\t");
            _builder.append("ResolvedAsDataObject();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.newLine();
          } else {
            boolean _isEnum_1 = this._cppExtensions.isEnum(feature_3);
            if (_isEnum_1) {
              _builder.append("\t");
              _builder.append("int ");
              String _name_45 = this._cppExtensions.toName(feature_3);
              _builder.append(_name_45, "\t");
              _builder.append("() const;");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("void set");
              String _name_46 = this._cppExtensions.toName(feature_3);
              String _firstUpper_13 = StringExtensions.toFirstUpper(_name_46);
              _builder.append(_firstUpper_13, "\t");
              _builder.append("(int ");
              String _name_47 = this._cppExtensions.toName(feature_3);
              _builder.append(_name_47, "\t");
              _builder.append(");");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("void set");
              String _name_48 = this._cppExtensions.toName(feature_3);
              String _firstUpper_14 = StringExtensions.toFirstUpper(_name_48);
              _builder.append(_firstUpper_14, "\t");
              _builder.append("(QString ");
              String _name_49 = this._cppExtensions.toName(feature_3);
              _builder.append(_name_49, "\t");
              _builder.append(");");
              _builder.newLineIfNotEmpty();
            } else {
              _builder.append("\t");
              String _typeOrQObject_6 = this._cppExtensions.toTypeOrQObject(feature_3);
              _builder.append(_typeOrQObject_6, "\t");
              _builder.append(" ");
              String _name_50 = this._cppExtensions.toName(feature_3);
              _builder.append(_name_50, "\t");
              _builder.append("() const;");
              _builder.newLineIfNotEmpty();
              {
                boolean _isTypeOfDates = this._cppExtensions.isTypeOfDates(feature_3);
                if (_isTypeOfDates) {
                  _builder.append("\t");
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("Q_INVOKABLE");
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("bool has");
                  String _name_51 = this._cppExtensions.toName(feature_3);
                  String _firstUpper_15 = StringExtensions.toFirstUpper(_name_51);
                  _builder.append(_firstUpper_15, "\t");
                  _builder.append("();");
                  _builder.newLineIfNotEmpty();
                }
              }
              {
                boolean _and_1 = false;
                boolean _isTypeOfDataObject_1 = this._cppExtensions.isTypeOfDataObject(feature_3);
                if (!_isTypeOfDataObject_1) {
                  _and_1 = false;
                } else {
                  boolean _isContained_2 = this._cppExtensions.isContained(feature_3);
                  _and_1 = _isContained_2;
                }
                if (_and_1) {
                  _builder.append("\t");
                  _builder.append("// no SETTER ");
                  String _name_52 = this._cppExtensions.toName(feature_3);
                  _builder.append(_name_52, "\t");
                  _builder.append("() is only convenience method to get the parent");
                  _builder.newLineIfNotEmpty();
                } else {
                  _builder.append("\t");
                  _builder.append("void set");
                  String _name_53 = this._cppExtensions.toName(feature_3);
                  String _firstUpper_16 = StringExtensions.toFirstUpper(_name_53);
                  _builder.append(_firstUpper_16, "\t");
                  _builder.append("(");
                  String _typeOrQObject_7 = this._cppExtensions.toTypeOrQObject(feature_3);
                  _builder.append(_typeOrQObject_7, "\t");
                  _builder.append(" ");
                  String _name_54 = this._cppExtensions.toName(feature_3);
                  _builder.append(_name_54, "\t");
                  _builder.append(");");
                  _builder.newLineIfNotEmpty();
                  {
                    boolean _isTypeOfDataObject_2 = this._cppExtensions.isTypeOfDataObject(feature_3);
                    if (_isTypeOfDataObject_2) {
                      _builder.append("\t");
                      _builder.newLine();
                      _builder.append("\t");
                      _builder.append("Q_INVOKABLE");
                      _builder.newLine();
                      _builder.append("\t");
                      _builder.append("void delete");
                      String _name_55 = this._cppExtensions.toName(feature_3);
                      String _firstUpper_17 = StringExtensions.toFirstUpper(_name_55);
                      _builder.append(_firstUpper_17, "\t");
                      _builder.append("();");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.newLine();
                      _builder.append("\t");
                      _builder.append("Q_INVOKABLE");
                      _builder.newLine();
                      _builder.append("\t");
                      _builder.append("bool has");
                      String _name_56 = this._cppExtensions.toName(feature_3);
                      String _firstUpper_18 = StringExtensions.toFirstUpper(_name_56);
                      _builder.append(_firstUpper_18, "\t");
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
    }
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_11 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_11 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          return Boolean.valueOf(HppGenerator.this._cppExtensions.isToMany(it));
        }
      };
      Iterable<? extends LFeature> _filter_11 = IterableExtensions.filter(_allFeatures_11, _function_11);
      for(final LFeature feature_4 : _filter_11) {
        {
          boolean _isArrayList = this._cppExtensions.isArrayList(feature_4);
          boolean _not_1 = (!_isArrayList);
          if (_not_1) {
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("QVariantList ");
            String _name_57 = this._cppExtensions.toName(feature_4);
            _builder.append(_name_57, "\t");
            _builder.append("AsQVariantList();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("void addTo");
            String _name_58 = this._cppExtensions.toName(feature_4);
            String _firstUpper_19 = StringExtensions.toFirstUpper(_name_58);
            _builder.append(_firstUpper_19, "\t");
            _builder.append("(");
            String _typeName_10 = this._cppExtensions.toTypeName(feature_4);
            _builder.append(_typeName_10, "\t");
            _builder.append("* ");
            String _typeName_11 = this._cppExtensions.toTypeName(feature_4);
            String _firstLower_4 = StringExtensions.toFirstLower(_typeName_11);
            _builder.append(_firstLower_4, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("bool removeFrom");
            String _name_59 = this._cppExtensions.toName(feature_4);
            String _firstUpper_20 = StringExtensions.toFirstUpper(_name_59);
            _builder.append(_firstUpper_20, "\t");
            _builder.append("(");
            String _typeName_12 = this._cppExtensions.toTypeName(feature_4);
            _builder.append(_typeName_12, "\t");
            _builder.append("* ");
            String _typeName_13 = this._cppExtensions.toTypeName(feature_4);
            String _firstLower_5 = StringExtensions.toFirstLower(_typeName_13);
            _builder.append(_firstLower_5, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("void addTo");
            String _name_60 = this._cppExtensions.toName(feature_4);
            String _firstUpper_21 = StringExtensions.toFirstUpper(_name_60);
            _builder.append(_firstUpper_21, "\t");
            _builder.append("FromMap(const QVariantMap& ");
            String _typeName_14 = this._cppExtensions.toTypeName(feature_4);
            String _firstLower_6 = StringExtensions.toFirstLower(_typeName_14);
            _builder.append(_firstLower_6, "\t");
            _builder.append("Map);");
            _builder.newLineIfNotEmpty();
            {
              boolean _referenceHasUuid = this._cppExtensions.referenceHasUuid(feature_4);
              if (_referenceHasUuid) {
                _builder.append("\t");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("bool removeFrom");
                String _name_61 = this._cppExtensions.toName(feature_4);
                String _firstUpper_22 = StringExtensions.toFirstUpper(_name_61);
                _builder.append(_firstUpper_22, "\t");
                _builder.append("ByUuid(const QString& uuid);");
                _builder.newLineIfNotEmpty();
              }
            }
            {
              boolean _and_2 = false;
              boolean _referenceHasDomainKey = this._cppExtensions.referenceHasDomainKey(feature_4);
              if (!_referenceHasDomainKey) {
                _and_2 = false;
              } else {
                String _referenceDomainKey_2 = this._cppExtensions.referenceDomainKey(feature_4);
                boolean _notEquals_1 = (!Objects.equal(_referenceDomainKey_2, "uuid"));
                _and_2 = _notEquals_1;
              }
              if (_and_2) {
                _builder.append("\t");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("bool removeFrom");
                String _name_62 = this._cppExtensions.toName(feature_4);
                String _firstUpper_23 = StringExtensions.toFirstUpper(_name_62);
                _builder.append(_firstUpper_23, "\t");
                _builder.append("By");
                String _referenceDomainKey_3 = this._cppExtensions.referenceDomainKey(feature_4);
                String _firstUpper_24 = StringExtensions.toFirstUpper(_referenceDomainKey_3);
                _builder.append(_firstUpper_24, "\t");
                _builder.append("(const ");
                String _referenceDomainKeyType_3 = this._cppExtensions.referenceDomainKeyType(feature_4);
                _builder.append(_referenceDomainKeyType_3, "\t");
                _builder.append("& ");
                String _referenceDomainKey_4 = this._cppExtensions.referenceDomainKey(feature_4);
                _builder.append(_referenceDomainKey_4, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              }
            }
          } else {
            _builder.append("\t");
            _builder.newLine();
            {
              String _typeName_15 = this._cppExtensions.toTypeName(feature_4);
              boolean _equals_1 = Objects.equal(_typeName_15, "QString");
              if (_equals_1) {
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void addTo");
                String _name_63 = this._cppExtensions.toName(feature_4);
                String _firstUpper_25 = StringExtensions.toFirstUpper(_name_63);
                _builder.append(_firstUpper_25, "\t");
                _builder.append("StringList(const ");
                String _typeName_16 = this._cppExtensions.toTypeName(feature_4);
                _builder.append(_typeName_16, "\t");
                _builder.append("& stringValue);");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("bool removeFrom");
                String _name_64 = this._cppExtensions.toName(feature_4);
                String _firstUpper_26 = StringExtensions.toFirstUpper(_name_64);
                _builder.append(_firstUpper_26, "\t");
                _builder.append("StringList(const ");
                String _typeName_17 = this._cppExtensions.toTypeName(feature_4);
                _builder.append(_typeName_17, "\t");
                _builder.append("& stringValue);");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void addTo");
                String _name_65 = this._cppExtensions.toName(feature_4);
                String _firstUpper_27 = StringExtensions.toFirstUpper(_name_65);
                _builder.append(_firstUpper_27, "\t");
                _builder.append("List(const ");
                String _typeName_18 = this._cppExtensions.toTypeName(feature_4);
                _builder.append(_typeName_18, "\t");
                _builder.append("& ");
                String _typeName_19 = this._cppExtensions.toTypeName(feature_4);
                String _firstLower_7 = StringExtensions.toFirstLower(_typeName_19);
                _builder.append(_firstLower_7, "\t");
                _builder.append("Value);");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("bool removeFrom");
                String _name_66 = this._cppExtensions.toName(feature_4);
                String _firstUpper_28 = StringExtensions.toFirstUpper(_name_66);
                _builder.append(_firstUpper_28, "\t");
                _builder.append("List(const ");
                String _typeName_20 = this._cppExtensions.toTypeName(feature_4);
                _builder.append(_typeName_20, "\t");
                _builder.append("& ");
                String _typeName_21 = this._cppExtensions.toTypeName(feature_4);
                String _firstLower_8 = StringExtensions.toFirstLower(_typeName_21);
                _builder.append(_firstLower_8, "\t");
                _builder.append("Value);");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("Q_INVOKABLE");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("int ");
        String _name_67 = this._cppExtensions.toName(feature_4);
        String _firstLower_9 = StringExtensions.toFirstLower(_name_67);
        _builder.append(_firstLower_9, "\t");
        _builder.append("Count();");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.newLine();
        {
          boolean _isArrayList_1 = this._cppExtensions.isArrayList(feature_4);
          boolean _not_2 = (!_isArrayList_1);
          if (_not_2) {
            _builder.append("\t");
            _builder.append(" ");
            _builder.append("// access from C++ to ");
            String _name_68 = this._cppExtensions.toName(feature_4);
            _builder.append(_name_68, "\t ");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("QList<");
            String _typeName_22 = this._cppExtensions.toTypeName(feature_4);
            _builder.append(_typeName_22, "\t");
            _builder.append("*> ");
            String _name_69 = this._cppExtensions.toName(feature_4);
            _builder.append(_name_69, "\t");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("void set");
            String _name_70 = this._cppExtensions.toName(feature_4);
            String _firstUpper_29 = StringExtensions.toFirstUpper(_name_70);
            _builder.append(_firstUpper_29, "\t");
            _builder.append("(QList<");
            String _typeName_23 = this._cppExtensions.toTypeName(feature_4);
            _builder.append(_typeName_23, "\t");
            _builder.append("*> ");
            String _name_71 = this._cppExtensions.toName(feature_4);
            _builder.append(_name_71, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("// access from QML to ");
            String _name_72 = this._cppExtensions.toName(feature_4);
            _builder.append(_name_72, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("QDeclarativeListProperty<");
            String _typeName_24 = this._cppExtensions.toTypeName(feature_4);
            _builder.append(_typeName_24, "\t");
            _builder.append("> ");
            String _name_73 = this._cppExtensions.toName(feature_4);
            _builder.append(_name_73, "\t");
            _builder.append("PropertyList();");
            _builder.newLineIfNotEmpty();
          } else {
            {
              String _typeName_25 = this._cppExtensions.toTypeName(feature_4);
              boolean _equals_2 = Objects.equal(_typeName_25, "QString");
              if (_equals_2) {
                _builder.append("\t");
                _builder.append("QStringList ");
                String _name_74 = this._cppExtensions.toName(feature_4);
                _builder.append(_name_74, "\t");
                _builder.append("StringList();");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("void set");
                String _name_75 = this._cppExtensions.toName(feature_4);
                String _firstUpper_30 = StringExtensions.toFirstUpper(_name_75);
                _builder.append(_firstUpper_30, "\t");
                _builder.append("StringList(const QStringList& ");
                String _name_76 = this._cppExtensions.toName(feature_4);
                _builder.append(_name_76, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("// access from C++ to ");
                String _name_77 = this._cppExtensions.toName(feature_4);
                _builder.append(_name_77, "\t");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("QList<");
                String _typeName_26 = this._cppExtensions.toTypeName(feature_4);
                _builder.append(_typeName_26, "\t");
                _builder.append("> ");
                String _name_78 = this._cppExtensions.toName(feature_4);
                _builder.append(_name_78, "\t");
                _builder.append("();");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("void set");
                String _name_79 = this._cppExtensions.toName(feature_4);
                String _firstUpper_31 = StringExtensions.toFirstUpper(_name_79);
                _builder.append(_firstUpper_31, "\t");
                _builder.append("(QList<");
                String _typeName_27 = this._cppExtensions.toTypeName(feature_4);
                _builder.append(_typeName_27, "\t");
                _builder.append("> ");
                String _name_80 = this._cppExtensions.toName(feature_4);
                _builder.append(_name_80, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("// access from QML to ");
                String _name_81 = this._cppExtensions.toName(feature_4);
                _builder.append(_name_81, "\t");
                _builder.append(" (array of ");
                String _typeName_28 = this._cppExtensions.toTypeName(feature_4);
                _builder.append(_typeName_28, "\t");
                _builder.append(")");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("QVariantList ");
                String _name_82 = this._cppExtensions.toName(feature_4);
                _builder.append(_name_82, "\t");
                _builder.append("List();");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("void set");
                String _name_83 = this._cppExtensions.toName(feature_4);
                String _firstUpper_32 = StringExtensions.toFirstUpper(_name_83);
                _builder.append(_firstUpper_32, "\t");
                _builder.append("List(const QVariantList& ");
                String _name_84 = this._cppExtensions.toName(feature_4);
                _builder.append(_name_84, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    _builder.newLine();
    {
      boolean _isTree = this._cppExtensions.isTree(dto);
      if (_isTree) {
        _builder.append("\t");
        _builder.append("// tree with children of same type - get all as flat list");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("QList<QObject*> all");
        String _name_85 = this._cppExtensions.toName(dto);
        String _firstUpper_33 = StringExtensions.toFirstUpper(_name_85);
        _builder.append(_firstUpper_33, "\t");
        _builder.append("Children();");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("\t");
    _builder.append("virtual ~");
    String _name_86 = this._cppExtensions.toName(dto);
    String _firstUpper_34 = StringExtensions.toFirstUpper(_name_86);
    _builder.append(_firstUpper_34, "\t");
    _builder.append("();");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Q_SIGNALS:");
    _builder.newLine();
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_12 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_12 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _isToMany = HppGenerator.this._cppExtensions.isToMany(it);
          return Boolean.valueOf((!_isToMany));
        }
      };
      Iterable<? extends LFeature> _filter_12 = IterableExtensions.filter(_allFeatures_12, _function_12);
      for(final LFeature feature_5 : _filter_12) {
        {
          boolean _isLazy_2 = this._cppExtensions.isLazy(feature_5);
          if (_isLazy_2) {
            _builder.append("\t");
            _builder.append("// ");
            String _name_87 = this._cppExtensions.toName(feature_5);
            _builder.append(_name_87, "\t");
            _builder.append(" lazy pointing to ");
            String _typeOrQObject_8 = this._cppExtensions.toTypeOrQObject(feature_5);
            _builder.append(_typeOrQObject_8, "\t");
            _builder.append(" (domainKey: ");
            String _referenceDomainKey_5 = this._cppExtensions.referenceDomainKey(feature_5);
            _builder.append(_referenceDomainKey_5, "\t");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("void ");
            String _name_88 = this._cppExtensions.toName(feature_5);
            _builder.append(_name_88, "\t");
            _builder.append("Changed(");
            String _referenceDomainKeyType_4 = this._cppExtensions.referenceDomainKeyType(feature_5);
            _builder.append(_referenceDomainKeyType_4, "\t");
            _builder.append(" ");
            String _name_89 = this._cppExtensions.toName(feature_5);
            _builder.append(_name_89, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
          } else {
            boolean _isEnum_2 = this._cppExtensions.isEnum(feature_5);
            if (_isEnum_2) {
              _builder.append("\t");
              _builder.append("void ");
              String _name_90 = this._cppExtensions.toName(feature_5);
              _builder.append(_name_90, "\t");
              _builder.append("Changed(int ");
              String _name_91 = this._cppExtensions.toName(feature_5);
              _builder.append(_name_91, "\t");
              _builder.append(");");
              _builder.newLineIfNotEmpty();
            } else {
              {
                boolean _and_3 = false;
                boolean _isTypeOfDataObject_3 = this._cppExtensions.isTypeOfDataObject(feature_5);
                if (!_isTypeOfDataObject_3) {
                  _and_3 = false;
                } else {
                  boolean _isContained_3 = this._cppExtensions.isContained(feature_5);
                  _and_3 = _isContained_3;
                }
                if (_and_3) {
                  _builder.append("\t");
                  _builder.append("// no SIGNAL ");
                  String _name_92 = this._cppExtensions.toName(feature_5);
                  _builder.append(_name_92, "\t");
                  _builder.append(" is only convenience way to get the parent");
                  _builder.newLineIfNotEmpty();
                } else {
                  _builder.append("\t");
                  _builder.append("void ");
                  String _name_93 = this._cppExtensions.toName(feature_5);
                  _builder.append(_name_93, "\t");
                  _builder.append("Changed(");
                  String _typeOrQObject_9 = this._cppExtensions.toTypeOrQObject(feature_5);
                  _builder.append(_typeOrQObject_9, "\t");
                  _builder.append(" ");
                  String _name_94 = this._cppExtensions.toName(feature_5);
                  _builder.append(_name_94, "\t");
                  _builder.append(");");
                  _builder.newLineIfNotEmpty();
                  {
                    boolean _isTypeOfDataObject_4 = this._cppExtensions.isTypeOfDataObject(feature_5);
                    if (_isTypeOfDataObject_4) {
                      _builder.append("\t");
                      _builder.append("void ");
                      String _name_95 = this._cppExtensions.toName(feature_5);
                      String _firstLower_10 = StringExtensions.toFirstLower(_name_95);
                      _builder.append(_firstLower_10, "\t");
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
    }
    {
      List<? extends LFeature> _allFeatures_13 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_13 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          return Boolean.valueOf(HppGenerator.this._cppExtensions.isToMany(it));
        }
      };
      Iterable<? extends LFeature> _filter_13 = IterableExtensions.filter(_allFeatures_13, _function_13);
      for(final LFeature feature_6 : _filter_13) {
        {
          boolean _isArrayList_2 = this._cppExtensions.isArrayList(feature_6);
          boolean _not_3 = (!_isArrayList_2);
          if (_not_3) {
            _builder.append("\t");
            _builder.append("void ");
            String _name_96 = this._cppExtensions.toName(feature_6);
            _builder.append(_name_96, "\t");
            _builder.append("Changed(QList<");
            String _typeName_29 = this._cppExtensions.toTypeName(feature_6);
            _builder.append(_typeName_29, "\t");
            _builder.append("*> ");
            String _name_97 = this._cppExtensions.toName(feature_6);
            _builder.append(_name_97, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("void addedTo");
            String _name_98 = this._cppExtensions.toName(feature_6);
            String _firstUpper_35 = StringExtensions.toFirstUpper(_name_98);
            _builder.append(_firstUpper_35, "\t");
            _builder.append("(");
            String _typeName_30 = this._cppExtensions.toTypeName(feature_6);
            _builder.append(_typeName_30, "\t");
            _builder.append("* ");
            String _typeName_31 = this._cppExtensions.toTypeName(feature_6);
            String _firstLower_11 = StringExtensions.toFirstLower(_typeName_31);
            _builder.append(_firstLower_11, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            {
              boolean _referenceHasUuid_1 = this._cppExtensions.referenceHasUuid(feature_6);
              if (_referenceHasUuid_1) {
                _builder.append("\t");
                _builder.append("void removedFrom");
                String _name_99 = this._cppExtensions.toName(feature_6);
                String _firstUpper_36 = StringExtensions.toFirstUpper(_name_99);
                _builder.append(_firstUpper_36, "\t");
                _builder.append("ByUuid(QString uuid);");
                _builder.newLineIfNotEmpty();
              }
            }
            {
              boolean _and_4 = false;
              boolean _referenceHasDomainKey_1 = this._cppExtensions.referenceHasDomainKey(feature_6);
              if (!_referenceHasDomainKey_1) {
                _and_4 = false;
              } else {
                String _referenceDomainKey_6 = this._cppExtensions.referenceDomainKey(feature_6);
                boolean _notEquals_2 = (!Objects.equal(_referenceDomainKey_6, "uuid"));
                _and_4 = _notEquals_2;
              }
              if (_and_4) {
                _builder.append("\t");
                _builder.append("void removedFrom");
                String _name_100 = this._cppExtensions.toName(feature_6);
                String _firstUpper_37 = StringExtensions.toFirstUpper(_name_100);
                _builder.append(_firstUpper_37, "\t");
                _builder.append("By");
                String _referenceDomainKey_7 = this._cppExtensions.referenceDomainKey(feature_6);
                String _firstUpper_38 = StringExtensions.toFirstUpper(_referenceDomainKey_7);
                _builder.append(_firstUpper_38, "\t");
                _builder.append("(");
                String _referenceDomainKeyType_5 = this._cppExtensions.referenceDomainKeyType(feature_6);
                _builder.append(_referenceDomainKeyType_5, "\t");
                _builder.append(" ");
                String _referenceDomainKey_8 = this._cppExtensions.referenceDomainKey(feature_6);
                _builder.append(_referenceDomainKey_8, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.newLine();
          } else {
            {
              String _typeName_32 = this._cppExtensions.toTypeName(feature_6);
              boolean _equals_3 = Objects.equal(_typeName_32, "QString");
              if (_equals_3) {
                _builder.append("\t");
                _builder.append("void ");
                String _name_101 = this._cppExtensions.toName(feature_6);
                _builder.append(_name_101, "\t");
                _builder.append("StringListChanged(QStringList ");
                String _name_102 = this._cppExtensions.toName(feature_6);
                _builder.append(_name_102, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("void addedTo");
                String _name_103 = this._cppExtensions.toName(feature_6);
                String _firstUpper_39 = StringExtensions.toFirstUpper(_name_103);
                _builder.append(_firstUpper_39, "\t");
                _builder.append("StringList(");
                String _typeName_33 = this._cppExtensions.toTypeName(feature_6);
                _builder.append(_typeName_33, "\t");
                _builder.append(" stringValue);");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("void removedFrom");
                String _name_104 = this._cppExtensions.toName(feature_6);
                String _firstUpper_40 = StringExtensions.toFirstUpper(_name_104);
                _builder.append(_firstUpper_40, "\t");
                _builder.append("StringList(");
                String _typeName_34 = this._cppExtensions.toTypeName(feature_6);
                _builder.append(_typeName_34, "\t");
                _builder.append(" stringValue);");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("void ");
                String _name_105 = this._cppExtensions.toName(feature_6);
                _builder.append(_name_105, "\t");
                _builder.append("ListChanged(QVariantList ");
                String _name_106 = this._cppExtensions.toName(feature_6);
                _builder.append(_name_106, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("void addedTo");
                String _name_107 = this._cppExtensions.toName(feature_6);
                String _firstUpper_41 = StringExtensions.toFirstUpper(_name_107);
                _builder.append(_firstUpper_41, "\t");
                _builder.append("List(");
                String _typeName_35 = this._cppExtensions.toTypeName(feature_6);
                _builder.append(_typeName_35, "\t");
                _builder.append(" ");
                String _typeName_36 = this._cppExtensions.toTypeName(feature_6);
                String _firstLower_12 = StringExtensions.toFirstLower(_typeName_36);
                _builder.append(_firstLower_12, "\t");
                _builder.append("Value);");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("void removedFrom");
                String _name_108 = this._cppExtensions.toName(feature_6);
                String _firstUpper_42 = StringExtensions.toFirstUpper(_name_108);
                _builder.append(_firstUpper_42, "\t");
                _builder.append("List(");
                String _typeName_37 = this._cppExtensions.toTypeName(feature_6);
                _builder.append(_typeName_37, "\t");
                _builder.append(" ");
                String _typeName_38 = this._cppExtensions.toTypeName(feature_6);
                String _firstLower_13 = StringExtensions.toFirstLower(_typeName_38);
                _builder.append(_firstLower_13, "\t");
                _builder.append("Value);");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.newLine();
    _builder.append("private:");
    _builder.newLine();
    _builder.newLine();
    {
      List<? extends LFeature> _allFeatures_14 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_14 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          boolean _isToMany = HppGenerator.this._cppExtensions.isToMany(it);
          return Boolean.valueOf((!_isToMany));
        }
      };
      Iterable<? extends LFeature> _filter_14 = IterableExtensions.filter(_allFeatures_14, _function_14);
      for(final LFeature feature_7 : _filter_14) {
        {
          boolean _and_5 = false;
          boolean _isTypeOfDataObject_5 = this._cppExtensions.isTypeOfDataObject(feature_7);
          if (!_isTypeOfDataObject_5) {
            _and_5 = false;
          } else {
            boolean _isContained_4 = this._cppExtensions.isContained(feature_7);
            _and_5 = _isContained_4;
          }
          if (_and_5) {
            _builder.append("\t");
            _builder.append("// no MEMBER m");
            String _name_109 = this._cppExtensions.toName(feature_7);
            String _firstUpper_43 = StringExtensions.toFirstUpper(_name_109);
            _builder.append(_firstUpper_43, "\t");
            _builder.append(" it\'s the parent");
            _builder.newLineIfNotEmpty();
          } else {
            boolean _isLazy_3 = this._cppExtensions.isLazy(feature_7);
            if (_isLazy_3) {
              _builder.append("\t");
              String _referenceDomainKeyType_6 = this._cppExtensions.referenceDomainKeyType(feature_7);
              _builder.append(_referenceDomainKeyType_6, "\t");
              _builder.append(" m");
              String _name_110 = this._cppExtensions.toName(feature_7);
              String _firstUpper_44 = StringExtensions.toFirstUpper(_name_110);
              _builder.append(_firstUpper_44, "\t");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              String _typeOrQObject_10 = this._cppExtensions.toTypeOrQObject(feature_7);
              _builder.append(_typeOrQObject_10, "\t");
              _builder.append(" m");
              String _name_111 = this._cppExtensions.toName(feature_7);
              String _firstUpper_45 = StringExtensions.toFirstUpper(_name_111);
              _builder.append(_firstUpper_45, "\t");
              _builder.append("AsDataObject;");
              _builder.newLineIfNotEmpty();
            } else {
              boolean _isEnum_3 = this._cppExtensions.isEnum(feature_7);
              if (_isEnum_3) {
                _builder.append("\t");
                _builder.append("int m");
                String _name_112 = this._cppExtensions.toName(feature_7);
                String _firstUpper_46 = StringExtensions.toFirstUpper(_name_112);
                _builder.append(_firstUpper_46, "\t");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("int ");
                String _name_113 = this._cppExtensions.toName(feature_7);
                String _firstLower_14 = StringExtensions.toFirstLower(_name_113);
                _builder.append(_firstLower_14, "\t");
                _builder.append("StringToInt(QString ");
                String _name_114 = this._cppExtensions.toName(feature_7);
                String _firstLower_15 = StringExtensions.toFirstLower(_name_114);
                _builder.append(_firstLower_15, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                String _typeOrQObject_11 = this._cppExtensions.toTypeOrQObject(feature_7);
                _builder.append(_typeOrQObject_11, "\t");
                _builder.append(" m");
                String _name_115 = this._cppExtensions.toName(feature_7);
                String _firstUpper_47 = StringExtensions.toFirstUpper(_name_115);
                _builder.append(_firstUpper_47, "\t");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    {
      List<? extends LFeature> _allFeatures_15 = dto.getAllFeatures();
      final Function1<LFeature, Boolean> _function_15 = new Function1<LFeature, Boolean>() {
        public Boolean apply(final LFeature it) {
          return Boolean.valueOf(HppGenerator.this._cppExtensions.isToMany(it));
        }
      };
      Iterable<? extends LFeature> _filter_15 = IterableExtensions.filter(_allFeatures_15, _function_15);
      for(final LFeature feature_8 : _filter_15) {
        {
          boolean _isArrayList_3 = this._cppExtensions.isArrayList(feature_8);
          boolean _not_4 = (!_isArrayList_3);
          if (_not_4) {
            _builder.append("\t");
            _builder.append("QList<");
            String _typeName_39 = this._cppExtensions.toTypeName(feature_8);
            _builder.append(_typeName_39, "\t");
            _builder.append("*> m");
            String _name_116 = this._cppExtensions.toName(feature_8);
            String _firstUpper_48 = StringExtensions.toFirstUpper(_name_116);
            _builder.append(_firstUpper_48, "\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("// implementation for QDeclarativeListProperty to use");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("// QML functions for List of ");
            String _typeName_40 = this._cppExtensions.toTypeName(feature_8);
            _builder.append(_typeName_40, "\t");
            _builder.append("*");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("static void appendTo");
            String _name_117 = this._cppExtensions.toName(feature_8);
            String _firstUpper_49 = StringExtensions.toFirstUpper(_name_117);
            _builder.append(_firstUpper_49, "\t");
            _builder.append("Property(QDeclarativeListProperty<");
            String _typeName_41 = this._cppExtensions.toTypeName(feature_8);
            _builder.append(_typeName_41, "\t");
            _builder.append("> *");
            String _name_118 = this._cppExtensions.toName(feature_8);
            _builder.append(_name_118, "\t");
            _builder.append("List,");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            String _typeName_42 = this._cppExtensions.toTypeName(feature_8);
            _builder.append(_typeName_42, "\t\t");
            _builder.append("* ");
            String _typeName_43 = this._cppExtensions.toTypeName(feature_8);
            String _firstLower_16 = StringExtensions.toFirstLower(_typeName_43);
            _builder.append(_firstLower_16, "\t\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("static int ");
            String _name_119 = this._cppExtensions.toName(feature_8);
            _builder.append(_name_119, "\t");
            _builder.append("PropertyCount(QDeclarativeListProperty<");
            String _typeName_44 = this._cppExtensions.toTypeName(feature_8);
            _builder.append(_typeName_44, "\t");
            _builder.append("> *");
            String _name_120 = this._cppExtensions.toName(feature_8);
            _builder.append(_name_120, "\t");
            _builder.append("List);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("static ");
            String _typeName_45 = this._cppExtensions.toTypeName(feature_8);
            _builder.append(_typeName_45, "\t");
            _builder.append("* at");
            String _name_121 = this._cppExtensions.toName(feature_8);
            String _firstUpper_50 = StringExtensions.toFirstUpper(_name_121);
            _builder.append(_firstUpper_50, "\t");
            _builder.append("Property(QDeclarativeListProperty<");
            String _typeName_46 = this._cppExtensions.toTypeName(feature_8);
            _builder.append(_typeName_46, "\t");
            _builder.append("> *");
            String _name_122 = this._cppExtensions.toName(feature_8);
            _builder.append(_name_122, "\t");
            _builder.append("List, int pos);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("static void clear");
            String _name_123 = this._cppExtensions.toName(feature_8);
            String _firstUpper_51 = StringExtensions.toFirstUpper(_name_123);
            _builder.append(_firstUpper_51, "\t");
            _builder.append("Property(QDeclarativeListProperty<");
            String _typeName_47 = this._cppExtensions.toTypeName(feature_8);
            _builder.append(_typeName_47, "\t");
            _builder.append("> *");
            String _name_124 = this._cppExtensions.toName(feature_8);
            _builder.append(_name_124, "\t");
            _builder.append("List);");
            _builder.newLineIfNotEmpty();
          } else {
            {
              String _typeName_48 = this._cppExtensions.toTypeName(feature_8);
              boolean _equals_4 = Objects.equal(_typeName_48, "QString");
              if (_equals_4) {
                _builder.append("\t");
                _builder.append("QStringList m");
                String _name_125 = this._cppExtensions.toName(feature_8);
                String _firstUpper_52 = StringExtensions.toFirstUpper(_name_125);
                _builder.append(_firstUpper_52, "\t");
                _builder.append("StringList;");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("QList<");
                String _typeName_49 = this._cppExtensions.toTypeName(feature_8);
                _builder.append(_typeName_49, "\t");
                _builder.append("> m");
                String _name_126 = this._cppExtensions.toName(feature_8);
                String _firstUpper_53 = StringExtensions.toFirstUpper(_name_126);
                _builder.append(_firstUpper_53, "\t");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Q_DISABLE_COPY (");
    String _name_127 = this._cppExtensions.toName(dto);
    String _firstUpper_54 = StringExtensions.toFirstUpper(_name_127);
    _builder.append(_firstUpper_54, "\t");
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    _builder.append("};");
    _builder.newLine();
    _builder.append("Q_DECLARE_METATYPE(");
    String _name_128 = this._cppExtensions.toName(dto);
    String _firstUpper_55 = StringExtensions.toFirstUpper(_name_128);
    _builder.append(_firstUpper_55, "");
    _builder.append("*)");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("#endif /* ");
    String _name_129 = this._cppExtensions.toName(dto);
    String _upperCase_2 = _name_129.toUpperCase();
    _builder.append(_upperCase_2, "");
    _builder.append("_HPP_ */");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    return _builder;
  }
}
