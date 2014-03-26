package snjdck.ioc.ip;

import snjdck.ioc.IInjector;
import snjdck.ioc.tag.Inject;

abstract class InjectionPoint
{
	protected String name;
	protected Inject info;
	
	public InjectionPoint(String name, Inject info)
	{
		this.name = name;
		this.info = info;
	}
	
	final protected Object[] getArgValues(Class<?>[] argTypes, IInjector injector)
	{
		Object[] argValues = new Object[argTypes.length];
		for(int i = 0; i < argTypes.length; i++){
//			String id = info != null ? info[i] : null;
			argValues[i] = injector.getInstance(argTypes[i], null);
		}
		return argValues;
	}
}
