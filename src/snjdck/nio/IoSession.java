package snjdck.nio;

import java.nio.ByteBuffer;

public interface IoSession
{
	void onConnected();
	void onDisconnected();
	
	void onReadyRecv();
	void onReadySend();
	
	void interestWriteOp();
	void interestReadOp();
	
	int doRead(ByteBuffer dst);
	int doWrite(ByteBuffer src);
}