package snjdck;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

import snjdck.core.IPacketDispatcher;
import snjdck.nio.IPacket;
import snjdck.nio.IoSession;
import snjdck.nio.io.PacketReader;
import snjdck.nio.io.PacketWriter;

public class Client implements IoSession
{
	private static final Logger logger = Logger.getLogger(Client.class.getName());
	
	private final IPacketDispatcher packetDispatcher;
	private IPacket packetFactory;
	
	private final SelectionKey selectionKey;
	private final PacketReader packetReader;
	private final PacketWriter packetWriter;
	
	public Client(SelectionKey selectionKey, IPacketDispatcher packetDispatcher, IPacket packetFactory)
	{
		this.packetDispatcher = packetDispatcher;
		this.packetFactory = packetFactory;
		this.selectionKey = selectionKey;
		
		packetReader = new PacketReader(this, 0x20000, packetFactory.create());
		packetWriter = new PacketWriter(this, 0x10000);
	}
	
	@Override
	public void close()
	{
		selectionKey.cancel();
		try{
			selectionKey.channel().close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void send(int msgId, byte[] msg)
	{
		packetWriter.send(packetFactory.create(msgId, msg));
	}
	
	@Override
	public void onConnected()
	{
		logger.info("client connected!");
	}
	
	@Override
	public void onDisconnected()
	{
		close();
		logger.info("client disconnected!");
	}
	
	@Override
	public void onReadyRecv()
	{
		logger.info("nio ready recv");
		packetReader.onRecv();
		while(packetReader.hasPacket()){
			packetDispatcher.dispatch(this, packetReader.shiftPacket());
		}
	}
	
	@Override
	public void onReadySend()
	{
		logger.info("nio ready send");
		packetWriter.onSend();
	}
	
	@Override
	public void interestReadOp()
	{
		selectionKey.interestOps(SelectionKey.OP_READ);
	}
	
	@Override
	public void interestWriteOp()
	{
		selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
	}
	
	@Override
	public int doRead(ByteBuffer dst)
	{
		int bytesRead = 0;
		try{
			bytesRead = getChannel().read(dst);
		}catch(IOException e){//client force close socket.
			onDisconnected();
		}
		if(bytesRead < 0){//client close socket normally.
			onDisconnected();
		}
		return bytesRead;
	}
	
	@Override
	public int doWrite(ByteBuffer src)
	{
		try{
			return getChannel().write(src);
		}catch(IOException e){
			e.printStackTrace();
			onDisconnected();
		}
		return 0;
	}
	
	private SocketChannel getChannel()
	{
		return (SocketChannel)selectionKey.channel();
	}
}