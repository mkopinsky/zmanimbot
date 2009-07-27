package zmanimbot;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Command line application that parses user input according to normal methods, but
 * outputs the response to stdout instead of to a user over a protocol. This allows
 * easy interfacing with CGI (for setting up SMS) or another gateway such as email.
 */
public class ZmanimCommandLine extends ZmanimMessageListener {
	
	
	public ZmanimCommandLine (String s) {
		parser = new ZmanimParser();
		hp = new HebcalProvider();
        System.out.println(parse(s,"Command Line"));
	}
	
	/**
	 * Normally, log messages can go to stdout, since the output of parse is being sent 
	 * elsewhere, but here, where the main output is to stdout, we need to repress these
	 * messages.
	 *  
	 * @see zmanimbot.ZmanimMessageListener#log(java.lang.String)
	 */
	protected void log(String s) {
		;
	}
	
	public static void main(String[] args) {
		String str="";
		for (String s:args)
			str+=s+" ";
		if (str.trim().length()==0)
			str = "hi";
		try {
	        ZmanimCommandLine zcl = new ZmanimCommandLine(URLDecoder.decode(str, "UTF-8"));
        }
        catch (UnsupportedEncodingException e) {
        	System.out.println("Any error occured! Please report this to the ZmanimBot team.");
        }
	}
	
}
