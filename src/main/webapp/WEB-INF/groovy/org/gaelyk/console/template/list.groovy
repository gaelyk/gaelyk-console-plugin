package eu.appsatori.gaelyk.console.template

import eu.appsatori.gaelyk.console.ConsoleTemplateRepository
import groovy.io.FileType;
import groovy.json.JsonBuilder


JsonBuilder json = []
response.contentType = 'application/json'

if(!users.userLoggedIn || !users.userAdmin){
	json([])
	json.writeTo(out)
	return
}

def tags = params.tags ?: []
if(tags in String){
	tags = [tags]
}


def templates = new ArrayList(ConsoleTemplateRepository.list(tags as String[]))

if(!tags || 'war' in tags){ 
    File root = new File('.')
    root.listFiles().each {
        if(it.name == '_ah'){
            return
        }
        if(it.directory){
            it.eachFileRecurse {
                if(it.name.endsWith('.gtpl')){
                    templates << [name: it.absolutePath[root.absolutePath.length()..-1], tags: ['war']]
                }                
            }
        } else {
            if(it.name.endsWith('.gtpl')){
                templates << [name: it.absolutePath[root.absolutePath.length()..-1], tags: ['war']]
            }
        }
    }
}

json templates
json.writeTo(out)