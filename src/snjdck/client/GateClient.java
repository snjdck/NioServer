package snjdck.client;

import java.nio.channels.SelectionKey;

import snjdck.core.IGameWorld;
import snjdck.core.IoSession;
import snjdck.server.action.ActionQueue;

public class GateClient implements IoSession
{

	public GateClient(IGameWorld gameWorld, SelectionKey selectionKey)
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onConnected()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onReadyRecv(ActionQueue actionQueue)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onReadySend()
	{
		// TODO Auto-generated method stub

	}

}
