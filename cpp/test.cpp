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

#include "EpollServer.h"

using namespace std;

int main()
{
	int port = 2501;
	cout << "server startup at port:" << port << endl;

	EpollServer server(port);
	server.run();

	cout << "server shutdown" << endl;
	return 0;
}