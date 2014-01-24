package com.arzen.iFoxLib.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arzen.iFoxLib.R;
import com.arzen.iFoxLib.api.HttpIfoxApi;
import com.arzen.iFoxLib.api.HttpSetting;
import com.arzen.iFoxLib.bean.Order;
import com.arzen.iFoxLib.bean.PayList;
import com.arzen.iFoxLib.bean.PayList.Data;
import com.arzen.iFoxLib.bean.PrepaidCard;
import com.arzen.iFoxLib.pay.AliPay;
import com.arzen.iFoxLib.pay.BasePay;
import com.arzen.iFoxLib.pay.PrepaidCardPay;
import com.arzen.iFoxLib.pay.UnionPay;
import com.arzen.iFoxLib.pay.ali.AliPayUtil;
import com.arzen.iFoxLib.pay.ali.Result;
import com.arzen.iFoxLib.setting.KeyConstants;
import com.arzen.iFoxLib.setting.UserSetting;
import com.arzen.iFoxLib.utils.CommonUtil;
import com.arzen.iFoxLib.utils.MsgUtil;
import com.baidu.mobstat.StatService;
import com.encore.libs.http.HttpConnectManager;
import com.encore.libs.http.OnRequestListener;
import com.encore.libs.utils.Log;

public class PayFragment extends BaseFragment {

	public static final String TAG = "PayFragment";

	/**
	 * 当前支付时间,记录是否取消当前创建订单等操作
	 */
	private long mCurrentPayTime = 0;
	// 微派支付
	private TextView mTvWayPay;
	// 支付宝
	private TextView mTvAlipay;
	// 银联支付
	private TextView mTvUnionpay;
	// 充值卡
	private TextView mTvPrepaidCard;
	// 支付帮助
	private TextView mTvHelp;
	// // 微派支付工具类
	// private WayPay mWayPay;
	// 主程序传递过来的数据
	private Bundle mBundle;
	// 支付列表对象
	private PayList mPayList = null;
	// 主内容view
	private View mViewContent;
	// 当前tab id
	public int mCurrentTab = -1;
	// 选中的textView
	public TextView mTvSelectedText;
	// 价格view
	public View mViewPrice;
	// 支付按钮
	public Button mBtnPay;

	private TextView mTv100;
	private TextView mTv200;
	private TextView mTv500;
	private TextView mTv1000;
	private TextView mTv50;
	private TextView mTv30;
	private TextView mTv20;
	private TextView mTv10;
	private EditText mEtCusPrice;
	// 当前选中text
	private TextView mCurrentSelectPrice;
	// 当前订单
	private Order mCurrentOrder;
	// 当前价钱
	private float mCurrentPrice;
	// 充值卡view
	private View mViewPrepaidCard;

	private EditText mEtCard;
	private EditText mEtPassword;
	private EditText mEtPrice;
	
	private BasePay mBasePay;

	/**
	 * 检查时间,检测是否需要请求
	 */
	private static long mCheckTime = 24 * 60 * 60 * 1000;
	/**
	 * 支付列表缓存地址
	 */
	public static String mPayListChachePath = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mPayListChachePath = getActivity().getCacheDir().getAbsolutePath() + "/playList.cache";

		View payView = inflater.inflate(R.layout.fragment_pay, null);
		initUI(payView);
		return payView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mBundle = getArguments();

		Log.d(TAG, "onActivityCreated() savedInstanceState is null?:" + (savedInstanceState == null));
		Log.d(TAG, "mPayList is null?:" + (mPayList == null));

		initData(mPayList);
		/*
		 * 获取是否固定支付金额
		 */
		Float price = mBundle.getFloat(KeyConstants.INTENT_DATA_KEY_AMOUNT);
		if (price != null && price > 0) { // 固定支付金额关闭金钱选择项
			mEtCusPrice.setText(price + "");
			mEtCusPrice.setEnabled(false);

			mEtPrice.setText(price + "");
			mEtPrice.setEnabled(false);

			mTv100.setEnabled(false);
			mTv200.setEnabled(false);
			mTv500.setEnabled(false);
			mTv1000.setEnabled(false);
			mTv50.setEnabled(false);
			mTv30.setEnabled(false);
			mTv20.setEnabled(false);
			mTv10.setEnabled(false);
		}
	}

	/**
	 * 初始化ui
	 * 
	 * @param view
	 */
	public void initUI(View view) {
		mViewPrepaidCard = view.findViewById(R.id.viewPrepaidCard);
		mViewContent = view.findViewById(R.id.viewContent);
		mTvWayPay = (TextView) view.findViewById(R.id.tvWayPay);
		mTvAlipay = (TextView) view.findViewById(R.id.tvAlipay);
		mTvUnionpay = (TextView) view.findViewById(R.id.tvUnionpay);
		mTvHelp = (TextView) view.findViewById(R.id.tvHelp);
		mTvSelectedText = (TextView) view.findViewById(R.id.tvSelectedText);
		mViewPrice = view.findViewById(R.id.viewPrice);
		mTvPrepaidCard = (TextView) view.findViewById(R.id.tvPrepaidCard);
		mEtCard = (EditText) view.findViewById(R.id.etCard);
		mEtPassword = (EditText) view.findViewById(R.id.etPassword);
		mEtPrice = (EditText) view.findViewById(R.id.etPrice);
		mBtnPay = (Button) view.findViewById(R.id.btnPay);
		mBtnPay.setVisibility(View.GONE);

		mTvWayPay.setOnClickListener(mOnClickListener);
		mTvAlipay.setOnClickListener(mOnClickListener);
		mTvUnionpay.setOnClickListener(mOnClickListener);
		mTvPrepaidCard.setOnClickListener(mOnClickListener);
		mTvHelp.setOnClickListener(mOnClickListener);
		mBtnPay.setOnClickListener(mPayClickListener);
		setOnRefreshClickListener(mOnRefreshClickListener);

		mTv100 = (TextView) view.findViewById(R.id.tv100);
		mTv200 = (TextView) view.findViewById(R.id.tv200);
		mTv500 = (TextView) view.findViewById(R.id.tv500);
		mTv1000 = (TextView) view.findViewById(R.id.tv1000);
		mTv50 = (TextView) view.findViewById(R.id.tv50);
		mTv30 = (TextView) view.findViewById(R.id.tv30);
		mTv20 = (TextView) view.findViewById(R.id.tv20);
		mTv10 = (TextView) view.findViewById(R.id.tv10);
		mEtCusPrice = (EditText) view.findViewById(R.id.etCusPrice);

		mTv100.setOnClickListener(mOnPriceClickListener);
		mTv200.setOnClickListener(mOnPriceClickListener);
		mTv500.setOnClickListener(mOnPriceClickListener);
		mTv1000.setOnClickListener(mOnPriceClickListener);
		mTv50.setOnClickListener(mOnPriceClickListener);
		mTv30.setOnClickListener(mOnPriceClickListener);
		mTv20.setOnClickListener(mOnPriceClickListener);
		mTv10.setOnClickListener(mOnPriceClickListener);

		mEtCusPrice.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence c, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				initPriceSelected();
				mCurrentSelectPrice = null;
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initPriceSelected() {
		mTv100.setSelected(false);
		mTv200.setSelected(false);
		mTv500.setSelected(false);
		mTv1000.setSelected(false);
		mTv50.setSelected(false);
		mTv30.setSelected(false);
		mTv20.setSelected(false);
		mTv10.setSelected(false);
	}

	public OnClickListener mOnPriceClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			mEtCusPrice.setText("");
			initPriceSelected();
			switch (view.getId()) {
			case R.id.tv100:
				mTv100.setSelected(true);
				break;
			case R.id.tv200:
				mTv200.setSelected(true);
				break;
			case R.id.tv500:
				mTv500.setSelected(true);
				break;
			case R.id.tv1000:
				mTv1000.setSelected(true);
				break;
			case R.id.tv50:
				mTv50.setSelected(true);
				break;
			case R.id.tv30:
				mTv30.setSelected(true);
				break;
			case R.id.tv20:
				mTv20.setSelected(true);
				break;
			case R.id.tv10:
				mTv10.setSelected(true);
				break;
			}
			mCurrentSelectPrice = (TextView) view;
		}
	};

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
			PayList cache = (PayList) CommonUtil.file2Object(mPayListChachePath);
			long payListTime = UserSetting.getPayListTime(getActivity()); // 得到上次检查的时间
			if (cache == null || System.currentTimeMillis() - payListTime > mCheckTime) {
				// 判断是否有网络的情况
				if (setErrorVisibility(getView(), mViewContent, null)) {
					// 显示loading view
					setLoadingViewVisibility(true, getView(), mViewContent);
					// 请求
					requestPayList();
				}
			} else {
				initData(cache);
			}
		}
	}

	/**
	 * 设置显示支付列表
	 * 
	 * @param payList
	 */
	public void initPayList(PayList payList) {

		mTvWayPay.setVisibility(View.GONE);
		mTvAlipay.setVisibility(View.GONE);
		mTvUnionpay.setVisibility(View.GONE);
		mTvHelp.setVisibility(View.VISIBLE);
		boolean isHasWayPay = false;
		View fristPayView = null;
		// 微派
		Data data = payList.getData();
		if (data != null) {
			String wiipay = data.getList().getWiipay();
			if (wiipay != null && !wiipay.equals("")) {
				mTvWayPay.setVisibility(View.VISIBLE);
				isHasWayPay = true;
				fristPayView = mTvWayPay;
			}
			// 支付宝
			String alipay = data.getList().getAlipay();
			if (alipay != null && !alipay.equals("")) {
				mTvAlipay.setVisibility(View.VISIBLE);
				if (fristPayView == null)
					fristPayView = mTvAlipay;
			}

			// 银联
			String unionpay = data.getList().getUnionpay();
			if (unionpay != null && !unionpay.equals("")) {
				mTvUnionpay.setVisibility(View.VISIBLE);
				if (fristPayView == null)
					fristPayView = mTvUnionpay;
			}

			String prepaidCard = data.getList().getPrepaidCard();
			if (prepaidCard != null && !prepaidCard.equals("")) {
				mTvPrepaidCard.setVisibility(View.VISIBLE);
				if (fristPayView == null)
					fristPayView = mTvPrepaidCard;
			}
		}

		// 设置显示的view
		if (fristPayView != null) {
			changeTab((TextView) fristPayView, fristPayView.getId());
		}
	}

	/**
	 * 改变当前tab
	 * 
	 * @param view
	 */
	public void changeTab(TextView view, int id) {
		mTvWayPay.setSelected(false);
		mTvAlipay.setSelected(false);
		mTvUnionpay.setSelected(false);
		mTvPrepaidCard.setSelected(false);
		mTvHelp.setSelected(false);

		view.setSelected(true);

		//
		mTvSelectedText.setVisibility(View.GONE);
		mViewPrice.setVisibility(View.GONE);
		mViewPrepaidCard.setVisibility(View.GONE);
		mBtnPay.setVisibility(View.VISIBLE);
		switch (id) {
		case R.id.tvWayPay:
			mTvSelectedText.setVisibility(View.VISIBLE);
			mTvSelectedText.setText("点击打开微派支付");
			break;
		case R.id.tvAlipay:
		case R.id.tvUnionpay:
			mTvSelectedText.setVisibility(View.VISIBLE);
			mViewPrice.setVisibility(View.VISIBLE);
			mTvSelectedText.setText("请选择支付面额");
			break;
		case R.id.tvPrepaidCard:
			mTvSelectedText.setVisibility(View.VISIBLE);
			mTvSelectedText.setText("充值卡充值");
			mViewPrepaidCard.setVisibility(View.VISIBLE);
			break;
		case R.id.tvHelp:
			mBtnPay.setVisibility(View.GONE);
			break;
		}
		mCurrentTab = id;

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		try {
			// getActivity().unregisterReceiver(mCreateOrderBroadcastReceiver);
			// mCreateOrderBroadcastReceiver = null;
			if(mBasePay != null && mBasePay instanceof UnionPay){
				UnionPay unionPay = (UnionPay) mBasePay;
				getActivity().unregisterReceiver(unionPay.mPayUnionResultBroadcastReceiver);
				unionPay.mPayUnionResultBroadcastReceiver = null;
			}
			
		} catch (Exception e) {
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
		String clientId = mBundle.getString(KeyConstants.INTENT_DATA_KEY_CLIENTID);
		String clientSecret = mBundle.getString(KeyConstants.INTENT_DATA_KEY_CLIENTSECRET);
		// 请求
		HttpIfoxApi.requestPayList(getActivity(), gid, cid, token, clientId, clientSecret, mOnPayListRequestListener);
	}

	public OnRequestListener mOnPayListRequestListener = new OnRequestListener() {

		@Override
		public void onResponse(final String url, final int state, final Object result, final int type) {
			// TODO Auto-generated method stub
			getMainHandler().post(new Runnable() {

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
							UserSetting.savePayListTime(getActivity().getApplicationContext(), System.currentTimeMillis());
							CommonUtil.object2File(payList, mPayListChachePath);
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
			case R.id.tvWayPay:
				break;
			case R.id.tvAlipay:
				break;
			case R.id.tvUnionpay:
				break;
			case R.id.tvHelp:
				break;
			}
			changeTab((TextView) view, view.getId());
		}
	};
	/**
	 * 支付点击
	 */
	public OnClickListener mPayClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.d(TAG, "mPayClickListener tab:" + mCurrentTab);
			if (mCurrentTab == -1) {
				return;
			}
			switch (mCurrentTab) {
			case R.id.tvWayPay:
				toWayPay();
				break;
			case R.id.tvAlipay:
				if (mCurrentSelectPrice != null || !mEtCusPrice.getText().toString().equals("")) {
					float amount = getPrice();
					toAliPay(amount);
				} else {
					MsgUtil.msg("请'选择/输入'需要充值的金额", getActivity());
				}
				break;
			case R.id.tvUnionpay:
				if (mCurrentSelectPrice != null || !mEtCusPrice.getText().toString().equals("")) {
					float amount = getPrice();
					toUnionpay(amount);
				} else {
					MsgUtil.msg("请'选择/输入'需要充值的金额", getActivity());
				}
				break;
			case R.id.tvPrepaidCard:
				String card = mEtCard.getText().toString().trim();
				String password = mEtPassword.getText().toString().trim();
				String price = mEtPrice.getText().toString().trim();
				
				if (card.equals("") || password.equals("") || price.equals("")) {
					MsgUtil.msg("卡号，密码，价钱不能为空！", getActivity());
					return;
				} else {
					final float amount = Float.parseFloat(mEtPrice.getText().toString().trim());
					toPrepaidCardPay(amount);
				}

				break;
			}
		}
	};
	/**
	 * 跳转支付
	 */
	public BasePay getPayInfo(int payType,float amount)
	{
		BasePay basePay = null;
		if(payType == BasePay.PAY_TYPE_ALIPAY){
			basePay = new AliPay();
		}else if(payType == BasePay.PAY_TYPE_UNIONPAY){
			basePay = new UnionPay();
		}else if(payType == BasePay.PAY_TYPE_PREPAIDCARD){
			basePay = new PrepaidCardPay();
		}
		
		if(basePay != null){
			mBasePay = basePay;
			basePay.setGid(mBundle.getString(KeyConstants.INTENT_DATA_KEY_GID));
			basePay.setCid(mBundle.getString(KeyConstants.INTENT_DATA_KEY_CID));
			basePay.setToken(mBundle.getString(KeyConstants.INTENT_DATA_KEY_TOKEN));
			basePay.setPid(mBundle.getInt(KeyConstants.INTENT_DATA_KEY_PID));
			basePay.setExtra(mBundle.getString(KeyConstants.INTENT_DATA_KEY_EXTRA));
			basePay.setClientId( mBundle.getString(KeyConstants.INTENT_DATA_KEY_CLIENTID));
			basePay.setClientSecret( mBundle.getString(KeyConstants.INTENT_DATA_KEY_CLIENTSECRET));
			basePay.setAmount(amount);
		}
		return basePay;
	}
	/**
	 * 得到支付价钱
	 * @return
	 */
	public float getPrice() {
		float price = 0;
		if (mCurrentSelectPrice != null) {
			String p = mCurrentSelectPrice.getText().toString();
			price = Float.parseFloat(p.substring(0, p.lastIndexOf("元")));

		} else {
			price = Float.parseFloat(mEtCusPrice.getText().toString());
		}
		mCurrentPrice = price;
		return price;
	}

	/**
	 * 跳转微派支付
	 * 已关闭
	 */
	@Deprecated
	public void toWayPay() {
		Log.d(TAG, "toWayPay()");
		Intent intent = new Intent(KeyConstants.RECEIVER_PAY_START_ACTION);
		intent.putExtra(KeyConstants.INTENT_DATA_KEY_PAY_TYPE, BasePay.PAY_TYPE_WIIPAY);
		getActivity().sendBroadcast(intent);
	}

	/**
	 * show Loading
	 * 
	 * @return
	 */
	public View showLoading() {
		if (getView() == null) {
			return null;
		}
		// 显示loadingView
		final View mLoadingView = getView().findViewById(R.id.loadingView);

		if (mLoadingView != null) {
			mLoadingView.setVisibility(View.VISIBLE);
		}
		return mLoadingView;
	}

	/**
	 * 充值卡请求
	 * 
	 * @param orderId
	 */
	public void toPrepaidCardPay(float amount) {

		String card = mEtCard.getText().toString().trim();
		String password = mEtPassword.getText().toString().trim();
		
		BasePay basePay = getPayInfo(BasePay.PAY_TYPE_PREPAIDCARD, amount);
		if(basePay != null && basePay instanceof PrepaidCardPay){
			PrepaidCardPay prepaidCardPay = (PrepaidCardPay) basePay;
			prepaidCardPay.setCard(card);
			prepaidCardPay.setPassword(password);
			prepaidCardPay.pay(getActivity()); //支付
		}
	}

	/**
	 * 银联支付
	 */
	public void toUnionpay(float amount) {
		BasePay basePay = getPayInfo(BasePay.PAY_TYPE_UNIONPAY, amount);
		if(basePay != null && basePay instanceof UnionPay){
			UnionPay unionPay = (UnionPay) basePay;
			getActivity().registerReceiver(unionPay.mPayUnionResultBroadcastReceiver, new IntentFilter(KeyConstants.ACTION_PAY_RESULT_RECEIVER));
			unionPay.pay(getActivity()); //支付
		}
	}

	/**
	 * 支付宝支付
	 * 
	 * @param orderId
	 * @param amount
	 */
	public void toAliPay(float amount) {
		BasePay basePay = getPayInfo(BasePay.PAY_TYPE_ALIPAY, amount);
		if(basePay != null && basePay instanceof AliPay){
			AliPay aliPay = (AliPay) basePay;
			String notifyUrl = mBundle.getString(KeyConstants.INTENT_DATA_KEY_NOTIFY_URL);
			aliPay.setNotifyUrl(notifyUrl);
			aliPay.pay(getActivity()); //支付
		}
	}


	@Override
	public boolean onKeyDown(Activity activity, Integer keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Bundle bundle = new Bundle();
			bundle.putString(KeyConstants.INTENT_KEY_RESULT, KeyConstants.INTENT_KEY_CANCEL);
			// 回调取消
			BasePay.sendResultBroadcast(activity, bundle, KeyConstants.RECEIVER_ACTION_PAY);
			return false;
		}
		return true;
	}



}
