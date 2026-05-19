package com.urovo.sdk.view;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.device.DeviceManager;
import android.device.SEManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.urovo.sdk.R;
import com.urovo.sdk.insertcard.InsertCardHandlerImpl;
import com.urovo.sdk.insertcard.utils.Constant;
import com.urovo.sdk.utils.BytesUtil;

import java.io.FileOutputStream;

public class ICReaderActivity_PDA extends BaseActivity implements View.OnClickListener {

    private InsertCardHandlerImpl icReader;
    private EditText editText_apdu;
    private byte cardType = Constant.Mode.MODE_USER;
    private RadioGroup radioGroup;

    private boolean mNeedOpenHost = true; //是否需要打开HOST和初始化SE，true：需要

    private String ip = "www.baidu.com";

    private ProgressDialog mProgressDialog = null;

    private int DELAY_HOST_MODE_OPEN = 6 * 1000;//HOST模式打开时延时时间
    private int DELAY_HOST_MODE_CLOSE = 2 * 1000;//HOST模式关闭时延时时间，固件版本
    public static long TIME_HOST_OPEN_PREPARE_DELAY = 50;
    public static final int MESSAGE_HOST_OPEN = 0x01;
    public static final int MESSAGE_PSAM_OPEN = 0x02;
    public static final int MESSAGE_DIALOG_SHOW = 0x04;
    public static final int MESSAGE_DIALOG_DISMISS = 0x05;
    public static final int MESSAGE_REFRESH = 0x06;

    public static final String NOde_53XC = "/sys/kernel/kobject_pogo_otg_status/pogo_typeC_status";
    public static final String NOde_55_5G = "/sys/devices/platform/otg_typecdig/pogo_host";
    public static final String NOde_53S = "/sys/devices/virtual/pogo/pogo_pin/pogo_otg_mode";
    public static final String Node_53XC_OTG_Status = "persist.sys.urv.otg.plugin";
    public static final String Node_55B_OTG_Ethernet = "/sys/kernel/kobject_pogo_otg_status/pogo_plug_status";
    public static final String Node_55B_OTG_Ethernet_File = "File-/sys/kernel/kobject_pogo_otg_status/pogo_plug_status";

    public Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MESSAGE_DIALOG_SHOW:
                    if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                        mProgressDialog.show();
                    }
                    break;
                case MESSAGE_DIALOG_DISMISS:
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    break;
                case MESSAGE_REFRESH:
                    mNeedOpenHost = true;
                    HOSTEnable(false);
                    mHandler.sendEmptyMessageDelayed(MESSAGE_HOST_OPEN, DELAY_HOST_MODE_CLOSE);
                    break;
                case MESSAGE_HOST_OPEN:
                    HOSTEnable(true);
                    mHandler.sendEmptyMessageDelayed(MESSAGE_PSAM_OPEN, DELAY_HOST_MODE_OPEN);
                    break;
                case MESSAGE_PSAM_OPEN:
                    if (!getHOSTStatus()) {
                        outputColorText(TextColor.RED, "The bottom HOST mode is not turned on, making it impossible to operate the PSAM card!");
                        return;
                    }
                    mHandler.sendEmptyMessageAtTime(MESSAGE_DIALOG_DISMISS, 0);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icreader);
        initView();
        icReader = InsertCardHandlerImpl.getInstance();
        editText_apdu = (EditText) findViewById(R.id.editText_apdu);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup_type);
        findViewById(R.id.radio_des).setVisibility(View.GONE);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_des:
                        cardType = 0;
                        break;
                    case R.id.radio_sm4:
                        cardType = 1;
                        break;
                    case R.id.radio_aes:
                        cardType = 2;
                        break;
                }
            }
        });
        button_clearAPDU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_apdu.setText("");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgressDialog = new ProgressDialog(ICReaderActivity_PDA.this);
        mProgressDialog.setMessage("Initializing Bottom Host mode...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);

        openHostMode();
    }

    @Override
    public void onClick(View view) {
        try {
            boolean status = false;
            switch (view.getId()) {
                case R.id.btnpowerUp:
                    outputText("powerUp");
                    byte[] atrData = new byte[64];
                    int ret = icReader.powerUp(cardType, atrData);
                    outputText("result:" + (ret > 0));
                    if (ret > 0) {
                        byte[] dataOut = new byte[ret];
                        System.arraycopy(atrData, 0, dataOut, 0, ret);
                        outputText("ATR:" + BytesUtil.bytes2HexString(dataOut));
                        SE_Init();
                    }
                    break;
                case R.id.btnpowerDown:
                    outputText("powerDown");
                    status = icReader.powerDown(cardType);
                    outputText("" + status);
                    SE_Close();
                    break;
                case R.id.btnIsCardIn:
                    outputText("isCardIn");
                    status = icReader.isCardIn();
                    outputText("" + status);
                    break;
                case R.id.btnIsPsamCardIn:
                    outputText("isPSAMCardExists");
                    status = icReader.isPSAMCardExists(cardType);
                    outputText("" + status);
                    break;
                case R.id.btnExchangeApdu:
                    outputText("exchangeApdu");
                    String apduStr = editText_apdu.getText().toString();
                    byte[] cmdHead = new byte[]{0x00, (byte) 0xa4, 0x04, 0x00, 0x0e};
                    byte[] fileName = "1PAY.SYS.DDF01".getBytes();
                    byte[] apdu = new byte[cmdHead.length + fileName.length];
                    System.arraycopy(cmdHead, 0, apdu, 0, cmdHead.length);
                    System.arraycopy(fileName, 0, apdu, cmdHead.length, fileName.length);
                    if (cardType != Constant.Mode.MODE_USER) {
                        apdu = BytesUtil.hexString2Bytes("0084000004");
                    }
                    if (!TextUtils.isEmpty(apduStr)) {
                        apdu = BytesUtil.hexString2Bytes(apduStr);
                    }
                    long startTime = System.currentTimeMillis();
                    byte[] rspData = icReader.exchangeApdu(cardType, apdu);
                    long endTime = System.currentTimeMillis();
                    outputText("time:" + (endTime - startTime) + "\nAPDU result:" + BytesUtil.bytes2HexString(rspData));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int PSAM_OPEN_COUNT = 0; //PSAM打开重试次数

    public void openHostMode() {
        Log.e(TAG, "openHostMode, mNeedOpenHost:" + mNeedOpenHost);
        mProgressDialog.setMessage("Initializing Bottom Host mode...");
        if (!mNeedOpenHost) {
            mHandler.sendEmptyMessage(MESSAGE_DIALOG_SHOW);
            mHandler.sendEmptyMessageDelayed(MESSAGE_PSAM_OPEN, 0);
            return;
        }
        if (PSAM_OPEN_COUNT == 0) {
            mHandler.sendEmptyMessage(MESSAGE_DIALOG_SHOW);
        }
        HOSTEnable(false);
        mHandler.sendEmptyMessageDelayed(MESSAGE_HOST_OPEN, DELAY_HOST_MODE_CLOSE);
    }

    public void HOSTEnable(boolean enable) {
        Log.e(TAG, "HOSTEnable:" + enable);
        FileOutputStream node_1 = null;
        String Node = NOde_53XC;
        String deviceType = "";
        try {
            deviceType = getDeviceType();
            if (checkDeviceType_5G(deviceType)) {
                Node = NOde_55_5G;
            } else if (TextUtils.equals("SQ53S", deviceType)) {
                Node = NOde_53S;
                DeviceManager deviceManager = new DeviceManager();
                deviceManager.setSettingProperty("File-" + Node, enable ? "2" : "0");
                deviceManager.setSettingProperty("System-sys.hostkey.switch", enable ? "1" : "0");
                return;
            }
            node_1 = new FileOutputStream(Node);
            byte[] open_two = enable ? "1".getBytes() : "0".getBytes();
            node_1.write(open_two);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (!TextUtils.equals("SQ53S", deviceType)) {
                    DeviceManager deviceManager = new DeviceManager();
                    deviceManager.setSettingProperty("File-" + Node, enable ? "1" : "0");
                }
            } catch (Throwable exception) {
                exception.printStackTrace();
                Log.e(TAG, "Throwable:" + exception.getMessage());
            }
        } finally {
            try {
                if (node_1 != null) {
                    node_1.close();
                }
                showHostModeNotification(ICReaderActivity_PDA.this, enable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean getHOSTStatus() {
        Log.e(TAG, "getHOSTStatus");
        String Node = NOde_53XC;
        String deviceType = getDeviceType();
        if (checkDeviceType_5G(deviceType)) {
            Node = NOde_55_5G;
        } else if (TextUtils.equals("SQ53S", deviceType)) {
            Node = NOde_53S;
        }
        String hostStatus = new DeviceManager().getSettingProperty("File-" + Node).trim();
        Log.e(TAG, "hostStatus:" + hostStatus);
        return TextUtils.equals("1", hostStatus);
    }

    public boolean getOTGStatus() {
        Log.e(TAG, "getOTGStatus");
        String otgStatus = new DeviceManager().getSettingProperty(Node_53XC_OTG_Status).trim();
        Log.e(TAG, "otgStatus:" + otgStatus);
        return TextUtils.equals("true", otgStatus);
    }

    private boolean mSEOpened = false;
    private SEManager mSeManager = null;

    public void SE_Init() {
        Log.e(TAG, "SE_Init");
        byte[] resp = new byte[128];
        byte[] len = new byte[4];
        if (mSeManager == null) {
            mSeManager = new SEManager();
        }
        int ret = mSeManager.open();
        Log.e(TAG, "mSeManager.open:" + ret);
        mSEOpened = (ret == 0);
        ret = mSeManager.enableSuspend(0, resp, len);
        Log.e(TAG, "mSeManager.enableSuspend:" + ret + ", len:" + BytesUtil.bytes2HexString(len) + ", resp:" + BytesUtil.bytes2HexString(resp));
    }

    public void SE_Close() {
        Log.e(TAG, "SE_Close");
        if (mSeManager != null && mSEOpened) {
            int close = mSeManager.close();
            Log.e(TAG, "mSeManager.close()：" + close);
        }
        mSEOpened = false;
        mSeManager = null;
    }

    public String getDeviceType() {
        DeviceManager deviceManager = new DeviceManager();
        String deviceType = "";
        try {
            deviceType = deviceManager.getSettingProperty("pwv.project");
            if (TextUtils.isEmpty(deviceType)) {
                deviceType = deviceManager.getSettingProperty("ro.build.product");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e(TAG, "deviceType:" + deviceType);
        return deviceType;
    }

    public boolean checkDeviceType_5G(String deviceType) {
        if (TextUtils.equals("SQ55-5G", deviceType)
                || TextUtils.equals("SQ55_5G", deviceType)
                || TextUtils.equals("DT50_5G_EEA", deviceType)) {
            return true;
        }
        return false;
    }

    public static void showHostModeNotification(Context context, boolean enable) {
        Log.e(TAG, "showHostModeNotification:" + enable);
        Intent intent = new Intent();
        if (enable) {
            intent.setAction("urovo.intent.action.ACTION_SHOW_HOST_TIP");
        } else {
            intent.setAction("urovo.intent.action.ACTION_HIDE_HOST_TIP");
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        context.sendBroadcast(intent);
    }


}
