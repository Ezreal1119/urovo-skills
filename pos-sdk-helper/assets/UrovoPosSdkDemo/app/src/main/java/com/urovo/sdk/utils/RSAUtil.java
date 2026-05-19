package com.urovo.sdk.utils;

import android.device.SEManager;
import android.os.Bundle;
import android.util.Log;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class RSAUtil {

    private static final String TAG = "RSAUtil===>";

    /**
     * RSA算法
     */
    public static final String RSA = "RSA";
    /**
     * 加密方式，标准jdk的
     */

    /**
     * 签名
     *
     * @param content    明文
     * @param privateKey 私钥
     * @return 签名后的密文
     * @throws Exception e
     */
    public static byte[] signWithPrivateKey(PrivateKey privateKey, String algorithm, byte[] content) throws Exception {
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(content);
        return signature.sign();
    }

    /**
     * 公钥验签
     *
     * @param publicKey
     * @param algorithm
     * @param sourceData
     * @param signatureData
     * @return
     * @throws Exception
     */
    public static boolean verifySignatureWithPublicKey(PublicKey publicKey, String algorithm, byte[] signatureData, byte[] sourceData) throws Exception {
        Log.e(TAG, "verifySignatureWithPublicKey: algorithm=" + algorithm
                + ",\nsourceData:" + BytesUtil.bytes2HexString(sourceData)
                + ",\nsignatureData:" + BytesUtil.bytes2HexString(signatureData));
        Signature signature = Signature.getInstance(algorithm);
        signature.initVerify(publicKey);
        signature.update(sourceData);
        return signature.verify(signatureData);
    }

    /**
     * 使用私钥加密
     */
    public static byte[] encryptWithPrivateKey(PrivateKey privateKey, String transformation, byte[] sourceData) throws Exception {
        Log.e(TAG, "decryptByPrivateKey: transformation=" + transformation
                + ",\nsourceData:" + BytesUtil.bytes2HexString(sourceData));
        // 解密数据
        Cipher cp = Cipher.getInstance(transformation);
        cp.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] arr = cp.doFinal(sourceData);
        return arr;
    }

    /**
     * 使用私钥解密
     */
    public static byte[] decryptWithPrivateKey(PrivateKey privateKey, String transformation, byte[] encryptionData) throws Exception {
        Log.e(TAG, "decryptByPrivateKey: transformation=" + transformation
                + ",\nencryptionData:" + BytesUtil.bytes2HexString(encryptionData));
        // 解密数据
        Cipher cp = Cipher.getInstance(transformation);
        cp.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] arr = cp.doFinal(encryptionData);
        return arr;
    }

    /**
     * 使用公钥加密
     */
    public static byte[] encryptWithPublicKey(PublicKey publicKey, String transformation, byte[] sourceData) throws Exception {
        Log.e(TAG, "encryptWithPublicKey: transformation=" + transformation
                + ",\nsourceData:" + BytesUtil.bytes2HexString(sourceData));
        // 加密数据
        Cipher cp = Cipher.getInstance(transformation);
        cp.init(Cipher.ENCRYPT_MODE, publicKey);
        return cp.doFinal(sourceData);
    }

    /**
     * 使用公钥解密
     */
    public static byte[] decryptWithPublicKey(PublicKey publicKey, String transformation, byte[] encryptionData) throws Exception {
        Log.e(TAG, "decryptWithPublicKey: transformation=" + transformation
                + ",\nencryptionData:" + BytesUtil.bytes2HexString(encryptionData));
        // 加密数据
        Cipher cp = Cipher.getInstance(transformation);
        cp.init(Cipher.DECRYPT_MODE, publicKey);
        return cp.doFinal(encryptionData);
    }

    /**
     * Add PKCS#1 v1.5 padding to data before RSA encryption
     * Format: 0x00 || [0x00|0x01|0x02] || PS || 0x00 || M
     *
     * @param message The original message data
     * @param keySize The RSA key size in bytes (e.g., 256 for 2048-bit RSA)
     * @return The padded data ready for RSA encryption, or null if message is too long
     */
    public static byte[] addPKCS1Padding(PADDING_TYPE type, byte[] message, int keySize) {
        if (message == null) {
            return null;
        }

        byte[] dataToSign = message;

        // For signature type, add DigestInfo structure for SHA256
        if (type == PADDING_TYPE.SIGNATURE && message.length == 32) { // SHA256 hash length
            // Create DigestInfo structure for SHA256
            // DigestInfo ::= SEQUENCE {
            //     digestAlgorithm AlgorithmIdentifier,
            //     digest OCTET STRING
            // }
            byte[] sha256DigestInfo = {
                    0x30, 0x31, // SEQUENCE, length 49
                    0x30, 0x0d, // SEQUENCE, length 13
                    0x06, 0x09, // OBJECT IDENTIFIER, length 9
                    0x60, (byte) 0x86, 0x48, 0x01, 0x65, 0x03, 0x04, 0x02, 0x01, // SHA256 OID
                    0x05, 0x00, // NULL
                    0x04, 0x20  // OCTET STRING, length 32
            };

            // Combine DigestInfo + hash
            dataToSign = new byte[sha256DigestInfo.length + message.length];
            System.arraycopy(sha256DigestInfo, 0, dataToSign, 0, sha256DigestInfo.length);
            System.arraycopy(message, 0, dataToSign, sha256DigestInfo.length, message.length);

            Log.e(TAG, "Added DigestInfo for SHA256 signature, total length: " + dataToSign.length);
        }

        // Check if message length is valid
        // Format: 0x00(1) + 0x01(1) + PS(>=8) + 0x00(1) + DigestInfo + Hash
        // So minimum overhead is 11 bytes
        if (dataToSign.length > keySize - 11) {
            Log.e(TAG, "Message too long for PKCS#1 padding: " + dataToSign.length + " bytes (max: " + (keySize - 11) + ")");
            return message;
        }

        byte[] paddedData = new byte[keySize];
        // Set header bytes
        paddedData[0] = 0x00;  // Start byte
        paddedData[1] = type.getBt();  // Block type for signature
        // Calculate padding length
        int paddingLength = keySize - dataToSign.length - 3; // 3 = start + block type + separator
        // Fill padding section based on padding type
        for (int i = 2; i < 2 + paddingLength; i++) {
            byte paddingByte;
            switch (type) {
                case SIGNATURE:
                    paddingByte = (byte) 0xFF;
                    break;
                case CACULATE_PRI:
                    paddingByte = (byte) 0x00;
                    break;
                default:
                    // Generate random non-zero byte (avoid 0x00)
                    do {
                        paddingByte = (byte) (1 + (Math.random() * 255));
                    } while (paddingByte == 0);
                    break;
            }
            paddedData[i] = paddingByte;
        }

        // Add separator
        paddedData[2 + paddingLength] = 0x00;

        // Add DigestInfo + hash
        System.arraycopy(dataToSign, 0, paddedData, 2 + paddingLength + 1, dataToSign.length);

        Log.e(TAG, "PKCS#1 padding added successfully: data=" + dataToSign.length + " bytes, padding=" + paddingLength + " bytes, total=" + keySize + " bytes");
        Log.e(TAG, "After PKCS#1 padding:" + BytesUtil.bytes2HexString(paddedData));
        return paddedData;
    }

    /**
     * Remove PKCS#1 v1.5 padding from decrypted RSA data
     * Format: 0x00 || [0x00|0x01|0x02] || PS || 0x00 || M
     *
     * @param paddedData The data with PKCS#1 padding
     * @return The original message data without padding, or null if padding is invalid
     */
    public static byte[] removePKCS1Padding(byte[] paddedData) {
        if (paddedData == null || paddedData.length < 11) {
            // Minimum length: 0x00 + 0x02 + 8 bytes PS + 0x00 + 1 byte message = 11 bytes
            return paddedData;
        }

        byte blockType = paddedData[1];
        boolean typeOK = blockType == 0x00 || blockType == 0x01 || blockType == 0x02;

        if (paddedData[0] != 0x00 || !typeOK) {
            Log.e(TAG, "Invalid PKCS#1 padding: incorrect header bytes");
            return paddedData;
        }

        int separatorIndex = -1;

        if (blockType == 0x00) {
            // For block type 0x00, padding bytes are also 0x00
            // Find first non-zero byte which indicates start of message
            for (int i = 10; i < paddedData.length; i++) {
                if (paddedData[i] != 0x00) {
                    // Message starts at this position, separator is at i-1
                    separatorIndex = i - 1;
                    break;
                }
            }
        } else {
            // For block type 0x01 (signature) and 0x02 (encryption)
            // Find the separator (0x00) after padding bytes
            for (int i = 2; i < paddedData.length; i++) {
                if (paddedData[i] == 0x00) {
                    separatorIndex = i;
                    break;
                }
            }
        }

        if (separatorIndex < 10) {
            Log.e(TAG, "Invalid PKCS#1 padding: separator not found or insufficient padding");
            return null;
        }

        int messageLength = paddedData.length - separatorIndex - 1;
        if (messageLength <= 0) {
            Log.e(TAG, "Invalid PKCS#1 padding: no message data found");
            return null;
        }

        byte[] message = new byte[messageLength];
        System.arraycopy(paddedData, separatorIndex + 1, message, 0, messageLength);

        Log.e(TAG, "PKCS#1 padding removed successfully, original message length: " + messageLength);
        return message;
    }

    public enum PADDING_TYPE {
        CACULATE_PRI((byte) 0x00),
        SIGNATURE((byte) 0x01),
        CACULATE_PUB((byte) 0x02);

        private byte bt;

        PADDING_TYPE(byte value) {
            this.bt = value;
        }

        public byte getBt() {
            return bt;
        }
    }

}
