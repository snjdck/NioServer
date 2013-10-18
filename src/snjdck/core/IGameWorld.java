package snjdck.core;

import snjdck.Client;

public interface IGameWorld
{
	void onUpdate(long timeElapsed);
	IClientManager getClientManager();
}
