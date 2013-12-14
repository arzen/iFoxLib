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
	public static final String IFOX_SERVER_URL = "http://ifox.api.eeeeeec.com";

	/**
	 * ifox init url
	 */
	public static final String IFOX_INIT_URL = IFOX_SERVER_URL + "/app/init.php";

	/**
	 * 支付列表url
	 */
	public static final String IFOX_PAYLIST_URL = IFOX_SERVER_URL + "/payment/list.php";
	
	/**
	 * 创建订单url
	 */
	public static final String IFOX_CREATE_ORDER_URL =  IFOX_SERVER_URL + "/payment/create.php";
	
	/**
	 * 登录url
	 */
	public static final String IFOX_LOGIN_URL = IFOX_SERVER_URL+ "/account/login.php";
	
	/**
	 * 注册url
	 */
	public static final String IFOX_REGISTER_URL = IFOX_SERVER_URL+ "/account/register.php";
	
	/**
	 * 修改密码url
	 */
	public static final String IFOX_CHANGEPASSWORD_URL = IFOX_SERVER_URL+ "/account/modify.php";
	/**
	 * 检查是否有更新url
	 */
	public static final String IFOX_UPDATE_DYNAMICLIB = IFOX_SERVER_URL + "/app/bizz.php";
	
	/**
	 * 排行榜url
	 */
	public static final String IFOX_TOP_URL= IFOX_SERVER_URL + "/leaderboard/list.php";
	/**
	 * 上传通讯录
	 */
	public static final String IFOX_UPLOAD_CONTACT_URL= IFOX_SERVER_URL + "/leaderboard/contact.php";
	// 商户服务器地址
	public static final String MERCHANT_SERVER = "http://202.104.148.76/merchant_server/SubmitOrder";
	private final String MODE_RELEASE = "00"; // 正式
	private final String MODE_TEST = "01"; // 测试

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
	 * @return
	 */
	public static String getDynamicUpdateUrl()
	{
		return IFOX_UPDATE_DYNAMICLIB;
	}
}
