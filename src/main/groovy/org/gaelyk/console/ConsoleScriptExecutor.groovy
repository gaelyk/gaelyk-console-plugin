package org.gaelyk.console

import groovyx.gaelyk.GaelykBindingEnhancer
import groovyx.gaelyk.plugins.PluginsHandler

class ConsoleScriptExecutor {
	
	static evaluate(String channelId, String scriptText){
		Writer out = new StringWriter()
		Binding binding = new Binding()
		binding.out = out
		binding.report = new ChannelReporter(channelId)
 		binding.log = new OutLogger(out)
		GaelykBindingEnhancer.bind(binding)
		PluginsHandler.instance.enrich(binding)
		GroovyShell shell = new GroovyShell(binding)
		try {
			def result = shell.evaluate(scriptText)
			return [result: result ? (result.toString()) : '', output: out.toString()]
		} catch (Throwable e){
			return [exception: e, output: out.toString()]
        }
	}
}

class OutLogger {
    private out
    
    def OutLogger(out) {
        this.out = out
    }

    def finest(string) {
        this.out << "FINEST: ${string}\n"
    }
        
    def finer(string) {
        this.out << "FINER: ${string}\n"
    }
    
    def fine(string) {
        this.out << "FINE: ${string}\n"
    }
    
    def config(string) {
        this.out << "CONFIG: ${string}\n"
    }
    
    def info(string) {
        this.out << "INFO: ${string}\n"
    }
    
    def warning(string) {
        this.out << "WARNING: ${string}\n"
    }
    
    def severe(string) {
        this.out << "SEVERE: ${string}\n"
    }
}
