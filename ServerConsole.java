import java.io.*;
import common.ChatIF;

public class ServerConsole implements ChatIF{
	
	EchoServer sv;
	
	public ServerConsole (EchoServer sv) {
		this.sv = sv;
	}
	
	public void display(String message) {
		System.out.println("SERVER MSG> " + message);
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
	    
	    try {
	    	sv.listen(); //Start listening for connections
	    } catch (Exception ex) {
	    	System.out.println("ERROR - Could not listen for clients!");
	    }
	  }
	
}