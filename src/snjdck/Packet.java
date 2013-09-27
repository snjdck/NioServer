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
//		if(false == isHeadReady){
//			if(buffer.remaining() < headSize){
//				return false;
//			}else{
//				writeHead(buffer);
//				isHeadReady = true;
//			}
//		}
//		
//		if(false == isBodyReady){
//			if(buffer.remaining() < bodySize){
//				return false;
//			}else{
//				writeBody(buffer);
//				isBodyReady = true;
//			}
//		}
		
		return true;
	}

	@Override
	public boolean read(ByteBuffer buffer)
	{
		if(false == isHeadReady){
			if(buffer.remaining() < headSize){
				return false;
			}else{
				readHead(buffer);
				isHeadReady = true;
			}
		}
		
		if(false == isBodyReady){
			if(buffer.remaining() < bodySize){
				return false;
			}else{
				readBody(buffer);
				isBodyReady = true;
			}
		}
		
		return true;
	}

	private void readHead(ByteBuffer buffer)
	{
		bodySize = buffer.getInt();
		msgId = buffer.getInt();
		msgIndex = buffer.getInt();
	}
	
	private void writeHead(ByteBuffer buffer)
	{
		buffer.putInt(bodySize);
		buffer.putInt(msgId);
		buffer.putInt(msgIndex);
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

	private boolean isHeadReady;
	private boolean isBodyReady;
	
	final private int headSize = 12;
	private int bodySize;
	
	private int msgId;
	private int msgIndex;
	private HashMap<String, Object> body;
	private byte[] bodyBytes;
}