package com.urovo.sdk.pollingcard;

import android.device.IccManager;
import android.device.MagManager;
import android.device.PiccManager;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.urovo.file.logfile;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PollingCardUtil {

    public static final String TAG = "PollingCardUtil===>";

    private static MagManager mMagManager;
    private static IccManager mIccManager;
    private static PiccManager mPiccManager;
    private static boolean openMag;
    private static boolean openIcc;
    private static boolean openPicc;
    private static boolean mPollingProcess;
    private static PollingResult mResult = null;
    private static Handler main = new Handler(Looper.getMainLooper());
    private static CountDownLatch mLatch = new CountDownLatch(1);

    public static PollingResult polling(long timeOut, boolean enableMAG, boolean enableICC, boolean enablePicc) {
        Log.e(TAG, "polling: timeOut=" + timeOut + ", enableMAG=" + enableMAG + ", enableICC=" + enableICC + ", enablePicc=" + enablePicc);
        try {
            mResult = null;
            if (enableMAG && mMagManager == null) {
                mMagManager = new MagManager();
                int ret = mMagManager.open();
                Log.e(TAG, "mMagManager.open:" + ret);
                openMag = (ret == 0);
            }

            if (enableICC && mIccManager == null) {
                mIccManager = new IccManager();
                int ret = mIccManager.open((byte) 0, (byte) 1, (byte) 1);
                Log.e(TAG, "mIccManager.open:" + ret);
                openIcc = (ret == 0);
            }

            if (enablePicc && mPiccManager == null) {
                mPiccManager = new PiccManager();
                int ret = mPiccManager.open();
                Log.e(TAG, "mPiccManager.open:" + ret);
                openPicc = (ret == 0);
            }

            if (!openMag && !openIcc && !openPicc) {
                stopPolling();
                return mResult;
            }
            mPollingProcess = true;
            main.post(new Runnable() {
                @Override
                public void run() {
                    while (mPollingProcess) {
                        if (openMag) {
                            int ret = mMagManager.checkCard();
                            Log.e(TAG, "mMagManager.checkCard:" + ret);
                            if (ret == 0) {
                                byte[] stripInfo = new byte[1024];
                                int allLen = mMagManager.getAllStripInfo(stripInfo);
                                mResult = GetMagCardStrMagManager(stripInfo);
                                stopPolling();
                                return;
                            }
                        }

                        if (openIcc) {
                            int ret = mIccManager.detect();
                            Log.e(TAG, "mIccManager.detect:" + ret);
                            if (ret == 0) {
                                mResult = new PollingResult();
                                mResult.setType(2);
                                stopPolling();
                                return;
                            }
                        }

                        if (openPicc) {
                            byte[] mode = new byte[32];
                            byte[] Atqa = new byte[32];
                            int ret = mPiccManager.request(mode, Atqa);
                            Log.e(TAG, "mPiccManager.request:" + ret);
                            if (ret > 0) {
                                mResult = new PollingResult();
                                mResult.setType(3);
                                stopPolling();
                                return;
                            }
                        }
                    }
                }
            });
            mLatch.await(timeOut, TimeUnit.SECONDS);
            mPollingProcess = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mResult;
    }

    public static void stopPolling() {
        Log.e(TAG, "stopPolling");
        mPollingProcess = false;
        if (mMagManager != null) {
            mMagManager.close();
            mMagManager = null;
        }
        if (mIccManager != null) {
            mIccManager.close();
            mIccManager = null;
        }
        if (mPiccManager != null) {
            mPiccManager.close();
            mPiccManager = null;
        }
        if (mLatch != null) {
            mLatch.countDown();
            mLatch = null;
        }
        openMag = false;
        openIcc = false;
        openPicc = false;
    }

    public static PollingResult GetMagCardStrMagManager(byte[] stripInfo) {
        logfile.printLog(TAG + "===GetMagCardStrMagManager:" + new String(stripInfo, 0, 113));
        PollingResult result = new PollingResult();
        result.setType(1);
        int len = stripInfo[1];
        if (len != 0) {
            result.setTrack1(new String(stripInfo, 2, len));
        }
        int len2 = stripInfo[3 + len];
        String track2 = "";
        if (len2 != 0) {
            result.setTrack1(new String(stripInfo, 4 + len, len2));
        }
        result.setTrack2(track2.trim());
        if (TextUtils.isEmpty(track2)) {
            int len3 = stripInfo[5 + len + len2];
            if (len3 != 0) {
                String track3 = new String(stripInfo, 6 + len + len2, len3);
                result.setTrack3(track3.trim());
            }
        } else {
            int index = track2.indexOf("=");
            if (index != -1) {
                String pan = track2.substring(0, index);
                result.setPan(pan);
                if (!TextUtils.isEmpty(track2.substring(index + 1))) {
                    if (track2.length() >= (index + 5)) {
                        String validTime = track2.substring(index + 1, index + 5);
                        result.setExpireDate(validTime);
                        if (track2.length() >= (index + 8)) {
                            String serviceCode = track2.substring(index + 5, index + 8);
                            result.setServiceCode(serviceCode);
                        }
                    }
                }
                int len3 = stripInfo[5 + len + len2];
                if (len3 != 0) {
                    String track3 = new String(stripInfo, 6 + len + len2, len3);
                    result.setTrack3(track3.trim());
                }
            }
        }
        return result;
    }
}
