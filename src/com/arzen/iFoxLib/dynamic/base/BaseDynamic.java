package com.arzen.iFoxLib.dynamic.base;

import com.arzen.iFoxLib.setting.KeyConstants;
import com.arzen.iFoxLib.utils.CommonUtil;
import com.baidu.mobstat.StatService;
import com.encore.libs.utils.Log;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public abstract class BaseDynamic {

	private Activity mActivity;

	public Bundle mBundle;

	public static Boolean mIsDebugOpen;

	public static boolean mIsInitChannel = false;
	/**
	 * 请求超时msg
	 */
	public static final int MESSAGE_TIME_OUT = 0x3588;
	/**
	 * 请求失败
	 */
	public static final int MESSAGE_REQUEST_FAIL = 0X3599;

	public void onCreate(Activity activity, Bundle bundle) {
		mActivity = activity;
		mBundle = bundle;

		if (mIsDebugOpen == null) {
			mIsDebugOpen = CommonUtil.getDebugModel(getActivity().getApplicationContext());
		}

		if (mBundle != null && !mIsInitChannel) {
			mIsInitChannel = true;
			String mCid = mBundle.getString(KeyConstants.INTENT_DATA_KEY_BAIDU_CID);
			StatService.setAppChannel(getActivity().getApplicationContext(), mCid, true);
			StatService.setDebugOn(mIsDebugOpen);
		}

		Log.DEBUG = mIsDebugOpen;
	};

	/**
	 * 初始化获取view在此方法执行,获取到view后返回到主程序加载
	 * 
	 * @param activity
	 * @return
	 */
	public abstract View onCreateView(Activity activity);

	/**
	 * 模仿fragment,的onActivityCreate方法
	 */
	public abstract void onActivityCreate();

	/**
	 * 生命周期控制
	 * onResume
	 */
	public void onResume() {
//		StatService.onResume(mActivity);
	};

	/**
	 * 生命周期控制
	 * onPause
	 */
	public void onPause() {
//		StatService.onPause(mActivity);
	};

	/**
	 * 生命周期控制
	 * onDestroy
	 */
	public void onDestroy() {
	};

	/**
	 * 生命周期控制
	 * onStop
	 */
	public void onStop() {
	};

	/**
	 * 生命周期控制
	 * onStart
	 */
	public void onStart() {
	};
	/**
	 * onActivityResult
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	};
	/**
	 * onKeyDown
	 * @param activity
	 * @param keyCode
	 * @param event
	 * @return
	 */
	public boolean onKeyDown(Activity activity, Integer keyCode, KeyEvent event) {
		return true;
	};

	public Activity getActivity() {
		return mActivity;
	}

	/**
	 * 发送消息
	 * 
	 * @param what
	 * @param object
	 */
	public void sendErrorMessage(Object object) {
		Message message = new Message();
		message.what = MESSAGE_REQUEST_FAIL;
		message.obj = object;
		mBaseHandler.sendMessage(message);
	}

	/**
	 * 发送消息
	 * 
	 * @param handler
	 * @param what
	 * @param object
	 */
	public void sendMessage(Handler handler, int what, Object object) {
		Message message = new Message();
		message.what = what;
		message.obj = object;
		handler.sendMessage(message);
	}

	/**
	 * 跳转通用activity
	 * 
	 * @param bundle
	 */
	public void startCommonActivity(Bundle bundle) {
		if (bundle == null || mActivity == null) {
			return;
		}
		Intent intent = new Intent(KeyConstants.ACTION_COMMON_ACTIVITY);
		intent.putExtras(bundle);
		mActivity.startActivity(intent);
	}

	/**
	 * 跳转通用activity
	 * 
	 * @param bundle
	 */
	public void startCommonActivityForResult(Bundle bundle, int requstCode) {
		if (bundle == null || mActivity == null) {
			return;
		}
		Intent intent = new Intent(KeyConstants.ACTION_COMMON_ACTIVITY);
		intent.putExtras(bundle);
		mActivity.startActivityForResult(intent, requstCode);
	}

	/**
	 * 发送结果回调广播
	 * 
	 * @param bundle
	 *            需要回调的内容
	 * @param resultAction
	 *            需要回调处理的acion， KeyConstants.RECEIVER_ACTION_PAY or
	 *            KeyConstants.RECEIVER_ACTION_LOGIN
	 */
	public void sendResultBroadcast(Activity activity, Bundle bundle, String resultAction) {
		if (activity != null && bundle != null && resultAction != null) {
			bundle.putString(KeyConstants.RECEIVER_KEY_DISPOSE_ACTION, resultAction);

			Intent intent = new Intent(KeyConstants.RECEIVER_RESULT_ACTION);
			intent.putExtras(bundle);
			activity.sendBroadcast(intent);
		}
	}

	private Handler mBaseHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_REQUEST_FAIL:
				Toast.makeText(getActivity(), (String) msg.obj, Toast.LENGTH_LONG).show();
				break;
			}
		};
	};

	public synchronized Handler getMainHandler() {
		return mBaseHandler;
	}

	/**
	 * 判断activity是否已退出
	 * 
	 * @return
	 */
	public boolean isAdded() {
		if (mActivity != null && !mActivity.isFinishing()) {
			return true;
		}
		return false;
	}

	public String getString(int resId) {
		if (mActivity != null) {
			return mActivity.getString(resId);
		}
		return "";
	}
}
