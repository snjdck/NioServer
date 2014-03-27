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
	private Map<String, IInjectionType> dict;
	private IInjector parent;
	
	public Injector()
	{
		dict = new HashMap<String, IInjectionType>();
	}

	@Override
	public void mapValue(Class<?> keyCls, Object value, String id, Boolean needInject)
	{
		dict.put(getKey(keyCls, id), new InjectionTypeValue(value, needInject));
	}

	@Override
	public void mapValue(Class<?> keyCls, Object value, String id)
	{
		mapValue(keyCls, value, id, true);
	}

	@Override
	public void mapValue(Class<?> keyCls, Object value)
	{
		mapValue(keyCls, value, null);
	}
	
	@Override
	public void mapClass(Class<?> keyCls, Class<?> valueCls, String id)
	{
		if(null == valueCls){
			valueCls = keyCls;
		}
		dict.put(getKey(keyCls, id), new InjectionTypeClass(valueCls));
	}

	@Override
	public void mapClass(Class<?> keyCls, Class<?> valueCls)
	{
		mapClass(keyCls, valueCls, null);
	}

	@Override
	public void mapClass(Class<?> keyCls)
	{
		mapClass(keyCls, keyCls);
	}
	
	@Override
	public void mapSingleton(Class<?> keyCls, Class<?> valueCls, String id)
	{
		if(null == valueCls){
			valueCls = keyCls;
		}
		dict.put(getKey(keyCls, id), new InjectionTypeSingleton(valueCls));
	}

	@Override
	public void mapSingleton(Class<?> keyCls, Class<?> valueCls)
	{
		mapSingleton(keyCls, valueCls, null);
	}

	@Override
	public void mapSingleton(Class<?> keyCls)
	{
		mapSingleton(keyCls, keyCls);
	}
	
	@Override
	public void mapRule(Class<?> keyCls, IInjectionType rule)
	{
		dict.put(getKey(keyCls, null), rule);
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
	
	@Override
	public IInjectionType getMapping(String key)
	{
		return dict.get(key);
	}
	
	@Override
	public Object getInstance(Class<?> clsRef, String id)
	{
		IInjectionType injectionType = getInjectionType(getKey(clsRef, id));
		
		if(injectionType != null){
			return injectionType.getValue(this, null);
		}else if(id != null){
			injectionType = getInjectionType(getKey(clsRef));
		}
		if(null == injectionType){
			return null;
		}
		return injectionType.getValue(this, id);
	}

	@Override
	public Object getInstance(Class<?> clsRef)
	{
		return getInstance(clsRef, null);
	}

	@Override
	public Object newInstance(Class<?> clsRef)
	{
		return getInjectionPoint(clsRef).newInstance(this);
	}

	@Override
	public void injectInto(Object target)
	{
		getInjectionPoint(target.getClass()).injectInto(target, this);
	}

	@Override
	public List<Class<?>> getTypesNeedToBeInjected(Class<?> clsRef)
	{
		List<Class<?>> result = new ArrayList<Class<?>>();
		getInjectionPoint(clsRef).getTypesNeedToBeInjected(result);
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
	
	private IInjectionType getInjectionType(String key)
	{
		IInjectionType injectionType;
		IInjector injector = this;
		
		do{
			injectionType = injector.getMapping(key);
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
	
	private String getKey(Class<?> keyCls)
	{
		return getKey(keyCls, null);
	}
	
	static private InjectionPoints getInjectionPoint(Class<?> clsRef)
	{
		String clsName = clsRef.getName();
		if(clsInfoDict.containsKey(clsName) == false){
			clsInfoDict.put(clsName, new InjectionPoints(clsRef));
		}
		return clsInfoDict.get(clsName);
	}
	
	static private final Map<String, InjectionPoints> clsInfoDict = new HashMap<String, InjectionPoints>();
}