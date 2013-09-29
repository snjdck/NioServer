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
	public ClientState getState()
	{
		return state;
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
		boolean isReadSuccess = false;
		do{
			isReadSuccess = nextRecvPacket.read(recvBuffer);
			if(isReadSuccess){
				gameWorld.addAction(this, nextRecvPacket);
				nextRecvPacket = new Packet();
			}else if(recvBuffer.position() > 0){
				if(recvBuffer.hasRemaining()){
					recvBuffer.compact();
				}else{
					recvBuffer.clear();
				}
			}
		}while(isReadSuccess);
	}
	
	private void writePacket()
	{
		boolean isWriteSuccess = false;
		do{
			isWriteSuccess = nextSendPacket.write(sendBuffer);
			if(isWriteSuccess){
				if(packetListToSend.isEmpty()){
					nextSendPacket = null;
					break;
				}else{
					nextSendPacket = packetListToSend.removeFirst();
				}
			}
		}while(isWriteSuccess);
	}
	
	@Override
	public void onReadySend()
	{
		logger.info("nio ready send");
		if(null != nextSendPacket){
			writePacket();
		}
		
		sendBuffer.flip();
		
		SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
		final int nBytesWrite;
		
		try{
			nBytesWrite = socketChannel.write(sendBuffer);
		}catch(IOException e){
			e.printStackTrace();
			return;
		}
		
		if(nBytesWrite < 0){
			logger.info("write bytes < 0");
			logout();
		}else if(nBytesWrite > 0){
			if(sendBuffer.hasRemaining()){
				sendBuffer.compact();
			}else{
				sendBuffer.clear();
				selectionKey.interestOps(SelectionKey.OP_READ);
				isSending = false;
			}
		}else{
			logger.info("写入数据长度为0!");
		}
	}
	
	private void sendPacket(IPacket packet)
	{
		packetListToSend.add(packet);
		if(false == isSending){
			nextSendPacket = packetListToSend.removeFirst();
			selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			isSending = true;
		}
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
	
	private IPacket nextRecvPacket;
	private IPacket nextSendPacket;
	
	private final IGameWorld gameWorld;
	private final SelectionKey selectionKey;
	
	private boolean isSending;
	
	private final ByteBuffer recvBuffer;
	private final ByteBuffer sendBuffer;
	
	private final LinkedList<IPacket> packetListToSend;
}