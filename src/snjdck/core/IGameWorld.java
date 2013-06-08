package snjdck.core;

public interface IGameWorld
{
	void onUpdate(long timeElapsed);
	IClientManager getClientManager();
}
