package com.septinary.xbwapp.activity.details;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import butterknife.InjectView;

import com.septinary.xbwapp.R;
import com.septinary.xbwapp.base.BaseActivity;

public class TeacherDetailsActivity extends BaseActivity{
	
	@InjectView(R.id.ltd_viewpager)
	ViewPager ltd_viewpager;
	
	@Override
	public int getLayoutId() {
		return R.layout.layout_teacherdetails;
	}
	
	@Override
	public void init(Bundle savedInstanceState) {
		
	}

	

}
