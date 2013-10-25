package snjdck.packet;

import java.nio.ByteBuffer;

import snjdck.core.IPacket;

public final class PolicyPacket implements IPacket
{
	static public IPacket Create()
	{
		PolicyPacket packet = new PolicyPacket();
		
		packet.body = response.getBytes();
		
		return packet;
	}
	
	static public final byte[] request = "<policy-file-request/>".getBytes();
	static private final String response = "<policy-file-request/>";
	
	private byte[] body;
	
	public PolicyPacket()
	{
		
	}

	@Override
	public boolean read(ByteBuffer buffer)
	{
		if(buffer.remaining() < request.length){
			return false;
		}
		body = new byte[request.length];
		buffer.get(body);
		return true;
	}

	@Override
	public boolean write(ByteBuffer buffer)
	{
		if(buffer.remaining() < body.length){
			return false;
		}
		buffer.put(body);
		return true;
	}

	@Override
	public int getMsgId()
	{
		return 0;
	}

	@Override
	public byte[] getBody()
	{
		return body;
	}

	@Override
	public IPacket create()
	{
		return new PolicyPacket();
	}

	@Override
	public IPacket createReply(byte[] msg)
	{
		return null;
	}
}