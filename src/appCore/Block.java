/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appCore;

import dynamicHashingCore.Converter;
import dynamicHashingCore.IRecord;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Bugy
 */
public class Block {

    private int address;
    private List<Record> recordsList;
    private int factor;
    private static final int SIZE_IN_BYTE_ADDRESS_IN_BLOCK = 4;
    private static final int SIZE_IN_BYTE_FACTOR_IN_BLOCK = 4;

    public Block(int address, int factor, IRecord record) {
        this.address = address;
        this.factor = factor;
        initRecords(record);
    }

    private Block(int address, int factor, List<Record> recordsList) {
        this.address = address;
        this.factor = factor;
        this.recordsList = recordsList;
    }

    public byte[] toByteArray() {
        Converter.ConverterToByteArray converterToByteArray = new Converter.ConverterToByteArray();

        converterToByteArray.writeInt(this.address);
        converterToByteArray.writeInt(this.factor);
        for (Record record : recordsList) {
            converterToByteArray.writeByteArray(record.toByteArray());
        }

        return converterToByteArray.toByteArray();
    }

    public Block fromByteArray(byte[] byteArray) {
        Converter.ConverterFromByteArray converterFromByteArray = new Converter.ConverterFromByteArray(byteArray);
        
        int address = converterFromByteArray.readInt();
        int factor = converterFromByteArray.readInt();

        Record record =  recordsList.get(0).copy();
        List<Record> recordsList = new ArrayList<>();
        for (int i = 0; i < factor; i++) {
            Record recordFrom = record.fromByteArray(converterFromByteArray.readByteArray(record.getSize()));
            recordsList.add(recordFrom);
        }

        return new Block(address, factor, recordsList);
    }

    private void initRecords(IRecord record) {
        this.recordsList = new ArrayList<>();
        for (int i = 0; i < factor; i++) {
            recordsList.add(new Record(false, record));
        }
    }

    public void sortRecordsFirstValid() {
        Collections.sort(this.recordsList, new Comparator<Record>() {

            @Override
            public int compare(Record o1, Record o2) {
                if (o1.isIsValid() && o2.isIsValid()) {
                    return 0;
                } else if (o1.isIsValid() && !o2.isIsValid()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
    }

    public int getValidRecordsCount() {
        int count = 0;
        for (Record record : recordsList) {
            if (record.isIsValid()) {
                count++;
            }
        }
        return count;
    }

    public int getInvalidRecordsCount() {
        int count = 0;
        for (Record record : recordsList) {
            if (!record.isIsValid()) {
                count++;
            }
        }
        return count;
    }
    
    public boolean addRecord(Record record){
        if(getInvalidRecordsCount() == 0){
            return false;
        }
        int index = getFirstInvalidIndexRecordSortedByIsValidFirst();
        if(index == -1){
            return false;
        }
        this.recordsList.set(index, record);
        return true;
    }
    
    // mozes urychlit ak najde r.isValid == false potom dalej neprehladava lebo pole je utrieene podla validnych zaznamov
    public Record findRecord(Record record){
        for (Record r : recordsList) {
            if(r.isIsValid() && r.equals(record)){
                return r;
            }
        }
        return null;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public List<Record> getRecordsList() {
        return recordsList;
    }
    
    public void setValidRecord(int index) {
        this.recordsList.get(index).setIsValid(true);
    }
    
    public Record getFirstInvalidRecordSortedByIsValidFirst(){
        Record result = null;
        sortRecordsFirstValid();
        for (Record record : recordsList) {
            if(!record.isIsValid()){
                return record;
            }
        }
        return result;
    }
    
    public int getFirstInvalidIndexRecordSortedByIsValidFirst(){
        int result = -1;
        sortRecordsFirstValid();
        for (int i = 0; i < factor; i++) {
            if(!recordsList.get(i).isIsValid()){
                return i;
            }
        }
        return result;
    }
    
    public int getSize(){
      return (this.recordsList.get(0).getSize() * this.factor) + SIZE_IN_BYTE_ADDRESS_IN_BLOCK + SIZE_IN_BYTE_FACTOR_IN_BLOCK;
    }

    public int getFactor() {
        return factor;
    }

    @Override
    public String toString() {
        String  recordsListString= new String();
        for (Record record : recordsList) {
            recordsListString += record.toString() + "\n";
        }
        
        return "Block{" + "address=" + address + ", factor=" + factor + 
                ", \nrecordsList=\n" + recordsListString + '}';
    }
    
    
}
