package com.arzen.iFoxLib.setting;

import android.content.Context;

import com.arzen.iFoxLib.utils.SettingUtils;

public class UserSetting {

	/**
	 * 保存用户数据
	 * 
	 * @param context
	 * @param uid
	 * @param token
	 */
	public static final void saveUserData(Context context, String uid, String token, String userName, String pwd) {
		SettingUtils settingUtils = getSettingUtils(context);
		settingUtils.putString(KeyConstants.SHARED_KEY_TOKEN, token);
		settingUtils.putString(KeyConstants.SHARED_KEY_UID, uid);
		settingUtils.putString(KeyConstants.SHARED_KEY_USERNAME, userName);
		settingUtils.putString(KeyConstants.SHARED_KEY_PWD, pwd);
		settingUtils.commitOperate();
	}
	
	/**
	 * 保存用户数据
	 * 
	 * @param context
	 * @param uid
	 * @param token
	 */
	public static final void saveUserData(Context context, String uid, String token) {
		SettingUtils settingUtils = getSettingUtils(context);
		settingUtils.putString(KeyConstants.SHARED_KEY_TOKEN, token);
		settingUtils.putString(KeyConstants.SHARED_KEY_UID, uid);
		settingUtils.commitOperate();
	}
	
	public static void savePayListTime(Context context,long time){
		SettingUtils settingUtils = getSettingUtils(context);
		settingUtils.putLong(KeyConstants.SHARED_KEY_TIME, time);
		settingUtils.commitOperate();
	}
	
	/**
	 * 上次初始化保存的时间,
	 * @param context
	 */
	public static long getPayListTime(Context context){
		SettingUtils settingUtils = getSettingUtils(context);
		return settingUtils.getLong(KeyConstants.SHARED_KEY_TIME, 0);
	}

	/**
	 * 获取用户token
	 * 
	 * @param context
	 * @return
	 */
	public static final String getUserToken(Context context) {
		SettingUtils settingUtils = getSettingUtils(context);
		return settingUtils.getString(KeyConstants.SHARED_KEY_TOKEN, "");
	}

	/**
	 * 获取用户uid
	 * 
	 * @param context
	 * @return
	 */
	public static final String getUserUid(Context context) {
		SettingUtils settingUtils = getSettingUtils(context);
		return settingUtils.getString(KeyConstants.SHARED_KEY_UID, "");
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	private static SettingUtils getSettingUtils(Context context) {
		SettingUtils settingUtils = new SettingUtils(context, KeyConstants.SHARED_NAME_USER, Context.MODE_PRIVATE);
		return settingUtils;
	}

	/**
	 * 获取用户用户名
	 * 
	 * @param context
	 * @return
	 */
	public static final String getUserName(Context context) {
		SettingUtils settingUtils = getSettingUtils(context);
		return settingUtils.getString(KeyConstants.SHARED_KEY_USERNAME, "");
	}

	/**
	 * 获得密码
	 * 
	 * @param context
	 * @return
	 */
	public static final String getPwd(Context context) {
		SettingUtils settingUtils = getSettingUtils(context);
		return settingUtils.getString(KeyConstants.SHARED_KEY_PWD, "");
	}

}
