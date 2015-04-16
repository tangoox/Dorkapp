package com.septinary.xbwapp.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.septinary.xbwapp.base.BitmapCache;

public class SingleImageLoader {

	private static ImageLoader mImageLoader;
	private RequestQueue mQueue;
	private SingleImageLoader(Context context){
		mQueue = SingleRequestQueue.getRequestQueue(context);
		mImageLoader = new ImageLoader(mQueue,new BitmapCache());
	};
	
	public static synchronized ImageLoader getImageLoader(Context context){
        if (mImageLoader == null){
            new SingleImageLoader(context.getApplicationContext());
        }
        return mImageLoader;
    } 
	
}
