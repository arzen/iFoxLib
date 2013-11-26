package com.arzen.iFoxLib.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arzen.iFoxLib.R;
import com.encore.libs.http.HttpConnectManager;
import com.encore.libs.http.OnRequestListener;
import com.encore.libs.http.Request;
import com.encore.libs.utils.Log;

public class HomeFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View homeView = inflater.inflate(R.layout.fragment_home, null);
//		homeView.findViewById(R.id.imageView).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(getActivity(),TestActivity.class);
//				startActivity(intent);
//			}
//		});
		return homeView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		android.util.Log.d("HomeFragment", "request 1111111");
		Log.d("HomeFragment", "request 222222");
		Request request = new Request("http://mapp.easou.com/hige/version.xml");
		request.setOnRequestListener(new OnRequestListener() {
			
			@Override
			public void onResponse(String arg0, int state, Object object, int arg3) {
				// TODO Auto-generated method stub
				if(object != null){
					Log.d("HomeFragment", "ok!!!!!!!!! state:");
				}
				
				Log.d("HomeFragment", "call back");
			}
		});
		
		HttpConnectManager.getInstance(getActivity()).doGet(request);
	}
	
	
}
