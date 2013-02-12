package org.gaelyk.console.template

import groovy.json.JsonSlurper
import groovyx.gaelyk.spock.GroovletUnderSpec

import org.gaelyk.console.ConsolePluginGroovletSpec
import org.gaelyk.console.ConsoleTemplateRepository

class LoadGroovletSpec extends ConsolePluginGroovletSpec {

	
	String getGroovletName() {'template/load.groovy'}
	
	def "Loading works"(){
		when:
		ConsoleTemplateRepository.save("name", "text", "template", "first", "second")
		groovletInstance.params = [name: "name"]
		
		groovletInstance.get()
		
		def json = new JsonSlurper().parseText(sw.toString())
		
		then:
		1 * response.setContentType('application/json')
		json.size() == 5
		json.name == 'name'
		json.script == 'text'
        json.template == 'template'
		json.tags == ["first", "second"]
        json.war == false
	}
    
    def "Loading existing template works"(){
        when:
        groovletInstance.params = [name: "/src/main/resources/WEB-INF/org/example/gaelykconsole/test.gtpl"]
        
        groovletInstance.get()
        
        def json = new JsonSlurper().parseText(sw.toString())
        
        then:
        1 * response.setContentType('application/json')
        json.size() == 5
        json.name == "/src/main/resources/WEB-INF/org/example/gaelykconsole/test.gtpl"
        json.script == ''
        json.template == 'TEST'
        json.tags == ["war"]
        json.war == true
    }
	

}
