package com.urovo.sdk.utils;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.device.DeviceManager;
import android.device.SEManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class PhoneUtil {

    private static final String TAG = "PhoneUtil";

    /*
* @des:获取移动设备国际识别码--IMEI是区别移动设备的标识，储存在移动设备中，可用于监控被窃
        IMEI印在手机机身背面的标志上，如图1所示；并且读写存储在手机内存中。它也是该手机在厂家的“档案”和“身份证号”。
*/
    public static String getIMEI(Context context, int slot) {
        Log.e(TAG, "getIMEI:" + slot);
        String IMEINumber = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.e(TAG, "getIMEI permission declined");
                return "";
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                IMEINumber = telephonyManager.getImei(slot);
                Log.e(TAG, "telephonyManager.getImei:" + IMEINumber);
            } else {
                IMEINumber = new DeviceManager().getImei(slot);
                Log.e(TAG, "DeviceManager().getImei:" + IMEINumber);
            }
        } catch (Throwable e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e(TAG, "getIMEI:" + e.getMessage());
        }
        Log.e(TAG, "IMEINumber:" + IMEINumber);
        return TextUtils.isEmpty(IMEINumber) ? "" : IMEINumber;
    }

    public static String getSimSN(Context context, int slot) {
        String simSN = "";
        try {
            simSN = new DeviceManager().getSimSerialNumber(slot);
            return simSN;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.e(TAG, "getSimSN permission declined");
                return simSN;
            }
            TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            simSN = telManager.getSimSerialNumber();
        } catch (Throwable e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e(TAG, "getSimSN:" + e.getMessage());
        }
        Log.e(TAG, "getSimSN:" + simSN);
        return TextUtils.isEmpty(simSN) ? "" : simSN;
    }

    public static String getSimPhoneNo(Context context, int slot) {
        String simNumber = "";
        try {
            simNumber = new DeviceManager().getPhoneNumber(slot);
            return simNumber;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        Log.e(TAG, "getLine1Number:" + simNumber);
        if (!TextUtils.isEmpty(simNumber)) {
            return simNumber;
        }
        if (slot == 0) {
            simNumber = new DeviceManager().getSettingProperty("persist.sys.urv.solt0.phonenumber");
            Log.e(TAG, "solt0.phonenumber" + simNumber);
        } else {
            simNumber = new DeviceManager().getSettingProperty("persist.sys.urv.solt1.phonenumber");
            Log.e(TAG, "solt1.phonenumber" + simNumber);
        }
        Log.e(TAG, "phonenumber:" + simNumber);
        if (!TextUtils.isEmpty(simNumber)) {
            return simNumber;
        }
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.e(TAG, "getSimPhoneNo permission declined");
                return simNumber;
            }
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            simNumber = telephonyManager.getLine1Number();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e(TAG, "getLine1Number:" + simNumber);

        return TextUtils.isEmpty(simNumber) ? "" : simNumber;
    }

    public static String getSimOperator(Context context) {
        String simOperator = "";
        try {
            TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            simOperator = telManager.getSimOperator();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e(TAG, "getSimOperator:" + e.getMessage());
        }
        Log.e(TAG, "getSimOperator:" + simOperator);
        return TextUtils.isEmpty(simOperator) ? "" : simOperator;
    }

    public static String getSimOperatorName(Context context) {
        String simOperatorName = "";
        try {
            TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            simOperatorName = telManager.getSimOperatorName();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e(TAG, "simOperatorName:" + e.getMessage());
        }
        Log.e(TAG, "simOperatorName:" + simOperatorName);
        return TextUtils.isEmpty(simOperatorName) ? "" : simOperatorName;
    }

    /**
     * 获取MCC
     *
     * @param @param  context
     * @param @return
     * @return String
     * @throws
     * @Title: getMCC
     * @Description:TODO
     */
    public static String getMCC(Context context) {
        String mcc = "";
        try {
            TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "";
            }
            String imsi = telManager.getSubscriberId();
            Log.e(TAG, "imsi:" + imsi);
            if (!TextUtils.isEmpty(imsi) && imsi.length() >= 3) {
                mcc = imsi.substring(0, 3);
            }
        } catch (Throwable e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e(TAG, "getMCC:" + e.getMessage());
        }
        return TextUtils.isEmpty(mcc) ? "" : mcc;
    }

    /**
     * 获取MNC
     *
     * @param @param  context
     * @param @return
     * @return String
     * @throws
     * @Title: getMNC
     * @Description:TODO
     */
    public static String getMNC(Context context) {
        String mnc = "";
        try {
            TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "";
            }
            String imsi = telManager.getSubscriberId();
            Log.e(TAG, "imsi:" + imsi);
            if (!TextUtils.isEmpty(imsi) && imsi.length() >= 5) {
                mnc = imsi.substring(3, 5);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e(TAG, "getMNC:" + e.getMessage());
        }
        return TextUtils.isEmpty(mnc) ? "" : mnc;
    }

    public static String getBluetoothMac() {
        try {
            try {
                return new DeviceManager().getBluetoothMac();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            //Starting from Android 6.0 (API 23), getAddress() returns a fixed fake address for non-system applications:
            //02:00:00:00:00:00
            return BluetoothAdapter.getDefaultAdapter().getAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getWifiMacAddress(Context context) {
        try {
            try {
                return new DeviceManager().getWifiMacAddress();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            //Starting from Android 6.0 (API 23), getAddress() returns a fixed fake address for non-system applications:
            //02:00:00:00:00:00
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return wifiInfo.getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getOSVersion() {
        try {
            return SystemProperties.getSystemProperty("ro.vendor.build.id", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getSEVersion() {
        byte[] rspData = new byte[256];
        byte[] rspLen = new byte[2];
        String seversion = "";
        try {
            int iRet = new SEManager().getFirmwareVersion(rspData, rspLen);
            byte[] rspBuff = new byte[rspLen[0]];
            System.arraycopy(rspData, 0, rspBuff, 0, rspBuff.length);
            seversion = new String(rspBuff, "GBK").toUpperCase();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return seversion;
    }

}
