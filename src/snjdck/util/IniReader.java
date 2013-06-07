package snjdck.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class IniReader
{
	public static void main(String[] args)
	{
		try {
			IniReader reader = new IniReader("D:\\test.ini");
			System.out.println(reader.getValue("session1", "name"));
			System.out.println(reader.getValue("session1", "password"));
			System.out.println(reader.getValue("session1", "sex"));
			System.out.println(reader.getValue("session1", "age"));
			System.out.println(reader.getValue("session1", "chinesename"));
			System.out.println(reader.getValue("session2", "name"));
			System.out.println(reader.getValue("session2", "password"));
			System.out.println(reader.getValue("session2", "age"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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

	private final HashMap<String, Properties> sessionMap;
	
	private transient Properties currentProperties;
}