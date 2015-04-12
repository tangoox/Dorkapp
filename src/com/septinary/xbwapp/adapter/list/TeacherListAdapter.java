package com.septinary.xbwapp.adapter.list;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.septinary.xbwapp.R;
import com.septinary.xbwapp.model.main.Teacher;
import com.septinary.xbwapp.views.FlowLayout;
import com.septinary.xbwapp.views.MyHorizontalScrollView;

public class TeacherListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	// private HorizontalScrollViewAdapter mAdapter;
	private MyGridViewAdapter mAdapter;
	private ImageLoader imageLoader;
	private Context mContext;
	private ArrayList<Teacher> teachers;
	private static final int LENGHT = 100;
	private DisplayMetrics dm;
	private float density;

	public TeacherListAdapter(Context context, ArrayList<Teacher> teachers,
			ImageLoader imageLoader) {
		this.mContext = context;
		this.teachers = teachers;
		this.imageLoader = imageLoader;
		mInflater = LayoutInflater.from(context);
		dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		density = dm.density;
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

		mAdapter = new MyGridViewAdapter(mContext,
				teacher.getTeacher_listimgs(), imageLoader);
		// holder.ill_horizontalScrollView.initDatas(mAdapter);
		
		int size = teacher.getTeacher_listimgs().size();
		int gridviewWidth = (int) ((size + 1) * (LENGHT + 4) * density);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
		holder.ill_gridView.setLayoutParams(params);
		holder.ill_gridView.setAdapter(mAdapter);

		holder.ill_img.setImageUrl(teacher.getTeacher_img(), imageLoader);
		holder.ill_address.setText("地址：" + teacher.getTeacher_address());
		holder.ill_name.setText(teacher.getTeacher_name());
		holder.ill_range.setText("<" + teacher.getTeacher_range() + "m");
		holder.ill_teacherlable.removeAllViews();
		for (int i = 0; i < teacher.getTeacher_lable().size(); i++) {
			TextView view = new TextView(mContext);
			view.setText(teacher.getTeacher_lable().get(i));
			view.setBackgroundResource(R.color.lable);
			view.setTextColor(mContext.getResources().getColor(R.color.white));
			view.setGravity(Gravity.CENTER);
			view.setPadding(15, 0, 15, 0);
			view.setTextSize(14);
			holder.ill_teacherlable.addView(view);
		}
		holder.ill_teacheraptitude.removeAllViews();
		for (int i = 0; i < teacher.getTeacher_aptitude().size(); i++) {
			TextView view = new TextView(mContext);
			view.setText( " " + teacher.getTeacher_aptitude().get(i));
			Drawable drawable= mContext.getResources().getDrawable(R.drawable.lt_lable_icon);
			/// 这一步必须要做,否则不会显示.
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			view.setCompoundDrawables(drawable,null,null,null);
			view.setGravity(Gravity.CENTER);
			view.setTextSize(16);
			holder.ill_teacheraptitude.addView(view);
		}
		Log.e("TeacherListAdapter", "执行");

		return convertView;
	}

	class TeacherListHolder {
		// 相册
		private MyHorizontalScrollView ill_horizontalScrollView;
		private GridView ill_gridView;
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

			ill_gridView = (GridView) view.findViewById(R.id.ill_gridView);
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
