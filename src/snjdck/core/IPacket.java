package snjdck.core;

import java.nio.ByteBuffer;
import java.util.HashMap;

public interface IPacket
{
	boolean read(ByteBuffer buffer);
	boolean write(ByteBuffer buffer);
	
	IPacket createReply(HashMap<String, Object> msg);
	
	int getMsgId();
	int getMsgIndex();
	HashMap<String, Object> getBody();
}
