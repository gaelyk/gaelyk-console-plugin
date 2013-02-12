package org.gaelyk.console.script

import groovy.json.JsonBuilder

import org.gaelyk.console.ConsoleScriptRepository
import org.gaelyk.console.GaelykConsolePlugin


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