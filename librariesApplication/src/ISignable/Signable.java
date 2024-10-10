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
     * @return User es el objeto que se retorna en el metodo
     * @throws Exception Si ocurre un error durante el registro.
     */
    public User signUp(User user) throws Exception;

    /**
     * Método para iniciar sesión de un usuario existente.
     * 
     * @return User es el objeto que se retorna en el metodo
     * @throws Exception Si ocurre un error durante el inicio de sesión.
     */
    public User signIn(User user) throws Exception;

}
