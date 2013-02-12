package org.gaelyk.console

import com.google.appengine.api.channel.ChannelMessage
import com.google.appengine.api.channel.ChannelService
import com.google.appengine.api.channel.ChannelServiceFactory

class ChannelReporter {
	final ChannelService service = ChannelServiceFactory.channelService
	final String channel

	public ChannelReporter(String message) {
		this.channel = message
	}
	
	public String leftShift(Object message){
		try {
			service.sendMessage(new ChannelMessage(channel, "" + message))
		} catch(Exception e){
			// don't care, you just don't get the notification
		}
	}
	
	public String call(Object message){
		leftShift(message)
	}
	

}
