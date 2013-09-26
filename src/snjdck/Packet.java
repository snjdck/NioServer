package snjdck;

import java.nio.ByteBuffer;
import java.util.HashMap;

import snjdck.core.IPacket;
import snjdck.util.Amf3;

final public class Packet implements IPacket
{
	static private final Amf3 amf3 = new Amf3();
	
	static public Packet Create(int msgId, HashMap<String, Object> msg)
	{
		Packet packet = new Packet();
		
		packet.msgId = msgId;
		packet.bodyBytes = amf3.encode(msg);
		packet.body = msg;
		packet.bodySize = packet.bodyBytes.length;
		
		return packet;
	}
	
	public Packet()
	{
		headSize = 8;
	}
	
	@Override
	public boolean read(ByteBuffer buffer)
	{
		if(false == isHeadReaded){
			if(buffer.remaining() < headSize){
				return false;
			}else{
				readHead(buffer);
				isHeadReaded = true;
			}
		}
		
		if(false == isBodyReaded){
			if(buffer.remaining() < bodySize){
				return false;
			}else{
				readBody(buffer);
				isBodyReaded = true;
			}
		}
		
		return true;
	}

	private void readHead(ByteBuffer buffer)
	{
		bodySize = buffer.getInt();
		msgId = buffer.getInt();
	}

	@SuppressWarnings("unchecked")
	private void readBody(ByteBuffer buffer)
	{
		bodyBytes = new byte[bodySize];
		buffer.get(bodyBytes);
		body = (HashMap<String, Object>) amf3.decode(bodyBytes);
	}

	@Override
	public HashMap<String, Object> getBody()
	{
		return body;
	}
	
	@Override
	public int size()
	{
		return headSize + bodySize;
	}

	private boolean isHeadReaded;
	private boolean isBodyReaded;
	
	private int headSize;
	private int bodySize;
	
	private int msgId;
	private HashMap<String, Object> body;
	private byte[] bodyBytes;
}