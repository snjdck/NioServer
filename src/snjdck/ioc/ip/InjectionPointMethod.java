package snjdck.ioc.ip;

import java.util.List;

import snjdck.ioc.IInjector;
import snjdck.util.Lambda;

class InjectionPointMethod implements IInjectionPoint
{
	private String methodName;
	private Class<?>[] argTypes;
	
	public InjectionPointMethod(String methodName, Class<?>[] argTypes)
	{
		this.methodName = methodName;
		this.argTypes = argTypes;
	}

	@Override
	public void injectInto(Object target, IInjector injector)
	{
		Lambda.Call(target, methodName, injector.getInstance(argTypes));
	}

	@Override
	public void getTypesNeedInject(List<Class<?>> result)
	{
		for(Class<?> argType : argTypes){
			result.add(argType);
		}
	}
}