package snjdck.nio.action;

import snjdck.nio.IPacket;

public class Action<T>
{
	public T client;
	public IPacket packet;
	
	public Action(T client, IPacket packet)
	{
		this.client = client;
		this.packet = packet;
	}
}