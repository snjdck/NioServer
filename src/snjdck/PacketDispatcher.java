package snjdck;

import java.util.HashMap;
import java.util.logging.Logger;

import entityengine.EntityEngine;
import entityengine.ISystem;
import entityengine.Module;

import snjdck.core.IPacketDispatcher;
import snjdck.core.IPacketFilter;
import snjdck.core.IPacketHandler;
import snjdck.nio.IPacket;

public class PacketDispatcher extends Module implements IPacketDispatcher, ISystem
{
	static private final Logger logger = Logger.getLogger(PacketDispatcher.class.getName());
	
	private HashMap<Integer, IPacketHandler> handlerDict;
	private IPacketFilter filter;
	
	public PacketDispatcher(IPacketFilter filter)
	{
		this.handlerDict = new HashMap<Integer, IPacketHandler>();
		this.filter = filter;
	}
	
	@Override
	public void addHandler(int msgId, IPacketHandler handler)
	{
		handlerDict.put(msgId, handler);
	}

	@Override
	public void dispatch(Client client, IPacket packet)
	{
		if(filter.filter(client, packet) == false){
			logger.info("packet 被过滤");
			return;
		}
		
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
