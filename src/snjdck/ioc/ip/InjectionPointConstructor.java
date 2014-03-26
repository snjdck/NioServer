package snjdck.ioc.ip;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import snjdck.ioc.IInjector;
import snjdck.ioc.tag.Inject;

class InjectionPointConstructor extends InjectionPoint
{
	private Class<?>[] argTypes;
	
	public InjectionPointConstructor(String name, Inject info, Class<?>[] argTypes)
	{
		super(name, info);
		this.argTypes = argTypes;
	}
	
	public Object newInstance(IInjector injector)
	{
		try{
			Class<?> clazz = Class.forName(name);
			if(null == argTypes || argTypes.length <= 0){
				return clazz.newInstance();
			}
			Constructor<?> constructor = clazz.getConstructor(new Class[argTypes.length]);
			return constructor.newInstance(getArgValues(argTypes, injector));
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}catch(InstantiationException e){
			e.printStackTrace();
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}catch(SecurityException e){
			e.printStackTrace();
		}catch(NoSuchMethodException e){
			e.printStackTrace();
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}catch(InvocationTargetException e){
			e.printStackTrace();
		}
		return null;
	}

	public void getTypesNeedToBeInjected(List<Class<?>> result)
	{
		for(Class<?> argType : argTypes){
			result.add(argType);
		}
	}
}