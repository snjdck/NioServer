import java.io.IOException;

import snjdck.GameServer;
import snjdck.GameWorld;

public class NioServer
{
	public static void main(String[] args)
	{
		GameWorld gameWorld = new GameWorld(1000L);
		new Thread(gameWorld).start();
		
		GameServer server = new GameServer(gameWorld, 7410);
		
		try{
			server.startup();
		}catch(IOException e){
			e.printStackTrace();
			server.shutdown();
		}
	}
}