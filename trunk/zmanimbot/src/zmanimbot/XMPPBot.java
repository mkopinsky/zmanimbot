package zmanimbot;

import zmanimbot.*;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.*;

import java.util.*;


/**
 * Bot for connecting to XMPP protocols, such as Google talk. An XMPP Bot object is created
 * by ZmanimBot.
 */
class XMPPBot extends Bot {
	/* Inherited fields:
	 * @ String[] admins
	 */
	
	
	XMPPConnection connection;
    Presence presence;
    XMPPMessageListener ml;
    String server;
    String username;
    String password;
	
	
	/**
	 * Constructor XMPPBot()
	 * 
	 * Creates the bot, connects to the server, sets presence aka status message, 
	 * creates a chatListener. Everything is handled by the chatListener and the rest of the 
	 * Smack API.
	 *
	 */
	public XMPPBot(String p_server, String p_username, String p_password) {
		server=p_server;
		username=p_username;
		password=p_password;
		try {
			ml = new XMPPMessageListener();
			
	        //Set presence
	        presence = new Presence(Presence.Type.available, "Check out zmanimbot.blogspot.com. And tell your friends about ZmanimBot!",0,Presence.Mode.available);

	        connectToServer();

	        new Timer().scheduleAtFixedRate(new TimerTask() { public void run() { connection.sendPacket(presence);}} 	, 0, 10*60*1000);
	        
		} catch (Exception ex) {
			System.out.println(new Date() + "\tCaught in XMPPBot():");
			ex.printStackTrace();
		}
	
	}	
	
	/**
	 * connectToServer()
	 * 
	 * Creates new XMPPConnection, connects to server, logs in, adds message listener
	 * to handle regular commands, and adds a packet listener to listener for errors
	 * and print them to stdout. 
	 * 
	 * 
	 * @throws XMPPException
	 */
	public void connectToServer() throws XMPPException {
		System.out.println(new Date() + "\tLogging in to "+server);
		connection=new XMPPConnection(server);

		connection.connect();
		connection.login(username, password);
        connection.getChatManager().addChatListener(new ChatManagerListener() {
        	public void chatCreated(Chat chat, boolean createdLocally) {
        		chat.addMessageListener(ml);
        	}
        });
		connection.addPacketListener(new PacketListener() {
			@Override
            public void processPacket(Packet p) {
				System.out.println(((Message)p).toXML());
            }
		}, new PacketFilter() {
			@Override
            public boolean accept(Packet p) {
				return p instanceof Message && ((Message)p).getType()==Message.Type.error;
            }
		});
		connection.sendPacket(presence);
		
	}
	

	/**
	 * Disconnect and reconnect to network with no wait
	 */
	public void cycleConnection() {
		cycleConnection(0);
	}
	
	/**
	 * Disconnect and reconnect to network, with specified wait interval.
	 *
	 */
	public void cycleConnection(long wait) {
		System.out.println("Cycling connection: "+connection.getHost() + " with wait of " + wait + " seconds.");
		connection.disconnect();
		new Timer().schedule(new TimerTask() {
            public void run() {
				try {
					connectToServer();
				} catch (XMPPException ex) {
					ex.printStackTrace();
				}
            }
		}, wait*1000);
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
