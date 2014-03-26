package snjdck.ioc.it;

import snjdck.ioc.IInjector;

public class InjectionTypeClass implements IInjectionType
{
	private Class<?> cls;
	
	public InjectionTypeClass(Class<?> cls)
	{
		this.cls = cls;
	}

	@Override
	public Object getValue(IInjector injector, String id)
	{
		if (null != id) return null;
		return injector.newInstance(cls);
	}
}