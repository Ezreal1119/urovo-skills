package com.urovo.sdk.permission;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Permission helper that centralizes runtime-permission definitions and pending-permission calculation.
 * The main goal is to keep API-level branching in one place so callers do not duplicate checks inconsistently.
 */
public class PermissionUtil {

    /**
     * Full permission constant list (including higher API permissions) for backward compatibility.
     * For actual requests, prefer {@link #getPermissionsForCurrentApi()} to avoid invalid requests on lower APIs.
     */
    public static final String[] Permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_PHONE_NUMBERS,      // API 26+
            Manifest.permission.READ_SMS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.BLUETOOTH_CONNECT         // API 31+
    };

    /**
     * Returns the permission list that should be requested on the current API level.
     * Permissions are filtered by SDK version to prevent no-op/invalid requests on older systems.
     */
    public static String[] getPermissionsForCurrentApi() {
        List<String> list = new ArrayList<>();
        list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        list.add(Manifest.permission.CAMERA);
        list.add(Manifest.permission.READ_PHONE_STATE);
        // READ_PHONE_NUMBERS is available on API 26+ only.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            list.add(Manifest.permission.READ_PHONE_NUMBERS);
        }
        list.add(Manifest.permission.READ_SMS);
        list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        list.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        list.add(Manifest.permission.ACCESS_WIFI_STATE);
        // BLUETOOTH_CONNECT requires explicit runtime grant on API 31+.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            list.add(Manifest.permission.BLUETOOTH_CONNECT);
        }
        return list.toArray(new String[0]);
    }

    /**
     * Returns permissions that are not granted yet and still need to be requested.
     * Uses API-filtered defaults to reduce version-check omissions in callers.
     */
    public static List<String> getPendingPermissionList(Context context) {
        return getPendingPermissionList(context, getPermissionsForCurrentApi());
    }

    /**
     * Computes the pending-request list from the provided permission set.
     * The returned list contains only denied items so callers can pass it directly to request APIs.
     */
    public static List<String> getPendingPermissionList(Context context, String... permissions) {
        List<String> pending = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                pending.add(permission);
            }
        }
        return pending;
    }

    /**
     * @deprecated Use {@link #getPendingPermissionList(Context)} instead.
     */
    public static List<String> getRequestPermissionList(Context context, String... permissions) {
        return getPendingPermissionList(context, permissions);
    }

}
