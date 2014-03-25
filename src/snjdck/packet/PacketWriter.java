package snjdck.packet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;

import snjdck.core.IPacket;
import snjdck.core.IoSession;

final public class PacketWriter
{
	private final ByteBuffer buffer;
	private final LinkedList<IPacket> sendQueue;
	private final IoSession session;

	public PacketWriter(IoSession session, int bufferSize)
	{
		this.session = session;
		buffer = ByteBuffer.allocate(bufferSize);
		sendQueue = new LinkedList<IPacket>();
	}
	
	public void send(IPacket packet)
	{
		if(sendQueue.isEmpty()){
			session.interestWriteOp();
		}
		sendQueue.add(packet);
	}

	public void onSend() throws IOException
	{
		writePacketsToBuffer();
		buffer.flip();
		
		final int nBytesWrite = session.doWrite(buffer);
		
		if(nBytesWrite < 0){
			throw new IOException();
		}
		
		if(buffer.hasRemaining()){
			buffer.compact();
		}else{
			buffer.clear();
			if(sendQueue.isEmpty()){
				session.interestReadOp();
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
