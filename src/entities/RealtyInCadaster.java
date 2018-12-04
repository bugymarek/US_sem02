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
public class RealtyInCadaster implements IRecord<RealtyInCadaster>{
    private int registerNumer;
    private int address;
    private String cadasterName;

    public RealtyInCadaster(int registerNumer, int address, String cadasterName) {
        this.registerNumer = registerNumer;
        this.address = address;
        this.cadasterName = cadasterName;
    }

    /**
     * Sets default values
     */
    public RealtyInCadaster() {
        this.registerNumer = -1;
        this.address = -1;
        this.cadasterName = "cadasterName";
    }
    
    /**
     * For find by id
     */
    public RealtyInCadaster(int registerNumer) {
        this.registerNumer = registerNumer;
    }

    @Override
    public boolean equalsData(RealtyInCadaster data) {
        return this.registerNumer == data.registerNumer;
    }

    @Override
    public byte[] toByteArray() {
        Converter.ConverterToByteArray converterToByteArray = new Converter.ConverterToByteArray();

        converterToByteArray.writeInt(this.registerNumer);
        converterToByteArray.writeInt(this.address);
        
        if (this.cadasterName.length() < CommonConstants.REALTY_CADASTER_NAME_MAX_LENGTH) {
            int invalidCharactersCount = CommonConstants.REALTY_CADASTER_NAME_MAX_LENGTH - this.cadasterName.length();
            String invalidCharactersSubstring = new StringBuilder(CommonConstants.INVALID_CHARACTERS).substring(0, invalidCharactersCount);
            this.cadasterName += invalidCharactersSubstring;
        }
        converterToByteArray.writeString(this.cadasterName);
        
        System.out.println(converterToByteArray.toByteArray().length);

        return converterToByteArray.toByteArray();
    }

    @Override
    public IRecord fromByteArray(byte[] byteArray) {
        Converter.ConverterFromByteArray converterFromByteArray = new Converter.ConverterFromByteArray(byteArray);

        int id = converterFromByteArray.readInt();
        int address = converterFromByteArray.readInt();
        
        String cadasterName = new String();
        for (int i = 0; i < CommonConstants.REALTY_CADASTER_NAME_MAX_LENGTH; i++) {
            char charFromArr = converterFromByteArray.readChar();
            if (charFromArr != '$') {
                cadasterName += Character.toString(charFromArr);
            }
        }

        return new RealtyInCadaster(id, address, cadasterName);
    }

    @Override
    public int getSize() {
        return SIZE_IN_BYTE_REALTY_IN_CADASTER;
    }

    @Override
    public String getHashKey() {
        return "" + this.registerNumer + this.cadasterName;
    }
}
