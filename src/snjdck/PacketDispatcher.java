package snjdck;

import java.util.HashMap;
import java.util.logging.Logger;

import snjdck.core.IPacket;
import snjdck.core.IPacketDispatcher;
import snjdck.core.IPacketHandler;

public class PacketDispatcher implements IPacketDispatcher
{
	static private final Logger logger = Logger.getLogger(PacketDispatcher.class.getName());
	
	private HashMap<Integer, IPacketHandler> handlerDict;
	
	public PacketDispatcher()
	{
		handlerDict = new HashMap<Integer, IPacketHandler>();
	}
	
	@Override
	public void addHandler(int msgId, IPacketHandler handler)
	{
		handlerDict.put(msgId, handler);
	}

	@Override
	public void dispatch(Client client, IPacket packet)
	{
		IPacketHandler handler = handlerDict.get(packet.getMsgId());
		
		if(null == handler){
			logger.warning("unhandled msg id:" + String.valueOf(packet.getMsgId()));
			return;
		}
		
		handler.handle(client, packet);
	}

}
