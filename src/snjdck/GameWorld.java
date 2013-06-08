package snjdck;

import java.util.logging.Logger;

import snjdck.core.IClientManager;
import snjdck.core.IGameWorld;

public class GameWorld implements IGameWorld
{
	private static final Logger logger = Logger.getLogger(GameWorld.class.getName());
	
	public GameWorld()
	{
		clientManager = new ClientManager();
	}

	@Override
	public IClientManager getClientManager()
	{
		return clientManager;
	}
	
	public void onUpdate(long timeElapsed)
	{
		logger.info("game world update");
	}
	
	private final IClientManager clientManager;
}
