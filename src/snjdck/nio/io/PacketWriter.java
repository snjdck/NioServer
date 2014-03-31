package snjdck.nio.io;

import snjdck.nio.IPacket;
import snjdck.nio.IoSession;

final public class PacketWriter extends PacketIO
{
	private final IoSession session;

	public PacketWriter(IoSession session, int bufferSize)
	{
		super(bufferSize);
		this.session = session;
	}
	
	public void send(IPacket packet)
	{
		if(hasPacket() == false){
			session.interestWriteOp();
		}
		addPacket(packet);
	}

	public void onSend()
	{
		writePacketsToBuffer();
		buffer.flip();
		
		int nBytesWrite = session.doWrite(buffer);
		
		if(nBytesWrite < 0){
			return;
		}
		
		if(buffer.hasRemaining()){
			buffer.compact();
		}else{
			buffer.clear();
			if(hasPacket() == false){
				session.interestReadOp();
			}
		}
	}
	
	private void writePacketsToBuffer()
	{
		while(hasPacket())
		{
			IPacket packet = getPacket();
			if(packet.write(buffer)){
				shiftPacket();
			}else{
				break;
			}
		}
	}
}
