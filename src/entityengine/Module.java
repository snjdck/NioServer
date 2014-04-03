package entityengine;

import snjdck.ioc.IInjector;
import snjdck.ioc.Injector;

public class Module implements ISystem
{
	protected final IInjector injector;
	
	public Module()
	{
		injector = new Injector();
	}

	@Override
	public void onInit(EntityEngine engine)
	{
		injector.parent(engine.getInjector());
	}

	@Override
	public void update(long timeElapsed)
	{

	}
}