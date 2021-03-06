package com.arzen.iFoxLib.api;

/**
 * http 设置
 * 
 * @author Encore.liang
 * 
 */
public class HttpSetting {

	/**
	 * 请求返回码
	 */
	public static final int RESULT_CODE_OK = 200;
	/**
	 * 服务器地址
	 */
	public static String IFOX_SERVER_URL = "http://ifox.api.eeeeeec.com";

	/**
	 * ifox init url
	 */
	public static String IFOX_INIT_URL = IFOX_SERVER_URL + "/app/init.php";

	/**
	 * 支付列表url
	 */
	public static String IFOX_PAYLIST_URL = IFOX_SERVER_URL + "/payment/list.php";

	/**
	 * 创建订单url
	 */
	public static String IFOX_CREATE_ORDER_URL = IFOX_SERVER_URL + "/payment/create.php";

	/**
	 * 登录url
	 */
	public static String IFOX_LOGIN_URL = IFOX_SERVER_URL + "/account/login.php";
	/**
	 * OUTH验证第一步url
	 */
	public static String IFOX_AUTH_URL = IFOX_SERVER_URL + "/oauth2/auth.php";
	/**
	 * 获取token 第二部
	 */
	public static String IFOX_TOKEN_URL = IFOX_SERVER_URL + "/oauth2/token.php";

	/**
	 * 注册url
	 */
	public static String IFOX_REGISTER_URL = IFOX_SERVER_URL + "/account/register.php";

	/**
	 * 修改密码url
	 */
	public static String IFOX_CHANGEPASSWORD_URL = IFOX_SERVER_URL + "/account/modify.php";
	/**
	 * 检查是否有更新url
	 */
	public static String IFOX_UPDATE_DYNAMICLIB = IFOX_SERVER_URL + "/app/bizz.php";

	/**
	 * 提交分数
	 */
	public static String IFOX_COMMIT_SCORE = IFOX_SERVER_URL + "/leaderboard/score.php";

	/**
	 * 排行榜url
	 */
	public static String IFOX_TOP_URL = IFOX_SERVER_URL + "/leaderboard/list.php";
	/**
	 * 上传通讯录
	 */
	public static String IFOX_UPLOAD_CONTACT_URL = IFOX_SERVER_URL + "/leaderboard/contact.php";
	/**
	 * 充值卡
	 */
	public static String IFOX_PREPAIDCARDPAY_URL = IFOX_SERVER_URL + "/callback/szfu.php";
	/**
	 * 邀请模版
	 */
	public static String IFOX_INVETED_URL = IFOX_SERVER_URL + "/leaderboard/invite.php";
	/**
	 * 分享模版
	 */
	public static String IFOX_SHARE_URL = IFOX_SERVER_URL + "/share/msg.php";

	// 商户服务器地址
	public static final String MERCHANT_SERVER = "http://202.104.148.76/merchant_server/SubmitOrder";

	/**
	 * 获取服务器url
	 * 
	 * @return
	 */
	public static String getServerUrl() {
		return IFOX_SERVER_URL;
	}

	/**
	 * 得到初始化url
	 * 
	 * @return
	 */
	public static String getIFoxInitUrl() {
		return IFOX_INIT_URL;
	}

	/**
	 * 获取支付列表url
	 * 
	 * @return
	 */
	public static String getPayListUrl() {
		return IFOX_PAYLIST_URL;
	}

	/**
	 * 获取动态库更新url
	 * 
	 * @return
	 */
	public static String getDynamicUpdateUrl() {
		return IFOX_UPDATE_DYNAMICLIB;
	}

	public static String getCommitScore() {
		return IFOX_COMMIT_SCORE;
	}

	public static String getShareUrl() {
		return IFOX_SHARE_URL;
	}

	public static void setServerUrl(String serverUrl) {
		IFOX_SERVER_URL = serverUrl;
		//切换后重新生成地址
		IFOX_INIT_URL = IFOX_SERVER_URL + "/app/init.php";
		IFOX_PAYLIST_URL = IFOX_SERVER_URL + "/payment/list.php";
		IFOX_CREATE_ORDER_URL = IFOX_SERVER_URL + "/payment/create.php";
		IFOX_LOGIN_URL = IFOX_SERVER_URL + "/account/login.php";
		IFOX_AUTH_URL = IFOX_SERVER_URL + "/oauth2/auth.php";
		IFOX_TOKEN_URL = IFOX_SERVER_URL + "/oauth2/token.php";
		IFOX_REGISTER_URL = IFOX_SERVER_URL + "/account/register.php";
		IFOX_CHANGEPASSWORD_URL = IFOX_SERVER_URL + "/account/modify.php";
		IFOX_UPDATE_DYNAMICLIB = IFOX_SERVER_URL + "/app/bizz.php";
		IFOX_COMMIT_SCORE = IFOX_SERVER_URL + "/leaderboard/score.php";
		IFOX_TOP_URL = IFOX_SERVER_URL + "/leaderboard/list.php";
		IFOX_UPLOAD_CONTACT_URL = IFOX_SERVER_URL + "/leaderboard/contact.php";
		IFOX_PREPAIDCARDPAY_URL = IFOX_SERVER_URL + "/callback/szfu.php";
		IFOX_INVETED_URL = IFOX_SERVER_URL + "/leaderboard/invite.php";
		IFOX_SHARE_URL = IFOX_SERVER_URL + "/share/msg.php";
	}
}
