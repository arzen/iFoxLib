package com.arzen.iFoxLib.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arzen.iFoxLib.R;
import com.arzen.iFoxLib.api.HttpIfoxApi;
import com.arzen.iFoxLib.api.HttpSetting;
import com.arzen.iFoxLib.bean.BaseBean;
import com.arzen.iFoxLib.bean.User;
import com.arzen.iFoxLib.contacts.Contact;
import com.arzen.iFoxLib.contacts.ContactUtils;
import com.arzen.iFoxLib.contacts.ContactUtils.ContactCallBack;
import com.arzen.iFoxLib.setting.KeyConstants;
import com.arzen.iFoxLib.setting.UserSetting;
import com.arzen.iFoxLib.utils.MD5Util;
import com.arzen.iFoxLib.utils.MsgUtil;
import com.encore.libs.http.HttpConnectManager;
import com.encore.libs.http.OnRequestListener;
import com.encore.libs.utils.Log;

public class LoadingFragment extends BaseFragment {
	public static final String TAG = "LoginLoadingFragment";

	private TextView mTvUserName;
	private TextView mTvChangeUser;

	public Bundle mBundle;

	private String mPhoneNumber;
	private String mPassword;
	private String mCid;
	private String mGid;
	private int mFrom = -1; // 0 = 登录页跳转过来 1 = 注册页跳转过来 2 = 修改密码跳转过来

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mBundle = getArguments();

		if (mBundle != null) {
			mPhoneNumber = mBundle.getString(KeyConstants.INTENT_DATA_KEY_PHONE_NUMBER);
			mPassword = mBundle.getString(KeyConstants.INTENT_DATA_KEY_PASSWORD);
			mCid = mBundle.getString(KeyConstants.INTENT_DATA_KEY_CID);
			mGid = mBundle.getString(KeyConstants.INTENT_DATA_KEY_GID);
			mFrom = mBundle.getInt(KeyConstants.INTENT_DATA_KEY_FROM);
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
		if (mFrom == 0) {
			// 登录
			login();
		} else if (mFrom == 1) {
			// 注册
			register();
		} else if (mFrom == 2) {
			changePassword();
		}

	}

	/**
	 * 初始化UI
	 * 
	 * @param view
	 */
	public void initUI(View view) {
		mTvUserName = (TextView) view.findViewById(R.id.tvUserName);
		mTvChangeUser = (TextView) view.findViewById(R.id.tvChangeUser);
		if (mFrom == 0) {
			mTvUserName.setText("用户帐号:" + mPhoneNumber);
			mTvChangeUser.setVisibility(View.VISIBLE);
		} else if (mFrom == 1) {
			mTvUserName.setText("注册帐号:" + mPhoneNumber);
			mTvChangeUser.setVisibility(View.GONE);
		} else if (mFrom == 2) {
			mTvUserName.setText("修改密码");
			mTvChangeUser.setVisibility(View.GONE);
		}

		/**
		 * 切换账户
		 */
		mTvChangeUser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				boolean isSuccess = false;
				intent.putExtra(KeyConstants.IS_SUCCESS, isSuccess);
				getActivity().setResult(Activity.RESULT_OK, intent);
				getActivity().finish();
			}
		});
	}

	/**
	 * 请求登录
	 */
	public void login() {
		String password = MD5Util.getMD5String(mPassword); // 密码需要md5
		HttpIfoxApi.requestLogin(getActivity(), mGid, mCid, mPhoneNumber, password, new OnRequestListener() {

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
						Intent intent = new Intent();
						boolean isSuccess = false;
						if (state == HttpConnectManager.STATE_SUC && result != null && result instanceof User) {
							User login = (User) result;
							if (login.getCode() == HttpSetting.RESULT_CODE_OK) {
								// MsgUtil.msg("登录成功", getActivity());
								intent.putExtra(KeyConstants.INTENT_DATA_KEY_TOKEN, login.getData().getToken());
								saveData(login.getData().getUid(), login.getData().getToken(), mPhoneNumber, mPassword);
								isSuccess = true;
							} else {
								MsgUtil.msg(login.getMsg(), getActivity());
							}
						} else if (state == HttpConnectManager.STATE_TIME_OUT) { // 请求超时
							MsgUtil.msg(getString(R.string.time_out), getActivity());
						} else { // 请求失败
							MsgUtil.msg(getString(R.string.request_fail), getActivity());
						}

						intent.putExtra(KeyConstants.IS_SUCCESS, isSuccess);
						getActivity().setResult(Activity.RESULT_OK, intent);
						getActivity().finish();
					}
				});
			}
		});
	}

	public void register() {
		// mPassword = MD5Util.getMD5String(mPassword); // 密码需要md5

		// 注册时不需要md5
		HttpIfoxApi.requestRegister(getActivity(), mGid, mCid, mPhoneNumber, mPassword, new OnRequestListener() {

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
						Intent intent = new Intent();
						boolean isSuccess = false;
						if (state == HttpConnectManager.STATE_SUC && result != null && result instanceof User) {
							User login = (User) result;
							if (login.getCode() == HttpSetting.RESULT_CODE_OK) {
								MsgUtil.msg("注册成功", getActivity());
								intent.putExtra(KeyConstants.INTENT_DATA_KEY_TOKEN, login.getData().getToken());
								saveData(login.getData().getUid(), login.getData().getToken(), mPhoneNumber, mPassword);
								isSuccess = true;
							} else {
								MsgUtil.msg(login.getMsg(), getActivity());
							}
						} else if (state == HttpConnectManager.STATE_TIME_OUT) { // 请求超时
							MsgUtil.msg(getString(R.string.time_out), getActivity());
						} else { // 请求失败
							MsgUtil.msg(getString(R.string.request_fail), getActivity());
						}

						intent.putExtra(KeyConstants.IS_SUCCESS, isSuccess);
						getActivity().setResult(Activity.RESULT_OK, intent);
						getActivity().finish();
					}
				});
			}
		});
	}

	/**
	 * 修改密码
	 */
	public void changePassword() {
		if (mBundle == null) {
			return;
		}
		String token = mBundle.getString(KeyConstants.INTENT_DATA_KEY_TOKEN);
		String oldPassword = mBundle.getString(KeyConstants.INTENT_DATA_KEY_OLDPASSWORD);
		String newPassword = mBundle.getString(KeyConstants.INTENT_DATA_KEY_OLDPASSWORD);

		// oldPassword = MD5Util.getMD5String(oldPassword); // 密码需要md5
		// newPassword = MD5Util.getMD5String(newPassword);

		HttpIfoxApi.requestChangePassword(getActivity(), mGid, mCid, token, oldPassword, newPassword, new OnRequestListener() {

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
						Intent intent = new Intent();
						boolean isSuccess = false;
						if (state == HttpConnectManager.STATE_SUC && result != null && result instanceof BaseBean) {
							BaseBean baseBean = (BaseBean) result;
							if (baseBean.getCode() == HttpSetting.RESULT_CODE_OK) {
								MsgUtil.msg("修改成功", getActivity());
								isSuccess = true;
							} else {
								MsgUtil.msg(baseBean.getMsg(), getActivity());
							}
						} else if (state == HttpConnectManager.STATE_TIME_OUT) { // 请求超时
							MsgUtil.msg(getString(R.string.time_out), getActivity());
						} else { // 请求失败
							MsgUtil.msg(getString(R.string.request_fail), getActivity());
						}

						intent.putExtra(KeyConstants.IS_SUCCESS, isSuccess);
						getActivity().setResult(Activity.RESULT_OK, intent);
						getActivity().finish();
					}
				});
			}
		});
	}

	/**
	 * 保存数据
	 */
	public void saveData(String uid, String token, String userName, String password) {
		UserSetting.saveUserData(getActivity(), uid, token, userName, password);
		
		//获取本地通讯录，对比上传，缓存等操作
		upLoadContacts(getActivity().getApplicationContext(),token);
	}
	/**
	 * 上传通讯录
	 */
	public void upLoadContacts(final Context context,final String token)
	{
		new Thread(){
			public void run() {
				Log.d("Contact", "----- getContactCache ------");
				//是否有缓存
				final ArrayList<Contact> cacheContacts = ContactUtils.getAllContactsByCache(context);
				
				Log.d("Contact", "getContactCache size:" + cacheContacts == null ? "0" : cacheContacts.size()+"");
				Log.d("Contact", "getAllSystemConcatsDatas()");
				//读取本地通讯录
				ContactUtils.getAllConcatsDatas(context, new ContactCallBack() {
					
					@Override
					public void onCallBack(ArrayList<Contact> localContacts) {
						// TODO Auto-generated method stub
						Log.d("Contact", "getAllSystemConcatsDatas size" + localContacts == null ? "0" : localContacts.size()+"");
						if(localContacts != null && localContacts.size() != 0){
							ArrayList<Contact> upLoadContacts = null;
							//本地缓存是否存在
							if(cacheContacts != null){
								//对比需要上传的通讯录
								upLoadContacts = ContactUtils.getUpLoadContacts(localContacts, cacheContacts);
							}else{ //缓存是空，全部上传
								//保存缓存,上传通讯录
								upLoadContacts = localContacts;
								ContactUtils.saveContacts(context, localContacts);
							}
							
							ContactUtils.upLoadContacts(context, token, upLoadContacts);
						}
					}
				});
			};
		}.start();
	}

	@Override
	public boolean onKeyDown(Activity activity, Integer keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

}
