package com.septinary.xbwapp.adapter.details;

import java.util.ArrayList;
import java.util.HashMap;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.septinary.xbwapp.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class JudgeListAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, String>> judges;
	private static final String TAG = "JudgeListAdapter";
	private ImageLoader imageLoader;
	private LayoutInflater mInflater;
	private Context mContext;

	public JudgeListAdapter(Context mContext,
			ArrayList<HashMap<String, String>> judges, ImageLoader imageLoader) {
		this.mContext = mContext;
		this.judges = judges;
		this.imageLoader = imageLoader;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public Object getItem(int position) {
		return judges.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		JudgeViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_judge_item, parent, false);
			holder = new JudgeViewHolder(convertView);
		} else {
			holder = (JudgeViewHolder) convertView.getTag();
		}
		HashMap<String, String> item = judges.get(position);
		holder.iji_img.setDefaultImageResId(R.drawable.badimage);
		holder.iji_img.setErrorImageResId(R.drawable.badimage);
		holder.iji_img.setImageUrl(item.get("img"), imageLoader);
		
		holder.iji_name.setText(item.get("name"));
		holder.iji_date.setText(item.get("date"));
		holder.iji_judge.setText(item.get("judge"));
		Log.e(TAG, TAG+"执行");
		return convertView;
	}

	private class JudgeViewHolder {

		private NetworkImageView iji_img;
		private TextView iji_name;
		private TextView iji_date;
		private TextView iji_judge;

		public JudgeViewHolder(View view) {
			iji_img = (NetworkImageView) view.findViewById(R.id.iji_img);
			iji_name = (TextView) view.findViewById(R.id.iji_name);
			iji_date = (TextView) view.findViewById(R.id.iji_date);
			iji_judge = (TextView) view.findViewById(R.id.iji_judge);
			view.setTag(this);
		}
	}

}
