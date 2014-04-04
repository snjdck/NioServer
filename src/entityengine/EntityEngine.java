package entityengine;

import java.util.LinkedList;
import java.util.List;

import snjdck.ioc.IInjector;

public class EntityEngine
{
	private final List<Module> moduleList;
	
	private IInjector injector;
	
	public EntityEngine(IInjector injector)
	{
		moduleList = new LinkedList<Module>();
		this.injector = injector;
	}
	
	public IInjector getInjector()
	{
		return injector;
	}
	
	public void addSystem(Module module)
	{
		injector.injectInto(module);
		moduleList.add(module);
		module.onInit(this);
	}
	
	public void removeSystem(Module module)
	{
		moduleList.remove(module);
	}
	
	public void update(long timeElapsed)
	{
		for(Module module : moduleList){
			module.update(timeElapsed);
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