package com.arzen.iFoxLib.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;

import com.arzen.iFoxLib.bean.BaseBean;
import com.arzen.iFoxLib.bean.User;
import com.arzen.iFoxLib.bean.Order;
import com.arzen.iFoxLib.bean.PayList;
import com.encore.libs.http.HttpConnectManager;
import com.encore.libs.http.OnRequestListener;
import com.encore.libs.http.Request;
import com.encore.libs.json.JsonParser;
import com.encore.libs.utils.Log;

/**
 * 请求类
 * 
 * @author Encore.liang
 * 
 */
public class HttpIfoxApi {
	/**
	 * 参数gid
	 */
	public static final String PARAM_GID = "gid";

	/**
	 * 参数cid
	 */
	public static final String PARAM_CID = "cid";

	/**
	 * 参数token
	 */
	public static final String PARAM_TOKEN = "token";

	/**
	 * 参数app_key
	 */
	public static final String PARAM_APP_KEY = "app_key";

	/**
	 * 参数app_secret
	 */
	public static final String PARAM_APP_SECRET = "app_secret";

	/**
	 * 参数package
	 */
	public static final String PARAM_PACKAGE = "package";

	/**
	 * 参数pid 道具id
	 */
	public static final String PARAM_PID = "pid";

	/**
	 * 参数price 价格
	 */
	public static final String PARAM_PRICE = "price";

	/**
	 * 参数type 支付类型
	 */
	public static final String PARAM_TYPE = "type";

	/**
	 * 参数created 创建订单时间
	 */
	public static final String PARAM_CREATED = "created";

	/**
	 * 游戏需要上传的信息
	 */
	public static final String PARAM_EXTRA = "extra";
	/**
	 * 用户名key
	 */
	public static final String PARAM_USERNAME = "uname";
	/**
	 * 用户密码key
	 */
	public static final String PARAM_PASSWORD = "passwd";
	
	/**
	 * 用户新密码key
	 */
	public static final String PARAM_NEWPASSWORD = "new_passwd";

	/**
	 * 请求支付列表
	 * 
	 * @param gid
	 *            游戏id
	 * @param cid
	 *            渠道id
	 * @param token
	 *            登录后的token
	 */
	public static void requestPayList(Activity activity, String gid, String cid, String token, OnRequestListener onRequestListener) {
		// 得到支付列表url
		String url = HttpSetting.getPayListUrl();

		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put(PARAM_GID, gid);
		maps.put(PARAM_CID, cid);
		maps.put(PARAM_TOKEN, token);

		String postParam = createParams(maps);

		Request request = new Request();
		request.setUrl(url);
		request.setParser(new JsonParser(PayList.class));
		request.setOnRequestListener(onRequestListener);
		HttpConnectManager.getInstance(activity.getApplicationContext()).doPost(request, postParam);
	}

	/**
	 * 创建订单
	 * 
	 * @param gid
	 *            游戏id
	 * @param cid
	 *            渠道id
	 * @param token
	 *            登陆后的token
	 * @param pid
	 *            道具id
	 * @param amount
	 *            价格
	 * @param payType
	 *            支付类型
	 * @param extra
	 *            游戏上传的自定义信息
	 */
	public static void createOrder(Context context, String gid, String cid, String token, int pid, float amount, int payType, String extra, OnRequestListener onRequestListener) {
		String url = HttpSetting.IFOX_CREATE_ORDER_URL;

		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put(PARAM_GID, gid);
		maps.put(PARAM_CID, cid);
		maps.put(PARAM_TOKEN, token);
		maps.put(PARAM_PID, pid);
		maps.put(PARAM_PRICE, amount);
		maps.put(PARAM_TYPE, payType);
		maps.put(PARAM_CREATED, System.currentTimeMillis());
		try {
			maps.put(PARAM_EXTRA, URLEncoder.encode(extra, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String postParam = createParams(maps);

		Request request = new Request();
		request.setUrl(url);
		request.setParser(new JsonParser(Order.class));
		request.setOnRequestListener(onRequestListener);
		HttpConnectManager.getInstance(context.getApplicationContext()).doPost(request, postParam);
	}

	/**
	 * 登录帐号
	 * 
	 * @param context
	 * @param gid
	 *            游戏id
	 * @param cid
	 *            渠道id
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param onRequestListener
	 */
	public static void requestLogin(Context context, String gid, String cid, String userName, String password, OnRequestListener onRequestListener) {
		String url = HttpSetting.IFOX_LOGIN_URL;

		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put(PARAM_GID, gid);
		maps.put(PARAM_CID, cid);
		maps.put(PARAM_USERNAME, userName);
		maps.put(PARAM_PASSWORD, password);

		String postParam = createParams(maps);

		Request request = new Request();
		request.setUrl(url);
		request.setParser(new JsonParser(User.class));
		request.setOnRequestListener(onRequestListener);
		HttpConnectManager.getInstance(context.getApplicationContext()).doPost(request, postParam);
	}

	/**
	 * 注册帐号
	 * 
	 * @param context
	 * @param gid
	 * @param cid
	 * @param userName
	 * @param password
	 * @param onRequestListener
	 */
	public static void requestRegister(Context context, String gid, String cid, String userName, String password, OnRequestListener onRequestListener) {
		String url = HttpSetting.IFOX_REGISTER_URL;

		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put(PARAM_GID, gid);
		maps.put(PARAM_CID, cid);
		maps.put(PARAM_USERNAME, userName);
		maps.put(PARAM_PASSWORD, password);

		String postParam = createParams(maps);

		Request request = new Request();
		request.setUrl(url);
		request.setParser(new JsonParser(User.class));
		request.setOnRequestListener(onRequestListener);
		HttpConnectManager.getInstance(context.getApplicationContext()).doPost(request, postParam);
	}

	/**
	 * 修改密码
	 * 
	 * @param context
	 * @param gid
	 * @param cid
	 * @param token
	 * @param oldPassword
	 * @param newPassword
	 * @param onRequestListener
	 */
	public static void requestChangePassword(Context context, String gid, String cid, String token, String oldPassword, String newPassword, OnRequestListener onRequestListener) {
		String url = HttpSetting.IFOX_CHANGEPASSWORD_URL;

		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put(PARAM_GID, gid);
		maps.put(PARAM_CID, cid);
		maps.put(PARAM_TOKEN, token);
		maps.put(PARAM_PASSWORD, oldPassword);
		maps.put(PARAM_NEWPASSWORD, newPassword);

		String postParam = createParams(maps);

		Request request = new Request();
		request.setUrl(url);
		request.setParser(new JsonParser(BaseBean.class));
		request.setOnRequestListener(onRequestListener);
		HttpConnectManager.getInstance(context.getApplicationContext()).doPost(request, postParam);
	}
	/**
	 * 得到流水号
	 * @param context
	 * @param onRequestListener
	 */
	public static void requestTnNumber(Context context,OnRequestListener onRequestListener){
		String url = HttpSetting.MERCHANT_SERVER;
		Request request = new Request();
		request.setUrl(url);
		request.setOnRequestListener(onRequestListener);
		HttpConnectManager.getInstance(context.getApplicationContext()).doPost(request);
	};

	/**
	 * 创建参数
	 * 
	 * @param params
	 * @return
	 */
	public static String createParams(Map<String, Object> params) {
		StringBuffer sb = new StringBuffer();
		String paramString = "";
		try {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				sb.append(entry.getKey());
				sb.append("=");
				sb.append(entry.getValue());
				sb.append("&");
			}
			int lastIndex = sb.toString().lastIndexOf("&");
			paramString = sb.toString().substring(0, lastIndex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String data = paramString.toString();
		Log.i("WebUtils", "postData:" + data);
		return data;
	}
}
