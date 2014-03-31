package snjdck.nio.impl;

import java.nio.ByteBuffer;

import snjdck.nio.IPacket;

final class Packet implements IPacket
{
	static public Packet Create(int msgId, byte[] msg)
	{
		Packet packet = new Packet();
		
		packet.bodySize = msg.length;
		packet.msgId = msgId;
		packet.body = msg;
		
		return packet;
	}
	
	final private int headSize = 8;
	private int bodySize;
	
	private int msgId;
	private byte[] body;
	
	public Packet()
	{
	}
	
	@Override
	public IPacket create()
	{
		return new Packet();
	}

	@Override
	public IPacket create(int msgId, byte[] body)
	{
		return null;
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
	}
	
	private void writeHead(ByteBuffer buffer)
	{
		buffer.putInt(bodySize);
		buffer.putInt(msgId);
	}

	private void readBody(ByteBuffer buffer)
	{
		body = new byte[bodySize];
		buffer.get(body);
	}
	
	private void writeBody(ByteBuffer buffer)
	{
		buffer.put(body);
	}
	
	@Override
	public int getMsgId()
	{
		return msgId;
	}

	@Override
	public byte[] getBody()
	{
		return body;
	}
}