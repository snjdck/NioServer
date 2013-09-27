import java.io.IOException;

import packethandlers.DefaultPacketHandler;

import snjdck.GameServer;
import snjdck.GameWorld;
import snjdck.PacketDispatcher;
import snjdck.core.IGameWorld;
import snjdck.core.IPacketDispatcher;

public class NioServer
{
	public static void main(String[] args)
	{
		IPacketDispatcher packetDispatcher = new PacketDispatcher();
		
		packetDispatcher.addHandler(0, new DefaultPacketHandler());
		
		IGameWorld gameWorld = new GameWorld(packetDispatcher);
		
		GameServer server = new GameServer(gameWorld, 7410);
		
		try{
			server.startup();
			server.runMainLoop();
		}catch(IOException e){
			e.printStackTrace();
			server.shutdown();
		}
	}
}