import java.io.IOException;

import snjdck.GameServer;

public class NioServer
{
	public static void main(String[] args)
	{
		GameServer server = new GameServer(7410);
		try{
			server.startup();
		}catch(IOException e){
			e.printStackTrace();
			server.shutdown();
		}
	}
}