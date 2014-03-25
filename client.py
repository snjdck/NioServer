import socket
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect(('localhost', 2501))

while True:
	content = raw_input("input:")
	sock.send(content)
	#print sock.recv(1024)