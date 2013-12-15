package com.arzen.iFoxLib.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony.Mms;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;

import com.arzen.iFoxLib.R;
import com.arzen.iFoxLib.adapter.TopAdapter;
import com.arzen.iFoxLib.api.HttpIfoxApi;
import com.arzen.iFoxLib.api.HttpSetting;
import com.arzen.iFoxLib.bean.Invited;
import com.arzen.iFoxLib.bean.Top;
import com.arzen.iFoxLib.bean.Top.TopList;
import com.arzen.iFoxLib.contacts.Contact;
import com.arzen.iFoxLib.contacts.ContactUtils;
import com.arzen.iFoxLib.setting.KeyConstants;
import com.encore.libs.http.HttpConnectManager;
import com.encore.libs.http.OnRequestListener;
import com.encore.libs.http.Request;
import com.encore.libs.json.JsonParser;
import com.encore.libs.utils.Log;
import com.encore.libs.utils.NetWorkUtils;

public class TopFragment extends BaseFragment {

	// listView
	private ListView mListView;
	// adapter
	private TopAdapter mTopAdapter;
	// allData
	private List<TopList> mAllDatas;
	// 是否请求结束
	private boolean mIsRequesEnd = true;
	/**
	 * 页码
	 */
	public int mPageNumber = 1;
	/**
	 * 是否有下一页
	 */
	public boolean mIsHasNext = false;

	public Bundle mBundle;

	public static String mInviteString;

	HashMap<String, String> maps = new HashMap<String, String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mBundle = getArguments();
		// 获取邀请模版
		getInveteData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_top, null);
		initUI(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		loadContacts();
		initData(null);
	}

	public void loadContacts() {
		// 是否有缓存
		final ArrayList<Contact> cacheContacts = ContactUtils.getAllContactsByCache(getActivity().getApplicationContext());
		if (cacheContacts != null) {

			for (int j = 0; j < cacheContacts.size(); j++) {
				Contact cacheContact = cacheContacts.get(j);
				maps.put(cacheContact.phone, cacheContact.name);
			}
		}
	}

	/**
	 * 初始化ui
	 */
	public void initUI(View view) {
		mListView = (ListView) view.findViewById(R.id.listView);
		mListView.addFooterView(getFooterView());
		mListView.setOnScrollListener(mOnScrollListener);
		mListView.setDividerHeight(0);

		// 异常情况下点击刷新按钮处理
		setOnRefreshClickListener(mOnRefreshClickListener);
	}

	/**
	 * 初始化内容
	 * 
	 * @param datas
	 */
	public void initData(List<TopList> datas) {
		if (mListView.getAdapter() == null) {
			mTopAdapter = new TopAdapter(getActivity());
			mListView.setAdapter(mTopAdapter);
		}

		if (datas != null && datas.size() != 0) {
			if (mAllDatas == null) {
				mAllDatas = new ArrayList<TopList>();
			}
			mAllDatas.addAll(datas);
			mTopAdapter.setDatas(mAllDatas, maps);
			mTopAdapter.notifyDataSetChanged();
		} else {
			// 判断是否有网络的情况
			if (setErrorVisibility(getView(), mListView, null)) {
				// 显示loading view
				setLoadingViewVisibility(true, getView(), mListView);
				// 请求,第一次默认请求第一页
				requestTopListData(1, false);
			}
		}
	}

	public void requestTopListData(int pageNumber, final boolean isLoadMore) {
		if (!mIsRequesEnd) {
			return;
		}
		mIsRequesEnd = false; // 改变正在请求的标识

		String url = HttpSetting.IFOX_TOP_URL;
		// 请求
		request(url, isLoadMore, pageNumber);
	}

	/**
	 * 请求数据
	 * 
	 * @param url
	 */
	public void request(String url, boolean isLoadMore, int pageNumber) {
		// Request request = new Request(url);
		// request.setOnRequestListener(new OnTopRequestListener(isLoadMore));
		// request.setParser(new JsonParser(Top.class, false));
		// HttpConnectManager.getInstance(getActivity().getApplicationContext()).doGet(request);

		String gid = mBundle.getString(KeyConstants.INTENT_DATA_KEY_GID);
		String token = mBundle.getString(KeyConstants.INTENT_DATA_KEY_TOKEN);
		Log.d("Top", "gid: " + gid + "token:" + token);
		Log.d("Top", "pageNumber:" + pageNumber);
		HttpIfoxApi.requestTopList(getActivity().getApplicationContext(), gid, token, pageNumber, new OnTopRequestListener(isLoadMore));
	}

	public class OnTopRequestListener implements OnRequestListener {

		// 当前是否正在做加载更多的操作
		private boolean mIsLoadMore = false;

		public OnTopRequestListener(boolean isLoadMore) {
			mIsLoadMore = isLoadMore;
		}

		@Override
		public void onResponse(final String url, final int state, final Object result, final int type) {
			// TODO Auto-generated method stub
			mIsRequesEnd = true;
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (!isAdded()) // fragment 已退出,返回
					{
						return;
					}
					// 显示loading view
					setLoadingViewVisibility(false, getView(), mListView);
					Log.d("topFragment", "request url:" + url + " resultState :" + state + " result:" + result);
					if (state == HttpConnectManager.STATE_SUC && result != null && result instanceof Top) {
						Top top = (Top) result;
						if (top.getData() != null && top.getData().getList() != null && top.getData().getList().size() != 0) {
							// 显示数据
							initData(top.getData().getList());

							if (top.getData().getList().size() < 10) {
								mIsHasNext = false;
								setFooterViewVisibility(View.GONE);// 隐藏footerView
							} else {
								mPageNumber++;
								mIsHasNext = true;
								setFooterViewVisibility(View.VISIBLE);
							}

						} else {
							mIsHasNext = false;
							setFooterViewVisibility(View.GONE);// 隐藏footerView
							if (!mIsLoadMore) {
								// 没有数据
								setNotDataVisibility(getView(), mListView);
							}
						}
					} else if (state == HttpConnectManager.STATE_TIME_OUT) { // 请求超时
						// 隐藏loadingView,显示主体内容listView
						setLoadingViewVisibility(false, getView(), mListView);

						if (!mIsLoadMore)// 是否加载下一页的请求
							setErrorVisibility(getView(), mListView, getString(R.string.time_out));
						else {
							Toast.makeText(getActivity(), R.string.time_out, Toast.LENGTH_SHORT).show();
							setFooterViewVisibility(View.GONE);// 隐藏footerView
						}
						mIsHasNext = true;
						mIsRequesEnd = false;
					} else { // 请求失败
						// 隐藏loadingView,显示主体内容listView
						setLoadingViewVisibility(false, getView(), mListView);

						if (!mIsLoadMore)
							setErrorVisibility(getView(), mListView, getString(R.string.request_fail));
						else {
							Toast.makeText(getActivity(), R.string.request_fail, Toast.LENGTH_SHORT).show();
							setFooterViewVisibility(View.GONE);// 隐藏footerView
						}
						mIsHasNext = true;
						mIsRequesEnd = false;
					}
				}
			});
		}

	}

	/**
	 * 刷新按钮
	 */
	private OnRefreshClickListener mOnRefreshClickListener = new OnRefreshClickListener() {

		@Override
		public void onClick() {
			initData(null); // 请求数据
		}
	};

	/**
	 * listView 滚动时间监听
	 */
	public OnScrollListener mOnScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub

			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
					if (!mIsHasNext) { // 最后一页不做请求
						setFooterViewVisibility(View.GONE);
						return;
					}
					// 没有网络
					if (!NetWorkUtils.isNetworkAvailable(getActivity().getApplicationContext())) {
						Toast.makeText(getActivity(), R.string.not_network, Toast.LENGTH_SHORT).show();
						setFooterViewVisibility(View.GONE);
						return;
					}
					// 显示footerView
					setFooterViewVisibility(View.VISIBLE);
					// 请求下一页数据
					requestTopListData(mPageNumber, true);
				}
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub

		}
	};

	public void getInveteData() {
		String gid = mBundle.getString(KeyConstants.INTENT_DATA_KEY_GID);
		String token = mBundle.getString(KeyConstants.INTENT_DATA_KEY_TOKEN);
		HttpIfoxApi.requestInviteData(getActivity(), token, gid, new OnRequestListener() {

			@Override
			public void onResponse(final String url, final int state, final Object result, final int type) {
				// TODO Auto-generated method stub
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// TODO Auto-generated method stub
						if (!isAdded()) // fragment 已退出,返回
						{
							return;
						}
						// 显示loading view
						setLoadingViewVisibility(false, getView(), mListView);
						Log.d("topFragment", "request url:" + url + " resultState :" + state + " result:" + result);
						if (state == HttpConnectManager.STATE_SUC && result != null && result instanceof Invited) {
							Invited invited = (Invited) result;
							if (invited.getCode() == HttpSetting.RESULT_CODE_OK && invited.getData() != null) {
								mInviteString = invited.getData().getMsg();
							}

						} else if (state == HttpConnectManager.STATE_TIME_OUT) { // 请求超时
							Toast.makeText(getActivity(), R.string.time_out, Toast.LENGTH_SHORT).show();
						} else { // 请求失败
							Toast.makeText(getActivity(), R.string.request_fail, Toast.LENGTH_SHORT).show();
						}

						if (mInviteString == null || mInviteString.equals("")) {
							getInveteData();
						}
					}
				});
			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mInviteString = null;
	}

	@Override
	public boolean onKeyDown(Activity activity, Integer keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

}
