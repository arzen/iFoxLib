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
	public static final String IFOX_INIT = IFOX_SERVER_URL + "/app/init.php";

	/**
	 * 支付列表url
	 */
	public static final String IFOX_PAYLIST = IFOX_SERVER_URL + "/payment/list.php";
	
	/**
	 * 创建订单url
	 */
	public static final String IFOX_CREATE_ORDER =  IFOX_SERVER_URL + "/payment/create.php";
	
	/**
	 * 检查是否有更新url
	 */
	public static final String IFOX_UPDATE_DYNAMICLIB = IFOX_SERVER_URL + "/app/bizz.php";

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
		return IFOX_INIT;
	}

	/**
	 * 获取支付列表url
	 * 
	 * @return
	 */
	public static String getPayListUrl() {
		return IFOX_PAYLIST;
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
