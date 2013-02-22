package org.gaelyk.console

import groovyx.gaelyk.plugins.PluginBaseScript

class GaelykConsolePlugin extends PluginBaseScript {
    
    static final int FIRST_ROUTE_INDEX = -30000
    
    static allowReadOnly = false
    static fontSize = 12
    
    @Override
    public Object run() {
        firstRouteIndex = FIRST_ROUTE_INDEX
        
        get     "/_ah/channel/**",                      ignore: true
        
        post    "/_ah/gaelyk-console/script/execute",   forward: "/org/gaelyk/console/script/executor.groovy"
        get     "/_ah/gaelyk-console/script/@name",     forward: "/org/gaelyk/console/script/load.groovy?name=@name"
        post    "/_ah/gaelyk-console/script/@name",     forward: "/org/gaelyk/console/script/save.groovy?name=@name"
        delete  "/_ah/gaelyk-console/script/@name",     forward: "/org/gaelyk/console/script/delete.groovy?name=@name"
        get     "/_ah/gaelyk-console/scripts",          forward: "/org/gaelyk/console/script/list.groovy"
        get     "/_ah/gaelyk-console/scripts/@tag",     forward: "/org/gaelyk/console/script/list.groovy?tags=@tag"
        
        get     "/_ah/gaelyk-console/",                 forward: "/org/gaelyk/console/script.gtpl"
        
        post    "/_ah/gaelyk-console/template/render",  forward: "/org/gaelyk/console/template/renderer.groovy"
        get     "/_ah/gaelyk-console/template/@name",   forward: "/org/gaelyk/console/template/load.groovy?name=@name"
        post    "/_ah/gaelyk-console/template/@name",   forward: "/org/console/template/save.groovy?name=@name"
        delete  "/_ah/gaelyk-console/template/@name",   forward: "/org/gaelyk/console/template/delete.groovy?name=@name"
        get     "/_ah/gaelyk-console/templates",        forward: "/org/gaelyk/console/template/list.groovy"
        get     "/_ah/gaelyk-console/templates/@tag",   forward: "/org/gaelyk/console/template/list.groovy?tags=@tag"
        
        get     "/_ah/gaelyk-console/render/",          forward: "/org/gaelyk/console/template.gtpl"
    }

}
