package packethandlers;

import java.util.HashMap;
import java.util.logging.Logger;

import snjdck.Client;
import snjdck.core.IPacket;
import snjdck.core.IPacketHandler;

public class DefaultPacketHandler implements IPacketHandler
{
	static private final Logger logger = Logger.getLogger(DefaultPacketHandler.class.getName());
	
	@Override
	public void handle(Client client, IPacket packet)
	{
		HashMap<String, Object> obj = packet.getBody();
		logger.info("recv msg:" + obj.get("msg"));
		obj.put("msg", "i am java nio reply");
		client.reply(packet, obj);
	}
}
