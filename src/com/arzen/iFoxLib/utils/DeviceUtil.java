package com.arzen.iFoxLib.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;

public class DeviceUtil {
	
	public static final String UNKNOWN = "unknown";
	public static final String WIFI = "wifi";
	public static final int CODE_PKG_NOT_FOUND = 1,
			CODE_MAIN_ACT_NOT_FOUND = 2, 
			CODE_SUCCESS = 3;
	
	/**
	 * Get IMEI of the device. If it has no IMEI or no
	 * {@link android.Manifest.permission#READ_PHONE_STATE} permission or it is
	 * an emulator, {@link #UNKNOWN} will be returned.
	 */
	public static String getIMEI(Context context) {
		String id = null;
		if (CommonUtil.checkPermission(context,
				android.Manifest.permission.READ_PHONE_STATE)) {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			id = tm.getDeviceId();
		}
		if (TextUtils.isEmpty(id) || CommonUtil.isZero(id)) {
			return UNKNOWN;
		}

		return id;
	}

	public static String getIMSI(Context context) {
		String id = null;
		if (CommonUtil.checkPermission(context,
				android.Manifest.permission.READ_PHONE_STATE)) {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			id = tm.getSubscriberId();
		}
		if (TextUtils.isEmpty(id) || CommonUtil.isZero(id)) {
			return UNKNOWN;
		}

		return id;
	}

	public static String getUUID(Context context) {
		final TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, androidId;
		if (CommonUtil.checkPermission(context,
				android.Manifest.permission.READ_PHONE_STATE)) {
			tmDevice = "-" + tm.getDeviceId();
		} else {
			tmDevice = "";
		}
		androidId = ""
				+ Settings.Secure.getString(context.getContentResolver(),
						Settings.Secure.ANDROID_ID);

		return androidId + tmDevice;
	}

	public static String getResolutionAsString(Context context) {
		int widthPixels = 0;
		int heightPixels = 0;
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		if (Build.VERSION.SDK_INT <= 12) {
			widthPixels = display.getWidth();
			heightPixels = display.getHeight();
		} else {
			try {
				Method methodW = Display.class.getMethod("getRawWidth");
				widthPixels = (Integer) methodW.invoke(display);

				Method methodH = Display.class.getMethod("getRawHeight");
				heightPixels = (Integer) methodH.invoke(display);
			} catch (Exception e) {
				e.printStackTrace();
				widthPixels = display.getWidth();
				heightPixels = display.getHeight();
			}
		}
		return widthPixels < heightPixels ? widthPixels + "x" + heightPixels
				: heightPixels + "x" + widthPixels;
	}
	
	public static String getCpuFre() {
		String cpuFreFile = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";
		return ((Long)readLong(cpuFreFile)).toString();
	}
	
	private static long readLong(String file) {
		RandomAccessFile raf = null;

		try {
			raf = getFile(file);
			return Long.valueOf(raf.readLine());
		} catch (Exception e) {
			return 0;
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	private static RandomAccessFile getFile(String filename) throws IOException {
		File f = new File(filename);
		return new RandomAccessFile(f, "r");
	}
	
	public static String getDeviceInfo(Context context) {
		String DeviceInfo = "";
		DeviceInfo += "br:"+Build.BRAND.replace(" ", "");
		DeviceInfo += ";md:"+Build.MODEL.replace(" ", "");
		DeviceInfo += ";sver:"+Build.VERSION.RELEASE;
		DeviceInfo += ";res:"+getResolutionAsString(context.getApplicationContext());
		DeviceInfo += ";cpu:"+getCpuFre();
		
		return DeviceInfo;
	}

	public static boolean isNetworkAvaliable(Context context) {
		if (!CommonUtil.checkPermission(context,
				android.Manifest.permission.ACCESS_NETWORK_STATE)) {
			return true;
		}
		ConnectivityManager conMan = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = conMan.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnected();
	}


	
}
