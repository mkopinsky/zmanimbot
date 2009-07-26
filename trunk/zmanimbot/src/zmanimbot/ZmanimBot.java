/**
* ZmanimBot main class
*
* Links to API Javadocs:
*		http://www.kosherjava.com/zmanim/docs/api/
*		http://www.igniterealtime.org/builds/smack/docs/latest/javadoc/
*
* Things to implement now:
*
* Ideas for the future:
*		Alert for x minutes before mincha
*		Ability to save user data ("save {sunrise, shma_gra, minchagedola, shkia, tzeisRabbeinuTam} 19104", and
*				when I come back tomorrow and type "zmanim" it gives me all that
*		Parshat Hashavua, Daf Yomi, additions to davening, etc.
*		Natural language support ("When is shkiah in Los Angeles?")
*		Change architecture to think in chats, rather than messages (User:"shkiah" Bot:"What location?" User:"19104" 
*					Bot:"Shkiah in Philadelphia is...")
*		Interface with GoDaven.com for minyan times and locations
*		
*
* @author Michael Kopinsky
* @version 1.00 2/7/2008
*/
package zmanimbot;

import java.io.*; 
import java.util.*;
import java.text.*;
import java.net.URL;
import java.net.URLEncoder;
import net.sourceforge.zmanim.*;
import net.sourceforge.zmanim.util.*; 

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.*;
import org.jivesoftware.smack.packet.*;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;

/**
 * Main class that runs the entire ZmanimBot program.
 */
public class ZmanimBot {
    ////////////////////////////////////////////////////
    //Global variables
	static Hashtable<String, GeoLocation> locationTable = new Hashtable<String, GeoLocation>(0);
	static final String GoogleMapsAPIKey = "ABQIAAAA5FyU4V9CG_Ib25k4CSzLSRRcyQuJXIrjqIosfS0WHLeFIsXZ9hR_mXYe8-G2pa7Bn-NfZpplWQ6qig";
	private static Map<String,Bot> m_bots;
    
//	static Bot gchatbot;
//	static Bot aimbot;
	static Bot twitterbot;
    private static ZmanimParser parser;
	public static ZmanimParser getZmanimParser() { return parser; }
    
	private static HebcalProvider hp;
	public static HebcalProvider getHebcalProvider() { return hp; }
						
    ////////////////////////////////////////////////////
    //Main method
    public static void main(String[] args) {
        System.out.println(new Date()+"\tStarting ZmanimBot...");
        parser = new ZmanimParser();
        hp = new HebcalProvider();
        readLocationsFile("resources/IL1.txt");
        m_bots = InitializeBots("passwords.txt");


    }
    
    /** HashMap<String,Bot> InitializeBots(String filename)
     * Initialize Bots based on the password file.
     * 
     * @param filename String filename with all passwords
     */
    private static HashMap<String,Bot> InitializeBots(String filename)
    {
    	HashMap<String,Bot> bots = new HashMap<String,Bot>();
    	BufferedReader reader = null;
        try{
        	
			reader = new BufferedReader(new InputStreamReader (new FileInputStream(filename)));
    		
			//Read password file and put bots into m_bots map
    		String line;
    		while ((line=reader.readLine())!=null) {
    			if (line.length()==0 || line.startsWith("#"))
    				continue;
    			String[] ls = line.split("[\t ,;]");
    			if (ls.length<4) {
    				System.out.println(ls.length);
    				System.out.println("Invalid syntax in passwords file. Line was: " + line);
    				continue;
    			}
    			if (ls[0].equalsIgnoreCase("AIM"))
    				bots.put(ls[1], new AIMBot(ls[2],ls[3]));
    			else if (ls[0].equalsIgnoreCase("XMPP"))
    				bots.put(ls[1], new XMPPBot(ls[1],ls[2],ls[3]));
    			else if (ls[0].equalsIgnoreCase("twitter")) //for other non-protocol passwords
    			{
    				twitterbot = new TwitterBot(ls[2], ls[3]);	
    			}
    			else
    				System.out.println("Invalid entry in passwords file. Line was: " + line);
    				
    		}
    		return bots;
            
        } 
        catch (Exception ex) 
        {
        	System.out.println(
        		new Date()+"\tCaught in ZmanimBot(): ");
        	ex.printStackTrace();
        }
        finally 
        {
        	if(reader!=null)
        	{
				try 
        		{
					reader.close();
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
		}
        return null;
    }


    /** GeoLocation getGeoLocation(String s)
     *
     *	Looks up a location in the custom location Hashtable, and if it's not found there, looks it up using
     *		Google Maps API, and the GeoNames webservice.
     *
     *	@param s String Location to look up.
     *
     */
    public static GeoLocation getGeoLocation(String s)
    {
        GeoLocation loc = (GeoLocation)locationTable.get(s.toUpperCase().trim());
        if (loc!=null) {
                return loc;
        }
        else
        {
            try
            {
	            //download data from Google maps and GeoNames.  This needs additional errorchecking.
	            //Open connection to Google maps, download latitude, longitude, and locname
	            URL GoogleMapsXML = new URL("http://maps.google.com/maps/geo?q="+URLEncoder.encode(s,"UTF-8")+"&output=xml&key="+GoogleMapsAPIKey);
//	            System.out.println("Fetching URL: "+GoogleMapsXML);
	            BufferedReader reader = new BufferedReader(new InputStreamReader(GoogleMapsXML.openStream()));
	            String line;
	            while ((line=reader.readLine())!=null && !line.contains("<address>")) {
	                    ;
	            }
//	            System.out.println("Here's the relevant line: "+line);
	            String locName = line.substring(line.indexOf("<address>")+9,line.indexOf("</address>"));
	            while ((line=reader.readLine())!=null && !line.contains("<coordinates>")) {
	                    ;
	            }
//	            System.out.println("Here's the relevant line: "+line);
	            String coords[] = line.substring(line.indexOf("<coordinates>")+13,line.indexOf("</coordinates>")).split(",");
	
	            //String csv[] = reader.readLine().split(",");
	            reader.close();
	            
	            if (locName==null || coords[0]==null ||coords[1]==null) {
	            	throw new Exception("Error downloading data from Google maps.");
	            }
	            
	
	            //Open connection to GeoNames, download time zone info for lat and long just found from google
	            URL GeonamesTimezone = new URL("http://ws.geonames.org/timezone?lat="+coords[1]+"&lng="+coords[0]);
//	            System.out.println("Fetching URL: "+GeonamesTimezone);
	            reader = new BufferedReader(new InputStreamReader(GeonamesTimezone.openStream()));
	            while ((line=reader.readLine())!=null && !line.contains("timezoneId")) {
	                    ;
	            }
	            reader.close();
	            
	            TimeZone tz = TimeZone.getTimeZone(line.substring(line.indexOf("<timezoneId>")+12,line.lastIndexOf("</timezoneId>")));
	
	            //Create GeoLocation and ZemanimCalendar, and print zemanim
	            loc = new GeoLocation (locName,Double.parseDouble(coords[1]),Double.parseDouble(coords[0]),tz);
	            
	            //Cache the location in locationTable
	            locationTable.put(s.toUpperCase().trim(),loc);
	        } catch(Exception ex){
	        	System.err.println(new Date()+"\tException caught in getGeoLocation: ");
	        	System.err.println(ex);
	        	System.err.println("at " + ex.getStackTrace()[1]);
	        	System.err.println("at " + ex.getStackTrace()[2]);
	        	System.err.println("at " + ex.getStackTrace()[3]);
	        	System.err.println("... " + (ex.getStackTrace().length-3) + " more.");
	        }
        }
		return loc;
    }
    

    /** void readLocationsFile(String filename)
     *
     *	Reads location data from a CSV file, and puts GeoLocations into locationTable HashTable
     *
     *	The format must be:
     *		ZIP_CODE, CITY, STATE, TIME_ZONE, LATITUDE, LONGITUDE
     *	The first line is assumed to be a header 
     *
     * This method is used to supplement data that is lacking in Google Maps. For example, 
     * Google doesn't have great data on locations in Israel, so we can have a text file with
     * the data and read it in manually.
     */
    static void  readLocationsFile(String filename) {
    	System.out.println(new Date() + "\tReading in locations from "+filename);
    	try{
			BufferedReader reader = new BufferedReader(new InputStreamReader (new FileInputStream(filename), "UTF-16"));
    		
    		String line = reader.readLine();
    		
    		//I need to find latitude, longitude, name, names, and timezone
    		String[] data = line.split("\t");
    		int index_lat=-1, index_long=-1, index_name=-1, index_altnames=-1, index_tz=-1;
    		
    		for (int i=0; i<data.length; i++) {
    			if (data[i].equalsIgnoreCase("LATITUDE")) {
    				index_lat = i;
    				break;
    			}
    		}
    		
    		for (int i=0; i<data.length; i++) {
    			if (data[i].equalsIgnoreCase("LONGITUDE")) {
    				index_long = i;
    				break;
    			}
    		}
    		
    		for (int i=0; i<data.length; i++) {
    			if (data[i].equalsIgnoreCase("NAME")) {
    				index_name = i;
    				break;
    			}
    		}
    		for (int i=0; i<data.length; i++) {
    			if (data[i].equalsIgnoreCase("ALT NAMES")) {
    				index_altnames = i;
    				break;
    			}
    		}
    		for (int i=0; i<data.length; i++) {
    			if (data[i].equalsIgnoreCase("TIMEZONE")) {
    				index_tz = i;
    				break;
    			}
    		}
    		
    		
    		line=reader.readLine();
    		while (line!=null) {
    			data = line.split("\t");
    			
    			GeoLocation loc = new GeoLocation(data[index_name], Double.parseDouble(data[index_lat]),
    									Double.parseDouble(data[index_long]),TimeZone.getTimeZone(data[index_tz]));
    			//check that there isn't already an entry in the hashtable with this zip code/city,state
    			//(not *really* necessary, but this should improve speed)
    			String altnames = data[index_altnames];
    			if (!altnames.isEmpty() && altnames.charAt(0)=='\"') 
    				altnames = altnames.substring(1,altnames.length()-1);
    			String[] altnamesarray = altnames.split(",");
    			
    			if (!locationTable.containsKey(data[index_name].toUpperCase())) {
					  	 locationTable.put(data[index_name].toUpperCase(),loc);
     			}
     			
     			for (String altname : altnamesarray) {
	    			if (!locationTable.containsKey(altname.trim().toUpperCase())) {
	    				locationTable.put(altname.trim().toUpperCase(),loc);
	    			}
     				
     			}
    			
    			line=reader.readLine();	
    				
				}
			reader.close();
			} catch (Exception ex) {ex.printStackTrace();}
    }
    
    
	/**
	 * Sends a message to a recipient, routing it through the correct protocol using the 
	 * protocol suffix appended elsewhere in the program, or by the admins when using the
	 * "send message" command.
	 * 
	 * @param chatter The user to set the message to, including the protocol suffix
	 * @param message The message to send.
	 */
    public static void sendMessage (String chatter, String message) throws Exception {    	
    	String suffix = chatter.split("@")[1];

    	if (m_bots.containsKey(suffix)) {
    		m_bots.get(suffix).send(chatter, message);
    	}
    	else if(suffix.contains("twitter"))
	    	twitterbot.send(chatter.substring(1), message);
    	else {
    		m_bots.get("gmail.com").send(chatter, message);
    	}
    }
    
	/**
	 * Set away/available status for all bots.
	 * 
	 * @param status String representation of the type of status to set. Currently only 
	 * 			"Away" and "Available" are supported.
	 * @param message The text accompanying the status message. Can be "" if none desired.
	 */
    public static void setStatus(String status, String message) {
    	setStatus(status,message,false);
    }
    
    /**
	 * Set away/available status for all bots.
	 * 
	 * @param status String representation of the type of status to set. Currently only 
	 * 			"Away" and "Available" are supported.
	 * @param message The text accompanying the status message. Can be "" if none desired.
	 */
    public static void setStatus(String status, String message, Boolean updateTwitter) {
    	//Set status on all bots.
    	for(Bot b : m_bots.values())
    		b.setStatus(status, message);
    	
    	if(updateTwitter)
    		twitterbot.setStatus(status, message);
    }
    
    /**
     * Clear the cache of stored locations. This is sometimes necessary when reloading text
     * files or the like, or if there's some error with the data stored. This is invoked by 
     * using the "admin clear cache" command.
     */
    public static void clearCache() {
    	locationTable.clear();
    }


    /**
     * 
	 * Cause all XMPP Bots to disconnect and reconnect from their servers. You can 
	 * specify a wait time (in seconds) for each bot to wait before they reconnect.
     * 
     * @param wait number of seconds to wait
     */
    public static void cycleXMPPs(long wait) {
    	for (Bot b : m_bots.values())
    		if (b instanceof XMPPBot)
    			((XMPPBot)b).cycleConnection(wait);
    	
    }
    
    
    
}
