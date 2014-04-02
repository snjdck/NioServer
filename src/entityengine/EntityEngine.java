package entityengine;

import java.util.LinkedList;
import java.util.List;

import snjdck.ioc.IInjector;

public class EntityEngine
{
	private final List<ISystem> systemList;
	
	private IInjector injector;
	
	public EntityEngine(IInjector injector)
	{
		systemList = new LinkedList<ISystem>();
		this.injector = injector;
	}
	
	public void addSystem(ISystem system)
	{
		injector.injectInto(system);
		systemList.add(system);
	}
	
	public void removeSystem(ISystem system)
	{
		systemList.remove(system);
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
}