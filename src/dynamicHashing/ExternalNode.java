/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamicHashing;

/**
 *
 * @author Bugy
 */
public class ExternalNode extends Node{
    private int recordsCount;
    private int addressBlock;
    
    public ExternalNode(Node father, int depth) {
        super(father, depth);
        this.recordsCount = 0;
    }

    public int getRecordsCount() {
        return recordsCount;
    }

    public int getAddressBlock() {
        return addressBlock;
    }

    public void setRecordsCount(int recordsCount) {
        this.recordsCount = recordsCount;
    }

    public void setAddressBlock(int addressBlock) {
        this.addressBlock = addressBlock;
    }
    
}
