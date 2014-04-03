package test;

import entityengine.EntityEngine;
import snjdck.ClientManager;
import snjdck.PacketDispatcher;
import snjdck.PacketDispatcherFactory;
import snjdck.core.IPacketDispatcher;
import snjdck.ioc.IInjector;
import snjdck.ioc.Injector;
import snjdck.server.Server;

public class ServerTest
{
	static public void main(String[] args)
	{
		IInjector injector = new Injector();
		EntityEngine engine = new EntityEngine(injector);
		
		PacketDispatcher packetDispatcher = PacketDispatcherFactory.newPacketDispatcher();
		injector.mapValue(IPacketDispatcher.class, packetDispatcher);
		injector.mapSingleton(ClientManager.class);
		injector.mapValue(IInjector.class, injector);
		
		engine.addSystem(new Server(7410, 20));
		engine.addSystem(packetDispatcher);
		
		engine.run();
	}
}
