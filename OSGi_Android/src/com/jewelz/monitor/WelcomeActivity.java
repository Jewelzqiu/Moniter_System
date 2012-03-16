package com.jewelz.monitor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class WelcomeActivity extends Activity {

	ArrayList<String> ImageList = new ArrayList<String>();
	Intent intent = new Intent();
	boolean InitOK = false;
	Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);

		intent.setClass(this, OSGi_AndroidActivity.class);		

		new Thread() {
			
			public void run() {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				InitOK = init(intent);
				handler.post(new Start());
			}
			
		}.start();
		
	}
	
	boolean init(Intent intent) {
		try {
			FileInputStream in = this.openFileInput("settings");
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
			String line = SocketIn.readLine();
			while (line != null) {
				ImageList.add(line);
				line = SocketIn.readLine();
			}
			SocketIn.close();
			SocketOut.close();
			socket.close();
			
			//ImageList.add("1_20120308125409.jpg");
			reader.close();
			Inreader.close();
			in.close();
			return true;
		} catch (Exception e) {
			Log.d("debug", e.getMessage());
			return false;
		}
	}
	
	class Start implements Runnable {

		public void run() {
			intent.putExtra("init_ok", InitOK);			
			intent.putStringArrayListExtra("ImageList", ImageList);
			startActivity(intent);
		}
		
	}

}
