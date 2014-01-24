package com.arzen.iFoxLib.pay.ali;

import java.net.URLEncoder;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.arzen.iFoxLib.R;

public class AliPayUtil {

	public static final String TAG = "AliPayUtil";
	
	public static final int RQF_PAY = 1;

	private static final int RQF_LOGIN = 2;

	/**
	 * 
	 * @param activity 主activity
	 * @param mHandler 主线程的handler
	 * @param orderId 订单号
	 * @param subject 显示在支付宝上的标题
	 * @param body 主题内容
	 * @param amount 支付的价钱
	 * @param notifyUrl 回调地址
	 */
	public static void pay(final Activity activity,final Handler mHandler,String orderId,String subject,String body,float amount,String notifyUrl) {
		try {
			String info = getNewOrderInfo(orderId, subject, body, amount, notifyUrl);
			String sign = Rsa.sign(info, Keys.PRIVATE);
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();
			Log.i(TAG, "start pay");
			// start the pay.
			Log.i(TAG, "info = " + info);

			final String orderInfo = info;
			new Thread() {
				public void run() {
					AliPay alipay = new AliPay(activity, mHandler);

					// 设置为沙箱模式，不设置默认为线上环境
					// alipay.setSandBox(true);

					String result = alipay.pay(orderInfo);

					Log.i(TAG, "result = " + result);
					Message msg = new Message();
					msg.what = RQF_PAY;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			}.start();

		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(activity, ex.toString(), Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 获取订单信息
	 * @param orderId 订单号
	 * @param subject 标题,显示在支付宝的包体
	 * @param body 主题内容
	 * @param amount 支付的价钱
	 * @param notifyUrl 回调地址
	 * @return
	 */
	private static String getNewOrderInfo(String orderId,String subject,String body,float amount,String notifyUrl) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(orderId);
		sb.append("\"&subject=\"");
		sb.append(subject);
		sb.append("\"&body=\"");
		sb.append(body);
		sb.append("\"&total_fee=\"");
		sb.append(amount);
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		sb.append(URLEncoder.encode(notifyUrl));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
//		sb.append("\"&return_url=\"");
//		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}
	
	private static String getSignType() {
		return "sign_type=\"RSA\"";
	}
}
