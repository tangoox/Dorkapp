package com.septinary.xbwapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {
	
	final String filename = "useinfo";
	SharedPreferences sp;
	SharedPreferences.Editor ed;
	private static AESUtil aesUtil = AESUtil.getInstance();
	private String temp = "";

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

	public void login(String token, String uid, String uname, String sex,
			String upwd, String uphone, String nickname) {
		ed = sp.edit();
		ed.putString(Contants.TOKEN, token);
		ed.putString(Contants.UID, uid);
		ed.putString(Contants.UNAME, uname);
		ed.putString(Contants.SEX, sex);
		ed.putString(Contants.UPWD, upwd);
		ed.putString(Contants.UPHONE, uphone);
		ed.putString(Contants.NICKNAME, nickname);
		ed.commit();
		ed.clear();
	}
	
	public void clearAll() {
		ed = sp.edit();
		ed.clear();
		ed.commit();
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

	public void setToken(String token) {
		setValue(Contants.TOKEN, token);
	}

	public String getToken() {
		return getValue(Contants.TOKEN);
	}

	public void setUid(String uid) {
		setValue(Contants.UID, uid);
	}

	public String getUid() {
		return getValue(Contants.UID);
	}

	public void setUname(String uname) {
		setValue(Contants.UNAME, uname);
	}

	public String getUname() {
		return getValue(Contants.UNAME);
	}

	public void setSex(String sex) {
		setValue(Contants.SEX, sex);
	}

	public String getSex() {
		return getValue(Contants.SEX);
	}

	public void setUpwd(String upwd) {
		setValue(Contants.UPWD, upwd);
	}

	public String getUpwd() {
		return getValue(Contants.UPWD);
	}

	public void setNickname(String nickname) {
		setValue(Contants.NICKNAME, nickname);
	}

	public String getNickname() {
		return getValue(Contants.NICKNAME);
	}

	public void setUphone(String uphone) {
		setValue(Contants.UPHONE, uphone);
	}

	public String getPhone() {
		return getValue(Contants.UPHONE);
	}

	/**
	 * 对保存的值进行加密(如果保存的值为"",则不进行加密)
	 * */
	private void setValue(String key, String value) {
		ed = sp.edit();
		if (!(value.length() == 0 || value.equals(""))) {
			try {
				ed.putString(key, aesUtil.aesEncrypt(value, Contants.MPKEY));
			} catch (Exception e) {
				ed.putString(key, "");
				e.printStackTrace();
			}
		}else{
			ed.putString(key, "");
		}
		ed.commit();
		ed.clear();
	}

	/**
	 * 对取出的值进行解密(如果取出的值为"",则不进行解密)
	 * */
	private String getValue(String key) {
		temp = sp.getString(key, "");
		if (temp.length() == 0 || temp.equals("")) {
			return "";
		} else {
			try {
				return aesUtil.aesDecrypt(temp, Contants.MPKEY);
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}

	}
}
