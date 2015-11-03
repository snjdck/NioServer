package alex.nio.io;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import alex.nio.IoSession;


final public class PacketWriter extends PacketIO
{
	private final SelectionKey selectionKey;

	public PacketWriter(SelectionKey selectionKey, int bufferSize)
	{
		super(bufferSize);
		this.selectionKey = selectionKey;
	}
	
	public void send(byte[] packet)
	{
		if(hasPacket() == false){
			selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		}
		addPacket(packet);
	}

	public void doSend() throws IOException
	{
		writePacketsToBuffer();
		
		buffer.flip();
		
		SocketChannel channel = (SocketChannel)selectionKey.channel();
		channel.write(buffer);
		
		if(buffer.hasRemaining()){
			buffer.compact();
			return;
		}
		
		buffer.clear();
		
		if(hasPacket() == false){
			selectionKey.interestOps(SelectionKey.OP_READ);
		}
	}
	
	private void writePacketsToBuffer()
	{
		while(hasPacket())
		{
			byte[] packet = getPacket();
			if(buffer.remaining() < packet.length){
				break;
			}
			buffer.put(packet);
			shiftPacket();
		}
	}
}
