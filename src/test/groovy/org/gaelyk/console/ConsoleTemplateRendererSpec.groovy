package org.gaelyk.console

import org.gaelyk.console.ConsoleTemplateRenderer;

import groovy.lang.Binding;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalFileServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalImagesServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMailServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;
import com.google.appengine.tools.development.testing.LocalURLFetchServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalXMPPServiceTestConfig;

import spock.lang.Specification

class ConsoleTemplateRendererSpec extends Specification {
	
    // setup the local environment stub services
    private LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig(),
            new LocalMemcacheServiceTestConfig(),
            new LocalURLFetchServiceTestConfig(),
            new LocalMailServiceTestConfig(),
            new LocalImagesServiceTestConfig(),
            new LocalUserServiceTestConfig(),
            new LocalTaskQueueTestConfig(),
            new LocalXMPPServiceTestConfig(),
            new LocalBlobstoreServiceTestConfig(),
            new LocalFileServiceTestConfig()
    )

    def setup() {
        helper.setUp()
    }

    def cleanup() {
        helper.tearDown()
    }
    
    
    def "Simple rendering"(){
        StringWriter sw = []
        PrintWriter out = [sw]
        ConsoleTemplateRenderer.render out, 'request.name = "Idefix"', '<html>Hello, ${request.name}</html>'
        expect:
        sw.toString() == '<html>Hello, Idefix</html>'
        
    }
}
