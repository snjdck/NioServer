package snjdck.nio;

import java.nio.ByteBuffer;

import snjdck.nio.action.ActionQueue;

public interface IoSession
{
	void onConnected();
	
	void onReadyRecv(ActionQueue actionQueue);
	void onReadySend();
	
	void interestWriteOp();
	void interestReadOp();
	
	int doRead(ByteBuffer dst);
	int doWrite(ByteBuffer src);
	
	void handlePacket(IPacket packet);
}