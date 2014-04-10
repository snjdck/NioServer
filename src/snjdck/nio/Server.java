package snjdck.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import entityengine.EntityEngine;
import entityengine.Module;

final public class Server extends Module
{
	protected Selector selector;
	private ServerSocketChannel serverSocketChannel;
	
	private final int port;
	private final int selectTimeout;
	
	public Server(int port, int selectTimeout)
	{
		this.selectTimeout = selectTimeout;
		this.port = port;
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
	
	public void shutdown() throws IOException
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
		
		Client client = (Client)selectionKey.attachment();
		client.getComponent(IoSession.class).updateIO();
	}
	
	private void addSocketChannel(SocketChannel socketChannel) throws IOException
	{
		SelectionKey selectionKey = registerToSelector(socketChannel, SelectionKey.OP_READ);
		Client client = new Client(selectionKey);
		client.onConnected();
		selectionKey.attach(client);
	}
	
	private SelectionKey registerToSelector(SelectableChannel channel, int ops) throws IOException
	{
		channel.configureBlocking(false);
		return channel.register(selector, ops);
	}
}