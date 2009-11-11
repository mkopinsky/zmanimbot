package google.waveapps.zmanimbot;

import zmanimbot.HebcalProvider;
import zmanimbot.ZmanimParser;


public class WaveBot extends zmanimbot.ZmanimMessageListener {
	public WaveBot(String zmanimFile)
	{
		super();
		parser = new ZmanimParser(zmanimFile);
		hp = new HebcalProvider();
	}
}
