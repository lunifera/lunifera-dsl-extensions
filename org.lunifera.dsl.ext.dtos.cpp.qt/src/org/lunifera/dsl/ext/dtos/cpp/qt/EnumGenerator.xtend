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
package org.lunifera.dsl.ext.dtos.cpp.qt

import org.lunifera.dsl.semantic.common.types.LEnum

class EnumGenerator {	
	
	def String toFileName(LEnum en) {
		en.name + ".hpp"
	}
	
	def CharSequence toContent(LEnum en) '''
	#ifndef «en.name.toUpperCase»_HPP_
	#define «en.name.toUpperCase»_HPP_

	#include <QObject>
	
	class «en.name»: public QObject
	{
	
	public:
		«en.name»();
		virtual ~«en.name»()
		{
		}
		
		enum «en.name»Enum
		{	
			«FOR literal : en.literals SEPARATOR ", "»
			«literal.name»
			«ENDFOR»
		};
		Q_ENUMS («en.name»Enum)
		
		«en.name»(QObject *parent = 0);
	
};
	
#endif /* «en.name.toUpperCase»_HPP_ */
	
	
	'''
}