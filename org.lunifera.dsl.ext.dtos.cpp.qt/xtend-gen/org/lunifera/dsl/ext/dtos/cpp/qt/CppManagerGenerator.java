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
    return "DTOManager.cpp";
  }
  
  public CharSequence toContent(final LTypedPackage pkg) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("#include <QObject>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include \"DTOManager.hpp\"");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include <bb/cascades/Application>");
    _builder.newLine();
    _builder.append("#include <bb/cascades/AbstractPane>");
    _builder.newLine();
    _builder.append("#include <bb/data/JsonDataAccess>");
    _builder.newLine();
    _builder.append("#include  <bb/cascades/GroupDataModel>");
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
          boolean _isRootDTO = this._managerExtensions.isRootDTO(dto);
          if (_isRootDTO) {
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
    _builder.append("DTOManager::DTOManager(QObject *parent) :");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("QObject(parent)");
    _builder.newLine();
    _builder.append("{");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// ApplicationUI is parent of DTOManager");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// DTOManager is parent of all root DTOs");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("// ROOT DTOs are parent of contained DTOs");
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
          boolean _isRootDTO_1 = this._managerExtensions.isRootDTO(dto_1);
          if (_isRootDTO_1) {
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
    _builder.append("// register all DTOs to get access to properties from QML:\t");
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
        _builder.append(">(\"org.ekkescorner\", 1, 0, \"");
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
        _builder.append("::");
        String _name_8 = this._cppExtensions.toName(en);
        _builder.append(_name_8, "\t");
        _builder.append("Enum>(\"org.ekkescorner\", 1, 0, \"");
        String _name_9 = this._cppExtensions.toName(en);
        _builder.append(_name_9, "\t");
        _builder.append("Enum\");");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* loads all data from cache.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* tip: call from main.qml with delay using QTimer");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("void DTOManager::init()");
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
          boolean _isRootDTO_2 = this._managerExtensions.isRootDTO(dto_3);
          if (_isRootDTO_2) {
            _builder.append("    ");
            _builder.append("init");
            String _name_10 = this._cppExtensions.toName(dto_3);
            _builder.append(_name_10, "    ");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLine();
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
          boolean _isRootDTO_3 = this._managerExtensions.isRootDTO(dto_4);
          if (_isRootDTO_3) {
            _builder.append("/*");
            _builder.newLine();
            _builder.append(" ");
            _builder.append("* reads ");
            String _name_11 = this._cppExtensions.toName(dto_4);
            _builder.append(_name_11, " ");
            _builder.append("\'s in from stored cache");
            _builder.newLineIfNotEmpty();
            _builder.append(" ");
            _builder.append("* creates List of QObject* containing ");
            String _name_12 = this._cppExtensions.toName(dto_4);
            _builder.append(_name_12, " ");
            _builder.append("\'s");
            _builder.newLineIfNotEmpty();
            _builder.append(" ");
            _builder.append("*/");
            _builder.newLine();
            _builder.append("void DTOManager::init");
            String _name_13 = this._cppExtensions.toName(dto_4);
            _builder.append(_name_13, "");
            _builder.append("()");
            _builder.newLineIfNotEmpty();
            _builder.append("{");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("mAll");
            String _name_14 = this._cppExtensions.toName(dto_4);
            _builder.append(_name_14, "    ");
            _builder.append(".clear();");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("QVariantList cacheList;");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("cacheList = readCache(cache");
            String _name_15 = this._cppExtensions.toName(dto_4);
            _builder.append(_name_15, "    ");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("qDebug() << \"read ");
            String _name_16 = this._cppExtensions.toName(dto_4);
            _builder.append(_name_16, "    ");
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
            String _name_17 = this._cppExtensions.toName(dto_4);
            _builder.append(_name_17, "        ");
            _builder.append("* ");
            String _name_18 = this._cppExtensions.toName(dto_4);
            String _firstLower = StringExtensions.toFirstLower(_name_18);
            _builder.append(_firstLower, "        ");
            _builder.append(" = new ");
            String _name_19 = this._cppExtensions.toName(dto_4);
            _builder.append(_name_19, "        ");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            _builder.append("// Important: DTOManager must be parent of all root DTOs");
            _builder.newLine();
            _builder.append("        ");
            String _name_20 = this._cppExtensions.toName(dto_4);
            String _firstLower_1 = StringExtensions.toFirstLower(_name_20);
            _builder.append(_firstLower_1, "        ");
            _builder.append("->setParent(this);");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            String _name_21 = this._cppExtensions.toName(dto_4);
            String _firstLower_2 = StringExtensions.toFirstLower(_name_21);
            _builder.append(_firstLower_2, "        ");
            _builder.append("->fillFromMap(cacheMap);");
            _builder.newLineIfNotEmpty();
            _builder.append("        ");
            _builder.append("mAll");
            String _name_22 = this._cppExtensions.toName(dto_4);
            _builder.append(_name_22, "        ");
            _builder.append(".append(");
            String _name_23 = this._cppExtensions.toName(dto_4);
            String _firstLower_3 = StringExtensions.toFirstLower(_name_23);
            _builder.append(_firstLower_3, "        ");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("}");
            _builder.newLine();
            _builder.append("    ");
            _builder.append("qDebug() << \"created ");
            String _name_24 = this._cppExtensions.toName(dto_4);
            _builder.append(_name_24, "    ");
            _builder.append("* #\" << mAll");
            String _name_25 = this._cppExtensions.toName(dto_4);
            _builder.append(_name_25, "    ");
            _builder.append(".size();");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
            _builder.newLine();
            _builder.append("void DTOManager::fill");
            String _name_26 = this._cppExtensions.toName(dto_4);
            _builder.append(_name_26, "");
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
            _builder.append("if(dataModel){");
            _builder.newLine();
            _builder.append("        ");
            _builder.append("QList<QObject*> theList;");
            _builder.newLine();
            _builder.append("        ");
            _builder.append("for (int i = 0; i < mAll");
            String _name_27 = this._cppExtensions.toName(dto_4);
            _builder.append(_name_27, "        ");
            _builder.append(".size(); ++i) {");
            _builder.newLineIfNotEmpty();
            _builder.append("            ");
            _builder.append("theList.append(mAll");
            String _name_28 = this._cppExtensions.toName(dto_4);
            _builder.append(_name_28, "            ");
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
            String _name_29 = this._cppExtensions.toName(dto_4);
            _builder.append(_name_29, "        ");
            _builder.append(" for \" << objectName;");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append("}");
            _builder.newLine();
            _builder.append("}");
            _builder.newLine();
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
    _builder.append("QVariantList DTOManager::readCache(QString& fileName)");
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
    _builder.append("DTOManager::~DTOManager()");
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
