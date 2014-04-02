package test;

import entityengine.EntityEngine;
import snjdck.server.Server;

public class ServerTest
{
	static public void main(String[] args)
	{
		EntityEngine engine = new EntityEngine();
		
		engine.addSystem(new Server(7410, 20));
		
		engine.run();
	}
}
