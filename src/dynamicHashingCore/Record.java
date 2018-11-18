/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamicHashingCore;

import data.IRecord;
/**
 *
 * @author Bugy
 */
public class Record{
    private boolean isValid;
    private IRecord  data;

    public Record(boolean isValid, IRecord data) {
        this.isValid = isValid;
        this.data = data;
    }

    public boolean equals(IRecord parData) {
       return data.equalsData(data);
    }
    
    public byte[] toByteArray(){
        return new byte[5];
    }
    
    public Record fromByteArray(byte[] byteArray){
        return null;
    }
    
    public int getSize(){
        return this.data.getSize() + constants.CommonConstants.SIZE_IN_BYTE_IS_VALID_RECORD;
    }
}
