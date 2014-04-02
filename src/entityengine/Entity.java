package entityengine;

import java.util.HashMap;
import java.util.Map;

public class Entity implements IComponent
{
	private final Map<Class<? extends IComponent>, IComponent> componentDict;
	
	public Entity()
	{
		componentDict = new HashMap<Class<? extends IComponent>, IComponent>();
	}
	
	public <T extends IComponent> void addComponent(T component, Class<T> componentType)
	{
		if(hasComponent(componentType) == false){
			componentDict.put(componentType, component);
		}
	}
	
	public void addComponent(IComponent component)
	{
		Class<? extends IComponent> componentType = component.getClass();
		if(hasComponent(componentType) == false){
			componentDict.put(componentType, component);
		}
	}
	
	public void delComponent(Class<? extends IComponent> componentType)
	{
		componentDict.remove(componentType);
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