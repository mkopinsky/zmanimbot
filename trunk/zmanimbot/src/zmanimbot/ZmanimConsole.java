package zmanimbot;

import java.io.*;

/**
 * Console application that parses user input according to normal methods, but doesn't
 * require a network connection or any protocol communication. (For development, primarily.)
 */
public class ZmanimConsole extends ZmanimMessageListener {
	
	String[] admins= {"ConsoleUser"};
	
	public ZmanimConsole () {
		parser = new ZmanimParser();
		hp = new HebcalProvider();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line="";
		while (true) {
			System.out.print("Enter command: ");
			try {
	            line=br.readLine();
	            if (line.equalsIgnoreCase("exit")) {
	            	System.out.println("Goodbye.");
	            	break;
	            }
	            System.out.println(parse(line,"ConsoleUser"));
            }
            catch (IOException e) {
	            e.printStackTrace();
            }
		}
	}
	/**
	 * Auto generated method comment
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ZmanimConsole zc = new ZmanimConsole();
	}
	
}
