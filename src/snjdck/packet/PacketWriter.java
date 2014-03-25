package snjdck.packet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;

import snjdck.Client;
import snjdck.core.IPacket;

final public class PacketWriter
{
	private final ByteBuffer buffer;
	private final LinkedList<IPacket> sendQueue;
	private final Client client;

	public PacketWriter(Client client, int bufferSize)
	{
		this.client = client;
		buffer = ByteBuffer.allocate(bufferSize);
		sendQueue = new LinkedList<IPacket>();
	}
	
	public void send(IPacket packet)
	{
		if(sendQueue.isEmpty()){
			client.interestWriteOp();
		}
		sendQueue.add(packet);
	}

	public void onSend() throws IOException
	{
		writePacketsToBuffer();
		buffer.flip();
		
		final int nBytesWrite = client.doWrite(buffer);
		
		if(nBytesWrite < 0){
			throw new IOException();
		}
		
		if(buffer.hasRemaining()){
			buffer.compact();
		}else{
			buffer.clear();
			if(sendQueue.isEmpty()){
				client.interestReadOp();
			}
		}
	}
	
	private void writePacketsToBuffer()
	{
		while(sendQueue.size() > 0)
		{
			IPacket packet = sendQueue.getFirst();
			if(packet.write(buffer)){
				sendQueue.removeFirst();
			}else{
				break;
			}
		}
	}
}
