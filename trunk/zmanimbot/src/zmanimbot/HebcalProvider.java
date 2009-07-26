package zmanimbot;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class HebcalProvider {
	private HashMap<Date,String> omerMap;
	private HashMap<Date,String> datesMap;
	private HashMap<Date,String> parshaMap;
	private HashMap<Date,String> holidaysMap;
	
	public HebcalProvider () {
		omerMap=loadMap("resources/hebcal_omer.txt");
		datesMap=loadMap("resources/hebcal_dates.txt");
		parshaMap = loadMap("resources/hebcal_parsha.txt");
		holidaysMap=loadMap("resources/hebcal_holidays.txt");
	}
	
	/**
	 * 
	 * Load a text file generated by hebcal and load it into a hashmap.
	 * 
	 * The format must be:
	 * 5/3/2009 Some text to store with that date
	 * 
	 * @param filename
	 * @return a HashMap with each date mapped to the corresponding info stored in the file  
	 */
	static HashMap<Date,String> loadMap(String filename) {
		HashMap<Date,String> map = new HashMap<Date,String>();
		try {
		BufferedReader reader = new BufferedReader(new InputStreamReader (new FileInputStream(filename)));
    	SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
		
		String line;
	        while ((line=reader.readLine()) != null) {
	        	
	        	map.put(sdf.parse(line.substring(0, line.indexOf(" "))),
	        		line.substring(line.indexOf(" ")+1));
	        	}
        }
        catch (IOException e) {
	        e.printStackTrace();
        }
        catch (ParseException e) {
        	e.printStackTrace();
        }
		return map;
	}
	
	public String getOmer(Date d) {
		if (omerMap.containsKey(d)) 
			return omerMap.get(d);
		else
			return null;
	}
	
	public String getHebrewDate(Date d) {
		return datesMap.get(d);
	}
	
	public String getParsha(Date d) {
		return parshaMap.get(d);
	}
	
	public String getHolidays(Date d) {
		return holidaysMap.get(d);
	}
}
