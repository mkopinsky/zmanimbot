/** class ZmanimParser
 *
 *
 *	Parses a string and returns the zman associated with the string.
 *
 *	Not to be confused with ZmanimMessageListener, which processes user commands,
 *		which may be requests for zmanim or any other requests (help, admin, etc.)
 *
 */
package zmanimbot;

import java.util.*;
import java.io.*;
import java.text.*;
import java.net.*;
import java.awt.*;
import net.sourceforge.zmanim.*;
import net.sourceforge.zmanim.util.*; 

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.*;
import org.jivesoftware.smack.packet.*;




public class ZmanimParser {

	String zmanimFile = "resources/zman.txt";
	
    final static String[] acceptableNames = {"Alos 72", "Alos Hashachar", "Sunrise", "Shema GRA","Shema MGA",
					"Tefilla GRA", "Tefilla MGA", "Chatzos", "Mincha Gedola", "Mincha Ketana", "Plag HaMincha",
					"Candle lighting", "Sunset", "Tzeis", "Rabbeinu Tam"};
	public String[] getAcceptableNames() {return acceptableNames;}
	
	private String standardZmanimList = "alos hashachar, sunrise, shema gra, tefilla gra, chatzos, mincha gedola, sunset, tzeis";
	public String getStandardList() {return standardZmanimList;}
	public void setStandardList(String str) {standardZmanimList=str;}


	private Hashtable<String,String> namesTable = new Hashtable<String,String>();
	public Hashtable<String,String> getNamesTable() { return namesTable;}
	public void setNamesTable (Hashtable<String,String> ht) { namesTable = ht;}
	
	public ZmanimParser() {
		try {
			loadHashtable();
		} catch (Exception ex) {
			System.err.println("\n"+new Date() +"\tCaught in ZmanimParser():");
			ex.printStackTrace();
			System.exit(1);
		}
	}

	public ZmanimParser(String filename) {
		try {
			loadHashtable(filename);
		} catch (Exception ex) {
			System.err.println("\n"+new Date() +"\tCaught in ZmanimParser():");
			ex.printStackTrace();
			System.exit(1);
		}
	}
	
	public void loadHashtable() throws Exception {
		loadHashtable(zmanimFile);
	}
	
	
	public void loadHashtable(String filename) throws Exception {
		namesTable.clear();
		
//		System.out.println(new Date()+"\tReading in zmanim list from "+filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader (new FileInputStream(filename), "UTF-16"));
		String line;
		while ((line=reader.readLine()) != null) {
			if (line.charAt(0) == '#')
				continue;
			String[] names = line.split(",");
			for (int i = 0; i<names.length; i++) {
				if (names[i].trim().length()>0)
					namesTable.put(names[i].trim().toLowerCase(),names[0]);
			}
		}
	}
	
	public Date getZman(String zmanName, ZmanimCalendar cal, boolean lenient) {
		if (lenient==true)
			if (namesTable.containsKey(zmanName.trim().toLowerCase()))
				zmanName = namesTable.get(zmanName.trim().toLowerCase());
		return getZman(zmanName, cal);
	}
	
	
	//This method is strict, ie it does not accept alternate spellings of zmanName
    public Date getZman(String zmanName, ZmanimCalendar cal) {
    	Date theZman = new Date();

        if(zmanName.equalsIgnoreCase("alos hashachar"))
        {
            theZman = cal.getAlosHashachar();
            if (theZman==null)
            	System.out.println("Null alos hashachar");
        }
        else if(zmanName.equalsIgnoreCase("alos 72"))
        {
            theZman = cal.getAlos72();
        }
        else if(zmanName.equalsIgnoreCase("sunrise"))
        {
            theZman = cal.getSunrise();
        }
        else if(zmanName.equalsIgnoreCase("shema gra"))
        {
            theZman = cal.getSofZmanShmaGRA();
        }
        else if(zmanName.equalsIgnoreCase("shema mga"))
        {
            theZman = cal.getSofZmanShmaMGA();
        }
        else if(zmanName.equalsIgnoreCase("tefilla gra"))
        {
            theZman = cal.getSofZmanTfilaGRA();
        }
        else if(zmanName.equalsIgnoreCase("tefilla mga"))
        {
            theZman = cal.getSofZmanTfilaMGA();
        }
        else if(zmanName.equalsIgnoreCase("chatzos"))
        {
            theZman = cal.getChatzos();
        }
        else if(zmanName.equalsIgnoreCase("mincha gedola"))
        {
            theZman = cal.getMinchaGedola();
        }
        else if(zmanName.equalsIgnoreCase("mincha ketana"))
        {
            theZman = cal.getMinchaKetana();
        }
        else if(zmanName.equalsIgnoreCase("plag hamincha"))
        {
            theZman = cal.getPlagHamincha();
        }
        else if(zmanName.equalsIgnoreCase("candle lighting"))
        {
            theZman = cal.getCandelLighting();
        }
        else if(zmanName.equalsIgnoreCase("sunset"))
        {
            theZman = cal.getSunset();
        }
        else if(zmanName.equalsIgnoreCase("tzeis"))
        {
            theZman = cal.getTzais();
        }
        else if(zmanName.equalsIgnoreCase("Rabbeinu Tam"))
        {
            theZman = cal.getTzais72();
        }
        else {
        	theZman = null;
        }
        return theZman;
    }

    String getZmanimString(String zmanimList, ZmanimCalendar cal){
    	String retStr = "";
    	DateFormat df = new SimpleDateFormat("h:mm:ss");  //previously was "h:mm:ss aaa"
    	String[] zmanim = zmanimList.split("[,;]");


		for (String zman : zmanim) {
			if (namesTable.containsKey(zman.trim().toLowerCase())) {
				String name = namesTable.get(zman.trim().toLowerCase());
				retStr += "\n" + name + ":\t" + df.format(getZman(name, cal));
//				retStr += "\n" + name + ":\t" + getZman(name, cal).toString();
			}
			else retStr += "\n" + zman +":\tInvalid zman name";
		}
		
		return retStr;
    }

	String getZmanimString(ZmanimCalendar cal) {
		return getZmanimString(standardZmanimList, cal);
	}
	
	String getZmanimString(String zmanimList, ZmanimCalendar cal, Date d) {
		Calendar c = cal.getCalendar();
		c.setTime(d);
		c.setTimeZone(cal.getGeoLocation().getTimeZone());
		return getZmanimString(zmanimList, cal);
	}
	
	String getZmanimString(ZmanimCalendar cal, Date d) {
		return getZmanimString(standardZmanimList,cal,d);
	}
	
	
    public Date parseDate(String str) {
		Date d;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date today;
        try {
        	/*
        	 *When we only need to parse or display dates, the time doesn't make a difference.
        	 *But when we need to look up things in a hashmap based on them (such as in 
        	 *HebcalProvider, we need the dates to have no time. The following is a hack to 
        	 *get that to work.
        	 */
	        today = sdf.parse(sdf.format(new Date()));
        }
        catch (ParseException e) {
        	today = new Date();
        }
		if (str.equalsIgnoreCase("today")) {
			return today;
		}
		else if (str.equalsIgnoreCase("tomorrow")) {
			return new Date(today.getTime() + 24*60*60*1000);
		}
		else if (str.equalsIgnoreCase("yesterday")) {
			return new Date(today.getTime() - 24*60*60*1000);
		}
			 
		try {
			sdf = new SimpleDateFormat("MM-dd-yy");
			d =  sdf.parse(str);
		} catch(java.text.ParseException ex1) { try {
			sdf = new SimpleDateFormat("MM-dd-yyyy");
			d =  sdf.parse(str);
		} catch(java.text.ParseException ex2) { try {
			sdf = new SimpleDateFormat ("MM/dd/yy");
			d = sdf.parse(str);
		} catch(java.text.ParseException ex3) { try {
			sdf = new SimpleDateFormat ("MM/dd/yyyy");
			d = sdf.parse(str);
		} catch(java.text.ParseException ex4) { try {
			sdf = new SimpleDateFormat ("MMMMM dd, yy");
			d = sdf.parse(str);
		} catch(java.text.ParseException ex5) { try {
			sdf = new SimpleDateFormat ("MMMMM dd yy");
			d = sdf.parse(str);
		} catch(java.text.ParseException ex6) { try {
			sdf = new SimpleDateFormat ("MMMMM dd, yyyy");
			d = sdf.parse(str);
		} catch(java.text.ParseException ex7) { try {
			sdf = new SimpleDateFormat ("MMMMM dd yyyy");
			d = sdf.parse(str);
		} catch(java.text.ParseException ex8) { try {
			sdf = new SimpleDateFormat ("MMMMM dd yyyy");
			d = sdf.parse(str+" 2008");
		} catch(Exception ex) {
				d = null;
			}
		}}}}}}}}
		return d;
    	
    }

}
