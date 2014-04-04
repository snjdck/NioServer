package entityengine;

import snjdck.ioc.IInjector;
import snjdck.ioc.Injector;

public class Module
{
	protected final IInjector injector;
	
	public Module()
	{
		injector = new Injector();
	}

	public void onInit(EntityEngine engine)
	{
		injector.parent(engine.getInjector());
	}

	public void update(long timeElapsed)
	{

	}
}