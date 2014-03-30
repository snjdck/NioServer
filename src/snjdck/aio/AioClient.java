package snjdck.aio;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import snjdck.packet.Packet;
import snjdck.packet.PacketReader;
import snjdck.packet.PacketWriter;

public class AioClient
{
	private AsynchronousSocketChannel socket;
	
	private final PacketReader packetReader;
	private final PacketWriter packetWriter;
	private boolean isSending = false;
	
	public AioClient(AsynchronousSocketChannel socket)
	{
		this.socket = socket;
		
		packetReader = new PacketReader(0x20000, new Packet());
		packetWriter = new PacketWriter(null, 0x10000);
	}
	
	public void start()
	{
		pendingRead();
	}
	
	public void send()
	{
		if(isSending){
			
		}
	}
	
	private void pendingRead()
	{
		socket.read(packetReader.getByteBuffer(), this, handlerRead);
	}
	
	private void pendingWrite()
	{
		socket.write(packetWriter.getByteBuffer(), this, handlerWrite);
	}
	
	public void onClosed()
	{
		
	}
	
	static private CompletionHandler<Integer, AioClient> handlerRead = new CompletionHandler<Integer, AioClient>(){
		@Override
		public void completed(Integer nBytesRead, AioClient client){
			if(nBytesRead < 0){
				client.onClosed();
				return;
			}
			client.packetReader.onRecv(nBytesRead);
			client.pendingRead();
		}
		@Override
		public void failed(Throwable exc, AioClient client){
			exc.printStackTrace();
		}
	};
	
	static private CompletionHandler<Integer, AioClient> handlerWrite = new CompletionHandler<Integer, AioClient>(){
		@Override
		public void completed(Integer bytesWrite, AioClient client){
		}
		@Override
		public void failed(Throwable exc, AioClient client){
			exc.printStackTrace();
		}
	};
}
