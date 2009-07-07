/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package zmanimbot;

import java.util.*;
import net.sourceforge.zmanim.*;
import net.sourceforge.zmanim.util.*; 
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.*;
import org.jivesoftware.smack.packet.*;

/**
 *
 * @author lkopinsk
 */
public class ReminderBot {
    private final static long fONCE_PER_DAY = 1000*60*60*24;
    private final static long fONCE_PER_HOUR = 1000*60*60;
    private final static long fONCE_PER_MINUTE = 1000*60;
    private final static long fONCE_PER_SECOND = 1000;
    
    Timer timer = new Timer();
    
    public ReminderBot(String name, XMPPConnection gChat)
    {
        m_name = name;
        m_reminders = new ArrayList<ZmanimReminder>();
        m_gChat = gChat;
    }
    
    //Reminders
    private List<ZmanimReminder> m_reminders;
    public void addReminder(ZmanimReminder reminder)
    {
        Calendar c = new GregorianCalendar();
        c.setTime(reminder.getNextTime());
//        System.out.println(reminder.getNextTime().toGMTString() +" " +c.DAY_OF_MONTH + " " + c.DAY_OF_WEEK + " " + c.HOUR_OF_DAY);
        timer.schedule(reminder, reminder.getNextTime());
    }
    public void removeReminder(ZmanimReminder reminder)
    {
        m_reminders.remove(reminder);
    }
    public void removeReminderAtIndex(int index)
    {
        m_reminders.remove(index);
    }
    public List<ZmanimReminder> getReminderList()
    {
        return m_reminders;
    }
    
    //Name
    private String m_name;
    public String getName(){return m_name;}
    public void setName(String name){m_name=name;}
    
    //Connection
    XMPPConnection m_gChat;
}
