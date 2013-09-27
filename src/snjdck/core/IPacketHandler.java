package snjdck.core;

import snjdck.Client;

public interface IPacketHandler
{
	void handle(Client client, IPacket packet);
}
