package entityengine;

import java.util.HashMap;
import java.util.Map;

import snjdck.ioc.IInjector;
import snjdck.ioc.Injector;

public class Entity implements IComponent
{
	private final Map<Class<? extends IComponent>, IComponent> componentDict;
	private final IInjector injector;
	
	public Entity()
	{
		componentDict = new HashMap<Class<? extends IComponent>, IComponent>();
		injector = new Injector();
		
		injector.mapValue(Entity.class, this);
	}
	
	public <T extends IComponent> void addComponent(Class<T> componentType, T component)
	{
		if(hasComponent(componentType)){
			return;
		}
		componentDict.put(componentType, component);
		injector.injectInto(component);
		injector.mapValue(componentType, component);
	}
	
	public void delComponent(Class<? extends IComponent> componentType)
	{
		if(hasComponent(componentType) == false){
			return;
		}
		componentDict.remove(componentType);
		injector.unmap(componentType);
	}
	
	public boolean hasComponent(Class<? extends IComponent> componentType)
	{
		return componentDict.containsKey(componentType);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IComponent> T getComponent(Class<T> componentType)
	{
		return (T)componentDict.get(componentType);
	}
}