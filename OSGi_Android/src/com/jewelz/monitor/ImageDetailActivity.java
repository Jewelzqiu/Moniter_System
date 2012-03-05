package com.jewelz.monitor;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.widget.ImageView;

public class ImageDetailActivity extends PreferenceActivity {

	String ImageName;
	String Number;
	String Time;
	
	Preference CameraNo, ImageTime;
	PreferenceCategory ImageCategory;
	ImageView Imageview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.detail);
		
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
		ImageCategory = (PreferenceCategory) findPreference("image");
	}

}
