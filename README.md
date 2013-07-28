About
----------
- ChatServer:
	
  The server allows clients to connect and disconnect, receive messages, and send the messages to all connected clients.

- ChatClient:
        
  The client connects to server and initiates a graphical interface that allows user to type in text and display the ongoing dialog.

Try it
----------
This program models a 1-to-N Server-client chatting room. To use this tool, please follow the instructions as follows:

1. start server. 


		USAGE: java ChatServer [port] 
		  [port]: the port number to listen and accept the incoming connection request, 8000 by default.

2. start clients and chatting.


		USAGE: java ChatClient [serverHost] [serverPort] [user]
		  [serverHost]: the IP or host name of the server to be connected to, "localhost" by default.
		  [serverPort]: the port number the server is listening to, 8000 by default.
		  [user]: the user using the chatting client, "anonymous_user" by default.
	    
