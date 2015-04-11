package com.septinary.xbwapp.adapter.list;


import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.septinary.xbwapp.R;
import com.septinary.xbwapp.model.main.Teacher;
import com.septinary.xbwapp.views.MyHorizontalScrollView;

public class HorizontalScrollViewAdapter {

	private final static String TAG = "HorizontalScrollViewAdapter";
	private LayoutInflater mInflater;
	private ArrayList<String> imgs;
	private ImageLoader imageLoader;

	public HorizontalScrollViewAdapter(Context context,ArrayList<String> imgs, ImageLoader imageLoader) {
		this.imageLoader = imageLoader;
		this.imgs = imgs;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return imgs.size();
	}

	public Object getItem(int position) {
		return imgs.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.item_gallery_item, parent, false);
			viewHolder.mImg = (NetworkImageView) convertView
					.findViewById(R.id.igi_img);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mImg.setImageUrl(imgs.get(position), imageLoader);
		Log.e("TAG", TAG+"执行");
		return convertView;
	}

	private class ViewHolder {
		NetworkImageView mImg;
	}

}
