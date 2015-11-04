package alex;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class HeartBeatServer
{
	public HeartBeatServer()
	{
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress("127.0.0.1", 7410));
			OutputStream outputStream = socket.getOutputStream();
			for(;;){
				outputStream.write(null);
				Thread.sleep(10);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}