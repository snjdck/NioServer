package snjdck.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

import snjdck.GameWorld;
import snjdck.PacketDispatcherFactory;
import snjdck.core.IGameWorld;
import snjdck.core.IPacketDispatcher;
import snjdck.core.IoSession;
import snjdck.server.action.ActionQueue;
import snjdck.util.SocketFactory;


public class PolicyServer extends Server
{
	static public void main(String[] args)
	{
		PolicyServer policyServer = new PolicyServer();
		
		try{
			policyServer.startup();
			policyServer.runMainLoop();
		}catch(IOException e){
			e.printStackTrace();
		}
		String s = "<policy-file-request/>";
		s = "<?xml version=\"1.0\"?><cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"*\" /></cross-domain-policy>";
	}
	
	private ServerSocketChannel serverSocketChannel;
	
	public PolicyServer()
	{
		super();
	}

	@Override
	public void startup() throws IOException
	{
		super.startup();
		serverSocketChannel = SocketFactory.CreateServerSocketChannel(843);
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

class PolicyClient implements IoSession
{
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