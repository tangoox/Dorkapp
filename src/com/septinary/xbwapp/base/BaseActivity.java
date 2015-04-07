package com.septinary.xbwapp.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;

public abstract class BaseActivity extends FragmentActivity {

	/**
	 * 初始化
	 * */
	public abstract void init(Bundle savedInstanceState);

	/**
	 * 设置布局
	 * */
	public abstract int getLayoutId();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		logUseMemory();
		AppManager.getInstance().addActivity(this);
		// 去标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 界面竖屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		setContentView(getLayoutId());
		init(savedInstanceState);
		// 每一个activity加入管理器中
	}

	/**
	 * 测试 内存占用率
	 * */
	private void logUseMemory() {
		long total = Runtime.getRuntime().totalMemory();
		long free = Runtime.getRuntime().freeMemory();
		long isuse = total - free;
		StringBuffer strlog = new StringBuffer();
		strlog.append(getClass().getName()).append("====总内存:").append(total)
				.append(";已用内存:").append(isuse).append(";自由内存:").append(free);
		Log.v("内存使用情况", strlog.toString());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getInstance().finishActivity(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
}
