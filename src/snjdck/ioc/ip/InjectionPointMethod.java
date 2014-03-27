package snjdck.ioc.ip;

import java.util.List;

import snjdck.ioc.IInjector;
import snjdck.ioc.tag.Inject;
import snjdck.util.Lambda;

class InjectionPointMethod extends InjectionPoint implements IInjectionPoint
{
	private Class<?>[] argTypes;
	
	public InjectionPointMethod(String name, Inject info, Class<?>[] argTypes)
	{
		super(name, info);
		this.argTypes = argTypes;
	}

	@Override
	public void injectInto(Object target, IInjector injector)
	{
		Lambda.Call(target, name, injector.getInstance(argTypes));
	}

	@Override
	public int getPriority()
	{
		return argTypes.length > 0 ? 2 : 3;
	}

	@Override
	public void getTypesNeedToBeInjected(List<Class<?>> result)
	{
		for(Class<?> argType : argTypes){
			result.add(argType);
		}
	}
}