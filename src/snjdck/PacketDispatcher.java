package snjdck;

import java.util.HashMap;
import java.util.logging.Logger;

import snjdck.core.IPacketDispatcher;
import snjdck.core.IPacketHandler;
import snjdck.nio.IPacket;
import entityengine.EntityEngine;
import entityengine.Module;

public class PacketDispatcher extends Module implements IPacketDispatcher
{
	static private final Logger logger = Logger.getLogger(PacketDispatcher.class.getName());
	
	private HashMap<Integer, IPacketHandler> handlerDict;
	
	public PacketDispatcher()
	{
		this.handlerDict = new HashMap<Integer, IPacketHandler>();
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

	@Override
	public void onInit(EntityEngine engine)
	{
		super.onInit(engine);
	}

	@Override
	public void update(long timeElapsed)
	{
	}
}
