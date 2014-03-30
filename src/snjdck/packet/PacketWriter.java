package snjdck.packet;

import java.io.IOException;

import snjdck.core.IPacket;
import snjdck.core.IoSession;

final public class PacketWriter extends PacketIO
{
	private final IoSession session;
//	private boolean isSending;

	public PacketWriter(IoSession session, int bufferSize)
	{
		super(bufferSize);
//		isSending = false;
		this.session = session;
	}
//	
//	public void send(IPacket packet)
//	{
//		addPacket(packet);
//		if(false == isSending){
//			
//		}
//	}

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
