package snjdck.nio.action;

import java.util.LinkedList;

import snjdck.nio.IPacket;
import snjdck.nio.IoSession;

public class ActionQueue<T extends IoSession>
{
	private final LinkedList<Action<T>> list;
	
	public ActionQueue()
	{
		list = new LinkedList<Action<T>>();
	}
	
	public void addAction(T client, IPacket packet)
	{
		list.add(new Action<T>(client, packet));
	}
	
	public void handleAllActions()
	{
		while(list.size() > 0)
		{
			Action<T> action = list.removeFirst();
			action.client.handlePacket(action.packet);
		}
	}
}
