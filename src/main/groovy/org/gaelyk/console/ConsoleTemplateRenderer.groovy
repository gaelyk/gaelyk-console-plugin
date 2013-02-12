package org.gaelyk.console

import groovy.text.SimpleTemplateEngine
import groovyx.gaelyk.GaelykBindingEnhancer
import groovyx.gaelyk.plugins.PluginsHandler

import org.codehaus.groovy.control.CompilationFailedException

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
            String templateAsScript = TemplateToScriptConverter.getTemplateAsScript(template)
            shell.evaluate script + '\n' + templateAsScript
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

