package snjdck.ioc.ip;

import java.util.List;

import snjdck.ioc.IInjector;

interface IInjectionPoint
{
	void injectInto(Object target, IInjector injector);
	void getTypesNeedInject(List<Class<?>> result);
}