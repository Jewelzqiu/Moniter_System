package com.jewelz.monitor;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class WelcomeActivity extends Activity {

	ArrayList<String> ImageList = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);

		init();
		
		Intent intent = new Intent();
		intent.setClass(this, OSGi_AndroidActivity.class);
		intent.putStringArrayListExtra("ImageList", ImageList);
		startActivity(intent);
	}
	
	void init() {
		ImageList.add("test1");
		// TODO 
	}

}
