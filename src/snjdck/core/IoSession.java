package snjdck.core;

import snjdck.server.action.ActionQueue;

public interface IoSession
{
	void onConnected();
	void onReadyRecv(ActionQueue actionQueue);
	void onReadySend();
}
