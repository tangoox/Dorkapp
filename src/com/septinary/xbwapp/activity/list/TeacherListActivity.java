package com.septinary.xbwapp.activity.list;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.septinary.xbwapp.R;
import com.septinary.xbwapp.activity.details.TeacherDetailsActivity;
import com.septinary.xbwapp.activity.main.MainActivity;
import com.septinary.xbwapp.adapter.list.TeacherListAdapter;
import com.septinary.xbwapp.base.AppManager;
import com.septinary.xbwapp.base.BaseActivity;
import com.septinary.xbwapp.base.BitmapCache;
import com.septinary.xbwapp.base.MyActHandler;
import com.septinary.xbwapp.model.main.Teacher;
import com.septinary.xbwapp.utils.ActUtil;
import com.septinary.xbwapp.utils.AnimationToast;
import com.septinary.xbwapp.utils.CustomToast;
import com.septinary.xbwapp.utils.SingleRequestQueue;
import com.septinary.xbwapp.views.BounceListView;
import com.septinary.xbwapp.views.ViewPagerSwipeRefreshLayout;

/**
 * @author S_ven
 * @info 2015年4月8日15:30:37 教师列表页
 * */
@SuppressLint("InlinedApi")
public class TeacherListActivity extends BaseActivity implements OnRefreshListener{

	private RequestQueue mQueue;
	private ImageLoader mImageLoader;
	private ArrayList<Teacher> teachers = new ArrayList<Teacher>();

	// 回退按钮
	@InjectView(R.id.lt_back)
	ImageButton lt_back;

	@OnClick(R.id.lt_back)
	public void lt_back() {
		ActUtil.getInstance().MoveToNewAct(TeacherListActivity.this,
				MainActivity.class);
		AppManager.getInstance().finishActivity(TeacherListActivity.this);
	}

	//刷新空间
	@InjectView(R.id.lt_refreshview)
	ViewPagerSwipeRefreshLayout lt_refreshview;
	
	// 老师列表
	@InjectView(R.id.lt_teacherlist)
	BounceListView lt_teacherlist;
	// 教师列表适配器
	TeacherListAdapter mAdapter;

	@Override
	public void init(Bundle savedInstanceState) {
		ButterKnife.inject(this);
		mQueue = SingleRequestQueue.getRequestQueue(TeacherListActivity.this);
		mImageLoader = new ImageLoader(mQueue, new BitmapCache());
		lt_refreshview.setColorSchemeResources(
				android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		lt_refreshview.setOnRefreshListener(this);
		initTeacherlist();
		new Thread(new Runnable() {
			@Override
			public void run() {
				initTeacherData();
			}
		}).start();
		
	}

	@Override
	public int getLayoutId() {
		return R.layout.layout_teacherlist;
	}

	private void initTeacherlist() {
		mAdapter = new TeacherListAdapter(TeacherListActivity.this, teachers,
				mImageLoader);
		lt_teacherlist.setOnItemClickListener(lt_teacherListener);
		lt_teacherlist.setAdapter(mAdapter);
//		ActUtil.getInstance().setListViewHeightBasedOnChildren(lt_teacherlist);
	}
	
	
	OnItemClickListener lt_teacherListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			CustomToast.getInstance().showToast(TeacherListActivity.this, "asdasd");
			ActUtil.getInstance().MoveToNewAct(TeacherListActivity.this, TeacherDetailsActivity.class);
		}
	};

	private void initTeacherData() {
		teachers.clear();
		// 初始化教师信息
		Teacher teacher = new Teacher();
		teacher.setTeacher_name("大保健");
		teacher.setTeacher_range("100");
		teacher.setTeacher_address("花果园大街B区13栋");
		teacher.setTeacher_img("http://imgsrc.baidu.com/forum/w%3D580/sign=04d241b7dd33c895a67e9873e1127397/02d5c895d143ad4bdae570e086025aafa50f0628.jpg");
		ArrayList<String> lables = new ArrayList<String>();
		lables.add("按摩");
		lables.add("足疗");
		lables.add("桑拿");
		lables.add("保健");
		teacher.setTeacher_lable(lables);
		ArrayList<String> strings = new ArrayList<String>();
		strings.add("可上门");
		strings.add("保健资格证");
		teacher.setTeacher_aptitude(strings);
		ArrayList<String> imgaddrs = new ArrayList<String>();
		imgaddrs.add("http://f.hiphotos.baidu.com/zhidao/pic/item/622762d0f703918f9d18f9ca513d269758eec4e1.jpg");
		imgaddrs.add("http://n.7k7kimg.cn/2013/0608/1370678110622.jpg");
		imgaddrs.add("http://b.hiphotos.baidu.com/zhidao/pic/item/241f95cad1c8a786504f617c6409c93d70cf505e.jpg");
		imgaddrs.add("http://img1.imgtn.bdimg.com/it/u=2135870666,1269143969&fm=21&gp=0.jpg");
		imgaddrs.add("http://img4.imgtn.bdimg.com/it/u=3898285183,3828688237&fm=21&gp=0.jpg");
		imgaddrs.add("http://img4.duitang.com/uploads/item/201407/14/20140714113451_KL4UN.thumb.700_0.jpeg");
		teacher.setTeacher_listimgs(imgaddrs);
		teachers.add(teacher);

		Teacher teacher1 = new Teacher();
		teacher1.setTeacher_name("小保健");
		teacher1.setTeacher_range("50");
		teacher1.setTeacher_address("花果园大街B区13栋");
		teacher1.setTeacher_img("http://img4.imgtn.bdimg.com/it/u=2758854116,1338504272&fm=21&gp=0.jpg");
		ArrayList<String> lables1 = new ArrayList<String>();
		lables1.add("按摩");
		lables1.add("足疗");
		lables1.add("保健");
		teacher1.setTeacher_lable(lables1);
		ArrayList<String> strings1 = new ArrayList<String>();
		strings1.add("必须可上门");
		strings1.add("大保健资格证");
		teacher1.setTeacher_aptitude(strings1);
		ArrayList<String> imgaddrs1 = new ArrayList<String>();
		imgaddrs1
				.add("http://f.hiphotos.baidu.com/zhidao/pic/item/622762d0f703918f9d18f9ca513d269758eec4e1.jpg");
		imgaddrs1
				.add("http://img4.duitang.com/uploads/item/201407/14/20140714113451_KL4UN.thumb.700_0.jpeg");
		imgaddrs1.add("http://n.7k7kimg.cn/2013/0608/1370678110622.jpg");
		imgaddrs1
				.add("http://img4.imgtn.bdimg.com/it/u=3898285183,3828688237&fm=21&gp=0.jpg");
		imgaddrs1
				.add("http://b.hiphotos.baidu.com/zhidao/pic/item/241f95cad1c8a786504f617c6409c93d70cf505e.jpg");
		imgaddrs1
				.add("http://img1.imgtn.bdimg.com/it/u=2135870666,1269143969&fm=21&gp=0.jpg");
		teacher1.setTeacher_listimgs(imgaddrs1);

		teachers.add(teacher1);

		actHandler.sendEmptyMessage(1);
	}

	private MyActHandler actHandler = new MyActHandler(this) {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				mAdapter.notifyDataSetChanged();
//				ActUtil.getInstance().setListViewHeightBasedOnChildren(
//						lt_teacherlist);
				break;
			}
		}

	};

	@Override
	public void onRefresh() {	
		actHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				lt_refreshview.setRefreshing(false);
				AnimationToast.getInstance().showNotify(TeacherListActivity.this,
						"刷新完成", R.id.lt_toast);
			}
		}, 2000);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		AnimationToast.getInstance().destroy();
	}

}
