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
public class HppManagerGenerator {
  @Inject
  @Extension
  private CppExtensions _cppExtensions;
  
  @Inject
  @Extension
  private ManagerExtensions _managerExtensions;
  
  public String toFileName(final LTypedPackage pkg) {
    return "DTOManager.hpp";
  }
  
  public CharSequence toContent(final LTypedPackage pkg) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("#ifndef DTOMANAGER_HPP_");
    _builder.newLine();
    _builder.append("#define DTOMANAGER_HPP_");
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
    _builder.append("class DTOManager: public QObject");
    _builder.newLine();
    _builder.append("{");
    _builder.newLine();
    _builder.append("Q_OBJECT");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public:");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("DTOManager(QObject *parent = 0);");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("virtual ~DTOManager();");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("Q_INVOKABLE");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("void init();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("private:");
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
          boolean _isRootDTO = this._managerExtensions.isRootDTO(dto_1);
          if (_isRootDTO) {
            _builder.append("    ");
            _builder.append("QList<QObject*> mAll");
            String _name_1 = this._cppExtensions.toName(dto_1);
            _builder.append(_name_1, "    ");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
        {
          boolean _isTree = this._managerExtensions.isTree(dto_1);
          if (_isTree) {
            _builder.append("    ");
            _builder.append("QList<QObject*> mAll");
            String _name_2 = this._cppExtensions.toName(dto_1);
            _builder.append(_name_2, "    ");
            _builder.append("asTree;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
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
          boolean _isRootDTO_1 = this._managerExtensions.isRootDTO(dto_2);
          if (_isRootDTO_1) {
            _builder.append("    ");
            _builder.append("void init");
            String _name_3 = this._cppExtensions.toName(dto_2);
            _builder.append(_name_3, "    ");
            _builder.append("();");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.newLine();
    _builder.append("\t");
    _builder.append("QVariantList readCache(QString& fileName);");
    _builder.newLine();
    _builder.append("};");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#endif /* DTOMANAGER_HPP_ */");
    _builder.newLine();
    return _builder;
  }
}
