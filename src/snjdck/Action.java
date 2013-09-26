package snjdck;

import snjdck.core.IPacket;

public class Action
{
	final public Client client;
	final public IPacket packet;
	
	public Action(Client client, IPacket packet)
	{
		this.client = client;
		this.packet = packet;
	}
}
