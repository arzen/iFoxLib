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
	public static final void saveUserData(Context context, String uid, String token) {
		SettingUtils settingUtils = getSettingUtils(context);
		settingUtils.putString(KeyConstants.SHARED_KEY_TOKEN, token);
		settingUtils.putString(KeyConstants.SHARED_KEY_UID, uid);
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
}
