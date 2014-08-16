package com.pdf.moufsidetalkouloub;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;

import com.pdf.moufsidetalkouloub.externals.AKManager;
import com.pdf.moufsidetalkouloub.externals.Book;
import com.pdf.moufsidetalkouloub.utils.MySuperScaler;
import com.pdf.moufsidetalkouloub.utils.Utils;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("HandlerLeak")
public class MainBookChoice extends MySuperScaler implements OnClickListener {

	private Button info ;
	private ImageView book_1, book_2, book_3, book_4 ,book_5 ,book_6 ,
						book_7,book_8 , book_9 , book_10 ,book_11, book_12 , img_cover;
	
	private ArrayList<Book> books;
	
	private RelativeLayout principal_layout ;
	private ScrollView scrollView;
	
	private String pdfFile ;
	private int book_id;
 	
	private AnimationSet animationSet, animationSet2, animationSet3;
	private TranslateAnimation translate;
	private Animation zoomin, alpha, alphaToHide;
	private AKManager akManager;
	
	private boolean isFinished = false;
	private boolean isClickEnabled = true;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			LayoutParams params = new RelativeLayout.LayoutParams(screen_width, screen_height);
			img_cover.setLayoutParams(params);
			img_cover.startAnimation(animationSet3);
		};
	};
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_books_choice);
		
		akManager = AKManager.getInstance(this);
		books = akManager.getBooks();
		
		zoomin = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
		alpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
		alphaToHide = AnimationUtils.loadAnimation(this, R.anim.alphatohide);
		
		animationSet3 = new AnimationSet(true);
		animationSet3.addAnimation(zoomin);
		animationSet3.addAnimation(alpha);
		animationSet3.setFillAfter(true);
		animationSet3.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {

				if(!isFinished){
					Message msg =  Message.obtain();
					msg.what = 1;
					splashHandler.sendMessageDelayed(msg, 2000);
				}
			}
		});

		principal_layout = (RelativeLayout) findViewById(R.id.principal_layout);
		img_cover = (ImageView) findViewById(R.id.img_cover);
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		
		
		info = (Button) findViewById(R.id.info);
		
		book_1 = (ImageView) findViewById(R.id.book_1);
		book_2 = (ImageView) findViewById(R.id.book_2);
		book_3 = (ImageView) findViewById(R.id.book_3);
		book_4 = (ImageView) findViewById(R.id.book_4);
		book_5 = (ImageView) findViewById(R.id.book_5);
		book_6 = (ImageView) findViewById(R.id.book_6);
		book_7 = (ImageView) findViewById(R.id.book_7);
		book_8 = (ImageView) findViewById(R.id.book_8);
		book_9 = (ImageView) findViewById(R.id.book_9);
		book_10 = (ImageView) findViewById(R.id.book_10);
		book_11 = (ImageView) findViewById(R.id.book_11);
		book_12 = (ImageView) findViewById(R.id.book_12);
		

		int w = book_1.getWidth();
		int h = book_1.getHeight();
		
		for (int i = 0; i < books.size(); i++) {

			Book b = books.get(i);
			Bitmap bm = AKManager.originalResolution(this, b.getCover(), w, h);
			Drawable d = new BitmapDrawable(getResources(), bm);

			switch (i+1) {
			case 1:
				book_1.setTag(i);
				book_1.setBackgroundDrawable(d);
				break;
			case 2:
				book_2.setTag(i);
				book_2.setBackgroundDrawable(d);
				break;
			case 3:
				book_3.setTag(i);
				book_3.setBackgroundDrawable(d);
				break;
			case 4:
				book_4.setTag(i);
				book_4.setBackgroundDrawable(d);
				break;
			case 5:
				book_5.setTag(i);
				book_5.setBackgroundDrawable(d);
				break;
			case 6:
				book_6.setTag(i);
				book_6.setBackgroundDrawable(d);
				break;
			case 7:
				book_7.setTag(i);
				book_7.setBackgroundDrawable(d);
				break;
			case 8:
				book_8.setTag(i);
				book_8.setBackgroundDrawable(d);
				break;
			case 9:
				book_9.setTag(i);
				book_9.setBackgroundDrawable(d);
				break;
			case 10:
				book_10.setTag(i);
				book_10.setBackgroundDrawable(d);
				break;
			case 11:
				book_11.setTag(i);
				book_11.setBackgroundDrawable(d);
				break;
			case 12:
				book_12.setTag(i);
				book_12.setBackgroundDrawable(d);
				break;
			default :
				break;
			}
		}
		
		
		book_1.setOnClickListener(this);
		book_2.setOnClickListener(this);
		book_3.setOnClickListener(this);
		book_4.setOnClickListener(this);
		book_5.setOnClickListener(this);
		book_6.setOnClickListener(this);
		book_7.setOnClickListener(this);
		book_8.setOnClickListener(this);
		book_9.setOnClickListener(this);
		book_10.setOnClickListener(this);
		book_11.setOnClickListener(this);
		book_12.setOnClickListener(this);

		
		info.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				startActivity(new Intent(MainBookChoice.this, AboutActivity.class));
				overridePendingTransition(R.anim.left_in, R.anim.left_out);
				finish();
			}
		});
		
		
		
	}
	public static void setMargins (View v, View srcView, int myNewX, int myNewY) {
		RelativeLayout.LayoutParams absParams = 
				(RelativeLayout.LayoutParams)srcView.getLayoutParams();
		absParams.leftMargin = myNewX;
		
		if(!isTablet)
//			absParams.topMargin = myNewY;
			absParams.bottomMargin = (int)(screen_height - myNewY + srcView.getHeight() - 0.46875 * screen_height);
		else
			absParams.bottomMargin = (int)(screen_height - myNewY + srcView.getHeight() - 0.234375 * screen_height);
		
//		if(absParams.height < 0)
//			absParams.height = (int) (0.2390625 * screen_height);
		
		v.setLayoutParams(absParams);
	}

	private Handler splashHandler = new Handler() {
		
		public void handleMessage(Message msg) {

			Intent i = new Intent(MainBookChoice.this, PDFViewerActivity.class) ;
			i.putExtra("book", pdfFile);
			i.putExtra("book_id", book_id);
			
			startActivity(i);
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
			finish();

			super.handleMessage(msg);
		}
	};
	
	@Override
	public void onClick(final View v) {
		
		if(!isClickEnabled)
			return;
		
		Rect scrollBounds = new Rect();
		scrollView.getHitRect(scrollBounds);
		if (v.getLocalVisibleRect(scrollBounds)) {
		    // Any portion of the imageView, even a single pixel, is within the visible window
			Log.i(""," view in bounds " + scrollBounds.toShortString());
			Log.i(""," left " + scrollBounds.left + 
					" ... top " + scrollBounds.top + 
					" ... right " + scrollBounds.right + 
					" ... bottom " + scrollBounds.bottom);
			if(scrollBounds.bottom < scrollBounds.right)
			{
				scrollView.smoothScrollBy(0, book_1.getHeight());
				return;
			}
			
		} else {
		    // NONE of the imageView is within the visible window
			Log.i(""," view out of bounds");
			return;
		}

		int selectedPosition = (Integer) v.getTag();
		pdfFile = books.get(selectedPosition).getPdfFile();
		book_id = books.get(selectedPosition).getId();
		
		v.setVisibility(View.INVISIBLE);
		img_cover.setBackgroundDrawable(v.getBackground());
		img_cover.bringToFront();
		img_cover.setVisibility(View.VISIBLE);
		
		int[] locations = new int[2];
		v.getLocationOnScreen(locations);
		int x = locations[0];
		int y = locations[1];
		
		Log.i("", "locations:  X " + x + " ... Y " + y );
		setMargins(img_cover, v, x, y);
		
		animCounter = 0;
		moveViewToScreenCenter(img_cover);
		
		isClickEnabled = false;

	}
	
	int animCounter = 0;
	private void moveViewToScreenCenter( final View view )
	{
	    DisplayMetrics dm = new DisplayMetrics();
	    this.getWindowManager().getDefaultDisplay().getMetrics( dm );
	    int statusBarOffset = dm.heightPixels - principal_layout.getMeasuredHeight();

	    int originalPos[] = new int[2];
	    view.getLocationOnScreen( originalPos );

	    int xDest = dm.widthPixels/2;
	    xDest -= (view.getMeasuredWidth()/2);
	    int yDest = dm.heightPixels/2 - (view.getMeasuredHeight()/2) - statusBarOffset;

	    translate = new TranslateAnimation( 0, xDest - originalPos[0] , 0, yDest - originalPos[1] );
	    translate.setDuration(100);
	    translate.setFillAfter( true );
	    
	    translate.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				if(animCounter == 0)
				{
					moveViewToScreenCenter(view);
					animCounter++;
				}
			}
		});
	    
	    AlphaAnimation alphaFictive = new AlphaAnimation( 0, 0);
	    alphaFictive.setDuration(100);
	    alphaFictive.setFillAfter( false );
	    
	    animationSet = new AnimationSet(true);
		animationSet.addAnimation(alphaFictive);
		animationSet.addAnimation(translate);
		animationSet.setFillAfter(false);
		animationSet.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				translate.setDuration(1500);
				animationSet2.addAnimation(translate);
				
				view.startAnimation(animationSet2);
				mHandler.sendMessageDelayed(new Message(), 1400);
			}
		});
		
		animationSet2 = new AnimationSet(true);
		animationSet2.addAnimation(alphaToHide);
		animationSet2.setFillAfter(true);
		
	    view.startAnimation(animationSet);
	    
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		img_cover.clearAnimation();
		img_cover.setVisibility(View.GONE);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Utils.cleanViews(principal_layout);
		
		book_1 = null;
		book_2 = null;
		book_3 = null;
		book_4 = null;
		book_5 = null;
		book_6 = null;
		book_7 = null;
		book_8 = null;
		book_9 = null;
		book_10 = null;
		book_11 = null;
		book_12 = null;
		
		img_cover = null;
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		isFinished = true;
		
	}
}
