package org.gaelyk.console.script

import groovy.json.JsonSlurper
import groovyx.gaelyk.spock.GroovletUnderSpec

import org.gaelyk.console.ConsolePluginGroovletSpec
import org.gaelyk.console.ConsoleScriptRepository

class ListGroovletSpec extends ConsolePluginGroovletSpec {

	String getGroovletName() { 'script/list.groovy' }
	
	def "Listing works"(){
		when:
		ConsoleScriptRepository.save("name", "text", "first", "second")
		ConsoleScriptRepository.save("other", "text", "second", "third")
		
		groovletInstance.get()
		
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
		
		groovletInstance.get()
		
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
