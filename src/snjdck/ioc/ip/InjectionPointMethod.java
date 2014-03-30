package snjdck.ioc.ip;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import snjdck.ioc.IInjector;

class InjectionPointMethod implements IInjectionPoint
{
	private Method method;
	private Class<?>[] argTypes;
	
	public InjectionPointMethod(Method method)
	{
		this.method = method;
		this.argTypes = method.getParameterTypes();
	}

	@Override
	public void injectInto(Object target, IInjector injector)
	{
		try{
			method.invoke(target, injector.getInstance(argTypes));
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}catch(InvocationTargetException e){
			e.printStackTrace();
		}
	}

	@Override
	public void getTypesNeedInject(List<Class<?>> result)
	{
		for(Class<?> argType : argTypes){
			result.add(argType);
		}
	}
}