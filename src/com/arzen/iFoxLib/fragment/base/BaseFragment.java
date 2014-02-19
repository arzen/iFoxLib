//package com.arzen.iFoxLib.fragment.base;
//
//import android.app.Activity;
//import android.app.Fragment;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.arzen.iFoxLib.R;
//import com.arzen.iFoxLib.setting.KeyConstants;
//import com.arzen.iFoxLib.utils.CommonUtil;
//import com.baidu.mobstat.StatService;
//import com.encore.libs.utils.Log;
//import com.encore.libs.utils.NetWorkUtils;
//
//public abstract class BaseFragment extends Fragment {
//
//	// 加载更多view
//	private View mFooterView = null;
//	// 用于是否显示moreItemView
//	private View mMoreItemView = null;
//
//	public static Boolean mIsDebugOpen;
//
//	public static boolean mIsInitChannel = false;
//
//	/**
//	 * 请求超时msg
//	 */
//	public static final int MESSAGE_TIME_OUT = 0x3588;
//	/**
//	 * 请求失败
//	 */
//	public static final int MESSAGE_REQUEST_FAIL = 0X3599;
//
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onActivityCreated(savedInstanceState);
//
//		Bundle mBundle = getArguments();
//
//		if (mIsDebugOpen == null) {
//			mIsDebugOpen = CommonUtil.getDebugModel(getActivity().getApplicationContext());
//		}
//
//		if (mBundle != null && !mIsInitChannel) {
//			mIsInitChannel = true;
//			String mCid = mBundle.getString(KeyConstants.INTENT_DATA_KEY_BAIDU_CID);
//			StatService.setAppChannel(getActivity().getApplicationContext(), mCid, true);
//			StatService.setDebugOn(mIsDebugOpen);
//		}
//
//		Log.DEBUG = mIsDebugOpen;
//	}
//
//	private Handler mBaseHandler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.what) {
//			case MESSAGE_REQUEST_FAIL:
//				Toast.makeText(getActivity(), (String) msg.obj, Toast.LENGTH_LONG).show();
//				break;
//			}
//		};
//	};
//
//	public synchronized Handler getMainHandler() {
//		return mBaseHandler;
//	}
//
////	// 出错刷新按钮
////	private OnRefreshClickListener mOnRefreshClickListener;
////
////	/**
////	 * 设置是否显示网络view
////	 * 
////	 * @param parent
////	 *            最上层的view
////	 * @param containerView
////	 *            容器view 权重之间的view,loadview显示或者内容view显示
////	 * @param content
////	 *            出错时在界面上显示的内容 如果content == null 则当前是判断有没有网络的情况 否则是其他异常情况
////	 * @return 是否有有网络
////	 */
////	public boolean setErrorVisibility(View parentView, View containerView, String content) {
////		if (parentView == null || containerView == null) {
////			return true;
////		}
////		View mNotNetWorkView = parentView.findViewById(R.id.notNetWork);
////		Button mBtnRefresh = (Button) parentView.findViewById(R.id.btnRefresh);
////		ImageView imageView = (ImageView) parentView.findViewById(R.id.imgErrorView);
////		TextView textView = (TextView) parentView.findViewById(R.id.tvErrorContent);
////		textView.setText((content == null || content.equals("")) ? getString(R.string.not_network) : content);
////
////		imageView.setImageResource(R.drawable.ic_launcher);
////		mBtnRefresh.setOnClickListener(mOnClickListener);
////
////		if (NetWorkUtils.isNetworkAvailable(getActivity()) && content == null) {
////			containerView.setVisibility(View.VISIBLE);
////			mNotNetWorkView.setVisibility(View.GONE);
////			return true;
////		} else {
////			containerView.setVisibility(View.GONE);
////			mNotNetWorkView.setVisibility(View.VISIBLE);
////			return false;
////		}
////	}
////
////	/**
////	 * 显示没有数据暂时的view
////	 * 
////	 * @param parentView
////	 * @param containerView
////	 */
////	public void setNotDataVisibility(View parentView, View containerView) {
////		if (parentView == null || containerView == null) {
////			return;
////		}
////
////		View mNotNetWorkView = parentView.findViewById(R.id.notNetWork);
////
////		Button mBtnRefresh = (Button) parentView.findViewById(R.id.btnRefresh);
////		TextView textView = (TextView) parentView.findViewById(R.id.tvErrorContent);
////		ImageView imageView = (ImageView) parentView.findViewById(R.id.imgErrorView);
////		textView.setText("没有排行榜数据！");
////
////		// imageView.setImageResource(R.drawable.ic_launcher);
////		imageView.setVisibility(View.GONE);
////
////		mBtnRefresh.setOnClickListener(mOnClickListener);
////		containerView.setVisibility(View.GONE);
////		mNotNetWorkView.setVisibility(View.VISIBLE);
////	}
////
////	/**
////	 * 设置是否显示加载view
////	 * 
////	 * @param isShowLoadView
////	 *            是否显示loadVIew true = 显示 .false = 不显示
////	 * @param parentView
////	 *            最外层view 用来获取loadView findViewById(R.id.loadingView)
////	 * @param containerView
////	 *            容器view 权重之间的view,loadview显示或者内容view显示
////	 */
////	public void setLoadingViewVisibility(boolean isShowLoadView, View parentView, View containerView) {
////		setLoadingViewVisibility(isShowLoadView, parentView, containerView, false);
////	}
////
////	/**
////	 * 设置是否显示加载view
////	 * 
////	 * @param isShowLoadView
////	 *            是否显示loadVIew true = 显示 .false = 不显示
////	 * @param parentView
////	 *            最外层view 用来获取loadView findViewById(R.id.loadingView)
////	 * @param containerView
////	 *            容器view 权重之间的view,loadview显示或者内容view显示
////	 */
////	public void setLoadingViewVisibility(boolean isShowLoadView, View parentView, View containerView, boolean isShowBlackBG) {
////		if (containerView == null || parentView == null) {
////			return;
////		}
////		View mLoadingView = parentView.findViewById(R.id.loadingView);
////
////		if (mLoadingView == null) {
////			return;
////		}
////
////		ImageView imgLoadingViewBg = (ImageView) mLoadingView.findViewById(R.id.imgLoadingViewBg);
////
////		if (imgLoadingViewBg != null && isShowBlackBG) {
////			imgLoadingViewBg.setBackgroundResource(R.drawable.ic_launcher);
////		} else {
////			imgLoadingViewBg.setBackgroundDrawable(null);
////		}
////
////		TextView tvLoadingContent = (TextView) mLoadingView.findViewById(R.id.tvLoadingContent);
////		tvLoadingContent.setText(R.string.loading);
////		if (isShowLoadView) {
////			mLoadingView.setVisibility(View.VISIBLE);
////			containerView.setVisibility(View.GONE);
////		} else {
////			mLoadingView.setVisibility(View.GONE);
////			containerView.setVisibility(View.VISIBLE);
////		}
////	}
////
////	/**
////	 * 设置无网络下按钮点击事件
////	 * 
////	 * @param mOnRefreshClickListener
////	 */
////	public void setOnRefreshClickListener(OnRefreshClickListener mOnRefreshClickListener) {
////		this.mOnRefreshClickListener = mOnRefreshClickListener;
////	}
////
////	public interface OnRefreshClickListener {
////		public void onClick();
////	}
////
////	private OnClickListener mOnClickListener = new OnClickListener() {
////
////		@Override
////		public void onClick(View v) {
////			if (v.getId() == R.id.btnRefresh) {
////				if (mOnRefreshClickListener != null) {
////					mOnRefreshClickListener.onClick();
////				}
////			} else {
////			}
////		}
////	};
//
//	/**
//	 * 跳转通用activity
//	 * 
//	 * @param bundle
//	 */
//	public void startCommonActivity(Bundle bundle) {
//		if (bundle == null) {
//			return;
//		}
//		Intent intent = new Intent(KeyConstants.ACTION_COMMON_ACTIVITY);
//		intent.putExtras(bundle);
//		startActivity(intent);
//	}
//
//	/**
//	 * 跳转通用activity
//	 * 
//	 * @param bundle
//	 */
//	public void startCommonActivityForResult(Bundle bundle, int requstCode) {
//		if (bundle == null) {
//			return;
//		}
//		Intent intent = new Intent(KeyConstants.ACTION_COMMON_ACTIVITY);
//		intent.putExtras(bundle);
//		startActivityForResult(intent, requstCode);
//	}
//
//	/**
//	 * 发送结果回调广播
//	 * 
//	 * @param bundle
//	 *            需要回调的内容
//	 * @param resultAction
//	 *            需要回调处理的acion， KeyConstants.RECEIVER_ACTION_PAY or
//	 *            KeyConstants.RECEIVER_ACTION_LOGIN
//	 */
//	public void sendResultBroadcast(Activity activity, Bundle bundle, String resultAction) {
//		if (activity != null && bundle != null && resultAction != null) {
//			bundle.putString(KeyConstants.RECEIVER_KEY_DISPOSE_ACTION, resultAction);
//
//			Intent intent = new Intent(KeyConstants.RECEIVER_RESULT_ACTION);
//			intent.putExtras(bundle);
//			activity.sendBroadcast(intent);
//		}
//	}
//
//	/**
//	 * 初始化footerView
//	 */
//	public View getFooterView() {
//		if (mFooterView == null) {
//			mFooterView = LayoutInflater.from(getActivity()).inflate(R.layout.common_footerview_more_item, null);
//		}
//		mMoreItemView = mFooterView.findViewById(R.id.lin_more);
//		mMoreItemView.setEnabled(false);
//		setFooterViewVisibility(View.GONE);
//		return mFooterView;
//	}
//
//	/**
//	 * 设置是否显示footerView
//	 * 
//	 * @param visibility
//	 */
//	public void setFooterViewVisibility(int visibility) {
//		if (mMoreItemView != null)
//			mMoreItemView.setVisibility(visibility);
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		StatService.onResume(this);
//	}
//
//	@Override
//	public void onPause() {
//		super.onPause();
//		StatService.onPause(this);
//	}
//	
//	
//	/**
//	 * 发送消息
//	 * @param what
//	 * @param object
//	 */
//	public void sendErrorMessage( Object object){
//		Message message = new Message();
//		message.what = MESSAGE_REQUEST_FAIL;
//		message.obj = object;
//		mBaseHandler.sendMessage(message);
//	}
//	/**
//	 * 发送消息
//	 * @param handler
//	 * @param what
//	 * @param object
//	 */
//	public void sendMessage(Handler handler,int what, Object object){
//		Message message = new Message();
//		message.what = what;
//		message.obj = object;
//		handler.sendMessage(message);
//	}
//
//	/**
//	 * 后退点击事件
//	 * 
//	 * @return
//	 */
//	public abstract boolean onKeyDown(Activity activity, Integer keyCode, KeyEvent event);
//}
