package com.septinary.xbwapp.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class CustomToast {

	private final static class CustomToastHandler {
		private final static CustomToast INSTANCE = new CustomToast();
	}

	public CustomToast() {

	}

	public static CustomToast getInstance() {
		return CustomToastHandler.INSTANCE;
	}

	private static Toast mToast;
	private static Handler mHandler = new Handler();
	private static Runnable r = new Runnable() {
		@Override
		public void run() {
			mToast.cancel();
		}
	};

	public void showToast(Context mContext, String text) {
		mHandler.removeCallbacks(r);
		if (mToast != null)
			mToast.setText(text);
		else
			mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
		mHandler.postDelayed(r, 1000);
		mToast.show();
	}

	public void showToast(Context mContext, int resId) {
		showToast(mContext, mContext.getResources().getString(resId));
	}

}
