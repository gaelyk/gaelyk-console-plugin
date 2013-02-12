package eu.appsatori.gaelyk.console.script

import eu.appsatori.gaelyk.console.ConsoleScriptRepository
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

def script = ConsoleScriptRepository.load(name)

if(!script){
	json [:]
	json.writeTo(out)
	return
}


json script
json.writeTo(out)