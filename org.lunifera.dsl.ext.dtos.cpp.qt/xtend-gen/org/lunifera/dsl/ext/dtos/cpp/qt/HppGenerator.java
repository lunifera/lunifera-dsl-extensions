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
import org.eclipse.emf.common.util.EList;
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
import org.lunifera.dsl.semantic.dto.LDtoFeature;

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
      } else {
        boolean _existsHierarchy = this._cppExtensions.existsHierarchy(dto);
        if (_existsHierarchy) {
          _builder.append("#include <QDeclarativeListProperty>");
          _builder.newLine();
        }
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
            boolean _or = false;
            boolean _isLazyArray = HppGenerator.this._cppExtensions.isLazyArray(it);
            if (_isLazyArray) {
              _or = true;
            } else {
              String _typeName = HppGenerator.this._cppExtensions.toTypeName(it);
              boolean _equals = Objects.equal(_typeName, "QString");
              _or = _equals;
            }
            _and = _or;
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
    {
      boolean _hasSqlCachePropertyName = this._cppExtensions.hasSqlCachePropertyName(dto);
      if (_hasSqlCachePropertyName) {
        _builder.append("#include <QtSql/QSqlQuery>");
        _builder.newLine();
        _builder.append("#include <QtSql/QSqlRecord>");
        _builder.newLine();
      }
    }
    {
      boolean _existsGeoCoordinate = this._cppExtensions.existsGeoCoordinate(dto);
      if (_existsGeoCoordinate) {
        _builder.append("// #include <QtLocationSubset/QGeoCoordinate>");
        _builder.newLine();
      }
    }
    {
      boolean _existsGeoAddress = this._cppExtensions.existsGeoAddress(dto);
      if (_existsGeoAddress) {
        _builder.append("// #include <QtLocationSubset/QGeoAddress>");
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
                {
                  boolean _isReferencing = this._cppExtensions.isReferencing(reference, dto);
                  if (_isReferencing) {
                    _builder.append("// forward declaration (target references to this)");
                    _builder.newLine();
                    _builder.append("class ");
                    String _typeName_2 = this._cppExtensions.toTypeName(reference);
                    String _firstUpper = StringExtensions.toFirstUpper(_typeName_2);
                    _builder.append(_firstUpper, "");
                    _builder.append(";");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("#include \"");
                    String _typeName_3 = this._cppExtensions.toTypeName(reference);
                    _builder.append(_typeName_3, "");
                    _builder.append(".hpp\"");
                    _builder.newLineIfNotEmpty();
                  }
                }
              }
            }
          } else {
            _builder.append("// forward declaration to avoid circular dependencies");
            _builder.newLine();
            _builder.append("class ");
            String _typeName_4 = this._cppExtensions.toTypeName(reference);
            String _firstUpper_1 = StringExtensions.toFirstUpper(_typeName_4);
            _builder.append(_firstUpper_1, "");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.newLine();
    {
      boolean _existsGeo = this._cppExtensions.existsGeo(dto);
      if (_existsGeo) {
        _builder.append("// using namespace QtMobilitySubset;\t");
        _builder.newLine();
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
              String _firstUpper_2 = StringExtensions.toFirstUpper(_name_9);
              _builder.append(_firstUpper_2, "\t");
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
              String _firstUpper_3 = StringExtensions.toFirstUpper(_name_13);
              _builder.append(_firstUpper_3, "\t");
              _builder.append("AsDataObject NOTIFY ");
              String _name_14 = this._cppExtensions.toName(feature);
              _builder.append(_name_14, "\t");
              _builder.append("AsDataObjectChanged FINAL)");
              _builder.newLineIfNotEmpty();
              {
                boolean _isHierarchy = this._cppExtensions.isHierarchy(dto, feature);
                if (_isHierarchy) {
                  _builder.append("\t");
                  _builder.append("// QDeclarativeListProperty to get easy access to hierarchy of ");
                  String _name_15 = this._cppExtensions.toName(feature);
                  _builder.append(_name_15, "\t");
                  _builder.append(" property from QML");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("// Must always be initialized from DataManager before");
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("// DataManager::init");
                  String _name_16 = this._cppExtensions.toName(feature);
                  String _firstUpper_4 = StringExtensions.toFirstUpper(_name_16);
                  _builder.append(_firstUpper_4, "\t");
                  _builder.append("HierarchyList");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("Q_PROPERTY(QDeclarativeListProperty<");
                  String _name_17 = dto.getName();
                  _builder.append(_name_17, "\t");
                  _builder.append("> ");
                  String _name_18 = this._cppExtensions.toName(feature);
                  _builder.append(_name_18, "\t");
                  _builder.append("PropertyList READ ");
                  String _name_19 = this._cppExtensions.toName(feature);
                  _builder.append(_name_19, "\t");
                  _builder.append("PropertyList CONSTANT)");
                  _builder.newLineIfNotEmpty();
                }
              }
            } else {
              boolean _isEnum = this._cppExtensions.isEnum(feature);
              if (_isEnum) {
                _builder.append("\t");
                _builder.append("// int ENUM ");
                String _typeName_5 = this._cppExtensions.toTypeName(feature);
                _builder.append(_typeName_5, "\t");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("Q_PROPERTY(int ");
                String _name_20 = this._cppExtensions.toName(feature);
                _builder.append(_name_20, "\t");
                _builder.append(" READ ");
                String _name_21 = this._cppExtensions.toName(feature);
                _builder.append(_name_21, "\t");
                _builder.append(" WRITE set");
                String _name_22 = this._cppExtensions.toName(feature);
                String _firstUpper_5 = StringExtensions.toFirstUpper(_name_22);
                _builder.append(_firstUpper_5, "\t");
                _builder.append(" NOTIFY ");
                String _name_23 = this._cppExtensions.toName(feature);
                _builder.append(_name_23, "\t");
                _builder.append("Changed FINAL)");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("Q_PROPERTY(");
                String _typeOrQObject_3 = this._cppExtensions.toTypeOrQObject(feature);
                _builder.append(_typeOrQObject_3, "\t");
                _builder.append(" ");
                String _name_24 = this._cppExtensions.toName(feature);
                _builder.append(_name_24, "\t");
                _builder.append(" READ ");
                String _name_25 = this._cppExtensions.toName(feature);
                _builder.append(_name_25, "\t");
                _builder.append(" WRITE set");
                String _name_26 = this._cppExtensions.toName(feature);
                String _firstUpper_6 = StringExtensions.toFirstUpper(_name_26);
                _builder.append(_firstUpper_6, "\t");
                _builder.append(" NOTIFY ");
                String _name_27 = this._cppExtensions.toName(feature);
                _builder.append(_name_27, "\t");
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
        String _typeName_6 = this._cppExtensions.toTypeName(feature_1);
        _builder.append(_typeName_6, "\t");
        _builder.append("> ");
        String _name_28 = this._cppExtensions.toName(feature_1);
        _builder.append(_name_28, "\t");
        _builder.append("PropertyList READ ");
        String _name_29 = this._cppExtensions.toName(feature_1);
        _builder.append(_name_29, "\t");
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
          String _typeName_7 = this._cppExtensions.toTypeName(feature_2);
          boolean _equals = Objects.equal(_typeName_7, "QString");
          if (_equals) {
            _builder.append("\t");
            _builder.append("Q_PROPERTY(QStringList ");
            String _name_30 = this._cppExtensions.toName(feature_2);
            _builder.append(_name_30, "\t");
            _builder.append("StringList READ ");
            String _name_31 = this._cppExtensions.toName(feature_2);
            _builder.append(_name_31, "\t");
            _builder.append("StringList  WRITE set");
            String _name_32 = this._cppExtensions.toName(feature_2);
            String _firstUpper_7 = StringExtensions.toFirstUpper(_name_32);
            _builder.append(_firstUpper_7, "\t");
            _builder.append("StringList NOTIFY ");
            String _name_33 = this._cppExtensions.toName(feature_2);
            _builder.append(_name_33, "\t");
            _builder.append("StringListChanged FINAL)");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t");
            _builder.append("// QVariantList to get easy access from QML to ");
            String _typeName_8 = this._cppExtensions.toTypeName(feature_2);
            _builder.append(_typeName_8, "\t");
            _builder.append(" Array");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("Q_PROPERTY(QVariantList ");
            String _name_34 = this._cppExtensions.toName(feature_2);
            _builder.append(_name_34, "\t");
            _builder.append("List READ ");
            String _name_35 = this._cppExtensions.toName(feature_2);
            _builder.append(_name_35, "\t");
            _builder.append("List  WRITE set");
            String _name_36 = this._cppExtensions.toName(feature_2);
            String _firstUpper_8 = StringExtensions.toFirstUpper(_name_36);
            _builder.append(_firstUpper_8, "\t");
            _builder.append("List NOTIFY ");
            String _name_37 = this._cppExtensions.toName(feature_2);
            _builder.append(_name_37, "\t");
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
    String _name_38 = this._cppExtensions.toName(dto);
    _builder.append(_name_38, "\t");
    _builder.append("(QObject *parent = 0);");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    {
      boolean _or = false;
      boolean _existsLazy = this._cppExtensions.existsLazy(dto);
      if (_existsLazy) {
        _or = true;
      } else {
        boolean _existsLazyArray = this._cppExtensions.existsLazyArray(dto);
        _or = _existsLazyArray;
      }
      if (_or) {
        _builder.append("\t");
        _builder.append("Q_INVOKABLE");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("bool isAllResolved();");
        _builder.newLine();
      }
    }
    _builder.newLine();
    _builder.append("\t");
    _builder.append("void fillFromMap(const QVariantMap& ");
    String _name_39 = this._cppExtensions.toName(dto);
    String _firstLower = StringExtensions.toFirstLower(_name_39);
    _builder.append(_firstLower, "\t");
    _builder.append("Map);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("void fillFromForeignMap(const QVariantMap& ");
    String _name_40 = this._cppExtensions.toName(dto);
    String _firstLower_1 = StringExtensions.toFirstLower(_name_40);
    _builder.append(_firstLower_1, "\t");
    _builder.append("Map);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("void fillFromCacheMap(const QVariantMap& ");
    String _name_41 = this._cppExtensions.toName(dto);
    String _firstLower_2 = StringExtensions.toFirstLower(_name_41);
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
            String _name_42 = this._cppExtensions.toName(feature_3);
            _builder.append(_name_42, "\t");
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
            String _name_43 = this._cppExtensions.toName(feature_3);
            _builder.append(_name_43, "\t");
            _builder.append("() const;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("void set");
            String _name_44 = this._cppExtensions.toName(feature_3);
            String _firstUpper_9 = StringExtensions.toFirstUpper(_name_44);
            _builder.append(_firstUpper_9, "\t");
            _builder.append("(");
            String _referenceDomainKeyType_2 = this._cppExtensions.referenceDomainKeyType(feature_3);
            _builder.append(_referenceDomainKeyType_2, "\t");
            _builder.append(" ");
            String _name_45 = this._cppExtensions.toName(feature_3);
            _builder.append(_name_45, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            String _typeOrQObject_5 = this._cppExtensions.toTypeOrQObject(feature_3);
            _builder.append(_typeOrQObject_5, "\t");
            _builder.append(" ");
            String _name_46 = this._cppExtensions.toName(feature_3);
            _builder.append(_name_46, "\t");
            _builder.append("AsDataObject() const;");
            _builder.newLineIfNotEmpty();
            {
              boolean _isHierarchy_1 = this._cppExtensions.isHierarchy(dto, feature_3);
              if (_isHierarchy_1) {
                _builder.append("\t");
                _builder.append("// access to the hierarchy - must be initialized from DataManager before");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("// DataManager::init");
                String _name_47 = this._cppExtensions.toName(feature_3);
                String _firstUpper_10 = StringExtensions.toFirstUpper(_name_47);
                _builder.append(_firstUpper_10, "\t");
                _builder.append("HierarchyList");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("void init");
                String _name_48 = this._cppExtensions.toName(feature_3);
                String _firstUpper_11 = StringExtensions.toFirstUpper(_name_48);
                _builder.append(_firstUpper_11, "\t");
                _builder.append("PropertyList(QList<");
                String _name_49 = dto.getName();
                _builder.append(_name_49, "\t");
                _builder.append("*> ");
                String _name_50 = this._cppExtensions.toName(feature_3);
                _builder.append(_name_50, "\t");
                _builder.append("PropertyList);");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("// if list should be cleared from C++ - from QML use QDeclarativeList function");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void clear");
                String _name_51 = this._cppExtensions.toName(feature_3);
                String _firstUpper_12 = StringExtensions.toFirstUpper(_name_51);
                _builder.append(_firstUpper_12, "\t");
                _builder.append("PropertyList();");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("QDeclarativeListProperty<");
                String _name_52 = dto.getName();
                _builder.append(_name_52, "\t");
                _builder.append("> ");
                String _name_53 = this._cppExtensions.toName(feature_3);
                _builder.append(_name_53, "\t");
                _builder.append("PropertyList();");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("void resolve");
            String _name_54 = this._cppExtensions.toName(feature_3);
            String _firstUpper_13 = StringExtensions.toFirstUpper(_name_54);
            _builder.append(_firstUpper_13, "\t");
            _builder.append("AsDataObject(");
            String _typeName_9 = this._cppExtensions.toTypeName(feature_3);
            String _firstUpper_14 = StringExtensions.toFirstUpper(_typeName_9);
            _builder.append(_firstUpper_14, "\t");
            _builder.append("* ");
            String _typeName_10 = this._cppExtensions.toTypeName(feature_3);
            String _firstLower_3 = StringExtensions.toFirstLower(_typeName_10);
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
            String _name_55 = this._cppExtensions.toName(feature_3);
            String _firstUpper_15 = StringExtensions.toFirstUpper(_name_55);
            _builder.append(_firstUpper_15, "\t");
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
            String _firstUpper_16 = StringExtensions.toFirstUpper(_name_56);
            _builder.append(_firstUpper_16, "\t");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("bool is");
            String _name_57 = this._cppExtensions.toName(feature_3);
            String _firstUpper_17 = StringExtensions.toFirstUpper(_name_57);
            _builder.append(_firstUpper_17, "\t");
            _builder.append("ResolvedAsDataObject();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("void mark");
            String _name_58 = this._cppExtensions.toName(feature_3);
            String _firstUpper_18 = StringExtensions.toFirstUpper(_name_58);
            _builder.append(_firstUpper_18, "\t");
            _builder.append("AsInvalid();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.newLine();
          } else {
            boolean _isEnum_1 = this._cppExtensions.isEnum(feature_3);
            if (_isEnum_1) {
              _builder.append("\t");
              _builder.append("int ");
              String _name_59 = this._cppExtensions.toName(feature_3);
              _builder.append(_name_59, "\t");
              _builder.append("() const;");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("void set");
              String _name_60 = this._cppExtensions.toName(feature_3);
              String _firstUpper_19 = StringExtensions.toFirstUpper(_name_60);
              _builder.append(_firstUpper_19, "\t");
              _builder.append("(int ");
              String _name_61 = this._cppExtensions.toName(feature_3);
              _builder.append(_name_61, "\t");
              _builder.append(");");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("void set");
              String _name_62 = this._cppExtensions.toName(feature_3);
              String _firstUpper_20 = StringExtensions.toFirstUpper(_name_62);
              _builder.append(_firstUpper_20, "\t");
              _builder.append("(QString ");
              String _name_63 = this._cppExtensions.toName(feature_3);
              _builder.append(_name_63, "\t");
              _builder.append(");");
              _builder.newLineIfNotEmpty();
            } else {
              _builder.append("\t");
              String _typeOrQObject_6 = this._cppExtensions.toTypeOrQObject(feature_3);
              _builder.append(_typeOrQObject_6, "\t");
              _builder.append(" ");
              String _name_64 = this._cppExtensions.toName(feature_3);
              _builder.append(_name_64, "\t");
              _builder.append("() const;");
              _builder.newLineIfNotEmpty();
              {
                boolean _isTypeOfDates = this._cppExtensions.isTypeOfDates(feature_3);
                if (_isTypeOfDates) {
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("Q_INVOKABLE");
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("bool has");
                  String _name_65 = this._cppExtensions.toName(feature_3);
                  String _firstUpper_21 = StringExtensions.toFirstUpper(_name_65);
                  _builder.append(_firstUpper_21, "\t");
                  _builder.append("();");
                  _builder.newLineIfNotEmpty();
                  {
                    String _typeName_11 = this._cppExtensions.toTypeName(feature_3);
                    boolean _equals_1 = Objects.equal(_typeName_11, "QTime");
                    if (_equals_1) {
                      _builder.newLine();
                      _builder.append("\t");
                      _builder.append("Q_INVOKABLE");
                      _builder.newLine();
                      _builder.append("\t");
                      _builder.append("void set");
                      String _name_66 = this._cppExtensions.toName(feature_3);
                      String _firstUpper_22 = StringExtensions.toFirstUpper(_name_66);
                      _builder.append(_firstUpper_22, "\t");
                      _builder.append("FromPickerValue(QString ");
                      String _name_67 = this._cppExtensions.toName(feature_3);
                      String _firstLower_4 = StringExtensions.toFirstLower(_name_67);
                      _builder.append(_firstLower_4, "\t");
                      _builder.append("Value);");
                      _builder.newLineIfNotEmpty();
                    }
                  }
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
                  String _name_68 = this._cppExtensions.toName(feature_3);
                  _builder.append(_name_68, "\t");
                  _builder.append("() is only convenience method to get the parent");
                  _builder.newLineIfNotEmpty();
                } else {
                  _builder.append("\t");
                  _builder.append("void set");
                  String _name_69 = this._cppExtensions.toName(feature_3);
                  String _firstUpper_23 = StringExtensions.toFirstUpper(_name_69);
                  _builder.append(_firstUpper_23, "\t");
                  _builder.append("(");
                  String _typeOrQObject_7 = this._cppExtensions.toTypeOrQObject(feature_3);
                  _builder.append(_typeOrQObject_7, "\t");
                  _builder.append(" ");
                  String _name_70 = this._cppExtensions.toName(feature_3);
                  _builder.append(_name_70, "\t");
                  _builder.append(");");
                  _builder.newLineIfNotEmpty();
                  {
                    boolean _isTypeOfDataObject_2 = this._cppExtensions.isTypeOfDataObject(feature_3);
                    if (_isTypeOfDataObject_2) {
                      {
                        boolean _and_2 = false;
                        String _typeName_12 = this._cppExtensions.toTypeName(feature_3);
                        boolean _equals_2 = Objects.equal(_typeName_12, "GeoAddress");
                        boolean _not_1 = (!_equals_2);
                        if (!_not_1) {
                          _and_2 = false;
                        } else {
                          String _typeName_13 = this._cppExtensions.toTypeName(feature_3);
                          boolean _equals_3 = Objects.equal(_typeName_13, "GeoCoordinate");
                          boolean _not_2 = (!_equals_3);
                          _and_2 = _not_2;
                        }
                        if (_and_2) {
                          _builder.append("\t");
                          _builder.append("Q_INVOKABLE");
                          _builder.newLine();
                          _builder.append("\t");
                          String _typeName_14 = this._cppExtensions.toTypeName(feature_3);
                          _builder.append(_typeName_14, "\t");
                          _builder.append("* create");
                          String _name_71 = this._cppExtensions.toName(feature_3);
                          String _firstUpper_24 = StringExtensions.toFirstUpper(_name_71);
                          _builder.append(_firstUpper_24, "\t");
                          _builder.append("();");
                          _builder.newLineIfNotEmpty();
                          _builder.newLine();
                          _builder.append("\t");
                          _builder.append("Q_INVOKABLE");
                          _builder.newLine();
                          _builder.append("\t");
                          _builder.append("void undoCreate");
                          String _name_72 = this._cppExtensions.toName(feature_3);
                          String _firstUpper_25 = StringExtensions.toFirstUpper(_name_72);
                          _builder.append(_firstUpper_25, "\t");
                          _builder.append("(");
                          String _typeName_15 = this._cppExtensions.toTypeName(feature_3);
                          _builder.append(_typeName_15, "\t");
                          _builder.append("* ");
                          String _typeName_16 = this._cppExtensions.toTypeName(feature_3);
                          String _firstLower_5 = StringExtensions.toFirstLower(_typeName_16);
                          _builder.append(_firstLower_5, "\t");
                          _builder.append(");");
                          _builder.newLineIfNotEmpty();
                          _builder.append("\t");
                          _builder.newLine();
                        }
                      }
                      _builder.append("\t");
                      _builder.append("Q_INVOKABLE");
                      _builder.newLine();
                      _builder.append("\t");
                      _builder.append("void delete");
                      String _name_73 = this._cppExtensions.toName(feature_3);
                      String _firstUpper_26 = StringExtensions.toFirstUpper(_name_73);
                      _builder.append(_firstUpper_26, "\t");
                      _builder.append("();");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.newLine();
                      _builder.append("\t");
                      _builder.append("Q_INVOKABLE");
                      _builder.newLine();
                      _builder.append("\t");
                      _builder.append("bool has");
                      String _name_74 = this._cppExtensions.toName(feature_3);
                      String _firstUpper_27 = StringExtensions.toFirstUpper(_name_74);
                      _builder.append(_firstUpper_27, "\t");
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
          boolean _not_3 = (!_isArrayList);
          if (_not_3) {
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("QVariantList ");
            String _name_75 = this._cppExtensions.toName(feature_4);
            _builder.append(_name_75, "\t");
            _builder.append("AsQVariantList();");
            _builder.newLineIfNotEmpty();
            _builder.newLine();
            {
              boolean _isTypeRootDataObject = this._cppExtensions.isTypeRootDataObject(feature_4);
              boolean _not_4 = (!_isTypeRootDataObject);
              if (_not_4) {
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                String _typeName_17 = this._cppExtensions.toTypeName(feature_4);
                _builder.append(_typeName_17, "\t");
                _builder.append("* createElementOf");
                String _name_76 = this._cppExtensions.toName(feature_4);
                String _firstUpper_28 = StringExtensions.toFirstUpper(_name_76);
                _builder.append(_firstUpper_28, "\t");
                _builder.append("();");
                _builder.newLineIfNotEmpty();
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void undoCreateElementOf");
                String _name_77 = this._cppExtensions.toName(feature_4);
                String _firstUpper_29 = StringExtensions.toFirstUpper(_name_77);
                _builder.append(_firstUpper_29, "\t");
                _builder.append("(");
                String _typeName_18 = this._cppExtensions.toTypeName(feature_4);
                _builder.append(_typeName_18, "\t");
                _builder.append("* ");
                String _typeName_19 = this._cppExtensions.toTypeName(feature_4);
                String _firstLower_6 = StringExtensions.toFirstLower(_typeName_19);
                _builder.append(_firstLower_6, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("void addTo");
            String _name_78 = this._cppExtensions.toName(feature_4);
            String _firstUpper_30 = StringExtensions.toFirstUpper(_name_78);
            _builder.append(_firstUpper_30, "\t");
            _builder.append("(");
            String _typeName_20 = this._cppExtensions.toTypeName(feature_4);
            _builder.append(_typeName_20, "\t");
            _builder.append("* ");
            String _typeName_21 = this._cppExtensions.toTypeName(feature_4);
            String _firstLower_7 = StringExtensions.toFirstLower(_typeName_21);
            _builder.append(_firstLower_7, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("bool removeFrom");
            String _name_79 = this._cppExtensions.toName(feature_4);
            String _firstUpper_31 = StringExtensions.toFirstUpper(_name_79);
            _builder.append(_firstUpper_31, "\t");
            _builder.append("(");
            String _typeName_22 = this._cppExtensions.toTypeName(feature_4);
            _builder.append(_typeName_22, "\t");
            _builder.append("* ");
            String _typeName_23 = this._cppExtensions.toTypeName(feature_4);
            String _firstLower_8 = StringExtensions.toFirstLower(_typeName_23);
            _builder.append(_firstLower_8, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("void clear");
            String _name_80 = this._cppExtensions.toName(feature_4);
            String _firstUpper_32 = StringExtensions.toFirstUpper(_name_80);
            _builder.append(_firstUpper_32, "\t");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
            _builder.newLine();
            {
              boolean _isLazyArray = this._cppExtensions.isLazyArray(feature_4);
              if (_isLazyArray) {
                _builder.append("\t");
                _builder.append("// lazy Array of independent Data Objects: only keys are persisted");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("bool are");
                String _name_81 = this._cppExtensions.toName(feature_4);
                String _firstUpper_33 = StringExtensions.toFirstUpper(_name_81);
                _builder.append(_firstUpper_33, "\t");
                _builder.append("KeysResolved();");
                _builder.newLineIfNotEmpty();
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("QStringList ");
                String _name_82 = this._cppExtensions.toName(feature_4);
                String _firstLower_9 = StringExtensions.toFirstLower(_name_82);
                _builder.append(_firstLower_9, "\t");
                _builder.append("Keys();");
                _builder.newLineIfNotEmpty();
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void resolve");
                String _name_83 = this._cppExtensions.toName(feature_4);
                String _firstUpper_34 = StringExtensions.toFirstUpper(_name_83);
                _builder.append(_firstUpper_34, "\t");
                _builder.append("Keys(QList<");
                String _typeName_24 = this._cppExtensions.toTypeName(feature_4);
                _builder.append(_typeName_24, "\t");
                _builder.append("*> ");
                String _name_84 = this._cppExtensions.toName(feature_4);
                String _firstLower_10 = StringExtensions.toFirstLower(_name_84);
                _builder.append(_firstLower_10, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              }
            }
            {
              boolean _isTypeRootDataObject_1 = this._cppExtensions.isTypeRootDataObject(feature_4);
              boolean _not_5 = (!_isTypeRootDataObject_1);
              if (_not_5) {
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void addTo");
                String _name_85 = this._cppExtensions.toName(feature_4);
                String _firstUpper_35 = StringExtensions.toFirstUpper(_name_85);
                _builder.append(_firstUpper_35, "\t");
                _builder.append("FromMap(const QVariantMap& ");
                String _typeName_25 = this._cppExtensions.toTypeName(feature_4);
                String _firstLower_11 = StringExtensions.toFirstLower(_typeName_25);
                _builder.append(_firstLower_11, "\t");
                _builder.append("Map);");
                _builder.newLineIfNotEmpty();
              }
            }
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
                String _name_86 = this._cppExtensions.toName(feature_4);
                String _firstUpper_36 = StringExtensions.toFirstUpper(_name_86);
                _builder.append(_firstUpper_36, "\t");
                _builder.append("ByUuid(const QString& uuid);");
                _builder.newLineIfNotEmpty();
              }
            }
            {
              boolean _and_3 = false;
              boolean _referenceHasDomainKey = this._cppExtensions.referenceHasDomainKey(feature_4);
              if (!_referenceHasDomainKey) {
                _and_3 = false;
              } else {
                String _referenceDomainKey_2 = this._cppExtensions.referenceDomainKey(feature_4);
                boolean _notEquals_1 = (!Objects.equal(_referenceDomainKey_2, "uuid"));
                _and_3 = _notEquals_1;
              }
              if (_and_3) {
                _builder.append("\t");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("bool removeFrom");
                String _name_87 = this._cppExtensions.toName(feature_4);
                String _firstUpper_37 = StringExtensions.toFirstUpper(_name_87);
                _builder.append(_firstUpper_37, "\t");
                _builder.append("By");
                String _referenceDomainKey_3 = this._cppExtensions.referenceDomainKey(feature_4);
                String _firstUpper_38 = StringExtensions.toFirstUpper(_referenceDomainKey_3);
                _builder.append(_firstUpper_38, "\t");
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
              String _typeName_26 = this._cppExtensions.toTypeName(feature_4);
              boolean _equals_4 = Objects.equal(_typeName_26, "QString");
              if (_equals_4) {
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void addTo");
                String _name_88 = this._cppExtensions.toName(feature_4);
                String _firstUpper_39 = StringExtensions.toFirstUpper(_name_88);
                _builder.append(_firstUpper_39, "\t");
                _builder.append("StringList(const ");
                String _typeName_27 = this._cppExtensions.toTypeName(feature_4);
                _builder.append(_typeName_27, "\t");
                _builder.append("& stringValue);");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("bool removeFrom");
                String _name_89 = this._cppExtensions.toName(feature_4);
                String _firstUpper_40 = StringExtensions.toFirstUpper(_name_89);
                _builder.append(_firstUpper_40, "\t");
                _builder.append("StringList(const ");
                String _typeName_28 = this._cppExtensions.toTypeName(feature_4);
                _builder.append(_typeName_28, "\t");
                _builder.append("& stringValue);");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void addTo");
                String _name_90 = this._cppExtensions.toName(feature_4);
                String _firstUpper_41 = StringExtensions.toFirstUpper(_name_90);
                _builder.append(_firstUpper_41, "\t");
                _builder.append("List(const ");
                String _typeName_29 = this._cppExtensions.toTypeName(feature_4);
                _builder.append(_typeName_29, "\t");
                _builder.append("& ");
                String _typeName_30 = this._cppExtensions.toTypeName(feature_4);
                String _firstLower_12 = StringExtensions.toFirstLower(_typeName_30);
                _builder.append(_firstLower_12, "\t");
                _builder.append("Value);");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("bool removeFrom");
                String _name_91 = this._cppExtensions.toName(feature_4);
                String _firstUpper_42 = StringExtensions.toFirstUpper(_name_91);
                _builder.append(_firstUpper_42, "\t");
                _builder.append("List(const ");
                String _typeName_31 = this._cppExtensions.toTypeName(feature_4);
                _builder.append(_typeName_31, "\t");
                _builder.append("& ");
                String _typeName_32 = this._cppExtensions.toTypeName(feature_4);
                String _firstLower_13 = StringExtensions.toFirstLower(_typeName_32);
                _builder.append(_firstLower_13, "\t");
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
        String _name_92 = this._cppExtensions.toName(feature_4);
        String _firstLower_14 = StringExtensions.toFirstLower(_name_92);
        _builder.append(_firstLower_14, "\t");
        _builder.append("Count();");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.newLine();
        {
          boolean _isArrayList_1 = this._cppExtensions.isArrayList(feature_4);
          boolean _not_6 = (!_isArrayList_1);
          if (_not_6) {
            _builder.append("\t");
            _builder.append(" ");
            _builder.append("// access from C++ to ");
            String _name_93 = this._cppExtensions.toName(feature_4);
            _builder.append(_name_93, "\t ");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("QList<");
            String _typeName_33 = this._cppExtensions.toTypeName(feature_4);
            _builder.append(_typeName_33, "\t");
            _builder.append("*> ");
            String _name_94 = this._cppExtensions.toName(feature_4);
            _builder.append(_name_94, "\t");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("void set");
            String _name_95 = this._cppExtensions.toName(feature_4);
            String _firstUpper_43 = StringExtensions.toFirstUpper(_name_95);
            _builder.append(_firstUpper_43, "\t");
            _builder.append("(QList<");
            String _typeName_34 = this._cppExtensions.toTypeName(feature_4);
            _builder.append(_typeName_34, "\t");
            _builder.append("*> ");
            String _name_96 = this._cppExtensions.toName(feature_4);
            _builder.append(_name_96, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("// access from QML to ");
            String _name_97 = this._cppExtensions.toName(feature_4);
            _builder.append(_name_97, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("QDeclarativeListProperty<");
            String _typeName_35 = this._cppExtensions.toTypeName(feature_4);
            _builder.append(_typeName_35, "\t");
            _builder.append("> ");
            String _name_98 = this._cppExtensions.toName(feature_4);
            _builder.append(_name_98, "\t");
            _builder.append("PropertyList();");
            _builder.newLineIfNotEmpty();
          } else {
            {
              String _typeName_36 = this._cppExtensions.toTypeName(feature_4);
              boolean _equals_5 = Objects.equal(_typeName_36, "QString");
              if (_equals_5) {
                _builder.append("\t");
                _builder.append("QStringList ");
                String _name_99 = this._cppExtensions.toName(feature_4);
                _builder.append(_name_99, "\t");
                _builder.append("StringList();");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("void set");
                String _name_100 = this._cppExtensions.toName(feature_4);
                String _firstUpper_44 = StringExtensions.toFirstUpper(_name_100);
                _builder.append(_firstUpper_44, "\t");
                _builder.append("StringList(const QStringList& ");
                String _name_101 = this._cppExtensions.toName(feature_4);
                _builder.append(_name_101, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("// access from C++ to ");
                String _name_102 = this._cppExtensions.toName(feature_4);
                _builder.append(_name_102, "\t");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("QList<");
                String _typeName_37 = this._cppExtensions.toTypeName(feature_4);
                _builder.append(_typeName_37, "\t");
                _builder.append("> ");
                String _name_103 = this._cppExtensions.toName(feature_4);
                _builder.append(_name_103, "\t");
                _builder.append("();");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("void set");
                String _name_104 = this._cppExtensions.toName(feature_4);
                String _firstUpper_45 = StringExtensions.toFirstUpper(_name_104);
                _builder.append(_firstUpper_45, "\t");
                _builder.append("(QList<");
                String _typeName_38 = this._cppExtensions.toTypeName(feature_4);
                _builder.append(_typeName_38, "\t");
                _builder.append("> ");
                String _name_105 = this._cppExtensions.toName(feature_4);
                _builder.append(_name_105, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("// access from QML to ");
                String _name_106 = this._cppExtensions.toName(feature_4);
                _builder.append(_name_106, "\t");
                _builder.append(" (array of ");
                String _typeName_39 = this._cppExtensions.toTypeName(feature_4);
                _builder.append(_typeName_39, "\t");
                _builder.append(")");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("QVariantList ");
                String _name_107 = this._cppExtensions.toName(feature_4);
                _builder.append(_name_107, "\t");
                _builder.append("List();");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("void set");
                String _name_108 = this._cppExtensions.toName(feature_4);
                String _firstUpper_46 = StringExtensions.toFirstUpper(_name_108);
                _builder.append(_firstUpper_46, "\t");
                _builder.append("List(const QVariantList& ");
                String _name_109 = this._cppExtensions.toName(feature_4);
                _builder.append(_name_109, "\t");
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
        String _name_110 = this._cppExtensions.toName(dto);
        String _firstUpper_47 = StringExtensions.toFirstUpper(_name_110);
        _builder.append(_firstUpper_47, "\t");
        _builder.append("Children();");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      boolean _hasSqlCachePropertyName_1 = this._cppExtensions.hasSqlCachePropertyName(dto);
      if (_hasSqlCachePropertyName_1) {
        _builder.append("\t");
        _builder.append("// SQL");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("static const QString createTableCommand();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("static const QString createParameterizedInsertNameBinding();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("static const QString createParameterizedInsertPosBinding();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("void toSqlCache(");
        {
          EList<LDtoFeature> _features = dto.getFeatures();
          boolean _hasElements = false;
          for(final LDtoFeature feature_5 : _features) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(", ", "\t");
            }
            _builder.append("QVariantList& ");
            String _name_111 = this._cppExtensions.toName(feature_5);
            _builder.append(_name_111, "\t");
            _builder.append("List");
          }
        }
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("void fillFromSqlQuery(const QSqlQuery& sqlQuery);");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("static void fillSqlQueryPos(const QSqlRecord& record);");
        _builder.newLine();
        {
          boolean _is2PhaseInit = this._cppExtensions.is2PhaseInit(dto);
          if (_is2PhaseInit) {
            _builder.append("\t");
            _builder.append("static bool isPreloaded(const QSqlQuery& sqlQuery, const QVariantMap& preloadMap);");
            _builder.newLine();
          }
        }
      }
    }
    _builder.newLine();
    _builder.append("\t");
    _builder.append("virtual ~");
    String _name_112 = this._cppExtensions.toName(dto);
    String _firstUpper_48 = StringExtensions.toFirstUpper(_name_112);
    _builder.append(_firstUpper_48, "\t");
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
      for(final LFeature feature_6 : _filter_12) {
        {
          boolean _isLazy_2 = this._cppExtensions.isLazy(feature_6);
          if (_isLazy_2) {
            _builder.append("\t");
            _builder.append("// ");
            String _name_113 = this._cppExtensions.toName(feature_6);
            _builder.append(_name_113, "\t");
            _builder.append(" lazy pointing to ");
            String _typeOrQObject_8 = this._cppExtensions.toTypeOrQObject(feature_6);
            _builder.append(_typeOrQObject_8, "\t");
            _builder.append(" (domainKey: ");
            String _referenceDomainKey_5 = this._cppExtensions.referenceDomainKey(feature_6);
            _builder.append(_referenceDomainKey_5, "\t");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("void ");
            String _name_114 = this._cppExtensions.toName(feature_6);
            _builder.append(_name_114, "\t");
            _builder.append("Changed(");
            String _referenceDomainKeyType_4 = this._cppExtensions.referenceDomainKeyType(feature_6);
            _builder.append(_referenceDomainKeyType_4, "\t");
            _builder.append(" ");
            String _name_115 = this._cppExtensions.toName(feature_6);
            _builder.append(_name_115, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("void ");
            String _name_116 = this._cppExtensions.toName(feature_6);
            _builder.append(_name_116, "\t");
            _builder.append("AsDataObjectChanged(");
            String _typeName_40 = this._cppExtensions.toTypeName(feature_6);
            _builder.append(_typeName_40, "\t");
            _builder.append("* ");
            String _typeName_41 = this._cppExtensions.toTypeName(feature_6);
            String _firstLower_15 = StringExtensions.toFirstLower(_typeName_41);
            _builder.append(_firstLower_15, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
          } else {
            boolean _isEnum_2 = this._cppExtensions.isEnum(feature_6);
            if (_isEnum_2) {
              _builder.append("\t");
              _builder.append("void ");
              String _name_117 = this._cppExtensions.toName(feature_6);
              _builder.append(_name_117, "\t");
              _builder.append("Changed(int ");
              String _name_118 = this._cppExtensions.toName(feature_6);
              _builder.append(_name_118, "\t");
              _builder.append(");");
              _builder.newLineIfNotEmpty();
            } else {
              {
                boolean _and_4 = false;
                boolean _isTypeOfDataObject_3 = this._cppExtensions.isTypeOfDataObject(feature_6);
                if (!_isTypeOfDataObject_3) {
                  _and_4 = false;
                } else {
                  boolean _isContained_3 = this._cppExtensions.isContained(feature_6);
                  _and_4 = _isContained_3;
                }
                if (_and_4) {
                  _builder.append("\t");
                  _builder.append("// no SIGNAL ");
                  String _name_119 = this._cppExtensions.toName(feature_6);
                  _builder.append(_name_119, "\t");
                  _builder.append(" is only convenience way to get the parent");
                  _builder.newLineIfNotEmpty();
                } else {
                  _builder.append("\t");
                  _builder.append("void ");
                  String _name_120 = this._cppExtensions.toName(feature_6);
                  _builder.append(_name_120, "\t");
                  _builder.append("Changed(");
                  String _typeOrQObject_9 = this._cppExtensions.toTypeOrQObject(feature_6);
                  _builder.append(_typeOrQObject_9, "\t");
                  _builder.append(" ");
                  String _name_121 = this._cppExtensions.toName(feature_6);
                  _builder.append(_name_121, "\t");
                  _builder.append(");");
                  _builder.newLineIfNotEmpty();
                  {
                    boolean _isTypeOfDataObject_4 = this._cppExtensions.isTypeOfDataObject(feature_6);
                    if (_isTypeOfDataObject_4) {
                      _builder.append("\t");
                      _builder.append("void ");
                      String _name_122 = this._cppExtensions.toName(feature_6);
                      String _firstLower_16 = StringExtensions.toFirstLower(_name_122);
                      _builder.append(_firstLower_16, "\t");
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
      for(final LFeature feature_7 : _filter_13) {
        {
          boolean _isArrayList_2 = this._cppExtensions.isArrayList(feature_7);
          boolean _not_7 = (!_isArrayList_2);
          if (_not_7) {
            _builder.append("\t");
            _builder.append("void ");
            String _name_123 = this._cppExtensions.toName(feature_7);
            _builder.append(_name_123, "\t");
            _builder.append("Changed(QList<");
            String _typeName_42 = this._cppExtensions.toTypeName(feature_7);
            _builder.append(_typeName_42, "\t");
            _builder.append("*> ");
            String _name_124 = this._cppExtensions.toName(feature_7);
            _builder.append(_name_124, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("void addedTo");
            String _name_125 = this._cppExtensions.toName(feature_7);
            String _firstUpper_49 = StringExtensions.toFirstUpper(_name_125);
            _builder.append(_firstUpper_49, "\t");
            _builder.append("(");
            String _typeName_43 = this._cppExtensions.toTypeName(feature_7);
            _builder.append(_typeName_43, "\t");
            _builder.append("* ");
            String _typeName_44 = this._cppExtensions.toTypeName(feature_7);
            String _firstLower_17 = StringExtensions.toFirstLower(_typeName_44);
            _builder.append(_firstLower_17, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            {
              boolean _referenceHasUuid_1 = this._cppExtensions.referenceHasUuid(feature_7);
              if (_referenceHasUuid_1) {
                _builder.append("\t");
                _builder.append("void removedFrom");
                String _name_126 = this._cppExtensions.toName(feature_7);
                String _firstUpper_50 = StringExtensions.toFirstUpper(_name_126);
                _builder.append(_firstUpper_50, "\t");
                _builder.append("ByUuid(QString uuid);");
                _builder.newLineIfNotEmpty();
              }
            }
            {
              boolean _and_5 = false;
              boolean _referenceHasDomainKey_1 = this._cppExtensions.referenceHasDomainKey(feature_7);
              if (!_referenceHasDomainKey_1) {
                _and_5 = false;
              } else {
                String _referenceDomainKey_6 = this._cppExtensions.referenceDomainKey(feature_7);
                boolean _notEquals_2 = (!Objects.equal(_referenceDomainKey_6, "uuid"));
                _and_5 = _notEquals_2;
              }
              if (_and_5) {
                _builder.append("\t");
                _builder.append("void removedFrom");
                String _name_127 = this._cppExtensions.toName(feature_7);
                String _firstUpper_51 = StringExtensions.toFirstUpper(_name_127);
                _builder.append(_firstUpper_51, "\t");
                _builder.append("By");
                String _referenceDomainKey_7 = this._cppExtensions.referenceDomainKey(feature_7);
                String _firstUpper_52 = StringExtensions.toFirstUpper(_referenceDomainKey_7);
                _builder.append(_firstUpper_52, "\t");
                _builder.append("(");
                String _referenceDomainKeyType_5 = this._cppExtensions.referenceDomainKeyType(feature_7);
                _builder.append(_referenceDomainKeyType_5, "\t");
                _builder.append(" ");
                String _referenceDomainKey_8 = this._cppExtensions.referenceDomainKey(feature_7);
                _builder.append(_referenceDomainKey_8, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.newLine();
          } else {
            {
              String _typeName_45 = this._cppExtensions.toTypeName(feature_7);
              boolean _equals_6 = Objects.equal(_typeName_45, "QString");
              if (_equals_6) {
                _builder.append("\t");
                _builder.append("void ");
                String _name_128 = this._cppExtensions.toName(feature_7);
                _builder.append(_name_128, "\t");
                _builder.append("StringListChanged(QStringList ");
                String _name_129 = this._cppExtensions.toName(feature_7);
                _builder.append(_name_129, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("void addedTo");
                String _name_130 = this._cppExtensions.toName(feature_7);
                String _firstUpper_53 = StringExtensions.toFirstUpper(_name_130);
                _builder.append(_firstUpper_53, "\t");
                _builder.append("StringList(");
                String _typeName_46 = this._cppExtensions.toTypeName(feature_7);
                _builder.append(_typeName_46, "\t");
                _builder.append(" stringValue);");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("void removedFrom");
                String _name_131 = this._cppExtensions.toName(feature_7);
                String _firstUpper_54 = StringExtensions.toFirstUpper(_name_131);
                _builder.append(_firstUpper_54, "\t");
                _builder.append("StringList(");
                String _typeName_47 = this._cppExtensions.toTypeName(feature_7);
                _builder.append(_typeName_47, "\t");
                _builder.append(" stringValue);");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("void ");
                String _name_132 = this._cppExtensions.toName(feature_7);
                _builder.append(_name_132, "\t");
                _builder.append("ListChanged(QVariantList ");
                String _name_133 = this._cppExtensions.toName(feature_7);
                _builder.append(_name_133, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("void addedTo");
                String _name_134 = this._cppExtensions.toName(feature_7);
                String _firstUpper_55 = StringExtensions.toFirstUpper(_name_134);
                _builder.append(_firstUpper_55, "\t");
                _builder.append("List(");
                String _typeName_48 = this._cppExtensions.toTypeName(feature_7);
                _builder.append(_typeName_48, "\t");
                _builder.append(" ");
                String _typeName_49 = this._cppExtensions.toTypeName(feature_7);
                String _firstLower_18 = StringExtensions.toFirstLower(_typeName_49);
                _builder.append(_firstLower_18, "\t");
                _builder.append("Value);");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("void removedFrom");
                String _name_135 = this._cppExtensions.toName(feature_7);
                String _firstUpper_56 = StringExtensions.toFirstUpper(_name_135);
                _builder.append(_firstUpper_56, "\t");
                _builder.append("List(");
                String _typeName_50 = this._cppExtensions.toTypeName(feature_7);
                _builder.append(_typeName_50, "\t");
                _builder.append(" ");
                String _typeName_51 = this._cppExtensions.toTypeName(feature_7);
                String _firstLower_19 = StringExtensions.toFirstLower(_typeName_51);
                _builder.append(_firstLower_19, "\t");
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
      for(final LFeature feature_8 : _filter_14) {
        {
          boolean _and_6 = false;
          boolean _isTypeOfDataObject_5 = this._cppExtensions.isTypeOfDataObject(feature_8);
          if (!_isTypeOfDataObject_5) {
            _and_6 = false;
          } else {
            boolean _isContained_4 = this._cppExtensions.isContained(feature_8);
            _and_6 = _isContained_4;
          }
          if (_and_6) {
            _builder.append("\t");
            _builder.append("// no MEMBER m");
            String _name_136 = this._cppExtensions.toName(feature_8);
            String _firstUpper_57 = StringExtensions.toFirstUpper(_name_136);
            _builder.append(_firstUpper_57, "\t");
            _builder.append(" it\'s the parent");
            _builder.newLineIfNotEmpty();
          } else {
            boolean _isLazy_3 = this._cppExtensions.isLazy(feature_8);
            if (_isLazy_3) {
              _builder.append("\t");
              String _referenceDomainKeyType_6 = this._cppExtensions.referenceDomainKeyType(feature_8);
              _builder.append(_referenceDomainKeyType_6, "\t");
              _builder.append(" m");
              String _name_137 = this._cppExtensions.toName(feature_8);
              String _firstUpper_58 = StringExtensions.toFirstUpper(_name_137);
              _builder.append(_firstUpper_58, "\t");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("bool m");
              String _name_138 = this._cppExtensions.toName(feature_8);
              String _firstUpper_59 = StringExtensions.toFirstUpper(_name_138);
              _builder.append(_firstUpper_59, "\t");
              _builder.append("Invalid;");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              String _typeOrQObject_10 = this._cppExtensions.toTypeOrQObject(feature_8);
              _builder.append(_typeOrQObject_10, "\t");
              _builder.append(" m");
              String _name_139 = this._cppExtensions.toName(feature_8);
              String _firstUpper_60 = StringExtensions.toFirstUpper(_name_139);
              _builder.append(_firstUpper_60, "\t");
              _builder.append("AsDataObject;");
              _builder.newLineIfNotEmpty();
              {
                boolean _isHierarchy_2 = this._cppExtensions.isHierarchy(dto, feature_8);
                if (_isHierarchy_2) {
                  _builder.append("\t");
                  _builder.append("// hierarchy of ");
                  String _name_140 = this._cppExtensions.toName(dto);
                  _builder.append(_name_140, "\t");
                  _builder.append("*");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("bool mIs");
                  String _name_141 = this._cppExtensions.toName(feature_8);
                  String _firstUpper_61 = StringExtensions.toFirstUpper(_name_141);
                  _builder.append(_firstUpper_61, "\t");
                  _builder.append("AsPropertyListInitialized;");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("QList<");
                  String _name_142 = this._cppExtensions.toName(dto);
                  _builder.append(_name_142, "\t");
                  _builder.append("*> m");
                  String _name_143 = this._cppExtensions.toName(feature_8);
                  String _firstUpper_62 = StringExtensions.toFirstUpper(_name_143);
                  _builder.append(_firstUpper_62, "\t");
                  _builder.append("AsPropertyList;");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("// implementation for QDeclarativeListProperty to use");
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("// QML functions for hierarchy of ");
                  String _name_144 = this._cppExtensions.toName(dto);
                  _builder.append(_name_144, "\t");
                  _builder.append("*");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("static void appendTo");
                  String _name_145 = this._cppExtensions.toName(feature_8);
                  String _firstUpper_63 = StringExtensions.toFirstUpper(_name_145);
                  _builder.append(_firstUpper_63, "\t");
                  _builder.append("Property(QDeclarativeListProperty<");
                  String _name_146 = this._cppExtensions.toName(dto);
                  _builder.append(_name_146, "\t");
                  _builder.append("> *");
                  String _name_147 = this._cppExtensions.toName(feature_8);
                  _builder.append(_name_147, "\t");
                  _builder.append("List,");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("\t");
                  String _name_148 = this._cppExtensions.toName(dto);
                  _builder.append(_name_148, "\t\t");
                  _builder.append("* ");
                  String _name_149 = this._cppExtensions.toName(dto);
                  String _firstLower_20 = StringExtensions.toFirstLower(_name_149);
                  _builder.append(_firstLower_20, "\t\t");
                  _builder.append(");");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("static int ");
                  String _name_150 = this._cppExtensions.toName(feature_8);
                  _builder.append(_name_150, "\t");
                  _builder.append("PropertyCount(QDeclarativeListProperty<");
                  String _name_151 = this._cppExtensions.toName(dto);
                  _builder.append(_name_151, "\t");
                  _builder.append("> *");
                  String _name_152 = this._cppExtensions.toName(feature_8);
                  _builder.append(_name_152, "\t");
                  _builder.append("List);");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("static ");
                  String _name_153 = this._cppExtensions.toName(dto);
                  _builder.append(_name_153, "\t");
                  _builder.append("* at");
                  String _name_154 = this._cppExtensions.toName(feature_8);
                  String _firstUpper_64 = StringExtensions.toFirstUpper(_name_154);
                  _builder.append(_firstUpper_64, "\t");
                  _builder.append("Property(QDeclarativeListProperty<");
                  String _name_155 = this._cppExtensions.toName(dto);
                  _builder.append(_name_155, "\t");
                  _builder.append("> *");
                  String _name_156 = this._cppExtensions.toName(feature_8);
                  _builder.append(_name_156, "\t");
                  _builder.append("List,");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("int pos);");
                  _builder.newLine();
                  _builder.append("\t");
                  _builder.append("static void clear");
                  String _name_157 = this._cppExtensions.toName(feature_8);
                  String _firstUpper_65 = StringExtensions.toFirstUpper(_name_157);
                  _builder.append(_firstUpper_65, "\t");
                  _builder.append("Property(QDeclarativeListProperty<");
                  String _name_158 = this._cppExtensions.toName(dto);
                  _builder.append(_name_158, "\t");
                  _builder.append("> *");
                  String _name_159 = this._cppExtensions.toName(feature_8);
                  _builder.append(_name_159, "\t");
                  _builder.append("List);");
                  _builder.newLineIfNotEmpty();
                }
              }
            } else {
              boolean _isEnum_3 = this._cppExtensions.isEnum(feature_8);
              if (_isEnum_3) {
                _builder.append("\t");
                _builder.append("int m");
                String _name_160 = this._cppExtensions.toName(feature_8);
                String _firstUpper_66 = StringExtensions.toFirstUpper(_name_160);
                _builder.append(_firstUpper_66, "\t");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("int ");
                String _name_161 = this._cppExtensions.toName(feature_8);
                String _firstLower_21 = StringExtensions.toFirstLower(_name_161);
                _builder.append(_firstLower_21, "\t");
                _builder.append("StringToInt(QString ");
                String _name_162 = this._cppExtensions.toName(feature_8);
                String _firstLower_22 = StringExtensions.toFirstLower(_name_162);
                _builder.append(_firstLower_22, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                String _typeOrQObject_11 = this._cppExtensions.toTypeOrQObject(feature_8);
                _builder.append(_typeOrQObject_11, "\t");
                _builder.append(" m");
                String _name_163 = this._cppExtensions.toName(feature_8);
                String _firstUpper_67 = StringExtensions.toFirstUpper(_name_163);
                _builder.append(_firstUpper_67, "\t");
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
      for(final LFeature feature_9 : _filter_15) {
        {
          boolean _isLazyArray_1 = this._cppExtensions.isLazyArray(feature_9);
          if (_isLazyArray_1) {
            _builder.append("\t");
            _builder.append("// lazy Array of independent Data Objects: only keys are persisted");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("QStringList m");
            String _name_164 = this._cppExtensions.toName(feature_9);
            String _firstUpper_68 = StringExtensions.toFirstUpper(_name_164);
            _builder.append(_firstUpper_68, "\t");
            _builder.append("Keys;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("bool m");
            String _name_165 = this._cppExtensions.toName(feature_9);
            String _firstUpper_69 = StringExtensions.toFirstUpper(_name_165);
            _builder.append(_firstUpper_69, "\t");
            _builder.append("KeysResolved;");
            _builder.newLineIfNotEmpty();
          }
        }
        {
          boolean _isArrayList_3 = this._cppExtensions.isArrayList(feature_9);
          boolean _not_8 = (!_isArrayList_3);
          if (_not_8) {
            _builder.append("\t");
            _builder.append("QList<");
            String _typeName_52 = this._cppExtensions.toTypeName(feature_9);
            _builder.append(_typeName_52, "\t");
            _builder.append("*> m");
            String _name_166 = this._cppExtensions.toName(feature_9);
            String _firstUpper_70 = StringExtensions.toFirstUpper(_name_166);
            _builder.append(_firstUpper_70, "\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("// implementation for QDeclarativeListProperty to use");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("// QML functions for List of ");
            String _typeName_53 = this._cppExtensions.toTypeName(feature_9);
            _builder.append(_typeName_53, "\t");
            _builder.append("*");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("static void appendTo");
            String _name_167 = this._cppExtensions.toName(feature_9);
            String _firstUpper_71 = StringExtensions.toFirstUpper(_name_167);
            _builder.append(_firstUpper_71, "\t");
            _builder.append("Property(QDeclarativeListProperty<");
            String _typeName_54 = this._cppExtensions.toTypeName(feature_9);
            _builder.append(_typeName_54, "\t");
            _builder.append("> *");
            String _name_168 = this._cppExtensions.toName(feature_9);
            _builder.append(_name_168, "\t");
            _builder.append("List,");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            String _typeName_55 = this._cppExtensions.toTypeName(feature_9);
            _builder.append(_typeName_55, "\t\t");
            _builder.append("* ");
            String _typeName_56 = this._cppExtensions.toTypeName(feature_9);
            String _firstLower_23 = StringExtensions.toFirstLower(_typeName_56);
            _builder.append(_firstLower_23, "\t\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("static int ");
            String _name_169 = this._cppExtensions.toName(feature_9);
            _builder.append(_name_169, "\t");
            _builder.append("PropertyCount(QDeclarativeListProperty<");
            String _typeName_57 = this._cppExtensions.toTypeName(feature_9);
            _builder.append(_typeName_57, "\t");
            _builder.append("> *");
            String _name_170 = this._cppExtensions.toName(feature_9);
            _builder.append(_name_170, "\t");
            _builder.append("List);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("static ");
            String _typeName_58 = this._cppExtensions.toTypeName(feature_9);
            _builder.append(_typeName_58, "\t");
            _builder.append("* at");
            String _name_171 = this._cppExtensions.toName(feature_9);
            String _firstUpper_72 = StringExtensions.toFirstUpper(_name_171);
            _builder.append(_firstUpper_72, "\t");
            _builder.append("Property(QDeclarativeListProperty<");
            String _typeName_59 = this._cppExtensions.toTypeName(feature_9);
            _builder.append(_typeName_59, "\t");
            _builder.append("> *");
            String _name_172 = this._cppExtensions.toName(feature_9);
            _builder.append(_name_172, "\t");
            _builder.append("List, int pos);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("static void clear");
            String _name_173 = this._cppExtensions.toName(feature_9);
            String _firstUpper_73 = StringExtensions.toFirstUpper(_name_173);
            _builder.append(_firstUpper_73, "\t");
            _builder.append("Property(QDeclarativeListProperty<");
            String _typeName_60 = this._cppExtensions.toTypeName(feature_9);
            _builder.append(_typeName_60, "\t");
            _builder.append("> *");
            String _name_174 = this._cppExtensions.toName(feature_9);
            _builder.append(_name_174, "\t");
            _builder.append("List);");
            _builder.newLineIfNotEmpty();
          } else {
            {
              String _typeName_61 = this._cppExtensions.toTypeName(feature_9);
              boolean _equals_7 = Objects.equal(_typeName_61, "QString");
              if (_equals_7) {
                _builder.append("\t");
                _builder.append("QStringList m");
                String _name_175 = this._cppExtensions.toName(feature_9);
                String _firstUpper_74 = StringExtensions.toFirstUpper(_name_175);
                _builder.append(_firstUpper_74, "\t");
                _builder.append("StringList;");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("QList<");
                String _typeName_62 = this._cppExtensions.toTypeName(feature_9);
                _builder.append(_typeName_62, "\t");
                _builder.append("> m");
                String _name_176 = this._cppExtensions.toName(feature_9);
                String _firstUpper_75 = StringExtensions.toFirstUpper(_name_176);
                _builder.append(_firstUpper_75, "\t");
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
    String _name_177 = this._cppExtensions.toName(dto);
    String _firstUpper_76 = StringExtensions.toFirstUpper(_name_177);
    _builder.append(_firstUpper_76, "\t");
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    _builder.append("};");
    _builder.newLine();
    _builder.append("Q_DECLARE_METATYPE(");
    String _name_178 = this._cppExtensions.toName(dto);
    String _firstUpper_77 = StringExtensions.toFirstUpper(_name_178);
    _builder.append(_firstUpper_77, "");
    _builder.append("*)");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("#endif /* ");
    String _name_179 = this._cppExtensions.toName(dto);
    String _upperCase_2 = _name_179.toUpperCase();
    _builder.append(_upperCase_2, "");
    _builder.append("_HPP_ */");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    return _builder;
  }
}
