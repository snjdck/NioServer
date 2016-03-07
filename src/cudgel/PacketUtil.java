package cudgel;

public class PacketUtil
{
	static public int ReadShort(byte[] packet, int index)
	{
		int a = 0xFF & packet[index];
		int b = 0xFF & packet[index+1];
		return a << 8 | b;
	}
	
	static public void WriteShort(byte[] packet, int index, int value)
	{
		packet[index  ] = (byte)(value >> 8);
		packet[index+1] = (byte)value;
	}
}
