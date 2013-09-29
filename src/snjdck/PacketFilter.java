package snjdck;

import snjdck.core.ClientState;
import snjdck.core.IClient;
import snjdck.core.IPacket;
import snjdck.core.IPacketFilter;

public class PacketFilter implements IPacketFilter
{
	@Override
	public boolean filter(IClient client, IPacket packet)
	{
		if(client.getState() == ClientState.AUTHORIZED){
			return true;
		}
		
		if(client.getState() != ClientState.CONNECTED){
			assert false;
			return false;
		}
		switch(packet.getMsgId())
		{
			case 0x0001://登录消息
				return true;
		}
		return false;
	}
}
