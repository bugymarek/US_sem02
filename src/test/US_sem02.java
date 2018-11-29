/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import test.Realty;
import dynamicHashingCore.Record;
import dynamicHashingCore.nodes.InternalNode;
import constants.CommonConstants;
import entities.RealtyInCadaster;
import dynamicHashingCore.Block;
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
        DynamicHashing<Realty> dynamicHashing = new DynamicHashing("subor", 5,20, new Record(true, new Realty()), 20);
        dynamicHashing.createNewFiles("subor");
        
        Test test = new Test(dynamicHashing);
        test.checkInsert();
        dynamicHashing.saveTrieToFile();
    }
}
