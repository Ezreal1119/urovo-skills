package com.urovo.sdk.utils;

import android.device.SEManager;
import android.util.Log;

public class TR34Util {

    private static final String TAG = "TR34Util===>";

    /**
     * writeTR34Cert
     *
     * @param type      CA_TYPE_KMSCA,0xF2
     *                  CA_TYPE_PEDCRT,0xF3
     *                  CA_TYPE_KDHCRRT,0xF4
     *                  CA_TYPE_PEDPRV, 0xF5
     * @param index:0-3
     * @param data      resLen：长度
     * @return
     */
    public static int writeTR34Cert(int type, int index, byte[] data) throws Exception {
        Log.e(TAG, "writeTR34Cert: type=" + type + ", index=" + index);
        int ret = -1;
        try {
            if (type < 0xF2 || type > 0xF5) {
                throw new Exception("Invalid Type, should be0xF2-0xF5");
            }
            if (index < 0 || index > 3) {
                throw new Exception("Invalid Index, should be 0-3");
            }
            SEManager seManager = new SEManager();
            ret = seManager.deleteTR34Cert(type, index);
            Log.e(TAG, "deleteTR34Cert:" + ret);

            ret = seManager.writeTR34Cert(type, index, data, data.length);
            Log.e(TAG, "writeTR34Cert:" + ret);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
        return ret;
    }

    /**
     * readTR34Cert
     *
     * @param type         CA_TYPE_KMSCA,0xF2
     *                     CA_TYPE_PEDCRT,0xF3
     *                     CA_TYPE_KDHCRRT,0xF4
     *                     CA_TYPE_PEDPRV, 0xF5
     * @param index:0-3
     * @param responseData
     * @param resLen
     * @return
     */
    public static int readTR34Cert(int type, int index, byte[] responseData, int[] resLen) throws Exception {
        Log.e(TAG, "readTR34Cert: type=" + type + ", index=" + index);
        int ret = -1;
        try {
            if (type < 0xF2 || type > 0xF5) {
                throw new Exception("Invalid Type, should be0xF2-0xF5");
            }
            if (index < 0 || index > 3) {
                throw new Exception("Invalid Index, should be 0-3");
            }
            ret = new SEManager().readTR34Cert(type, index, responseData, resLen);
            Log.e(TAG, "readTR34Cert ret:" + ret);
            Log.e(TAG, "readTR34Cert resLen:" + resLen[0]);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
        return ret;
    }
}
