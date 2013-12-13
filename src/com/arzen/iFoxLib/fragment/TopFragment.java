package com.arzen.iFoxLib.fragment;

import com.arzen.iFoxLib.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TopFragment extends BaseFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_top, null);
		return view;
	}
	/**
	 * 初始化ui
	 */
	public void initUI(){
		
	}

	@Override
	public boolean onKeyDown(Activity activity, Integer keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
