package com.arzen.iFoxLib.pay;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.arzen.iFoxLib.bean.Order;
import com.arzen.iFoxLib.setting.KeyConstants;
import com.baidu.mobstat.StatService;

/**
 * 银联支付
 * 
 * @author Encore.liang
 * 
 */
public class UnionPay extends BasePay {
	private int mCurrentPayType = PAY_TYPE_UNIONPAY;

	private Activity mActivity;

	@Override
	public void pay(Activity activity) {
		// TODO Auto-generated method stub
		mActivity = activity;
		createOrder(mActivity, mCurrentPayType, new OnCreateOrderCallBack() {

			@Override
			public void onResult(Order order) {
				// TODO Auto-generated method stub
				if (order != null && order.getData() != null) {
					toUnionpay(order.getData().getTn());
				}
			}
		});
	}

	/**
	 * 银联支付
	 * 
	 * @param tn
	 *            银联需要用到的支付流水号
	 */
	public void toUnionpay(String tn) {
		// 发送广播调用支付
		Intent intent = new Intent(KeyConstants.RECEIVER_PAY_START_ACTION);
		intent.putExtra(KeyConstants.INTENT_DATA_KEY_PAY_TYPE, mCurrentPayType);
		intent.putExtra(KeyConstants.INTENT_DATA_KEY_PAY_TN, tn);
		mActivity.sendBroadcast(intent);
	}

	/**
	 * 银联支付广播
	 */
	public BroadcastReceiver mPayUnionResultBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String result = intent.getStringExtra(KeyConstants.INTENT_KEY_RESULT);
			if (result.equalsIgnoreCase("success")) {
				StatService.onEvent(mActivity.getApplicationContext(), "PAY_UNION_SUCCESS", "");
				Order order = getOrder();
				if (order != null) {
					int pid = getPid();
					String orderId = order.getData().getOrderid();
					float amount = getAmount();
					sendPayResultReceiver(mActivity, orderId, pid, amount, KeyConstants.INTENT_KEY_SUCCESS, "支付成功");
				}
			} else if (result.equalsIgnoreCase("fail")) {
				StatService.onEvent(mActivity.getApplicationContext(), "PAY_UNION_FAIL", "");

				sendPayFailResultReceiver(mActivity, KeyConstants.INTENT_KEY_FAIL, "支付失败");
			} else if (result.equalsIgnoreCase("cancel")) {

				// Bundle bundle = new Bundle();
				// bundle.putString(KeyConstants.INTENT_KEY_RESULT,
				// KeyConstants.INTENT_KEY_CANCEL);
				// // 回调取消
				// sendResultBroadcast(getActivity(), bundle,
				// KeyConstants.RECEIVER_ACTION_PAY);
			}
		}

	};

}
