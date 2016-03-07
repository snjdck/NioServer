import alex.packet.PacketQueue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

import snjdck.ioc.Injector;

import cudgel.ClientManager;
import cudgel.PacketDispatcher;
import cudgel.PacketSender;

public class LogicServer
{
	public static void main(String[] args)
	{
		new LogicServer("127.0.0.1", 7410);
	}

	final PacketQueue		packetRecvQueue	= new PacketQueue();
	final PacketQueue		packetSendQueue	= new PacketQueue();
	final Socket			socket			= new Socket();
	final ByteBuffer		recvBuffer		= ByteBuffer.allocate(0x20000);
	
	final Injector			injector		= new Injector();
	final PacketDispatcher	dispatcher		= new PacketDispatcher(injector);
	final PacketSender		packetSender	= new PacketSender(packetSendQueue);

	public LogicServer(String host, int port)
	{
		injector.mapValue(PacketSender.class, packetSender);
		injector.mapSingleton(ClientManager.class);
		try{
			socket.connect(new InetSocketAddress(host, port));
			ByteBuffer buffer = ByteBuffer.allocate(7);
			buffer.putShort((short)buffer.capacity());
			buffer.put("logic".getBytes("UTF8"));
			socket.getOutputStream().write(buffer.array());
			new HandlerInit(dispatcher);
			init();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	void init()
	{
		new Thread(new ThreadRecv()).start();
		new Thread(new ThreadSend()).start();
		for(;;){
			dispatcher.dispatch(packetRecvQueue.take());
		}
	}

	class ThreadRecv implements Runnable
	{
		@Override
		public void run()
		{
			try{
				onRun(socket.getInputStream());
			}catch(IOException e){
				e.printStackTrace();
			}
		}

		void onRun(InputStream inputStream) throws IOException
		{
			for(;;){
				int nBytesRead = inputStream.read(recvBuffer.array(),
						recvBuffer.position(), recvBuffer.remaining());
				if(nBytesRead <= 0)
					return;
				recvBuffer.position(recvBuffer.position()+nBytesRead);
				recvBuffer.flip();
				int offset = 0;
				for(;;){
					if(recvBuffer.remaining() < 2)
						break;
					int packetLen = recvBuffer.getShort(offset) & 0xFFFF;
					if(recvBuffer.remaining() < packetLen)
						break;
					byte[] packet = new byte[packetLen];
					recvBuffer.get(packet);
					packetRecvQueue.put(packet);
					offset += packetLen;
				}
				if(recvBuffer.hasRemaining()){
					recvBuffer.compact();
				}else{
					recvBuffer.clear();
				}
			}
		}
	}

	class ThreadSend implements Runnable
	{
		@Override
		public void run()
		{
			try{
				onRun(socket.getOutputStream());
			}catch(IOException e){
				e.printStackTrace();
			}
		}

		void onRun(OutputStream outputStream) throws IOException
		{
			for(;;){
				outputStream.write(packetSendQueue.take());
			}
		}
	}
}
