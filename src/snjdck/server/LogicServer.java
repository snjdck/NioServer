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
import snjdck.util.SocketFactory;

public class LogicServer extends Server
{
	static public void main(String[] args)
	{
		IGameWorld gameWorld = new GameWorld();
		LogicServer gameServer = new LogicServer(gameWorld, 7410);
		
		try{
			gameServer.startup();
			gameServer.runMainLoop();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	static private final Logger logger = Logger.getLogger(LogicServer.class.getName());
	
	private final IGameWorld gameWorld;
	private final int port;
	
	private IPacketDispatcher packetDispatcher;
	private ServerSocketChannel serverSocketChannel;
	
	public LogicServer(IGameWorld gameWorld, int port)
	{
		super();
		packetDispatcher = PacketDispatcherFactory.newPacketDispatcher();
		this.gameWorld = gameWorld;
		this.port = port;
	}
	
	@Override
	public void startup() throws IOException
	{
		super.startup();
		serverSocketChannel = SocketFactory.CreateServerSocketChannel(port);
		registerToSelector(serverSocketChannel, SelectionKey.OP_ACCEPT);
	}

	@Override
	public void shutdown() throws IOException
	{
		super.shutdown();
		serverSocketChannel.close();
	}

	@Override
	protected void select() throws IOException
	{
		selector.selectNow();
	}

	@Override
	protected IoSession createSession(SelectionKey selectionKey)
	{
		Client client = new Client(0, gameWorld, selectionKey, packetDispatcher);
		return client;
	}

	@Override
	protected void onUpdate(long timeElapsed)
	{
		gameWorld.onUpdate(timeElapsed);
	}
}