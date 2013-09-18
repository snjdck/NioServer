package entityengine;

public interface IEntity
{
	void addComponent(IComponent component);
	void removeComponent(Class<? extends IComponent> componentCls);
	Boolean hasComponent(Class<? extends IComponent> componentCls);
}
