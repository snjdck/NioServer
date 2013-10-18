package snjdck.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.logging.Logger;

import snjdck.Client;
import snjdck.GameWorld;
import snjdck.PacketDispatcherFactory;
import snjdck.core.IGameWorld;
import snjdck.core.IPacketDispatcher;
import snjdck.core.IoSession;

public class GateServer extends Server
{
	static public void main(String[] args)
	{
		IPacketDispatcher packetDispatcher = PacketDispatcherFactory.newPacketDispatcher();
		
//		IGameWorld gameWorld = new GameWorld(packetDispatcher);
		GateServer gameServer = new GateServer(packetDispatcher, 2501);
		
		try{
			gameServer.startup();
			gameServer.runMainLoop();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private static final Logger logger = Logger.getLogger(GateServer.class.getName());
	
	private ServerSocketChannel serverSocketChannel;
	private SocketChannel logicSocket;
	private final int port;
	
	private HashMap<Integer, Client> clientDict;
	private int numClients;
	
	public GateServer(IPacketDispatcher packetDispatcher, int port)
	{
		super(packetDispatcher);
		this.port = port;
		clientDict = new HashMap<Integer, Client>();
		numClients = 0;
	}
	
	@Override
	public void startup() throws IOException
	{
		super.startup();
		
		logger.info("准备连接LogicServer");
		logicSocket = SocketChannel.open();
		logicSocket.connect(new InetSocketAddress("127.0.0.1", 7410));
		logger.info("连接LogicServer成功,准备启动");
		
		serverSocketChannel = CreateServerSocketChannel(port);
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
		selector.select();
	}

	@Override
	protected IoSession createSession(SelectionKey selectionKey)
	{
		Client client = new Client(numClients, null, selectionKey);
		clientDict.put(numClients, client);
		numClients++;
		return client;
	}

	@Override
	protected void onUpdate(long timeElapsed)
	{
	}
}
