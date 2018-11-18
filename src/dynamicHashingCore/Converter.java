/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamicHashingCore;

import java.nio.ByteBuffer;
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
public class Converter {

    public static byte[] integerToByteArray(int value) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(CommonConstants.SIZE_BY_BYTE_CONVERTOR_INTEGER);
        byteBuffer.putInt(value);
        return byteBuffer.array();
    }
}
