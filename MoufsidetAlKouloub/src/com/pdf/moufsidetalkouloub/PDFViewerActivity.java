package com.pdf.moufsidetalkouloub;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.pdf.moufsidetalkouloub.externals.AKManager;
import com.pdf.moufsidetalkouloub.utils.MySuperScaler;

public class PDFViewerActivity extends MySuperScaler implements OnLoadCompleteListener, OnPageChangeListener, OnSeekBarChangeListener {

	private PDFView pdf ;

	public static final int REQUEST_CODE_CROP_IMAGE   	= 0x1;
	public static final int REQUEST_CODE_SHARE   		= 0x2;

	private static final String PARTS_FRAGMENT = "parts_fragment";
	private static final String BOOKMARKS_FRAGMENT = "bookmark_fragment";

	private Fragment fragment;

	private int book_id;

	private ImageView preview1, preview2, preview3, preview4, preview5, 
	preview6, preview7, preview8, preview9, preview10, preview11, preview12, preview13, preview14 ;

	private ImageView back, add_bookmark, bookmark_list, crop, list_summary ;
	private RelativeLayout  bottom_layout ;

	FrameLayout frame_layout;
	private LinearLayout top_layout;
	private int pdf_pages_number ;
	private SeekBar bar ;
	private TextView txt_pages ;

	private String filePath;
	private Uri resultUri;
	private String baseStoragePath;
	private AKManager akManager;
	
	int last ;
	private boolean fromSeekBar = false;
	private boolean seeking = false;
	public static int inversed_page ;
	boolean enable64 = false ;
	private boolean enableJump = false;
	
	private Animation fadein, fadeout;
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			updatePreviews();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pdf_shower);

		akManager = AKManager.getInstance(this);
		pdf = (PDFView) findViewById(R.id.pdfView);

		fadein = AnimationUtils.loadAnimation(this, R.anim.fadein);
		fadeout = AnimationUtils.loadAnimation(this, R.anim.fadeout);
		
		fadein.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				top_layout.setVisibility(View.VISIBLE);
				bottom_layout.setVisibility(View.VISIBLE);
				
				toggleViewEnable(true);
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				
			}
		});
		
		fadeout.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				top_layout.setVisibility(View.GONE);
				bottom_layout.setVisibility(View.GONE);
				
				toggleViewEnable(false);
			}
		});

		top_layout = (LinearLayout) findViewById(R.id.top_layout);
		bottom_layout = (RelativeLayout) findViewById(R.id.bottom_layout);
		frame_layout = (FrameLayout) findViewById(R.id.fragment_view);
		txt_pages = (TextView) findViewById(R.id.pages);

		int size = (int) screen_width / 25 ;
		txt_pages.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		txt_pages.bringToFront();
		
		
		preview1 = (ImageView) findViewById(R.id.preview1);
		preview2 = (ImageView) findViewById(R.id.preview2);
		preview3 = (ImageView) findViewById(R.id.preview3);
		preview4 = (ImageView) findViewById(R.id.preview4);
		preview5 = (ImageView) findViewById(R.id.preview5);
		preview6 = (ImageView) findViewById(R.id.preview6);
		preview7 = (ImageView) findViewById(R.id.preview7);
		preview8 = (ImageView) findViewById(R.id.preview8);
		preview9 = (ImageView) findViewById(R.id.preview9);
		preview10 = (ImageView) findViewById(R.id.preview10);

		if (isTablet(PDFViewerActivity.this)){
			preview11 = (ImageView) findViewById(R.id.preview11);
			preview12 = (ImageView) findViewById(R.id.preview12);
			preview13 = (ImageView) findViewById(R.id.preview13);
			preview14 = (ImageView) findViewById(R.id.preview14);
		}
		

		back = (ImageView) findViewById(R.id.pdf_back);
		add_bookmark = (ImageView) findViewById(R.id.pdf_bookmark);
		bookmark_list = (ImageView) findViewById(R.id.pdf_bookmark_list);
		crop = (ImageView) findViewById(R.id.pdf_crop);
		list_summary = (ImageView) findViewById(R.id.pdf_list);

		Bundle b = getIntent().getExtras();
		final String book_to_read = b.getString("book");
		book_id = b.getInt("book_id");
		int totalPages = akManager.getBooks().get(book_id - 1).getPagesNumber();

		pdf.setDrawingCacheEnabled(true);
		pdf.fromAsset(book_to_read + ".pdf")
		.pages(getPageNumbers(totalPages))
		.defaultPage(totalPages-1)
		.showMinimap(false)
		.enableSwipe(true)
		.onLoad(this)
		.onPageChange(this)
		.load();
		

		Bitmap bm1 = AKManager.originalResolution(this, "previews/"+book_to_read+"/1.png", preview1.getWidth(), preview1.getHeight());
		Bitmap bm2 = AKManager.originalResolution(this, "previews/"+book_to_read+"/2.png", preview2.getWidth(), preview2.getHeight());
		Bitmap bm3 = AKManager.originalResolution(this, "previews/"+book_to_read+"/3.png", preview3.getWidth(), preview3.getHeight());
		Bitmap bm4 = AKManager.originalResolution(this, "previews/"+book_to_read+"/4.png", preview4.getWidth(), preview4.getHeight());
		Bitmap bm5 = AKManager.originalResolution(this, "previews/"+book_to_read+"/5.png", preview5.getWidth(), preview5.getHeight());
		Bitmap bm6 = AKManager.originalResolution(this, "previews/"+book_to_read+"/6.png", preview6.getWidth(), preview6.getHeight());
		
		Bitmap bm7 = AKManager.originalResolution(this, "previews/"+book_to_read+"/7.png", preview7.getWidth(), preview7.getHeight());
		Bitmap bm8 = AKManager.originalResolution(this, "previews/"+book_to_read+"/8.png", preview8.getWidth(), preview8.getHeight());
		Bitmap bm9 = AKManager.originalResolution(this, "previews/"+book_to_read+"/4.png", preview4.getWidth(), preview4.getHeight());
		Bitmap bm10 = AKManager.originalResolution(this, "previews/"+book_to_read+"/5.png", preview5.getWidth(), preview5.getHeight());

		if (isTablet(PDFViewerActivity.this)){
			Bitmap bm11 = AKManager.originalResolution(this, "previews/"+book_to_read+"/7.png", preview7.getWidth(), preview7.getHeight());
			Bitmap bm12 = AKManager.originalResolution(this, "previews/"+book_to_read+"/8.png", preview8.getWidth(), preview8.getHeight());
			Bitmap bm13 = AKManager.originalResolution(this, "previews/"+book_to_read+"/9.png", preview9.getWidth(), preview9.getHeight());
			Bitmap bm14 = AKManager.originalResolution(this, "previews/"+book_to_read+"/10.png", preview10.getWidth(), preview10.getHeight());
		
			preview11.setImageDrawable(new BitmapDrawable(getResources(), bm11));
			preview12.setImageDrawable(new BitmapDrawable(getResources(), bm12));
			preview13.setImageDrawable(new BitmapDrawable(getResources(), bm13));
			preview14.setImageDrawable(new BitmapDrawable(getResources(), bm14));
		
		}
		


		preview1.setImageDrawable(new BitmapDrawable(getResources(), bm1));
		preview2.setImageDrawable(new BitmapDrawable(getResources(), bm2));
		preview3.setImageDrawable(new BitmapDrawable(getResources(), bm3));
		preview4.setImageDrawable(new BitmapDrawable(getResources(), bm4));
		preview5.setImageDrawable(new BitmapDrawable(getResources(), bm5));
		preview6.setImageDrawable(new BitmapDrawable(getResources(), bm6));
		preview7.setImageDrawable(new BitmapDrawable(getResources(), bm7));
		preview8.setImageDrawable(new BitmapDrawable(getResources(), bm8));
		preview9.setImageDrawable(new BitmapDrawable(getResources(), bm9));
		preview10.setImageDrawable(new BitmapDrawable(getResources(), bm10));


		bar = (SeekBar)findViewById(R.id.seekBar1); // make seekbar object
		bar.bringToFront();

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});

		list_summary.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				gotoFragment(PARTS_FRAGMENT);

			}
		});

		crop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				startCropImage();
			}
		});

		bookmark_list.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				gotoFragment(BOOKMARKS_FRAGMENT);

			}
		});

		add_bookmark.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				boolean isBookMarked = pdfDB.isBookMarked(book_id, inversed_page);
				if(isBookMarked){
					pdfDB.removeFromBookMarks(book_id, inversed_page);
				}else
					pdfDB.addToBookMarks(book_id, inversed_page);

				toggleBookMarkButton(!isBookMarked);

			}
		});

		last = totalPages - 1;

	}
	@Override
	public void loadComplete(int nbPages) {

		showTopBottom();
		pdf_pages_number = nbPages ;

		bar.setOnSeekBarChangeListener(this);
		bar.setMax(pdf_pages_number);
		bar.setProgress(pdf_pages_number);
		pdf.jumpTo(nbPages);
		Log.e("NUMBER OF PAGES", pdf_pages_number +"");

	}
	@Override
	public void onPageChanged(int page, int pageCount) {

		inversed_page = pdf_pages_number - page + 1 ;
		toggleBookMarkButton(pdfDB.isBookMarked(book_id, inversed_page));
		
		if (inversed_page > 0 && inversed_page < pdf_pages_number) enable64 = true ;
		
		if(!seeking && inversed_page > 0 && enable64){
		//	Toast.makeText(PDFViewerActivity.this, " صفحة "+inversed_page+" من "+pdf_pages_number, Toast.LENGTH_SHORT).show();
			txt_pages.setText(" صفحة "+inversed_page+" من "+pdf_pages_number);
		
		}
		
		if(!fromSeekBar)
		{
//			if (inversed_page == 1) showTopBottom() ;
//			else 
				if (page != last )hideTopBottom();
			else 
				if (pdf.getCurrentPage() == page - 1) toggleTopBottom();
		}
		
		mHandler.sendMessageDelayed(new Message(), 500);
		
		bar.setProgress(page);
		
		fromSeekBar = false;
		last = page;
		
	}


	private void toggleBookMarkButton(boolean isBookMarked) {
		if(isBookMarked)
			add_bookmark.setImageResource(R.drawable.pdf_bookmark_added);
		else
			add_bookmark.setImageResource(R.drawable.pdf_bookmark);
	}
	public Bitmap getBitmapFromAssets(String fileName) {

		AssetManager assetManager = getAssets();
		Bitmap bitmap = null;
		InputStream istr;
		try {
			istr = assetManager.open(fileName);
			bitmap = BitmapFactory.decodeStream(istr);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	@Override
	public void onBackPressed() {

		if(fragment == null){
			startActivity(new Intent( PDFViewerActivity.this, MainBookChoice.class));
			overridePendingTransition(R.anim.right_in, R.anim.right_out);
		}else{
			toggleEnabledViews(true);
			fragment = null;
		}
		super.onBackPressed();
	}

	public void onPageItemClicked(int pageTo){
		onBackPressed();
		int inversed = pdf_pages_number - pageTo + 1 ;
		pdf.jumpTo(inversed);
	}

	private void gotoFragment(String fragmentTAG){

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.setCustomAnimations(R.anim.down_in, R.anim.down_out, R.anim.up_in, R.anim.up_out);

		fragment = getSupportFragmentManager().findFragmentByTag(fragmentTAG);

		if(fragment == null){
			Log.i("", "new instance of views fragment................");
			if(fragmentTAG.equals(PARTS_FRAGMENT)){
				fragment = new BookContentFragment(book_id);
				transaction.add(R.id.fragment_view, fragment, fragmentTAG);
			}

			else 
			{
				fragment = new BookMarkFragment(book_id);
				transaction.replace(R.id.fragment_view, fragment, fragmentTAG);
				scaled = false;
			}

			transaction.addToBackStack(fragmentTAG);
		}else{
			Log.i("", "show the same instance");
			transaction.attach(fragment);
		}


		transaction.commit();

		toggleEnabledViews(false);
	}

	private void toggleEnabledViews(boolean enabled){
		pdf.setEnabled(enabled);
		back.setEnabled(enabled);
		add_bookmark.setEnabled(enabled);
		bookmark_list.setEnabled(enabled);
		crop.setEnabled(enabled);
		list_summary.setEnabled(enabled);
	}
	
	@Override
	public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
		fromSeekBar = true;
		int inversedProgress = pdf_pages_number - progress + 1 ;
		txt_pages.setText(" صفحة "+inversedProgress+" من "+pdf_pages_number);
	}
	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		seeking = true ;
		enableJump = true;
	}
	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		
		if(enableJump)
		{
			pdf.jumpTo(arg0.getProgress());
			enableJump = false;
		}
		
		seeking = false ;
	}

	public void updatePreviews()
	{

		pdf.buildDrawingCache();
		if(pdf.getDrawingCache() != null){
			int width = (int) (0.10416 * screen_width) ; // 75 sur S3 1280x720
			int height = (int) (0.0859375 * screen_height); // 110 sur S3 1280x720
			if(isTablet)
			{
				width = (int) (0.075 * screen_width); // 60 sur tablette 10" 1280x800
				height = (int) (0.0703125 * screen_height); // 90 sur tablette 10" 1280x800
			}
			Bitmap bm = Bitmap.createScaledBitmap(pdf.getDrawingCache(), width, height, false);
			Drawable d = new BitmapDrawable(getResources(), addGrayBorder(bm, 1));
			bar.setThumb(d);
		}
		pdf.destroyDrawingCache();
		
	}

	private Bitmap addGrayBorder(Bitmap bmp, int borderSize) {
	    Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
	    Canvas canvas = new Canvas(bmpWithBorder);
	    canvas.drawColor(Color.GRAY);
	    canvas.drawBitmap(bmp, borderSize, borderSize, null);
	    return bmpWithBorder;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK) {

			return;
		}

		switch (requestCode) {
		case REQUEST_CODE_CROP_IMAGE:

			String path = data.getStringExtra(ImageCropActivity.IMAGE_PATH);
			if (path == null) {
				return;
			}

			shareCroppedImage(filePath);
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	private void startCropImage() {
		pdf.buildDrawingCache();
		storeImage(pdf.getDrawingCache());
		pdf.destroyDrawingCache();

		Intent intent = new Intent(this, ImageCropActivity.class);
		intent.putExtra(ImageCropActivity.IMAGE_PATH, filePath);

		startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	}

	private boolean storeImage(Bitmap imageData) {
		//get path to external storage (SD card)

		String folder = getString(R.string.app_name);

		baseStoragePath = Environment.getExternalStorageDirectory() + File.separator + "Pictures" + File.separator + folder;

		//create storage directories, if they don't exist
		AKManager.dirChecker(baseStoragePath);

		try {
			String filename = "ak_crop_" + System.currentTimeMillis();
			filePath = baseStoragePath + File.separator + filename + ".jpg";
			FileOutputStream fileOutputStream = new FileOutputStream(filePath);

			BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

			//choose another format if PNG doesn't suit you
			imageData.compress(CompressFormat.JPEG, 100, bos);

			bos.flush();
			bos.close();

			ContentValues image = new ContentValues();
			image.put(Images.Media.TITLE, folder);
			image.put(Images.Media.DISPLAY_NAME, filename);
			image.put(Images.Media.DESCRIPTION, filePath);
			image.put(Images.Media.DATE_ADDED, System.currentTimeMillis());
			image.put(Images.Media.MIME_TYPE, "image/jpeg");
			image.put(Images.Media.ORIENTATION, 0);
			File fName = new File(filePath);
			File parent = fName.getParentFile();
			image.put(Images.ImageColumns.BUCKET_ID, parent.toString()
					.toLowerCase().hashCode());
			image.put(Images.ImageColumns.BUCKET_DISPLAY_NAME, parent.getName()
					.toLowerCase());
			image.put(Images.Media.SIZE, fName.length());
			image.put(Images.Media.DATA, fName.getAbsolutePath());
			resultUri = getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, image);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
			{
				Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				File f = new File("file://"+ Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
				Uri contentUri = Uri.fromFile(f);
				mediaScanIntent.setData(contentUri);
				this.sendBroadcast(mediaScanIntent);
			}
			else
			{
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
			}

		} catch (FileNotFoundException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());
			return false;
		} catch (IOException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());
			return false;
		} catch (Exception e) {
			Log.w("TAG", "Error creating image file: " + e.getMessage());
			return false;
		}

		return true;
	}

	private void shareCroppedImage(String imagePath){

		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("image/jpeg");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
		sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, resultUri);
		startActivity(Intent.createChooser(sharingIntent, getString(R.string.share)));

	}

	private void clearAllFiles(){
		try{
			File dir = new File(baseStoragePath);
			for(File fd : dir.listFiles()){
				if(fd.isFile())
					fd.delete();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void hideTopBottom(){
		if (top_layout.getVisibility() == View.VISIBLE && bottom_layout.getVisibility() == View.VISIBLE )
		{
			top_layout.startAnimation(fadeout);
			bottom_layout.startAnimation(fadeout);
		}
	}

	private void showTopBottom(){
		top_layout.startAnimation(fadein);
		bottom_layout.startAnimation(fadein);
	}

	private void toggleTopBottom(){
		if (top_layout.getVisibility() == View.VISIBLE && bottom_layout.getVisibility() == View.VISIBLE )
			hideTopBottom();
		else
			showTopBottom();
	}
	
	private void toggleViewEnable(boolean enabled){
		back.setEnabled(enabled);
		add_bookmark.setEnabled(enabled);
		bookmark_list.setEnabled(enabled);
		crop.setEnabled(enabled);
		list_summary.setEnabled(enabled);
		
		bar.setEnabled(enabled);
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();

		if(baseStoragePath != null)
			clearAllFiles();
	}

	private int[] getPageNumbers(int totalPages){
		int[] pages = new int[totalPages];
		int counter = 0;
		for (int i = totalPages-1; i >=0; i--) {
			pages[counter++] = i;
		}
		
		return pages;
	}
	
}
