package org.gaelyk.console.template

import groovy.json.JsonBuilder

import org.gaelyk.console.ConsoleTemplateRepository
import org.gaelyk.console.GaelykConsolePlugin


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