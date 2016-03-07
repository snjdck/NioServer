package cudgel;

import java.io.UnsupportedEncodingException;

public class PacketUtil
{
	static public byte[] CreateNamePacket(String name)
	{
		byte[] packet = new byte[2+name.length()];
		WriteShort(packet, 0, packet.length);
		try{
			System.arraycopy(name.getBytes("UTF8"), 0, packet, 2, name.length());
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return packet;
	}
	
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
