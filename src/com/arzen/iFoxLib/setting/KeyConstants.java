package com.arzen.iFoxLib.setting;

/**
 * keyContants 公用全局变量类
 * 
 * @author Encore.liang
 * 
 */
public class KeyConstants {

	/**
	 * 动态加载fragment key
	 */
	public static final String KEY_PACKAGE_NAME = "keyPackage";

	/**
	 * 登录loading页
	 */
	public static final String PKG_LOGIN_LOADING_FRAGMENT = "com.arzen.iFoxLib.fragment.LoginLoadingFragment";
	
	/**
	 * 注册页面
	 */
	public static final String PKG_REGISTER_FRAGMENT = "com.arzen.iFoxLib.fragment.RegisterFragment";
	
	/**
	 * 帐号
	 */
	public static final String INTENT_KEY_PHONE_NUMBER = "phone";
	/**
	 * 公用activty action
	 */
	public static final String ACTION_COMMON_ACTIVITY = "com.action.common.activty";
	/**
	 * 密码
	 */
	public static final String INTENT_KEY_PHONE_PASSWORD = "password";

	/**
	 * 支付方式 微派
	 */
	public static final int PAY_TYPE_WIIPAY = 1;
	/**
	 * 支付宝
	 */
	public static final int PAY_TYPE_ALIPAY = 2;
	/**
	 * 银联
	 */
	public static final int PAY_TYPE_UNIONPAY = 3;

	/**
	 * intent 传递数据key ->gid游戏id
	 */
	public static String INTENT_DATA_KEY_GID = "gid";

	/**
	 * intent 传递数据key ->cid 渠道id
	 */
	public static String INTENT_DATA_KEY_CID = "cid";

	/**
	 * intent 传递数据key ->登录后的token
	 */
	public static String INTENT_DATA_KEY_TOKEN = "token";
	/**
	 * intent 传递数据key -> 支付游戏需要传递记录的数据;如：区服信息，角色信息等 "sn=133242434&role=张三"
	 */
	public static String INTENT_DATA_KEY_EXTRA = "extra";
	/**
	 * 支付key 传递过来的道具编号
	 */
	public static String INTENT_DATA_KEY_PID = "pid";
	/**
	 * 支付金额
	 */
	public static String INTENT_DATA_KEY_AMOUNT = "amount";
	/**
	 * 订单号
	 */
	public static String INTENT_DATA_KEY_ORDERID = "orderid";

	/**
	 * 结果回调action
	 */
	public static final String RECEIVER_RESULT_ACTION = "android.action.result.receiver";
	/**
	 * 支付结果key
	 */
	public static final String INTENT_KEY_PAY_RESULT = "result";
	/**
	 * 支付提示key
	 */
	public static final String INTENT_KEY_PAY_MSG = "showMsg";

	/**
	 * 支付成功
	 */
	public static final String INTENT_KEY_PAY_SUCCESS = "success";

	/**
	 * 支付失败。
	 */
	public static final String INTENT_KEY_PAY_FAIL = "fail";
	/**
	 * 支付取消。
	 */
	public static final String INTENT_KEY_PAY_CANCEL = "cancel";
	
	/**
	 * 用户速数据shaed
	 */
	public static final String SHARED_NAME_USER = "user_setting";
	/**
	 * token
	 */
	public static final String SHARED_KEY_TOKEN = "token";
	/**
	 * 用户id
	 */
	public static final String SHARED_KEY_UID = "uid";
	
}
