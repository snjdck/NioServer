package snjdck.packet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;

import snjdck.core.IPacket;
import snjdck.core.IoSession;

final public class PacketReader
{
	private final ByteBuffer buffer;
	private final LinkedList<IPacket> recvQueue;
	private final IoSession session;
	private IPacket nextPacket;
	
	public PacketReader(IoSession session, int bufferSize, IPacket packet)
	{
		this.session = session;
		buffer = ByteBuffer.allocate(bufferSize);
		recvQueue = new LinkedList<IPacket>();
		nextPacket = packet;
	}

	public void onRecv() throws IOException
	{
		final int nBytesRead = session.doRead(buffer);
		if(nBytesRead < 0){
			throw new IOException();
		}
		if(nBytesRead > 0){
			readPacketsFromBuffer();
		}
	}
	
	private void readPacketsFromBuffer()
	{
		buffer.flip();
		while(nextPacket.read(buffer)){
			recvQueue.add(nextPacket);
			nextPacket = nextPacket.create();
		}
		if(buffer.hasRemaining()){
			buffer.compact();
		}else{
			buffer.clear();
		}
	}
	
	public boolean hasPacket()
	{
		return recvQueue.size() > 0;
	}
	
	public IPacket shiftPacket()
	{
		return recvQueue.removeFirst();
	}
}