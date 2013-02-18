<!doctype html>
<html>
<head>

<title>${twitterBootstrap.brand} Console</title>

<link rel="shortcut icon" href="http://gaelyk.appspot.com/favicon.ico"
    type="image/ico">
<link rel="icon" href="http://gaelyk.appspot.com/favicon.ico"
    type="image/ico">
<link rel="stylesheet" type="text/css"
    href="$twitterBootstrap.css" />
<link rel="stylesheet" type="text/css"
    href="$twitterBootstrap.responsive" />
<script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-migrate-1.1.0.min.js"></script>
<script type="text/javascript" src="$twitterBootstrap.js"></script>
<style type="text/css">
    body {
        padding-top: <%=twitterBootstrap.gap%>px;
    }
    
    .ace_editor {
       font-size: ${org.gaelyk.console.GaelykConsolePlugin.fontSize}px;
    }
    
    #script, #template{
        position: relative;
        top:  0px;
        left: 0px;
        bottom: 0px;
        right: 5px;
        margin: 0px;
        padding: 0px;
        background: white;
    }
    
    #script {
       height: 100px;
    }
    
    #template {
       height: 300px;
    }

    
    .scriptBrowser {
        height: 250px;
        overflow-y: auto;
        overflow-x: hidden;
    }
    
    .form-actions.span12 {
        padding-left: 0px;
        padding-right: 0px;
    }
    #render {
        margin-left: 30px;
    }
    
    #clear {
        margin-right: 20px;
    }
    
    #name, #tags {
 		margin-bottom: 9px;
 	}
    
</style>

<script type="text/javascript" src="/gpr/gaelykconsole/js/jq-common.js"></script>
<script type="text/javascript">
    function updateSaveButton(templateeditor, aceeditor){
        var val = aceeditor.getSession().getValue();
        var template = templateeditor.getSession().getValue();
        if(template && jQuery('#name').val()){
            jQuery('#save.disabled').removeClass('disabled');
        } else {
            jQuery('#save').addClass('disabled');
        }
    }
</script>
<script type="text/javascript" src="/gpr/gaelykconsole/js/jq-template.js"></script>
<script type="text/javascript">
    jQuery(function(){  
        var j = jQuery;
        <% if(org.gaelyk.console.GaelykConsolePlugin.allowReadOnly && (!users.userLoggedIn || !users.userAdmin)) { %>
	        showMessage('Template read only!', 'info', 'Console is read only because you are not logged in as admin!', true);
	        j('.form-actions a').addClass('disabled');
	        j('#name, #tags, #render').attr('disabled', 'disabled')
            if(window.location.hash){
                loadItem(window.location.hash.substring(1));
            }
        <% } else if(!users.userLoggedIn || !users.userAdmin) { %>
            showMessage('Console disabled!', 'error', 'Console is disabled because you are not logged in as admin!', true);
            j('.form-actions a').addClass('disabled');
            j('#name, #tags').attr('disabled', 'disabled')
            
        <% } else { %>
            if(window.location.hash){
                loadItem(window.location.hash.substring(1));
            }
            j('#name, #tags').keyup(function(){
                updateSaveButton(templateeditor, aceeditor);
            });
        <% } %>
        
    });
</script>

</head>
<body>
    <div class="navbar navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container">
                <a class="brand" href="/_ah/gaelyk-console/">${twitterBootstrap.brand} Console</a>
                <ul class="nav">
                    <li><a href="/_ah/gaelyk-console/">Scripts</a></li>
                    <li class="active"><a href="/_ah/gaelyk-console/render/">Templates</a></li>
                </ul>
                <ul class="nav pull-right">
                    <% if(users.userLoggedIn) { %>
                    <li><a href="${users.createLogoutURL(request.originalURI)}">Log out</a></li>
                    <% } else { %>
                    <li><a href="${users.createLoginURL(request.originalURI)}">Log in</a></li>
                    <% } %>
                </ul>
            </div>
        </div>
    </div>

    <div class="container">
      <form method="post" action="/_ah/gaelyk-console/template/render" target="result" id="tplform">
        <div class="row">
                <div class="span8">
                    <input class="span8" id="name" placeholder="Template name" title="Name"/>
                </div>
                <div class="span4">
                      <input class="span4" id="tags" placeholder="Comma separated tags" title="Comma separated tags"/>
                </div>
        </div>
       <div class="row">
            <div class="span12">
                <pre id="template" class="span12 editor"></pre>
                <input type="hidden" name="template" id="hiddenTemplate"/>
            </div>
        </div>
        <div class="row">
            <div class="span12">
                <h4>Initialization</h4>
                <pre id="script" class="span12 editor"></pre>
                <input type="hidden" name="script" id="hiddenScript"/>
            </div>
        </div>
        <div class="row">
            <div class="form-actions span12">
                    <button type="submit" class="btn btn-primary" id="render"><i class="icon-camera icon-white"></i> Render</button>
                    <a class="btn" id="load"><i class="icon-folder-open"></i> Load</a>
                    <a class="btn disabled" id="save"><i class="icon-ok"></i> Save</a>
                    <a class="btn disabled" id="delete"><i class="icon-trash"></i> Delete</a>
                    <a class="btn disabled pull-right" id="clear"><i class="icon-refresh"></i> Clear Results</a>
            </div>
        </div>
    </form>
        <div id="progress" class="hide progress progress-info progress-striped active">
            <div class="bar" style="width: 100%;"></div>
        </div>
        <div id="flash-holder">
        
        </div>
        <div class="modal hide" id="myModal">
            <div class="modal-header">
                <a class="close closeLoad" data-dismiss="myModal">×</a>
                <h3>Load script</h3>
            </div>
            <div class="modal-body">
                <div id="loading-scripts" class="progress progress-info progress-striped active">
                    <div class="bar" style="width: 100%;"></div>
                </div>
                <ul class="scriptBrowser nav nav-list">
                    
                </ul>
            </div>
            <div class="modal-footer">
                <a class="btn close closeLoad">Close</a>
            </div>
        </div>
        <div class="modal hide" id="deleteModal">
            <div class="modal-header">
                <a class="close closeDelete" data-dismiss="deleteModal">×</a>
                <h3>Delete script</h3>
            </div>
            <div class="modal-body">
                <p>Do you really want to delete this script?</p>
            </div>
            <div class="modal-footer">
                <a class="btn close closeDelete">Close</a>
                <a class="btn btn-danger" id="doDelete">Delete</a>
            </div>
        </div>
    </div>
    <script type="text/javascript" src="/gpr/gaelykconsole/js/ace.js"></script>
    <script type="text/javascript" src="/gpr/gaelykconsole/js/theme-eclipse.js"></script>
    <script type="text/javascript" src="/gpr/gaelykconsole/js/mode-groovy.js"></script>
    <script type="text/javascript" src="/gpr/gaelykconsole/js/mode-html.js"></script>
    <script>
        window.onload = function() {
            
            var GroovyMode = require("ace/mode/groovy").Mode;
            var HtmlMode = require("ace/mode/html").Mode;
            
            var editor = ace.edit("script");
            editor.setTheme("ace/theme/eclipse");
            editor.getSession().setMode(new GroovyMode());
            
            var tpleditor = ace.edit("template");
            tpleditor.setTheme("ace/theme/eclipse");
            tpleditor.getSession().setMode(new HtmlMode());
                
            
            window.aceeditor = editor;
            window.templateeditor = tpleditor;
            
            <% if(users.userLoggedIn && users.userAdmin){ %>
                editor.getSession().on('change', function(event){
                    updateSaveButton(tpleditor, editor);
                    jQuery('#hiddenScript').val(editor.getSession().getValue())
                });
                tpleditor.getSession().on('change', function(event){
                    updateSaveButton(tpleditor, editor);
                    jQuery('#hiddenTemplate').val(tpleditor.getSession().getValue())
                });
            <% } else { %>
                editor.setReadOnly(true);
                tpleditor.setReadOnly(true);
            <% } %>
            
            document.getElementById('script').style.fontSize='${org.gaelyk.console.GaelykConsolePlugin.fontSize}px';
            document.getElementById('template').style.fontSize='${org.gaelyk.console.GaelykConsolePlugin.fontSize}px';
        };
        </script>
</body>
</html>