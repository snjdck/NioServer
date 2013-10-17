import java.io.IOException;

import snjdck.GameWorld;
import snjdck.PacketDispatcherFactory;
import snjdck.core.IGameWorld;
import snjdck.core.IPacketDispatcher;
import snjdck.server.LogicServer;

public class NioServer
{
	public static void main(String[] args)
	{
		IPacketDispatcher packetDispatcher = PacketDispatcherFactory.newPacketDispatcher();
		
		IGameWorld gameWorld = new GameWorld(packetDispatcher);
		LogicServer gameServer = new LogicServer(gameWorld, 7410);
		
		try{
			gameServer.startup();
			gameServer.runMainLoop();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}