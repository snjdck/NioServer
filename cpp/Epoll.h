#ifndef EPOLL_H
#define EPOLL_H

#include <sys/epoll.h>

const int MAX_EPOLL_SIZE = 2000;

class Epoll
{
public:
	Epoll();
	bool add(int fd, int option);
	bool update(int fd, int option);
	bool remove(int fd);
	int wait();
	int getEventFd(int event_index);
	int getEventOption(int event_index);
private:
	int m_fd;
	int m_count;

	struct epoll_event event_list[MAX_EPOLL_SIZE];
	struct epoll_event event;
};

#endif