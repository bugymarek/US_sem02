/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;


/**
 *
 * @author Bugy
 */
public interface IRecord<T> {
    
    boolean equalsData(T data);
    
    byte[] toByteArray();
    
    IRecord fromByteArray(byte[] byteArray);
    
    int getSize();
}
