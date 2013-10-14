#ifndef EPOLLSERVER_H
#define EPOLLSERVER_H

#include <map>
#include <vector>

#include "Socket.h"
#include "Epoll.h"

using namespace std;

const int MAX_RECV = 0X20000;
const int MAX_SEND = 0X10000;


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
	void handleEvent(int fd, int option);
private:
	Socket server;
	Epoll epoll;
	map<int, Socket*> socketDict;

	byte bufferRecv[MAX_RECV];
	byte bufferSend[MAX_SEND];
};

#endif