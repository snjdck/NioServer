package packethandlers;

import cudgel.PacketHandler;

public class HeartBeatHandler implements PacketHandler
{
	@Override
	public void exec(int clientId, byte[] packet)
	{
		System.out.println("heart beat");
	}
}
