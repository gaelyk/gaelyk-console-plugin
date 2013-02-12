package org.gaelyk.console.template

import groovy.json.JsonSlurper
import groovyx.gaelyk.spock.GroovletUnderSpec

import org.gaelyk.console.ConsolePluginGroovletSpec
import org.gaelyk.console.ConsoleScriptRepository
import org.gaelyk.console.ConsoleTemplateRepository

class DeleteGroovletSpec extends ConsolePluginGroovletSpec {


    String getGroovletName() {
        'template/delete.groovy'
    }

    def "Deletion works"(){
        when:
        ConsoleTemplateRepository.save("name", "script", "template", "first", "second")
        groovletInstance.params = [name: "name"]

        groovletInstance.get()

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

        groovletInstance.get()

        def json = new JsonSlurper().parseText(sw.toString())

        then:
        1 * response.setContentType('application/json')
        json.size() == 1
        json.exception
    }
}
