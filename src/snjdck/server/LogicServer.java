package snjdck.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

import snjdck.Client;
import snjdck.GameWorld;
import snjdck.PacketDispatcherFactory;
import snjdck.client.GateClient;
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
		
		try{
			gameServer.startup();
			gameServer.runMainLoop();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private static final Logger logger = Logger.getLogger(LogicServer.class.getName());
	
	private final IGameWorld gameWorld;
	private final int port;
	
	private ServerSocketChannel serverSocketChannel;
	private SocketChannel gateSocket;
	
	public LogicServer(IPacketDispatcher packetDispatcher, IGameWorld gameWorld, int port)
	{
		super(packetDispatcher);
		this.gameWorld = gameWorld;
		this.port = port;
	}
	
	@Override
	public void startup() throws IOException
	{
		super.startup();
		serverSocketChannel = CreateServerSocketChannel(port);
		logger.info("等待GateServer连接...");
		gateSocket = serverSocketChannel.accept();
		accept(gateSocket);
		logger.info("GateServer已连接,准备启动");
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
		return new GateClient(gameWorld, selectionKey);
	}

	@Override
	protected void onUpdate(long timeElapsed)
	{
		gameWorld.onUpdate(timeElapsed);
	}
}
