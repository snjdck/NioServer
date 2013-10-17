package snjdck.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

public class GateServer extends Server
{
	private ServerSocketChannel serverSocketChannel;
	private final int port;
	
	public GateServer(int port)
	{
		this.port = port;
	}
	
	@Override
	public void startup() throws IOException
	{
		super.startup();
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
	protected void onAccept(SelectionKey selectionKey)
	{
	}

	@Override
	protected void onUpdate(long timeElapsed)
	{
	}
}
