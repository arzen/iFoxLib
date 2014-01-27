package com.arzen.iFoxLib.pay;

import com.arzen.iFoxLib.api.HttpIfoxApi;
import com.arzen.iFoxLib.api.HttpSetting;
import com.arzen.iFoxLib.bean.Order;
import com.arzen.iFoxLib.bean.PrepaidCard;
import com.arzen.iFoxLib.pay.IFoxPay.OnCreateOrderCallBack;
import com.arzen.iFoxLib.setting.KeyConstants;
import com.arzen.iFoxLib.utils.CommonUtil;
import com.arzen.iFoxLib.utils.MsgUtil;
import com.baidu.mobstat.StatService;
import com.encore.libs.http.HttpConnectManager;
import com.encore.libs.http.OnRequestListener;

import android.app.Activity;
import android.content.Context;

/**
 * 充值卡支付
 * 
 * @author Encore.liang
 * 
 */
public class PrepaidCardPay extends IFoxPay {
	private int mCurrentPayType = PAY_TYPE_PREPAIDCARD;

	private String card;
	private String password;

	private Activity mActivity;

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void pay(Activity activity) {
		// TODO Auto-generated method stub
		mActivity = activity;
		createOrder(mActivity, mCurrentPayType, new OnCreateOrderCallBack() {

			@Override
			public void onResult(Order order) {
				// TODO Auto-generated method stub
				if (order != null && order.getData() != null) {
					toPrepaidCardPay(order.getData().getOrderid());
				}
			}
		});
	}

	/**
	 * 充值卡请求
	 * 
	 * @param orderId
	 */
	public void toPrepaidCardPay(final String orderId) {
		HttpIfoxApi.requestPrepaidCardPay(mActivity.getApplicationContext(), getAmount(), getCard(), getPassword(), orderId, getExtra(), new OnRequestListener() {

			@Override
			public void onResponse(final String url, final int state, final Object result, final int type) {
				// TODO Auto-generated method stub
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (mProgressDialog != null && mProgressDialog.isShowing()) {
							mProgressDialog.dismiss();
						}
						if (state == HttpConnectManager.STATE_SUC && result != null && result instanceof PrepaidCard) {
							PrepaidCard baseBean = (PrepaidCard) result;
							if (baseBean.getCode() == HttpSetting.RESULT_CODE_OK) { // 请求成功
								if (baseBean.getData().getMsg().equals("200")) {
									MsgUtil.msg("充值成功", mActivity);
									sendPayResultReceiver(mActivity, orderId, 0, getAmount(), KeyConstants.INTENT_KEY_SUCCESS, "");
									// 统计
									StatService.onEvent(mActivity.getApplicationContext(), "PAY_CARD_SUCCESS", "");
								} else {
									MsgUtil.msg("充值失败:" + CommonUtil.getPrepaidCardPayMsg(baseBean.getData().getMsg()), mActivity);
									// 统计
									StatService.onEvent(mActivity.getApplicationContext(), "PAY_CARD_FAIL", "");
								}
							} else {
								MsgUtil.msg("充值失败:" + baseBean.getMsg(), mActivity);
							}
						} else if (state == HttpConnectManager.STATE_TIME_OUT) { // 请求超时
							MsgUtil.msg("请求超时,正在重试请稍后...", mActivity);
						} else { // 请求失败
							MsgUtil.msg("请求失败，请重试", mActivity);
						}
					}
				});
			}
		});
	}

}
