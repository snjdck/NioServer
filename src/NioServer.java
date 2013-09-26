import java.io.IOException;

import snjdck.GameServer;
import snjdck.GameWorld;
import snjdck.core.IGameWorld;

public class NioServer
{
	public static void main(String[] args)
	{
		IGameWorld gameWorld = new GameWorld();
		
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