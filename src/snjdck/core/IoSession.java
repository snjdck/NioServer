package snjdck.core;

import java.io.IOException;
import java.nio.ByteBuffer;

import snjdck.server.action.ActionQueue;

public interface IoSession
{
	void onConnected();
	
	void onReadyRecv(ActionQueue actionQueue);
	void onReadySend();
	
	void interestWriteOp();
	void interestReadOp();
	
	int doWrite(ByteBuffer src) throws IOException;
}
