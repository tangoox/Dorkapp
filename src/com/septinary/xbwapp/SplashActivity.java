package com.septinary.xbwapp;

import android.os.Bundle;
import android.os.Message;

import com.septinary.xbwapp.activity.main.MainActivity;
import com.septinary.xbwapp.base.AppManager;
import com.septinary.xbwapp.base.BaseActivity;
import com.septinary.xbwapp.base.MyActHandler;
import com.septinary.xbwapp.utils.ActUtil;


public class SplashActivity extends BaseActivity{

	@Override
	public void init(Bundle savedInstanceState) {
		new Thread(){
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					actHandler.sendEmptyMessage(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}.start();
	}

	@Override
	public int getLayoutId() {
		return R.layout.layout_splash;
	}

	/**
	 * handler使用软引用
	 * */
	private MyActHandler actHandler = new MyActHandler(this){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				ActUtil.getInstance().MoveToNewAct(SplashActivity.this, MainActivity.class);
				AppManager.getInstance().finishActivity(SplashActivity.this);
				break;
			}
		}
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		actHandler.removeCallbacks(null);
	};
}
