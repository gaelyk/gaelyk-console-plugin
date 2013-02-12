
package org.gaelyk.console

import spock.lang.Specification

import com.google.appengine.api.NamespaceManager
import com.google.appengine.api.datastore.Category
import com.google.appengine.api.datastore.DatastoreService
import com.google.appengine.api.datastore.DatastoreServiceFactory
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.EntityNotFoundException
import com.google.appengine.api.datastore.Key
import com.google.appengine.api.datastore.KeyFactory
import com.google.appengine.api.datastore.Text
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig
import com.google.appengine.tools.development.testing.LocalServiceTestHelper

class ConsoleTemplateRepositorySpec extends Specification {

	private LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig()
	)
	
	def setup() {
		helper.setUp()
	}

	def cleanup() {
		helper.tearDown()
	}
	
	def 'Create or update template file if not exist'(){
		NamespaceManager.set('gaelyk_console')
		Key key = KeyFactory.createKey('template', 'firstTemplate')
		DatastoreService ds = DatastoreServiceFactory.datastoreService

		when:
		ds.get(key)
		
		then:
		thrown(EntityNotFoundException)
		
		when:
		Key newKey = ConsoleTemplateRepository.save('firstTemplate', scriptText, templateText, 'first', 'template')
		
		
		then:
		key.namespace == 'gaelyk_console'
		key.kind == 'template'
		key.name == 'firstTemplate'
		
		when:
		Entity template = ds.get(key)
		
		then:
		template
		template.getProperty('script') instanceof Text
        template.getProperty('template') instanceof Text
		template.getProperty('tags') == ['first', 'template'].collect{ new Category(it) }
		
		where:
		scriptText << ['A', 'a'*600]
        templateText << ['A', 'a'*600]
	}
	
	def 'Load script by name'(){
		ConsoleTemplateRepository.save('firstTemplate', '//nothing', 'Hello world!', 'first', 'template')
		
		expect:
		!ConsoleTemplateRepository.load('secondTemplate')
		
		when:
		def template = ConsoleTemplateRepository.load('firstTemplate')
		
		then:
		template.name == 'firstTemplate'
		template.script == '//nothing'
        template.template == 'Hello world!'
		template.tags == ['first', 'template']
	}
	
	def 'Delete script'(){
		ConsoleTemplateRepository.save('firstTemplate', '//nothing', 'Hello world!', 'first', 'template')
		ConsoleTemplateRepository.delete('firstTemplate')
		
		expect:
		!ConsoleTemplateRepository.load('firstTemplate')

	}
	
	def "List by tag"(){
		ConsoleTemplateRepository.save('firstTemplate', '//nothing', 'Hello world!', 'first', 'template')
		ConsoleTemplateRepository.save('secondTemplate', '//nothing', 'Hello world!', 'second', 'template')
		ConsoleTemplateRepository.save('thirdTemplate', '//nothing', 'Hello world!', 'first', 'blah')
		
		expect:
		ConsoleTemplateRepository.list("template").size() == 2
		ConsoleTemplateRepository.list("first").size() == 2
		ConsoleTemplateRepository.list("first", "template").size() == 1
		
		when:
		def template = ConsoleTemplateRepository.list("first", "template").first()
		
		then:
		template.name == 'firstTemplate'
		template.tags == ['first', 'template']
	}
}
