package snjdck.core;

public interface IClient
{
	ClientState state();
	void state(ClientState newState);
}
