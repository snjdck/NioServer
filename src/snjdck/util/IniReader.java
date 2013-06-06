package snjdck.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class IniReader
{
	public IniReader(String fileName) throws IOException
	{
		sessionMap = new HashMap<String, Properties>();
		
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		parseFile(reader);
		reader.close();
	}
	
	private void parseFile(BufferedReader reader) throws IOException
	{
		String line = null;
		while(true){
			line = reader.readLine();
			if(null == line){
				break;
			}else{
				parseLine(line);
			}
		}
	}

	private void parseLine(String line)
	{
		line = line.trim();
		if(line.matches("\\[.*\\]")){
			currentSession = line.replaceFirst("\\[(.*)\\]", "$1");
			currentProperties = new Properties();
			sessionMap.put(currentSession, currentProperties);
		}else if(line.matches(".*=.*")){
			if(null == currentSession){
				return;
			}
		}
	}
	
	public String getValue(String session, String key)
	{
		Properties properties = sessionMap.get(session);
		if(null == properties){
			return null;
		}
		return properties.getProperty(key);
	}

	private final HashMap<String, Properties> sessionMap;
	
	private transient String currentSession;
	private transient Properties currentProperties;
}