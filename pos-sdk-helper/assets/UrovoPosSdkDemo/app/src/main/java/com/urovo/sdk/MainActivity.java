package com.urovo.sdk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.urovo.file.logfile;
import com.urovo.sdk.emv.EmvActivityNew;
import com.urovo.sdk.view.BaseActivity;
import com.urovo.sdk.view.BeeperActivity;
import com.urovo.sdk.view.ICReaderActivity;
import com.urovo.sdk.view.ICReaderSLEActivity;
import com.urovo.sdk.view.LedActivity;
import com.urovo.sdk.view.MagCardReaderActivity;
import com.urovo.sdk.view.PinpadActivity;
import com.urovo.sdk.view.PrintActivity;
import com.urovo.sdk.view.RFReaderActivity;
import com.urovo.sdk.view.ScanActivity;
import com.urovo.sdk.view.ScanCustomActivity;
import com.urovo.sdk.view.SerialPortActivity;
import com.urovo.sdk.view.SilentInstallActivity;
import com.urovo.sdk.view.SystemActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logfile.setLogcatOut(true);
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.btn_emvnfc:
                    startActivity(EmvActivityNew.class);
                    break;
                case R.id.btn_pinpad:
                    startActivity(PinpadActivity.class);
                    break;
                case R.id.btn_print:
                    startActivity(PrintActivity.class);
                    break;
                case R.id.btn_Scanner:
                    startActivity(ScanActivity.class);
                    break;
                case R.id.btn_Scanner_custom:
                    startActivity(ScanCustomActivity.class);
                    break;
                case R.id.btn_MagCardReader:
                    startActivity(MagCardReaderActivity.class);
                    break;
                case R.id.btn_InsertCardReader:
                    startActivity(ICReaderActivity.class); //POS
//                    startActivity(ICReaderActivity_PDA.class); //PDA
                    break;
                case R.id.btn_sle:
                    startActivity(ICReaderSLEActivity.class);
                    break;
                case R.id.btn_RFCardReader:
                    startActivity(RFReaderActivity.class);
                    break;
                case R.id.btn_Beeper:
                    startActivity(BeeperActivity.class);
                    break;
                case R.id.btn_Led:
                    startActivity(LedActivity.class);
                    break;
                case R.id.btn_serialport:
                    startActivity(SerialPortActivity.class);
                    break;
                case R.id.btn_install:
                    startActivity(SilentInstallActivity.class);
                    break;
                case R.id.btn_system:
                    startActivity(SystemActivity.class);
                    break;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            showMessage("Exception:" + e.getMessage());
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e(TAG, "dispatchKeyEvent: keyCode=" + event.getKeyCode() + ", action=" + event.getAction());
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        Log.e(TAG, "onKeyLongPress: keyCode=" + event.getKeyCode() + ", action=" + event.getAction());
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Log.e(TAG, "onBackPressed");
        super.onBackPressed();
    }

    private void startActivity(Class<?> clsName) {
        Intent intent = new Intent(this, clsName);
        startActivity(intent);
        overridePendingTransition(R.anim.dync_in_from_right, R.anim.dync_out_to_left);
    }

}
