package snjdck.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import snjdck.core.IPacketDispatcher;
import snjdck.core.IoSession;
import snjdck.server.action.ActionQueue;

abstract public class Server
{
	static public ServerSocketChannel CreateServerSocketChannel(int port) throws IOException
	{
		ServerSocketChannel channel = ServerSocketChannel.open();
		
		channel.socket().setReuseAddress(true);
		channel.socket().bind(new InetSocketAddress(port));
		
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
	
	public void startup() throws IOException
	{
		selector = Selector.open();
	}
	
	public void shutdown() throws IOException
	{
		selector.close();
	}
	
	final public void runMainLoop() throws IOException
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
	
	private void update() throws IOException
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
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
			accept(serverSocketChannel.accept());
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
	
	final protected void accept(SocketChannel socketChannel) throws IOException
	{
		SelectionKey selectionKey = registerToSelector(socketChannel, SelectionKey.OP_READ);
		IoSession session = createSession(selectionKey);
		session.onConnected();
		selectionKey.attach(session);
	}
	
	final protected SelectionKey registerToSelector(SelectableChannel channel, int ops) throws IOException
	{
		channel.configureBlocking(false);
		return channel.register(selector, ops);
	}
	
	abstract protected void select() throws IOException;
	abstract protected IoSession createSession(SelectionKey selectionKey);
	abstract protected void onUpdate(long timeElapsed);
}