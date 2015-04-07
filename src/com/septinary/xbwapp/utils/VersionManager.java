package com.septinary.xbwapp.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;



/**
 * 版本更新
 * 
 * @author Administrator
 * */

public class VersionManager {

	Context mContext;
	List<HashMap<String, String>> version_info;
	Handler handler;

	public VersionManager(Context mContext,
			List<HashMap<String, String>> version_info, Handler handler) {
		this.mContext = mContext;
		this.version_info = version_info;
		this.handler = handler;
	}

	// 获取当前版本�?
	public String getVsrsionName() {
		String version_name = "";
		try {
			version_name = mContext.getPackageManager().getPackageInfo(
					"程序包名", 1).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version_name;
	}

	// 版本升级
	public void version_updata(int i) {
		// 版本相同,给出提示不更�?
		if (compare_version()) {
			if (i == 1)
				CustomToast.getInstance().showToast(mContext, "当前为最新版本无需更新");
			// 版本不同,给出更新提示
		} else {
			shwoUpdataDialog();
		}
	}

	public boolean compare_version() {
		float tag1 = Float.valueOf(version_info.get(0).get("AndroidVersion"));
		float tag2 = Float.valueOf(getVsrsionName());
		if (tag1 > tag2) {
			// 当前版本比服务器�?
			return false;
		} else {
			return true;
		}
	}

	// 提示更新窗口
	public void shwoUpdataDialog() {
		downloadApk(); // 下载APK
	}

	// 下载Apk文件
	private void downloadApk() {
		final ProgressDialog pd;
		pd = new ProgressDialog(mContext);
		pd.setCancelable(false);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载更新");
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					File file = getFileFromServer(
							version_info.get(0).get("AndroidUrl"), pd);
					// 下载完成2秒后�?��安装
					sleep(2000);
					installApk(file);
					pd.dismiss();
				} catch (IOException e) {
					// 下载失败 给设置界面的handler发�?失败消息
					Message msg = new Message();
					msg.what = Contants.NETWORK_SUCCESS;
					msg.arg1 = Contants.VERSION_DEFULT;
					handler.sendMessage(msg);
					pd.dismiss();
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	// 从服务器下载Apk
	public File getFileFromServer(String path, ProgressDialog pd)
			throws IOException {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置5秒超�?
			conn.setConnectTimeout(5000);
			// 获取文件大小
			pd.setMax(conn.getContentLength());
			InputStream is = conn.getInputStream();
			File file = new File(Environment.getExternalStorageDirectory(),
					"hgy_project.apk");
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len;
			int total = 0;
			while ((len = bis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				total += len;
				pd.setProgress(total);
			}
			fos.close();
			bis.close();
			is.close();
			return file;
		} else {
			CustomToast.getInstance().showToast(mContext, "SD卡不可用");
		}
		return null;
	}

	// 安装apk
	public void installApk(File file) {
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类�?
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		mContext.startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}
