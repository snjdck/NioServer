#include "Epoll.h"

Epoll::Epoll():m_count(0)
{
	m_fd = ::epoll_create(MAX_EPOLL_SIZE);
}

bool Epoll::add(int fd, int option)
{
	event.events = option;
	event.data.fd = fd;

	bool success = ::epoll_ctl(m_fd, EPOLL_CTL_ADD, fd, &event) >= 0;
	if(false == success){
		return false;
	}

	++m_count;
	return true;
}

bool Epoll::remove(int event_index)
{
	int fd = getEventFd(event_index);
	bool success = ::epoll_ctl(m_fd, EPOLL_CTL_DEL, fd, &event) >= 0;
	if(false == success){
		return false;
	}

	--m_count;
	return true;
}

int Epoll::wait()
{
	return ::epoll_wait(m_fd, event_list, m_count, -1);
}

int Epoll::getEventFd(int event_index)
{
	return event_list[event_index].data.fd;
}

int Epoll::getEventOption(int event_index)
{
	return event_list[event_index].events;
}