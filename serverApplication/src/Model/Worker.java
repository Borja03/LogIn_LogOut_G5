/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import ISignable.Signable;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 *
 * @author 2dam
 */
public class Worker implements Runnable {

   private ObjectInputStream objectReader = null;
   private ObjectOutputStream objectWriter = null;
   private Socket socket;
   private Signable signable;
   private TipoMensaje type;
   private Message msg;
   private User user;
   private static final Logger LOGGER = Logger.getLogger(Worker.class.getName());
   
   
    @Override
    public void run() {
        try{
            
        } catch(IncorrectCredentialsException e){
            
        }
    }

}
