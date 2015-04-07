package com.septinary.xbwapp.adapter;


import com.septinary.xbwapp.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author S_ven
 * @info 2015年4月6日15:19:58 左侧菜单适配器
 * */
public class LeftMenuAdapter extends BaseAdapter {

	private String[] menu;
	private int[] menu_icon;
	private LayoutInflater inflater;

	public LeftMenuAdapter(String[] menu, int[] menu_icon, Context context) {
		this.menu = menu;
		this.menu_icon = menu_icon;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return menu.length;
	}

	@Override
	public Object getItem(int position) {
		return menu[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LeftMenuViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_ml_left_menu, parent,
					false);
			holder = new LeftMenuViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (LeftMenuViewHolder) convertView.getTag();
		}

		holder.imlm_icon.setBackgroundResource(menu_icon[position]);
		holder.imlm_text.setText(menu[position]);
		holder.imlm_tips.setVisibility(View.GONE);

		return convertView;
	}

	class LeftMenuViewHolder {
		@InjectView(R.id.imlm_icon)
		ImageView imlm_icon;
		@InjectView(R.id.imlm_text)
		TextView imlm_text;
		@InjectView(R.id.imlm_tips)
		ImageView imlm_tips;

		public LeftMenuViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}

}
