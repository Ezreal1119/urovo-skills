package com.urovo.sdk.utils;

import android.content.Context;
import android.device.DeviceManager;

import com.urovo.sdk.emvlogupload.EmvLogUploadUtil;
import com.urovo.sdk.emvlogupload.listener.LogUploadCallBack;

import org.json.JSONObject;

import java.util.Date;

public class EmvLogUtil {

    public static void uploadEmvLog(Context context, String ip, String port, final String errorCode, final String errorMsg, LogUploadCallBack callBack) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                //service.urovo.com:1881
                String url = "https://" + ip + ":" + port + "/outsideapi/v1/upload/data";
                try {
                    JSONObject requestJson = new JSONObject();
                    requestJson.put("appID", "UTMS_Opay"); //Mandatory. App id.
                    requestJson.put("sn", new DeviceManager().getDeviceId()); //Mandatory. Device id.
                    requestJson.put("code", errorCode + ""); //Mandatory. Error code.
                    requestJson.put("businessType", 0); //Mandatory. Bussiness type:0-EMV log, 1-Trigger event.
                    requestJson.put("terminalTime", DateUtil.getDateTime(new Date())); //Optional. Upload date time.(yyyy/MM/dd HH:mm:ss)
                    requestJson.put("msg", errorMsg + ""); //Optional. Error message.
                    requestJson.put("msg1", ""); //Optional. Error message 2.
                    requestJson.put("msg2", ""); //Optional. Error message 3.

                    String errorDate = DateUtil.getDateTime3(new Date()); //Error date time:yyyyMMdd
                    EmvLogUploadUtil.uploadEmvLog(context, url, errorDate, requestJson, callBack);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
