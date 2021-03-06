// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  
  String loginID = "";

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
  }

  
  //Instance methods ************************************************
  
  public void connect() throws IOException{
	  try {
		  openConnection();
		  handleMessageFromClientUI("#login " + loginID);
	  } catch (IOException e) {
			clientUI.display("ERROR - No login ID specified.  Connection aborted.");
	  }
  }
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
	  if (message.startsWith("#") && !message.startsWith("#login ")) {
		  consoleCommand(message);
	  } else {
	  
		  try
		  {
			  sendToServer(message);
		  }
		  catch(IOException e)
		  {
			  clientUI.display
			  ("Could not send message to server.  Terminating client.");
			  quit();
		  }
	  }  
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  /**
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
	protected void connectionClosed() {
		clientUI.display("Connection to server closed. Terminating client.");
	}

	/**
	 * Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
	protected void connectionException(Exception exception) {
		clientUI.display("Abnormal termination of connection.");
	}
	
	public void consoleCommand(String msg) {
		
		if (msg.equals("#quit")) {
			quit();
			
		} else if (msg.equals("#logoff")) {
			try {
				closeConnection();
			} catch (IOException e) {
				
			}
			
		} else if (msg.startsWith("#sethost")) {
			if (!isConnected()) {
				if (msg.length() >= 10) {
					setHost(msg.substring(9));
					clientUI.display("Host set to: " + getHost());
				} else {
					clientUI.display("Invalid host");
				}
			} else {
				clientUI.display("Unable to set new host while connected");
			}
			
		} else if (msg.startsWith("#setport")) {
			if (!isConnected()) {
				try {
					setPort(Integer.parseInt(msg.substring(9)));
					clientUI.display("Port set to: " + getPort());
				} catch (NumberFormatException e) {
					clientUI.display("Invalid port");
				}
			} else {
				clientUI.display("Invalid port");
			}
			
		} else if (msg.equals("#login")) {
			if (!isConnected()) {
				try {
					openConnection();
					handleMessageFromClientUI("#login " + loginID);
				} catch (IOException e) {
					
				}
			} else {
				clientUI.display("Already logged in");
			}
			
		} else if (msg.equals("#gethost")) {
			
			clientUI.display(getHost());
			
		} else if (msg.equals("#getport")) {
			
			clientUI.display(Integer.toString(getPort()));
			
		} else {
			clientUI.display("Invalid command");
		}
	}
}
//End of ChatClient class

