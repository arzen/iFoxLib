package com.arzen.iFoxLib.fragment;

import com.arzen.iFoxLib.R;
import com.arzen.iFoxLib.utils.MsgUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterFragment extends BaseFragment {

	// 文本框帐号
	private EditText mEtPhone;
	// 文本框密码
	private EditText mEtPassword;
	// 注册
	private Button mBtnRegister;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View registerView = inflater.inflate(R.layout.fragment_register, null);
		return registerView;
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

		mBtnRegister.setOnClickListener(mOnClickListener);
	}

	public OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btnRegister:
				register();
				break;
			}
		}
	};
	
	public void register(){
		String phone = mEtPhone.getText().toString().trim();
		String password = mEtPassword.getText().toString().trim();
		
		if (check(phone, password)) {
			
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
			MsgUtil.msg("您输出的手机号码不正确,请重新输入!", getActivity());
		} else if (password.length() < 6) {
			MsgUtil.msg("您输入的密码不能少于6位", getActivity());
		} else {
			isOk = true;
		}
		return isOk;
	}

	@Override
	public boolean onKeyDown(Activity activity, Integer keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

}
