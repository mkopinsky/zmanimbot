package zmanimbot;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import zmanimbot.*;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.*;
import java.util.*;


/**
 * Bot for connecting to Twitter. */
class TwitterBot extends Bot {
	/* Inherited fields:
	 * @ String[] admins
	 * @ String suffix
	 */
	static Twitter twitterApi;
	
	public TwitterBot(String username, String password) {
		try {
			twitterApi = new Twitter(username,password);
		} catch (Exception ex) {
			System.out.println(new Date() + "\tCaught in TwitterBot():");
			ex.printStackTrace();
		}
	
	}	

	/**
	 * Set away/available status for the bot.
	 * 
	 * @param status String representation of the type of status to set. Currently only 
	 * 			"Away" and "Available" are supported.
	 * @param message The text accompanying the status message. Can be "" if none desired.
	 */
	public void setStatus(String status, String awayMessage) {
		try {
    		  Status s = twitterApi.updateStatus(awayMessage);
    		  System.out.println("Wrote to twitter: " + s.getText());
    		}
    		catch(Exception ex)
    		{
    			System.out.println(ex.getMessage());
    		}
    }

	@Override
	public void send(String recipient, String message) {
		try
		{
			twitterApi.sendMessage(recipient, message);
		}
		catch(Exception ex)
		{
   			System.out.println(ex.getMessage());
		}
	}

}
