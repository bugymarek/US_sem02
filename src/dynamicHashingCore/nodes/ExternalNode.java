/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamicHashingCore.nodes;

/**
 *
 * @author Bugy
 */
public class ExternalNode extends Node{
    private int validRecordsCount;
    private int addressBlock;
    
    public ExternalNode(Node father, int depth) {
        super(father, depth);
        this.validRecordsCount = 0;
        this.addressBlock = -1;
    }

    public int getValidRecordsCount() {
        return validRecordsCount;
    }

    public int getAddressBlock() {
        return addressBlock;
    }

    public void setValidRecordsCount(int validRecordsCount) {
        this.validRecordsCount = validRecordsCount;
    }

    public void setAddressBlock(int addressBlock) {
        this.addressBlock = addressBlock;
    }
    
    public void incrementValidRecordsCount(){
        validRecordsCount++;
    }
    
    public void decrementValidRecordsCount(){
        validRecordsCount--;
    }
}
