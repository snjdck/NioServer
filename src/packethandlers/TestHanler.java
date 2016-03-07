package packethandlers;

import java.nio.ByteBuffer;

import snjdck.ioc.tag.Inject;
import cudgel.PacketHandler;
import cudgel.PacketSender;

public class TestHanler implements PacketHandler
{
	@Inject
	public PacketSender packetSender;
	
	@Override
	public void exec(int clientId, byte[] packet)
	{
//		ByteBuffer buffer = ByteBuffer.allocate(6);
		packet[3] = 102;
		packetSender.send(packet);
	}
}
