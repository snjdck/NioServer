package snjdck.ioc.ip;

import snjdck.ioc.tag.Inject;

abstract class InjectionPoint
{
	protected String name;
	protected Inject info;
	
	public InjectionPoint(String name, Inject info)
	{
		this.name = name;
		this.info = info;
	}
}
