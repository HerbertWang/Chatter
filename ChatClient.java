import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;

public class ChatClient {

	private BufferedReader in;
	private PrintWriter out;
	private JFrame frame=new JFrame("chatter");
	private JTextField dataField=new JTextField(40);
	private JTextArea messageArea=new JTextArea(8,60);
	
	public ChatClient()
	{
		dataField.setEditable(false);
		messageArea.setEditable(false);
		frame.getContentPane().add(dataField,"North");
		frame.getContentPane().add(new JScrollPane(messageArea),"Center");
		frame.pack();
		
		dataField.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						out.println(dataField.getText());
						dataField.setText("");
					}
				});
	}
	
	private String getServerAddress(){
		return JOptionPane.showInputDialog( 
				frame,
				"Enter IP address of the Server:",
				"welcome to the chatter",
				JOptionPane.QUESTION_MESSAGE);
	}
	
	private String getName() {
		return JOptionPane.showInputDialog( 
				frame,
				"choose a screen name",
				"Screen name selection",
				JOptionPane.PLAIN_MESSAGE);
	}
	
	
	@SuppressWarnings("unused")
	private void run() throws IOException {	
        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, 9001);
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
         out = new PrintWriter(socket.getOutputStream(), true);
         
         
         while (true) {
             String line = in.readLine();
             if (line.startsWith("SUBMITNAME")) {
                 out.println(getName());
             } else if (line.startsWith("NAMEACCEPTED")) {
                 dataField.setEditable(true);
             } else if (line.startsWith("MESSAGE")) {
                 messageArea.append(line.substring(8) + "\n");
             }
         }
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ChatClient client=new ChatClient();
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.frame.setVisible(true);
		client.run();
	}

}
