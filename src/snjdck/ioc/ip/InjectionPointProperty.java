package snjdck.ioc.ip;

import java.util.List;

import snjdck.ioc.IInjector;
import snjdck.ioc.tag.Inject;
import snjdck.util.Lambda;

class InjectionPointProperty extends InjectionPoint implements IInjectionPoint
{
	private Class<?> argType;
	
	public InjectionPointProperty(String name, Inject info, Class<?> argType)
	{
		super(name, info);
		this.argType = argType;
	}

	@Override
	public void injectInto(Object target, IInjector injector)
	{
		Object val = injector.getInstance(argType, info.id());
		if(null == val){
			return;
		}
		Lambda.SetField(target, name, val);
	}

	@Override
	public int getPriority()
	{
		return 1;
	}

	@Override
	public void getTypesNeedToBeInjected(List<Class<?>> result)
	{
		result.add(argType);
	}
}