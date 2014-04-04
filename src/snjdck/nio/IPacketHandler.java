package snjdck.nio;

public interface IPacketHandler<T>
{
	void handle(T session, IPacket packet);
}