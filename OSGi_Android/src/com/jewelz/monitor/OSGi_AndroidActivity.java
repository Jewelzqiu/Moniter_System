package com.jewelz.monitor;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.util.Log;
import android.view.KeyEvent;

public class OSGi_AndroidActivity extends PreferenceActivity {
	
	Preference RefreshPreference;
	PreferenceCategory ImagesCatory;
	
	ArrayList<String> ImageList;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.mainlist);
        RefreshPreference = findPreference("refresh");
        ImagesCatory = (PreferenceCategory) findPreference("images");
        ImageList = this.getIntent().getStringArrayListExtra("ImageList");
        
        for (String name : ImageList) {
        	Preference preference = new Preference(this);
        	preference.setTitle(name);
        	preference.setOnPreferenceClickListener(new OnImageNameClickListener());
        	ImagesCatory.addPreference(preference);
        }
    }
    
    class OnImageNameClickListener implements OnPreferenceClickListener {

		public boolean onPreferenceClick(Preference preference) {
			String name = preference.getTitle().toString();
			// TODO if name is legal
			Intent intent = new Intent();
			intent.setClass(OSGi_AndroidActivity.this, ImageDetailActivity.class);
			intent.putExtra("name", name);
			startActivity(intent);
			return false;
		}
    	
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.d("debug", "back");
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
    
}