package snjdck.ioc.ip;

import java.util.List;

import snjdck.ioc.IInjector;

public interface IInjectionPoint
{
	void injectInto(Object target, IInjector injector);
	int getPriority();
	void getTypesNeedToBeInjected(List<String> result);
}
