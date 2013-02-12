package org.gaelyk.console.template

import groovy.json.JsonBuilder

import org.gaelyk.console.ConsoleTemplateRepository

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

if(name.endsWith('.gtpl')){
    json([exception: "Name ending .gtpl is confusing. You cannot save templates loaded from war directory!"])
    json.writeTo(out)
    return
}

String script = params.script ?: ''

String text = params.template

if(text == null){
	json([exception: "Template cannot be null!"])
	json.writeTo(out)
	return
}

def tags = params.tags ?: []
if(tags in String){
	tags = [tags]
}

try {
	ConsoleTemplateRepository.save(name, script, text, tags as String[])
	json(ok:true)
} catch(IllegalArgumentException e){
	json(ok:false, exception: e.message)
}

json.writeTo(out)