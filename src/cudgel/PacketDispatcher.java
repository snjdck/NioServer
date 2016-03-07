package cudgel;

import java.util.HashMap;

public class PacketDispatcher
{
	HashMap<Integer, PacketHandler> handlerDict;
	
	public void regHandler()
	{
		handlerDict = new HashMap<Integer, PacketHandler>();
	}
	
	public void dispatch(byte[] packet)
	{
		int msgId = packet[3] << 8 | packet[4];
		PacketHandler handler = handlerDict.get(msgId);
		handler.exec();
	}
}
