package com.pdf.moufsidetalkouloub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.pdf.moufsidetalkouloub.utils.MySuperScaler;

public class AboutActivity extends MySuperScaler{

	Button more_apps, share ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		more_apps = (Button) findViewById(R.id.more_apps);
		share = (Button) findViewById(R.id.share);

		more_apps.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://alnour.ws/zadapp/")));  
				
			}
		});
		
		share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String shareBody = " *** TO DEFINE ***";
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
				startActivity(Intent.createChooser(sharingIntent, getString(R.string.share)));
				
			}
		});

	}


	@Override
	public void onBackPressed() {
		startActivity(new Intent (AboutActivity.this,MainBookChoice.class));
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
		super.onBackPressed();
		
	}
}
