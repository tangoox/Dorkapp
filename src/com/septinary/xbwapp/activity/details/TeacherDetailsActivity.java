package com.septinary.xbwapp.activity.details;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.septinary.xbwapp.R;
import com.septinary.xbwapp.adapter.details.CourseListAdapter;
import com.septinary.xbwapp.adapter.details.InterestListAdapter;
import com.septinary.xbwapp.adapter.details.JudgeListAdapter;
import com.septinary.xbwapp.adapter.details.RecordListAdapter;
import com.septinary.xbwapp.adapter.details.TeacherDetailsViewPagerAdapter;
import com.septinary.xbwapp.base.BaseActivity;
import com.septinary.xbwapp.base.MyActHandler;
import com.septinary.xbwapp.model.main.Teacher;
import com.septinary.xbwapp.utils.ActUtil;
import com.septinary.xbwapp.utils.AnimationToast;
import com.septinary.xbwapp.utils.SingleImageLoader;
import com.septinary.xbwapp.views.FlowLayout;

/**
 * @author S_ven
 * @info 2015年4月14日11:15:22 教师详情页
 * */
public class TeacherDetailsActivity extends BaseActivity {

	private ImageLoader imageLoader;
	private int cur = 0;
	private Handler start = new Handler();
	private Teacher teacher;
	private Bundle bundle = null;

	private ArrayList<NetworkImageView> imgs = new ArrayList<NetworkImageView>();
	private NetworkImageView tempImgView;

	private ArrayList<String> courses = new ArrayList<String>();
	private ArrayList<HashMap<String, String>> records = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> judges = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> interests = new ArrayList<HashMap<String,String>>();
	// 轮换图片
	@InjectView(R.id.ltd_viewpager)
	ViewPager ltd_viewpager;
	private TeacherDetailsViewPagerAdapter detailsViewPagerAdapter;
	// 轮换图片提示
	@InjectView(R.id.ltd_imgtips)
	TextView ltd_imgtips;
	// 头像
	@InjectView(R.id.ltd_headimg)
	NetworkImageView ltd_headimg;
	// 名字
	@InjectView(R.id.ltd_name)
	TextView ltd_name;
	// 标签
	@InjectView(R.id.ltd_lable)
	FlowLayout ltd_lable;
	// 资质
	@InjectView(R.id.ltd_teacheraptitude)
	FlowLayout ltd_teacheraptitude;
	// 自述
	@InjectView(R.id.ltd_teachersay)
	TextView ltd_teachersay;
	// 评分
	@InjectView(R.id.ltd_judge)
	ProgressBar ltd_judge;
	// 评价分数
	@InjectView(R.id.ltd_judge_score)
	TextView ltd_judge_score;
	// 课程套餐
	@InjectView(R.id.ltd_course)
	ListView ltd_course;
	private CourseListAdapter courseListAdapter;
	// 教师履历
	@InjectView(R.id.ltd_record)
	ListView ltd_record;
	private RecordListAdapter recordListAdapter;
	// 教师地址
	@InjectView(R.id.ltd_address)
	TextView ltd_address;
	// 评论
	@InjectView(R.id.ltd_judges)
	ListView ltd_judges;
	private JudgeListAdapter judgeListAdapter;
	// 加载更多评论
	@InjectView(R.id.ltd_showmore)
	LinearLayout ltd_showmore;
	// 学霸认证
	@InjectView(R.id.ltd_xb_auth)
	FlowLayout ltd_xb_auth;
	private String[] auths = {"身份证","电话","邮箱","微信/QQ"};
	//感兴趣推荐
	@InjectView(R.id.ltd_interest)
	ListView ltd_interest;
	private InterestListAdapter interestListAdapter;

	@Override
	public int getLayoutId() {
		return R.layout.layout_teacherdetails;
	}

	@Override
	public void init(Bundle savedInstanceState) {
		ButterKnife.inject(this);
		imageLoader = SingleImageLoader
				.getImageLoader(TeacherDetailsActivity.this);
		// 获取传递过来的教师数据
		if (getBundleData()) {
			initTeacherInfo();
			initViewPager();
			initCourse();
			initRecord();
			initJudge();
			initInterest();
			new Thread() {
				public void run() {
					initImgs();
					initCourseData();
					initRecordData();
					initJudgeData();
					initAuthData();
					initInterestData();
				};
			}.start();
		} else {
			// 如果没有获取则提示
		}

	}

	// 初始化教师信息
	private void initTeacherInfo() {
		// 头像
		ltd_headimg.setDefaultImageResId(R.drawable.badimage);
		ltd_headimg.setErrorImageResId(R.drawable.badimage);
		ltd_headimg.setImageUrl(teacher.getTeacher_img(), imageLoader);
		// 名字
		ltd_name.setText(teacher.getTeacher_name());
		// 标签
		ltd_lable.removeAllViews();
		for (int i = 0; i < teacher.getTeacher_lable().size(); i++) {
			TextView view = new TextView(TeacherDetailsActivity.this);
			view.setText(teacher.getTeacher_lable().get(i));
			view.setBackgroundResource(R.color.lable);
			view.setTextColor(getResources().getColor(R.color.white));
			view.setGravity(Gravity.CENTER);
			view.setPadding(15, 0, 15, 0);
			view.setTextSize(14);
			ltd_lable.addView(view);
		}
		// 资质
		ltd_teacheraptitude.removeAllViews();
		for (int i = 0; i < teacher.getTeacher_aptitude().size(); i++) {
			TextView view = new TextView(TeacherDetailsActivity.this);
			view.setText(" " + teacher.getTeacher_aptitude().get(i));
			Drawable drawable = getResources().getDrawable(
					R.drawable.lt_lable_icon);
			// / 这一步必须要做,否则不会显示.
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			view.setCompoundDrawables(drawable, null, null, null);
			view.setGravity(Gravity.CENTER);
			view.setTextSize(16);
			ltd_teacheraptitude.addView(view);
		}
		// 自述
		ltd_teachersay.setText("草泥马！草泥马！草泥马！草泥马！草泥马！");
		// 评分
		ltd_judge.setProgress((int) (5.6 * 10));
		ltd_judge_score.setText(5.6 + "");

	}

	// 获取教师列表传递过来的数据
	private boolean getBundleData() {
		bundle = getIntent().getExtras();
		if (bundle != null) {
			try {
				teacher = (Teacher) bundle.getSerializable("teacher");
				if (teacher != null)
					return true;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	// 初始化轮换图片
	private void initViewPager() {
		detailsViewPagerAdapter = new TeacherDetailsViewPagerAdapter(imgs);
		ltd_imgtips.setText("1/" + teacher.getTeacher_listimgs().size());
		ltd_viewpager.setAdapter(detailsViewPagerAdapter);
		ltd_viewpager.setOnPageChangeListener(pageChangeListener);
		start.postDelayed(timetask, 5000);
	}

	OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			cur = arg0;
			ltd_imgtips.setText((arg0 + 1) + "/"
					+ teacher.getTeacher_listimgs().size());
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};

	Runnable timetask = new Runnable() {
		@Override
		public void run() {
			cur = (cur + 1) % imgs.size();
			ltd_viewpager.setCurrentItem(cur);
			start.postDelayed(timetask, 5000);
		}
	};

	// 初始化图片
	private void initImgs() {
		imgs.clear();
		for (int i = 0; i < teacher.getTeacher_listimgs().size(); i++) {
			tempImgView = new NetworkImageView(this.getApplicationContext());
			tempImgView.setDefaultImageResId(R.drawable.badimage);
			tempImgView.setErrorImageResId(R.drawable.badimage);
			tempImgView.setImageUrl(teacher.getTeacher_listimgs().get(i),
					imageLoader);
			tempImgView.setScaleType(ScaleType.FIT_XY);
			imgs.add(tempImgView);
			tempImgView = null;
		}
		detailsViewPagerAdapter.notifyDataSetChanged();
	}

	// 初始化课程列表
	private void initCourse() {
		courseListAdapter = new CourseListAdapter(TeacherDetailsActivity.this,
				courses);
		ltd_course.setAdapter(courseListAdapter);
	}

	// 初始化课程列表信息
	private void initCourseData() {
		courses.clear();
		courses.add("儿童保健(15课时)");
		courses.add("大保健(10课时)");
		courses.add("小保健(1课时)");
		courseListAdapter.notifyDataSetChanged();
		ActUtil.getInstance().setListViewHeightBasedOnChildren(ltd_course);
	}

	// 初始化教师履历列表
	private void initRecord() {
		recordListAdapter = new RecordListAdapter(TeacherDetailsActivity.this,
				records);
		ltd_record.setAdapter(recordListAdapter);
		ltd_address.setText("地址：金西监狱");
	}

	// 初始化教师履历数据
	private void initRecordData() {
		records.clear();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("time", "现在");
		map.put("address", "沙河花园");
		records.add(map);

		HashMap<String, String> map1 = new HashMap<String, String>();
		map1.put("time", "2014/4/15 - 2015/3/1");
		map1.put("address", "监狱");
		records.add(map1);
		recordListAdapter.notifyDataSetChanged();
		ActUtil.getInstance().setListViewHeightBasedOnChildren(ltd_record);
	}

	// 初始化评论列表
	private void initJudge() {
		judgeListAdapter = new JudgeListAdapter(TeacherDetailsActivity.this,
				judges, imageLoader);
		ltd_judges.setAdapter(judgeListAdapter);
		ltd_showmore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AnimationToast.getInstance().showNotify(
						TeacherDetailsActivity.this, "点击了查看更多评论",
						R.id.ltd_toast);
			}
		});
	}

	// 初始化评论列表数据
	private void initJudgeData() {
		judges.clear();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("img",
				"http://g.hiphotos.baidu.com/image/w%3D400/sign=b40bc5c2ca95d143da76e52343f28296/8ad4b31c8701a18b717a3b8e9c2f07082938fe5a.jpg");
		map.put("name", "BJ哥");
		map.put("date", "2015/4/16 12:02");
		map.put("judge", "好好好！");
		judges.add(map);
		HashMap<String, String> map1 = new HashMap<String, String>();
		map1.put(
				"img",
				"http://b.hiphotos.baidu.com/image/w%3D230/sign=75fc3b64cebf6c81f7372beb8c3fb1d7/7c1ed21b0ef41bd5441037f853da81cb39db3d7f.jpg");
		map1.put("name", "事实上实施");
		map1.put("date", "2015/3/16 11:02");
		map1.put("judge", "好好好！好好好！好好好！好好好！好好好！好好好！好好好！好好好！");
		judges.add(map1);
		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put(
				"img",
				"http://f.hiphotos.baidu.com/image/w%3D230/sign=9c4075c8d52a60595210e6191834342d/a2cc7cd98d1001e9f44e8d0eba0e7bec54e79745.jpg");
		map2.put("name", "地方而");
		map2.put("date", "2015/4/12 15:47");
		map2.put("judge", "sdsdf士大夫士大夫");
		judges.add(map2);
		actHandler.sendEmptyMessage(1);
	};

	//初始化学霸认证
	private void initAuthData() {
		ltd_xb_auth.removeAllViews();
		for (int i = 0; i < auths.length; i++) {
			TextView view = new TextView(TeacherDetailsActivity.this);
			view.setText(auths[i]);
			view.setBackgroundResource(R.color.xb_color);
			view.setTextColor(getResources().getColor(R.color.white));
			view.setGravity(Gravity.CENTER);
			view.setPadding(15, 3, 15, 3);
			view.setTextSize(18);
			ltd_xb_auth.addView(view);
		}
	}
	
	//初始化感 兴趣列表
	private void initInterest(){
		interestListAdapter = new InterestListAdapter(TeacherDetailsActivity.this, interests, imageLoader);
		ltd_interest.setAdapter(interestListAdapter);
	}
	
	//初始化感兴趣列表数据
	private void initInterestData(){
		interests.clear();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("img", "http://img4.duitang.com/uploads/item/201308/09/20130809115652_GhiQE.thumb.700_0.jpeg");
		map.put("name", "迷茫哥");
		map.put("range", ">500m");
		map.put("address", "贵阳市");
		interests.add(map);
		HashMap<String, String> map1 = new HashMap<String, String>();
		map1.put("img", "http://img.boqiicdn.com/Data/BK/A/1408/19/img61631408431991_y.jpg");
		map1.put("name", "大概");
		map1.put("range", "<100m");
		map1.put("address", "问问请问请问");
		interests.add(map1);
		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put("img", "http://img5.imgtn.bdimg.com/it/u=2305608554,2782572337&fm=21&gp=0.jpg");
		map2.put("name", "底孤鸿寡鹄");
		map2.put("range", ">1000km");
		map2.put("address", "算得上是法士大夫士大夫似的");
		interests.add(map2);
		interestListAdapter.notifyDataSetChanged();
		ActUtil.getInstance().setListViewHeightBasedOnChildren(ltd_interest);
	}

	@Override
	protected void onPause() {
		start.removeCallbacks(timetask);
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (imgs.size() > 0) {
			start.postDelayed(timetask, 3500);
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		start.removeCallbacks(timetask);
		super.onDestroy();
	}

	private MyActHandler actHandler = new MyActHandler(this) {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (judges.size() > 2) {
					ltd_showmore.setVisibility(View.VISIBLE);
				} else {
					ltd_showmore.setVisibility(View.GONE);
				}
				judgeListAdapter.notifyDataSetChanged();
				ActUtil.getInstance().setListViewHeightBasedOnChildren(
						ltd_judges);
				break;
			}
		}

	};
}
