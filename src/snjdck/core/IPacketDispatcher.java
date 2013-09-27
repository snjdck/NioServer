package snjdck.core;

import snjdck.Client;

public interface IPacketDispatcher
{
	void addHandler(int msgId, IPacketHandler handler);
	void dispatch(Client client, IPacket packet);
}
