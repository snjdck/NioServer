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

public class GameServer
{
	private static final Logger logger = Logger.getLogger(GameServer.class.getName());
	
	public GameServer(int port)
	{
		this.port = port;
	}
	
	public void startup() throws IOException
	{
		serverSocketChannel = createServerSocketChannel();
		selector = Selector.open();
		
		registerToSelector(serverSocketChannel, SelectionKey.OP_ACCEPT);
		
		while(true){
			int nKeysUpdated = selector.select();
			
			logger.log(Level.INFO, "nKeysUpdated:" + nKeysUpdated);
			
			updateSelectionKeys();
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
	
	private void updateSelectionKeys() throws IOException
	{
		Iterator<SelectionKey> it = selector.selectedKeys().iterator();
		while(it.hasNext()){
			handleSelectionKey(it.next());
			it.remove();
		}
	}
	
	private void handleSelectionKey(SelectionKey selectionKey) throws IOException
	{
		if(selectionKey.isAcceptable()){
			onReadyAccept((ServerSocketChannel) selectionKey.channel());
			return;
		}
		
		Client client = (Client) selectionKey.attachment();
		
		if(selectionKey.isReadable()){
			client.onReadyRecv();
		}
		if(selectionKey.isValid() == false){
			return;
		}
		if(selectionKey.isWritable()){
			client.onReadySend();
		}
	}
	
	private void onReadyAccept(ServerSocketChannel serverSocketChannel) throws IOException
	{
		SocketChannel socketChannel = serverSocketChannel.accept();
		SelectionKey selectionKey = registerToSelector(socketChannel, SelectionKey.OP_READ);
		selectionKey.attach(new Client(clientManager, selectionKey));
	}
	
	private SelectionKey registerToSelector(SelectableChannel channel, int ops) throws IOException
	{
		channel.configureBlocking(false);
		return channel.register(selector, ops);
	}
	
	private final int port;
	private final ClientManager clientManager = new ClientManager();
	
	private ServerSocketChannel serverSocketChannel;
	private Selector selector;
}