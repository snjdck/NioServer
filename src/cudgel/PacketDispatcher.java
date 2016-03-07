package cudgel;

import java.util.HashMap;

import snjdck.ioc.Injector;

public class PacketDispatcher
{
	HashMap<Integer, PacketHandler> handlerDict;
	Injector injector;
	
	public PacketDispatcher(Injector injector)
	{
		this.injector = injector;
		handlerDict = new HashMap<Integer, PacketHandler>();
	}
	
	public void regHandler(int msgId, PacketHandler handler)
	{
		injector.injectInto(handler);
		handlerDict.put(msgId, handler);
	}
	
	public void dispatch(byte[] packet)
	{
		int msgId	 = PacketUtil.ReadShort(packet, 2);
		int clientId = PacketUtil.ReadShort(packet, 4);
		PacketHandler handler = handlerDict.get(msgId);
		if(handler == null){
			System.out.println("handler not set" + msgId);
			return;
		}
		handler.exec(clientId, packet);
	}
}
