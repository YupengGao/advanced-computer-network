import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class ServerGui extends JFrame implements ActionListener, WindowListener{
	private JLabel label;
	private JTextField tPortNumber;
	private JTextArea event;
	private Server server;
	private JButton button;
	private static final long serialVersionUID = 1L;
	
	public void windowClosed(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}

	ServerGui(int port){
		
		super("server");
		server=null;
//JFrame.setDefaultLookAndFeelDecorated(true);
		/*
		 * north part of the panel
		 */
		JPanel north=new JPanel();
		north.add(new JLabel("port number: "));
		tPortNumber= new JTextField(" "+port );
		north.add(tPortNumber);
		button=new JButton("start");
		button.addActionListener(this);
		north.add(button);
		add(north,BorderLayout.NORTH);
		/*
		 * the south part of panel
		 */
		JPanel center=new JPanel(new GridLayout(1,1));
		event=new JTextArea(40,40);
		event.setEditable(false);
		appendEvent("Event log.\n");
		center.add(new JScrollPane(event));
		add(center);
		
		addWindowListener(this);
		setSize(400,200);
		setVisible(true);
		
	}
	/*
	 * function append message
	 */
	void appendEvent(String str){
		event.append(str);
		//event.setCaretPosition();
	}
	/*
	 * Button start 
	 */
	public void actionPerformed(ActionEvent e){
		if(server!=null){
			server.stop();
			server=null;
			tPortNumber.setEditable(true);
			button.setText("Start");
			return;
		}
		int port;
		try{
			port=Integer.parseInt(tPortNumber.getText().trim());
		}
		catch(Exception e1){
			appendEvent("invalid port number");
			return;
		}
		server =new Server(port,this);
		new ServerRunning().start();
		button.setText("stop");
		tPortNumber.setEditable(false);
		
	}
	
	public static void main(String args[]){
		new ServerGui(2000);
	}
	
	public void windowClosing(WindowEvent e){
		if(server!=null){
			try{
				server.stop();
			}
			catch(Exception eClose){}
			server=null;
		}
		dispose();
		System.exit(0);
	}
	
	class ServerRunning extends Thread{
		public void run(){
			server.start();
			button.setText("start");
			tPortNumber.setEditable(true);
			appendEvent("Server crashed\n");
			server=null;
		}
	}
	

}
