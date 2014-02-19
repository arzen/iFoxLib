package com.arzen.iFoxLib.dynamic;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arzen.iFoxLib.R;
import com.arzen.iFoxLib.api.HttpIfoxApi;
import com.arzen.iFoxLib.api.OnRequestCallback;
import com.arzen.iFoxLib.bean.PayList;
import com.arzen.iFoxLib.bean.PayList.Data;
import com.arzen.iFoxLib.dynamic.base.ProgressDynamic;
import com.arzen.iFoxLib.pay.AliPay;
import com.arzen.iFoxLib.pay.IFoxPay;
import com.arzen.iFoxLib.pay.PrepaidCardPay;
import com.arzen.iFoxLib.pay.UnionPay;
import com.arzen.iFoxLib.setting.KeyConstants;
import com.arzen.iFoxLib.setting.UserSetting;
import com.arzen.iFoxLib.utils.CommonUtil;
import com.arzen.iFoxLib.utils.MsgUtil;
import com.encore.libs.utils.Log;
import com.encore.libs.utils.NetWorkUtils;

public class PayDynamic extends ProgressDynamic {
	
	public static final String TAG = "PayDynamic";
	/**
	 * 刷新数据
	 */
	public static final int TAG_REFRESH_DATA = 0;
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
	// 支付列表对象
	private PayList mPayList = null;
	// 当前tab id
	public int mCurrentTab = -1;
	// 选中的textView
	public TextView mTvSelectedText;
	// 价格view
	public View mViewPrice;
	// 支付按钮
	public Button mBtnPay;
	// 当前选中text
	private TextView mCurrentSelectPrice;
	// 充值卡view
	private View mViewPrepaidCard;
	/**
	 * 充值卡控件
	 */
	private EditText mEtCard;
	private EditText mEtPassword;
	private EditText mEtPrice;
	/**
	 * 支付选择价钱控件
	 */
	private TextView mTv100;
	private TextView mTv200;
	private TextView mTv500;
	private TextView mTv1000;
	private TextView mTv50;
	private TextView mTv30;
	private TextView mTv20;
	private TextView mTv10;
	private EditText mEtCusPrice;
	// 支付对象
	private IFoxPay mIFoxPay;
	// fragment 主体内容view
	private View mContentView;
	/**
	 * 检查时间,检测是否需要请求
	 */
	private static long mCheckTime = 24 * 60 * 60 * 1000;
	/**
	 * 支付列表缓存地址
	 */
	public static String PAYLISTCHACHEPATH = "";
	
	@Override
	public void onCreate(Activity activity, Bundle bundle) {
		super.onCreate(activity, bundle);
	}
	
	@Override
	public View onCreateView(Activity activity) {
		// TODO Auto-generated method stub
		mContentView = LayoutInflater.from(activity).inflate(R.layout.fragment_pay, null);
		return super.onCreateView(activity);
	}

	@Override
	public void onActivityCreate() {
		//上传过来的参数
		//缓存列表地址
		PAYLISTCHACHEPATH = getActivity().getCacheDir().getAbsolutePath() + "/playList.cache";
		//设置主内容
		setContentView(mContentView);
		//初始化ui
		initUI(mContentView);
		// 初始化内容
		initData(mPayList);
		// 检查是否传了金额进来,固定支付金额
		checkIsImmobilizationAmount();
	}
	
	
	/**
	 * 检查是否固定支付金额,禁止选择金额框的enabled
	 */
	public void checkIsImmobilizationAmount() {
		/*
		 * 获取是否固定支付金额
		 */
		Float price = mBundle.getFloat(KeyConstants.INTENT_DATA_KEY_AMOUNT);
		if (price != null && price > 0) { // 固定支付金额关闭金钱选择项
			mEtCusPrice.setText(String.valueOf(price));
			mEtCusPrice.setEnabled(false);
			
			mEtPrice.setText(String.valueOf(price));
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

	/**
	 * 初始化价格选择状态
	 */
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
			//初始化价钱选择项
			initPriceSelected();
			view.setSelected(true);//改变当前选择状态
			mCurrentSelectPrice = (TextView) view;
		}
	};

	/**
	 * 初始化数据
	 * 
	 * @param payList
	 */
	public void initData(PayList payList) {
		if (payList != null) {
			// 取消loading状态
			setContentShown(true);
			mPayList = payList;
			initPayList(payList);
		} else {
			PayList cache = (PayList) CommonUtil.file2Object(PAYLISTCHACHEPATH);
			long payListTime = UserSetting.getPayListTime(getActivity()); // 得到上次检查的时间
			if (cache == null || System.currentTimeMillis() - payListTime > mCheckTime) {
				// 没有网络
				if (!NetWorkUtils.isNetworkAvailable(getActivity())) {
					setContentError(true, getActivity().getString(R.string.not_network));
					return;
				}
				// 当前为loading状态
				setContentShown(false);
				// 请求支付列表
				requestPayList();
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
		View fristPayView = null;
		// 微派
		Data data = payList.getData();
		if (data != null) {
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

	public Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			if (!isAdded()) {
				return;
			}
			switch (msg.what) {
			case TAG_REFRESH_DATA:
				if (msg.obj != null) {
					PayList payList = (PayList) msg.obj;
					UserSetting.savePayListTime(getActivity().getApplicationContext(), System.currentTimeMillis());
					CommonUtil.object2File(payList, PAYLISTCHACHEPATH); // 保存缓存
					initData(payList);
				}
				break;
			}
		};
	};

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
			mTvSelectedText.setText(R.string.pay_select_pay_price);
			break;
		case R.id.tvPrepaidCard:
			mTvSelectedText.setVisibility(View.VISIBLE);
			mTvSelectedText.setText(R.string.toast_prepaidCard);
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

//		try {
//			if (mIFoxPay != null && mIFoxPay instanceof UnionPay) {
//				UnionPay unionPay = (UnionPay) mIFoxPay;
//				getActivity().unregisterReceiver(unionPay.mPayUnionResultBroadcastReceiver);
//				unionPay.mPayUnionResultBroadcastReceiver = null;
//			}
//
//		} catch (Exception e) {
//		}
	}

	/**
	 * 请求支付列表
	 */
	public void requestPayList() {
		if (mBundle == null) {
			sendErrorMessage(R.string.not_param);
			// 显示出错view
			setContentError(true, getActivity().getString(R.string.not_param));
			return;
		}
		String gid = mBundle.getString(KeyConstants.INTENT_DATA_KEY_GID);
		String cid = mBundle.getString(KeyConstants.INTENT_DATA_KEY_CID);
		String token = mBundle.getString(KeyConstants.INTENT_DATA_KEY_TOKEN);
		String clientId = mBundle.getString(KeyConstants.INTENT_DATA_KEY_CLIENTID);
		String clientSecret = mBundle.getString(KeyConstants.INTENT_DATA_KEY_CLIENTSECRET);
		// 请求
		HttpIfoxApi.requestPayList(getActivity(), gid, cid, token, clientId, clientSecret, new OnRequestCallback() {

			@Override
			public void onSuccess(Object result) {
				PayList payList = (PayList) result;
				// 刷新数据
				sendMessage(mHandler, TAG_REFRESH_DATA, payList);
			}

			@Override
			public void onFail(String msg, int state) {
				// TODO Auto-generated method stub
				sendErrorMessage(msg);
				// 显示出错view
				setContentError(true, msg);
			}
		});
	}

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
					MsgUtil.msg(getActivity().getString(R.string.input_pay_amount), getActivity());
				}
				break;
			case R.id.tvUnionpay:
				if (mCurrentSelectPrice != null || !mEtCusPrice.getText().toString().equals("")) {
					float amount = getPrice();
					toUnionpay(amount);
				} else {
					MsgUtil.msg(getActivity().getString(R.string.input_pay_amount), getActivity());
				}
				break;
			case R.id.tvPrepaidCard:
				String card = mEtCard.getText().toString().trim();
				String password = mEtPassword.getText().toString().trim();
				String price = mEtPrice.getText().toString().trim();

				if (card.equals("") || password.equals("") || price.equals("")) {
					MsgUtil.msg(getActivity().getString(R.string.card_pwd_not_null), getActivity());
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
	public IFoxPay getPayInfo(int payType, float amount) {
		IFoxPay iFoxPay = null;
		if (payType == IFoxPay.PAY_TYPE_ALIPAY) {
			iFoxPay = new AliPay();
		} else if (payType == IFoxPay.PAY_TYPE_UNIONPAY) {
			iFoxPay = new UnionPay();
		} else if (payType == IFoxPay.PAY_TYPE_PREPAIDCARD) {
			iFoxPay = new PrepaidCardPay();
		}

		if (iFoxPay != null) {
			mIFoxPay = iFoxPay;
			iFoxPay.setGid(mBundle.getString(KeyConstants.INTENT_DATA_KEY_GID));
			iFoxPay.setCid(mBundle.getString(KeyConstants.INTENT_DATA_KEY_CID));
			iFoxPay.setToken(mBundle.getString(KeyConstants.INTENT_DATA_KEY_TOKEN));
			iFoxPay.setPid(mBundle.getInt(KeyConstants.INTENT_DATA_KEY_PID));
			iFoxPay.setExtra(mBundle.getString(KeyConstants.INTENT_DATA_KEY_EXTRA));
			iFoxPay.setClientId(mBundle.getString(KeyConstants.INTENT_DATA_KEY_CLIENTID));
			iFoxPay.setClientSecret(mBundle.getString(KeyConstants.INTENT_DATA_KEY_CLIENTSECRET));
			iFoxPay.setAmount(amount);
		}
		return iFoxPay;
	}

	/**
	 * 得到支付价钱
	 * 
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
		return price;
	}

	/**
	 * 跳转微派支付 已关闭
	 */
	@Deprecated
	public void toWayPay() {
		Log.d(TAG, "toWayPay()");
		Intent intent = new Intent(KeyConstants.RECEIVER_PAY_START_ACTION);
		intent.putExtra(KeyConstants.INTENT_DATA_KEY_PAY_TYPE, IFoxPay.PAY_TYPE_WIIPAY);
		getActivity().sendBroadcast(intent);
	}


	/**
	 * 充值卡请求
	 * 
	 * @param orderId
	 */
	public void toPrepaidCardPay(float amount) {

		String card = mEtCard.getText().toString().trim();
		String password = mEtPassword.getText().toString().trim();

		IFoxPay basePay = getPayInfo(IFoxPay.PAY_TYPE_PREPAIDCARD, amount);
		if (basePay != null && basePay instanceof PrepaidCardPay) {
			PrepaidCardPay prepaidCardPay = (PrepaidCardPay) basePay;
			prepaidCardPay.setCard(card);
			prepaidCardPay.setPassword(password);
			prepaidCardPay.pay(getActivity()); // 支付
		}
	}

	/**
	 * 银联支付
	 */
	public void toUnionpay(float amount) {
		IFoxPay basePay = getPayInfo(IFoxPay.PAY_TYPE_UNIONPAY, amount);
		if (basePay != null && basePay instanceof UnionPay) {
			UnionPay unionPay = (UnionPay) basePay;
//			getActivity().registerReceiver(unionPay.mPayUnionResultBroadcastReceiver, new IntentFilter(KeyConstants.ACTION_PAY_RESULT_RECEIVER));
			unionPay.pay(getActivity()); // 支付
		}
	}

	/**
	 * 支付宝支付
	 * 
	 * @param orderId
	 * @param amount
	 */
	public void toAliPay(float amount) {
		IFoxPay basePay = getPayInfo(IFoxPay.PAY_TYPE_ALIPAY, amount);
		if (basePay != null && basePay instanceof AliPay) {
			AliPay aliPay = (AliPay) basePay;
			String notifyUrl = mBundle.getString(KeyConstants.INTENT_DATA_KEY_NOTIFY_URL);
			aliPay.setNotifyUrl(notifyUrl);
			aliPay.pay(getActivity()); // 支付
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		/*************************************************
		 * 
		 * 步骤3：处理银联手机支付控件返回的支付结果
		 * 
		 ************************************************/
		if (requestCode == 10) {
			if (data == null) {
				return;
			}
			/*
			 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
			 */
			String result = data.getExtras().getString("pay_result");
			Intent intent = new Intent();
			intent.putExtra(KeyConstants.INTENT_KEY_RESULT, result);
//			getActivity().sendBroadcast(intent);
			
			if (mIFoxPay != null && mIFoxPay instanceof UnionPay) {
				UnionPay unionPay = (UnionPay) mIFoxPay;
				unionPay.disposeResult(intent);
			}
		}
	}

	@Override
	public boolean onKeyDown(Activity activity, Integer keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Bundle bundle = new Bundle();
			bundle.putString(KeyConstants.INTENT_KEY_RESULT, KeyConstants.INTENT_KEY_CANCEL);
			// 回调取消
			IFoxPay.sendResultBroadcast(activity, bundle, KeyConstants.RECEIVER_ACTION_PAY);
			return false;
		}
		return true;
	}
}
