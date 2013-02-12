package eu.appsatori.gaelyk.console.template

import eu.appsatori.gaelyk.console.ConsoleTemplateRepository
import eu.appsatori.gaelyk.console.GaelykConsolePlugin;
import groovy.json.JsonBuilder


JsonBuilder json = []
response.contentType = 'application/json'

String name = params.name

if(!name || (!GaelykConsolePlugin.allowReadOnly && (!users.userLoggedIn || !users.userAdmin))){
	json [:]
	json.writeTo(out)
	return
}

def template = ConsoleTemplateRepository.load(name)

if(!template){
	json [:]
	json.writeTo(out)
	return
}


json template
json.writeTo(out)