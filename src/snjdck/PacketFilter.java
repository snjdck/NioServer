package snjdck;

import snjdck.core.ClientState;
import snjdck.core.IClient;
import snjdck.core.IPacketFilter;
import snjdck.nio.IPacket;

public class PacketFilter implements IPacketFilter
{
	@Override
	public boolean filter(IClient client, IPacket packet)
	{
		if(client.state() == ClientState.AUTHORIZED){
			return isLoginPacket(packet) == false;
		}
		
		if(client.state() == ClientState.CONNECTED){
			return isLoginPacket(packet);
		}
		
		assert false : "ClientState invalid!";
		return false;
	}
	
	private boolean isLoginPacket(IPacket packet)
	{
		switch(packet.getMsgId())
		{
			case 0x0000://登录消息
			case 0x0001://登录消息
				return true;
		}
		return false;
	}
}
