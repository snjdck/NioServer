package alex.packet;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

final public class PacketQueue
{
	final BlockingQueue<byte[]> queue;
	
	public PacketQueue()
	{
		queue = new LinkedBlockingQueue<byte[]>();
	}
	
	public byte[] take()
	{
		try {
			return queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void put(byte[] packet)
	{
		try {
			queue.put(packet);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
