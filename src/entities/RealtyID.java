/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import constants.CommonConstants;
import static constants.CommonConstants.*;
import dynamicHashingCore.Converter;
import dynamicHashingCore.IRecord;

/**
 *
 * @author Bugy
 */
public class RealtyID implements IRecord<RealtyID> {

    private int id;
    private int address;
    
    public RealtyID(int id, int address) {
        this.id = id;
        this.address = address;
    }
    
    /**
     * Sets default values
     */
    public RealtyID() {
        this.id = -1;
        this.address = -1;
    }
    
    /**
     * For find by id
     */
    public RealtyID(int id) {
        this.id = id;
    }

    @Override
    public boolean equalsData(RealtyID data) {
        return this.id == data.id;
    }

    @Override
    public byte[] toByteArray() {
        Converter.ConverterToByteArray converterToByteArray = new Converter.ConverterToByteArray();

        converterToByteArray.writeInt(this.id);
        converterToByteArray.writeInt(this.address);

        return converterToByteArray.toByteArray();
    }

    @Override
    public IRecord fromByteArray(byte[] byteArray) {
        Converter.ConverterFromByteArray converterFromByteArray = new Converter.ConverterFromByteArray(byteArray);

        int id = converterFromByteArray.readInt();
        int address = converterFromByteArray.readInt();

        return new RealtyID(id, address);
    }

    @Override
    public int getSize() {
        return SIZE_IN_BYTE_REALTY_ID;
    }

    @Override
    public String getHashKey() {
        return "" + this.id;
    }

    public int getAddress() {
        return address;
    }

    public int getId() {
        return id;
    }
    
    
}
