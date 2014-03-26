package snjdck.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

final public class SocketFactory
{
	static public ServerSocketChannel CreateServerSocketChannel(int port)
	{
		ServerSocketChannel channel = null;
		try{
			channel = ServerSocketChannel.open();
			ServerSocket socket = channel.socket();
			socket.setReuseAddress(true);
			socket.bind(new InetSocketAddress(port));
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
		
		return channel;
	}
	
	static public SocketChannel CreateSocketChannel(String host, int port)
	{
		SocketChannel channel = null;
		try{
			channel = SocketChannel.open();
			channel.connect(new InetSocketAddress(host, port));
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
		return channel;
	}
}