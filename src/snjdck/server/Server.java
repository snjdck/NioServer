package snjdck.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.logging.Logger;

import snjdck.core.IPacketDispatcher;
import snjdck.core.IoSession;
import snjdck.server.action.ActionQueue;

abstract public class Server
{
	static private final Logger logger = Logger.getLogger(Server.class.getName());
	
	static public ServerSocketChannel CreateServerSocketChannel(int port)
	{
		ServerSocketChannel channel;
		
		try{
			channel = ServerSocketChannel.open();
		}catch(IOException e){
			logger.info(e.toString());
			return null;
		}
		
		ServerSocket socket = channel.socket();
		
		try{
			socket.setReuseAddress(true);
			socket.bind(new InetSocketAddress(port));
		}catch(SocketException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return channel;
	}
	
	private final IPacketDispatcher packetDispatcher;
	private final ActionQueue actionQueue;
	protected Selector selector;
	
	public Server(IPacketDispatcher packetDispatcher)
	{
		this.packetDispatcher = packetDispatcher;
		this.actionQueue = new ActionQueue();
	}
	
	public void startup()
	{
		try{
			selector = Selector.open();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void shutdown()
	{
		try{
			selector.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	final public void runMainLoop()
	{
		long prevTimestamp = System.currentTimeMillis();
		long nextTimestamp;
		long timeElapsed;
		
		while(true)
		{
			select();
			update();
			
			nextTimestamp = System.currentTimeMillis();
			timeElapsed = nextTimestamp - prevTimestamp;
			prevTimestamp = nextTimestamp;
			
			actionQueue.handleAllActions(packetDispatcher);
			onUpdate(timeElapsed);
		}
	}
	
	private void update()
	{
		Iterator<SelectionKey> it = selector.selectedKeys().iterator();
		while(it.hasNext()){
			SelectionKey selectionKey = it.next();
			it.remove();
			handleSelectionKey(selectionKey);
		}
	}
	
	private void handleSelectionKey(SelectionKey selectionKey)
	{
		if(selectionKey.isAcceptable()){
			onReadyAccept((ServerSocketChannel)selectionKey.channel());
			return;
		}
		
		IoSession session = (IoSession) selectionKey.attachment();
		
		if(selectionKey.isReadable()){
			session.onReadyRecv(actionQueue);
		}
		if(selectionKey.isValid() == false){
			return;
		}
		if(selectionKey.isWritable()){
			session.onReadySend();
		}
	}
	
	private void onReadyAccept(ServerSocketChannel serverSocketChannel)
	{
		SocketChannel socketChannel;
		try{
			socketChannel = serverSocketChannel.accept();
		}catch(IOException e){
			e.printStackTrace();
			return;
		}
		SelectionKey selectionKey = registerToSelector(socketChannel, SelectionKey.OP_READ);
		IoSession session = createSession(selectionKey);
		session.onConnected();
		selectionKey.attach(session);
	}
	
	final protected SelectionKey registerToSelector(SelectableChannel channel, int ops)
	{
		try{
			channel.configureBlocking(false);
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
		SelectionKey selectionKey = null;
		try{
			selectionKey = channel.register(selector, ops);
		}catch(ClosedChannelException e){
			e.printStackTrace();
		}
		return selectionKey;
	}
	
	abstract protected void select();
	abstract protected IoSession createSession(SelectionKey selectionKey);
	abstract protected void onUpdate(long timeElapsed);
}