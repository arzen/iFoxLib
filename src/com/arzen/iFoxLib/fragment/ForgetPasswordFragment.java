package com.arzen.iFoxLib.fragment;

import com.arzen.iFoxLib.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ForgetPasswordFragment extends BaseFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_forget_password, null);
		initUI(view);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	/**
	 * 初始化ui
	 * 
	 * @param view
	 */
	public void initUI(View view) {
	}

	@Override
	public boolean onKeyDown(Activity activity, Integer keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
}
