package snjdck.core;

//import snjdck.nio.IPacket;

public interface IClient
{
	ClientState state();
	void state(ClientState newState);
	
	void logout();
	
//	void handlePacket(IPacket packet);
}
