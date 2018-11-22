/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import dynamicHashingCore.IRecord;
import constants.CommonConstants;
import dynamicHashingCore.Converter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.BitSet;

/**
 *
 * @author Bugy
 */
public class Realty implements IRecord<Realty> {

    private int id;
    private int idInCadaster;
    private String cadsterName;
    private String desc;

    public Realty(int id, int idInCadaster, String cadsterName, String desc) {
        this.id = id;
        this.idInCadaster = idInCadaster;
        this.cadsterName = cadsterName;
        this.desc = desc;
    }

    /**
     * Sets default values
     */
    public Realty() {
        this.id = -1;
        this.idInCadaster = -1;
        this.cadsterName = "cdasterName";
        this.desc = "description";
    }

    public int getId() {
        return id;
    }

    public int getIdInCadaster() {
        return idInCadaster;
    }

    public String getCadsterName() {
        return cadsterName;
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

    public void setCadsterName(String name) {
        this.cadsterName = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public boolean equalsData(Realty data) {
        return this.id == data.id;
    }

    @Override
    public byte[] toByteArray() {
        Converter.ConverterToByteArray converterToByteArray = new Converter.ConverterToByteArray();

        converterToByteArray.writeInt(this.id);
        converterToByteArray.writeInt(this.idInCadaster);

        if (this.cadsterName.length() < CommonConstants.REALTY_CADASTER_NAME_MAX_LENGTH) {
            int invalidCharactersCount = CommonConstants.REALTY_CADASTER_NAME_MAX_LENGTH - this.cadsterName.length();
            String invalidCharactersSubstring = new StringBuilder(CommonConstants.INVALID_CHARACTERS).substring(0, invalidCharactersCount);
            this.cadsterName += invalidCharactersSubstring;
        }
        converterToByteArray.writeString(this.cadsterName);

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

        String cadsterName = new String();
        for (int i = 0; i < CommonConstants.REALTY_CADASTER_NAME_MAX_LENGTH; i++) {
            char charFromArr = converterFromByteArray.readChar();
            if (charFromArr != '$') {
                cadsterName += Character.toString(charFromArr);
            }
        }

        String desc = new String();
        for (int i = 0; i < CommonConstants.REALTY_DESC_MAX_LENGTH; i++) {
            char charFromArr = converterFromByteArray.readChar();
            if (charFromArr != '$') {
                desc += Character.toString(charFromArr);
            }
        }

        return new Realty(id, idInCadaster, cadsterName, desc);
    }

    @Override
    public int getSize() {
        return constants.CommonConstants.SIZE_IN_BYTE_REALTY;
    }

    @Override
    public String toString() {
        return "Realty{" + "id=" + id + ", idInCadaster=" + idInCadaster + ", cadsterName=" + cadsterName + ", desc=" + desc + '}';
    }

    @Override
    public String getHashKey() {
        return "" + this.id;
    }

}
