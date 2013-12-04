package com.arzen.iFoxLib.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arzen.iFoxLib.R;
import com.arzen.iFoxLib.api.HttpIfoxApi;
import com.arzen.iFoxLib.api.HttpSetting;
import com.arzen.iFoxLib.bean.User;
import com.arzen.iFoxLib.setting.KeyConstants;
import com.arzen.iFoxLib.setting.UserSetting;
import com.arzen.iFoxLib.utils.MD5Util;
import com.arzen.iFoxLib.utils.MsgUtil;
import com.encore.libs.http.HttpConnectManager;
import com.encore.libs.http.OnRequestListener;
import com.encore.libs.utils.Log;

public class LoginLoadingFragment extends BaseFragment {
	public static final String TAG = "LoginLoadingFragment";
	
	private TextView mTvUserName;

	public Bundle mBundle;

	private String mPhoneNumber;
	private String mPassword;
	private String mCid;
	private String mGid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mBundle = getArguments();
		if (mBundle != null) {
			mPhoneNumber = mBundle.getString(KeyConstants.INTENT_KEY_PHONE_NUMBER);
			mPassword = mBundle.getString(KeyConstants.INTENT_KEY_PHONE_PASSWORD);
			mCid = mBundle.getString(KeyConstants.INTENT_DATA_KEY_CID);
			mGid = mBundle.getString(KeyConstants.INTENT_DATA_KEY_GID);
		}
		
		Log.d(TAG, "phoneNumber:" + mPhoneNumber + " password:" + mPassword + " cid:" + mCid + " gid:" + mGid);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View loadingView = inflater.inflate(R.layout.fragment_login_loading, null);
		initUI(loadingView);
		return loadingView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// 登录
		login();
	}

	/**
	 * 初始化UI
	 * 
	 * @param view
	 */
	public void initUI(View view) {
		mTvUserName = (TextView) view.findViewById(R.id.tvUserName);
		mTvUserName.setText("用户帐号:" + mPhoneNumber);
	}

	/**
	 * 请求登录
	 */
	public void login() {
		mPassword = MD5Util.getMD5String(mPassword); // 密码需要md5
		HttpIfoxApi.requestLogin(getActivity(), mGid, mCid, mPhoneNumber, mPassword, new OnRequestListener() {

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

						if (state == HttpConnectManager.STATE_SUC && result != null && result instanceof User) {
							User login = (User) result;
							if (login.getCode() == HttpSetting.RESULT_CODE_OK) {
								MsgUtil.msg("登录成功", getActivity());
								saveData(login.getData().getUid(), login.getData().getToken());
							} else {
								MsgUtil.msg(login.getMsg(), getActivity());
							}
						} else if (state == HttpConnectManager.STATE_TIME_OUT) { // 请求超时
							MsgUtil.msg(getString(R.string.time_out), getActivity());
						} else { // 请求失败
							MsgUtil.msg(getString(R.string.request_fail), getActivity());
						}
						getActivity().finish();
					}
				});
			}
		});
	}

	/**
	 * 保存数据
	 */
	public void saveData(String uid, String token) {
		UserSetting.saveUserData(getActivity(), uid, token);
	}

	@Override
	public boolean onKeyDown(Activity activity, Integer keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

}
