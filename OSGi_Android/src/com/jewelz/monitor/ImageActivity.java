package com.jewelz.monitor;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageActivity extends Activity {

	ImageView imageview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.imageview);
		imageview = (ImageView) findViewById(R.id.image);
		imageview.setImageResource(R.drawable.a20120308125409);
	}

}
