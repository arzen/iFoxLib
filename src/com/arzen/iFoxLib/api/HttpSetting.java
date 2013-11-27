package com.arzen.iFoxLib.api;
/**
 * http 设置
 * @author Encore.liang
 *
 */
public class HttpSetting {
	/**
	 * 服务器地址
	 */
	public static final String IFOX_SERVER_URL = "http://ifox";
	
	/**
	 * ifox init url
	 */
	public static final String IFOX_INIT = IFOX_SERVER_URL + "/app/init.php";
	
	/**
	 * 获取服务器url
	 * @return
	 */
	public static String getServerUrl(){
		return IFOX_SERVER_URL;
	}
	
	/**
	 * 得到初始化url
	 * @return
	 */
	public static String getIFoxInitUrl()
	{
		return IFOX_INIT;
	}
}
