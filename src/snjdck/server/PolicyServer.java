package snjdck.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

import snjdck.core.IoSession;
import snjdck.server.action.ActionQueue;


public class PolicyServer extends Server
{
	static public void main(String[] args)
	{
		String s = "<policy-file-request/>";
		s = "<?xml version=\"1.0\"?><cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"*\" /></cross-domain-policy>";
	}
	
	private ServerSocketChannel serverSocketChannel;
	
	public PolicyServer()
	{
		super(null);
	}

	@Override
	public void startup()
	{
		super.startup();
		serverSocketChannel = CreateServerSocketChannel(843);
		registerToSelector(serverSocketChannel, SelectionKey.OP_ACCEPT);
	}

	@Override
	public void shutdown()
	{
		super.shutdown();
		try{
			serverSocketChannel.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	protected void select()
	{
		try{
			selector.select();
		}catch(IOException e){
			e.printStackTrace();
		}
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