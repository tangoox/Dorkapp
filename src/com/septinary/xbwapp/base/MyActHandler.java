package com.septinary.xbwapp.base;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

public class MyActHandler extends Handler{
	
	WeakReference<Activity> mReference;
	
	public MyActHandler(Activity activity){
		mReference = new WeakReference<Activity>(activity);
	}

	@Override
	public void handleMessage(Message msg) {
		final Activity activity = mReference.get();
		if(activity == null)
			return;
	}
}
