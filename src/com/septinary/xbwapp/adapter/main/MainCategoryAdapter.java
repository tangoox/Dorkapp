package com.septinary.xbwapp.adapter.main;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.septinary.xbwapp.R;
import com.septinary.xbwapp.model.main.Category;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MainCategoryAdapter extends BaseAdapter {

	private static final String TAG = "MainCategoryAdapter";
	
	private LayoutInflater mInflater;
	private ArrayList<Category> categories;
	private ImageLoader imageLoader;

	public MainCategoryAdapter(Context mContext, ArrayList<Category> categories,ImageLoader imageLoader) {
		this.imageLoader = imageLoader;
		this.categories = categories;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return categories.size();
	}

	@Override
	public Object getItem(int position) {
		return categories.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MainCategoryHolder holder;
		if (convertView == null) {
			if (position % 2 == 0) {
				convertView = mInflater.inflate(R.layout.item_mr_listitem1,
						parent, false);
			} else {
				convertView = mInflater.inflate(R.layout.item_mr_listitem2,
						parent, false);
			}
			holder = new MainCategoryHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (MainCategoryHolder) convertView.getTag();
		}

		Category item = categories.get(position);
		
		if (position % 2 == 0) {
			holder.iml1_img.setImageUrl(item.getImgaddress(), imageLoader);
			holder.iml1_title.setText(item.getCateTitle());
			holder.iml1_entext.setText(item.getCateEn());
			holder.iml1_teacher_num.setText(" " + item.getTeacherNum() + "个教师");
			holder.iml1_course_num.setText(" " + item.getCourseNum() + "个课程");
			
		} else {
			holder.iml2_img.setImageUrl(item.getImgaddress(), imageLoader);
			holder.iml2_title.setText(item.getCateTitle());
			holder.iml2_entext.setText(item.getCateEn());
			holder.iml2_teacher_num.setText(" " + item.getTeacherNum() + "个教师");
			holder.iml2_course_num.setText(" " + item.getCourseNum() + "个课程");
		}
		Log.e(TAG, TAG + "执行");
		return convertView;
	}

	class MainCategoryHolder {
		
		NetworkImageView iml1_img;
		
		NetworkImageView iml2_img;
		
		TextView iml1_title;
		
		TextView iml2_title;
		
		TextView iml1_entext;
		
		TextView iml2_entext;
		
		TextView iml1_teacher_num;
		
		TextView iml2_teacher_num;
		
		TextView iml1_course_num;
		
		TextView iml2_course_num;
		
		
		public MainCategoryHolder(View view) {
			iml1_img = (NetworkImageView) view.findViewById(R.id.iml1_img);
			iml1_title = (TextView) view.findViewById(R.id.iml1_title);
			iml1_entext = (TextView) view.findViewById(R.id.iml1_entext);
			iml1_teacher_num = (TextView) view.findViewById(R.id.iml1_teacher_num);
			iml1_course_num = (TextView) view.findViewById(R.id.iml1_course_num);
			
			iml2_img = (NetworkImageView) view.findViewById(R.id.iml2_img);
			iml2_title = (TextView) view.findViewById(R.id.iml2_title);
			iml2_entext = (TextView) view.findViewById(R.id.iml2_entext);
			iml2_teacher_num = (TextView) view.findViewById(R.id.iml2_teacher_num);
			iml2_course_num = (TextView) view.findViewById(R.id.iml2_course_num);
		}
	}
}
