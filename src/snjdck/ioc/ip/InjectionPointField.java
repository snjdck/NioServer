package snjdck.ioc.ip;

import java.lang.reflect.Field;
import java.util.List;

import snjdck.ioc.IInjector;
import snjdck.ioc.tag.Inject;

class InjectionPointField implements IInjectionPoint
{
	private Field field;
	private Inject tagInfo;
	private Class<?> argType;
	
	public InjectionPointField(Field field)
	{
		this.field = field;
		this.tagInfo = field.getAnnotation(Inject.class);
		this.argType = field.getType();
	}

	@Override
	public void injectInto(Object target, IInjector injector)
	{
		Object val = injector.getInstance(argType, tagInfo.id());
		if(val == null){
			return;
		}
		try{
			field.set(target, val);
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}
	}

	@Override
	public void getTypesNeedInject(List<Class<?>> result)
	{
		result.add(argType);
	}
}