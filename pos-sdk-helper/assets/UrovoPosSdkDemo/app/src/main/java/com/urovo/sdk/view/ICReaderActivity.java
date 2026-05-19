package com.urovo.sdk.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.urovo.sdk.R;
import com.urovo.sdk.insertcard.InsertCardHandlerImpl;
import com.urovo.sdk.insertcard.utils.Constant;
import com.urovo.sdk.utils.BytesUtil;


public class ICReaderActivity extends BaseActivity implements View.OnClickListener {

    private InsertCardHandlerImpl icReader;
    private EditText editText_apdu;
    private byte cardType = Constant.Mode.MODE_USER;
    private RadioGroup radioGroup_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icreader);
        initView();
        icReader = InsertCardHandlerImpl.getInstance();
        editText_apdu = (EditText) findViewById(R.id.editText_apdu);
        radioGroup_type = (RadioGroup) findViewById(R.id.radioGroup_type);
        radioGroup_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_ic:
                        cardType = Constant.Mode.MODE_USER;
                        break;
                    case R.id.radio_psam1:
                        cardType = Constant.Mode.MODE_PSAM1;
                        break;
                    case R.id.radio_psam2:
                        cardType = Constant.Mode.MODE_PSAM2;
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
    }

    @Override
    protected void onDestroy() {
        try {
            icReader.powerDown(cardType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        try {
            boolean status = false;
            switch (view.getId()) {
                case R.id.btnpowerUp:
                    outputText("powerUp");

                    //For multiple psam cards of different baud rate.
//                    int baudrate = 2; //0-9600, 1-19200, 2-38400.
//                    if (cardType == Constant.Mode.MODE_PSAM1 || cardType == Constant.Mode.MODE_PSAM2) {
//                        int ret = icReader.setPsamParameter(cardType, 1, baudrate, 1, 1);
//                        outputText("setPsamParameter:" + ret);
//                    }

                    byte[] atrData = new byte[64];
                    int ret = icReader.powerUp(cardType, atrData);
                    outputText("result:" + (ret > 0));
                    if (ret > 0) {
                        byte[] dataOut = new byte[ret];
                        System.arraycopy(atrData, 0, dataOut, 0, ret);
                        outputText("ATR:" + BytesUtil.bytes2HexString(dataOut));
                    }
                    break;
                case R.id.btnpowerDown:
                    outputText("powerDown");
                    status = icReader.powerDown(cardType);
                    outputText("" + status);
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
                    //PSE
                    byte[] apdu = BytesUtil.hexString2Bytes("00A404000E315041592E5359532E444446303100");
                    if (cardType != Constant.Mode.MODE_USER) {
                        apdu = BytesUtil.hexString2Bytes("0084000004");
                    }
                    if (!TextUtils.isEmpty(apduStr)) {
                        apdu = BytesUtil.hexString2Bytes(apduStr);
                    }
                    long startTime = System.currentTimeMillis();
                    byte[] rspData = icReader.exchangeApdu(cardType, apdu);
                    long endTime = System.currentTimeMillis();
                    outputText("time:" + (endTime - startTime) + "\nResult:" + BytesUtil.bytes2HexString(rspData));
                    Log.e(TAG, BytesUtil.bytes2HexString(rspData));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
