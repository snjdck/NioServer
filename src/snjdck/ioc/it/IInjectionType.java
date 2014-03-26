package snjdck.ioc.it;

import snjdck.ioc.IInjector;

public interface IInjectionType
{
	Object getValue(IInjector injector, String id);
}
