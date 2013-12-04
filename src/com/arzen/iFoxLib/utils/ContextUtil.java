package com.arzen.iFoxLib.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class ContextUtil {

	private static final String[] FILTERED_PACKAGES = new String[] {
			"com.google.*", "com.android.*" };

	public static boolean isHomeLauncherPkName(Context context,
			String packageName) {
		boolean result = false;
		PackageManager pm = context.getPackageManager();
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent,
				PackageManager.GET_ACTIVITIES);
		if (resolveInfos == null || resolveInfos.size() <= 0) {
			return false;
		}

		for (ResolveInfo info : resolveInfos) {
			if (info.activityInfo.packageName.equals(packageName)) {
				result = true;
			}

		}
		return result;
	}

	public static void shareText(Context context, String title, String text) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT, text);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);// Intent.createChooser(intent, title)
	}

	@SuppressLint("SetJavaScriptEnabled")
	public static void showGlobalDialog(final Context context, final Bundle params) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);

		WebView wv = new WebView(context);
		wv.loadUrl( params.getString("url") );
		wv.requestFocus(View.FOCUS_DOWN);
		wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

		WebSettings setting = wv.getSettings();
		setting.setJavaScriptEnabled(true);
//		setting.setPluginsEnabled(false);
		setting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

		dialog.setView(wv);
		
		String btTitle = "马上下载";
		if ( params.getString("category").equals("1") ) {
			btTitle = "查看更多";
		}
		
		dialog.setPositiveButton("送给朋友", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ContextUtil.shareText(context, params.getString("st"), params.getString("sc"));
				
			}
		});
		dialog.setNegativeButton(btTitle, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				downloadAPK(context, params.getString("d"));
			}
		});
		AlertDialog mDialog = dialog.create();
		mDialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);// 设定为系统级警告，关键
		mDialog.show();
	}
	
	public static void downloadAPK(Context context, String url) {
		Uri uri = Uri.parse(url); 
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);		
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public static boolean isPackageInstalled(Context context, String packageName) {
		if (TextUtils.isEmpty(packageName)) {
			return false;
		}
		PackageManager pm = context.getPackageManager();
		try {
			pm.getPackageInfo(packageName, 0);
			return true;
		} catch (NameNotFoundException e) {
		}
		return false;
	}

	private static ArrayList<AppInfoItem> getFilteredAppInfo(Context context) {
		ArrayList<AppInfoItem> allAppInfoItems = new ArrayList<AppInfoItem>();
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
		for (PackageInfo info : packageInfos) {
			AppInfoItem item = new AppInfoItem();
			item.applicationName = info.applicationInfo.loadLabel(pm)
					.toString();
			item.versionName = info.versionName;
			item.packageName = info.packageName;
			item.versionCode = "" + info.versionCode;
//			LogUtil.i("FilterApp", "packgeName:" + item.packageName);
			boolean filter = false;
			for (String s : FILTERED_PACKAGES) {
				if (item.packageName.matches(s)) {
					filter = true;
				}
			}
			if (!filter) {
				allAppInfoItems.add(item);
			}
		}

		return allAppInfoItems;
	}

	static class AppInfoItem {
		String packageName;
		String applicationName;
		String versionName;
		String versionCode;

		public String toString() {
			return "packageName:" + packageName + "\n" + "applicationName:"
					+ applicationName + "\n" + "versionName:" + versionName;
		}
	}

	public static String getFilteredAppJson(Context context) {
		ArrayList<AppInfoItem> allAppInfoItems = getFilteredAppInfo(context);
		return generateAppJson(allAppInfoItems);
	}

	/**
	 * 
	 * @param list
	 *            appinfo list
	 * 
	 * @return list to json String
	 */
	private static String generateAppJson(ArrayList<AppInfoItem> list) {
		int len = list.size();
		if (len <= 0) {
			return null;
		}
		JSONStringer stringer = new JSONStringer();
		try {
			stringer.array();
			for (int i = 0; i < len; i++) {
				AppInfoItem info = list.get(i);
				stringer.array();
				stringer.value(info.packageName);
				stringer.value(info.applicationName);
				stringer.value(info.versionName);
				stringer.value(info.versionCode);
				stringer.endArray();
			}
			stringer.endArray();
			return stringer.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static final boolean startWithPackage(Context context, String packageName) {
		boolean started=false;
		if ( isPackageInstalled(context, packageName) ) {      
		    Intent intent = context.getPackageManager().getLaunchIntentForPackage(      
		            packageName);      
		    context.startActivity(intent);   
		    started=true;
		}
		return started;
	}

	public static String getCurrentPackageName(Context context) {
		return context.getApplicationContext().getPackageName();
	}

	public static int getVersionCode(Context context, String packageName) {
		PackageInfo pi = null;
		try {
			pi = context.getPackageManager().getPackageInfo(packageName, 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static JSONObject getAssConfig(Context context) {
		AssetManager am = context.getAssets();
		try {
			InputStream is = am.open("config.dat");
			byte[] b = new byte[is.available()];
			is.read(b);
			String channelFile = new String(b);
			JSONObject json = new JSONObject(channelFile);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
