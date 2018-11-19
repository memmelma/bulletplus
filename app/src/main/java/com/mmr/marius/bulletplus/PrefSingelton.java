package com.mmr.marius.bulletplus;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class PrefSingleton{
    private static PrefSingleton mInstance;
    private Context mContext;

    private SharedPreferences mMyPreferences;

    private PrefSingleton(){ }

    public static PrefSingleton getInstance(){
        if (mInstance == null) mInstance = new PrefSingleton();
        return mInstance;
    }

    public void Initialize(Context ctxt){
        mContext = ctxt;
        //
        mMyPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public void writePreference(String key, String value){
        SharedPreferences.Editor e = mMyPreferences.edit();
        e.putString(key, value);
        e.commit();
    }

    public String getPreference(String key){
        return mMyPreferences.getString(key, "");
    }

    public void clear(){
        SharedPreferences.Editor e = mMyPreferences.edit();
        e.clear();
        e.commit();
    }
}