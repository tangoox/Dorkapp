package com.septinary.xbwapp.adapter.details;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.septinary.xbwapp.R;
import com.septinary.xbwapp.views.FlowLayout;

public class InterestListAdapter extends BaseAdapter {

	private static final String TAG = "InterestListAdapter";
	private Context mContext;
	private ArrayList<HashMap<String, String>> interests;
	private ImageLoader imageLoader;
	private LayoutInflater mInflater;
	private String[] lables = {"英语","法语","日语"};

	public InterestListAdapter(Context mContext,
			ArrayList<HashMap<String, String>> interests,
			ImageLoader imageLoader) {
		this.mContext = mContext;
		this.interests = interests;
		this.imageLoader = imageLoader;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return interests.size();
	}

	@Override
	public Object getItem(int position) {
		return interests.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		InterestViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_interest_item,
					parent, false);
			holder = new InterestViewHolder(convertView);
		} else {
			holder = (InterestViewHolder) convertView.getTag();
		}
		HashMap<String, String> item = interests.get(position);
		holder.iii_img.setDefaultImageResId(R.drawable.badimage);
		holder.iii_img.setErrorImageResId(R.drawable.badimage);
		holder.iii_img.setImageUrl(item.get("img"), imageLoader);

		holder.iii_name.setText(item.get("name"));
		holder.iii_range.setText(item.get("range"));
		holder.iii_teacherlable.removeAllViews();
		for (int i = 0; i < lables.length; i++) {
			TextView view = new TextView(mContext);
			view.setText(lables[i]);
			view.setBackgroundResource(R.color.lable);
			view.setTextColor(mContext.getResources().getColor(R.color.white));
			view.setGravity(Gravity.CENTER);
			view.setPadding(15, 0, 15, 0);
			view.setTextSize(14);
			holder.iii_teacherlable.addView(view);
		}
		holder.iii_address.setText("地址：" + item.get("address"));
		Log.e(TAG, TAG+"执行");
		return convertView;
	}

	private class InterestViewHolder {

		private NetworkImageView iii_img;
		private TextView iii_name;
		private TextView iii_range;
		private FlowLayout iii_teacherlable;
		private TextView iii_address;

		public InterestViewHolder(View view) {
			iii_img = (NetworkImageView) view.findViewById(R.id.iii_img);
			iii_name = (TextView) view.findViewById(R.id.iii_name);
			iii_range = (TextView) view.findViewById(R.id.iii_range);
			iii_teacherlable = (FlowLayout) view
					.findViewById(R.id.iii_teacherlable);
			iii_address = (TextView) view.findViewById(R.id.iii_address);
			view.setTag(this);
		}

	}
}
