package org.gaelyk.console.template

import java.io.StringWriter

import eu.appsatori.gaelyk.console.ConsolePluginGroovletSpec
import eu.appsatori.gaelyk.console.ConsoleScriptRepository
import eu.appsatori.gaelyk.console.ConsoleTemplateRepository

import groovy.json.JsonSlurper
import groovy.lang.MetaClass
import groovyx.gaelyk.GaelykCategory
import groovyx.gaelyk.spock.GroovletUnderSpec

class DeleteGroovletSpec extends ConsolePluginGroovletSpec {


    String getGroovletName() {
        'template/delete.groovy'
    }

    def "Deletion works"(){
        when:
        ConsoleTemplateRepository.save("name", "script", "template", "first", "second")
        groovletInstance.params = [name: "name"]

        use(GaelykCategory){ groovletInstance.get() }

        def json = new JsonSlurper().parseText(sw.toString())

        then:
        !ConsoleScriptRepository.load("name")
        1 * response.setContentType('application/json')
        json.size() == 1
        json.deleted == true
    }
    
    def "Deletion doesn't work for gptl files"(){
        when:
        groovletInstance.params = [name: "/WEB-INF/org/example/gaelykconsole/test.gtpl"]

        use(GaelykCategory){ groovletInstance.get() }

        def json = new JsonSlurper().parseText(sw.toString())

        then:
        1 * response.setContentType('application/json')
        json.size() == 1
        json.exception
    }
}
