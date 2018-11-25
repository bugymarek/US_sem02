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
import dynamicHashingCore.nodes.ExternalNode;
import dynamicHashingCore.nodes.InternalNode;
import dynamicHashingCore.nodes.Node;
import java.util.ArrayList;
import java.util.Collections;

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
    private ArrayList<Integer> freeAddresses;
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
        this.freeAddresses = new ArrayList<>();
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
        int depth = 0;
        while (currentNode instanceof InternalNode) {
            if (hash.get(depth)) {
                currentNode = ((InternalNode) currentNode).getRightNode();
            } else {
                currentNode = ((InternalNode) currentNode).getLeftNode();
            }
            depth++;
        }

        while (true) {// osteri ak ma externz prvok max hlbku a v nom uz nieje miesto tak vznik kolizie... tato podmienka to zatial neosetruje
            if (((ExternalNode) currentNode).getAddressBlock() == -1) { //ak nieje alokovaný blok
                Block block = new Block(getNextFreeAddress(), factor, templateRecord.getData());
                boolean resultMemory = block.addRecord(newRecord);
                boolean resultFile = writeToFile(block.getAddress() * TEMPLATE_BLOCK.getSize(), block.toByteArray(), mainFile);
                if (resultMemory && resultFile) {
                    ((ExternalNode) currentNode).setAddressBlock(block.getAddress());
                    ((ExternalNode) currentNode).incrementValidRecordsCount();
                    return true;
                } else {
                    System.out.println("\n nepodarilo sa vlozit záznam: " + record);
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
                        //System.out.println(block.toString());
                        return true;
                    } else {
                        System.out.println("\n nepodarilo sa vlozit záznam: " + record);
                        return false;
                    }
                }
            } else { // ak nieje v bloku miesto
                if (currentNode.getDepth() == maxHashSize) {
                    break; // kolizia. som v najväčšej hlbke. Preto nemôžem už vytvárať dalšie externe vrcholy a musím riešiť kolíziu
                }
                Node newInternalNode = new InternalNode(currentNode.getFather(), currentNode.getDepth());

                Node sonLeft = new ExternalNode(newInternalNode, newInternalNode.getDepth() + 1);
                Block blockSonLeft = new Block(getNextFreeAddress(), factor, templateRecord.getData());
                ((ExternalNode) sonLeft).setAddressBlock(blockSonLeft.getAddress());

                Node sonRight = new ExternalNode(newInternalNode, newInternalNode.getDepth() + 1);
                Block blockSonRight = new Block(getNextFreeAddress(), factor, templateRecord.getData());
                ((ExternalNode) sonRight).setAddressBlock(blockSonRight.getAddress());

                ((InternalNode) newInternalNode).setLeftNode(sonLeft);
                ((InternalNode) newInternalNode).setRightNode(sonRight);

                if (currentNode.getFather() == null) {// tu bude pravdepodobne chyba
                    root = newInternalNode;
                } else if (((InternalNode) currentNode.getFather()).getLeftNode() instanceof InternalNode) { // ak je lavy interny potom nastav praveho potomka
                    ((InternalNode) currentNode.getFather()).setRightNode(newInternalNode);
                } else if (((InternalNode) currentNode.getFather()).getRightNode() instanceof InternalNode) { // ak je pravy interny potom nastav laveho potomka
                    ((InternalNode) currentNode.getFather()).setLeftNode(newInternalNode);
                } else if (((ExternalNode) ((InternalNode) currentNode.getFather()).getLeftNode()).getAddressBlock() == ((ExternalNode) currentNode).getAddressBlock()) { // zsiti koho si synom podla adresy, oba synovia su urcite externe vrcholy
                    ((InternalNode) currentNode.getFather()).setLeftNode(newInternalNode);
                } else {
                    ((InternalNode) currentNode.getFather()).setRightNode(newInternalNode);
                }

                // presuvam zaznamy. Tu potrebujes zistit hlbku aktualneho vrchola, co vlastne predstavuje frefix z hash retazca. To znamena ze vsetky zaznamy v tomto
                // aktualnom vrchole maju rovnaku hodnotu prefixu. Preto potrebuješ pre každy zaznam v bolku zistit dalši bit z has retazca podla kotoreho ich potom 
                // rozdeliš do synov. Bud do prava(1) alebo do lava(0)
                Block block = TEMPLATE_BLOCK.fromByteArray(readFromFile(((ExternalNode) currentNode).getAddressBlock() * TEMPLATE_BLOCK.getSize(), mainFile));
                int prefixSize = currentNode.getDepth() + 1;
                for (Record r : block.getRecordsList()) {
                    if (r.isIsValid()) {
                        BitSet hashRecord = Converter.getHashFromKey(r.getHashKey(), maxHashSize);
                        if (hashRecord.get(prefixSize - 1)) {// idex dalsieho bitu
                            blockSonRight.addRecord(r);
                            ((ExternalNode) sonRight).incrementValidRecordsCount();
                        } else {
                            blockSonLeft.addRecord(r);
                            ((ExternalNode) sonLeft).incrementValidRecordsCount();
                        }
                    }
                }

                if (blockSonLeft.getInvalidRecordsCount() > 0 && blockSonRight.getInvalidRecordsCount() > 0) {
                    if (hash.get(prefixSize - 1)) {// idex dalsieho bitu
                        blockSonRight.addRecord(newRecord);
                        ((ExternalNode) sonRight).incrementValidRecordsCount();
                    } else {
                        blockSonLeft.addRecord(newRecord);
                        ((ExternalNode) sonLeft).incrementValidRecordsCount();
                    }
                    boolean resultFile1 = writeToFile(blockSonLeft.getAddress() * TEMPLATE_BLOCK.getSize(), block.toByteArray(), mainFile);
                    boolean resultFile2 = writeToFile(blockSonRight.getAddress() * TEMPLATE_BLOCK.getSize(), block.toByteArray(), mainFile);
                    if (resultFile1 && resultFile2) {
                        return true;
                    } else {
                        System.out.println("nepodarilo sa vlozit záznam: " + record);
                        return false;
                    }
                }

                if (blockSonLeft.getInvalidRecordsCount() == factor) {
                    freeAddresses.add(blockSonLeft.getAddress());
                    ((ExternalNode) sonLeft).setAddressBlock(-1);
                    blockSonLeft.setAddress(-1);
                    currentNode = sonRight;
                    writeToFile(blockSonRight.getAddress() * TEMPLATE_BLOCK.getSize(), block.toByteArray(), mainFile);
                }

                if (blockSonRight.getInvalidRecordsCount() == factor) {
                    freeAddresses.add(blockSonRight.getAddress());
                    ((ExternalNode) sonRight).setAddressBlock(-1);
                    blockSonRight.setAddress(-1);
                    currentNode = sonLeft;
                    writeToFile(blockSonLeft.getAddress() * TEMPLATE_BLOCK.getSize(), block.toByteArray(), mainFile);
                }
            }
        }
        // tu budem riešiť koliziu. Daj pozor aby sa vykonal tento kod len pri kolizii. To znamena že pri uspešnom/ neuspešnom vloženi treba mať return z metody.
        System.out.println("\nKolizia");
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
        if (!freeAddresses.isEmpty()) {
            Collections.sort(freeAddresses);
            return freeAddresses.remove(0);
        }
        this.nextFreeAddress++;
        return this.nextFreeAddress;
    }

}
