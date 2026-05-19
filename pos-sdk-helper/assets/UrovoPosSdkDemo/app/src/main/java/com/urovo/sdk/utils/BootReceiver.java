package com.urovo.sdk.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.urovo.sdk.permission.PermissionActivity;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("BootReceiver", "onReceive:" + intent.getAction());
//        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())
//                || Intent.ACTION_MY_PACKAGE_REPLACED.equals(intent.getAction())) {
//            Intent intent1 = new Intent(context, PermissionActivity.class);
//            intent1.setAction("android.intent.action.MAIN");
//            intent1.addCategory("android.intent.category.LAUNCHER");
//            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            context.startActivity(intent1);
//        }
    }

}
