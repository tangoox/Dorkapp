package com.septinary.xbwapp.adapter.list;

import java.util.ArrayList;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.septinary.xbwapp.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MyGridViewAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<String> imgs;
	private ImageLoader imageLoader;

	public MyGridViewAdapter(Context context, ArrayList<String> imgs,
			ImageLoader imageLoader) {
		this.imgs = imgs;
		this.imageLoader = imageLoader;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return imgs.size();
	}

	@Override
	public Object getItem(int position) {
		return imgs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		GridViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_gallery_item, parent,
					false);
			holder = new GridViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (GridViewHolder) convertView.getTag();
		}

		holder.igi_img.setImageUrl(imgs.get(position), imageLoader);
		Log.e("MyGridViewAdapter", "执行");

		return convertView;

	}

	private class GridViewHolder {

		private NetworkImageView igi_img;

		public GridViewHolder(View view) {
			igi_img = (NetworkImageView) view.findViewById(R.id.igi_img);
		}
	}
}
