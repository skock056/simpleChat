import java.io.*;
import java.util.*;
import common.ChatIF;

public class ServerConsole implements ChatIF{
	
	EchoServer sv;
	Scanner fromConsole;
	
	public ServerConsole (EchoServer sv) {
		this.sv = sv;
		fromConsole = new Scanner(System.in);
	}
	
	public void display(String message) {
		System.out.println(message);
	}
	
	public void accept() 
	  {
	    try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        sv.handleMessageFromServer("SERVER MESSAGE> " + message);
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }
	
	public static void main(String[] args) {
	    int port = 0; //Port to listen on

	    try {
	    	port = Integer.parseInt(args[0]); //Get port from command line
	    } catch(Throwable t) {
	    	port = 5555; //Set port to 5555
	    }
	    
	    EchoServer sv = new EchoServer(port);
	    
	    ServerConsole console = new ServerConsole(sv);
	    
	    sv.setConsole(console);
	    
	    try {
	    	sv.listen(); //Start listening for connections
	    } catch (Exception ex) {
	    	System.out.println("ERROR - Could not listen for clients!");
	    }
	    
	    console.accept();
	  }
	
}