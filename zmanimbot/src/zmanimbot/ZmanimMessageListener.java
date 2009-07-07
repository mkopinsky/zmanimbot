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


	/**
	 *Constructor
	 *
	 */
	public ZmanimMessageListener() {
		parser = ZmanimBot.getZmanimParser();
	} 
	

	/**
	 * Parse a command from a user.
	 * 
	 * @param str the command
	 * @param chatter the user who sent the command
	 */
	public void parse(String str, String chatter) {
		System.out.println(new SimpleDateFormat("[M/d/yy hh:mm:ss aaa]").format(new Date())+" "+chatter+": "+str);
    	String lowerCase = str.toLowerCase().trim();
    	
    	String param = (str.split(" ").length>1 ? str.substring(str.indexOf(" ")+1) : "" );
    	try {
    		if (isBlocked(chatter)) {
    			ZmanimBot.sendMessage(chatter, blocked(param,chatter));
    		}

	    	else if (lowerCase.startsWith("help") || lowerCase.startsWith("?")) {
	    		ZmanimBot.sendMessage(chatter, help(param));
	    	}
	    	
	    	else if (lowerCase.startsWith("list")){
	    		ZmanimBot.sendMessage(chatter, list(param));
	    	}
	    	
	    	else if (lowerCase.startsWith("credits") || lowerCase.startsWith("about")) {
	    		ZmanimBot.sendMessage(chatter, credits(param ));
	    	}
	    	
	    	else if(lowerCase.startsWith("comment") || lowerCase.startsWith("feedback")){
	    		ZmanimBot.sendMessage(chatter, comment( param, chatter));
	    	}
	    		    	
	    	else if (lowerCase.startsWith("map")) {
	    		ZmanimBot.sendMessage(chatter, map(param) );
	    	}
	    	
	    	else if (lowerCase.startsWith("zmanim")) {
	    		ZmanimBot.sendMessage(chatter, zmanim(param));
	    	}
	    	else if (lowerCase.startsWith("admin")) {
	    		ZmanimBot.sendMessage(chatter, admin(param, chatter ));
	    	}
	    	else if (lowerCase.startsWith("hello") || lowerCase.startsWith("hi") || lowerCase.startsWith("hey")) {
	    		ZmanimBot.sendMessage(chatter, hello(param));
	    	}
	    	else { 
	    		ZmanimBot.sendMessage(chatter, zmanim(str));
	    	}
	    } catch (Exception ex) {System.out.println("Caught in ZmanimMessageListener.parse():");ex.printStackTrace();}
    	
    	
/*        //reminder
    	else if(lowerCase.startsWith("remind")){
            //Add the current chatter to the reminder list
            if(!ZmanimBot.m_reminderBots.keySet().contains(chatter))
            {
                System.out.println("Adding " + chatter + " to reminder list.");
                m_reminderBots.put(chatter, new ReminderBot(chatter,gchat));
            }
            String[] arguments = lowerCase.split("-");
             
            String locationString = "";
            String timeString = "";
            for (String s : arguments)
            {
                if(s.startsWith("l"))
                    locationString = s.substring(1).trim();
                else if(s.startsWith("t"))
                    timeString = s.substring(1).trim();
            }
            
            GeoLocation g = ZmanimBot.getGeoLocation(locationString);
            
            ZmanimReminder zr = new ZmanimReminder(chatter, gchat);
            zr.setTime(timeString, g);
            m_reminderBots.get(chatter).addReminder(zr);
            DateFormat df = DateFormat.getTimeInstance();
            Date temp = zr.getNextTime();
            System.out.println( "Reminder Set. Next Reminder at " + df.format(zr.getNextTime()));
            return "Reminder Set. Next Reminder at " + df.format(zr.getNextTime());
    	}
  */  	

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
		"To leave a comment/bug report for us, type \"comment\" and then your comment.";
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
			"Zvi Rosen is evil.",
			"No zmanim for you!",
			"An important message for "+chatter+": Turn off your goyishe music and stop being mevatel Torah."
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
    
    String zmanim(String str) {
	    try {
	    	String[] strArray = str.split("[:;|]");
	    	for (int i =0; i<strArray.length;i++)
	    		strArray[i]=strArray[i].trim();

			GeoLocation loc = ZmanimBot.getGeoLocation(strArray[0].toUpperCase().trim());
			System.out.println('{'+loc.getLocationName()+'}');
	        ZmanimCalendar cal = new ZmanimCalendar(loc);

			DateFormat df = DateFormat.getTimeInstance();
	        SimpleDateFormat sdf = new SimpleDateFormat("MMMMM dd, yyyy");

	        Calendar c = cal.getCalendar();
	        Date d;
	        
	        switch (strArray.length) {
	        	case 1: {
	        		return  "Zmanim for " + loc.getLocationName() + "\n" +
	                	sdf.format(new Date()) + 
	                	parser.getZmanimString(cal);
	        	}
	        	
	        	case 2: {
		        	if ((d = parser.parseDate(strArray[1]))!=null) { //"new york:tomorrow"
				        return  "Zmanim for " + loc.getLocationName() + "\n" +
				                sdf.format(d) +
				                parser.getZmanimString(cal,d);
			        }
			        else { 									//"new york:shema"
				        return  "Zmanim for " + loc.getLocationName() + "\n" +
				                sdf.format(new Date()) + 
				                parser.getZmanimString(strArray[1],cal);
			        }
	        	}
	        	
	        	case 3:
	        	default: {
		        	if ((d = parser.parseDate(strArray[1]))!=null) { //"new york:tomorrow:shema"
				        return  "Zmanim for " + loc.getLocationName() + "\n" +
				                sdf.format(d) + 
				                parser.getZmanimString(strArray[2],cal,d);
			        }
		        	else if ((d = parser.parseDate(strArray[2]))!=null) { //"new york:shema:tomorrow"
				        return  "Zmanim for " + loc.getLocationName() + "\n" +
				                sdf.format(d) + 
				                parser.getZmanimString(strArray[1],cal,d);
			        	} 
			        else return "Invalid syntax. See zmanimbot.blogspot.com for more details.";
			    }
	        }
	        
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	return "Unknown command or location. Type \"help\" for a list of commands.";
	    }
    	
    }
    
    
    boolean isAdmin(String chatter) {
		boolean authorized = false;
		for (String admin:admins)
			if (admin.equalsIgnoreCase(chatter))
				authorized = true;
		return authorized;
    }
    
    boolean isBlocked(String chatter) {
    	boolean blocked = false;
    	for (String user:blockedUsers)
    		if (user.equalsIgnoreCase(chatter))
    			blocked = true;
    	return blocked;
    }
    
    String admin( String str, String chatter) {
		//Check that user is authorized
		if (!isAdmin(chatter))
			return "You must be part of the admins list to use this command.";
		
		else {
    		//Shutdown
    		if (str.startsWith("shutdown")) {
    			System.out.println(new Date() + "\tShut down by "+chatter+"\n");
    			System.exit(0);
    			return "shutting down...";
    		}
    		
    		else if (str.startsWith("clear cache")) {
    			ZmanimBot.clearCache();
    			return "locationTable successfully cleared.";
    		}
    		
    		else if (str.startsWith("away")) {
    			String awayMessage = (str.length()>4 ? str.substring(5) : "");
    			ZmanimBot.setStatus ("away",awayMessage);
    			return "Away message set: "+awayMessage;
    		}

    		else if (str.startsWith("available")) {
    			String awayMessage = (str.length()>9 ? str.substring(10) : "");
    			ZmanimBot.setStatus ("available",awayMessage);
    			return "Available message set: "+awayMessage;
    		}
    		
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
    		
    		else if (str.startsWith("set list")) {
    			String list = (str.length()>9? str.substring(9):"alos hashachar, sunrise, shema gra, tefilla gra, chatzos, mincha gedola, sunset, tzeis");
    			parser.setStandardList(list);
    			return "Standard Zmanim List set to: "+list;    			
    		}
    				
    		else if (str.startsWith("message") || str.startsWith("send")) {
    			String dest = str.split(" ")[1];
    			String message = str.substring(dest.length()+9);
    			System.out.println("Sending message to "+dest+":\t"+message);
    			try {
    				ZmanimBot.sendMessage(dest,message);
    				return "Message sent.";
    			} catch (Exception ex) {System.out.println("Trying to send admin message: "+ex);return "Sending failed.";}
    		}
    		return "Unknown admin command.";
    	}
    }


}

