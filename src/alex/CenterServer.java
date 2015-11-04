package alex;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

import snjdck.util.SocketFactory;

import alex.nio.io.PacketReader;
import alex.packet.PacketQueue;
import alex.router.RouterDict;

public class CenterServer
{
	final RouterDict handlerDict;
	final PacketQueue packetList;
	
	public CenterServer(int port)
	{
		handlerDict = new RouterDict();
		packetList = new PacketQueue();
		RunThread(new WriteThread());
		
		try {
			ServerSocket server = SocketFactory.CreateServerSocket(port);
			for(;;){
				Socket client = server.accept();
				RunThread(new ReadThread(client));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void onClientDisconnect(Socket socket)
	{
		handlerDict.removeAll(socket);
	}
	
	static int ReadShort(byte[] packet, int offset)
	{
		return (packet[offset] << 8) | packet[offset+1];
	}
	
	static int GetPacketId(byte[] packet)
	{
		return ReadShort(packet, 4);
	}
	
	static void RunThread(Runnable runnable)
	{
		Thread thread = new Thread(runnable);
		thread.setDaemon(true);
		thread.start();
	}
	
	class ReadThread implements Runnable
	{
		PacketReader reader;
		Socket socket;
		
		public ReadThread(Socket socket)
		{
			this.socket = socket;
			reader = new PacketReader(socket, 0x20000);
		}
		
		@Override
		public void run()
		{
			try {
				for(;;){
					reader.doRecv();
					while(reader.hasPacket()){
						handleMsg(reader.shiftPacket());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				onClientDisconnect(socket);
			}
		}
		
		void handleMsg(byte[] packet)
		{
			int msgId = GetPacketId(packet);
			int offset = 6;
			switch(msgId)
			{
			case 1:
				while(offset < packet.length){
					msgId = ReadShort(packet, offset);
					handlerDict.add(msgId, socket);
				}
				break;
			case 2:
				while(offset < packet.length){
					msgId = ReadShort(packet, offset);
					handlerDict.remove(msgId, socket);
				}
				break;
			default:
				packetList.put(packet);
			}
		}
	}
	
	class WriteThread implements Runnable
	{
		@Override
		public void run()
		{
			HashSet<Socket> handlerList = new HashSet<Socket>();
			for(;;){
				byte[] packet = packetList.take();
				int msgId = GetPacketId(packet);
				if(msgId <= 0){
					continue;
				}
				handlerDict.getHandlerSet(msgId, handlerList);
				for(Socket socket : handlerList){
					try {
						socket.getOutputStream().write(packet);
					} catch (IOException e) {
						onClientDisconnect(socket);
					}
				}
				handlerList.clear();
			}
		}
	}
}