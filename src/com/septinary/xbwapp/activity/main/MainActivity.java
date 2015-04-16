package com.septinary.xbwapp.activity.main;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.septinary.xbwapp.R;
import com.septinary.xbwapp.activity.list.TeacherListActivity;
import com.septinary.xbwapp.adapter.main.LeftMenuAdapter;
import com.septinary.xbwapp.adapter.main.MainCategoryAdapter;
import com.septinary.xbwapp.adapter.main.MainViewPagerAdapter;
import com.septinary.xbwapp.base.AppManager;
import com.septinary.xbwapp.base.BaseActivity;
import com.septinary.xbwapp.base.MyActHandler;
import com.septinary.xbwapp.model.main.Category;
import com.septinary.xbwapp.utils.ActUtil;
import com.septinary.xbwapp.utils.AnimationToast;
import com.septinary.xbwapp.utils.CustomToast;
import com.septinary.xbwapp.utils.MyPreferences;
import com.septinary.xbwapp.utils.SingleImageLoader;
import com.septinary.xbwapp.views.ResideLayout;

/**
 * @author S_ven
 * @info 2015年4月6日15:19:17 程序主界面
 * */
@SuppressLint("InlinedApi")
public class MainActivity extends BaseActivity implements OnRefreshListener {

	// 双击退出时间
	private long firstTime = 0;
	private long secondTime = 0;

	// 网络请求数据工具
	private ImageLoader mImageLoader;
	private MyPreferences myPreferences;

	// 定义菜单描述与菜单图标
	private String[] stu_menu = { "寻求名师", "猜你喜欢", "兴趣标签", "我的预约", "我的消息" };
	private int[] stu_menu_icon = { R.drawable.ml_search, R.drawable.ml_guess,
			R.drawable.ml_fav, R.drawable.ml_advance, R.drawable.ml_message };

	// 滚动广告
	private String[] adImgs = {
			"http://www.sy234.cn/shengjingmh/uploads/allimg/131202/19-131202091225310.jpg",
			"http://img3.imgtn.bdimg.com/it/u=1201091980,3816261279&fm=21&gp=0.jpg",
			"http://news.mydrivers.com/img/20130628/6b87ded145ad4258b6b4d7b87817cdea.jpg",
			"http://img4.imgtn.bdimg.com/it/u=2121392482,2002778976&fm=21&gp=0.jpg" };
	private ArrayList<NetworkImageView> adImgViews = new ArrayList<NetworkImageView>();
	private NetworkImageView tempImgView;

	// 主布局控件
	@InjectView(R.id.main_reside)
	ResideLayout main_reside;

	// 左侧菜单
	private LeftMenuAdapter lmAdapter;
	@InjectView(R.id.ml_menu)
	ListView ml_menu;

	// 右侧布局
	// 菜单开关按钮
	@InjectView(R.id.mr_slidebtn)
	ImageButton mr_slidebtn;

	// 下拉刷新控件
	@InjectView(R.id.mr_refreshview)
	SwipeRefreshLayout mr_refreshview;

	@OnClick(R.id.mr_slidebtn)
	public void mr_slidebtn() {
		if (!main_reside.isOpen())
			main_reside.openPane();
	}

	// 滚动广告
	@InjectView(R.id.mr_rollad)
	ViewPager mr_rollad;
	// 滚动广告控件适配器
	MainViewPagerAdapter mPagerAdapter;

	// 类目列表
	@InjectView(R.id.mr_classlist)
	ListView mr_classlist;
	// 类目列表适配器
	private MainCategoryAdapter categoryAdapter;
	private ArrayList<Category> categories = new ArrayList<Category>();
	// 自定义分类
	private String[][] category = {
			{
					"http://a3.att.hudong.com/51/30/01300000645873128090307180622.jpg",
					"外语语言", "Foreign language", "15", "22" },
			{ "http://file.youboy.com/a/86/31/4/5/11135855.jpg", "家教辅导",
					"Toturing", "12", "25" },
			{ "http://www.photophoto.cn/m72/004/037/0040370165.jpg", "体育运动",
					"Physical culture", "5", "5" },
			{
					"http://px.thea.cn/Public/Upload/Uploadfiles/image/20140801095514_22735.jpg",
					"音乐舞蹈", "Music and dance", "30", "30" },
			{ "http://pic3.nipic.com/20090608/1484718_133401065_2.jpg", "美术摄影",
					"Art and Photography", "10", "12" },
			{
					"http://img3.imgtn.bdimg.com/it/u=4081183205,544908962&fm=21&gp=0.jpg",
					"国学文化", "Chinese culture", "20", "22" } };

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
		myPreferences.clearAll();
		mr_refreshview.setColorSchemeResources(
				android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		mr_refreshview.setOnRefreshListener(this);
		// 构建菜单
		initMenu();
		// 初始化滚动广告控件
		initViewpager();
		// 初始化广告图片数据
		new Thread(){
			@Override
			public void run() {
				initImgs();
			};
		}.start();
		// 初始化分类列表
		initCategotyList();
		// 初始化分类列表数据
		initCategotyListData();
	}

	private void initCategotyList() {
		categoryAdapter = new MainCategoryAdapter(MainActivity.this,
				categories, mImageLoader);
		mr_classlist.setAdapter(categoryAdapter);
		mr_classlist.setOnItemClickListener(mr_classlistListener);
	}

	private void initCategotyListData() {
		categories.clear();
		for (int i = 0; i < category.length; i++) {
			Category item = new Category();
			for (int j = 0; j < category[i].length; j++) {
				switch (j) {
				case 0:
					item.setImgaddress(category[i][j]);
					break;
				case 1:
					item.setCateTitle(category[i][j]);
					break;
				case 2:
					item.setCateEn(category[i][j]);
					break;
				case 3:
					item.setTeacherNum(category[i][j]);
					break;
				case 4:
					item.setCourseNum(category[i][j]);
					break;
				}
			}
			categories.add(item);
		}
		categoryAdapter.notifyDataSetChanged();
		ActUtil.getInstance().setListViewHeightBasedOnChildren(mr_classlist);
	}

	// 菜单初始化
	private void initMenu() {
		lmAdapter = new LeftMenuAdapter(stu_menu, stu_menu_icon,
				MainActivity.this);
		ml_menu.setAdapter(lmAdapter);
		ml_menu.setOnItemClickListener(ml_menuListener);
		ActUtil.getInstance().setListViewHeightBasedOnChildren(ml_menu);
	}

	// 滚动广告控件初始化
	private void initViewpager() {
		mPagerAdapter = new MainViewPagerAdapter(adImgViews);
		mr_rollad.setAdapter(mPagerAdapter);
	}

	// 滚动广告图片数据初始化
	private void initImgs() {
		adImgViews.clear();
		// 加载网络图片
		for (int i = 0; i < adImgs.length; i++) {
			tempImgView = new NetworkImageView(this.getApplicationContext());
			tempImgView.setDefaultImageResId(R.drawable.badimage);
			tempImgView.setErrorImageResId(R.drawable.badimage);
			tempImgView.setImageUrl(adImgs[i], mImageLoader);
			tempImgView.setScaleType(ScaleType.FIT_XY);
			adImgViews.add(tempImgView);
			tempImgView = null;
		}
		mPagerAdapter.notifyDataSetChanged();
	}

	// 左边菜单列表点击事件
	OnItemClickListener ml_menuListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
		}
	};

	// 右边分类列表点击事件
	OnItemClickListener mr_classlistListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			ActUtil.getInstance().MoveToNewAct(MainActivity.this,
					TeacherListActivity.class);
		}
	};

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

	// 下拉刷新执行操作
	@Override
	public void onRefresh() {
		actHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mr_refreshview.setRefreshing(false);
				AnimationToast.getInstance().showNotify(MainActivity.this,
						"刷新完成", R.id.mr_toast);
			}
		}, 2000);
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
	
}
