package snjdck.ioc;

import java.util.List;

import snjdck.ioc.it.IInjectionType;

public interface IInjector
{
	void mapValue(Class<?> keyCls, Object value, String id, Boolean needInject);
	void mapValue(Class<?> keyCls, Object value, String id);
	void mapValue(Class<?> keyCls, Object value);
	
	void mapClass(Class<?> keyCls, Class<?> valueCls, String id);
	void mapClass(Class<?> keyCls, Class<?> valueCls);
	void mapClass(Class<?> keyCls);
	
	void mapSingleton(Class<?> keyCls, Class<?> valueCls, String id);
	void mapSingleton(Class<?> keyCls, Class<?> valueCls);
	void mapSingleton(Class<?> keyCls);
	
	void mapRule(Class<?> keyCls, IInjectionType rule);

	void unmap(Class<?> keyCls, String id);
	void unmap(Class<?> keyCls);
	
	IInjectionType getMapping(String key);

	Object getInstance(Class<?> clsRef, String id);
	Object getInstance(Class<?> clsRef);
	
	Object newInstance(Class<?> clsRef);
	
	void injectInto(Object target);

	List<Class<?>> getTypesNeedToBeInjected(Class<?> clsRef);

	IInjector parent();
	void parent(IInjector value);
}