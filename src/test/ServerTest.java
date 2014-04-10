package test;

import snjdck.ClientManager;
import snjdck.ioc.IInjector;
import snjdck.ioc.Injector;
import snjdck.nio.Server;
import entityengine.EntityEngine;

public class ServerTest
{
	static public void main(String[] args)
	{
		IInjector injector = new Injector();
		EntityEngine engine = new EntityEngine(injector);
		
		injector.mapSingleton(ClientManager.class);
		injector.mapValue(IInjector.class, injector);
		
		engine.addSystem(new Server(7410, 20));
		
		engine.run();
	}
}
