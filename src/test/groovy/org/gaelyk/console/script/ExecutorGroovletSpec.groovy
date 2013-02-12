package org.gaelyk.console.script

import groovy.json.JsonSlurper
import groovyx.gaelyk.spock.GroovletUnderSpec

import org.gaelyk.console.ConsolePluginGroovletSpec

class ExecutorGroovletSpec extends ConsolePluginGroovletSpec {

	String getGroovletName() { 'script/executor.groovy' }
	
	def "Executor works"(){
		when:
		
		groovletInstance.params = [text: "7+9"]
		
		groovletInstance.post()
		
		def json = new JsonSlurper().parseText(sw.toString())
		
		then:
		1 * response.setContentType('application/json')
		json.size() == 2
		json.result == '16'
		!json.exception
		!json.output
	}
	
	def "Exception is stack trace"(){
		when:
		
		groovletInstance.params = [text: "xxx"]
		
		groovletInstance.post()
		
		def json = new JsonSlurper().parseText(sw.toString())
		
		then:
		1 * response.setContentType('application/json')
		json.exception in String
	}
	

}
