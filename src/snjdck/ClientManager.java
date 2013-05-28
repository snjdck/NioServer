package snjdck;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientManager
{
	private static final Logger logger = Logger.getLogger(ClientManager.class.getName());
	
	public ClientManager()
	{
		clientList = new ArrayList<Client>();
	}
	
	public void addClient(Client client)
	{
		clientList.add(client);
		logger.log(Level.INFO, "add client");
	}
	
	public void removeClient(Client client)
	{
		clientList.remove(client);
		logger.log(Level.INFO, "remove client");
	}
	
	public Client getClientByID(int clientID)
	{
		return null;
	}
	
	public Iterator<Client> getIterator()
	{
		return clientList.iterator();
	}
	
	public int getClientAmount()
	{
		return clientList.size();
	}
	
	private final ArrayList<Client> clientList;
}
