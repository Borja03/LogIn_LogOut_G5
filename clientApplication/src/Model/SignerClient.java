/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import ISignable.Signable;

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

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param user El usuario que se desea registrar.
     * @return El usuario registrado.
     * @throws Exception Si ocurre un error durante el registro.
     */
    @Override
    public User signUp(User user) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
