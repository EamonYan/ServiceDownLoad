package com.cnziz.updatelib.download;

import java.io.File;
import java.io.IOException;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.cnziz.updatelib.R;
import com.cnziz.updatelib.utils.Listener.onUpdateListener;
import com.cnziz.updatelib.utils.LogUtils;

public class DownloadServices extends Service {
	private final static int DOWNLOAD_COMPLETE = -2;
	private final static int DOWNLOAD_FAIL = -1;

	// 自定义通知栏类
	MyNotification myNotification;

	String filePathString; // 下载文件绝对路径(包括文件名)

	// 通知栏跳转Intent
	private Intent updateIntent = null;
	
	private PendingIntent updatePendingIntent = null;

	DownFileThread downFileThread; // 自定义文件下载线程
	
	private onUpdateListener mUpdateListener;

	private Handler updateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_COMPLETE:
				// 点击安装PendingIntent
				Uri uri = Uri.fromFile(downFileThread.getApkFile());
				Intent installIntent = new Intent(Intent.ACTION_VIEW);
				installIntent.setDataAndType(uri,
						"application/vnd.android.package-archive");
				installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(installIntent);
//				updatePendingIntent = PendingIntent.getActivity(
//						DownloadServices.this, 0, installIntent, 0);
//				myNotification.changeContentIntent(updatePendingIntent);
//				myNotification.notification.defaults = Notification.DEFAULT_SOUND;// 铃声提醒
//				myNotification.changeNotificationText("下载完成，请点击安装！");

				// 停止服务
				// myNotification.removeNotification();
				stopSelf();
				break;
			case DOWNLOAD_FAIL:
				// 下载失败
				// myNotification.changeProgressStatus(DOWNLOAD_FAIL);
				myNotification.changeNotificationText("文件下载失败！");
				mUpdateListener.downloadFail();
				stopSelf();
				break;
			default: // 下载中
				LogUtils.e("service", "index" + msg.what);
				// myNotification.changeNotificationText(msg.what+"%");
				myNotification.changeProgressStatus(msg.what);
			}
		}
	};

	public DownloadServices() {
		// TODO Auto-generated constructor stub
		// mcontext=context;
		LogUtils.e("service", "DownloadServices1");

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		LogUtils.e("service", "onCreate");
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		LogUtils.e("service", "onDestroy");
		if (downFileThread != null)
			downFileThread.interuptThread();
//		if (null != myNotification) {
//			myNotification.removeNotification();
//		}
		stopSelf();
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		LogUtils.e("service", "onStartCommand");
		String url = intent.getStringExtra("url");
		String filePath = intent.getStringExtra("file_path");
		mUpdateListener = (onUpdateListener) intent.getParcelableExtra("listener");
		// updateIntent = new Intent(this, MainActivity.class);
		// PendingIntent updatePendingIntent = PendingIntent.getActivity(this,
		// 0,
		// updateIntent, 0);
		myNotification = new MyNotification(this, updatePendingIntent, 1);

		// myNotification.showDefaultNotification(R.drawable.ic_launcher, "测试",
		// "开始下载");
		myNotification.showCustomizeNotification(R.drawable.ic_launcher,
				"测试下载", R.layout.notification);
		if (!filePath.endsWith("/")) {
			filePath = filePath +"/";
		}
		File path = new File(filePath);
		if (!path.exists()) {
			path.mkdir();
		}
		String[] s = url.split("/");
		filePathString = filePath + s[s.length-1];
		File file = new File(filePathString);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
		downFileThread = new DownFileThread(
				updateHandler,
				url,
				filePathString);
		new Thread(downFileThread).start();

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		LogUtils.e("service", "onStart");
		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		LogUtils.e("service", "onBind");
		return null;
	}

}