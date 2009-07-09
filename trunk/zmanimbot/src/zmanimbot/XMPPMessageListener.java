package zmanimbot;

import zmanimbot.*;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.*;
import java.util.*;

class XMPPMessageListener extends ZmanimMessageListener implements MessageListener {
	
	/**
	 * Method XMPPMessageListener
	 *
	 *
	 */
	public XMPPMessageListener() {
		super();
//		System.out.println(new Date() + "\tCreating new XMPPMessageListener.");
	}

	/**
	 * Method processMessage
	 *
	 *
	 * @param chat
	 * @param message
	 *
	 */
	public void processMessage(Chat chat, Message message) {
		String str = message.getBody();
        if ((str!= null) && (message.getType()!=Message.Type.error)) {
	    	String chatter = message.getFrom().split("/")[0];
        	try {
	            chat.sendMessage(parse(str,chatter));
            }
            catch (XMPPException e) {
	            e.printStackTrace();
            }
        }
	}

}
