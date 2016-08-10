package com.cnziz.updatelib;

import com.cnziz.updatelib.download.DownloadServices;
import com.cnziz.updatelib.utils.Listener.onUpdateListener;

import android.content.Context;
import android.content.Intent;

public class UpdateUtils {
	
	public static UpdateUtils mUpdateUtils;
	
	public static UpdateUtils getInstanse(){
		if (null == mUpdateUtils) {
			mUpdateUtils = new UpdateUtils();
		}
		return mUpdateUtils;
	}
	
	public void update(Context context, String appInfo, onUpdateListener mUpdateListener){
		String url = "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk";
		String filePath = "/sdcard/download";
		download(context, url, filePath, mUpdateListener);
	}
	/**
	 * 下载更新
	 * @param context
	 * @param url  			下载地址
	 * @param filePath		保存目录
	 * @param mUpdateListener
	 */
	public void download(Context context, String url, String filePath, onUpdateListener mUpdateListener){
		Intent intent = new Intent(context, DownloadServices.class);
		intent.putExtra("url", url);
		intent.putExtra("file_path", filePath);
		context.startService(intent);
	}
}
