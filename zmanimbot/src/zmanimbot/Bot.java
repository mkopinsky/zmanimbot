package zmanimbot;


/**
 * Generic superclass that all protocol-specific bots should implement.
 * @author Michael Kopinsky
 */
abstract class Bot {

	String[] admins;

	/**
	 * Get the list of usernames of users who are authorized to do admin functions. 
	 */
	public String[] getAdmins() {return admins;}

	/**
	 * Set the list of usernames of users who are authorized to do admin functions. 
	 */
	public void setAdmins(String[] newAdmins) {admins=newAdmins;}
	
	/**
	 * Send a message to a recipient.
	 * 
	 * @param recipient The user to set the message to.
	 * @param message The message to send.
	 */
	public abstract void send(String recipient, String message);
	
	/**
	 * Set away/available status for the bot.
	 * 
	 * @param status String representation of the type of status to set. Currently only 
	 * 			"Away" and "Available" are supported.
	 * @param message The text accompanying the status message. Can be "" if none desired.
	 */
	public abstract void setStatus(String status, String message);

}
