package snjdck.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final public class Lambda
{
	static public <T> T Apply(Class<T> clazz, Object... args)
	{
		try{
			if(null == args || args.length <= 0){
				return clazz.newInstance();
			}
			Constructor<T> constructor = clazz.getConstructor(new Class[args.length]);
			return constructor.newInstance(args);
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
	
	static public Object Call(Object target, String methodName, Object... args)
	{
		try{
			Method method = target.getClass().getMethod(methodName, new Class[args.length]);
			return method.invoke(target, args);
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
		return null;
	}
	
	static public Object GetField(Object target, String fieldName)
	{
		try{
			Field field = target.getClass().getField(fieldName);
			return field.get(target);
		}catch(SecurityException e){
			e.printStackTrace();
		}catch(NoSuchFieldException e){
			e.printStackTrace();
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}
		return null;
	}
	
	static public boolean SetField(Object target, String fieldName, Object value)
	{
		try{
			Field field = target.getClass().getField(fieldName);
			field.set(target, value);
			return true;
		}catch(SecurityException e){
			e.printStackTrace();
		}catch(NoSuchFieldException e){
			e.printStackTrace();
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}
		return false;
	}
}