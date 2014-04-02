package entityengine;

import java.util.LinkedList;
import java.util.List;

import snjdck.ioc.IInjector;

public class EntityEngine
{
	private final List<Integer> priorityList;
	private final List<ISystem> systemList;
	
	private IInjector injector;
	
	public EntityEngine()
	{
		priorityList = new LinkedList<Integer>();
		systemList = new LinkedList<ISystem>();
	}
	
	public void addSystem(ISystem system, int priority)
	{
		int index = getIndex(priority);
		priorityList.add(index, priority);
		systemList.add(index, system);
	}
	
	public void removeSystem(ISystem system)
	{
		if(systemList.contains(system) == false){
			return;
		}
		int index = systemList.indexOf(system);
		systemList.remove(index);
		priorityList.remove(index);
	}
	
	public void update(long timeElapsed)
	{
		for(ISystem system : systemList){
			system.update(timeElapsed);
		}
	}
	
	public void run()
	{
		long prevTimestamp = System.currentTimeMillis();
		long nextTimestamp;
		long timeElapsed = 0;
		
		while(true)
		{
			update(timeElapsed);
			
			nextTimestamp = System.currentTimeMillis();
			timeElapsed = nextTimestamp - prevTimestamp;
			prevTimestamp = nextTimestamp;
		}
	}
	
	private int getIndex(int priority)
	{
		for(int index=priorityList.size()-1; index >= 0; --index){
			if(priorityList.get(index) <= priority){
				return index + 1;
			}
		}
		return 0;
	}
}