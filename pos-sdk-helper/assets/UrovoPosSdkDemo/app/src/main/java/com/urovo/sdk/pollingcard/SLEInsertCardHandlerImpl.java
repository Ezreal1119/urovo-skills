package com.urovo.sdk.pollingcard;

import android.device.IccManager;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.urovo.file.logfile;
import com.urovo.sdk.utils.BytesUtil;

public class SLEInsertCardHandlerImpl {

    private static final String TAG = SLEInsertCardHandlerImpl.class.getSimpleName() + "===>";
    public IccManager mIccManager;

    private SLESearchListener mSleSearchListener;
    private byte mCardType;

    private WorkHandler mWorkHandler;
    private WorkHandlerThread mWorkHandlerThread;
    public static SLEInsertCardHandlerImpl insertCardHandler;

    public static final byte VOLT_3 = 0x01;

    public static SLEInsertCardHandlerImpl getInstance() {
        if (insertCardHandler == null) {
            insertCardHandler = new SLEInsertCardHandlerImpl();
        }
        return insertCardHandler;
    }

    private void enableCardReader(boolean enable) {
        if (mIccManager == null) mIccManager = new IccManager();
        if (enable) {
            int ret = mIccManager.open((byte) 0, (byte) 2, VOLT_3);
            logfile.printLog(TAG + "open:" + ret);
        } else {
            int ret = mIccManager.deactivate();
            logfile.printLog(TAG + "deactivate:" + ret);
            ret = mIccManager.close();
            logfile.printLog(TAG + "close:" + ret);
            mIccManager = null;
            insertCardHandler = null;
        }
    }

    public void searchCard(int timeout, byte cardType, SLESearchListener listener) {
        // TODO Auto-generated method stub
        logfile.printLog(TAG + "searchCard: timeout=" + timeout + ", cardType=" + cardType);
        if (listener == null) {
            logfile.printLog(TAG + "listener is null");
        }
        mSleSearchListener = listener;
        if (cardType != SLEInsertCardHandlerImpl.CardType.SLE4428
                && cardType != SLEInsertCardHandlerImpl.CardType.SLE4436
                && cardType != SLEInsertCardHandlerImpl.CardType.SLE4442) {
            enableCardReader(false);
            if (mSleSearchListener != null) {
                mSleSearchListener.onFail(-99, "Invalid Card Type, must be (0x01/0x02/0x03)");
            }
            return;
        }
        this.mCardType = cardType;
        if (mWorkHandlerThread == null) {
            logfile.printLog(TAG + "searchCard mWorkHandlerThread");
            mWorkHandlerThread = new WorkHandlerThread("SELCARD");
            mWorkHandlerThread.startThread();
        }
        enableCardReader(true);
        mWorkHandler.removeMessages(WorkHandler.MESSAGE_READ_TIMEOUT);
        Message m = Message.obtain(mWorkHandler, WorkHandler.MESSAGE_CARD_READ);
        mWorkHandler.sendMessage(m);

        if (timeout > 0) {
            mWorkHandler.sendEmptyMessageDelayed(WorkHandler.MESSAGE_READ_TIMEOUT, timeout * 1000);
        }
    }

    public void stopSearch() {
        // TODO Auto-generated method stub
        logfile.printLog(TAG + "stopSearch");
        mSleSearchListener = null;
        if (mWorkHandlerThread != null) {
            logfile.printLog(TAG + "stopSearch release");
            if (mWorkHandler != null) {
                Message m = Message.obtain(mWorkHandler, WorkHandler.MESSAGE_STOP_WORK);
                mWorkHandler.sendMessage(m);
            }
            mWorkHandlerThread.getLooper().quitSafely();
            mWorkHandler = null;
            mWorkHandlerThread.interrupt();
            mWorkHandlerThread = null;
            logfile.printLog(TAG + "stopSearch release end");
        }
        enableCardReader(false);
    }

    private class WorkHandler extends Handler {
        public static final int MESSAGE_CARD_READ = 1;
        public static final int MESSAGE_STOP_WORK = 2;
        public static final int MESSAGE_MSRCARD_DATA_OK = 3;
        public static final int MESSAGE_READ_TIMEOUT = 4;

        private final Object cardReadEvent = new Object();
        private boolean cardReadNotified = false;
        private CardNotifyThread CardNotify = new CardNotifyThread();

        public WorkHandler(Looper loop) {
            super(loop);
        }

        private class CardNotifyThread extends Thread {

            @Override
            public void run() {
                int ret;
                while (cardReadNotified) {
                    if (mIccManager == null) {
                        enableCardReader(true);
                    }
                    logfile.printLog(TAG + "detect start");
                    ret = mIccManager.detect();
                    logfile.printLog(TAG + "detect:" + ret);
                    if (ret == 0) {
                        byte[] atr = new byte[64];
                        if (mCardType == SLEInsertCardHandlerImpl.CardType.SLE4428) {
                            ret = mIccManager.sle4428_reset(atr);
                            logfile.printLog(TAG + "sle4428_reset:" + ret);
                        } else if (mCardType == SLEInsertCardHandlerImpl.CardType.SLE4436) {
                            ret = mIccManager.sle4436_reset(atr);
                            logfile.printLog(TAG + "sle4436_reset:" + ret);
                        } else if (mCardType == SLEInsertCardHandlerImpl.CardType.SLE4442) {
                            ret = mIccManager.sle4442_reset(atr);
                            logfile.printLog(TAG + "sle4442_reset:" + ret);
                        }
                        if (ret >= 0) {
                            logfile.printLog(TAG + "atr:" + BytesUtil.bytes2HexString(atr, ret));
                            byte[] atrRsp = new byte[ret];
                            System.arraycopy(atr, 0, atrRsp, 0, ret);
                            cardReadNotified = false;
                            if (mWorkHandler != null) {
                                Message msg = mWorkHandler.obtainMessage(MESSAGE_MSRCARD_DATA_OK);
                                msg.obj = atrRsp;
                                mWorkHandler.sendMessage(msg);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void handleMessage(Message msg) {
            logfile.printLog(TAG + "handleMessage Sub Thread ID------------>" + Thread.currentThread().getId() + " pid " + Binder.getCallingPid() + " Uid " + Binder.getCallingUid() + "--msg----------->" + msg.what);
            switch (msg.what) {
                case MESSAGE_CARD_READ:
                    cardFeedback();
                    break;
                case MESSAGE_READ_TIMEOUT:
                    cardReadNotified = false;
                    synchronized (cardReadEvent) {
                        if (CardNotify != null) {
                            CardNotify.interrupt();
                            CardNotify = null;
                        }
                    }
                    if (mSleSearchListener != null) {
                        try {
                            mSleSearchListener.onTimeout();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    break;
                case MESSAGE_STOP_WORK:
                    if (mWorkHandler != null)
                        mWorkHandler.removeMessages(MESSAGE_READ_TIMEOUT);
                    cardReadNotified = false;
                    synchronized (cardReadEvent) {
                        if (CardNotify != null) {
                            CardNotify.interrupt();
                            CardNotify = null;
                        }
                    }
                    break;
                case MESSAGE_MSRCARD_DATA_OK:
                    if (mWorkHandler != null)
                        mWorkHandler.removeMessages(MESSAGE_READ_TIMEOUT);
                    cardReadNotified = false;
                    synchronized (cardReadEvent) {
                        if (CardNotify != null) {
                            CardNotify.interrupt();
                            CardNotify = null;
                        }
                    }
                    byte[] atr = (byte[]) msg.obj;
                    if (mSleSearchListener != null) {
                        try {
                            mSleSearchListener.onCardInsert(atr);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }

        private void cardFeedback() {
            synchronized (cardReadEvent) {
                //cardReadEvent.notify();
                if (CardNotify == null) {
                    CardNotify = new CardNotifyThread();
                } else {
                    if (cardReadNotified || CardNotify.isAlive()) {
                        logfile.printLog(TAG + "CardNotify is working " + cardReadNotified);
                        return;
                    }
                }
                if (CardNotify != null) {
                    cardReadNotified = true;
                    CardNotify.start();
                }
            }
        }

    }

    private class WorkHandlerThread extends HandlerThread {
        private Looper myLooper;

        public WorkHandlerThread(String name) {
            super(name);
            start();
            myLooper = this.getLooper();
        }

        public void startThread() {
            mWorkHandler = new WorkHandler(myLooper);
        }
    }

    public interface SLESearchListener {

        void onCardInsert(byte[] atr);

        void onFail(int error, String message);

        void onTimeout();
    }

    public interface CardType {
        byte SLE4428 = 0x01;
        byte SLE4436 = 0x02;
        byte SLE4442 = 0x03;
    }

    public interface PasswordMode {
        int Verify = 1;
        int Change = 2;
    }

}
