import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/*
*gui for client part
*
*/
public class ClientGui extends JFrame implements ActionListener, WindowListener{
	private JLabel label;
	private JTextField textfield;
	private JTextArea conversation;
	private Client client1;
	private JButton login;
	private JButton logout;
	private static final long serialVersionUID = 1L;
	private boolean connected;

	public void windowClosed(WindowEvent e) {}
	public void windowClosing(WindowEvent e){}
	public void windowOpened(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}

	ClientGui(){
		
		super("client");
		client1=null;
//JFrame.setDefaultLookAndFeelDecorated(true);
		/*
		 * north part of the panel
		 */
		JPanel north=new JPanel(new GridLayout(2,1));
		JPanel message=new JPanel(new GridLayout(2,1,1,5));
		north.add(new JLabel("enter your message here: ",SwingConstants.CENTER));
		textfield=new JTextField("*****");
		textfield.setBackground(Color.WHITE);
		north.add(textfield);
		add(north,BorderLayout.NORTH);
	
		/*
		 * the middle part of panel
		 */
		JPanel center=new JPanel(new GridLayout(1,5,1,5));
		conversation=new JTextArea("welcome to the Char room\n",40,40);
		conversation.setEditable(false);
		appendMsg("please click the login button first\n");
		center.add(new JScrollPane(conversation));
		add(center,BorderLayout.CENTER);
		
		/*
		 * the south part of the panel
		 */
		JPanel south =new JPanel(new GridLayout(1,5,1,5));
		
		login=new JButton("Login");
		login.addActionListener(this);
		logout=new JButton("Logout");
		logout.addActionListener(this);
		login.setEnabled(true);
		logout.setEnabled(false);
		south.add(login);
		south.add(logout);
		add(south,BorderLayout.SOUTH);
		addWindowListener(this);
		setSize(400,200);
		setVisible(true);
		textfield.requestFocus();
		
	}
	private String ServerAddress(){
		return JOptionPane.showInputDialog(textfield,"Enter IP Address of the Server",
				JOptionPane.QUESTION_MESSAGE);
	}
	private String Name(){
		return JOptionPane.showInputDialog(textfield,"Enter your name",
				JOptionPane.QUESTION_MESSAGE);
	}
	private String getPort(){
		return JOptionPane.showInputDialog(textfield,"Enter port number of the Server",
				JOptionPane.QUESTION_MESSAGE);
	}
	/*
	 * function append message
	 */
	void appendMsg(String str){
		conversation.append(str);
		conversation.setCaretPosition(conversation.getText().length()-1);
	}
	/*
	 * Function for actionperformed 
	 */
	public void actionPerformed(ActionEvent e){
		Object click=e.getSource();
		if(click==logout){
			client1.sendMsg("logout");
			System.exit(0);
			
		}
		if(connected){
			String a=textfield.getText();
			client1.sendMsg(a);
			textfield.setText("");
			return;
		}
		  
		 if(click==login){
			connected=true;
			String userName=Name();
			String IP=ServerAddress();
			int port=Integer.parseInt(getPort());
			client1=new Client(IP,port,userName,this);
			client1.start();
			login.setEnabled(false);
			logout.setEnabled(true);
			textfield.addActionListener(this);
		}
		 
		
		
	}
	/*
	 * function called by gui when the connection is failed
	 * reset all the button lable textfeild
	 */
	void connectionFailed(){
		login.setEnabled(true);
		logout.setEnabled(false);
		textfield.setText("*****");
		textfield.removeActionListener(this);
		connected=false;
		
	}
	
	public static void main(String args[]){
		new ClientGui();
	}
}
