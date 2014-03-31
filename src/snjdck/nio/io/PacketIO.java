package snjdck.nio.io;

import java.nio.ByteBuffer;
import java.util.LinkedList;

import snjdck.nio.IPacket;

abstract class PacketIO
{
	final private LinkedList<IPacket> packetList;
	final protected ByteBuffer buffer;
	
	public PacketIO(int bufferSize)
	{
		packetList = new LinkedList<IPacket>();
		buffer = ByteBuffer.allocate(bufferSize);
	}
	
	public IPacket shiftPacket()
	{
		return packetList.removeFirst();
	}
	
	public boolean hasPacket()
	{
		return packetList.size() > 0;
	}
	
	protected void addPacket(IPacket packet)
	{
		packetList.add(packet);
	}
	
	protected IPacket getPacket()
	{
		return packetList.getFirst();
	}
}