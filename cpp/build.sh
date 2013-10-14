#! /bin/bash
file="/home/dev/testcpp/hello"
if [ -f "$file" ]; then
	rm -f "$file"
fi
g++ -o "$file" test.cpp Epoll.cpp Socket.cpp EpollServer.cpp
"$file"