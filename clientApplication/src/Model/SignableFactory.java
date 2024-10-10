/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import ISignable.Signable;

/**
 *
 * @author Alder
 */
public class SignableFactory {
    public Signable getSignable(){
        return new SignerClient();
    }
}
