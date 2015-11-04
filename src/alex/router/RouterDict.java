package alex.router;

import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

public class RouterDict
{
	final private HashMap<Integer, HashSet<Socket>> handlerDict;
	final private Object lock;
	
	public RouterDict()
	{
		handlerDict = new HashMap<Integer, HashSet<Socket>>();
		lock = new Object();
	}
	
	public void add(int msgId, Socket socket)
	{
		synchronized (lock) {
			getSocketSet(msgId).add(socket);
		}
	}
	
	public void remove(int msgId, Socket socket)
	{
		synchronized (lock) {
			getSocketSet(msgId).remove(socket);
		}
	}
	
	public void removeAll(Socket socket)
	{
		synchronized (lock) {
			for(HashSet<Socket> handlerList : handlerDict.values()){
				handlerList.remove(socket);
			}
		}
	}
	
	public void getHandlerSet(int msgId, HashSet<Socket> result)
	{
		synchronized (lock) {
			result.addAll(getSocketSet(msgId));
			result.addAll(getSocketSet(0));
		}
	}
	
	private HashSet<Socket> getSocketSet(int msgId)
	{
		if(!handlerDict.containsKey(msgId)){
			handlerDict.put(msgId, new HashSet<Socket>());
		}
		return handlerDict.get(msgId);
	}
}
