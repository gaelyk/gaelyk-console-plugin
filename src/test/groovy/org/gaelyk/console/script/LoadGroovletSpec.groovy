package org.gaelyk.console.script

import java.io.StringWriter;

import eu.appsatori.gaelyk.console.ConsolePluginGroovletSpec;
import eu.appsatori.gaelyk.console.ConsoleScriptRepository;

import groovy.json.JsonSlurper;
import groovy.lang.MetaClass
import groovyx.gaelyk.GaelykCategory;
import groovyx.gaelyk.spock.GaelykUnitSpec;
import groovyx.gaelyk.spock.GroovletUnderSpec;

class LoadGroovletSpec extends ConsolePluginGroovletSpec {

	
	String getGroovletName() {'script/load.groovy'}
	
	def "Loading works"(){
		when:
		ConsoleScriptRepository.save("name", "text", "first", "second")
		groovletInstance.params = [name: "name"]
		
		use(GaelykCategory){
			groovletInstance.get()
		}
		
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
        
        use(GaelykCategory){
            groovletInstance.get()
        }
        
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
