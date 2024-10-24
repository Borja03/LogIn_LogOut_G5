package database;

import ISignable.Signable;
import static Model.TipoMensaje.SERVER_ERROR;
import Model.User;
import exception.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase UserDao que implementa la interfaz Signable para gestionar el inicio de
 * sesión y el registro de usuarios en el sistema. Realiza operaciones en las
 * tablas 'res_partner' y 'res_users'.
 *
 * @author Omar y Alder
 */
public class UserDao implements Signable {

    /**
     * Logger utilizado para registrar la actividad de la clase.
     */
    private static final Logger LOGGER = Logger.getLogger(UserDao.class.getName());

    /**
     * Consulta SQL para insertar un nuevo registro en la tabla 'res_partner'.
     */
    private static final String INSERT_USER_PARTNERS_TABLE = "INSERT INTO public.res_partner (name, email, display_name, is_company, company_id, street, city, zip, active) "
                    + "VALUES (?, ?, ?, FALSE, 1, ?, ?, ?, ?) RETURNING id";

    /**
     * Consulta SQL para insertar un nuevo registro en la tabla 'res_users'.
     */
    private static final String INSERT_USER_USERS_TABLE = "INSERT INTO public.res_users (login, password, partner_id, company_id, notification_type) "
                    + "VALUES (?, ?, ?, ?, 'email')";

    /**
     * Consulta SQL para obtener un usuario por su login (email) desde la tabla
     * 'res_users'.
     */
    private static final String SELECT_RES_USERS = "SELECT * FROM public.res_users WHERE login = ?";

    /**
     * Consulta SQL para obtener un partner por su email desde la tabla
     * 'res_partner'.
     */
    private static final String SELECT_RES_PARTNER = "SELECT * FROM public.res_partner WHERE email = ?";

    /**
     * Método auxiliar para realizar el hash de una contraseña.
     *
     * @param password La contraseña en texto plano.
     * @return La contraseña en su forma hash (en este caso sin hash para demo).
     * Se recomienda implementar un algoritmo de hash como bcrypt o SHA-512.
     */
    private String hashPassword(String password) {
        // Para demostración, devolver la contraseña tal como está.
        return password;
    }

    /**
     * Método para realizar el inicio de sesión de un usuario. Verifica si el
     * usuario está activo en la tabla 'res_partner' y si las credenciales
     * coinciden en 'res_users'.
     *
     * @author Alder
     * @param user El objeto User que contiene el email y la contraseña
     * proporcionados por el usuario.
     * @return El objeto User con los datos adicionales obtenidos de la base de
     * datos, como nombre, dirección, etc.
     * @throws Exception Si el usuario no está activo, si las credenciales son
     * incorrectas o si ocurre algún error con la base de datos.
     * @throws IncorrectCredentialsException Si el usuario está inactivo o si
     * las credenciales son incorrectas.
     */
    @Override
    public synchronized User signIn(User user) throws Exception {
        Connection connection = null;
        PreparedStatement partnerStmt = null;
        PreparedStatement userStmt = null;
        ResultSet partnerRs = null;
        ResultSet userRs = null;

        try {
            // Establece la conexión con la base de datos
            connection = DBConnectionPool.getConnection();

            // Consulta para obtener los datos del partner por email
            partnerStmt = connection.prepareStatement(SELECT_RES_PARTNER);
            partnerStmt.setString(1, user.getEmail());
            partnerRs = partnerStmt.executeQuery();

            // Verifica si se encontró el partner
            if (partnerRs.next()) {
                boolean isActive = partnerRs.getBoolean("active");

                // Si el partner no está activo, lanzamos una excepción
                if (!isActive) {
                    throw new IncorrectCredentialsException("El usuario no está activo.");
                }

                // Consulta en res_users para obtener el usuario por su login (email)
                userStmt = connection.prepareStatement(SELECT_RES_USERS);
                userStmt.setString(1, user.getEmail());
                userRs = userStmt.executeQuery();

                // Verifica si se encontró el usuario
                if (userRs.next()) {
                    String passwordInDb = userRs.getString("password");

                    // Comparamos la contraseña ingresada con la almacenada
                    if (passwordInDb.equals(user.getPassword())) {
                        // Login exitoso, retornamos el usuario con los datos de la base de datos
                        user.setEmail(userRs.getString("login"));
                        user.setName(partnerRs.getString("name"));
                        user.setPassword(userRs.getString("password"));
                        user.setActivo(isActive);
                        user.setCompanyID(userRs.getInt("company_id"));
                        user.setCity(partnerRs.getString("city"));
                        user.setStreet(partnerRs.getString("street"));
                        user.setZip(partnerRs.getInt("zip"));
                        return user;
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } finally {
            // Cierra todos los recursos
            if (partnerRs != null) {
                partnerRs.close();
            }
            if (userRs != null) {
                userRs.close();
            }
            if (partnerStmt != null) {
                partnerStmt.close();
            }
            if (userStmt != null) {
                userStmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Método para registrar un nuevo usuario en las tablas 'res_partner' y
     * 'res_users'. Utiliza una transacción para garantizar que ambos registros
     * se realicen correctamente.
     *
     * @author Omar
     * @param user El objeto User que contiene los datos del usuario a registrar
     * (nombre, email, contraseña, dirección, etc.).
     * @return El objeto User con los datos registrados en la base de datos.
     * @throws Exception Si se produce un error en la inserción de los datos o
     * en la transacción.
     */
    @Override
    public synchronized User signUp(User user) throws ServerErrorException, UserAlreadyExistsException {
        Connection conn = null;
        PreparedStatement psPartner = null;
        PreparedStatement psUser = null;
        ResultSet rs = null;

        try {
            // Get a connection (Use a connection pool for scalability)
            conn = DBConnectionPool.getConnection();
            if(conn == null){
               throw new ServerErrorException("Erver not working");
            }
            conn.setAutoCommit(false); // Start transaction

            // Insert into res_partner
            String sqlPartner = "INSERT INTO res_partner (name, email, display_name, is_company, company_id, street, city, zip, active) "
                            + "VALUES (?, ?, ?, FALSE, 1, ?, ?, ?, ?) RETURNING id";
            psPartner = conn.prepareStatement(sqlPartner);
            psPartner.setString(1, user.getName());
            psPartner.setString(2, user.getEmail());
            psPartner.setString(3, user.getName()); // Assuming display_name is similar to the name field
            psPartner.setString(4, user.getStreet());
            psPartner.setString(5, user.getCity());
            psPartner.setInt(6, user.getZip());
            psPartner.setBoolean(7, user.isActivo());
            rs = psPartner.executeQuery();
            int partnerId = 0;
            if (rs.next()) {
                partnerId = rs.getInt("id");  // Get generated partner_id
            } else {
                throw new UserAlreadyExistsException("Email already exist.");
            }

            // Insert into res_users
            String sqlUser = "INSERT INTO res_users (login, password, partner_id, company_id, notification_type) "
                            + "VALUES (?, ?, ?, ?, 'email')";
            psUser = conn.prepareStatement(sqlUser);
            psUser.setString(1, user.getEmail());
            psUser.setString(2, user.getPassword());
            psUser.setInt(3, partnerId);  // Use the partner ID from res_partner
            psUser.setInt(4, user.getCompanyID());
            psUser.executeUpdate();

            // Commit the transaction
            conn.commit();
            return user;

        } catch (SQLException e) {
            // Rollback in case of error
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            return null;
        } finally {
            // Close all resources
            try {
                if (rs != null) {
                    rs.close();
                }
                if (psPartner != null) {
                    psPartner.close();
                }
                if (psUser != null) {
                    psUser.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

}