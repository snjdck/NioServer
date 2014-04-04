package snjdck;

import java.nio.channels.SelectionKey;

import snjdck.nio.IoSession;

public interface IClientFactory
{
	IoSession createClient(SelectionKey selectionKey);
}