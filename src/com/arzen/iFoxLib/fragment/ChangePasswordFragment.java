//package com.arzen.iFoxLib.fragment;
//
//import com.arzen.iFoxLib.R;
//import com.arzen.iFoxLib.fragment.base.BaseFragment;
//import com.arzen.iFoxLib.setting.KeyConstants;
//import com.arzen.iFoxLib.utils.MsgUtil;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//public class ChangePasswordFragment extends BaseFragment {
//
//	// 旧密码
//	private EditText mEtOldPassword;
//	// 新密码
//	private EditText mEtNewPassword;
//	// 注册
//	private Button mBtnCancel;
//	// 登录
//	private Button mBtnChange;
//	// 主程序传递过来的数据
//	private Bundle mBundle;
//
//	private String mGid;
//	private String mCid;
//	private String mToken;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//
//		mBundle = getArguments();
//		mGid = mBundle.getString(KeyConstants.INTENT_DATA_KEY_GID);
//		mCid = mBundle.getString(KeyConstants.INTENT_DATA_KEY_CID);
//		mToken = mBundle.getString(KeyConstants.INTENT_DATA_KEY_TOKEN);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		View view = inflater.inflate(R.layout.fragment_changepwd, null);
//		initUI(view);
//		return view;
//	}
//
//	/**
//	 * 初始化ui
//	 * 
//	 * @param view
//	 */
//	public void initUI(View view) {
//		mEtOldPassword = (EditText) view.findViewById(R.id.etOldPassword);
//		mEtNewPassword = (EditText) view.findViewById(R.id.etNewPassword);
//		mBtnCancel = (Button) view.findViewById(R.id.btnCancel);
//		mBtnChange = (Button) view.findViewById(R.id.btnChange);
//
//		mBtnCancel.setOnClickListener(mOnClickListener);
//		mBtnChange.setOnClickListener(mOnClickListener);
//	}
//
//	public OnClickListener mOnClickListener = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			switch (v.getId()) {
//			case R.id.btnCancel:
//				cancel(getActivity());
//				break;
//			case R.id.btnChange:
//				changePassword();
//				break;
//			}
//		}
//	};
//
//	/**
//	 * 修改密码
//	 */
//	public void changePassword() {
//		String oldPassword = mEtOldPassword.getText().toString().trim();
//		String newPassword = mEtNewPassword.getText().toString().trim();
//		if (check(oldPassword, newPassword)) {
//			Bundle bundle = new Bundle();
//			bundle.putString(KeyConstants.KEY_PACKAGE_NAME, KeyConstants.PKG_LOGIN_LOADING_FRAGMENT);
//			bundle.putString(KeyConstants.INTENT_DATA_KEY_GID, mGid);
//			bundle.putString(KeyConstants.INTENT_DATA_KEY_CID, mCid); // 渠道id
//			bundle.putString(KeyConstants.INTENT_DATA_KEY_TOKEN, mToken);
//			bundle.putString(KeyConstants.INTENT_DATA_KEY_OLDPASSWORD, oldPassword);
//			bundle.putString(KeyConstants.INTENT_DATA_KEY_NEWPASSWORD, newPassword);
//			bundle.putInt(KeyConstants.INTENT_DATA_KEY_FROM, 2);
//			startCommonActivityForResult(bundle, 0x02);
//		}
//	}
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		if (resultCode == Activity.RESULT_OK && data != null) {
//			boolean isSuccess = data.getBooleanExtra(KeyConstants.IS_SUCCESS, false);
//			if (isSuccess) { // 注册成功
//				Bundle bundle = new Bundle();
//				bundle.putString(KeyConstants.INTENT_KEY_RESULT, KeyConstants.INTENT_KEY_SUCCESS); // 回调成功
//				sendResultBroadcast(getActivity(), bundle, KeyConstants.RECEIVER_ACTION_CHANGE_PASSWORD);
//			}
//		}
//		super.onActivityResult(requestCode, resultCode, data);
//	}
//
//	/**
//	 * 检查帐号密码是否正确
//	 * 
//	 * @return
//	 */
//	public boolean check(String oldPassword, String newPassword) {
//
//		boolean isOk = false;
//		if (oldPassword.length() < 6 || newPassword.length() < 6) {
//			MsgUtil.msg("您输入的密码不能少于6位", getActivity());
//		} else {
//			isOk = true;
//		}
//		return isOk;
//	}
//
//	@Override
//	public boolean onKeyDown(Activity activity, Integer keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			cancel(activity);
//			return false;
//		}
//		return true;
//	}
//
//	/**
//	 * 取消
//	 * 
//	 * @param activity
//	 */
//	public void cancel(Activity activity) {
//		Bundle bundle = new Bundle();
//		bundle.putString(KeyConstants.INTENT_KEY_RESULT, KeyConstants.INTENT_KEY_CANCEL);
//		sendResultBroadcast(activity, bundle, KeyConstants.RECEIVER_ACTION_CHANGE_PASSWORD);
//	}
//
//}
