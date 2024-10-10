package ISignable;

import Model.User;

/**
 * Interfaz que define las operaciones de registro e inicio de sesión para los usuarios.
 * Esta interfaz debe ser implementada por cualquier clase que maneje la lógica
 * de autenticación de usuarios.
 * 
 * @author Alder
 */
public interface Signable {
    
    /**
     * Método para registrar un nuevo usuario.
     * 
     * @param user El objeto User que contiene la información del nuevo usuario.
     * @throws Exception Si ocurre un error durante el registro.
     */
    public void signUp(User user) throws Exception;

    /**
     * Método para iniciar sesión de un usuario existente.
     * 
     * @param user El objeto User que contiene la información de inicio de sesión.
     * @throws Exception Si ocurre un error durante el inicio de sesión.
     */
    public void signIn(User user) throws Exception;
    
        /**
     * Método para sacar datos de un usuario existente.
     * 
     * @param user El objeto User que contiene la información de inicio de sesión.
     * @throws Exception Si ocurre un error durante el inicio de sesión.
     */
    public void main(User user) throws Exception; 
    
    
}
