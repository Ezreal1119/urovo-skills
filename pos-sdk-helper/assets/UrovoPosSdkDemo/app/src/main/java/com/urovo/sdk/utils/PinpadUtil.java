package com.urovo.sdk.utils;

import android.util.Log;

import com.urovo.sdk.pinpad.PinPadProviderImpl;
import com.urovo.sdk.pinpad.utils.Constant;
import com.urovo.sdk.pinpad.utils.SM4;

public class PinpadUtil {

    private static final String TAG = "PinpadUtil";

    public static void generatePinBlock(int keyIndex, String Pan, String pin) {
        String panStr = Pan.substring(0, Pan.length() - 1);
        panStr = panStr.substring(panStr.length() - 12);
        panStr = StringUtil.addZero(panStr, 16, true);
        Log.e(TAG, "panStr:" + panStr);
        byte[] panBuff = ByteUtils.hexString2Bytes(panStr);

        String pinStr = "0" + Integer.toHexString(pin.length()) + pin;
        int paddLen = 16 - pinStr.length();
        for (int i = 0; i < paddLen; i++) {
            pinStr += "F";
        }
        Log.e(TAG, "pinStr:" + pinStr);
        byte[] pinBuff = ByteUtils.hexString2Bytes(pinStr);

        byte[] pinBlock = ByteUtils.getxor(panBuff, pinBuff, panBuff.length);
        Log.e(TAG, "pinBlock:" + BytesUtil.bytes2HexString(pinBlock));

        byte[] outdata = new byte[pinBlock.length];
        int[] outlen = new int[2];
        byte[] ksnBuff = new byte[10];
        int[] KsnLen = new int[2];
        try {
            PinPadProviderImpl pinpad = PinPadProviderImpl.getInstance();
            int ret = pinpad.DukptEncryptDataIV(1, keyIndex, 0x00, new byte[8], 8, pinBlock, pinBlock.length,
                    outdata, outlen, ksnBuff, KsnLen);
            Log.e(TAG, "DukptEncryptDataIV:" + ret);
            if (ret == 0) {
                Log.e(TAG, "ksnBuff:" + BytesUtil.bytes2HexString(ksnBuff));
                Log.e(TAG, "outdata:" + BytesUtil.bytes2HexString(outdata));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPinData(String Pan, byte[] key, byte[] data) {
        String pinBlockStr = "";
        try {
            byte[] pinBlock = TDESUtil.decrypt_ECB(key, data);
            Log.e(TAG, "pinBlock:" + Funs.bytesToHexString(pinBlock));

            String panStr = Pan.substring(0, Pan.length() - 1);
            panStr = panStr.substring(panStr.length() - 12);
            panStr = BytesUtil.FormatWithZero(panStr, "0000000000000000");
            Log.e(TAG, "panStr:" + panStr);

            byte[] panBuff = Funs.StrToHexByte(panStr);
            do_xor_urovo(panBuff, pinBlock, 8);
            Log.e(TAG, "pinBlock 2:" + Funs.bytesToHexString(panBuff));

            pinBlockStr = Funs.bytesToHexString(panBuff);
            int pinLen = Integer.parseInt(pinBlockStr.substring(1, 2), 16);
            Log.e(TAG, "Pin length:" + pinLen + "");
            pinBlockStr = pinBlockStr.substring(2, 2 + pinLen);

            Log.e(TAG, "clear pin:" + pinBlockStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pinBlockStr;
    }

    public static String getPinData_format1(String Pan, byte[] key, byte[] data) {
        String pinBlockStr = "";
        try {
            byte[] pinBlock = TDESUtil.decrypt_ECB(key, data);
            pinBlockStr = Funs.bytesToHexString(pinBlock);
            Log.e(TAG, "pinBlock:" + pinBlockStr);

            int pinLen = Integer.parseInt(pinBlockStr.substring(1, 2), 16);
            Log.e(TAG, "Pin length:" + pinLen + "");
            pinBlockStr = pinBlockStr.substring(2, 2 + pinLen);

            Log.e(TAG, "clear pin:" + pinBlockStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pinBlockStr;
    }

    public static String getPinData_format4(String Pan, byte[] key, byte[] data) {
        Log.e(TAG, "getPinData_format4--------->");
        String pinBlockStr = "";
        try {
            Log.e(TAG, "Enciphered PIN Block:" + BytesUtil.bytes2HexString(data));
            byte[] pinBlock = AESUtil.decryptECB(key, data);
            Log.e(TAG, "Intermediate Block B:" + BytesUtil.bytes2HexString(pinBlock));

            String panStr = "4" + com.urovo.i9000s.api.emv.Funs.FormatWithvalueR(Pan, "000000000000000000000000000000");
            Log.e(TAG, "Plain text PAN Field:" + panStr);

            byte[] panBuff2 = BytesUtil.hexString2Bytes(panStr);
            do_xor_urovo(panBuff2, pinBlock, 16);
            Log.e(TAG, "Intermediate Block A:" + BytesUtil.bytes2HexString(panBuff2));

            pinBlock = AESUtil.decryptECB(key, panBuff2);
            pinBlockStr = BytesUtil.bytes2HexString(pinBlock);
            Log.e(TAG, "Plain text PIN Field:" + pinBlockStr);

            int pinLen = Integer.parseInt(pinBlockStr.substring(1, 2), 16);
            Log.e(TAG, "Pin length:" + pinLen + "");
            pinBlockStr = pinBlockStr.substring(2, 2 + pinLen);

            Log.e(TAG, "clear pin:" + pinBlockStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pinBlockStr;
    }

    public static String getPinData_SM4(String Pan, byte[] key, byte[] data) {
        String pinBlockStr = "";
        try {
            byte[] pinBlock = SM4.SM4DecryptECB(key, data, data.length);
            Log.e(TAG, "pinBlock:" + BytesUtil.bytes2HexString(pinBlock));

            String panStr = Pan.substring(0, Pan.length() - 1);
            panStr = panStr.substring(panStr.length() - 12);
            panStr = BytesUtil.FormatWithZero(panStr, "00000000000000000000000000000000");

            Log.e(TAG, "panStr:" + panStr);
            byte[] panBuff = BytesUtil.hexString2Bytes(panStr);

            do_xor_urovo(panBuff, pinBlock, 16);
            Log.e(TAG, "pinBlock 2:" + BytesUtil.bytes2HexString(panBuff));

            pinBlockStr = BytesUtil.bytes2HexString(panBuff);
            int pinLen = Integer.parseInt(pinBlockStr.substring(1, 2), 16);
            Log.e(TAG, "Pin length:" + pinLen);
            pinBlockStr = pinBlockStr.substring(2, 2 + pinLen);

            Log.e(TAG, "clear pin:" + pinBlockStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pinBlockStr;
    }

    public static String getPinData_AES(String Pan, byte[] key, byte[] data) {
        Log.e(TAG, "getPinData_AES--------->");
        String pinBlockStr = "";
        try {
            Log.e(TAG, "Enciphered PIN Block:" + BytesUtil.bytes2HexString(data));
            byte[] pinBlock = AESUtil.decryptECB(key, data);
            Log.e(TAG, "Intermediate Block B:" + BytesUtil.bytes2HexString(pinBlock));

            String panStr = "4" + com.urovo.i9000s.api.emv.Funs.FormatWithvalueR(Pan, "000000000000000000000000000000");
            Log.e(TAG, "Plain text PAN Field:" + panStr);

            byte[] panBuff2 = BytesUtil.hexString2Bytes(panStr);
            do_xor_urovo(panBuff2, pinBlock, 16);
            Log.e(TAG, "Intermediate Block A:" + BytesUtil.bytes2HexString(panBuff2));

            pinBlock = AESUtil.decryptECB(key, panBuff2);
            pinBlockStr = BytesUtil.bytes2HexString(pinBlock);
            Log.e(TAG, "Plain text PIN Field:" + pinBlockStr);

            int pinLen = Integer.parseInt(pinBlockStr.substring(1, 2), 16);
            Log.e(TAG, "Pin length:" + pinLen + "");
            pinBlockStr = pinBlockStr.substring(2, 2 + pinLen);

            Log.e(TAG, "clear pin:" + pinBlockStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pinBlockStr;
    }

    public static void do_xor_urovo(byte[] src1, byte[] src2, int num) {
        int i;
        for (i = 0; i < num; i++) {
            src1[i] ^= src2[i];
        }
    }

}
