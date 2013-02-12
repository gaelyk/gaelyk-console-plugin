package org.gaelyk.console.template

import java.io.StringWriter;

import eu.appsatori.gaelyk.console.ConsolePluginGroovletSpec;
import eu.appsatori.gaelyk.console.ConsoleTemplateRepository;

import groovy.json.JsonSlurper;
import groovy.lang.MetaClass
import groovyx.gaelyk.GaelykCategory;
import groovyx.gaelyk.spock.GaelykUnitSpec;
import groovyx.gaelyk.spock.GroovletUnderSpec;

class LoadGroovletSpec extends ConsolePluginGroovletSpec {

	
	String getGroovletName() {'template/load.groovy'}
	
	def "Loading works"(){
		when:
		ConsoleTemplateRepository.save("name", "text", "template", "first", "second")
		groovletInstance.params = [name: "name"]
		
		use(GaelykCategory){
			groovletInstance.get()
		}
		
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
        
        use(GaelykCategory){
            groovletInstance.get()
        }
        
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
