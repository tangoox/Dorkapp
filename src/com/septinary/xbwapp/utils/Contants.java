package com.septinary.xbwapp.utils;

/**
 * @author 项目配置文件
 * */
public class Contants {

	/**
	 * 网络请求标识
	 * */

	public final static int NETWORK_SUCCESS = 1;// 成功
	public final static int NETWORK_ERROR = -1;// 失败

	/**
	 * 版本更新状
	 * */
	public final static int VERSION_INFO = 0; // 检查版本是否有更新
	public final static int VERSION_UPDATE = 1; // 更新
	public final static int VERSION_DEFULT = 2; // 版本更新失败
	
	/**
	 * MyPreferences配置
	 * */
	public final static String WELCOME = "welcome";
	public final static String TOKEN = "token";
	public final static String UID = "uid";
	public final static String UNAME = "uname";
	public final static String SEX = "sex";
	public final static String UPWD = "upwd";
	public final static String UPHONE = "uphone";
	public final static String NICKNAME = "nickname";
	public final static String MPKEY = android.os.Build.MODEL;
}
