package com.jewelz.monitor;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
		CameraNo.setSummary(Number);
		ImageTime = findPreference("time");
		ImageTime.setSummary(Time);
		
		Imageview = (ImageView) findViewById(R.id.imageview);
		Imageview.setVisibility(ImageView.INVISIBLE);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		setProgressBarIndeterminateVisibility(true);
		
		new Thread() {
			
			public void run() {
				try {
					FileInputStream in = ImageDetailActivity.this.openFileInput("settings");
					InputStreamReader Inreader = new InputStreamReader(in);
					BufferedReader reader = new BufferedReader(Inreader);
					String IPAddr = reader.readLine();
					int port = Integer.parseInt(reader.readLine());

					Socket socket = new Socket(IPAddr, port);
					DataInputStream ins = new DataInputStream(socket.getInputStream());
					PrintWriter writer = new PrintWriter(socket.getOutputStream());
					writer.println(ImageName);
					writer.flush();
					
					DataOutputStream out =
							new DataOutputStream(
									new BufferedOutputStream(
											new FileOutputStream(sd_path + "/" + ImageName)));
					byte[] buf = new byte[8192];
					while (true) {
						int read = 0;
						if (ins != null) {
							read = ins.read(buf);
						}
						if (read == -1) {
							break;
						}
						out.write(buf, 0, read);
					}
					ins.close();
					writer.close();
					out.close();
					socket.close();
					
					reader.close();
					Inreader.close();
					in.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				handler.post(new setImage());
			}
			
		}.start();
	}
	
	class setImage implements Runnable {

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
