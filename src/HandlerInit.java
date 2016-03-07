import packethandlers.ClientConnectHandler;
import packethandlers.ClientDisconnectHandler;
import packethandlers.HeartBeatHandler;
import packethandlers.Test2Handler;
import packethandlers.TestHanler;
import cudgel.PacketDispatcher;

public class HandlerInit
{
	HandlerInit(PacketDispatcher dispatcher)
	{
		dispatcher.regHandler(0, new HeartBeatHandler());
		dispatcher.regHandler(1, new ClientConnectHandler());
		dispatcher.regHandler(2, new ClientDisconnectHandler());
		dispatcher.regHandler(101, new TestHanler());
		dispatcher.regHandler(1001, new Test2Handler());
	}
}
