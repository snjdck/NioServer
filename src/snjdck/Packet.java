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
		packet.body = msg;
		packet.bodyBytes = amf3.encode(msg);
		packet.bodySize = packet.bodyBytes.length;
		
		return packet;
	}
	
	final private int headSize = 16;
	private int bodySize;
	
	private int msgId;
	private int msgIndex;
	private int from;
	private HashMap<String, Object> body;
	private byte[] bodyBytes;
	
	public Packet()
	{
	}
	
	@Override
	public boolean write(ByteBuffer buffer)
	{
		if(buffer.remaining() < headSize + bodySize){
			return false;
		}
		
		writeHead(buffer);
		writeBody(buffer);
		
		return true;
	}

	@Override
	public boolean read(ByteBuffer buffer)
	{
		if(buffer.remaining() < headSize){
			return false;
		}
		
		buffer.mark();
		readHead(buffer);
		
		if(buffer.remaining() < bodySize){
			buffer.reset();
			return false;
		}
		
		readBody(buffer);
		
		return true;
	}

	private void readHead(ByteBuffer buffer)
	{
		bodySize = buffer.getInt();
		msgId = buffer.getInt();
		msgIndex = buffer.getInt();
		from = buffer.getInt();
	}
	
	private void writeHead(ByteBuffer buffer)
	{
		buffer.putInt(bodySize);
		buffer.putInt(msgId);
		buffer.putInt(msgIndex);
		buffer.putInt(from);
	}

	@SuppressWarnings("unchecked")
	private void readBody(ByteBuffer buffer)
	{
		bodyBytes = new byte[bodySize];
		buffer.get(bodyBytes);
		body = (HashMap<String, Object>) amf3.decode(bodyBytes);
	}
	
	private void writeBody(ByteBuffer buffer)
	{
		buffer.put(bodyBytes);
	}

	@Override
	public HashMap<String, Object> getBody()
	{
		return body;
	}

	@Override
	public int getMsgId()
	{
		return msgId;
	}

	@Override
	public int getMsgIndex()
	{
		return msgIndex;
	}

	@Override
	public IPacket createReply(HashMap<String, Object> msg)
	{
		Packet packet = Create(msgId, msg);
		
		packet.msgIndex = msgIndex;
		
		return packet;
	}
}