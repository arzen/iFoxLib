package com.arzen.iFoxLib.utils;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SettingUtils {
    private final String TAG = "BookReaderSetting";

    public static final String KEY_VERSION = "VERSION";
    public static final String KEY_UPDATE = "UPDATE";
    public static final String KEY_LOCALFILES = "LOCALFILES";

    public static final int OBTAIN_DATA_FAIL = -10000;

    private SharedPreferences mSharedData;
    private Editor mSharedDataEditor;

    public SettingUtils(Context context, String sharedName, int mode) {
        this.mSharedData = context.getSharedPreferences(sharedName, mode);

    }

    /**
     * ��ע������commit()
     * 
     * @param key
     * @param value
     */
    public void putBolean(String key, boolean value) {
        if (obtainWritePermission()) {
            mSharedDataEditor.putBoolean(key, value);
            commitOperate();
        }
    }

    /**
     * ��ע������commit()
     * 
     * @param key
     * @param value
     */
    public void putInteger(String key, int value) {
        if (obtainWritePermission()) {
            mSharedDataEditor.putInt(key, value);
            commitOperate();
        }
    }

    public void putString(String key, String value) {
        if (obtainWritePermission()) {
            mSharedDataEditor.putString(key, value);
            commitOperate();
        }
    }

    /**
     * �ύ���в���
     */
    public void commitOperate() {
        // if(obtainWritePermission()){
        mSharedDataEditor.commit();
        // }
    }

    /**
     * ��ȡ���?
     * 
     * @param key
     * @param defValue
     * @return
     */
    public int getInteger(String key, int defValue) {
        if (obtainReadPermission()) {
            return mSharedData.getInt(key, defValue);
        }
        return OBTAIN_DATA_FAIL;
    }

    /**
     * ��ȡ���?
     * 
     * @param key
     * @param defValue
     * @return
     */
    public boolean getBoolean(String key, boolean defValue) {
        if (obtainReadPermission()) {
            return mSharedData.getBoolean(key, defValue);
        }
        return false;
    }

    /**
     * ��ȡ���?
     * 
     * @param key
     * @param defValue
     * @return
     */
    public String getString(String key, String defValue) {
        if (obtainReadPermission()) {

            return mSharedData.getString(key, defValue);
        }
        return "0";
    }

    /**
     * �Ƿ�����д��
     * 
     * @return �����򷵻�true
     */
    public boolean obtainWritePermission() {
        if (mSharedDataEditor == null && mSharedData != null) {
            mSharedDataEditor = mSharedData.edit();
            return true;
        } else if (mSharedDataEditor != null) {
            return true;
        }
        Log.e(TAG, "can't obtain write shared perferences permission");
        return false;
    }

    /**
     * �Ƿ�������?
     * 
     * @return �����򷵻�true
     */
    public boolean obtainReadPermission() {
        if (mSharedData != null) {
            return true;
        }
        Log.e(TAG, "can't obtain read shared perferences permission");
        return false;
    }

    /**
     * ɾ���Ӧ�ļ��?
     * 
     * @author �����?
     * @Since:2010-12-9
     * @param key
     * @return
     */
    public boolean remove(String key) {
        mSharedDataEditor.remove(key);
        commitOperate();
        return true;
    }

    public Map<String, ?> getAll() {
        if (mSharedData != null) {
            return mSharedData.getAll();
        }
        return null;
    }

    public void removeAll() {
        if (obtainWritePermission()) {
            mSharedDataEditor.clear();
            commitOperate();
        }
    }

    public long getLong(String key, long defValue) {
        if (obtainReadPermission()) {
            return mSharedData.getLong(key, defValue);
        }
        return defValue;
    }

    public void putLong(String key, long value) {
        if (obtainWritePermission()) {
            mSharedDataEditor.putLong(key, value);
        }
    }
}
