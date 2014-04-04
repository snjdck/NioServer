package packethandlers;

import java.util.HashMap;
import java.util.logging.Logger;

import snjdck.nio.Client;
import snjdck.nio.IPacket;
import snjdck.nio.IPacketHandler;
import snjdck.util.Amf3;

public class DefaultPacketHandler implements IPacketHandler<Client>
{
	static private final Logger logger = Logger.getLogger(DefaultPacketHandler.class.getName());
	static private final Amf3 amf = new Amf3();
	
	@Override
	public void handle(Client client, IPacket packet)
	{
		HashMap<String, Object> obj = (HashMap<String, Object>)amf.decode(packet.getBody());
		
		logger.info("recv msg:" + obj.get("msg"));
		obj.put("msg", "i am java nio reply");
		client.send(1, amf.encode(obj));
	}
}
