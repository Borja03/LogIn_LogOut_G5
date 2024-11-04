package database;

import ISignable.Signable;

/**
 * La clase <code>DaoFactory</code> es una fábrica para crear instancias
 * de objetos de acceso a datos (DAO). Esta clase utiliza el patrón
 * Singleton para asegurar que solo haya una única instancia de cada tipo
 * de DAO, optimizando así la gestión de recursos.
 *
 * <p>En particular, esta implementación permite obtener una instancia de
 * <code>Signable</code>, que se utiliza para realizar operaciones de
 * acceso a datos relacionadas con usuarios.</p>
 *
 * <p>Se garantiza que las instancias de los DAOs sean creadas de forma
 * segura en un entorno multihilo mediante sincronización.</p>
 *
 * @author Alder, Omar
 */
public class DaoFactory {

    private static Signable signable = null;

    /**
     * Obtiene una instancia del objeto <code>Signable</code>. Si la instancia
     * aún no ha sido creada, se crea una nueva instancia de <code>UserDao</code>.
     *
     * @return Una instancia de <code>Signable</code>.
     */
    public synchronized static Signable getSignable() {
        if (signable == null) 
            signable = new UserDao();
        return signable;
    }
}
