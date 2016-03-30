package org.gaelyk.console

import spock.lang.Specification

import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig
import com.google.appengine.tools.development.testing.LocalFileServiceTestConfig
import com.google.appengine.tools.development.testing.LocalImagesServiceTestConfig
import com.google.appengine.tools.development.testing.LocalMailServiceTestConfig
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig
import com.google.appengine.tools.development.testing.LocalServiceTestHelper
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig
import com.google.appengine.tools.development.testing.LocalURLFetchServiceTestConfig
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig
import com.google.appengine.tools.development.testing.LocalXMPPServiceTestConfig

class ConsoleScriptExecutorSpec extends Specification {
	
    
    
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
            new LocalFileServiceTestConfig(),
            //new LocalProspectiveSearchServiceTestConfig()
    )

    private Binding binding

    def setup() {
        helper.setUp()
    }

    def cleanup() {
        helper.tearDown()
    }
    
    
    def "Simple calculation"(){
        def result = ConsoleScriptExecutor.evaluate "channel", """
            4 + 5
        """
        expect:
        !result.exception
        !result.output
        result.result == '9'
        
    }
    
    def "Gaelyk bindings available"(){
        def result = ConsoleScriptExecutor.evaluate "channel", """
            datastore
        """
        
        expect:
        !result.exception
        !result.output
        result.result in String
    }
    
    def "Gaelyk category available"(){
        def result = ConsoleScriptExecutor.evaluate "channel", """
            new  com.google.appengine.api.datastore.Entity("test").save()
        """
        
        expect:
        !result.exception
        !result.output
        result.result in String
    }
    
    def "Report compilation error"(){
        def result = ConsoleScriptExecutor.evaluate "channel", """
            out << "Test"
            def xyz = 'test'
            def aaa = xyz
            zzz
            def zzz = 'zzz'
            eee
        """
        
        expect:
        result.exception in MissingPropertyException
        !result.result
        result.output == "Test"
    }
    
    def "Println works"(){
        def result = ConsoleScriptExecutor.evaluate "channel", """
            out << 'Hello World!'
            5 + 6
        """
        
        expect:
        !result.exception
        result.result == "11"
        result.output == "Hello World!"
    }

    def "Log works"(){
        def result = ConsoleScriptExecutor.evaluate "channel", """
            log.info 'Logging is informative!'
        """
        
        expect:
        !result.exception
        result.output == "INFO: Logging is informative!\n"
    }
}
