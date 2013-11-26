package com.arzen.iFoxLib.fragment;

import com.arzen.iFoxLib.R;
import com.arzen.iFoxLib.pay.WayPay;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PayFragment extends Fragment {

	private Button mBtnWiipay;
	private Button mBtnAlipay;
	private Button mBtnUnionpay;
	
	//微派支付
	private WayPay mWayPay;
	
	private Bundle mBundle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View payView = inflater.inflate(R.layout.fragment_pay, null);
		initUI(payView);
		return payView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mBundle = getArguments();
	}

	public void initUI(View view) {
		mBtnWiipay = (Button) view.findViewById(R.id.btnWiipay);
		mBtnAlipay = (Button) view.findViewById(R.id.btnAlipay);
		mBtnUnionpay = (Button) view.findViewById(R.id.btnUnionpay);
		
		mBtnWiipay.setOnClickListener(mOnClickListener);
		mBtnAlipay.setOnClickListener(mOnClickListener);
		mBtnUnionpay.setOnClickListener(mOnClickListener);
	}

	public OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case R.id.btnWiipay:
				if(mWayPay == null){
					mWayPay = new WayPay(mBundle);
				}
				mWayPay.toPay(getActivity(), "0001");
				break;
			case R.id.btnAlipay:

				break;
			case R.id.btnUnionpay:

				break;
			}
		}
	};
	
}
