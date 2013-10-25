package packethandlers;

import java.util.HashMap;
import java.util.logging.Logger;

import snjdck.Client;
import snjdck.GameWorld;
import snjdck.core.IPacket;
import snjdck.core.IPacketHandler;
import snjdck.util.Amf3;

public class DefaultPacketHandler implements IPacketHandler
{
	static private final Logger logger = Logger.getLogger(DefaultPacketHandler.class.getName());
	static private final Amf3 amf = new Amf3();
	
	@Override
	public void handle(Client client, IPacket packet)
	{
		HashMap<String, Object> obj = (HashMap<String, Object>)amf.decode(packet.getBody());
		
		logger.info("recv msg:" + obj.get("msg"));
		obj.put("msg", "i am java nio reply");
		client.reply(packet, amf.encode(obj));
	}
}
