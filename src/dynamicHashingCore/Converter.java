/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamicHashingCore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bugy
 */
public class Converter {

    public static class ConverterToByteArray {

        ByteArrayOutputStream hlpByteArrayOutputStream;
        DataOutputStream hlpOutStream;

        public ConverterToByteArray() {
            hlpByteArrayOutputStream = new ByteArrayOutputStream();
            hlpOutStream = new DataOutputStream(hlpByteArrayOutputStream);
        }

        public byte[] toByteArray() {
            return hlpByteArrayOutputStream.toByteArray();
        }

        public void writeInt(int value) {
            try {
                hlpOutStream.writeInt(value);
            } catch (IOException e) {
                throw new IllegalStateException("Error during conversion to byte array.");
            }
        }

        public void writeString(String value) {
            try {
                hlpOutStream.writeChars(value);
            } catch (IOException e) {
                throw new IllegalStateException("Error during conversion to byte array.");
            }
        }

        void writeBoolean(boolean value) {
            try {
                hlpOutStream.writeBoolean(value);
            } catch (IOException e) {
                throw new IllegalStateException("Error during conversion to byte array.");
            }
        }

        void writeByteArray(byte[] value) {
            try {
                hlpOutStream.write(value);
            } catch (IOException e) {
                throw new IllegalStateException("Error during conversion to byte array.");
            }
        }
    }

    public static class ConverterFromByteArray {

        ByteArrayInputStream hlpByteArrayInputStream;
        DataInputStream hlpInStream;

        public ConverterFromByteArray(byte[] byteArray) {
            hlpByteArrayInputStream = new ByteArrayInputStream(byteArray);
            hlpInStream = new DataInputStream(hlpByteArrayInputStream);
        }

        public int readInt() {
            try {
                return hlpInStream.readInt();
            } catch (IOException e) {
                throw new IllegalStateException("Error during conversion from byte array.");
            }
        }

        public char readChar() {
            try {
                return hlpInStream.readChar();
            } catch (IOException e) {
                throw new IllegalStateException("Error during conversion from byte array.");
            }
        }

        public boolean readBoolean() {
            try {
                return hlpInStream.readBoolean();
            } catch (IOException e) {
                throw new IllegalStateException("Error during conversion from byte array.");
            }
        }

        byte[] readByteArray(int size) {
            try {
                byte[] byteArray = new byte[size];
                for (int i = 0; i < size; i++) {
                    byteArray[i] = hlpInStream.readByte();
                }
                return byteArray;
            } catch (IOException e) {
                throw new IllegalStateException("Error during conversion from byte array.");
            }
        }

    }

    public static BitSet getHashFromKey(String key, int size) {
        BitSet bitSetResult = new BitSet();
        char[] charsArr = key.toCharArray();
        int number = 0;
        for (char c : charsArr) {
            number += c;
        }

        BitSet bitSetNumber = BitSet.valueOf(new long[]{number});

        System.out.print("id -> ");
        for (int i = bitSetNumber.length() - 1; i >= 0; i--) {
            System.out.print(bitSetNumber.get(i) ? "1" : "0");
        }

        for (int i = 0; i < size; i++) {
            bitSetResult.set(i, bitSetNumber.get(i));
        }

        System.out.print(" id with define size-> ");
        for (int i = size - 1; i >= 0; i--) {
            System.out.print(bitSetResult.get(i) ? "1" : "0");
        }
        System.out.println("\n");
        return bitSetResult;
    }
}
