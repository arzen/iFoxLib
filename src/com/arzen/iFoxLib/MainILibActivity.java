package com.arzen.iFoxLib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.arzen.iFoxLib.activity.PayActivity;
import com.arzen.iFoxLib.setting.KeyConstants;

public class MainILibActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Intent intent = new Intent(this, PayActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(KeyConstants.INTENT_DATA_KEY_GID, "222"); // 游戏id
		bundle.putString(KeyConstants.INTENT_DATA_KEY_CID, "11111"); // 渠道id
		bundle.putString(KeyConstants.INTENT_DATA_KEY_TOKEN, "token"); // token
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
