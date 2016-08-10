package com.cnziz.updatelib.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import com.cnziz.updatelib.utils.LogUtils;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;

public class DownFileThread implements Runnable {
	public final static int DOWNLOAD_COMPLETE = -2;
	public final static int DOWNLOAD_FAIL = -1;
	public final static String TAG = "DownFileThread";
	Handler mHandler; // �����Handler,������Activity��service֪ͨ���ؽ���
	String urlStr; // ����URL
	File apkFile; // �ļ�����·��
	boolean isFinished; // �����Ƿ����
	boolean interupted = false; // �Ƿ�ǿ��ֹͣ�����߳�

	public DownFileThread(Handler handler, String urlStr, String filePath) {
		LogUtils.i(TAG, urlStr);
		this.mHandler = handler;
		this.urlStr = urlStr;
		apkFile = new File(filePath);
		isFinished = false;
	}

	public File getApkFile() {
		if (isFinished)
			return apkFile;
		else
			return null;
	}

	public boolean isFinished() {
		return isFinished;
	}

	/**
	 * ǿ����ֹ�ļ�����
	 */
	public void interuptThread() {
		interupted = true;
	}

	@SuppressLint("NewApi")
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			java.net.URL url = null;
			HttpURLConnection conn = null;
			InputStream iStream = null;
			// if (DEVELOPER_MODE)
			{
				StrictMode
						.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
								.detectDiskReads().detectDiskWrites()
								.detectNetwork() // or .detectAll() for all
													// detectable problems
								.penaltyLog().build());
				StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
						.detectLeakedSqlLiteObjects()
						.detectLeakedClosableObjects().penaltyLog()
						.penaltyDeath().build());
			}
			try {
				url = new java.net.URL(urlStr);
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5 * 1000);
				conn.setReadTimeout(20 * 1000);
				iStream = conn.getInputStream();
			} catch (MalformedURLException e) {
				LogUtils.e(TAG, "MalformedURLException");
				e.printStackTrace();
			} catch (Exception e) {
				LogUtils.e(TAG, "���������ʧ��");
				e.printStackTrace();
			}
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(apkFile);
			} catch (FileNotFoundException e) {
				LogUtils.i(TAG, "��������ʧ�ܣ�new FileOutputStream(apkFile);");
				e.printStackTrace();
			}
			BufferedInputStream bis = new BufferedInputStream(iStream);
			byte[] buffer = new byte[1024];
			int len;
			// ��ȡ�ļ��ܳ���
			int length = conn.getContentLength();
			double rate = (double) 100 / length; // ������ת��Ϊ100
			int timeLoad = length/100/1024;
			int total = 0;
			int times = 0;// ���ø���Ƶ�ʣ�Ƶ�������գ��̻߳ᵼ��ϵͳ����
			try {
				LogUtils.e("threadStatus", "��ʼ����");
				while (false == interupted && ((len = bis.read(buffer)) != -1)) {
					fos.write(buffer, 0, len);
					// ��ȡ�Ѿ���ȡ����
					total += len;
					int p = (int) (total * rate);
					// Log.e("num", rate + "," + total + "," + p);
					if (times >= timeLoad || p == 100) {
						/*
						 * ���Ƿ�ֹƵ���ظ���֪ͨ��������ϵͳ�������������� �ǳ���Ҫ����������
						 */
						LogUtils.e("time", String.valueOf(times));
						times = 0;
						Message msg = Message.obtain();
						msg.what = p;
						mHandler.sendMessage(msg);
					}
					times++;
				}
				fos.close();
				bis.close();
				iStream.close();
				if (total == length) {
					isFinished = true;
					mHandler.sendEmptyMessage(DOWNLOAD_COMPLETE);
					LogUtils.e(TAG, "������ɽ���");
					return;
				}
				LogUtils.e(TAG, "ǿ����;����");
				// mhandler.sendEmptyMessage(4);
			} catch (IOException e) {
				LogUtils.e(TAG, "�쳣��;����");
				mHandler.sendEmptyMessage(DOWNLOAD_FAIL);
				e.printStackTrace();
			}
		} else {
			LogUtils.e(TAG, "�ⲿ�洢�������ڣ�����ʧ�ܣ�");
			mHandler.sendEmptyMessage(DOWNLOAD_FAIL);
		}
	}
}
