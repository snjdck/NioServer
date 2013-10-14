#ifndef SOCKET_H
#define SOCKET_H

#include <netinet/in.h>
#include <string>

typedef unsigned char byte;

class Socket
{
public:
	Socket(int fd=-1);
	virtual ~Socket();

	bool create();
	bool bind(int port);
	bool listen(int count) const;

	Socket* accept() const;
	bool connect(const std::string& host, int port);

	int recv(byte* buffer, int buffer_size);
	int send(byte* buffer, int buffer_size);

	bool setBlocking(bool flag) const;

	bool isValid() const;
	int getFd() const;
private:
	struct sockaddr_in m_address;
	int m_fd;
};

#endif