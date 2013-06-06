package snjdck;

import snjdck.core.IClientManager;
import snjdck.core.IGameWorld;

public class GameWorld implements IGameWorld
{
	private final IClientManager clientManager;
	
	public GameWorld()
	{
		clientManager = new ClientManager();
	}

	@Override
	public IClientManager getClientManager()
	{
		return clientManager;
	}
}
