
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

class ConsoleScriptRepositorySpec extends Specification {

	private LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig()
	)
	
	def setup() {
		helper.setUp()
	}

	def cleanup() {
		helper.tearDown()
	}
	
	def 'Create or update script file if not exist'(){
		NamespaceManager.set('gaelyk_console')
		Key key = KeyFactory.createKey('script', 'firstScript')
		DatastoreService ds = DatastoreServiceFactory.datastoreService

		when:
		ds.get(key)
		
		then:
		thrown(EntityNotFoundException)
		
		when:
		Key newKey = ConsoleScriptRepository.save('firstScript', scriptText, 'first', 'script')
		
		
		then:
		key.namespace == 'gaelyk_console'
		key.kind == 'script'
		key.name == 'firstScript'
		
		when:
		Entity script = ds.get(key)
		
		then:
		script
		script.getProperty('text') instanceof Text
		script.getProperty('tags') == ['first', 'script'].collect{ new Category(it) }
		
		where:
		scriptText << ['A', 'a'*600]
	}
	
	def 'Load script by name'(){
		ConsoleScriptRepository.save('firstScript', '"Hello World!"', 'first', 'script')
		
		expect:
		!ConsoleScriptRepository.load('secondScript')
		
		when:
		def script = ConsoleScriptRepository.load('firstScript')
		
		then:
		script.name == 'firstScript'
		script.text == '"Hello World!"'
		script.tags == ['first', 'script']
	}
	
	def 'Delete script'(){
		ConsoleScriptRepository.save('firstScript', '"Hello World!"', 'first', 'script')
		ConsoleScriptRepository.delete('firstScript')
		
		expect:
		!ConsoleScriptRepository.load('firstScript')

	}
	
	def "List by tag"(){
		ConsoleScriptRepository.save('firstScript', '"Hello World!"', 'first', 'script')
		ConsoleScriptRepository.save('secondScript', '"Hello World!"', 'second', 'script')
		ConsoleScriptRepository.save('thirdScript', '"Hello World!"', 'first', 'blah')
		
		expect:
		ConsoleScriptRepository.list("script").size() == 2
		ConsoleScriptRepository.list("first").size() == 2
		ConsoleScriptRepository.list("first", "script").size() == 1
		
		when:
		def script = ConsoleScriptRepository.list("first", "script").first()
		
		then:
		script.name == 'firstScript'
		script.tags == ['first', 'script']
	}
}
