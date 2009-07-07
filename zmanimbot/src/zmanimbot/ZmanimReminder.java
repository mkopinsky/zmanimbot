/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
/**
 *
 * @author lkopinsk
 */
public class ZmanimReminder extends TimerTask{

	/////////////////////////////////////////////
	//Global variables
    private GeoLocation m_location;
    private String m_delayName;
    private int m_alertMinutes;
    public int getAlertMinutes(){return m_alertMinutes;}
    public String getDelayName(){return m_delayName;}
    
    private ZmanimParser parser = ZmanimBot.getZmanimParser();
    
    private String m_name;
    public String getName(){return m_name;}
    public void setName(String name){m_name=name;}
    
    
    private final static long fONCE_PER_DAY = 1000*60*60*24;
    private final static long fONCE_PER_HOUR = 1000*60*60;
    private final static long fONCE_PER_MINUTE = 1000*60;
    private final static long fONCE_PER_SECOND = 1000;

	//////////////////////////////////////////////
	//Class methods
    public ZmanimReminder(String name)
    {
        m_name = name;
    }
    public void run() {
        System.out.println("Timer Event");
        
        try
        {
            DateFormat df = DateFormat.getTimeInstance();
//            ZmanimBot.bot.sendMessage(m_name,"REMINDER! It is now " + this.getAlertMinutes() + "minutes before " + this.getDelayName());
//            ZmanimBot.bot.sendMessage(m_name,"Current Time is: " + df.format(new Date(System.currentTimeMillis())));
//            ZmanimBot.bot.sendMessage(m_name,"Next Reminder is set for "+df.format(this.getNextTime()));
            Timer t = new Timer();
            t.schedule(this, this.getNextTime());
        }
        catch(Exception ex)
        {System.out.println(new Date()+"\tCaught in ZmanimReminder.run():");ex.printStackTrace();}

    }
                
    /* Parses a time string to set the reminder time
     * Format:
     *  <Time Name> [minutes before time]
     *      Valid Time Names: "Alos","Sunrise","Shema Gra","Shema Mga","Chatzos","Mincha Gedola","Sunset","Tzeis"
     */
    public void setTime(String timeString, GeoLocation gl)
    {
        m_location = gl;
//        try
//        {
            timeString = timeString.toLowerCase();
            String[] acceptableNames = {"alos 72", "alos", "dawn", 
            							"sunrise", "hanetz", "netz", 
            							"shema gra", "shema mga", "shema",
            							"tefillah gra", "tefillah mga", "tefillah", 
            							"chatzos", "midday",
            							"mincha gedola", "mincha ketana", "plag hamincha", "plag", "earliest mincha",
            							"sunset", "shkiah",
            							"tzeis", "tzeis 72", "rabbeinu tam"};
            
            for (int i = 0; i<acceptableNames.length; i++) {
            	if (timeString.startsWith(acceptableNames[i])){
            		m_delayName = acceptableNames[i];
            		try {
            			//look for number immediately after the zman
            			m_alertMinutes = Integer.parseInt(timeString.substring(m_delayName.length()+1));
            		} 
            		catch (NumberFormatException ex) { //if a number was not specified
            			m_alertMinutes = 0;
            		}
            		
            		break; //out of for loop
            	}
            }
            
/*            String[] timeStringSplit = timeString.split(" ");
            int count = timeStringSplit.length;
            switch(count)
            {
                case 1:
                    m_delayName=timeString;
                    m_alertMinutes=0;
                    break;
                case 2:
                    if(timeString.startsWith("mincha gedola") ||
                            timeString.startsWith("shema gra") ||
                            timeString.startsWith("shema mga") )
                    {
                        m_delayName = timeStringSplit[0] + " " + timeStringSplit[1];
                        m_alertMinutes=0;
                        break;
                    }
                    m_delayName = timeStringSplit[0];
                    m_alertMinutes = Integer.parseInt(timeStringSplit[1]);
                    break;
                case 3:
                    m_delayName = timeStringSplit[0] + " " + timeStringSplit[1];
                    m_alertMinutes = Integer.parseInt(timeStringSplit[2]);
                    break;
            }
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
*/    }
    
    public Date getNextTime()
    {
        ZmanimCalendar cal = new ZmanimCalendar(m_location);
        Date nextTime = getNextTime(cal);
        
        //Make sure next time is in the future
        if(nextTime.getTime() > System.currentTimeMillis())
            return nextTime;
        
        Calendar cal2 = cal.getCalendar();
        cal2.setTimeInMillis(cal2.getTimeInMillis() + fONCE_PER_DAY);
        cal.setCalendar(cal2);
        nextTime = getNextTime(cal);
        if(nextTime.getTime() > System.currentTimeMillis())
            return nextTime;
        
        cal2.setTimeInMillis(cal2.getTimeInMillis() + fONCE_PER_DAY);
        cal.setCalendar(cal2);
        nextTime = getNextTime(cal);
        if(nextTime.getTime() > System.currentTimeMillis())
            return nextTime;
        else
            System.out.println("HUH?");
        return null;
    }
    
    private Date getNextTime(ZmanimCalendar cal)
    {
        Date nextTime = parser.getZman(m_delayName,cal);

        nextTime.setTime(nextTime.getTime()-m_alertMinutes*60*1000);
        return nextTime;
    }
}
