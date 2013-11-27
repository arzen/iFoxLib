package com.arzen.iFoxLib.pay;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

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

	public WayPay(Bundle bundle) {
		mBundle = bundle;
	}

	public void toPay(final Activity activity, String payCode) {
		if (mBxPay == null)
			mBxPay = new BXPay(activity);
		Map<String, String> devPrivate = new HashMap<String, String>();
		devPrivate.put("开发者要传的KEY值", "开发者要传的VALUE值");
		mBxPay.setDevPrivate(devPrivate);
		mBxPay.pay(payCode, new BXPay.PayCallback() {

			@Override
			public void pay(Map<String, String> resultInfo) {
				String result = resultInfo.get("result"); // 支付结果
				String payCode = resultInfo.get("payCode");// 计费点编号
				String price = resultInfo.get("price");// 价格
				String logCode = resultInfo.get("logCode");// 定得编号
				String showMsg = resultInfo.get("showMsg");// 支付结果提示
				String payType = resultInfo.get("payType");// 支付方式

				Log.d(TAG, "payResult:" + result);

				Log.d(TAG, "payType:" + payType + " price:" + price + " showMsg:" + showMsg);

				// new
				// AlertDialog.Builder(activity).setTitle("支付结果返回：").setMessage("返回结果："
				// + resultInfo.toString()).setPositiveButton("确定",
				// null).show();

			}
		});
	}
}
