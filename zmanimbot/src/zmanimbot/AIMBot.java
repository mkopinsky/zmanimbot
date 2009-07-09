package zmanimbot;
import com.aol.acc.*;
import java.util.*;


/**
 * Bot for connecting to AIM. An AIMBot object is created  by ZmanimBot.
 */
public class AIMBot extends Bot {
	AccSession session;
	boolean running = true;
	String key = "ZmanimBot (Key:zm15hQ3po7RrfGUH)";
	
	
	//Constructor
	AIMBot(final String username, final String password) {
//		Thread th = new Thread( new Runnable() {
//			@Override
//			public void run() {
				try {
//					System.out.println(new Date() + "\tCreating new AIMBot.");
					// Create main session object
					session = new AccSession();
					
					// Add event listener
					session.setEventListener(new AIMMessageListener());
					
					// set key
					AccClientInfo info = session.getClientInfo();
					info.setDescription(key);
					
					
					// set screen name
					System.out.println(new Date() + "\tLogging into AIM.");
					session.setIdentity(username);
					
					// setup prefs so anyone can IM us, but not chats or DIMs
					session.setPrefsHook(new AIMPrefs());
					AccPreferences prefs = session.getPrefs();
					prefs.setValue("aimcc.im.chat.permissions.buddies", AccPermissions.RejectAll);
					prefs.setValue("aimcc.im.chat.permissions.nonBuddies", AccPermissions.RejectAll);
					prefs.setValue("aimcc.im.direct.permissions.buddies", AccPermissions.RejectAll);
					prefs.setValue("aimcc.im.direct.permissions.nonBuddies", AccPermissions.RejectAll);
					prefs.setValue("aimcc.im.standard.permissions.buddies", AccPermissions.AcceptAll);
					prefs.setValue("aimcc.im.standard.permissions.nonBuddies", AccPermissions.AcceptAll);
				
					session.signOn(password);
					
			
					//msg pump
				    while( running ) 
				    {
				    	try {
				    		AccSession.pump(50);
				    	} catch (Exception e) {
				    		e.printStackTrace();
				    	}
				    	
				   		try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				    	
				    }
				} catch (AccException ex) { 
					System.out.println(new Date() + "\tCaught in AIMBot()");ex.printStackTrace();
					}
			}

//		});
		
//		th.start();
//	}
	
	/**
	 * Send a message to a recipient.
	 * 
	 * @param recipient The user to set the message to.
	 * @param message The message to send.
	 */
	public void send (String recipient, String message) {
		try {
			AccIm im = session.createIm(message,null);
			recipient = recipient.split("@")[0];
			AccImSession imSession = session.createImSession(recipient, AccImSessionType.Im);
			imSession.sendIm(im);
		} catch (Exception ex) {System.out.println(new Date() + "\tCaught in AIMBot.send()");ex.printStackTrace();}
	}
		
	/**
	 * Set away/available status for the bot.
	 * 
	 * @param status String representation of the type of status to set. Currently only 
	 * 			"Away" and "Available" are supported.
	 * @param message The text accompanying the status message. Can be "" if none desired.
	 */
	public void setStatus(String status, String message) {
		if (message.equals("")) message = " ";
		try {
			if (status.equalsIgnoreCase("away")) {
				session.setProperty(AccSessionProp.AwayMessage, new AccVariant(message));


			}
			else {
				session.setProperty(AccSessionProp.AwayMessage, new AccVariant(""));
				session.setProperty(AccSessionProp.StatusText, new AccVariant(message));
			}
		} catch (AccException ex) {
			System.out.println(ex);
			System.out.println(ex.errorCode);
		}
		
	}
	
}
