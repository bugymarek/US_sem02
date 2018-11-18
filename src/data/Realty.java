/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import constants.CommonConstants;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
        ByteArrayOutputStream hlpByteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream hlpOutStream = new DataOutputStream(hlpByteArrayOutputStream);

        try {
            hlpOutStream.writeInt(this.id);
            hlpOutStream.writeInt(this.idInCadaster);

            if (this.cadsterName.length() < CommonConstants.REALTY_CADASTER_NAME_MAX_LENGTH) {
                int invalidCharactersCount = CommonConstants.REALTY_CADASTER_NAME_MAX_LENGTH - this.cadsterName.length();
                String invalidCharactersSubstring = new StringBuilder(CommonConstants.INVALID_CHARACTERS).substring(0, invalidCharactersCount);
                this.cadsterName += invalidCharactersSubstring;
            }
            hlpOutStream.writeChars(this.cadsterName);

            if (this.desc.length() < CommonConstants.REALTY_DESC_MAX_LENGTH) {
                int invalidCharactersCount = CommonConstants.REALTY_DESC_MAX_LENGTH - this.desc.length();
                String invalidCharactersSubstring = new StringBuilder(CommonConstants.INVALID_CHARACTERS).substring(0, invalidCharactersCount);
                this.desc += invalidCharactersSubstring;
            }
            hlpOutStream.writeChars(this.desc);

            return hlpByteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            throw new IllegalStateException("Error during conversion to byte array.");
        }
    }

    @Override
    public IRecord fromByteArray(byte[] byteArray) {
        ByteArrayInputStream hlpByteArrayInputStream = new ByteArrayInputStream(byteArray);
        DataInputStream hlpInStream = new DataInputStream(hlpByteArrayInputStream);
        try {
            this.id = hlpInStream.readInt();
            this.idInCadaster = hlpInStream.readInt();

            this.cadsterName = new String();
            for (int i = 0; i < CommonConstants.REALTY_CADASTER_NAME_MAX_LENGTH; i++) {
                char charFromArr = hlpInStream.readChar();
                if (charFromArr != '$') {
                    this.cadsterName += Character.toString(charFromArr);
                }
            }

            this.desc = new String();
            for (int i = 0; i < CommonConstants.REALTY_DESC_MAX_LENGTH; i++) {
                char charFromArr = hlpInStream.readChar();
                if (charFromArr != '$') {
                    this.desc += Character.toString(charFromArr);
                }
            }

            return new Realty(id, idInCadaster, cadsterName, desc);
        } catch (IOException e) {
            throw new IllegalStateException("Error during conversion from byte array.");
        }
    }

    @Override
    public int getSize() {
        return constants.CommonConstants.SIZE_IN_BYTE_REALTY;
    }

    @Override
    public String toString() {
        return "Realty{" + "id=" + id + ", idInCadaster=" + idInCadaster + ", cadsterName=" + cadsterName + ", desc=" + desc + '}';
    }

}
