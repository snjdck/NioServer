package snjdck.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.util.logging.Logger;

import snjdck.Client;
import snjdck.GameWorld;
import snjdck.PacketDispatcherFactory;
import snjdck.core.IGameWorld;
import snjdck.core.IPacketDispatcher;
import snjdck.core.IoSession;

public class LogicServer extends Server
{
	static public void main(String[] args)
	{
		IPacketDispatcher packetDispatcher = PacketDispatcherFactory.newPacketDispatcher();
		
		IGameWorld gameWorld = new GameWorld();
		LogicServer gameServer = new LogicServer(packetDispatcher, gameWorld, 7410);
		
		gameServer.startup();
		gameServer.runMainLoop();
	}
	
	static private final Logger logger = Logger.getLogger(LogicServer.class.getName());
	
	private final IGameWorld gameWorld;
	private final int port;
	
	private ServerSocketChannel serverSocketChannel;
	
	public LogicServer(IPacketDispatcher packetDispatcher, IGameWorld gameWorld, int port)
	{
		super(packetDispatcher);
		this.gameWorld = gameWorld;
		this.port = port;
	}
	
	@Override
	public void startup()
	{
		super.startup();
		serverSocketChannel = CreateServerSocketChannel(port);
		registerToSelector(serverSocketChannel, SelectionKey.OP_ACCEPT);
	}

	@Override
	public void shutdown()
	{
		super.shutdown();
		try{
			serverSocketChannel.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	protected void select()
	{
		try{
			selector.selectNow();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	protected IoSession createSession(SelectionKey selectionKey)
	{
		Client client = new Client(0, gameWorld, selectionKey);
		return client;
	}

	@Override
	protected void onUpdate(long timeElapsed)
	{
		gameWorld.onUpdate(timeElapsed);
	}
}