package snjdck.nio;

import entityengine.Entity;

public interface IPacketHandler
{
	void handle(Entity client, IPacket packet);
}