package com.arzen.iFoxLib.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.arzen.iFoxLib.dynamic.PayDynamic;

public class PayActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		FrameLayout rootView = new FrameLayout(this);
		rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		rootView.setId(android.R.id.primary);
		

		PayDynamic payDynamic = new PayDynamic();
		Bundle mBundle = getIntent().getExtras();
		View view = payDynamic.onCreateView(this);
		payDynamic.onCreate(this, mBundle);
		payDynamic.onActivityCreate();
		setContentView(view);
//		try {
//			Fragment f = new PayFragment();
//			
//			Bundle mBundle = getIntent().getExtras();
//			if(mBundle != null){
//				f.setArguments(mBundle);
//			}
//			
//			FragmentManager fm = getFragmentManager();
//			FragmentTransaction ft = fm.beginTransaction();
//			ft.add(android.R.id.primary, f);
//			ft.commit();
//			fm.executePendingTransactions();
//		} catch (Exception e) {
//			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
//		}
	}
}
