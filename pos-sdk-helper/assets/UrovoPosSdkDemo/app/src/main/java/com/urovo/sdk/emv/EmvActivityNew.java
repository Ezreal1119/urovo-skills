package com.urovo.sdk.emv;

import android.app.ProgressDialog;
import android.device.DeviceManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.gson.Gson;
import com.urovo.file.logfile;
import com.urovo.i9000s.api.emv.ContantPara;
import com.urovo.i9000s.api.emv.EmvListener;
import com.urovo.i9000s.api.emv.EmvNfcKernelApi;
import com.urovo.i9000s.api.emv.Funs;
import com.urovo.sdk.R;
import com.urovo.sdk.beeper.BeeperImpl;
import com.urovo.sdk.emvlogupload.listener.LogUploadCallBack;
import com.urovo.sdk.emvlogupload.model.LogUploadResponse;
import com.urovo.sdk.pinpad.PinPadProviderImpl;
import com.urovo.sdk.pinpad.listener.OfflinePinInputListener;
import com.urovo.sdk.pinpad.listener.PinInputListener;
import com.urovo.sdk.pinpad.utils.Constant;
import com.urovo.sdk.pinpad.utils._3DES;
import com.urovo.sdk.utils.ByteUtils;
import com.urovo.sdk.utils.BytesUtil;
import com.urovo.sdk.utils.EmvLogUtil;
import com.urovo.sdk.view.BaseActivity;
import com.urovo.sdk.view.PinpadActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class EmvActivityNew extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "MainActivity===>";

    private CheckBox checkbox_enableEmvLog;
    public EmvNfcKernelApi mEmvNfcKernelApi = null;
    public static EmvListener mEmvListener = null;
    private Bundle pinpadBundle = null;
    private Bundle emvPinBundle = null;
    private String PAN;
    public boolean PINPAD_DUKPT = false;
    public boolean NEED_ONLINE_PIN = true;
    private boolean needUploadEmvLog = false;

    public final static int INDEX_MK = PinpadActivity.INDEX_MK;
    public final static int INDEX_WK = PinpadActivity.INDEX_WK;
    public final static String plainMainKey = PinpadActivity.plainMainKey;
    public final static String plainPinKey = PinpadActivity.plainPinKey;

    public final static int INDEX_DUKPT = 1;
    private final static byte[] bdkBuff = PinpadActivity.bdkBuff;
    private final static byte[] ksnBuff_msr = PinpadActivity.ksnBuff_msr;
    private PinPadProviderImpl mPinPadProvider = PinPadProviderImpl.getInstance();

    private int cardReadMode; //1-swipe, 2-contact, 3-contactless.

    public static final int MESSAGE_PROGRESS_SHOW = 0x101;
    public static final int MESSAGE_PROGRESS_DISMISS = 0x102;
    public static final int MESSAGE_CARD_MSG = 0x02;
    public static final int MESSAGE_CARD_ICC = 0x03;
    public static final int MESSAGE_CARD_PICC = 0x04;

    public final static int ionRequestSetAmount = 5;
    public final static int ionProcessMAG = 6;
    public final static int ionProcessICC = 7;
    public final static int ionProcessNFC = 8;
    public final static int ionRequestOnlineProcess = 10;
    public final static int ionRequestPinEntry = 11;
    public final static int ionRequestOfflinePINVerify = 12;
    public final static int manyApp = 13;
    public final static int iOnReturnUpdateTerminalSettingResult = 14;
    public final static int iNeedFallBack = 15;
    public final static int iCancelCheckCard = 16;
    public final static int ionError = 17;
    public final static int ionReturnIssuerScriptResult_SUCCESS = 18;
    public final static int ionReturnIssuerScriptResult_Failed = 19;
    public final static int onOnlineApproved = 20;
    public final static int onOfflineApproved = 21;
    public final static int onOnlineDeclined = 22;
    public final static int onOfflineDeclined = 23;
    public final static int onReturnIssuerScriptResult_NULL = 24;
    public final static int onNFCrequestTipsConfirm = 25;
    public final static int ionNFCTransResult = 26;
    public final static int afterPinEntry = 27;
    public final static int onCanceled = 29;
    public final static int ionErrorAmount = 30;
    public final static int offlinePin_retry = 31;
    public final static int badSwipe = 32;
    public final static int chipCardSwipe = 33;
    public final static int ChipInsertError = 34;
    public final static int ICSChipInsertError = 35;
    public final static int insertCard = 36;
    public final static int iOTHERINTERFACES = 38;
    public final static int iTRY_AGAIN_RESENT_CARD = 40;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_PROGRESS_SHOW:
                    String message = (String) msg.obj;
                    break;
                case MESSAGE_PROGRESS_DISMISS:
                    break;
                case MESSAGE_CARD_MSG:
                    BeeperImpl.getInstance().startBeep(1, 100);
                    break;
                case MESSAGE_CARD_ICC:
                case MESSAGE_CARD_PICC:
                    sendHandlerMessage(MESSAGE_PROGRESS_SHOW, "Card Reading...");
                    break;
                case insertCard:
                    BeeperImpl.getInstance().startBeep(2, 50);
                    StartKernel(ContantPara.CheckCardMode.INSERT);
                    break;
                case iNeedFallBack:
                    BeeperImpl.getInstance().startBeep(2, 50);
                    StartKernel(ContantPara.CheckCardMode.SWIPE);
                    break;
                case iOTHERINTERFACES:
                    BeeperImpl.getInstance().startBeep(2, 50);
                    StartKernel(ContantPara.CheckCardMode.SWIPE_OR_TAP);
                    break;
                case ionRequestPinEntry:
                    sendHandlerMessage(MESSAGE_PROGRESS_DISMISS, "");
//                    BeeperImpl.getInstance().startBeep(1, 100);
                    startPinInput(true, true, 0, false, null, "");
                    break;
                case ionRequestOfflinePINVerify:
                    sendHandlerMessage(MESSAGE_PROGRESS_DISMISS, "");
//                    BeeperImpl.getInstance().startBeep(1, 100);
                    OfflinePinBean pinBean = (OfflinePinBean) msg.obj;
                    int pinEntryType = pinBean.getPinEntryType();
                    int retryTimes = pinBean.getRetryTimes();
                    message = "Enter Offline PIN\nPin Try Times: " + retryTimes;
                    startPinInput(true, false, pinEntryType, retryTimes == 1, emvPinBundle, message);
                    break;
                case offlinePin_retry:
                    sendHandlerMessage(MESSAGE_PROGRESS_DISMISS, "");
//                    BeeperImpl.getInstance().startBeep(1, 100);
                    pinBean = (OfflinePinBean) msg.obj;
                    pinEntryType = pinBean.getPinEntryType();
                    retryTimes = pinBean.getRetryTimes();
                    if (retryTimes == 1) {
                        message = "Re-enter Offline PIN\nLast Pin Try";
                    } else {
                        message = "Re-enter Offline PIN\nPin Try Times: " + retryTimes;
                    }
                    startPinInput(false, false, pinEntryType, retryTimes == 1, emvPinBundle, message);
                    break;
                case ionRequestOnlineProcess:
                case onOfflineApproved:
                    sendHandlerMessage(MESSAGE_PROGRESS_DISMISS, "");
//                    BeeperImpl.getInstance().startBeep(1, 100);
                    getTrack2();
                    createField55();
                    //1. Send online request to the host.
                    //2. After the host responds or Or the response failed, send the response result into the kernel(2nd GAC).
                    if (cardReadMode == CardTypeConstant.CONTACT) {
                        sendOnlineProcessResult(true);
                    }
                    break;
                case ionNFCTransResult:
                case ionError:
                    try {
                        sendHandlerMessage(MESSAGE_PROGRESS_DISMISS, "");
                        BeeperImpl.getInstance().startBeep(2, 50);
                        outputColorText(TextColor.RED, "" + msg.obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emv_new);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPinKey();
        initEMV();
    }

    @Override
    public void initView() {
        super.initView();
        checkbox_enableEmvLog = findViewById(R.id.checkbox_emv_log);
        checkbox_enableEmvLog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEmvNfcKernelApi.LogOutEnable(isChecked ? 1 : 0);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_emv_start:
                outputText("Present your card");
                showMessage("Present your card");
                StartKernel(ContantPara.CheckCardMode.SWIPE_OR_INSERT_OR_TAP);
                break;
            case R.id.btn_emv_stop:
                outputText("Stop Card");
                mEmvNfcKernelApi.abortKernel();
                break;
            case R.id.btn_emv_aidcapk:
                outputText("updateEMVParams");
                updateEMVParams();
                break;
            case R.id.btn_emv_log:
                boolean ret = EmvNfcKernelApi.exportLogFilesToExternalStorage(this);
                outputColorText(ret ? TextColor.BLUE : TextColor.RED, "Export EMV Log " + (ret ? "Successful" : "Failed"));
                break;
            case R.id.btn_delete_log:
                ret = EmvNfcKernelApi.deleteLogFiles(this);
                outputColorText(ret ? TextColor.BLUE : TextColor.RED, "Delete EMV Log " + (ret ? "Successful" : "Failed"));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        abortEMV();
        super.onDestroy();
    }

    public void initEMV() {
        Log.e(TAG, "initEMV");
        mEmvNfcKernelApi = EmvNfcKernelApi.getInstance(this);
        mEmvListener = new MyEmvListener();
        mEmvNfcKernelApi.setListener(mEmvListener);
        mEmvNfcKernelApi.setContext(this);
        //0-disable kernel log  1-enable kernel log
        //Need to disable the kernel log in production.
        mEmvNfcKernelApi.LogOutEnable(0);
    }

    public void abortEMV() {
        Log.e(TAG, TAG + "abortEMV");
        try {
            if (mEmvNfcKernelApi != null) {
                mEmvNfcKernelApi.abortKernel();
            }
            if (cardThread != null) {
                cardThread.interrupt();
            }
            cardThread = null;

            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initPinKey() {
        if (PINPAD_DUKPT) {
            int ret = mPinPadProvider.DukptGetKsn(INDEX_DUKPT, new byte[10]);
            if (ret != 0) {
                ret = mPinPadProvider.downloadKeyDukpt(INDEX_DUKPT, bdkBuff, bdkBuff.length, ksnBuff_msr, ksnBuff_msr.length, null, 0);
                outputText("downloadKeyDukpt:" + ret);
            }
        } else {
            if (!mPinPadProvider.isKeyExist(Constant.KeyType.PIN_KEY, INDEX_WK)) {
                byte[] plainMainKeyBuff = BytesUtil.hexString2Bytes(plainMainKey);
                byte[] plainPinKeyBuff = BytesUtil.hexString2Bytes(plainPinKey);
                boolean status = mPinPadProvider.loadMainKey(INDEX_MK, plainMainKeyBuff, null);
                Log.e(TAG, "loadMainKey:" + status);
                byte[] encPinKey = _3DES.ThreeDes_crypt16(plainMainKeyBuff, plainPinKeyBuff);
                boolean succ = mPinPadProvider.loadWorkKey(Constant.KeyType.PIN_KEY, INDEX_MK, INDEX_WK, encPinKey, null);
                Log.e(TAG, "loadMainKey:" + succ);
            }
        }
    }

    public void updateEMVParams() {
        sendHandlerMessage(MESSAGE_PROGRESS_SHOW, "Initializing EMV parameters...");
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    //Update the parameters required in actual use
                    mEmvNfcKernelApi.updateAID(ContantPara.Operation.CLEAR, null);
                    mEmvNfcKernelApi.updateCAPK(ContantPara.Operation.CLEAR, null);//
                    TestEmv.initEMV_AID_CAPK();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                outputText("Init AID/CAPK Success");
                sendHandlerMessage(MESSAGE_PROGRESS_DISMISS, "");
            }
        }.start();
    }

    private Thread cardThread = null;

    public void StartKernel(final ContantPara.CheckCardMode checkCardMode) {
        cardReadMode = 0;
        cardThread = new Thread() {
            public void run() {
                try {
                    Hashtable<String, Object> data = new Hashtable<String, Object>();
                    data.put("checkCardMode", checkCardMode);//
                    data.put("emvOption", ContantPara.EmvOption.START);  // START_WITH_FORCE_ONLINE
                    data.put("amount", "0");
                    data.put("cashbackAmount", "0");
                    data.put("checkCardTimeout", "30");// Check Card time out .Second
                    data.put("transactionType", "20"); //00-goods 01-cash 09-cashback 20-refund 15-balance
                    data.put("isEnterAmtAfterReadRecord", false);
                    data.put("FallbackSwitch", "0");//0- close fallback 1-open fallback
                    data.put("supportDRL", true); // support Visa DRL?
                    data.put("checkCardMode", checkCardMode);//
                    data.put("emvOption", ContantPara.EmvOption.START);  // START_WITH_FORCE_ONLINE
                    data.put("enableBeeper", true);

//                    data.put("refundProcessFlag", 1);//Opitional. 1-Redsys(Spain)
//                    data.put("prioritizedCandidateApp", "A0000007271010");
//                    data.put("enableUpdateDataByAid", false);
//                    data.put("enableEncMagStripe", "1");
//                    data.put("NeedFallBackTryTimes", "3");
//                    data.put("DisableCheckMSRFormat", "1");
//                    data.put("enableTransTypeMatchAID", false);
//                    data.put("MSRKeyIndex", "2");
//                    data.put("forceInputPIN",true) ;


                    data.put("currencyCode", "156"); //currencyCode for contact/contactless

                    String currencyCode = "5F2A020156"; //5F2A for contact
                    String countryCode = "9F1A020156";  //9F1A for contact/contactless
                    String TerminalCapabilities = "9F3303E0F8C8";
                    String terminalParameters = TerminalCapabilities + currencyCode + countryCode;
                    mEmvNfcKernelApi.updateTerminalParamters(ContantPara.CardSlot.ICC, terminalParameters);
                    mEmvNfcKernelApi.updateTerminalParamters(ContantPara.CardSlot.PICC, terminalParameters);
                    mEmvNfcKernelApi.startKernel(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        cardThread.start();
    }

    class MyEmvListener implements EmvListener {

        @Override
        public void onRequestSetAmount() {
            Log.e(TAG, TAG + "onRequestSetAmount");
            outputText("onRequestSetAmount");
            mEmvNfcKernelApi.setAmountEx(1L, 0L);
        }

        @Override
        public void onReturnCheckCardResult(ContantPara.CheckCardResult checkCardResult, Hashtable<String, String> hashtable) {
            Log.e(TAG, TAG + "onReturnCheckCardResult, checkCardResult:" + checkCardResult
                    + ",\nhashtable:" + hashtable.toString()
                    + ",\nPOS Entry Mode:" + hashtable.get("POSEntryMode"));
            if (checkCardResult == ContantPara.CheckCardResult.MSR) {
                outputText("onReturnCheckCardResult:" + checkCardResult);
                Bundle cardInfo = GetMagCardStrEMV(hashtable.get("StripInfo").toUpperCase());
                outputColorText(TextColor.BLACK, "TRACK1:" + cardInfo.getString(EMVDataConstant.TRACK1));
                outputColorText(TextColor.BLACK, "TRACK2:" + cardInfo.getString(EMVDataConstant.TRACK2));
                outputColorText(TextColor.BLACK, "TRACK3:" + cardInfo.getString(EMVDataConstant.TRACK3));
                outputColorText(TextColor.BLACK, "PAN:" + cardInfo.getString(EMVDataConstant.PAN));
                outputColorText(TextColor.BLACK, "EXPIRED DATE:" + cardInfo.getString(EMVDataConstant.EXPIRED_DATE));
                outputColorText(TextColor.BLACK, "SERVICE CODE:" + cardInfo.getString(EMVDataConstant.SERVICE_CODE));
                outputColorText(TextColor.BLACK, "CARD HOLDER NAME:" + cardInfo.getString(EMVDataConstant.CARD_HOLDER_NAME));
                cardReadMode = CardTypeConstant.SWIPE;
                sendHandlerMessage(MESSAGE_CARD_MSG, cardInfo);
            } else if (checkCardResult == ContantPara.CheckCardResult.INSERTED_CARD) {
                outputText("onReturnCheckCardResult:" + checkCardResult);
                cardReadMode = CardTypeConstant.CONTACT;
                sendHandlerMessage(MESSAGE_CARD_ICC, "");
            } else if (checkCardResult == ContantPara.CheckCardResult.TAP_CARD_DETECTED) {
                outputText("onReturnCheckCardResult:" + checkCardResult);
                cardReadMode = CardTypeConstant.CONTACTLESS;
                sendHandlerMessage(MESSAGE_CARD_PICC, "");
            } else if (checkCardResult == ContantPara.CheckCardResult.NEED_FALLBACK) {
                outputColorText(TextColor.RED, "onReturnCheckCardResult:" + checkCardResult);
                sendHandlerMessage(iNeedFallBack, "Please Swipe");
            } else if (checkCardResult == ContantPara.CheckCardResult.USE_ICC_CARD) {
                outputColorText(TextColor.RED, "onReturnCheckCardResult:" + checkCardResult);
                sendHandlerMessage(insertCard, "Please Insert Card");
            } else {
                outputColorText(TextColor.RED, "onReturnCheckCardResult:" + checkCardResult);
                sendHandlerMessage(ionError, checkCardResult + "");
                uploadEmvLog(checkCardResult + "", checkCardResult + "");
            }
        }

        @Override
        public void onRequestSelectApplication(ArrayList<String> arrayList) {
            Log.e(TAG, TAG + "onRequestSelectApplication, arrayList:" + arrayList.toString());
            outputText("onRequestSelectApplication:" + arrayList.toString());
            mEmvNfcKernelApi.selectApplication(0);
        }

        @Override
        public void onRequestPinEntry(ContantPara.PinEntrySource pinEntrySource) {
            Log.e(TAG, TAG + "onRequestPinEntry, pinEntrySource:" + pinEntrySource);
            outputText("onRequestPinEntry");
            if (NEED_ONLINE_PIN) {
                sendHandlerMessage(ionRequestPinEntry, "");
            } else {
                mEmvNfcKernelApi.sendPinEntry();
            }
        }

        @Override
        public void onRequestOfflinePinEntry(ContantPara.PinEntrySource pinEntrySource, int i) {
            Log.e(TAG, TAG + "onRequestOfflinePinEntry, pinEntrySource:" + pinEntrySource + ", i:" + i);
            outputText("onRequestOfflinePinEntry");
        }

        @Override
        public void onRequestConfirmCardno() {
            Log.e(TAG, TAG + "onRequestConfirmCardno");
            outputText("onRequestConfirmCardno");
            mEmvNfcKernelApi.sendConfirmCardnoResult(true);
        }

        @Override
        public void onRequestFinalConfirm() {
            Log.e(TAG, TAG + "onRequestFinalConfirm");
            outputText("onRequestFinalConfirm");
            mEmvNfcKernelApi.sendFinalConfirmResult(true);
        }

        @Override
        public void onRequestOnlineProcess(String cardTlvData, String dataKsn) {
            Log.e(TAG, TAG + "onRequestOnlineProcess, cardTlvData:" + cardTlvData + ", dataKsn:" + dataKsn);
            outputText("onRequestOnlineProcess");
            sendHandlerMessage(ionRequestOnlineProcess, "");
        }

        @Override
        public void onReturnBatchData(String s) {
            Log.e(TAG, TAG + "onReturnBatchData, s:" + s);
            outputText("onReturnBatchData:" + s);
        }

        @Override
        public void onReturnTransactionResult(ContantPara.TransactionResult transactionResult) {
            Log.e(TAG, TAG + "onReturnTransactionResult, transactionResult:" + transactionResult);
            if (transactionResult == ContantPara.TransactionResult.ONLINE_APPROVAL) {
                outputText("onReturnTransactionResult:" + transactionResult);
                sendHandlerMessage(onOnlineApproved, "");
            } else if (transactionResult == ContantPara.TransactionResult.OFFLINE_APPROVAL) {
                outputText("onReturnTransactionResult:" + transactionResult);
                sendHandlerMessage(onOfflineApproved, "");
            } else {
                outputColorText(TextColor.RED, "onReturnTransactionResult:" + transactionResult);
                sendHandlerMessage(ionError, "" + transactionResult);
                uploadEmvLog(transactionResult + "", transactionResult + "");
            }
        }

        @Override
        public void onRequestDisplayText(ContantPara.DisplayText displayText) {
            Log.e(TAG, TAG + "onRequestDisplayText, displayText:" + displayText);
            outputText("onRequestDisplayText:" + displayText);
        }

        @Override
        public void onRequestOfflinePINVerify(ContantPara.PinEntrySource pinEntrySource, int pinEntryType, Bundle bundle) {
            Log.e(TAG, TAG + "onRequestOfflinePINVerify, pinEntrySource:" + pinEntrySource + ", pinEntryType:" + pinEntryType);
            outputText("onRequestOfflinePINVerify");
            String AID = mEmvNfcKernelApi.getValByTag(0x84).toUpperCase();
            Log.e(TAG, TAG + "onRequestOfflinePINVerify AID:" + AID);
            if (bundle == null) {
                bundle = new Bundle();
            }
            int pinTryTimes = mEmvNfcKernelApi.getOfflinePinTryTimes();
            Log.e(TAG, TAG + "getOfflinePinTryTimes:" + pinTryTimes);
            emvPinBundle = bundle;
            emvPinBundle.putInt("pinEntryType", pinEntryType);
            OfflinePinBean pinBean = new OfflinePinBean();
            pinBean.setPinEntryType(pinEntryType);
            pinBean.setRetryTimes(pinTryTimes);
            sendHandlerMessage(ionRequestOfflinePINVerify, pinBean);
        }

        @Override
        public void onReturnIssuerScriptResult(ContantPara.IssuerScriptResult issuerScriptResult, String s) {
            Log.e(TAG, TAG + "onReturnIssuerScriptResult, issuerScriptResult:" + issuerScriptResult + ", s:" + s);
            outputText("onReturnIssuerScriptResult");
        }

        @Override
        public void onNFCrequestTipsConfirm(ContantPara.NfcTipMessageID nfcTipMessageID, String s) {
            Log.e(TAG, TAG + "onNFCrequestTipsConfirm, nfcTipMessageID:" + nfcTipMessageID + ", s:" + s);
            outputText("onNFCrequestTipsConfirm:" + nfcTipMessageID);
        }

        @Override
        public void onReturnNfcCardData(Hashtable<String, String> hashtable) {
            Log.e(TAG, TAG + "onReturnNfcCardData, hashtable:" + hashtable.toString());
            outputText("onReturnNfcCardData:" + hashtable.toString());
        }

        @Override
        public void onNFCrequestOnline() {
            Log.e(TAG, TAG + "onNFCrequestOnline");
            outputText("onNFCrequestOnline");
            sendHandlerMessage(ionRequestOnlineProcess, "");
        }

        @Override
        public void onNFCrequestImportPin(int type, int lasttimeFlag, String amt) {
            Log.e(TAG, TAG + "onNFCrequestImportPin, type:" + type + ", lasttimeFlag:" + lasttimeFlag + ", amt:" + amt);
            outputText("onNFCrequestImportPin");
            if (NEED_ONLINE_PIN) {
                sendHandlerMessage(ionRequestPinEntry, "");
            } else {
                mEmvNfcKernelApi.sendPinEntry();
            }
        }

        @Override
        public void onNFCTransResult(ContantPara.NfcTransResult nfcTransResult) {
            Log.e(TAG, TAG + "onNFCTransResult, nfcTransResult:" + nfcTransResult);
            if (nfcTransResult == ContantPara.NfcTransResult.OFFLINE_APPROVAL) {
                outputText("onNFCTransResult:" + nfcTransResult);
                sendHandlerMessage(onOfflineApproved, "");
            } else if (nfcTransResult == ContantPara.NfcTransResult.ONLINE_APPROVAL) {
                outputText("onNFCTransResult:" + nfcTransResult);
                sendHandlerMessage(onOnlineApproved, "" + nfcTransResult);
            } else {
                outputColorText(TextColor.RED, "onNFCTransResult:" + nfcTransResult);
                sendHandlerMessage(ionError, nfcTransResult + "");
                uploadEmvLog(nfcTransResult + "", nfcTransResult + "");
            }
        }

        @Override
        public void onNFCErrorInfor(ContantPara.NfcErrMessageID nfcErrMessageID, String errorStr) {
            Log.e(TAG, TAG + "onNFCErrorInfor, nfcErrMessageID:" + nfcErrMessageID + ", errorStr:" + errorStr);
            outputColorText(TextColor.RED, "onNFCErrorInfor:" + nfcErrMessageID);
            if (nfcErrMessageID == ContantPara.NfcErrMessageID.AID_PARAM_NOT_FIND
                    || nfcErrMessageID == ContantPara.NfcErrMessageID.CAPK_PARAM_NOT_FIND
                    || nfcErrMessageID == ContantPara.NfcErrMessageID.ICS_PARAM_NOT_FIND) {
                sendHandlerMessage(ionError, "No AID/CPAK in the device, Please configure");
            } else {
                sendHandlerMessage(ionNFCTransResult, errorStr + "");
                uploadEmvLog(nfcErrMessageID + "", errorStr + "");
            }
        }

        @Override
        public void onNFCrequestFinalSelect(byte[] bytes) {
            outputText("onNFCrequestFinalSelect:" + BytesUtil.bytes2HexString(bytes));
        }
    }

    public void startPinInput(final boolean isFirstPin, boolean isOnline, final int pinEntryType, final boolean isLastPinTry, final Bundle emvBundle, String message) {
        Log.e(TAG, TAG + "startPinInput");
        pinpadBundle = new Bundle();
        if (TextUtils.isEmpty(PAN)) {
            getPan();
        }

        pinpadBundle.putString("title", "");
        pinpadBundle.putBoolean("sound", false);
        pinpadBundle.putLong("timeOutMS", 60 * 1000);
        pinpadBundle.putString("cardNo", PAN);
        pinpadBundle.putBoolean("FullScreen", true);
        if (isOnline) {
            if (TextUtils.isEmpty(message)) {
                message = "Enter Online PIN";
                if (!isFirstPin) {
                    message = "Re-enter Online PIN";
                }
            }
            pinpadBundle.putString("message", message);
            pinpadBundle.putBoolean("bypass", true);
            pinpadBundle.putString("supportPinLen", "0,4,5,6,7,8,9,10,11,12");
            pinpadBundle.putBoolean("onlinePin", true);
            if (PINPAD_DUKPT) {
                pinpadBundle.putInt("PINKeyNo", INDEX_DUKPT);
                mPinPadProvider.GetDukptPinBlock(pinpadBundle, new PinInputListener() {
                    @Override
                    public void onInput(int len, int key) {

                    }

                    @Override
                    public void onConfirm(byte[] data, boolean isNonePin) {

                    }

                    @Override
                    public void onConfirm_dukpt(byte[] pinBlock, byte[] ksn) {
                        boolean bypass = false;
                        if (pinBlock == null || pinBlock.length <= 0) {
                            bypass = true;
                        }
                        if (cardReadMode == CardTypeConstant.CONTACT || cardReadMode == CardTypeConstant.CONTACTLESS) {
                            if (bypass) {
                                mEmvNfcKernelApi.bypassPinEntry();
                            } else {
                                mEmvNfcKernelApi.sendPinEntry();
                            }
                        } else {
                            sendHandlerMessage(ionRequestOnlineProcess, "");
                        }
                    }

                    @Override
                    public void onCancel() {
                        sendHandlerMessage(ionError, "Pinpad Cancelled");
                    }

                    @Override
                    public void onTimeOut() {
                        sendHandlerMessage(ionError, "Pinpad Time Out");
                    }

                    @Override
                    public void onError(int i) {
                        if (i == 23) {
                            sendHandlerMessage(ionError, i + ":" + "The Pin key is not exist, please Logon first");
                            return;
                        }
                        if (i == 7010) {
                            //PCI requirement: PIN input cannot exceed 120 times within one hour
                            sendHandlerMessage(ionError, i + ":" + "PCI requirement: PIN input cannot exceed 120 times within one hour.");
                            return;
                        }
                        sendHandlerMessage(ionError, i + ":Pinpad Error");
                    }
                });
            } else {
                pinpadBundle.putInt("PINKeyNo", INDEX_WK);
                mPinPadProvider.getPinBlockEx(pinpadBundle, new PinInputListener() {
                    @Override
                    public void onInput(int i, int i1) {

                    }

                    @Override
                    public void onConfirm(byte[] pinBlock, boolean nonePin) {
                        if (cardReadMode == CardTypeConstant.CONTACT || cardReadMode == CardTypeConstant.CONTACTLESS) {
                            if (nonePin) {
                                mEmvNfcKernelApi.bypassPinEntry();
                            } else {
                                mEmvNfcKernelApi.sendPinEntry();
                            }
                        } else {
                            sendHandlerMessage(ionError, "");
                        }
                    }

                    @Override
                    public void onConfirm_dukpt(byte[] pinBlock, byte[] ksn) {

                    }

                    @Override
                    public void onCancel() {
                        sendHandlerMessage(ionError, "Pinpad Cancelled");
                    }

                    @Override
                    public void onTimeOut() {
                        sendHandlerMessage(ionError, "Pinpad Time Out");
                    }

                    @Override
                    public void onError(int i) {
                        if (i == 23) {
                            sendHandlerMessage(ionError, i + ":" + "The Pin key is not exist, please Logon first");
                            return;
                        }
                        if (i == 7010) {
                            //PCI requirement: PIN input cannot exceed 120 times within one hour
                            sendHandlerMessage(ionError, i + ":" + "PCI requirement: PIN input cannot exceed 120 times within one hour.");
                            return;
                        }
                        sendHandlerMessage(ionError, i + ":Pinpad Error");
                    }
                });
            }
        } else {
            pinpadBundle.putInt("inputType", 3); //Offline PlainPin
            pinpadBundle.putInt("CardSlot", 0);
            pinpadBundle.putBoolean("onlinePin", false);
            pinpadBundle.putBoolean("bypass", true);
            pinpadBundle.putString("supportPinLen", "0,4,5,6,7,8,9,10,11,12");
            if (TextUtils.isEmpty(message)) {
                message = "Enter Offline PIN";
                if (!isFirstPin) {
                    message = "Re-enter Offline PIN";
                }
                if (isLastPinTry) {
                    message = "One last attempt allowed to enter the PIN";
                }
            }
            pinpadBundle.putString("message", message);

            if (pinEntryType == 1) {
                pinpadBundle.putInt("inputType", 4); //Offline CipherPin
                final byte[] pub = emvBundle.getByteArray("pub");
                final int[] publen = emvBundle.getIntArray("publen");
                final byte[] exp = emvBundle.getByteArray("exp");
                final int[] explen = emvBundle.getIntArray("explen");
                Log.e(TAG, TAG + "ModuleLen:" + publen[0] + ", " + Funs.bytesToHexString(pub));

                if (publen[0] == 0 || explen[0] == 0) {
                    mEmvNfcKernelApi.sendOfflinePINVerifyResult(-198);
                    return;
                }

                int ModuleLen = publen[0];
                int ExponentLen = explen[0];
                byte[] Module = new byte[ModuleLen];
                byte[] Exponent = new byte[ExponentLen];
                System.arraycopy(pub, 0, Module, 0, ModuleLen);
                System.arraycopy(exp, 0, Exponent, 0, ExponentLen);
                pinpadBundle.putInt("ModuleLen", ModuleLen);//Modulus length
                pinpadBundle.putString("Module", Funs.bytesToHexString(Module));//Module
                pinpadBundle.putInt("ExponentLen", ExponentLen);//Exponent length
                pinpadBundle.putString("Exponent", Funs.bytesToHexString(Exponent));//Exponent
            }

            //================================================================
            //Key keyboard does not exit, automatically prompts password retry.
//            String nipCaptureMessage = "Enter offline PIN";
//            String retryNipMessage = "Pin Try Times: #";
//            String lastRetryNipMessage = "Last Pin Try";
//            if (isLastPinTry) {
//                nipCaptureMessage = lastRetryNipMessage;
//            }
//            //if PinTryMode=1, the keyboard will not dismiss after error pin entered.
//            pinpadBundle.putInt("PinTryMode", 1);
//            pinpadBundle.putString("message", nipCaptureMessage);
//            // "CAPTURE SU NIP"                      //first tip
//            pinpadBundle.putString("ErrorMessage", retryNipMessage);
//            // "NIP INVÁLIDO, REINTENTOS POSIBLES: " //middle tip
//            pinpadBundle.putString("ErrorMessageLast", lastRetryNipMessage);
            //  "NIP INVÁLIDO, ÚLTIMO REINTENTO"      //last tip
            //================================================================
            mPinPadProvider.getOfflinePinBlock(pinpadBundle, new OfflinePinInputListener() {
                @Override
                public void onInput(int len, int key) {
                    Log.e(TAG, "OfflinePinInputListener, onInput: len=" + len + ", key=" + key);
                }

                @Override
                public void onConfirm(int resultCode) {
                    Log.e(TAG, "OfflinePinInputListener, onConfirm: resultCode=" + resultCode);
                    mEmvNfcKernelApi.sendOfflinePINVerifyResult(resultCode);
                }

                @Override
                public void onRetry(int pinEntryType, int retryTimes) {
                    Log.e(TAG, "OfflinePinInputListener, onRetrypin:EntryType=" + pinEntryType + ", availableTimes=" + retryTimes);
                    OfflinePinBean offlinePinBean = new OfflinePinBean();
                    offlinePinBean.setPinEntryType(pinEntryType);
                    offlinePinBean.setRetryTimes(retryTimes);
                    sendHandlerMessage(offlinePin_retry, offlinePinBean);
                }

                @Override
                public void onCancel(int errorCode) {
                    Log.e(TAG, "OfflinePinInputListener, onCancel: errorCode=" + errorCode);
                    mEmvNfcKernelApi.sendOfflinePINVerifyResult(errorCode);
                }

                @Override
                public void onTimeOut(int errorCode) {
                    Log.e(TAG, "OfflinePinInputListener, onTimeOut: errorCode=" + errorCode);
                    mEmvNfcKernelApi.sendOfflinePINVerifyResult(errorCode);
                }

                @Override
                public void onError(int errorCode) {
                    Log.e(TAG, "OfflinePinInputListener, onTimeOut: errorCode=" + errorCode);
                    mEmvNfcKernelApi.sendOfflinePINVerifyResult(errorCode);
                }

            });
        }
    }


    /**
     * 2nd GAC.
     * After online, import the online result to the kernel to completion the transaction.
     *
     * @param onlineSuccess
     */
    private void sendOnlineProcessResult(boolean onlineSuccess) {
        if (onlineSuccess) {
            //Online success
            String responseEmvData = "8A02303091080102030405060708";
            //If there is no tag 8A in response DE55, package the tag 8A.
            //Use the real response code, "00" is sample value.
            if (TextUtils.isEmpty(responseEmvData) || TextUtils.isEmpty(Funs.TLV_Find("8A", responseEmvData))) {
                String responseCode = "00";
                String tag8AValue = Funs.New_TLV("8A", ByteUtils.bytes2HexString(responseCode.getBytes()));
                responseEmvData += tag8AValue;
            }
            Log.e(TAG, "responseEmvData:" + responseEmvData);
            mEmvNfcKernelApi.sendOnlineProcessResult(true, responseEmvData);
        } else {
            //Online failed
            mEmvNfcKernelApi.sendOnlineProcessResult(false, null);
        }
    }

    /**
     * You can call "getTlvByTagLists" to generate the request DE55.
     */
    public void createField55() {
        if (cardReadMode != CardTypeConstant.CONTACT && cardReadMode != CardTypeConstant.CONTACTLESS) {
            return;
        }
        String[] field55Arr = new String[]{"5F2A", "5F34", "82", "84", "95", "9A", "9C", "9F02", "9F03", "9F06", "9F10", "9F1A", "9F1E", "9F21", "9F26", "9F27",
                "9F33", "9F34", "9F35", "9F36", "9F37", "9F12", "9F41", "9F6E", "5F36"};
        List<String> fieldList = Arrays.asList(field55Arr);
        String field55 = mEmvNfcKernelApi.getTlvByTagLists(fieldList);
        Log.e(TAG, "Field55:" + field55);
        outputColorText(TextColor.BLACK, "Field55:" + field55);
    }

    public void getPan() {
        if (cardReadMode != CardTypeConstant.CONTACT && cardReadMode != CardTypeConstant.CONTACTLESS) {
            return;
        }
        if (TextUtils.isEmpty(PAN)) {
            PAN = mEmvNfcKernelApi.getValByTag(0x5A);
            if (!TextUtils.isEmpty(PAN) && PAN.toUpperCase().endsWith("F")) {
                PAN = PAN.substring(0, PAN.length() - 1);
            }
        }
        if (TextUtils.isEmpty(PAN)) {
            String TRACK2 = mEmvNfcKernelApi.getValByTag(0x57).toUpperCase();
            Log.e(TAG, "TRACK2:" + TRACK2);
            int index = TRACK2.indexOf("D");
            if (index != -1) {
                if (TextUtils.isEmpty(PAN)) {
                    PAN = TRACK2.substring(0, index);//获得卡号
                }
            }
        }

    }

    /**
     * You can call "getValByTag" to obtain tag value.
     */
    public void getTrack2() {
        if (cardReadMode != CardTypeConstant.CONTACT && cardReadMode != CardTypeConstant.CONTACTLESS) {
            return;
        }

        String cardHolder = new String(ByteUtils.hexString2Bytes(mEmvNfcKernelApi.getValByTag(0x5F20).toUpperCase()));
        if (TextUtils.isEmpty(cardHolder)) {
            String tag56 = mEmvNfcKernelApi.getValByTag(0x56).toUpperCase();
            Log.e(TAG, "TRACK1:" + tag56);
            if (!TextUtils.isEmpty(tag56)) {
                tag56 = new String(ByteUtils.hexString2Bytes(tag56));
                String[] arr = tag56.split("\\^");
                if (arr != null && arr.length >= 2) {
                    cardHolder = arr[1].trim();
                }
            }
        }
        Log.e(TAG, cardHolder + cardHolder);
        String TRACK2 = mEmvNfcKernelApi.getValByTag(0x57).toUpperCase();
        Log.e(TAG, "TRACK2:" + TRACK2);
        PAN = mEmvNfcKernelApi.getValByTag(0x5A);
        if (!TextUtils.isEmpty(PAN) && PAN.toUpperCase().endsWith("F")) {
            PAN = TRACK2.substring(0, PAN.length() - 1);
        }
        String CARD_SN = mEmvNfcKernelApi.getValByTag(0x5F34);
        Log.e(TAG, "CARD_SN:" + CARD_SN);
        String EXPIRED_DATE = mEmvNfcKernelApi.getValByTag(0x5F24);
        String ServiceCode = "";
        Log.e(TAG, "CARD_SN:" + ServiceCode);
        int index = TRACK2.indexOf("D");
        if (index != -1) {
            if (TextUtils.isEmpty(PAN)) {
                PAN = TRACK2.substring(0, index);//获得卡号
            }
            index++;
            if (TextUtils.isEmpty(EXPIRED_DATE)) {
                EXPIRED_DATE = TRACK2.substring(index, index + 4);//获得有效期
            }
            if (TRACK2.length() >= (index + 8)) {
                ServiceCode = TRACK2.substring(index + 5, index + 8);
            }
        }
        if (!TextUtils.isEmpty(EXPIRED_DATE)) {
            if (EXPIRED_DATE.length() > 4) {
                EXPIRED_DATE = EXPIRED_DATE.substring(0, 4);
            }
        }
        Log.e(TAG, "PAN:" + PAN);
        Log.e(TAG, "ServiceCode:" + ServiceCode);
        Log.e(TAG, "EXPIRED_DATE:" + EXPIRED_DATE);

        String AID = mEmvNfcKernelApi.getValByTag(0x84).toUpperCase();
        Log.e(TAG, "AID:" + AID);

        outputColorText(TextColor.BLACK, "TRACK2:" + TRACK2);
        outputColorText(TextColor.BLACK, "PAN:" + PAN);
        outputColorText(TextColor.BLACK, "EXPIRED DATE:" + EXPIRED_DATE);
        outputColorText(TextColor.BLACK, "SERVICE CODE:" + ServiceCode);
        outputColorText(TextColor.BLACK, "AID:" + AID);
        outputColorText(TextColor.BLACK, "CARD HOLDER NAME:" + cardHolder);
    }

    /**
     * Parse magnetic stripe card data.
     *
     * @param StripInfo
     * @return
     */
    public static Bundle GetMagCardStrEMV(String StripInfo) {
        logfile.printLog(TAG + "===GetMagCardStrEMV:" + StripInfo);
        StripInfo = StripInfo.toUpperCase();
        Bundle cardInfo = new Bundle();
        String track1 = Funs.TLV_Find("D1", StripInfo).toUpperCase();
        String track2 = Funs.TLV_Find("D2", StripInfo).toUpperCase();
        String track3 = Funs.TLV_Find("D3", StripInfo).toUpperCase();
        String cardHolder = "";
        String serviceCode = "";
        String validTime = "";
        String pan = "";
        if (!TextUtils.isEmpty(track1)) {
            track1 = new String(BytesUtil.hexString2Bytes(track1));
            String[] arr = track1.split("\\^");
            if (arr != null && arr.length >= 2) {
                cardHolder = arr[1].trim();
            }
        }
        cardInfo.putString(EMVDataConstant.CARD_HOLDER_NAME, cardHolder);
        cardInfo.putString(EMVDataConstant.TRACK1, track1);
        if (!TextUtils.isEmpty(track3)) {
            track3 = new String(BytesUtil.hexString2Bytes(track3));
        }
        cardInfo.putString(EMVDataConstant.TRACK3, track3);
        if (!TextUtils.isEmpty(track2)) {
            track2 = new String(BytesUtil.hexString2Bytes(track2));
            int index = track2.indexOf("=");
            if (index != -1) {
                pan = track2.substring(0, index);
                if (!(TextUtils.isEmpty(track2.substring(index + 1)))) {
                    if (track2.length() >= (index + 5)) {
                        validTime = track2.substring(index + 1, index + 5);
                        if (track2.length() < (index + 8)) {
                            cardInfo.putString(EMVDataConstant.SERVICE_CODE, "");
                        } else {
                            serviceCode = track2.substring(index + 5, index + 8);
                        }
                    }
                }
            }
        }
        cardInfo.putString(EMVDataConstant.TRACK2, track2);
        cardInfo.putString(EMVDataConstant.EXPIRED_DATE, validTime);//yyMM
        cardInfo.putString(EMVDataConstant.PAN, pan);
        cardInfo.putString(EMVDataConstant.SERVICE_CODE, serviceCode);
        return cardInfo;
    }

    public void uploadEmvLog(final String errorCode, final String errorMsg) {
        if (!needUploadEmvLog) {
            return;
        }
        //service.urovo.com:1881
        EmvLogUtil.uploadEmvLog(this, "service.urovo.com", "1881", errorCode, errorMsg, new LogUploadCallBack() {
            @Override
            public void callBack(LogUploadResponse response) {
                if (response == null) {
                    Log.e(TAG, "LogUploadCallBack: response is null");
                    outputColorText(TextColor.BLACK, "Log Upload Result: response is null");
                    return;
                }
                Log.e(TAG, "LogUploadCallBack:" + new Gson().toJson(response));
                outputColorText(TextColor.BLACK, "Log Upload Result:\n" + new Gson().toJson(response));
            }
        });
    }

    public void sendHandlerMessage(int what, Object object) {
        Message message = mHandler.obtainMessage(what);
        message.obj = object;
        mHandler.sendMessage(message);
    }

    interface CardTypeConstant {
        int SWIPE = 1;
        int CONTACT = 2;
        int CONTACTLESS = 3;
    }

    class OfflinePinBean {

        int pinEntryType = 0;
        int retryTimes = 0;

        public int getPinEntryType() {
            return pinEntryType;
        }

        public void setPinEntryType(int pinEntryType) {
            this.pinEntryType = pinEntryType;
        }

        public int getRetryTimes() {
            return retryTimes;
        }

        public void setRetryTimes(int retryTimes) {
            this.retryTimes = retryTimes;
        }
    }

}
