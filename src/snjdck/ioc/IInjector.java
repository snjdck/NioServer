package snjdck.ioc;

import java.util.List;

import snjdck.ioc.it.IInjectionType;

public interface IInjector
{
	void mapValue(Class<?> keyCls, Object value, String id, Boolean needInject);
	void mapClass(Class<?> keyCls, Class<?> valueCls, String id);
	void mapSingleton(Class<?> keyCls, Class<?> valueCls, String id);
	void mapRule(Class<?> keyCls, IInjectionType rule);

	IInjectionType getMapping(String key);

	void unmap(Class<?> keyCls, String id);

	Object getInstance(Class<?> clsRef, String id);
	Object newInstance(Class<?> clsRef);
	
	void injectInto(Object target);

	List<Class<?>> getTypesNeedToBeInjected(Class<?> clsRef);

	IInjector parent();
	void parent(IInjector value);
}