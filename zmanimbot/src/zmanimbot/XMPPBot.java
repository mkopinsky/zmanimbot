package zmanimbot;

import zmanimbot.*;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.*;
import java.util.*;


/**
 * Bot for connecting to XMPP protocols, such as Google talk. An XMPP Bot object is created
 * by ZmanimBot.
 */
class XMPPBot extends Bot {
	/* Inherited fields:
	 * @ String[] admins
	 * @ String suffix
	 */
	
	
	XMPPConnection connection;
    Presence presence;
    XMPPMessageListener ml;
	
	
	/**
	 * Constructor XMPPBot
	 * 
	 * Creates the bot, connects to the server, sets presence aka status message, 
	 * creates a chatListener. Everything is handled by the chatListener and the rest of the 
	 * Smack API.
	 *
	 */
	public XMPPBot(String server, String username, String password) {
		try {
			ml = new XMPPMessageListener();
			
			connection = new XMPPConnection(server);
			
			System.out.println(new Date() + "\tLogging in to "+server);
			connection.connect();
			connection.login(username,password);
	
	        //Set presence
	        presence = new Presence(Presence.Type.available, "Check out zmanimbot.blogspot.com. And tell your friends about ZmanimBot!",0,Presence.Mode.available);
	        Timer presenceTimer = new Timer();
	        presenceTimer.scheduleAtFixedRate(new TimerTask() { public void run() { connection.sendPacket(presence);}} 	, 0, 10*60*1000);
	        
	        //Create chatListener, and attach it to connection
	        connection.getChatManager().addChatListener(new ChatManagerListener() {
	        	public void chatCreated(Chat chat, boolean createdLocally) {
	        		chat.addMessageListener(ml);
	        	}
	        	
	        });
	        
		} catch (Exception ex) {
			System.out.println(new Date() + "\tCaught in XMPPBot():");
			ex.printStackTrace();
		}
	
	}	
	
	
	/**
	 * Send a message to a recipient.
	 * 
	 * @param recipient The user to set the message to.
	 * @param message The message to send.
	 */
	public void send(String recipient, String message) {
		try {
//			System.out.println("Sending... chatter="+recipient+"\tmessage="+message);
	    	connection.getChatManager().createChat(recipient,ml).sendMessage(message);
		} catch (Exception ex) {System.out.println(new Date() + "\tCaught in XMPPBot.send():");ex.printStackTrace();}
	}

	/**
	 * Set away/available status for the bot.
	 * 
	 * @param status String representation of the type of status to set. Currently only 
	 * 			"Away" and "Available" are supported.
	 * @param message The text accompanying the status message. Can be "" if none desired.
	 */
	public void setStatus(String status, String awayMessage) {
		Presence p;
		try {
			if (status.equalsIgnoreCase("away")) {
				p = new Presence(Presence.Type.available, awayMessage,0,Presence.Mode.away);
			}
			else { //available, as well as default behavior
				p = new Presence(Presence.Type.available, awayMessage,0,Presence.Mode.available);
			}
			presence = p;
			connection.sendPacket(p);
		} catch (Exception ex) {System.out.println(new Date() + "\tCaught in XMPPBot.setStatus():");ex.printStackTrace();}
	}

}
