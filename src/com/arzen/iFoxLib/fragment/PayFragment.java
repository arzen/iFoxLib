package com.arzen.iFoxLib.fragment;

import java.util.Map;

import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.arzen.iFoxLib.MsgUtil;
import com.arzen.iFoxLib.R;
import com.arzen.iFoxLib.api.HttpIfoxApi;
import com.arzen.iFoxLib.api.HttpSetting;
import com.arzen.iFoxLib.bean.Order;
import com.arzen.iFoxLib.bean.PayList;
import com.arzen.iFoxLib.bean.PayList.Data;
import com.arzen.iFoxLib.pay.WayPay;
import com.arzen.iFoxLib.setting.KeyConstants;
import com.bx.pay.ApkUpdate;
import com.bx.pay.ApkUpdate.ApkUpdateCallback;
import com.encore.libs.http.HttpConnectManager;
import com.encore.libs.http.OnRequestListener;
import com.encore.libs.utils.Log;

public class PayFragment extends BaseFragment {

	public static final String TAG = "PayFragment";

	private Button mBtnWiipay;
	private Button mBtnAlipay;
	private Button mBtnUnionpay;

	// 微派支付工具类
	private WayPay mWayPay;
	// 主程序传递过来的数据
	private Bundle mBundle;
	// 支付列表对象
	private PayList mPayList = null;
	// 主内容view
	private View mViewContent;
	// 进度dialog
	private ProgressDialog mProgressDialog;
	// 是否正在创建订单
	private boolean mIsCreateingOrder = false;
	// 订单请求出错后重试次数
	public int mReTryCreateOrderCount = 4;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View payView = inflater.inflate(R.layout.fragment_pay, null);
		initUI(payView);
		return payView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mBundle = getArguments();

		initData(mPayList);
	}

	private Handler mHandler = new Handler() {
	};

	/**
	 * 初始化ui
	 * 
	 * @param view
	 */
	public void initUI(View view) {
		mViewContent = view.findViewById(R.id.viewContent);

		mBtnWiipay = (Button) view.findViewById(R.id.btnWiipay);
		mBtnAlipay = (Button) view.findViewById(R.id.btnAlipay);
		mBtnUnionpay = (Button) view.findViewById(R.id.btnUnionpay);

		mBtnWiipay.setOnClickListener(mOnClickListener);
		mBtnAlipay.setOnClickListener(mOnClickListener);
		mBtnUnionpay.setOnClickListener(mOnClickListener);

		setOnRefreshClickListener(mOnRefreshClickListener);
	}

	/**
	 * 刷新按钮
	 */
	private OnRefreshClickListener mOnRefreshClickListener = new OnRefreshClickListener() {

		@Override
		public void onClick() {
			initData(null); // 请求数据
		}
	};

	/**
	 * 初始化数据
	 * 
	 * @param payList
	 */
	public void initData(PayList payList) {
		if (payList != null) {
			mPayList = payList;
			initPayList(payList);
		} else {
			// 判断是否有网络的情况
			if (setErrorVisibility(getView(), mViewContent, null)) {
				// 显示loading view
				setLoadingViewVisibility(true, getView(), mViewContent);
				// 请求
				requestPayList();
			}
		}
	}

	/**
	 * 设置显示支付列表
	 * 
	 * @param payList
	 */
	public void initPayList(PayList payList) {
		mBtnWiipay.setVisibility(View.GONE);
		mBtnAlipay.setVisibility(View.GONE);
		mBtnUnionpay.setVisibility(View.GONE);
		// 微派
		Data data = payList.getData();
		if (data != null) {
			String wiipay = data.getList().getWiipay();
			if (wiipay != null && !wiipay.equals("")) {
				mBtnWiipay.setVisibility(View.VISIBLE);
			}
			// 支付宝
			String alipay = data.getList().getAlipay();
			if (alipay != null && !alipay.equals("")) {
				mBtnAlipay.setVisibility(View.VISIBLE);
			}

			// 银联
			String unionpay = data.getList().getUnionpay();
			if (unionpay != null && !unionpay.equals("")) {
				mBtnUnionpay.setVisibility(View.VISIBLE);
			}
		}

	}

	/**
	 * 请求支付列表
	 */
	public void requestPayList() {
		if (mBundle == null) {
			// 隐藏loadding view
			setLoadingViewVisibility(false, getView(), mViewContent);
			// 显示错误view
			setErrorVisibility(getView(), mViewContent, "没有传参数!");
			return;
		}
		String gid = mBundle.getString(KeyConstants.INTENT_DATA_KEY_GID);
		String cid = mBundle.getString(KeyConstants.INTENT_DATA_KEY_CID);
		String token = mBundle.getString(KeyConstants.INTENT_DATA_KEY_TOKEN);
		// 请求
		HttpIfoxApi.requestPayList(getActivity(), gid, cid, token, mOnPayListRequestListener);
	}

	public OnRequestListener mOnPayListRequestListener = new OnRequestListener() {

		@Override
		public void onResponse(final String url, final int state, final Object result, final int type) {
			// TODO Auto-generated method stub
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (!isAdded()) // fragment 已退出,返回
					{
						return;
					}
					// 隐藏loadingView,显示主体内容listView
					setLoadingViewVisibility(false, getView(), mViewContent);

					if (state == HttpConnectManager.STATE_SUC && result != null && result instanceof PayList) {
						PayList payList = (PayList) result;
						if (payList.getCode() == HttpSetting.RESULT_CODE_OK) { // 请求成功
							initData(payList);
						} else {
							setErrorVisibility(getView(), mViewContent, getString(R.string.request_fail));
						}
					} else if (state == HttpConnectManager.STATE_TIME_OUT) { // 请求超时
						setErrorVisibility(getView(), mViewContent, getString(R.string.time_out));
					} else { // 请求失败
						setErrorVisibility(getView(), mViewContent, getString(R.string.request_fail));
					}
				}
			});
		}
	};

	public OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case R.id.btnWiipay:
				toWayPay();
				break;
			case R.id.btnAlipay:

				break;
			case R.id.btnUnionpay:

				break;
			}
		}
	};

	/**
	 * 跳转微派支付
	 */
	public void toWayPay() {
		// 检测微派支付有没更新
		new ApkUpdate(getActivity(), new ApkUpdateCallback() {
			@Override
			public void launch(Map<String, String> arg0) {
				// TODO Auto-generated method stub
				mReTryCreateOrderCount = 4;
				if (mWayPay == null) {
					mWayPay = new WayPay(mBundle);
				}
				Log.d("PayFragment", getActivity().toString());
				mWayPay.toPay(PayFragment.this, getActivity(), "0001");
			}
		});
	}

	/**
	 * 支付成功后,创建订单
	 * 
	 * @param payType
	 *            支付方式 (微派:KeyConstants.PAY_TYPE_WIIPAY) (支付宝:
	 *            KeyConstants.PAY_TYPE_ALIPAY)(银联:
	 *            KeyConstants.PAY_TYPE_UNIONPAY )
	 * @param result
	 *            支付结果
	 * @param msg
	 *            支付提示
	 */
	public void createOrder(final int payType, final String result, final String msg) {
		if (mIsCreateingOrder) {
			return;
		}

		mHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mReTryCreateOrderCount == 0) {
					MsgUtil.msg("创建订单失败,请检查网络或联系我们!", getActivity());
					return;
				}
				// 改变重试次数
				mReTryCreateOrderCount--;

				if (mBundle == null) {
					MsgUtil.msg("支付数据为空!", getActivity());
					return;
				}
				mIsCreateingOrder = true;
				String gid = mBundle.getString(KeyConstants.INTENT_DATA_KEY_GID);
				String cid = mBundle.getString(KeyConstants.INTENT_DATA_KEY_CID);
				String token = mBundle.getString(KeyConstants.INTENT_DATA_KEY_TOKEN);
				int pid = mBundle.getInt(KeyConstants.INTENT_DATA_KEY_PID);
				float amount = mBundle.getFloat(KeyConstants.INTENT_DATA_KEY_AMOUNT);
				String extra = mBundle.getString(KeyConstants.INTENT_DATA_KEY_EXTRA);
				//创建订单
				Log.d(TAG, "createOrder -> gid:" + gid + " cid:" + cid + " token:" + token + " pid:" + pid + " amount:" + amount + " extra:" + extra + " payType:" + payType);
				
				if(mProgressDialog == null || !mProgressDialog.isShowing()){
					mProgressDialog = ProgressDialog.show(getActivity(), "正在处理订单", "正在处理订单,请不要退出当前操作!", true, true);
//					mProgressDialog.show();
				}

				HttpIfoxApi.createOrder(getActivity(), gid, cid, token, pid, amount, payType, extra, new OnCreateOrderListener(payType, result, msg));
			}
		});
	}

	/**
	 * 创建订单回调
	 */
	public class OnCreateOrderListener implements OnRequestListener {

		private int mPayType;
		private String mResult;
		private String mMsg;

		public OnCreateOrderListener(final int payType, String result, String msg) {
			mPayType = payType;
			mResult = result;
			mMsg = msg;
		}

		@Override
		public void onResponse(final String url, final int state, final Object result, final int typem) {
			// TODO Auto-generated method stub
			mIsCreateingOrder = false;
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (!isAdded()) { // fragment 已退出
						return;
					}
					if (state == HttpConnectManager.STATE_SUC && result != null && result instanceof Order) {
						Order order = (Order) result;
						if (order.getCode() == HttpSetting.RESULT_CODE_OK) { // 请求成功
							if (mProgressDialog != null && mProgressDialog.isShowing()) {
								mProgressDialog.dismiss();
							}
							
							int pid = mBundle.getInt(KeyConstants.INTENT_DATA_KEY_PID);
							float amount = mBundle.getFloat(KeyConstants.INTENT_DATA_KEY_AMOUNT);
							//回调成功状态
							sendPayResultReceiver(getActivity(), order.getData().getOrderid(), pid, amount, mResult, mMsg);
							
						} else {
							MsgUtil.msg("创建订单失败:" + order.getMsg() + ",正在重试请稍后...", getActivity());
							// 重试创建订单
							createOrder(mPayType, mResult, mMsg);
						}
					} else if (state == HttpConnectManager.STATE_TIME_OUT) { // 请求超时
						MsgUtil.msg("请求超时,正在重试请稍后...", getActivity());
						// 重试创建订单
						createOrder(mPayType, mResult, mMsg);
					} else { // 请求失败
						MsgUtil.msg("创建订单失败,正在重试请稍后...", getActivity());
						// 重试创建订单
						createOrder(mPayType, mResult, mMsg);
					}
				}
			});
		}
	};
	
	
	/**
	 * 发送支付回调
	 */
	public void sendPayResultReceiver(Activity activity, String result, String msg) {
		
		Bundle bundle = new Bundle();
		bundle.putString(KeyConstants.INTENT_KEY_PAY_RESULT, result);
		bundle.putString(KeyConstants.INTENT_KEY_PAY_MSG, msg);
	
		
		Intent intent = new Intent(KeyConstants.PAY_RESULT_RECEIVER_ACTION);
		intent.putExtras(bundle);
		activity.sendBroadcast(intent);
	}
	
	/**
	 * 发送成功支付支付回调
	 */
	public void sendPayResultReceiver(Activity activity,String orderId, int pid,float amount,String result, String msg) {
		
		Bundle bundle = new Bundle();
		bundle.putString(KeyConstants.INTENT_KEY_PAY_RESULT, result);
		bundle.putString(KeyConstants.INTENT_KEY_PAY_MSG, msg);
		bundle.putInt(KeyConstants.INTENT_DATA_KEY_PID, pid);
		bundle.putFloat(KeyConstants.INTENT_DATA_KEY_AMOUNT, amount);
		bundle.putString(KeyConstants.INTENT_DATA_KEY_ORDERID, orderId);
		
		Intent intent = new Intent(KeyConstants.PAY_RESULT_RECEIVER_ACTION);
		intent.putExtras(bundle);
		activity.sendBroadcast(intent);
	}
}
