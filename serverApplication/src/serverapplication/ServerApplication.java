/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication;

import Model.MyServerSocket;

/**
 *
 * @author Borja
 */
public class ServerApplication {

    /**
     * @param args the command line arguments
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