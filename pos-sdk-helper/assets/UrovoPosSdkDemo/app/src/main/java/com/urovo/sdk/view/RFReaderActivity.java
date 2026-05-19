package com.urovo.sdk.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.urovo.sdk.R;
import com.urovo.sdk.rfcard.RFCardHandlerImpl;
import com.urovo.sdk.rfcard.listener.RFSearchListener;
import com.urovo.sdk.rfcard.utils.CardInfo;
import com.urovo.sdk.rfcard.utils.Constant;
import com.urovo.sdk.utils.BytesUtil;

public class RFReaderActivity extends BaseActivity implements View.OnClickListener {

    private EditText editText_block;
    private EditText editText_block2;
    private EditText editText_apdu;
    private EditText editText_data;
    private RadioGroup radioGroup;
    private CheckBox checkBox;

    private int block = 1;
    private int block2 = 1;
    private int M1KeyType = 0; //0:KEY_A, 1:KEY_B.
    private boolean isMifareUltralight;
    public RFCardHandlerImpl rfReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfreader);
        initView();
        rfReader = RFCardHandlerImpl.getInstance();
        editText_block = (EditText) findViewById(R.id.editText_block);
        editText_block2 = (EditText) findViewById(R.id.editText_block2);
        editText_apdu = (EditText) findViewById(R.id.editText_apdu);
        editText_data = (EditText) findViewById(R.id.editText_data);
        button_clearAPDU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_apdu.setText("");
                editText_data.setText("");
            }
        });
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup_keyAlg);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_keyTypeA:
                        M1KeyType = 0;
                        break;
                    case R.id.radio_keyTypeB:
                        M1KeyType = 1;
                        break;
                }
            }
        });
        checkBox = (CheckBox) findViewById(R.id.checkBox_m1Ultralight);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.checkBox_m1Ultralight:
                        isMifareUltralight = isChecked;
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        try {
            rfReader.stopSearch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        Log.e(TAG, "dispatchKeyEvent: keyCode=" + event.getKeyCode() + ", action=" + event.getAction());
//        if (event.getAction() == KeyEvent.ACTION_UP) {
//            if (event.getKeyCode() == KeyEvent.KEYCODE_1) {
//                btnstopCard();
//                return false;
//            } else if (event.getKeyCode() == KeyEvent.KEYCODE_2) {
//                btnexchangeApdu();
//                return false;
//            } else if (event.getKeyCode() == KeyEvent.KEYCODE_3) {
//                btnexchangeApdu();
//                return false;
//            } else if (event.getKeyCode() == KeyEvent.KEYCODE_4) {
//                btnreadBlock();
//                return false;
//            } else if (event.getKeyCode() == KeyEvent.KEYCODE_5) {
//                btnwriteBlock();
//                return false;
//            } else if (event.getKeyCode() == KeyEvent.KEYCODE_6) {
//                btnM1Init();
//                return false;
//            } else if (event.getKeyCode() == KeyEvent.KEYCODE_7) {
//                btnincreaseValue();
//                return false;
//            } else if (event.getKeyCode() == KeyEvent.KEYCODE_8) {
//                btndecreaseValue();
//                return false;
//            } else if (event.getKeyCode() == KeyEvent.KEYCODE_9) {
//                btnM1Amount();
//                return false;
//            }
//        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onClick(View view) {
        boolean status;
        int iRet = -1;
        try {
            switch (view.getId()) {
                case R.id.btnsearchCard:
                    btnsearchCard();
                    break;
                case R.id.btnstopCard:
                    btnstopCard();
                    break;
                case R.id.btnexchangeApdu:
                    btnexchangeApdu();
                    break;
                case R.id.btnreadBlock:
                    btnreadBlock();
                    break;
                case R.id.btnwriteBlock:
                    btnwriteBlock();
                    break;
                case R.id.btnM1Init:
                    btnM1Init();
                    break;
                case R.id.btnM1Amount:
                    btnM1Amount();
                    break;
                case R.id.btnrestore:
                    btnrestore();
                    break;
                case R.id.btnincreaseValue:
                    btnincreaseValue();
                    break;
                case R.id.btndecreaseValue:
                    btndecreaseValue();
                    break;
                case R.id.btnIsExist:
                    btnIsExist();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnsearchCard() {
        try {
            outputText("Search Card");
            startSearchCard();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnstopCard() {
        try {
            outputText("Stop Search");
            rfReader.stopSearch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnexchangeApdu() {
        try {
            apduComm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnreadBlock() {
        try {
            outputText("Read Block");
            if (!authBlock(false)) {
                return;
            }
            byte[] response = new byte[16];
            int ret3 = rfReader.readBlock(block, response);
            outputText("readBlock:" + ret3);
            if (ret3 > 0) {
                byte[] result = new byte[ret3];
                System.arraycopy(response, 0, result, 0, ret3);
                outputText("result:" + ret3 + ",\nresult:" + BytesUtil.bytes2HexString(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnwriteBlock() {
        try {
            outputText("Write Block");
            if (!authBlock(false)) {
                return;
            }
            byte[] writeData = BytesUtil.hexString2Bytes("9999");
            String dataStr = editText_data.getText().toString().trim();
            if (!TextUtils.isEmpty(dataStr)) {
                writeData = BytesUtil.hexString2Bytes(dataStr.length() > 32 ? dataStr.substring(0, 32) : dataStr);
            }
            outputText("Write data:" + BytesUtil.bytes2HexString(writeData));
            int ret4 = rfReader.writeBlock(block, writeData);
            outputText("result:" + ret4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnM1Init() {
        try {
            outputText("M1 Amount Init");
            if (!authBlock(false)) {
                return;
            }
            outputText("m1_amount_init");
            int iRet = rfReader.m1_amount_init(block, 0);
            outputText("m1_amount_init:" + iRet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnM1Amount() {
        try {
            outputText("Read M1 Amount");
            if (!authBlock(false)) {
                return;
            }
            outputText("Read Amount");
            int iRet = rfReader.m1_amount_read(block);
            outputText("m1_amount_read:" + iRet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnrestore() {
        try {
            outputText("Restore M1 Amount");
            if (!authBlock(false)) {
                return;
            }
            if (!authBlock(true)) {
                return;
            }
            //need to verify two blocks before restore.
            int iRet = rfReader.m1_amount_restore(block);
            outputText("Restore:" + iRet);
            if (iRet == 0) {
                iRet = rfReader.m1_amount_transfer(block2);
                outputText("Transfer:" + iRet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnincreaseValue() {
        try {
            outputText("Increase Value");
            if (!authBlock(false)) {
                return;
            }
            int iRet = rfReader.increaseValue(block, 1);
            outputText("Increase:" + iRet);
            if (iRet == 0) {
                iRet = rfReader.m1_amount_transfer(block);
                outputColorText(iRet == 0 ? TextColor.BLUE : TextColor.RED, "Transfer:" + iRet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btndecreaseValue() {
        try {
            outputText("Decrease Value");
            if (!authBlock(false)) {
                return;
            }
            int iRet = rfReader.decreaseValue(block, 1);
            outputText("Decrease" + iRet);
            if (iRet == 0) {
                iRet = rfReader.m1_amount_transfer(block);
                outputText("Transfer:" + iRet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void btnIsExist() {
        try {
            outputText("isExist");
            boolean iRet = rfReader.isExist();
            outputText("isExist:" + iRet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startSearchCard() {
        try {
            rfReader.searchCard(new RFSearchListener() {
                @Override
                public void onCardPass(int cardType, byte[] UID, CardInfo cardInfo) {
                    outputText("onCardPass");
                    outputText("MODE:" + Integer.toHexString(cardInfo.MODE));
                    outputText("SAK:" + Integer.toHexString(cardInfo.SAK));
                    outputText("ATQA:" + BytesUtil.bytes2HexString(cardInfo.ATQA));
                    outputText("UID:" + BytesUtil.bytes2HexString(cardInfo.UID));
                }

                @Override
                public void onFail(int error, String message) {
                    outputColorText(TextColor.RED, "onFail: error=" + error + ", message:" + message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void apduComm() {
        try {
            String apduStr = editText_apdu.getText().toString().trim();
            if (TextUtils.isEmpty(apduStr)) {
                if (isMifareUltralight) {
                    apduStr = "1A00";
                } else {
                    //PSE
                    apduStr = "00A404000E325041592E5359532E444446303100";
                }
            }
            byte[] cmd = BytesUtil.hexString2Bytes(apduStr);
            outputText("APDU:" + BytesUtil.bytes2HexString(cmd));
            byte[] rspData;
            long startTime = System.currentTimeMillis();
            if (isMifareUltralight) {
                rspData = rfReader.exchangeApdu_M1(cmd, 1, 0);
            } else {
                rspData = rfReader.exchangeApdu(cmd);
            }
            long endTime = System.currentTimeMillis();
            if (rspData != null && rspData.length >= 0) {
                outputText("time:" + (endTime - startTime) + "\nResult:" + BytesUtil.bytes2HexString(rspData));
                Log.e(TAG, BytesUtil.bytes2HexString(rspData));
            } else {
                outputColorText(TextColor.RED, "APDU Failed: null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean authBlock(boolean isBlock2) {
        boolean status = false;
        try {
            String blockNo = "";
            if (isBlock2) {
                blockNo = editText_block2.getText().toString().trim();
            } else {
                blockNo = editText_block.getText().toString().trim();
            }
            if (TextUtils.isEmpty(blockNo)) {
                showMessage(isBlock2 ? "Input the block" : "Input the block2");
                return false;
            }
            if (isBlock2) {
                block2 = Integer.parseInt(blockNo);
            } else {
                block = Integer.parseInt(blockNo);
            }
            byte[] key = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
            String passwordStr = editText_apdu.getText().toString().trim();
            if (!TextUtils.isEmpty(passwordStr)) {
                key = BytesUtil.hexString2Bytes(passwordStr);
            }
            int ret = rfReader.authBlock(isBlock2 ? block2 : block, M1KeyType == 0 ? Constant.KeyType.KEY_A : Constant.KeyType.KEY_B, key);
            if (ret == 0) {
                outputText("(block=" + (isBlock2 ? block2 : block) + ") Auth:" + ret);
            } else {
                outputColorText(TextColor.RED, "(block=" + (isBlock2 ? block2 : block) + ") Auth Failed:" + ret);
            }
            return ret == 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

}
