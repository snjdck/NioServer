package snjdck.core;

import java.nio.ByteBuffer;
import java.util.HashMap;

public interface IPacket
{
	boolean read(ByteBuffer buffer);
	HashMap<String, Object> getBody();
	int size();
}
