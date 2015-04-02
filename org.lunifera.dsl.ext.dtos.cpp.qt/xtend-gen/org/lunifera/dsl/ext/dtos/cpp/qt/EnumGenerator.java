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
import org.lunifera.dsl.ext.dtos.cpp.qt.CppExtensions;
import org.lunifera.dsl.semantic.common.types.LEnum;
import org.lunifera.dsl.semantic.common.types.LEnumLiteral;

@SuppressWarnings("all")
public class EnumGenerator {
  @Inject
  @Extension
  private CppExtensions _cppExtensions;
  
  public String toFileName(final LEnum en) {
    String _name = en.getName();
    return (_name + ".hpp");
  }
  
  public CharSequence toContent(final LEnum en) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("#ifndef ");
    String _name = this._cppExtensions.toName(en);
    String _upperCase = _name.toUpperCase();
    _builder.append(_upperCase, "");
    _builder.append("_HPP_");
    _builder.newLineIfNotEmpty();
    _builder.append("#define ");
    String _name_1 = this._cppExtensions.toName(en);
    String _upperCase_1 = _name_1.toUpperCase();
    _builder.append(_upperCase_1, "");
    _builder.append("_HPP_");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("#include <QObject>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("class ");
    String _name_2 = this._cppExtensions.toName(en);
    _builder.append(_name_2, "");
    _builder.append(": public QObject");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public:");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("static const int DEFAULT_VALUE = 0;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("static const int NO_VALUE = -1;");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// ");
    String _enumValues = this._cppExtensions.toEnumValues(en);
    _builder.append(_enumValues, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("enum ");
    String _name_3 = this._cppExtensions.toName(en);
    _builder.append(_name_3, "\t");
    _builder.append("Enum");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("{\t");
    _builder.newLine();
    {
      EList<LEnumLiteral> _literals = en.getLiterals();
      boolean _hasElements = false;
      for(final LEnumLiteral literal : _literals) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(", ", "\t\t");
        }
        _builder.append("\t\t");
        String _name_4 = literal.getName();
        _builder.append(_name_4, "\t\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("};");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Q_ENUMS (");
    String _name_5 = this._cppExtensions.toName(en);
    _builder.append(_name_5, "\t");
    _builder.append("Enum)");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("};");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#endif /* ");
    String _name_6 = this._cppExtensions.toName(en);
    String _upperCase_2 = _name_6.toUpperCase();
    _builder.append(_upperCase_2, "");
    _builder.append("_HPP_ */");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.newLine();
    return _builder;
  }
}
