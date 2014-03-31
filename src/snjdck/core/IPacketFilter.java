package snjdck.core;

import snjdck.nio.IPacket;

public interface IPacketFilter
{
	boolean filter(IClient client, IPacket packet);
}
