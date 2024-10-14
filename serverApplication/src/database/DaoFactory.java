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

    public static Signable getSignable() {
        Signable signable = null;

        signable = (Signable) new UserDao();
        return signable;
    }
}
