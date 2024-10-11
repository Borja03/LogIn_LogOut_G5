/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import ISignable.Signable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import exception.*;
import java.io.IOException;
import java.util.logging.Level;
/**
 * La clase <code>SignerClient</code> implementa la interfaz <code>Signable</code>,
 * proporcionando métodos para el registro e inicio de sesión de un usuario.
 * 
 * <p>
 * Actualmente, los métodos <code>signUp</code> y <code>signIn</code> lanzan una 
 * excepción <code>UnsupportedOperationException</code> ya que no están 
 * implementados. Se espera que estos métodos sean implementados en el futuro 
 * para proporcionar la funcionalidad deseada.
 * </p>
 *
 * @author Alder
 */

public class SignerClient implements Signable {
    
    private static final ResourceBundle archivo = ResourceBundle.getBundle("Utils.socketConfig");
    private static final int PUERTO = Integer.parseInt(archivo.getString("PORT"));
    private static final String HOST = archivo.getString("IP");
    private static final Logger LOGGER = Logger.getLogger("/Model/SignerClient");
    TipoMensaje mt;
    private Message msg = null;

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param user El usuario que se desea registrar.
     * @return El usuario registrado.
     * @throws Exception Si ocurre un error durante el registro.
     */
    @Override
    public User signUp(User user) throws Exception {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try {
            LOGGER.info("Iniciando registro...");
            //Instanciamos el socket
            Socket socketCliente = new Socket(HOST, PUERTO);
            //Creamos el output y preparamos el encapsulador para enviarlo al servidor
            oos = new ObjectOutputStream(socketCliente.getOutputStream());
            msg = new Message();
            msg.setUser(user);
            msg.setTipo(TipoMensaje.SIGN_UP_REQUEST);
            oos.writeObject(msg);

            //Recibimos el objeto encapsulado del servidor
            ois = new ObjectInputStream(socketCliente.getInputStream());
            msg = (Message) ois.readObject();
            user = msg.getUser();
            //Cerramos las conexiónes
            oos.close();
            socketCliente.close();
            //Dependiendo de el mensaje que reciva lanza o escribe un mensaje nuevo
            switch (msg.getTipo()) {
                case OK_RESPONSE:
                    return user;
                case EMAIL_EXISTS:
                    throw new UserAlreadyExistsException("El usuario ya existe");
                case SERVER_ERROR:
                    throw new ConnectionException("Ha ocurrido algun error en el servidor");
                case MAX_THREAD_USER:
                    throw new Exception("Maximo de usuarios alcanzado, inténtelo más tarde");
            }
            //Control de errores
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SignerClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SignerClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SignerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Devuleve un objeto user
        return user;
    }

    /**
     * Inicia sesión para un usuario existente.
     *
     * @param user El usuario que desea iniciar sesión.
     * @return El usuario que ha iniciado sesión.
     * @throws Exception Si ocurre un error durante el inicio de sesión.
     */
    @Override
    public User signIn(User user) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
