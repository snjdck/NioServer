package test;

import snjdck.ioc.IInjector;
import snjdck.ioc.Injector;

public class InjectorTest
{
	public InjectorTest()
	{
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		IInjector injector = new Injector();
//		injector.mapValue(null, null);
		injector.mapValue(C.class, new A());
		injector.mapValue(A.class, new A());
		injector.mapValue(B.class, new B());
//		injector.mapValue(B.class, new A());
		
		injector.mapClass(C.class, A.class);
		injector.mapClass(A.class, A.class);
		injector.mapClass(B.class, B.class);
		injector.mapClass(B.class, null);
//		injector.mapClass(B.class, A.class);
		
		C c = injector.getInstance(A.class);
		System.out.println(c);
	}
}

class A implements C
{
	public A()
	{
		
	}
}


class B extends A
{
	
}

interface C
{
	
}