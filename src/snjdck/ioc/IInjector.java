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

	Object getInstance(Object keyClsOrName, String id);
	Object newInstance(Class<?> clsRef);
	void injectInto(Object target);

	List<String> getTypesNeedToBeInjected(Class<?> keyClsOrName);

	IInjector getParent();
	void setParent(IInjector value);
}