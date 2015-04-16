package com.septinary.xbwapp.model.main;

import java.io.Serializable;
import java.util.ArrayList;

public class Teacher implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	// 教师姓名
	private String teacher_name;
	// 教师头像
	private String teacher_img;
	// 教师标签
	private ArrayList<String> teacher_lable;
	// 教师资质
	private ArrayList<String> teacher_aptitude;
	// 教师列表展示图片
	private ArrayList<String> teacher_listimgs;
	// 教师地址
	private String teacher_address;
	// 教师距离用户距离
	private String teacher_range;

	public String getTeacher_img() {
		return teacher_img;
	}

	public void setTeacher_img(String teacher_img) {
		this.teacher_img = teacher_img;
	}

	public String getTeacher_name() {
		return teacher_name;
	}

	public void setTeacher_name(String teacher_name) {
		this.teacher_name = teacher_name;
	}

	public ArrayList<String> getTeacher_lable() {
		return teacher_lable;
	}

	public void setTeacher_lable(ArrayList<String> teacher_lable) {
		this.teacher_lable = teacher_lable;
	}

	public ArrayList<String> getTeacher_aptitude() {
		return teacher_aptitude;
	}

	public void setTeacher_aptitude(ArrayList<String> teacher_aptitude) {
		this.teacher_aptitude = teacher_aptitude;
	}

	public ArrayList<String> getTeacher_listimgs() {
		return teacher_listimgs;
	}

	public void setTeacher_listimgs(ArrayList<String> teacher_listimgs) {
		this.teacher_listimgs = teacher_listimgs;
	}

	public String getTeacher_address() {
		return teacher_address;
	}

	public void setTeacher_address(String teacher_address) {
		this.teacher_address = teacher_address;
	}

	public String getTeacher_range() {
		return teacher_range;
	}

	public void setTeacher_range(String teacher_range) {
		this.teacher_range = teacher_range;
	}

}
