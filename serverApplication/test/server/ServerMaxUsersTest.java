/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import Model.Message;
import Model.Server;
import Model.TipoMensaje;
import Model.User;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Omar
 */
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ServerMaxUsersTest {
    private Server server;
    private static final ResourceBundle config = ResourceBundle.getBundle("Utils.socketConfig");
    private static final int MAX_USERS = Integer.parseInt(config.getString("MAX_USERS"));
    private static final int PORT = Integer.parseInt(config.getString("PORT"));
    private List<Socket> clientSockets;
    private List<ObjectOutputStream> outputStreams;
    private List<ObjectInputStream> inputStreams;
    
    @Before
    public void setUp() {
        // Start server in a separate thread
        Thread serverThread = new Thread(() -> {
            server = new Server();
        });
        serverThread.setDaemon(true); // Make it a daemon thread so it doesn't prevent JVM shutdown
        serverThread.start();
        
        // Give the server some time to start
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        clientSockets = new ArrayList<>();
        outputStreams = new ArrayList<>();
        inputStreams = new ArrayList<>();
    }
    
    @Test
    public void testMaxUsersConnection() throws Exception {
        // Create a latch to wait for all connections to complete
        CountDownLatch connectionsLatch = new CountDownLatch(MAX_USERS + 1);
        List<Message> responses = new ArrayList<>();
        
        // Try to connect MAX_USERS + 1 clients
        for (int i = 0; i < MAX_USERS + 1; i++) {
            final int clientIndex = i;
            Thread clientThread = new Thread(() -> {
                try {
                    // Create client socket
                    Socket socket = new Socket("localhost", PORT);
                    clientSockets.add(socket);
                    
                    // Create streams
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    outputStreams.add(out);
                    
                    // Create test user
                    User testUser = new User();
                    testUser.setEmail("test" + clientIndex + "@test.com");
                    testUser.setPassword("password");
                    
                    // Create and send message
                    Message message = new Message();
                    message.setUser(testUser);
                    message.setTipo(TipoMensaje.SIGN_IN_REQUEST);
                    out.writeObject(message);
                    
                    // Read response
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    inputStreams.add(in);
                    Message response = (Message) in.readObject();
                    synchronized(responses) {
                        responses.add(response);
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    connectionsLatch.countDown();
                }
            });
            clientThread.start();
        }
        
        // Wait for all connections to complete or timeout after 10 seconds
        assertTrue("Timed out waiting for connections", 
                  connectionsLatch.await(10, TimeUnit.SECONDS));
        
        // Verify results
        int maxThreadResponses = 0;
        int normalResponses = 0;
        
        for (Message response : responses) {
            if (response.getTipo() == TipoMensaje.MAX_THREAD_USER) {
                maxThreadResponses++;
            } else {
                normalResponses++;
            }
        }
        
        // Assert that we got exactly MAX_USERS successful connections
        assertEquals("Should have MAX_USERS successful connections", 
                    MAX_USERS, normalResponses);
        
        // Assert that we got exactly one MAX_THREAD_USER response
        assertEquals("Should have one MAX_THREAD_USER response", 
                    1, maxThreadResponses);
    }
    
    @After
    public void tearDown() throws Exception {
        // Close all streams and sockets
        for (ObjectInputStream in : inputStreams) {
            try {
                in.close();
            } catch (Exception e) {
                // Ignore
            }
        }
        
        for (ObjectOutputStream out : outputStreams) {
            try {
                out.close();
            } catch (Exception e) {
                // Ignore
            }
        }
        
        for (Socket socket : clientSockets) {
            try {
                socket.close();
            } catch (Exception e) {
                // Ignore
            }
        }
        
        // Stop the server by simulating 'q' input
        // You might need to modify your Server class to expose a stop method
        // or implement a different shutdown mechanism
    }
}