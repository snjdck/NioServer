package snjdck;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import snjdck.core.IGameWorld;
import snjdck.core.IoSession;

public class GameServer
{
	private static final Logger logger = Logger.getLogger(GameServer.class.getName());
	
	public GameServer(IGameWorld gameWorld, int port)
	{
		this.gameWorld = gameWorld;
		this.port = port;
	}
	
	public void startup() throws IOException
	{
		serverSocketChannel = createServerSocketChannel();
		selector = Selector.open();
		
		registerToSelector(serverSocketChannel, SelectionKey.OP_ACCEPT);
		
		loopForever();
	}
	
	private void loopForever() throws IOException
	{
		long prevTimestamp = System.currentTimeMillis();
		long nextTimestamp;
		long timeElapsed;
		
		while(true)
		{
			selector.selectNow();
			updateIO();
			
			nextTimestamp = System.currentTimeMillis();
			timeElapsed = nextTimestamp - prevTimestamp;
			prevTimestamp = nextTimestamp;
			
			gameWorld.onUpdate(timeElapsed);
		}
	}
	
	public void shutdown()
	{
		try{
			selector.close();
			serverSocketChannel.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private ServerSocketChannel createServerSocketChannel() throws IOException
	{
		ServerSocketChannel channel = ServerSocketChannel.open();
		
		channel.socket().setReuseAddress(true);
		channel.socket().bind(new InetSocketAddress(port));
		
		return channel;
	}
	
	private void updateIO() throws IOException
	{
		Iterator<SelectionKey> it = selector.selectedKeys().iterator();
		while(it.hasNext()){
			SelectionKey selectionKey = it.next();
			it.remove();
			handleSelectionKey(selectionKey);
		}
	}
	
	private void handleSelectionKey(SelectionKey selectionKey) throws IOException
	{
		if(selectionKey.isAcceptable()){
			onReadyAccept((ServerSocketChannel) selectionKey.channel());
			return;
		}
		
		IoSession session = (IoSession) selectionKey.attachment();
		
		if(selectionKey.isReadable()){
			session.onReadyRecv();
		}
		if(selectionKey.isValid() == false){
			return;
		}
		if(selectionKey.isWritable()){
			session.onReadySend();
		}
	}
	
	private void onReadyAccept(ServerSocketChannel serverSocketChannel) throws IOException
	{
		SocketChannel socketChannel = serverSocketChannel.accept();
		SelectionKey selectionKey = registerToSelector(socketChannel, SelectionKey.OP_READ);
		selectionKey.attach(new Client(gameWorld, selectionKey));
	}
	
	private SelectionKey registerToSelector(SelectableChannel channel, int ops) throws IOException
	{
		channel.configureBlocking(false);
		return channel.register(selector, ops);
	}
	
	private final IGameWorld gameWorld;
	private final int port;
	
	private ServerSocketChannel serverSocketChannel;
	private Selector selector;
}