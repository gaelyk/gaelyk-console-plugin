<!doctype html>
<% def clientId = 'ch' + new Random().nextLong() %>
<% def token = com.google.appengine.api.channel.ChannelServiceFactory.channelService.createChannel(clientId)  %>
<!-- com.google.appengine.api.users.UserServiceFactory -->
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
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script type="text/javascript" src="$twitterBootstrap.js"></script>
<style type="text/css">
	body {
		padding-top: ${twitterBootstrap.gap}px;
	}
	
	#script{
    	position: relative;
	    top:  0px;
	    left: 0px;
	    bottom: 0px;
	    right: 5px;
	    margin: 0px;
	    padding: 0px;
	    height: 400px;
	    background: white;
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
 	#execute {
 		margin-left: 30px;
 	}
 	
 	#clear {
 		margin-right: 20px;
 	}
 	
 	
</style>
<script type="text/javascript" src="/gpr/gaelykconsole/js/jq-common.js"></script>
<script type="text/javascript">
    window.chid = '$clientId';

	function updateSaveButton(aceeditor){
		var val = aceeditor.getSession().getValue();
		if(val && jQuery('#name').val()){
			jQuery('#save.disabled').removeClass('disabled');
		} else {
			jQuery('#save').addClass('disabled');
		}
	}
</script>
<script type="text/javascript" src="/gpr/gaelykconsole/js/jq-script.js"></script>
<script type="text/javascript">
	jQuery(function(){

		
		var j = jQuery;
        <% if(org.gaelyk.console.GaelykConsolePlugin.allowReadOnly && (!users.userLoggedIn || !users.userAdmin)) { %>
	        showMessage('Script read only!', 'info', 'Console is read only because you are not logged in as admin!', true);
	        j('.form-actions a').addClass('disabled');
	        j('#name, #tags').attr('disabled', 'disabled')
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
			
			channel = new goog.appengine.Channel("$token");
		    socket = channel.open();
		    socket.onmessage = function(message){
		    	showMessage("Reporter", 'info', message.data);
		    };
			j('#name, #tags').keyup(function(){
				updateSaveButton(aceeditor);
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
                    <li class="active"><a href="/_ah/gaelyk-console/">Scripts</a></li>
                    <li><a href="/_ah/gaelyk-console/render/">Templates</a></li>
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
	    <div class="row">
				<div class="span8">
					<input class="span8" id="name" placeholder="Script name" title="Name"/>
				</div>
				<div class="span4">
					  <input class="span4" id="tags" placeholder="Comma separated tags" title="Comma separated tags"/>
				</div>
		</div>
		<div class="row">
			<div class="span12">
				<pre id="script" class="span12 editor"></pre>
			</div>
		</div>
		<div class="row">
			<div class="form-actions span12">
					<a class="btn btn-primary" id="execute"><i class="icon-play icon-white"></i> Execute</a>
					<a class="btn" id="load"><i class="icon-folder-open"></i> Load</a>
					<a class="btn disabled" id="save"><i class="icon-ok"></i> Save</a>
					<a class="btn disabled" id="delete"><i class="icon-trash"></i> Delete</a>
					<a class="btn disabled pull-right" id="clear"><i class="icon-refresh"></i> Clear Results</a>
			</div>
		</div>
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
		<footer class="footer">
				<p>&copy; 2012 <a href="http://www.appsatori.eu">AppSatori s.r.o.</a></p>
		</footer>
	</div>
	<script type="text/javascript" src="/gpr/gaelykconsole/js/ace.js"></script>
	<script type="text/javascript" src="/gpr/gaelykconsole/js/theme-eclipse.js"></script>
	<script type="text/javascript" src="/gpr/gaelykconsole/js/mode-groovy.js"></script>
	<script type="text/javascript" src="/_ah/channel/jsapi"></script>
	<script>
		window.onload = function() {
		    var editor = ace.edit("script");
		    editor.setTheme("ace/theme/eclipse");
		    
		    var GroovyMode = require("ace/mode/groovy").Mode;
		    editor.getSession().setMode(new GroovyMode());
		    window.aceeditor = editor;
			<% if(users.userLoggedIn && users.userAdmin){ %>
				editor.getSession().on('change', function(event){
					updateSaveButton(editor);
				});
			<% } else { %>
				editor.setReadOnly(true);
			<% } %>
			document.getElementById('script').style.fontSize='${eu.appsatori.gaelyk.console.GaelykConsolePlugin.fontSize}px';
		};
		</script>
</body>
</html>