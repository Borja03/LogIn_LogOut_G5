package serverapplication;


import Model.MyServerSocket;

/**
 * The main class for the server application. This class initializes the server
 * with a default port (9999) or a port provided through the command line.
 * It then creates and starts an instance of the server using the specified port.
 * 
 * <p>The server listens for incoming connections from clients, handling requests
 * as defined in the {@link MyServerSocket} class.</p>
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
     * <p>The method creates a {@link MyServerSocket} instance with the specified port
     * and starts the server to listen for incoming connections.</p>
     *
     * @param args the command-line arguments, where the first argument is expected to be the port number
     */
    public static void main(String[] args) {
        int port = 9999;  // Default port

        // Check if a port is provided via command-line arguments
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
               // System.out.println("Invalid port number. Using default port 9999.");
            }
        }
        
        // Create a server instance
        MyServerSocket server = new MyServerSocket(port);
        
        // Start the server
        server.start();
    }
}