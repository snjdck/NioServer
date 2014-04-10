package snjdck.nio;

import java.nio.channels.SelectionKey;
import java.util.logging.Logger;

import snjdck.PacketDispatcherFactory;
import snjdck.core.IPacketDispatcher;
import snjdck.nio.impl.Packet;
import entityengine.Entity;

public class Client extends Entity
{
	private static final Logger logger = Logger.getLogger(Client.class.getName());
	
	public Client(SelectionKey selectionKey)
	{
		addComponent(Client.class, this);
		addComponent(IPacketDispatcher.class, PacketDispatcherFactory.newPacketDispatcher());
		addComponent(IoSession.class, new IoSession(selectionKey, new Packet()));
	}
	
	public void onConnected()
	{
		logger.info("client connected!");
	}
	
	public void onDisconnected()
	{
		logger.info("client disconnected!");
	}
}