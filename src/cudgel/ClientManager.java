package cudgel;

import java.util.LinkedList;
import java.util.List;

public class ClientManager
{
	LinkedList<Integer> list;
	
	public ClientManager()
	{
		list = new LinkedList<Integer>();
	}
	
	public int getClientCount()
	{
		return list.size();
	}
	
	public void addClient(int clientId)
	{
		list.add(clientId);
	}
	
	public void removeClient(int clientId)
	{
		int index = list.indexOf(clientId);
		if(index >= 0)
			list.remove(index);
	}
	
	public List<Integer> getList()
	{
		return list;
	}
}
