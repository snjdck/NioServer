package snjdck.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Http
{
	static private final String charsetName = "UTF-8";
	
	static public String Post(String path, Map<String, String> params) throws IOException
	{
		URL url = new URL(path);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream(), charsetName);
		osw.write("post data");
		osw.flush();
		osw.close();

		StringBuffer buffer = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), charsetName));
		buffer.append("\n");

		return buffer.toString();
	}
	
	public Http()
	{
	}

}
