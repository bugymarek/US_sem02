/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamicHashingCore;

import com.sun.java.swing.plaf.windows.WindowsTreeUI;
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

    public Block(int address, int factor, IRecord record) {
        this.address = address;
        this.factor = factor;
        initRecords(record);
    }

    public byte[] toByteArray() {
        return new byte[5];
    }

    public Record fromByteArray(byte[] byteArray) {
        return null;
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
            if (record.isIsValid()) {
                count++;
            }
        }
        return count;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void setValidRecord(int index) {
        this.recordsList.get(index).setIsValid(true);
    }
}
