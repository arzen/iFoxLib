package com.arzen.iFoxLib;

import android.util.Log;

public class LogUtil {

	static boolean DEBUG_MOD = true; 
	public static void i(String tag, String msg) {
		if (LogUtil.DEBUG_MOD) {
			Log.i(tag, msg);
		}
	}
}
