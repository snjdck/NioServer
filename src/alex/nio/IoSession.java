package alex.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

import alex.nio.io.PacketReader;
import alex.nio.io.PacketWriter;



final public class IoSession
{
	private static final Logger logger = Logger.getLogger(IoSession.class.getName());
	
	private final SelectionKey selectionKey;
	public final PacketReader packetReader;
	public final PacketWriter packetWriter;
	
	public IoSession(SelectionKey selectionKey)
	{
		this.selectionKey = selectionKey;
		
		SocketChannel channel = (SocketChannel)selectionKey.channel();
		
		packetReader = new PacketReader(channel.socket(), 0x20000);
		packetWriter = new PacketWriter(selectionKey, 0x10000);
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
	
	public void send(byte[] packet)
	{
		packetWriter.send(packet);
	}
}