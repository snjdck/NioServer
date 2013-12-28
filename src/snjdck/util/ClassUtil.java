package snjdck.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("unchecked")
final public class ClassUtil
{
	static public <T> Class<T> getSuperGenericClass(Class<?> targetClass, int index)
	{
		ParameterizedType paramType = (ParameterizedType) targetClass.getGenericSuperclass();
		Type[] typeArgs = paramType.getActualTypeArguments();
		return (Class<T>) typeArgs[index];
	}
	
	static public Object newInstance(Class<?> classRef)
	{
		try{
			return classRef.newInstance();
		}catch(InstantiationException e){
			e.printStackTrace();
		}catch(IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	static public Object newInstance(String className)
	{
		try{
			Class<?> classRef = Class.forName(className);
			return newInstance(classRef);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		return null;
	}
}
