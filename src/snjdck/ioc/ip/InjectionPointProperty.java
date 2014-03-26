package snjdck.ioc.ip;

import java.lang.reflect.Field;
import java.util.List;

import snjdck.ioc.IInjector;
import snjdck.ioc.tag.Inject;

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
		Object val = injector.getInstance(argType, null);
		if(null == val){
			return;
		}
		try{
			Field field = target.getClass().getField(name);
			field.set(target, val);
		}catch(SecurityException e){
			e.printStackTrace();
		}catch(NoSuchFieldException e){
			e.printStackTrace();
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}
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