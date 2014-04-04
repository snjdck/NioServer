package test;

import java.nio.channels.SelectionKey;

import entityengine.EntityEngine;
import snjdck.ClientManager;
import snjdck.IClientFactory;
import snjdck.PacketDispatcher;
import snjdck.PacketDispatcherFactory;
import snjdck.core.IPacketDispatcher;
import snjdck.ioc.IInjector;
import snjdck.ioc.Injector;
import snjdck.nio.Client;
import snjdck.nio.IPacket;
import snjdck.nio.IoSession;
import snjdck.nio.Server;
import snjdck.nio.impl.Packet;

public class ServerTest
{
	static public void main(String[] args)
	{
		IInjector injector = new Injector();
		EntityEngine engine = new EntityEngine(injector);
		
		final PacketDispatcher packetDispatcher = PacketDispatcherFactory.newPacketDispatcher();
		
		injector.mapValue(IPacketDispatcher.class, packetDispatcher);
		injector.mapSingleton(ClientManager.class);
		injector.mapValue(IInjector.class, injector);
		
		engine.addSystem(new Server(7410, 20, new IClientFactory(){
			@Override
			public IoSession createClient(SelectionKey selectionKey){
				IoSession session = new Client(selectionKey, new Packet()){
					@Override
					public void handlePacket(IPacket packet){
						packetDispatcher.dispatch(this, packet);
					}
				};
				return session;
			}
		}));
		engine.addSystem(packetDispatcher);
		
		engine.run();
	}
}
