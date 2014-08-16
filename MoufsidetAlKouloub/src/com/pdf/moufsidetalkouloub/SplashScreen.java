package com.pdf.moufsidetalkouloub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.pdf.moufsidetalkouloub.utils.MySuperScaler;



@SuppressLint("HandlerLeak")
public class SplashScreen extends MySuperScaler {
	
	private static final int STOPSPLASH = 0;
	private static final long SPLASHTIME = 2000;
	private Handler splashHandler = new Handler() {
		
		public void handleMessage(Message msg) {

			Intent intent = new Intent(SplashScreen.this, MainBookChoice.class);
			startActivity(intent);
			overridePendingTransition(R.anim.up_in, R.anim.up_out);
			finish();

			super.handleMessage(msg);
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		Message msg = new Message();
		msg.what = STOPSPLASH;

		splashHandler.sendMessageDelayed(msg, SPLASHTIME);

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			splashHandler.removeMessages(STOPSPLASH);
		}
		return super.onKeyDown(keyCode, event);
	}


}