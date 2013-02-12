package org.gaelyk.console.template

import groovy.json.JsonSlurper
import groovyx.gaelyk.spock.GroovletUnderSpec

import org.gaelyk.console.ConsolePluginGroovletSpec
import org.gaelyk.console.ConsoleTemplateRepository

class SaveGroovletSpec extends ConsolePluginGroovletSpec {

	String getGroovletName() {'template/save.groovy'}
	
	def "Saving works"(){
		when:
		groovletInstance.params = [name: "name", tags: ["first", "second"], script: 'script', template: 'template']
        
		
		groovletInstance.post()
		
		def json = new JsonSlurper().parseText(sw.toString())
		
		then:
		1 * response.setContentType('application/json')
		json.size() == 1
		json.ok == true
		
		when:
		def template = ConsoleTemplateRepository.load("name")
		
		then:
		template.name == 'name'
		template.script == 'script'
        template.template == 'template'
		template.tags == ["first", "second"]
	}
    
    def "Saving fails for existing template"(){
        when:
        groovletInstance.params = [name: "/WEB-INF/org/example/gaelykconsole/test.gtpl", tags: ["war"], script: '', template: 'TEST']
        
        
        groovletInstance.post()
        
        def json = new JsonSlurper().parseText(sw.toString())
        
        then:
        1 * response.setContentType('application/json')
        json.size() == 1
        json.exception
        
    }

}
