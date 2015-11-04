package alex;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

import alex.nio.io.PacketReader;
import alex.packet.PacketQueue;


public class CenterServer
{
	final HashMap<Integer, HashSet<Socket>> handlerDict;
	final PacketQueue packetList;
	
	public CenterServer(int port)
	{
		handlerDict = new HashMap<Integer, HashSet<Socket>>();
		packetList = new PacketQueue();
		RunThread(new WriteThread());
		
		try {
			ServerSocket server = new ServerSocket();
			server.setReuseAddress(true);
			server.bind(new InetSocketAddress(port));
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
		synchronized (handlerDict) {
			for(HashSet<Socket> handlerList : handlerDict.values()){
				handlerList.remove(socket);
			}
		}
	}
	
	HashSet<Socket> getSocketSet(int msgId)
	{
		if(!handlerDict.containsKey(msgId)){
			handlerDict.put(msgId, new HashSet<Socket>());
		}
		return handlerDict.get(msgId);
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
						onPacketRecv(reader.shiftPacket());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				onClientDisconnect(socket);
			}
		}
		
		void onPacketRecv(byte[] packet) {
			if(handleMsg(packet)){
				packetList.put(packet);
			}
		}
		
		boolean handleMsg(byte[] packet)
		{
			int msgId = GetPacketId(packet);
			int offset = 6;
			switch(msgId)
			{
			case 1:
				while(offset < packet.length){
					msgId = ReadShort(packet, offset);
					synchronized (handlerDict) {
						getSocketSet(msgId).add(socket);
					}
				}
				return false;
			case 2:
				while(offset < packet.length){
					msgId = ReadShort(packet, offset);
					synchronized (handlerDict) {
						getSocketSet(msgId).remove(socket);
					}
				}
				return false;
			}
			return true;
		}
	}
	
	class WriteThread implements Runnable
	{
		@Override
		public void run()
		{
			onRun();
		}
		
		void onRun()
		{
			HashSet<Socket> handlerList = new HashSet<Socket>();
			for(;;){
				byte[] packet = packetList.take();
				int msgId = GetPacketId(packet);
				if(msgId <= 0){
					continue;
				}
				synchronized (handlerDict) {
					handlerList.addAll(getSocketSet(msgId));
					handlerList.addAll(getSocketSet(0));
				}
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