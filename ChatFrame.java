import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.util.*;

/**
 * A frame containing following widgets:<br/>
 * - a lable showing the port number and user name.<br/>
 * - a text field to type in and to send message.<br/>
 * - a text area to show the chatting history. 
 */
public class ChatFrame extends JFrame {
	
	/**
	 * Construct a ServerFrame.
	 */
	public ChatFrame(String userName, String label) {		
		this.userName = userName;
		JPanel infoPanel = new JPanel();
		infoPanel.add(new JLabel(label));
		add(infoPanel, BorderLayout.NORTH);
		JPanel chatPanel = initChatPanel(userName);
		add(chatPanel, BorderLayout.CENTER);
		pack();	
	}	

	JPanel initChatPanel(final String whoamI){
		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout());	
		
		JPanel msgPanel = new JPanel();
		msgToBeSent = new JTextField(50);		
		msgToBeSent.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				String msg = String.format("%tT %s:\n    %s", new Date(), whoamI, msgToBeSent.getText());
				sendMsg(msg);
				//appendLine(msg);
				msgToBeSent.setText("");
			}
		});
		msgPanel.add(msgToBeSent);
		chatPanel.add(msgPanel, BorderLayout.NORTH);

		JPanel hisPanel = new JPanel();
		history = new JTextArea(20, 50);
		JScrollPane scrollPane = new JScrollPane(history);
		scrollPane.setEnabled(false);				
		Border titled = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "chatting history");
		hisPanel.setBorder(titled);
		hisPanel.add(scrollPane);
		chatPanel.add(hisPanel, BorderLayout.CENTER);	
		return chatPanel;
	}
	
	void setOutput(OutputStream outStream) {
		output = new PrintWriter(outStream, true);
	}
	
	void appendLine(String msg) {
		history.append(msg+"\n");	
	}	
	
	void greet() {
		sendMsg(String.format("%tT %s:\n    Hello, this is %s", new Date(), userName, userName));
	}
	
	void sendMsg(String msg) {
		if(output != null) { output.println(msg);}
	}
	
	private String userName;
	private JTextArea history;
	private JTextField msgToBeSent;
	private PrintWriter output;			
}
