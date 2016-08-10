package com.cnziz.updatelib.utils;

public class Listener {
	public static interface onUpdateListener{
		public void noNewVersion();
		public void downloadFail();
		public void updateSuccess();
		public void installFail();
	}
}
