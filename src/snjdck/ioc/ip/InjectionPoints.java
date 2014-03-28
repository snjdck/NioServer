package snjdck.ioc.ip;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import snjdck.ioc.IInjector;
import snjdck.ioc.tag.Inject;

public class InjectionPoints<T> implements IInjectionPoint
{
	private InjectionPointConstructor<T> injectionPointCtor;
	private final List<IInjectionPoint> injectionPointList;

	public InjectionPoints(Class<T> clsRef)
	{
		injectionPointList = new ArrayList<IInjectionPoint>();
		
		initCtor(clsRef);
		for(Field field : clsRef.getFields()){
			if(field.isAnnotationPresent(Inject.class)){
				addInjectionPointProperty(field);
				System.out.println("bingo prop");
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
			addInjectionPointMethod(method);
			System.out.println("bingo method");
		}
	}
	
	private void addInjectionPointConstructor(Constructor<T> ctor)
	{
		assert null == injectionPointCtor : "multi constructor inject error!";
		injectionPointCtor = new InjectionPointConstructor<T>(ctor.getDeclaringClass(), ctor.getParameterTypes());
		System.out.println("bingo ctor");
	}
	
	private void addInjectionPointMethod(Method method)
	{
		injectionPointList.add(
			new InjectionPointMethod(
				method.getName(),
				method.getAnnotation(Inject.class),
				method.getParameterTypes()
			)
		);
	}
	
	private void addInjectionPointProperty(Field field)
	{
		injectionPointList.add(
			new InjectionPointProperty(
				field.getName(),
				field.getAnnotation(Inject.class),
				field.getType()
			)
		);
	}
	
	public T newInstance(IInjector injector)
	{
		T obj = injectionPointCtor.newInstance(injector);
		injectInto(obj, injector);
		return obj;
	}

	/**
	 * 1.注入属性
	 * 2.注入有参数的方法
	 * 3.注入无参数的方法
	 */
	@Override
	public void injectInto(Object target, IInjector injector)
	{
		for(IInjectionPoint injectionPoint: injectionPointList){
			injectionPoint.injectInto(target, injector);
		}
	}

	@Override
	public void getTypesNeedInject(List<Class<?>> result)
	{
		if(injectionPointCtor != null){
			injectionPointCtor.getTypesNeedInject(result);
		}
		for(IInjectionPoint injectionPoint: injectionPointList){
			injectionPoint.getTypesNeedInject(result);
		}
	}
	
	static private Comparator<Method> methodSorter = new Comparator<Method>(){
		@Override
		public int compare(Method left, Method right){
			boolean leftHasArgs = left.getParameterTypes().length > 0;
			boolean rightHasArgs = right.getParameterTypes().length > 0;
			if(leftHasArgs == rightHasArgs){
				return 0;
			}
			return rightHasArgs ? -1 : 1;
		}
	};
}