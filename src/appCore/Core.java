/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appCore;

import dynamicHashingCore.DynamicHashing;
import appCore.Record;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dynamicHashingCore.IRecord;
import entities.RealtyData;
import entities.RealtyID;
import entities.RealtyInCadaster;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bugy
 */
public class Core {

    private final String PATH = "C:/Users/Bugy/Documents/NetBeansProjects/US_sem02/src/storage/";
    private final String UNSORTED_FILE_TYPE = ".bin";
    private final String NAME_FILE = "Neutriedeny";
    private final int FACTOR = 2;
    private final RealtyData TEMPLATE_REALTY_DATA = new RealtyData();
    private final Block TEMPLATE_BLOCK = new Block(-1, FACTOR, TEMPLATE_REALTY_DATA);

    private final RealtyID TEPLATE_REALTY_ID = new RealtyID();
    private final RealtyInCadaster TEPLATE_REALTY_IN_CADASTER = new RealtyInCadaster();

    private RandomAccessFile unsrotedFile;
    private DynamicHashing<RealtyID> dynamicHashingRealty;
    private DynamicHashing<RealtyInCadaster> dynamicHashingRealtyInCadaster;
    private Random generatorSeeds = new Random();

    public Core(String fileMainName, String fileAdditionName, int mainFactor, int additionFactor, int maxHashSize) {
        this.dynamicHashingRealty = new DynamicHashing(fileMainName, mainFactor, additionFactor, this.TEPLATE_REALTY_ID, maxHashSize);
        this.dynamicHashingRealty.createNewFiles(fileMainName);

        this.dynamicHashingRealtyInCadaster = new DynamicHashing(fileAdditionName, mainFactor, additionFactor, this.TEPLATE_REALTY_IN_CADASTER, maxHashSize);
        this.dynamicHashingRealtyInCadaster.createNewFiles(fileAdditionName);

        try {
            this.unsrotedFile = new RandomAccessFile(PATH + NAME_FILE + UNSORTED_FILE_TYPE, "rw");
            try {
                this.unsrotedFile.setLength(0);
            } catch (IOException ex) {
                Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Core(String fileMainName, String fileAdditionName) {
        this.dynamicHashingRealty = DynamicHashing.loadTrie(fileMainName, TEPLATE_REALTY_ID);
        this.dynamicHashingRealtyInCadaster = DynamicHashing.loadTrie(fileAdditionName, TEPLATE_REALTY_IN_CADASTER);

        try {
            this.unsrotedFile = new RandomAccessFile(PATH + NAME_FILE + UNSORTED_FILE_TYPE, "rw");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DynamicHashing<RealtyID> getDynamicHashingRealty() {
        return dynamicHashingRealty;
    }

    public DynamicHashing<RealtyInCadaster> getDynamicHashingRealtyInCadaster() {
        return dynamicHashingRealtyInCadaster;
    }

    public boolean saveToConfigFiles() {
        boolean result1 = this.dynamicHashingRealty.saveTrieToFile();
        boolean result2 = this.dynamicHashingRealtyInCadaster.saveTrieToFile();
        return result1 && result2;
    }

    private boolean writeToFile(int offset, byte[] byteArr) {
        try {
            this.unsrotedFile.seek(offset);
        } catch (IOException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        try {
            this.unsrotedFile.write(byteArr);
        } catch (IOException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }

    private byte[] readFromFile(int offset) {

        byte[] byteArr = new byte[TEMPLATE_BLOCK.getSize()];
        try {
            unsrotedFile.seek(offset);
        } catch (IOException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        try {
            unsrotedFile.read(byteArr, 0, TEMPLATE_BLOCK.getSize());
        } catch (IOException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return byteArr;
    }

    public int addRealty(int id, int idInCadaster, String cadasterName, String desc) {

        RealtyID findedRealtyID = (RealtyID) dynamicHashingRealty.find(new RealtyID(id));
        if (findedRealtyID != null) {
            return -1;
        }

        RealtyInCadaster findedRealtyInCadaster = (RealtyInCadaster) dynamicHashingRealtyInCadaster.find(new RealtyInCadaster(idInCadaster, cadasterName));
        if (findedRealtyInCadaster != null) {
            return -2;
        }

        int address = addRealtyDataToFile(new RealtyData(id, idInCadaster, cadasterName, desc));
        if (address == -1) {
            return -3;
        }

        if (!dynamicHashingRealty.add(new RealtyID(id, address))) {
            return -4;
        }

        if (!dynamicHashingRealtyInCadaster.add(new RealtyInCadaster(idInCadaster, address, cadasterName))) {
            return -5;
        }

        return 0;
    }

    private int addRealtyDataToFile(RealtyData realtyData) {
        Record newRecord = new Record(true, realtyData);
        int fileSize = 0;
        try {
            fileSize = (int) unsrotedFile.length();
        } catch (IOException ex) {
            Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

        Block block = null;
        if (fileSize == 0) { // ak je subor prazndy zapis tam novy blok
            block = new Block(0, FACTOR, TEMPLATE_REALTY_DATA);
            boolean resultMemory = block.addRecord(newRecord);
            boolean resultFile = writeToFile(0, block.toByteArray());
            if (resultMemory && resultFile) {
                return 0;
            } else {
                //System.out.println("\n nepodarilo sa vlozit záznam: " + newRecord.toString());
                return -1;
            }
        } else { // ake nieje subor prazdy
            int a = fileSize - TEMPLATE_BLOCK.getSize();
            block = TEMPLATE_BLOCK.fromByteArray(readFromFile(fileSize - TEMPLATE_BLOCK.getSize()));
            if (block.getInvalidRecordsCount() > 0) { // je v poslednom bloku miesto na zapis zaznamu?
                boolean resultMemory = block.addRecord(newRecord);
                boolean resultFile = writeToFile(fileSize - TEMPLATE_BLOCK.getSize(), block.toByteArray());
                if (resultMemory && resultFile) {
                    return block.getAddress();
                } else {
                    //System.out.println("\n nepodarilo sa vlozit záznam: " + newRecord.toString());
                    return -1;
                }
            } else { // v poslednom blku uz nieje miesto na zapis... vytvor dalsi blok a vloz tam zaznam
                block = new Block(fileSize / TEMPLATE_BLOCK.getSize(), FACTOR, TEMPLATE_REALTY_DATA);
                boolean resultMemory = block.addRecord(newRecord);
                boolean resultFile = writeToFile(fileSize, block.toByteArray());
                if (resultMemory && resultFile) {
                    return block.getAddress();
                } else {
                    //System.out.println("\n nepodarilo sa vlozit záznam: " + newRecord.toString());
                    return -1;
                }
            }
        }
    }

    private RealtyData findRealtyInUnsortedFile(int address, IRecord record) {
        Block block = TEMPLATE_BLOCK.fromByteArray(readFromFile(address * TEMPLATE_BLOCK.getSize()));
        Record findedRecord = block.findRecord(new Record(true, record));
        if (findedRecord != null) {
            return (RealtyData) findedRecord.getData();
        }
        return null;
    }

    private RealtyData findRealtyInBlock(Block block, IRecord record) {
        Record findedRecord = block.findRecord(new Record(true, record));
        if (findedRecord != null) {
            return (RealtyData) findedRecord.getData();
        }
        return null;
    }

    public int generateRealties(int count) {
        Random randomGenerator = new Random(generatorSeeds.nextInt());

        int failedAddedCount = 0;
        for (int i = 0; i < count; i++) {
            int registerNumber = getRandomId(8, 9);
            int idRealty = randomGenerator.nextInt(999999999);
            String cadasterName = getRandomString(randomGenerator.nextInt(10) + 5, false);
            String desc = getRandomString(randomGenerator.nextInt(16) + 4, true);

            int result = addRealty(registerNumber, idRealty, cadasterName, desc);
            if (result != 0) {
                failedAddedCount++;
            }
        }
        return failedAddedCount;
    }

    private String getRandomString(int length, boolean numbers) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if (numbers) {
            SALTCHARS += "123456789";
        }
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random(generatorSeeds.nextInt());
        int index;
        while (salt.length() < length) { // length of the random string.
            index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    private int getRandomId(int fromNumeralCount, int toNumeralCount) {
        Random randomGenerator = new Random(generatorSeeds.nextInt());
        int lenth = randomGenerator.nextInt(toNumeralCount - fromNumeralCount) + fromNumeralCount;
        String strNumper = new String();
        for (int j = 0; j < lenth; j++) {
            if (j == 0) {
                strNumper += randomGenerator.nextInt(8) + 1;
            } else {
                strNumper += randomGenerator.nextInt(9);
            }
        }
        return Integer.parseInt(strNumper);
    }

    public JsonObject findRealtyByID(int id) {
        JsonObject jobj = new JsonObject();
        RealtyID realtyID = (RealtyID) dynamicHashingRealty.find(new RealtyID(id));
        if (realtyID == null) {
            jobj.addProperty("err", "Nehnuteľnosť sa nenašla.");
            return jobj;
        }

        RealtyData realtyData = findRealtyInUnsortedFile(realtyID.getAddress(), new RealtyData(id));
        if (realtyData == null) {
            jobj.addProperty("err", "Nehnuteľnosť sa nenašla v neusporiadanom súbore.");
            return jobj;
        }

        jobj.addProperty("id", realtyData.getId());
        jobj.addProperty("idRealty", realtyData.getIdInCadaster());
        jobj.addProperty("cadasterName", realtyData.getCadasterName());
        jobj.addProperty("desc", realtyData.getDesc());

        return jobj;
    }

    public JsonObject findRealtyByRegistrationNumberAndCadasterName(int id, String name) {
        JsonObject jobj = new JsonObject();
        RealtyInCadaster realtyInCadaster = (RealtyInCadaster) dynamicHashingRealtyInCadaster.find(new RealtyInCadaster(id, name));
        if (realtyInCadaster == null) {
            jobj.addProperty("err", "Nehnuteľnosť sa nenašla.");
            return jobj;
        }

        RealtyData realtyData = findRealtyInUnsortedFile(realtyInCadaster.getAddress(), new RealtyData(id, name));
        if (realtyData == null) {
            jobj.addProperty("err", "Nehnuteľnosť sa nenašla v neusporiadanom súbore.");
            return jobj;
        }

        jobj.addProperty("id", realtyData.getId());
        jobj.addProperty("idRealty", realtyData.getIdInCadaster());
        jobj.addProperty("cadasterName", realtyData.getCadasterName());
        jobj.addProperty("desc", realtyData.getDesc());

        return jobj;
    }

    public JsonArray getBlocksRealtyByIdFromMainFile() {
        ArrayList<dynamicHashingCore.Block> arr = dynamicHashingRealty.readAllBlocksFromMainFile();
        if (arr.isEmpty()) {
            return null;
        }

        JsonArray jsonArray = new JsonArray();
        for (dynamicHashingCore.Block block : arr) {
            JsonObject jobjBlock = new JsonObject();
            jobjBlock.addProperty("isValid", !dynamicHashingRealty.isBlocAddressFree(block.getAddress()));
            jobjBlock.addProperty("address", block.getAddress());
            jobjBlock.addProperty("addressNextBlock", block.getAddressNextBlock());
            jobjBlock.addProperty("factor", block.getFactor());
            jobjBlock.addProperty("validCount", block.getValidRecordsCount());

            JsonArray jsonArrayRecords = new JsonArray();
            for (dynamicHashingCore.Record record : block.getRecordsList()) {
                JsonObject jobjRecord = new JsonObject();
                jobjRecord.addProperty("isValid", record.isIsValid());
                jobjRecord.addProperty("id", ((RealtyID) record.getData()).getId());
                jobjRecord.addProperty("address", ((RealtyID) record.getData()).getAddress());

                jsonArrayRecords.add(jobjRecord);
            }
            jobjBlock.add("records", jsonArrayRecords);

            jsonArray.add(jobjBlock);
        }

        return jsonArray;
    }

    public JsonArray getBlocksRealtyByIdFromAdditionFile() {
        ArrayList<dynamicHashingCore.Block> arr = dynamicHashingRealty.readAllBlocksFromAdditionFile();
        if (arr.isEmpty()) {
            return null;
        }

        JsonArray jsonArray = new JsonArray();
        for (dynamicHashingCore.Block block : arr) {
            JsonObject jobjBlock = new JsonObject();
            jobjBlock.addProperty("address", block.getAddress());
            jobjBlock.addProperty("addressNextBlock", block.getAddressNextBlock());
            jobjBlock.addProperty("factor", block.getFactor());
            jobjBlock.addProperty("validCount", block.getValidRecordsCount());

            JsonArray jsonArrayRecords = new JsonArray();
            for (dynamicHashingCore.Record record : block.getRecordsList()) {
                JsonObject jobjRecord = new JsonObject();
                jobjRecord.addProperty("isValid", record.isIsValid());
                jobjRecord.addProperty("id", ((RealtyID) record.getData()).getId());
                jobjRecord.addProperty("address", ((RealtyID) record.getData()).getAddress());

                jsonArrayRecords.add(jobjRecord);
            }
            jobjBlock.add("records", jsonArrayRecords);

            jsonArray.add(jobjBlock);
        }

        return jsonArray;
    }

    public JsonArray getBlocksRealtyByIdInCadasterAndCadasterNameFromMainFile() {
        ArrayList<dynamicHashingCore.Block> arr = dynamicHashingRealtyInCadaster.readAllBlocksFromMainFile();
        if (arr.isEmpty()) {
            return null;
        }

        JsonArray jsonArray = new JsonArray();
        for (dynamicHashingCore.Block block : arr) {
            JsonObject jobjBlock = new JsonObject();
            jobjBlock.addProperty("isValid", !dynamicHashingRealtyInCadaster.isBlocAddressFree(block.getAddress()));
            jobjBlock.addProperty("address", block.getAddress());
            jobjBlock.addProperty("addressNextBlock", block.getAddressNextBlock());
            jobjBlock.addProperty("factor", block.getFactor());
            jobjBlock.addProperty("validCount", block.getValidRecordsCount());

            JsonArray jsonArrayRecords = new JsonArray();
            for (dynamicHashingCore.Record record : block.getRecordsList()) {
                JsonObject jobjRecord = new JsonObject();
                jobjRecord.addProperty("isValid", record.isIsValid());
                jobjRecord.addProperty("registerNumber", ((RealtyInCadaster) record.getData()).getRegisterNumer());
                jobjRecord.addProperty("cadasterName", ((RealtyInCadaster) record.getData()).getCadasterName());
                jobjRecord.addProperty("address", ((RealtyInCadaster) record.getData()).getAddress());

                jsonArrayRecords.add(jobjRecord);
            }
            jobjBlock.add("records", jsonArrayRecords);

            jsonArray.add(jobjBlock);
        }

        return jsonArray;
    }

    public JsonArray getBlocksRealtyByIdInCadasterAndCadasterNameFromAdditionFile() {
        ArrayList<dynamicHashingCore.Block> arr = dynamicHashingRealtyInCadaster.readAllBlocksFromAdditionFile();
        if (arr.isEmpty()) {
            return null;
        }

        JsonArray jsonArray = new JsonArray();
        for (dynamicHashingCore.Block block : arr) {
            JsonObject jobjBlock = new JsonObject();
            jobjBlock.addProperty("address", block.getAddress());
            jobjBlock.addProperty("addressNextBlock", block.getAddressNextBlock());
            jobjBlock.addProperty("factor", block.getFactor());
            jobjBlock.addProperty("validCount", block.getValidRecordsCount());

            JsonArray jsonArrayRecords = new JsonArray();
            for (dynamicHashingCore.Record record : block.getRecordsList()) {
                JsonObject jobjRecord = new JsonObject();
                jobjRecord.addProperty("isValid", record.isIsValid());
                jobjRecord.addProperty("registerNumber", ((RealtyInCadaster) record.getData()).getRegisterNumer());
                jobjRecord.addProperty("cadasterName", ((RealtyInCadaster) record.getData()).getCadasterName());
                jobjRecord.addProperty("address", ((RealtyInCadaster) record.getData()).getAddress());

                jsonArrayRecords.add(jobjRecord);
            }
            jobjBlock.add("records", jsonArrayRecords);

            jsonArray.add(jobjBlock);
        }

        return jsonArray;
    }

    public JsonArray getBlocksRealtyDataFromUnsortedFile() {
        ArrayList<Block> arr = readAllBlocksFromUnsortedFile();
        if (arr.isEmpty()) {
            return null;
        }

        JsonArray jsonArray = new JsonArray();
        for (Block block : arr) {
            JsonObject jobjBlock = new JsonObject();
            jobjBlock.addProperty("address", block.getAddress());
            jobjBlock.addProperty("factor", block.getFactor());
            jobjBlock.addProperty("validCount", block.getValidRecordsCount());

            JsonArray jsonArrayRecords = new JsonArray();
            for (Record record : block.getRecordsList()) {
                JsonObject jobjRecord = new JsonObject();
                jobjRecord.addProperty("isValid", record.isIsValid());
                jobjRecord.addProperty("id", ((RealtyData) record.getData()).getId());
                jobjRecord.addProperty("registerNumber", ((RealtyData) record.getData()).getIdInCadaster());
                jobjRecord.addProperty("cadasterName", ((RealtyData) record.getData()).getCadasterName());
                jobjRecord.addProperty("desc", ((RealtyData) record.getData()).getDesc());

                jsonArrayRecords.add(jobjRecord);
            }
            jobjBlock.add("records", jsonArrayRecords);

            jsonArray.add(jobjBlock);
        }

        return jsonArray;
    }

    public ArrayList<Block> readAllBlocksFromUnsortedFile() {
        ArrayList<Block> blockArr = new ArrayList<>();
        int offset = 0;
        int fileLength = 0;
        try {
            fileLength = (int) unsrotedFile.length();
        } catch (IOException ex) {
            Logger.getLogger(DynamicHashing.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (fileLength != offset) {
            Block block = TEMPLATE_BLOCK.fromByteArray(readFromFile(offset));
            blockArr.add(block);
            offset += TEMPLATE_BLOCK.getSize();
        }
        return blockArr;
    }

    public JsonObject editRealty(int id, String desc) {
        JsonObject jobj = new JsonObject();
        RealtyID realtyID = (RealtyID) dynamicHashingRealty.find(new RealtyID(id));
        if (realtyID == null) {
            jobj.addProperty("err", "Nehnuteľnosť sa nenašla.");
            return jobj;
        }

        Block block = TEMPLATE_BLOCK.fromByteArray(readFromFile(realtyID.getAddress() * TEMPLATE_BLOCK.getSize()));

        RealtyData realtyData = findRealtyInBlock(block, new RealtyData(id));
        if (realtyData == null) {
            jobj.addProperty("err", "Nehnuteľnosť sa nenašla v neusporiadanom súbore.");
            return jobj;
        }

        realtyData.setDesc(desc);
        
        jobj.addProperty("id", realtyData.getId());
        jobj.addProperty("idRealty", realtyData.getIdInCadaster());
        jobj.addProperty("cadasterName", realtyData.getCadasterName());
        jobj.addProperty("desc", realtyData.getDesc());

        boolean resultFile = writeToFile(realtyID.getAddress() * TEMPLATE_BLOCK.getSize(), block.toByteArray());
        if (!resultFile) {
            jobj = new JsonObject();
            jobj.addProperty("err", "Nepodarilo sa editovať nehnuteľnosť.");
            return jobj;
        }

        return jobj;
    }

}
