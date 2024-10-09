package Model;

import java.io.Serializable;

/**
 * Clase que representa a un usuario en el sistema.
 * Contiene la información del usuario, como email, contraseña, nombre,
 * DNI, teléfono y ID de la empresa.
 * 
 * @author Alder
 */
public class User implements Serializable{
    private String email;
    private String password;
    private String name;
    private String dni;
    private String telefono;
    private int companyID;

    /**
     * Constructor por defecto que inicializa un usuario vacío.
     */
    public User() {
        this.email = "";
        this.password = "";
        this.name = "";
        this.dni = "";
        this.telefono = "";
        this.companyID = 0;
    }

    /**
     * Constructor que inicializa un usuario con los datos proporcionados.
     * 
     * @param email     El email del usuario.
     * @param password  La contraseña del usuario.
     * @param name      El nombre del usuario.
     * @param dni       El DNI del usuario.
     * @param telefono  El número de teléfono del usuario.
     * @param companyID El ID de la empresa asociada al usuario.
     */
    public User(String email, String password, String name, String dni, String telefono, int companyID) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.dni = dni;
        this.telefono = telefono;
        this.companyID = companyID;
    }

    /**
     * Obtiene el email del usuario.
     * 
     * @return El email del usuario.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el email del usuario.
     * 
     * @param email El nuevo email del usuario.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la contraseña del usuario.
     * 
     * @return La contraseña del usuario.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     * 
     * @param password La nueva contraseña del usuario.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el nombre del usuario.
     * 
     * @return El nombre del usuario.
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del usuario.
     * 
     * @param name El nuevo nombre del usuario.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene el DNI del usuario.
     * 
     * @return El DNI del usuario.
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI del usuario.
     * 
     * @param dni El nuevo DNI del usuario.
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Obtiene el número de teléfono del usuario.
     * 
     * @return El número de teléfono del usuario.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el número de teléfono del usuario.
     * 
     * @param telefono El nuevo número de teléfono del usuario.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene el ID de la empresa asociada al usuario.
     * 
     * @return El ID de la empresa.
     */
    public int getCompanyID() {
        return companyID;
    }

    /**
     * Establece el ID de la empresa asociada al usuario.
     * 
     * @param companyID El nuevo ID de la empresa.
     */
    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }
}
