package com.septinary.xbwapp.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ActUtil {

	private static class ActUtilHolder {
		private static final ActUtil INSTANCE = new ActUtil();
	}

	private ActUtil() {
	}

	public static final ActUtil getInstance() {
		return ActUtilHolder.INSTANCE;
	}

	/* Activity跳转方法 */
	public void MoveToNewAct(Context context, Class<?> cls) {
		Intent intent = new Intent(context, cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);

	}

	/**
	 * 手机号码验证
	 * */
	public boolean checkPhone(String phone) {

		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][358][0-9]{9}$"); // 验证手机�?
		m = p.matcher(phone);
		b = m.matches();
		return b;
	}


	/**
	 * 判断参数值是否空
	 * */
	public boolean IsParamNull(String... param) {
		boolean rs = true;
		for (String temp : param) {
			if (temp == null || temp.equals(""))
				rs = false;
		}
		return rs;

	}

	/* 带参数的Activity跳转 */
	public void MoveToNewAct(Context mcontext, Class<?> cls, String keys[],
			String values[]) {
		Intent intent = new Intent(mcontext, cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle bundle = new Bundle();
		for (int i = 0; i < keys.length; i++) {
			bundle.putString(keys[i], values[i]);
		}
		intent.putExtras(bundle);
		mcontext.startActivity(intent);

	}

	/**
	 * 获取设备的ID
	 * */
	public String ReadDevId(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String devid = tm.getDeviceId();
		return devid;
	}

	//
	public void MoveToNewAct(Context mcontext, Class<?> cls, String keys[],
			HashMap<String, String> values) {
		Intent intent = new Intent(mcontext, cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle bundle = new Bundle();
		for (int i = 0; i < keys.length; i++) {
			bundle.putString(keys[i], values.get(keys[i]));
		}
		intent.putExtras(bundle);
		mcontext.startActivity(intent);

	}

	/* 封装�?��progressDialog */
	public ProgressDialog ShowNewProgressDlg(Context context) {
		ProgressDialog pdg = new ProgressDialog(context);
		pdg.setProgressStyle(AlertDialog.THEME_HOLO_LIGHT);
		pdg.setTitle(null);
		pdg.setIcon(null);
		// 设置提示信息
		pdg.setMessage("加载中");
		// 设置是否可以通过返回键取�?
		pdg.setCancelable(true);
		pdg.setIndeterminate(false);
		return pdg;

	}

	/*
	 * 创建�?��CustomProgressDialog public CustomProgressDialog
	 * showCustomProgressDialog(Context context){
	 */

	private int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// 源图片的高度和宽�?
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比�?
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中�?��的比率作为inSampleSize的�?，这样可以保证最终图片的宽和�?
			// �?��都会大于等于目标的宽和高�?
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	// �?��比例缩放图片，自己定义合适的宽高
	public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
			int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		// 调用上面定义的方法计算inSampleSize�?
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// 使用获取到的inSampleSize值再次解析图�?
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	// 按比例缩放图�?
	public Bitmap getBitmapAsSize(int rs, Context context, int inSampleSize) {
		Bitmap bitmap = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = inSampleSize;
		bitmap = BitmapFactory.decodeResource(context.getResources(), rs, opts);
		return bitmap;
	}

	// 将Bitmap转换成InputStream
	public InputStream Bitmap2InputStream(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	// 转换为bitmap
	public Bitmap drawableToBitmap(Drawable drawable) // drawable 转换�?bitmap
	{
		int width = drawable.getIntrinsicWidth(); // �?drawable 的长�?
		int height = drawable.getIntrinsicHeight();
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565; // �?drawable 的颜色格�?
		Bitmap bitmap = Bitmap.createBitmap(width, height, config); // 建立对应
																	// bitmap
		Canvas canvas = new Canvas(bitmap); // 建立对应 bitmap 的画�?
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas); // �?drawable 内容画到画布�?
		return bitmap;
	}

	// 缩放drawable
	@SuppressWarnings("deprecation")
	public Drawable zoomDrawable(Drawable drawable, int w, int h) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap oldbmp = drawableToBitmap(drawable); // drawable 转换�?bitmap
		Matrix matrix = new Matrix(); // 创建操作图片用的 Matrix 对象
		float scaleWidth = ((float) w / width); // 计算缩放比例
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidth, scaleHeight); // 设置缩放比例
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
				matrix, true); // 建立新的 bitmap ，其内容是对�?bitmap 的缩放后的图
		return new BitmapDrawable(newbmp); // �?bitmap 转换�?drawable 并返�?
	}

	/**
	 * 以最省内存的方式读取本地资源的图�?
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 网络 情况 �?��
	 * */
	/**
	 * 对网络连接状态进行判�?
	 * 
	 * @return true, 可用�?false�?不可�?
	 */
	public boolean isOpenNetwork(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
			return connManager.getActiveNetworkInfo().isAvailable();
		}

		return false;
	}
}
