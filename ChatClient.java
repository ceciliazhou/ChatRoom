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

    public static final String DEFAULT_SERVER = "localhost";
    public static final String DEFAULT_USER = "anonymous_client";
    
    private static ChatFrame frame;
    private static String user;
    private static Socket serverSocket;

    public static void main(String[] args)  throws InterruptedException, IOException {
        final String server = (args.length > 0) ? args[0] : DEFAULT_SERVER;
        final int port = (args.length > 1) ? Integer.parseInt(args[1]) : ChatServer.DEFAULT_PORT;   
        user = (args.length > 2) ? args[2] : DEFAULT_USER;
        connect2Server(server, port);   

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                ChatClient.frame = new ChatFrame(user, server, port);
                ChatClient.frame.setTitle("Chatting Client");
                ChatClient.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ChatClient.frame.setVisible(true);                     
            }
        });    

        startChatting();            
    }
    
    /**
     * Start a thread to:
     * 1. connect to the specified server host on the specified port,
     * 2. and then keep processing incoming message from the server.
     * @param server the host name or IP of the server to be connected.
     * @param port the port number on which the server is listening.
     */  
     public static void connect2Server(String server, int port) throws IOException{
        serverSocket = new Socket();
        serverSocket.connect(new InetSocketAddress(server, port), 10000 );        
        PrintWriter output = new PrintWriter(serverSocket.getOutputStream(), true);
        output.println(user);                        
    }

    private static void startChatting(){
        try{
            Scanner incomingMsg = new Scanner(serverSocket.getInputStream());
            while (incomingMsg.hasNextLine()){
                String msg = incomingMsg.nextLine();
                if(ChatClient.frame != null)
                    ChatClient.frame.showMessage(msg);
            }
        }
        catch(IOException e){
        }
        finally{
            try{
                if(serverSocket != null)
                    serverSocket.close();
                    serverSocket  = null;
            }
            catch(IOException e){
            }
        }
    }
    
    public static void sendMsg(String msg) {
        if(serverSocket != null){
            try{
                PrintWriter output = new PrintWriter(serverSocket.getOutputStream(), true);
                output.println(msg);     
            }
            catch(IOException e){
                System.err.println("Failed to send message! Please be sure the server is still alive.");
            }
        }
    }
}
