package com.septinary.xbwapp.fragment.main.stu;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import butterknife.InjectView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.septinary.xbwapp.R;
import com.septinary.xbwapp.activity.list.TeacherListActivity;
import com.septinary.xbwapp.adapter.main.MainCategoryAdapter;
import com.septinary.xbwapp.adapter.main.MainViewPagerAdapter;
import com.septinary.xbwapp.base.BaseFragment;
import com.septinary.xbwapp.base.MyActHandler;
import com.septinary.xbwapp.model.main.Category;
import com.septinary.xbwapp.utils.ActUtil;
import com.septinary.xbwapp.utils.AnimationToast;
import com.septinary.xbwapp.utils.CustomToast;
import com.septinary.xbwapp.views.ViewPagerSwipeRefreshLayout;

/**
 * @author S_ven
 * @info 2015年4月6日16:41:47 学生的第一个菜单相当于首页
 * */
@SuppressLint({ "ValidFragment", "InlinedApi" })
public class StuMenuFirstFragment extends BaseFragment implements
		OnRefreshListener {

	private static final String TAG = "StuMenuFirstFragment";

	// 判断该fragment是否第一次加载（第一次说明没有加载，系统初始化控件，
	// 不是第一次说明fragment已经加载过了，系统不再初始化控件）
	private boolean isFirst = true;
	// 是否需要加载数据
	public boolean isLoadData = true;
	private FirstStuMenuListener firstStuMenuListener;

	private ImageLoader mImageLoader;
	private MyActHandler actHandler;

	public StuMenuFirstFragment(MyActHandler actHandler,
			ImageLoader mImageLoader, boolean isLoadData) {
		this.actHandler = actHandler;
		this.mImageLoader = mImageLoader;
		this.isLoadData = isLoadData;
	}

	// 刷新控件
	@InjectView(R.id.fmsf_refreshview)
	ViewPagerSwipeRefreshLayout fmsf_refreshview;
	// 滚动广告
	@InjectView(R.id.fmsf_rollad)
	ViewPager fmsf_rollad;
	// 滚动广告控件适配器
	MainViewPagerAdapter mPagerAdapter;
	// 滚动广告
	private String[] adImgs = {
			"http://www.sy234.cn/shengjingmh/uploads/allimg/131202/19-131202091225310.jpg",
			"http://img3.imgtn.bdimg.com/it/u=1201091980,3816261279&fm=21&gp=0.jpg",
			"http://news.mydrivers.com/img/20130628/6b87ded145ad4258b6b4d7b87817cdea.jpg",
			"http://img4.imgtn.bdimg.com/it/u=2121392482,2002778976&fm=21&gp=0.jpg" };
	private ArrayList<NetworkImageView> adImgViews = new ArrayList<NetworkImageView>();
	private NetworkImageView tempImgView;
	// 分类列表
	@InjectView(R.id.fmsf_classlist)
	ListView fmsf_classlist;
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

	@Override
	public int getLayout() {
		return R.layout.fragment_mr_stu_first;
	}

	@Override
	public void init() {
		if (isFirst) {
			isFirst = false;
			// 初始化该页面控件设置
			initWidgets();
			// 初始化广告图片数据 ,分类列表数据
			if (isLoadData) {
				initDatas();
			}
		}
	}

	// 初始化广告图片数据 ,分类列表数据
	private void initDatas() {
		new Thread() {
			@Override
			public void run() {
				initImgs();
				initCategotyListData();
			};
		}.start();
	}

	// 初始化该页面控件设置
	private void initWidgets() {
		fmsf_refreshview.setColorSchemeResources(
				android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		fmsf_refreshview.setOnRefreshListener(this);
		// 初始化滚动广告控件
		initViewpager();
		// 初始化分类列表
		initCategotyList();
	}

	// 刷新分类数据
	Runnable refresh = new Runnable() {
		@Override
		public void run() {
			fmsf_refreshview.setRefreshing(false);
			AnimationToast.getInstance().showNotify(getActivity(), "刷新完成",
					R.id.mr_toast);
		}
	};

	// 滚动广告控件初始化
	private void initViewpager() {
		mPagerAdapter = new MainViewPagerAdapter(adImgViews);
		fmsf_rollad.setAdapter(mPagerAdapter);
	}

	// 滚动广告图片数据初始化
	private void initImgs() {
		adImgViews.clear();
		// 加载网络图片
		for (int i = 0; i < adImgs.length; i++) {
			tempImgView = new NetworkImageView(getActivity()
					.getApplicationContext());
			tempImgView.setDefaultImageResId(R.drawable.badimage);
			tempImgView.setErrorImageResId(R.drawable.badimage);
			tempImgView.setImageUrl(adImgs[i], mImageLoader);
			tempImgView.setScaleType(ScaleType.FIT_XY);
			adImgViews.add(tempImgView);
			tempImgView = null;
		}
		mPagerAdapter.notifyDataSetChanged();
	}

	private void initCategotyList() {
		categoryAdapter = new MainCategoryAdapter(getActivity(), categories,
				mImageLoader);
		fmsf_classlist.setAdapter(categoryAdapter);
		fmsf_classlist.setOnItemClickListener(mr_classlistListener);
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
		ActUtil.getInstance().setListViewHeightBasedOnChildren(fmsf_classlist);
	}

	// 右边分类列表点击事件
	OnItemClickListener mr_classlistListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			ActUtil.getInstance().MoveToNewAct(getActivity(),
					TeacherListActivity.class);
		}
	};

	@Override
	public void onRefresh() {
		actHandler.postDelayed(refresh, 2000);
	}

	@Override
	public void onAttach(Activity activity) {
		try {
			firstStuMenuListener = (FirstStuMenuListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + "必须实现" + TAG
					+ "的FirstStuMenuListener接口");
		}
		super.onAttach(activity);

	}

	public interface FirstStuMenuListener {

		public void firstMenuClick();

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if(isVisibleToUser){
			CustomToast.getInstance().showToast(getActivity().getApplicationContext(), "可见");
		}else{
			CustomToast.getInstance().showToast(getActivity().getApplicationContext(), "不可见");
		}
		super.setUserVisibleHint(isVisibleToUser);
	}
}
