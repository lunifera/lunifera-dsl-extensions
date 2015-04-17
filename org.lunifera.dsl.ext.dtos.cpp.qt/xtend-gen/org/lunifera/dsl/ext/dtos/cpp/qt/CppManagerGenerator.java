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
import org.lunifera.dsl.semantic.common.types.LEnum;
import org.lunifera.dsl.semantic.common.types.LFeature;
import org.lunifera.dsl.semantic.common.types.LType;
import org.lunifera.dsl.semantic.common.types.LTypedPackage;
import org.lunifera.dsl.semantic.dto.LDto;

@SuppressWarnings("all")
public class CppManagerGenerator {
  @Inject
  @Extension
  private CppExtensions _cppExtensions;
  
  @Inject
  @Extension
  private ManagerExtensions _managerExtensions;
  
  public String toFileName(final LTypedPackage pkg) {
    return "DataManager.cpp";
  }
  
  public CharSequence toContent(final LTypedPackage pkg) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("#include <QObject>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include \"DataManager.hpp\"");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include <bb/cascades/Application>");
    _builder.newLine();
    _builder.append("#include <bb/cascades/AbstractPane>");
    _builder.newLine();
    _builder.append("#include <bb/data/JsonDataAccess>");
    _builder.newLine();
    _builder.append("#include <bb/cascades/GroupDataModel>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("static QString dataAssetsPath(const QString& fileName)");
    _builder.newLine();
    _builder.append("{");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("return QDir::currentPath() + \"/app/native/assets/datamodel/\" + fileName;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("static QString dataPath(const QString& fileName)");
    _builder.newLine();
    _builder.append("{");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("return QDir::currentPath() + \"/data/\" + fileName;");
    _builder.newLine();
    _builder.append("}");
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
        {
          boolean _isTree = this._cppExtensions.isTree(dto);
          if (_isTree) {
            _builder.append("// cache");
            String _name = this._cppExtensions.toName(dto);
            _builder.append(_name, "");
            _builder.append(" is tree of  ");
            String _name_1 = this._cppExtensions.toName(dto);
            _builder.append(_name_1, "");
            _builder.newLineIfNotEmpty();
            _builder.append("// there\'s also a flat list (in memory only) useful for easy filtering");
            _builder.newLine();
          }
        }
        {
          boolean _isRootDataObject = this._managerExtensions.isRootDataObject(dto);
          if (_isRootDataObject) {
            _builder.append("static QString cache");
            String _name_2 = this._cppExtensions.toName(dto);
            _builder.append(_name_2, "");
            _builder.append(" = \"cache");
            String _name_3 = this._cppExtensions.toName(dto);
            _builder.append(_name_3, "");
            _builder.append(".json\";");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.newLine();
    _builder.append("using namespace bb::cascades;");
    _builder.newLine();
    _builder.append("using namespace bb::data;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("DataManager::DataManager(QObject *parent) :");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("QObject(parent)");
    _builder.newLine();
    _builder.append("{");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// ApplicationUI is parent of DataManager");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// DataManager is parent of all root DataObjects");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// ROOT DataObjects are parent of contained DataObjects");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// ROOT:");
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
          boolean _isRootDataObject_1 = this._managerExtensions.isRootDataObject(dto_1);
          if (_isRootDataObject_1) {
            _builder.append("    ");
            _builder.append("// ");
            String _name_4 = this._cppExtensions.toName(dto_1);
            _builder.append(_name_4, "    ");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// register all DataObjects to get access to properties from QML:");
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
        _builder.append("\t");
        _builder.append("qmlRegisterType<");
        String _name_5 = this._cppExtensions.toName(dto_2);
        _builder.append(_name_5, "\t");
        _builder.append(">(\"org.ekkescorner.data\", 1, 0, \"");
        String _name_6 = this._cppExtensions.toName(dto_2);
        _builder.append(_name_6, "\t");
        _builder.append("\");");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      boolean _hasGeo = this._managerExtensions.hasGeo(pkg);
      if (_hasGeo) {
        _builder.append("\t");
        _builder.append("// QGeo... classes wrapped as QObject* to be able to access via Q_PROPERTY");
        _builder.newLine();
        {
          boolean _hasGeoCoordinate = this._managerExtensions.hasGeoCoordinate(pkg);
          if (_hasGeoCoordinate) {
            _builder.append("\t");
            _builder.append("qmlRegisterType<GeoCoordinate>(\"org.ekkescorner.data\", 1, 0, \"GeoCoordinate\");");
            _builder.newLine();
          }
        }
        {
          boolean _hasGeoAddress = this._managerExtensions.hasGeoAddress(pkg);
          if (_hasGeoAddress) {
            _builder.append("\t");
            _builder.append("qmlRegisterType<GeoAddress>(\"org.ekkescorner.data\", 1, 0, \"GeoAddress\");");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("// register all ENUMs to get access from QML");
    _builder.newLine();
    {
      EList<LType> _types_3 = pkg.getTypes();
      final Function1<LType, Boolean> _function_6 = new Function1<LType, Boolean>() {
        public Boolean apply(final LType it) {
          return Boolean.valueOf((it instanceof LEnum));
        }
      };
      Iterable<LType> _filter_3 = IterableExtensions.<LType>filter(_types_3, _function_6);
      final Function1<LType, LEnum> _function_7 = new Function1<LType, LEnum>() {
        public LEnum apply(final LType it) {
          return ((LEnum) it);
        }
      };
      Iterable<LEnum> _map_3 = IterableExtensions.<LType, LEnum>map(_filter_3, _function_7);
      for(final LEnum en : _map_3) {
        _builder.append("\t");
        _builder.append("qmlRegisterType<");
        String _name_7 = this._cppExtensions.toName(en);
        _builder.append(_name_7, "\t");
        _builder.append(">(\"org.ekkescorner.enums\", 1, 0, \"");
        String _name_8 = this._cppExtensions.toName(en);
        _builder.append(_name_8, "\t");
        _builder.append("\");");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("// useful Types for all APPs dealing with data");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// QTimer");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("qmlRegisterType<QTimer>(\"org.ekkescorner.common\", 1, 0, \"QTimer\");");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// no auto exit: we must persist the cache before");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("bb::Application::instance()->setAutoExit(false);");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("bool res = QObject::connect(bb::Application::instance(), SIGNAL(manualExit()), this, SLOT(onManualExit()));");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("Q_ASSERT(res);");
    _builder.newLine();
    _builder.newLine();
    _builder.append("    ");
    _builder.append("Q_UNUSED(res);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* loads all data from cache.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* called from main.qml with delay using QTimer");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("void DataManager::init()");
    _builder.newLine();
    _builder.append("{");
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
      for(final LDto dto_3 : _map_4) {
        {
          boolean _isRootDataObject_2 = this._managerExtensions.isRootDataObject(dto_3);
          if (_isRootDataObject_2) {
            _builder.append("    ");
            _builder.append("init");
            String _name_9 = this._cppExtensions.toName(dto_3);
            _builder.append(_name_9, "    ");
            _builder.append("FromCache();");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("void DataManager::finish()");
    _builder.newLine();
    _builder.append("{");
    _builder.newLine();
    {
      EList<LType> _types_5 = pkg.getTypes();
      final Function1<LType, Boolean> _function_10 = new Function1<LType, Boolean>() {
        public Boolean apply(final LType it) {
          return Boolean.valueOf((it instanceof LDto));
        }
      };
      Iterable<LType> _filter_5 = IterableExtensions.<LType>filter(_types_5, _function_10);
      final Function1<LType, LDto> _function_11 = new Function1<LType, LDto>() {
        public LDto apply(final LType it) {
          return ((LDto) it);
        }
      };
      Iterable<LDto> _map_5 = IterableExtensions.<LType, LDto>map(_filter_5, _function_11);
      for(final LDto dto_4 : _map_5) {
        {
          boolean _isRootDataObject_3 = this._managerExtensions.isRootDataObject(dto_4);
          if (_isRootDataObject_3) {
            {
              boolean _isReadOnlyCache = this._cppExtensions.isReadOnlyCache(dto_4);
              if (_isReadOnlyCache) {
                _builder.append("    ");
                _builder.append("// ");
                String _name_10 = this._cppExtensions.toName(dto_4);
                _builder.append(_name_10, "    ");
                _builder.append(" is read-only - not saved to cache");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("    ");
                _builder.append("save");
                String _name_11 = this._cppExtensions.toName(dto_4);
                _builder.append(_name_11, "    ");
                _builder.append("ToCache();");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    {
      EList<LType> _types_6 = pkg.getTypes();
      final Function1<LType, Boolean> _function_12 = new Function1<LType, Boolean>() {
        public Boolean apply(final LType it) {
          return Boolean.valueOf((it instanceof LDto));
        }
      };
      Iterable<LType> _filter_6 = IterableExtensions.<LType>filter(_types_6, _function_12);
      final Function1<LType, LDto> _function_13 = new Function1<LType, LDto>() {
        public LDto apply(final LType it) {
          return ((LDto) it);
        }
      };
      Iterable<LDto> _map_6 = IterableExtensions.<LType, LDto>map(_filter_6, _function_13);
      for(final LDto dto_5 : _map_6) {
        {
          boolean _isRootDataObject_4 = this._managerExtensions.isRootDataObject(dto_5);
          if (_isRootDataObject_4) {
            _builder.append("/*");
            _builder.newLine();
            _builder.append(" ");
            _builder.append("* reads Maps of ");
            String _name_12 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_12, " ");
            _builder.append(" in from JSON cache");
            _builder.newLineIfNotEmpty();
            _builder.append(" ");
            _builder.append("* creates List of ");
            String _name_13 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_13, " ");
            _builder.append("*  from QVariantList");
            _builder.newLineIfNotEmpty();
            _builder.append(" ");
            _builder.append("* List declared as list of QObject* - only way to use in GroupDataModel");
            _builder.newLine();
            _builder.append(" ");
            _builder.append("*/");
            _builder.newLine();
            _builder.append("void DataManager::init");
            String _name_14 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_14, "");
            _builder.append("FromCache()");
            _builder.newLineIfNotEmpty();
            _builder.append("{");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("mAll");
            String _name_15 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_15, "    ");
            _builder.append(".clear();");
            _builder.newLineIfNotEmpty();
            {
              boolean _isTree_1 = this._cppExtensions.isTree(dto_5);
              if (_isTree_1) {
                _builder.append("    ");
                _builder.append("mAll");
                String _name_16 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_16, "    ");
                _builder.append("Flat.clear();");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("    ");
            _builder.append("QVariantList cacheList;");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("cacheList = readFromCache(cache");
            String _name_17 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_17, "    ");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("qDebug() << \"read ");
            String _name_18 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_18, "    ");
            _builder.append(" from cache #\" << cacheList.size();");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("for (int i = 0; i < cacheList.size(); ++i) {");
            _builder.newLine();
            _builder.append("        ");
            _builder.append("QVariantMap cacheMap;");
            _builder.newLine();
            _builder.append("        ");
            _builder.append("cacheMap = cacheList.at(i).toMap();");
            _builder.newLine();
            _builder.append("        ");
            String _name_19 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_19, "        ");
            _builder.append("* ");
            String _name_20 = this._cppExtensions.toName(dto_5);
            String _firstLower = StringExtensions.toFirstLower(_name_20);
            _builder.append(_firstLower, "        ");
            _builder.append(" = new ");
            String _name_21 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_21, "        ");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            _builder.append("// Important: DataManager must be parent of all root DTOs");
            _builder.newLine();
            _builder.append("        ");
            String _name_22 = this._cppExtensions.toName(dto_5);
            String _firstLower_1 = StringExtensions.toFirstLower(_name_22);
            _builder.append(_firstLower_1, "        ");
            _builder.append("->setParent(this);");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            String _name_23 = this._cppExtensions.toName(dto_5);
            String _firstLower_2 = StringExtensions.toFirstLower(_name_23);
            _builder.append(_firstLower_2, "        ");
            _builder.append("->fillFromCacheMap(cacheMap);");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            _builder.append("mAll");
            String _name_24 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_24, "        ");
            _builder.append(".append(");
            String _name_25 = this._cppExtensions.toName(dto_5);
            String _firstLower_3 = StringExtensions.toFirstLower(_name_25);
            _builder.append(_firstLower_3, "        ");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            {
              boolean _isTree_2 = this._cppExtensions.isTree(dto_5);
              if (_isTree_2) {
                _builder.append("        ");
                _builder.append("mAll");
                String _name_26 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_26, "        ");
                _builder.append("Flat.append(");
                String _name_27 = this._cppExtensions.toName(dto_5);
                String _firstLower_4 = StringExtensions.toFirstLower(_name_27);
                _builder.append(_firstLower_4, "        ");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                _builder.append("mAll");
                String _name_28 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_28, "        ");
                _builder.append("Flat.append(");
                String _name_29 = this._cppExtensions.toName(dto_5);
                String _firstLower_5 = StringExtensions.toFirstLower(_name_29);
                _builder.append(_firstLower_5, "        ");
                _builder.append("->all");
                String _name_30 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_30, "        ");
                _builder.append("Children());");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("    ");
            _builder.append("}");
            _builder.newLine();
            {
              boolean _isTree_3 = this._cppExtensions.isTree(dto_5);
              if (_isTree_3) {
                _builder.append("    ");
                _builder.append("qDebug() << \"created Tree of ");
                String _name_31 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_31, "    ");
                _builder.append("* #\" << mAll");
                String _name_32 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_32, "    ");
                _builder.append(".size();");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("qDebug() << \"created Flat list of ");
                String _name_33 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_33, "    ");
                _builder.append("* #\" << mAll");
                String _name_34 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_34, "    ");
                _builder.append("Flat.size();");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("    ");
                _builder.append("qDebug() << \"created ");
                String _name_35 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_35, "    ");
                _builder.append("* #\" << mAll");
                String _name_36 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_36, "    ");
                _builder.append(".size();");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("}");
            _builder.newLine();
            _builder.newLine();
            _builder.append("/*");
            _builder.newLine();
            _builder.append(" ");
            _builder.append("* save List of ");
            String _name_37 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_37, " ");
            _builder.append("* to JSON cache");
            _builder.newLineIfNotEmpty();
            _builder.append(" ");
            _builder.append("* convert list of ");
            String _name_38 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_38, " ");
            _builder.append("* to QVariantList");
            _builder.newLineIfNotEmpty();
            _builder.append(" ");
            _builder.append("* toCacheMap stores all properties without transient values");
            _builder.newLine();
            {
              boolean _isReadOnlyCache_1 = this._cppExtensions.isReadOnlyCache(dto_5);
              if (_isReadOnlyCache_1) {
                _builder.append(" * ");
                String _name_39 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_39, "");
                _builder.append(" is read-only Cache - so it\'s not saved automatically at exit");
              }
            }
            _builder.newLineIfNotEmpty();
            _builder.append(" ");
            _builder.append("*/");
            _builder.newLine();
            _builder.append("void DataManager::save");
            String _name_40 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_40, "");
            _builder.append("ToCache()");
            _builder.newLineIfNotEmpty();
            _builder.append("{");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("QVariantList cacheList;");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("qDebug() << \"now caching ");
            String _name_41 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_41, "    ");
            _builder.append("* #\" << mAll");
            String _name_42 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_42, "    ");
            _builder.append(".size();");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("for (int i = 0; i < mAll");
            String _name_43 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_43, "    ");
            _builder.append(".size(); ++i) {");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            String _name_44 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_44, "        ");
            _builder.append("* ");
            String _name_45 = this._cppExtensions.toName(dto_5);
            String _firstLower_6 = StringExtensions.toFirstLower(_name_45);
            _builder.append(_firstLower_6, "        ");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            String _name_46 = this._cppExtensions.toName(dto_5);
            String _firstLower_7 = StringExtensions.toFirstLower(_name_46);
            _builder.append(_firstLower_7, "        ");
            _builder.append(" = (");
            String _name_47 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_47, "        ");
            _builder.append("*)mAll");
            String _name_48 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_48, "        ");
            _builder.append(".at(i);");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            _builder.append("QVariantMap cacheMap;");
            _builder.newLine();
            _builder.append("        ");
            _builder.append("cacheMap = ");
            String _name_49 = this._cppExtensions.toName(dto_5);
            String _firstLower_8 = StringExtensions.toFirstLower(_name_49);
            _builder.append(_firstLower_8, "        ");
            _builder.append("->toCacheMap();");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            _builder.append("cacheList.append(cacheMap);");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("}");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("qDebug() << \"");
            String _name_50 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_50, "    ");
            _builder.append("* converted to JSON cache #\" << cacheList.size();");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("writeToCache(cache");
            String _name_51 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_51, "    ");
            _builder.append(", cacheList);");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
            _builder.newLine();
            {
              boolean _existsLazy = this._cppExtensions.existsLazy(dto_5);
              if (_existsLazy) {
                {
                  List<? extends LFeature> _allFeatures = dto_5.getAllFeatures();
                  final Function1<LFeature, Boolean> _function_14 = new Function1<LFeature, Boolean>() {
                    public Boolean apply(final LFeature it) {
                      return Boolean.valueOf(CppManagerGenerator.this._cppExtensions.isLazy(it));
                    }
                  };
                  Iterable<? extends LFeature> _filter_7 = IterableExtensions.filter(_allFeatures, _function_14);
                  for(final LFeature feature : _filter_7) {
                    {
                      boolean _isHierarchy = this._cppExtensions.isHierarchy(dto_5, feature);
                      if (_isHierarchy) {
                        _builder.append("// isHierarchy of: ");
                        String _name_52 = this._cppExtensions.toName(dto_5);
                        _builder.append(_name_52, "");
                        _builder.append(" FEATURE: ");
                        String _name_53 = this._cppExtensions.toName(feature);
                        _builder.append(_name_53, "");
                        _builder.newLineIfNotEmpty();
                        _builder.append("void DataManager::init");
                        String _name_54 = this._cppExtensions.toName(feature);
                        String _firstUpper = StringExtensions.toFirstUpper(_name_54);
                        _builder.append(_firstUpper, "");
                        _builder.append("HierarchyList(");
                        String _name_55 = this._cppExtensions.toName(dto_5);
                        _builder.append(_name_55, "");
                        _builder.append("* ");
                        String _name_56 = this._cppExtensions.toName(feature);
                        _builder.append(_name_56, "");
                        String _name_57 = this._cppExtensions.toName(dto_5);
                        _builder.append(_name_57, "");
                        _builder.append(")");
                        _builder.newLineIfNotEmpty();
                        _builder.append("{");
                        _builder.newLine();
                        _builder.append("\t");
                        _builder.append("if (!");
                        String _name_58 = this._cppExtensions.toName(feature);
                        _builder.append(_name_58, "\t");
                        String _name_59 = this._cppExtensions.toName(dto_5);
                        _builder.append(_name_59, "\t");
                        _builder.append(") {");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t\t");
                        _builder.append("qDebug() << \"cannot init");
                        String _name_60 = this._cppExtensions.toName(feature);
                        String _firstUpper_1 = StringExtensions.toFirstUpper(_name_60);
                        _builder.append(_firstUpper_1, "\t\t");
                        _builder.append("HierarchyList with ");
                        String _name_61 = this._cppExtensions.toName(dto_5);
                        String _firstLower_9 = StringExtensions.toFirstLower(_name_61);
                        _builder.append(_firstLower_9, "\t\t");
                        _builder.append(" NULL\";");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t\t");
                        _builder.append("return;");
                        _builder.newLine();
                        _builder.append("\t");
                        _builder.append("}");
                        _builder.newLine();
                        _builder.append("\t");
                        _builder.append("QList<");
                        String _name_62 = this._cppExtensions.toName(dto_5);
                        _builder.append(_name_62, "\t");
                        _builder.append("*> ");
                        String _name_63 = this._cppExtensions.toName(feature);
                        _builder.append(_name_63, "\t");
                        _builder.append("PropertyList;");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("bool more = true;");
                        _builder.newLine();
                        _builder.append("    ");
                        String _name_64 = this._cppExtensions.toName(dto_5);
                        _builder.append(_name_64, "    ");
                        _builder.append("* ");
                        String _name_65 = this._cppExtensions.toName(dto_5);
                        String _firstLower_10 = StringExtensions.toFirstLower(_name_65);
                        _builder.append(_firstLower_10, "    ");
                        _builder.append(";");
                        _builder.newLineIfNotEmpty();
                        _builder.append("    ");
                        String _name_66 = this._cppExtensions.toName(dto_5);
                        String _firstLower_11 = StringExtensions.toFirstLower(_name_66);
                        _builder.append(_firstLower_11, "    ");
                        _builder.append(" = ");
                        String _name_67 = this._cppExtensions.toName(feature);
                        _builder.append(_name_67, "    ");
                        String _name_68 = this._cppExtensions.toName(dto_5);
                        _builder.append(_name_68, "    ");
                        _builder.append(";");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("while (more) {");
                        _builder.newLine();
                        _builder.append("\t\t");
                        _builder.append("if (");
                        String _name_69 = this._cppExtensions.toName(dto_5);
                        String _firstLower_12 = StringExtensions.toFirstLower(_name_69);
                        _builder.append(_firstLower_12, "\t\t");
                        _builder.append("->has");
                        String _name_70 = this._cppExtensions.toName(feature);
                        String _firstUpper_2 = StringExtensions.toFirstUpper(_name_70);
                        _builder.append(_firstUpper_2, "\t\t");
                        _builder.append("() && !");
                        String _name_71 = this._cppExtensions.toName(dto_5);
                        String _firstLower_13 = StringExtensions.toFirstLower(_name_71);
                        _builder.append(_firstLower_13, "\t\t");
                        _builder.append("->is");
                        String _name_72 = this._cppExtensions.toName(feature);
                        String _firstUpper_3 = StringExtensions.toFirstUpper(_name_72);
                        _builder.append(_firstUpper_3, "\t\t");
                        _builder.append("ResolvedAsDataObject()){");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t\t\t");
                        _builder.append("qDebug() << \"RESOLVE REFERENCES \" << ");
                        String _name_73 = this._cppExtensions.toName(dto_5);
                        String _firstLower_14 = StringExtensions.toFirstLower(_name_73);
                        _builder.append(_firstLower_14, "\t\t\t");
                        _builder.append("->");
                        String _domainKey = this._cppExtensions.domainKey(dto_5);
                        _builder.append(_domainKey, "\t\t\t");
                        _builder.append("();");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t\t\t");
                        _builder.append("resolve");
                        String _name_74 = this._cppExtensions.toName(dto_5);
                        _builder.append(_name_74, "\t\t\t");
                        _builder.append("References(");
                        String _name_75 = this._cppExtensions.toName(dto_5);
                        String _firstLower_15 = StringExtensions.toFirstLower(_name_75);
                        _builder.append(_firstLower_15, "\t\t\t");
                        _builder.append(");");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t\t");
                        _builder.append("}");
                        _builder.newLine();
                        _builder.append("\t\t");
                        _builder.append("if (");
                        String _name_76 = this._cppExtensions.toName(dto_5);
                        String _firstLower_16 = StringExtensions.toFirstLower(_name_76);
                        _builder.append(_firstLower_16, "\t\t");
                        _builder.append("->is");
                        String _name_77 = this._cppExtensions.toName(feature);
                        String _firstUpper_4 = StringExtensions.toFirstUpper(_name_77);
                        _builder.append(_firstUpper_4, "\t\t");
                        _builder.append("ResolvedAsDataObject()) {");
                        _builder.newLineIfNotEmpty();
                        _builder.append("            ");
                        String _name_78 = this._cppExtensions.toName(feature);
                        _builder.append(_name_78, "            ");
                        _builder.append("PropertyList.append(");
                        String _name_79 = this._cppExtensions.toName(dto_5);
                        String _firstLower_17 = StringExtensions.toFirstLower(_name_79);
                        _builder.append(_firstLower_17, "            ");
                        _builder.append("->");
                        String _name_80 = this._cppExtensions.toName(feature);
                        _builder.append(_name_80, "            ");
                        _builder.append("AsDataObject());");
                        _builder.newLineIfNotEmpty();
                        _builder.append("            ");
                        String _name_81 = this._cppExtensions.toName(dto_5);
                        String _firstLower_18 = StringExtensions.toFirstLower(_name_81);
                        _builder.append(_firstLower_18, "            ");
                        _builder.append(" = ");
                        String _name_82 = this._cppExtensions.toName(dto_5);
                        String _firstLower_19 = StringExtensions.toFirstLower(_name_82);
                        _builder.append(_firstLower_19, "            ");
                        _builder.append("->");
                        String _name_83 = this._cppExtensions.toName(feature);
                        _builder.append(_name_83, "            ");
                        _builder.append("AsDataObject();");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t\t");
                        _builder.append("} else {");
                        _builder.newLine();
                        _builder.append("\t\t\t");
                        _builder.append("more = false;");
                        _builder.newLine();
                        _builder.append("\t\t");
                        _builder.append("}");
                        _builder.newLine();
                        _builder.append("\t");
                        _builder.append("}");
                        _builder.newLine();
                        _builder.append("    ");
                        String _name_84 = this._cppExtensions.toName(dto_5);
                        String _firstLower_20 = StringExtensions.toFirstLower(_name_84);
                        _builder.append(_firstLower_20, "    ");
                        _builder.append(" = 0;");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("qDebug() << \"init");
                        String _name_85 = this._cppExtensions.toName(feature);
                        String _firstUpper_5 = StringExtensions.toFirstUpper(_name_85);
                        _builder.append(_firstUpper_5, "\t");
                        _builder.append("HierarchyList DONE with #\" << ");
                        String _name_86 = this._cppExtensions.toName(feature);
                        _builder.append(_name_86, "\t");
                        _builder.append("PropertyList.size();");
                        _builder.newLineIfNotEmpty();
                        _builder.append("    ");
                        String _name_87 = this._cppExtensions.toName(feature);
                        _builder.append(_name_87, "    ");
                        String _name_88 = this._cppExtensions.toName(dto_5);
                        _builder.append(_name_88, "    ");
                        _builder.append("->init");
                        String _name_89 = this._cppExtensions.toName(feature);
                        String _firstUpper_6 = StringExtensions.toFirstUpper(_name_89);
                        _builder.append(_firstUpper_6, "    ");
                        _builder.append("PropertyList(");
                        String _name_90 = this._cppExtensions.toName(feature);
                        _builder.append(_name_90, "    ");
                        _builder.append("PropertyList);");
                        _builder.newLineIfNotEmpty();
                        _builder.append("}");
                        _builder.newLine();
                        _builder.newLine();
                      }
                    }
                  }
                }
                _builder.newLine();
                _builder.append("void DataManager::resolve");
                String _name_91 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_91, "");
                _builder.append("References(");
                String _name_92 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_92, "");
                _builder.append("* ");
                String _name_93 = this._cppExtensions.toName(dto_5);
                String _firstLower_21 = StringExtensions.toFirstLower(_name_93);
                _builder.append(_firstLower_21, "");
                _builder.append(")");
                _builder.newLineIfNotEmpty();
                _builder.append("{");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("if (!");
                String _name_94 = this._cppExtensions.toName(dto_5);
                String _firstLower_22 = StringExtensions.toFirstLower(_name_94);
                _builder.append(_firstLower_22, "\t");
                _builder.append(") {");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                _builder.append("qDebug() << \"cannot resolve");
                String _name_95 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_95, "        ");
                _builder.append("References with ");
                String _name_96 = this._cppExtensions.toName(dto_5);
                String _firstLower_23 = StringExtensions.toFirstLower(_name_96);
                _builder.append(_firstLower_23, "        ");
                _builder.append(" NULL\";");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                _builder.append("return;");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("}");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("if(");
                String _name_97 = this._cppExtensions.toName(dto_5);
                String _firstLower_24 = StringExtensions.toFirstLower(_name_97);
                _builder.append(_firstLower_24, "    ");
                _builder.append("->isAllResolved()) {");
                _builder.newLineIfNotEmpty();
                _builder.append("\t    ");
                _builder.append("qDebug() << \"nothing to do: all is resolved\";");
                _builder.newLine();
                _builder.append("\t    ");
                _builder.append("return;");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("}");
                _builder.newLine();
                {
                  List<? extends LFeature> _allFeatures_1 = dto_5.getAllFeatures();
                  final Function1<LFeature, Boolean> _function_15 = new Function1<LFeature, Boolean>() {
                    public Boolean apply(final LFeature it) {
                      return Boolean.valueOf(CppManagerGenerator.this._cppExtensions.isLazy(it));
                    }
                  };
                  Iterable<? extends LFeature> _filter_8 = IterableExtensions.filter(_allFeatures_1, _function_15);
                  for(final LFeature feature_1 : _filter_8) {
                    _builder.append("    ");
                    _builder.append("if (");
                    String _name_98 = this._cppExtensions.toName(dto_5);
                    String _firstLower_25 = StringExtensions.toFirstLower(_name_98);
                    _builder.append(_firstLower_25, "    ");
                    _builder.append("->has");
                    String _name_99 = this._cppExtensions.toName(feature_1);
                    String _firstUpper_7 = StringExtensions.toFirstUpper(_name_99);
                    _builder.append(_firstUpper_7, "    ");
                    _builder.append("() && !");
                    String _name_100 = this._cppExtensions.toName(dto_5);
                    String _firstLower_26 = StringExtensions.toFirstLower(_name_100);
                    _builder.append(_firstLower_26, "    ");
                    _builder.append("->is");
                    String _name_101 = this._cppExtensions.toName(feature_1);
                    String _firstUpper_8 = StringExtensions.toFirstUpper(_name_101);
                    _builder.append(_firstUpper_8, "    ");
                    _builder.append("ResolvedAsDataObject()) {");
                    _builder.newLineIfNotEmpty();
                    _builder.append("    ");
                    _builder.append("\t");
                    String _typeName = this._cppExtensions.toTypeName(feature_1);
                    _builder.append(_typeName, "    \t");
                    _builder.append("* ");
                    String _name_102 = this._cppExtensions.toName(feature_1);
                    String _firstLower_27 = StringExtensions.toFirstLower(_name_102);
                    _builder.append(_firstLower_27, "    \t");
                    _builder.append(";");
                    _builder.newLineIfNotEmpty();
                    _builder.append("   \t\t");
                    String _name_103 = this._cppExtensions.toName(feature_1);
                    String _firstLower_28 = StringExtensions.toFirstLower(_name_103);
                    _builder.append(_firstLower_28, "   \t\t");
                    _builder.append(" = find");
                    String _typeName_1 = this._cppExtensions.toTypeName(feature_1);
                    _builder.append(_typeName_1, "   \t\t");
                    _builder.append("By");
                    String _referenceDomainKey = this._cppExtensions.referenceDomainKey(feature_1);
                    String _firstUpper_9 = StringExtensions.toFirstUpper(_referenceDomainKey);
                    _builder.append(_firstUpper_9, "   \t\t");
                    _builder.append("(");
                    String _name_104 = this._cppExtensions.toName(dto_5);
                    String _firstLower_29 = StringExtensions.toFirstLower(_name_104);
                    _builder.append(_firstLower_29, "   \t\t");
                    _builder.append("->");
                    String _name_105 = this._cppExtensions.toName(feature_1);
                    String _firstLower_30 = StringExtensions.toFirstLower(_name_105);
                    _builder.append(_firstLower_30, "   \t\t");
                    _builder.append("());");
                    _builder.newLineIfNotEmpty();
                    _builder.append("    ");
                    _builder.append("\t");
                    _builder.append("if (");
                    String _name_106 = this._cppExtensions.toName(feature_1);
                    String _firstLower_31 = StringExtensions.toFirstLower(_name_106);
                    _builder.append(_firstLower_31, "    \t");
                    _builder.append(") {");
                    _builder.newLineIfNotEmpty();
                    _builder.append("    ");
                    _builder.append("\t\t");
                    String _name_107 = this._cppExtensions.toName(dto_5);
                    String _firstLower_32 = StringExtensions.toFirstLower(_name_107);
                    _builder.append(_firstLower_32, "    \t\t");
                    _builder.append("->resolve");
                    String _name_108 = this._cppExtensions.toName(feature_1);
                    String _firstUpper_10 = StringExtensions.toFirstUpper(_name_108);
                    _builder.append(_firstUpper_10, "    \t\t");
                    _builder.append("AsDataObject(");
                    String _name_109 = this._cppExtensions.toName(feature_1);
                    String _firstLower_33 = StringExtensions.toFirstLower(_name_109);
                    _builder.append(_firstLower_33, "    \t\t");
                    _builder.append(");");
                    _builder.newLineIfNotEmpty();
                    _builder.append("    ");
                    _builder.append("\t");
                    _builder.append("} else {");
                    _builder.newLine();
                    _builder.append("    ");
                    _builder.append("\t\t");
                    _builder.append("qDebug() << \"mark");
                    String _name_110 = this._cppExtensions.toName(feature_1);
                    String _firstUpper_11 = StringExtensions.toFirstUpper(_name_110);
                    _builder.append(_firstUpper_11, "    \t\t");
                    _builder.append("AsInvalid: \" << ");
                    String _name_111 = this._cppExtensions.toName(dto_5);
                    String _firstLower_34 = StringExtensions.toFirstLower(_name_111);
                    _builder.append(_firstLower_34, "    \t\t");
                    _builder.append("->");
                    String _name_112 = this._cppExtensions.toName(feature_1);
                    String _firstLower_35 = StringExtensions.toFirstLower(_name_112);
                    _builder.append(_firstLower_35, "    \t\t");
                    _builder.append("();");
                    _builder.newLineIfNotEmpty();
                    _builder.append("    ");
                    _builder.append("\t\t");
                    String _name_113 = this._cppExtensions.toName(dto_5);
                    String _firstLower_36 = StringExtensions.toFirstLower(_name_113);
                    _builder.append(_firstLower_36, "    \t\t");
                    _builder.append("->mark");
                    String _name_114 = this._cppExtensions.toName(feature_1);
                    String _firstUpper_12 = StringExtensions.toFirstUpper(_name_114);
                    _builder.append(_firstUpper_12, "    \t\t");
                    _builder.append("AsInvalid();");
                    _builder.newLineIfNotEmpty();
                    _builder.append("    ");
                    _builder.append("\t");
                    _builder.append("}");
                    _builder.newLine();
                    _builder.append("    ");
                    _builder.append("}");
                    _builder.newLine();
                  }
                }
                _builder.append("}");
                _builder.newLine();
                _builder.append("void DataManager::resolveReferencesForAll");
                String _name_115 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_115, "");
                _builder.append("()");
                _builder.newLineIfNotEmpty();
                _builder.append("{");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("for (int i = 0; i < mAll");
                String _name_116 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_116, "    ");
                _builder.append(".size(); ++i) {");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                String _name_117 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_117, "        ");
                _builder.append("* ");
                String _name_118 = this._cppExtensions.toName(dto_5);
                String _firstLower_37 = StringExtensions.toFirstLower(_name_118);
                _builder.append(_firstLower_37, "        ");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                String _name_119 = this._cppExtensions.toName(dto_5);
                String _firstLower_38 = StringExtensions.toFirstLower(_name_119);
                _builder.append(_firstLower_38, "        ");
                _builder.append(" = (");
                String _name_120 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_120, "        ");
                _builder.append("*)mAll");
                String _name_121 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_121, "        ");
                _builder.append(".at(i);");
                _builder.newLineIfNotEmpty();
                _builder.append("    \t");
                _builder.append("resolve");
                String _name_122 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_122, "    \t");
                _builder.append("References(");
                String _name_123 = this._cppExtensions.toName(dto_5);
                String _firstLower_39 = StringExtensions.toFirstLower(_name_123);
                _builder.append(_firstLower_39, "    \t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("}");
                _builder.newLine();
                _builder.append("}");
                _builder.newLine();
              }
            }
            _builder.newLine();
            _builder.append("void DataManager::insert");
            String _name_124 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_124, "");
            _builder.append("(");
            String _name_125 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_125, "");
            _builder.append("* ");
            String _name_126 = this._cppExtensions.toName(dto_5);
            String _firstLower_40 = StringExtensions.toFirstLower(_name_126);
            _builder.append(_firstLower_40, "");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
            _builder.append("{");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("// Important: DataManager must be parent of all root DTOs");
            _builder.newLine();
            _builder.append("    ");
            String _name_127 = this._cppExtensions.toName(dto_5);
            String _firstLower_41 = StringExtensions.toFirstLower(_name_127);
            _builder.append(_firstLower_41, "    ");
            _builder.append("->setParent(this);");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("mAll");
            String _name_128 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_128, "    ");
            _builder.append(".append(");
            String _name_129 = this._cppExtensions.toName(dto_5);
            String _firstLower_42 = StringExtensions.toFirstLower(_name_129);
            _builder.append(_firstLower_42, "    ");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            {
              boolean _isTree_4 = this._cppExtensions.isTree(dto_5);
              if (_isTree_4) {
                _builder.append("    ");
                _builder.append("mAll");
                String _name_130 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_130, "    ");
                _builder.append("Flat.append(");
                String _name_131 = this._cppExtensions.toName(dto_5);
                String _firstLower_43 = StringExtensions.toFirstLower(_name_131);
                _builder.append(_firstLower_43, "    ");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("    ");
            _builder.append("emit addedToAll");
            String _name_132 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_132, "    ");
            _builder.append("(");
            String _name_133 = this._cppExtensions.toName(dto_5);
            String _firstLower_44 = StringExtensions.toFirstLower(_name_133);
            _builder.append(_firstLower_44, "    ");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
            _builder.newLine();
            _builder.append("void DataManager::insert");
            String _name_134 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_134, "");
            _builder.append("FromMap(const QVariantMap& ");
            String _name_135 = this._cppExtensions.toName(dto_5);
            String _firstLower_45 = StringExtensions.toFirstLower(_name_135);
            _builder.append(_firstLower_45, "");
            _builder.append("Map,");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            _builder.append("const bool& useForeignProperties)");
            _builder.newLine();
            _builder.append("{");
            _builder.newLine();
            _builder.append("    ");
            String _name_136 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_136, "    ");
            _builder.append("* ");
            String _name_137 = this._cppExtensions.toName(dto_5);
            String _firstLower_46 = StringExtensions.toFirstLower(_name_137);
            _builder.append(_firstLower_46, "    ");
            _builder.append(" = new ");
            String _name_138 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_138, "    ");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            String _name_139 = this._cppExtensions.toName(dto_5);
            String _firstLower_47 = StringExtensions.toFirstLower(_name_139);
            _builder.append(_firstLower_47, "    ");
            _builder.append("->setParent(this);");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("if (useForeignProperties) {");
            _builder.newLine();
            _builder.append("        ");
            String _name_140 = this._cppExtensions.toName(dto_5);
            String _firstLower_48 = StringExtensions.toFirstLower(_name_140);
            _builder.append(_firstLower_48, "        ");
            _builder.append("->fillFromForeignMap(");
            String _name_141 = this._cppExtensions.toName(dto_5);
            String _firstLower_49 = StringExtensions.toFirstLower(_name_141);
            _builder.append(_firstLower_49, "        ");
            _builder.append("Map);");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("} else {");
            _builder.newLine();
            _builder.append("        ");
            String _name_142 = this._cppExtensions.toName(dto_5);
            String _firstLower_50 = StringExtensions.toFirstLower(_name_142);
            _builder.append(_firstLower_50, "        ");
            _builder.append("->fillFromMap(");
            String _name_143 = this._cppExtensions.toName(dto_5);
            String _firstLower_51 = StringExtensions.toFirstLower(_name_143);
            _builder.append(_firstLower_51, "        ");
            _builder.append("Map);");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("}");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("mAll");
            String _name_144 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_144, "    ");
            _builder.append(".append(");
            String _name_145 = this._cppExtensions.toName(dto_5);
            String _firstLower_52 = StringExtensions.toFirstLower(_name_145);
            _builder.append(_firstLower_52, "    ");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            {
              boolean _isTree_5 = this._cppExtensions.isTree(dto_5);
              if (_isTree_5) {
                _builder.append("    ");
                _builder.append("mAll");
                String _name_146 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_146, "    ");
                _builder.append("Flat.append(");
                String _name_147 = this._cppExtensions.toName(dto_5);
                String _firstLower_53 = StringExtensions.toFirstLower(_name_147);
                _builder.append(_firstLower_53, "    ");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("    ");
            _builder.append("emit addedToAll");
            String _name_148 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_148, "    ");
            _builder.append("(");
            String _name_149 = this._cppExtensions.toName(dto_5);
            String _firstLower_54 = StringExtensions.toFirstLower(_name_149);
            _builder.append(_firstLower_54, "    ");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
            _builder.newLine();
            _builder.append("bool DataManager::delete");
            String _name_150 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_150, "");
            _builder.append("(");
            String _name_151 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_151, "");
            _builder.append("* ");
            String _name_152 = this._cppExtensions.toName(dto_5);
            String _firstLower_55 = StringExtensions.toFirstLower(_name_152);
            _builder.append(_firstLower_55, "");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
            _builder.append("{");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("bool ok = false;");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("ok = mAll");
            String _name_153 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_153, "    ");
            _builder.append(".removeOne(");
            String _name_154 = this._cppExtensions.toName(dto_5);
            String _firstLower_56 = StringExtensions.toFirstLower(_name_154);
            _builder.append(_firstLower_56, "    ");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("if (!ok) {");
            _builder.newLine();
            _builder.append("        ");
            _builder.append("return ok;");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("}");
            _builder.newLine();
            {
              boolean _isTree_6 = this._cppExtensions.isTree(dto_5);
              if (_isTree_6) {
                _builder.append("    ");
                _builder.append("mAll");
                String _name_155 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_155, "    ");
                _builder.append("Flat.removeOne(");
                String _name_156 = this._cppExtensions.toName(dto_5);
                String _firstLower_57 = StringExtensions.toFirstLower(_name_156);
                _builder.append(_firstLower_57, "    ");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
              }
            }
            {
              boolean _hasUuid = this._cppExtensions.hasUuid(dto_5);
              if (_hasUuid) {
                _builder.append("    ");
                _builder.append("emit deletedFromAll");
                String _name_157 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_157, "    ");
                _builder.append("ByUuid(");
                String _name_158 = this._cppExtensions.toName(dto_5);
                String _firstLower_58 = StringExtensions.toFirstLower(_name_158);
                _builder.append(_firstLower_58, "    ");
                _builder.append("->uuid());");
                _builder.newLineIfNotEmpty();
              } else {
                boolean _hasDomainKey = this._cppExtensions.hasDomainKey(dto_5);
                if (_hasDomainKey) {
                  _builder.append("    ");
                  _builder.append("emit deletedFromAll");
                  String _name_159 = this._cppExtensions.toName(dto_5);
                  _builder.append(_name_159, "    ");
                  _builder.append("By");
                  String _domainKey_1 = this._cppExtensions.domainKey(dto_5);
                  String _firstUpper_13 = StringExtensions.toFirstUpper(_domainKey_1);
                  _builder.append(_firstUpper_13, "    ");
                  _builder.append("(");
                  String _name_160 = this._cppExtensions.toName(dto_5);
                  String _firstLower_59 = StringExtensions.toFirstLower(_name_160);
                  _builder.append(_firstLower_59, "    ");
                  _builder.append("->");
                  String _domainKey_2 = this._cppExtensions.domainKey(dto_5);
                  _builder.append(_domainKey_2, "    ");
                  _builder.append("());");
                  _builder.newLineIfNotEmpty();
                }
              }
            }
            _builder.append("    ");
            String _name_161 = this._cppExtensions.toName(dto_5);
            String _firstLower_60 = StringExtensions.toFirstLower(_name_161);
            _builder.append(_firstLower_60, "    ");
            _builder.append("->deleteLater();");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            String _name_162 = this._cppExtensions.toName(dto_5);
            String _firstLower_61 = StringExtensions.toFirstLower(_name_162);
            _builder.append(_firstLower_61, "    ");
            _builder.append(" = 0;");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("return ok;");
            _builder.newLine();
            _builder.append("}");
            _builder.newLine();
            _builder.newLine();
            {
              boolean _hasUuid_1 = this._cppExtensions.hasUuid(dto_5);
              if (_hasUuid_1) {
                _builder.append("bool DataManager::delete");
                String _name_163 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_163, "");
                _builder.append("ByUuid(const QString& uuid)");
                _builder.newLineIfNotEmpty();
                _builder.append("{");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("if (uuid.isNull() || uuid.isEmpty()) {");
                _builder.newLine();
                _builder.append("        ");
                _builder.append("qDebug() << \"cannot delete ");
                String _name_164 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_164, "        ");
                _builder.append(" from empty uuid\";");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                _builder.append("return false;");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("}");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("for (int i = 0; i < mAll");
                String _name_165 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_165, "    ");
                _builder.append(".size(); ++i) {");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                String _name_166 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_166, "        ");
                _builder.append("* ");
                String _name_167 = this._cppExtensions.toName(dto_5);
                String _firstLower_62 = StringExtensions.toFirstLower(_name_167);
                _builder.append(_firstLower_62, "        ");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                String _name_168 = this._cppExtensions.toName(dto_5);
                String _firstLower_63 = StringExtensions.toFirstLower(_name_168);
                _builder.append(_firstLower_63, "        ");
                _builder.append(" = (");
                String _name_169 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_169, "        ");
                _builder.append("*) mAll");
                String _name_170 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_170, "        ");
                _builder.append(".at(i);");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                _builder.append("if (");
                String _name_171 = this._cppExtensions.toName(dto_5);
                String _firstLower_64 = StringExtensions.toFirstLower(_name_171);
                _builder.append(_firstLower_64, "        ");
                _builder.append("->uuid() == uuid) {");
                _builder.newLineIfNotEmpty();
                _builder.append("            ");
                _builder.append("mAll");
                String _name_172 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_172, "            ");
                _builder.append(".removeAt(i);");
                _builder.newLineIfNotEmpty();
                {
                  boolean _isTree_7 = this._cppExtensions.isTree(dto_5);
                  if (_isTree_7) {
                    _builder.append("            ");
                    _builder.append("mAll");
                    String _name_173 = this._cppExtensions.toName(dto_5);
                    _builder.append(_name_173, "            ");
                    _builder.append("Flat.removeOne(");
                    String _name_174 = this._cppExtensions.toName(dto_5);
                    String _firstLower_65 = StringExtensions.toFirstLower(_name_174);
                    _builder.append(_firstLower_65, "            ");
                    _builder.append(");");
                    _builder.newLineIfNotEmpty();
                  }
                }
                _builder.append("            ");
                _builder.append("emit deletedFromAll");
                String _name_175 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_175, "            ");
                _builder.append("ByUuid(uuid);");
                _builder.newLineIfNotEmpty();
                _builder.append("            ");
                String _name_176 = this._cppExtensions.toName(dto_5);
                String _firstLower_66 = StringExtensions.toFirstLower(_name_176);
                _builder.append(_firstLower_66, "            ");
                _builder.append("->deleteLater();");
                _builder.newLineIfNotEmpty();
                _builder.append("            ");
                String _name_177 = this._cppExtensions.toName(dto_5);
                String _firstLower_67 = StringExtensions.toFirstLower(_name_177);
                _builder.append(_firstLower_67, "            ");
                _builder.append(" = 0;");
                _builder.newLineIfNotEmpty();
                _builder.append("            ");
                _builder.append("return true;");
                _builder.newLine();
                _builder.append("        ");
                _builder.append("}");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("}");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("return false;");
                _builder.newLine();
                _builder.append("}");
                _builder.newLine();
              }
            }
            _builder.newLine();
            {
              boolean _and = false;
              boolean _hasDomainKey_1 = this._cppExtensions.hasDomainKey(dto_5);
              if (!_hasDomainKey_1) {
                _and = false;
              } else {
                String _domainKey_3 = this._cppExtensions.domainKey(dto_5);
                boolean _notEquals = (!Objects.equal(_domainKey_3, "uuid"));
                _and = _notEquals;
              }
              if (_and) {
                _builder.append("bool DataManager::delete");
                String _name_178 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_178, "");
                _builder.append("By");
                String _domainKey_4 = this._cppExtensions.domainKey(dto_5);
                String _firstUpper_14 = StringExtensions.toFirstUpper(_domainKey_4);
                _builder.append(_firstUpper_14, "");
                _builder.append("(const ");
                String _domainKeyType = this._cppExtensions.domainKeyType(dto_5);
                _builder.append(_domainKeyType, "");
                _builder.append("& ");
                String _domainKey_5 = this._cppExtensions.domainKey(dto_5);
                _builder.append(_domainKey_5, "");
                _builder.append(")");
                _builder.newLineIfNotEmpty();
                _builder.append("{");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("for (int i = 0; i < mAll");
                String _name_179 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_179, "    ");
                _builder.append(".size(); ++i) {");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                String _name_180 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_180, "        ");
                _builder.append("* ");
                String _name_181 = this._cppExtensions.toName(dto_5);
                String _firstLower_68 = StringExtensions.toFirstLower(_name_181);
                _builder.append(_firstLower_68, "        ");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                String _name_182 = this._cppExtensions.toName(dto_5);
                String _firstLower_69 = StringExtensions.toFirstLower(_name_182);
                _builder.append(_firstLower_69, "        ");
                _builder.append(" = (");
                String _name_183 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_183, "        ");
                _builder.append("*) mAll");
                String _name_184 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_184, "        ");
                _builder.append(".at(i);");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                _builder.append("if (");
                String _name_185 = this._cppExtensions.toName(dto_5);
                String _firstLower_70 = StringExtensions.toFirstLower(_name_185);
                _builder.append(_firstLower_70, "        ");
                _builder.append("->");
                String _domainKey_6 = this._cppExtensions.domainKey(dto_5);
                _builder.append(_domainKey_6, "        ");
                _builder.append("() == ");
                String _domainKey_7 = this._cppExtensions.domainKey(dto_5);
                _builder.append(_domainKey_7, "        ");
                _builder.append(") {");
                _builder.newLineIfNotEmpty();
                _builder.append("            ");
                _builder.append("mAll");
                String _name_186 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_186, "            ");
                _builder.append(".removeAt(i);");
                _builder.newLineIfNotEmpty();
                {
                  boolean _isTree_8 = this._cppExtensions.isTree(dto_5);
                  if (_isTree_8) {
                    _builder.append("            ");
                    _builder.append("mAll");
                    String _name_187 = this._cppExtensions.toName(dto_5);
                    _builder.append(_name_187, "            ");
                    _builder.append("Flat.removeOne(");
                    String _name_188 = this._cppExtensions.toName(dto_5);
                    String _firstLower_71 = StringExtensions.toFirstLower(_name_188);
                    _builder.append(_firstLower_71, "            ");
                    _builder.append(");");
                    _builder.newLineIfNotEmpty();
                  }
                }
                _builder.append("            ");
                _builder.append("emit deletedFromAll");
                String _name_189 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_189, "            ");
                _builder.append("By");
                String _domainKey_8 = this._cppExtensions.domainKey(dto_5);
                String _firstUpper_15 = StringExtensions.toFirstUpper(_domainKey_8);
                _builder.append(_firstUpper_15, "            ");
                _builder.append("(");
                String _domainKey_9 = this._cppExtensions.domainKey(dto_5);
                _builder.append(_domainKey_9, "            ");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                _builder.append("            ");
                String _name_190 = this._cppExtensions.toName(dto_5);
                String _firstLower_72 = StringExtensions.toFirstLower(_name_190);
                _builder.append(_firstLower_72, "            ");
                _builder.append("->deleteLater();");
                _builder.newLineIfNotEmpty();
                _builder.append("            ");
                String _name_191 = this._cppExtensions.toName(dto_5);
                String _firstLower_73 = StringExtensions.toFirstLower(_name_191);
                _builder.append(_firstLower_73, "            ");
                _builder.append(" = 0;");
                _builder.newLineIfNotEmpty();
                _builder.append("            ");
                _builder.append("return true;");
                _builder.newLine();
                _builder.append("        ");
                _builder.append("}");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("}");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("return false;");
                _builder.newLine();
                _builder.append("}");
                _builder.newLine();
              }
            }
            _builder.newLine();
            {
              boolean _isTree_9 = this._cppExtensions.isTree(dto_5);
              if (_isTree_9) {
                _builder.append("void DataManager::fill");
                String _name_192 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_192, "");
                _builder.append("TreeDataModel(QString objectName)");
                _builder.newLineIfNotEmpty();
                _builder.append("{");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("GroupDataModel* dataModel = Application::instance()->scene()->findChild<GroupDataModel*>(");
                _builder.newLine();
                _builder.append("            ");
                _builder.append("objectName);");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("if (dataModel) {");
                _builder.newLine();
                _builder.append("        ");
                _builder.append("QList<QObject*> theList;");
                _builder.newLine();
                _builder.append("        ");
                _builder.append("for (int i = 0; i < mAll");
                String _name_193 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_193, "        ");
                _builder.append(".size(); ++i) {");
                _builder.newLineIfNotEmpty();
                _builder.append("            ");
                _builder.append("theList.append(mAll");
                String _name_194 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_194, "            ");
                _builder.append(".at(i));");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                _builder.append("}");
                _builder.newLine();
                _builder.append("        ");
                _builder.append("dataModel->clear();");
                _builder.newLine();
                _builder.append("        ");
                _builder.append("dataModel->insertList(theList);");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("} else {");
                _builder.newLine();
                _builder.append("        ");
                _builder.append("qDebug() << \"NO GRP DATA FOUND ");
                String _name_195 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_195, "        ");
                _builder.append(" for \" << objectName;");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("}");
                _builder.newLine();
                _builder.append("}");
                _builder.newLine();
                _builder.append("void DataManager::fill");
                String _name_196 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_196, "");
                _builder.append("FlatDataModel(QString objectName)");
                _builder.newLineIfNotEmpty();
                _builder.append("{");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("GroupDataModel* dataModel = Application::instance()->scene()->findChild<GroupDataModel*>(");
                _builder.newLine();
                _builder.append("            ");
                _builder.append("objectName);");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("if (dataModel) {");
                _builder.newLine();
                _builder.append("        ");
                _builder.append("QList<QObject*> theList;");
                _builder.newLine();
                _builder.append("        ");
                _builder.append("for (int i = 0; i < mAll");
                String _name_197 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_197, "        ");
                _builder.append("Flat.size(); ++i) {");
                _builder.newLineIfNotEmpty();
                _builder.append("            ");
                _builder.append("theList.append(mAll");
                String _name_198 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_198, "            ");
                _builder.append("Flat.at(i));");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                _builder.append("}");
                _builder.newLine();
                _builder.append("        ");
                _builder.append("dataModel->clear();");
                _builder.newLine();
                _builder.append("        ");
                _builder.append("dataModel->insertList(theList);");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("} else {");
                _builder.newLine();
                _builder.append("        ");
                _builder.append("qDebug() << \"NO GRP DATA FOUND ");
                String _name_199 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_199, "        ");
                _builder.append(" for \" << objectName;");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("}");
                _builder.newLine();
                _builder.append("}");
                _builder.newLine();
              } else {
                _builder.append("void DataManager::fill");
                String _name_200 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_200, "");
                _builder.append("DataModel(QString objectName)");
                _builder.newLineIfNotEmpty();
                _builder.append("{");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("GroupDataModel* dataModel = Application::instance()->scene()->findChild<GroupDataModel*>(");
                _builder.newLine();
                _builder.append("            ");
                _builder.append("objectName);");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("if (dataModel) {");
                _builder.newLine();
                _builder.append("        ");
                _builder.append("QList<QObject*> theList;");
                _builder.newLine();
                _builder.append("        ");
                _builder.append("for (int i = 0; i < mAll");
                String _name_201 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_201, "        ");
                _builder.append(".size(); ++i) {");
                _builder.newLineIfNotEmpty();
                _builder.append("            ");
                _builder.append("theList.append(mAll");
                String _name_202 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_202, "            ");
                _builder.append(".at(i));");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                _builder.append("}");
                _builder.newLine();
                _builder.append("        ");
                _builder.append("dataModel->clear();");
                _builder.newLine();
                _builder.append("        ");
                _builder.append("dataModel->insertList(theList);");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("} else {");
                _builder.newLine();
                _builder.append("        ");
                _builder.append("qDebug() << \"NO GRP DATA FOUND ");
                String _name_203 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_203, "        ");
                _builder.append(" for \" << objectName;");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("}");
                _builder.newLine();
                _builder.append("}");
                _builder.newLine();
              }
            }
            {
              boolean _hasUuid_2 = this._cppExtensions.hasUuid(dto_5);
              if (_hasUuid_2) {
                String _name_204 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_204, "");
                _builder.append("* DataManager::find");
                String _name_205 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_205, "");
                _builder.append("ByUuid(const QString& uuid){");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("if (uuid.isNull() || uuid.isEmpty()) {");
                _builder.newLine();
                _builder.append("        ");
                _builder.append("qDebug() << \"cannot find ");
                String _name_206 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_206, "        ");
                _builder.append(" from empty uuid\";");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                _builder.append("return 0;");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("}");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("for (int i = 0; i < mAll");
                String _name_207 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_207, "    ");
                {
                  boolean _isTree_10 = this._cppExtensions.isTree(dto_5);
                  if (_isTree_10) {
                    _builder.append("Flat");
                  }
                }
                _builder.append(".size(); ++i) {");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                String _name_208 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_208, "        ");
                _builder.append("* ");
                String _name_209 = this._cppExtensions.toName(dto_5);
                String _firstLower_74 = StringExtensions.toFirstLower(_name_209);
                _builder.append(_firstLower_74, "        ");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                String _name_210 = this._cppExtensions.toName(dto_5);
                String _firstLower_75 = StringExtensions.toFirstLower(_name_210);
                _builder.append(_firstLower_75, "        ");
                _builder.append(" = (");
                String _name_211 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_211, "        ");
                _builder.append("*)mAll");
                String _name_212 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_212, "        ");
                {
                  boolean _isTree_11 = this._cppExtensions.isTree(dto_5);
                  if (_isTree_11) {
                    _builder.append("Flat");
                  }
                }
                _builder.append(".at(i);");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                _builder.append("if(");
                String _name_213 = this._cppExtensions.toName(dto_5);
                String _firstLower_76 = StringExtensions.toFirstLower(_name_213);
                _builder.append(_firstLower_76, "        ");
                _builder.append("->uuid() == uuid){");
                _builder.newLineIfNotEmpty();
                _builder.append("            ");
                _builder.append("return ");
                String _name_214 = this._cppExtensions.toName(dto_5);
                String _firstLower_77 = StringExtensions.toFirstLower(_name_214);
                _builder.append(_firstLower_77, "            ");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                _builder.append("}");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("}");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("qDebug() << \"no ");
                String _name_215 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_215, "    ");
                _builder.append(" found for uuid \" << uuid;");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("return 0;");
                _builder.newLine();
                _builder.append("}");
                _builder.newLine();
              }
            }
            _builder.newLine();
            {
              boolean _and_1 = false;
              boolean _hasDomainKey_2 = this._cppExtensions.hasDomainKey(dto_5);
              if (!_hasDomainKey_2) {
                _and_1 = false;
              } else {
                String _domainKey_10 = this._cppExtensions.domainKey(dto_5);
                boolean _notEquals_1 = (!Objects.equal(_domainKey_10, "uuid"));
                _and_1 = _notEquals_1;
              }
              if (_and_1) {
                _builder.append("// nr is DomainKey");
                _builder.newLine();
                String _name_216 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_216, "");
                _builder.append("* DataManager::find");
                String _name_217 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_217, "");
                _builder.append("By");
                String _domainKey_11 = this._cppExtensions.domainKey(dto_5);
                String _firstUpper_16 = StringExtensions.toFirstUpper(_domainKey_11);
                _builder.append(_firstUpper_16, "");
                _builder.append("(const ");
                String _domainKeyType_1 = this._cppExtensions.domainKeyType(dto_5);
                _builder.append(_domainKeyType_1, "");
                _builder.append("& ");
                String _domainKey_12 = this._cppExtensions.domainKey(dto_5);
                _builder.append(_domainKey_12, "");
                _builder.append("){");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("for (int i = 0; i < mAll");
                String _name_218 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_218, "    ");
                {
                  boolean _isTree_12 = this._cppExtensions.isTree(dto_5);
                  if (_isTree_12) {
                    _builder.append("Flat");
                  }
                }
                _builder.append(".size(); ++i) {");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                String _name_219 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_219, "        ");
                _builder.append("* ");
                String _name_220 = this._cppExtensions.toName(dto_5);
                String _firstLower_78 = StringExtensions.toFirstLower(_name_220);
                _builder.append(_firstLower_78, "        ");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                String _name_221 = this._cppExtensions.toName(dto_5);
                String _firstLower_79 = StringExtensions.toFirstLower(_name_221);
                _builder.append(_firstLower_79, "        ");
                _builder.append(" = (");
                String _name_222 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_222, "        ");
                _builder.append("*)mAll");
                String _name_223 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_223, "        ");
                {
                  boolean _isTree_13 = this._cppExtensions.isTree(dto_5);
                  if (_isTree_13) {
                    _builder.append("Flat");
                  }
                }
                _builder.append(".at(i);");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                _builder.append("if(");
                String _name_224 = this._cppExtensions.toName(dto_5);
                String _firstLower_80 = StringExtensions.toFirstLower(_name_224);
                _builder.append(_firstLower_80, "        ");
                _builder.append("->");
                String _domainKey_13 = this._cppExtensions.domainKey(dto_5);
                _builder.append(_domainKey_13, "        ");
                _builder.append("() == ");
                String _domainKey_14 = this._cppExtensions.domainKey(dto_5);
                _builder.append(_domainKey_14, "        ");
                _builder.append("){");
                _builder.newLineIfNotEmpty();
                _builder.append("            ");
                _builder.append("return ");
                String _name_225 = this._cppExtensions.toName(dto_5);
                String _firstLower_81 = StringExtensions.toFirstLower(_name_225);
                _builder.append(_firstLower_81, "            ");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                _builder.append("}");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("}");
                _builder.newLine();
                _builder.append("    ");
                _builder.append("qDebug() << \"no ");
                String _name_226 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_226, "    ");
                _builder.append(" found for ");
                String _domainKey_15 = this._cppExtensions.domainKey(dto_5);
                _builder.append(_domainKey_15, "    ");
                _builder.append(" \" << ");
                String _domainKey_16 = this._cppExtensions.domainKey(dto_5);
                _builder.append(_domainKey_16, "    ");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("return 0;");
                _builder.newLine();
                _builder.append("}");
                _builder.newLine();
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
    _builder.append("* reads data in from stored cache");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* if no cache found tries to get data from assets/datamodel");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("QVariantList DataManager::readFromCache(QString& fileName)");
    _builder.newLine();
    _builder.append("{");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("JsonDataAccess jda;");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("QVariantList cacheList;");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("QFile dataFile(dataPath(fileName));");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("if (!dataFile.exists()) {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("QFile assetDataFile(dataAssetsPath(fileName));");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("if (assetDataFile.exists()) {");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("// copy file from assets to data");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("bool copyOk = assetDataFile.copy(dataPath(fileName));");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("if (!copyOk) {");
    _builder.newLine();
    _builder.append("                ");
    _builder.append("qDebug() << \"cannot copy dataAssetsPath(fileName) to dataPath(fileName)\";");
    _builder.newLine();
    _builder.append("                ");
    _builder.append("// no cache, no assets - empty list");
    _builder.newLine();
    _builder.append("                ");
    _builder.append("return cacheList;");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("} else {");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("// no cache, no assets - empty list");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("return cacheList;");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("cacheList = jda.load(dataPath(fileName)).toList();");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("return cacheList;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("void DataManager::writeToCache(QString& fileName, QVariantList& data)");
    _builder.newLine();
    _builder.append("{");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("QString filePath;");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("filePath = dataPath(fileName);");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("JsonDataAccess jda;");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("jda.save(data, filePath);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("void DataManager::onManualExit()");
    _builder.newLine();
    _builder.append("{");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("qDebug() << \"## DataManager ## MANUAL EXIT\";");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("finish();");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("bb::Application::instance()->exit(0);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("DataManager::~DataManager()");
    _builder.newLine();
    _builder.append("{");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// clean up");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}
