package com.jewelz.checkinhelper;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

import com.github.mhendred.face4j.examples.MyExample;
import com.github.mhendred.face4j.exception.FaceClientException;
import com.github.mhendred.face4j.exception.FaceServerException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CheckInFragment extends Fragment {

	static final int TRAIN = 0;
	static final int CHECK = 1;
	static final int ADD = 1;
	static final int REMOVE = 2;

	static int count = 0;

	static String CODE = null;
	static String names = "";
	static String uids = "";

	static HashMap<String, String> namelist = new HashMap<String, String>();

	static ImageView Captured_Img;

	static Handler handler = new Handler();

	static AlertDialog WaitDialog;
	static AlertDialog WelcomeDialog;
	static AlertDialog SorryDialog;
	static AlertDialog CodeErrorDialog;

	static Bitmap bmp;

	static MyExample FaceRec = new MyExample();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setHasOptionsMenu(true);
		
		updateNamelist();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add(R.string.check_in).setIcon(R.drawable.ic_menu_mark)
				.setOnMenuItemClickListener(new OnCheckListener())
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		menu.add(R.string.train).setIcon(android.R.drawable.ic_menu_upload)
				.setOnMenuItemClickListener(new OnTrainListener())
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		menu.add(R.string.add).setIcon(R.drawable.ic_menu_invite)
				.setOnMenuItemClickListener(new OnAddListener())
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		menu.add(R.string.remove).setIcon(R.drawable.ic_menu_blocked_user)
				.setOnMenuItemClickListener(new OnRemoveListener())
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.checkin, container, false);
		Captured_Img = (ImageView) v.findViewById(R.id.captured_img);
		setImage(true);

		Builder WaitBuilder = new Builder(getActivity());
		WaitDialog = WaitBuilder.setTitle(R.string.wait)
				.setMessage(R.string.procing)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create();

		Builder WelcomeBuilder = new Builder(getActivity());
		WelcomeDialog = WelcomeBuilder.setTitle(R.string.welcome)
				.setMessage("")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create();

		Builder SorryBuilder = new Builder(getActivity());
		SorryDialog = SorryBuilder
				.setTitle(R.string.sorry)
				.setMessage(R.string.cant_rec)
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}

						})
				.setPositiveButton(R.string.train,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								new UidInputDialog(getActivity(), TRAIN).show();
							}

						}).create();

		Builder CodeErrorBuilder = new Builder(getActivity());
		CodeErrorDialog = CodeErrorBuilder.setTitle(R.string.sorry)
				.setMessage(R.string.code_err)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create();

		count = 0;

		return v;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (Activity.RESULT_OK == resultCode) {
			setImage(false);

			if (requestCode == CHECK) {

				new Thread() {

					public void run() {
						handler.post(new ShowWait());
						try {
							HashSet<String> list = FaceRec
									.recognize(MainActivity.path);
							names = "";
							uids = "";
							for (String uid : list) {
								if (namelist.containsKey(uid)) {
									names += namelist.get(uid) + " ";
									uids += uid + " ";
								}
							}
							if (names.equals("")) {
								handler.post(new ShowSorry());
							} else {
								handler.post(new ShowWelcome());
							}
						} catch (FaceClientException e) {
							e.printStackTrace();
						} catch (FaceServerException e) {
							e.printStackTrace();
						}
					}

				}.start();

			} else if (requestCode == TRAIN) {
				new UidInputDialog(getActivity(), TRAIN).show();
			}
		}
	}

	class OnCheckListener implements OnMenuItemClickListener {

		public boolean onMenuItemClick(MenuItem item) {
			System.out.println(item.getTitle());
			File image = new File(MainActivity.path_ori);
			Uri imageUri = Uri.fromFile(image);
			Intent intent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, CHECK);
			return false;
		}

	}

	class OnTrainListener implements OnMenuItemClickListener {

		public boolean onMenuItemClick(MenuItem item) {
			System.out.println(item.getTitle());
			File image = new File(MainActivity.path_ori);
			Uri imageUri = Uri.fromFile(image);
			Intent intent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, TRAIN);
			return false;
		}

	}

	class OnAddListener implements OnMenuItemClickListener {

		public boolean onMenuItemClick(MenuItem item) {
			System.out.println(item.getTitle());
			new UidInputDialog(getActivity(), ADD);
			return false;
		}

	}

	class OnRemoveListener implements OnMenuItemClickListener {

		public boolean onMenuItemClick(MenuItem item) {
			System.out.println(item.getTitle());
			new UidInputDialog(getActivity(), REMOVE);
			return false;
		}

	}

	static class ShowWait implements Runnable {

		public void run() {
			dismissAll();
			WaitDialog.show();
		}

	}

	static class ShowWelcome implements Runnable {

		public void run() {
			CheckInFragment.count = 0;
			dismissAll();
			WelcomeDialog.setMessage(names);
			WelcomeDialog.show();

			new Thread() {

				public void run() {
					try {
						Socket socket = new Socket(MainActivity.SERVER_IP,
								MainActivity.SERVER_PORT);
						PrintWriter writer = new PrintWriter(
								new OutputStreamWriter(
										new BufferedOutputStream(
												socket.getOutputStream()),
										"UTF-8"));

						writer.println("check " + System.currentTimeMillis()
								+ " " + uids);
						writer.flush();
						writer.close();
						socket.close();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}.start();
		}

	}

	static class ShowSorry implements Runnable {

		public void run() {
			names = "";
			uids = "";
			CheckInFragment.count++;
			if (CheckInFragment.count >= 3) {
				// TODO
			}
			dismissAll();
			SorryDialog.show();
		}

	}

	static void dismissAll() {
		WaitDialog.dismiss();
		WelcomeDialog.dismiss();
		SorryDialog.dismiss();
	}

	static void updateNamelist() {

		new Thread() {

			public void run() {
				try {
					Socket socket = new Socket(MainActivity.SERVER_IP,
							MainActivity.SERVER_PORT);
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(socket.getInputStream(),
									"UTF-8"));
					PrintWriter writer = new PrintWriter(
							new OutputStreamWriter(new BufferedOutputStream(
									socket.getOutputStream()), "UTF-8"));

					writer.println("request namelist");
					writer.flush();
					CODE = reader.readLine();
					String line = reader.readLine();
					namelist.clear();
					while (line != null) {
						StringTokenizer tokenizer = new StringTokenizer(line);
						String uid = tokenizer.nextToken();
						String name = tokenizer.nextToken();
						namelist.put(uid, name);
						line = reader.readLine();
					}

					reader.close();
					writer.close();
					socket.close();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	static void train(final String uid) {

		new Thread() {

			public void run() {
				if (!namelist.containsKey(uid)) {
					handler.post(new ShowSorry());
					return;
				}
				boolean success = false;
				handler.post(new ShowWait());
				try {
					success = FaceRec.train(MainActivity.path, uid, "");
				} catch (FaceClientException e) {
					e.printStackTrace();
				} catch (FaceServerException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}

				if (success) {
					if (namelist.containsKey(uid)) {
						names = namelist.get(uid);
						uids = uid;
						handler.post(new ShowWelcome());
					}
				} else {
					handler.post(new ShowSorry());
				}
				
			}

		}.start();

	}

	static void addMember(final String uid, final String name) {
		new Thread() {

			public void run() {
				try {
					Socket socket = new Socket(MainActivity.SERVER_IP,
							MainActivity.SERVER_PORT);
					PrintWriter writer = new PrintWriter(
							new OutputStreamWriter(new BufferedOutputStream(
									socket.getOutputStream()), "UTF-8"));

					writer.println("add " + uid + " " + name);
					writer.flush();
					writer.close();
					socket.close();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}.start();

		updateNamelist();
	}

	static void removeMember(final String uid) {
		new Thread() {

			public void run() {
				try {
					Socket socket = new Socket(MainActivity.SERVER_IP,
							MainActivity.SERVER_PORT);
					PrintWriter writer = new PrintWriter(
							new OutputStreamWriter(new BufferedOutputStream(
									socket.getOutputStream()), "UTF-8"));

					writer.println("remove " + uid);
					writer.flush();
					writer.close();
					socket.close();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}.start();

		updateNamelist();
	}

	@SuppressWarnings("deprecation")
	void setImage(boolean first) {
		bmp = null;
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();

		if (!first) {
			Display currentDisplay = getActivity().getWindowManager()
					.getDefaultDisplay();
			int dw = currentDisplay.getWidth();
			int dh = currentDisplay.getHeight();

			bmpFactoryOptions.inJustDecodeBounds = true;
			bmp = BitmapFactory.decodeFile(MainActivity.path_ori,
					bmpFactoryOptions);

			int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
					/ (float) dh);
			int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
					/ (float) dw);

			if ((heightRatio > 1) && (widthRatio > 1)) {
				if (heightRatio > widthRatio) {
					// Height ratio is larger, scale according to it
					bmpFactoryOptions.inSampleSize = heightRatio;
				} else {
					// Width ratio is larger, scale according to it
					bmpFactoryOptions.inSampleSize = widthRatio;
				}
			}

			bmpFactoryOptions.inJustDecodeBounds = false;
			bmp = BitmapFactory.decodeFile(MainActivity.path_ori,
					bmpFactoryOptions);

		} else {
			bmpFactoryOptions.inJustDecodeBounds = false;
			bmp = BitmapFactory
					.decodeFile(MainActivity.path, bmpFactoryOptions);
		}

		if (bmp != null) {
			Captured_Img.setImageBitmap(bmp);
		}

		if (!first) {
			try {
				FileOutputStream fout = new FileOutputStream(MainActivity.path);
				bmp.compress(Bitmap.CompressFormat.JPEG, 100, fout);
				fout.flush();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
