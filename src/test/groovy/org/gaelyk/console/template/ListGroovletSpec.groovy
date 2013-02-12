package org.gaelyk.console.template

import groovy.json.JsonSlurper
import groovyx.gaelyk.spock.GroovletUnderSpec

import org.gaelyk.console.ConsolePluginGroovletSpec
import org.gaelyk.console.ConsoleTemplateRepository

class ListGroovletSpec extends ConsolePluginGroovletSpec {

	String getGroovletName() { 'template/list.groovy' }
	
	def "Listing works"(){
		when:
		ConsoleTemplateRepository.save("name", "text", "template", "first", "second")
		ConsoleTemplateRepository.save("other", "text", "template", "second", "third")
		
		groovletInstance.get()
		
		def json = new JsonSlurper().parseText(sw.toString())
		
		then:
		1 * response.setContentType('application/json')
		json.size() > 3
		json.any{ it.name == "name" }
		json.any{ it.name == "other" }
        json.any { it.name.endsWith '/WEB-INF/org/example/gaelykconsole/test.gtpl'}
	}
	
	def "Listing by tag"(){
		when:
		ConsoleTemplateRepository.save("name", "text", "template", "first", "second")
		ConsoleTemplateRepository.save("other", "text", "template", "second", "third")
		
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
