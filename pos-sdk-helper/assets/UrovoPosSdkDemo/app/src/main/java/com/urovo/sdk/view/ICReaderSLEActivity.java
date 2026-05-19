package com.urovo.sdk.view;

import android.device.IccManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.urovo.sdk.R;
import com.urovo.sdk.pollingcard.SLEInsertCardHandlerImpl;
import com.urovo.sdk.utils.BytesUtil;


public class ICReaderSLEActivity extends BaseActivity implements View.OnClickListener {

    private EditText editText_data;
    private byte cardType = SLEInsertCardHandlerImpl.CardType.SLE4428;
    private int passwordMode = SLEInsertCardHandlerImpl.PasswordMode.Verify;
    private RadioGroup radioGroup_type;
    private RadioGroup radioGroup_password;
    private EditText editText_address;
    private EditText editText_password;

    private IccManager mIccManager;
    private SLEInsertCardHandlerImpl mSleInsertCardHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icreader_sle);
        initView();
        editText_address = (EditText) findViewById(R.id.editText_address);
        editText_password = (EditText) findViewById(R.id.editText_password);
        editText_data = (EditText) findViewById(R.id.editText_data);
        radioGroup_type = (RadioGroup) findViewById(R.id.radioGroup_type);
        radioGroup_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_sle4428:
                        cardType = SLEInsertCardHandlerImpl.CardType.SLE4428;
                        break;
                    case R.id.radio_sle4436:
                        cardType = SLEInsertCardHandlerImpl.CardType.SLE4436;
                        break;
                    case R.id.radio_sle4442:
                        cardType = SLEInsertCardHandlerImpl.CardType.SLE4442;
                        break;
                }
            }
        });
        radioGroup_password = (RadioGroup) findViewById(R.id.radioGroup_password);
        radioGroup_password.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_verifyPassword:
                        passwordMode = SLEInsertCardHandlerImpl.PasswordMode.Verify;
                        break;
                    case R.id.radio_changePassword:
                        passwordMode = SLEInsertCardHandlerImpl.PasswordMode.Change;
                        break;
                }
            }
        });
        button_clearAPDU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_data.setText("");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSleInsertCardHandler = SLEInsertCardHandlerImpl.getInstance();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSearch();
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.btnSearchCard:
                    searchCard();
                    break;
                case R.id.btnPassword:
                    password();
                    break;
                case R.id.btnRead:
                    read();
                    break;
                case R.id.btnWrite:
                    write();
                    break;
                case R.id.btnStopSearch:
                    stopSearch();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchCard() {
        outputText("=====searchCard=====");
        if (mSleInsertCardHandler == null) {
            mSleInsertCardHandler = SLEInsertCardHandlerImpl.getInstance();
        }
        mSleInsertCardHandler.searchCard(30, cardType, new SLEInsertCardHandlerImpl.SLESearchListener() {

            @Override
            public void onCardInsert(byte[] atr) {
                outputText("onCardInsert");
                outputText("atr:" + BytesUtil.bytes2HexString(atr));
                mIccManager = mSleInsertCardHandler.mIccManager;
            }

            @Override
            public void onFail(int error, String message) {
                outputColorText(TextColor.RED, "onFail: error=" + error + ", message=" + message);
            }

            @Override
            public void onTimeout() {
                outputColorText(TextColor.RED, "onTimeout");
            }
        });
    }

    private void stopSearch() {
        outputText("=====stopSearch=====");
        mSleInsertCardHandler.stopSearch();
    }

    private boolean password() {
        outputText("=====password=====");
        if (mIccManager == null) {
            outputColorText(TextColor.RED, "mIccManager is null");
            return false;
        }
        String passwordStr = editText_password.getText().toString().trim();
        if (TextUtils.isEmpty(passwordStr)) {
            passwordStr = "FFFFFF";
        }
        byte[] password = BytesUtil.hexString2Bytes(passwordStr);
        int ret = -1;
        if (cardType == SLEInsertCardHandlerImpl.CardType.SLE4428) {
            ret = mIccManager.sle4428_password(passwordMode, password);
            outputColorText((ret == 0) ? TextColor.BLUE : TextColor.BLUE, "sle4428_password:" + ret);
        } else if (cardType == SLEInsertCardHandlerImpl.CardType.SLE4436) {
            ret = mIccManager.sle4436_verifyPassword(password);
            outputColorText((ret == 0) ? TextColor.BLUE : TextColor.BLUE, "sle4436_verifyPassword:" + ret);
        } else if (cardType == SLEInsertCardHandlerImpl.CardType.SLE4442) {
            if (passwordMode == SLEInsertCardHandlerImpl.PasswordMode.Change) {
                ret = mIccManager.sle4442_changePassword(password);
                outputColorText((ret == 0) ? TextColor.BLUE : TextColor.BLUE, "sle4442_changePassword:" + ret);
            } else {
                ret = mIccManager.sle4442_verifyPassword(password);
                outputColorText((ret == 0) ? TextColor.BLUE : TextColor.BLUE, "sle4442_verifyPassword:" + ret);
            }
        }

        return ret == 0;
    }

    private void read() {
        outputText("=====read=====");
        if (mIccManager == null) {
            outputColorText(TextColor.RED, "mIccManager is null");
            return;
        }
        String addressStr = editText_address.getText().toString().trim();
        if (TextUtils.isEmpty(addressStr)) {
            outputColorText(TextColor.RED, "Please input address");
            return;
        }
        int address = Integer.parseInt(addressStr);
        if (cardType == SLEInsertCardHandlerImpl.CardType.SLE4428) {
            //address: 0-0x3FF.
            //length: 1-0x400
            byte[] data = mIccManager.sle4428_readMemory(address, 1);
            outputColorText((data != null) ? TextColor.BLUE : TextColor.RED,
                    (data != null) ? "sle4428_readMemory:" + BytesUtil.bytes2HexString(data) : "Read sle4428 memory failed");
        } else if (cardType == SLEInsertCardHandlerImpl.CardType.SLE4436) {
            //address: 0-112.
            //length: 1-112.
            byte[] data = mIccManager.sle4436_readMemory(address, 1);
            outputColorText((data != null) ? TextColor.BLUE : TextColor.RED,
                    (data != null) ? "sle4428_readMemory:" + BytesUtil.bytes2HexString(data) : "Read sle4436 memory failed");
        } else if (cardType == SLEInsertCardHandlerImpl.CardType.SLE4442) {
            //address: 0-255.
            //length: 1-256.
            byte[] data = mIccManager.sle4442_readMainMemory(address, 1);
            outputColorText((data != null) ? TextColor.BLUE : TextColor.RED,
                    (data != null) ? "sle4442_readMemory:" + BytesUtil.bytes2HexString(data) : "Read sle4442 memory failed");
        }
    }

    private void write() {
        outputText("=====write=====");
        if (mIccManager == null) {
            outputColorText(TextColor.RED, "mIccManager is null");
            return;
        }
        String addressStr = editText_address.getText().toString().trim();
        if (TextUtils.isEmpty(addressStr)) {
            outputColorText(TextColor.RED, "Please input address");
            return;
        }
        String dataStr = editText_data.getText().toString().trim();
        if (TextUtils.isEmpty(addressStr)) {
            outputColorText(TextColor.RED, "Please input data");
            return;
        }
        int address = Integer.parseInt(addressStr);
        byte[] data = BytesUtil.hexString2Bytes(dataStr);
        int ret = -1;
        if (cardType == SLEInsertCardHandlerImpl.CardType.SLE4428) {
            //address: 0-0x3FF.
            //length: 1-0x400
            ret = mIccManager.sle4428_writeMemory(address, data, data.length);
            outputColorText((ret == 0) ? TextColor.BLUE : TextColor.RED, "sle4428_writeMemory:" + ret);
        } else if (cardType == SLEInsertCardHandlerImpl.CardType.SLE4436) {
            //address: 0-112.
            //length: 1-112.
            ret = mIccManager.sle4436_writeMemory(address, data, data.length);
            outputColorText((ret == 0) ? TextColor.BLUE : TextColor.RED, "sle4436_writeMemory:" + ret);
        } else if (cardType == SLEInsertCardHandlerImpl.CardType.SLE4442) {
            //address: 0-255.
            //length: 1-256.
            ret = mIccManager.sle4442_writeMainMemory(address, data, data.length);
            outputColorText((ret == 0) ? TextColor.BLUE : TextColor.RED, "sle4442_writeMainMemory:" + ret);
        }
    }

}
