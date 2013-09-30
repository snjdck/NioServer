package snjdck.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AopHandler implements InvocationHandler
{
	private Object delegate;
	
	public AopHandler()
	{
	}
	
	public Object bind(Object delegate)
	{
		this.delegate = delegate;
		return Proxy.newProxyInstance(
			delegate.getClass().getClassLoader(),
			delegate.getClass().getInterfaces(),
			this
		);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		return method.invoke(delegate, args);
	}
}
