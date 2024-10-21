/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import ISignable.Signable;

/**
 *
 * @author Omar
 */
public class DaoFactory {


    private static Signable signable = null;

    public synchronized static Signable getSignable() {
        if (signable == null) 
            signable = new UserDao();
        return signable;
    }
}
