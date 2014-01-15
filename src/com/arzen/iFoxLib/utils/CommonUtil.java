package com.arzen.iFoxLib.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.encore.libs.imagecache.Utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;

public class CommonUtil {
	/**
	 * Check whether the specified permission is granted to the current package.
	 * 
	 * @param context
	 * @param permissionName
	 *            The permission.
	 * @return True if granted, false otherwise.
	 */
	public static boolean checkPermission(Context context, String permissionName) {
		PackageManager packageManager = context.getPackageManager();
		String pkgName = context.getPackageName();
		return packageManager.checkPermission(permissionName, pkgName) == PackageManager.PERMISSION_GRANTED;
	}

	public static boolean isZero(String id) {
		for (int i = 0; i < id.length(); i++) {
			char index = id.charAt(i);
			if (index != '0')
				return false;
		}
		return true;
	}

	/**
	 * 文件转化为Object
	 * 
	 * @param fileName
	 * @return byte[]
	 */
	public static Object file2Object(String fileName) {

		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(fileName);
			ois = new ObjectInputStream(fis);
			Object object = ois.readObject();
			return object;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 把Object输出到文件
	 * 
	 * @param obj
	 * @param outputFile
	 */
	public static void object2File(Object obj, String outputFile) {
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(outputFile));
			oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	/**
	 * Get a usable cache directory (external if available, internal otherwise).
	 * 
	 * @param context
	 *            The context to use
	 * @param uniqueName
	 *            A unique directory name to append to the cache dir
	 * @return The cache dir
	 */
	public static File getDiskCacheDir(Context context, String uniqueName) {
		// Check if media is mounted or storage is built-in, if so, try and use
		// external cache dir
		// otherwise use internal cache dir
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() : context.getCacheDir().getPath();
		File file = new File(cachePath + File.separator + uniqueName);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	/**
	 * Check if external storage is built-in or removable.
	 * 
	 * @return True if external storage is removable (like an SD card), false
	 *         otherwise.
	 */
	@TargetApi(9)
	public static boolean isExternalStorageRemovable() {
		if (Utils.hasGingerbread()) {
			return Environment.isExternalStorageRemovable();
		}
		return true;
	}

	/**
	 * Get the external app cache directory.
	 * 
	 * @param context
	 *            The context to use
	 * @return The external cache dir
	 */
	@TargetApi(8)
	public static File getExternalCacheDir(Context context) {
		if (Utils.hasFroyo()) {
			return context.getExternalCacheDir();
		}

		// Before Froyo we need to construct the external cache dir ourselves
		final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
		return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
	}

	public static String getPrepaidCardPayMsg(String code) {
		String msg = "";
		if (code.equals("101")) {
			msg = "md5 验证失败";
		} else if (code.equals("102")) {
			msg = "订单号重复";
		} else if (code.equals("103")) {
			msg = "恶意用户";
		} else if (code.equals("104")) {
			msg = "序列号，密码简单验证失败或之前曾提交过的卡密已验证失败";
		} else if (code.equals("105")) {
			msg = "密码正在处理中";
		} else if (code.equals("106")) {
			msg = "系统繁忙，暂停提交";
		} else if (code.equals("107")) {
			msg = "多次充值时卡内余额不足";
		} else if (code.equals("109")) {
			msg = "des 解密失败";
		} else if (code.equals("201")) {
			msg = "证书验证失败";
		} else if (code.equals("501")) {
			msg = "插入数据库失败";
		} else if (code.equals("502")) {
			msg = "插入数据库失败";
		} else if (code.equals("902")) {
			msg = "商户参数不全";
		} else if (code.equals("903")) {
			msg = "商户 ID 不存在";
		} else if (code.equals("904")) {
			msg = "商户没有激活";
		} else if (code.equals("905")) {
			msg = "商户没有使用该接口的权限";
		} else if (code.equals("906")) {
			msg = "商户没有设置  密钥（privateKey）";
		} else if (code.equals("907")) {
			msg = "商户没有设置  DES 密钥";
		} else if (code.equals("908")) {
			msg = "该笔订单已经处理完成（订单状态已经为确定的状态：成功  或者  失败）";
		} else if (code.equals("910")) {
			msg = "服务器返回地址，不符合规范";
		} else if (code.equals("911")) {
			msg = "订单号，不符合规范";
		} else if (code.equals("912")) {
			msg = "非法订单";
		} else if (code.equals("913")) {
			msg = "该地方卡暂时不支持";
		} else if (code.equals("914")) {
			msg = "金额非法";
		} else if (code.equals("915")) {
			msg = "卡面额非法";
		} else if (code.equals("916")) {
			msg = "商户不支持该充值卡";
		} else if (code.equals("917")) {
			msg = "参数格式不正确";
		} else if (code.equals("0")) {
			msg = "网络连接失败";
		}
		return msg;
	}

	// 测试配置文件
	private static HashMap<String, String> mTestConfigs = new HashMap<String, String>();

	private static String readFile(Context context, String fileName, boolean isAssetFile) {
		if (TextUtils.isEmpty(fileName)) {
			return "";
		}
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			if (isAssetFile) {
				is = context.getAssets().open(fileName);
			} else {
				is = new FileInputStream(fileName);
			}
			byte[] buffer = new byte[1024];
			int readBytes = is.read(buffer);
			baos = new ByteArrayOutputStream(1024);
			while (0 < readBytes) {
				baos.write(buffer, 0, readBytes);
				readBytes = is.read(buffer);
			}
			String s = baos.toString();

			return s;
		} catch (IOException e) {
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			if (null != baos) {
				try {
					baos.close();
				} catch (IOException e) {
				}
			}
		}
		return "";
	}

	private static void initTestConfig(Context context) {
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			String configs = readFile(context, Environment.getExternalStorageDirectory().getAbsolutePath() + "/config.txt", false);
			if (!configs.equals("")) {
				// File skynet_config.txt exists in assets directory
				try {
					JSONObject jo = new JSONObject(configs);
					Iterator<?> keys = jo.keys();
					while (keys.hasNext()) {
						String key = keys.next().toString();
						mTestConfigs.put(key, jo.getString(key));
					}
				} catch (JSONException e) {
				}
			}
		}
	}

	/**
	 * 模式
	 * 
	 * @return
	 */
	public static boolean getDebugModel(Context context) {
		String debug = getTestConfig(context, "debug");
		if (debug == null) {
			return false;
		}
		return Boolean.parseBoolean(debug);
	}
	
	public static String getServerUrl(Context context){
		String serverUrl = getTestConfig(context, "serverUrl");
		return serverUrl;
	}

	private static String getTestConfig(Context context, String key) {
		// return res;
		if (mTestConfigs.size() == 0) {
			initTestConfig(context);
		}
		if (mTestConfigs.size() == 0) {
			return null;
		}
		return mTestConfigs.get(key);
	}

}
