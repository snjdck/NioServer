package snjdck.server.action;

import java.util.LinkedList;

import snjdck.Client;
import snjdck.core.IPacket;
import snjdck.core.IPacketDispatcher;

public class ActionQueue
{
	private final LinkedList<Action> list;
	
	public ActionQueue()
	{
		list = new LinkedList<Action>();
	}
	
	public void addAction(Client client, IPacket packet)
	{
		list.add(Action.Create(client, packet));
	}
	
	public void handleAllActions(IPacketDispatcher packetDispatcher)
	{
		while(list.size() > 0)
		{
			Action action = list.removeFirst();
			packetDispatcher.dispatch(action.client, action.packet);
			action.destroy();
		}
	}
}
