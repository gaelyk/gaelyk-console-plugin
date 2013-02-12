package org.gaelyk.console

import java.io.IOException;

import groovyx.gaelyk.GaelykBindingEnhancer;
import groovyx.gaelyk.GaelykCategory;
import groovyx.gaelyk.plugins.PluginsHandler;

class ConsoleScriptExecutor {
	
	static evaluate(String channelId, String scriptText){
		Writer out = new StringWriter()
		Binding binding = new Binding()
		binding.out = out
		binding.report = new ChannelReporter(channelId)
		GaelykBindingEnhancer.bind(binding)
		PluginsHandler.instance.enrich(binding)
		GroovyShell shell = new GroovyShell(binding)
		try {
			def result = use([GaelykCategory, * PluginsHandler.instance.categories]) {
				shell.evaluate(scriptText)
			}
			return [result: result ? (result.toString()) : '', output: out.toString()]
		} catch (Throwable e){
			return [exception: e, output: out.toString()]
		}
	}

}
