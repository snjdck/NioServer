package snjdck.core;

import snjdck.nio.IPacket;
import snjdck.nio.IPacketHandler;

public interface IPacketDispatcher<T>
{
	void addHandler(int msgId, IPacketHandler<T> handler);
	void dispatch(T client, IPacket packet);
}
