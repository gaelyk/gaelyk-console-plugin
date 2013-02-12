package org.gaelyk.console.template

import groovyx.gaelyk.spock.GroovletUnderSpec

import org.gaelyk.console.ConsolePluginGroovletSpec

class RendererGroovletSpec extends ConsolePluginGroovletSpec {

	String getGroovletName() { 'template/renderer.groovy' }
	
	def "Renerer works"(){
		when:
		
		groovletInstance.params = [template: '<html>Hello, ${request.name}</html>', script: 'request.name = "Idefix"']
		
		groovletInstance.post()
		
        then:
        sw.toString() == '<html>Hello, Idefix</html>'
	}

	

}
