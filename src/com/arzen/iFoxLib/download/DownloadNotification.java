package com.arzen.iFoxLib.download;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import com.arzen.iFoxLib.R;

/**
 * 系统状态通知栏消息管理
 * 
 * @author looming
 * 
 */
public class DownloadNotification {
	private PendingIntent mContentIntent;
	private Notification mNotification;
	private static DownloadNotification mInstence = null;
	private NotificationManager notificationManager;

	private DownloadNotification(Context context) {
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		initContentIntent();
	}

	public static DownloadNotification getInstence(Context context) {
		if (mInstence == null) {
			mInstence = new DownloadNotification(context);
		}
		return mInstence;
	};

	private void initContentIntent() {
		int icon = R.drawable.ic_launcher;
		// Intent intent = new Intent(IntentAction.INTENT_BASE_ACTIVITY);
		// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
		// Intent.FLAG_ACTIVITY_NEW_TASK);
		// intent = new Intent(Intent.ACTION_MAIN);
		// intent.addCategory(Intent.CATEGORY_LAUNCHER);
		// intent.setClass(mContext, MainActivity.class);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
		// Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		// mContentIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
		mNotification = new Notification(icon, "", System.currentTimeMillis());
	}

	public void setup(Context context, int notification_id, String noticeTitle) {
		mNotification.tickerText = "开始下载:" + noticeTitle;
		mNotification.setLatestEventInfo(context, "下载 " + noticeTitle, "", mContentIntent);
		notificationManager.notify(notification_id, mNotification);
	}

	public void update(Context context, int notification_id, String noticeTitle, String noticeContent) {
		update(context, notification_id, noticeTitle, noticeContent, false);
	}

	/**
	 * 
	 * @param noticeTitle
	 *            标题
	 * @param noticeContent
	 *            内容
	 * @param withAppName
	 * 
	 */
	public void update(Context context, int notification_id, String noticeTitle, String noticeContent, boolean withAppName) {
		if (withAppName) {
			noticeTitle = context.getString(R.string.app_name) + " - " + noticeTitle;
		}

		// mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
		// notificationManager.cancel(NOTIFICATION_ID);
		// mNotification.tickerText = "下载进度:" + noticeContent;
		mNotification.setLatestEventInfo(context, "下载 " + noticeTitle, "当前进度:" + noticeContent, mContentIntent);
		notificationManager.notify(notification_id, mNotification);
	}

	public void updateNotify(int current, int total) {
		// mNotification.contentView.setProgressBar(R.id.downpb, (int) total,
		// (int) current, false);
		// if (total != 0) {
		// if ((current * 100 / total) % 100 == 10)
		// mNotification.contentView.setTextViewText(R.id.downCount, current *
		// 100 / total + "%");
		// }
		// notificationManager.notify(NOTIFICATION_ID, mNotification);
	}

	public void update(Context context, int notification_id, String noticeContent) {
		notificationManager.cancel(notification_id);
		mNotification.setLatestEventInfo(context, context.getResources().getString(R.string.app_name), noticeContent, mContentIntent);
		notificationManager.notify(notification_id, mNotification);
	}

	public void cancel(int notification_id) {
		notificationManager.cancel(notification_id);
	}
}
