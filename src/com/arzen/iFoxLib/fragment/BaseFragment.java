package com.arzen.iFoxLib.fragment;

import android.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arzen.iFoxLib.R;
import com.encore.libs.utils.NetWorkUtils;

public class BaseFragment extends Fragment {

	// 出错刷新按钮
	private OnRefreshClickListener mOnRefreshClickListener;

	/**
	 * 设置是否显示网络view
	 * 
	 * @param parent
	 *            最上层的view
	 * @param containerView
	 *            容器view 权重之间的view,loadview显示或者内容view显示
	 * @param content
	 *            出错时在界面上显示的内容 如果content == null 则当前是判断有没有网络的情况 否则是其他异常情况
	 * @return 是否有有网络
	 */
	public boolean setErrorVisibility(View parentView, View containerView, String content) {
		if (parentView == null || containerView == null) {
			return true;
		}
		View mNotNetWorkView = parentView.findViewById(R.id.notNetWork);
		Button mBtnRefresh = (Button) parentView.findViewById(R.id.btnRefresh);
		ImageView imageView = (ImageView) parentView.findViewById(R.id.imgErrorView);
		TextView textView = (TextView) parentView.findViewById(R.id.tvErrorContent);
		textView.setText((content == null || content.equals("")) ? getString(R.string.not_network) : content);

		imageView.setImageResource(R.drawable.ic_launcher);
		mBtnRefresh.setOnClickListener(mOnClickListener);

		if (NetWorkUtils.isNetworkAvailable(getActivity()) && content == null) {
			containerView.setVisibility(View.VISIBLE);
			mNotNetWorkView.setVisibility(View.GONE);
			return true;
		} else {
			containerView.setVisibility(View.GONE);
			mNotNetWorkView.setVisibility(View.VISIBLE);
			return false;
		}
	}

	/**
	 * 显示没有数据暂时的view
	 * 
	 * @param parentView
	 * @param containerView
	 */
	public void setNotDataVisibility(View parentView, View containerView) {
		if (parentView == null || containerView == null) {
			return;
		}

		View mNotNetWorkView = parentView.findViewById(R.id.notNetWork);

		Button mBtnRefresh = (Button) parentView.findViewById(R.id.btnRefresh);
		TextView textView = (TextView) parentView.findViewById(R.id.tvErrorContent);
		ImageView imageView = (ImageView) parentView.findViewById(R.id.imgErrorView);
		textView.setText("没有歌曲...刷新看看?");

		imageView.setImageResource(R.drawable.ic_launcher);
		mBtnRefresh.setOnClickListener(mOnClickListener);
		containerView.setVisibility(View.GONE);
		mNotNetWorkView.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置是否显示加载view
	 * 
	 * @param isShowLoadView
	 *            是否显示loadVIew true = 显示 .false = 不显示
	 * @param parentView
	 *            最外层view 用来获取loadView findViewById(R.id.loadingView)
	 * @param containerView
	 *            容器view 权重之间的view,loadview显示或者内容view显示
	 */
	public void setLoadingViewVisibility(boolean isShowLoadView, View parentView, View containerView) {
		setLoadingViewVisibility(isShowLoadView, parentView, containerView, false);
	}

	/**
	 * 设置是否显示加载view
	 * 
	 * @param isShowLoadView
	 *            是否显示loadVIew true = 显示 .false = 不显示
	 * @param parentView
	 *            最外层view 用来获取loadView findViewById(R.id.loadingView)
	 * @param containerView
	 *            容器view 权重之间的view,loadview显示或者内容view显示
	 */
	public void setLoadingViewVisibility(boolean isShowLoadView, View parentView, View containerView, boolean isShowBlackBG) {
		if (containerView == null || parentView == null) {
			return;
		}
		View mLoadingView = parentView.findViewById(R.id.loadingView);

		if (mLoadingView == null) {
			return;
		}

		ImageView imgLoadingViewBg = (ImageView) mLoadingView.findViewById(R.id.imgLoadingViewBg);

		if (imgLoadingViewBg != null && isShowBlackBG) {
			imgLoadingViewBg.setBackgroundResource(R.drawable.ic_launcher);
		} else {
			imgLoadingViewBg.setBackgroundDrawable(null);
		}

		TextView tvLoadingContent = (TextView) mLoadingView.findViewById(R.id.tvLoadingContent);
		tvLoadingContent.setText(R.string.loading);
		if (isShowLoadView) {
			mLoadingView.setVisibility(View.VISIBLE);
			containerView.setVisibility(View.GONE);
		} else {
			mLoadingView.setVisibility(View.GONE);
			containerView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 设置无网络下按钮点击事件
	 * 
	 * @param mOnRefreshClickListener
	 */
	public void setOnRefreshClickListener(OnRefreshClickListener mOnRefreshClickListener) {
		this.mOnRefreshClickListener = mOnRefreshClickListener;
	}

	public interface OnRefreshClickListener {
		public void onClick();
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.btnRefresh) {
				if (mOnRefreshClickListener != null) {
					mOnRefreshClickListener.onClick();
				}
			} else {
			}
		}
	};

}
