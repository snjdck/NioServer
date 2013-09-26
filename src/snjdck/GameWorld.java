package snjdck;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Logger;

import snjdck.core.IClientManager;
import snjdck.core.IGameWorld;
import snjdck.core.IPacket;

public class GameWorld implements IGameWorld
{
	private static final Logger logger = Logger.getLogger(GameWorld.class.getName());
	
	public GameWorld()
	{
		actionList = new LinkedList<Action>();
		clientManager = new ClientManager();
	}

	@Override
	public IClientManager getClientManager()
	{
		return clientManager;
	}
	
	public void onUpdate(long timeElapsed)
	{
//		logger.info("game world update, " + timeElapsed);
	}
	
	@Override
	public void addAction(Client client, IPacket packet)
	{
		actionList.add(new Action(client, packet));
	}
	
	@Override
	public void handleAllActions()
	{
		while(actionList.size() > 0)
		{
			Action action = actionList.removeFirst();
			HashMap<String, Object> obj = action.packet.getBody();
			logger.info("recv msg:" + obj.get("msg"));
		}
	}

	private final LinkedList<Action> actionList;
	private final IClientManager clientManager;
}
