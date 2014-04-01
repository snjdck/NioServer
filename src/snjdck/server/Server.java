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
import java.util.LinkedList;
import java.util.List;

import snjdck.Client;
import snjdck.nio.IoSession;

final public class Server
{
	static public void main(String[] args)
	{
		Server server = new Server(7410);
		
		long prevTimestamp = System.currentTimeMillis();
		long nextTimestamp;
		
		while(true)
		{
			server.select(20);
			
			nextTimestamp = System.currentTimeMillis();
			onUpdate(nextTimestamp - prevTimestamp);
			prevTimestamp = nextTimestamp;
		}
	}
	
	static private void onUpdate(long timeElapsed)
	{
		
	}
	
	protected Selector selector;
	private ServerSocketChannel serverSocketChannel;
	
	private final List<IoSession> sessionList;
	private final int port;
	
	public Server(int port)
	{
		sessionList = new LinkedList<IoSession>();
		this.port = port;
	}
	
	public void startup() throws IOException
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
	
	public void select(int timeout)
	{
		try{
			selectImpl(timeout);
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
		Client client = new Client(0, selectionKey, null);
		sessionList.add(client);
		return client;
	}
}