package snjdck.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import snjdck.IClientFactory;
import snjdck.ioc.IInjector;
import snjdck.ioc.tag.Inject;
import snjdck.nio.IoSession;
import entityengine.EntityEngine;
import entityengine.ISystem;
import entityengine.Module;

final public class Server extends Module implements ISystem
{
	@Inject
	public IInjector injector;
	
	protected Selector selector;
	private ServerSocketChannel serverSocketChannel;
	
	private final int port;
	private final int selectTimeout;
	private final IClientFactory clientFactory;
	
	public Server(int port, int selectTimeout, IClientFactory clientFactory)
	{
		this.selectTimeout = selectTimeout;
		this.port = port;
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void onInit(EntityEngine engine)
	{
		super.onInit(engine);
		try{
			startup();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void startup() throws IOException
	{
		selector = Selector.open();
		
		serverSocketChannel = ServerSocketChannel.open();
		ServerSocket serverSocket = serverSocketChannel.socket();
		serverSocket.setReuseAddress(true);
		serverSocket.bind(new InetSocketAddress(port));
		
		registerToSelector(serverSocketChannel, SelectionKey.OP_ACCEPT);
	}
	
	private void shutdown() throws IOException
	{
		serverSocketChannel.close();
		selector.close();
	}
	
	@Override
	public void update(long timeElapsed)
	{
		try{
			selectImpl(selectTimeout);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void selectImpl(int timeout) throws IOException
	{
		selector.select(timeout);
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
	
	private void addSocketChannel(SocketChannel socketChannel) throws IOException
	{
		SelectionKey selectionKey = registerToSelector(socketChannel, SelectionKey.OP_READ);
		IoSession session = createSession(selectionKey);
		session.onConnected();
		selectionKey.attach(session);
	}
	
	private SelectionKey registerToSelector(SelectableChannel channel, int ops) throws IOException
	{
		channel.configureBlocking(false);
		return channel.register(selector, ops);
	}
	
	private IoSession createSession(SelectionKey selectionKey)
	{
		IoSession session = clientFactory.createClient(selectionKey);
		injector.injectInto(session);
		return session;
	}
}