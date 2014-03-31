package snjdck.nio.io;

import snjdck.nio.IPacket;
import snjdck.nio.IoSession;

final public class PacketReader extends PacketIO
{
	private final IoSession session;
	private IPacket nextPacket;
	
	public PacketReader(IoSession session, int bufferSize, IPacket packet)
	{
		super(bufferSize);
		this.session = session;
		this.nextPacket = packet;
	}

	public void onRecv()
	{
		int nBytesRead = session.doRead(buffer);
		
		if(nBytesRead <= 0){
			return;
		}
		
		buffer.flip();
		
		readPacketsFromBuffer();
		
		if(buffer.hasRemaining()){
			buffer.compact();
		}else{
			buffer.clear();
		}
	}
	
	private void readPacketsFromBuffer()
	{
		while(nextPacket.read(buffer)){
			addPacket(nextPacket);
			nextPacket = nextPacket.create();
		}
	}
}