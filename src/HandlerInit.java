import packethandlers.HeartBeatHandler;
import packethandlers.TestHanler;
import cudgel.PacketDispatcher;

public class HandlerInit
{
	HandlerInit(PacketDispatcher dispatcher)
	{
		dispatcher.regHandler(0, new HeartBeatHandler());
		dispatcher.regHandler(101, new TestHanler());
	}
}
