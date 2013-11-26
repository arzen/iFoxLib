package com.arzen.iFoxLib.pay;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

import com.bx.pay.BXPay;

public class WayPay {

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
				new AlertDialog.Builder(activity).setTitle("支付结果返回：")
				.setMessage("返回结果：" + resultInfo.toString())
				.setPositiveButton("确定", null).show();

			}
		});
	}
}
