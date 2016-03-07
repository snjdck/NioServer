package cudgel;

public interface PacketHandler
{
	void exec(int clientId, byte[] packet);
}
