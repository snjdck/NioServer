package snjdck.spi;

import java.nio.ByteBuffer;

import snjdck.core.IPacket;

public abstract class AbstractPacket implements IPacket
{
	public AbstractPacket()
	{
		tailSize = 0;
	}
	
	@Override
	final public int getHeadSize()
	{
		return headSize;
	}

	@Override
	final public int getBodySize()
	{
		return bodySize;
	}

	@Override
	final public int getTailSize()
	{
		return tailSize;
	}

	@Override
	abstract public void readHead(ByteBuffer buffer);

	@Override
	abstract public void readBody(ByteBuffer buffer);

	@Override
	public void readTail(ByteBuffer buffer)
	{
	}
	
	@Override
	abstract public Object getBody();

	protected int headSize;
	protected int bodySize;
	protected int tailSize;
}