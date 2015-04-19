package com.septinary.xbwapp.activity.main;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.toolbox.ImageLoader;
import com.septinary.xbwapp.R;
import com.septinary.xbwapp.adapter.main.LeftMenuAdapter;
import com.septinary.xbwapp.base.AppManager;
import com.septinary.xbwapp.base.BaseActivity;
import com.septinary.xbwapp.base.MyActHandler;
import com.septinary.xbwapp.fragment.main.stu.StuMenuFirstFragment;
import com.septinary.xbwapp.fragment.main.stu.StuMenuFirstFragment.FirstStuMenuListener;
import com.septinary.xbwapp.fragment.main.stu.StuMenuSecondFragment;
import com.septinary.xbwapp.utils.ActUtil;
import com.septinary.xbwapp.utils.CustomToast;
import com.septinary.xbwapp.utils.MyPreferences;
import com.septinary.xbwapp.utils.SingleImageLoader;
import com.septinary.xbwapp.views.ResideLayout;

/**
 * @author S_ven
 * @info 2015年4月6日15:19:17 程序主界面
 * */
@SuppressLint("InlinedApi")
public class MainActivity extends BaseActivity implements FirstStuMenuListener{

	// 双击退出时间
	private long firstTime = 0;
	private long secondTime = 0;

	// 网络请求数据工具
	private ImageLoader mImageLoader;
	private MyPreferences myPreferences;
	
	private FragmentManager manager;
	private FragmentTransaction transaction;
	//学生菜单
	private Fragment stumenufFirFragment;
	private Fragment stuMenuSecondFragment;
	//表示当前显示的菜单
	private int currentShow = 0;
	
	// 定义菜单描述与菜单图标
	private String[] stu_menu = { "first menu", "second menu", "third menu", "fourth menu", "fifth menu" };
	private int[] stu_menu_icon = { R.drawable.ml_search, R.drawable.ml_guess,
			R.drawable.ml_fav, R.drawable.ml_advance, R.drawable.ml_message };
	
	//学生菜单界面
	private ArrayList<Fragment> stuMenus = new ArrayList<Fragment>();
	
	// 主布局控件
	@InjectView(R.id.main_reside)
	ResideLayout main_reside;

	// 左侧菜单
	/*private LeftMenuAdapter lmAdapter;
	@InjectView(R.id.ml_menu)
	ListView ml_menu;*/

	// 右侧布局
	// 菜单开关按钮
	@InjectView(R.id.mr_slidebtn)
	ImageButton mr_slidebtn;
	@OnClick(R.id.mr_slidebtn)
	public void mr_slidebtn() {
		if (!main_reside.isOpen())
			main_reside.openPane();
	}
	
	//内容区域
	@InjectView(R.id.mr_content)
	FrameLayout mr_content;
	
	private MyActHandler actHandler = new MyActHandler(this) {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}

	};

	@Override
	public int getLayoutId() {
		return R.layout.layout_main;
	}

	@Override
	public void init(Bundle savedInstanceState) {
		ButterKnife.inject(this);
		mImageLoader = SingleImageLoader.getImageLoader(MainActivity.this);
		myPreferences = MyPreferences.getInstance();
		myPreferences.init(MainActivity.this.getApplicationContext());
		// 构建菜单
//		initMenu();
		
		stumenufFirFragment = new StuMenuFirstFragment(actHandler,mImageLoader,true);
		stuMenuSecondFragment = new StuMenuSecondFragment(actHandler, mImageLoader, false);
		stuMenus.add(stumenufFirFragment);
		stuMenus.add(stuMenuSecondFragment);
		
		//加载学生的第一个菜单（首页）
		manager = getSupportFragmentManager();
		transaction = manager.beginTransaction();
		transaction.add(R.id.mr_content, stumenufFirFragment);
		transaction.add(R.id.mr_content, stuMenuSecondFragment);
		currentShow = 0;
		transaction.commit();
	}

	
	// 菜单初始化
	/*private void initMenu() {
		lmAdapter = new LeftMenuAdapter(stu_menu, stu_menu_icon,
				MainActivity.this);
		ml_menu.setAdapter(lmAdapter);
		ml_menu.setOnItemClickListener(ml_menuListener);
		ActUtil.getInstance().setListViewHeightBasedOnChildren(ml_menu);
	}*/
	
	// 左边菜单列表点击事件
	OnItemClickListener ml_menuListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			selectTab(arg2);
		}
	};
	
	private void selectTab(int position){
		//如果点击的就是当前显示的不做任何操作
		if(position == currentShow){
			return;
		}else{
			manager = getSupportFragmentManager();
			transaction = manager.beginTransaction();
			//隐藏当前显示的菜单
			if(stuMenus.get(currentShow) != null){
				transaction.hide(stuMenus.get(currentShow));
			}
			//显示点击的菜单
			if(stuMenus.get(position) != null){
				transaction.show(stuMenus.get(position));
				//将当前显示的菜单置为点击的菜单
				currentShow = position;
			}
			transaction.commit();
			if (main_reside.isOpen()) {
				main_reside.closePane();
			} 
		}
		
	}
	
	// 回退按钮点击事件
	@Override
	public void onBackPressed() {
		if (main_reside.isOpen()) {
			main_reside.closePane();
		} else {
			secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 2000) {
				// 提示再次点击退出
				firstTime = secondTime;
				CustomToast.getInstance().showToast(MainActivity.this,
						getResources().getString(R.string.logout_tips));
				return;
			} else {
				// 退出
				AppManager.getInstance().finishAllActivity();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		actHandler.removeCallbacks(null);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void firstMenuClick() {
		
	}
	
}
