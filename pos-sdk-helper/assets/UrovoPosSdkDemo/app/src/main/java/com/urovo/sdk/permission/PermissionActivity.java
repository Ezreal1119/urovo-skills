package com.urovo.sdk.permission;

import android.content.Intent;
import android.device.DeviceManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.urovo.sdk.MainActivity;
import com.urovo.sdk.PosApplication;

import java.util.List;
import java.util.Map;

/**
 * Permission-request entry activity.
 * Uses Activity Result APIs instead of the deprecated onActivityResult flow.
 */
public class PermissionActivity extends FragmentActivity {

    private final static String TAG = "PermissionActivity";

    private final ActivityResultLauncher<String[]> mPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    // Keep the result check for observability; storage-manager permission is validated separately below.
                    boolean allGranted = result != null && !result.values().contains(Boolean.FALSE);
                    Log.e(TAG, "allGranted:" + allGranted);
                    // On Android 11+, MANAGE_EXTERNAL_STORAGE is outside normal runtime permission groups.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
                            && !Environment.isExternalStorageManager()) {
                        mStorageLauncher.launch(new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                                .setData(Uri.parse("package:" + getPackageName())));
                        return;
                    }
                    launchMain();
                }
            });

    private final ActivityResultLauncher<Intent> mStorageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // Fail fast on denial to make permission issues immediately visible to users.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
                        launchMain();
                    } else {
                        Toast.makeText(PermissionActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (needRequestPermissions()) {
            grantRuntimePermission();
            if (needRequestPermissions()) {
                requestPermissions();
            }
        } else {
            launchMain();
        }
    }

    /**
     * Use system function to enable the permission first.
     */
    private void grantRuntimePermission() {
        // Urovo system API pre-grants permissions when available; runtime requests still act as a safety net.
        try {
            for (String permission : PermissionUtil.Permissions) {
                boolean ret = new DeviceManager().grantRuntimePermission(getPackageName(), permission);
                Log.e(TAG, "grantRuntimePermission:" + ret);
            }
        } catch (Throwable e) {
            //Some device models and OS do not support this function, so try-catch(Throwable) it.
            e.printStackTrace();
        }
    }

    private boolean needRequestPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;
        // Request flow is needed if any runtime permission is pending or storage-manager access is missing on R+.
        List<String> pending = PermissionUtil.getPendingPermissionList(this);
        return !pending.isEmpty()
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager());
    }

    private void requestPermissions() {
        List<String> pending = PermissionUtil.getPendingPermissionList(this);
        if (!pending.isEmpty()) {
            mPermissionLauncher.launch(pending.toArray(new String[0]));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            // Runtime permissions are granted, but all-files access still needs explicit settings-page authorization.
            mStorageLauncher.launch(new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    .setData(Uri.parse("package:" + getPackageName())));
        } else {
            launchMain();
        }
    }

    private void launchMain() {
        PosApplication.getInstance().init(getApplicationContext());
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
