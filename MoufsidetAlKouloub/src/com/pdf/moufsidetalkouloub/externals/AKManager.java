package com.pdf.moufsidetalkouloub.externals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

/**
 * Moufsideet Al Kouloub
 * @author HICHEM LAROUSSI - RAMI TRABELSI
 * Copyright (c) 2014 Zad Group. All rights reserved.
 */

public class AKManager {

	static final String TAG = "AKManager";
	
	private static AKManager mInstance = null;
	private static SharedPreferences settings;
	private SharedPreferences.Editor editor;
	private Context mContext;
	
	private ArrayList<Book> books = new ArrayList<Book>();
	
	
	public AKManager(Context context) {
		
		mContext = context;
		settings = PreferenceManager.getDefaultSharedPreferences(context);
		editor = settings.edit();
		
		books.add(new Book(1, "covers/book1.png", "01", 61));
		books.add(new Book(2, "covers/book2.png", "02", 63));
		books.add(new Book(3, "covers/book3.png", "03", 62));
		books.add(new Book(4, "covers/book4.png", "04", 64));
		books.add(new Book(5, "covers/book5.png", "05", 54));
		books.add(new Book(6, "covers/book6.png", "06", 64));
		books.add(new Book(7, "covers/book7.png", "07", 64));
		books.add(new Book(8, "covers/book8.png", "08", 60));
		books.add(new Book(9, "covers/book9.png", "09", 64));
		books.add(new Book(10, "covers/book10.png", "10", 64));
		
	}

	public ArrayList<Book> getBooks() {
		return books;
	}

	public void setBooks(ArrayList<Book> books) {
		this.books = books;
	}

	public synchronized static AKManager getInstance(Context context) {
		if (mInstance == null)
			mInstance = new AKManager(context);

		return mInstance;
	}
	
	
	public static Bitmap originalResolutionFromSDCard(String path, int width, int height)   {
		Bitmap bm = null;
		BitmapFactory.Options bfOptions=new BitmapFactory.Options();
		bfOptions.inDither=false;                     //Disable Dithering mode
		bfOptions.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
		bfOptions.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
		bfOptions.inTempStorage=new byte[32 * 1024]; 

		// Calculate inSampleSize
		bfOptions.inSampleSize = calculateInSampleSize(bfOptions, width, height);

		// Decode bitmap with inSampleSize set
		bfOptions.inJustDecodeBounds = false;    

		File file=new File(path);
		FileInputStream fs=null;
		try {
			fs = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			//TODO do something intelligent
			e.printStackTrace();
		}

		try {
			if(fs!=null) bm=BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
		} catch (IOException e) {
			//TODO do something intelligent
			e.printStackTrace();
		} finally{ 
			if(fs!=null) {
				try {
					fs.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return bm;

	}
	
	public static Bitmap originalResolution(Context context, String path, int width, int height)   {
		Bitmap bm = null;
		BitmapFactory.Options bfOptions=new BitmapFactory.Options();
		bfOptions.inDither=false;                     //Disable Dithering mode
		bfOptions.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
		bfOptions.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
		bfOptions.inTempStorage=new byte[32 * 1024]; 

		// Calculate inSampleSize
		bfOptions.inSampleSize = calculateInSampleSize(bfOptions, width, height);

		// Decode bitmap with inSampleSize set
		bfOptions.inJustDecodeBounds = false;    

		InputStream in = null;
		try {
			in = context.getAssets().open(path);
			bm=BitmapFactory.decodeStream(in, null, bfOptions);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bm;

	}
	
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
	
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);

	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	    return resizedBitmap;
	}
	
	public static Bitmap decodeSampledBitmapFromDescriptor(
			Bitmap bmp, int reqWidth, int reqHeight) {

		//Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inPurgeable = true;
		o2.inInputShareable = true;
		o2.inSampleSize = calculateInSampleSize(bmp, reqWidth, reqHeight);
		//o2.inSampleSize = scale;
		o2.inJustDecodeBounds = false;

		int bytes = bmp.getHeight() * bmp.getWidth() * 4;

		ByteBuffer buffer = ByteBuffer.allocate(bytes);

		bmp.copyPixelsToBuffer(buffer);

		byte[] array = buffer.array();

		return BitmapFactory.decodeByteArray(array, 0, bytes, o2);
	}

	public static int calculateInSampleSize(
			Bitmap bmp, int reqWidth, int reqHeight) {

		// Raw height and width of image
		final int height = bmp.getHeight();
		final int width = bmp.getWidth();

		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float)height / (float)reqHeight);
			} else {
				inSampleSize = Math.round((float)width / (float)reqWidth);
			}
		}

		return inSampleSize;
	}
	public Drawable createCopy(Bitmap b){
		Config config = Config.ARGB_8888;
		Bitmap bm = b.copy(config, false);
		return new BitmapDrawable(mContext.getResources(), bm);
	}
	
	public static void dirChecker(String dir) {
		File f = new File(dir);
		if (!f.isDirectory()) {
			f.mkdirs();
		}
	}
}
