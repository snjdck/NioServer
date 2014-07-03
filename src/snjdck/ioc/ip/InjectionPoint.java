package snjdck.ioc.ip;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import snjdck.ioc.IInjector;
import snjdck.ioc.tag.Inject;

final public class InjectionPoint<T>
{
	static private final Map<String, InjectionPoint<?>> injectionPointDict = new HashMap<String, InjectionPoint<?>>();
	
	@SuppressWarnings("unchecked")
	static private <T> InjectionPoint<T> Fetch(Class<T> clsRef)
	{
		String clsName = clsRef.getName();
		if(injectionPointDict.containsKey(clsName) == false){
			injectionPointDict.put(clsName, new InjectionPoint<T>(clsRef));
		}
		return (InjectionPoint<T>)injectionPointDict.get(clsName);
	}
	
	static public <T> T NewInstance(Class<T> clsRef, IInjector injector)
	{
		return Fetch(clsRef).newInstance(injector);
	}

	static public void InjectInto(Object target, IInjector injector)
	{
		Fetch(target.getClass()).injectInto(target, injector);
	}
	
	static public List<Class<?>> GetTypesNeedInject(Class<?> clsRef)
	{
		return Fetch(clsRef).getTypesNeedInject();
	}
	
	private InjectionPointConstructor<T> injectionPointCtor;
	private final List<IInjectionPoint> injectionPointList;

	private InjectionPoint(Class<T> clsRef)
	{
		injectionPointList = new ArrayList<IInjectionPoint>();
		
		initCtor(clsRef);
		for(Field field : clsRef.getFields()){
			if(field.isAnnotationPresent(Inject.class)){
				injectionPointList.add(new InjectionPointField(field));
			}
		}
		initMethod(clsRef);
	}
	
	@SuppressWarnings("unchecked")
	private void initCtor(Class<T> clsRef)
	{
		Constructor<?>[] ctorList = clsRef.getConstructors();
		int ctorAmount = ctorList.length;
		if(ctorAmount <= 0){
			return;
		}
		for(Constructor<?> ctor : ctorList){
			if(ctor.isAnnotationPresent(Inject.class)){
				addInjectionPointConstructor((Constructor<T>)ctor);
			}
		}
		if(injectionPointCtor != null){
			return;
		}
		for(Constructor<?> ctor : ctorList){
			addInjectionPointConstructor((Constructor<T>)ctor);
			break;
		}
	}
	
	private void initMethod(Class<T> clsRef)
	{
		List<Method> methodList = new ArrayList<Method>();
		for(Method method : clsRef.getMethods()){
			if(method.isAnnotationPresent(Inject.class)){
				methodList.add(method);
			}
		}
		Collections.sort(methodList, methodSorter);
		for(Method method : methodList){
			injectionPointList.add(new InjectionPointMethod(method));
		}
	}
	
	private void addInjectionPointConstructor(Constructor<T> ctor)
	{
		assert null == injectionPointCtor : "multi constructor inject error!";
		injectionPointCtor = new InjectionPointConstructor<T>(ctor);
	}
	
	private T newInstance(IInjector injector)
	{
		T obj = injectionPointCtor.newInstance(injector);
		injectInto(obj, injector);
		return obj;
	}

	private void injectInto(Object target, IInjector injector)
	{
		for(IInjectionPoint injectionPoint: injectionPointList){
			injectionPoint.injectInto(target, injector);
		}
	}

	private List<Class<?>> getTypesNeedInject()
	{
		List<Class<?>> result = new ArrayList<Class<?>>();
		if(injectionPointCtor != null){
			injectionPointCtor.getTypesNeedInject(result);
		}
		for(IInjectionPoint injectionPoint: injectionPointList){
			injectionPoint.getTypesNeedInject(result);
		}
		return result;
	}
	
	static private Comparator<Method> methodSorter = new Comparator<Method>(){
		@Override
		public int compare(Method left, Method right){
			return right.getParameterTypes().length - left.getParameterTypes().length;
		}
	};
}