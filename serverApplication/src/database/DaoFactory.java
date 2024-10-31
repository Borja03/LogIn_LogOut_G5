
package database;

import ISignable.Signable;

/**
 * The {@code DaoFactory} class provides a static method to obtain instances 
 * of {@code Signable} data access objects. This class implements the Singleton 
 * design pattern to ensure that only one instance of {@code UserDao} is 
 * created throughout the application.
 *
 * This class is thread-safe and can be accessed concurrently without 
 * creating multiple instances of the data access object.
 * 
 * 
 * @author Omar
 */
public class DaoFactory {

    private static Signable signable = null;

    /**
     * Returns a singleton instance of a {@code Signable} implementation. 
     * If an instance does not already exist, it creates a new 
     * {@code UserDao} instance.
     *
     * @return a {@code Signable} instance representing the data access object.
     */
    public synchronized static Signable getSignable() {
        if (signable == null) 
            signable = new UserDao();
        return signable;
    }
}
