package com.urovo.sdk.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.urovo.sdk.R;
import com.urovo.sdk.scanner.InnerScannerImpl;
import com.urovo.sdk.scanner.listener.ScannerListener;
import com.urovo.sdk.scanner.utils.Constant;
import com.urovo.sdk.utils.BytesUtil;
import com.urovo.sdk.utils.DateUtil;

import java.util.Date;

public class ScanActivity extends BaseActivity implements View.OnClickListener {

    public InnerScannerImpl mInnerScanner = null;
    private CheckBox checkbox_disableQR;
    private CheckBox checkbox_flash;
    private boolean flashEnable;
    private String[] codeType_disable = null;
    private RadioGroup radioGroup_camera;
    private int mCameraId = Constant.CameraID.BACK;

    public static final int TIMEOUT = 5000;
    // Why: keep a stable reference so delayed auto-stop can be canceled when user exits early.
    private final Runnable delayedStopScanRunnable = new Runnable() {
        @Override
        public void run() {
            if (mInnerScanner != null) {
                mInnerScanner.stopScan();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        initView();
        checkbox_disableQR = findViewById(R.id.checkbox_disableQR);
        checkbox_disableQR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    codeType_disable = new String[1];
                    codeType_disable[0] = Constant.CodeType.QR_CODE;
                } else {
                    codeType_disable = null;
                }
            }
        });

        checkbox_flash = findViewById(R.id.checkbox_flash);
        checkbox_flash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                flashEnable = isChecked;
            }
        });

        radioGroup_camera = findViewById(R.id.radioGroup_camera);
        radioGroup_camera.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_front:
                        mCameraId = Constant.CameraID.FRONT;
                        break;
                    case R.id.radio_back:
                        mCameraId = Constant.CameraID.BACK;
                        break;
                    case R.id.radio_top:
                        mCameraId = Constant.CameraID.TOP;
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mInnerScanner = InnerScannerImpl.getInstance(ScanActivity.this);
    }

    private void stopScan() {
        // Boundary control: avoid stacking multiple delayed stop tasks in rapid clicks.
        mHandler.removeCallbacks(delayedStopScanRunnable);
        mHandler.postDelayed(delayedStopScanRunnable, TIMEOUT);
    }

    private void cancelDelayedStopScanTask() {
        mHandler.removeCallbacks(delayedStopScanRunnable);
    }

    @Override
    protected void onDestroy() {
        // Cleanup path: ensure no delayed stop task survives Activity destruction.
        cancelDelayedStopScanTask();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.btnStartScan:
                    outputText("startScan,please scan the qr code in 30 seconds: " + DateUtil.getDateTime(new Date()));
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.Scankey.upPromptString, "upPromptString");
                    bundle.putString(Constant.Scankey.downPromptString, "downPromptString");
                    bundle.putString(Constant.Scankey.title, "title");
                    //disable some code types
                    if (codeType_disable != null && codeType_disable.length > 0) {
                        bundle.putStringArray(Constant.Scankey.codeType_disable, codeType_disable);
                    }
                    bundle.putBoolean(Constant.Scankey.flash_enable, flashEnable);
                    mInnerScanner.startScan(ScanActivity.this, bundle, mCameraId, 30, new ScannerListener() {
                        @Override
                        public void onSuccess(String data, byte[] byData) {
                            outputText("onSuccess: " + data);
                            outputText("byData: " + BytesUtil.bytes2HexString(byData));
                        }

                        @Override
                        public void onError(int error, String message) {
                            outputText("onError: \n" + error + "\n" + message);
                        }

                        @Override
                        public void onTimeout() {
                            outputText("onTimeout");
                        }

                        @Override
                        public void onCancel() {
                            outputText("onCancel");
                        }
                    });
                    break;
                case R.id.btnCloseScan:
                    outputText("stopScan");
                    outputText("startScan,please scan the qr code in 30 seconds: " + DateUtil.getDateTime(new Date()));
                    bundle = new Bundle();
                    bundle.putString(Constant.Scankey.upPromptString, "upPromptString");
                    bundle.putString(Constant.Scankey.downPromptString, "downPromptString");
                    bundle.putString(Constant.Scankey.title, "title");
                    mInnerScanner.startScan(ScanActivity.this, bundle, mCameraId, 30, new ScannerListener() {
                        @Override
                        public void onSuccess(String data, byte[] byData) {
                            // Cleanup path: user leaves within timeout, so auto-stop task must be canceled.
                            cancelDelayedStopScanTask();
                            outputText("onSuccess: " + data);
                            outputText("byData: " + BytesUtil.bytes2HexString(byData));
                        }

                        @Override
                        public void onError(int error, String message) {
                            // Cleanup path: user leaves within timeout, so auto-stop task must be canceled.
                            cancelDelayedStopScanTask();
                            outputText("onError: \n" + error + "\n" + message);
                        }

                        @Override
                        public void onTimeout() {
                            // Cleanup path: user leaves within timeout, so auto-stop task must be canceled.
                            cancelDelayedStopScanTask();
                            outputText("onTimeout");
                        }

                        @Override
                        public void onCancel() {
                            // Cleanup path: user leaves within timeout, so auto-stop task must be canceled.
                            cancelDelayedStopScanTask();
                            outputText("onCancel");
                        }
                    });
                    stopScan();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
