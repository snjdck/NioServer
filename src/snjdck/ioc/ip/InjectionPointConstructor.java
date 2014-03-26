package snjdck.ioc.ip;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import snjdck.ioc.IInjector;

public class InjectionPointConstructor extends InjectionPoint
{
	private Class<?>[] argTypes;
	
	public InjectionPointConstructor(String name, Object info, Class<?>[] argTypes)
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

	public void getTypesNeedToBeInjected(List<String> result)
	{
		for(Class<?> argType : argTypes){
			result.add(argType.getName());
		}
	}
}