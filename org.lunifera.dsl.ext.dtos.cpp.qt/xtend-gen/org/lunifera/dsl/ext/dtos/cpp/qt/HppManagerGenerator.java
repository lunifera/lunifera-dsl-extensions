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
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.lunifera.dsl.ext.dtos.cpp.qt.CppExtensions;
import org.lunifera.dsl.ext.dtos.cpp.qt.ManagerExtensions;
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
    _builder.newLine();
    _builder.append("class DataManager: public QObject");
    _builder.newLine();
    _builder.append("{");
    _builder.newLine();
    _builder.append("Q_OBJECT");
    _builder.newLine();
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
          boolean _isRootDataObject = this._managerExtensions.isRootDataObject(dto_1);
          if (_isRootDataObject) {
            _builder.append("\t");
            _builder.newLine();
            {
              boolean _isTree = this._cppExtensions.isTree(dto_1);
              if (_isTree) {
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void fill");
                String _name_1 = this._cppExtensions.toName(dto_1);
                _builder.append(_name_1, "\t");
                _builder.append("TreeDataModel(QString objectName);");
                _builder.newLineIfNotEmpty();
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void fill");
                String _name_2 = this._cppExtensions.toName(dto_1);
                _builder.append(_name_2, "\t");
                _builder.append("FlatDataModel(QString objectName);");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void fill");
                String _name_3 = this._cppExtensions.toName(dto_1);
                _builder.append(_name_3, "\t");
                _builder.append("DataModel(QString objectName);");
                _builder.newLineIfNotEmpty();
              }
            }
            {
              boolean _existsLazy = this._cppExtensions.existsLazy(dto_1);
              if (_existsLazy) {
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("void resolve");
                String _name_4 = this._cppExtensions.toName(dto_1);
                _builder.append(_name_4, "\t");
                _builder.append("References(");
                String _name_5 = this._cppExtensions.toName(dto_1);
                _builder.append(_name_5, "\t");
                _builder.append("* ");
                String _name_6 = this._cppExtensions.toName(dto_1);
                String _firstLower = StringExtensions.toFirstLower(_name_6);
                _builder.append(_firstLower, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("void insert");
            String _name_7 = this._cppExtensions.toName(dto_1);
            _builder.append(_name_7, "\t");
            _builder.append("(");
            String _name_8 = this._cppExtensions.toName(dto_1);
            _builder.append(_name_8, "\t");
            _builder.append("* ");
            String _name_9 = this._cppExtensions.toName(dto_1);
            String _firstLower_1 = StringExtensions.toFirstLower(_name_9);
            _builder.append(_firstLower_1, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("void insert");
            String _name_10 = this._cppExtensions.toName(dto_1);
            _builder.append(_name_10, "\t");
            _builder.append("FromMap(const QVariantMap& ");
            String _name_11 = this._cppExtensions.toName(dto_1);
            String _firstLower_2 = StringExtensions.toFirstLower(_name_11);
            _builder.append(_firstLower_2, "\t");
            _builder.append("Map, const bool& useForeignProperties);");
            _builder.newLineIfNotEmpty();
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Q_INVOKABLE");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("bool delete");
            String _name_12 = this._cppExtensions.toName(dto_1);
            _builder.append(_name_12, "\t");
            _builder.append("(");
            String _name_13 = this._cppExtensions.toName(dto_1);
            _builder.append(_name_13, "\t");
            _builder.append("* ");
            String _name_14 = this._cppExtensions.toName(dto_1);
            String _firstLower_3 = StringExtensions.toFirstLower(_name_14);
            _builder.append(_firstLower_3, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            {
              boolean _hasUuid = this._cppExtensions.hasUuid(dto_1);
              if (_hasUuid) {
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("bool delete");
                String _name_15 = this._cppExtensions.toName(dto_1);
                _builder.append(_name_15, "\t");
                _builder.append("ByUuid(const QString& uuid);");
                _builder.newLineIfNotEmpty();
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("\t");
                String _name_16 = this._cppExtensions.toName(dto_1);
                _builder.append(_name_16, "\t");
                _builder.append("* find");
                String _name_17 = this._cppExtensions.toName(dto_1);
                _builder.append(_name_17, "\t");
                _builder.append("ByUuid(const QString& uuid);");
                _builder.newLineIfNotEmpty();
              }
            }
            {
              boolean _and = false;
              boolean _hasDomainKey = this._cppExtensions.hasDomainKey(dto_1);
              if (!_hasDomainKey) {
                _and = false;
              } else {
                String _domainKey = this._cppExtensions.domainKey(dto_1);
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
                String _name_18 = this._cppExtensions.toName(dto_1);
                _builder.append(_name_18, "\t");
                _builder.append("By");
                String _domainKey_1 = this._cppExtensions.domainKey(dto_1);
                String _firstUpper = StringExtensions.toFirstUpper(_domainKey_1);
                _builder.append(_firstUpper, "\t");
                _builder.append("(const int& ");
                String _domainKey_2 = this._cppExtensions.domainKey(dto_1);
                _builder.append(_domainKey_2, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                _builder.newLine();
                _builder.append("\t");
                _builder.append("Q_INVOKABLE");
                _builder.newLine();
                _builder.append("    ");
                String _name_19 = this._cppExtensions.toName(dto_1);
                _builder.append(_name_19, "    ");
                _builder.append("* find");
                String _name_20 = this._cppExtensions.toName(dto_1);
                _builder.append(_name_20, "    ");
                _builder.append("By");
                String _domainKey_3 = this._cppExtensions.domainKey(dto_1);
                String _firstUpper_1 = StringExtensions.toFirstUpper(_domainKey_3);
                _builder.append(_firstUpper_1, "    ");
                _builder.append("(const ");
                String _domainKeyType = this._cppExtensions.domainKeyType(dto_1);
                _builder.append(_domainKeyType, "    ");
                _builder.append("& ");
                String _domainKey_4 = this._cppExtensions.domainKey(dto_1);
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
    _builder.append("Q_SIGNALS:");
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
          boolean _isRootDataObject_1 = this._managerExtensions.isRootDataObject(dto_2);
          if (_isRootDataObject_1) {
            _builder.append("\t");
            _builder.append("void addedToAll");
            String _name_21 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_21, "\t");
            _builder.append("(");
            String _name_22 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_22, "\t");
            _builder.append("* orderData);");
            _builder.newLineIfNotEmpty();
            {
              boolean _hasUuid_1 = this._cppExtensions.hasUuid(dto_2);
              if (_hasUuid_1) {
                _builder.append("\t");
                _builder.append("void deletedFromAll");
                String _name_23 = this._cppExtensions.toName(dto_2);
                _builder.append(_name_23, "\t");
                _builder.append("ByUuid(QString uuid);");
                _builder.newLineIfNotEmpty();
              }
            }
            {
              boolean _and_1 = false;
              boolean _hasDomainKey_1 = this._cppExtensions.hasDomainKey(dto_2);
              if (!_hasDomainKey_1) {
                _and_1 = false;
              } else {
                String _domainKey_5 = this._cppExtensions.domainKey(dto_2);
                boolean _notEquals_1 = (!Objects.equal(_domainKey_5, "uuid"));
                _and_1 = _notEquals_1;
              }
              if (_and_1) {
                _builder.append("\t");
                _builder.append("void deletedFromAll");
                String _name_24 = this._cppExtensions.toName(dto_2);
                _builder.append(_name_24, "\t");
                _builder.append("By");
                String _domainKey_6 = this._cppExtensions.domainKey(dto_2);
                String _firstUpper_2 = StringExtensions.toFirstUpper(_domainKey_6);
                _builder.append(_firstUpper_2, "\t");
                _builder.append("(int ");
                String _domainKey_7 = this._cppExtensions.domainKey(dto_2);
                _builder.append(_domainKey_7, "\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              }
            }
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
          boolean _isRootDataObject_2 = this._managerExtensions.isRootDataObject(dto_3);
          if (_isRootDataObject_2) {
            _builder.append("    ");
            _builder.append("QList<QObject*> mAll");
            String _name_25 = this._cppExtensions.toName(dto_3);
            _builder.append(_name_25, "    ");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
        {
          boolean _isTree_1 = this._cppExtensions.isTree(dto_3);
          if (_isTree_1) {
            _builder.append("    ");
            _builder.append("QList<QObject*> mAll");
            String _name_26 = this._cppExtensions.toName(dto_3);
            _builder.append(_name_26, "    ");
            _builder.append("Flat;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.newLine();
    {
      EList<LType> _types_4 = pkg.getTypes();
      final Function1<LType, Boolean> _function_8 = new Function1<LType, Boolean>() {
        public Boolean apply(final LType it) {
          return Boolean.valueOf((it instanceof LDto));
        }
      };
      Iterable<LType> _filter_4 = IterableExtensions.<LType>filter(_types_4, _function_8);
      final Function1<LType, LDto> _function_9 = new Function1<LType, LDto>() {
        public LDto apply(final LType it) {
          return ((LDto) it);
        }
      };
      Iterable<LDto> _map_4 = IterableExtensions.<LType, LDto>map(_filter_4, _function_9);
      for(final LDto dto_4 : _map_4) {
        {
          boolean _isRootDataObject_3 = this._managerExtensions.isRootDataObject(dto_4);
          if (_isRootDataObject_3) {
            _builder.append("    ");
            _builder.append("void init");
            String _name_27 = this._cppExtensions.toName(dto_4);
            _builder.append(_name_27, "    ");
            _builder.append("FromCache();");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("void save");
            String _name_28 = this._cppExtensions.toName(dto_4);
            _builder.append(_name_28, "    ");
            _builder.append("ToCache();");
            _builder.newLineIfNotEmpty();
          }
        }
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
