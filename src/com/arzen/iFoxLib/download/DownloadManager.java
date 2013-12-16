package com.arzen.iFoxLib.download;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.arzen.iFoxLib.utils.CommonUtil;
import com.arzen.iFoxLib.utils.MD5Util;
import com.arzen.iFoxLib.utils.MsgUtil;
import com.encore.libs.http.download.DownloadBean;
import com.encore.libs.http.download.DownloadTaskManager;
import com.encore.libs.http.download.DownloadTaskManager.OnDownloadListener;
import com.encore.libs.utils.Log;

public class DownloadManager {

	/**
	 * sdcard 下目录
	 */
	private static final String DOWNLOAD_DECRTOY = "download";

	private static DownloadTaskManager mDownloadTaskManager;

//	private static NotificationManager notificationMrg;

	/**
	 * 下载文件
	 * 
	 * @param downloadUrl
	 */
	public static void downloadFile(final Context context, String downloadUrl,final String name,final int id) {
		if (context == null || downloadUrl == null || downloadUrl.equals("")) {
			return;
		}
//		notificationMrg = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		if (mDownloadTaskManager == null) {
			mDownloadTaskManager = new DownloadTaskManager();
		}

		List<DownloadBean> downloadBeans = mDownloadTaskManager.getAllDownloads();
		if (downloadBeans != null && downloadBeans.size() > 0) {
			for (int i = 0; i < downloadBeans.size(); i++) {
				if (downloadBeans.get(i).downloadUrl.equals(downloadUrl)) {
					MsgUtil.msg("已在下载队列", context);
					return;
				}
			}
		}

		String fileName = MD5Util.getMD5String(downloadUrl);
		File file = CommonUtil.getDiskCacheDir(context, DOWNLOAD_DECRTOY);
		final String path = file.getAbsolutePath() + File.separator + fileName + ".apk"; // 本地路径

		DownloadBean downloadBean = new DownloadBean();

		downloadBean.downloadUrl = downloadUrl;
		downloadBean.savePath = path; // 保存路径
		File downloadFile = new File(path);
		long offset = 0;
//		if (downloadFile.exists()) {
//			offset = downloadFile.length(); // 断点续传
//		}
		downloadBean.downloadOffset = offset;

		mDownloadTaskManager.addDownload(downloadBean);
		mDownloadTaskManager.setOnDownloadListener(new OnDownloadListener() {

			@Override
			public void onDownloadSuccess(DownloadBean bean) {
				// TODO Auto-generated method stub
				Log.d("download", "downloadSuccess()");
				
				 Intent intent = new Intent(Intent.ACTION_VIEW);  
			        intent.setDataAndType(Uri.parse("file://"  
			                + path),  
			                "application/vnd.android.package-archive");  
			        context.startActivity(intent);  
			}

			@Override
			public void onDownloadStop(DownloadBean bean) {
				// TODO Auto-generated method stub
				Log.d("download", "onDownloadStop()");
			}

			@Override
			public void onDownloadStart(DownloadBean bean) {
				// TODO Auto-generated method stub
				Log.d("download", "onDownloadStart()");
				DownloadNotification.getInstence(context).setup(context, id,name);
//				showNotification(context);
			}

			@Override
			public void onDownloadProgress(int progress, DownloadBean bean) {
				// TODO Auto-generated method stub
				Log.d("download", "onDownloadProgress() 进度:" + progress);
				DownloadNotification.getInstence(context).update(context,id, name, progress + "%", false);
			}

			@Override
			public void onDownloadPrepare(DownloadBean bean) {
				// TODO Auto-generated method stub
				Log.d("download", "downloadSuccess()");
			}

			@Override
			public void onDownloadError(int state, DownloadBean bean) {
				// TODO Auto-generated method stub
				Log.d("download", "onDownloadError() :" + state);
			}
		});
		mDownloadTaskManager.startDownload(context, null);
	}

//	private static Notification displayNotificationMessage(Notification notification, int count, int flag, String url, String filename) {
//		RemoteViews contentView1 = notification.contentView;
//		Log.i("TAG", "updata   flag==  " + flag);
//		Log.i("TAG", "updata   count==  " + count);
//		Log.i("TAG", "updata   filename==  " + filename);
//		contentView1.setTextViewText(R.id.n_title, filename);
//		contentView1.setTextViewText(R.id.n_text, "当前进度：" + count + "% ");
//		contentView1.setProgressBar(R.id.n_progress, 100, count, false);
//		notification.contentView = contentView1;
//		// 提交一个通知在状态栏中显示。如果拥有相同标签和相同id的通知已经被提交而且没有被移除，该方法会用更新的信息来替换之前的通知。
//		notificationMrg.notify(flag, notification);
//		return notification;
//	}
//
//	public static void showNotification(Context context) {
//		Intent notificationIntent = new Intent(context, context.getClass());
//		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		// addflag设置跳转类型
//		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
//		// 创建Notifcation对象，设置图标，提示文字
//		long number = 100;
//		Notification notification = new Notification(R.drawable.ic_launcher, "DnwoLoadManager", number);// 设定Notification出现时的声音，一般不建议自定义
//																										// System.currentTimeMillis()
//		notification.flags |= Notification.FLAG_ONGOING_EVENT;// 出现在 “正在运行的”栏目下面
//		RemoteViews contentView1 = new RemoteViews(context.getPackageName(), R.layout.notification_version);
//		contentView1.setTextViewText(R.id.n_title, "准备下载");
//		contentView1.setTextViewText(R.id.n_text, "当前进度：" + 0 + "% ");
//		contentView1.setProgressBar(R.id.n_progress, 100, 0, false);
//		notification.contentView = contentView1;
//		notification.contentIntent = contentIntent;
//		
//		notificationMrg.notify();
//	}
}
