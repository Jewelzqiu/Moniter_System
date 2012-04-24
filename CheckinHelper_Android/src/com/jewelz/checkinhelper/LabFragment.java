package com.jewelz.checkinhelper;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class LabFragment extends Fragment {

	ImageView LabImage;
	String img_path = MainActivity.folder + "/lab.jpg";
	Handler handler = new Handler();
	GetImage thread = new GetImage();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add(R.string.on).setOnMenuItemClickListener(new OnOnListener())
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(R.string.off).setOnMenuItemClickListener(new OnOffListener())
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.lab, container, false);
		LabImage = (ImageView) v.findViewById(R.id.lab_img);
		if (!thread.isAlive()) {
			thread.start();			
		}
		handler.post(new SetImage());
		return v;
	}

//	@Override
//	public void onPause() {
//		if (thread.isAlive()) {
//			thread.interrupt();
//		}
//		super.onPause();
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		if (!thread.isAlive()) {
//			thread.start();
//		}
//	}

	class OnOnListener implements OnMenuItemClickListener {

		public boolean onMenuItemClick(MenuItem item) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	class OnOffListener implements OnMenuItemClickListener {

		public boolean onMenuItemClick(MenuItem item) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	class GetImage extends Thread {

		public void run() {
			while (true) {
				try {
					Socket socket = new Socket(MainActivity.SERVER_IP,
							MainActivity.SERVER_PORT);
					PrintWriter writer = new PrintWriter(
							new OutputStreamWriter(new BufferedOutputStream(
									socket.getOutputStream()), "UTF-8"));
					writer.println("labimage");
					writer.flush();

					DataInputStream ins = new DataInputStream(
							socket.getInputStream());
					DataOutputStream out = new DataOutputStream(
							new BufferedOutputStream(new FileOutputStream(
									img_path)));
					
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
					socket.close();
					out.close();

				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				handler.post(new SetImage());

				try {
					sleep(1000 * 60);
				} catch (InterruptedException e) {
					e.printStackTrace();
					this.interrupt();
				}
			}
		}

	}

	class SetImage implements Runnable {

		public void run() {
			Bitmap bmp = BitmapFactory.decodeFile(img_path);
			if (bmp != null) {
				LabImage.setImageBitmap(bmp);
			}
		}

	}

}