#include "EpollServer.h"

#include <errno.h>
#include <stdio.h>
#include <iostream>

EpollServer::EpollServer(int port)
{
	bool result = false;

	result = server.create();
	if(false == result){
		cout << "socket create error" << endl;
		return;
	}
	result = server.bind(port);
	if(false == result){
		cout << "socket bind error, port:" << port << endl;
		return;
	}
	result = server.listen(MAX_EPOLL_SIZE);
	if(false == result){
		cout << "socket listen error" << endl;
		return;
	}

	server.setBlocking(false);
}

EpollServer::~EpollServer()
{
	map<int, Socket*>::iterator it;
	for(it=socketDict.begin();it != socketDict.end(); it++){
		delete it->second;
	}
}

void EpollServer::run()
{
	bool success = epoll.add(server.getFd(), EPOLLIN);
	if(false == success){
		return;
	}

	while(true)
	{
		const int event_count = epoll.wait();

		cout << "loop " << event_count << endl;
		for(int i=0; i<event_count; i++)
		{
			const int option = epoll.getEventOption(i);
			if((option & EPOLLERR) || (option & EPOLLHUP))
			{
				perror("epoll error");
				continue;
			}
			const int fd = epoll.getEventFd(i);
			cout << "print fd:" << fd;
			cout << "print server fd:" << server.getFd();

			if(option & EPOLLIN)
			{
				if(fd == server.getFd()){
					onAccept();
				}else{
					onRecv(socketDict[fd]);
				}
			}

			if(option & EPOLLOUT)
			{
				onSend(socketDict[fd]);
			}
		}
	}
}

void EpollServer::onAccept()
{
	Socket* socket = new Socket();
	server.accept(*socket);

	socket->setBlocking(false);
	bool success = epoll.add(socket->getFd(), EPOLLIN);

	socketDict[socket->getFd()] = socket;

	cout << "onAccept:" << success << socket->getFd() << endl;
}

void EpollServer::onRecv(Socket* socket)
{
	socket->recv();
	cout << "onRecv";
}

void EpollServer::onSend(Socket* socket)
{

}