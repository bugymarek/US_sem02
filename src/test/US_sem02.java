/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import entities.Realty;
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
        // TODO code application logic here
        //InternalNode internalNode = new InternalNode(null, 0);
//        Realty realty = new Realty(1000000000, 1000000000, "janko", "marek");
//        byte[] byteArr =  realty.toByteArray();
//        System.out.println("velkost pola: " + byteArr.length);
//        realty = (Realty) realty.fromByteArray(byteArr);
//        System.out.println(realty.toString());
//        
//        Record record = new Record(false, realty);
//        byte[] byteRecord = record.toByteArray();
//        record = record.fromByteArray(byteRecord);
//        System.out.println(record.toString());
//        
//        
//        Block block = new Block(0, 5, new Realty(1000000000, 1000000000, "janko", "marek"));
//        block.addRecord(new Record(true, new Realty(1000000002, 1000000002, "janko2", "marek2")));
//        block.addRecord(new Record(true, new Realty(1000000003, 1000000003, "janko3", "marek3")));
//        block.addRecord(new Record(true, new Realty(1000000004, 1000000004, "janko4", "marek4")));
//        block.addRecord(new Record(true, new Realty(1000000005, 1000000005, "janko5", "marek5")));
//        byte[]byteBlock = block.toByteArray();
//        Block newBlock = block.fromByteArray(byteBlock);
//        System.out.println(newBlock);
        //block.sortRecordsFirstValid();
        
        int factor =3;
        //DynamicHashing<Realty> dynamicHashing = new DynamicHashing("subor", 2, new Record(true, new RealtyInCadaster(new Realty(525, 1000000005, "janko5", "marek5"))), 5);
        DynamicHashing<Realty> dynamicHashing = new DynamicHashing("subor", factor, new Record(true, new Realty()), 6);
        dynamicHashing.createNewFiles("subor");
        
//        dynamicHashing.add(new Realty(525567, 1516541, "janko0", "marek0"));
//        dynamicHashing.add(new Realty(2, 1516541, "janko2", "marek2"));
//        dynamicHashing.add(new Realty(3, 1516541, "janko2", "marek2"));
//        dynamicHashing.add(new Realty(4, 1516541, "janko2", "marek2"));
//        dynamicHashing.add(new Realty(5, 1516541, "janko2", "marek2"));
//        dynamicHashing.add(new Realty(6, 1516541, "janko2", "marek2"));
//        dynamicHashing.add(new Realty(7, 1516541, "janko2", "marek2"));
//        dynamicHashing.add(new Realty(8, 1516541, "janko2", "marek2"));
//        dynamicHashing.add(new Realty(9, 1516541, "janko2", "marek2"));
//        System.out.println(dynamicHashing.getFreeAddressesString());
//        dynamicHashing.readAllBlocksFromFile("subor");
        for (int i = 0; i < 1000; i++) {
            dynamicHashing.add(new Realty(i, 1516541, "janko" + i, "marek" + i));
        }
        System.out.println(dynamicHashing.getFreeAddressesString());
        dynamicHashing.readAllBlocksFromFile("subor");
        //dynamicHashing.add(new RealtyInCadaster(new Realty(525, 1000000005, "janko5", "marek5")));
        //dynamicHashing.createNewFiles("subor");
    }
    
}
