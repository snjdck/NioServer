package snjdck.packet;

import snjdck.core.IPacket;

final public class PacketReader extends PacketIO
{
	private IPacket nextPacket;
	
	public PacketReader(int bufferSize, IPacket packet)
	{
		super(bufferSize);
		nextPacket = packet;
	}

	public void onRecv(int nBytesRead)
	{
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