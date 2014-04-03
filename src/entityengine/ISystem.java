package entityengine;

public interface ISystem
{
	void onInit(EntityEngine engine);
	void update(long timeElapsed);
}