package com.pdf.moufsidetalkouloub.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.pdf.moufsidetalkouloub.externals.PDFDataBase;

/**
 * This is Super Scaler
 *
 *
 * Moufsideet Al Kouloub
 * @author HICHEM LAROUSSI - RAMI TRABELSI
 * Copyright (c) 2014 Zad Group. All rights reserved.
 */
 
public class MonitoredActivity extends Activity  {
	
	private final ArrayList<LifeCycleListener> mListeners =
            new ArrayList<LifeCycleListener>();

    public static interface LifeCycleListener {

        public void onActivityCreated(MonitoredActivity activity);

        public void onActivityDestroyed(MonitoredActivity activity);

        public void onActivityPaused(MonitoredActivity activity);

        public void onActivityResumed(MonitoredActivity activity);

        public void onActivityStarted(MonitoredActivity activity);

        public void onActivityStopped(MonitoredActivity activity);
    }

    public static class LifeCycleAdapter implements LifeCycleListener {

        public void onActivityCreated(MonitoredActivity activity) {

        }

        public void onActivityDestroyed(MonitoredActivity activity) {

        }

        public void onActivityPaused(MonitoredActivity activity) {

        }

        public void onActivityResumed(MonitoredActivity activity) {

        }

        public void onActivityStarted(MonitoredActivity activity) {

        }

        public void onActivityStopped(MonitoredActivity activity) {

        }
    }

    public void addLifeCycleListener(LifeCycleListener listener) {

        if (mListeners.contains(listener)) return;
        mListeners.add(listener);
    }

    public void removeLifeCycleListener(LifeCycleListener listener) {

        mListeners.remove(listener);
    }
	
	private static final String TAG = MonitoredActivity.class.getSimpleName();
	public static float scale ;
	public static boolean scaled = false;
	
	protected Activity thisAct ;
	
	public static int screen_width;
	public static int screen_height;
	public static int my_font_size ;
	
	protected boolean isFirstStart = true;
	
	public static boolean isTablet ;
	
	public PDFDataBase pdfDB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		for (LifeCycleListener listener : mListeners) {
            listener.onActivityCreated(this);
        }
		
		pdfDB = new PDFDataBase(this);
		
		memoryAnalyser();
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screen_width = size.x;
		screen_height = size.y;
		
		isTablet = isTablet(this);
		
		Log.e("SCREEN WIDTH *****", String.valueOf(screen_width));
		Log.e("SCREEN Height *****", String.valueOf(screen_height));
		Log.e("SCALE *****", String.valueOf(scale));
		
		
//		if (tabletInchSize()> 8.5 ) my_font_size = 14 ;
//		
//		else if (tabletInchSize()>= 6 && tabletInchSize()<= 7.5) my_font_size =  10;
//		
//		else my_font_size = 9 ;
		
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		for (LifeCycleListener listener : mListeners) {
			listener.onActivityStarted(this);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		for (LifeCycleListener listener : mListeners) {
            listener.onActivityDestroyed(this);
        }
		
		memoryAnalyser();
	}

		
		
		
	public void onBackPressed() {
		super.onBackPressed();
		
	}
	
	public static boolean isTablet(Context context) {
	    return (context.getResources().getConfiguration().screenLayout
	            & Configuration.SCREENLAYOUT_SIZE_MASK)
	            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		thisAct = this;
		
		if(pdfDB == null){
			pdfDB = new PDFDataBase(this);
		}
	
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		thisAct = null;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		if(pdfDB != null){
			pdfDB.close();
			pdfDB = null;
		}
		
		for (LifeCycleListener listener : mListeners) {
            listener.onActivityStopped(this);
        }
	}
	
    protected double tabletInchSize(){
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		float widthInInches = metrics.widthPixels / metrics.xdpi;
		float heightInInches = metrics.heightPixels / metrics.ydpi;
		
		double sizeInInches = Math.sqrt(Math.pow(widthInInches, 2) + Math.pow(heightInInches, 2));
		//0.5" buffer for 7" devices
	//	boolean is7inchTablet = sizeInInches >= 6.5 && sizeInInches <= 7.5; 
		
		Log.e("//////////////// SIZE IN INCH ////////////////", String.valueOf(sizeInInches));
		
		return sizeInInches ;
	}
	
    protected void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	}
    
	public void memoryAnalyser(){
		
		Log.i(TAG,"... Memory Analyser check test ");
		Runtime r = Runtime.getRuntime();
		long mem0 = r.totalMemory();
		Log.v(TAG,"Total memory is: " + (int)(mem0 / (1024*1024)) + " MB"); 
		long mem1 = r.freeMemory();
		Log.v(TAG,"Initial free memory: " + (int)(mem1 / (1024*1024)) + " MB");
		long mem2 = r.maxMemory();
		Log.v(TAG,"Max memory: " + (int)(mem2 / (1024*1024)) + " MB");

		Log.v(TAG,"Memory usage : " + (int)((mem0*100)/mem2) + " %");
		
		Log.v(TAG,"Memory allocated : " + (int)((mem0 - mem1) / (1024*1024)) + " MB");
		
		long mem_alloc = Debug.getNativeHeapAllocatedSize();
		Log.v(TAG,"Native Heap memory Allocated : " + (int)(mem_alloc / (1024*1024)) + " MB");
	}

	
	protected Drawable createCopy(Bitmap b){
		Config config = Config.ARGB_8888;
		Bitmap bm = b.copy(config, false);
		return new BitmapDrawable(getResources(), bm);
	}
}
