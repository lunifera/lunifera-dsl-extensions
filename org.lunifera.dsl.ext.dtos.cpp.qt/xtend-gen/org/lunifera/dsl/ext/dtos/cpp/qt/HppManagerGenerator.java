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
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.lunifera.dsl.ext.dtos.cpp.qt.CppExtensions;
import org.lunifera.dsl.ext.dtos.cpp.qt.ManagerExtensions;
import org.lunifera.dsl.semantic.common.types.LFeature;
import org.lunifera.dsl.semantic.common.types.LType;
import org.lunifera.dsl.semantic.common.types.LTypedPackage;
import org.lunifera.dsl.semantic.dto.LDto;

@SuppressWarnings("all")
public class HppManagerGenerator {
  @Inject
  @Extension
  private CppExtensions _cppExtensions;
  
  @Inject
  @Extension
  private ManagerExtensions _managerExtensions;
  
  public String toFileName(final LTypedPackage pkg) {
    return "DataManager.hpp";
  }
  
  public CharSequence toContent(final LTypedPackage pkg) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("#ifndef DATAMANAGER_HPP_");
    _builder.newLine();
    _builder.append("#define DATAMANAGER_HPP_");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include <qobject.h>");
    _builder.newLine();
    {
      boolean _hasTargetOS = this._managerExtensions.hasTargetOS(pkg);
      if (_hasTargetOS) {
        _builder.append("#include <QQmlListProperty>");
        _builder.newLine();
      } else {
        _builder.append("#include <QtDeclarative>");
        _builder.newLine();
      }
    }
    _builder.append("#include <QStringList>");
    _builder.newLine();
    {
      boolean _hasSqlCache = this._managerExtensions.hasSqlCache(pkg);
      if (_hasSqlCache) {
        _builder.append("#include <QtSql/QtSql>");
        _builder.newLine();
        {
          boolean _has2PhaseInit = this._managerExtensions.has2PhaseInit(pkg);
          if (_has2PhaseInit) {
            _builder.append("#include <QtSql/QSqlQuery>");
            _builder.newLine();
            _builder.append("#include <QTimer>");
            _builder.newLine();
          }
        }
      }
    }
    _builder.newLine();
    {
      EList<LType> _types = pkg.getTypes();
      final Function1<LType, Boolean> _function = new Function1<LType, Boolean>() {
        public Boolean apply(final LType it) {
          return Boolean.valueOf((it instanceof LDto));
        }
      };
      Iterable<LType> _filter = IterableExtensions.<LType>filter(_types, _function);
      final Function1<LType, LDto> _function_1 = new Function1<LType, LDto>() {
        public LDto apply(final LType it) {
          return ((LDto) it);
        }
      };
      Iterable<LDto> _map = IterableExtensions.<LType, LDto>map(_filter, _function_1);
      for(final LDto dto : _map) {
        _builder.append("#include \"");
        String _name = this._cppExtensions.toName(dto);
        _builder.append(_name, "");
        _builder.append(".hpp\"");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      boolean _hasGeoCoordinate = this._managerExtensions.hasGeoCoordinate(pkg);
      if (_hasGeoCoordinate) {
        _builder.append("#include \"../GeoCoordinate.hpp\"");
        _builder.newLine();
      }
    }
    {
      boolean _hasGeoAddress = this._managerExtensions.hasGeoAddress(pkg);
      if (_hasGeoAddress) {
        _builder.append("#include  \"../GeoAddress.hpp\"");
        _builder.newLine();
      }
    }
    _builder.newLine();
    _builder.append("class DataManager: public QObject");
    _builder.newLine();
    _builder.append("{");
    _builder.newLine();
    _builder.append("Q_OBJECT");
    _builder.newLine();
    _builder.newLine();
    {
      boolean _hasTargetOS_1 = this._managerExtensions.hasTargetOS(pkg);
      if (_hasTargetOS_1) {
        _builder.append("// QQmlListProperty to get easy access from QML");
        _builder.newLine();
      } else {
        _builder.append("// QDeclarativeListProperty to get easy access from QML");
        _builder.newLine();
      }
    }
    {
      EList<LType> _types_1 = pkg.getTypes();
      final Function1<LType, Boolean> _function_2 = new Function1<LType, Boolean>() {
        public Boolean apply(final LType it) {
          return Boolean.valueOf((it instanceof LDto));
        }
      };
      Iterable<LType> _filter_1 = IterableExtensions.<LType>filter(_types_1, _function_2);
      final Function1<LType, LDto> _function_3 = new Function1<LType, LDto>() {
        public LDto apply(final LType it) {
          return ((LDto) it);
        }
      };
      Iterable<LDto> _map_1 = IterableExtensions.<LType, LDto>map(_filter_1, _function_3);
      for(final LDto dto_1 : _map_1) {
        {
          boolean _isRootDataObject = this._cppExtensions.isRootDataObject(dto_1);
          if (_isRootDataObject) {
            {
              boolean _hasTargetOS_2 = this._managerExtensions.hasTargetOS(pkg);
              if (_hasTargetOS_2) {
                {
                  String _name_1 = dto_1.getName();
                  boolean _notEquals = (!Objects.equal(_name_1, "SettingsData"));
                  if (_notEquals) {
                    _builder.append("Q_PROPERTY(QQmlListProperty<");
                    String _name_2 = this._cppExtensions.toName(dto_1);
                    _builder.append(_name_2, "");
                    _builder.append("> ");
                    String _name_3 = this._cppExtensions.toName(dto_1);
                    String _firstLower = StringExtensions.toFirstLower(_name_3);
                    _builder.append(_firstLower, "");
                    _builder.append("PropertyList READ ");
                    String _name_4 = this._cppExtensions.toName(dto_1);
                    String _firstLower_1 = StringExtensions.toFirstLower(_name_4);
                    _builder.append(_firstLower_1, "");
                    _builder.append("PropertyList NOTIFY ");
                    String _name_5 = this._cppExtensions.toName(dto_1);
                    String _firstLower_2 = StringExtensions.toFirstLower(_name_5);
                    _builder.append(_firstLower_2, "");
                    _builder.append("PropertyListChanged)");
                    _builder.newLineIfNotEmpty();
                  }
                }
              } else {
                _builder.append("Q_PROPERTY(QDeclarativeListProperty<");
                String _name_6 = this._cppExtensions.toName(dto_1);
                _builder.append(_name_6, "");
                _builder.append("> ");
                String _name_7 = this._cppExtensions.toName(dto_1);
                String _firstLower_3 = StringExtensions.toFirstLower(_name_7);
                _builder.append(_firstLower_3, "");
                _builder.append("PropertyList READ ");
                String _name_8 = this._cppExtensions.toName(dto_1);
                String _firstLower_4 = StringExtensions.toFirstLower(_name_8);
                _builder.append(_firstLower_4, "");
                _builder.append("PropertyList NOTIFY ");
                String _name_9 = this._cppExtensions.toName(dto_1);
                String _firstLower_5 = StringExtensions.toFirstLower(_name_9);
                _builder.append(_firstLower_5, "");
                _builder.append("PropertyListChanged)");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    _builder.newLine();
    _builder.append("public:");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("DataManager(QObject *parent = 0);");
    _builder.newLine();
    {
      EList<LType> _types_2 = pkg.getTypes();
      final Function1<LType, Boolean> _function_4 = new Function1<LType, Boolean>() {
        public Boolean apply(final LType it) {
          boolean _and = false;
          if (!(it instanceof LDto)) {
            _and = false;
          } else {
            String _name = it.getName();
            boolean _equals = Objects.equal(_name, "SettingsData");
            _and = _equals;
          }
          return Boolean.valueOf(_and);
        }
      };
      Iterable<LType> _filter_2 = IterableExtensions.<LType>filter(_types_2, _function_4);
      final Function1<LType, LDto> _function_5 = new Function1<LType, LDto>() {
        public LDto apply(final LType it) {
          return ((LDto) it);
        }
      };
      Iterable<LDto> _map_2 = IterableExtensions.<LType, LDto>map(_filter_2, _function_5);
      for(final LDto dto_2 : _map_2) {
        {
          boolean _hasFriendsClassPropertyName = this._cppExtensions.hasFriendsClassPropertyName(dto_2);
          if (_hasFriendsClassPropertyName) {
            _builder.append("\t");
            _builder.newLine();
            {
              String _friendsClassValue = this._cppExtensions.getFriendsClassValue(dto_2);
              String[] _split = _friendsClassValue.split(",");
              for(final String friend : _split) {
                _builder.append("\t");
                _builder.append("friend class ");
                _builder.append(friend, "\t");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("    ");
    _builder.append("virtual ~DataManager();");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("Q_INVOKABLE");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("void init();");
    _builder.newLine();
    {
      boolean _and = false;
      boolean _has2PhaseInit_1 = this._managerExtensions.has2PhaseInit(pkg);
      if (!_has2PhaseInit_1) {
        _and = false;
      } else {
        boolean _hasSqlCache_1 = this._managerExtensions.hasSqlCache(pkg);
        _and = _hasSqlCache_1;
      }
      if (_and) {
        _builder.append("\t");
        _builder.append("Q_INVOKABLE");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("void init2();");
        _builder.newLine();
      }
    }
    {
      boolean _hasTargetOS_3 = this._managerExtensions.hasTargetOS(pkg);
      if (_hasTargetOS_3) {
        _builder.append("\t");
        _builder.append("bool checkDirs();");
        _builder.newLine();
      }
    }
    _builder.newLine();
    {
      EList<LType> _types_3 = pkg.getTypes();
      final Function1<LType, Boolean> _function_6 = new Function1<LType, Boolean>() {
        public Boolean apply(final LType it) {
          return Boolean.valueOf((it instanceof LDto));
        }
      };
      Iterable<LType> _filter_3 = IterableExtensions.<LType>filter(_types_3, _function_6);
      final Function1<LType, LDto> _function_7 = new Function1<LType, LDto>() {
        public LDto apply(final LType it) {
          return ((LDto) it);
        }
      };
      Iterable<LDto> _map_3 = IterableExtensions.<LType, LDto>map(_filter_3, _function_7);
      for(final LDto dto_3 : _map_3) {
        {
          boolean _isRootDataObject_1 = this._cppExtensions.isRootDataObject(dto_3);
          if (_isRootDataObject_1) {
            {
              boolean _hasTargetOS_4 = this._managerExtensions.hasTargetOS(pkg);
              boolean _not = (!_hasTargetOS_4);
              if (_not) {
                {
                  boolean _isTree = this._cppExtensions.isTree(dto_3);
                  if (_isTree) {
                    _builder.append("\t");
                    _builder.append("Q_INVOKABLE");
                    _builder.newLine();
                    _builder.append("\t");
                    _builder.append("void fill");
                    String _name_10 = this._cppExtensions.toName(dto_3);
                    _builder.append(_name_10, "\t");
                    _builder.append("TreeDataModel(QString objectName);");
                    _builder.newLineIfNotEmpty();
                    _builder.newLine();
                    _builder.append("\t");
                    _builder.append("Q_INVOKABLE");
                    _builder.newLine();
                    _builder.append("\t");
                    _builder.append("void fill");
                    String _name_11 = this._cppExtensions.toName(dto_3);
                    _builder.append(_name_11, "\t");
                    _builder.append("FlatDataModel(QString objectName);");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t");
                    _builder.append("Q_INVOKABLE");
                    _builder.newLine();
                    _builder.append("\t");
                    _builder.append("void fill");
                    String _name_12 = this._cppExtensions.toName(dto_3);
                    _builder.append(_name_12, "\t");
                    _builder.append("DataModel(QString objectName);");
                    _builder.newLineIfNotEmpty();
                  }
                }
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void replaceItemIn");
                String _name_13 = this._cppExtensions.toName(dto_3);
                _builder.append(_name_13, "\t");
                _builder.append("DataModel(QString objectName, ");
                String _name_14 = this._cppExtensions.toName(dto_3);
                _builder.append(_name_14, "\t");
                _builder.append("* listItem);");
                _builder.newLineIfNotEmpty();
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void removeItemFrom");
                String _name_15 = this._cppExtensions.toName(dto_3);
                _builder.append(_name_15, "\t");
                _builder.append("DataModel(QString objectName, ");
                String _name_16 = this._cppExtensions.toName(dto_3);
                _builder.append(_name_16, "\t");
                _builder.append("* listItem);");
                _builder.newLineIfNotEmpty();
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void insertItemInto");
                String _name_17 = this._cppExtensions.toName(dto_3);
                _builder.append(_name_17, "\t");
                _builder.append("DataModel(QString objectName, ");
                String _name_18 = this._cppExtensions.toName(dto_3);
                _builder.append(_name_18, "\t");
                _builder.append("* listItem);");
                _builder.newLineIfNotEmpty();
                {
                  List<? extends LFeature> _allFeatures = dto_3.getAllFeatures();
                  final Function1<LFeature, Boolean> _function_8 = new Function1<LFeature, Boolean>() {
                    public Boolean apply(final LFeature it) {
                      return Boolean.valueOf(HppManagerGenerator.this._cppExtensions.hasIndex(it));
                    }
                  };
                  Iterable<? extends LFeature> _filter_4 = IterableExtensions.filter(_allFeatures, _function_8);
                  for(final LFeature feature : _filter_4) {
                    _builder.newLine();
                    _builder.append("\t");
                    _builder.append("Q_INVOKABLE");
                    _builder.newLine();
                    {
                      boolean _isLazy = this._cppExtensions.isLazy(feature);
                      if (_isLazy) {
                        _builder.append("\t");
                        _builder.append("void fill");
                        String _name_19 = this._cppExtensions.toName(dto_3);
                        _builder.append(_name_19, "\t");
                        _builder.append("DataModelBy");
                        String _name_20 = this._cppExtensions.toName(feature);
                        String _firstUpper = StringExtensions.toFirstUpper(_name_20);
                        _builder.append(_firstUpper, "\t");
                        _builder.append("(QString objectName, const ");
                        String _referenceDomainKeyType = this._cppExtensions.referenceDomainKeyType(feature);
                        _builder.append(_referenceDomainKeyType, "\t");
                        _builder.append("& ");
                        String _name_21 = this._cppExtensions.toName(feature);
                        _builder.append(_name_21, "\t");
                        _builder.append(");");
                        _builder.newLineIfNotEmpty();
                      } else {
                        _builder.append("\t");
                        _builder.append("void fill");
                        String _name_22 = this._cppExtensions.toName(dto_3);
                        _builder.append(_name_22, "\t");
                        _builder.append("DataModelBy");
                        String _name_23 = this._cppExtensions.toName(feature);
                        String _firstUpper_1 = StringExtensions.toFirstUpper(_name_23);
                        _builder.append(_firstUpper_1, "\t");
                        _builder.append("(QString objectName, const ");
                        String _typeName = this._cppExtensions.toTypeName(feature);
                        _builder.append(_typeName, "\t");
                        _builder.append("& ");
                        String _name_24 = this._cppExtensions.toName(feature);
                        _builder.append(_name_24, "\t");
                        _builder.append(");");
                        _builder.newLineIfNotEmpty();
                      }
                    }
                  }
                }
              }
            }
            _builder.append("\t");
            _builder.newLine();
            {
              boolean _existsLazy = this._cppExtensions.existsLazy(dto_3);
              if (_existsLazy) {
                {
                  List<? extends LFeature> _allFeatures_1 = dto_3.getAllFeatures();
                  final Function1<LFeature, Boolean> _function_9 = new Function1<LFeature, Boolean>() {
                    public Boolean apply(final LFeature it) {
                      return Boolean.valueOf(HppManagerGenerator.this._cppExtensions.isLazy(it));
                    }
                  };
                  Iterable<? extends LFeature> _filter_5 = IterableExtensions.filter(_allFeatures_1, _function_9);
                  for(final LFeature feature_1 : _filter_5) {
                    {
                      boolean _isHierarchy = this._cppExtensions.isHierarchy(dto_3, feature_1);
                      if (_isHierarchy) {
                        _builder.newLine();
                        _builder.append("\t");
                        _builder.append("// isHierarchy of: ");
                        String _name_25 = this._cppExtensions.toName(dto_3);
                        _builder.append(_name_25, "\t");
                        _builder.append(" FEATURE: ");
                        String _name_26 = this._cppExtensions.toName(feature_1);
                        _builder.append(_name_26, "\t");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("Q_INVOKABLE");
                        _builder.newLine();
                        _builder.append("\t");
                        _builder.append("void init");
                        String _name_27 = this._cppExtensions.toName(feature_1);
                        String _firstUpper_2 = StringExtensions.toFirstUpper(_name_27);
                        _builder.append(_firstUpper_2, "\t");
                        _builder.append("HierarchyList(");
                        String _name_28 = this._cppExtensions.toName(dto_3);
                        _builder.append(_name_28, "\t");
                        _builder.append("* ");
                        String _name_29 = this._cppExtensions.toName(feature_1);
                        _builder.append(_name_29, "\t");
                        String _name_30 = this._cppExtensions.toName(dto_3);
                        _builder.append(_name_30, "\t");
                        _builder.append(");");
                        _builder.newLineIfNotEmpty();
                      }
                    }
                  }
                }
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void resolve");
                String _name_31 = this._cppExtensions.toName(dto_3);
                _builder.append(_name_31, "\t");
                _builder.append("References(");
                String _name_32 = this._cppExtensions.toName(dto_3);
                _builder.append(_name_32, "\t");
                _builder.append("* ");
                String _name_33 = this._cppExtensions.toName(dto_3);
                String _firstLower_6 = StringExtensions.toFirstLower(_name_33);
                _builder.append(_firstLower_6, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void resolveReferencesForAll");
                String _name_34 = this._cppExtensions.toName(dto_3);
                _builder.append(_name_34, "\t");
                _builder.append("();");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.newLine();
            {
              boolean _or = false;
              boolean _hasTargetOS_5 = this._managerExtensions.hasTargetOS(pkg);
              boolean _not_1 = (!_hasTargetOS_5);
              if (_not_1) {
                _or = true;
              } else {
                String _name_35 = dto_3.getName();
                boolean _notEquals_1 = (!Objects.equal(_name_35, "SettingsData"));
                _or = _notEquals_1;
              }
              if (_or) {
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("QList<");
                String _name_36 = this._cppExtensions.toName(dto_3);
                _builder.append(_name_36, "\t");
                _builder.append("*> listOf");
                String _name_37 = this._cppExtensions.toName(dto_3);
                _builder.append(_name_37, "\t");
                _builder.append("ForKeys(QStringList keyList);");
                _builder.newLineIfNotEmpty();
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("QVariantList ");
                String _name_38 = this._cppExtensions.toName(dto_3);
                String _firstLower_7 = StringExtensions.toFirstLower(_name_38);
                _builder.append(_firstLower_7, "\t");
                _builder.append("AsQVariantList();");
                _builder.newLineIfNotEmpty();
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("QList<QObject*> all");
                String _name_39 = this._cppExtensions.toName(dto_3);
                _builder.append(_name_39, "\t");
                _builder.append("();");
                _builder.newLineIfNotEmpty();
                _builder.newLine();
                {
                  boolean _or_1 = false;
                  boolean _hasTargetOS_6 = this._managerExtensions.hasTargetOS(pkg);
                  boolean _not_2 = (!_hasTargetOS_6);
                  if (_not_2) {
                    _or_1 = true;
                  } else {
                    String _name_40 = dto_3.getName();
                    boolean _notEquals_2 = (!Objects.equal(_name_40, "SettingsData"));
                    _or_1 = _notEquals_2;
                  }
                  if (_or_1) {
                    _builder.append("\t");
                    _builder.append("Q_INVOKABLE");
                    _builder.newLine();
                    _builder.append("\t");
                    _builder.append("void delete");
                    String _name_41 = this._cppExtensions.toName(dto_3);
                    _builder.append(_name_41, "\t");
                    _builder.append("();");
                    _builder.newLineIfNotEmpty();
                  }
                }
                _builder.newLine();
                _builder.append("\t");
                _builder.append("// access from QML to list of all ");
                String _name_42 = this._cppExtensions.toName(dto_3);
                _builder.append(_name_42, "\t");
                _builder.newLineIfNotEmpty();
                {
                  boolean _hasTargetOS_7 = this._managerExtensions.hasTargetOS(pkg);
                  if (_hasTargetOS_7) {
                    _builder.append("\t");
                    _builder.append("QQmlListProperty<");
                    String _name_43 = this._cppExtensions.toName(dto_3);
                    _builder.append(_name_43, "\t");
                    _builder.append("> ");
                    String _name_44 = this._cppExtensions.toName(dto_3);
                    String _firstLower_8 = StringExtensions.toFirstLower(_name_44);
                    _builder.append(_firstLower_8, "\t");
                    _builder.append("PropertyList();");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t");
                    _builder.append("QDeclarativeListProperty<");
                    String _name_45 = this._cppExtensions.toName(dto_3);
                    _builder.append(_name_45, "\t");
                    _builder.append("> ");
                    String _name_46 = this._cppExtensions.toName(dto_3);
                    String _firstLower_9 = StringExtensions.toFirstLower(_name_46);
                    _builder.append(_firstLower_9, "\t");
                    _builder.append("PropertyList();");
                    _builder.newLineIfNotEmpty();
                  }
                }
              }
            }
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            String _name_47 = this._cppExtensions.toName(dto_3);
            _builder.append(_name_47, "\t");
            _builder.append("* create");
            String _name_48 = this._cppExtensions.toName(dto_3);
            _builder.append(_name_48, "\t");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("void undoCreate");
            String _name_49 = this._cppExtensions.toName(dto_3);
            _builder.append(_name_49, "\t");
            _builder.append("(");
            String _name_50 = this._cppExtensions.toName(dto_3);
            _builder.append(_name_50, "\t");
            _builder.append("* ");
            String _name_51 = this._cppExtensions.toName(dto_3);
            String _firstLower_10 = StringExtensions.toFirstLower(_name_51);
            _builder.append(_firstLower_10, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.newLine();
            {
              boolean _or_2 = false;
              boolean _hasTargetOS_8 = this._managerExtensions.hasTargetOS(pkg);
              boolean _not_3 = (!_hasTargetOS_8);
              if (_not_3) {
                _or_2 = true;
              } else {
                String _name_52 = dto_3.getName();
                boolean _notEquals_3 = (!Objects.equal(_name_52, "SettingsData"));
                _or_2 = _notEquals_3;
              }
              if (_or_2) {
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void insert");
                String _name_53 = this._cppExtensions.toName(dto_3);
                _builder.append(_name_53, "\t");
                _builder.append("(");
                String _name_54 = this._cppExtensions.toName(dto_3);
                _builder.append(_name_54, "\t");
                _builder.append("* ");
                String _name_55 = this._cppExtensions.toName(dto_3);
                String _firstLower_11 = StringExtensions.toFirstLower(_name_55);
                _builder.append(_firstLower_11, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void insert");
                String _name_56 = this._cppExtensions.toName(dto_3);
                _builder.append(_name_56, "\t");
                _builder.append("FromMap(const QVariantMap& ");
                String _name_57 = this._cppExtensions.toName(dto_3);
                String _firstLower_12 = StringExtensions.toFirstLower(_name_57);
                _builder.append(_firstLower_12, "\t");
                _builder.append("Map, const bool& useForeignProperties);");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.newLine();
            {
              boolean _or_3 = false;
              boolean _hasTargetOS_9 = this._managerExtensions.hasTargetOS(pkg);
              boolean _not_4 = (!_hasTargetOS_9);
              if (_not_4) {
                _or_3 = true;
              } else {
                String _name_58 = dto_3.getName();
                boolean _notEquals_4 = (!Objects.equal(_name_58, "SettingsData"));
                _or_3 = _notEquals_4;
              }
              if (_or_3) {
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("bool delete");
                String _name_59 = this._cppExtensions.toName(dto_3);
                _builder.append(_name_59, "\t");
                _builder.append("(");
                String _name_60 = this._cppExtensions.toName(dto_3);
                _builder.append(_name_60, "\t");
                _builder.append("* ");
                String _name_61 = this._cppExtensions.toName(dto_3);
                String _firstLower_13 = StringExtensions.toFirstLower(_name_61);
                _builder.append(_firstLower_13, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.newLine();
            {
              boolean _hasUuid = this._cppExtensions.hasUuid(dto_3);
              if (_hasUuid) {
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("bool delete");
                String _name_62 = this._cppExtensions.toName(dto_3);
                _builder.append(_name_62, "\t");
                _builder.append("ByUuid(const QString& uuid);");
                _builder.newLineIfNotEmpty();
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                String _name_63 = this._cppExtensions.toName(dto_3);
                _builder.append(_name_63, "\t");
                _builder.append("* find");
                String _name_64 = this._cppExtensions.toName(dto_3);
                _builder.append(_name_64, "\t");
                _builder.append("ByUuid(const QString& uuid);");
                _builder.newLineIfNotEmpty();
              }
            }
            {
              boolean _and_1 = false;
              boolean _hasDomainKey = this._cppExtensions.hasDomainKey(dto_3);
              if (!_hasDomainKey) {
                _and_1 = false;
              } else {
                String _domainKey = this._cppExtensions.domainKey(dto_3);
                boolean _notEquals_5 = (!Objects.equal(_domainKey, "uuid"));
                _and_1 = _notEquals_5;
              }
              if (_and_1) {
                _builder.newLine();
                {
                  boolean _or_4 = false;
                  boolean _hasTargetOS_10 = this._managerExtensions.hasTargetOS(pkg);
                  boolean _not_5 = (!_hasTargetOS_10);
                  if (_not_5) {
                    _or_4 = true;
                  } else {
                    String _name_65 = dto_3.getName();
                    boolean _notEquals_6 = (!Objects.equal(_name_65, "SettingsData"));
                    _or_4 = _notEquals_6;
                  }
                  if (_or_4) {
                    _builder.append("\t");
                    _builder.append("Q_INVOKABLE");
                    _builder.newLine();
                    _builder.append("\t");
                    _builder.append("bool delete");
                    String _name_66 = this._cppExtensions.toName(dto_3);
                    _builder.append(_name_66, "\t");
                    _builder.append("By");
                    String _domainKey_1 = this._cppExtensions.domainKey(dto_3);
                    String _firstUpper_3 = StringExtensions.toFirstUpper(_domainKey_1);
                    _builder.append(_firstUpper_3, "\t");
                    _builder.append("(const ");
                    String _domainKeyType = this._cppExtensions.domainKeyType(dto_3);
                    _builder.append(_domainKeyType, "\t");
                    _builder.append("& ");
                    String _domainKey_2 = this._cppExtensions.domainKey(dto_3);
                    _builder.append(_domainKey_2, "\t");
                    _builder.append(");");
                    _builder.newLineIfNotEmpty();
                    _builder.newLine();
                    _builder.append("\t");
                    _builder.append("Q_INVOKABLE");
                    _builder.newLine();
                    _builder.append("    ");
                    String _name_67 = this._cppExtensions.toName(dto_3);
                    _builder.append(_name_67, "    ");
                    _builder.append("* find");
                    String _name_68 = this._cppExtensions.toName(dto_3);
                    _builder.append(_name_68, "    ");
                    _builder.append("By");
                    String _domainKey_3 = this._cppExtensions.domainKey(dto_3);
                    String _firstUpper_4 = StringExtensions.toFirstUpper(_domainKey_3);
                    _builder.append(_firstUpper_4, "    ");
                    _builder.append("(const ");
                    String _domainKeyType_1 = this._cppExtensions.domainKeyType(dto_3);
                    _builder.append(_domainKeyType_1, "    ");
                    _builder.append("& ");
                    String _domainKey_4 = this._cppExtensions.domainKey(dto_3);
                    _builder.append(_domainKey_4, "    ");
                    _builder.append(");");
                    _builder.newLineIfNotEmpty();
                  }
                }
                _builder.newLine();
              }
            }
          }
        }
      }
    }
    _builder.newLine();
    {
      boolean _hasSqlCache_2 = this._managerExtensions.hasSqlCache(pkg);
      if (_hasSqlCache_2) {
        _builder.append("\t");
        _builder.append("Q_INVOKABLE");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("void setChunkSize(const int& newChunkSize);");
        _builder.newLine();
        {
          boolean _has2PhaseInit_2 = this._managerExtensions.has2PhaseInit(pkg);
          if (_has2PhaseInit_2) {
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("bool is2PhaseInitDone();");
            _builder.newLine();
          }
        }
      }
    }
    _builder.newLine();
    {
      EList<LType> _types_4 = pkg.getTypes();
      final Function1<LType, Boolean> _function_10 = new Function1<LType, Boolean>() {
        public Boolean apply(final LType it) {
          return Boolean.valueOf((it instanceof LDto));
        }
      };
      Iterable<LType> _filter_6 = IterableExtensions.<LType>filter(_types_4, _function_10);
      final Function1<LType, LDto> _function_11 = new Function1<LType, LDto>() {
        public LDto apply(final LType it) {
          return ((LDto) it);
        }
      };
      Iterable<LDto> _map_4 = IterableExtensions.<LType, LDto>map(_filter_6, _function_11);
      for(final LDto dto_4 : _map_4) {
        {
          boolean _isRootDataObject_2 = this._cppExtensions.isRootDataObject(dto_4);
          if (_isRootDataObject_2) {
            {
              boolean _or_5 = false;
              boolean _hasTargetOS_11 = this._managerExtensions.hasTargetOS(pkg);
              boolean _not_6 = (!_hasTargetOS_11);
              if (_not_6) {
                _or_5 = true;
              } else {
                String _name_69 = dto_4.getName();
                boolean _notEquals_7 = (!Objects.equal(_name_69, "SettingsData"));
                _or_5 = _notEquals_7;
              }
              if (_or_5) {
                _builder.append("    ");
                _builder.append("void init");
                String _name_70 = this._cppExtensions.toName(dto_4);
                _builder.append(_name_70, "    ");
                _builder.append("FromCache();");
                _builder.newLineIfNotEmpty();
                {
                  boolean _hasSqlCachePropertyName = this._cppExtensions.hasSqlCachePropertyName(dto_4);
                  if (_hasSqlCachePropertyName) {
                    {
                      boolean _is2PhaseInit = this._cppExtensions.is2PhaseInit(dto_4);
                      if (_is2PhaseInit) {
                        _builder.append("    ");
                        _builder.append("void init");
                        String _name_71 = this._cppExtensions.toName(dto_4);
                        _builder.append(_name_71, "    ");
                        _builder.append("FromSqlCache1();");
                        _builder.newLineIfNotEmpty();
                      } else {
                        _builder.append("    ");
                        _builder.append("void init");
                        String _name_72 = this._cppExtensions.toName(dto_4);
                        _builder.append(_name_72, "    ");
                        _builder.append("FromSqlCache();");
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
    }
    {
      boolean _hasTargetOS_12 = this._managerExtensions.hasTargetOS(pkg);
      if (_hasTargetOS_12) {
        _builder.append("\t");
        _builder.append("Q_INVOKABLE");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("SettingsData* settingsData();");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("void finish();");
        _builder.newLine();
      }
    }
    _builder.newLine();
    _builder.append("Q_SIGNALS:");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("void shuttingDown();");
    _builder.newLine();
    {
      EList<LType> _types_5 = pkg.getTypes();
      final Function1<LType, Boolean> _function_12 = new Function1<LType, Boolean>() {
        public Boolean apply(final LType it) {
          return Boolean.valueOf((it instanceof LDto));
        }
      };
      Iterable<LType> _filter_7 = IterableExtensions.<LType>filter(_types_5, _function_12);
      final Function1<LType, LDto> _function_13 = new Function1<LType, LDto>() {
        public LDto apply(final LType it) {
          return ((LDto) it);
        }
      };
      Iterable<LDto> _map_5 = IterableExtensions.<LType, LDto>map(_filter_7, _function_13);
      for(final LDto dto_5 : _map_5) {
        {
          boolean _isRootDataObject_3 = this._cppExtensions.isRootDataObject(dto_5);
          if (_isRootDataObject_3) {
            {
              boolean _or_6 = false;
              boolean _hasTargetOS_13 = this._managerExtensions.hasTargetOS(pkg);
              boolean _not_7 = (!_hasTargetOS_13);
              if (_not_7) {
                _or_6 = true;
              } else {
                String _name_73 = dto_5.getName();
                boolean _notEquals_8 = (!Objects.equal(_name_73, "SettingsData"));
                _or_6 = _notEquals_8;
              }
              if (_or_6) {
                _builder.append("\t");
                _builder.append("void addedToAll");
                String _name_74 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_74, "\t");
                _builder.append("(");
                String _name_75 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_75, "\t");
                _builder.append("* ");
                String _name_76 = this._cppExtensions.toName(dto_5);
                String _firstLower_14 = StringExtensions.toFirstLower(_name_76);
                _builder.append(_firstLower_14, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                {
                  boolean _hasUuid_1 = this._cppExtensions.hasUuid(dto_5);
                  if (_hasUuid_1) {
                    _builder.append("\t");
                    _builder.append("void deletedFromAll");
                    String _name_77 = this._cppExtensions.toName(dto_5);
                    _builder.append(_name_77, "\t");
                    _builder.append("ByUuid(QString uuid);");
                    _builder.newLineIfNotEmpty();
                  }
                }
                {
                  boolean _and_2 = false;
                  boolean _hasDomainKey_1 = this._cppExtensions.hasDomainKey(dto_5);
                  if (!_hasDomainKey_1) {
                    _and_2 = false;
                  } else {
                    String _domainKey_5 = this._cppExtensions.domainKey(dto_5);
                    boolean _notEquals_9 = (!Objects.equal(_domainKey_5, "uuid"));
                    _and_2 = _notEquals_9;
                  }
                  if (_and_2) {
                    _builder.append("\t");
                    _builder.append("void deletedFromAll");
                    String _name_78 = this._cppExtensions.toName(dto_5);
                    _builder.append(_name_78, "\t");
                    _builder.append("By");
                    String _domainKey_6 = this._cppExtensions.domainKey(dto_5);
                    String _firstUpper_5 = StringExtensions.toFirstUpper(_domainKey_6);
                    _builder.append(_firstUpper_5, "\t");
                    _builder.append("(");
                    String _domainKeyType_2 = this._cppExtensions.domainKeyType(dto_5);
                    _builder.append(_domainKeyType_2, "\t");
                    _builder.append(" ");
                    String _domainKey_7 = this._cppExtensions.domainKey(dto_5);
                    _builder.append(_domainKey_7, "\t");
                    _builder.append(");");
                    _builder.newLineIfNotEmpty();
                  }
                }
                _builder.append("\t");
                _builder.append("void deletedFromAll");
                String _name_79 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_79, "\t");
                _builder.append("(");
                String _name_80 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_80, "\t");
                _builder.append("* ");
                String _name_81 = this._cppExtensions.toName(dto_5);
                String _firstLower_15 = StringExtensions.toFirstLower(_name_81);
                _builder.append(_firstLower_15, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("void ");
                String _name_82 = this._cppExtensions.toName(dto_5);
                String _firstLower_16 = StringExtensions.toFirstLower(_name_82);
                _builder.append(_firstLower_16, "\t");
                _builder.append("PropertyListChanged();");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    {
      boolean _and_3 = false;
      boolean _hasSqlCache_3 = this._managerExtensions.hasSqlCache(pkg);
      if (!_hasSqlCache_3) {
        _and_3 = false;
      } else {
        boolean _has2PhaseInit_3 = this._managerExtensions.has2PhaseInit(pkg);
        _and_3 = _has2PhaseInit_3;
      }
      if (_and_3) {
        _builder.append("    ");
        _builder.append("void finished2PhaseInit();");
        _builder.newLine();
      }
    }
    _builder.newLine();
    {
      boolean _hasTargetOS_14 = this._managerExtensions.hasTargetOS(pkg);
      boolean _not_8 = (!_hasTargetOS_14);
      if (_not_8) {
        _builder.append("public slots:");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("void onManualExit();");
        _builder.newLine();
      }
    }
    _builder.newLine();
    {
      boolean _and_4 = false;
      boolean _hasSqlCache_4 = this._managerExtensions.hasSqlCache(pkg);
      if (!_hasSqlCache_4) {
        _and_4 = false;
      } else {
        boolean _has2PhaseInit_4 = this._managerExtensions.has2PhaseInit(pkg);
        _and_4 = _has2PhaseInit_4;
      }
      if (_and_4) {
        _builder.append("private Q_SLOTS:");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("void onPhase2TimerTimeout();");
        _builder.newLine();
        _builder.newLine();
      }
    }
    _builder.append("private:");
    _builder.newLine();
    {
      boolean _hasTargetOS_15 = this._managerExtensions.hasTargetOS(pkg);
      if (_hasTargetOS_15) {
        _builder.append("\t");
        _builder.append("QString mDataRoot;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("QString mDataPath;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("QString mSettingsPath;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("QString mDataAssetsPath;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("QString dataAssetsPath(const QString& fileName);");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("QString dataPath(const QString& fileName);");
        _builder.newLine();
        _builder.newLine();
        _builder.append("\t");
        _builder.append("SettingsData* mSettingsData;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("void readSettings();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("void saveSettings();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("bool mCompactJson;");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("// DataObject stored in List of QObject*");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// GroupDataModel only supports QObject*");
    _builder.newLine();
    {
      EList<LType> _types_6 = pkg.getTypes();
      final Function1<LType, Boolean> _function_14 = new Function1<LType, Boolean>() {
        public Boolean apply(final LType it) {
          return Boolean.valueOf((it instanceof LDto));
        }
      };
      Iterable<LType> _filter_8 = IterableExtensions.<LType>filter(_types_6, _function_14);
      final Function1<LType, LDto> _function_15 = new Function1<LType, LDto>() {
        public LDto apply(final LType it) {
          return ((LDto) it);
        }
      };
      Iterable<LDto> _map_6 = IterableExtensions.<LType, LDto>map(_filter_8, _function_15);
      for(final LDto dto_6 : _map_6) {
        {
          boolean _isRootDataObject_4 = this._cppExtensions.isRootDataObject(dto_6);
          if (_isRootDataObject_4) {
            {
              boolean _or_7 = false;
              boolean _hasTargetOS_16 = this._managerExtensions.hasTargetOS(pkg);
              boolean _not_9 = (!_hasTargetOS_16);
              if (_not_9) {
                _or_7 = true;
              } else {
                String _name_83 = dto_6.getName();
                boolean _notEquals_10 = (!Objects.equal(_name_83, "SettingsData"));
                _or_7 = _notEquals_10;
              }
              if (_or_7) {
                _builder.append("    ");
                _builder.append("QList<QObject*> mAll");
                String _name_84 = this._cppExtensions.toName(dto_6);
                _builder.append(_name_84, "    ");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
              }
            }
            {
              boolean _is2PhaseInit_1 = this._cppExtensions.is2PhaseInit(dto_6);
              if (_is2PhaseInit_1) {
                _builder.append("    ");
                _builder.append("// collects data for priority loading");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("QVariantMap m");
                String _name_85 = this._cppExtensions.toName(dto_6);
                _builder.append(_name_85, "    ");
                _builder.append("2PhaseInit;");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("bool m");
                String _name_86 = this._cppExtensions.toName(dto_6);
                _builder.append(_name_86, "    ");
                _builder.append("Init2Done;");
                _builder.newLineIfNotEmpty();
              }
            }
            {
              boolean _hasTargetOS_17 = this._managerExtensions.hasTargetOS(pkg);
              if (_hasTargetOS_17) {
                {
                  String _name_87 = dto_6.getName();
                  boolean _notEquals_11 = (!Objects.equal(_name_87, "SettingsData"));
                  if (_notEquals_11) {
                    _builder.append("    ");
                    _builder.append("// implementation for QQmlListProperty to use");
                    _builder.newLine();
                    _builder.append("    ");
                    _builder.append("// QML functions for List of All ");
                    String _name_88 = this._cppExtensions.toName(dto_6);
                    _builder.append(_name_88, "    ");
                    _builder.append("*");
                    _builder.newLineIfNotEmpty();
                    _builder.append("    ");
                    _builder.append("static void appendTo");
                    String _name_89 = this._cppExtensions.toName(dto_6);
                    _builder.append(_name_89, "    ");
                    _builder.append("Property(");
                    _builder.newLineIfNotEmpty();
                    _builder.append("    ");
                    _builder.append("\t");
                    _builder.append("QQmlListProperty<");
                    String _name_90 = this._cppExtensions.toName(dto_6);
                    _builder.append(_name_90, "    \t");
                    _builder.append("> *");
                    String _name_91 = this._cppExtensions.toName(dto_6);
                    String _firstLower_17 = StringExtensions.toFirstLower(_name_91);
                    _builder.append(_firstLower_17, "    \t");
                    _builder.append("List,");
                    _builder.newLineIfNotEmpty();
                    _builder.append("    ");
                    _builder.append("\t");
                    String _name_92 = this._cppExtensions.toName(dto_6);
                    _builder.append(_name_92, "    \t");
                    _builder.append("* ");
                    String _name_93 = this._cppExtensions.toName(dto_6);
                    String _firstLower_18 = StringExtensions.toFirstLower(_name_93);
                    _builder.append(_firstLower_18, "    \t");
                    _builder.append(");");
                    _builder.newLineIfNotEmpty();
                    _builder.append("    ");
                    _builder.append("static int ");
                    String _name_94 = this._cppExtensions.toName(dto_6);
                    String _firstLower_19 = StringExtensions.toFirstLower(_name_94);
                    _builder.append(_firstLower_19, "    ");
                    _builder.append("PropertyCount(");
                    _builder.newLineIfNotEmpty();
                    _builder.append("    ");
                    _builder.append("\t");
                    _builder.append("QQmlListProperty<");
                    String _name_95 = this._cppExtensions.toName(dto_6);
                    _builder.append(_name_95, "    \t");
                    _builder.append("> *");
                    String _name_96 = this._cppExtensions.toName(dto_6);
                    String _firstLower_20 = StringExtensions.toFirstLower(_name_96);
                    _builder.append(_firstLower_20, "    \t");
                    _builder.append("List);");
                    _builder.newLineIfNotEmpty();
                    _builder.append("    ");
                    _builder.append("static ");
                    String _name_97 = this._cppExtensions.toName(dto_6);
                    _builder.append(_name_97, "    ");
                    _builder.append("* at");
                    String _name_98 = this._cppExtensions.toName(dto_6);
                    _builder.append(_name_98, "    ");
                    _builder.append("Property(");
                    _builder.newLineIfNotEmpty();
                    _builder.append("    ");
                    _builder.append("\t");
                    _builder.append("QQmlListProperty<");
                    String _name_99 = this._cppExtensions.toName(dto_6);
                    _builder.append(_name_99, "    \t");
                    _builder.append("> *");
                    String _name_100 = this._cppExtensions.toName(dto_6);
                    String _firstLower_21 = StringExtensions.toFirstLower(_name_100);
                    _builder.append(_firstLower_21, "    \t");
                    _builder.append("List, int pos);");
                    _builder.newLineIfNotEmpty();
                    _builder.append("    ");
                    _builder.append("static void clear");
                    String _name_101 = this._cppExtensions.toName(dto_6);
                    _builder.append(_name_101, "    ");
                    _builder.append("Property(");
                    _builder.newLineIfNotEmpty();
                    _builder.append("    ");
                    _builder.append("\t");
                    _builder.append("QQmlListProperty<");
                    String _name_102 = this._cppExtensions.toName(dto_6);
                    _builder.append(_name_102, "    \t");
                    _builder.append("> *");
                    String _name_103 = this._cppExtensions.toName(dto_6);
                    String _firstLower_22 = StringExtensions.toFirstLower(_name_103);
                    _builder.append(_firstLower_22, "    \t");
                    _builder.append("List);");
                    _builder.newLineIfNotEmpty();
                  }
                }
              } else {
                _builder.append("    ");
                _builder.append("// implementation for QDeclarativeListProperty to use");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("// QML functions for List of All ");
                String _name_104 = this._cppExtensions.toName(dto_6);
                _builder.append(_name_104, "    ");
                _builder.append("*");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("static void appendTo");
                String _name_105 = this._cppExtensions.toName(dto_6);
                _builder.append(_name_105, "    ");
                _builder.append("Property(");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("\t");
                _builder.append("QDeclarativeListProperty<");
                String _name_106 = this._cppExtensions.toName(dto_6);
                _builder.append(_name_106, "    \t");
                _builder.append("> *");
                String _name_107 = this._cppExtensions.toName(dto_6);
                String _firstLower_23 = StringExtensions.toFirstLower(_name_107);
                _builder.append(_firstLower_23, "    \t");
                _builder.append("List,");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("\t");
                String _name_108 = this._cppExtensions.toName(dto_6);
                _builder.append(_name_108, "    \t");
                _builder.append("* ");
                String _name_109 = this._cppExtensions.toName(dto_6);
                String _firstLower_24 = StringExtensions.toFirstLower(_name_109);
                _builder.append(_firstLower_24, "    \t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("static int ");
                String _name_110 = this._cppExtensions.toName(dto_6);
                String _firstLower_25 = StringExtensions.toFirstLower(_name_110);
                _builder.append(_firstLower_25, "    ");
                _builder.append("PropertyCount(");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("\t");
                _builder.append("QDeclarativeListProperty<");
                String _name_111 = this._cppExtensions.toName(dto_6);
                _builder.append(_name_111, "    \t");
                _builder.append("> *");
                String _name_112 = this._cppExtensions.toName(dto_6);
                String _firstLower_26 = StringExtensions.toFirstLower(_name_112);
                _builder.append(_firstLower_26, "    \t");
                _builder.append("List);");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("static ");
                String _name_113 = this._cppExtensions.toName(dto_6);
                _builder.append(_name_113, "    ");
                _builder.append("* at");
                String _name_114 = this._cppExtensions.toName(dto_6);
                _builder.append(_name_114, "    ");
                _builder.append("Property(");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("\t");
                _builder.append("QDeclarativeListProperty<");
                String _name_115 = this._cppExtensions.toName(dto_6);
                _builder.append(_name_115, "    \t");
                _builder.append("> *");
                String _name_116 = this._cppExtensions.toName(dto_6);
                String _firstLower_27 = StringExtensions.toFirstLower(_name_116);
                _builder.append(_firstLower_27, "    \t");
                _builder.append("List, int pos);");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("static void clear");
                String _name_117 = this._cppExtensions.toName(dto_6);
                _builder.append(_name_117, "    ");
                _builder.append("Property(");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("\t");
                _builder.append("QDeclarativeListProperty<");
                String _name_118 = this._cppExtensions.toName(dto_6);
                _builder.append(_name_118, "    \t");
                _builder.append("> *");
                String _name_119 = this._cppExtensions.toName(dto_6);
                String _firstLower_28 = StringExtensions.toFirstLower(_name_119);
                _builder.append(_firstLower_28, "    \t");
                _builder.append("List);");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("    ");
            _builder.append("\t");
            _builder.newLine();
          }
        }
        {
          boolean _isTree_1 = this._cppExtensions.isTree(dto_6);
          if (_isTree_1) {
            _builder.append("    ");
            _builder.append("QList<QObject*> mAll");
            String _name_120 = this._cppExtensions.toName(dto_6);
            _builder.append(_name_120, "    ");
            _builder.append("Flat;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.newLine();
    {
      EList<LType> _types_7 = pkg.getTypes();
      final Function1<LType, Boolean> _function_16 = new Function1<LType, Boolean>() {
        public Boolean apply(final LType it) {
          return Boolean.valueOf((it instanceof LDto));
        }
      };
      Iterable<LType> _filter_9 = IterableExtensions.<LType>filter(_types_7, _function_16);
      final Function1<LType, LDto> _function_17 = new Function1<LType, LDto>() {
        public LDto apply(final LType it) {
          return ((LDto) it);
        }
      };
      Iterable<LDto> _map_7 = IterableExtensions.<LType, LDto>map(_filter_9, _function_17);
      for(final LDto dto_7 : _map_7) {
        {
          boolean _isRootDataObject_5 = this._cppExtensions.isRootDataObject(dto_7);
          if (_isRootDataObject_5) {
            {
              boolean _or_8 = false;
              boolean _hasTargetOS_18 = this._managerExtensions.hasTargetOS(pkg);
              boolean _not_10 = (!_hasTargetOS_18);
              if (_not_10) {
                _or_8 = true;
              } else {
                String _name_121 = dto_7.getName();
                boolean _notEquals_12 = (!Objects.equal(_name_121, "SettingsData"));
                _or_8 = _notEquals_12;
              }
              if (_or_8) {
                _builder.append("    ");
                _builder.append("void save");
                String _name_122 = this._cppExtensions.toName(dto_7);
                _builder.append(_name_122, "    ");
                _builder.append("ToCache();");
                _builder.newLineIfNotEmpty();
                {
                  boolean _hasSqlCachePropertyName_1 = this._cppExtensions.hasSqlCachePropertyName(dto_7);
                  if (_hasSqlCachePropertyName_1) {
                    _builder.append("    ");
                    _builder.append("\t");
                    _builder.append("void save");
                    String _name_123 = this._cppExtensions.toName(dto_7);
                    _builder.append(_name_123, "    \t");
                    _builder.append("ToSqlCache();");
                    _builder.newLineIfNotEmpty();
                    {
                      boolean _is2PhaseInit_2 = this._cppExtensions.is2PhaseInit(dto_7);
                      if (_is2PhaseInit_2) {
                        _builder.append("    ");
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("void init");
                        String _name_124 = this._cppExtensions.toName(dto_7);
                        _builder.append(_name_124, "    \t\t");
                        _builder.append("FromSqlCache2();");
                        _builder.newLineIfNotEmpty();
                        _builder.append("    ");
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("void process");
                        String _name_125 = this._cppExtensions.toName(dto_7);
                        _builder.append(_name_125, "    \t\t");
                        _builder.append("Query2();");
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
    }
    _builder.newLine();
    {
      boolean _hasSqlCache_5 = this._managerExtensions.hasSqlCache(pkg);
      if (_hasSqlCache_5) {
        _builder.append("// S Q L");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("QSqlDatabase mDatabase;");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("bool mDatabaseAvailable;");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("bool initDatabase();");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("void bulkImport(const bool& tuneJournalAndSync);");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("int mChunkSize;");
        _builder.newLine();
        {
          boolean _has2PhaseInit_5 = this._managerExtensions.has2PhaseInit(pkg);
          if (_has2PhaseInit_5) {
            _builder.append("    ");
            _builder.append("bool m2PhaseInitDone;");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("QSqlQuery mPhase2Query;");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("QTimer *mPhase2Timer;");
            _builder.newLine();
          }
        }
      }
    }
    _builder.newLine();
    _builder.append("\t");
    _builder.append("QVariantList readFromCache(const QString& fileName);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("void writeToCache(const QString& fileName, QVariantList& data);");
    _builder.newLine();
    {
      boolean _hasTargetOS_19 = this._managerExtensions.hasTargetOS(pkg);
      boolean _not_11 = (!_hasTargetOS_19);
      if (_not_11) {
        _builder.append("\t");
        _builder.append("void finish();");
        _builder.newLine();
      }
    }
    _builder.append("};");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#endif /* DATAMANAGER_HPP_ */");
    _builder.newLine();
    return _builder;
  }
}
