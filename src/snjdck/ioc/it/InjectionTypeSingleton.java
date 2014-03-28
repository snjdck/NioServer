package snjdck.ioc.it;

import snjdck.ioc.IInjector;

public class InjectionTypeSingleton<T> implements IInjectionType<T>
{
	private Class<T> cls;
	private T val;
	
	public InjectionTypeSingleton(Class<T> cls)
	{
		this.cls = cls;
	}
	
	@Override
	public T getValue(IInjector injector)
	{
		if(null == val){
			val = injector.newInstance(cls);
		}
		return val;
	}
}