package com.arzen.iFoxLib.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.arzen.iFoxLib.R;
import com.arzen.iFoxLib.adapter.TopAdapter;
import com.arzen.iFoxLib.api.HttpIfoxApi;
import com.arzen.iFoxLib.api.HttpSetting;
import com.arzen.iFoxLib.api.OnRequestCallback;
import com.arzen.iFoxLib.bean.Invited;
import com.arzen.iFoxLib.bean.Top;
import com.arzen.iFoxLib.bean.Top.TopList;
import com.arzen.iFoxLib.contacts.Contact;
import com.arzen.iFoxLib.contacts.ContactUtils;
import com.arzen.iFoxLib.fragment.base.ProgressFragment;
import com.arzen.iFoxLib.setting.KeyConstants;
import com.arzen.iFoxLib.utils.MsgUtil;
import com.baidu.mobstat.StatService;
import com.encore.libs.http.HttpConnectManager;
import com.encore.libs.http.OnRequestListener;
import com.encore.libs.utils.Log;
import com.encore.libs.utils.NetWorkUtils;

public class TopFragment extends ProgressFragment {

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

	// fragment 主体内容view
	private View mContentView;

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
		mContentView = inflater.inflate(R.layout.fragment_top, null);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// 设置主内容
		setContentView(mContentView);
		// 初始化ui
		initUI(mContentView);
		// 加载本地通讯录
		loadContacts();
		// 初始化内容
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
		mListView.setOnItemClickListener(mOnItemClickListener);

		// 异常情况下点击刷新按钮处理
		// setOnRefreshClickListener(mOnRefreshClickListener);
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
			// 没有网络
			if (!NetWorkUtils.isNetworkAvailable(getActivity())) {
				setContentError(true, getActivity().getString(R.string.not_network));
				return;
			}
			requestTopListData(1, false);
		}
	}

	public void requestTopListData(int pageNumber, final boolean isLoadMore) {
		if (!mIsRequesEnd) {
			return;
		}
		mIsRequesEnd = false; // 改变正在请求的标识

		// 请求
		request(isLoadMore, pageNumber);
	}

	/**
	 * 请求数据
	 * 
	 * @param url
	 */
	public void request(boolean isLoadMore, int pageNumber) {
		if (!isLoadMore) {
			// 改变当前为loading状态
			setContentShown(false);
		}

		String gid = mBundle.getString(KeyConstants.INTENT_DATA_KEY_GID);
		String token = mBundle.getString(KeyConstants.INTENT_DATA_KEY_TOKEN);
		String cid = mBundle.getString(KeyConstants.INTENT_DATA_KEY_CID);
		String clientId = mBundle.getString(KeyConstants.INTENT_DATA_KEY_CLIENTID);
		String clientSecret = mBundle.getString(KeyConstants.INTENT_DATA_KEY_CLIENTSECRET);
		HttpIfoxApi.requestTopList(getActivity(), gid, token, pageNumber, cid, clientId, clientSecret, new OnTopRequestCallBack(isLoadMore));
	}

	public class OnTopRequestCallBack implements OnRequestCallback {
		// 当前是否正在做加载更多的操作
		private boolean mIsLoadMore = false;

		public OnTopRequestCallBack(boolean isLoadMore) {
			mIsLoadMore = isLoadMore;
		}

		@Override
		public void onSuccess(Object result) {
			// TODO Auto-generated method stub
			mIsRequesEnd = true;
			disposeSuccess(mIsLoadMore, result);
		}

		@Override
		public void onFail(String msg, int state) {
			// TODO Auto-generated method stub
			mIsRequesEnd = true;

			disposeFail(mIsLoadMore, msg);
		}

	}

	/**
	 * 处理请求成功
	 * 
	 * @param mIsLoadMore
	 * @param result
	 */
	public void disposeSuccess(final boolean mIsLoadMore, final Object result) {
		getMainHandler().post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (!isAdded()) // fragment 已退出,返回
				{
					return;
				}
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
						setContentError(true, "没有数据");
					}
				}
				if (!mIsLoadMore) {
					// 显示主内容
					setContentShown(true);
				}
			}
		});

	}

	/**
	 * 处理失败
	 * 
	 * @param mIsLoadMore
	 * @param msg
	 */
	public void disposeFail(final boolean mIsLoadMore, final String msg) {
		getMainHandler().post(new Runnable() {

			@Override
			public void run() {
				if (!isAdded()) // fragment 已退出,返回
				{
					return;
				}
				if (!mIsLoadMore) {// 是否加载下一页的请求
					setContentError(true, msg);
				} else {
					sendErrorMessage(msg);
					setFooterViewVisibility(View.GONE);// 隐藏footerView
				}
				mIsHasNext = true;
				mIsRequesEnd = false;
			}
		});
	}

	// /**
	// * 刷新按钮
	// */
	// private OnRefreshClickListener mOnRefreshClickListener = new
	// OnRefreshClickListener() {
	//
	// @Override
	// public void onClick() {
	// initData(null); // 请求数据
	// }
	// };

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
		HttpIfoxApi.requestInviteData(getActivity(), token, gid, new OnRequestCallback() {

			@Override
			public void onSuccess(final Object result) {
				// TODO Auto-generated method stub
				getMainHandler().post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// TODO Auto-generated method stub
						if (!isAdded()) // fragment 已退出,返回
						{
							return;
						}
						Invited invited = (Invited) result;
						if (invited.getCode() == HttpSetting.RESULT_CODE_OK && invited.getData() != null) {
							mInviteString = invited.getData().getMsg();
						}
						if (mInviteString == null || mInviteString.equals("")) {
							getInveteData();
						}
					}
				});
			}

			@Override
			public void onFail(String msg, int state) {
				// TODO Auto-generated method stub
//				sendErrorMessage(msg);
			}
		});
	}

	public OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
			// TODO Auto-generated method stub
			if (mTopAdapter != null && mAllDatas.size() != 0) {
				StatService.onEvent(getActivity(), "LEADERBOARD_PLAYING", "");
				TopList topList = mAllDatas.get(position);

				String downloadUrl = topList.dl;

				if (downloadUrl != null && !downloadUrl.equals("")) {
					download(topList, position + topList.hashCode());
				}
			}
		}

	};

	public void download(final TopList topList, final int notificationId) {
		new AlertDialog.Builder(getActivity()).setTitle("下载提示").setMessage("是否下载:" + topList.getPlay_game()).setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(KeyConstants.RECEIVER_DOWNLOAD_ACTION);
				String downloadUrl = topList.getDl();

				intent.putExtra("downloadUrl", downloadUrl);
				intent.putExtra("gameName", topList.getPlay_game());
				intent.putExtra("id", notificationId);

				getActivity().sendBroadcast(intent);
			}
		}).setNegativeButton("取消", null).create().show();
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
