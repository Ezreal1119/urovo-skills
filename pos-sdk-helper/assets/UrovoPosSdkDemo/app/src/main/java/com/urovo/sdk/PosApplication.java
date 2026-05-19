package com.urovo.sdk;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class PosApplication extends Application {

    private static final String TAG = "PosApplication===>";
    private static PosApplication mInstance;

    public static PosApplication getInstance() {
        if (mInstance == null) {
            mInstance = new PosApplication();
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public void init(Context context) {
        initFontTtf(context, Environment.getExternalStorageDirectory() + "/");
    }

    public void initFontTtf(Context context, String path) {
        Log.e(TAG, "initFontTtf");
        try {
            String fileName = "CALIBRI.ttf";
            String filePath = path + fileName;
            File file = new File(filePath);
            if (file.exists()) {
                Log.e(TAG, "Font TTF exist:" + filePath);
                return;
            }
            InputStream inputStream = context.getAssets().open(fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            //这里开始拷贝
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.close();//用完了，记得关闭
            inputStream.close();
            Log.e(TAG, "fontNamePath:" + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e(TAG, "initFontTtf finish");
    }

}
