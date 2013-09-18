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
	public interface IHttpCallback
	{
		void onResult(Boolean ok, Object data);
	}

	public static void main(String[] args)
	{
		try {
			Http.Get("http://www.baidu.com", null, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("==end==");
	}
	
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
	
	static public void Get(final String path, final Map<String, String> params, final IHttpCallback callback) throws IOException
	{
		new Thread(new Runnable(){
			@Override
			public void run(){
				String result = null;
				try {
					result = GetImp(path, params);
				} catch (IOException e) {
					e.printStackTrace();
				}
				callback.onResult(true, result);
			}
		}).start();
	}
	
	static private String GetImp(String path, Map<String, String> params) throws IOException
	{
		URL url = new URL(path);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.setRequestMethod("GET");
		connection.setUseCaches(false);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		StringBuffer buffer = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), charsetName));
		buffer.append("hello world!~" + br.readLine());

		return buffer.toString();
	}
}
