package org.gaelyk.console

import com.google.appengine.api.NamespaceManager
import com.google.appengine.api.datastore.Category
import com.google.appengine.api.datastore.DatastoreServiceFactory
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.EntityNotFoundException
import com.google.appengine.api.datastore.FetchOptions
import com.google.appengine.api.datastore.Key
import com.google.appengine.api.datastore.KeyFactory
import com.google.appengine.api.datastore.PreparedQuery
import com.google.appengine.api.datastore.Query
import com.google.appengine.api.datastore.Text
import com.google.appengine.api.datastore.Query.FilterOperator

class ConsoleTemplateRepository {

	static final String KIND = 'template'
	static final String SCRIPT = 'script'
    static final String TEMPLATE = 'template'
	static final String TAGS = 'tags'
	static final String NAMESPACE = 'gaelyk_console'

	static Key save(String name, String scriptText, String templateText, String... tags){
		if(!((name+tags.join('')) =~ /[a-zA-Z0-9_\- ]/)){
			throw new IllegalArgumentException("Only letters, numbers, underscore, tilda and space is allowed as name or tag")
		}
		def oldNs = NamespaceManager.get()
		NamespaceManager.set(NAMESPACE)
		try {
			Entity en = new Entity(KIND, name);
			en.setUnindexedProperty(SCRIPT, new Text(scriptText))
            en.setUnindexedProperty(TEMPLATE, new Text(templateText))
			en.setProperty(TAGS, tags.collect{ new Category(it)} ?: [])
			return DatastoreServiceFactory.getDatastoreService().put(en);
		} finally {
			NamespaceManager.set(oldNs)
		}
	}
	
	static load(String name){
        if(name.endsWith('.gtpl')){
            File file = new File('.' + name)
            if(!file.exists()) {
                return null
            }
            return [name: name, script: '', template: file.text, tags: ['war'], war: true]
        }
		def oldNs = NamespaceManager.get()
		NamespaceManager.set(NAMESPACE)
		Entity template = null
		try {
			template = DatastoreServiceFactory.datastoreService.get(KeyFactory.createKey(KIND, name))
		} catch(EntityNotFoundException e) {
			// template is null
		} finally {
			NamespaceManager.set(oldNs)
		}
		asConsoleTemplate(template)
	}
	
	static void delete (String name){
		def oldNs = NamespaceManager.get()
		NamespaceManager.set(NAMESPACE)
		Entity template = null
		try {
			DatastoreServiceFactory.datastoreService.delete(KeyFactory.createKey(KIND, name))
		} finally {
			NamespaceManager.set(oldNs)
		}
	}
	
	static list(String... tags){
		def oldNs = NamespaceManager.get()
		NamespaceManager.set(NAMESPACE)
		try {
			Query q = new Query(KIND)
			for(tag in tags){
				q.addFilter(TAGS, FilterOperator.EQUAL, tag)
			}
			PreparedQuery pq = DatastoreServiceFactory.datastoreService.prepare(q);
			pq.asList(FetchOptions.Builder.withDefaults()).collect{ asConsoleTemplate(it, true)}
		} finally {
			NamespaceManager.set(oldNs)
		}
	}

	private static asConsoleTemplate(Entity template, boolean excludeText = false) {
		if(!template){
			return null;
		}
		def ret = [name: template.key.name]
        if(!excludeText){
            ret.script = template.getProperty(SCRIPT)?.value
            ret.template = template.getProperty(TEMPLATE)?.value           
        }
		ret.tags = template.getProperty(TAGS)*.category
        ret.war = false
		ret
	}
	
	
}
