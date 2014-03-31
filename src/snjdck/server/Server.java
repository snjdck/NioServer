package snjdck.server;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import snjdck.nio.IoSession;

abstract public class Server
{
	protected Selector selector;
	
	public Server()
	{
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
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
			addSocketChannel(serverSocketChannel.accept());
			return;
		}
		
		IoSession session = (IoSession)selectionKey.attachment();
		
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
	
	final public void addSocketChannel(SocketChannel socketChannel) throws IOException
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