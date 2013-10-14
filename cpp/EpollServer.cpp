#include "EpollServer.h"

#include <errno.h>
#include <stdio.h>
#include <iostream>
#include <string>

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
	epoll.add(server.getFd(), EPOLLIN);
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
	while(true)
	{
		const int event_count = epoll.wait();

		for(int i=0; i<event_count; i++)
		{
			handleEvent(epoll.getEventFd(i), epoll.getEventOption(i));
		}
	}
}

void EpollServer::handleEvent(int fd, int option)
{
	if((option & EPOLLERR) || (option & EPOLLHUP) || (option & EPOLLRDHUP))
	{
		Socket* socket = socketDict[fd];
		epoll.remove(fd);
		socketDict[fd] = NULL;
		delete socket;
		cout << "epoll option be err or hup" << endl;
		return;
	}
	
	cout << "fd:" << fd << endl;
	cout << "server fd:" << server.getFd() << endl;

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

void EpollServer::onAccept()
{
	Socket* socket = server.accept();

	socket->setBlocking(false);
	bool success = epoll.add(socket->getFd(), EPOLLIN | EPOLLRDHUP);

	socketDict[socket->getFd()] = socket;

	cout << "onAccept:" << socket->getFd() << endl;
}

void EpollServer::onRecv(Socket* socket)
{
	int bytesRead = socket->recv(bufferRecv, MAX_RECV);
	if(bytesRead < 0){
		cout << "socket read error" << endl;
		epoll.remove(socket->getFd());
		socketDict[socket->getFd()] = NULL;
		delete socket;
		return;	
	}
	cout << "onRecv" << endl << string((const char*)bufferRecv);
}

void EpollServer::onSend(Socket* socket)
{

}