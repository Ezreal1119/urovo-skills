package com.urovo.sdk.view;

import android.content.Intent;
import android.device.DeviceManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.urovo.sdk.R;
import com.urovo.sdk.system.SystemProviderImpl;
import com.urovo.sdk.utils.PhoneUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SystemActivity extends BaseActivity implements View.OnClickListener {

    public SystemProviderImpl mSystemProvider = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);
        initView();
        mSystemProvider = SystemProviderImpl.getInstance(SystemActivity.this);
    }

    @Override
    public void onClick(View view) {
        int ret = -1;
        switch (view.getId()) {
            case R.id.btn_setDefaultDataSubId0:
                outputText("setDefaultDataSubId0");
                ret = mSystemProvider.setDefaultDataSubId(0);
                outputText("setDefaultDataSubId0:" + ret);
                break;
            case R.id.btn_setDefaultDataSubId1:
                outputText("setDefaultDataSubId1");
                ret = mSystemProvider.setDefaultDataSubId(1);
                outputText("setDefaultDataSubId1:" + ret);
                break;
            case R.id.btn_setForceLockScreen:
                outputText("setForceLockScreen");
                mSystemProvider.setForceLockScreen(true);
                break;
            case R.id.btn_setLockSreenNon:
                outputText("setLockScreenNon");
                mSystemProvider.setLockScreenNon();
                break;
            case R.id.btn_setLanguage:
                outputText("setLanguage");
                mSystemProvider.setLanguage(new Locale("zh", "CN"));
                break;
            case R.id.btn_getPhoneInfo:
                outputText("getPhoneInfo");
                try {
                    outputText("Device Model:" + Build.MODEL);
                    outputText("Serial No:" + new DeviceManager().getDeviceId());
                    outputText("OS Version:" + PhoneUtil.getOSVersion());
                    outputText("SE Version:" + PhoneUtil.getSEVersion());
                    outputText("IMEI 1:" + PhoneUtil.getIMEI(this, 0));
                    outputText("IMEI 2:" + PhoneUtil.getIMEI(this, 1));
                    outputText("SIM 1 SN:" + PhoneUtil.getSimSN(this, 0));
                    outputText("SIM 2 SN:" + PhoneUtil.getSimSN(this, 1));
                    outputText("Phone Number 1:" + PhoneUtil.getSimPhoneNo(this, 0));
                    outputText("Phone Number 2:" + PhoneUtil.getSimPhoneNo(this, 1));
                    outputText("MCC:" + PhoneUtil.getMCC(this));
                    outputText("MNC:" + PhoneUtil.getMNC(this));
                    outputText("SIM Operator:" + PhoneUtil.getSimOperator(this));
                    outputText("SIM Operator Name:" + PhoneUtil.getSimOperatorName(this));
                    outputText("Bluetooth MAC:" + PhoneUtil.getBluetoothMac());
                    outputText("Wifi MAC:" + PhoneUtil.getWifiMacAddress(this));

                    getDeviceInfo();
                    getBatteryInfo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_doubleclick_enable:
                //doubleclick-enable,  ""-disable
                new DeviceManager().setSettingProperty("persist-persist.sys.urv.tp.wakeup.gesture", "doubleclick");
                break;
            case R.id.btn_doubleclick_disable:
                //doubleclick-enable,  ""-disable
                new DeviceManager().setSettingProperty("persist-persist.sys.urv.tp.wakeup.gesture", "");
                break;
            case R.id.btn_keyboard_state:
                String MODE_KB_NUM = "0";
                String MODE_KB_LOWER = "1";
                String MODE_KB_UPPER = "2";
                outputText("getKeyboardState");
                String state = new DeviceManager().getSettingProperty("Global-ufans.keyboard.state");
                outputText("" + state);
                if (TextUtils.equals(MODE_KB_NUM, state)) {
                    outputText("MODE_KB_NUM");
                } else if (TextUtils.equals(MODE_KB_LOWER, state)) {
                    outputText("MODE_KB_LOWER");
                } else if (TextUtils.equals(MODE_KB_UPPER, state)) {
                    outputText("MODE_KB_UPPER");
                }
                break;
            case R.id.btn_disable_keyboardswitch:
                outputText("disable_keyboardswitch");
                //disable keyboard switch: true-disable, false-enable.
                boolean status = new DeviceManager().setSettingProperty("persist-persist.sys.urv.disable.change_language", "true");
                outputColorText(status ? TextColor.RED : TextColor.BLUE, "" + status);
                break;
            case R.id.btn_enable_keyboardswitch:
                outputText("enable_keyboardswitch");
                //disable keyboard switch: true-disable, false-enable.
                status = new DeviceManager().setSettingProperty("persist-persist.sys.urv.disable.change_language", "false");
                outputColorText(status ? TextColor.RED : TextColor.BLUE, "" + status);
                break;
            case R.id.btn_switch_keyboard_num:
                outputText("switch_keyboard_num");
                new DeviceManager().setSettingProperty("Global-ufans.keyboard.state", "0");
                //After keyboard switching, need to update the keyboard state in the status bar.
                outputText("updateKeyboardStateInStatusbar");
                sendBroadcast(new Intent("android.intent.action.ACTION_SWITCH_KEY_STATE"));
                break;
            case R.id.btn_switch_keyboard_lower:
                outputText("switch_keyboard_lower");
                new DeviceManager().setSettingProperty("Global-ufans.keyboard.state", "1");
                //After keyboard switching, need to update the keyboard state in the status bar.
                outputText("updateKeyboardStateInStatusbar");
                sendBroadcast(new Intent("android.intent.action.ACTION_SWITCH_KEY_STATE"));
                break;
            case R.id.btn_switch_keyboard_upper:
                outputText("switch_keyboard_upper");
                new DeviceManager().setSettingProperty("Global-ufans.keyboard.state", "2");
                //After keyboard switching, need to update the keyboard state in the status bar.
                outputText("updateKeyboardStateInStatusbar");
                sendBroadcast(new Intent("android.intent.action.ACTION_SWITCH_KEY_STATE"));
                break;
            case R.id.btn_lockTaskMode_on:
                new DeviceManager().setLockTaskMode(getPackageName(), true);
                new DeviceManager().setLockTaskModePassword("123456");
                break;
            case R.id.btn_lockTaskMode_off:
                new DeviceManager().setLockTaskMode(getPackageName(), false);
                break;
            case R.id.btn_switchWifi_on:
                outputText("switchWifi(true)");
                new DeviceManager().switchWifi(true);
                break;
            case R.id.btn_switchWifi_off:
                outputText("switchWifi(false)");
                new DeviceManager().switchWifi(false);
                break;
            case R.id.btn_switchBT_on:
                outputText("switchBT(true)");
                new DeviceManager().switchBT(true);
                break;
            case R.id.btn_switchBT_off:
                outputText("switchBT(false)");
                new DeviceManager().switchBT(false);
                break;
            case R.id.btn_switchAirplaneMode_on:
                outputText("switchAirplaneMode(true)");
                new DeviceManager().setAirplaneMode(true);
                break;
            case R.id.btn_switchAirplaneMode_off:
                outputText("switchAirplaneMode(false)");
                new DeviceManager().setAirplaneMode(false);
                break;
            case R.id.btn_forgetAllWifi:
                outputText("forgetAllWifi");
                new DeviceManager().forgetAllWifi();
                break;
            case R.id.btn_checkNetworkStatus:
                outputText("checkNetworkStatus");
                //0:网络正常连接/Normal network connection.
                //1:SIM卡未就绪/SIM card not ready.
                //2:移动数据关闭/Mobile network shutdown.
                //3:未注册到网络/Not registered on the network.
                //4:APN配置失败/APN configuration failed.
                //5:无数据连接/No data connection.
                //6:无互联网连接/No Internet connection.
                ret = new DeviceManager().checkNetworkStatus();
                outputText("checkNetworkStatus:" + ret);
                break;
        }
    }

    public void setTimeZone(String value) {
        boolean status = new DeviceManager().setSettingProperty("persist-persist.sys.timezone", value);
        Log.e(TAG, "set timezone:" + status);
        showMessage(value + ":" + status);

//        new DeviceManager().setSettingProperty("persist-persist.sys.settimezone","America/Maxico_City");
    }

    public void getDeviceInfo() {
        Log.e(TAG, "ANDROID Verison:" + Build.VERSION.RELEASE);

        String androidId = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e(TAG, "ANDROID ID:" + androidId);
        outputText("ANDROID ID:" + androidId);

        String seversion = new DeviceManager().getSettingProperty("persist-urv.se.version");
        Log.e(TAG, "SE version:" + seversion);
        outputText("SE version:" + seversion);

        String buildNumber = new DeviceManager().getSettingProperty("ro.vendor.build.id");
        Log.e(TAG, "Build number:" + buildNumber);
        outputText("Build number:" + buildNumber);

//        //If UFS comstomization
//        String hwversion = new DeviceManager().getSettingProperty("ro.ufs.hw.version");
//        Log.e(TAG, "Hardware version:" + hwversion);
//        outputText("Hardware version:" + hwversion);
//
//        //If UFS comstomization
//        String fwversion = new DeviceManager().getSettingProperty("ro.ufs.sw");
//        Log.e(TAG, "Firmware version:" + fwversion);
//        outputText("Firmware version:" + fwversion);

        //Universal
        String hwversion = new DeviceManager().getSettingProperty("persist.sys.hw.version");
        Log.e(TAG, "Hardware version:" + hwversion);
        outputText("Hardware version:" + hwversion);

        //Universal
        String fwversion = new DeviceManager().getSettingProperty("persist.sys.sw.version");
        Log.e(TAG, "Firmware version:" + fwversion);
        outputText("Firmware version:" + fwversion);

        String ufsCustom = new DeviceManager().getSettingProperty("ro.ufs.custom");
        Log.e(TAG, "ufsCustom:" + ufsCustom);
        String ufsAttatch = new DeviceManager().getSettingProperty("ro.ufs.custom.attach");
        Log.e(TAG, "attach:" + ufsAttatch);
        String ufsVersion = new DeviceManager().getSettingProperty("ro.ufs.build.version");
        Log.e(TAG, "version:" + ufsVersion);
        String ufsDate = new DeviceManager().getSettingProperty("ro.ufs.build.date.utc");
        Log.e(TAG, "utc:" + ufsDate);
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append(ufsCustom);
        if (!TextUtils.isEmpty(ufsAttatch) && !TextUtils.equals(ufsAttatch.toUpperCase(), "XX"))
            stringBuilder.append("_" + ufsAttatch);
        if (!TextUtils.isEmpty(ufsVersion)) stringBuilder.append("_" + ufsVersion);
        if (!TextUtils.isEmpty(ufsDate)) {
            String format = "yyyyMMdd";
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            stringBuilder.append("_" + sdf.format(new Date(Long.valueOf(ufsDate))));
        }
        Log.e(TAG, "Custom version:" + stringBuilder.toString().toUpperCase());
        outputText("Custom version:" + stringBuilder.toString().toUpperCase());
    }

    public void getBatteryInfo() {
        Bundle bundle = new DeviceManager().getBatteryInfo();
        int level = bundle.getInt("level"); //battery percentage
        int plugged = bundle.getInt("plugged"); //Is it charging, 0:no, 1:yes
        outputText("BatteryInfo:" + "level=" + level + ", plugged=" + plugged);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        int keyAction = event.getAction();
        Log.e("MainActivity", "dispatchKeyEvent keyCode:" + keyCode + ", keyAction:" + keyAction);
        if (keyCode == 513 && keyAction == KeyEvent.ACTION_UP) { //1
            Toast.makeText(SystemActivity.this, "1", Toast.LENGTH_SHORT).show();
            return false;
        } else if (keyCode == 111 && keyAction == KeyEvent.ACTION_UP) { //2
            Toast.makeText(SystemActivity.this, "2", Toast.LENGTH_SHORT).show();
            return false;
        } else if (keyCode == 67 && keyAction == KeyEvent.ACTION_UP) { //3
            Toast.makeText(SystemActivity.this, "3", Toast.LENGTH_SHORT).show();
            return false;
        } else if (keyCode == 66 && keyAction == KeyEvent.ACTION_UP) { //4
            Toast.makeText(SystemActivity.this, "4", Toast.LENGTH_SHORT).show();
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

}
