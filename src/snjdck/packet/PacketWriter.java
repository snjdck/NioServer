package snjdck.packet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

import snjdck.Client;
import snjdck.core.IPacket;

final public class PacketWriter
{
	private final ByteBuffer buffer;
	private final LinkedList<IPacket> sendQueue;
	private final Client client;

	public PacketWriter(Client client, int bufferSize)
	{
		this.client = client;
		buffer = ByteBuffer.allocate(bufferSize);
		sendQueue = new LinkedList<IPacket>();
	}
	
	public void send(IPacket packet)
	{
		if(sendQueue.isEmpty()){
			client.getSelectionKey().interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		}
		sendQueue.add(packet);
	}

	public void onSend()
	{
		writePacketsToBuffer();
		buffer.flip();
		
		SelectionKey selectionKey = client.getSelectionKey();
		SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
		final int nBytesWrite;
		
		try{
			nBytesWrite = socketChannel.write(buffer);
		}catch(IOException e){
			client.onLogout();
			return;
		}
		
		if(nBytesWrite < 0){
			client.onLogout();
			return;
		}
		
		if(buffer.hasRemaining()){
			buffer.compact();
			return;
		}
		
		buffer.clear();
		if(sendQueue.isEmpty()){
			selectionKey.interestOps(SelectionKey.OP_READ);
		}
	}
	
	private void writePacketsToBuffer()
	{
		while(sendQueue.size() > 0)
		{
			IPacket packet = sendQueue.getFirst();
			if(packet.write(buffer)){
				sendQueue.removeFirst();
			}else{
				break;
			}
		}
	}
}
