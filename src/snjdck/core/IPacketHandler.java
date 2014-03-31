package snjdck.core;

import snjdck.Client;
import snjdck.nio.IPacket;

public interface IPacketHandler
{
	void handle(Client client, IPacket packet);
}
