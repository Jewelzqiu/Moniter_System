package com.jewelz.monitor;

import java.util.ArrayList;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.util.Log;

public class OSGi_AndroidActivity extends PreferenceActivity {
	
	Preference RefreshPreference;
	PreferenceCategory ImagesCatory;
	
	ArrayList<String> ImageList;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.mainlist);
        this.getListView().setBackgroundResource(R.drawable.blue);
        RefreshPreference = findPreference("refresh");
        ImagesCatory = (PreferenceCategory) findPreference("images");
        ImageList = this.getIntent().getStringArrayListExtra("ImageList");
        
        for (String name : ImageList) {
        	Preference preference = new Preference(this);
        	preference.setTitle(name);
        	ImagesCatory.addPreference(preference);
        }
    }
}