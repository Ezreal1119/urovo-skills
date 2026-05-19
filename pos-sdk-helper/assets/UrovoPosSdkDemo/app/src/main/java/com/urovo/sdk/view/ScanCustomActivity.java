package com.urovo.sdk.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.urovo.sdk.R;
import com.urovo.sdk.scanner.InnerScannerCustomImpl;
import com.urovo.sdk.scanner.listener.ScannerListener;
import com.urovo.sdk.scanner.utils.Constant;
import com.urovo.sdk.utils.BytesUtil;
import com.urovo.sdk.utils.DateUtil;

import java.util.Date;

public class ScanCustomActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llScan;
    private FrameLayout layout_scan;
    private ScrollView scrollView;
    private CheckBox checkbox_flash;
    private CheckBox checkbox_disableQR;
    private RadioGroup radioGroup_camera;
    private int mCameraId = Constant.CameraID.BACK;
    private String[] codeType_disable = null;
    public InnerScannerCustomImpl mInnerScanner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_custom);
        initView();

        checkbox_flash = findViewById(R.id.checkbox_flash);
        checkbox_flash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkbox_flash.setText(isChecked ? "Turn Off" : "Turn On");
                mInnerScanner.switchFlash(isChecked);
            }
        });

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

        scrollView = findViewById(R.id.scrollView);
        layout_scan = findViewById(R.id.layout_scan);

        llScan = findViewById(R.id.ll_scan);
        mInnerScanner = InnerScannerCustomImpl.getInstance(ScanCustomActivity.this);
        if (!mInnerScanner.isFlashSupported(mCameraId)) {
            checkbox_flash.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartScan:
                scrollView.setVisibility(View.GONE);
                layout_scan.setVisibility(View.VISIBLE);

                outputText("startScan,please scan the qr code in 30 seconds: " + DateUtil.getDateTime(new Date()));
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.Scankey.cameraId, mCameraId);
                bundle.putInt(Constant.Scankey.timeOut, 30);
                //disable some code types
                if (codeType_disable != null && codeType_disable.length > 0) {
                    bundle.putStringArray(Constant.Scankey.codeType_disable, codeType_disable);
                }
                mInnerScanner.startScan(ScanCustomActivity.this, llScan, bundle, new ScannerListener() {
                    @Override
                    public void onSuccess(String data, byte[] byData) {
                        scrollView.setVisibility(View.VISIBLE);
                        layout_scan.setVisibility(View.GONE);
                        outputText("onSuccess: " + data);
                        outputText("byData: " + BytesUtil.bytes2HexString(byData));
                    }

                    @Override
                    public void onError(int error, String message) {
                        scrollView.setVisibility(View.VISIBLE);
                        layout_scan.setVisibility(View.GONE);
                        outputText("onError: \n" + error + "\n" + message);
                    }

                    @Override
                    public void onTimeout() {
                        scrollView.setVisibility(View.VISIBLE);
                        layout_scan.setVisibility(View.GONE);
                        outputText("onTimeout");
                    }

                    @Override
                    public void onCancel() {
                        scrollView.setVisibility(View.VISIBLE);
                        layout_scan.setVisibility(View.GONE);
                        outputText("onCancel");
                    }
                });
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (layout_scan.getVisibility() == View.GONE) {
            super.onBackPressed();
            return;
        }
        mInnerScanner.stopScan();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
