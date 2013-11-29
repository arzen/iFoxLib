package com.arzen.iFoxLib.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;

import com.arzen.iFoxLib.bean.Order;
import com.arzen.iFoxLib.bean.PayList;
import com.encore.libs.http.HttpConnectManager;
import com.encore.libs.http.OnRequestListener;
import com.encore.libs.http.Request;
import com.encore.libs.json.JsonParser;

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
	 * 参数amount 价格
	 */
	public static final String PARAM_AMOUNT = "amount";

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
		String url = HttpSetting.IOFX_CREATE_ORDER;

		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put(PARAM_GID, gid);
		maps.put(PARAM_CID, cid);
		maps.put(PARAM_TOKEN, token);
		maps.put(PARAM_PID, gid);
		maps.put(PARAM_AMOUNT, cid);
		maps.put(PARAM_TYPE, payType);
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
		return paramString.toString();
	}
}
