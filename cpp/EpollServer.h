#ifndef EPOLLSERVER_H
#define EPOLLSERVER_H

#include <map>
#include <vector>

#include "Socket.h"
#include "Epoll.h"

using namespace std;

class EpollServer
{
public:
	EpollServer(int port);
	virtual ~EpollServer();
	void run();
private:
	void onAccept();
	void onRecv(Socket* socket);
	void onSend(Socket* socket);
private:
	Socket server;
	Epoll epoll;
	map<int, Socket*> socketDict;
};

#endif