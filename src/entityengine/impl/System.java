package entityengine.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.logging.Logger;

import snjdck.ioc.tag.Inject;

import entityengine.IEntity;
import entityengine.ISystem;

public class System implements ISystem
{
	static private final Logger log = Logger.getLogger(System.class.getName());
	
//	@Inject(value=IEntity.class)
	public Class<IEntity> ref;
	
	
	@Override
	public void update(long timeElapsed)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Class<System> c = System.class;
		Field[] list = c.getFields();
		for(Field field : list){
			Annotation[] annoList = field.getAnnotations();
			log.info(Integer.toString(annoList.length));
			log.info(String.valueOf(field.isAnnotationPresent(Inject.class)));
			for(Annotation anno : annoList){
				if(anno.annotationType().equals(Inject.class)){
//					log.info("-----------"+((Inject)anno).value().getName());
				}
				log.info("," + anno.annotationType().getName());
			}
			log.info(field.getName() + "," + field.getType().getName());
		}
	}

}
