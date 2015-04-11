package com.septinary.xbwapp.adapter.list;

import java.util.ArrayList;
import java.util.ResourceBundle.Control;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.septinary.xbwapp.R;
import com.septinary.xbwapp.model.main.Teacher;
import com.septinary.xbwapp.views.FlowLayout;
import com.septinary.xbwapp.views.MyHorizontalScrollView;
import com.septinary.xbwapp.views.ScrollListView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TeacherListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private HorizontalScrollViewAdapter mAdapter;
	private ImageLoader imageLoader;
	private Context mContext;
	private ArrayList<Teacher> teachers;

	public TeacherListAdapter(Context context, ArrayList<Teacher> teachers,
			ImageLoader imageLoader) {
		this.mContext = context;
		this.teachers = teachers;
		this.imageLoader = imageLoader;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return teachers.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		TeacherListHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_lt_listitem, parent,
					false);
			holder = new TeacherListHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (TeacherListHolder) convertView.getTag();
		}

		Teacher teacher = teachers.get(position);
		mAdapter = new HorizontalScrollViewAdapter(mContext,
				teacher.getTeacher_listimgs(), imageLoader);
		holder.ill_horizontalScrollView.initDatas(mAdapter);
		holder.ill_img.setImageUrl(teacher.getTeacher_img(), imageLoader);
		holder.ill_address.setText("地址：" + teacher.getTeacher_address());
		holder.ill_name.setText(teacher.getTeacher_name());
		holder.ill_range.setText("<" + teacher.getTeacher_range() + "m");
		/*for (int i = 0; i < teacher.getTeacher_lable().size(); i++) {
			TextView view = new TextView(mContext);
			view.setText(teacher.getTeacher_lable().get(i));
			holder.ill_teacherlable.addView(view);
		}
		for (int i = 0; i < teacher.getTeacher_aptitude().size(); i++) {
			TextView view = new TextView(mContext);
			view.setText(teacher.getTeacher_aptitude().get(i));
			holder.ill_teacheraptitude.addView(view);
		}*/
		Log.e("TeacherListAdapter", "执行");
		return convertView;
	}

	class TeacherListHolder {
		// 相册
		private MyHorizontalScrollView ill_horizontalScrollView;
		// 头像
		private NetworkImageView ill_img;
		// 名字
		private TextView ill_name;
		// 距离
		private TextView ill_range;
		// 标签
		private FlowLayout ill_teacherlable;
		// 资质
		private FlowLayout ill_teacheraptitude;
		// 地址
		private TextView ill_address;

		public TeacherListHolder(View view) {
			ill_horizontalScrollView = (MyHorizontalScrollView) view
					.findViewById(R.id.ill_horizontalScrollView);
			ill_img = (NetworkImageView) view.findViewById(R.id.ill_img);
			ill_name = (TextView) view.findViewById(R.id.ill_name);
			ill_range = (TextView) view.findViewById(R.id.ill_range);
			ill_teacherlable = (FlowLayout) view
					.findViewById(R.id.ill_teacherlable);
			ill_teacheraptitude = (FlowLayout) view
					.findViewById(R.id.ill_teacheraptitude);
			ill_address = (TextView) view.findViewById(R.id.ill_address);
		}
	}
	
}
