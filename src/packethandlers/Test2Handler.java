package packethandlers;

import java.util.Arrays;

import snjdck.ioc.tag.Inject;
import cudgel.ClientManager;
import cudgel.PacketHandler;
import cudgel.PacketSender;
import cudgel.PacketUtil;

public class Test2Handler implements PacketHandler
{
	@Inject
	public ClientManager clientMgr;
	
	@Inject
	public PacketSender packetSender;
	
	@Override
	public void exec(int clientId, byte[] packet)
	{
		for(int cid : clientMgr.getList()){
			if(cid == clientId)
				continue;
			byte[] copy = Arrays.copyOf(packet, packet.length);
			PacketUtil.WriteShort(copy, 2, 1002);
			PacketUtil.WriteShort(copy, 4, clientId);
			packetSender.send(copy);
		}
	}
}
