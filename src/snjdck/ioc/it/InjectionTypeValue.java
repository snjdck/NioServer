package snjdck.ioc.it;

import snjdck.ioc.IInjector;

public class InjectionTypeValue implements IInjectionType
{
	private boolean needInject;
	private boolean hasInjected;
	private Object value;
	
	public InjectionTypeValue(Object value, boolean needInject)
	{
		this.needInject = needInject;
		this.value = value;
	}

	@Override
	public Object getValue(IInjector injector, String id)
	{
		if (null != id) return null;
		if(needInject && !hasInjected){
			injector.injectInto(value);
			hasInjected = true;
		}
		return value;
	}
}