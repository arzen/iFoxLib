package com.arzen.iFoxLib.pay;

import com.arzen.iFoxLib.bean.Order;
import com.arzen.iFoxLib.pay.ali.AliPayUtil;
import com.arzen.iFoxLib.pay.ali.Result;
import com.arzen.iFoxLib.setting.KeyConstants;
import com.baidu.mobstat.StatService;
import com.encore.libs.utils.Log;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * 支付宝支付类
 * 
 * @author Encore.liang
 * 
 */
public class AliPay extends BasePay {
	/**
	 * 当前是否类型,支付宝
	 */
	private int mCurrentPayType = PAY_TYPE_ALIPAY;

	private Activity mActivity;
	
	/**
	 * 回调地址,支付宝需要的参数
	 */
	private String notifyUrl;

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	@Override
	public void pay(Activity activity) {
		// TODO Auto-generated method stub
		mActivity = activity;
		StatService.onEvent(mActivity.getApplicationContext(), "PAY_ALIPAY", "");
		createOrder(mActivity, mCurrentPayType, new OnCreateOrderCallBack() {

			@Override
			public void onResult(Order order) {
				// TODO Auto-generated method stub
				if (order != null && order.getData() != null) {
					toAliPay(order.getData().getOrderid());
				}
			}
		});
	}

	/**
	 * 支付宝支付
	 * 
	 * @param orderId
	 * @param amount
	 */
	public void toAliPay(String orderId) {
		String notifyUrl = getNotifyUrl();
		float amount = getAmount();
		AliPayUtil.pay(mActivity, mUIHandler, orderId, "充值游戏币", "道具", amount, notifyUrl);
	}
	
	public Handler mUIHandler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case AliPayUtil.RQF_PAY:
				aliPayResultDispose((String) msg.obj);
				break;
			}
		};
	};
	
	/**
	 * 支付宝支付结果处理
	 */
	public void aliPayResultDispose(String re) {
		Result result = new Result(re);
		result.parseResult();
		Log.d("pay", "disposeResult:" + result.getResult() + " resultStatus:" + result.resultStatus);
		if (result.resultStatus != null && result.resultStatus.equals("success")) { // 判断是否支付成功
			if (getOrder() != null) {
				Order order = getOrder();
				StatService.onEvent(mActivity.getApplicationContext(), "PAY_ALIPAY_SUCCESS", "");
				int pid = getPid();
				float amount = getAmount();
				String orderId = order.getData().getOrderid();
				sendPayResultReceiver(mActivity, orderId, pid, amount, KeyConstants.INTENT_KEY_SUCCESS, "支付成功");
			}
		} else {
			StatService.onEvent(mActivity.getApplicationContext(), "PAY_ALIPAY_FAIL", "");
			Toast.makeText(mActivity, result.getResult(), Toast.LENGTH_SHORT).show();
		}
	}

}
