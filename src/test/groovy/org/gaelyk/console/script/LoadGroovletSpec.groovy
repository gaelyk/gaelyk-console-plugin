package org.gaelyk.console.script

import groovy.json.JsonSlurper
import groovyx.gaelyk.spock.GroovletUnderSpec

import org.gaelyk.console.ConsolePluginGroovletSpec
import org.gaelyk.console.ConsoleScriptRepository

class LoadGroovletSpec extends ConsolePluginGroovletSpec {

	
	String getGroovletName() {'script/load.groovy'}
	
	def "Loading works"(){
		when:
		ConsoleScriptRepository.save("name", "text", "first", "second")
		groovletInstance.params = [name: "name"]
		
		groovletInstance.get()
		
		def json = new JsonSlurper().parseText(sw.toString())
		
		then:
		1 * response.setContentType('application/json')
		json.size() == 4
		json.name == 'name'
		json.text == 'text'
		json.tags == ["first", "second"]
        json.war  == false
	}
    
    def "Loading from war works"(){
        when:
        groovletInstance.params = [name: "/src/main/resources/WEB-INF/org/example/gaelykconsole/test.groovy"]
        
        groovletInstance.get()
        
        def json = new JsonSlurper().parseText(sw.toString())
        
        then:
        1 * response.setContentType('application/json')
        json.size() == 4
        json.name == "/src/main/resources/WEB-INF/org/example/gaelykconsole/test.groovy"
        json.text == '"TEST"'
        json.tags == ["war"]
        json.war  == true
    }
	

}
