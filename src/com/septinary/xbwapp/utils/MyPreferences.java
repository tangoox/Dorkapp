package com.septinary.xbwapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {
	final String filename = "useinfo";
	SharedPreferences sp;
	SharedPreferences.Editor ed;

	private static class MyPreferencesHolder {
		private static final MyPreferences INSTANCE = new MyPreferences();
	}

	private MyPreferences() {
	}

	public static final MyPreferences getInstance() {
		return MyPreferencesHolder.INSTANCE;
	}

	public static MyPreferences getPreferences() {
		return new MyPreferences();
	}

	public void init(Context context) {
		sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
	}

	public void SaveUseInfo() {
		ed = sp.edit();

		ed.commit();
		ed.clear();

	}


	/*
	 * 
	 * 设置仅是第一次有导航
	 */
	public void setWelcome() {
		ed = sp.edit();
		ed.putBoolean(Contants.WELCOME, false);
		ed.commit();
		ed.clear();
	}

	public boolean getWelcome() {
		return sp.getBoolean(Contants.WELCOME, true);
	}


}
