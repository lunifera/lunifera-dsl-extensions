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
import org.lunifera.dsl.ext.dtos.cpp.qt.CppExtensions;
import org.lunifera.dsl.ext.dtos.cpp.qt.ManagerExtensions;
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
    _builder.append("// root DTOs are parent of contained DTOs");
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
          boolean _isRootDTO = this._managerExtensions.isRootDTO(dto);
          if (_isRootDTO) {
            _builder.append("// ROOT: ");
            String _name = this._cppExtensions.toName(dto);
            _builder.append(_name, "");
            _builder.newLineIfNotEmpty();
          }
        }
        {
          boolean _isTree = this._managerExtensions.isTree(dto);
          if (_isTree) {
            _builder.append("// TREE: ");
            String _name_1 = this._cppExtensions.toName(dto);
            _builder.append(_name_1, "");
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
        _builder.append("\t");
        _builder.append("qmlRegisterType<");
        String _name_2 = this._cppExtensions.toName(dto_1);
        _builder.append(_name_2, "\t");
        _builder.append(">(\"org.ekkescorner\", 1, 0, \"");
        String _name_3 = this._cppExtensions.toName(dto_1);
        _builder.append(_name_3, "\t");
        _builder.append("\");");
        _builder.newLineIfNotEmpty();
      }
    }
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
