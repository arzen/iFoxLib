package com.arzen.iFoxLib.fragment;

import java.util.Map;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.arzen.iFoxLib.R;
import com.arzen.iFoxLib.pay.WayPay;
import com.bx.pay.ApkUpdate;
import com.bx.pay.ApkUpdate.ApkUpdateCallback;
import com.encore.libs.utils.Log;

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
				toWayPay();
				break;
			case R.id.btnAlipay:

				break;
			case R.id.btnUnionpay:

				break;
			}
		}
	};
	
	/**
	 * 跳转微派支付
	 */
	public void toWayPay()
	{
		//检测微派支付有没更新
		new ApkUpdate(getActivity(), new ApkUpdateCallback() {
  			@Override
			public void launch(Map<String, String> arg0) {
				// TODO Auto-generated method stub
				if(mWayPay == null){
					mWayPay = new WayPay(mBundle);
				}
				Log.d("PayFragment", getActivity().toString());
				mWayPay.toPay(getActivity(), "0001");
			}
		});
	}
	
}
