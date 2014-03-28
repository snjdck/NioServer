package snjdck.ioc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import snjdck.ioc.ip.InjectionPoints;
import snjdck.ioc.it.IInjectionType;
import snjdck.ioc.it.InjectionTypeClass;
import snjdck.ioc.it.InjectionTypeSingleton;
import snjdck.ioc.it.InjectionTypeValue;

public class Injector implements IInjector
{
	private Map<String, IInjectionType<?>> dict;
	private IInjector parent;
	
	public Injector()
	{
		dict = new HashMap<String, IInjectionType<?>>();
	}

	@Override
	public <T> void mapValue(Class<? super T> keyCls, T value, String id, Boolean needInject)
	{
		dict.put(getKey(keyCls, id), new InjectionTypeValue<T>(value, needInject));
	}

	@Override
	public <T> void mapValue(Class<? super T> keyCls, T value, String id)
	{
		mapValue(keyCls, value, id, true);
	}

	@Override
	public <T> void mapValue(Class<? super T> keyCls, T value)
	{
		mapValue(keyCls, value, null);
	}
	
	@Override
	public <T> void mapClass(Class<? super T> keyCls, Class<T> valueCls, String id)
	{
		dict.put(getKey(keyCls, id), new InjectionTypeClass<T>(valueCls));
	}

	@Override
	public <T> void mapClass(Class<T> keyCls, Class<? extends T> valueCls)
	{
		mapClass(keyCls, valueCls, null);
	}

	@Override
	public <T> void mapClass(Class<T> keyCls)
	{
		mapClass(keyCls, keyCls);
	}
	
	@Override
	public <T> void mapSingleton(Class<? super T> keyCls, Class<T> valueCls, String id)
	{
		dict.put(getKey(keyCls, id), new InjectionTypeSingleton<T>(valueCls));
	}

	@Override
	public <T> void mapSingleton(Class<T> keyCls, Class<? extends T> valueCls)
	{
		mapSingleton(keyCls, valueCls, null);
	}

	@Override
	public <T> void mapSingleton(Class<T> keyCls)
	{
		mapSingleton(keyCls, keyCls);
	}
	
	@Override
	public void unmap(Class<?> keyCls, String id)
	{
		dict.remove(getKey(keyCls, id));
	}

	@Override
	public void unmap(Class<?> keyCls)
	{
		unmap(keyCls, null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> IInjectionType<T> getMapping(Class<T> clsRef, String id)
	{
		return (IInjectionType<T>)dict.get(getKey(clsRef, id));
	}
	
	@Override
	public <T> T getInstance(Class<T> clsRef, String id)
	{
		IInjectionType<T> injectionType = getInjectionType(clsRef, id);
		if(injectionType != null){
			return injectionType.getValue(this);
		}
		return null;
	}

	@Override
	public <T> T getInstance(Class<T> clsRef)
	{
		return getInstance(clsRef, null);
	}

	@Override
	public Object[] getInstance(Class<?>[] argTypes)
	{
		Object[] argValues = new Object[argTypes.length];
		for(int i = 0; i < argTypes.length; i++){
			argValues[i] = getInstance(argTypes[i], null);
		}
		return argValues;
	}

	@Override
	public <T> T newInstance(Class<T> clsRef)
	{
		return getInjectionPoint(clsRef).newInstance(this);
	}

	@Override
	public void injectInto(Object target)
	{
		getInjectionPoint(target.getClass()).injectInto(target, this);
	}

	@Override
	public List<Class<?>> getTypesNeedInject(Class<?> clsRef)
	{
		List<Class<?>> result = new ArrayList<Class<?>>();
		getInjectionPoint(clsRef).getTypesNeedInject(result);
		return result;
	}

	@Override
	public IInjector parent()
	{
		return parent;
	}

	@Override
	public void parent(IInjector value)
	{
		parent = value;
	}
	
	private <T> IInjectionType<T> getInjectionType(Class<T> clsRef, String id)
	{
		IInjectionType<T> injectionType;
		IInjector injector = this;
		
		do{
			injectionType = injector.getMapping(clsRef, id);
			injector = injector.parent();
		}while(null == injectionType && null != injector);
		
		return injectionType;
	}
	
	private String getKey(Class<?> keyCls, String id)
	{
		String key = keyCls.getName();
		if(null == id || id.length() <= 0){
			return key;
		}
		return key + "@" + id;
	}
	
	@SuppressWarnings("unchecked")
	static private <T> InjectionPoints<T> getInjectionPoint(Class<T> clsRef)
	{
		String clsName = clsRef.getName();
		if(clsInfoDict.containsKey(clsName) == false){
			clsInfoDict.put(clsName, new InjectionPoints<T>(clsRef));
		}
		return (InjectionPoints<T>)clsInfoDict.get(clsName);
	}
	
	static private final Map<String, InjectionPoints<?>> clsInfoDict = new HashMap<String, InjectionPoints<?>>();
}