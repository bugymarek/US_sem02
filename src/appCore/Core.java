/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appCore;

import dynamicHashingCore.DynamicHashing;
import dynamicHashingCore.Record;
import entities.Realty;
import entities.RealtyInCadaster;

/**
 *
 * @author Bugy
 */
public class Core {
    
    private final Realty TEPLATE_REALTY = new Realty();
    private final RealtyInCadaster TEPLATE_REALTY_IN_CADASTER = new RealtyInCadaster(TEPLATE_REALTY);
    private DynamicHashing<Realty> dynamicHashingRealty;
    private DynamicHashing<RealtyInCadaster> dynamicHashingRealtyInCadaster;

    public Core(String fileMainName, String fileAdditionName,  int mainFactor, int additionFactor, int maxHashSize ) {
        this.dynamicHashingRealty = new DynamicHashing(fileMainName, mainFactor, additionFactor, this.TEPLATE_REALTY, maxHashSize);
        this.dynamicHashingRealty.createNewFiles(fileMainName);
        
        this.dynamicHashingRealtyInCadaster = new DynamicHashing(fileAdditionName, mainFactor, additionFactor, this.TEPLATE_REALTY_IN_CADASTER, maxHashSize);
        this.dynamicHashingRealtyInCadaster.createNewFiles(fileAdditionName);
    }
    
    public Core(String fileMainName, String fileAdditionName) {
        this.dynamicHashingRealty = DynamicHashing.loadTrie(fileMainName, TEPLATE_REALTY);
        this.dynamicHashingRealtyInCadaster = DynamicHashing.loadTrie(fileAdditionName, TEPLATE_REALTY_IN_CADASTER);
    }

    public DynamicHashing<Realty> getDynamicHashingRealty() {
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
}
