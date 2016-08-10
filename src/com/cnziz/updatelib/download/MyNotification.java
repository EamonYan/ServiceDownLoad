package com.cnziz.updatelib.download;

import com.cnziz.updatelib.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.widget.RemoteViews;

public class MyNotification {
	public final static int DOWNLOAD_COMPLETE = -2;
	public final static int DOWNLOAD_FAIL = -1;
	Context mContext; // Activity��Service������
	Notification notification; // notification
	NotificationManager nm;
	String titleStr; // ֪ͨ����
	String contentStr; // ֪ͨ����
	PendingIntent contentIntent; // ���֪ͨ��Ķ���
	int notificationID; // ֪ͨ��Ψһ��ʾID
	int iconID; // ֪ͨ��ͼ��
	long when = System.currentTimeMillis();
	RemoteViews remoteView = null; // �Զ����֪ͨ����ͼ

	/**
	 * 
	 * @param context
	 *            Activity��Service������
	 * @param contentIntent
	 *            ���֪ͨ��Ķ���
	 * @param id
	 *            ֪ͨ��Ψһ��ʾID
	 */
	public MyNotification(Context context, PendingIntent contentIntent, int id) {
		// TODO Auto-generated constructor stub
		mContext = context;
		notificationID = id;
		this.contentIntent = contentIntent;
		this.nm = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	/**
	 * ��ʾ�Զ���֪ͨ
	 * 
	 * @param icoId
	 *            �Զ�����ͼ�е�ͼƬID
	 * @param titleStr
	 *            ֪ͨ������
	 * @param layoutId
	 *            �Զ��岼���ļ�ID
	 */
	public void showCustomizeNotification(int icoId, String titleStr,
			int layoutId) {
		this.titleStr = titleStr;
		notification = new Notification(R.drawable.ic_launcher, titleStr, when);
		notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.contentIntent = this.contentIntent;

		// 1������һ���Զ������Ϣ���� view.xml
		// 2���ڳ��������ʹ��RemoteViews�ķ���������image��text��Ȼ���RemoteViews���󴫵�contentView�ֶ�
		if (remoteView == null) {
			remoteView = new RemoteViews(mContext.getPackageName(), layoutId);
			remoteView.setImageViewResource(R.id.ivNotification, icoId);
			remoteView.setTextViewText(R.id.tvTitle, titleStr);
			remoteView.setTextViewText(R.id.tvTip, "��ʼ����");
			remoteView.setProgressBar(R.id.pbNotification, 100, 0, false);
			notification.contentView = remoteView;
		}
		nm.notify(notificationID, notification);
	}

	/**
	 * �����Զ��岼���ļ��еĽ�������ֵ
	 * 
	 * @param p
	 *            ����ֵ(0~100)
	 */
	public void changeProgressStatus(int p) {
		if (notification.contentView != null) {
			if (p == DOWNLOAD_FAIL)
				notification.contentView.setTextViewText(R.id.tvTip, "����ʧ�ܣ� ");
			else if (p == 100)
				notification.contentView.setTextViewText(R.id.tvTip,
						"������ɣ�������װ");
			else
				notification.contentView.setTextViewText(R.id.tvTip, "����(" + p
						+ "%)");
			notification.contentView.setProgressBar(R.id.pbNotification, 100,
					p, false);
		}
		nm.notify(notificationID, notification);
	}

	public void changeContentIntent(PendingIntent intent) {
		this.contentIntent = intent;
		notification.contentIntent = intent;
	}

	/**
	 * ��ʾϵͳĬ�ϸ�ʽ֪ͨ
	 * 
	 * @param iconId
	 *            ֪ͨ��ͼ��ID
	 * @param titleText
	 *            ֪ͨ������
	 * @param contentStr
	 *            ֪ͨ������
	 */
	public void showDefaultNotification(int iconId, String titleText,
			String contentStr) {
		this.titleStr = titleText;
		this.contentStr = contentStr;
		this.iconID = iconId;

		notification = new Notification();
		notification.tickerText = titleStr;
		notification.icon = iconID;
		notification.flags = Notification.FLAG_INSISTENT;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.contentIntent = this.contentIntent;

		// �������Ч��
		// notification.defaults |= Notification.DEFAULT_SOUND;

		// �����,������֪��Ҫ�����Ȩ�� : Virbate Permission
		// mNotification.defaults |= Notification.DEFAULT_VIBRATE ;

		// ���״̬��־
		// FLAG_AUTO_CANCEL ��֪ͨ�ܱ�״̬���������ť�������
		// FLAG_NO_CLEAR ��֪ͨ�ܱ�״̬���������ť�������
		// FLAG_ONGOING_EVENT ֪ͨ��������������
		// FLAG_INSISTENT ֪ͨ������Ч��һֱ����
		notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
		changeNotificationText(contentStr);
	}

	/**
	 * �ı�Ĭ��֪ͨ����֪ͨ����
	 * 
	 * @param content
	 */
	public void changeNotificationText(String content) {
		notification.setLatestEventInfo(mContext, titleStr, content,
				contentIntent);

		// ����setLatestEventInfo����,��������û�App�����쳣
		// NotificationManager mNotificationManager = (NotificationManager)
		// getSystemService(Context.NOTIFICATION_SERVICE);
		// ע���֪ͨ
		// �����NOTIFICATION_ID��֪ͨ�Ѵ��ڣ�����ʾ����֪ͨ�������Ϣ ������tickerText ��
		nm.notify(notificationID, notification);
	}

	/**
	 * �Ƴ�֪ͨ
	 */
	public void removeNotification() {
		// ȡ����ֻ�ǵ�ǰContext��Notification
		nm.cancel(notificationID);
	}
}
