package snjdck.ioc.it;

import snjdck.ioc.IInjector;

public class InjectionTypeValue<T> implements IInjectionType<T>
{
	private boolean needInject;
	private boolean hasInjected;
	private T value;
	
	public InjectionTypeValue(T value, boolean needInject)
	{
		this.needInject = needInject;
		this.value = value;
	}

	@Override
	public T getValue(IInjector injector)
	{
		if(needInject && !hasInjected){
			injector.injectInto(value);
			hasInjected = true;
		}
		return value;
	}
}