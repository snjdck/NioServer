package entityengine;

public interface IEntityEngine extends ISystem
{
	void addSystem(ISystem system, int priority);
	void removeSystem(ISystem system);
	
	void addEntity(IEntity entity);
	void removeEntity(IEntity entity);
}
