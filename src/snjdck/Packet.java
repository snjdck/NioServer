package snjdck;

import java.nio.ByteBuffer;
import java.util.HashMap;

import snjdck.spi.AbstractPacket;
import snjdck.util.Amf3;

final public class Packet extends AbstractPacket
{
	static private final Amf3 amf3 = new Amf3();
	
	public Packet()
	{
		headSize = 4;
	}

	@Override
	public void readHead(ByteBuffer buffer)
	{
		bodySize = buffer.getInt();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readBody(ByteBuffer buffer)
	{
		byte[] bodyBytes = new byte[bodySize];
		buffer.get(bodyBytes);
		body = (HashMap<String, Object>) amf3.decode(bodyBytes);
	}

	@Override
	public HashMap<String, Object> getBody()
	{
		return body;
	}
	
	private HashMap<String, Object> body;
}