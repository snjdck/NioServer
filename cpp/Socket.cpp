#include "Socket.h"

#include <stdlib.h>
#include <memory.h>
#include <iostream>
#include <fcntl.h>

#include <sys/socket.h>
#include <sys/types.h>
#include <arpa/inet.h>

using namespace std;

Socket::Socket(int fd):m_fd(fd)
{
}

Socket::~Socket()
{
	if(isValid()){
		::close(m_fd);
	}
}

bool Socket::create()
{
	m_fd = ::socket(AF_INET, SOCK_STREAM, 0);
	return isValid();
}

bool Socket::bind(int port)
{
	m_address.sin_family = AF_INET;
	m_address.sin_addr.s_addr = htonl(INADDR_ANY);
	m_address.sin_port = htons(port);

	int bind_result = ::bind(m_fd, (struct sockaddr*)&m_address, sizeof(m_address));
	return -1 != bind_result;
}

bool Socket::listen(int count) const
{
	int listen_result = ::listen(m_fd, count);
	return -1 != listen_result;
}

Socket* Socket::accept() const
{
	Socket* socket = new Socket();
	int addrLen = sizeof(socket->m_address);
	socket->m_fd = ::accept(m_fd, (struct sockaddr*)&(socket->m_address), (socklen_t *)&addrLen);
	return socket;
}

bool Socket::connect(const string& host, int port)
{
	m_address.sin_family = AF_INET;
	m_address.sin_addr.s_addr = inet_addr(host.c_str());
	m_address.sin_port = htons(port);

	int result = ::connect(m_fd, (struct sockaddr*)&m_address, sizeof(m_address));
	return -1 != result;
}

int Socket::recv(byte* buffer, int buffer_size)
{
	memset(buffer, 0, buffer_size);
	int bytesRead = ::recv(m_fd, buffer, buffer_size, 0);
	return bytesRead;
}

int Socket::send(byte* buffer, int buffer_size)
{
	int bytesWrite = ::send(m_fd, buffer, buffer_size, 0);
	return bytesWrite;
}

bool Socket::setBlocking(bool flag) const
{
	int opts = fcntl(m_fd, F_GETFL);

	if(-1 == opts){
		cout << "socket set option error" << endl;
		return false;
	}
	if(flag){
		opts &= ~O_NONBLOCK;
	}else{
		opts |= O_NONBLOCK;
	}
	
	fcntl(m_fd, F_SETFL, opts);
	return true;
}

bool Socket::isValid() const
{
	return -1 != m_fd;
}

int Socket::getFd() const
{
	return m_fd;
}