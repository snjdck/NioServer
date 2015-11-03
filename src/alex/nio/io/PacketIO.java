package alex.nio.io;

import java.nio.ByteBuffer;
import java.util.LinkedList;

abstract class PacketIO
{
	final private LinkedList<byte[]> packetList;
	final protected ByteBuffer buffer;
	
	public PacketIO(int bufferSize)
	{
		packetList = new LinkedList<byte[]>();
		buffer = ByteBuffer.allocate(bufferSize);
	}
	
	public byte[] shiftPacket()
	{
		return packetList.removeFirst();
	}
	
	public boolean hasPacket()
	{
		return packetList.size() > 0;
	}
	
	protected void addPacket(byte[] packet)
	{
		packetList.add(packet);
	}
	
	protected byte[] getPacket()
	{
		return packetList.getFirst();
	}
}