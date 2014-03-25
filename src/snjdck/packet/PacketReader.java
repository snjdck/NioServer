package snjdck.packet;

import java.io.IOException;
import java.nio.ByteBuffer;

import snjdck.Client;
import snjdck.core.IPacket;
import snjdck.server.action.ActionQueue;

final public class PacketReader
{
	private final ByteBuffer buffer;
	private final Client client;
	private IPacket nextPacket;
	
	public PacketReader(Client client, int bufferSize, IPacket packet)
	{
		this.client = client;
		buffer = ByteBuffer.allocate(bufferSize);
		nextPacket = packet;
	}

	public void onRecv(ActionQueue actionQueue)
	{
		final int nBytesRead;
		
		try{
			nBytesRead = client.doRead(buffer);
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
			nextPacket = nextPacket.create();
		}
		if(buffer.hasRemaining()){
			buffer.compact();
		}else{
			buffer.clear();
		}
	}
}