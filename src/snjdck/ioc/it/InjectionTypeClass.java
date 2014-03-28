package snjdck.ioc.it;

import snjdck.ioc.IInjector;

public class InjectionTypeClass<T> implements IInjectionType<T>
{
	private Class<T> cls;
	
	public InjectionTypeClass(Class<T> cls)
	{
		this.cls = cls;
	}

	@Override
	public T getValue(IInjector injector)
	{
		return injector.newInstance(cls);
	}
}