package org.gaelyk.console.script

import java.io.StringWriter;

import eu.appsatori.gaelyk.console.ConsolePluginGroovletSpec;

import spock.lang.Shared;

import groovy.json.JsonSlurper;
import groovy.lang.MetaClass
import groovyx.gaelyk.GaelykCategory;
import groovyx.gaelyk.spock.GaelykUnitSpec;
import groovyx.gaelyk.spock.GroovletUnderSpec;

class ExecutorGroovletSpec extends ConsolePluginGroovletSpec {

	String getGroovletName() { 'script/executor.groovy' }
	
	def "Executor works"(){
		when:
		
		groovletInstance.params = [text: "7+9"]
		
		use(GaelykCategory){
			groovletInstance.post()
		}
		
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
		
		use(GaelykCategory){
			groovletInstance.post()
		}
		
		def json = new JsonSlurper().parseText(sw.toString())
		
		then:
		1 * response.setContentType('application/json')
		json.exception in String
	}
	

}
