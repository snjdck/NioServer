package snjdck.util;

import java.util.LinkedList;

@SuppressWarnings("unchecked")
public class ObjectPool<T>
{
	private LinkedList<T> cache;
	private Class<T> classType;
	
	public ObjectPool(Class<T> classType)
	{
		this.cache = new LinkedList<T>();
		this.classType = classType;
	}
	
	public T get()
	{
		T result = null;
		
		if(cache.isEmpty()){
			result = (T) ClassUtil.newInstance(classType);
		}else{
			result = cache.removeLast();
		}
		
		return result;
	}
	
	public void put(T value)
	{
		cache.add(value);
	}
}
