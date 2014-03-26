package snjdck.ioc.ip;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import snjdck.ioc.IInjector;
import snjdck.ioc.tag.Inject;

public class InjectionPoints implements IInjectionPoint
{
	private InjectionPointConstructor injectionPointCtor;
	private final List<IInjectionPoint> injectionPointList;
	private boolean hasSort;

	public InjectionPoints(Class<?> clsRef)
	{
		injectionPointList = new ArrayList<IInjectionPoint>();
		
		injectionPointCtor = new InjectionPointConstructor(clsRef.getName(), null, null);
		for(Field field : clsRef.getFields()){
			Inject injectTag = field.getAnnotation(Inject.class);
			if(injectTag != null){
				addInjectionPointProperty(field, injectTag);
				System.out.println("bingo prop");
			}
		}
		for(Method method : clsRef.getMethods()){
			Inject injectTag = method.getAnnotation(Inject.class);
			if(injectTag != null){
				addInjectionPointMethod(method, injectTag);
				System.out.println("bingo method");
			}
		}
	}
	
	private void addInjectionPointProperty(Field varNode, Inject injectTag)
	{
		injectionPointList.add(
			new InjectionPointProperty(
				varNode.getName(),
				injectTag,
				varNode.getType()
			)
		);
	}
	
	private void addInjectionPointMethod(Method methodNode, Inject injectTag)
	{
		injectionPointList.add(
			new InjectionPointMethod(
				methodNode.getName(),
				injectTag,
				methodNode.getParameterTypes()
			)
		);
	}
	
	public Object newInstance(IInjector injector)
	{
		Object obj = injectionPointCtor.newInstance(injector);
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
		if(!hasSort){
			Collections.sort(injectionPointList, comparator);
			hasSort = true;
		}
		for(IInjectionPoint injectionPoint: injectionPointList){
			injectionPoint.injectInto(target, injector);
		}
	}

	@Override
	public int getPriority()
	{
		return 0;
	}

	@Override
	public void getTypesNeedToBeInjected(List<String> result)
	{
		injectionPointCtor.getTypesNeedToBeInjected(result);
		for(IInjectionPoint injectionPoint: injectionPointList){
			injectionPoint.getTypesNeedToBeInjected(result);
		}
	}
	
	static private Comparator<IInjectionPoint> comparator = new Comparator<IInjectionPoint>()
	{
		@Override
		public int compare(IInjectionPoint o1, IInjectionPoint o2)
		{
			return o1.getPriority() - o2.getPriority();
		}
	};
}