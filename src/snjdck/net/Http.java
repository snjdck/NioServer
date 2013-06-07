package snjdck.net;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class Http
{
	static public String Post(String path, Map<String, String> params) throws IOException
	{
		URL url = new URL(path);
		URLConnection connection = url.openConnection();
		return null;
	}
	
	public Http()
	{
	}

}
