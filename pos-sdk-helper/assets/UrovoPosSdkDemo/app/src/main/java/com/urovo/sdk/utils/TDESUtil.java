package com.urovo.sdk.utils;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class TDESUtil {

    private static final String TAG = "TDESUtil===>";

    private static final String TRANSFOMATION_ECB_DES = "DES/ECB/NoPadding";
    private static final String TRANSFOMATION_ECB_TDES = "DESede/ECB/NoPadding";
    private static final String TRANSFOMATION_CBC_DES = "DES/CBC/NoPadding";
    private static final String TRANSFOMATION_CBC_TDES = "DESede/CBC/NoPadding";
    private static final String ALGORITHM_DES = "DES";
    private static final String ALGORITHM_TDES = "DESede";
    private static final int BLOCK_SIZE = 8; // DES 块大小是 8 字节

    public static byte[] encrypt_ECB(byte[] keyValue, byte[] data) {
        return doEcbCrypto(keyValue, data, Cipher.ENCRYPT_MODE);
    }

    public static byte[] decrypt_ECB(byte[] keyValue, byte[] data) {
        return doEcbCrypto(keyValue, data, Cipher.DECRYPT_MODE);
    }

    public static byte[] encrypt_CBC(byte[] keyValue, byte[] data, byte[] iv) {
        return doCbcCrypto(keyValue, data, iv, Cipher.ENCRYPT_MODE);
    }

    public static byte[] decrypt_CBC(byte[] keyValue, byte[] data, byte[] iv) {
        return doCbcCrypto(keyValue, data, iv, Cipher.DECRYPT_MODE);
    }

    /**
     * 统一ECB处理入口。
     * Why: 消除 DES/TDES 与加解密四条分支的重复代码，降低维护成本。
     */
    private static byte[] doEcbCrypto(byte[] keyValue, byte[] data, int mode) {
        if (keyValue == null) {
            throw new IllegalArgumentException("ECB key must not be null");
        }
        if (data == null) {
            throw new IllegalArgumentException("ECB data must not be null");
        }
        try {
            byte[] finalData = padZeroToBlock(data, BLOCK_SIZE);
            if (keyValue.length == 8) {
                Cipher cipher = Cipher.getInstance(TRANSFOMATION_ECB_DES);
                SecretKey secretKey = new SecretKeySpec(keyValue, ALGORITHM_DES);
                cipher.init(mode, secretKey);
                return cipher.doFinal(finalData);
            }

            byte[] finalKey = normalizeTdesKey(keyValue);
            Cipher cipher = Cipher.getInstance(TRANSFOMATION_ECB_TDES);
            SecretKey secretKey = new SecretKeySpec(finalKey, ALGORITHM_TDES);
            cipher.init(mode, secretKey);
            return cipher.doFinal(finalData);
        } catch (Exception e) {
            // 异常路径: 统一fail-fast，避免返回null掩盖加解密失败原因。
            throw new IllegalStateException("TDES ECB crypto failed", e);
        }
    }

    /**
     * 统一CBC处理入口。
     * Why: 统一IV与数据补齐校验，保证输入不足块长时自动按0x00补齐。
     */
    private static byte[] doCbcCrypto(byte[] keyValue, byte[] data, byte[] iv, int mode) {
        if (keyValue == null) {
            throw new IllegalArgumentException("CBC key must not be null");
        }
        if (data == null) {
            throw new IllegalArgumentException("CBC data must not be null");
        }
        if (iv == null || iv.length != BLOCK_SIZE) {
            throw new IllegalArgumentException("CBC IV must be 8 bytes");
        }
        try {
            byte[] finalData = padZeroToBlock(data, BLOCK_SIZE);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            if (keyValue.length == 8) {
                Cipher cipher = Cipher.getInstance(TRANSFOMATION_CBC_DES);
                SecretKey secretKey = new SecretKeySpec(keyValue, ALGORITHM_DES);
                cipher.init(mode, secretKey, ivSpec);
                return cipher.doFinal(finalData);
            }

            byte[] finalKey = normalizeTdesKey(keyValue);
            Cipher cipher = Cipher.getInstance(TRANSFOMATION_CBC_TDES);
            SecretKey secretKey = new SecretKeySpec(finalKey, ALGORITHM_TDES);
            cipher.init(mode, secretKey, ivSpec);
            return cipher.doFinal(finalData);
        } catch (Exception e) {
            // 异常路径: 统一fail-fast，避免返回null导致调用方误判为“空结果”。
            throw new IllegalStateException("TDES CBC crypto failed", e);
        }
    }

    /**
     * 归一化TDES密钥长度到24字节。
     * 边界规则: 支持16字节(双长)和24字节(三长)；8字节由DES路径独立处理。
     */
    private static byte[] normalizeTdesKey(byte[] keyValue) {
        if (keyValue == null) {
            throw new IllegalArgumentException("TDES key must not be null");
        }
        if (keyValue.length == 16) {
            byte[] finalKey = new byte[24];
            System.arraycopy(keyValue, 0, finalKey, 0, 16);
            System.arraycopy(keyValue, 0, finalKey, 16, 8);
            return finalKey;
        }
        if (keyValue.length == 24) {
            return Arrays.copyOf(keyValue, 24);
        }
        throw new IllegalArgumentException("TDES key must be 16 or 24 bytes");
    }

    /**
     * 按块大小自动补0x00；已对齐时返回副本。
     * Why: 满足“入参长度不足自动补齐0x00”的兼容要求。
     */
    private static byte[] padZeroToBlock(byte[] data, int blockSize) {
        if (blockSize <= 0) {
            throw new IllegalArgumentException("Block size must be positive");
        }
        int len = data.length;
        int remainder = len % blockSize;
        if (remainder == 0) {
            return Arrays.copyOf(data, len);
        }
        int paddedLen = len + (blockSize - remainder);
        byte[] out = new byte[paddedLen];
        System.arraycopy(data, 0, out, 0, len);
        return out;
    }

    /**
     * 手动实现 TDES-CMAC
     *
     * @param key     24 字节（3-key TDES）
     * @param message 原始数据
     * @return CMAC（8 字节）
     */
    public static byte[] generateCMAC(byte[] key, byte[] message) throws Exception {
        // 1. 生成子密钥 K1, K2
        byte[] k1 = new byte[BLOCK_SIZE];
        byte[] k2 = new byte[BLOCK_SIZE];
        generateSubKey(key, k1, k2);

        // 2. 填充消息（PKCS1）
        byte[] paddedMsg = padMessage(message);

        // 3. 对最后一个块应用 K1/K2
        int lastBlockStart = paddedMsg.length - BLOCK_SIZE;
        byte[] lastBlock = new byte[BLOCK_SIZE];
        System.arraycopy(paddedMsg, lastBlockStart, lastBlock, 0, BLOCK_SIZE);

        if ((message.length % BLOCK_SIZE) == 0) {
            // 如果不是最后一个块，用 K1
            lastBlock = xorBytes(lastBlock, k1);
        } else {
            // 如果是最后一个块，用 K2
            lastBlock = xorBytes(lastBlock, k2);
        }
        // 4. 计算 CBC-MAC
        byte[] mac = new byte[BLOCK_SIZE];
        for (int i = 0; i < paddedMsg.length; i += BLOCK_SIZE) {
            byte[] block = new byte[BLOCK_SIZE];
            System.arraycopy(paddedMsg, i, block, 0, BLOCK_SIZE);
            if (i + BLOCK_SIZE >= paddedMsg.length) {
                mac = encrypt_ECB(key, xorBytes(mac, lastBlock));
            } else {
                mac = encrypt_ECB(key, xorBytes(mac, block));
            }
        }
        return mac;
    }

    public static void generateSubKey(byte[] key, byte[] subkey_1, byte[] subkey_2) {
        byte[] zeros = new byte[BLOCK_SIZE];
        byte[] L = new byte[BLOCK_SIZE];
        Arrays.fill(zeros, (byte) 0);
        Arrays.fill(L, (byte) 0);
        Arrays.fill(subkey_1, (byte) 0);
        Arrays.fill(subkey_2, (byte) 0);

        zeros = encrypt_ECB(key, zeros);

        System.arraycopy(zeros, 0, L, 0, BLOCK_SIZE);

        left_shift(subkey_1, L);
        if ((L[0] & 0x80) != 0)
            subkey_1[(BLOCK_SIZE - 1)] ^= 0x1B;//AES use 0x87

        left_shift(subkey_2, subkey_1);
        if ((subkey_1[0] & 0x80) != 0)
            subkey_2[(BLOCK_SIZE - 1)] ^= 0x1B;
    }

    public static void left_shift(byte[] dest, byte[] src) {
        int i;
        byte overflow = 0x00;

        for (i = (BLOCK_SIZE - 1); i >= 0; i--) {
            dest[i] = (byte) (src[i] << 1);
            dest[i] |= overflow;
            overflow = (byte) ((src[i] >> 7) & 1);
        }
    }

    private static byte[] padMessage(byte[] message) {
        if (message.length % BLOCK_SIZE == 0) {
            return Arrays.copyOf(message, message.length);
        }
        int padLen = BLOCK_SIZE - (message.length % BLOCK_SIZE);
        byte[] padded = new byte[message.length + padLen];
        System.arraycopy(message, 0, padded, 0, message.length);
        for (int i = message.length; i < padded.length; i++) {
            if (i == message.length) {
                padded[message.length] = (byte) 0x80;
            } else {
                padded[i] = (byte) 0x00;
            }
        }
        return padded;
    }

    private static byte[] xorBytes(byte[] a, byte[] b) {
        byte[] result = new byte[BLOCK_SIZE];
        for (int i = 0; i < BLOCK_SIZE; i++) {
            result[i] = (byte) (a[i] ^ b[i]);
        }
        return result;
    }

    /**
     * 软算法 ANSI X9.19(919) MAC:
     * 1) 用左半密钥K1做DES-CBC(IV=0)迭代
     * 2) 最后结果先用K2解密，再用K1加密，得到8字节MAC
     * Why: 与常见银联/收单场景的“3DES 919 MAC”口径保持一致。
     *
     * @param key16 双长密钥，固定16字节(K1|K2)
     * @param data  待计算MAC的数据，按0x00补齐到8字节倍数
     * @return 8字节MAC
     */
    public static byte[] generate919Mac(byte[] key16, byte[] data) {
        if (key16 == null) {
            throw new IllegalArgumentException("919 MAC key must not be null");
        }
        if (data == null) {
            throw new IllegalArgumentException("919 MAC data must not be null");
        }
        if (key16.length != 16) {
            // Boundary check: X9.19 retail MAC使用双长密钥(K1|K2)。
            throw new IllegalArgumentException("919 MAC key must be 16 bytes (K1|K2)");
        }
        try {
            byte[] k1 = Arrays.copyOfRange(key16, 0, 8);
            byte[] k2 = Arrays.copyOfRange(key16, 8, 16);
            byte[] padded = padZeroToBlock(data, BLOCK_SIZE);
            byte[] macState = new byte[8]; // IV = 0x0000000000000000

            // 关键流程：DES-CBC(K1)
            for (int i = 0; i < padded.length; i += 8) {
                byte[] block = Arrays.copyOfRange(padded, i, i + 8);
                macState = desEcbNoPadding(k1, xorBytes(macState, block), Cipher.ENCRYPT_MODE);
            }

            // 结束变换：D(K2) -> E(K1)
            byte[] step2 = desEcbNoPadding(k2, macState, Cipher.DECRYPT_MODE);
            byte[] mac = desEcbNoPadding(k1, step2, Cipher.ENCRYPT_MODE);
            System.out.println(TAG + "generate919Mac mac:" + BytesUtil.bytes2HexString(mac));
            return mac;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate 919 MAC", e);
        }
    }

    /**
     * 单块DES ECB NoPadding，供919 MAC内部流程使用。
     */
    private static byte[] desEcbNoPadding(byte[] key8, byte[] block8, int mode) throws Exception {
        if (key8 == null || key8.length != 8) {
            throw new IllegalArgumentException("DES key must be 8 bytes");
        }
        if (block8 == null || block8.length != 8) {
            throw new IllegalArgumentException("DES block must be 8 bytes");
        }
        Cipher cipher = Cipher.getInstance(TRANSFOMATION_ECB_DES);
        SecretKey secretKey = new SecretKeySpec(key8, ALGORITHM_DES);
        cipher.init(mode, secretKey);
        return cipher.doFinal(block8);
    }

}
