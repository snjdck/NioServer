package snjdck.ioc.ip;

import java.util.List;

import snjdck.ioc.IInjector;
import snjdck.ioc.tag.Inject;
import snjdck.util.Lambda;

class InjectionPointProperty implements IInjectionPoint
{
	private String fieldName;
	private Inject tagInfo;
	private Class<?> argType;
	
	public InjectionPointProperty(String fieldName, Inject tagInfo, Class<?> argType)
	{
		this.fieldName = fieldName;
		this.tagInfo = tagInfo;
		this.argType = argType;
	}

	@Override
	public void injectInto(Object target, IInjector injector)
	{
		Object val = injector.getInstance(argType, tagInfo.id());
		if(null == val){
			return;
		}
		Lambda.SetField(target, fieldName, val);
	}

	@Override
	public void getTypesNeedInject(List<Class<?>> result)
	{
		result.add(argType);
	}
}