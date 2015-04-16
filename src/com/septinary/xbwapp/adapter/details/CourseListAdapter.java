package com.septinary.xbwapp.adapter.details;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.septinary.xbwapp.R;
import com.septinary.xbwapp.utils.CustomToast;

public class CourseListAdapter extends BaseAdapter{

	private static final String TAG = "CourseListAdapter";
	private ArrayList<String> courses;
	private LayoutInflater mInflater;
	private Context mContext;
	
	public CourseListAdapter(Context mContext,ArrayList<String> courses){
		this.courses = courses;
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
	}
	
	@Override
	public int getCount() {
		return courses.size();
	}

	@Override
	public Object getItem(int position) {
		return courses.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CourseViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_course_item, parent ,false);
			holder = new CourseViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder = (CourseViewHolder) convertView.getTag();
		}
		holder.ici_course_name.setText(courses.get(position));
		holder.ici_audition.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CustomToast.getInstance().showToast(mContext.getApplicationContext(), "点击");
			}
		});
		Log.e(TAG, "执行");
		return convertView;
	}

	private class CourseViewHolder{
		
		private TextView ici_course_name;
		private TextView ici_audition;
		
		public CourseViewHolder(View view){
			ici_course_name = (TextView) view.findViewById(R.id.ici_course_name);
			ici_audition = (TextView) view.findViewById(R.id.ici_audition);
		}
	}
	
}
