package zmanimbot.users;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;


public class User {
	
	private String _protocol;
	private String _username;
	
	private String _settingsFile;
	
	private String _logFile;
	private int _connectionCount;
		
	public User(String protocol, String username)
	{
		//Set known values
		_protocol=protocol;
		_username=username;
		_settingsFile="users/settings/" + protocol + "-" + username + ".xml";
		_logFile="users/logs/" + protocol + ":" + username + ".log";

		//Set default values. These values will be overwritten by reading in
		//the settings file. If it exists.
		_userType = UserType.Default;
		_defaultZmanim=new String[]{"Alos Hashachar","Sunrise", "Shema GRA", 
				"Tefilla GRA", "Chatzos", "Mincha Gedola", "Candle Lighting",
				"Sunset", "Tzeis"};
		_connectionCount = 0;
		_defaultLocation="Jerusalem";
		//Extract values from settings file
		ReadSettingsFile();
		
		//Increment the connection count and rewrite the file.
		_connectionCount++;
		WriteSettingsFile();
	}
	
	public void PrintInfo()
	{
		System.out.println("UserName: "+_username);
		System.out.println("Protocol: "+_protocol);
		System.out.println("Settings File: "+_settingsFile);
		System.out.println("LogFile: "+_logFile);
		System.out.println("UserType: "+_userType);
		System.out.println("DefaultLocation: "+_defaultLocation);
		System.out.println("ConnectionCount: "+_connectionCount);
		System.out.println("DefaultZmanim");
		for(int i=0;i<_defaultZmanim.length;i++)
		{
			System.out.println("\t"+_defaultZmanim[i]);
		}
		
	}

	public void LogCommand(String command)
	{
		BufferedWriter out=null;
		try{
		    // Create file 
		    FileWriter fstream = new FileWriter(_logFile,true);
		    out = new BufferedWriter(fstream);
		    out.write(command);
		}
		catch (Exception e){//Catch exception if any
		      e.printStackTrace();
		}
		finally
		{
			try
			{
				if(out!=null)
					out.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private UserType _userType;
	public UserType getUserType()
	{
		return _userType;
	}
	public void setUserType(UserType userType)
	{
		_userType=userType;
		WriteSettingsFile();
	}

	private String _defaultLocation;
	public String getDefaultLocation()
	{
		return _defaultLocation;
	}
	public void setDefaultLocation(String location)
	{
		_defaultLocation=location;
		WriteSettingsFile();
	}

	private String[] _defaultZmanim;
	public String[] getDefaultZmanim()
	{
		return _defaultZmanim;
	}
	public void setDefaultZmanim(String[] defaultZmanim)
	{
		_defaultZmanim=defaultZmanim;
		WriteSettingsFile();
	}
	
	private void ReadSettingsFile()
	{
		try{
			File file = new File(_settingsFile);
			if(!file.exists())
				WriteSettingsFile();
			else
			{
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(file);
				doc.getDocumentElement().normalize();
				System.out.println("Root element " + doc.getDocumentElement().getNodeName());
				
				Node settings = doc.getElementsByTagName("Settings").item(0);
				
				Node userInformation = doc.getElementsByTagName("UserInformation").item(0);
				NamedNodeMap userInfoMap = userInformation.getAttributes();
				String userType = userInfoMap.getNamedItem("UserType").getNodeValue();
				if(userType.equals("Admin"))
				{
					_userType= UserType.Admin;
				}
				else if(userType.equals("Blocked"))
				{
					_userType= UserType.Blocked;
				}
				else if(userType.equals("Default"))
				{
					_userType= UserType.Default;
				}
				_logFile = userInfoMap.getNamedItem("LogFile").getNodeValue();
				_connectionCount = Integer.parseInt(userInfoMap.getNamedItem("ConnectionCount").getNodeValue());
				
				Node location = doc.getElementsByTagName("Location").item(0);
				NamedNodeMap locationMap = location.getAttributes();
				_defaultLocation = locationMap.getNamedItem("DefaultLocation").getNodeValue();
				
				Node zmanim = doc.getElementsByTagName("Zmanim").item(0);
				Node defaultZmanim = zmanim.getFirstChild();
				NodeList defaultZmanimList = defaultZmanim.getChildNodes();
				_defaultZmanim = new String[defaultZmanimList.getLength()];
				
				for (int i=0;i<defaultZmanimList.getLength();i++)
				{
					Node zman = defaultZmanimList.item(i);
					NamedNodeMap zmanMap = zman.getAttributes();
					_defaultZmanim[i] = zmanMap.getNamedItem("Name").getNodeValue();
				}
				
			}
		}
		catch (Exception e){
			e.printStackTrace();
			WriteSettingsFile();
		}
	}

	private void WriteSettingsFile()
	{
		File file = new File(_settingsFile);
		if(file.exists())
			file.delete();
		try
		{
			DocumentBuilderFactory factory
	          = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        DOMImplementation impl = builder.getDOMImplementation();

	        Document doc = impl.createDocument(null,null,null);
			
			Element user = doc.createElement("User");
			
			Element connection = doc.createElement("Connection");
			connection.setAttribute("Protocol", _protocol);
			connection.setAttribute("Username", _username);
			user.appendChild(connection);
			
			Element settingsBase = doc.createElement("Settings");
			Element userInformation = doc.createElement("UserInformation");
			userInformation.setAttribute("UserType", _userType.toString());
			userInformation.setAttribute("LogFile", _logFile);
			userInformation.setAttribute("ConnectionCount", String.valueOf(_connectionCount));
			settingsBase.appendChild(userInformation);
			
			Element location = doc.createElement("Location");
			location.setAttribute("DefaultLocation",_defaultLocation);
			settingsBase.appendChild(location);
			
			Element zmanim = doc.createElement("Zmanim");
			Element defaultZmanimList = doc.createElement("DefaultZmanimList");
			for(int i=0;i<_defaultZmanim.length;i++)
			{
				Element zman = doc.createElement("Zman");
				zman.setAttribute("Name", _defaultZmanim[i]);
				defaultZmanimList.appendChild(zman);
			}			
			zmanim.appendChild(defaultZmanimList);
			settingsBase.appendChild(zmanim);
			user.appendChild(settingsBase);
			
			doc.appendChild(user);
			writeXmlFile(doc,_settingsFile);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	// This method writes a DOM document to a file
    private static void writeXmlFile(Document doc, String filename) {
        try {
            // Prepare the DOM document for writing
            Source source = new DOMSource(doc);
    
            // Prepare the output file
            File file = new File(filename);
            Result result = new StreamResult(file);
    
            // Write the DOM document to the file
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

}