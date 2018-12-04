/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import test.Realty;
import entities.RealtyInCadaster;
import dynamicHashingCore.DynamicHashing;

/**
 *
 * @author Bugy
 */
public class US_sem02 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DynamicHashing<Realty> dynamicHashing = new DynamicHashing("subor", 5,10, new Realty(), 15);
        dynamicHashing.createNewFiles("subor");
        
        Test test = new Test(dynamicHashing);
        test.checkInsertSaveAndLoad();            
    }
}
