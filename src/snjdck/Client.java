package snjdck;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import snjdck.core.IGameWorld;
import snjdck.core.IPacket;
import snjdck.core.IoSession;

public class Client implements IoSession
{
	private static final Logger logger = Logger.getLogger(Client.class.getName());
	static private final Charset charset = Charset.forName("UTF-8");
	
	public Client(IGameWorld gameWorld, SelectionKey selectionKey)
	{
		this.gameWorld = gameWorld;
		this.selectionKey = selectionKey;
		
		recvBuffer = ByteBuffer.allocate(0x20000);
		sendBuffer = ByteBuffer.allocate(0x10000);
		
		sendPacketList = new LinkedList<IPacket>();
		
		nextPacket = new Packet();
	}
	
	public int getID()
	{
		return 0;
	}
	
	public void login()
	{
		gameWorld.getClientManager().addClient(this);
	}
	
	public void logout()
	{
		selectionKey.cancel();
		gameWorld.getClientManager().removeClient(this);
	}
	
	@Override
	public void onReadyRecv()
	{
		SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
		final int nBytesRead;
		
		try{
			nBytesRead = socketChannel.read(recvBuffer);
		}catch(IOException e){
			logger.info("client quit!");
			logout();
			return;
		}
		
		if(nBytesRead < 0){
			logger.info("recv bytes < 0");
			logout();
		}else if(nBytesRead > 0){
			recvBuffer.flip();
			readPacket();
		}else{
			logger.info("读取数据长度为0!");
		}
	}
	
	private void readPacket()
	{
		boolean isReadSuccess;
		do{
			isReadSuccess = nextPacket.read(recvBuffer);
			if(isReadSuccess){
				gameWorld.addAction(this, nextPacket);
				nextPacket = new Packet();
			}else if(recvBuffer.position() > 0){
				if(recvBuffer.hasRemaining()){
					recvBuffer.compact();
				}else{
					recvBuffer.clear();
				}
			}
		}while(isReadSuccess);
	}
	
	@Override
	public void onReadySend()
	{
		SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
		
		try{
			int nBytesWrite = socketChannel.write(sendBuffer);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void send(Object msg)
	{
//		sendQueue.add(msg);
		if(false == isSending){
			selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			isSending = true;
		}
	}
	
	public void reply(IPacket packet, Object msg)
	{
		
	}
	
	private IPacket nextPacket;
	
	private final IGameWorld gameWorld;
	private final SelectionKey selectionKey;
	
	private final List<String> sendQueue = new LinkedList<String>();
	private boolean isSending;
	
	private final ByteBuffer recvBuffer;
	private final ByteBuffer sendBuffer;
	
	private final LinkedList<IPacket> sendPacketList;
}
