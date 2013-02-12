package org.gaelyk.console.script

import java.io.StringWriter;

import eu.appsatori.gaelyk.console.ConsolePluginGroovletSpec;
import eu.appsatori.gaelyk.console.ConsoleScriptRepository;

import groovy.json.JsonSlurper;
import groovy.lang.MetaClass
import groovyx.gaelyk.GaelykCategory;
import groovyx.gaelyk.spock.GaelykUnitSpec;
import groovyx.gaelyk.spock.GroovletUnderSpec;

class ListGroovletSpec extends ConsolePluginGroovletSpec {

	String getGroovletName() { 'script/list.groovy' }
	
	def "Listing works"(){
		when:
		ConsoleScriptRepository.save("name", "text", "first", "second")
		ConsoleScriptRepository.save("other", "text", "second", "third")
		
		use(GaelykCategory){
			groovletInstance.get()
		}
		
		def json = new JsonSlurper().parseText(sw.toString())
		
		then:
		1 * response.setContentType('application/json')
		json.size() > 3
		json.any{ it.name == "name" }
		json.any{ it.name == "other" }
        json.any{ it.name.endsWith "/WEB-INF/org/example/gaelykconsole/test.groovy" }
	}
	
	def "Listing by tag"(){
		when:
		ConsoleScriptRepository.save("name", "text", "first", "second")
		ConsoleScriptRepository.save("other", "text", "second", "third")
		
		groovletInstance.params = [tags: "third"]
		
		use(GaelykCategory){
			groovletInstance.get()
		}
		
		def json = new JsonSlurper().parseText(sw.toString())
		
		then:
		1 * response.setContentType('application/json')
		json.size() == 1
		!json.any{ it.name == "name" }
		json.any{ it.name == "other" }
		
		where:
		tags << ["third", ["second", "third"]]
	}
	

}
