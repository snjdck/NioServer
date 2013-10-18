package snjdck.server.action;

import snjdck.Client;
import snjdck.core.IPacket;
import snjdck.util.ObjectPool;

public class Action
{
	static private final ObjectPool<Action> cache = new ObjectPool<Action>(Action.class);
	
	static public Action Create(Client client, IPacket packet)
	{
		Action action = cache.get();
		action.client = client;
		action.packet = packet;
		return action;
	}
	
	public Client client;
	public IPacket packet;
	
	public void destroy()
	{
		client = null;
		packet = null;
		cache.put(this);
	}
}
