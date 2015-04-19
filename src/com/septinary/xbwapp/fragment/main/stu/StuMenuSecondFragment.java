package com.septinary.xbwapp.fragment.main.stu;

import android.annotation.SuppressLint;
import com.android.volley.toolbox.ImageLoader;
import com.septinary.xbwapp.R;
import com.septinary.xbwapp.base.BaseFragment;
import com.septinary.xbwapp.base.MyActHandler;


@SuppressLint("ValidFragment")
public class StuMenuSecondFragment extends BaseFragment{

	// 判断该fragment是否第一次加载（第一次说明没有加载，系统初始化控件，
	// 不是第一次说明fragment已经加载过了，系统不再初始化控件）
	private boolean isFirst = true;
	// 是否需要加载数据
	public boolean isLoadData = true;
	
	private ImageLoader mImageLoader;
	private MyActHandler actHandler;
	
	public StuMenuSecondFragment(MyActHandler actHandler, ImageLoader mImageLoader, boolean isLoadData){
		this.actHandler = actHandler;
		this.mImageLoader = mImageLoader;
		this.isLoadData = isLoadData;
	}
	
	@Override
	public int getLayout() {
		return R.layout.fragment_mr_stu_second;
	}

	@Override
	public void init() {
		
	}

}
