import java.io.IOException;

import snjdck.GameServer;
import snjdck.GameWorld;
import snjdck.PacketDispatcherFactory;
import snjdck.core.IGameWorld;
import snjdck.core.IPacketDispatcher;

public class NioServer
{
	public static void main(String[] args)
	{
		IPacketDispatcher packetDispatcher = PacketDispatcherFactory.newPacketDispatcher();
		
		IGameWorld gameWorld = new GameWorld(packetDispatcher);
		GameServer gameServer = new GameServer(gameWorld, 7410);
		
		try{
			gameServer.startup();
			gameServer.runMainLoop();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}