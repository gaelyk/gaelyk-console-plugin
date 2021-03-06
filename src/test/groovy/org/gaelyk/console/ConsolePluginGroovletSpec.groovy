package org.gaelyk.console

import groovyx.gaelyk.spock.GaelykUnitSpec

abstract class ConsolePluginGroovletSpec extends GaelykUnitSpec {

	StringWriter sw = []
	
	def setup(){
		out = new PrintWriter(sw)
		groovlet getGroovletName()
		helper
			.setEnvIsAdmin(true)
			.setEnvIsLoggedIn(true)
			.setEnvEmail('vladimir.orany@appsatori.eu')
			.setEnvAuthDomain('appsatori.eu')
	}
	
	def groovlet = {
		groovletInstance = new PluginGroovletUnderSpec("$it")

		[ 'sout', 'out', 'response', 'datastore', 'memcache', 'mail', 'urlFetch', 'images', 'users', 'user', 'defaultQueue', 'queues', 'xmpp',
		  'blobstore', 'files', 'oauth', 'channel', 'capabilities', 'namespace', 'localMode', 'app', 'backends', 'lifecycle'
		].each { groovletInstance."$it" = this."$it" }

		this.metaClass."${it.tokenize('.').first()}" = groovletInstance
	}
	
	abstract String getGroovletName()
	

}
