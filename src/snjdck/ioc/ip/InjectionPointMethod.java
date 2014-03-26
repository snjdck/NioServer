package snjdck.ioc.ip;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import snjdck.ioc.IInjector;
import snjdck.ioc.tag.Inject;

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
		try{
			Method method = target.getClass().getMethod(name, new Class[argTypes.length]);
			method.invoke(target, getArgValues(argTypes, injector));
		}catch(SecurityException e){
			e.printStackTrace();
		}catch(NoSuchMethodException e){
			e.printStackTrace();
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}catch(InvocationTargetException e){
			e.printStackTrace();
		}
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