package cudgel;

public class PacketSender
{
	final PacketQueue packetSendQueue;
	
	public PacketSender(PacketQueue	packetSendQueue)
	{
		this.packetSendQueue = packetSendQueue;
	}
	
	public void send(byte[] packet)
	{
		packetSendQueue.put(packet);
	}
}
