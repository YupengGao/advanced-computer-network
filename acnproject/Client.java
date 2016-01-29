/*
*this is the code for client part
*this code can run independently without the client gui code
*/

import java.io.*;
import java.net.*;
public class Client {
    private int port;
    private String ip;
    private String username;
    private ClientGui cg;
    private Socket smtpSocket ;
    private PrintWriter out ;
    private BufferedReader in ;
	
	/*
	*constructor for client 
	*initialize for the client name, prot number, ip address
	*/
    Client(String ip,int port,String username,ClientGui cg){
	    this.username=username;
	    this.port=port;
	    this.ip=ip;
	    this.cg=cg;
		
	  }
    
    Client(String ip,int port,String username){
    	this(ip,port,username,null);
    }
    
    public void start(){
    	
    	try{
    		smtpSocket = new Socket(ip,port);
    		
    	}catch(Exception e){
    		show("error when connecting to server"+e);
    		
    	}
    	
    	show("Client has made connection with server: "+smtpSocket.getInetAddress());
    	
    	try{
    		in = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));
            out = new PrintWriter(smtpSocket.getOutputStream(),true);
    	}catch(IOException ioE){
    		show("error when creating IO stream: "+ioE);
    	}
    	handleServer a=new handleServer();
		a.start();

			try{
			sendMsg(username);
			show("begin chatting: ");
		}catch(Exception e2){
			show("ERROR when login: "+e2);
			disconnect();
		}
		
		
    }
    
    
    public void show(String msg){
    	if(cg==null){
    		System.out.println(msg);
    	}
    	else
    		cg.appendMsg(msg+"\n");
    }
    
    
    public void sendMsg(String msg){
    	try{
    		out.println(msg);
    	}catch(Exception e1){
    		show("Error when send msg to server: "+e1);
    	}
    }
    
    private void disconnect(){
    	try{
    		smtpSocket.close();
    		in.close();
    		out.close();
    		cg.connectionFailed();
    	}catch(Exception e){
    		show("Error when closing the client: "+ e);
    	}
    }
    
    /*
	*thread for keep receiving the message from server
	*
	*/
    private  class handleServer extends Thread{

	  private PrintWriter out;
	  private BufferedReader type;
	  private int port;
	  private String ip;
	  
	  handleServer(){

	  }
	  /*
	   * this is for handle the message from server
	   * @see java.lang.Thread#run()
	   */
	  public void run(){
	   try{

			 while (true) {//infinite loop for keep looking at the message get from the server
				    
					String line = in.readLine();

		            	if (line.substring(0,7).equals("MESSAGE")) {
		            		if(cg==null){
		            			show("<<"+line.substring(8) + "\n");
		            		}
		            		else{
		            			cg.appendMsg("<<"+line.substring(8) + "\n");
		            		}
		            } 

		        }
		   }catch(IOException e){
		     show("server has close the connection "+e);
		     if(cg!=null)
		    	 cg.connectionFailed();
		     
		   }
	   

	  
	  }
	}
    
    public static void main(String[] args) throws Exception {


		String ip="localhost";//the default ip address for the server is local host
		int portNumber=1500;//default port number is 1500
		String username;
    
        int n=0;

           System.out.println("please type your name:");
           BufferedReader type=new BufferedReader(new InputStreamReader(System.in));
           String nm=type.readLine();
           Client client=new Client(ip,portNumber,nm);//please note the client here is not a thread
           client.start();
           System.out.println("begin chatting:");
  	
  			 while(true){//here is a infinite loop for keep reading the input of user
  			         
  					String st=type.readLine();

  					if(st.equalsIgnoreCase("LOGOUT")) {// if user type logout then the client break to do the disconnect
  						client.sendMsg("LOGOUT");
  						
  						break;
  					}
  					else{// get the input of user and send it to server
  						client.sendMsg(st);
  					}
  					
  			
  			 }

				
           client.disconnect();
        }
    }

