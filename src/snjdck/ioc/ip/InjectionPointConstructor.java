package snjdck.ioc.ip;

import java.util.List;

import snjdck.ioc.IInjector;
import snjdck.util.Lambda;

class InjectionPointConstructor<T>
{
	private Class<T> ctor;
	private Class<?>[] argTypes;
	
	public InjectionPointConstructor(Class<T> ctor, Class<?>[] argTypes)
	{
		this.ctor = ctor;
		this.argTypes = argTypes;
	}
	
	public T newInstance(IInjector injector)
	{
		return Lambda.Apply(ctor, injector.getInstance(argTypes));
	}

	public void getTypesNeedInject(List<Class<?>> result)
	{
		for(Class<?> argType : argTypes){
			result.add(argType);
		}
	}
}