package snjdck.core;

public interface IPacketFilter
{
	boolean filter(IClient client, IPacket packet);
}
