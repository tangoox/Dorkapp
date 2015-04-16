package com.septinary.xbwapp.adapter.details;

import java.util.ArrayList;

import com.android.volley.toolbox.NetworkImageView;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class TeacherDetailsViewPagerAdapter extends PagerAdapter{

	private ArrayList<NetworkImageView> imgs;
	
	public TeacherDetailsViewPagerAdapter(ArrayList<NetworkImageView> imgs){
		this.imgs = imgs;
	}
	
	@Override
	public int getCount() {
		return imgs.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(imgs.get(position));
		return imgs.get(position);
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View)object);
	}
}
