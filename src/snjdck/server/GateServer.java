package snjdck.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;

import snjdck.core.IPacketDispatcher;
import snjdck.core.IoSession;

public class GateServer extends Server
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
	}
	
	public GateServer()
	{
		super();
	}

	@Override
	protected void select() throws IOException
	{
		selector.select();
	}

	@Override
	protected IoSession createSession(SelectionKey selectionKey)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onUpdate(long timeElapsed)
	{
		// TODO Auto-generated method stub

	}
}