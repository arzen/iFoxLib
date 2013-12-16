package com.arzen.iFoxLib;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.arzen.iFoxLib.activity.PayActivity;
import com.arzen.iFoxLib.contacts.Contact;
import com.arzen.iFoxLib.contacts.ContactUtils;
import com.arzen.iFoxLib.contacts.ContactUtils.ContactCallBack;
import com.arzen.iFoxLib.download.DownloadManager;
import com.arzen.iFoxLib.setting.KeyConstants;
import com.encore.libs.http.download.DownloadBean;
import com.encore.libs.http.download.DownloadTaskManager;
import com.encore.libs.http.download.DownloadTaskManager.OnDownloadListener;
import com.encore.libs.utils.Log;

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
		bundle.putString(KeyConstants.INTENT_DATA_KEY_TOKEN, "token"); //
		intent.putExtras(bundle);
		startActivity(intent);

		upLoadContacts(getApplicationContext(), "123123");

		testDownLoad();
	}

	public void testDownLoad() {
//		String downloadUrl = "http://hot.m.shouji.360tpcdn.com/131128/37c327b4c2f2940bdc20ed4a71f8f55e/com.qvod.player_3230.apk";
//		// 启动下载
//		DownloadManager.downloadFile(this, downloadUrl, "快播", 123);
//		
//		String dowloadUrl2 = "http://file.liqucn.com/upload/2013/wangyou/JIUYOU-IamMT_200100500_uc_3.1.0.2_3102_33322_1456171eb494-20131121.apk";
//		DownloadManager.downloadFile(this, dowloadUrl2, "我叫mt", 222);
	}

	/**
	 * 上传通讯录
	 */
	public void upLoadContacts(final Context context, final String token) {
		new Thread() {
			public void run() {
				Log.d("Contact", "----- getContactCache ------");
				// 是否有缓存
				final ArrayList<Contact> cacheContacts = ContactUtils.getAllContactsByCache(context);
				if (cacheContacts != null)
					Log.d("Contact", "getContactCache size:" + cacheContacts.size() + "");

				Log.d("Contact", "getAllSystemConcatsDatas()");
				// 读取本地通讯录
				ContactUtils.getAllConcatsDatas(context, new ContactCallBack() {

					@Override
					public void onCallBack(ArrayList<Contact> localContacts) {
						// TODO Auto-generated method stub
						Log.d("Contact", "getAllSystemConcatsDatas size" + localContacts == null ? "0" : localContacts.size() + "");
						if (localContacts != null && localContacts.size() != 0) {
							ArrayList<Contact> upLoadContacts = null;
							// 本地缓存是否存在
							if (cacheContacts != null) {
								// 对比需要上传的通讯录
								upLoadContacts = ContactUtils.getUpLoadContacts(localContacts, cacheContacts);
							} else { // 缓存是空，全部上传
										// 保存缓存,上传通讯录
								upLoadContacts = localContacts;
								ContactUtils.saveContacts(getApplicationContext(), localContacts);
							}

							ContactUtils.upLoadContacts(context, token, upLoadContacts);
						}
					}
				});
			};
		}.start();
	}
}
