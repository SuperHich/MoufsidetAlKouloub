package com.pdf.moufsidetalkouloub;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.edmodo.cropper.CropImageView;
import com.pdf.moufsidetalkouloub.externals.AKManager;
import com.pdf.moufsidetalkouloub.utils.CropUtil;
import com.pdf.moufsidetalkouloub.utils.MonitoredActivity;

/**
 * Moufsideet Al Kouloub
 * @author HICHEM LAROUSSI - RAMI TRABELSI
 * Copyright (c) 2014 Zad Group. All rights reserved.
 */

public class ImageCropActivity extends MonitoredActivity{
	
	public static final  String IMAGE_PATH 				= "image-path";
	public static final  String IMAGE_BITMAP 			= "image-bitmap";
	
	public static final  String RETURN_DATA            	= "return-data";
    public static final  String RETURN_DATA_AS_BITMAP  	= "data";
    public static final  String ACTION_INLINE_DATA     	= "inline-data";
    
	// Static final constants
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
    private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
    private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
	private static final String TAG = null;

    // Instance variables
    private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
    private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;

    Bitmap croppedImage;
    
    private CropImageView cropImageView;
    private Button btn_cancel, btn_save;
	private String mImagePath;
	private ContentResolver mContentResolver;

	private       Bitmap.CompressFormat mOutputFormat    = Bitmap.CompressFormat.JPEG;
    private       Uri                   mSaveUri         = null;
    private final Handler               mHandler         = new Handler();
	
    // Saves the state upon rotating the screen/restarting the activity
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
        bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
    }

    // Restores the state upon rotating the screen/restarting the activity
    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
        mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.image_crop_layout);
    	
    	mContentResolver = getContentResolver();
    	
    	cropImageView = (CropImageView) findViewById(R.id.CropImageView);
    	btn_cancel = (Button) findViewById(R.id.discard);
    	btn_save = (Button) findViewById(R.id.save);
    	
    	btn_cancel.bringToFront();
    	btn_save.bringToFront();
    	
    	Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
        	mImagePath = extras.getString(IMAGE_PATH);
        	mSaveUri = getImageUri(mImagePath);
        	
        	croppedImage = AKManager.originalResolutionFromSDCard(mImagePath, cropImageView.getWidth(), cropImageView.getHeight());
        	cropImageView.setImageBitmap(croppedImage);
        }
    	
    	btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setResult(RESULT_CANCELED);
                finish();
			}
		});
    	
    	btn_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				croppedImage = cropImageView.getCroppedImage();
				final Bitmap b = croppedImage;
	            CropUtil.startBackgroundJob(ImageCropActivity.this, null, getString(R.string.saving_image),
	                    new Runnable() {
	                        public void run() {

	                            saveOutput(b);
	                        }
	                    }, mHandler);
				
			}
		});
    }
    
    private Uri getImageUri(String path) {

        return Uri.fromFile(new File(path));
    }
    
    private void saveOutput(Bitmap croppedImage) {

        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = mContentResolver.openOutputStream(mSaveUri);
                if (outputStream != null) {
                    croppedImage.compress(mOutputFormat, 90, outputStream);
                }
            } catch (IOException ex) {

                Log.e(TAG, "Cannot open file: " + mSaveUri, ex);
                setResult(RESULT_CANCELED);
                finish();
                return;
            } finally {

                CropUtil.closeSilently(outputStream);
            }

            Bundle extras = new Bundle();
            Intent intent = new Intent(mSaveUri.toString());
            intent.putExtras(extras);
            intent.putExtra(IMAGE_PATH, mImagePath);
            setResult(RESULT_OK, intent);
        } else {

            Log.e(TAG, "not defined image url");
        }
        croppedImage.recycle();
        finish();
    }
}
