/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import data.Realty;
import nodes.InternalNode;

/**
 *
 * @author Bugy
 */
public class US_sem02 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //InternalNode internalNode = new InternalNode(null, 0);
        Realty realty = new Realty(6, 9, "janko", "marek");
        byte[] byteArr =  realty.toByteArray();
        Realty realtyFromArr = (Realty) realty.fromByteArray(byteArr);
        System.out.println(realtyFromArr.toString());
    }
    
}
