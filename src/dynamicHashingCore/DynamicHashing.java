/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamicHashingCore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.BitSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import nodes.ExternalNode;
import nodes.InternalNode;
import nodes.Node;

/**
 *
 * @author Bugy
 */
public class DynamicHashing<T> {

    private Record<T> templateRecord;
    private int factor;
    private RandomAccessFile mainFile;
    private RandomAccessFile additionFile;
    private final String FILE_TYPE = ".hash";
    private int nextFreeAddress;
    private Node root;
    private int maxHashSize;
    private final String path = "C:/Users/Bugy/Documents/NetBeansProjects/US_sem02/src/storage/";
    private final Block TEMPLATE_BLOCK;

    public DynamicHashing(String fileNeme, int factor, Record<T> templateRecord, int maxHashSize) {
        try {
            mainFile = new RandomAccessFile(path + fileNeme + FILE_TYPE, "rw");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            additionFile = new RandomAccessFile(path + fileNeme + "Preplnovaci" + FILE_TYPE, "rw");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.templateRecord = templateRecord;
        this.factor = factor;
        this.nextFreeAddress = -1;
        this.maxHashSize = maxHashSize;
        this.TEMPLATE_BLOCK = new Block(-1, factor, templateRecord.getData());
        //Converter.getHashFromKey(templateRecord.getHashKey(), maxHashSize);
        root = new ExternalNode(null, 0);
    }

    public void createNewFiles(String fileNeme) {
        try {
            mainFile = new RandomAccessFile(path + fileNeme + FILE_TYPE, "rw");
            try {
                mainFile.setLength(0);
            } catch (IOException ex) {
                Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            additionFile = new RandomAccessFile(new File(path + fileNeme + "Preplnovaci" + FILE_TYPE), "rw");
            try {
                additionFile.setLength(0);
            } catch (IOException ex) {
                Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean add(IRecord<T> record) {
        Record newRecord = new Record(true, record);
        BitSet hash = Converter.getHashFromKey(record.getHashKey(), maxHashSize);
        Node currentNode = root;
        while (currentNode instanceof InternalNode) {
            
        }

        if (((ExternalNode) currentNode).getAddressBlock() == -1) { //ak nieje alokovaný blok
            Block block = new Block(getNextFreeAddress(), factor, templateRecord.getData());
            boolean resultMemory = block.addRecord(newRecord);
            boolean resultFile = writeToFile(block.getAddress() * TEMPLATE_BLOCK.getSize(), block.toByteArray(), mainFile);
            if (resultMemory && resultFile) {
                ((ExternalNode) currentNode).setAddressBlock(block.getAddress());
                ((ExternalNode) currentNode).incrementValidRecordsCount();
                return true;
            } else {
                System.out.println("nepodarilo sa vlozit záznam: " + record);
                return false;
            }
        } else if (((ExternalNode) currentNode).getValidRecordsCount() < factor) { // ak je v bloku miesto miesto
            Block block = TEMPLATE_BLOCK.fromByteArray(readFromFile(((ExternalNode) currentNode).getAddressBlock() * TEMPLATE_BLOCK.getSize(), mainFile));
            if (block.findRecord(newRecord) != null) {
                System.out.println("uz sa tam nachadza záznam s vkladaným ID: " + record.getHashKey());
                return false;// uz sa tam nachadza záznam s vkladaným ID
            } else {
                boolean resultMemory = block.addRecord(newRecord);
                boolean resultFile = writeToFile(block.getAddress() * TEMPLATE_BLOCK.getSize(), block.toByteArray(), mainFile);
                if (resultMemory && resultFile) {
                    ((ExternalNode) currentNode).incrementValidRecordsCount();
                    System.out.println(block.toString());
                    return true;
                } else {
                    System.out.println("nepodarilo sa vlozit záznam: " + record);
                    return false;
                }
            }
        } else { // ak nieje v bloku miesto
            System.out.println("Blok je plný");
        }
        return true;
    }

    private boolean writeToFile(int offset, byte[] byteArr, RandomAccessFile file) {
        try {
            file.seek(offset);
        } catch (IOException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        try {
            file.write(byteArr);
        } catch (IOException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }

    private byte[] readFromFile(int offset, RandomAccessFile file) {

        byte[] byteArr = new byte[TEMPLATE_BLOCK.getSize()];
        try {
            file.seek(offset);
        } catch (IOException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        try {
            file.read(byteArr, 0, TEMPLATE_BLOCK.getSize());
        } catch (IOException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return byteArr;
    }

    private int getNextFreeAddress() {
        this.nextFreeAddress++;
        return this.nextFreeAddress;
    }

}
