package com.septinary.xbwapp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表单数据验证
 * 
 * */
public class RegData {
	
	//验证邮箱
	public static boolean regEamil(String email){
		String pattern1 = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";
		Pattern pattern = Pattern.compile(pattern1);
		Matcher mat = pattern.matcher(email);
		if(!mat.find()){
			return false;
		}else{
			return true;
		}
	}
	
	//验证手机号
	public static boolean regMobile(String num){
		String pattern1 = "^1(3|4|5|7|8)\\d{9}$";
		Pattern pattern = Pattern.compile(pattern1);
		Matcher mat = pattern.matcher(num);
		if(!mat.find()){
			return false;
		}else{
			return true;
		}
		
	}
	
	//验证电话号码(包含手机和座机)
	public static boolean regPhones(String num){
		String pattern1 = "^(0[0-9]{2,3}\\-)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$|(^(13[0-9]|15[0-9]|18[0-9])\\d{8}$)";
		Pattern pattern = Pattern.compile(pattern1);
		Matcher mat = pattern.matcher(num);
		if(!mat.find()){
			return false;
		}else{
			return true;
		}
		
	}
	
	//验证4位数字验证码
	public static boolean reg4code(String num){
		String pattern1 = "^\\d{4}$";
		Pattern pattern = Pattern.compile(pattern1);
		Matcher mat = pattern.matcher(num);
		if(!mat.find()){
			return false;
		}else{
			return true;
		}
	}
}
