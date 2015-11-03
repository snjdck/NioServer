package alex.nio.io;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;

final public class PacketReader extends PacketIO
{
	final Socket socket;
	
	public PacketReader(Socket socket, int bufferSize)
	{
		super(bufferSize);
		this.socket = socket;
	}
	
	private void readPacketsFromBuffer()
	{
		for(;;){
			short packetSize = buffer.getShort(0);
			if(buffer.remaining() < packetSize){
				break;
			}
			byte[] packet = new byte[packetSize];
			buffer.get(packet);
			addPacket(packet);
		}
	}
	
	public void doRecv() throws IOException
	{
		int nBytesRead = doReadImpl();
		if(nBytesRead < 0){
			throw new IOException();
		}
		if(0 == nBytesRead){
			return;
		}
		buffer.flip();
		readPacketsFromBuffer();
		if(buffer.hasRemaining()){
			buffer.compact();
		}else{
			buffer.clear();
		}
	}
	
	private int doReadImpl() throws IOException
	{
		SocketChannel channel = socket.getChannel();
		if(null == channel){
			return socket.getInputStream().read(buffer.array(), buffer.position(), buffer.remaining());
		}
		return channel.read(buffer);
	}
}