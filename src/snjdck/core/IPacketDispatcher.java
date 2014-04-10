package snjdck.core;

import entityengine.Entity;
import entityengine.IComponent;
import snjdck.nio.IPacket;
import snjdck.nio.IPacketHandler;

public interface IPacketDispatcher extends IComponent
{
	void addHandler(int msgId, IPacketHandler handler);
	void dispatch(Entity client, IPacket packet);
}
