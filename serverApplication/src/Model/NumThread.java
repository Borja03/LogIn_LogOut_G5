/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Alder
 */
public class NumThread {
     private Integer threadCount = 0;

    public synchronized void increment(){
            threadCount++;
                    }
    
    public synchronized void decrement(){
            threadCount--;
                    }
    
    public synchronized int value(){
            return threadCount;
                    }
    
    
}
