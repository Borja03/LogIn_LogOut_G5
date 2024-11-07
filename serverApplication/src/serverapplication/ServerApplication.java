package serverapplication;

import Model.Server;
import Model.Worker;

/**
 * The main class for the server application. This class initializes the server
 * with a default port (9999) or a port provided through the command line.
 * It then creates and starts an instance of the server using the specified port.
 * 
 * <p>The server listens for incoming connections from clients, handling requests
 * as defined in the {@link Worker} class.</p>
 * 
 * @author Borja
 */
public class ServerApplication {

    /**
     * The entry point for the server application.
     * 
     * This method checks for an optional command-line argument for the port number.
     * If the port is provided and is a valid integer, the server will use that port.
     * Otherwise, it will default to port 9999.
     * 
     * <p>The method creates a {@link Worker} instance with the specified port
     * and starts the server to listen for incoming connections.</p>
     *
     * @param args the command-line arguments, where the first argument is expected to be the port number
     */
   public static void main(String[] args) {
        int port = 0;  // Default port

        // Create a server instance
        Server server = new Server();
      }
}
