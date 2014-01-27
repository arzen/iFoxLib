package com.arzen.iFoxLib.pay;

import com.arzen.iFoxLib.api.HttpIfoxApi;
import com.arzen.iFoxLib.api.HttpSetting;
import com.arzen.iFoxLib.bean.Order;
import com.arzen.iFoxLib.setting.KeyConstants;
import com.arzen.iFoxLib.utils.MsgUtil;
import com.encore.libs.http.HttpConnectManager;
import com.encore.libs.http.OnRequestListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public abstract class IFoxPay {
	/**
	 * 支付方式 微派 (已关闭)
	 */
	@Deprecated
	public static final int PAY_TYPE_WIIPAY = 1;
	/**
	 * 支付宝
	 */
	public static final int PAY_TYPE_ALIPAY = 2;
	/**
	 * 银联
	 */
	public static final int PAY_TYPE_UNIONPAY = 3;
	/**
	 * 充值卡
	 */
	public static final int PAY_TYPE_PREPAIDCARD = 4;

	/**
	 * 游戏id
	 */
	private String gid;
	/**
	 * 渠道id
	 */
	private String cid;
	/**
	 * 登陆后的token
	 */
	private String token;
	/**
	 * 道具号
	 */
	private int pid;
	/**
	 * 支付价钱
	 */
	private float amount;
	/**
	 * 需要保存到服务器的字符串
	 */
	private String extra;
	/**
	 * 游戏申请的key
	 */
	private String clientId;
	/**
	 * 游戏申请的secret
	 */
	private String clientSecret;

	/**
	 * 订单
	 */
	private Order order;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public Handler mHandler = new Handler(Looper.getMainLooper());

	/**
	 * 支付需要用到的动作
	 */
	public abstract void pay(Activity activity);

	// 订单请求出错后重试次数
	private int mReTryCreateOrderCount = 4;
	// 是否正在处理支付动作
	private boolean mIsDisposePayAction = false;
	// 进度dialog
	public ProgressDialog mProgressDialog;
	// 当前支付时间,用来控制取消支付
	private long mCurrentPayTime;
	// 回调
	private OnCreateOrderCallBack mOnCreateOrderCallBack;

	/**
	 * 创建订单
	 */
	public void createOrder(final Activity context, final int payType, OnCreateOrderCallBack onCreateOrderCallBack) {
		if (mIsDisposePayAction) {
			return;
		}
		mOnCreateOrderCallBack = onCreateOrderCallBack;
		// 改变当前支付动作状态,表示正在处理支付
		mIsDisposePayAction = true;
		// 重试次数减少
		mReTryCreateOrderCount--;
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				// 设置当前支付开始时间
				mCurrentPayTime = System.currentTimeMillis();

				if (mProgressDialog == null || !mProgressDialog.isShowing()) {
					mProgressDialog = ProgressDialog.show(context, "正在处理订单", "正在处理订单,请不要退出当前操作!", true, true);
					// mProgressDialog.show();
					mProgressDialog.setOnCancelListener(new OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {
							// TODO Auto-generated method stub
							mIsDisposePayAction = false;
							mCurrentPayTime = System.currentTimeMillis();
							Toast.makeText(context, "操作取消", Toast.LENGTH_LONG).show();
						}
					});
				}
				HttpIfoxApi.createOrder(context, gid, cid, token, pid, amount, payType, extra, clientId, clientSecret, new OnCreateOrderListener(context, payType, mCurrentPayTime));
			}
		});
	}

	/**
	 * 创建订单回调
	 */
	private class OnCreateOrderListener implements OnRequestListener {

		private int mPayType;
		private long mCreateTime = 0;
		private Activity mContext;

		public OnCreateOrderListener(Activity context, final int payType, long createTime) {
			mContext = context;
			mPayType = payType;
			mCreateTime = new Long(createTime);
		}

		@Override
		public void onResponse(final String url, final int state, final Object result, final int type) {
			// TODO Auto-generated method stub
			// 判断是否取消了当前的创建订单动作
			if (mCreateTime != mCurrentPayTime) {
				return;
			}
			mIsDisposePayAction = false;
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					if (state == HttpConnectManager.STATE_SUC && result != null && result instanceof Order) {
						Order order = (Order) result;
						if (order.getCode() == HttpSetting.RESULT_CODE_OK) { // 请求成功
							if (mProgressDialog != null && mProgressDialog.isShowing() && mPayType != PAY_TYPE_PREPAIDCARD) {
								mProgressDialog.dismiss();
							}
							if (mOnCreateOrderCallBack != null) {
								setOrder(order);
								mOnCreateOrderCallBack.onResult(order);
							}
						} else {
							String msg = "创建订单失败:" + order.getMsg() + ",正在重试请稍后...";
							// 处理失败
							createFailDispose(mContext, msg, mPayType);
						}
					} else if (state == HttpConnectManager.STATE_TIME_OUT) { // 请求超时
						// 重试创建订单
						createFailDispose(mContext, "请求超时,正在重试请稍后...", mPayType);
					} else {
						// 重试创建订单
						createFailDispose(mContext, "创建订单失败,正在重试请稍后...", mPayType);
					}
					// 当前重试次数用完,状态还是失败,判断当前,创建订单失败,关闭进度条
					if (mReTryCreateOrderCount == 0 && mProgressDialog != null && mProgressDialog.isShowing()) {
						MsgUtil.msg("创建订单失败!", mContext);
						mProgressDialog.dismiss();
					}
				}
			});
		}
	};

	/**
	 * 创建订单失败处理
	 */
	private void createFailDispose(Activity context, String msg, int payType) {
		MsgUtil.msg("创建订单失败:" + msg + ",正在重试请稍后...", context);
		// 重试创建订单
		createOrder(context, payType, mOnCreateOrderCallBack);
	}

	/**
	 * 发送支付失败回调
	 */
	public static void sendPayFailResultReceiver(Activity activity, String result, String msg) {

		Bundle bundle = new Bundle();
		bundle.putString(KeyConstants.INTENT_KEY_RESULT, result);
		bundle.putString(KeyConstants.INTENT_KEY_MSG, msg);
		// 回调广播
		sendResultBroadcast(activity, bundle, KeyConstants.RECEIVER_ACTION_PAY);
	}

	/**
	 * 发送成功支付支付回调
	 */
	public static void sendPayResultReceiver(Activity activity, String orderId, int pid, float amount, String result, String msg) {

		// 回调内容
		Bundle bundle = new Bundle();
		bundle.putString(KeyConstants.INTENT_KEY_RESULT, result);
		bundle.putString(KeyConstants.INTENT_KEY_MSG, msg);
		bundle.putInt(KeyConstants.INTENT_DATA_KEY_PID, pid);
		bundle.putFloat(KeyConstants.INTENT_DATA_KEY_AMOUNT, amount);
		bundle.putString(KeyConstants.INTENT_DATA_KEY_ORDERID, orderId);

		sendResultBroadcast(activity, bundle, KeyConstants.RECEIVER_ACTION_PAY);
	}

	/**
	 * 发送结果回调广播
	 * 
	 * @param bundle
	 *            需要回调的内容
	 * @param resultAction
	 *            需要回调处理的acion， KeyConstants.RECEIVER_ACTION_PAY or
	 *            KeyConstants.RECEIVER_ACTION_LOGIN
	 */
	public static void sendResultBroadcast(Activity activity, Bundle bundle, String resultAction) {
		if (activity != null && bundle != null && resultAction != null) {
			bundle.putString(KeyConstants.RECEIVER_KEY_DISPOSE_ACTION, resultAction);

			Intent intent = new Intent(KeyConstants.RECEIVER_RESULT_ACTION);
			intent.putExtras(bundle);
			activity.sendBroadcast(intent);
		}
	}

	public interface OnCreateOrderCallBack {
		public void onResult(Order order);
	}
}
