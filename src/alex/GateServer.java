package alex;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import snjdck.util.SocketFactory;

import alex.nio.IoSession;
import alex.nio.io.PacketReader;
import alex.packet.PacketQueue;


public class GateServer
{
	Selector selector;
	
	final PacketQueue packetList;
	final Socket socket;
	
	public GateServer(int port)
	{
		packetList = new PacketQueue();
		
		socket = new Socket();
		try {
			socket.connect(new InetSocketAddress("127.0.0.1", 7410));
			new Thread(new WriteThread()).start();
			new Thread(new ReadThread(socket)).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try{
			startup(port);
			for(;;){
				select();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	void startup(int port) throws IOException
	{
		selector = Selector.open();
		
		ServerSocketChannel serverSocketChannel = SocketFactory.CreateServerSocketChannel(port);
		
		registerToSelector(serverSocketChannel, SelectionKey.OP_ACCEPT);
	}

	void select() throws IOException
	{
		selector.select();
		Iterator<SelectionKey> it = selector.selectedKeys().iterator();
		while(it.hasNext()){
			SelectionKey selectionKey = it.next();
			it.remove();
			handleSelectionKey(selectionKey);
		}
	}
	
	void handleSelectionKey(SelectionKey selectionKey) throws IOException
	{
		if(selectionKey.isValid() && selectionKey.isReadable()){
			IoSession session = (IoSession)selectionKey.attachment();
			session.packetReader.doRecv();
			__onRecvClientPacket(session);
			return;
		}
		if(selectionKey.isValid() && selectionKey.isWritable()){
			IoSession session = (IoSession)selectionKey.attachment();
			session.packetWriter.doSend();
			return;
		}
		if(selectionKey.isValid() && selectionKey.isAcceptable()){
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
			addSocketChannel(serverSocketChannel.accept());
			return;
		}
	}
	
	void addSocketChannel(SocketChannel socketChannel) throws IOException
	{
		SelectionKey selectionKey = registerToSelector(socketChannel, SelectionKey.OP_READ);
		IoSession session = new IoSession(selectionKey);
		selectionKey.attach(session);
		__onClientConnected();
	}
	
	SelectionKey registerToSelector(SelectableChannel channel, int ops) throws IOException
	{
		channel.configureBlocking(false);
		return channel.register(selector, ops);
	}
	
	void __onRecvClientPacket(IoSession session)
	{
		while(session.packetReader.hasPacket()){
			byte[] packet = session.packetReader.shiftPacket();
			packetList.put(packet);
		}
	}
	
	void __onClientConnected()
	{
		packetList.put(null);
	}
	
	class ReadThread implements Runnable
	{
		PacketReader reader;
		
		public ReadThread(Socket socket)
		{
			reader = new PacketReader(socket, 0x20000);
		}
		
		@Override
		public void run()
		{
			try {
				for(;;){
					reader.doRecv();
					while(reader.hasPacket()){
						onPacketRecv(reader.shiftPacket());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		void onPacketRecv(byte[] packet) {
		}
	}

	class WriteThread implements Runnable
	{
		@Override
		public void run()
		{
			try {
				onRun();
			}catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
		void onRun() throws IOException
		{
			OutputStream outputStream = socket.getOutputStream();
			for(;;){
				byte[] packet = packetList.take();
				outputStream.write(packet);
			}
		}
	}
}
