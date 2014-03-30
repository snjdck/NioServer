package snjdck.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AioServer
{
	static public void main(String[] args)
	{
	}
	
	private AsynchronousServerSocketChannel socket;

	public AioServer() throws IOException
	{
		ExecutorService executor = Executors.newFixedThreadPool(20);
		AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withThreadPool(executor);
		
		socket = AsynchronousServerSocketChannel.open(channelGroup);
		
		socket.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		socket.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
		
		socket.bind(new InetSocketAddress(2501));
		
		pendingAccept();
	}
	
	private void pendingAccept()
	{
		socket.accept(this, acceptHandler);
	}
	
	static private CompletionHandler<AsynchronousSocketChannel, AioServer> acceptHandler = new CompletionHandler<AsynchronousSocketChannel, AioServer>(){
		@Override
		public void completed(AsynchronousSocketChannel socket, AioServer server){
			AioClient client = new AioClient(socket);
			client.start();
			server.pendingAccept();
		}
		@Override
		public void failed(Throwable exc, AioServer attachment){
			exc.printStackTrace();
		}
	};
}