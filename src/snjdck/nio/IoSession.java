package snjdck.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

import entityengine.IComponent;

import snjdck.core.IPacketDispatcher;
import snjdck.ioc.tag.Inject;
import snjdck.nio.io.PacketReader;
import snjdck.nio.io.PacketWriter;

final public class IoSession implements IComponent
{
	private static final Logger logger = Logger.getLogger(IoSession.class.getName());
	
	private IPacket packetFactory;
	
	@Inject
	public Client client;
	
	@Inject
	public IPacketDispatcher packetRouter;
	
	private final SelectionKey selectionKey;
	private final PacketReader packetReader;
	private final PacketWriter packetWriter;
	
	public IoSession(SelectionKey selectionKey, IPacket packetFactory)
	{
		this.packetFactory = packetFactory;
		this.selectionKey = selectionKey;
		
		packetReader = new PacketReader(this, 0x20000, packetFactory.create());
		packetWriter = new PacketWriter(this, 0x10000);
	}
	
	public void updateIO()
	{
		if(selectionKey.isValid() && selectionKey.isReadable()){
			logger.info("nio ready recv");
			packetReader.onRecv();
			while(packetReader.hasPacket()){
				packetRouter.dispatch(client, packetReader.shiftPacket());
			}
		}
		if(selectionKey.isValid() && selectionKey.isWritable()){
			logger.info("nio ready send");
			packetWriter.onSend();
		}
	}
	
	public void close()
	{
		selectionKey.cancel();
		try{
			selectionKey.channel().close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void send(int msgId, byte[] msg)
	{
		packetWriter.send(packetFactory.create(msgId, msg));
	}
	
	private void onDisconnected()
	{
		close();
		client.onDisconnected();
	}
	
	public void interestWriteOp()
	{
		selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
	}
	
	public void uninterestWriteOp()
	{
		selectionKey.interestOps(SelectionKey.OP_READ);
	}
	
	public int doRead(ByteBuffer dst)
	{
		int bytesRead = 0;
		try{
			bytesRead = channel().read(dst);
		}catch(IOException e){//client force close socket.
			onDisconnected();
		}
		if(bytesRead < 0){//client close socket normally.
			onDisconnected();
		}
		return bytesRead;
	}
	
	public int doWrite(ByteBuffer src)
	{
		try{
			return channel().write(src);
		}catch(IOException e){
			e.printStackTrace();
			onDisconnected();
		}
		return 0;
	}
	
	private SocketChannel channel()
	{
		return (SocketChannel)selectionKey.channel();
	}
}