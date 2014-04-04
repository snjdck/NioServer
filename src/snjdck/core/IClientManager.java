package snjdck.core;

import snjdck.nio.Client;

public interface IClientManager
{
	void addClient(Client client);
	void removeClient(Client client);
}
