import java.util.*;
import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
public class Server{

  /*
  *server private parameter 
  *
  */
  private static HashSet<String> names;
  private static HashSet<PrintWriter> writers;
  private int port;
  private boolean isServerGoing;
  private SimpleDateFormat time;
  private int seq;
  private ServerGui sg;
  
  /*
  *server constructor with GUI
  *
  */
  public Server(int port, ServerGui sg){
    this.sg=sg;
	this.port=port;
	names=new HashSet<String>();
	writers=new HashSet<PrintWriter>();
	time=new SimpleDateFormat("HH:mm:ss");
  }
  /*
  *constructor without GUI
  *
  */
  public Server(int port){
   this(port,null);
  }
  /*
  *function start() 
  *start the server 
  */
   
   public void start(){
     isServerGoing=true;
	 seq=0;
	  
	  try{
	  ServerSocket serversocket=new ServerSocket(port);
	  show("The server is running....");
	  show("listening to the port "+port);
      while(isServerGoing){
      new handleClient(seq++,serversocket.accept());//every time when a client trying to make a connection with the server, the server will new a thread for listening at the client
	  if(!isServerGoing)
	  break;
      }
        try{
        	serversocket.close();
        }
        catch(Exception e){
        	show("Exception closing the server and clients: "+e);
        }
	   
	  }catch(Exception e){
		  String msg=time.format(new Date())+"Exception on new ServerSocket: "+e+"\n";
		  show(msg);
	  }
	  

}
  /*
  *function show() 
  *print the message 
  */
   private void show(String msg){
     String t=time.format(new Date())+" "+msg;
	 if(sg==null)
	  System.out.println(t);
	 else
	  sg.appendEvent(t +"\n");
   } 
   
   /*
  *function stop() 
  *for the gui to stop the server
  */
   public void stop(){
     isServerGoing =false;
	 try{
	    new Socket("localhost",port);//connect to myself as client to exit statement
		                             //we don't want the socket just break up. we need it can be connected to server again
	 }
	 catch(Exception e){}
   } 
   
  
   
  public static void main(String args[]) throws Exception{
     int portNumber =1500;
	 Server server=new Server(portNumber);
	 server.start();
 
  
  
 
}
/*
*this is use for handling client when there is a client connecting to server
*
*/
   private class handleClient extends Thread{
    private int seqNum;
    private String name;
    private Socket socket;
    //InputStream is=null;
    //OutputStream os=null;
    private BufferedReader in;
    private PrintWriter out;
    
    handleClient(int sequenceNumber, Socket socket){
    this.socket=socket;
    seqNum=sequenceNumber;
    start();
    show("make connection with client #:"+seqNum);
   }
    
    public void run(){
    	
	//System.out.println("aabb");
     try{
    	 boolean isGoing=true;
         in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
         out=new PrintWriter(socket.getOutputStream(),true);
         //if(sg==null){
        	  name=in.readLine();
         //}
        
         show(name+" just connected.");

         writers.add(out);//add the printwirter corresponding to this client. for multicast
 
        while(isGoing){
               String input=in.readLine();
			   show(input);
               if(input==null){

               	return;
               	}

              if(input.equalsIgnoreCase("logout")){
            	  show(name+" just disconnected from the server.");
            	  //write.println(logout);
            	  isGoing=false;
            	  break;
              }
              else{//broadcast
            	  for(PrintWriter write: writers){
                 write.println("MESSAGE "+name+ " say: "+input);
               }
              }
                 



            }
      }//end of try

      catch(IOException e){
        System.out.println(e);
         }
       finally{
              if(name!=null){
               names.remove(name);
               }
             if(out!=null){
              writers.remove(out);
               }
               try{
                socket.close();
               }catch(IOException e){}
      }
     }//end of run()

   }//end of class handleClient
}//end of all


