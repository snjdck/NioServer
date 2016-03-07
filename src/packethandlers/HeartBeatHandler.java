package packethandlers;

import snjdck.ioc.tag.Inject;
import cudgel.PacketHandler;
import cudgel.PacketSender;

public class HeartBeatHandler implements PacketHandler
{
	@Inject
	public PacketSender packetSender;

	@Override
	public void exec(int clientId, byte[] packet)
	{
		System.out.println("heart beat");
	}

}
