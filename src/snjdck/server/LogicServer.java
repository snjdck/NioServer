package snjdck.server;
import java.io.IOException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

import snjdck.Client;
import snjdck.core.IGameWorld;


public class LogicServer extends Server
{
	private final IGameWorld gameWorld;
	private final int port;
	
	private ServerSocketChannel serverSocketChannel;
	
	public LogicServer(IGameWorld gameWorld, int port)
	{
		this.gameWorld = gameWorld;
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
		selector.selectNow();
	}

	@Override
	protected void onAccept(SelectionKey selectionKey)
	{
		selectionKey.attach(new Client(gameWorld, selectionKey));
	}

	@Override
	protected void onUpdate(long timeElapsed)
	{
		gameWorld.onUpdate(timeElapsed);
		gameWorld.handleAllActions();
	}
}
