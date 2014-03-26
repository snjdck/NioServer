package snjdck.ioc.it;

import snjdck.ioc.IInjector;

public class InjectionTypeSingleton implements IInjectionType
{
	private Class<?> cls;
	private Object val;
	
	public InjectionTypeSingleton(Class<?> cls)
	{
		this.cls = cls;
	}
	
	@Override
	public Object getValue(IInjector injector, String id)
	{
		if(null != id){
			return null;
		}
		if(null == val){
			val = injector.newInstance(cls);
		}
		return val;
	}
}