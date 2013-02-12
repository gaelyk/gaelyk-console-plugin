package org.gaelyk.console.script

import groovy.json.JsonBuilder

import org.gaelyk.console.ConsoleScriptRepository

JsonBuilder json = []

response.contentType = 'application/json'


if(!users.userLoggedIn || !users.userAdmin){
	json([exception: "User must be admin!"])
	json.writeTo(out)
	return
}

String name = params.name

if(name == null){
	json([exception: "Name cannot be null!"])
	json.writeTo(out)
	return
}

if(name.endsWith('.groovy')){
    json([exception: "Name ending .groovy is confusing. You cannot save templates loaded from war directory!"])
    json.writeTo(out)
    return
}


String text = params.text

if(text == null){
	json([exception: "Value cannot be null!"])
	json.writeTo(out)
	return
}

def tags = params.tags ?: []
if(tags in String){
	tags = [tags]
}

try {
	ConsoleScriptRepository.save(name, text, tags as String[])
	json(ok:true)
} catch(IllegalArgumentException e){
	json(ok:false, exception: e.message)
}

json.writeTo(out)