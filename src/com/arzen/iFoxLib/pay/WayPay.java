package com.arzen.iFoxLib.pay;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.arzen.iFoxLib.fragment.PayFragment;
import com.arzen.iFoxLib.setting.KeyConstants;
import com.arzen.iFoxLib.utils.MsgUtil;
import com.bx.pay.BXPay;
import com.encore.libs.utils.Log;

/**
 * 微派支付
 * 
 * @author Encore.liang
 * 
 */
public class WayPay {

	// tag
	public static final String TAG = "WayPay";
	/**
	 * 支付成功
	 */
	public static final String SUCCESS = "success";
	/**
	 * 表示在付费周期里已经成功付费，已经是付费用户。
	 */
	public static final String PASS = "pass";
	/**
	 * 表示计费点暂时停止。
	 */
	public static final String PAUSE = "pause";
	/**
	 * 本地联网失败
	 */
	public static final String ERROR = "error";
	/**
	 * 支付失败。
	 */
	public static final String FAIL = "fail";
	/**
	 * 表示用户取消支付。
	 */
	public static final String CANCEL = "cancel";

	public Bundle mBundle;
	private BXPay mBxPay;
	// 支付页
	private PayFragment mPayFragment;

	public WayPay(Bundle bundle) {
		mBundle = bundle;
	}

	/**
	 * 跳转支付
	 * 
	 * @param activity
	 * @param payCode
	 */
	public void toPay(final PayFragment payFragment, final Activity activity, String payCode) {

		mPayFragment = payFragment;

		if (mBxPay == null)
			mBxPay = new BXPay(activity);
		Map<String, String> devPrivate = new HashMap<String, String>();
		devPrivate.put("开发者要传的KEY值", "开发者要传的VALUE值");
		mBxPay.setDevPrivate(devPrivate);
		mBxPay.pay(payCode, new BXPay.PayCallback() {

			@Override
			public void pay(Map<String, String> resultInfo) {
				String result = resultInfo.get(KeyConstants.INTENT_KEY_RESULT); // 支付结果
				String payCode = resultInfo.get("payCode");// 计费点编号
				String price = resultInfo.get("price");// 价格
				String logCode = resultInfo.get("logCode");// 定得编号
				String showMsg = resultInfo.get("showMsg");// 支付结果提示
				String payType = resultInfo.get("payType");// 支付方式

				Log.d(TAG, "payResult:" + result);

				Log.d(TAG, "payType:" + payType + " price:" + price + " showMsg:" + showMsg);

				// 处理支付结果
				disposePayResult(activity, result, showMsg);

			}
		});
	}

	/**
	 * 处理支付结果
	 * 
	 * @param result
	 */
	public void disposePayResult(Activity activity, String result, String msg) {
		//如果支付成功
		if (result.equals(SUCCESS)) {
			if (mPayFragment != null) {
				mPayFragment.createOrder(KeyConstants.PAY_TYPE_WIIPAY, result, msg); // 创建订单
			}
			// sendPayResultReceiver(activity, result, msg);
		} else if (result.equals(FAIL)) { // 支付失败
			if (mPayFragment != null) 
				mPayFragment.sendPayFailResultReceiver(activity, result, msg);
		}
	}

}
