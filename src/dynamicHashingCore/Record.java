/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamicHashingCore;

import constants.CommonConstants;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.BitSet;
/**
 *
 * @author Bugy
 */
public class Record<T> {
    private boolean isValid;
    private IRecord data;

    public Record(boolean isValid, IRecord data) {
        this.isValid = isValid;
        this.data = data;
    }
    
    public Record copy(){
        return new Record(isValid, data);
    }
    
    public IRecord getData() {
        return data;
    }

    public boolean isIsValid() {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean equals(Record<T> record) {
       return data.equalsData(record.data);
    }
    
    public byte[] toByteArray(){
        Converter.ConverterToByteArray converterToByteArray = new Converter.ConverterToByteArray();

        converterToByteArray.writeBoolean(this.isValid);
        converterToByteArray.writeByteArray(this.data.toByteArray());

        return converterToByteArray.toByteArray();
    }
    
    public Record fromByteArray(byte[] byteArray){
        Converter.ConverterFromByteArray converterFromByteArray = new Converter.ConverterFromByteArray(byteArray);
        
        boolean isValid = converterFromByteArray.readBoolean();
        IRecord data = this.data.fromByteArray(converterFromByteArray.readByteArray(this.data.getSize()));

        return new Record(isValid, data);
    }
    
    public int getSize(){
        return this.data.getSize() + constants.CommonConstants.SIZE_IN_BYTE_IS_VALID_RECORD;
    }

    @Override
    public String toString() {
        return "Record{" + "isValid=" + isValid + ", data=" + data.toString() + '}';
    }
    
    public String getHashKey(){
        return this.data.getHashKey();
    }
    
}
