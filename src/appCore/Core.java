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
    private DynamicHashing<Realty> dynamicHashingRealty;
    private DynamicHashing<RealtyInCadaster> dynamicHashingRealtyInCadaster;

    public Core(String fileMainNeme, String fileAdditionNeme,  int mainFactor, int additionFactor, int maxHashSize ) {
        this.dynamicHashingRealty = new DynamicHashing(fileMainNeme, mainFactor, additionFactor, this.TEPLATE_REALTY, maxHashSize);
        this.dynamicHashingRealtyInCadaster = new DynamicHashing(fileAdditionNeme, mainFactor, additionFactor, this.TEPLATE_REALTY, maxHashSize);
    }
    
    public Core(String fileMainNeme, String fileAdditionNeme) {
        this.dynamicHashingRealty = DynamicHashing.loadTrie(fileMainNeme, TEPLATE_REALTY);
        this.dynamicHashingRealty = DynamicHashing.loadTrie(fileAdditionNeme, TEPLATE_REALTY);
    }
    
    
}
