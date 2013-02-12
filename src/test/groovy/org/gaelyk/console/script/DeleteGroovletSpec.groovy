package org.gaelyk.console.script

import groovy.json.JsonSlurper
import groovyx.gaelyk.spock.GroovletUnderSpec

import org.gaelyk.console.ConsolePluginGroovletSpec
import org.gaelyk.console.ConsoleScriptRepository

class DeleteGroovletSpec extends ConsolePluginGroovletSpec {

	
	String getGroovletName() {'script/delete.groovy'}
	
	def "Deletion works"(){
		when:
		ConsoleScriptRepository.save("name", "text", "first", "second")
		groovletInstance.params = [name: "name"]
		
		groovletInstance.get()
		
		def json = new JsonSlurper().parseText(sw.toString())
		
		then:
		!ConsoleScriptRepository.load("name")
		1 * response.setContentType('application/json')
		json.size() == 1
		json.deleted == true
	}
    
    def "Deletion doesn't  work for groovlets in war"(){
        when:
        groovletInstance.params = [name: "/WEB-INF/org/example/gaelykconsole/test.groovy"]
        
        groovletInstance.get()
        
        def json = new JsonSlurper().parseText(sw.toString())
        
        then:
        1 * response.setContentType('application/json')
        json.size() == 1
        json.exception
    }
    
    
	

}
