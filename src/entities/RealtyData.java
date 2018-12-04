/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import dynamicHashingCore.IRecord;
import constants.CommonConstants;
import dynamicHashingCore.Converter;
/**
 *
 * @author Bugy
 */
public class RealtyData implements IRecord<RealtyData> {

    private int id;
    private int idInCadaster;
    private String cadasterName;
    private String desc;

    public RealtyData(int id, int idInCadaster, String cadasterName, String desc) {
        this.id = id;
        this.idInCadaster = idInCadaster;
        this.cadasterName = cadasterName;
        this.desc = desc;
    }

    /**
     * Sets default values
     */
    public RealtyData() {
        this.id = -1;
        this.idInCadaster = -1;
        this.cadasterName = "cdasterName";
        this.desc = "description";
    }
    
    /**
     * For find by id
     */
    public RealtyData(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getIdInCadaster() {
        return idInCadaster;
    }

    public String getCadasterName() {
        return cadasterName;
    }

    public String getDesc() {
        return desc;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdInCadaster(int idInCadaster) {
        this.idInCadaster = idInCadaster;
    }

    public void setCadasterName(String name) {
        this.cadasterName = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public boolean equalsData(RealtyData data) {
        return this.id == data.id;
    }

    @Override
    public byte[] toByteArray() {
        Converter.ConverterToByteArray converterToByteArray = new Converter.ConverterToByteArray();

        converterToByteArray.writeInt(this.id);
        converterToByteArray.writeInt(this.idInCadaster);

        if (this.cadasterName.length() < CommonConstants.REALTY_CADASTER_NAME_MAX_LENGTH) {
            int invalidCharactersCount = CommonConstants.REALTY_CADASTER_NAME_MAX_LENGTH - this.cadasterName.length();
            String invalidCharactersSubstring = new StringBuilder(CommonConstants.INVALID_CHARACTERS).substring(0, invalidCharactersCount);
            this.cadasterName += invalidCharactersSubstring;
        }
        converterToByteArray.writeString(this.cadasterName);

        if (this.desc.length() < CommonConstants.REALTY_DESC_MAX_LENGTH) {
            int invalidCharactersCount = CommonConstants.REALTY_DESC_MAX_LENGTH - this.desc.length();
            String invalidCharactersSubstring = new StringBuilder(CommonConstants.INVALID_CHARACTERS).substring(0, invalidCharactersCount);
            this.desc += invalidCharactersSubstring;
        }
        converterToByteArray.writeString(this.desc);

        return converterToByteArray.toByteArray();
    }

    @Override
    public IRecord fromByteArray(byte[] byteArray) {
        Converter.ConverterFromByteArray converterFromByteArray = new Converter.ConverterFromByteArray(byteArray);

        int id = converterFromByteArray.readInt();
        int idInCadaster = converterFromByteArray.readInt();

        String cadasterName = new String();
        for (int i = 0; i < CommonConstants.REALTY_CADASTER_NAME_MAX_LENGTH; i++) {
            char charFromArr = converterFromByteArray.readChar();
            if (charFromArr != '$') {
                cadasterName += Character.toString(charFromArr);
            }
        }

        String desc = new String();
        for (int i = 0; i < CommonConstants.REALTY_DESC_MAX_LENGTH; i++) {
            char charFromArr = converterFromByteArray.readChar();
            if (charFromArr != '$') {
                desc += Character.toString(charFromArr);
            }
        }

        return new RealtyData(id, idInCadaster, cadasterName, desc);
    }

    @Override
    public int getSize() {
        return constants.CommonConstants.SIZE_IN_BYTE_REALTY_DATA;
    }

    @Override
    public String toString() {
        return "Realty{" + "id=" + id + ", idInCadaster=" + idInCadaster + ", cadsterName=" + cadasterName + ", desc=" + desc + '}';
    }

    @Override
    public String getHashKey() {
        return "" + this.id;
    }
}
