package snjdck.ioc.ip;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import snjdck.ioc.IInjector;

class InjectionPointConstructor<T>
{
	private Constructor<T> constructor;
	private Class<?>[] argTypes;
	
	public InjectionPointConstructor(Constructor<T> constructor)
	{
		this.constructor = constructor;
		this.argTypes = constructor.getParameterTypes();
	}
	
	public T newInstance(IInjector injector)
	{
		try{
			return constructor.newInstance(injector.getInstance(argTypes));
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}catch(InstantiationException e){
			e.printStackTrace();
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}catch(InvocationTargetException e){
			e.printStackTrace();
		}
		return null;
	}

	public void getTypesNeedInject(List<Class<?>> result)
	{
		for(Class<?> argType : argTypes){
			result.add(argType);
		}
	}
}