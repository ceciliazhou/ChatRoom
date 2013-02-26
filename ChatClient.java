/**
 * This GUI program models a client in a Server-client architechture based chatting service.
 * To use this tool, please follow the instructions as follows:
 *
 * 1. start the ChatServer program on the server host.
 *    USAGE: java ChatServer port user
 *           optional command line arguments:
 *               [port]: the port number to listen and accept the incoming connection request, 8000 by default.
 *               [user]: the user using the chatting server, "anonymous_user" by default.
 *
 * 2. start the ChatClient program on the client host.
 *    USAGE: java ChatClient serverHost serverPort user
 *           optional command line arguments:
 *               [serverHost]: the IP or host name of the server to be connected to, "localhost" by default.
 *               [serverPort]: the port number the server is listening to, 8000 by default.
 *               [user]: the user using the chatting client, "anonymous_user" by default.
 *
 * 3. Once the two parts are set up, the two users can send message to each other. The chatting history are also
 *    displayed in the text area below the message typing box.
 * 
 * NOTE: The client will fail to connect if the server is not turned on or wrong server host and port number are given.
 *
 *@author: Cecilia
 */
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;

/**
 * ChatServer models a client in a Server-client architechture based chatting service.
 * It will connect to the server at the starting time.
 * Once connected there is no distinction between the server and client.
 * It will send the user's name together with the message to the server and allow messages to be sent and received from server asynchronously.
 */
public class ChatClient{
	
	public static void main(String[] args) {
		final String serverHost = (args.length > 0) ? args[0] : DEFAULT_SERVER;
		final int port = (args.length > 1) ? Integer.parseInt(args[1]) : ChatServer.DEFAULT_PORT;	
		final String user = (args.length > 2) ? args[2] : DEFAULT_USER;
		final ChatClient client = new ChatClient();

		EventQueue.invokeLater(new Runnable() {
		public void run() {
					client.frame = new ChatFrame(user, String.format("Server: %s    Port: %s    User: %s", serverHost, port, user ));
					client.frame.setTitle("Chatting Client");
					client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					client.frame.setVisible(true);						
				}
		});						
			
		// wait until the frame is ready.
		try {
			while(client.frame == null)Thread.sleep(500);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		if(client.connectToServer(serverHost, port)) {
			client.processIncomingMsg();
		}
	}

	/**
	 * Connect to the specified server host on the specified port.
	 * @param host the host name or IP of the server to be connected.
	 * @param port the port number on which the server is listening.
	 */	 
	private boolean connectToServer(String host, int port) {
		serverSocket = new Socket();
		try {
			serverSocket.connect(new InetSocketAddress(host, port), 10000 );		
			OutputStream output = serverSocket.getOutputStream();
			frame.setOutput(output);
			frame.greet();
			frame.appendLine("Connect to " + host + " [" + port + "] successfully!");
		}
		catch (IOException e){
			frame.appendLine(String.format("Failed to connect to %s [ %d ].", host, port));
			frame.appendLine("Please verify the server host name and the port number and make sure the server is started.\nThen restart this program.");
			return false;
		}
		return true;
	}
	
	/**
	 * Keeping processing incoming message from the server.
	 */
	private void processIncomingMsg() {
		while(true) {
			try {
				Scanner incomingMsg = new Scanner(serverSocket.getInputStream());
				while (incomingMsg.hasNextLine()){
					frame.appendLine(incomingMsg.nextLine());
				}
			}
			catch(IOException e) {
				frame.appendLine(e.toString());
			}
		}
	}	
	
	private static final String DEFAULT_SERVER = "localhost";
	private static final String DEFAULT_USER = "anonymous_client";
	private Socket serverSocket;
	private ChatFrame frame;
}
