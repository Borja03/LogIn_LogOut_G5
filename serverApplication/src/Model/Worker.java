/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import ISignable.Signable;
import database.*;
import exception.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
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
        try {
            objectReader = new ObjectInputStream (socket.getInputStream());
            DaoFactory factory = new DaoFactory();
            signable = factory.getSignable();
            msg = (Message) objectReader.readObject();
            switch (msg.getTipo()) {
                case SIGN_IN_REQUEST:
                    LOGGER.info("Iniciando sesión");
                    user = signable.signIn(msg.getUser());
                    msg.setUser(user);
                    if (user == null) {
                        msg.setTipo(type.SERVER_ERROR);
                    } else {
                        msg.setTipo(type.OK_RESPONSE);
                    }
                    break;

                case SIGN_UP_REQUEST:
                    LOGGER.info("Registrando usuario");
                    user = signable.signUp(msg.getUser());
                    msg.setUser(user);
                    if (user == null) {
                        msg.setTipo(type.SERVER_ERROR);
                    } else {
                        msg.setTipo(type.OK_RESPONSE);
                    }
                    break;
            }

        } catch (IncorrectCredentialsException e) {
            msg.setTipo(type.INCORRECT_CREDENTIALS_RESPONSE);
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, e);
        } catch (UserAlreadyExistsException e) {
            msg.setTipo(type.EMAIL_EXISTS);
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, e);
        } catch (ConnectionException e) {
            msg.setTipo(type.SERVER_ERROR);
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, e);
        } catch (ClassNotFoundException e) {
            msg.setTipo(type.SERVER_ERROR);
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            msg.setTipo(type.SERVER_ERROR);
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, e);

        } catch (Exception ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                LOGGER.info("Cerrando conexiones");
                objectWriter = new ObjectOutputStream(socket.getOutputStream());
                objectWriter.writeObject(msg);
                //CREAR FUNCION BORRAR CLIENTE
                objectReader.close();
                objectWriter.close();
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
