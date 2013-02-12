package org.gaelyk.console.script

import groovy.json.JsonSlurper
import groovyx.gaelyk.spock.GroovletUnderSpec

import org.gaelyk.console.ConsolePluginGroovletSpec
import org.gaelyk.console.ConsoleScriptRepository

class SaveGroovletSpec extends ConsolePluginGroovletSpec {

	String getGroovletName() {'script/save.groovy'}
	
	def "Saving works"(){
		when:
		groovletInstance.params = [name: "name", tags: ["first", "second"], text: 'text']
        
		
		groovletInstance.post()
		
		def json = new JsonSlurper().parseText(sw.toString())
		
		then:
		1 * response.setContentType('application/json')
		json.size() == 1
		json.ok == true
		
		when:
		def script = ConsoleScriptRepository.load("name")
		
		then:
		script.name == 'name'
		script.text == 'text'
		script.tags == ["first", "second"]
	}
    
    def "Saving fails for existing groovlet"(){
        when:
        groovletInstance.params = [name: "/WEB-INF/org/example/gaelykconsole/test.groovy", tags: ["war"], text: 'TEST']
        
        
        groovletInstance.post()
        
        def json = new JsonSlurper().parseText(sw.toString())
        
        then:
        1 * response.setContentType('application/json')
        json.size() == 1
        json.exception
        
    }

}
