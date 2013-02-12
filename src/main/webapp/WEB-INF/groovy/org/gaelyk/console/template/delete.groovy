package eu.appsatori.gaelyk.console.template

import eu.appsatori.gaelyk.console.ConsoleTemplateRepository
import groovy.json.JsonBuilder


JsonBuilder json = []
response.contentType = 'application/json'

if(!users.userLoggedIn || !users.userAdmin){
	json([deleted: false, exception:"User must be admin!"])
	json.writeTo(out)
	return
}

String name = params.name

if(!name){
	json([deleted: false, exception:"No such script"])
	json.writeTo(out)
	return
}

if(name.endsWith('.gtpl')){
    json([exception: "You cannot delete templates loaded from war directory!"])
    json.writeTo(out)
    return
}


def deleted = ConsoleTemplateRepository.delete(name)

json([deleted: true])
json.writeTo(out)