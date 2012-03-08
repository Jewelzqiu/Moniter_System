package com.jewelz.monitor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ImageDetailActivity extends PreferenceActivity {

	String sd_path = "/mnt/sdcard/Moniter_images/";
	String ImageName;
	String Number = "undefined";
	String Time = "undefined";
	
	Preference CameraNo, ImageTime;
	ImageView Imageview;
	ProgressBar progressbar;
	
	Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.detail);
		setContentView(R.layout.image);
		
		ImageName = this.getIntent().getStringExtra("name");
		int index = ImageName.indexOf('_');
		Number = ImageName.substring(0, index);
		String time = ImageName.substring(index + 1);
		Time = time.substring(0, 4) + "." + 
				time.substring(4, 6) + "." +
				time.substring(6, 8) + " " +
				time.substring(8, 10) + ":" +
				time.substring(10, 12) + ":" +
				time.substring(12, 14);
		
		CameraNo = findPreference("number");
		CameraNo.setTitle(Number);
		ImageTime = findPreference("time");
		ImageTime.setTitle(Time);
		
		Imageview = (ImageView) findViewById(R.id.imageview);
		Imageview.setVisibility(ImageView.INVISIBLE);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		setProgressBarIndeterminateVisibility(true);
		
		new Thread() {
			
			public void run() {
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.post(new getImage());
			}
			
		}.start();
	}
	
	class getImage implements Runnable {

		public void run() {
			try {
				Imageview.setImageBitmap(BitmapFactory.decodeStream(
						new FileInputStream(sd_path + ImageName)));
				progressbar.setVisibility(ProgressBar.INVISIBLE);
				Imageview.setVisibility(ImageView.VISIBLE);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Imageview.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					Uri uri = Uri.parse("file://" + sd_path + ImageName);
					Intent intent = new Intent();
					intent.setAction(android.content.Intent.ACTION_VIEW);				
					intent.setDataAndType(uri, "image/*");
					startActivity(intent);
				}
			});
		}
		
	}	

}
