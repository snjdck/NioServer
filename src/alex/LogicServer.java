package alex;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import alex.nio.io.PacketReader;
import alex.packet.PacketQueue;


public class LogicServer {

	final PacketQueue packetList;
	final Socket socket;
	
	public LogicServer() {
		packetList = new PacketQueue();
		
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
			}catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
		void onRun() throws IOException
		{
			OutputStream outputStream = socket.getOutputStream();
			for(;;){
				byte[] packet = packetList.take();
				outputStream.write(packet);
			}
		}
	}
}
