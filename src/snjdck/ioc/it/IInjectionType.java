package snjdck.ioc.it;

import snjdck.ioc.IInjector;

public interface IInjectionType<T>
{
	T getValue(IInjector injector, String id);
}