package com.septinary.xbwapp.activity.main;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.septinary.xbwapp.R;
import com.septinary.xbwapp.adapter.LeftMenuAdapter;
import com.septinary.xbwapp.base.AppManager;
import com.septinary.xbwapp.base.BaseActivity;
import com.septinary.xbwapp.utils.CustomToast;
import com.septinary.xbwapp.views.ResideLayout;


/**
 * @author S_ven
 * @info 2015年4月6日15:19:17 程序主界面
 * */
public class MainActivity extends BaseActivity implements OnItemClickListener{

	private long firstTime = 0;
	private long secondTime = 0;
	
	// 定义菜单描述与菜单图标
	private String[] stu_menu = { "寻求名师", "猜你喜欢", "兴趣标签", "我的预约", "我的消息" };
	private int[] stu_menu_icon = { R.drawable.ml_search, R.drawable.ml_guess,
			R.drawable.ml_fav, R.drawable.ml_advance, R.drawable.ml_message };

	//主布局控件
	@InjectView(R.id.main_reside)
	ResideLayout main_reside;
	
	//以下为 左侧菜单
	private LeftMenuAdapter lmAdapter;
	@InjectView(R.id.ml_menu)
	ListView ml_menu;
	
	//右侧布局
	//菜单开关按钮
	@InjectView(R.id.mr_slidebtn)
	ImageButton mr_slidebtn;
	@OnClick(R.id.mr_slidebtn)
	public void mr_slidebtn(){
		if(!main_reside.isOpen())
			main_reside.openPane();
	}
	
	@Override
	public int getLayoutId() {
		return R.layout.layout_main;
	}
	
	@Override
	public void init(Bundle savedInstanceState) {
		ButterKnife.inject(this);
		// 构建菜单
		lmAdapter = new LeftMenuAdapter(stu_menu, stu_menu_icon,
				MainActivity.this);
		ml_menu.setAdapter(lmAdapter);
		ml_menu.setOnItemClickListener(this);
	}

	//回退按钮点击事件
	@Override
	public void onBackPressed() {
		if(main_reside.isOpen()){
			main_reside.closePane();
		}else{
			secondTime = System.currentTimeMillis();
			if(secondTime - firstTime > 2000){
				//提示再次点击退出
				firstTime = secondTime;
				CustomToast.getInstance().showToast(MainActivity.this, getResources().getString(R.string.logout_tips));
				return;
			}else{
				//退出
				AppManager.getInstance().finishAllActivity();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
	}
}
