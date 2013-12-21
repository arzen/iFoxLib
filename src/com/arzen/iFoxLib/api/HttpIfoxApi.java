package com.arzen.iFoxLib.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;

import com.arzen.iFoxLib.R;
import com.arzen.iFoxLib.bean.Auth;
import com.arzen.iFoxLib.bean.BaseBean;
import com.arzen.iFoxLib.bean.Invited;
import com.arzen.iFoxLib.bean.Order;
import com.arzen.iFoxLib.bean.PayList;
import com.arzen.iFoxLib.bean.PrepaidCard;
import com.arzen.iFoxLib.bean.Token;
import com.arzen.iFoxLib.bean.Top;
import com.arzen.iFoxLib.bean.User;
import com.arzen.iFoxLib.setting.UserSetting;
import com.arzen.iFoxLib.utils.MD5;
import com.arzen.iFoxLib.utils.MD5Util;
import com.arzen.iFoxLib.utils.MsgUtil;
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

	public static final String PARAM_DATA = "data";
	/**
	 * 页码
	 */
	public static final String PARAM_PAGE = "page";
	
	/**
	 * RESPINSE TYPE
	 */
	public static final String PARAM_RESPONSE_TYPE = "response_type";
	/**
	 * URI
	 */
	public static final String PARAM_REDIRECT_URI = "redirect_uri";
	/**
	 * key
	 */
	public static final String PARAM_CLIENT_ID = "client_id";

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
	public static void requestPayList(final Activity activity, final String gid, final String cid, 
			final String token, final String clientId, final String clientSecret, final OnRequestListener onRequestListener) {
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
		request.setOnRequestListener(new OnRequestListener() {

			@Override
			public void onResponse(final String url, final int state, final Object result, final int type) {
				// TODO Auto-generated method stub
				if (state == HttpConnectManager.STATE_TOKEN_EXCEPTION) { // 先判断token失效
					refreshToken(activity.getApplicationContext(), gid, cid, clientId, clientSecret, new RefreshTokenCallBack() {

						@Override
						public void onSuccess(String token) {
							// TODO Auto-generated method stub
							requestPayList(activity, gid, cid, token, clientId, clientSecret, onRequestListener); // 刷新token
																									// 成功重新请求
						}

						@Override
						public void onFail(String msg) {
							// TODO Auto-generated method stub
							onRequestListener.onResponse(url, state, result, type);
						}
					});
					return;
				}

				onRequestListener.onResponse(url, state, result, type);
			}
		});
		HttpConnectManager.getInstance(activity.getApplicationContext()).doPost(request, postParam);
	}

	/**
	 * 刷新token
	 * 
	 * @param context
	 * @param gid
	 * @param cid
	 * @param userName
	 * @param password
	 * @param clientId
	 * @param clientSecret
	 */
	public static void refreshToken(final Context context, String gid, String cid, final String clientId, final String clientSecret, final RefreshTokenCallBack cb) {
		String userName = UserSetting.getUserName(context);
		String password = UserSetting.getPwd(context);

		if (userName != null && password != null && !userName.equals("") && !password.equals("")) {
			password = MD5Util.getMD5String(password);
			// 重新登录
			requestLogin(context, gid, cid, userName, password, clientId, clientSecret, new OnLoginCallBack() {

				@Override
				public void onSuccess(String uid, String token) {
					// TODO Auto-generated method stub
					cb.onSuccess(token);
				}

				@Override
				public void onFail(String msg) {
					// TODO Auto-generated method stub
					cb.onFail(msg);
				}
			});
		}
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
	public static void createOrder(final Context context,final String gid, 
			final String cid,final String token,final int pid,final float amount,final int payType,
			final String extra,final String clientId, final String clientSecret,final OnRequestListener onRequestListener) {
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
			if (extra == null || extra.equals("")) {
				maps.put(PARAM_EXTRA, URLEncoder.encode(" ", "utf-8"));
			}else{
				maps.put(PARAM_EXTRA, URLEncoder.encode(extra, "utf-8"));
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String postParam = createParams(maps);

		Request request = new Request();
		request.setUrl(url);
		request.setParser(new JsonParser(Order.class));
		
		request.setOnRequestListener(new OnRequestListener() {

			@Override
			public void onResponse(final String url, final int state, final Object result, final int type) {
				// TODO Auto-generated method stub
				if (state == HttpConnectManager.STATE_TOKEN_EXCEPTION) { // 先判断token失效
					refreshToken(context, gid, cid, clientId, clientSecret, new RefreshTokenCallBack() {

						@Override
						public void onSuccess(String token) {
							// TODO Auto-generated method stub
							createOrder(context, gid, cid, token, pid, amount, payType, extra, clientId, clientSecret, onRequestListener);
						}

						@Override
						public void onFail(String msg) {
							// TODO Auto-generated method stub
							onRequestListener.onResponse(url, state, result, type);
						}
					});
					return;
				}

				onRequestListener.onResponse(url, state, result, type);
			}
		});
		
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
	public static void requestLogin(final Context context, String gid, String cid, String userName, String password, final String clientId, final String clientSecret, final OnLoginCallBack cb) {
		String url = HttpSetting.IFOX_AUTH_URL;

		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put(PARAM_GID, gid);
		maps.put(PARAM_CID, cid);
		maps.put(PARAM_USERNAME, userName);
		maps.put(PARAM_PASSWORD, password);
		maps.put(PARAM_RESPONSE_TYPE, "code");
		maps.put(PARAM_REDIRECT_URI, "");
		maps.put(PARAM_CLIENT_ID, clientId);

		String postParam = createParams(maps);

		Request request = new Request();
		request.setUrl(url);
		request.setParser(new JsonParser(Auth.class));
		request.setOnRequestListener(new OnRequestListener() {

			@Override
			public void onResponse(final String url, final int state, final Object result, final int type) {
				// TODO Auto-generated method stub
				if (state == HttpConnectManager.STATE_SUC && result != null && result instanceof Auth) {
					Auth auth = (Auth) result;
					if (auth.getCode() == HttpSetting.RESULT_CODE_OK && auth.getData() != null) {
						requestToken(context, clientId, clientSecret, auth.getData().getCode(), cb);
					} else {
						cb.onFail(auth.getMsg());
					}
				} else if (state == HttpConnectManager.STATE_TIME_OUT) { // 请求超时
					cb.onFail(context.getString(R.string.time_out));
				} else { // 请求失败
					cb.onFail(context.getString(R.string.request_fail));
				}
			}
		});
		HttpConnectManager.getInstance(context.getApplicationContext()).doPost(request, postParam);
	}

	public static void requestToken(final Context context, String clientId, String clientSecret, String code, final OnLoginCallBack cb) {
		String url = HttpSetting.IFOX_TOKEN_URL;

		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put(PARAM_REDIRECT_URI, "");
		maps.put(PARAM_CLIENT_ID, clientId);
		maps.put("client_secret", clientSecret);
		maps.put("grant_type", "authorization_code");
		maps.put("code", code);

		String postParam = createParams(maps);

		Request request = new Request();
		request.setUrl(url);
		request.setParser(new JsonParser(Token.class));
		request.setOnRequestListener(new OnRequestListener() {

			@Override
			public void onResponse(final String url, final int state, final Object result, final int type) {
				// TODO Auto-generated method stub
				if (state == HttpConnectManager.STATE_SUC && result != null && result instanceof Token) {
					Token token = (Token) result;
					if (token.getCode() == HttpSetting.RESULT_CODE_OK && token.getData() != null) {
						UserSetting.saveUserData(context, token.getData().getUid(), token.getData().getToken());
						cb.onSuccess(token.getData().getUid(), token.getData().getToken());
					} else {
						cb.onFail(token.getMsg());
					}
				} else if (state == HttpConnectManager.STATE_TIME_OUT) { // 请求超时
					cb.onFail(context.getString(R.string.time_out));
				} else { // 请求失败
					cb.onFail(context.getString(R.string.request_fail));
				}
			}
		});
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
//		String url = HttpSetting.IFOX_CHANGEPASSWORD_URL;
//
//		Map<String, Object> maps = new HashMap<String, Object>();
//		maps.put(PARAM_GID, gid);
//		maps.put(PARAM_CID, cid);
//		maps.put(PARAM_TOKEN, token);
//		maps.put(PARAM_PASSWORD, oldPassword);
//		maps.put(PARAM_NEWPASSWORD, newPassword);
//
//		String postParam = createParams(maps);
//
//		Request request = new Request();
//		request.setUrl(url);
//		request.setParser(new JsonParser(BaseBean.class));
//		request.setOnRequestListener(onRequestListener);
//		HttpConnectManager.getInstance(context.getApplicationContext()).doPost(request, postParam);
	}

	/**
	 * 得到流水号
	 * 
	 * @param context
	 * @param onRequestListener
	 */
	public static void requestTnNumber(Context context, OnRequestListener onRequestListener) {
		String url = HttpSetting.MERCHANT_SERVER;
		Request request = new Request();
		request.setUrl(url);
		request.setOnRequestListener(onRequestListener);
		HttpConnectManager.getInstance(context.getApplicationContext()).doPost(request);
	};

	/**
	 * 请求排行榜列表
	 * 
	 * @param context
	 * @param gid
	 * @param token
	 * @param pageNumber
	 * @param onRequestListener
	 */
	public static void requestTopList(final Context context,final String gid,final String token,final int pageNumber,
			final String cid,final String clientId, final String clientSecret,final OnRequestListener onRequestListener) {
		String url = HttpSetting.IFOX_TOP_URL;

		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put(PARAM_GID, gid);
		maps.put(PARAM_TOKEN, token);
		maps.put(PARAM_PAGE, pageNumber);

		String postParam = createParams(maps);

		Request request = new Request(url);
		request.setParser(new JsonParser(Top.class, false));
		
		
		request.setOnRequestListener(new OnRequestListener() {

			@Override
			public void onResponse(final String url, final int state, final Object result, final int type) {
				// TODO Auto-generated method stub
				if (state == HttpConnectManager.STATE_TOKEN_EXCEPTION) { // 先判断token失效
					refreshToken(context, gid, cid, clientId, clientSecret, new RefreshTokenCallBack() {

						@Override
						public void onSuccess(String token) {
							// TODO Auto-generated method stub
							requestTopList(context, gid, token, pageNumber,cid, clientId, clientSecret, onRequestListener);
						}

						@Override
						public void onFail(String msg) {
							// TODO Auto-generated method stub
							onRequestListener.onResponse(url, state, result, type);
						}
					});
					return;
				}

				onRequestListener.onResponse(url, state, result, type);
			}
		});
		
		HttpConnectManager.getInstance(context.getApplicationContext()).doPost(request, postParam);
	}

	/**
	 * 上传通讯录
	 * 
	 * @param context
	 * @param token
	 * @param contactJson
	 * @param onRequestListener
	 */
	public static void upLoadContacts(final Context context,final String token,final String contactJson,
			final String gid,
			final String cid,final String clientId, final String clientSecret,
			final OnRequestListener onRequestListener) {
		String url = HttpSetting.IFOX_UPLOAD_CONTACT_URL;

		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put(PARAM_DATA, contactJson);
		maps.put(PARAM_TOKEN, token);

		String postParam = createParams(maps);

		Request request = new Request(url);
		request.setOnRequestListener(onRequestListener);
		request.setOnRequestListener(new OnRequestListener() {

			@Override
			public void onResponse(final String url, final int state, final Object result, final int type) {
				// TODO Auto-generated method stub
				if (state == HttpConnectManager.STATE_TOKEN_EXCEPTION) { // 先判断token失效
					refreshToken(context, gid, cid, clientId, clientSecret, new RefreshTokenCallBack() {

						@Override
						public void onSuccess(String token) {
							// TODO Auto-generated method stub
							upLoadContacts(context, token, contactJson, gid, cid, clientId, clientSecret, onRequestListener);
						}

						@Override
						public void onFail(String msg) {
							// TODO Auto-generated method stub
							onRequestListener.onResponse(url, state, result, type);
						}
					});
					return;
				}

				onRequestListener.onResponse(url, state, result, type);
			}
		});
		HttpConnectManager.getInstance(context.getApplicationContext()).doPost(request, postParam);
	}

	/**
	 * 充值卡充值
	 * 
	 * @param context
	 * @param price
	 * @param ca_sn
	 *            卡号
	 * @param ca_pwd
	 *            密码
	 * @param orderId
	 * @param extra
	 * @param onRequestListener
	 */
	public static void requestPrepaidCardPay(Context context, int price, String ca_sn, String ca_pwd, String orderId, String extra, OnRequestListener onRequestListener) {
		String url = HttpSetting.IFOX_PREPAIDCARDPAY_URL;

		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put(PARAM_PRICE, price);
		maps.put("ca_sn", ca_sn);
		maps.put("ca_pwd", ca_pwd);
		maps.put("oid", orderId);
		try {
			if (extra == null || extra.equals("")) {
				extra = " ";
			}
			maps.put(PARAM_EXTRA, URLEncoder.encode(extra, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String postParam = createParams(maps);

		Request request = new Request(url);
		request.setOnRequestListener(onRequestListener);
		request.setParser(new JsonParser(PrepaidCard.class, false));
		HttpConnectManager.getInstance(context.getApplicationContext()).doPost(request, postParam);
	}

	/**
	 * 获取邀请文本
	 * 
	 * @param context
	 * @param token
	 * @param gid
	 * @param onRequestListener
	 */
	public static void requestInviteData(Context context, String token, String gid, OnRequestListener onRequestListener) {
		String url = HttpSetting.IFOX_INVETED_URL;

		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put(PARAM_TOKEN, token);
		maps.put(PARAM_GID, gid);

		String postParam = createParams(maps);

		Request request = new Request(url);
		request.setOnRequestListener(onRequestListener);
		request.setParser(new JsonParser(Invited.class, false));
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
		String data = paramString.toString();
		Log.i("WebUtils", "postData:" + data);
		return data;
	}

//	public interface RequestCallBack {
//		public void onSuccess(Object object);
//
//		public void onFail(String msg);
//	}

	public interface RefreshTokenCallBack {
		public void onSuccess(String token);

		public void onFail(String msg);
	}
	
	public interface OnLoginCallBack {
		public void onSuccess(String uid, String token);

		public void onFail(String msg);
	}
}
