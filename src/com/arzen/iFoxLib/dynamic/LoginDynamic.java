package com.arzen.iFoxLib.dynamic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arzen.iFoxLib.R;
import com.arzen.iFoxLib.dynamic.base.BaseDynamic;
import com.arzen.iFoxLib.setting.KeyConstants;
import com.arzen.iFoxLib.setting.UserSetting;
import com.arzen.iFoxLib.utils.MsgUtil;
import com.baidu.mobstat.StatService;
import com.encore.libs.utils.Log;
import com.encore.libs.utils.NetWorkUtils;
/**
 * 登录
 * @author Encore.Liang
 *
 */
public class LoginDynamic extends BaseDynamic {

	/**
	 * 登录requestCode
	 */
	public int mLoginRequestCode = 600;
	// 注册requestCode
	public int mRegisterRequestCode = 300;

	public static final String TAG = "LoginFragment";

	// 文本框帐号
	private EditText mEtPhone;
	// 文本框密码
	private EditText mEtPassword;
	// 注册
	private Button mBtnRegister;
	// 登录
	private Button mBtnLogin;
	// 忘记密码
	private TextView mTvForgetPassword;

	private String mGid;
	private String mCid;
	
	@Override
	public void onCreate(Activity activity, Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(activity, bundle);
		
		mGid = mBundle.getString(KeyConstants.INTENT_DATA_KEY_GID);
		mCid = mBundle.getString(KeyConstants.INTENT_DATA_KEY_CID);

		String token = UserSetting.getUserToken(getActivity().getApplicationContext());
		if (token != null && !token.equals("")) { // 自动登录
			String userName = UserSetting.getUserName(getActivity().getApplicationContext());
			String pwd = UserSetting.getPwd(getActivity().getApplicationContext());
			// 自动登录
			login(userName, pwd);
		}
	}

	@Override
	public View onCreateView(Activity activity) {
		// TODO Auto-generated method stub
		View loginView = LayoutInflater.from(activity).inflate(R.layout.fragment_login, null);
		initUI(loginView);
		return loginView;
	}

	@Override
	public void onActivityCreate() {
		// TODO Auto-generated method stub
	}
	
	
	/**
	 * 初始化ui
	 * 
	 * @param view
	 */
	public void initUI(View view) {
		mEtPhone = (EditText) view.findViewById(R.id.etPhone);
		mEtPassword = (EditText) view.findViewById(R.id.etPassword);
		mBtnRegister = (Button) view.findViewById(R.id.btnRegister);
		mBtnLogin = (Button) view.findViewById(R.id.btnLogin);
		mTvForgetPassword = (TextView) view.findViewById(R.id.tvForgetPassword);

		mBtnRegister.setOnClickListener(mOnClickListener);
		mBtnLogin.setOnClickListener(mOnClickListener);
		mTvForgetPassword.setOnClickListener(mOnClickListener);
	}

	public OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btnRegister:
				StatService.onEvent(getActivity().getApplicationContext(), "USER_REG", "");
				register();
				break;
			case R.id.btnLogin:
				StatService.onEvent(getActivity().getApplicationContext(), "USER_LOGIN", "");
				Log.d(TAG, "login!");
				String phone = mEtPhone.getText().toString().trim();
				String password = mEtPassword.getText().toString().trim();
				login(phone, password);
				break;
			case R.id.tvForgetPassword:
				break;
			}
		}
	};

	/**
	 * 跳转登录加载页面
	 */
	public void login(String phone, String password) {
		// 没有网络
		if (!NetWorkUtils.isNetworkAvailable(getActivity())) {
			MsgUtil.msg(getActivity().getString(R.string.not_network), getActivity());
			return;
		}
		if (check(phone, password)) {
			Bundle bundle = new Bundle();
			bundle.putString(KeyConstants.KEY_PACKAGE_NAME, KeyConstants.PKG_LOGIN_LOADING_FRAGMENT);
			bundle.putString(KeyConstants.INTENT_DATA_KEY_PHONE_NUMBER, phone);
			bundle.putString(KeyConstants.INTENT_DATA_KEY_PASSWORD, password);
			bundle.putString(KeyConstants.INTENT_DATA_KEY_GID, mGid);
			bundle.putString(KeyConstants.INTENT_DATA_KEY_CID, mCid); // 渠道id
			bundle.putString(KeyConstants.INTENT_DATA_KEY_CLIENTID, mBundle.getString(KeyConstants.INTENT_DATA_KEY_CLIENTID));
			bundle.putString(KeyConstants.INTENT_DATA_KEY_CLIENTSECRET, mBundle.getString(KeyConstants.INTENT_DATA_KEY_CLIENTSECRET));
			bundle.putInt(KeyConstants.INTENT_DATA_KEY_FROM, 0);
			startCommonActivityForResult(bundle, mLoginRequestCode);
		}
	}

	/**
	 * 跳转注册
	 */
	public void register() {
		// 没有网络
		if (!NetWorkUtils.isNetworkAvailable(getActivity())) {
			MsgUtil.msg(getActivity().getString(R.string.not_network), getActivity());
			return;
		}
		String phone = mEtPhone.getText().toString().trim();
		String password = mEtPassword.getText().toString().trim();

		if (check(phone, password)) {
			Bundle bundle = new Bundle();
			bundle.putString(KeyConstants.KEY_PACKAGE_NAME, KeyConstants.PKG_LOGIN_LOADING_FRAGMENT);
			bundle.putString(KeyConstants.INTENT_DATA_KEY_PHONE_NUMBER, phone);
			bundle.putString(KeyConstants.INTENT_DATA_KEY_PASSWORD, password);
			bundle.putString(KeyConstants.INTENT_DATA_KEY_GID, mGid);
			bundle.putString(KeyConstants.INTENT_DATA_KEY_CID, mCid); // 渠道id
			bundle.putInt(KeyConstants.INTENT_DATA_KEY_FROM, 1);
			startCommonActivityForResult(bundle, mRegisterRequestCode);
		}
	}

	/**
	 * 检查帐号密码是否正确
	 * 
	 * @return
	 */
	public boolean check(String phone, String password) {

		boolean isOk = false;
		if (phone.length() < 11) {
			MsgUtil.msg(getActivity().getString(R.string.toast_phone_error), getActivity());
		} else if (password.length() < 6) {
			MsgUtil.msg(getActivity().getString(R.string.pwd_error), getActivity());
		} else {
			isOk = true;
		}
		return isOk;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if ((requestCode == mLoginRequestCode || requestCode == mRegisterRequestCode) && resultCode == Activity.RESULT_OK) {
			boolean isSuccess = data.getBooleanExtra(KeyConstants.IS_SUCCESS, false);
			if (isSuccess) { // 如果注册成功,或登录成功返回登录信息
				Bundle bundle = new Bundle();
				bundle.putString(KeyConstants.INTENT_KEY_RESULT, KeyConstants.INTENT_KEY_SUCCESS); // 回调成功
				bundle.putString(KeyConstants.INTENT_DATA_KEY_TOKEN, data.getStringExtra(KeyConstants.INTENT_DATA_KEY_TOKEN));
				bundle.putString(KeyConstants.INTENT_DATA_KEY_UID, data.getStringExtra(KeyConstants.INTENT_DATA_KEY_UID));
				Log.d(TAG, "onActivityResult: token:" + data.getStringExtra(KeyConstants.INTENT_DATA_KEY_TOKEN) + " uid:" + data.getStringExtra(KeyConstants.INTENT_DATA_KEY_UID));
				sendResultBroadcast(getActivity(), bundle, KeyConstants.RECEIVER_ACTION_LOGIN);
			}
		}
		Log.d(TAG, "onActivityResult");
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(Activity activity, Integer keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Bundle bundle = new Bundle();
			bundle.putString(KeyConstants.INTENT_KEY_RESULT, KeyConstants.INTENT_KEY_CANCEL);

			sendResultBroadcast(activity, bundle, KeyConstants.RECEIVER_ACTION_LOGIN);
			return false;
		}
		return true;
	}
}
