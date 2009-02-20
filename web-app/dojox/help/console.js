/*
	Copyright (c) 2004-2008, The Dojo Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
*/


if(!dojo._hasResource["dojox.help.console"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojox.help.console"] = true;
dojo.provide("dojox.help.console");
dojo.require("dojox.help._base");

dojo.mixin(dojox.help, {
	_plainText: function(str){
		return str.replace(/(<[^>]*>|&[^;]{2,6};)/g, '');
	},
	_displayLocated: function(located){
		var obj = {};
		dojo.forEach(located, function(item){ obj[item[0]] = (+dojo.isFF) ? { toString: function(){ return "Click to view"; }, item: item[1] } : item[1]; });
		
	},
	_displayHelp: function(loading, obj){
		if(loading){
			var message = "Help for: " + obj.name;
			
			var underline = "";
			for(var i = 0; i < message.length; i++){
				underline += "=";
			}
			
		}else if(!obj){
			
		}else{
			var anything = false;
			for(var attribute in obj){
				var value = obj[attribute];
				if(attribute == "returns" && obj.type != "Function" && obj.type != "Constructor"){
					continue;
				}
				if(value && (!dojo.isArray(value) || value.length)){
					anything = true;
					
					value = dojo.isString(value) ? dojox.help._plainText(value) : value;
					if(attribute == "returns"){
						var returns = dojo.map(value.types || [], "return item.title;").join("|");
						if(value.summary){
							if(returns){
								returns += ": ";
							}
							returns += dojox.help._plainText(value.summary);
						}
						
					}else if(attribute == "parameters"){
						for(var j = 0, parameter; parameter = value[j]; j++){
							var type = dojo.map(parameter.types, "return item.title").join("|");
							
							var summary = "";
							if(parameter.optional){
								summary += "Optional. ";
							}
							if(parameter.repating){
								summary += "Repeating. ";
							}
							summary += dojox.help._plainText(parameter.summary);
							if(summary){
								summary = "  - " + summary;
								for(var k = 0; k < parameter.name.length; k++){
									summary = " " + summary;
								}
								
							}
						}
					}else{
						
					}
				}
			}
			if(!anything){
				
			}
		}
	}
});

dojox.help.init();

}
