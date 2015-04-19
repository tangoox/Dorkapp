package com.septinary.xbwapp.views;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class LinearLayoutForListView extends LinearLayout {

	private BaseAdapter adapter;
	private OnClickListener onClickListener = null;

	/**
	 * 绑定布局
	 */
	public void bindLinearLayout() {
		int count = adapter.getCount();
		this.removeAllViews();
		for (int i = 0; i < count; i++) {
			View v = adapter.getView(i, null, null);

			v.setOnClickListener(this.onClickListener);
			addView(v, i);
		}
		Log.v("countTAG", "" + count);
	}

	public LinearLayoutForListView(Context context) {
		super(context);
	}
}
