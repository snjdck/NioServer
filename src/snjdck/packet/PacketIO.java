package snjdck.packet;

import java.nio.ByteBuffer;
import java.util.LinkedList;

import snjdck.core.IPacket;

abstract class PacketIO
{
	final private LinkedList<IPacket> packetList;
	final protected ByteBuffer buffer;
	
	public PacketIO(int bufferSize)
	{
		packetList = new LinkedList<IPacket>();
		buffer = ByteBuffer.allocate(bufferSize);
	}
	
	public ByteBuffer getByteBuffer()
	{
		return buffer;
	}
	
	public void addPacket(IPacket packet)
	{
		packetList.add(packet);
	}
	
	public IPacket shiftPacket()
	{
		return packetList.removeFirst();
	}
	
	public boolean hasPacket()
	{
		return packetList.size() > 0;
	}
	
	protected IPacket getPacket()
	{
		return packetList.getFirst();
	}
}
