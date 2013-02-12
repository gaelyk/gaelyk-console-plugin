package eu.appsatori.gaelyk.console.template

import eu.appsatori.gaelyk.console.ConsoleTemplateRenderer


// because of webkit restrictions
// http://stackoverflow.com/questions/1547884/refused-to-execute-a-javascript-script-source-code-of-script-found-within-reque

response.setHeader('X-XSS-Protection', '0')

if(!users.userLoggedIn || !users.userAdmin){
	out << "User must be admin!"
	return
}

String scriptText = params.script ?: ''
String templateText = params.template

if(templateText == null){
    out << "<pre>"
    out << "Template text cannot be null!"
    out << "</pre>"
    return
}


def results = ConsoleTemplateRenderer.render(out, scriptText, templateText)