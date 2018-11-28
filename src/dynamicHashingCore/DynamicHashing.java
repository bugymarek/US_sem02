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

    private int factor;
    private RandomAccessFile mainFile;
    private RandomAccessFile additionFile;
    private final String FILE_TYPE = ".hash";
    private int nextFreeAddress;
    private int nextFreeAddressAdditionFile;
    private ArrayList<Integer> freeAddresses;
    private Node root;
    private int maxHashSize;
    private final String PATH = "C:/Users/Bugy/Documents/NetBeansProjects/US_sem02/src/storage/";
    private final Block TEMPLATE_BLOCK;
    private final Record<T> TEMPLATE_RECORD;

    public DynamicHashing(String fileNeme, int factor, Record<T> templateRecord, int maxHashSize) {
        try {
            mainFile = new RandomAccessFile(PATH + fileNeme + FILE_TYPE, "rw");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            additionFile = new RandomAccessFile(PATH + fileNeme + "Preplnovaci" + FILE_TYPE, "rw");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.TEMPLATE_RECORD = templateRecord;
        this.factor = factor;
        this.nextFreeAddress = -1;
        this.freeAddresses = new ArrayList<>();
        this.maxHashSize = maxHashSize;
        this.TEMPLATE_BLOCK = new Block(-1, factor, templateRecord.getData());
        this.nextFreeAddressAdditionFile = -1;
        root = new ExternalNode(null, 0);
    }

    public void createNewFiles(String fileNeme) {
        try {
            mainFile = new RandomAccessFile(PATH + fileNeme + FILE_TYPE, "rw");
            try {
                mainFile.setLength(0);
            } catch (IOException ex) {
                Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            additionFile = new RandomAccessFile(new File(PATH + fileNeme + "Preplnovaci" + FILE_TYPE), "rw");
            try {
                additionFile.setLength(0);
            } catch (IOException ex) {
                Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public IRecord find(IRecord<T> record) {
        BitSet hash = Converter.getHashFromKey(record.getHashKey(), maxHashSize);
        ExternalNode externalNode = findExternalNode(hash);

        if (externalNode.getAddressBlock() > -1 && externalNode.getValidRecordsCount() > 0) {
            Block block = TEMPLATE_BLOCK.fromByteArray(readFromFile(externalNode.getAddressBlock() * TEMPLATE_BLOCK.getSize(), mainFile));
            Record findedRecord = block.findRecord(new Record(true, record));
            if (findedRecord != null) {
                System.out.println("Nasiel sa zaznam: " + findedRecord.getData().toString());
                return findedRecord.getData();
            } else { // hladaj v preplnovacom subore              
                while (block.getAddressNextBlock() > -1) {
                    block = TEMPLATE_BLOCK.fromByteArray(readFromFile(block.getAddressNextBlock() * TEMPLATE_BLOCK.getSize(), additionFile));
                    findedRecord = block.findRecord(new Record(true, record));
                    if (findedRecord != null) {
                        System.out.println("Nasiel sa zaznam: " + findedRecord.getData().toString());
                        return findedRecord.getData();
                    }
                }
                System.out.println("Nenasiel sa zaznam s hladanym ID: " + record.getHashKey());
            }
        }
        return null;
    }

    private ExternalNode findExternalNode(BitSet hash) {
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
        return (ExternalNode) currentNode;
    }

    public boolean add(IRecord<T> record) {
        Record newRecord = new Record(true, record);
        BitSet hash = Converter.getHashFromKey(record.getHashKey(), maxHashSize);
        ExternalNode currentNode = findExternalNode(hash);

        while (true) {// osteri ak ma externy prvok max hlbku a v nom uz nieje miesto tak vznik kolizie... tato podmienka to zatial neosetruje
            if (currentNode.getAddressBlock() == -1) { //ak nieje alokovaný blok
                int add = getNextFreeAddress();
                Block block = new Block(add, factor, TEMPLATE_RECORD.getData());
                boolean resultMemory = block.addRecord(newRecord);
                boolean resultFile = writeToFile(block.getAddress() * TEMPLATE_BLOCK.getSize(), block.toByteArray(), mainFile);
                if (resultMemory && resultFile) {
                    currentNode.setAddressBlock(block.getAddress());
                    currentNode.incrementValidRecordsCount();
                    return true;
                } else {
                    System.out.println("\n nepodarilo sa vlozit záznam: " + record);
                    return false;
                }
            } else if (currentNode.getValidRecordsCount() < factor) { // ak je v prvom bloku miesto miesto
                Block block = TEMPLATE_BLOCK.fromByteArray(readFromFile(currentNode.getAddressBlock() * TEMPLATE_BLOCK.getSize(), mainFile));
                if (block.findRecord(newRecord) != null) {
                    System.out.println("uz sa tam nachadza záznam s vkladaným ID: " + record.getHashKey());
                    return false;// uz sa tam nachadza záznam s vkladaným ID
                } else {
                    boolean resultMemory = block.addRecord(newRecord);
                    boolean resultFile = writeToFile(block.getAddress() * TEMPLATE_BLOCK.getSize(), block.toByteArray(), mainFile);
                    if (resultMemory && resultFile) {
                        currentNode.incrementValidRecordsCount();
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
                Block block = TEMPLATE_BLOCK.fromByteArray(readFromFile(currentNode.getAddressBlock() * TEMPLATE_BLOCK.getSize(), mainFile));
                if (block.findRecord(newRecord) != null) {
                    System.out.println("uz sa tam nachadza záznam s vkladaným ID: " + record.getHashKey());
                    return false;// uz sa tam nachadza záznam s vkladaným ID
                }
                freeAddresses.add(currentNode.getAddressBlock());
                Node newInternalNode = new InternalNode(currentNode.getFather(), currentNode.getDepth());

                ExternalNode sonLeft = new ExternalNode(newInternalNode, newInternalNode.getDepth() + 1);
                Block blockSonLeft = new Block(getNextFreeAddress(), factor, TEMPLATE_RECORD.getData());
                sonLeft.setAddressBlock(blockSonLeft.getAddress());

                ExternalNode sonRight = new ExternalNode(newInternalNode, newInternalNode.getDepth() + 1);
                Block blockSonRight = new Block(getNextFreeAddress(), factor, TEMPLATE_RECORD.getData());
                sonRight.setAddressBlock(blockSonRight.getAddress());

                ((InternalNode) newInternalNode).setLeftNode(sonLeft);
                ((InternalNode) newInternalNode).setRightNode(sonRight);

                if (currentNode.getFather() == null) {// tu bude pravdepodobne chyba
                    root = newInternalNode;
                } else if (((InternalNode) currentNode.getFather()).getLeftNode() instanceof InternalNode) { // ak je lavy interny potom nastav praveho potomka
                    ((InternalNode) currentNode.getFather()).setRightNode(newInternalNode);
                } else if (((InternalNode) currentNode.getFather()).getRightNode() instanceof InternalNode) { // ak je pravy interny potom nastav laveho potomka
                    ((InternalNode) currentNode.getFather()).setLeftNode(newInternalNode);
                } else if (((ExternalNode) ((InternalNode) currentNode.getFather()).getLeftNode()).getAddressBlock() == currentNode.getAddressBlock()) { // zsiti koho si synom podla adresy, oba synovia su urcite externe vrcholy
                    ((InternalNode) currentNode.getFather()).setLeftNode(newInternalNode);
                } else {
                    ((InternalNode) currentNode.getFather()).setRightNode(newInternalNode);
                }

                // presuvam zaznamy. Tu potrebujes zistit hlbku aktualneho vrchola, co vlastne predstavuje frefix z hash retazca. To znamena ze vsetky zaznamy v tomto
                // aktualnom vrchole maju rovnaku hodnotu prefixu. Preto potrebuješ pre každy zaznam v bolku zistit dalši bit z has retazca podla kotoreho ich potom 
                // rozdeliš do synov. Bud do prava(1) alebo do lava(0)
                int prefixSize = currentNode.getDepth() + 1;
                for (Record r : block.getRecordsList()) {
                    if (r.isIsValid()) {
                        BitSet hashRecord = Converter.getHashFromKey(r.getHashKey(), maxHashSize);
                        if (hashRecord.get(prefixSize - 1)) {// idex dalsieho bitu
                            blockSonRight.addRecord(r);
                            sonRight.incrementValidRecordsCount();
                        } else {
                            blockSonLeft.addRecord(r);
                            sonLeft.incrementValidRecordsCount();
                        }
                    }
                }

                // vlozim vkladany prvok do jedneho zo synov... viem ze sa tam prvok nenachadza
                if (hash.get(prefixSize - 1)) {// idex dalsieho bitu
                    if(blockSonRight.addRecord(newRecord)){
                        sonRight.incrementValidRecordsCount();
                    }   
                } else {                 
                    if(blockSonLeft.addRecord(newRecord)){
                        sonLeft.incrementValidRecordsCount();
                    }
                }

                if (blockSonLeft.getValidRecordsCount() > 0 && blockSonRight.getValidRecordsCount() > 0) {
                    boolean resultFile1 = writeToFile(blockSonLeft.getAddress() * TEMPLATE_BLOCK.getSize(), blockSonLeft.toByteArray(), mainFile);
                    boolean resultFile2 = writeToFile(blockSonRight.getAddress() * TEMPLATE_BLOCK.getSize(), blockSonRight.toByteArray(), mainFile);
                    //freeAddresses.add(((ExternalNode)currentNode).getAddressBlock());
                    if (resultFile1 && resultFile2) {
                        return true;
                    } else {
                        System.out.println("nepodarilo sa vlozit záznam: " + record);
                        return false;
                    }
                }

                if (blockSonLeft.getInvalidRecordsCount() == factor) {
                    freeAddresses.add(blockSonLeft.getAddress());
                    sonLeft.setAddressBlock(-1);
                    blockSonLeft.setAddress(-1);
                    currentNode = sonRight;
                    writeToFile(blockSonRight.getAddress() * TEMPLATE_BLOCK.getSize(), blockSonRight.toByteArray(), mainFile);
                }

                if (blockSonRight.getInvalidRecordsCount() == factor) {
                    freeAddresses.add(blockSonRight.getAddress());
                    sonRight.setAddressBlock(-1);
                    blockSonRight.setAddress(-1);
                    currentNode = sonLeft;
                    writeToFile(blockSonLeft.getAddress() * TEMPLATE_BLOCK.getSize(), blockSonLeft.toByteArray(), mainFile);
                }

                //freeAddresses.add(((ExternalNode)currentNode).getAddressBlock());
            }
        }
        // tu budem riešiť koliziu. Daj pozor aby sa vykonal tento kod len pri kolizii. To znamena že pri uspešnom/ neuspešnom vloženi treba mať return z metody.
        System.out.println("Kolizia. Id zaznamu: " + record.getHashKey());
        System.out.println("\n");
        return addToAdditionFile(currentNode, record);
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

    public ArrayList<Block> readAllBlocksFromFile(String fileName) {
        ArrayList<Block> blockArr = new ArrayList<>();
        int address = 0;
        while (true) {
            Block block = TEMPLATE_BLOCK.fromByteArray(readFromFile(address * TEMPLATE_BLOCK.getSize(), mainFile));
            if (block.getFactor() == 0) {
                break;
            }
            System.out.println(block.toString());
            blockArr.add(block);
            address++;
        }
        return blockArr;
    }

    public String getFreeAddressesString() {
        String result = "Addresy volných blokov: ";
        for (int address : freeAddresses) {
            result += address + ", ";
        }
        return result;
    }

    public ArrayList<Integer> getFreeAddresses() {
        return freeAddresses;
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

    private boolean addToAdditionFile(ExternalNode currentNode, IRecord<T> record) {
        Record newRecord = new Record(true, record);
        Block currentBlock = TEMPLATE_BLOCK.fromByteArray(readFromFile(currentNode.getAddressBlock() * TEMPLATE_BLOCK.getSize(), mainFile));
        boolean mainBlock = true;
        while (currentBlock.getAddressNextBlock() > -1) {
            if (currentBlock.findRecord(newRecord) != null) {
                System.out.println("uz sa tam nachadza záznam s vkladaným ID: " + record.getHashKey());
                return false;
            }
            currentBlock = TEMPLATE_BLOCK.fromByteArray(readFromFile(currentBlock.getAddressNextBlock() * TEMPLATE_BLOCK.getSize(), additionFile));
            mainBlock = false;
        }
        // tu uz mam blok ktory je na konci zretazeneho zoznamu a urcite na celej ceste k nemu sa nenasiel vkladany záznam
        if (currentBlock.findRecord(newRecord) != null) {// kontola ci sa v nom nenachádza vkladaný záznam
            System.out.println("uz sa tam nachadza záznam s vkladaným ID: " + record.getHashKey());
            return false;
        }

        if (currentBlock.getInvalidRecordsCount() > 0) {// v bloku je volne miesto
            boolean resultMemory = currentBlock.addRecord(newRecord);
            boolean resultFile = writeToFile(currentBlock.getAddress() * TEMPLATE_BLOCK.getSize(), currentBlock.toByteArray(), additionFile);
            if (resultMemory && resultFile) {
                //currentNode.incrementValidRecordsCount(); potrebme len ak by som implementoval delte, Taktiez tereaz nebudem vediet kolko zaznamov sa pod tymot vrcholom nachádza
                return true;
            } else {
                System.out.println("\n nepodarilo sa vlozit záznam. Nieco sa pokazilo: " + record);
                return false;
            }
        } else { // v bolku nieje volne miesto a preto vytvorim novy blok, zapisem tam zaznam a zretazim ho s aklutalnym blokom
            nextFreeAddressAdditionFile++;
            Block newAdditionBlock = new Block(nextFreeAddressAdditionFile, factor, TEMPLATE_RECORD.getData());
            boolean resultMemory = newAdditionBlock.addRecord(newRecord);
            boolean resultFile = writeToFile(newAdditionBlock.getAddress() * TEMPLATE_BLOCK.getSize(), newAdditionBlock.toByteArray(), additionFile);
            if (resultMemory && resultFile) {
                //currentNode.incrementValidRecordsCount(); potrebme len ak by som implementoval delte, Taktiez tereaz nebudem vediet kolko zaznamov sa pod tymot vrcholom nachádza
                currentBlock.setAddressNextBlock(nextFreeAddressAdditionFile);
                if (mainBlock) {
                    writeToFile(currentBlock.getAddress() * TEMPLATE_BLOCK.getSize(), currentBlock.toByteArray(), mainFile);
                } else {
                    writeToFile(currentBlock.getAddress() * TEMPLATE_BLOCK.getSize(), currentBlock.toByteArray(), additionFile);
                }
                return true;
            } else {
                nextFreeAddressAdditionFile--;
                System.out.println("\n nepodarilo sa vlozit záznam. Nieco sa pokazilo: " + record);
                return false;
            }
        }
    }

}
