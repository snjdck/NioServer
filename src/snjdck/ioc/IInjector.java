package snjdck.ioc;

import java.util.List;

import snjdck.ioc.it.IInjectionType;

public interface IInjector
{
	<T> void mapValue(Class<? super T> keyCls, T value, String id, Boolean needInject);
	<T> void mapValue(Class<? super T> keyCls, T value, String id);
	<T> void mapValue(Class<? super T> keyCls, T value);
	
	<T> void mapClass(Class<? super T> keyCls, Class<T> valueCls, String id);
	<T> void mapClass(Class<T> keyCls, Class<? extends T> valueCls);
	<T> void mapClass(Class<T> keyCls);
	
	<T> void mapSingleton(Class<? super T> keyCls, Class<T> valueCls, String id);
	<T> void mapSingleton(Class<T> keyCls, Class<? extends T> valueCls);
	<T> void mapSingleton(Class<T> keyCls);

	void unmap(Class<?> keyCls, String id);
	void unmap(Class<?> keyCls);
	
	<T> IInjectionType<T> getMapping(Class<T> clsRef, String id);

	<T> T getInstance(Class<T> clsRef, String id);
	<T> T getInstance(Class<T> clsRef);
	Object[] getInstance(Class<?>[] argTypes);
	
	<T> T newInstance(Class<T> clsRef);
	
	void injectInto(Object target);

	List<Class<?>> getTypesNeedInject(Class<?> clsRef);

	IInjector parent();
	void parent(IInjector value);
}