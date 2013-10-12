#ifndef SOCKET_H
#define SOCKET_H

#include <stdlib.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#include <string>

using namespace std;

const int MAX_RECV = 0X20000;
const int MAX_SEND = 0X10000;

class Socket
{
public:
	Socket(int fd=-1);
	virtual ~Socket();

	bool create();
	bool bind(int port);
	bool listen(int count) const;
	bool accept(Socket &socket) const;
	bool connect(const string& host, int port);

	int recv();
	int send();

	bool setBlocking(bool flag) const;

	bool isValid() const;
	int getFd() const;
private:
	struct sockaddr_in m_address;
	int m_fd;

	char bufferRecv[MAX_RECV];
	char bufferSend[MAX_SEND];
};

#endif