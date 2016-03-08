import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOfRange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import snjdck.ioc.Injector;
import cudgel.PacketQueue;
import cudgel.ClientManager;
import cudgel.PacketDispatcher;
import cudgel.PacketSender;
import cudgel.PacketUtil;

public class LogicServer
{
	public static void main(String[] args)
	{
		new LogicServer("127.0.0.1", 7410);
	}

	final PacketQueue		packetRecvQueue	= new PacketQueue();
	final PacketQueue		packetSendQueue	= new PacketQueue();
	final Socket			socket			= new Socket();
	final byte[]			recvBuffer		= new byte[0x20000];
	
	final Injector			injector		= new Injector();
	final PacketDispatcher	dispatcher		= new PacketDispatcher(injector);
	final PacketSender		packetSender	= new PacketSender(packetSendQueue);

	public LogicServer(String host, int port)
	{
		injector.mapValue(PacketSender.class, packetSender);
		injector.mapSingleton(ClientManager.class);
		HandlerInit.Init(dispatcher, "handler_config.xml");
		try{
			socket.connect(new InetSocketAddress(host, port));
			socket.getOutputStream().write(PacketUtil.CreateNamePacket("logic"));
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
			int begin = 0;
			for(;;){
				int end = inputStream.read(recvBuffer, begin, recvBuffer.length - begin);
				if(end <= 0)
					return;
				end += begin;
				begin = 0;
				for(;;){
					if(end - begin < 2)
						break;
					int packetLen = PacketUtil.ReadShort(recvBuffer, begin);
					if(end - begin < packetLen)
						break;
					packetRecvQueue.put(copyOfRange(recvBuffer, begin, begin + packetLen));
					begin += packetLen;
				}
				if(begin >= end){
					begin = 0;
				}else if(begin <= 0){
					begin = end;
				}else{
					arraycopy(recvBuffer, begin, recvBuffer, 0, end - begin);
					begin = end - begin;
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