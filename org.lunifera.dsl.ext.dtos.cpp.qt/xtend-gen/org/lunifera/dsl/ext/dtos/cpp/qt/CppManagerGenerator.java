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
import org.lunifera.dsl.semantic.common.types.LEnum;
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
          boolean _isTree = this._managerExtensions.isTree(dto);
          if (_isTree) {
            _builder.append("// cache");
            String _name = this._cppExtensions.toName(dto);
            _builder.append(_name, "");
            _builder.append(" is tree of  ");
            String _name_1 = this._cppExtensions.toName(dto);
            _builder.append(_name_1, "");
            _builder.newLineIfNotEmpty();
            _builder.append("// there\'s also a plain list (in memory only) useful for easy filtering");
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
            _builder.append("    ");
            _builder.append("QVariantList cacheList;");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("cacheList = readFromCache(cache");
            String _name_16 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_16, "    ");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("qDebug() << \"read ");
            String _name_17 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_17, "    ");
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
            String _name_18 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_18, "        ");
            _builder.append("* ");
            String _name_19 = this._cppExtensions.toName(dto_5);
            String _firstLower = StringExtensions.toFirstLower(_name_19);
            _builder.append(_firstLower, "        ");
            _builder.append(" = new ");
            String _name_20 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_20, "        ");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            _builder.append("// Important: DataManager must be parent of all root DTOs");
            _builder.newLine();
            _builder.append("        ");
            String _name_21 = this._cppExtensions.toName(dto_5);
            String _firstLower_1 = StringExtensions.toFirstLower(_name_21);
            _builder.append(_firstLower_1, "        ");
            _builder.append("->setParent(this);");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            String _name_22 = this._cppExtensions.toName(dto_5);
            String _firstLower_2 = StringExtensions.toFirstLower(_name_22);
            _builder.append(_firstLower_2, "        ");
            _builder.append("->fillFromCacheMap(cacheMap);");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            _builder.append("mAll");
            String _name_23 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_23, "        ");
            _builder.append(".append(");
            String _name_24 = this._cppExtensions.toName(dto_5);
            String _firstLower_3 = StringExtensions.toFirstLower(_name_24);
            _builder.append(_firstLower_3, "        ");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("}");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("qDebug() << \"created ");
            String _name_25 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_25, "    ");
            _builder.append("* #\" << mAll");
            String _name_26 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_26, "    ");
            _builder.append(".size();");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
            _builder.newLine();
            _builder.append("/*");
            _builder.newLine();
            _builder.append(" ");
            _builder.append("* save List of ");
            String _name_27 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_27, " ");
            _builder.append("* to JSON cache");
            _builder.newLineIfNotEmpty();
            _builder.append(" ");
            _builder.append("* convert list of ");
            String _name_28 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_28, " ");
            _builder.append("* to QVariantList");
            _builder.newLineIfNotEmpty();
            _builder.append(" ");
            _builder.append("* toCacheMap stores all properties without transient values");
            _builder.newLine();
            {
              boolean _isReadOnlyCache_1 = this._cppExtensions.isReadOnlyCache(dto_5);
              if (_isReadOnlyCache_1) {
                _builder.append(" * ");
                String _name_29 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_29, "");
                _builder.append(" is read-only Cache - so it\'s not saved automatically at exit");
              }
            }
            _builder.newLineIfNotEmpty();
            _builder.append(" ");
            _builder.append("*/");
            _builder.newLine();
            _builder.append("void DataManager::save");
            String _name_30 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_30, "");
            _builder.append("ToCache()");
            _builder.newLineIfNotEmpty();
            _builder.append("{");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("QVariantList cacheList;");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("qDebug() << \"now caching ");
            String _name_31 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_31, "    ");
            _builder.append("* #\" << mAll");
            String _name_32 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_32, "    ");
            _builder.append(".size();");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("for (int i = 0; i < mAll");
            String _name_33 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_33, "    ");
            _builder.append(".size(); ++i) {");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            String _name_34 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_34, "        ");
            _builder.append("* ");
            String _name_35 = this._cppExtensions.toName(dto_5);
            String _firstLower_4 = StringExtensions.toFirstLower(_name_35);
            _builder.append(_firstLower_4, "        ");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            String _name_36 = this._cppExtensions.toName(dto_5);
            String _firstLower_5 = StringExtensions.toFirstLower(_name_36);
            _builder.append(_firstLower_5, "        ");
            _builder.append(" = (");
            String _name_37 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_37, "        ");
            _builder.append("*)mAll");
            String _name_38 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_38, "        ");
            _builder.append(".at(i);");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            _builder.append("QVariantMap cacheMap;");
            _builder.newLine();
            _builder.append("        ");
            _builder.append("cacheMap = ");
            String _name_39 = this._cppExtensions.toName(dto_5);
            String _firstLower_6 = StringExtensions.toFirstLower(_name_39);
            _builder.append(_firstLower_6, "        ");
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
            String _name_40 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_40, "    ");
            _builder.append("* converted to JSON cache #\" << cacheList.size();");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("writeToCache(cache");
            String _name_41 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_41, "    ");
            _builder.append(", cacheList);");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
            _builder.newLine();
            _builder.append("void DataManager::fill");
            String _name_42 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_42, "");
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
            String _name_43 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_43, "        ");
            _builder.append(".size(); ++i) {");
            _builder.newLineIfNotEmpty();
            _builder.append("            ");
            _builder.append("theList.append(mAll");
            String _name_44 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_44, "            ");
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
            String _name_45 = this._cppExtensions.toName(dto_5);
            _builder.append(_name_45, "        ");
            _builder.append(" for \" << objectName;");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("}");
            _builder.newLine();
            _builder.append("}");
            _builder.newLine();
            {
              boolean _hasUuid = this._cppExtensions.hasUuid(dto_5);
              if (_hasUuid) {
                String _name_46 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_46, "");
                _builder.append("* DataManager::find");
                String _name_47 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_47, "");
                _builder.append("ByUuid(const QString& uuid){");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("if (uuid.isNull() || uuid.isEmpty()) {");
                _builder.newLine();
                _builder.append("        ");
                _builder.append("qDebug() << \"cannot find ");
                String _name_48 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_48, "        ");
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
                String _name_49 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_49, "    ");
                _builder.append(".size(); ++i) {");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                String _name_50 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_50, "        ");
                _builder.append("* ");
                String _name_51 = this._cppExtensions.toName(dto_5);
                String _firstLower_7 = StringExtensions.toFirstLower(_name_51);
                _builder.append(_firstLower_7, "        ");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                String _name_52 = this._cppExtensions.toName(dto_5);
                String _firstLower_8 = StringExtensions.toFirstLower(_name_52);
                _builder.append(_firstLower_8, "        ");
                _builder.append(" = (");
                String _name_53 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_53, "        ");
                _builder.append("*)mAll");
                String _name_54 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_54, "        ");
                _builder.append(".at(i);");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                _builder.append("if(");
                String _name_55 = this._cppExtensions.toName(dto_5);
                String _firstLower_9 = StringExtensions.toFirstLower(_name_55);
                _builder.append(_firstLower_9, "        ");
                _builder.append("->uuid() == uuid){");
                _builder.newLineIfNotEmpty();
                _builder.append("            ");
                _builder.append("return ");
                String _name_56 = this._cppExtensions.toName(dto_5);
                String _firstLower_10 = StringExtensions.toFirstLower(_name_56);
                _builder.append(_firstLower_10, "            ");
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
                String _name_57 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_57, "    ");
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
              boolean _and = false;
              boolean _hasDomainKey = this._cppExtensions.hasDomainKey(dto_5);
              if (!_hasDomainKey) {
                _and = false;
              } else {
                String _domainKey = this._cppExtensions.domainKey(dto_5);
                boolean _notEquals = (!Objects.equal(_domainKey, "uuid"));
                _and = _notEquals;
              }
              if (_and) {
                _builder.append("// nr is DomainKey");
                _builder.newLine();
                String _name_58 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_58, "");
                _builder.append("* DataManager::find");
                String _name_59 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_59, "");
                _builder.append("By");
                String _domainKey_1 = this._cppExtensions.domainKey(dto_5);
                String _firstUpper = StringExtensions.toFirstUpper(_domainKey_1);
                _builder.append(_firstUpper, "");
                _builder.append("(const int& ");
                String _domainKey_2 = this._cppExtensions.domainKey(dto_5);
                _builder.append(_domainKey_2, "");
                _builder.append("){");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("for (int i = 0; i < mAll");
                String _name_60 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_60, "    ");
                _builder.append(".size(); ++i) {");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                String _name_61 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_61, "        ");
                _builder.append("* ");
                String _name_62 = this._cppExtensions.toName(dto_5);
                String _firstLower_11 = StringExtensions.toFirstLower(_name_62);
                _builder.append(_firstLower_11, "        ");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                String _name_63 = this._cppExtensions.toName(dto_5);
                String _firstLower_12 = StringExtensions.toFirstLower(_name_63);
                _builder.append(_firstLower_12, "        ");
                _builder.append(" = (");
                String _name_64 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_64, "        ");
                _builder.append("*)mAll");
                String _name_65 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_65, "        ");
                _builder.append(".at(i);");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                _builder.append("if(");
                String _name_66 = this._cppExtensions.toName(dto_5);
                String _firstLower_13 = StringExtensions.toFirstLower(_name_66);
                _builder.append(_firstLower_13, "        ");
                _builder.append("->");
                String _domainKey_3 = this._cppExtensions.domainKey(dto_5);
                _builder.append(_domainKey_3, "        ");
                _builder.append("() == ");
                String _domainKey_4 = this._cppExtensions.domainKey(dto_5);
                _builder.append(_domainKey_4, "        ");
                _builder.append("){");
                _builder.newLineIfNotEmpty();
                _builder.append("            ");
                _builder.append("return ");
                String _name_67 = this._cppExtensions.toName(dto_5);
                String _firstLower_14 = StringExtensions.toFirstLower(_name_67);
                _builder.append(_firstLower_14, "            ");
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
                String _name_68 = this._cppExtensions.toName(dto_5);
                _builder.append(_name_68, "    ");
                _builder.append(" found for ");
                String _domainKey_5 = this._cppExtensions.domainKey(dto_5);
                _builder.append(_domainKey_5, "    ");
                _builder.append(" \" << ");
                String _domainKey_6 = this._cppExtensions.domainKey(dto_5);
                _builder.append(_domainKey_6, "    ");
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
