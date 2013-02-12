package org.gaelyk.console

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.text.SimpleTemplateEngine;
import groovyx.gaelyk.GaelykBindingEnhancer;
import groovyx.gaelyk.GaelykCategory;
import groovyx.gaelyk.plugins.PluginsHandler;

import java.io.StringWriter;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;

class ConsoleTemplateRenderer {
    
    static render(out, String script, String template){
        Binding binding = new Binding()
        binding.out = out
        binding.include = new TemplateIncluder(out)
        
        // map mocks
        binding.request = [:]
        binding.response = [:]
        binding.params = [:]
        binding.session = [:]
        binding.headers = [:]
        
        GaelykBindingEnhancer.bind(binding)
        PluginsHandler.instance.enrich(binding)
        GroovyShell shell = new GroovyShell(binding)
        try {
            use([GaelykCategory, * PluginsHandler.instance.categories]) {
                String templateAsScript = TemplateToScriptConverter.getTemplateAsScript(template)
                shell.evaluate script + '\n' + templateAsScript
            }
        } catch (Throwable e){
            e.printStackTrace(out)
        }
    }

}

class TemplateToScriptConverter {

    static String getTemplateAsScript(String template) {
        HiJackGroovyShell hjgs = []
        SimpleTemplateEngine ste = [hjgs]
        ste.createTemplate(template)
        hjgs.scriptText
    }
}

class HiJackGroovyShell extends GroovyShell {

    String scriptText

    @Override
    Script parse(String scriptText, String fileName) throws CompilationFailedException {
        this.scriptText = scriptText
        super.parse(scriptText, fileName)
    }
}

