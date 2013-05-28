package snjdck;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import snjdck.util.Amf3;

public class Client
{
	private static final Logger logger = Logger.getLogger(Client.class.getName());
	static private final Charset charset = Charset.forName("UTF-8");
	
	public Client(ClientManager clientManager, SelectionKey selectionKey)
	{
		this.clientManager = clientManager;
		this.selectionKey = selectionKey;
		
		recvBuffer = ByteBuffer.allocate(0x20000);
		sendBuffer = ByteBuffer.allocate(0x10000);
	}
	
	public int getID()
	{
		return 0;
	}
	
	public void login()
	{
		clientManager.addClient(this);
	}
	
	public void logout()
	{
		selectionKey.cancel();
		clientManager.removeClient(this);
	}
	
	public void onReadyRecv()
	{
		SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
		final int nBytesRead;
		
		try{
			nBytesRead = socketChannel.read(recvBuffer);
		}catch(IOException e){
			logger.log(Level.INFO, "client quit!");
			logout();
			return;
		}
		
		if(nBytesRead < 0){
			logger.log(Level.INFO, "recv bytes < 0");
			logout();
		}else if(nBytesRead > 0){
			recvBuffer.flip();
			readPacket();
		}else{
			logger.log(Level.INFO, "读取数据长度为0!");
		}
	}
	
	private void readPacket()
	{
		if(recvBuffer.remaining() < 4){
			resetRecvBuffer();
			return;
		}
		
		recvBuffer.mark();
		final int bodySize = recvBuffer.getInt();
		
		if(recvBuffer.remaining() < bodySize){
			recvBuffer.reset();
			resetRecvBuffer();
			return;
		}
		
		byte[] buffer = new byte[bodySize];
		recvBuffer.get(buffer);
		HashMap<String, Object> obj = (HashMap<String, Object>) amf3.decode(buffer);
		logger.info("recv msg:" + obj.get("msg"));
		
		readPacket();
	}
	
	private void resetRecvBuffer()
	{
		if(recvBuffer.position() > 0){
			if(recvBuffer.remaining() > 0){
				recvBuffer.compact();
			}else{
				recvBuffer.clear();
			}
		}
	}
	
	public void onReadySend()
	{
		SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
		
		try{
			int nBytesWrite = socketChannel.write(sendBuffer);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void send(String msg)
	{
		sendQueue.add(msg);
		if(false == isSending){
			selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			isSending = true;
		}
	}
	
	private final Amf3 amf3 = new Amf3();
	
	private final ClientManager clientManager;
	private final SelectionKey selectionKey;
	
	private boolean isSending;
	
	private ArrayList<String> sendQueue = new ArrayList<String>();
	
	private final ByteBuffer recvBuffer;
	private final ByteBuffer sendBuffer;
}
