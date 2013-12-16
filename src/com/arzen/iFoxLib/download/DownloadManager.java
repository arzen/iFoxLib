package com.arzen.iFoxLib.download;

import java.io.File;
import java.util.List;

import android.content.Context;

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

	/**
	 * 下载文件
	 * 
	 * @param downloadUrl
	 */
	public static void downloadFile(Context context, String downloadUrl) {
		if (context == null || downloadUrl == null || downloadUrl.equals("")) {
			return;
		}
		
		if (mDownloadTaskManager == null) {
			mDownloadTaskManager = new DownloadTaskManager();
		}
		
		List<DownloadBean> downloadBeans = mDownloadTaskManager.getAllDownloads();
		if(downloadBeans != null && downloadBeans.size() > 0){
			for(int i = 0;i<downloadBeans.size();i++){
				if(downloadBeans.get(i).downloadUrl.equals(downloadUrl)){
					MsgUtil.msg("已在下载队列", context);
					return;
				}
			}
		}
		

		String fileName = MD5Util.getMD5String(downloadUrl);
		File file = CommonUtil.getDiskCacheDir(context, DOWNLOAD_DECRTOY);
		String path = file.getAbsolutePath() + File.separator + fileName + ".apk"; // 本地路径

		DownloadBean downloadBean = new DownloadBean();

		downloadBean.downloadUrl = downloadUrl;
		downloadBean.savePath = path; // 保存路径
		File downloadFile = new File(path);
		long offset = 0;
		if (downloadFile.exists()) {
			offset = downloadFile.length(); //断点续传
		}
		downloadBean.downloadOffset = offset;
		
		mDownloadTaskManager.addDownload(downloadBean);
		mDownloadTaskManager.setOnDownloadListener(new OnDownloadListener() {

			@Override
			public void onDownloadSuccess(DownloadBean bean) {
				// TODO Auto-generated method stub
				Log.d("download", "downloadSuccess()");
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
			}

			@Override
			public void onDownloadProgress(int progress, DownloadBean bean) {
				// TODO Auto-generated method stub
				Log.d("download", "onDownloadProgress() 进度:" + progress);
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

	/**
	 * 保存缓存
	 * 
	 * @param bean
	 * @param path
	 */
	private static void saveCache(DownloadBean bean, String path) {
		// CommonUtil.object2File(bean, path);
	}
}
