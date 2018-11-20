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
/**
 *
 * @author Bugy
 */
public class Record {
    private boolean isValid;
    private IRecord  data;

    public Record(boolean isValid, IRecord data) {
        this.isValid = isValid;
        this.data = data;
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

    public boolean equals(IRecord parData) {
       return data.equalsData(data);
    }
    
    public byte[] toByteArray(){
        Converter.ConverterToByteArray converterToByteArray = new Converter.ConverterToByteArray();

        converterToByteArray.writeBoolean(this.isValid);
        converterToByteArray.writeByteArray(data.toByteArray());

        return converterToByteArray.toByteArray();
    }
    
    public Record fromByteArray(byte[] byteArray){
        Converter.ConverterFromByteArray converterFromByteArray = new Converter.ConverterFromByteArray(byteArray);
        
        this.isValid = converterFromByteArray.readBoolean();
        this.data = this.data.fromByteArray(converterFromByteArray.readByteArray(this.data.getSize()));

        return this;
    }
    
    public int getSize(){
        return this.data.getSize() + constants.CommonConstants.SIZE_IN_BYTE_IS_VALID_RECORD;
    }

    @Override
    public String toString() {
        return "Record{" + "isValid=" + isValid + ", data=" + data.toString() + '}';
    }
    
    
}
