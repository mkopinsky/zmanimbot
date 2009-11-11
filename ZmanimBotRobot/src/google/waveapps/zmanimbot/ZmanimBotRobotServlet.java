package google.waveapps.zmanimbot;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.*;
import com.google.wave.api.*;


@SuppressWarnings("serial")
public class ZmanimBotRobotServlet extends AbstractRobotServlet  {

	WaveBot bot = new WaveBot("resources/zman.txt");
	String[] keywords = new String[]{ "zmanimbot","zbot" };
	String openingMessage = "I'm Alive!\n" + 
							"To get zmanim, type 'zbot' and then any command zmanimbot understands.\n" +
							"For a list of commands, type 'zbot help'.\n" +
							"Tizku l'Mitzvos!";

	@Override
	public void processEvents(RobotMessageBundle bundle) {
		Wavelet wavelet = bundle.getWavelet();
		
		if (bundle.wasSelfAdded()) {
			Blip blip = wavelet.appendBlip();
			TextView textView = blip.getDocument();
			textView.append(openingMessage);

			//TODO: Parse all messages in wavelet
		}
		
		for (Event e: bundle.getEvents()) {
			if (//e.getType() == EventType.DOCUMENT_CHANGED ||
				e.getType() == EventType.BLIP_SUBMITTED) {
				
				Blip blip = e.getBlip();
				processText(blip);
				/*Wavelet wavelet = bundle.getWavelet();
				Blip root = wavelet.getRootBlip();
				TextView textView = root.getDocument();
				
				
				Blip blip = wavelet.appendBlip();
				TextView textView2 = blip.getDocument();
				textView2.replace("ZBOT:\n");
				textView2.append(textView.getText());
				processText(wavelet,textView2);
				*/
			}
		}
	}
	
	private Blip getTargetBlip(Blip blip)
	{
		List<Blip> children = blip.getChildren();
		
		//First loop over current level
		for (Blip childBlip : children) {
			if(childBlip.getCreator().contains("zmanim") && 
				!childBlip.getDocument().getText().contains(openingMessage))
				return childBlip;
		}
		return null;
	}

	public void processText(Blip blip )
	{
		Blip newBlip = null;
		TextView newText = null;

		Blip targetBlip = getTargetBlip(blip);
		if(targetBlip !=null)
			newBlip = targetBlip;
		
		TextView document = blip.getDocument();
		String text = document.getText().toLowerCase();
		
		for (int i = 0; i < keywords.length; i++) {
			String keyword = keywords[i];
			
			if(text.contains(keyword))
			{
				if(newBlip == null)
				{
					newBlip = blip.createChild();
				}

				newText = newBlip.getDocument();
				
				int startPosition = text.indexOf(keyword) + keyword.length();
				String s = text.substring(startPosition);
				
				int endPosition = s.indexOf("\n");
				if(endPosition > -1)
					s = s.substring(endPosition);
				s = s.trim();
				String output = bot.parse(s, "WaveBot");
				if(output != "")
				{
					newText.replace(output);
				}
			}
		}

	}
}
