package packethandlers;

import snjdck.ioc.tag.Inject;
import cudgel.ClientManager;
import cudgel.PacketHandler;

public class ClientConnectHandler implements PacketHandler
{
	@Inject
	public ClientManager clientMgr;
	
	@Override
	public void exec(int clientId, byte[] packet)
	{
		clientMgr.addClient(clientId);
		System.out.println(clientMgr.getClientCount());
	}
}
