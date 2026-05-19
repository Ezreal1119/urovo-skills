package com.urovo.sdk.view;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.urovo.sdk.R;
import com.urovo.sdk.utils.BytesUtil;
import com.urovo.serial.common.GlobalConstant;
import com.urovo.serial.utils.SerialPortListener;
import com.urovo.serial.utils.SerialPortTool;

public class SerialPortActivity extends BaseActivity implements OnClickListener {

    public static final String TAG = "SerialPortActivity";
    private EditText editText_data;
    private EditText editText_path;
    private SerialPortTool mSerialPortTool;

    protected List<String> mSerialPortList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serialport);
        initView();

        editText_data = (EditText) findViewById(R.id.editText_data);
        editText_path = (EditText) findViewById(R.id.editText_path);
        button_clearAPDU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_data.setText("");
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        try {
            switch (v.getId()) {
                case R.id.btnOpen:
                    outputText("open");
                    String path = editText_path.getText().toString().trim();
                    if (TextUtils.isEmpty(path)) {
                        showMessage("Input Serial Path");
                        outputColorText(TextColor.BLUE, "Input Serial Path");
                        return;
                    }
                    mSerialPortList.clear();
                    mSerialPortTool = new SerialPortTool();
                    for (int i = 0; i <= 9; i++) {
                        mSerialPortList.add(path.substring(0, path.length() - 1) + i);
                    }
                    int status = mSerialPortTool.openSerialPort(mSerialPortList, 115200);
                    outputColorText((status == 0) ? TextColor.BLUE : TextColor.RED, "open:" + status + ", " + GlobalConstant.getErrorMessage(status));
                    if (status == 0) {
                        mSerialPortTool.setOnListener(listener);
                    }
                    break;
                case R.id.btnClose:
                    mSerialPortList.clear();
                    if (mSerialPortTool != null) {
                        status = mSerialPortTool.close();
                        outputText("close:" + status);
                        mSerialPortTool = null;
                    }
                    break;
                case R.id.btnSend:
                    try {
                        if (mSerialPortTool == null) {
                            outputColorText(TextColor.RED, "Please open first");
                            return;
                        }
                        String cmd = editText_data.getText().toString().trim();
                        byte[] writeBuff = BytesUtil.hexString2Bytes(cmd);
                        outputText("write:" + BytesUtil.bytes2HexString(writeBuff));
                        String result = mSerialPortTool.sendData(writeBuff, writeBuff.length);
                        if (!TextUtils.isEmpty(result)) {
                            outputColorText(TextColor.RED, "" + result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    SerialPortListener listener = new SerialPortListener() {

        @Override
        public void onFail(String code, String msg) {
            outputText("onFail: code=" + code + "msg=" + msg);
        }

        @Override
        public void onReceive(byte[] data) {
            String result = BytesUtil.bytes2HexString(data);
            Log.e(TAG, "received data:" + result);
            outputText("received data:" + result);
        }
    };

}
