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

	public void onRecv(ActionQueue actionQueue) throws IOException
	{
		final int nBytesRead = client.doRead(buffer);
		if(nBytesRead < 0){
			throw new IOException();
		}
		if(nBytesRead > 0){
			readPacketsFromBuffer(actionQueue);
		}
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