package com.arzen.iFoxLib;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.arzen.iFoxLib.activity.PayActivity;
import com.arzen.iFoxLib.fragment.HomeFragment;
import com.arzen.iFoxLib.fragment.PayFragment;

public class MainILibActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Intent intent = new Intent(this, PayActivity.class);
		startActivity(intent);
	}
}
