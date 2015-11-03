package alex;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import alex.nio.io.PacketReader;


public class LogicServer {

	final BlockingQueue<byte[]> packetList;
	final Socket socket;
	
	public LogicServer() {
		packetList = new LinkedBlockingQueue<byte[]>();
		
		socket = new Socket();
		PacketReader reader = new PacketReader(socket, 0x20000);
		try {
			socket.connect(new InetSocketAddress("127.0.0.1", 7410));
			new Thread(new WriteThread()).start();
			for(;;){
				reader.doRecv();
				while(reader.hasPacket()){
					onPacketRecv(reader.shiftPacket());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void onPacketRecv(byte[] packet) {
	}

	class WriteThread implements Runnable
	{
		@Override
		public void run()
		{
			try {
				onRun();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
		void onRun() throws InterruptedException, IOException
		{
			OutputStream outputStream = socket.getOutputStream();
			for(;;){
				byte[] packet = packetList.take();
				outputStream.write(packet);
			}
		}
	}
}
