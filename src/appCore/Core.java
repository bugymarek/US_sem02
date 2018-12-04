/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appCore;

import dynamicHashingCore.DynamicHashing;
import static dynamicHashingCore.DynamicHashing.PATH;
import dynamicHashingCore.Record;
import entities.RealtyData;
import entities.RealtyID;
import entities.RealtyInCadaster;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bugy
 */
public class Core {
    
    private final String PATH = "C:/Users/Bugy/Documents/NetBeansProjects/US_sem02/src/storage/";
    private final String UNSORTED_FILE_TYPE = ".hash";
    private final String NAME_FILE = "Neutriedeny";
    private final int FACTOR = 5;
    private final RealtyData TEMPLATE_REALTY_DATA = new RealtyData();
    private final Block TEMPLATE_BLOCK = new Block(-1, FACTOR, TEMPLATE_REALTY_DATA);
    
    
    private final RealtyID TEPLATE_REALTY_ID = new RealtyID();
    private final RealtyInCadaster TEPLATE_REALTY_IN_CADASTER = new RealtyInCadaster();
    
    private RandomAccessFile unsrtedFile;
    private DynamicHashing<RealtyID> dynamicHashingRealty;
    private DynamicHashing<RealtyInCadaster> dynamicHashingRealtyInCadaster;

    public Core(String fileMainName, String fileAdditionName,  int mainFactor, int additionFactor, int maxHashSize ) {
        this.dynamicHashingRealty = new DynamicHashing(fileMainName, mainFactor, additionFactor, this.TEPLATE_REALTY_ID, maxHashSize);
        this.dynamicHashingRealty.createNewFiles(fileMainName);
        
        this.dynamicHashingRealtyInCadaster = new DynamicHashing(fileAdditionName, mainFactor, additionFactor, this.TEPLATE_REALTY_IN_CADASTER, maxHashSize);
        this.dynamicHashingRealtyInCadaster.createNewFiles(fileAdditionName);
        
        try {
            unsrtedFile = new RandomAccessFile(PATH + NAME_FILE + UNSORTED_FILE_TYPE, "rw");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Core(String fileMainName, String fileAdditionName) {
        this.dynamicHashingRealty = DynamicHashing.loadTrie(fileMainName, TEPLATE_REALTY_ID);
        this.dynamicHashingRealtyInCadaster = DynamicHashing.loadTrie(fileAdditionName, TEPLATE_REALTY_IN_CADASTER);
        
        try {
            unsrtedFile = new RandomAccessFile(PATH + NAME_FILE + UNSORTED_FILE_TYPE, "rw");
            try {
                unsrtedFile.setLength(0);
            } catch (IOException ex) {
                Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DynamicHashing<RealtyID> getDynamicHashingRealty() {
        return dynamicHashingRealty;
    }

    public DynamicHashing<RealtyInCadaster> getDynamicHashingRealtyInCadaster() {
        return dynamicHashingRealtyInCadaster;
    }
    
    public boolean saveToConfigFiles(){
       boolean result1 = this.dynamicHashingRealty.saveTrieToFile();
       boolean result2 = this.dynamicHashingRealtyInCadaster.saveTrieToFile();
       return result1 && result2;
    }
    
    private boolean writeToFile(int offset, byte[] byteArr, RandomAccessFile file) {
        try {
            file.seek(offset);
        } catch (IOException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        try {
            file.write(byteArr);
        } catch (IOException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }
    
    private byte[] readFromFile(int offset, RandomAccessFile file, int size) {

        byte[] byteArr = new byte[size];
        try {
            file.seek(offset);
        } catch (IOException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        try {
            file.read(byteArr, 0, size);
        } catch (IOException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return byteArr;
    }
}
