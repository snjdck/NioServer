package snjdck.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class IniReader
{
	private final HashMap<String, Properties> sessionMap;
	private transient Properties currentProperties;
	
	public IniReader(String fileName)
	{
		sessionMap = new HashMap<String, Properties>();
		
		BufferedReader reader;
		try{
			reader = new BufferedReader(new FileReader(fileName));
			parseFile(reader);
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private void parseFile(BufferedReader reader) throws IOException
	{
		String line = null;
		
		while(true){
			line = reader.readLine();
			if(null == line){
				break;
			}else{
				parseLine(line.trim());
			}
		}
		
		currentProperties = null;
	}

	private void parseLine(String line)
	{
		if(line.matches("\\[.*?\\]")){
			String sessionName = line.replaceFirst("\\[(.*?)\\]", "$1");
			if(sessionMap.containsKey(sessionName)){
				currentProperties = sessionMap.get(sessionName);
			}else{
				currentProperties = new Properties();
				sessionMap.put(sessionName, currentProperties);
			}
		}else if(null != currentProperties){
			int index = line.indexOf('=');
			if(index < 0){
				return;
			}
			String key = line.substring(0, index);
			String value = line.substring(index+1);
			currentProperties.setProperty(key, value);
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
}