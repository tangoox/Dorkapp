package com.septinary.xbwapp.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;

import com.gitonway.lee.niftynotification.R;
import com.gitonway.lee.niftynotification.lib.Configuration;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;

public class AnimationToast {

	private NiftyNotificationView view;
	
	private final static class AnimationToastHandler{
		private final static AnimationToast INSTANCE = new AnimationToast();
	}
	
	private AnimationToast(){}
	
	public static AnimationToast getInstance(){
		return AnimationToastHandler.INSTANCE;
	}
	
	private Effects effect;
	
	public void showNotify(Activity act,String msg,int res){
		effect = Effects.thumbSlider;
//		NiftyNotificationView.build(act,msg,effect,res)
//		.setIcon(R.drawable.lion)         //You must call this method if you use ThumbSlider effect
//        .show();
		
		Configuration cfg=new Configuration.Builder()
		 .setAnimDuration(700)
		 .setDispalyDuration(1500)
		 .setBackgroundColor("#FFBDC3C7")
		 .setTextColor("#FF444444")
		 .setIconBackgroundColor("#FFFFFFFF")
		 .setTextPadding(5) //dp
		 .setViewHeight(48) //dp
		 .setTextLines(2) //You had better use setViewHeight and setTextLines
		// together
		 .setTextGravity(Gravity.CENTER) //only text def
		// Gravity.CENTER,contain icon Gravity.CENTER_VERTICAL
		 .build();
		//
		view = NiftyNotificationView.build(act,msg, effect,res,cfg);
		view.setIcon(R.drawable.lion) //remove this line ,only text
//		 .setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View view) {
		// //add your code
		// }
		// })
		 .show(false); // show(boolean) allow duplicates or showSticky() sticky
		// notification,you can call removeSticky() method close it
		Log.e("11111111111111", "show执行完成");
	}
	
	public void destroy(){
		if(view != null){
			view.removeSticky();
			view = null;
		}
	}
}
