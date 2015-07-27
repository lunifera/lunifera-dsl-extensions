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
      boolean _hasSqlCache = this._managerExtensions.hasSqlCache(pkg);
      if (_hasSqlCache) {
        _builder.append("#include <QtSql/QtSql>");
        _builder.newLine();
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
        _builder.append("#include  \"GeoCoordinate.hpp\"");
        _builder.newLine();
      }
    }
    {
      boolean _hasGeoAddress = this._managerExtensions.hasGeoAddress(pkg);
      if (_hasGeoAddress) {
        _builder.append("#include  \"GeoAddress.hpp\"");
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
    _builder.append("// QDeclarativeListProperty to get easy access from QML");
    _builder.newLine();
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
            _builder.append("Q_PROPERTY(QDeclarativeListProperty<");
            String _name_1 = this._cppExtensions.toName(dto_1);
            _builder.append(_name_1, "");
            _builder.append("> ");
            String _name_2 = this._cppExtensions.toName(dto_1);
            String _firstLower = StringExtensions.toFirstLower(_name_2);
            _builder.append(_firstLower, "");
            _builder.append("PropertyList READ ");
            String _name_3 = this._cppExtensions.toName(dto_1);
            String _firstLower_1 = StringExtensions.toFirstLower(_name_3);
            _builder.append(_firstLower_1, "");
            _builder.append("PropertyList CONSTANT)");
            _builder.newLineIfNotEmpty();
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
    _builder.append("    ");
    _builder.append("virtual ~DataManager();");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("Q_INVOKABLE");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("void init();");
    _builder.newLine();
    _builder.newLine();
    {
      EList<LType> _types_2 = pkg.getTypes();
      final Function1<LType, Boolean> _function_4 = new Function1<LType, Boolean>() {
        public Boolean apply(final LType it) {
          return Boolean.valueOf((it instanceof LDto));
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
          boolean _isRootDataObject_1 = this._cppExtensions.isRootDataObject(dto_2);
          if (_isRootDataObject_1) {
            _builder.append("\t");
            _builder.newLine();
            {
              boolean _isTree = this._cppExtensions.isTree(dto_2);
              if (_isTree) {
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void fill");
                String _name_4 = this._cppExtensions.toName(dto_2);
                _builder.append(_name_4, "\t");
                _builder.append("TreeDataModel(QString objectName);");
                _builder.newLineIfNotEmpty();
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void fill");
                String _name_5 = this._cppExtensions.toName(dto_2);
                _builder.append(_name_5, "\t");
                _builder.append("FlatDataModel(QString objectName);");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void fill");
                String _name_6 = this._cppExtensions.toName(dto_2);
                _builder.append(_name_6, "\t");
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
            String _name_7 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_7, "\t");
            _builder.append("DataModel(QString objectName, ");
            String _name_8 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_8, "\t");
            _builder.append("* listItem);");
            _builder.newLineIfNotEmpty();
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("void removeItemFrom");
            String _name_9 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_9, "\t");
            _builder.append("DataModel(QString objectName, ");
            String _name_10 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_10, "\t");
            _builder.append("* listItem);");
            _builder.newLineIfNotEmpty();
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("void insertItemInto");
            String _name_11 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_11, "\t");
            _builder.append("DataModel(QString objectName, ");
            String _name_12 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_12, "\t");
            _builder.append("* listItem);");
            _builder.newLineIfNotEmpty();
            {
              List<? extends LFeature> _allFeatures = dto_2.getAllFeatures();
              final Function1<LFeature, Boolean> _function_6 = new Function1<LFeature, Boolean>() {
                public Boolean apply(final LFeature it) {
                  return Boolean.valueOf(HppManagerGenerator.this._cppExtensions.hasIndex(it));
                }
              };
              Iterable<? extends LFeature> _filter_3 = IterableExtensions.filter(_allFeatures, _function_6);
              for(final LFeature feature : _filter_3) {
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                {
                  boolean _isLazy = this._cppExtensions.isLazy(feature);
                  if (_isLazy) {
                    _builder.append("\t");
                    _builder.append("void fill");
                    String _name_13 = this._cppExtensions.toName(dto_2);
                    _builder.append(_name_13, "\t");
                    _builder.append("DataModelBy");
                    String _name_14 = this._cppExtensions.toName(feature);
                    String _firstUpper = StringExtensions.toFirstUpper(_name_14);
                    _builder.append(_firstUpper, "\t");
                    _builder.append("(QString objectName, const ");
                    String _referenceDomainKeyType = this._cppExtensions.referenceDomainKeyType(feature);
                    _builder.append(_referenceDomainKeyType, "\t");
                    _builder.append("& ");
                    String _name_15 = this._cppExtensions.toName(feature);
                    _builder.append(_name_15, "\t");
                    _builder.append(");");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t");
                    _builder.append("void fill");
                    String _name_16 = this._cppExtensions.toName(dto_2);
                    _builder.append(_name_16, "\t");
                    _builder.append("DataModelBy");
                    String _name_17 = this._cppExtensions.toName(feature);
                    String _firstUpper_1 = StringExtensions.toFirstUpper(_name_17);
                    _builder.append(_firstUpper_1, "\t");
                    _builder.append("(QString objectName, const ");
                    String _typeName = this._cppExtensions.toTypeName(feature);
                    _builder.append(_typeName, "\t");
                    _builder.append("& ");
                    String _name_18 = this._cppExtensions.toName(feature);
                    _builder.append(_name_18, "\t");
                    _builder.append(");");
                    _builder.newLineIfNotEmpty();
                  }
                }
              }
            }
            {
              boolean _existsLazy = this._cppExtensions.existsLazy(dto_2);
              if (_existsLazy) {
                {
                  List<? extends LFeature> _allFeatures_1 = dto_2.getAllFeatures();
                  final Function1<LFeature, Boolean> _function_7 = new Function1<LFeature, Boolean>() {
                    public Boolean apply(final LFeature it) {
                      return Boolean.valueOf(HppManagerGenerator.this._cppExtensions.isLazy(it));
                    }
                  };
                  Iterable<? extends LFeature> _filter_4 = IterableExtensions.filter(_allFeatures_1, _function_7);
                  for(final LFeature feature_1 : _filter_4) {
                    {
                      boolean _isHierarchy = this._cppExtensions.isHierarchy(dto_2, feature_1);
                      if (_isHierarchy) {
                        _builder.newLine();
                        _builder.append("\t");
                        _builder.append("// isHierarchy of: ");
                        String _name_19 = this._cppExtensions.toName(dto_2);
                        _builder.append(_name_19, "\t");
                        _builder.append(" FEATURE: ");
                        String _name_20 = this._cppExtensions.toName(feature_1);
                        _builder.append(_name_20, "\t");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("Q_INVOKABLE");
                        _builder.newLine();
                        _builder.append("\t");
                        _builder.append("void init");
                        String _name_21 = this._cppExtensions.toName(feature_1);
                        String _firstUpper_2 = StringExtensions.toFirstUpper(_name_21);
                        _builder.append(_firstUpper_2, "\t");
                        _builder.append("HierarchyList(");
                        String _name_22 = this._cppExtensions.toName(dto_2);
                        _builder.append(_name_22, "\t");
                        _builder.append("* ");
                        String _name_23 = this._cppExtensions.toName(feature_1);
                        _builder.append(_name_23, "\t");
                        String _name_24 = this._cppExtensions.toName(dto_2);
                        _builder.append(_name_24, "\t");
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
                String _name_25 = this._cppExtensions.toName(dto_2);
                _builder.append(_name_25, "\t");
                _builder.append("References(");
                String _name_26 = this._cppExtensions.toName(dto_2);
                _builder.append(_name_26, "\t");
                _builder.append("* ");
                String _name_27 = this._cppExtensions.toName(dto_2);
                String _firstLower_2 = StringExtensions.toFirstLower(_name_27);
                _builder.append(_firstLower_2, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void resolveReferencesForAll");
                String _name_28 = this._cppExtensions.toName(dto_2);
                _builder.append(_name_28, "\t");
                _builder.append("();");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("QList<");
            String _name_29 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_29, "\t");
            _builder.append("*> listOf");
            String _name_30 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_30, "\t");
            _builder.append("ForKeys(QStringList keyList);");
            _builder.newLineIfNotEmpty();
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("QVariantList ");
            String _name_31 = this._cppExtensions.toName(dto_2);
            String _firstLower_3 = StringExtensions.toFirstLower(_name_31);
            _builder.append(_firstLower_3, "\t");
            _builder.append("AsQVariantList();");
            _builder.newLineIfNotEmpty();
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("QList<QObject*> all");
            String _name_32 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_32, "\t");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("void delete");
            String _name_33 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_33, "\t");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
            _builder.newLine();
            _builder.append("\t");
            _builder.append("// access from QML to list of all ");
            String _name_34 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_34, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("QDeclarativeListProperty<");
            String _name_35 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_35, "\t");
            _builder.append("> ");
            String _name_36 = this._cppExtensions.toName(dto_2);
            String _firstLower_4 = StringExtensions.toFirstLower(_name_36);
            _builder.append(_firstLower_4, "\t");
            _builder.append("PropertyList();");
            _builder.newLineIfNotEmpty();
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            String _name_37 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_37, "\t");
            _builder.append("* create");
            String _name_38 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_38, "\t");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("void undoCreate");
            String _name_39 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_39, "\t");
            _builder.append("(");
            String _name_40 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_40, "\t");
            _builder.append("* ");
            String _name_41 = this._cppExtensions.toName(dto_2);
            String _firstLower_5 = StringExtensions.toFirstLower(_name_41);
            _builder.append(_firstLower_5, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("void insert");
            String _name_42 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_42, "\t");
            _builder.append("(");
            String _name_43 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_43, "\t");
            _builder.append("* ");
            String _name_44 = this._cppExtensions.toName(dto_2);
            String _firstLower_6 = StringExtensions.toFirstLower(_name_44);
            _builder.append(_firstLower_6, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("void insert");
            String _name_45 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_45, "\t");
            _builder.append("FromMap(const QVariantMap& ");
            String _name_46 = this._cppExtensions.toName(dto_2);
            String _firstLower_7 = StringExtensions.toFirstLower(_name_46);
            _builder.append(_firstLower_7, "\t");
            _builder.append("Map, const bool& useForeignProperties);");
            _builder.newLineIfNotEmpty();
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("bool delete");
            String _name_47 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_47, "\t");
            _builder.append("(");
            String _name_48 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_48, "\t");
            _builder.append("* ");
            String _name_49 = this._cppExtensions.toName(dto_2);
            String _firstLower_8 = StringExtensions.toFirstLower(_name_49);
            _builder.append(_firstLower_8, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            {
              boolean _hasUuid = this._cppExtensions.hasUuid(dto_2);
              if (_hasUuid) {
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("bool delete");
                String _name_50 = this._cppExtensions.toName(dto_2);
                _builder.append(_name_50, "\t");
                _builder.append("ByUuid(const QString& uuid);");
                _builder.newLineIfNotEmpty();
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                String _name_51 = this._cppExtensions.toName(dto_2);
                _builder.append(_name_51, "\t");
                _builder.append("* find");
                String _name_52 = this._cppExtensions.toName(dto_2);
                _builder.append(_name_52, "\t");
                _builder.append("ByUuid(const QString& uuid);");
                _builder.newLineIfNotEmpty();
              }
            }
            {
              boolean _and = false;
              boolean _hasDomainKey = this._cppExtensions.hasDomainKey(dto_2);
              if (!_hasDomainKey) {
                _and = false;
              } else {
                String _domainKey = this._cppExtensions.domainKey(dto_2);
                boolean _notEquals = (!Objects.equal(_domainKey, "uuid"));
                _and = _notEquals;
              }
              if (_and) {
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("bool delete");
                String _name_53 = this._cppExtensions.toName(dto_2);
                _builder.append(_name_53, "\t");
                _builder.append("By");
                String _domainKey_1 = this._cppExtensions.domainKey(dto_2);
                String _firstUpper_3 = StringExtensions.toFirstUpper(_domainKey_1);
                _builder.append(_firstUpper_3, "\t");
                _builder.append("(const ");
                String _domainKeyType = this._cppExtensions.domainKeyType(dto_2);
                _builder.append(_domainKeyType, "\t");
                _builder.append("& ");
                String _domainKey_2 = this._cppExtensions.domainKey(dto_2);
                _builder.append(_domainKey_2, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("    ");
                String _name_54 = this._cppExtensions.toName(dto_2);
                _builder.append(_name_54, "    ");
                _builder.append("* find");
                String _name_55 = this._cppExtensions.toName(dto_2);
                _builder.append(_name_55, "    ");
                _builder.append("By");
                String _domainKey_3 = this._cppExtensions.domainKey(dto_2);
                String _firstUpper_4 = StringExtensions.toFirstUpper(_domainKey_3);
                _builder.append(_firstUpper_4, "    ");
                _builder.append("(const ");
                String _domainKeyType_1 = this._cppExtensions.domainKeyType(dto_2);
                _builder.append(_domainKeyType_1, "    ");
                _builder.append("& ");
                String _domainKey_4 = this._cppExtensions.domainKey(dto_2);
                _builder.append(_domainKey_4, "    ");
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
      EList<LType> _types_3 = pkg.getTypes();
      final Function1<LType, Boolean> _function_8 = new Function1<LType, Boolean>() {
        public Boolean apply(final LType it) {
          return Boolean.valueOf((it instanceof LDto));
        }
      };
      Iterable<LType> _filter_5 = IterableExtensions.<LType>filter(_types_3, _function_8);
      final Function1<LType, LDto> _function_9 = new Function1<LType, LDto>() {
        public LDto apply(final LType it) {
          return ((LDto) it);
        }
      };
      Iterable<LDto> _map_3 = IterableExtensions.<LType, LDto>map(_filter_5, _function_9);
      for(final LDto dto_3 : _map_3) {
        {
          boolean _isRootDataObject_2 = this._cppExtensions.isRootDataObject(dto_3);
          if (_isRootDataObject_2) {
            _builder.append("    ");
            _builder.append("void init");
            String _name_56 = this._cppExtensions.toName(dto_3);
            _builder.append(_name_56, "    ");
            _builder.append("FromCache();");
            _builder.newLineIfNotEmpty();
            {
              boolean _hasSqlCachePropertyName = this._cppExtensions.hasSqlCachePropertyName(dto_3);
              if (_hasSqlCachePropertyName) {
                _builder.append("    ");
                _builder.append("void init");
                String _name_57 = this._cppExtensions.toName(dto_3);
                _builder.append(_name_57, "    ");
                _builder.append("FromSqlCache();");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    _builder.newLine();
    {
      boolean _hasSqlCache_1 = this._managerExtensions.hasSqlCache(pkg);
      if (_hasSqlCache_1) {
        _builder.append("\t");
        _builder.append("Q_INVOKABLE");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("void setChunkSize(const int& newChunkSize);");
        _builder.newLine();
      }
    }
    _builder.newLine();
    _builder.append("Q_SIGNALS:");
    _builder.newLine();
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
          boolean _isRootDataObject_3 = this._cppExtensions.isRootDataObject(dto_4);
          if (_isRootDataObject_3) {
            _builder.append("\t");
            _builder.append("void addedToAll");
            String _name_58 = this._cppExtensions.toName(dto_4);
            _builder.append(_name_58, "\t");
            _builder.append("(");
            String _name_59 = this._cppExtensions.toName(dto_4);
            _builder.append(_name_59, "\t");
            _builder.append("* ");
            String _name_60 = this._cppExtensions.toName(dto_4);
            String _firstLower_9 = StringExtensions.toFirstLower(_name_60);
            _builder.append(_firstLower_9, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            {
              boolean _hasUuid_1 = this._cppExtensions.hasUuid(dto_4);
              if (_hasUuid_1) {
                _builder.append("\t");
                _builder.append("void deletedFromAll");
                String _name_61 = this._cppExtensions.toName(dto_4);
                _builder.append(_name_61, "\t");
                _builder.append("ByUuid(QString uuid);");
                _builder.newLineIfNotEmpty();
              }
            }
            {
              boolean _and_1 = false;
              boolean _hasDomainKey_1 = this._cppExtensions.hasDomainKey(dto_4);
              if (!_hasDomainKey_1) {
                _and_1 = false;
              } else {
                String _domainKey_5 = this._cppExtensions.domainKey(dto_4);
                boolean _notEquals_1 = (!Objects.equal(_domainKey_5, "uuid"));
                _and_1 = _notEquals_1;
              }
              if (_and_1) {
                _builder.append("\t");
                _builder.append("void deletedFromAll");
                String _name_62 = this._cppExtensions.toName(dto_4);
                _builder.append(_name_62, "\t");
                _builder.append("By");
                String _domainKey_6 = this._cppExtensions.domainKey(dto_4);
                String _firstUpper_5 = StringExtensions.toFirstUpper(_domainKey_6);
                _builder.append(_firstUpper_5, "\t");
                _builder.append("(");
                String _domainKeyType_2 = this._cppExtensions.domainKeyType(dto_4);
                _builder.append(_domainKeyType_2, "\t");
                _builder.append(" ");
                String _domainKey_7 = this._cppExtensions.domainKey(dto_4);
                _builder.append(_domainKey_7, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.append("void deletedFromAll");
            String _name_63 = this._cppExtensions.toName(dto_4);
            _builder.append(_name_63, "\t");
            _builder.append("(");
            String _name_64 = this._cppExtensions.toName(dto_4);
            _builder.append(_name_64, "\t");
            _builder.append("* ");
            String _name_65 = this._cppExtensions.toName(dto_4);
            String _firstLower_10 = StringExtensions.toFirstLower(_name_65);
            _builder.append(_firstLower_10, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("    ");
    _builder.newLine();
    _builder.append("public slots:");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("void onManualExit();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("private:");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// DataObject stored in List of QObject*");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// GroupDataModel only supports QObject*");
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
          boolean _isRootDataObject_4 = this._cppExtensions.isRootDataObject(dto_5);
          if (_isRootDataObject_4) {
            _builder.append("    ");
            _builder.append("QList<QObject*> mAll");
            String _name_66 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_66, "    ");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            {
              boolean _is2PhaseInit = this._cppExtensions.is2PhaseInit(dto_5);
              if (_is2PhaseInit) {
                _builder.append("    ");
                _builder.append("// collects data for priority loading");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("QVariantMap m");
                String _name_67 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_67, "    ");
                _builder.append("2PhaseInit;");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("    ");
            _builder.append("// implementation for QDeclarativeListProperty to use");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("// QML functions for List of All ");
            String _name_68 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_68, "    ");
            _builder.append("*");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("static void appendTo");
            String _name_69 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_69, "    ");
            _builder.append("Property(");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("\t");
            _builder.append("QDeclarativeListProperty<");
            String _name_70 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_70, "    \t");
            _builder.append("> *");
            String _name_71 = this._cppExtensions.toName(dto_5);
            String _firstLower_11 = StringExtensions.toFirstLower(_name_71);
            _builder.append(_firstLower_11, "    \t");
            _builder.append("List,");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("\t");
            String _name_72 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_72, "    \t");
            _builder.append("* ");
            String _name_73 = this._cppExtensions.toName(dto_5);
            String _firstLower_12 = StringExtensions.toFirstLower(_name_73);
            _builder.append(_firstLower_12, "    \t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("static int ");
            String _name_74 = this._cppExtensions.toName(dto_5);
            String _firstLower_13 = StringExtensions.toFirstLower(_name_74);
            _builder.append(_firstLower_13, "    ");
            _builder.append("PropertyCount(");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("\t");
            _builder.append("QDeclarativeListProperty<");
            String _name_75 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_75, "    \t");
            _builder.append("> *");
            String _name_76 = this._cppExtensions.toName(dto_5);
            String _firstLower_14 = StringExtensions.toFirstLower(_name_76);
            _builder.append(_firstLower_14, "    \t");
            _builder.append("List);");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("static ");
            String _name_77 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_77, "    ");
            _builder.append("* at");
            String _name_78 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_78, "    ");
            _builder.append("Property(");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("\t");
            _builder.append("QDeclarativeListProperty<");
            String _name_79 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_79, "    \t");
            _builder.append("> *");
            String _name_80 = this._cppExtensions.toName(dto_5);
            String _firstLower_15 = StringExtensions.toFirstLower(_name_80);
            _builder.append(_firstLower_15, "    \t");
            _builder.append("List, int pos);");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("static void clear");
            String _name_81 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_81, "    ");
            _builder.append("Property(");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("\t");
            _builder.append("QDeclarativeListProperty<");
            String _name_82 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_82, "    \t");
            _builder.append("> *");
            String _name_83 = this._cppExtensions.toName(dto_5);
            String _firstLower_16 = StringExtensions.toFirstLower(_name_83);
            _builder.append(_firstLower_16, "    \t");
            _builder.append("List);");
            _builder.newLineIfNotEmpty();
          }
        }
        {
          boolean _isTree_1 = this._cppExtensions.isTree(dto_5);
          if (_isTree_1) {
            _builder.append("    ");
            _builder.append("QList<QObject*> mAll");
            String _name_84 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_84, "    ");
            _builder.append("Flat;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      boolean _has2PhaseInit = this._managerExtensions.has2PhaseInit(pkg);
      if (_has2PhaseInit) {
        _builder.append("\t");
        _builder.append("bool m2PhaseInitDone();");
        _builder.newLine();
      }
    }
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
          boolean _isRootDataObject_5 = this._cppExtensions.isRootDataObject(dto_6);
          if (_isRootDataObject_5) {
            _builder.append("    ");
            _builder.append("void save");
            String _name_85 = this._cppExtensions.toName(dto_6);
            _builder.append(_name_85, "    ");
            _builder.append("ToCache();");
            _builder.newLineIfNotEmpty();
            {
              boolean _hasSqlCachePropertyName_1 = this._cppExtensions.hasSqlCachePropertyName(dto_6);
              if (_hasSqlCachePropertyName_1) {
                _builder.append("    ");
                _builder.append("void save");
                String _name_86 = this._cppExtensions.toName(dto_6);
                _builder.append(_name_86, "    ");
                _builder.append("ToSqlCache();");
                _builder.newLineIfNotEmpty();
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
      }
    }
    _builder.newLine();
    _builder.append("\t");
    _builder.append("QVariantList readFromCache(QString& fileName);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("void writeToCache(QString& fileName, QVariantList& data);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("void finish();");
    _builder.newLine();
    _builder.append("};");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#endif /* DATAMANAGER_HPP_ */");
    _builder.newLine();
    return _builder;
  }
}
