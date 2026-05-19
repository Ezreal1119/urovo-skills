package com.urovo.sdk.emv.emvbean;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Iterator;

public class EmvAidCapkBean {

    public Hashtable<String, String> toHashtable() {
        try {
            Hashtable<String, String> hashtable = new Hashtable<>();
            JSONObject jsonObject = new JSONObject(new Gson().toJson(this));
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = jsonObject.getString(key);
                if (!TextUtils.isEmpty(value)) {
                    hashtable.put(key, value);
                }
            }
            Log.e("EmvAidCapkBean", this.getClass().getSimpleName() + ":" + hashtable.toString());
            return hashtable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
