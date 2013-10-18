package snjdck.server.packet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import snjdck.Client;
import snjdck.Packet;
import snjdck.core.IPacket;
import snjdck.server.action.ActionQueue;

public class PacketReader
{
	private final ByteBuffer buffer;
	private IPacket nextPacket;
	private Client client;
	
	public PacketReader(Client client, int bufferSize)
	{
		this.client = client;
		buffer = ByteBuffer.allocate(bufferSize);
		nextPacket = new Packet();
	}

	public void onRecv(ActionQueue actionQueue)
	{
		SocketChannel socketChannel = (SocketChannel) client.getSelectionKey().channel();
		final int nBytesRead;
		
		try{
			nBytesRead = socketChannel.read(buffer);
		}catch(IOException e){
			client.onLogout();
			return;
		}
		
		if(nBytesRead < 0){
			client.onLogout();
			return;
		}
		
		if(nBytesRead == 0){
			return;
		}
		
		readPacketsFromBuffer(actionQueue);
	}
	
	private void readPacketsFromBuffer(ActionQueue actionQueue)
	{
		buffer.flip();
		while(nextPacket.read(buffer)){
			actionQueue.addAction(client, nextPacket);
			nextPacket = new Packet();
		}
		if(buffer.hasRemaining()){
			buffer.compact();
		}else{
			buffer.clear();
		}
	}
}