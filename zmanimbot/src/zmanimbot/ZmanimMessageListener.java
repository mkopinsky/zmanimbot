package zmanimbot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Calendar;
import java.util.Date;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.*;
import net.sourceforge.zmanim.*;
import net.sourceforge.zmanim.util.*; 

/**
 * Most of the actual processing of messages goes on in ZmanimMessageListener. 
 */
abstract class ZmanimMessageListener {


	String[] admins = {"mkopinsky@gmail.com","liron.kopinsky@gmail.com",/*"kop42986@aim.com"*/};
	String[] blockedUsers = {"zroe102@gmail.com"};
	ZmanimParser parser;
	HebcalProvider hp;
	
	protected void log(String s) {
		System.out.println(s);
	}


	/**
	 *Constructor
	 *
	 */
	public ZmanimMessageListener() {
		parser = ZmanimBot.getZmanimParser();
		hp = ZmanimBot.getHebcalProvider();
	} 
	

	/**
	 * Parse a command from a user.
	 * 
	 * @param str the command
	 * @param chatter the user who sent the command
	 */
	public String parse(String str, String chatter) {
		log(new SimpleDateFormat("[M/d/yy hh:mm:ss aaa]").format(new Date())+" "+chatter+": "+str);
    	String lowerCase = str.toLowerCase().trim();
    	
    	String param = (str.split(" ").length>1 ? str.substring(str.indexOf(" ")+1) : "" );
    	try {
    		if (isBlocked(chatter)) {
    			return blocked(param,chatter);
    		}

	    	else if (lowerCase.startsWith("help") || lowerCase.startsWith("?")) {
	    		return help(param);
	    	}
	    	
	    	else if (lowerCase.startsWith("list")){
	    		return list(param);
	    	}
	    	
	    	else if (lowerCase.startsWith("credits") || lowerCase.startsWith("about")) {
	    		return credits(param );
	    	}
	    	
	    	else if(lowerCase.startsWith("comment") || lowerCase.startsWith("feedback")){
	    		return comment( param, chatter);
	    	}
	    		    	
	    	else if (lowerCase.startsWith("map")) {
	    		return  map(param);
	    	}
	    	
	    	else if (lowerCase.startsWith("zmanim")) {
	    		return zmanim(param);
	    	}
	    	else if (lowerCase.startsWith("admin")) {
	    		return admin(param, chatter );
	    	}
	    	else if (lowerCase.startsWith("hello") || lowerCase.startsWith("hi") || lowerCase.startsWith("hey")) {
	    		return  hello(param);
	    	}
	    	else if (lowerCase.startsWith("location")) {
	    		return location(param);
	    	}
	    	else if (lowerCase.startsWith("parsha") || lowerCase.startsWith("parasha")) {
	    		return parsha(param);
	    	}
	    	else if (lowerCase.startsWith("omer") || lowerCase.startsWith("sefira")) {
	    		return omer(param);
	    	}
	    	else if (lowerCase.startsWith("date")) {
	    		return hebrewdate(param);
	    	}
	    	else { 
	    		return zmanim(str);
	    	}
	    } catch (Exception ex) {
	    	log("Caught in ZmanimMessageListener.parse():");
	    	ex.printStackTrace();
	    	return "Error!";
	    }
    	
    }
    
    
    //Methods for specific commands.  Although some of them do not vary their behavior based on
	//the rest of the message, for uniformity's sake, each one takes a parameter of any trailing arguments

	/**
	 * Get the help message. Maybe at some point this could be expanded to give help for
	 * particular commands.
	 * 
	 * @return The standard help message.
	 */
    String help (String str) {
		return "Here are instructions for using ZmanimBot.\n" +
		"To get a default list of zmanim for your location, just type the location.\n" +
		"To get specific zmanim, type the location, a colon, and the zmanim separated by commas.\n"+
		"To get zmanim for a specific date, put a colon and the date (or the words \"today\", \"tomorrow\" or \"yesterday\" after the location or zmanim list.\n"+ 
		"Example: new york: hanetz: tomorrow\n"+
		"To see a list of available zmanim, type \"list\".\n"+
		"To see a map of a location with bearing to Yerushalayim, type \"map\" and the location.\n"+
		"To leave a comment/bug report for us, type \"comment\" and then your comment."+
		"To get information about a particular location, type \"location\" and then the location.";
            /*"remind -l <location> -t <halachic time name [minutes before time]>\n"+
            "\tlocation can be any location recognized by Google Maps\n"+
            "\ttime can be any of the halachic times returned by zmanim\n"+
            "\tminutes before is the number of minutes before the halachic time you want to be alerted."+*/
    }
    
    /**
     * @return the error to report to a blocked user.
     */
    String blocked (String str, String chatter) {
		java.util.Random rand = new java.util.Random();
		String[] messages = {
			"I'm sorry, but due to repeated abuse, "+chatter+" has been banned from using ZmanimBot.",
			"The number you have reached has been disconnected or is no longer in service. Please check the number and dial again.",
//			"No zmanim for you!",
		};
		return messages[rand.nextInt(messages.length)];
		
	}

    /**
     * A lot of people don't really know what to say to the bot, so they said hi.
     * 
     * @return the message to give to friendly people who just say hi.
     */
    String hello (String str) {
		return "Hi there. For a list of commands, type help. For more info, see http://zmanimbot.blogspot.com.";
	}

    /**
     * @return a list of zmanim that Zmanimbot is able to understand.
     */
    String list (String str) {
		String retStr = "";
		for (String st:parser.getAcceptableNames())
			retStr += "\""+st+"\", ";
		retStr +="\nZmanimBot also understands alternative spellings as well as Hebrew.";
		return retStr;
    }
    
    /**
     * @return the credits message
     */
    String credits (String str) {
		return "ZmanimBot is (C) 2008 by Michael Y. Kopinsky. Email him at mkopinsky@gmail.com with any questions.\n"+
				"ZmanimBot is based on the ZmanimAPI by KosherJava, at http://www.kosherjava.com/zmanim-project/.  See there for details on how the Zmanim are calculated.\n"+
				"The Gmail/Google Talk communication is based on Smack API, available at http://www.igniterealtime.org/projects/smack/index.jsp.";
    	
    }
    /**
     * 
     * Allow a user to send a comment/feedback message to bot admins.
     * 
     * @param str The message to send
     * @param from The name of the user sending the feedback 
     * @return success/failure message
     */
    String comment (String str, String from) {
    	if (str.trim().equals("")) return "You must include a comment.";
    	else try {
	    	for (String admin:admins) {
	    		ZmanimBot.sendMessage(admin,from+" commented: "+str);
	    	}
	    	return "Thanks for your feedback.";
	    } catch (Exception ex) {
	    	return "There was an error.";
	    }
    }

    /**
     * 
     * Provide information on a given location (as with all requests, location info
     * is either fetched from google maps/geonames, or gotten from the local cache)
     * 
     * @param str The location to look up
     * @return Formatted string to return to the user
     */
    String location (String str) {
    	if (str.equals("")) {
    		return "You must provide a location...";
    	}
		try {
			GeoLocation loc = ZmanimBot.getGeoLocation(str);

			StringBuilder sb = new StringBuilder();
			sb.append("Location:\t").append(loc.getLocationName());
			sb.append("\nLatitude:\t").append(loc.getLatitude()).append("°");
			sb.append("\nLongitude:\t").append(loc.getLongitude()).append("°");
//			sb.append("\nTimezone Name:\t").append(loc.getTimeZone().getID());
			sb.append("\nTimezone:\t").append(loc.getTimeZone().getDisplayName());
			sb.append("\n(GMT ").append((loc.getTimeZone().getRawOffset()>0?"+":"")).
				append(loc.getTimeZone().getRawOffset() / (1000*60*60));
			if (loc.getTimeZone().getDSTSavings()!=0)
				sb.append(", +").append(loc.getTimeZone().getDSTSavings() / (1000*60*60)).append(" hour for DST when applicable");
			sb.append(")");

			return sb.toString();
		}
		catch (Exception ex) {
			return "Invalid location.";
		}
    }

    /**
     * 
     * Provide users with a link to kosherjava's map applet showing location, zmanim,
     * and a line showing the direction to Jerusalem. 
     * 
     * @param str
     * @return
     */
    String map (String str) {
    	if (str.equals("")) {
    		return "You must provide a location...";
    	}
		try {
			GeoLocation loc = ZmanimBot.getGeoLocation(str);
			return "Click here (courtesy of KosherJava): http://www.kosherjava.com/maps/zmanim.html?lat="+loc.getLatitude()+"&lng="+loc.getLongitude()+"&zoom=13";
		}
		catch (Exception ex) {
			return "Invalid location.";
		}
    }
    
    /**
     * 
     * Provide a user with zmanim for a requested location/date/list of zmanim
     * 
     * @param str The string to parse for the details of the request
     * @return A string including all the requested info
     */
    String zmanim(String str) {
	    try {
	    	String[] strArray = str.split("[:;|]");
	    	for (int i =0; i<strArray.length;i++)
	    		strArray[i]=strArray[i].trim();

			GeoLocation loc = ZmanimBot.getGeoLocation(strArray[0].toUpperCase().trim());
			log('{'+loc.getLocationName()+'}');
	        ZmanimCalendar cal = new ZmanimCalendar(loc);

			DateFormat df = DateFormat.getTimeInstance();
	        SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd, yyyy");

	        Calendar c = cal.getCalendar();
	        Date d;
	        
	        switch (strArray.length) {
	        	case 1: {
	        		d = parser.parseDate("today");
	        		return  "Zmanim for " + loc.getLocationName() + "\n" +
	                	sdf.format(d) + holidays(d) + 
	                	parser.getZmanimString(cal);
	        	}
	        	
	        	case 2: {
		        	if ((d = parser.parseDate(strArray[1]))!=null) { //"new york:tomorrow"
				        return  "Zmanim for " + loc.getLocationName() + "\n" +
				                sdf.format(d) + holidays(d) + 
				                parser.getZmanimString(cal,d);
			        }
			        else { 									//"new york:shema"
			        	d = parser.parseDate("today");
				        return  "Zmanim for " + loc.getLocationName() + "\n" +
				                sdf.format(d) + holidays(d) + 
				                parser.getZmanimString(strArray[1],cal);
			        }
	        	}
	        	
	        	case 3:
	        	default: {
		        	if ((d = parser.parseDate(strArray[1]))!=null) { //"new york:tomorrow:shema"
				        return  "Zmanim for " + loc.getLocationName() + "\n" +
				                sdf.format(d) + holidays(d) +  
				                parser.getZmanimString(strArray[2],cal,d);
			        }
		        	else if ((d = parser.parseDate(strArray[2]))!=null) { //"new york:shema:tomorrow"
				        return  "Zmanim for " + loc.getLocationName() + "\n" +
				                sdf.format(d) + holidays(d) + 
				                parser.getZmanimString(strArray[1],cal,d);
			        	} 
			        else return "Invalid syntax. See zmanimbot.blogspot.com for more details.";
			    }
	        }
	        
	    } catch (Exception ex) {
//	    	ex.printStackTrace();
	    	return "Unknown command or location. Type \"help\" for a list of commands.";
	    }
    	
    }
    
    /**
     * 
     * Find the parsha for a given date, or get today's parsha if no valid date is given
     * 
     * @param param string to check for date
     * @return
     */
    String parsha(String param) {
    	Date d = parser.parseDate(param.trim());
    	if (d==null)
    		d=parser.parseDate("today");
    	String parsha = hp.getParsha(d);
    	if (parsha ==null)
    		return "No parsha could be found for that date. Sorry!";
    	else
    		return (new SimpleDateFormat("MMMMM dd, yyyy").format(d) + "\n" + parsha);
    }

    /**
     * 
     * Find the omer count for a given date, or get today's omer if no valid date is given
     * 
     * @param param string to check for date
     * @return
     */
    String omer(String param) {
    	Date d = parser.parseDate(param.trim());
    	if (d==null)
    		d=parser.parseDate("today");
    	String omer = hp.getOmer(d);
    	if (omer==null)
    		return "No omer could be found for that date. Sorry!";
    	else
    		return (new SimpleDateFormat("MMMMM dd, yyyy").format(d) + "\n" + omer);
    }

    /**
     * 
     * Find the hebrew date for a given date, or get today's date if no valid date is given
     * 
     * @param param string to check for date
     * @return
     */
    String hebrewdate(String param) {
    	Date d = parser.parseDate(param.trim());
    	if (d==null)
    		d=parser.parseDate("today");
    	String hebrewdate = hp.getHebrewDate(d);
    	if (hebrewdate==null)
    		return "No Hebrew date could be found for that date. Sorry!";
    	else
    		return (new SimpleDateFormat("MMMMM dd, yyyy").format(d) + "\n" + hebrewdate + holidays(d));
    }
    
    String holidays (Date d) {
    	String holiday = hp.getHolidays(d);
    	return (holiday == null ? "" : "\n"+holiday);
    }

    /**
     * 
     * Check if a user is an admin, to allow him to use admin commands.
     * 
     * @param chatter Username to check
     * @return true if admin, false otherwise
     */
    boolean isAdmin(String chatter) {
		boolean authorized = false;
		for (String admin:admins)
			if (admin.equalsIgnoreCase(chatter))
				authorized = true;
		return authorized;
    }

    /**
     * 
     * Check if a user has been blocked from using ZmanimBot.
     * 
     * @param chatter username to check
     * @return true if blocked, false otherwise
     */
    boolean isBlocked(String chatter) {
    	boolean blocked = false;
    	for (String user:blockedUsers)
    		if (user.equalsIgnoreCase(chatter))
    			blocked = true;
    	return blocked;
    }
    
    /**
     * 
     * Perform admin commands
     * 
     * @param str
     * @param chatter
     * @return
     */
    String admin( String str, String chatter) {
		//Check that user is authorized
		if (!isAdmin(chatter))
			return "You must be part of the admins list to use this command.";
		
		else {
    		//Shutdown
    		if (str.startsWith("shutdown")) {
    			log(new Date() + "\tShut down by "+chatter+"\n");
    			System.exit(0);
    			return "shutting down...";
    		}

    		/*
    		 * Clear the location table of any entries. This forces ZmanimBot to redownload
    		 * location info, in case it has bad information due to network issues or the like. 
    		 */
    		else if (str.startsWith("clear cache")) {
    			ZmanimBot.clearCache();
    			return "locationTable successfully cleared.";
    		}

    		/*
    		 * Set an away message. (This is usually only used during debugging or the like.)
    		 */
    		else if (str.startsWith("away")) {
    			String awayMessage = (str.length()>4 ? str.substring(5) : "");
    			ZmanimBot.setStatus ("away",awayMessage);
    			return "Away message set: "+awayMessage;
    		}

    		/*
    		 * Set an available message (similar to away message, but bot appears as "available"
    		 * instead of "away" 
    		 */
    		else if (str.startsWith("available")) {
    			String awayMessage = (str.length()>9 ? str.substring(10) : "");
    			ZmanimBot.setStatus ("available",awayMessage);
    			return "Available message set: "+awayMessage;
    		}
    		
    		/*
    		 * Set an available message, and send it to twitter as well
    		 */
    		else if(str.startsWith("twitter")) {
    			String awayMessage = (str.length()>7 ? str.substring(8) : "");
    			ZmanimBot.setStatus ("available",awayMessage, true); //Putting true here sets twitter as well
    			return "Twitter message set: "+awayMessage;
    		}
    		
    		/*
    		 * Reload the zmanim names table used by ZmanimParser. (This is used if you made changes
    		 * locally and want them to reflect to the bot.)
    		 */
    		else if (str.startsWith("reload table")) {
    			Hashtable<String,String> temp = new Hashtable<String,String>(parser.getNamesTable());
    			try {
    				parser.loadHashtable();
    				return "Hashtable reloaded.";
    			} catch (Exception ex) {
    				parser.setNamesTable( temp);
    				return "Loading hashtable failed. Reverting to old table.";
    			}
    		}
    		
    		/*
    		 * Set the default zmanim list (when users use the "zmanim" command without further specifying
    		 * which zmanim they want)
    		 */
    		else if (str.startsWith("set list")) {
    			String list = (str.length()>9? str.substring(9):"alos hashachar, sunrise, shema gra, tefilla gra, chatzos, mincha gedola, sunset, tzeis");
    			parser.setStandardList(list);
    			return "Standard Zmanim List set to: "+list;    			
    		}

    		/*
    		 * Send a message to a particular user.
    		 */
    		else if (str.startsWith("message") || str.startsWith("send")) {
    			String dest = str.split(" ")[1];
    			String message = str.substring(dest.length()+9);
    			log("Sending message to "+dest+":\t"+message);
    			try {
    				ZmanimBot.sendMessage(dest,message);
    				return "Message sent.";
    			} catch (Exception ex) {log("Trying to send admin message: "+ex);return "Sending failed.";}
    		}
    		
    		/*
    		 * Cause all XMPP Bots to disconnect and reconnect from their servers. You can 
    		 * specify a wait time (in seconds) for each bot to wait before they reconnect.
    		 */
    		else if (str.startsWith("cycle")) {
    			String[] spl=str.split(" ");
    			long wait;
				if (spl.length>1) {
					try {
    					wait = Long.parseLong(spl[1]);
					} catch (NumberFormatException ex) {
						wait = 0;
					}
				} else {
					wait = 0;
				}
    			ZmanimBot.cycleXMPPs(wait);
    			return "XMPP Bots were cycled.";
    		}
    		return "Unknown admin command.";
    	}
    }


}

