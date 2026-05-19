package com.urovo.sdk.utils;

import java.util.Arrays;

public class AESUtil {

    // AES参数
    private static final int AES_BLOCK_SIZE = 16;
    private static final int AES128_KEY_SIZE = 16;
    private static final int AES192_KEY_SIZE = 24;
    private static final int AES256_KEY_SIZE = 32;

    // S盒和逆S盒
    private static final byte[] S_BOX = {
            0x63, 0x7c, 0x77, 0x7b, (byte) 0xf2, 0x6b, 0x6f, (byte) 0xc5, 0x30, 0x01, 0x67, 0x2b, (byte) 0xfe, (byte) 0xd7, (byte) 0xab, 0x76,
            (byte) 0xca, (byte) 0x82, (byte) 0xc9, 0x7d, (byte) 0xfa, 0x59, 0x47, (byte) 0xf0, (byte) 0xad, (byte) 0xd4, (byte) 0xa2, (byte) 0xaf, (byte) 0x9c, (byte) 0xa4, 0x72, (byte) 0xc0,
            (byte) 0xb7, (byte) 0xfd, (byte) 0x93, 0x26, 0x36, 0x3f, (byte) 0xf7, (byte) 0xcc, 0x34, (byte) 0xa5, (byte) 0xe5, (byte) 0xf1, 0x71, (byte) 0xd8, 0x31, 0x15,
            0x04, (byte) 0xc7, 0x23, (byte) 0xc3, 0x18, (byte) 0x96, 0x05, (byte) 0x9a, 0x07, 0x12, (byte) 0x80, (byte) 0xe2, (byte) 0xeb, 0x27, (byte) 0xb2, 0x75,
            0x09, (byte) 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, (byte) 0xa0, 0x52, 0x3b, (byte) 0xd6, (byte) 0xb3, 0x29, (byte) 0xe3, 0x2f, (byte) 0x84,
            0x53, (byte) 0xd1, 0x00, (byte) 0xed, 0x20, (byte) 0xfc, (byte) 0xb1, 0x5b, 0x6a, (byte) 0xcb, (byte) 0xbe, 0x39, 0x4a, 0x4c, 0x58, (byte) 0xcf,
            (byte) 0xd0, (byte) 0xef, (byte) 0xaa, (byte) 0xfb, 0x43, 0x4d, 0x33, (byte) 0x85, 0x45, (byte) 0xf9, 0x02, 0x7f, 0x50, 0x3c, (byte) 0x9f, (byte) 0xa8,
            0x51, (byte) 0xa3, 0x40, (byte) 0x8f, (byte) 0x92, (byte) 0x9d, 0x38, (byte) 0xf5, (byte) 0xbc, (byte) 0xb6, (byte) 0xda, 0x21, 0x10, (byte) 0xff, (byte) 0xf3, (byte) 0xd2,
            (byte) 0xcd, 0x0c, 0x13, (byte) 0xec, 0x5f, (byte) 0x97, 0x44, 0x17, (byte) 0xc4, (byte) 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73,
            0x60, (byte) 0x81, 0x4f, (byte) 0xdc, 0x22, 0x2a, (byte) 0x90, (byte) 0x88, 0x46, (byte) 0xee, (byte) 0xb8, 0x14, (byte) 0xde, 0x5e, 0x0b, (byte) 0xdb,
            (byte) 0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, (byte) 0xc2, (byte) 0xd3, (byte) 0xac, 0x62, (byte) 0x91, (byte) 0x95, (byte) 0xe4, 0x79,
            (byte) 0xe7, (byte) 0xc8, 0x37, 0x6d, (byte) 0x8d, (byte) 0xd5, 0x4e, (byte) 0xa9, 0x6c, 0x56, (byte) 0xf4, (byte) 0xea, 0x65, 0x7a, (byte) 0xae, 0x08,
            (byte) 0xba, 0x78, 0x25, 0x2e, 0x1c, (byte) 0xa6, (byte) 0xb4, (byte) 0xc6, (byte) 0xe8, (byte) 0xdd, 0x74, 0x1f, 0x4b, (byte) 0xbd, (byte) 0x8b, (byte) 0x8a,
            0x70, 0x3e, (byte) 0xb5, 0x66, 0x48, 0x03, (byte) 0xf6, 0x0e, 0x61, 0x35, 0x57, (byte) 0xb9, (byte) 0x86, (byte) 0xc1, 0x1d, (byte) 0x9e,
            (byte) 0xe1, (byte) 0xf8, (byte) 0x98, 0x11, 0x69, (byte) 0xd9, (byte) 0x8e, (byte) 0x94, (byte) 0x9b, 0x1e, (byte) 0x87, (byte) 0xe9, (byte) 0xce, 0x55, 0x28, (byte) 0xdf,
            (byte) 0x8c, (byte) 0xa1, (byte) 0x89, 0x0d, (byte) 0xbf, (byte) 0xe6, 0x42, 0x68, 0x41, (byte) 0x99, 0x2d, 0x0f, (byte) 0xb0, 0x54, (byte) 0xbb, 0x16
    };

    private static final byte[] INV_S_BOX = {
            0x52, 0x09, 0x6a, (byte) 0xd5, 0x30, 0x36, (byte) 0xa5, 0x38, (byte) 0xbf, 0x40, (byte) 0xa3, (byte) 0x9e, (byte) 0x81, (byte) 0xf3, (byte) 0xd7, (byte) 0xfb,
            0x7c, (byte) 0xe3, 0x39, (byte) 0x82, (byte) 0x9b, 0x2f, (byte) 0xff, (byte) 0x87, 0x34, (byte) 0x8e, 0x43, 0x44, (byte) 0xc4, (byte) 0xde, (byte) 0xe9, (byte) 0xcb,
            0x54, 0x7b, (byte) 0x94, 0x32, (byte) 0xa6, (byte) 0xc2, 0x23, 0x3d, (byte) 0xee, 0x4c, (byte) 0x95, 0x0b, 0x42, (byte) 0xfa, (byte) 0xc3, 0x4e,
            0x08, 0x2e, (byte) 0xa1, 0x66, 0x28, (byte) 0xd9, 0x24, (byte) 0xb2, 0x76, 0x5b, (byte) 0xa2, 0x49, 0x6d, (byte) 0x8b, (byte) 0xd1, 0x25,
            0x72, (byte) 0xf8, (byte) 0xf6, 0x64, (byte) 0x86, 0x68, (byte) 0x98, 0x16, (byte) 0xd4, (byte) 0xa4, 0x5c, (byte) 0xcc, 0x5d, 0x65, (byte) 0xb6, (byte) 0x92,
            0x6c, 0x70, 0x48, 0x50, (byte) 0xfd, (byte) 0xed, (byte) 0xb9, (byte) 0xda, 0x5e, 0x15, 0x46, 0x57, (byte) 0xa7, (byte) 0x8d, (byte) 0x9d, (byte) 0x84,
            (byte) 0x90, (byte) 0xd8, (byte) 0xab, 0x00, (byte) 0x8c, (byte) 0xbc, (byte) 0xd3, 0x0a, (byte) 0xf7, (byte) 0xe4, 0x58, 0x05, (byte) 0xb8, (byte) 0xb3, 0x45, 0x06,
            (byte) 0xd0, 0x2c, 0x1e, (byte) 0x8f, (byte) 0xca, 0x3f, 0x0f, 0x02, (byte) 0xc1, (byte) 0xaf, (byte) 0xbd, 0x03, 0x01, 0x13, (byte) 0x8a, 0x6b,
            0x3a, (byte) 0x91, 0x11, 0x41, 0x4f, 0x67, (byte) 0xdc, (byte) 0xea, (byte) 0x97, (byte) 0xf2, (byte) 0xcf, (byte) 0xce, (byte) 0xf0, (byte) 0xb4, (byte) 0xe6, 0x73,
            (byte) 0x96, (byte) 0xac, 0x74, 0x22, (byte) 0xe7, (byte) 0xad, 0x35, (byte) 0x85, (byte) 0xe2, (byte) 0xf9, 0x37, (byte) 0xe8, 0x1c, 0x75, (byte) 0xdf, 0x6e,
            0x47, (byte) 0xf1, 0x1a, 0x71, 0x1d, 0x29, (byte) 0xc5, (byte) 0x89, 0x6f, (byte) 0xb7, 0x62, 0x0e, (byte) 0xaa, 0x18, (byte) 0xbe, 0x1b,
            (byte) 0xfc, 0x56, 0x3e, 0x4b, (byte) 0xc6, (byte) 0xd2, 0x79, 0x20, (byte) 0x9a, (byte) 0xdb, (byte) 0xc0, (byte) 0xfe, 0x78, (byte) 0xcd, 0x5a, (byte) 0xf4,
            0x1f, (byte) 0xdd, (byte) 0xa8, 0x33, (byte) 0x88, 0x07, (byte) 0xc7, 0x31, (byte) 0xb1, 0x12, 0x10, 0x59, 0x27, (byte) 0x80, (byte) 0xec, 0x5f,
            0x60, 0x51, 0x7f, (byte) 0xa9, 0x19, (byte) 0xb5, 0x4a, 0x0d, 0x2d, (byte) 0xe5, 0x7a, (byte) 0x9f, (byte) 0x93, (byte) 0xc9, (byte) 0x9c, (byte) 0xef,
            (byte) 0xa0, (byte) 0xe0, 0x3b, 0x4d, (byte) 0xae, 0x2a, (byte) 0xf5, (byte) 0xb0, (byte) 0xc8, (byte) 0xeb, (byte) 0xbb, 0x3c, (byte) 0x83, 0x53, (byte) 0x99, 0x61,
            0x17, 0x2b, 0x04, 0x7e, (byte) 0xba, 0x77, (byte) 0xd6, 0x26, (byte) 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d
    };

    // AES内部状态
    private static int Nb = 4;  // 固定4列
    private static int Nk;      // 密钥字数
    private static int Nr;      // 轮数

    // 初始化AES参数
    private static void aesInit(int keySize) {
        switch (keySize) {
            case 16:
                Nk = 4;
                Nr = 10;
                break;
            case 24:
                Nk = 6;
                Nr = 12;
                break;
            case 32:
                Nk = 8;
                Nr = 14;
                break;
            default:
                throw new IllegalArgumentException("Invalid key size: " + keySize);
        }
    }

    // GF(2^8)加法
    private static byte gadd(byte a, byte b) {
        return (byte) (a ^ b);
    }

    // GF(2^8)乘法
    private static byte gmult(byte a, byte b) {
        byte p = 0;
        byte hbs;
        for (int i = 0; i < 8; i++) {
            if ((b & 1) != 0) {
                p ^= a;
            }
            hbs = (byte) (a & (byte) 0x80);
            a = (byte) ((a & 0xFF) << 1);
            if (hbs != 0) {
                a ^= 0x1b; // x^8 + x^4 + x^3 + x + 1
            }
            b = (byte) ((b & 0xFF) >>> 1);
        }
        return p;
    }

    // 4字节字加法
    private static void coefAdd(byte[] a, byte[] b, byte[] d) {
        d[0] = gadd(a[0], b[0]);
        d[1] = gadd(a[1], b[1]);
        d[2] = gadd(a[2], b[2]);
        d[3] = gadd(a[3], b[3]);
    }

    // 4字节字乘法
    private static void coefMult(byte[] a, byte[] b, byte[] d) {
        d[0] = gmult(a[0], b[0]);
        d[0] = gadd(d[0], gmult(a[3], b[1]));
        d[0] = gadd(d[0], gmult(a[2], b[2]));
        d[0] = gadd(d[0], gmult(a[1], b[3]));

        d[1] = gmult(a[1], b[0]);
        d[1] = gadd(d[1], gmult(a[0], b[1]));
        d[1] = gadd(d[1], gmult(a[3], b[2]));
        d[1] = gadd(d[1], gmult(a[2], b[3]));

        d[2] = gmult(a[2], b[0]);
        d[2] = gadd(d[2], gmult(a[1], b[1]));
        d[2] = gadd(d[2], gmult(a[0], b[2]));
        d[2] = gadd(d[2], gmult(a[3], b[3]));

        d[3] = gmult(a[3], b[0]);
        d[3] = gadd(d[3], gmult(a[2], b[1]));
        d[3] = gadd(d[3], gmult(a[1], b[2]));
        d[3] = gadd(d[3], gmult(a[0], b[3]));
    }

    // 轮常数生成
    private static byte[] Rcon(int i) {
        byte[] R = new byte[4];
        if (i == 1) {
            R[0] = 0x01;
        } else if (i > 1) {
            R[0] = 0x02;
            i--;
            while (i > 1) {
                R[0] = gmult(R[0], (byte) 0x02);
                i--;
            }
        }
        return R;
    }

    // 轮密钥加
    private static void addRoundKey(byte[] state, byte[] w, int round) {
        for (int c = 0; c < Nb; c++) {
            state[Nb * 0 + c] ^= w[4 * Nb * round + 4 * c + 0];
            state[Nb * 1 + c] ^= w[4 * Nb * round + 4 * c + 1];
            state[Nb * 2 + c] ^= w[4 * Nb * round + 4 * c + 2];
            state[Nb * 3 + c] ^= w[4 * Nb * round + 4 * c + 3];
        }
    }

    // 列混合
    private static void mixColumns(byte[] state) {
        byte[] a = {0x02, 0x01, 0x01, 0x03};
        byte[] col = new byte[4];
        byte[] res = new byte[4];

        for (int j = 0; j < Nb; j++) {
            for (int i = 0; i < 4; i++) {
                col[i] = state[Nb * i + j];
            }

            coefMult(a, col, res);

            for (int i = 0; i < 4; i++) {
                state[Nb * i + j] = res[i];
            }
        }
    }

    // 逆行移位
    private static void invShiftRows(byte[] state) {
        for (int i = 1; i < 4; i++) {
            for (int s = 0; s < i; s++) {
                byte tmp = state[Nb * i + Nb - 1];
                for (int k = Nb - 1; k > 0; k--) {
                    state[Nb * i + k] = state[Nb * i + k - 1];
                }
                state[Nb * i + 0] = tmp;
            }
        }
    }

    // 行移位
    private static void shiftRows(byte[] state) {
        for (int i = 1; i < 4; i++) {
            for (int s = 0; s < i; s++) {
                byte tmp = state[Nb * i + 0];
                for (int k = 0; k < Nb - 1; k++) {
                    state[Nb * i + k] = state[Nb * i + k + 1];
                }
                state[Nb * i + Nb - 1] = tmp;
            }
        }
    }

    // 逆字节替换
    private static void invSubBytes(byte[] state) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < Nb; j++) {
                int index = state[Nb * i + j] & 0xFF;
                state[Nb * i + j] = INV_S_BOX[index];
            }
        }
    }

    // 字节替换
    private static void subBytes(byte[] state) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < Nb; j++) {
                int index = state[Nb * i + j] & 0xFF;
                state[Nb * i + j] = S_BOX[index];
            }
        }
    }

    // 逆列混合
    private static void invMixColumns(byte[] state) {
        byte[] a = {0x0e, 0x09, 0x0d, 0x0b};
        byte[] col = new byte[4];
        byte[] res = new byte[4];

        for (int j = 0; j < Nb; j++) {
            for (int i = 0; i < 4; i++) {
                col[i] = state[Nb * i + j];
            }

            coefMult(a, col, res);

            for (int i = 0; i < 4; i++) {
                state[Nb * i + j] = res[i];
            }
        }
    }

    // 字节替换（用于密钥扩展）
    private static void subWord(byte[] w) {
        for (int i = 0; i < 4; i++) {
            w[i] = S_BOX[w[i] & 0xFF];
        }
    }

    // 字节旋转（用于密钥扩展）
    private static void rotWord(byte[] w) {
        byte tmp = w[0];
        System.arraycopy(w, 1, w, 0, 3);
        w[3] = tmp;
    }

    // 密钥扩展
    public static byte[] aesKeyExpansion(byte[] key) {
        aesInit(key.length);
        int len = Nb * (Nr + 1) * 4;
        byte[] w = new byte[len];

        // 复制初始密钥
        System.arraycopy(key, 0, w, 0, key.length);

        byte[] tmp = new byte[4];
        for (int i = Nk; i < Nb * (Nr + 1); i++) {
            // 前一个字
            tmp[0] = w[4 * (i - 1) + 0];
            tmp[1] = w[4 * (i - 1) + 1];
            tmp[2] = w[4 * (i - 1) + 2];
            tmp[3] = w[4 * (i - 1) + 3];

            if (i % Nk == 0) {
                rotWord(tmp);
                subWord(tmp);
                byte[] rcon = Rcon(i / Nk);
                for (int j = 0; j < 4; j++) {
                    tmp[j] ^= rcon[j];
                }
            } else if (Nk > 6 && i % Nk == 4) {
                subWord(tmp);
            }

            // 计算新字
            int base = 4 * (i - Nk);
            w[4 * i + 0] = (byte) (w[base + 0] ^ tmp[0]);
            w[4 * i + 1] = (byte) (w[base + 1] ^ tmp[1]);
            w[4 * i + 2] = (byte) (w[base + 2] ^ tmp[2]);
            w[4 * i + 3] = (byte) (w[base + 3] ^ tmp[3]);
        }
        return w;
    }

    // AES加密
    public static void aesCipher(byte[] input, byte[] output, byte[] w) {
        byte[] state = new byte[4 * Nb];

        // 状态矩阵初始化
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < Nb; j++) {
                state[Nb * i + j] = input[i + 4 * j];
            }
        }

        addRoundKey(state, w, 0);

        for (int r = 1; r < Nr; r++) {
            subBytes(state);
            shiftRows(state);
            mixColumns(state);
            addRoundKey(state, w, r);
        }

        subBytes(state);
        shiftRows(state);
        addRoundKey(state, w, Nr);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < Nb; j++) {
                output[i + 4 * j] = state[Nb * i + j];
            }
        }
    }

    // AES解密
    public static void aesInvCipher(byte[] input, byte[] output, byte[] w) {
        byte[] state = new byte[4 * Nb];

        // 状态矩阵初始化
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < Nb; j++) {
                state[Nb * i + j] = input[i + 4 * j];
            }
        }

        addRoundKey(state, w, Nr);

        for (int r = Nr - 1; r >= 1; r--) {
            invShiftRows(state);
            invSubBytes(state);
            addRoundKey(state, w, r);
            invMixColumns(state);
        }

        invShiftRows(state);
        invSubBytes(state);
        addRoundKey(state, w, 0);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < Nb; j++) {
                output[i + 4 * j] = state[Nb * i + j];
            }
        }
    }

    // CBC/ECB加密
    public static byte[] cryptoAesEncrypt(byte[] key, byte[] plaintext, byte[] iv) {
        if (plaintext == null) {
            throw new IllegalArgumentException("Plaintext must not be null");
        }
        validateAesKeyLength(key);
        validateIvForCbcIfNeeded(iv);

        // 关键流程意图：统一在入口完成0x00补齐，避免各业务侧重复补位代码。
        byte[] finalPlaintext = padZeroToBlock(plaintext, AES_BLOCK_SIZE);
        byte[] w = aesKeyExpansion(key);
        byte[] ciphertext = new byte[finalPlaintext.length];
        byte[] prev = iv != null ? iv.clone() : new byte[AES_BLOCK_SIZE];
        byte[] block = new byte[AES_BLOCK_SIZE];

        for (int i = 0; i < finalPlaintext.length; i += AES_BLOCK_SIZE) {
            if (iv != null) {
                // CBC模式：先与IV或前一块密文异或
                for (int j = 0; j < AES_BLOCK_SIZE; j++) {
                    block[j] = (byte) (finalPlaintext[i + j] ^ prev[j]);
                }
            } else {
                // ECB模式：直接处理
                System.arraycopy(finalPlaintext, i, block, 0, AES_BLOCK_SIZE);
            }

            // AES加密
            byte[] outBlock = new byte[AES_BLOCK_SIZE];
            aesCipher(block, outBlock, w);

            // 保存密文块
            System.arraycopy(outBlock, 0, ciphertext, i, AES_BLOCK_SIZE);

            if (iv != null) {
                // 更新前一块密文
                System.arraycopy(outBlock, 0, prev, 0, AES_BLOCK_SIZE);
            }
        }
        return ciphertext;
    }

    // CBC/ECB解密
    public static byte[] cryptoAesDecrypt(byte[] key, byte[] ciphertext, byte[] iv) {
        if (ciphertext == null) {
            throw new IllegalArgumentException("Ciphertext must not be null");
        }
        validateAesKeyLength(key);
        validateIvForCbcIfNeeded(iv);

        // 边界处理：当密文长度非16字节倍数时，按需求自动0x00补齐后再参与解密。
        byte[] finalCiphertext = padZeroToBlock(ciphertext, AES_BLOCK_SIZE);
        byte[] w = aesKeyExpansion(key);
        byte[] plaintext = new byte[finalCiphertext.length];
        byte[] prev = iv != null ? iv.clone() : new byte[AES_BLOCK_SIZE];

        for (int i = 0; i < finalCiphertext.length; i += AES_BLOCK_SIZE) {
            // 解密当前块
            byte[] decrypted = new byte[AES_BLOCK_SIZE];
            byte[] inBlock = Arrays.copyOfRange(finalCiphertext, i, i + AES_BLOCK_SIZE);
            aesInvCipher(inBlock, decrypted, w);

            if (iv != null) {
                // CBC模式：与IV或前一块密文异或
                for (int j = 0; j < AES_BLOCK_SIZE; j++) {
                    plaintext[i + j] = (byte) (decrypted[j] ^ prev[j]);
                }
                // 更新前一块密文
                System.arraycopy(inBlock, 0, prev, 0, AES_BLOCK_SIZE);
            } else {
                // ECB模式：直接输出
                System.arraycopy(decrypted, 0, plaintext, i, AES_BLOCK_SIZE);
            }
        }
        return plaintext;
    }

    /**
     * 校验AES密钥长度。
     * Why: 在入口显式fail-fast，避免在密钥扩展阶段抛出不直观异常。
     */
    private static void validateAesKeyLength(byte[] key) {
        if (key == null) {
            throw new IllegalArgumentException("AES key must not be null");
        }
        if (key.length != AES128_KEY_SIZE && key.length != AES192_KEY_SIZE && key.length != AES256_KEY_SIZE) {
            throw new IllegalArgumentException("AES key must be 16/24/32 bytes");
        }
    }

    /**
     * 仅在CBC模式下校验IV。
     * 边界规则: ECB传null，CBC必须传16字节IV。
     */
    private static void validateIvForCbcIfNeeded(byte[] iv) {
        if (iv == null) {
            return;
        }
        if (iv.length != AES_BLOCK_SIZE) {
            throw new IllegalArgumentException("AES CBC IV must be 16 bytes");
        }
    }

    /**
     * 按块大小补0x00到整数倍；已对齐时返回副本。
     * Why: 满足“入参长度不够自动补0x00”的统一要求。
     */
    private static byte[] padZeroToBlock(byte[] data, int blockSize) {
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

    // ECB加密
    public static byte[] encryptECB(byte[] key, byte[] plaintext) {
        return cryptoAesEncrypt(key, plaintext, null);
    }

    // ECB解密
    public static byte[] decryptECB(byte[] key, byte[] ciphertext) {
        return cryptoAesDecrypt(key, ciphertext, null);
    }

    // CBC加密
    public static byte[] encryptCBC(byte[] key, byte[] plaintext, byte[] iv) {
        return cryptoAesEncrypt(key, plaintext, iv);
    }

    // CBC解密
    public static byte[] decryptCBC(byte[] key, byte[] ciphertext, byte[] iv) {
        return cryptoAesDecrypt(key, ciphertext, iv);
    }

    // CMAC计算
    public static byte[] cryptoAesCmac(byte[] key, byte[] data) {
        // 生成子密钥
        byte[] k1 = new byte[AES_BLOCK_SIZE];
        byte[] k2 = new byte[AES_BLOCK_SIZE];
        deriveSubkeys(key, k1, k2);

        // 处理数据
        byte[] iv = new byte[AES_BLOCK_SIZE];
        byte[] lastBlock;
        int blocks = (data.length + AES_BLOCK_SIZE - 1) / AES_BLOCK_SIZE;
        int lastBlockLen = data.length % AES_BLOCK_SIZE;

        if (lastBlockLen == 0 && data.length > 0) {
            lastBlock = Arrays.copyOfRange(data, data.length - AES_BLOCK_SIZE, data.length);
            // 异或K1
            for (int i = 0; i < AES_BLOCK_SIZE; i++) {
                lastBlock[i] ^= k1[i];
            }
        } else {
            lastBlock = new byte[AES_BLOCK_SIZE];
            if (data.length > 0) {
                System.arraycopy(data, (blocks - 1) * AES_BLOCK_SIZE, lastBlock, 0, lastBlockLen);
            }
            // 添加填充
            lastBlock[lastBlockLen] = (byte) 0x80;
            // 异或K2
            for (int i = 0; i < AES_BLOCK_SIZE; i++) {
                lastBlock[i] ^= k2[i];
            }
        }

        // 计算CBC-MAC
        for (int i = 0; i < blocks - 1; i++) {
            byte[] block = Arrays.copyOfRange(data, i * AES_BLOCK_SIZE, (i + 1) * AES_BLOCK_SIZE);
            iv = cryptoAesEncrypt(key, block, iv);
        }
        return cryptoAesEncrypt(key, lastBlock, iv);
    }

    // 生成CMAC子密钥
    private static void deriveSubkeys(byte[] key, byte[] k1, byte[] k2) {
        byte[] zero = new byte[AES_BLOCK_SIZE];
        byte[] l = cryptoAesEncrypt(key, zero, null);

        // 生成k1
        int carry = (l[0] & 0x80) != 0 ? 1 : 0;
        for (int i = 0; i < AES_BLOCK_SIZE; i++) {
            k1[i] = (byte) ((l[i] << 1) | (i < AES_BLOCK_SIZE - 1 ? (l[i + 1] >>> 7 & 1) : 0));
        }
        if (carry != 0) {
            k1[AES_BLOCK_SIZE - 1] ^= 0x87;
        }

        // 生成k2
        carry = (k1[0] & 0x80) != 0 ? 1 : 0;
        for (int i = 0; i < AES_BLOCK_SIZE; i++) {
            k2[i] = (byte) ((k1[i] << 1) | (i < AES_BLOCK_SIZE - 1 ? (k1[i + 1] >>> 7 & 1) : 0));
        }
        if (carry != 0) {
            k2[AES_BLOCK_SIZE - 1] ^= 0x87;
        }
    }

    /**
     * 数据按 ISO9797 Method 1 补齐到16字节倍数（不足位全部填0x00）。
     */
    private static byte[] padIso9797Method1ToBlock16(byte[] data) {
        int len = data.length;
        int paddedLen = ((len + AES_BLOCK_SIZE - 1) / AES_BLOCK_SIZE) * AES_BLOCK_SIZE;
        if (paddedLen == 0) {
            paddedLen = AES_BLOCK_SIZE;
        }
        byte[] out = new byte[paddedLen];
        System.arraycopy(data, 0, out, 0, len);
        return out;
    }

}