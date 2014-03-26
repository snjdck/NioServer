package snjdck.core;

import java.nio.ByteBuffer;

public interface IPacket
{
	boolean read(ByteBuffer buffer);
	boolean write(ByteBuffer buffer);
	
	int getMsgId();
	byte[] getBody();
	
	IPacket create();
}
