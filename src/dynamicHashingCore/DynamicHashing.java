/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamicHashingCore;

import nodes.Node;

/**
 *
 * @author Bugy
 */
public class DynamicHashing<T> {
    private Record<T> genericRecord;
    private int factor;
    private String fileName;
    private int nextFreeAddress;
    private Node root;
    private int maxHashSize;

    public DynamicHashing(String fileNeme, int factor, Record<T> genericRecord, int maxHashSize) {
        this.genericRecord = genericRecord;
        this.factor = factor;
        this.fileName = fileNeme;
        this.nextFreeAddress = 0;
        this.maxHashSize = maxHashSize;
        Converter.getHashFromKey(genericRecord.getHashKey(), maxHashSize);
    }
    
    
}
