package com.arzen.iFoxLib.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arzen.iFoxLib.LogUtil;
import com.arzen.iFoxLib.R;
import com.encore.libs.utils.Log;

public class LoginFragment extends BaseFragment {

	public static final String TAG = "LoginFragment";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View loginView = inflater.inflate(R.layout.fragment_login, null);;
		return loginView;
	}
}
