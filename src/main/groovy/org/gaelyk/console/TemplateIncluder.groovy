package org.gaelyk.console

class TemplateIncluder {
    
    private sout;
    
    TemplateIncluder(sout){
        this.sout = sout;
    }

    public String leftShift(String template){
            def tpl = ConsoleTemplateRepository.load(template)
            if(tpl){
                ConsoleTemplateRenderer.render(sout, tpl.script, tpl.template)                
            } else {
                sout << "Template $template should be included here!"            
            }
            
    }
    
    public void call(String template){
        leftShift(template)
    }
    
}
