package com.urovo.sdk.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.urovo.sdk.R;
import com.urovo.sdk.install.InstallManagerImpl;
import com.urovo.sdk.install.listener.InstallApkListener;
import com.urovo.sdk.utils.FileUtil;

public class SilentInstallActivity extends BaseActivity implements View.OnClickListener {

    private EditText editText_apkpath;
    private EditText editText_pachkageName;
    private InstallManagerImpl mInstallManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install);
        initView();

        editText_apkpath = (EditText) findViewById(R.id.editText_path);
        editText_pachkageName = (EditText) findViewById(R.id.editText_packagename);

        mInstallManager = InstallManagerImpl.getInstance(SilentInstallActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {//判断是否选择和Code判断
            try {
                Uri uri = data.getData();//拿到路径
                String filePath = FileUtil.getPath(SilentInstallActivity.this, uri);
                Log.e(TAG, "file Path:" + filePath);
                editText_apkpath.setText("" + filePath);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Exception:" + e.getMessage());
                outputColorText(TextColor.RED, "Failed to get file:" + e.getMessage());
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_path:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_install:
                outputColorText(TextColor.BLUE, "Install APK");
                String path = editText_apkpath.getText().toString().trim();
                if (TextUtils.isEmpty(path)) {
                    showMessage("Input APK Path");
                    outputColorText(TextColor.BLUE, "Input APK Path");
                    return;
                }
                try {
                    mInstallManager.install(path, new InstallApkListener() {
                        @Override
                        public void onInstallFinished(String packageName, int returnCode, String returnMsg) {
                            Log.e("MainActivity", "onInstallFinished, packageName:" + packageName + ", returnCode:" + returnCode + ", returnMsg:" + returnMsg);
                            outputColorText(TextColor.BLUE, "onInstallFinished, packageName:" + packageName + ", returnCode:" + returnCode + ", returnMsg:" + returnMsg);
                        }

                        @Override
                        public void onUnInstallFinished(String packageName, int returnCode, String returnMsg) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_uninstall:
                outputColorText(TextColor.BLUE, "UnInstall APK");
                String name = editText_pachkageName.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    showMessage("Input APK Package Name");
                    outputColorText(TextColor.BLUE, "Input APK Package Name");
                    return;
                }
                try {
                    InstallManagerImpl.getInstance(SilentInstallActivity.this).uninstall(name, new InstallApkListener() {
                        @Override
                        public void onInstallFinished(String packageName, int returnCode, String returnMsg) {

                        }

                        @Override
                        public void onUnInstallFinished(String packageName, int returnCode, String returnMsg) {
                            Log.e("MainActivity", "onUnInstallFinished, packageName:" + packageName + ", returnCode:" + returnCode + ", returnMsg:" + returnMsg);
                            outputColorText(TextColor.BLUE, "onUnInstallFinished, packageName:" + packageName + ", returnCode:" + returnCode + ", returnMsg:" + returnMsg);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}