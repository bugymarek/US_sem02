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
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 *
 * @author Bugy
 */
public class DynamicHashing<T> {

    private RandomAccessFile mainFile;
    private RandomAccessFile additionFile;
    private int nextFreeAddress;
    private int nextFreeAddressAdditionFile;
    private ArrayList<Integer> freeAddresses;
    private Node root;
    private int maxHashSize;
    private final String PATH = "C:/Users/Bugy/Documents/NetBeansProjects/US_sem02/src/storage/";
    private final Block TEMPLATE_MAIN_BLOCK;
    private final Block TEMPLATE_ADDOTION_BLOCK;
    private final Record<T> TEMPLATE_RECORD;
    private final String FILE_NAME;
    private final String FILE_TYPE = ".hash";
    private final String FILE_CONFIG_TYPE = ".txt";

    public DynamicHashing(String fileNeme, int mainFactor, int additionFactor, Record<T> templateRecord, int maxHashSize) {
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
        this.nextFreeAddress = -1;
        this.freeAddresses = new ArrayList<>();
        this.maxHashSize = maxHashSize;
        this.TEMPLATE_MAIN_BLOCK = new Block(-1, mainFactor, templateRecord.getData());
        this.TEMPLATE_ADDOTION_BLOCK = new Block(-1, additionFactor, templateRecord.getData());
        this.FILE_NAME = fileNeme;
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
            Block block = TEMPLATE_MAIN_BLOCK.fromByteArray(readFromFile(externalNode.getAddressBlock() * TEMPLATE_MAIN_BLOCK.getSize(), mainFile, TEMPLATE_MAIN_BLOCK.getSize()));
            Record findedRecord = block.findRecord(new Record(true, record));
            if (findedRecord != null) {
                //System.out.println("Nasiel sa zaznam: " + findedRecord.getData().toString());
                return findedRecord.getData();
            } else { // hladaj v preplnovacom subore              
                while (block.getAddressNextBlock() > -1) {
                    block = TEMPLATE_ADDOTION_BLOCK.fromByteArray(readFromFile(block.getAddressNextBlock() * TEMPLATE_ADDOTION_BLOCK.getSize(), additionFile, TEMPLATE_ADDOTION_BLOCK.getSize()));
                    findedRecord = block.findRecord(new Record(true, record));
                    if (findedRecord != null) {
                        //System.out.println("Nasiel sa zaznam v preplnovacom bloku: " + findedRecord.getData().toString());
                        return findedRecord.getData();
                    }
                }
                //System.out.println("Nenasiel sa zaznam s hladanym ID: " + record.getHashKey());
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
                Block block = new Block(add, TEMPLATE_MAIN_BLOCK.getFactor(), TEMPLATE_RECORD.getData());
                boolean resultMemory = block.addRecord(newRecord);
                boolean resultFile = writeToFile(block.getAddress() * TEMPLATE_MAIN_BLOCK.getSize(), block.toByteArray(), mainFile);
                if (resultMemory && resultFile) {
                    currentNode.setAddressBlock(block.getAddress());
                    currentNode.incrementValidRecordsCount();
                    return true;
                } else {
                    System.out.println("\n nepodarilo sa vlozit záznam: " + record);
                    return false;
                }
            } else if (currentNode.getValidRecordsCount() < TEMPLATE_MAIN_BLOCK.getFactor()) { // ak je v prvom bloku miesto miesto
                Block block = TEMPLATE_MAIN_BLOCK.fromByteArray(readFromFile(currentNode.getAddressBlock() * TEMPLATE_MAIN_BLOCK.getSize(), mainFile, TEMPLATE_MAIN_BLOCK.getSize()));
                if (block.findRecord(newRecord) != null) {
                    System.out.println("uz sa tam nachadza záznam s vkladaným ID: " + record.getHashKey());
                    return false;// uz sa tam nachadza záznam s vkladaným ID
                } else {
                    boolean resultMemory = block.addRecord(newRecord);
                    boolean resultFile = writeToFile(block.getAddress() * TEMPLATE_MAIN_BLOCK.getSize(), block.toByteArray(), mainFile);
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
                Block block = TEMPLATE_MAIN_BLOCK.fromByteArray(readFromFile(currentNode.getAddressBlock() * TEMPLATE_MAIN_BLOCK.getSize(), mainFile, TEMPLATE_MAIN_BLOCK.getSize()));
                if (block.findRecord(newRecord) != null) {
                    System.out.println("uz sa tam nachadza záznam s vkladaným ID: " + record.getHashKey());
                    return false;// uz sa tam nachadza záznam s vkladaným ID
                }
                freeAddresses.add(currentNode.getAddressBlock());
                Node newInternalNode = new InternalNode(currentNode.getFather(), currentNode.getDepth());

                ExternalNode sonLeft = new ExternalNode(newInternalNode, newInternalNode.getDepth() + 1);
                Block blockSonLeft = new Block(getNextFreeAddress(), TEMPLATE_MAIN_BLOCK.getFactor(), TEMPLATE_RECORD.getData());
                sonLeft.setAddressBlock(blockSonLeft.getAddress());

                ExternalNode sonRight = new ExternalNode(newInternalNode, newInternalNode.getDepth() + 1);
                Block blockSonRight = new Block(getNextFreeAddress(), TEMPLATE_MAIN_BLOCK.getFactor(), TEMPLATE_RECORD.getData());
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
                    if (blockSonRight.addRecord(newRecord)) {
                        sonRight.incrementValidRecordsCount();
                    }
                } else if (blockSonLeft.addRecord(newRecord)) {
                    sonLeft.incrementValidRecordsCount();
                }

                if (blockSonLeft.getValidRecordsCount() > 0 && blockSonRight.getValidRecordsCount() > 0) {
                    boolean resultFile1 = writeToFile(blockSonLeft.getAddress() * TEMPLATE_MAIN_BLOCK.getSize(), blockSonLeft.toByteArray(), mainFile);
                    boolean resultFile2 = writeToFile(blockSonRight.getAddress() * TEMPLATE_MAIN_BLOCK.getSize(), blockSonRight.toByteArray(), mainFile);
                    //freeAddresses.add(((ExternalNode)currentNode).getAddressBlock());
                    if (resultFile1 && resultFile2) {
                        return true;
                    } else {
                        System.out.println("nepodarilo sa vlozit záznam: " + record);
                        return false;
                    }
                }

                if (blockSonLeft.getInvalidRecordsCount() == TEMPLATE_MAIN_BLOCK.getFactor()) {
                    freeAddresses.add(blockSonLeft.getAddress());
                    sonLeft.setAddressBlock(-1);
                    blockSonLeft.setAddress(-1);
                    currentNode = sonRight;
                    writeToFile(blockSonRight.getAddress() * TEMPLATE_MAIN_BLOCK.getSize(), blockSonRight.toByteArray(), mainFile);
                }

                if (blockSonRight.getInvalidRecordsCount() == TEMPLATE_MAIN_BLOCK.getFactor()) {
                    freeAddresses.add(blockSonRight.getAddress());
                    sonRight.setAddressBlock(-1);
                    blockSonRight.setAddress(-1);
                    currentNode = sonLeft;
                    writeToFile(blockSonLeft.getAddress() * TEMPLATE_MAIN_BLOCK.getSize(), blockSonLeft.toByteArray(), mainFile);
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
            Block block = TEMPLATE_MAIN_BLOCK.fromByteArray(readFromFile(address * TEMPLATE_MAIN_BLOCK.getSize(), mainFile, TEMPLATE_MAIN_BLOCK.getSize()));
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

    private byte[] readFromFile(int offset, RandomAccessFile file, int size) {

        byte[] byteArr = new byte[size];
        try {
            file.seek(offset);
        } catch (IOException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        try {
            file.read(byteArr, 0, size);
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
        Block currentBlock = TEMPLATE_MAIN_BLOCK.fromByteArray(readFromFile(currentNode.getAddressBlock() * TEMPLATE_MAIN_BLOCK.getSize(), mainFile, TEMPLATE_MAIN_BLOCK.getSize()));
        boolean mainBlock = true;
        while (currentBlock.getAddressNextBlock() > -1) {
            if (currentBlock.findRecord(newRecord) != null) {
                System.out.println("uz sa tam nachadza záznam s vkladaným ID: " + record.getHashKey());
                return false;
            }
            currentBlock = TEMPLATE_ADDOTION_BLOCK.fromByteArray(readFromFile(currentBlock.getAddressNextBlock() * TEMPLATE_ADDOTION_BLOCK.getSize(), additionFile, TEMPLATE_ADDOTION_BLOCK.getSize()));
            mainBlock = false;
        }
        // tu uz mam blok ktory je na konci zretazeneho zoznamu a urcite na celej ceste k nemu sa nenasiel vkladany záznam
        if (currentBlock.findRecord(newRecord) != null) {// kontola ci sa v nom nenachádza vkladaný záznam
            System.out.println("uz sa tam nachadza záznam s vkladaným ID: " + record.getHashKey());
            return false;
        }

        if (currentBlock.getInvalidRecordsCount() > 0) {// v bloku je volne miesto
            boolean resultMemory = currentBlock.addRecord(newRecord);
            boolean resultFile = writeToFile(currentBlock.getAddress() * TEMPLATE_ADDOTION_BLOCK.getSize(), currentBlock.toByteArray(), additionFile);
            if (resultMemory && resultFile) {
                //currentNode.incrementValidRecordsCount(); potrebme len ak by som implementoval delte, Taktiez tereaz nebudem vediet kolko zaznamov sa pod tymot vrcholom nachádza
                return true;
            } else {
                System.out.println("\n nepodarilo sa vlozit záznam. Nieco sa pokazilo: " + record);
                return false;
            }
        } else { // v bolku nieje volne miesto a preto vytvorim novy blok, zapisem tam zaznam a zretazim ho s aklutalnym blokom
            nextFreeAddressAdditionFile++;
            Block newAdditionBlock = new Block(nextFreeAddressAdditionFile, TEMPLATE_ADDOTION_BLOCK.getFactor(), TEMPLATE_RECORD.getData());
            boolean resultMemory = newAdditionBlock.addRecord(newRecord);
            boolean resultFile = writeToFile(newAdditionBlock.getAddress() * TEMPLATE_ADDOTION_BLOCK.getSize(), newAdditionBlock.toByteArray(), additionFile);
            if (resultMemory && resultFile) {
                //currentNode.incrementValidRecordsCount(); potrebme len ak by som implementoval delte, Taktiez tereaz nebudem vediet kolko zaznamov sa pod tymot vrcholom nachádza
                currentBlock.setAddressNextBlock(nextFreeAddressAdditionFile);
                if (mainBlock) {
                    writeToFile(currentBlock.getAddress() * TEMPLATE_MAIN_BLOCK.getSize(), currentBlock.toByteArray(), mainFile);
                } else {
                    writeToFile(currentBlock.getAddress() * TEMPLATE_ADDOTION_BLOCK.getSize(), currentBlock.toByteArray(), additionFile);
                }
                return true;
            } else {
                nextFreeAddressAdditionFile--;
                System.out.println("\n nepodarilo sa vlozit záznam. Nieco sa pokazilo: " + record);
                return false;
            }
        }
    }

    public ArrayList<Node> levelOrder() {
        ArrayList<Node> listArr = new ArrayList<>();
        Queue<Node> level = new LinkedList<Node>();
        level.add(root);

        while (!level.isEmpty() && root != null) {
            int nodeCountInCurrentLevel = level.size(); //počet prvkov na danej urovni

            while (nodeCountInCurrentLevel > 0) {  // vloženie prvkov do pola z aktualnej urovne, pridanie prvkov do fornty z nasledujúceho levelu
                Node node = level.poll();
                if (node != null) {
                    listArr.add(node);
                    if (node instanceof InternalNode) {
                        if (((InternalNode) node).getLeftNode()!= null) {
                            level.add(((InternalNode) node).getLeftNode());
                        }
                        if (((InternalNode) node).getRightNode()!= null) {
                            level.add(((InternalNode) node).getRightNode());
                        }
                    }
                    nodeCountInCurrentLevel--;
                }
            }
        }
        return listArr;
    }
    
    public boolean saveTrieToFile() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(this.PATH + this.FILE_NAME + "Config" + this.FILE_CONFIG_TYPE);
        } catch (FileNotFoundException e) {
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                sb.append(element.toString());
                sb.append("\n");
            }
            System.out.println("Chyba suboru. Subor sa nenasiel alebo je otvoreny. Alebo nastala ina chyba suboru: \n" + sb.toString());
            return false;
        }
        
        ArrayList<Node> listArr = new ArrayList<>();
        Queue<Node> level = new LinkedList<Node>();
        level.add(root);

        while (!level.isEmpty() && root != null) {
            int nodeCountInCurrentLevel = level.size(); //počet prvkov na danej urovni

            while (nodeCountInCurrentLevel > 0) {  // vloženie prvkov do pola z aktualnej urovne, pridanie prvkov do fornty z nasledujúceho levelu
                Node node = level.poll();
                if (node != null) {
                    writer.append(node.save());
                    listArr.add(node);
                    if (node instanceof InternalNode) {
                        if (((InternalNode) node).getLeftNode()!= null) {
                            level.add(((InternalNode) node).getLeftNode());
                        }
                        if (((InternalNode) node).getRightNode()!= null) {
                            level.add(((InternalNode) node).getRightNode());
                        }
                    }
                    nodeCountInCurrentLevel--;
                }
            }
            
            writer.append(System.lineSeparator());
        }
        writer.close();
        return true;
    }
    
    public boolean loadTrie() {
        boolean result = true;
        Scanner sc = null;
        try {
            sc = new Scanner(new FileReader(this.PATH + this.FILE_NAME + "Config" + this.FILE_CONFIG_TYPE));
        } catch (FileNotFoundException e) {
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                sb.append(element.toString());
                sb.append("\n");
            }
            System.out.println("Chyba suboru. Subor sa nenasiel alebo je otvoreny. Alebo nastala ina chyba suboru: \n" + sb.toString());
            return false;
        }
        int depth = 0;
        Node root = null;
        LinkedList<Node> nodesLevel = new LinkedList<>();
        if (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(";");
            String nodeString = line[0];
            String[] params = nodeString.split("_");
            if (params[0].equals("[E]")) {
                Node extNode = new ExternalNode(null, depth);
                ((ExternalNode) extNode).setValidRecordsCount(Integer.parseInt(params[1]));
                ((ExternalNode) extNode).setAddressBlock(Integer.parseInt(params[2]));
                root = extNode;
            } else {
                root = new InternalNode(null, depth);
            }
            nodesLevel.add(root);
        }

        while (sc.hasNextLine()) {
            depth++;
            String[] line = sc.nextLine().split(";");
            LinkedList<Node> nextLevelNodes = new LinkedList<>();
            
            for (int i = 0; i < line.length; i++) {
                String nodeString = line[i];
                String[] params = nodeString.split("_");
                if (params[0].equals("[E]")) {
                    Node extNode = new ExternalNode(null, depth);
                    ((ExternalNode) extNode).setValidRecordsCount(Integer.parseInt(params[1]));
                    ((ExternalNode) extNode).setAddressBlock(Integer.parseInt(params[2]));
                    nextLevelNodes.add(extNode);
                } else {
                    nextLevelNodes.add(new InternalNode(null, depth));
                }
            }
            
            Iterator <Node> NodesIterator = nextLevelNodes.iterator();
            while (!nodesLevel.isEmpty()) {
                Node node = nodesLevel.poll();
                  
                Node nodeLeft = NodesIterator.next();
                Node nodeRight = NodesIterator.next();
                
                ((InternalNode) node).setLeftNode(nodeLeft);
                ((InternalNode) node).setRightNode(nodeRight);
                
                nodeLeft.setFather(node);
                nodeRight.setFather(node);
            }
            
            while(!nextLevelNodes.isEmpty()) {
                Node nodeNext = nextLevelNodes.poll();
                if(nodeNext instanceof InternalNode){
                    nodesLevel.add(nodeNext);
                }
            }
        }
        sc.close();

        this.setRoot(root);
        return result;
    }

    public void setRoot(Node root) {
        this.root = root;
    }
}
