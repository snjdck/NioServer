package packethandlers;

import snjdck.ioc.tag.Inject;
import cudgel.ClientManager;
import cudgel.PacketHandler;

public class ClientDisconnectHandler implements PacketHandler
{
	@Inject
	public ClientManager clientMgr;
	
	@Override
	public void exec(int clientId, byte[] packet)
	{
		clientMgr.removeClient(clientId);
		System.out.println(clientMgr.getClientCount());
	}
}
