package snjdck;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.logging.Logger;

import snjdck.core.ClientState;
import snjdck.core.IClient;
import snjdck.core.IGameWorld;
import snjdck.core.IPacket;
import snjdck.core.IPacketDispatcher;
import snjdck.core.IoSession;
import snjdck.packet.Packet;
import snjdck.packet.PacketReader;
import snjdck.packet.PacketWriter;
import snjdck.server.action.ActionQueue;

public class Client implements IClient, IoSession
{
	private static final Logger logger = Logger.getLogger(Client.class.getName());
	static private final Charset charset = Charset.forName("UTF-8");
	
	final private int id;
	private final IPacketDispatcher packetDispatcher;
	
	public Client(int id, IGameWorld gameWorld, SelectionKey selectionKey, IPacketDispatcher packetDispatcher)
	{
		this.id = id;
		this.packetDispatcher = packetDispatcher;
		this.gameWorld = gameWorld;
		this.selectionKey = selectionKey;
		
		packetReader = new PacketReader(this, 0x20000, new Packet());
		packetWriter = new PacketWriter(this, 0x10000);
	}
	
	@Override
	public void handlePacket(IPacket packet)
	{
		packetDispatcher.dispatch(this, packet);
	}

	@Override
	public void onLogin()
	{
		logger.info("client enter!");
	}

	@Override
	public void onLogout()
	{
		logout();
		logger.info("client quit!");
	}

	public int getID()
	{
		return id;
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
	
	@Override
	public void onConnected()
	{
		logger.info("client connected!");
	}

	public void logout()
	{
		selectionKey.cancel();
		gameWorld.getClientManager().removeClient(this);
	}
	
	@Override
	public void onReadyRecv(ActionQueue actionQueue)
	{
		logger.info("nio ready recv");
		packetReader.onRecv(actionQueue);
	}
	
	@Override
	public void onReadySend()
	{
		logger.info("nio ready send");
		packetWriter.onSend();
	}
	
	public void send(int msgId, byte[] msg)
	{
		packetWriter.send(Packet.Create(msgId, msg));
	}
	
	public void reply(IPacket packet, byte[] msg)
	{
		packetWriter.send(packet.createReply(msg));
	}
	
	public void interestReadOp()
	{
		selectionKey.interestOps(SelectionKey.OP_READ);
	}
	
	public void interestWriteOp()
	{
		selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
	}
	
	private SocketChannel getChannel()
	{
		return (SocketChannel)selectionKey.channel();
	}
	
	public int doRead(ByteBuffer dst) throws IOException
	{
		return getChannel().read(dst);
	}
	
	public int doWrite(ByteBuffer src) throws IOException
	{
		return getChannel().write(src);
	}
	
	private ClientState state = ClientState.CONNECTED;
	
	public final IGameWorld gameWorld;
	private final SelectionKey selectionKey;
	
	private final PacketReader packetReader;
	private final PacketWriter packetWriter;
}