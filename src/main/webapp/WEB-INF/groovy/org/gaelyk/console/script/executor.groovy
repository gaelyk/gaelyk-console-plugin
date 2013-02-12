package eu.appsatori.gaelyk.console.script

import eu.appsatori.gaelyk.console.ConsoleScriptExecutor
import groovy.json.JsonBuilder

JsonBuilder json = []

response.contentType = 'application/json'

if(!users.userLoggedIn || !users.userAdmin){
	json([exception:"User must be admin!"])
	json.writeTo(out)
	return
}

String scriptText = params.text
String channel = params.channel ?: "ch" + new Random().nextLong()

if(scriptText == null){
	json([exception: "Script text cannot be null!"])
	json.writeTo(out)
	return
}


def results = ConsoleScriptExecutor.evaluate(channel, scriptText)

if(results.exception){
	StringWriter sw = []
	PrintWriter pw = [sw]
	results.exception.printStackTrace(pw)
	results.exception = sw.toString()
}


json results
json.writeTo(out)