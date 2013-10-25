package packethandlers;

import java.util.HashMap;
import java.util.logging.Logger;

import snjdck.Client;
import snjdck.core.ClientState;
import snjdck.core.IPacket;
import snjdck.core.IPacketHandler;
import snjdck.util.Amf3;

public class DbPacketHandler implements IPacketHandler
{
	static private final Logger logger = Logger.getLogger(DbPacketHandler.class.getName());
	static private final Amf3 amf = new Amf3();
	
	@Override
	public void handle(Client client, IPacket packet)
	{
		HashMap<String, Object> obj = (HashMap<String, Object>)amf.decode(packet.getBody());
		logger.info("db recv msg:" + obj.get("msg"));
		
		String name = (String) obj.get("name");
		String pwd = (String) obj.get("pwd");
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		if(name.equals("shaokai") && pwd.equals("123456")){
			client.state(ClientState.AUTHORIZED);
			result.put("msg", "ok");
		}else{
			result.put("msg", "auth error!");
		}
		client.reply(packet, amf.encode(result));
	}

}
