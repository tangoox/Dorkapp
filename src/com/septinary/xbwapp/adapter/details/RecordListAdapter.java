package com.septinary.xbwapp.adapter.details;

import java.util.ArrayList;
import java.util.HashMap;

import com.septinary.xbwapp.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class RecordListAdapter extends BaseAdapter {

	private static final String TAG = "RecordListAdapter";
	private Context mContext;
	private ArrayList<HashMap<String, String>> records;
	private LayoutInflater mInflater;

	public RecordListAdapter(Context mContext,ArrayList<HashMap<String, String>> records) {
		this.mContext = mContext;
		this.records = records;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return records.size();
	}

	@Override
	public Object getItem(int position) {
		return records.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RecordViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_record_item, parent ,false);
			holder = new RecordViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder = (RecordViewHolder) convertView.getTag();
		}
		if(position == 0){
			holder.iri_line.setVisibility(View.INVISIBLE);
			holder.iri_point.setBackground(mContext.getResources().getDrawable(R.drawable.point_green));
		}else{
			holder.iri_line.setVisibility(View.VISIBLE);
			holder.iri_point.setBackground(mContext.getResources().getDrawable(R.drawable.point_gray));
		}
		HashMap<String, String> item = records.get(position);
		holder.iri_time.setText(item.get("time"));
		holder.iri_address.setText(item.get("address"));
		
		Log.e(TAG, TAG+"执行");
		return convertView;
	}

	private class RecordViewHolder {
		private View iri_line;
		private ImageView iri_point;
		private TextView iri_time;
		private TextView iri_address;

		public RecordViewHolder(View view) {
			iri_line = (View) view.findViewById(R.id.iri_line);
			iri_point = (ImageView) view.findViewById(R.id.iri_point);
			iri_time = (TextView) view.findViewById(R.id.iri_time);
			iri_address = (TextView) view.findViewById(R.id.iri_address);
		}
	}
}
