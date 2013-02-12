package org.gaelyk.console.template

import java.io.StringWriter;

import eu.appsatori.gaelyk.console.ConsolePluginGroovletSpec;

import spock.lang.Shared;

import groovy.json.JsonSlurper;
import groovy.lang.MetaClass
import groovyx.gaelyk.GaelykCategory;
import groovyx.gaelyk.spock.GaelykUnitSpec;
import groovyx.gaelyk.spock.GroovletUnderSpec;

class RendererGroovletSpec extends ConsolePluginGroovletSpec {

	String getGroovletName() { 'template/renderer.groovy' }
	
	def "Renerer works"(){
		when:
		
		groovletInstance.params = [template: '<html>Hello, ${request.name}</html>', script: 'request.name = "Idefix"']
		
		use(GaelykCategory){
			groovletInstance.post()
		}
		
        then:
        sw.toString() == '<html>Hello, Idefix</html>'
	}

	

}
