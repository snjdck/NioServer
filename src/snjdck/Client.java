package snjdck;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Logger;

import snjdck.core.ClientState;
import snjdck.core.IClient;
import snjdck.core.IGameWorld;
import snjdck.core.IPacket;
import snjdck.core.IoSession;

public class Client implements IClient, IoSession
{
	private static final Logger logger = Logger.getLogger(Client.class.getName());
	static private final Charset charset = Charset.forName("UTF-8");
	
	public Client(IGameWorld gameWorld, SelectionKey selectionKey)
	{
		this.gameWorld = gameWorld;
		this.selectionKey = selectionKey;
		
		recvBuffer = ByteBuffer.allocate(0x20000);
		sendBuffer = ByteBuffer.allocate(0x10000);
		
		packetListToSend = new LinkedList<IPacket>();
		
		nextRecvPacket = new Packet();
	}
	
	public int getID()
	{
		return 0;
	}
	
	@Override
	public ClientState state()
	{
		return state;
	}
	
	@Override
	public void state(ClientState newState)
	{
		state = newState;
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
		logger.info("nio ready recv");
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
			logout();
			return;
		}
		if(nBytesRead > 0){
			recvBuffer.flip();
			readPacketsFromBuffer();
		}
	}
	
	private void readPacketsFromBuffer()
	{
		while(nextRecvPacket.read(recvBuffer)){
			gameWorld.addAction(this, nextRecvPacket);
			nextRecvPacket = new Packet();
		}
		if(recvBuffer.hasRemaining()){
			recvBuffer.compact();
		}else{
			recvBuffer.clear();
		}
	}
	
	private void writePacketsToBuffer()
	{
		while(packetListToSend.size() > 0)
		{
			IPacket packet = packetListToSend.getFirst();
			if(packet.write(sendBuffer)){
				packetListToSend.removeFirst();
			}else{
				break;
			}
		}
	}
	
	@Override
	public void onReadySend()
	{
		logger.info("nio ready send");
		
		writePacketsToBuffer();
		sendBuffer.flip();
		
		SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
		
		try{
			socketChannel.write(sendBuffer);
		}catch(IOException e){
			e.printStackTrace();
			logout();
			return;
		}
		
		if(sendBuffer.hasRemaining()){
			sendBuffer.compact();
		}else{
			sendBuffer.clear();
			if(packetListToSend.isEmpty()){
				selectionKey.interestOps(SelectionKey.OP_READ);
			}
		}
	}
	
	private void sendPacket(IPacket packet)
	{
		if(packetListToSend.isEmpty()){
			selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		}
		packetListToSend.add(packet);
	}
	
	public void send(int msgId, HashMap<String, Object> msg)
	{
		sendPacket(Packet.Create(msgId, msg));
	}
	
	public void reply(IPacket packet, HashMap<String, Object> msg)
	{
		sendPacket(packet.createReply(msg));
	}
	
	private ClientState state = ClientState.CONNECTED;
	
	public final IGameWorld gameWorld;
	private final SelectionKey selectionKey;
	
	private final ByteBuffer recvBuffer;
	private final ByteBuffer sendBuffer;
	
	private IPacket nextRecvPacket;
	private final LinkedList<IPacket> packetListToSend;
}