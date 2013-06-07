package snjdck;

import java.util.logging.Logger;

import snjdck.core.IClientManager;
import snjdck.core.IGameWorld;

public class GameWorld implements IGameWorld, Runnable
{
	private static final Logger logger = Logger.getLogger(GameWorld.class.getName());
	
	public GameWorld(long updateInterval)
	{
		this.updateInterval = updateInterval;
		
		clientManager = new ClientManager();
	}

	@Override
	public IClientManager getClientManager()
	{
		return clientManager;
	}
	
	@Override
	public void run()
	{
		try{
			while(true){
				onUpdate();
				Thread.sleep(updateInterval);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	protected void onUpdate()
	{
		logger.info("game world update");
	}
	
	private final IClientManager clientManager;
	private final long updateInterval;
}
