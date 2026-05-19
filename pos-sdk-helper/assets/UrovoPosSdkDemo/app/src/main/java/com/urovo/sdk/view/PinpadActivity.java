package com.urovo.sdk.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.device.DeviceManager;
import android.device.SEManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.urovo.i9000s.api.emv.Funs;
import com.urovo.sdk.R;
import com.urovo.sdk.utils.AESUtil;
import com.urovo.sdk.utils.RSAUtil;
import com.urovo.sdk.utils.ScreenUtils;
import com.urovo.sdk.utils.StringUtil;
import com.urovo.sdk.model.Pinpad;
import com.urovo.sdk.model.Translations;
import com.urovo.sdk.pinpad.PinPadProviderImpl;
import com.urovo.sdk.pinpad.listener.PinInputListener;
import com.urovo.sdk.pinpad.utils.Constant;

import com.urovo.sdk.pinpad.utils.SM4;

import com.urovo.sdk.utils.BytesUtil;
import com.urovo.sdk.utils.PinpadUtil;
import com.urovo.sdk.utils.TDESUtil;
import com.urovo.sdk.utils.TR34Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Locale;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class PinpadActivity extends BaseActivity implements OnClickListener {

    public final static int INDEX_TEK = 9;
    public final static int INDEX_MK = 10;
    public final static int INDEX_WK = 10;
    public final static int INDEX_DUKPT_MSR = 1;
    public final static int INDEX_DUKPT_EMV = 2;
    public final static int INDEX_DUKPT_PIN = 3;
    public final static int INDEX_DUKPT_MAC = 4;
    public final static int INDEX_DUKPT_AES128 = 1;
    public final static int INDEX_DUKPT_AES192 = 2;
    public final static int INDEX_DUKPT_AES256 = 3;

    public boolean aesPinPadEnd = false;//for test

    private final static String macStr = "1200721405D820C0820116986009010120744800000000000001000020211104115855211104115855241200000101100020015065999211101001379860090101207448D24122011374015900000012733370041988888888028602869F2608FF852238242376749F2701809F10120114A74003020000000000000000000000FF9F370478D842739F360201C7950500800080009A032111049C01009F02060000000100005F2A020860820239009F1A0208609F03060000000000009F3303E0F0C89F34034403029F3501229F1E0830303030303030308407A08600010000019F090200209F41040000000443D0964F00000000";
    private final static String plainTEKey = "00000000000000000000000000000000";
    public final static String plainMainKey = "11111111111111111111111111111111";
    public final static String plainPinKey = "22222222222222222222222222222222";
    public final static String plainTDKey = "44444444444444444444444444444444";
    public final static String plainMacKey = "33333333333333334444444444444444";

    public final static byte[] bdkBuff = BytesUtil.hexString2Bytes("FFEEDDCCBBAA99887766554433221101");
    public final static byte[] ksnBuff_msr = BytesUtil.hexString2Bytes("11111746011BEDE00002");
    public final static byte[] ksnBuff_emv = BytesUtil.hexString2Bytes("22222746011BEDE00002");
    public final static byte[] ksnBuff_pin = BytesUtil.hexString2Bytes("33333746011BEDE00002");
    public final static byte[] ksnBuff_mac = BytesUtil.hexString2Bytes("44444746011BEDE00002");

    public final static byte[] bdk_aes_16 = BytesUtil.hexString2Bytes("574A31392BA580BFF13614B9DA6280DB");
    public final static byte[] bdk_aes_24 = BytesUtil.hexString2Bytes("574A31392BA580BFF13614B9DA6280DB574A31392BA580BF");
    public final static byte[] bdk_aes_32 = BytesUtil.hexString2Bytes("574A31392BA580BFF13614B9DA6280DB574A31392BA580BFF13614B9DA6280DB");
    public final static byte[] ksnBuff_aes = BytesUtil.hexString2Bytes("0FFFF9876543210E00000000");

    static String pan = "6214837803398183";
    private PinPadProviderImpl pinpad;

    private RadioGroup radioGroup_alg;
    private int keyAlg = Constant.KeyAlgorithm.DES;

    private String model = "";
    private int width;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinpad);
        initView();
        pinpad = PinPadProviderImpl.getInstance();
        pinpad.LogOutEnable(true);
        model = Build.MODEL.toUpperCase();
        Log.e(TAG, "model:" + model);
        width = ScreenUtils.getScreenWidth(this);
        height = ScreenUtils.getScreenHeight(this);
        Log.e(TAG, "Screen: width=" + width + ", height=" + height);

        radioGroup_alg = findViewById(R.id.radioGroup_alg);
        radioGroup_alg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_des:
                        keyAlg = Constant.KeyAlgorithm.DES;
                        pinpad.setKeyAlgorithm(keyAlg);
                        break;
                    case R.id.radio_aes:
                        keyAlg = Constant.KeyAlgorithm.AES;
                        pinpad.setKeyAlgorithm(keyAlg);
                        break;
                    case R.id.radio_sm4:
                        keyAlg = Constant.KeyAlgorithm.SM4;
                        pinpad.setKeyAlgorithm(keyAlg);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        try {
            boolean succ = false;
            int ret = -1;
            switch (v.getId()) {
                case R.id.button_deleteKey:
                    deleteKey();
                    break;
                case R.id.button_isKeyExist:
                    isKeyExist();
                    break;
                case R.id.button_loadTEK:
                    loadTEK();
                    break;
                case R.id.button_loadMainKeyEnc:
                    loadMainKeyEnc();
                    break;
                case R.id.button_loadMainKey:
                    loadMainKey();
                    break;
                case R.id.button_loadWorkKey:
                    loadWorkKey();
                    break;
                case R.id.button_calcMAC:
                    calcMac();
                    break;
                case R.id.button_encryptData:
                    encryptData();
                    break;
                case R.id.button_pin_online:
                    startInputPin(null, true, INDEX_WK, plainPinKey, false);
                    break;
                case R.id.button_pin_offline:
                    startInputPin(null, false, INDEX_WK, plainPinKey, false);
                    break;
                case R.id.button_diversifiedKey:
                    int status = pinpad.diversifiedKey(INDEX_MK, INDEX_WK, INDEX_WK + 1, plainPinKey);
                    final String newPinKey = "0F8ADFFB11DC27840F8ADFFB11DC2784";
                    outputText("diversifiedKey:" + status);
                    startInputPin(null, true, INDEX_WK + 1, newPinKey, false);
                    break;
                case R.id.button_genKeyHashValue:
                    genKeyHashValue();
                    break;
                case R.id.downloadKeyDukpt:
                    downloadKeyDukpt();
                    break;
                case R.id.DukptGetKsn:
                    DukptGetKsn();
                    break;
                case R.id.calculateMACOfDUKPTExtend:
                    calculateMACOfDUKPTExtend();
                    break;
                case R.id.button_getRSAPublicKeyModel:
                    getRSAPublicKeyModel();
                    ACS_FormatKeyBlockTest();
                    break;
                case R.id.button_encryptWithPEK:
                    encryptWithPEK(INDEX_DUKPT_MSR);
                    break;
                case R.id.button_DukptEncryptDataIV:
                    DukptEncryptDataIV(INDEX_DUKPT_EMV);
                    DukptEncryptDataIV_MAC5(INDEX_DUKPT_MAC);
                    break;
                case R.id.GetDukptPinBlock:
                    GetDukptPinBlock();
                    break;
                case R.id.button_customKeyboard1:
                    customKeyBoard1();
                    break;
                case R.id.button_customKeyboard3:
                    customKeyBoard3JSON();
                    break;
                case R.id.button_customKeyboard4:
                    customKeyBoard4JSON();
                    break;
                case R.id.button_customKeyboard5:
                    customKeyBoard5JSON();
                    break;
                case R.id.button_customKeyboard6:
                    if (TextUtils.equals("I5000", model)) {
                        customKeyBoard6JSON_I5000();
                    } else {
                        customKeyBoard6JSON();
                    }
                    break;
                case R.id.button_pinpad_half:
                    customKeyBoardJSON_HALF();
                    break;
                case R.id.button_pinpadblind:
                    customKeyBoardJSON_Blind(8);
                    break;
                case R.id.button_DukptAesInitial:
                    DukptAesInitial();
                    break;
                case R.id.button_DukptAesUpdateKSN:
                    DukptAesUpdateKsn();
                    break;
                case R.id.button_DukptAesGetKsn:
                    DukptAesGetKsn();
                    break;
                case R.id.button_DukptAesEncryptDataIV:
                    DukptAesEncryptDataIV();
                    break;
                case R.id.button_DukptAesGetPinBlock:
                    DukptAesPinBlock();
                    break;
                case R.id.button_downloadTR31_mksk:
                    downloadKeyTR31();
                    break;
                case R.id.button_downloadTR31_dukpt:
                    outputColorText(TextColor.BLUE, "downloadTR31DukptKey TDES");
                    //Key value: 18A2CE9D62E8925DA7645B54ECA14BF2 (KCV: 41676F)
                    byte[] kbpk = BytesUtil.hexString2Bytes("AB2E09DB3EF0BA71E0CE6CD755C23A3B");
                    String keyBlockTDES = "B0120B1TX00N0100KS181ED9D5000000BC2000003AEC5046580DE1463841EFAF08D29EC120854A493D6D031CA5740051CD8421A8A3D6A66C3BC93311";
                    downloadDukptTR31(kbpk, keyBlockTDES);

                    outputColorText(TextColor.BLUE, "\ndownloadTR31DukptKey TDES");
                    //Key value: 18A2CE9D62E8925DA7645B54ECA14BF2 (KCV: 41676F)
                    kbpk = BytesUtil.hexString2Bytes("0123456789ABCDEFFEDCBA9876543210");
                    String keyBlockAES = "D0144B1TX00S0200KS181ED9D5000000BC200000PB08UhKlC0CABC959AACAEB202F1EC32B550DC21C71311223B045FDCA73A5982426DF15ABEF6D51E974391B00EB80309A00C54E7";
                    downloadDukptTR31(kbpk, keyBlockAES);

                    outputColorText(TextColor.BLUE, "\ndownloadTR31DukptKey AES");
                    //Key value: 18A2CE9D62E8925DA7645B54ECA14BF2 (KCV: 727A15)
                    kbpk = BytesUtil.hexString2Bytes("AB2E09DB3EF0BA71E0CE6CD755C23A3B");
                    keyBlockAES = "B0112B1AX00S0200KS181ED9D5000000BC200000PB08UhKlB34EA8DD29667DDC399295254398F4A5C190AE49898BBC4588DE89E5B2FD5A7E";
                    downloadDukptTR31(kbpk, keyBlockAES);

                    outputColorText(TextColor.BLUE, "\ndownloadTR31DukptKey AES");
                    //Key value: 18A2CE9D62E8925DA7645B54ECA14BF2 (KCV: 727A15)
                    kbpk = BytesUtil.hexString2Bytes("0123456789ABCDEFFEDCBA9876543210");
                    keyBlockAES = "D0144B1AX00S0200KS181ED9D5000000BC200000PB08UhKl4CD15D040FFB305ACC5B76A5003247C596951BF50618FB795C2FD4EE946841B3341F2CDE1E94852022E83EFBBBB71F9E";
                    downloadDukptTR31(kbpk, keyBlockAES);
                    break;
                case R.id.button_rsa:
                    rsaTest();
                    break;
                case R.id.button_tr34:
                    int CA_TYPE_KMSCA = 0xF2;
                    int CA_TYPE_PEDCRT = 0xF3;
                    int CA_TYPE_KDHCRRT = 0xF4;
                    int CA_TYPE_PEDPRV = 0xF5;
                    outputColorText(TextColor.BLACK, "Type F2, Index 0");
                    tr34Test(CA_TYPE_KMSCA, 0, "F2000000000000000000000000000000");
                    outputColorText(TextColor.BLACK, "Type F2, Index 1");
                    tr34Test(CA_TYPE_KMSCA, 1, "F2111111111111111111111111111111");
                    outputColorText(TextColor.BLACK, "Type F2, Index 2");
                    tr34Test(CA_TYPE_KMSCA, 2, "F2222222222222222222222222222222");
                    outputColorText(TextColor.BLACK, "Type F2, Index 3");
                    tr34Test(CA_TYPE_KMSCA, 2, "F2333333333333333333333333333333");

                    outputColorText(TextColor.BLACK, "\nType F3, Index 0");
                    tr34Test(CA_TYPE_PEDCRT, 0, "F3000000000000000000000000000000");
                    outputColorText(TextColor.BLACK, "Type F3, Index 1");
                    tr34Test(CA_TYPE_PEDCRT, 1, "F3111111111111111111111111111111");
                    outputColorText(TextColor.BLACK, "Type F3, Index 2");
                    tr34Test(CA_TYPE_PEDCRT, 2, "F3222222222222222222222222222222");
                    outputColorText(TextColor.BLACK, "Type F3, Index 3");
                    tr34Test(CA_TYPE_PEDCRT, 3, "F3333333333333333333333333333333");

                    outputColorText(TextColor.BLACK, "\nType F4, Index 0");
                    tr34Test(CA_TYPE_KDHCRRT, 0, "F4000000000000000000000000000000");
                    outputColorText(TextColor.BLACK, "Type F4, Index 1");
                    tr34Test(CA_TYPE_KDHCRRT, 1, "F4111111111111111111111111111111");
                    outputColorText(TextColor.BLACK, "Type F4, Index 2");
                    tr34Test(CA_TYPE_KDHCRRT, 2, "F4222222222222222222222222222222");
                    outputColorText(TextColor.BLACK, "Type F4, Index 3");
                    tr34Test(CA_TYPE_KDHCRRT, 3, "F4333333333333333333333333333333");

                    outputColorText(TextColor.BLACK, "\nType F5, Index 0");
                    tr34Test(CA_TYPE_PEDPRV, 0, "F4000000000000000000000000000000");
                    outputColorText(TextColor.BLACK, "Type F5, Index 1");
                    tr34Test(CA_TYPE_PEDPRV, 1, "F4111111111111111111111111111111");
                    outputColorText(TextColor.BLACK, "Type F5, Index 2");
                    tr34Test(CA_TYPE_PEDPRV, 2, "F4222222222222222222222222222222");
                    outputColorText(TextColor.BLACK, "Type F5, Index 3");
                    tr34Test(CA_TYPE_PEDPRV, 3, "F4333333333333333333333333333333");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteKey() {
//        for (int i = 0; i <= 150; i++) {
//            int ret = pinpad.deleteKey(Constant.KeyType.MAIN_KEY, i);
//            outputColorText(TextColor.BLUE, "deleteKey(MK):" + ret);
//            ret = pinpad.deleteKey(Constant.KeyType.PIN_KEY, i);
//            outputColorText(TextColor.BLUE, "deleteKey(PIN):" + ret);
//            ret = pinpad.deleteKey(Constant.KeyType.MAC_KEY, i);
//            outputColorText(TextColor.BLUE, "deleteKey(MAC):" + ret);
//            ret = pinpad.deleteKey(Constant.KeyType.TD_KEY, i);
//            outputColorText(TextColor.BLUE, "deleteKey(ENC):" + ret);
//        }
        int ret = pinpad.deleteKey(Constant.KeyType.MAIN_KEY, INDEX_TEK);
        outputColorText(TextColor.BLUE, "deleteKey(TEK):" + ret);
        ret = pinpad.deleteKey(Constant.KeyType.MAIN_KEY, INDEX_MK);
        outputColorText(TextColor.BLUE, "deleteKey(MK):" + ret);
        ret = pinpad.deleteKey(Constant.KeyType.PIN_KEY, INDEX_WK);
        outputColorText(TextColor.BLUE, "deleteKey(PIN):" + ret);
        ret = pinpad.deleteKey(Constant.KeyType.MAC_KEY, INDEX_WK);
        outputColorText(TextColor.BLUE, "deleteKey(MAC):" + ret);
        ret = pinpad.deleteKey(Constant.KeyType.TD_KEY, INDEX_WK);
        outputColorText(TextColor.BLUE, "deleteKey(ENC):" + ret);
    }

    public void isKeyExist() {
        outputColorText(TextColor.BLUE, "------------------------------------------------");
        boolean success = pinpad.isKeyExist(Constant.KeyType.MAIN_KEY, INDEX_TEK);
        outputColorText(success ? TextColor.BLUE : TextColor.RED, "isKeyExist(TEK)(keyIndex=)" + INDEX_TEK + ":" + success);
        success = pinpad.isKeyExist(Constant.KeyType.MAIN_KEY, INDEX_MK);
        outputColorText(success ? TextColor.BLUE : TextColor.RED, "isKeyExist(MK)(keyIndex=10)" + INDEX_MK + ":" + success);
        success = pinpad.isKeyExist(Constant.KeyType.MAC_KEY, INDEX_WK);
        outputColorText(success ? TextColor.BLUE : TextColor.RED, "isKeyExist(MAC)(keyIndex=10)" + INDEX_WK + ":" + success);
        success = pinpad.isKeyExist(Constant.KeyType.PIN_KEY, INDEX_WK);
        outputColorText(success ? TextColor.BLUE : TextColor.RED, "isKeyExist(PIN)(keyIndex=10)" + INDEX_WK + ":" + success);
        success = pinpad.isKeyExist(Constant.KeyType.TD_KEY, INDEX_WK);
        outputColorText(success ? TextColor.BLUE : TextColor.RED, "isKeyExist(ENC)(keyIndex=10)" + INDEX_WK + ":" + success);
    }

    public void loadTEK() {
        byte[] checkValue = new byte[16];
        byte[] plainTEKeyBuff = BytesUtil.hexString2Bytes(plainTEKey);
        if (keyAlg == Constant.KeyAlgorithm.DES) {
            checkValue = TDESUtil.encrypt_ECB(plainTEKeyBuff, new byte[8]);
        } else if (keyAlg == Constant.KeyAlgorithm.SM4) {
            checkValue = SM4.SM4EncryptECB(BytesUtil.hexString2Bytes(plainTEKey), new byte[16], 16);
        } else if (keyAlg == Constant.KeyAlgorithm.AES) {
            checkValue = AESUtil.encryptECB(BytesUtil.hexString2Bytes(plainTEKey), new byte[16]);
        }
        boolean succ = pinpad.loadTEK(INDEX_TEK, plainTEKeyBuff, checkValue);
        outputColorText(TextColor.BLUE, "loadTEK(KCV IS NOT NULL):" + succ);
    }

    public void loadMainKeyEnc() {
        byte[] encMainKey = new byte[16];
        byte[] checkValue = new byte[16];
        byte[] plainTEKeyBuff = BytesUtil.hexString2Bytes(plainTEKey);
        byte[] plainMainKeyBuff = BytesUtil.hexString2Bytes(plainMainKey);
        if (keyAlg == Constant.KeyAlgorithm.DES) {
            encMainKey = TDESUtil.encrypt_ECB(plainTEKeyBuff, plainMainKeyBuff);
            checkValue = TDESUtil.encrypt_ECB(plainMainKeyBuff, new byte[8]);
        } else if (keyAlg == Constant.KeyAlgorithm.SM4) {
            encMainKey = SM4.SM4EncryptECB(plainTEKeyBuff, plainMainKeyBuff, plainMainKeyBuff.length);
            checkValue = SM4.SM4EncryptECB(plainMainKeyBuff, new byte[16], 16);
        } else if (keyAlg == Constant.KeyAlgorithm.AES) {
            encMainKey = AESUtil.encryptECB(plainTEKeyBuff, plainMainKeyBuff);
            checkValue = AESUtil.encryptECB(plainMainKeyBuff, new byte[16]);
        }
        boolean succ = pinpad.loadEncryptMainKey(INDEX_TEK, INDEX_MK, encMainKey, checkValue);
        outputColorText(TextColor.BLUE, "loadEncryptMainKey(KCV IS NOT NULL):" + succ);
    }

    public void loadMainKey() {
        byte[] checkValue = new byte[16];
        byte[] plainMainKeyBuff = BytesUtil.hexString2Bytes(plainMainKey);
        if (keyAlg == Constant.KeyAlgorithm.DES) {
            checkValue = TDESUtil.encrypt_ECB(plainMainKeyBuff, new byte[8]);
        } else if (keyAlg == Constant.KeyAlgorithm.SM4) {
            checkValue = SM4.SM4EncryptECB(BytesUtil.hexString2Bytes(plainMainKey), new byte[16], 16);
        } else if (keyAlg == Constant.KeyAlgorithm.AES) {
            checkValue = AESUtil.encryptECB(BytesUtil.hexString2Bytes(plainMainKey), new byte[16]);
        }
        boolean succ = pinpad.loadMainKey(INDEX_MK, plainMainKeyBuff, checkValue);
        outputColorText(TextColor.BLUE, "loadMainKey(KCV IS NULL):" + succ);
    }

    public void loadWorkKey() {
        byte[] encPinKey = new byte[16];
        byte[] checkValue = new byte[16];
        byte[] plainMainKeyBuff = BytesUtil.hexString2Bytes(plainMainKey);
        byte[] plainPinKeyBuff = BytesUtil.hexString2Bytes(plainPinKey);
        if (keyAlg == Constant.KeyAlgorithm.DES) {
            encPinKey = TDESUtil.encrypt_ECB(plainMainKeyBuff, plainPinKeyBuff);
            checkValue = TDESUtil.encrypt_ECB(plainPinKeyBuff, new byte[8]);
        } else if (keyAlg == Constant.KeyAlgorithm.SM4) {
            encPinKey = SM4.SM4EncryptECB(plainMainKeyBuff, plainPinKeyBuff, plainPinKeyBuff.length);
            checkValue = SM4.SM4EncryptECB(plainPinKeyBuff, new byte[16], 16);
        } else if (keyAlg == Constant.KeyAlgorithm.AES) {
            encPinKey = AESUtil.encryptECB(plainMainKeyBuff, plainPinKeyBuff);
            checkValue = AESUtil.encryptECB(plainPinKeyBuff, new byte[16]);
        }
        boolean succ = pinpad.loadWorkKey(Constant.KeyType.PIN_KEY, INDEX_MK, INDEX_WK, encPinKey, checkValue);
        outputColorText(TextColor.BLUE, "load PIN Key(KCV IS NOT NULL):" + succ);

        byte[] encMacKey = new byte[16];
        checkValue = new byte[16];
        byte[] plainMacKeyBuff = BytesUtil.hexString2Bytes(plainMacKey);
        if (keyAlg == Constant.KeyAlgorithm.DES) {
            encMacKey = TDESUtil.encrypt_ECB(plainMainKeyBuff, plainMacKeyBuff);
            checkValue = TDESUtil.encrypt_ECB(plainMacKeyBuff, new byte[8]);
        } else if (keyAlg == Constant.KeyAlgorithm.SM4) {
            encMacKey = SM4.SM4EncryptECB(plainMainKeyBuff, plainMacKeyBuff, plainMacKeyBuff.length);
            checkValue = SM4.SM4EncryptECB(plainMacKeyBuff, new byte[16], 16);
        } else if (keyAlg == Constant.KeyAlgorithm.AES) {
            encMacKey = AESUtil.encryptECB(plainMainKeyBuff, plainMacKeyBuff);
            checkValue = AESUtil.encryptECB(plainMacKeyBuff, new byte[16]);
        }
        succ = pinpad.loadWorkKey(Constant.KeyType.MAC_KEY, INDEX_MK, INDEX_WK, encMacKey, checkValue);
        outputColorText(TextColor.BLUE, "load MAC Key(KCV IS NOT NULL):" + succ);

        byte[] encTDKey = new byte[16];
        checkValue = new byte[16];
        byte[] plainTDKeyBuff = BytesUtil.hexString2Bytes(plainTDKey);
        if (keyAlg == Constant.KeyAlgorithm.DES) {
            encTDKey = TDESUtil.encrypt_ECB(plainMainKeyBuff, plainTDKeyBuff);
            checkValue = TDESUtil.encrypt_ECB(plainTDKeyBuff, new byte[8]);
        } else if (keyAlg == Constant.KeyAlgorithm.SM4) {
            encTDKey = SM4.SM4EncryptECB(plainMainKeyBuff, plainTDKeyBuff, plainTDKeyBuff.length);
            checkValue = SM4.SM4EncryptECB(plainTDKeyBuff, new byte[16], 16);
        } else if (keyAlg == Constant.KeyAlgorithm.AES) {
            encTDKey = AESUtil.encryptECB(plainMainKeyBuff, plainTDKeyBuff);
            checkValue = AESUtil.encryptECB(plainTDKeyBuff, new byte[16]);
        }
        succ = pinpad.loadWorkKey(Constant.KeyType.TD_KEY, INDEX_MK, INDEX_WK, encTDKey, checkValue);
        outputColorText(TextColor.BLUE, "load ENC Key(KCV IS NOT NULL):" + succ);
    }

    public void calcMac() {
        byte[] macSource = BytesUtil.hexString2Bytes(macStr);
        if (keyAlg == Constant.KeyAlgorithm.DES) {
            outputColorText(TextColor.RED, "calcMac DES");
            outputColorText(TextColor.RED, "ANSI X9.9");
            byte[] out = pinpad.calcMAC(INDEX_WK, macSource, 0x01);
            outputColorText(TextColor.BLUE, "response result:\n" + BytesUtil.bytes2HexString(out) + "\n" + "expected result:\n502A20C53785A8FB");

            outputColorText(TextColor.RED, "\nANSI X9.19");
            out = pinpad.calcMAC(INDEX_WK, macSource, 0x11);
            outputColorText(TextColor.BLUE, "response result:\n" + BytesUtil.bytes2HexString(out) + "\n" + "expected result:\nDD0106290E3A4B08");
        } else if (keyAlg == Constant.KeyAlgorithm.AES) {
            outputColorText(TextColor.RED, "calcMac AES");
            outputColorText(TextColor.RED, "CMAC-data len 488");
            String aesmacStr = "1200721405D820C0820116986009010120744800000000000001000020211104115855211104115855241200000101100020015065999211101001379860090101207448D24122011374015900000012733370041988888888028602869F2608FF852238242376749F2701809F10120114A74003020000000000000000000000FF9F370478D842739F360201C7950500800080009A032111049C01009F02060000000100005F2A020860820239009F1A0208609F03060000000000009F3303E0F0C89F34034403029F3501229F1E0830303030303030308407A08600010000019F090200209F41040000000443D0964F000000001200721405D820C0820116986009010120744800000000000001000020211104115855211104115855241200000101100020015065999211101001379860090101207448D24122011374015900000012733370041988888888028602869F2608FF852238242376749F2701809F10120114A74003020000000000000000000000FF9F370478D842739F360201C7950500800080009A032111049C01009F02060000000100005F2A020860820239009F1A0208609F03060000000000009F3303E0F0C89F34034403029F3501229F1E0830303030303030308407A08600010000019F090200209F41040000000443D0964F00000000";
            byte[] aesmacSource = BytesUtil.hexString2Bytes(aesmacStr);
            byte[] out = pinpad.calcMAC(INDEX_WK, aesmacSource, 0x07);
            outputColorText(TextColor.BLUE, "response result:\n" + BytesUtil.bytes2HexString(out) + "\n" + "expected result:\nE58632383E502ADF69EBB7E3BB86B754");

            outputColorText(TextColor.RED, "calcMac AES");
            outputColorText(TextColor.RED, "CMAC-data len 244");
            byte[] aesmacSource1 = BytesUtil.hexString2Bytes(macStr);
            byte[] out1 = pinpad.calcMAC(INDEX_WK, aesmacSource1, 0x07);
            outputColorText(TextColor.BLUE, "response result:\n" + BytesUtil.bytes2HexString(out1) + "\n" + "expected result:\n3F319E4B60D83A45A8F1C2CEF4F9E108");
        } else if (keyAlg == Constant.KeyAlgorithm.SM4) {
            outputColorText(TextColor.RED, "calcMac SM4");
            outputColorText(TextColor.RED, "XOR");
            byte[] out = pinpad.calcMAC(INDEX_WK, macSource, 0x00);
            outputColorText(TextColor.BLUE, "response result:\n" + BytesUtil.bytes2HexString(out) + "\n" + "expected result:\n98648531E0499D9A");

            outputColorText(TextColor.RED, "\nSM4 POS_ECB");
            out = pinpad.calcMAC(INDEX_WK, macSource, 0x10);
            outputColorText(TextColor.BLUE, "response result:\n" + BytesUtil.bytes2HexString(out) + "\n" + "expected result:\n9C080C6F3405025D");
        }
    }

    public void encryptData() {
        byte[] input = "621996044447640027D0506101152641".getBytes();
        byte[] out2 = new byte[input.length];

        if (keyAlg == Constant.KeyAlgorithm.DES) {
            outputColorText(TextColor.RED, "encryptData DES");
            outputColorText(TextColor.RED, "ECB encryption");
            int ret = pinpad.calculateDes(Constant.DesMode.ENC, Constant.Algorithm.DES_ECB, Constant.KeyType.TD_KEY, INDEX_WK, input, out2);
            outputColorText(TextColor.RED, "ret:" + ret);
            if (ret == 0) {
                outputText("response result:\n" + BytesUtil.bytes2HexString(out2) + "\n" + "expected result: \n1F2570DB45E40261D323ADA0FB83DB870787547AACCFEBB7257C76AF088733DF");
            }
            outputColorText(TextColor.RED, "ECB decryption");
            byte[] encryptionData = BytesUtil.hexString2Bytes("1F2570DB45E40261D323ADA0FB83DB870787547AACCFEBB7257C76AF088733DF");
            ret = pinpad.calculateDes(Constant.DesMode.DEC, Constant.Algorithm.DES_ECB, Constant.KeyType.TD_KEY, INDEX_WK, encryptionData, out2);
            outputColorText(TextColor.RED, "ret:" + ret);
            if (ret == 0) {
                outputText("response result:\n" + BytesUtil.bytes2HexString(out2) + "\n" + "expected result: \n3632313939363034343434373634303032374430353036313031313532363431");
            }

            outputColorText(TextColor.RED, "\nCBC encryption");
            out2 = new byte[input.length];
            ret = pinpad.calculateDes(Constant.DesMode.ENC, Constant.Algorithm.DES_CBC, Constant.KeyType.TD_KEY, INDEX_WK, input, out2);
            outputColorText(TextColor.RED, "ret:" + ret);
            if (ret == 0) {
                outputText("response result:\n" + BytesUtil.bytes2HexString(out2) + "\n" + "expected result:\n1F2570DB45E4026190C86844CB06EAA0A5F0C6845C0E0873DA31C229AE3D5FE4");
            }
            outputColorText(TextColor.RED, "CBC decryption");
            encryptionData = BytesUtil.hexString2Bytes("1F2570DB45E4026190C86844CB06EAA0A5F0C6845C0E0873DA31C229AE3D5FE4");
            ret = pinpad.calculateDes(Constant.DesMode.DEC, Constant.Algorithm.DES_CBC, Constant.KeyType.TD_KEY, INDEX_WK, encryptionData, out2);
            outputColorText(TextColor.RED, "ret:" + ret);
            if (ret == 0) {
                outputText("response result:\n" + BytesUtil.bytes2HexString(out2) + "\n" + "expected result:\n3632313939363034343434373634303032374430353036313031313532363431");
            }
        } else if (keyAlg == Constant.KeyAlgorithm.AES) {
            outputColorText(TextColor.RED, "encryptData AES");
            outputColorText(TextColor.RED, "ECB encryption");
            int ret = pinpad.calculateDes(Constant.DesMode.ENC, Constant.Algorithm.AES_ECB, Constant.KeyType.TD_KEY, INDEX_WK, input, out2);
            outputColorText(TextColor.RED, "ret:" + ret);
            if (ret == 0) {
                outputText("response result:\n" + BytesUtil.bytes2HexString(out2) + "\n" + "expected result:\nE9DCBDD6DAFBC51F198C9FAE5FAD65A7758573F0FCB0FCCFBED1987A36A0326F");
            }
            outputColorText(TextColor.RED, "ECB decryption");
            byte[] encryptionData = BytesUtil.hexString2Bytes("E9DCBDD6DAFBC51F198C9FAE5FAD65A7758573F0FCB0FCCFBED1987A36A0326F");
            ret = pinpad.calculateDes(Constant.DesMode.DEC, Constant.Algorithm.AES_ECB, Constant.KeyType.TD_KEY, INDEX_WK, encryptionData, out2);
            outputColorText(TextColor.RED, "ret:" + ret);
            if (ret == 0) {
                outputText("response result:\n" + BytesUtil.bytes2HexString(out2) + "\n" + "expected result:\n3632313939363034343434373634303032374430353036313031313532363431");
            }

            outputColorText(TextColor.RED, "\nCBC encryption");
            out2 = new byte[input.length];
            ret = pinpad.calculateDes(Constant.DesMode.ENC, Constant.Algorithm.AES_CBC, Constant.KeyType.TD_KEY, INDEX_WK, input, out2);
            outputColorText(TextColor.RED, "ret:" + ret);
            if (ret == 0) {
                outputText("response result:\n" + BytesUtil.bytes2HexString(out2) + "\n" + "expected result:\nE9DCBDD6DAFBC51F198C9FAE5FAD65A74D6FC077F7FC9932033D50D23C18A377");
            }
            outputColorText(TextColor.RED, "CBC decryption");
            encryptionData = BytesUtil.hexString2Bytes("E9DCBDD6DAFBC51F198C9FAE5FAD65A74D6FC077F7FC9932033D50D23C18A377");
            ret = pinpad.calculateDes(Constant.DesMode.DEC, Constant.Algorithm.AES_CBC, Constant.KeyType.TD_KEY, INDEX_WK, encryptionData, out2);
            outputColorText(TextColor.RED, "ret:" + ret);
            if (ret == 0) {
                outputText("response result:\n" + BytesUtil.bytes2HexString(out2) + "\n" + "expected result:\n3632313939363034343434373634303032374430353036313031313532363431");
            }
        } else if (keyAlg == Constant.KeyAlgorithm.SM4) {
            outputColorText(TextColor.RED, "encryptData SM4");
            outputColorText(TextColor.RED, "ECB encryption");
            int ret = pinpad.calculateDes(Constant.DesMode.ENC, Constant.Algorithm.SM4, Constant.KeyType.TD_KEY, INDEX_WK, input, out2);
            outputColorText(TextColor.RED, "ret:" + ret);
            if (ret == 0) {
                outputText("response result:\n" + BytesUtil.bytes2HexString(out2) + "\n" + "expected result:\nEE18F32D0668BC5D57DFE61F8ADBDCB8CDAB478BAB83AAC6ED1BA285614054BF");
            }
            outputColorText(TextColor.RED, "ECB decryption");
            byte[] encryptionData = BytesUtil.hexString2Bytes("EE18F32D0668BC5D57DFE61F8ADBDCB8CDAB478BAB83AAC6ED1BA285614054BF");
            ret = pinpad.calculateDes(Constant.DesMode.DEC, Constant.Algorithm.SM4, Constant.KeyType.TD_KEY, INDEX_WK, encryptionData, out2);
            outputColorText(TextColor.RED, "ret:" + ret);
            if (ret == 0) {
                outputText("response result:\n" + BytesUtil.bytes2HexString(out2) + "\n" + "expected result:\n3632313939363034343434373634303032374430353036313031313532363431");
            }
        }
    }

    public void genKeyHashValue() {
        byte[] inputData = BytesUtil.hexString2Bytes("18956198561290728915719572156891565189658916589165259681256193565794");
        byte[] respData = new byte[64];
        byte[] respLen = new byte[1];
        int ret = pinpad.genKeyHashValue(Constant.KeyType.PIN_KEY, INDEX_WK, inputData, inputData.length, respData, respLen);
        outputText("genKeyHashValue:" + ret);
        if (ret == 0) {
            byte[] result = new byte[respLen[0]];
            System.arraycopy(respData, 0, result, 0, result.length);
            outputText("response result:" + BytesUtil.bytes2HexString(result) + "\n" + "expected result: \n" + BytesUtil.generateHash256Value(inputData, plainPinKey));
        }
    }

    public void downloadKeyDukpt() {
        try {
            int ret = pinpad.downloadKeyDukpt(INDEX_DUKPT_MSR, bdkBuff, bdkBuff.length, ksnBuff_msr, ksnBuff_msr.length, null, 0);
            outputText("downloadKeyDukpt MSR ret:" + ret);
            outputText("KSN:" + BytesUtil.bytes2HexString(ksnBuff_msr));

            ret = pinpad.downloadKeyDukpt(INDEX_DUKPT_EMV, bdkBuff, bdkBuff.length, ksnBuff_emv, ksnBuff_emv.length, null, 0);
            outputText("downloadKeyDukpt EMV ret:" + ret);
            outputText("KSN:" + BytesUtil.bytes2HexString(ksnBuff_emv));

            ret = pinpad.downloadKeyDukpt(INDEX_DUKPT_PIN, bdkBuff, bdkBuff.length, ksnBuff_pin, ksnBuff_pin.length, null, 0);
            outputText("downloadKeyDukpt PIN ret:" + ret);
            outputText("KSN:" + BytesUtil.bytes2HexString(ksnBuff_pin));

            ret = pinpad.downloadKeyDukpt(INDEX_DUKPT_MAC, bdkBuff, bdkBuff.length, ksnBuff_mac, ksnBuff_mac.length, null, 0);
            outputText("downloadKeyDukpt MAC ret:" + ret);
            outputText("KSN:" + BytesUtil.bytes2HexString(ksnBuff_mac));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DukptGetKsn() {
        byte[] ksnBuff = new byte[10];
        try {
            int ret = pinpad.DukptGetKsn(INDEX_DUKPT_MSR, ksnBuff);
            outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "\nDukptGetKsn:" + ret + (ret == 0 ? "" : "(false)"));
            outputText("KeyIndex:" + INDEX_DUKPT_MSR);
            outputText("KSN:" + BytesUtil.bytes2HexString(ksnBuff));

            ret = pinpad.DukptGetKsn(INDEX_DUKPT_EMV, ksnBuff);
            outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "\nDukptGetKsn:" + ret + (ret == 0 ? "" : "(false)"));
            outputText("KeyIndex:" + INDEX_DUKPT_EMV);
            outputText("KSN:" + BytesUtil.bytes2HexString(ksnBuff));

            ret = pinpad.DukptGetKsn(INDEX_DUKPT_PIN, ksnBuff);
            outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "\nDukptGetKsn:" + ret + (ret == 0 ? "" : "(false)"));
            outputText("KeyIndex:" + INDEX_DUKPT_PIN);
            outputText("KSN:" + BytesUtil.bytes2HexString(ksnBuff));

            ret = pinpad.DukptGetKsn(INDEX_DUKPT_MAC, ksnBuff);
            outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "\nDukptGetKsn:" + ret + (ret == 0 ? "" : "(false)"));
            outputText("KeyIndex:" + INDEX_DUKPT_MAC);
            outputText("KSN:" + BytesUtil.bytes2HexString(ksnBuff));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void calculateMACOfDUKPTExtend() {
        byte[] rawData = BytesUtil.hexString2Bytes(macStr);
        byte[] outdata = new byte[rawData.length];
        int[] outlen = new int[2];
        byte[] ksnBuff = new byte[10];
        int[] KsnLen = new int[2];
        try {
            int ret = pinpad.calculateMACOfDUKPTExtend(INDEX_DUKPT_MAC, rawData, rawData.length, outdata, outlen, ksnBuff, KsnLen);
            outputText("calculateMACOfDUKPTExtend INDEX_DUKPT_MAC ret:" + ret);
            if (ret == 0) {
                outputText("calculateMACOfDUKPTExtend INDEX_DUKPT_MAC KSN:" + BytesUtil.bytes2HexString(ksnBuff));
                outputText("MAC:" + BytesUtil.bytes2HexString(outdata, outlen[0]));
                byte[] macOut = new byte[outlen[0]];
                System.arraycopy(outdata, 0, macOut, 0, outlen[0]);
                outputText("MAC OUT:" + BytesUtil.bytes2HexString(macOut));
            }
            ret = pinpad.calculateMACOfDUKPTExtend(INDEX_DUKPT_MSR, rawData, rawData.length, outdata, outlen, ksnBuff, KsnLen);
            outputText("calculateMACOfDUKPTExtend INDEX_DUKPT_MSR ret:" + ret);
            if (ret == 0) {
                outputText("calculateMACOfDUKPTExtend INDEX_DUKPT_MSR KSN:" + BytesUtil.bytes2HexString(ksnBuff));
                outputText("MAC:" + BytesUtil.bytes2HexString(outdata, outlen[0]));
            }

            ret = pinpad.calculateMACOfDUKPTExtend(INDEX_DUKPT_EMV, rawData, rawData.length, outdata, outlen, ksnBuff, KsnLen);
            outputText("calculateMACOfDUKPTExtend INDEX_DUKPT_EMV ret:" + ret);
            if (ret == 0) {
                outputText("calculateMACOfDUKPTExtend INDEX_DUKPT_EMV KSN:" + BytesUtil.bytes2HexString(ksnBuff));
                outputText("MAC:" + BytesUtil.bytes2HexString(outdata, outlen[0]));
            }
            ret = pinpad.calculateMACOfDUKPTExtend(INDEX_DUKPT_PIN, rawData, rawData.length, outdata, outlen, ksnBuff, KsnLen);
            outputText("calculateMACOfDUKPTExtend INDEX_DUKPT_PIN ret:" + ret);
            if (ret == 0) {
                outputText("calculateMACOfDUKPTExtend INDEX_DUKPT_PIN KSN:" + BytesUtil.bytes2HexString(ksnBuff));
                outputText("MAC:" + BytesUtil.bytes2HexString(outdata, outlen[0]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void encryptWithPEK(int keyIndex) {
        byte[] rawData = BytesUtil.hexString2Bytes("9F260814E50F30268921459F2701409F1007060201039400029F3704A7D8F2329F36020601950500800000009A031901029B02E8009C0100");
        byte[] outdata = new byte[rawData.length];
        int[] outlen = new int[2];
        byte[] ksnBuff = new byte[10];
        int[] KsnLen = new int[2];
        try {
            int ret = pinpad.encryptWithPEK(3, keyIndex, rawData, rawData.length, outdata, outlen, ksnBuff, KsnLen);
            outputText("encryptWithPEK ret:" + ret);
            if (ret == 0) {
                outputText("KSN:" + BytesUtil.bytes2HexString(ksnBuff));
                outputText("Result:" + BytesUtil.bytes2HexString(outdata));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DukptEncryptDataIV(int keyIndex) {
        //9F260814E50F30268921459F2701409F1007060201039400029F3704A7D8F2329F36020601950500800000009A031901029B02E8009C0100
        byte[] rawData = BytesUtil.hexString2Bytes("9F260814E50F30268921459F2701409F1007060201039400029F3704A7D8F2329F36020601950500800000009A031901029B02E8009C0100");
        byte[] outdata = new byte[rawData.length];
        int[] outlen = new int[2];
        byte[] ksnBuff = new byte[10];
        int[] KsnLen = new int[2];
        byte[] ivData = new byte[8];
        int ivLen = 8;
        try {
            byte[] decOut = new byte[outdata.length];
            int[] decOutlen = new int[2];
            int ret;
            outputText("rawData:" + "9F260814E50F30268921459F2701409F1007060201039400029F3704A7D8F2329F36020601950500800000009A031901029B02E8009C0100");
            ret = pinpad.DukptEncryptDataIV(0x03, keyIndex, 0x00, ivData, ivLen, rawData, rawData.length, outdata, outlen, ksnBuff, KsnLen);
            outputText("DukptEncryptDataIV ENC ECB ret:" + ret);
            if (ret == 0) {
                outputText("KSN:" + BytesUtil.bytes2HexString(ksnBuff));
                outputText("Enc result:" + BytesUtil.bytes2HexString(outdata));
            }
            ret = pinpad.DukptEncryptDataIV(0x03, keyIndex, 0x10, ivData, ivLen, outdata, outdata.length, decOut, decOutlen, ksnBuff, KsnLen);
            outputText("DukptEncryptDataIV DEC ECB ret:" + ret);
            if (ret == 0) {
                outputText("KSN:" + BytesUtil.bytes2HexString(ksnBuff));
                outputText("Dec result:" + BytesUtil.bytes2HexString(decOut));
            }

            ret = pinpad.DukptEncryptDataIV(0x03, keyIndex, 0x01, ivData, ivLen, rawData, rawData.length, outdata, outlen, ksnBuff, KsnLen);
            outputText("DukptEncryptDataIV ENC CBC ret:" + ret);
            if (ret == 0) {
                outputText("KSN:" + BytesUtil.bytes2HexString(ksnBuff));
                outputText("Enc result:" + BytesUtil.bytes2HexString(outdata));
            }

            decOut = new byte[outdata.length];
            decOutlen = new int[2];
            ret = pinpad.DukptEncryptDataIV(0x03, keyIndex, 0x11, ivData, ivLen, outdata, outdata.length, decOut, decOutlen, ksnBuff, KsnLen);
            outputText("DukptEncryptDataIV DEC CBC ret:" + ret);
            if (ret == 0) {
                outputText("KSN:" + BytesUtil.bytes2HexString(ksnBuff));
                outputText("Dec result:" + BytesUtil.bytes2HexString(decOut));
            }

            outdata = new byte[8];
            ret = pinpad.DukptEncryptDataIV(0x04, 1, 0x54, ivData, ivLen, rawData, rawData.length, outdata, outlen, ksnBuff, KsnLen);
            outputText("DukptEncryptDataIV CMAC1 ret:" + ret);
            if (ret == 0) {
                outputText("KSN:" + BytesUtil.bytes2HexString(ksnBuff));
                outputText("Enc result:" + BytesUtil.bytes2HexString(outdata));

            }

            ret = pinpad.DukptEncryptDataIV(0x04, 2, 0x54, ivData, ivLen, rawData, rawData.length, outdata, outlen, ksnBuff, KsnLen);
            outputText("DukptEncryptDataIV CMAC2 ret:" + ret);
            if (ret == 0) {
                outputText("KSN:" + BytesUtil.bytes2HexString(ksnBuff));
                outputText("Enc result:" + BytesUtil.bytes2HexString(outdata));
            }

            ret = pinpad.DukptEncryptDataIV(0x04, 3, 0x54, ivData, ivLen, rawData, rawData.length, outdata, outlen, ksnBuff, KsnLen);
            outputText("DukptEncryptDataIV CMAC3 ret:" + ret);
            if (ret == 0) {
                outputText("KSN:" + BytesUtil.bytes2HexString(ksnBuff));
                outputText("Enc result:" + BytesUtil.bytes2HexString(outdata));
            }
            ret = pinpad.DukptEncryptDataIV(0x04, 4, 0x54, ivData, ivLen, rawData, rawData.length, outdata, outlen, ksnBuff, KsnLen);
            outputText("DukptEncryptDataIV CMAC4 ret:" + ret);
            if (ret == 0) {
                outputText("KSN:" + BytesUtil.bytes2HexString(ksnBuff));
                outputText("Enc result:" + BytesUtil.bytes2HexString(outdata));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DukptEncryptDataIV_MAC5(int keyIndex) {
        try {
            byte[] rawData = BytesUtil.hexString2Bytes("9F260814E50F30268921459F2701409F1007060201039400029F3704A7D8F2329F36020601950500800000009A031901029B02E8009C0100");
            byte[] outdata = new byte[8];
            int[] outlen = new int[2];
            byte[] ksnBuff = new byte[10];
            int[] KsnLen = new int[2];
            byte[] ivData = new byte[8];
            int ivLen = 8;
            int ret = pinpad.DukptEncryptDataIV(0x04, keyIndex, 0x54, ivData, ivLen, rawData, rawData.length, outdata, outlen, ksnBuff, KsnLen);
            if (ret == 0) {

                outputText("KSN:" + BytesUtil.bytes2HexString(ksnBuff));
                outputText("DukptEncryptDataIV_MAC5 result:" + BytesUtil.bytes2HexString(outdata));
            }

            rawData = BytesUtil.hexString2Bytes("9F260814E50F30268921459F2701409F1007060201039400029F3704A7D8F2329F36020601950500800000009A031901029B02E8009C");
            ret = pinpad.DukptEncryptDataIV(0x04, keyIndex, 0x54, ivData, ivLen, rawData, rawData.length, outdata, outlen, ksnBuff, KsnLen);
            if (ret == 0) {
                outputText("KSN:" + BytesUtil.bytes2HexString(ksnBuff));
                outputText("DukptEncryptDataIV_MAC5(Padding) result:" + BytesUtil.bytes2HexString(outdata));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GetDukptPinBlock() {
//        {PINKeyNo=3,
//        sound=false,
//        timeOutMS=30000,
//        KeyUsage=2,
//        title=PIN PAD,
//        message=Please enter PIN on keypad,
//        cardNo=581897******1060,
//        pinAlgMode=10,
//        bypass=true,
//        supportPinLen=0,6,
//        randomKeyboard=true,
//        onlinePin=true,
//        FullScreen=false}

        Bundle pinpadBundle = new Bundle();
        pinpadBundle.putString("cardNo", pan);
        pinpadBundle.putString("title", "Security Keyborad");
        pinpadBundle.putString("message", "Please enter your pin");
        pinpadBundle.putBoolean("sound", true);
        pinpadBundle.putBoolean("bypass", true);
        pinpadBundle.putString("supportPinLen", "0,4,5,6,7,8,9,10,11,12");
        pinpadBundle.putBoolean("FullScreen", true);
        pinpadBundle.putBoolean("onlinePin", true);
        pinpadBundle.putLong("timeOutMS", 30 * 1000);
        pinpadBundle.putInt("PINKeyNo", INDEX_DUKPT_PIN);
        pinpadBundle.putBoolean("randomKeyboard", true);
        pinpadBundle.putBoolean("FullScreen", true);

        try {
            pinpad.GetDukptPinBlock(pinpadBundle, new PinInputListener() {
                @Override
                public void onInput(int pinLen, int v) {
                    Log.e("pinpad", "onInput:pinLen = " + pinLen + ",v = " + (char) v);
                    outputText("onInput:" + "\n" + "pinLen:" + pinLen + ", keyCode:" + v);
                }

                @Override
                public void onConfirm(byte[] bytes, boolean b) {

                }

                @Override
                public void onConfirm_dukpt(byte[] pinBlock, byte[] ksn) {
                    if (pinBlock == null) {
                        outputText("onConfirm_dukpt:" + "\n" + "NonePin");
                        return;
                    }
                    if (ksn != null) {
                        outputText("GetDukptPinBlock KSN:" + BytesUtil.bytes2HexString(ksn));
                    }
                    if (pinBlock != null) {
                        outputText("PinBlock:" + BytesUtil.bytes2HexString(pinBlock));
                    }
                }

                @Override
                public void onCancel() {
                    outputText("onCancel");
                }

                @Override
                public void onTimeOut() {
                    outputText("onTimeOut");
                }

                @Override
                public void onError(int errorCode) {
                    outputText("onError:" + errorCode);
                    if (errorCode == 7010) {
                        //PCI requirement: PIN input cannot exceed 120 times within one hour
                    }
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void GetDukptAesPinBlock(int PINKeyNo, int WorkKeyType) {
        Bundle pinpadBundle = new Bundle();
        pinpadBundle.putString("cardNo", pan);
        pinpadBundle.putString("title", "Security Keyborad");
        pinpadBundle.putString("message", "Please enter your pin");
        pinpadBundle.putBoolean("sound", true);
        pinpadBundle.putBoolean("bypass", true);
        pinpadBundle.putString("supportPinLen", "0,4,5,6,7,8,9,10,11,12");
        pinpadBundle.putBoolean("FullScreen", true);
        pinpadBundle.putBoolean("onlinePin", true);
        pinpadBundle.putLong("timeOutMS", 30 * 1000);
        pinpadBundle.putInt("PINKeyNo", PINKeyNo);
        pinpadBundle.putInt("WorkKeyType", WorkKeyType);
        pinpadBundle.putBoolean("randomKeyboard", false);

        try {
            pinpad.GetDukptAesPinBlock(pinpadBundle, new PinInputListener() {
                @Override
                public void onInput(int pinLen, int v) {
                    Log.e("pinpad", "onInput:pinLen = " + pinLen + ",v = " + (char) v);
                    outputText("onInput:" + "\n" + "pinLen:" + pinLen + ", keyCode:" + v);
                }

                @Override
                public void onConfirm(byte[] bytes, boolean b) {

                }

                @Override
                public void onConfirm_dukpt(byte[] pinBlock, byte[] ksn) {
                    if (pinBlock == null) {
                        outputText("onConfirm_dukpt:" + "\n" + "NonePin");
                        return;
                    }
                    if (ksn != null) {
                        outputText("GetDukptPinBlock KSN:" + BytesUtil.bytes2HexString(ksn));
                    }
                    if (pinBlock != null) {
                        outputText("PinBlock:" + BytesUtil.bytes2HexString(pinBlock));
                    }
                    aesPinPadEnd = true;
                }

                @Override
                public void onCancel() {
                    aesPinPadEnd = true;
                    outputText("onCancel");
                }

                @Override
                public void onTimeOut() {
                    aesPinPadEnd = true;
                    outputText("onTimeOut");
                }

                @Override
                public void onError(int errorCode) {
                    aesPinPadEnd = true;
                    outputText("onError:" + errorCode);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //    textSize, margin
    //    each index(0-6):
    //    public static final int SECURITY_KEYBOARD_TITLE = 0;
    //    public static final int SECURITY_KEYBOARD_INFO = 1;
    //    public static final int SECURITY_KEYBOARD_PASSWORD = 2;
    //    public static final int SECURITY_KEYBOARD_KEY_NUMBER = 3;
    //    public static final int SECURITY_KEYBOARD_KEY_CANCEL = 4;
    //    public static final int SECURITY_KEYBOARD_KEY_DELETE = 5;
    //    public static final int SECURITY_KEYBOARD_KEY_OK = 6;

    public void startInputPin(Bundle pinpadBundle, final boolean isOnlinePin, final int keyIndex, final String plainKey, boolean randomLocation) {
        if (pinpadBundle == null || pinpadBundle.isEmpty()) {
            pinpadBundle = new Bundle();
            if (!isOnlinePin) {
                //For emv offline pin verification
                pinpadBundle.putInt("inputType", 3); //3-Offline plaintext, 4-Offline ciphertext
                pinpadBundle.putInt("CardSlot", 0);
            }
            pinpadBundle.putString("cardNo", pan);
            pinpadBundle.putInt("soundVolume", 10); //1-15
            pinpadBundle.putString("supportPinLen", "0,4,5,6,7,8,9,10,11,12");
            pinpadBundle.putBoolean("onlinePin", isOnlinePin);
            pinpadBundle.putInt("PINKeyNo", keyIndex);
            pinpadBundle.putLong("timeOutMS", 30 * 1000);
            pinpadBundle.putString("title", "Security Keyboard");
            pinpadBundle.putString("message", "Enter Your Pin");
            pinpadBundle.putBoolean("sound", true);
            pinpadBundle.putBoolean("bypass", true);
            pinpadBundle.putBoolean("randomKeyboard", false);
            pinpadBundle.putBoolean("FullScreen", false);
            //Customization UI for half screen
            pinpadBundle.putInt("customKeyboardDialog", 5);

            //    backgroundColor, textColor
            //    each index(0-15):
            //    public static final int SECURITY_KEYBOARD_TITLE = 0;
            //    public static final int SECURITY_KEYBOARD_INFO = 1;
            //    public static final int SECURITY_KEYBOARD_PASSWORD = 2;
            //    public static final int SECURITY_KEYBOARD_KEY_NUMBER = 3;
            //    public static final int SECURITY_KEYBOARD_KEY_CANCEL = 4;
            //    public static final int SECURITY_KEYBOARD_KEY_DELETE = 5;
            //    public static final int SECURITY_KEYBOARD_KEY_OK = 6;
            //    public static final int SECURITY_KEYBOARD_HEAD = 7;
            //    public static final int SECURITY_KEYBOARD_MONEY = 8;
            //    public static final int SECURITY_KEYBOARD_VIEW = 9;
            //    public static final int SECURITY_KEYBOARD_KEY_BLANK = 10;
            //    public static final int SECURITY_KEYBOARD_KEY = 11;
            //    public static final int SECURITY_KEYBOARD_BODY = 12;
            //    public static final int SECURITY_KEYBOARD_BACKSPACE = 13;
            //    public static final int SECURITY_KEYBOARD_KEY_CONTINUE = 14;
            //    public static final int SECURITY_KEYBOARD_PASSWORD_BORDER = 15;
//            int[] backgroundColor = {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, 0xFFFF0000, 0xFFFCAC1B, 0xFF00FF00, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE};
//            pinpadBundle.putIntArray("backgroundColor", backgroundColor);

//            if (TextUtils.equals("I5000", model)) {
//                pinpadBundle.putBoolean("FullScreen", isOnlinePin);
//                int transparentWhite = Color.TRANSPARENT;
//                outputText("transparentWhite:" + transparentWhite);
//                backgroundColor = new int[]{transparentWhite, transparentWhite, transparentWhite,
//                        transparentWhite, transparentWhite, transparentWhite, transparentWhite,
//                        transparentWhite, transparentWhite, transparentWhite, transparentWhite,
//                        transparentWhite, transparentWhite, transparentWhite, transparentWhite, Color.BLUE};
//                int[] textColor = {Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW,
//                        Color.YELLOW, Color.YELLOW, Color.YELLOW,
//                        Color.YELLOW, Color.YELLOW, Color.YELLOW,
//                        Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW};
//                pinpadBundle.putIntArray("backgroundColor", backgroundColor);
//                pinpadBundle.putIntArray("textColor", textColor);
//
//                //    textSize, margin
//                //    each index(0-6):
//                //    public static final int SECURITY_KEYBOARD_TITLE = 0;
//                //    public static final int SECURITY_KEYBOARD_INFO = 1;
//                //    public static final int SECURITY_KEYBOARD_PASSWORD = 2;
//                //    public static final int SECURITY_KEYBOARD_KEY_NUMBER = 3;
//                //    public static final int SECURITY_KEYBOARD_KEY_CANCEL = 4;
//                //    public static final int SECURITY_KEYBOARD_KEY_DELETE = 5;
//                //    public static final int SECURITY_KEYBOARD_KEY_OK = 6;
//                short[] leftMargin = {0, 0, 50, 0, 0, 0, 0};
//                short[] rightMargin = {0, 0, 50, 0, 0, 0, 0};
//                short[] topMargin = {0, 0, 10, 0, 0, 0, 0};
//                short[] bottomMargin = {0, 0, 10, 0, 0, 0, 0};
//                pinpadBundle.putShortArray("leftMargin", leftMargin);
//                pinpadBundle.putShortArray("rightMargin", rightMargin);
//                pinpadBundle.putShortArray("topMargin", topMargin);
//                pinpadBundle.putShortArray("bottomMargin", bottomMargin);
//            }

            //		short [] textSize = {10, 10, 10, 10, 10, 10, 10 };   //  10-32  // 720*1280
            //		for(int i=0; i<7; i++){
            //			textSize[i] = 20;
            //		}
            //		textSize[SECURITY_KEYBOARD_KEY_CANCEL] = 15;
            //		textSize[SECURITY_KEYBOARD_KEY_DELETE] = 15;
            //		textSize[SECURITY_KEYBOARD_KEY_OK] = 15;
            //		pinpadBundle.putShortArray("textSize", textSize);  // text size
//            String[] numberText = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
//            for (int i = 0; i < numberText.length; i++) {
//                numberText[i] = DataFormatUtil.getNumberEnglishToArab(numberText[i]);
//            }
//            pinpadBundle.putStringArray("numberText", numberText);
//            pinpadBundle.putString("cancelText", "Cancel1");
//            pinpadBundle.putString("deleteText", "Delete1");
//            pinpadBundle.putString("okText", "Ok1");
        }
        try {
            pinpad.getPinBlockEx(pinpadBundle, new PinInputListener() {

                @Override
                public void onInput(int pinLen, int v) {
                    Log.e("pinpad", "onInput:pinLen = " + pinLen + ",v = " + (char) v);
                    outputText("onInput:" + "\n" + "pinLen:" + pinLen + ", keyCode:" + v);
                }

                @Override
                public void onConfirm(byte[] data, boolean isNonePin) {
                    //PIN密文
                    if (data == null) {
                        outputText("onConfirm:" + "\n" + "isNonePin:" + isNonePin + "\n" + "pinData:" + "");
                        return;
                    }

                    String pinBlockStr = (data == null) ? "" : new String(data);
                    if (!isOnlinePin) {
                        pinBlockStr = new String(data);
                        outputText("onConfirm:" + "\n" + "isNonePin:" + isNonePin + "\n" + "plain pinData:" + pinBlockStr);
                    } else {
                        outputText("onConfirm:" + "\n" + "encrypted pinData:" + pinBlockStr + "\n" + "isNonePin:" + isNonePin);
                        //decrypt PIN
                        byte[] pinBlock = BytesUtil.hexString2Bytes(pinBlockStr);
                        if (keyAlg == Constant.KeyAlgorithm.AES) {
                            pinBlockStr = PinpadUtil.getPinData_AES(pan, BytesUtil.hexString2Bytes(plainKey), pinBlock);
                        } else if (keyAlg == Constant.KeyAlgorithm.SM4) {
                            pinBlockStr = PinpadUtil.getPinData_SM4(pan, BytesUtil.hexString2Bytes(plainKey), pinBlock);
                        } else {
                            pinBlockStr = PinpadUtil.getPinData(pan, BytesUtil.hexString2Bytes(plainKey), pinBlock);
                        }
                        outputText("Plaintext PIN:" + pinBlockStr);
                    }
                }

                @Override
                public void onConfirm_dukpt(byte[] PinBlock, byte[] ksn) {

                }

                @Override
                public void onCancel() {
                    outputText("onCancel");
                }

                @Override
                public void onTimeOut() {
                    outputText("onTimeOut");
                }

                @Override
                public void onError(int errorCode) {
                    outputText("onError:" + errorCode);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String strJson;
    private Bitmap cancelBitmap;
    private Bitmap delBitmap;
    private Bitmap okBitemap;
    private Bitmap backspaceBitmap;
    private Bitmap imageViewBitmap;
    private Bitmap bodyBitmap;
    private Bitmap keyBitmap;
    private Bitmap echoBitmap_0;
    private Bitmap echoBitmap_1;
    private Bitmap echoBitmap_2;
    public static final int SECURITY_KEYBOARD_TITLE = 0;
    public static final int SECURITY_KEYBOARD_INFO = 1;
    public static final int SECURITY_KEYBOARD_PASSWORD = 2;
    public static final int SECURITY_KEYBOARD_KEY_NUMBER = 3;
    public static final int SECURITY_KEYBOARD_KEY_CANCEL = 4;
    public static final int SECURITY_KEYBOARD_KEY_DELETE = 5;
    public static final int SECURITY_KEYBOARD_KEY_OK = 6;
    public static final int SECURITY_KEYBOARD_HEAD = 7;
    public static final int SECURITY_KEYBOARD_MONEY = 8;
    public static final int SECURITY_KEYBOARD_VIEW = 9;
    public static final int SECURITY_KEYBOARD_KEY_BLANK = 10;
    public static final int SECURITY_KEYBOARD_KEY = 11;
    public static final int SECURITY_KEYBOARD_BODY = 12;
    public static final int SECURITY_KEYBOARD_BACKSPACE = 13;
    private int[] backgroundColor = {0XFF1c1c1c, 0XFFFFFFFF, 0XFFFFFFFF, 0XFFFFFFFF, 0Xffe3452f, 0Xffb4ac24, 0Xff2ead2a, Color.WHITE, 0XFFFFFFFF, 0XFFFFFFFF, 0XFFFFFFFF, 0X00FFFFFF, 0XFFFFFFFF, 0XFF1c1c1c};
    private int[] textColor = {Color.BLACK, Color.GRAY, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK};


    public void customKeyBoardJSON_HALF() {
        Bundle pinpadBundle = new Bundle();
        pinpadBundle.putString("cardNo", pan);
        pinpadBundle.putString("supportPinLen", "0,4,5,6,7,8,9,10,11,12");
        pinpadBundle.putInt("PINKeyNo", INDEX_WK);
        pinpadBundle.putLong("timeOutMS", 30 * 1000);
        pinpadBundle.putBoolean("sound", true);
        pinpadBundle.putBoolean("bypass", true);
        pinpadBundle.putBoolean("FullScreen", false);
        pinpadBundle.putBoolean("onlinePin", true);
        pinpadBundle.putBoolean("randomKeyboard", false);

        int[] textColor = {Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.WHITE, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK};
        pinpadBundle.putIntArray("textColor", textColor);
        int[] backgroundColor = {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, 0xFFFF0000, 0xFFFCAC1B, 0xFF00FF00, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE};
        pinpadBundle.putIntArray("backgroundColor", backgroundColor);

        if (width == 480) {
            strJson = getJson("json_custom_screen_half_480x800.json", this);
        } else {
            strJson = getJson("json_custom_screen_half.json", this);
        }

        pinpadBundle.putString("strJson", strJson);
        pinpadBundle.putBoolean("customization", true);

        startInputPin(pinpadBundle, true, INDEX_WK, plainPinKey, true);
    }

    public void customKeyBoard1() {
        Bundle pinpadBundle = new Bundle();
        pinpadBundle.putString("cardNo", pan);
        pinpadBundle.putString("supportPinLen", "0,4,5,6,7,8,9,10,11,12");
        pinpadBundle.putInt("PINKeyNo", INDEX_WK);
        pinpadBundle.putLong("timeOutMS", 30 * 1000);
        pinpadBundle.putString("title", "Security Keyborad");
        pinpadBundle.putString("message", "Please enter your pin");
        pinpadBundle.putBoolean("sound", true);
        pinpadBundle.putBoolean("bypass", true);
        pinpadBundle.putBoolean("FullScreen", true);
        pinpadBundle.putBoolean("onlinePin", true);

        pinpadBundle.putBoolean("randomKeyboard", true);
        pinpadBundle.putBoolean("randomKeyboardLocation", false);
        pinpadBundle.putBoolean("inputBySP", false);
        pinpadBundle.putString("infoLocation", "CENTER");
        pinpadBundle.putBoolean("customKeyboard", true);
        pinpadBundle.putInt("customKeyboardDialog", 4);//Hardcode
        pinpadBundle.putString("head", "Urovo Urovo Urovo");
        pinpadBundle.putString("money", "$110");
        startInputPin(pinpadBundle, true, INDEX_WK, plainPinKey, false);
    }

    public void customKeyBoard2() {
        Bundle pinpadBundle = new Bundle();
        pinpadBundle.putString("cardNo", pan);
        pinpadBundle.putString("supportPinLen", "0,4,5,6,7,8,9,10,11,12");
        pinpadBundle.putInt("PINKeyNo", INDEX_WK);
        pinpadBundle.putLong("timeOutMS", 30 * 1000);
        pinpadBundle.putString("title", "Security Keyborad");
        pinpadBundle.putString("message", "Please enter your pin");
        pinpadBundle.putBoolean("sound", true);
        pinpadBundle.putBoolean("bypass", true);
        pinpadBundle.putBoolean("FullScreen", true);
        pinpadBundle.putBoolean("onlinePin", true);

        pinpadBundle.putBoolean("randomKeyboard", true);
        pinpadBundle.putBoolean("randomKeyboardLocation", false);
        pinpadBundle.putBoolean("inputBySP", false);
        pinpadBundle.putString("infoLocation", "LEFT");
        pinpadBundle.putBoolean("customKeyboard", true);
        pinpadBundle.putInt("customKeyboardDialog", 4);//Hardcode
        pinpadBundle.putString("head", "Urovo Urovo Urovo");
        pinpadBundle.putString("money", "$110");
        startInputPin(pinpadBundle, true, INDEX_WK, plainPinKey, false);
    }

    public void customKeyBoard3JSON() {
        cancelBitmap = getImageFromAssetsFile(PinpadActivity.this, "pinpad_cancel_2.jpg");
        delBitmap = getImageFromAssetsFile(PinpadActivity.this, "pinpad_del_2.jpg");
        okBitemap = getImageFromAssetsFile(PinpadActivity.this, "pinpad_ok_2.jpg");
        backspaceBitmap = getImageFromAssetsFile(PinpadActivity.this, "pinpad_backspace_white.png");
        if (width == 480 && height == 800) {
            strJson = getJson("json_custom3_480x800.json", PinpadActivity.this);
        } else if (width == 480 && height == 854) {
            backspaceBitmap = getImageFromAssetsFile(PinpadActivity.this, "pinpad_backspace_white.png");
            strJson = getJson("json_custom3_480x854.json", PinpadActivity.this);
        } else {
            strJson = getJson("json_custom3_720x1280.json", PinpadActivity.this);
        }
        Bundle pinpadBundle = new Bundle();
        pinpadBundle.putString("cardNo", pan);
        pinpadBundle.putString("supportPinLen", "0,4,5,6,7,8,9,10,11,12");
        pinpadBundle.putInt("PINKeyNo", INDEX_WK);
        pinpadBundle.putLong("timeOutMS", 30 * 1000);
        pinpadBundle.putString("title", "");
        pinpadBundle.putString("money", "21,00 €");
        pinpadBundle.putString("head", "Introduzca pin y pulse aceptar");
        pinpadBundle.putString("message", "CONTACLTLESS11");
        pinpadBundle.putBoolean("sound", true);
        pinpadBundle.putBoolean("bypass", true);
        pinpadBundle.putBoolean("FullScreen", true);
        pinpadBundle.putBoolean("onlinePin", true);

        pinpadBundle.putIntArray("backgroundColor", backgroundColor);
        pinpadBundle.putIntArray("textColor", textColor);
        pinpadBundle.putBoolean("inputBySP", false);
        pinpadBundle.putString("infoLocation", "RIGHT");
        pinpadBundle.putBoolean("randomKeyboard", false);
        pinpadBundle.putBoolean("customization", true);
        pinpadBundle.putString("strJson", strJson);
        pinpadBundle.putParcelable("cancelBitmap", cancelBitmap);
        pinpadBundle.putParcelable("delBitmap", delBitmap);
        pinpadBundle.putParcelable("okBitmap", okBitemap);
        pinpadBundle.putParcelable("backspaceBitmap", backspaceBitmap);
        pinpadBundle.putBoolean("randomKeyboardLocation", false);
        pinpadBundle.putIntArray("randomKeyboardStaticLocation", new int[]{0, randomInt(height)});

        startInputPin(pinpadBundle, true, INDEX_WK, plainPinKey, true);
    }

    public int randomInt(int height) {
        int[] randomValue = new int[]{440, 480, 520, 560, 600, 660};
        String base = "012345";
        if (width == 480 && (height == 800)) {
            randomValue = new int[]{280, 310, 340, 360, 400};
            base = "01234";
        } else if (width == 480 && height == 854) {
            randomValue = new int[]{280, 310, 340, 360, 427};
            base = "01234";
        }
        int result = 0;
        Random random = new Random();
        int index = random.nextInt(base.length());
        result = randomValue[index];
        Log.e(TAG, "randomInt:" + result);
        return result;
    }

    public void customKeyBoard4JSON() {
        if (width == 480 && height == 800) {
            cancelBitmap = getImageFromAssetsFile(PinpadActivity.this, "cancel_butt_off_s.png");
            delBitmap = getImageFromAssetsFile(PinpadActivity.this, "delete_butt_off_s.png");
            okBitemap = getImageFromAssetsFile(PinpadActivity.this, "ok_butt_off_s.png");
            backspaceBitmap = getImageFromAssetsFile(PinpadActivity.this, "back_white.png");
            imageViewBitmap = getImageFromAssetsFile(PinpadActivity.this, "lock_art.png");
            bodyBitmap = getImageFromAssetsFile(PinpadActivity.this, "bg_480x800.png");
            keyBitmap = getImageFromAssetsFile(PinpadActivity.this, "bg_keyboard.png");
            strJson = getJson("json_custom4_480x800.json", PinpadActivity.this);
        } else {
            cancelBitmap = getImageFromAssetsFile(PinpadActivity.this, "cancel_butt_off.png");
            delBitmap = getImageFromAssetsFile(PinpadActivity.this, "delete_butt_off.png");
            okBitemap = getImageFromAssetsFile(PinpadActivity.this, "ok_butt_off.png");
            backspaceBitmap = getImageFromAssetsFile(PinpadActivity.this, "back_white.png");
            imageViewBitmap = getImageFromAssetsFile(PinpadActivity.this, "lock_art.png");
            bodyBitmap = getImageFromAssetsFile(PinpadActivity.this, "bg_720x1280.png");
            keyBitmap = getImageFromAssetsFile(PinpadActivity.this, "bg_keyboard.png");
            strJson = getJson("json_custom4_720x1280.json", PinpadActivity.this);
        }
        Bundle pinpadBundle = new Bundle();
        pinpadBundle.putString("cardNo", pan);
        pinpadBundle.putBoolean("sound", true);
        pinpadBundle.putBoolean("bypass", true);
        pinpadBundle.putString("supportPinLen", "0,4,5,6,7,8,9,10,11,12");
        pinpadBundle.putBoolean("FullScreen", true);
        pinpadBundle.putBoolean("onlinePin", true);
        pinpadBundle.putInt("PINKeyNo", INDEX_WK);
        pinpadBundle.putLong("timeOutMS", 30 * 1000);
        pinpadBundle.putString("title", "");
        pinpadBundle.putString("head", "");
        pinpadBundle.putString("money", "");
        pinpadBundle.putString("message", "Enter your pin");

        int[] backgroundColor = {0X00FFFFFF, 0X00FFFFFF, 0X00FFFFFF, 0X00e3452f, 0X00895623, 0X00258945, 0X00364952, 0XFF123456, 0XFF876328, 0X00FFFFFF, 0XFF877454, 0X00FFFFFF, 0xff1234FF, 0X001c1c1c};
        int[] textColor = {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE};
        pinpadBundle.putIntArray("backgroundColor", backgroundColor);
        pinpadBundle.putIntArray("textColor", textColor);
        pinpadBundle.putBoolean("inputBySP", false);
        pinpadBundle.putString("infoLocation", "RIGHT");
        pinpadBundle.putBoolean("randomKeyboard", false);
        pinpadBundle.putBoolean("randomKeyboardLocation", false);
        pinpadBundle.putBoolean("customization", true);
        pinpadBundle.putString("strJson", strJson);
        pinpadBundle.putParcelable("cancelBitmap", cancelBitmap);
        pinpadBundle.putParcelable("delBitmap", delBitmap);
        pinpadBundle.putParcelable("okBitmap", okBitemap);
        pinpadBundle.putParcelable("backspaceBitmap", backspaceBitmap);
        pinpadBundle.putParcelable("viewBitmap", imageViewBitmap);
        pinpadBundle.putParcelable("bodyBitmap", bodyBitmap);

        startInputPin(pinpadBundle, true, INDEX_WK, plainPinKey, true);
    }

    public void customKeyBoard5JSON() {
        cancelBitmap = getImageFromAssetsFile(PinpadActivity.this, "button_cancel_5.png");
        delBitmap = getImageFromAssetsFile(PinpadActivity.this, "button_delete_5.png");
        okBitemap = getImageFromAssetsFile(PinpadActivity.this, "button_confirm_5.png");
        backspaceBitmap = getImageFromAssetsFile(PinpadActivity.this, "back_black.png");

        if (width == 480 && height == 854) {
            strJson = getJson("json_custom5_480x854.json", this);
        } else {
            strJson = getJson("json_custom5_720x1280.json", this);
        }

        Bundle pinpadBundle = new Bundle();
        pinpadBundle.putString("cardNo", pan);
        pinpadBundle.putString("supportPinLen", "0,4,5,6,7,8,9,10,11,12");
        pinpadBundle.putInt("PINKeyNo", INDEX_WK);
        pinpadBundle.putLong("timeOutMS", 30 * 1000);
        pinpadBundle.putString("title", "");
        pinpadBundle.putString("head", "");
        pinpadBundle.putString("money", "");
        pinpadBundle.putString("message", "Enter your pin");
        pinpadBundle.putBoolean("sound", true);
        pinpadBundle.putBoolean("bypass", true);
        pinpadBundle.putBoolean("FullScreen", true);
        pinpadBundle.putBoolean("onlinePin", true);

        int[] textColor = {Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK};
        pinpadBundle.putIntArray("textColor", textColor);
        pinpadBundle.putBoolean("inputBySP", false);
        pinpadBundle.putBoolean("randomKeyboard", false);
        pinpadBundle.putBoolean("randomKeyboardLocation", false);
        pinpadBundle.putBoolean("customization", true);
        pinpadBundle.putString("strJson", strJson);
        pinpadBundle.putParcelable("cancelBitmap", cancelBitmap);
        pinpadBundle.putParcelable("delBitmap", delBitmap);
        pinpadBundle.putParcelable("okBitmap", okBitemap);
        pinpadBundle.putParcelable("backspaceBitmap", backspaceBitmap);

        startInputPin(pinpadBundle, true, INDEX_WK, plainPinKey, true);
    }

    public void customKeyBoard6JSON() {
        if (width == 480 && height == 800) {
            delBitmap = getImageFromAssetsFile(PinpadActivity.this, "button_delete_6_s.png");
            okBitemap = getImageFromAssetsFile(PinpadActivity.this, "button_confirm_6_s.png");
            backspaceBitmap = getImageFromAssetsFile(PinpadActivity.this, "back_green.png");
            //===========================
//                        keyBitmap = getImageFromAssetsFile(PinpadActivity.this, "bg_keyboard_6_s.png");
//                        echoBitmap_0 = getImageFromAssetsFile(PinpadActivity.this, "input_white.png");
//                        echoBitmap_1 = getImageFromAssetsFile(PinpadActivity.this, "input_yellow.png");
//                        echoBitmap_2 = getImageFromAssetsFile(PinpadActivity.this, "input_green.png");
//                          strJson = getJson("json_custom6_480x800.json", PinpadActivity.this);
            //===========================
            strJson = getJson("json_custom6_480x800_2.json", PinpadActivity.this);
            bodyBitmap = getImageFromAssetsFile(PinpadActivity.this, "bg_480x800_6.png");
            //===========================
        } else {
            delBitmap = getImageFromAssetsFile(PinpadActivity.this, "button_delete_6.png");
            okBitemap = getImageFromAssetsFile(PinpadActivity.this, "button_confirm_6.png");
            backspaceBitmap = getImageFromAssetsFile(PinpadActivity.this, "back_green.png");
            //===========================
//                        keyBitmap = getImageFromAssetsFile(PinpadActivity.this, "bg_keyboard_6.png");
//                        echoBitmap_0 = getImageFromAssetsFile(PinpadActivity.this, "input_white.png");
//                        echoBitmap_1 = getImageFromAssetsFile(PinpadActivity.this, "input_yellow.png");
//                        echoBitmap_2 = getImageFromAssetsFile(PinpadActivity.this, "input_green.png");
//                        strJson = getJson("json_custom6_720x1280.json", PinpadActivity.this);
            //===========================
            strJson = getJson("json_custom6_720x1280_2.json", PinpadActivity.this);
            bodyBitmap = getImageFromAssetsFile(PinpadActivity.this, "bg_720x1280_6.png");
            //===========================
        }
        Bundle pinpadBundle = new Bundle();
        pinpadBundle.putString("cardNo", pan);
        pinpadBundle.putString("supportPinLen", "0,4");
        pinpadBundle.putInt("PINKeyNo", INDEX_WK);
        pinpadBundle.putLong("timeOutMS", 30 * 1000);
        pinpadBundle.putString("title", "PIN de seguridad");
        pinpadBundle.putString("money", "Amount: 1.00");
        pinpadBundle.putString("message", "v.0.23 - build 5678        Serial 12345678");
        pinpadBundle.putBoolean("sound", true);
        pinpadBundle.putBoolean("bypass", true);
        pinpadBundle.putBoolean("FullScreen", true);
        pinpadBundle.putBoolean("onlinePin", true);

        int[] backgroundColor = {0X00FFFFFF, 0X00FFFFFF, 0X00FFFFFF, 0X00e3452f, 0X00895623, 0X00258945, 0X00364952, 0XFF123456, 0XFF876328, 0X00FFFFFF, 0XFF877454, 0X00FFFFFF, Color.BLACK, Color.BLACK, 0XFF1ED94F};
        int[] textColor = {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLACK};
        pinpadBundle.putIntArray("backgroundColor", backgroundColor);
        pinpadBundle.putIntArray("textColor", textColor);
        pinpadBundle.putBoolean("inputBySP", false);
        pinpadBundle.putBoolean("randomKeyboard", true);
        pinpadBundle.putBoolean("customization", true);
        pinpadBundle.putString("strJson", strJson);
        pinpadBundle.putParcelable("backspaceBitmap", backspaceBitmap);
        pinpadBundle.putParcelable("delBitmap", delBitmap);
        pinpadBundle.putParcelable("okBitmap", okBitemap);

        //===========================
//        pinpadBundle.putParcelable("echoBitmap_0", echoBitmap_0);
//        pinpadBundle.putParcelable("echoBitmap_1", echoBitmap_1);
//        pinpadBundle.putParcelable("echoBitmap_2", echoBitmap_2);
        //===========================
        pinpadBundle.putParcelable("bodyBitmap", bodyBitmap);
        //===========================
        startInputPin(pinpadBundle, true, INDEX_WK, plainPinKey, true);
    }

    public void customKeyBoard6JSON_I5000() {
        //===========================
//                        echoBitmap_0 = getImageFromAssetsFile(PinpadActivity.this, "input_gray.jpg");
//                        echoBitmap_1 = getImageFromAssetsFile(PinpadActivity.this, "input_black.jpg");
//                        echoBitmap_2 = getImageFromAssetsFile(PinpadActivity.this, "input_black.jpg");
        //===========================

//                        bodyBitmap = getImageFromAssetsFile(PinpadActivity.this, "bg_echo_line.png");
        imageViewBitmap = getImageFromAssetsFile(PinpadActivity.this, "line.png");
        strJson = getJson("json_custom6_240x320.json", PinpadActivity.this);
        Bundle pinpadBundle = new Bundle();
        pinpadBundle.putString("cardNo", pan);
        pinpadBundle.putString("supportPinLen", "0,6");
        pinpadBundle.putInt("PINKeyNo", INDEX_WK);
        pinpadBundle.putLong("timeOutMS", 30 * 1000);
        pinpadBundle.putString("message", "Please enter PIN");
        pinpadBundle.putString("money", "Amount: $12,789");
        pinpadBundle.putBoolean("sound", true);
        pinpadBundle.putBoolean("bypass", true);
        pinpadBundle.putBoolean("FullScreen", true);
        pinpadBundle.putBoolean("onlinePin", true);

        int[] backgroundColor = {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.BLUE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE};
        int[] textColor = {Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.WHITE, Color.WHITE, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK};
        pinpadBundle.putIntArray("backgroundColor", backgroundColor);
        pinpadBundle.putIntArray("textColor", textColor);
        pinpadBundle.putBoolean("inputBySP", false);
        pinpadBundle.putBoolean("randomKeyboard", true);
        pinpadBundle.putBoolean("customization", true);
        pinpadBundle.putString("strJson", strJson);

        //===========================
//        pinpadBundle.putParcelable("echoBitmap_0", echoBitmap_0);
//        pinpadBundle.putParcelable("echoBitmap_1", echoBitmap_1);
//        pinpadBundle.putParcelable("echoBitmap_2", echoBitmap_2);
        //===========================

        pinpadBundle.putParcelable("viewBitmap", imageViewBitmap);
//        pinpadBundle.putParcelable("bodyBitmap", bodyBitmap);
        startInputPin(pinpadBundle, true, INDEX_WK, plainPinKey, true);
    }

    public void customKeyBoardJSON_Blind(int type) {
        Bundle pinpadBundle = new Bundle();
        pinpadBundle.putString("cardNo", pan);
        pinpadBundle.putString("supportPinLen", "0,4,5,6,7,8,9,10,11,12");
        pinpadBundle.putInt("PINKeyNo", INDEX_WK);
        pinpadBundle.putLong("timeOutMS", 30 * 1000);
        pinpadBundle.putString("title", "Security Keyboard");
        pinpadBundle.putString("message", "Enter your pin");
        pinpadBundle.putBoolean("sound", true);
        pinpadBundle.putBoolean("bypass", true);
        pinpadBundle.putBoolean("FullScreen", true);
        pinpadBundle.putBoolean("onlinePin", true);

        int[] textColor = {Color.BLACK, Color.BLACK, Color.BLACK, Color.WHITE, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK};

        Locale locale = this.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        outputText("System language:" + language);

        language = new DeviceManager().getSettingProperty("Secure-tts_default_locale");
        outputText("TTS language:" + language);
        if (width == 480 && height == 854) {
            strJson = getJson("json_custom_blind_480x854.json", PinpadActivity.this);
        } else {
            strJson = getJson("json_custom_blind_720x1280.json", PinpadActivity.this);
        }

        //set tts language: Chinese zh_CN
        boolean status = new DeviceManager().setSettingProperty("Secure-tts_default_locale", "com.google.android.tts:zh_CN");
        Log.e(TAG, "Secure-tts_default_locale:" + status);
        Pinpad jsonPinpad = new Gson().fromJson(strJson, Pinpad.class);
        Pinpad.Key cancelKey = jsonPinpad.getKey_cancel();
        cancelKey.setText("取消");
        jsonPinpad.setKey_cancel(cancelKey);
        Pinpad.Key okKey = jsonPinpad.getKey_ok();
        okKey.setText("确认");
        jsonPinpad.setKey_ok(okKey);
        Translations translations = new Translations();
        translations.setPinpad_below("键盘在下面");
        translations.setPinpad_blank_click_tip("未选中任何数字或者是功能键");
        translations.setPinpad_input_less("对不起，密码过短");
        translations.setPinpad_input_more("对不起，密码过长");
        translations.setPassword_confirm("确认密码");
        translations.setPassword_cancel("退出键盘");
        translations.setHasSelectedOne("输入一位");
        translations.setHasSelectedTwo("输入两位");
        translations.setHasSelectedThree("输入三位");
        translations.setHasSelectedFour("输入四位");
        translations.setHasSelectedFive("输入五位");
        translations.setHasSelectedSix("输入六位");
        translations.setHasSelectedSeven("输入七位");
        translations.setHasSelectedEight("输入八位");
        translations.setHasSelectedNine("输入九位");
        translations.setHasSelectedTen("输入十位");
        translations.setHasSelectedEleven("输入十一位");
        translations.setHasSelectedTwelve("输入十二位");
        jsonPinpad.setTranslations(translations);
        strJson = new Gson().toJson(jsonPinpad);

        //set tts language: Spanish es_ES
//        new DeviceManager().setSettingProperty("Secure-tts_default_locale", "com.google.android.tts:es_ES");
//        //set tts message
//        Pinpad jsonPinpad = new Gson().fromJson(strJson, Pinpad.class);
//        Pinpad.Key cancelKey = jsonPinpad.getKey_cancel();
//        cancelKey.setText("Cancelación");
//        jsonPinpad.setKey_cancel(cancelKey);
//        Pinpad.Key okKey = jsonPinpad.getKey_ok();
//        okKey.setText("Confirmación");
//        jsonPinpad.setKey_ok(okKey);
//        Translations translations = new Translations();
//        translations.setPinpad_below("Teclado más abajo");
//        translations.setPinpad_blank_click_tip("Faltan dígitos");
//        translations.setPinpad_input_less("Faltan dígitos");
//        translations.setPinpad_input_more("Faltan dígitos");
//        translations.setPassword_confirm("Confirmación");
//        translations.setPassword_cancel("Cancelación");
//        translations.setHasSelectedOne("El primer número introducido");
//        translations.setHasSelectedTwo("Introduzca el segundo número");
//        translations.setHasSelectedThree("Introduzca el tercer número");
//        translations.setHasSelectedFour("Se introdujo el cuarto número");
//        translations.setHasSelectedFive("Se introdujo el quinto número");
//        translations.setHasSelectedSix("Se introdujo el sexto número");
//        translations.setHasSelectedSeven("Se introdujo el séptimo número");
//        translations.setHasSelectedEight("Se introdujo el octavo número");
//        translations.setHasSelectedNine("Se introdujo el noveno número");
//        translations.setHasSelectedTen("Se introdujo el décimo número");
//        translations.setHasSelectedEleven("Se introdujo el undécimo número");
//        translations.setHasSelectedTwelve("Se introducido el duodécimo número");
//        jsonPinpad.setTranslations(translations);
//        strJson = new Gson().toJson(jsonPinpad);

        pinpadBundle.putIntArray("textColor", textColor);
        pinpadBundle.putBoolean("inputBySP", false);
        pinpadBundle.putBoolean("randomKeyboard", false);
        pinpadBundle.putBoolean("randomKeyboardLocation", false);
        pinpadBundle.putBoolean("customization", true);
        //Mandaroty for blind mode, fixed value 8
        pinpadBundle.putInt("customKeyboardDialog", type);
        pinpadBundle.putString("strJson", strJson);
        startInputPin(pinpadBundle, true, INDEX_WK, plainPinKey, true);
    }

    public static String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void getRSAPublicKeyModel() {
        //type(1 bytes)+keyId(3 bytes)+ipek(16 bytes)+ksn(10 bytes)
        //01 000003 703DCF7100C7C516F7E08D5D85CE6D74 11111746011BEDE00001
        byte[] ClearDukptKey = BytesUtil.hexString2Bytes("01000003703DCF7100C7C516F7E08D5D85CE6D7411111746011BEDE00001");
        Log.e(TAG, "ClearDukptKey:" + BytesUtil.bytes2HexString(ClearDukptKey));
        byte[] publicKeyBuff = new byte[512];
        int[] publicKeyLen = new int[2];
        int[] exponent = new int[2];
        byte[] modulus;
        try {
            boolean status = pinpad.getRSAPublicKeyModel(publicKeyBuff, publicKeyLen, exponent);
            if (!status) {
                Log.e(TAG, "getRSAPublicKeyModel() error");
                return;
            }
            modulus = new byte[publicKeyLen[0]];
            System.arraycopy(publicKeyBuff, 0, modulus, 0, modulus.length);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            BigInteger n = new BigInteger(modulus);
            BigInteger e = new BigInteger(String.valueOf(exponent[0]));
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            Log.e(TAG, "publicKey:" + publicKey);
            /**进行RSA加密*/
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] cipherDukptKey = cipher.doFinal(ClearDukptKey);
            Log.e(TAG, "cipherDukptKey:" + Funs.bytesToHexString(cipherDukptKey));

            //type(1 bytes)+keyId(3 bytes)+ipek(16 bytes)+ksn(10 bytes)
            int ret = pinpad.loadDukptBlob(INDEX_DUKPT_PIN, cipherDukptKey, cipherDukptKey.length);
            outputText("loadDukptBlob:" + ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void ACS_FormatKeyBlockTest() throws Exception {


//        byte[] pubCrt=getPEDCrt();
//        if(pubCrt==null)
//        {
//            Log.d("applog", "getPEDCrt() error");
//            TextDisplay.append("getPEDCrt() error");
//            return;
//        }

        //IPEK:AA4C6FD0F731A520A8CDD067AB0D2514  IPEKLen:hex 10
        //KSN:FFFFD001F1445F000000  KSNLen:hex 0A

        //Format: IPEKLen + IPEK + KSNLen + KSN
        //10 AA4C6FD0F731A520A8CDD067AB0D2514 0A FFFFD001F1445F000000
//        Log.d("applog","pubCrt:"+Funs.bytes2HexString(pubCrt,pubCrt.length));
//        Log.d("applog","pubCrt-String:"+new String(pubCrt));

        byte[] ClearFormatKBL = Funs.StrToHexByte("10AA4C6FD0F731A520A8CDD067AB0D25140AFFFFD001F1445F000000");
        Log.d(TAG, "ClearFormatKBL:" + Funs.bytes2HexString(ClearFormatKBL, ClearFormatKBL.length));
        outputText("ClearFormatKBL:" + Funs.bytes2HexString(ClearFormatKBL, ClearFormatKBL.length));


        int[] exponent = new int[1];
        int[] publickeyModelLen = new int[1];
        byte[] publickeyModel = new byte[512];
        boolean isOk = pinpad.getRSAPublicKeyModel(publickeyModel, publickeyModelLen, exponent);
        if (isOk) {
            Log.d(TAG, "publickeyModelLen:" + publickeyModelLen[0]);
            Log.d(TAG, "exponent:" + exponent[0]);
            Log.d(TAG, "publickeyModel len:" + publickeyModel.length);
            Log.d(TAG, "publickeyModel:" + Funs.bytes2HexString(publickeyModel, publickeyModelLen[0]));

            try {
                byte[] model = new byte[publickeyModelLen[0]];
                System.arraycopy(publickeyModel, 0, model, 0, publickeyModelLen[0]);
                String exp = String.valueOf(exponent[0]);
                Log.d(TAG, "exp:" + exp);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                BigInteger modulus = new BigInteger(model);
                BigInteger publicExponent = new BigInteger(exp);
                RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
                PublicKey pub = keyFactory.generatePublic(rsaPublicKeySpec);

                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.ENCRYPT_MODE, pub);
                byte[] encData = cipher.doFinal(ClearFormatKBL);
                Log.d(TAG, "encData:" + Funs.bytes2HexString(encData, encData.length));

                int ret = pinpad.downloadKeyDukptByPrvDec(2, encData);
                outputText("downloadKeyDukptByPrvDec=" + ret);

                byte ipekLen = ClearFormatKBL[0];
                byte[] ipek = new byte[ipekLen];
                System.arraycopy(ClearFormatKBL, 1, ipek, 0, ipekLen);
                outputText("ipek len:" + ipek.length);
                outputText("ipek:" + Funs.bytes2HexString(ipek, ipek.length));
                byte iksnLen = ClearFormatKBL[1 + ipekLen];
                byte[] iksn = new byte[iksnLen];
                System.arraycopy(ClearFormatKBL, 2 + ipekLen, iksn, 0, iksnLen);
                outputText("iksn len:" + iksn.length);
                outputText("iksn:" + Funs.bytes2HexString(iksn, iksn.length));
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void DukptAesInitial() {
        int ret = pinpad.DukptAesInitial(INDEX_DUKPT_AES128, bdk_aes_16, bdk_aes_16.length, null, 0, Constant.DukptKeyType.AES128, ksnBuff_aes, ksnBuff_aes.length);
        outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "DukptAesInitial INDEX_DUKPT_AES128 ret:" + ret);
        if (ret == 0) {
            outputText("BDK:\n" + BytesUtil.bytes2HexString(bdk_aes_16) + "\n" + "KSN:\n" + BytesUtil.bytes2HexString(ksnBuff_aes));
        }
        ret = pinpad.DukptAesInitial(INDEX_DUKPT_AES192, bdk_aes_24, bdk_aes_24.length, null, 0, Constant.DukptKeyType.AES192, ksnBuff_aes, ksnBuff_aes.length);
        outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "DukptAesInitial INDEX_DUKPT_AES192 ret:" + ret);
        if (ret == 0) {
            outputText("BDK:\n" + BytesUtil.bytes2HexString(bdk_aes_24) + "\n" + "KSN:\n" + BytesUtil.bytes2HexString(ksnBuff_aes));
        }
        ret = pinpad.DukptAesInitial(INDEX_DUKPT_AES256, bdk_aes_32, bdk_aes_32.length, null, 0, Constant.DukptKeyType.AES256, ksnBuff_aes, ksnBuff_aes.length);
        outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "DukptAesInitial INDEX_DUKPT_AES256 ret:" + ret);
        if (ret == 0) {
            outputText("BDK:\n" + BytesUtil.bytes2HexString(bdk_aes_32) + "\n" + "KSN:\n" + BytesUtil.bytes2HexString(ksnBuff_aes));
        }
    }

    public void DukptAesUpdateKsn() {
        try {
            byte[] ksn1 = new byte[12];
            int ret = pinpad.DukptAesUpdateKsn(INDEX_DUKPT_AES128, ksn1);
            Log.d(TAG, "DukptAesUpdateKsn INDEX_DUKPT_AES128 ret: " + ret);
            outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "DukptAesUpdateKsn INDEX_DUKPT_AES128 ret:" + ret);
            if (ret == 0) {
                outputText("KSN:\n" + BytesUtil.bytes2HexString(ksn1));
            }
            ret = pinpad.DukptAesUpdateKsn(INDEX_DUKPT_AES192, ksn1);
            Log.d(TAG, "DukptAesUpdateKsn INDEX_DUKPT_AES192 ret: " + ret);
            outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "DukptAesUpdateKsn INDEX_DUKPT_AES192 ret:" + ret);
            if (ret == 0) {
                outputText("KSN:\n" + BytesUtil.bytes2HexString(ksn1));
            }
            ret = pinpad.DukptAesUpdateKsn(INDEX_DUKPT_AES256, ksn1);
            Log.d(TAG, "DukptAesUpdateKsn INDEX_DUKPT_AES256 ret: " + ret);
            outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "DukptAesUpdateKsn INDEX_DUKPT_AES256 ret:" + ret);
            if (ret == 0) {
                outputText("KSN:\n" + BytesUtil.bytes2HexString(ksn1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DukptAesGetKsn() {
        try {
            byte[] ksn2 = new byte[12];
            int ret = pinpad.DukptAesGetKsn(INDEX_DUKPT_AES128, ksn2);
            Log.d(TAG, "DukptAesGetKsn INDEX_DUKPT_AES128 ret: " + ret);
            outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "DukptAesGetKsn INDEX_DUKPT_AES128 ret:" + ret);
            if (ret == 0) {
                outputText("KSN:\n" + BytesUtil.bytes2HexString(ksn2));
            }
            ret = pinpad.DukptAesGetKsn(INDEX_DUKPT_AES192, ksn2);
            Log.d(TAG, "DukptAesGetKsn INDEX_DUKPT_AES192 ret: " + ret);
            outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "DukptAesGetKsn INDEX_DUKPT_AES192 ret:" + ret);
            if (ret == 0) {
                outputText("KSN:\n" + BytesUtil.bytes2HexString(ksn2));
            }
            ret = pinpad.DukptAesGetKsn(INDEX_DUKPT_AES256, ksn2);
            Log.d(TAG, "DukptAesGetKsn INDEX_DUKPT_AES256 ret: " + ret);
            outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "DukptAesGetKsn INDEX_DUKPT_AES256 ret:" + ret);
            if (ret == 0) {
                outputText("KSN:\n" + BytesUtil.bytes2HexString(ksn2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DukptAesEncryptDataIV() {
        byte[] data = BytesUtil.hexString2Bytes("9F18040000000286228C2400021D8711010102030405060708090A0B0C0D0E0F108E08010203040506070886160CDC018411810501020304058E081112131415161718860F8C1600000A8E082122232425262728000000000000000000000000");
        byte[] iv = BytesUtil.hexString2Bytes("00000000000000000000000000000000");
        byte[] response = new byte[256];
        byte[] bsKsn = new byte[12];
        int[] resLen = new int[2];
        int[] bsKsnLen = new int[2];

        int encMode = 0x01;//cbc
        int ret = pinpad.DukptAesEncryptDataIV(0x03, INDEX_DUKPT_AES128, encMode, Constant.DukptKeyType.AES128, iv, iv.length, data, data.length, response, resLen, bsKsn, bsKsnLen);
        outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "ret:" + ret);
        if (ret == 0) {
            outputText("DukptAesEncryptDataIV INDEX_DUKPT_AES128 EncryptData:AES-128:\n" + Funs.bytes2HexString(response, resLen[0]) + "\n KSN:" + BytesUtil.bytes2HexString(bsKsn));
        }

        ret = pinpad.DukptAesEncryptDataIV(0x03, INDEX_DUKPT_AES192, encMode, Constant.DukptKeyType.AES128, iv, iv.length, data, data.length, response, resLen, bsKsn, bsKsnLen);
        outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "ret:" + ret);
        if (ret == 0) {
            outputText("DukptAesEncryptDataIV INDEX_DUKPT_AES192 EncryptData:AES-128:\n" + Funs.bytes2HexString(response, resLen[0]) + "\n KSN:" + BytesUtil.bytes2HexString(bsKsn));
        }

        ret = pinpad.DukptAesEncryptDataIV(0x03, INDEX_DUKPT_AES192, encMode, Constant.DukptKeyType.AES192, iv, iv.length, data, data.length, response, resLen, bsKsn, bsKsnLen);
        outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "ret:" + ret);
        if (ret == 0) {
            outputText("DukptAesEncryptDataIV INDEX_DUKPT_AES192 EncryptData:AES-192:\n" + Funs.bytes2HexString(response, resLen[0]) + "\n KSN:" + BytesUtil.bytes2HexString(bsKsn));
        }

        ret = pinpad.DukptAesEncryptDataIV(0x03, INDEX_DUKPT_AES256, encMode, Constant.DukptKeyType.AES128, iv, iv.length, data, data.length, response, resLen, bsKsn, bsKsnLen);
        outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "ret:" + ret);
        if (ret == 0) {
            outputText("DukptAesEncryptDataIV INDEX_DUKPT_AES256 EncryptData:AES-128:\n" + Funs.bytes2HexString(response, resLen[0]) + "\n KSN:" + BytesUtil.bytes2HexString(bsKsn));
        }

        ret = pinpad.DukptAesEncryptDataIV(0x03, INDEX_DUKPT_AES256, encMode, Constant.DukptKeyType.AES192, iv, iv.length, data, data.length, response, resLen, bsKsn, bsKsnLen);
        outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "ret:" + ret);
        if (ret == 0) {
            outputText("DukptAesEncryptDataIV INDEX_DUKPT_AES256 EncryptData:AES-192:\n" + Funs.bytes2HexString(response, resLen[0]) + "\n KSN:" + BytesUtil.bytes2HexString(bsKsn));
        }

        ret = pinpad.DukptAesEncryptDataIV(0x03, INDEX_DUKPT_AES256, encMode, Constant.DukptKeyType.AES256, iv, iv.length, data, data.length, response, resLen, bsKsn, bsKsnLen);
        outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "ret:" + ret);
        if (ret == 0) {
            outputText("DukptAesEncryptDataIV INDEX_DUKPT_AES256 EncryptData:AES-256:\n" + Funs.bytes2HexString(response, resLen[0]) + "\n KSN:" + BytesUtil.bytes2HexString(bsKsn));
        }
        ///////////////////////// MAC /////////////////////////

        encMode = 0x01; //ANSI 9.19
        byte[] mac = new byte[8];
        int[] macLen = new int[2];
        ret = pinpad.DukptAesEncryptDataIV(0x102, INDEX_DUKPT_AES128, encMode, Constant.DukptKeyType.AES128, iv, iv.length, data, data.length, mac, macLen, bsKsn, bsKsnLen);
        outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "ret:" + ret);
        if (ret == 0) {
            outputText("DukptAesEncryptDataIV INDEX_DUKPT_AES128 CalMAC:AES-128:\n" + BytesUtil.bytes2HexString(mac) + "\n KSN:" + BytesUtil.bytes2HexString(bsKsn));
        }
        ret = pinpad.DukptAesEncryptDataIV(0x102, INDEX_DUKPT_AES192, encMode, Constant.DukptKeyType.AES128, iv, iv.length, data, data.length, mac, macLen, bsKsn, bsKsnLen);
        outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "ret:" + ret);
        if (ret == 0) {
            outputText("DukptAesEncryptDataIV INDEX_DUKPT_AES192 CalMAC:AES-128:\n" + BytesUtil.bytes2HexString(mac) + "\n KSN:" + BytesUtil.bytes2HexString(bsKsn));
        }
        ret = pinpad.DukptAesEncryptDataIV(0x102, INDEX_DUKPT_AES192, encMode, Constant.DukptKeyType.AES192, iv, iv.length, data, data.length, mac, macLen, bsKsn, bsKsnLen);
        outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "ret:" + ret);
        if (ret == 0) {
            outputText("DukptAesEncryptDataIV INDEX_DUKPT_AES192 CalMAC:AES-192:\n" + BytesUtil.bytes2HexString(mac) + "\n KSN:" + BytesUtil.bytes2HexString(bsKsn));
        }

        ret = pinpad.DukptAesEncryptDataIV(0x102, INDEX_DUKPT_AES256, encMode, Constant.DukptKeyType.AES128, iv, iv.length, data, data.length, mac, macLen, bsKsn, bsKsnLen);
        outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "ret:" + ret);
        if (ret == 0) {
            outputText("DukptAesEncryptDataIV INDEX_DUKPT_AES256 CalMAC:AES-128:\n" + BytesUtil.bytes2HexString(mac) + "\n KSN:" + BytesUtil.bytes2HexString(bsKsn));
        }
        ret = pinpad.DukptAesEncryptDataIV(0x102, INDEX_DUKPT_AES256, encMode, Constant.DukptKeyType.AES192, iv, iv.length, data, data.length, mac, macLen, bsKsn, bsKsnLen);
        outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "ret:" + ret);
        if (ret == 0) {
            outputText("DukptAesEncryptDataIV INDEX_DUKPT_AES256 CalMAC:AES-192:\n" + BytesUtil.bytes2HexString(mac) + "\n KSN:" + BytesUtil.bytes2HexString(bsKsn));
        }
        ret = pinpad.DukptAesEncryptDataIV(0x102, INDEX_DUKPT_AES256, encMode, Constant.DukptKeyType.AES256, iv, iv.length, data, data.length, mac, macLen, bsKsn, bsKsnLen);
        outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "ret:" + ret);
        if (ret == 0) {
            outputText("DukptAesEncryptDataIV INDEX_DUKPT_AES256 CalMAC:AES-256:\n" + BytesUtil.bytes2HexString(mac) + "\n KSN:" + BytesUtil.bytes2HexString(bsKsn));
        }

        encMode = 0x03;
        ret = pinpad.DukptAesEncryptDataIV(0x202, INDEX_DUKPT_AES256, encMode, Constant.DukptKeyType.AES256, iv, iv.length, data, data.length, mac, macLen, bsKsn, bsKsnLen);
        outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "ret:" + ret);
        if (ret == 0) {
            outputText("DukptAesEncryptDataIV INDEX_DUKPT_AES256 CMAC:AES-256:\n" + BytesUtil.bytes2HexString(mac) + "\n KSN:" + BytesUtil.bytes2HexString(bsKsn));
        }
    }

    public void DukptAesPinBlock() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                aesPinPadEnd = false;
                outputColorText(TextColor.BLUE, "GetDukptAesPinBlock INDEX_DUKPT_AES128: Constant.DukptKeyType.AES128 ");
                GetDukptAesPinBlock(INDEX_DUKPT_AES128, Constant.DukptKeyType.AES128);
                while (aesPinPadEnd == false) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                outputColorText(TextColor.BLUE, "GetDukptAesPinBlock INDEX_DUKPT_AES192: Constant.DukptKeyType.AES128 ");
                aesPinPadEnd = false;
                GetDukptAesPinBlock(INDEX_DUKPT_AES192, Constant.DukptKeyType.AES128);
                while (aesPinPadEnd == false) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                outputColorText(TextColor.BLUE, "GetDukptAesPinBlock INDEX_DUKPT_AES192: Constant.DukptKeyType.AES192 ");
                aesPinPadEnd = false;
                GetDukptAesPinBlock(INDEX_DUKPT_AES192, Constant.DukptKeyType.AES192);
                while (aesPinPadEnd == false) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                outputColorText(TextColor.BLUE, "GetDukptAesPinBlock INDEX_DUKPT_AES256: Constant.DukptKeyType.AES128 ");
                aesPinPadEnd = false;
                GetDukptAesPinBlock(INDEX_DUKPT_AES256, Constant.DukptKeyType.AES128);
                while (aesPinPadEnd == false) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                outputColorText(TextColor.BLUE, "GetDukptAesPinBlock INDEX_DUKPT_AES256: Constant.DukptKeyType.AES192 ");
                aesPinPadEnd = false;
                GetDukptAesPinBlock(INDEX_DUKPT_AES256, Constant.DukptKeyType.AES192);
                while (aesPinPadEnd == false) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                aesPinPadEnd = false;
                outputColorText(TextColor.BLUE, "GetDukptAesPinBlock INDEX_DUKPT_AES256: Constant.DukptKeyType.AES256 ");
                GetDukptAesPinBlock(INDEX_DUKPT_AES256, Constant.DukptKeyType.AES256);
            }
        }).start();
    }

    public void downloadKeyTR31() {
        if (keyAlg == Constant.KeyAlgorithm.AES) {
            outputColorText(TextColor.BLUE, "downloadKeyTR31 AES");
            byte[] kbpk = BytesUtil.hexString2Bytes("AB2E09DB3EF0BA71E0CE6CD755C23A3B");
            byte[] kcv = BytesUtil.hexString2Bytes("AB44B2");
            //load plain kbpk as master key.
            boolean succ = pinpad.loadMainKey(INDEX_MK, kbpk, kcv);
            outputColorText(succ ? TextColor.BLUE : TextColor.RED, "loadMainKey:" + succ);
            //plain text: 18A2CE9D62E8925DA7645B54ECA14BF2
            String KCV = "727A15";
            String keyBlock = "B0112B1AX00S0200KS181ED9D5000000BC200000PB08UhKlB34EA8DD29667DDC399295254398F4A5C190AE49898BBC4588DE89E5B2FD5A7E";
            Bundle bundle = new Bundle();
            bundle.putInt("keyNo", INDEX_MK); //KBPK index.
            bundle.putInt("sKeyNo", INDEX_WK); //key index.
            bundle.putByteArray("content", keyBlock.getBytes()); //tr31 keyblock.
            bundle.putInt("content_size", keyBlock.length()); // length of tr31 keyblock.

            //kbpk type, key type
            succ = pinpad.downloadKeyTR31(Constant.KeyType.MAIN_KEY, Constant.KeyType.PIN_KEY, bundle);
            outputColorText(succ ? TextColor.BLUE : TextColor.RED, "downloadKeyTR31:" + succ);

            if (succ) {
                byte[] kcvBuff = new byte[16];
                int ret = pinpad.calculateDes(Constant.DesMode.ENC, Constant.Algorithm.AES_ECB, Constant.KeyType.MAIN_KEY, INDEX_WK, new byte[8], kcvBuff);
                outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "calculateDes ret:" + ret);
                if (ret == 0) {
                    outputText("KCV:" + BytesUtil.bytes2HexString(kcvBuff) + "\nexpected KCV:\n" + KCV);
                }
            }
        } else {
            outputColorText(TextColor.BLUE, "downloadKeyTR31 TDES");
            byte[] kbpk = BytesUtil.hexString2Bytes("B28DD617072DDCFD61BD3741D7F30B02");
            byte[] kcv = BytesUtil.hexString2Bytes("3584A2");
            //load plain kbpk as master key.
            boolean succ = pinpad.loadMainKey(INDEX_MK, kbpk, kcv);
            outputColorText(succ ? TextColor.BLUE : TextColor.RED, "loadMainKey:" + succ);
            //plain text: 04B5155D867F1AFB45EAA2F4383EC215
            String KCV = "1F66CA";
            String keyBlock = "B0096P0TB0AE000001B5202B8A1015E560564CF9C9AE36504AB876E93E09F5BDFC7825D84CC99C4E7AA97767C87AC2CA";
            Bundle bundle = new Bundle();
            bundle.putInt("keyNo", INDEX_MK); //KBPK index.
            bundle.putInt("sKeyNo", INDEX_WK); //key index.
            bundle.putByteArray("content", keyBlock.getBytes()); //tr31 keyblock.
            bundle.putInt("content_size", keyBlock.length()); // length of tr31 keyblock.

            //kbpk type, key type
            succ = pinpad.downloadKeyTR31(Constant.KeyType.MAIN_KEY, Constant.KeyType.PIN_KEY, bundle);
            outputColorText(succ ? TextColor.BLUE : TextColor.RED, "downloadKeyTR31:" + succ);

            if (succ) {
                byte[] kcvBuff = new byte[8];
                int ret = pinpad.calculateDes(Constant.DesMode.ENC, Constant.Algorithm.DES_ECB, Constant.KeyType.PIN_KEY, INDEX_WK, new byte[8], kcvBuff);
                outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "calculateDes ret:" + ret);
                if (ret == 0) {
                    outputText("KCV:" + BytesUtil.bytes2HexString(kcvBuff) + "\nexpected KCV:\n" + KCV);
                }
            }
        }
    }

    public void downloadDukptTR31(byte[] kbpk, String keyBlock) {
        String versionId = keyBlock.substring(0, 1);
        if (TextUtils.equals("D", versionId)) {
            pinpad.setKeyAlgorithm(Constant.KeyAlgorithm.AES);
        } else {
            pinpad.setKeyAlgorithm(Constant.KeyAlgorithm.DES);
        }
        String keyAlgo = keyBlock.substring(7, 8);
        if (TextUtils.equals("A", keyAlgo)) {
            DukptAesInitialTr31(kbpk, keyBlock);
        } else {
            DukptInitialTr31(kbpk, keyBlock);
        }
    }

    public void DukptInitialTr31(byte[] kbpk, String keyBlock) {
        boolean succ = pinpad.loadMainKey(INDEX_MK, kbpk, null);
        outputColorText(succ ? TextColor.BLUE : TextColor.RED, "loadMainKey:" + succ);
        String keyUsage = keyBlock.substring(5, 7);
        int isBdk = (TextUtils.equals("B1", keyUsage) ? 0 : 1);
        byte[] ksnBuff = ksnBuff_msr;
        if (TextUtils.equals("B0", keyUsage) || TextUtils.equals("B1", keyUsage)) {
            String KS = keyBlock.substring(16, 18);
            if (TextUtils.equals("KS", KS)) {
                int KSLen = Integer.parseInt(keyBlock.substring(18, 20), 16);
                String KSI = keyBlock.substring(20, 20 + KSLen - 4);
                outputText("KSI:" + KSI);
                if (KSI.length() > 20) {
                    KSI = KSI.substring(0, 20);
                }
                String ksn = StringUtil.addZero(KSI, 20, false);
                ksnBuff = BytesUtil.hexString2Bytes(ksn);
            }
        }
        byte[] outKcv = new byte[16];
        int[] outKcvLen = new int[2];
        int kcvAlg = 1; //0-ECB, 1-CMAC.
        int ret = pinpad.DukptInitialTr31(Constant.KeyType.MAIN_KEY, INDEX_MK, INDEX_DUKPT_MSR, isBdk, keyBlock.getBytes(), keyBlock.length(), ksnBuff, ksnBuff.length, kcvAlg, new byte[16], 16, outKcv, outKcvLen);
        outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "DukptInitialTr31:" + ret);
        outputText("KSN:" + BytesUtil.bytes2HexString(ksnBuff));
        outputText("outKcv:" + BytesUtil.bytes2HexString(outKcv, outKcvLen[0]));
        if (ret != 0) {
            return;
        }

        byte[] rawData = BytesUtil.hexString2Bytes("9F260814E50F30268921459F2701409F1007060201039400029F3704A7D8F2329F36020601950500800000009A031901029B02E8009C0100");
        byte[] outdata = new byte[rawData.length];
        int[] outlen = new int[2];
        byte[] outKsn = new byte[10];
        int[] outKsnLen = new int[2];
        byte[] ivData = new byte[8];
        int ivLen = 8;
        try {
            outputText("rawData:" + "9F260814E50F30268921459F2701409F1007060201039400029F3704A7D8F2329F36020601950500800000009A031901029B02E8009C0100");
            int encMode = 0x01; //CBC
            ret = pinpad.DukptEncryptDataIV(0x03, INDEX_DUKPT_MSR, encMode, ivData, ivLen, rawData, rawData.length, outdata, outlen, outKsn, outKsnLen);
            outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "DukptEncryptDataIV ENC ECB ret:" + ret);
            outputText("KSN:" + BytesUtil.bytes2HexString(outKsn));
            if (ret == 0) {
                outputText("Enc result:" + BytesUtil.bytes2HexString(outdata, outlen[0]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DukptAesInitialTr31(byte[] kbpk, String keyBlock) {
        boolean succ = pinpad.loadMainKey(INDEX_MK, kbpk, null);
        outputColorText(succ ? TextColor.BLUE : TextColor.RED, "loadMainKey:" + succ);
        String keyUsage = keyBlock.substring(5, 7);
        int isBdk = (TextUtils.equals("B1", keyUsage) ? 0 : 1);
        byte[] ksnBuff = ksnBuff_aes;
        if (TextUtils.equals("B0", keyUsage) || TextUtils.equals("B1", keyUsage)) {
            String KS = keyBlock.substring(16, 18);
            if (TextUtils.equals("KS", KS)) {
                int KSLen = Integer.parseInt(keyBlock.substring(18, 20), 16);
                String KSI = keyBlock.substring(20, 20 + KSLen - 4);
                outputText("KSI:" + KSI);
                if (KSI.length() > 24) {
                    KSI = KSI.substring(0, 24);
                }
                String ksn = StringUtil.addZero(KSI, 24, false);
                ksnBuff = BytesUtil.hexString2Bytes(ksn);
            }
        }
        byte[] outKcv = new byte[16];
        int[] outKcvLen = new int[2];
        int keyLen = 16;
        int driveKeyType = Constant.DukptKeyType.AES128;
        int keySetNum = INDEX_DUKPT_AES128;
        if (keyLen == 16) {
            driveKeyType = Constant.DukptKeyType.AES128;
            keySetNum = INDEX_DUKPT_AES128;
        } else if (keyLen == 24) {
            driveKeyType = Constant.DukptKeyType.AES192;
            keySetNum = INDEX_DUKPT_AES192;
        } else if (keyLen == 32) {
            driveKeyType = Constant.DukptKeyType.AES256;
            keySetNum = INDEX_DUKPT_AES256;
        }
        int kcvAlg = 1; //0-ECB, 1-CMAC.
        int ret = pinpad.DukptAesInitialTr31(Constant.KeyType.MAIN_KEY, INDEX_MK, keySetNum, isBdk, driveKeyType, keyBlock.getBytes(), keyBlock.length(), ksnBuff, ksnBuff.length, kcvAlg, new byte[16], 16, outKcv, outKcvLen);
        outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "DukptAesInitialTr31:" + ret);
        outputText("KSN:" + BytesUtil.bytes2HexString(ksnBuff));
        outputText("outKcv:" + BytesUtil.bytes2HexString(outKcv, outKcvLen[0]));
        if (ret != 0) {
            return;
        }

        byte[] data = BytesUtil.hexString2Bytes("9F18040000000286228C2400021D8711010102030405060708090A0B0C0D0E0F108E08010203040506070886160CDC018411810501020304058E081112131415161718860F8C1600000A8E082122232425262728000000000000000000000000");
        byte[] iv = BytesUtil.hexString2Bytes("00000000000000000000000000000000");
        byte[] response = new byte[256];
        int[] resLen = new int[2];
        byte[] outKsn = new byte[12];
        int[] outKsnLen = new int[2];
        int encMode = 0x01;
        ret = pinpad.DukptAesEncryptDataIV(0x03, keySetNum, encMode, driveKeyType, iv, iv.length, data, data.length, response, resLen, outKsn, outKsnLen);
        outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "DukptAesEncryptDataIV ret:" + ret);
        outputText("KSN:" + BytesUtil.bytes2HexString(outKsn));
        if (ret == 0) {
            outputText("Enc result:" + BytesUtil.bytes2HexString(response, resLen[0]));
        }
    }

    public void rsaTest() {
        try {
            //0-9
            int rsaIndex = 1;
            int RSA_KeySize = 2048;
            String exponent = "010001";
            String HashAlg_SHA_256 = "SHA-256";
            String SignAlg_RSA = "SHA256withRSA";
            String transformation = "RSA/ECB/PKCS1Padding";
            byte[] sourceData = BytesUtil.hexString2Bytes("11111111111111122222222222222222222222");

            //CASE 1
            int iRet = pinpad.generateRSAKey(rsaIndex, RSA_KeySize, exponent);
            outputColorText(iRet == 0 ? TextColor.BLUE : TextColor.RED, "CASE 1(generateRSAKey):" + (iRet == 0) + "(" + iRet + ")");
            if (iRet != 0) {
                return;
            }

            //CASE 2
            PublicKey publicKey = pinpad.readRSAPublicKey(rsaIndex);
            outputColorText((publicKey != null) ? TextColor.BLUE : TextColor.RED, "CASE 2(rsaReadPublicKey):" + (publicKey != null));
            if (publicKey == null) {
                return;
            }

            //Sign data with private key
            //Calculate hash
            MessageDigest digest = MessageDigest.getInstance(HashAlg_SHA_256);
            byte[] hashedData = digest.digest(sourceData);
            Log.e(TAG, "hashedData:" + BytesUtil.bytes2HexString(hashedData));
            byte[] paddingHashedData = RSAUtil.addPKCS1Padding(RSAUtil.PADDING_TYPE.SIGNATURE, hashedData, RSA_KeySize / 8);
            Log.e(TAG, "paddingHashedData:" + BytesUtil.bytes2HexString(paddingHashedData));
            //CASE 3
            //Verify the signature
            byte[] bySign = pinpad.calculateWithRSAPrivateKey(rsaIndex, paddingHashedData);
            boolean valid = RSAUtil.verifySignatureWithPublicKey(publicKey, SignAlg_RSA, bySign, sourceData);
            outputColorText(valid ? TextColor.BLUE : TextColor.RED, "CASE 3(rsaPrvKeyOps):" + valid);

            //CASE 4
            //Encryption with public key
            byte[] encryptionResult = RSAUtil.encryptWithPublicKey(publicKey, transformation, sourceData);
            //Decryption with private key
            byte[] decryptionResult = pinpad.calculateWithRSAPrivateKey(rsaIndex, encryptionResult);
            byte[] finalResult = RSAUtil.removePKCS1Padding(decryptionResult);
            Log.e(TAG, "removePKCS1Padding:" + BytesUtil.bytes2HexString(finalResult));
            valid = TextUtils.equals(BytesUtil.bytes2HexString(sourceData).toUpperCase(), BytesUtil.bytes2HexString(finalResult).toUpperCase());
            outputColorText(valid ? TextColor.BLUE : TextColor.RED, "CASE 4(rsaPrvKeyOps):" + valid);

            //CASE 5
            //Encryption with private key
            paddingHashedData = RSAUtil.addPKCS1Padding(RSAUtil.PADDING_TYPE.SIGNATURE, sourceData, RSA_KeySize / 8);
            Log.e(TAG, "paddingHashedData:" + BytesUtil.bytes2HexString(paddingHashedData));
            Log.e(TAG, "paddingHashedData:" + BytesUtil.bytes2HexString(paddingHashedData));
            encryptionResult = pinpad.calculateWithRSAPrivateKey(rsaIndex, paddingHashedData);
            //Decryption with public key
            decryptionResult = RSAUtil.decryptWithPublicKey(publicKey, transformation, encryptionResult);
            Log.e(TAG, "decryptionResult:" + BytesUtil.bytes2HexString(decryptionResult));
            valid = TextUtils.equals(BytesUtil.bytes2HexString(sourceData).toUpperCase(), BytesUtil.bytes2HexString(decryptionResult).toUpperCase());
            outputColorText(valid ? TextColor.BLUE : TextColor.RED, "CASE 5(rsaPrvKeyOps):" + valid);

            //CASE 6
            //Decryption with public key
            decryptionResult = pinpad.calculateWithRSAPublicKey(rsaIndex, encryptionResult);
            finalResult = RSAUtil.removePKCS1Padding(decryptionResult);
            Log.e(TAG, "finalResult:" + BytesUtil.bytes2HexString(finalResult));
            valid = TextUtils.equals(BytesUtil.bytes2HexString(sourceData).toUpperCase(), BytesUtil.bytes2HexString(finalResult).toUpperCase());
            outputColorText(valid ? TextColor.BLUE : TextColor.RED, "CASE 6(rsaPubKeyOps):" + valid);
        } catch (Throwable e) {
            e.printStackTrace();
            outputColorText(TextColor.RED, e.getMessage());
        }
    }

    public void tr34Test(int type, int index, String sourceData) {
        try {
            int ret = TR34Util.writeTR34Cert(type, index, sourceData.getBytes());
            outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "writeTR34Cert:" + (ret == 0) + "(" + ret + ")");
            if (ret != 0) {
                return;
            }
            byte[] rspData = new byte[2048];
            int[] rspLen = new int[2];
            ret = TR34Util.readTR34Cert(type, index, rspData, rspLen);
            outputText("readTR34Cert:" + ret);
            outputColorText(ret == 0 ? TextColor.BLUE : TextColor.RED, "writeTR34Cert:" + (ret == 0) + "(" + ret + ")");
            if (type == 0xF5) {
                outputText("result:true");
                return;
            }
            if (ret != 0) {
                return;
            }
            int len = rspLen[0];
            byte[] result = new byte[len];
            System.arraycopy(rspData, 0, result, 0, len);
            String responseResult = new String(result);
            if (TextUtils.equals(sourceData, responseResult)) {
                outputText("result:true");
            } else {
                outputColorText(TextColor.RED, "response result:false");
                outputText("response result:\n" + responseResult + "\n" + "expected result:\n" + sourceData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            outputColorText(TextColor.RED, "response result:false\n" + e.getMessage());
        }
    }

    /**
     * @param type
     * CA_TYPE_KMSCA,0xF2
     * CA_TYPE_PEDCRT,0xF3
     * CA_TYPE_KDHCRT,0xF4
     * CA_TYPE_PEDPRV, 0xF5
     * index:0-3
     * @param data
     * @return
     */
    private static final int CA_TYPE_KMSCA = 0xF2;
    private static final int CA_TYPE_PEDCRT = 0xF3;
    private static final int CA_TYPE_KDHCRRT = 0xF4;
    private static final int CA_TYPE_PEDPRV = 0xF5;

    /**
     * writeTR34Cert
     *
     * @param type      CA_TYPE_KMSCA,0xF2
     *                  CA_TYPE_PEDCRT,0xF3
     *                  CA_TYPE_KDHCRRT,0xF4
     *                  CA_TYPE_PEDPRV, 0xF5
     * @param index:0-3
     * @param data      resLen：长度
     * @return
     */
    private static int writeTR34Cert(int type, int index, byte[] data) throws Exception {
        Log.e(TAG, "writeTR34Cert: type=" + type + ", index=" + index);
        int ret = -1;
        try {
            if (type < 0xF2 || type > 0xF5) {
                throw new Exception("Invalid Type, should be0xF2-0xF5");
            }
            if (index < 0 || index > 3) {
                throw new Exception("Invalid Index, should be 0-3");
            }
            SEManager seManager = new SEManager();
            ret = seManager.deleteTR34Cert(type, index);
            Log.e(TAG, "deleteTR34Cert:" + ret);

            ret = seManager.writeTR34Cert(type, index, data, data.length);
            Log.e(TAG, "writeTR34Cert:" + ret);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
        return ret;
    }

    /**
     * readTR34Cert
     *
     * @param type         CA_TYPE_KMSCA,0xF2
     *                     CA_TYPE_PEDCRT,0xF3
     *                     CA_TYPE_KDHCRRT,0xF4
     *                     CA_TYPE_PEDPRV, 0xF5
     * @param index:0-3
     * @param responseData
     * @param resLen
     * @return
     */
    private static int readTR34Cert(int type, int index, byte[] responseData, int[] resLen) throws Exception {
        Log.e(TAG, "readTR34Cert: type=" + type + ", index=" + index);
        int ret = -1;
        try {
            if (type < 0xF2 || type > 0xF5) {
                throw new Exception("Invalid Type, should be0xF2-0xF5");
            }
            if (index < 0 || index > 3) {
                throw new Exception("Invalid Index, should be 0-3");
            }
            ret = new SEManager().readTR34Cert(type, index, responseData, resLen);
            Log.e(TAG, "readTR34Cert ret:" + ret);
            Log.e(TAG, "readTR34Cert resLen:" + resLen[0]);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
        return ret;
    }

}
