#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <iostream>

#include <sys/epoll.h>
#include <sys/resource.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#include <string>
#include <memory.h>
#include <fcntl.h>

#include "Socket.h"
#include "Epoll.h"
#include "EpollServer.h"

using namespace std;

int main()
{
	EpollServer server(7410);
	server.run();

	cout << "hello linux world!~";
	return 0;
}