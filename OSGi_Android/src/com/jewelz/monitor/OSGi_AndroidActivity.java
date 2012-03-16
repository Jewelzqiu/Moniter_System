package com.jewelz.monitor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class OSGi_AndroidActivity extends PreferenceActivity {
	
	Button Refresh_Button, Settings_Button;
	PreferenceCategory ImagesCatory;
	
	ArrayList<String> ImageList;
	SettingsDialog dialog;
	
	Handler handler = new Handler();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.mainlist);
        setContentView(R.layout.main);
        
    	dialog = new SettingsDialog(this);
        boolean InitOK = this.getIntent().getBooleanExtra("init_ok", false);
        if (!InitOK) {
	        dialog.setTitle("Please set up the information of Server!");
	        dialog.show();
        }
        
        ImagesCatory = (PreferenceCategory) findPreference("images");
        ImageList = this.getIntent().getStringArrayListExtra("ImageList");
        
        for (String name : ImageList) {
        	Preference preference = new Preference(this);
        	preference.setTitle(name);
        	preference.setOnPreferenceClickListener(new OnImageNameClickListener());
        	ImagesCatory.addPreference(preference);
        }
        
        Refresh_Button = (Button) findViewById(R.id.refresh_btn);
        Refresh_Button.setOnClickListener(new OnRefreshListener());
        Settings_Button = (Button) findViewById(R.id.settings_btn);
        Settings_Button.setOnClickListener(new OnSettingsListener());
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
    
    class OnRefreshListener implements OnClickListener {

		public void onClick(View v) {
			refresh();
		}
    	
    }
    
    class OnSettingsListener implements OnClickListener {
    	
    	public void onClick(View v) {
    		dialog.setTitle("Settings");
    		dialog.show();
    	}
    	
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	void refresh() {
		new Thread() {
			
			public void run() {
				try{
					FileInputStream in = getApplicationContext().openFileInput("settings");
					InputStreamReader Inreader = new InputStreamReader(in);
					BufferedReader reader = new BufferedReader(Inreader);
					String IPAddr = reader.readLine();
					int port = Integer.parseInt(reader.readLine());
					
					Socket socket = new Socket(IPAddr, port);
					BufferedReader SocketIn = new BufferedReader(new InputStreamReader(
							socket.getInputStream()));
					PrintWriter SocketOut = new PrintWriter(socket.getOutputStream());
					SocketOut.println("list");
					SocketOut.flush();
					ImageList.clear();
					String line = SocketIn.readLine();
					while (line != null) {
						ImageList.add(line);
						line = SocketIn.readLine();
					}
					SocketIn.close();
					SocketOut.close();
					socket.close();
					
					reader.close();
					Inreader.close();
					in.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				handler.post(new UpdateUI());
			}
			
		}.start();
		
	}
	
	class UpdateUI implements Runnable {

		public void run() {
			ImagesCatory.removeAll();
			for (String name : ImageList) {
				Preference preference = new Preference(OSGi_AndroidActivity.this);
	        	preference.setTitle(name);
	        	preference.setOnPreferenceClickListener(new OnImageNameClickListener());
	        	ImagesCatory.addPreference(preference);
			}
		}
		
	}
    
}