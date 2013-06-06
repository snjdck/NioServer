package snjdck.core;

import java.nio.ByteBuffer;

public interface IPacket
{
	int getHeadSize();
	int getBodySize();
	int getTailSize();
	
	void readHead(ByteBuffer buffer);
	void readBody(ByteBuffer buffer);
	void readTail(ByteBuffer buffer);
	
	Object getBody();
}
