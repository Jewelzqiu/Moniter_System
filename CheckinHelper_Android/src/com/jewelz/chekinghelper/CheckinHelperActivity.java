package com.jewelz.chekinghelper;

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
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class CheckinHelperActivity extends Activity {

	static final int TRAIN = 0;
	static final int CHECK = 1;
	static final int ADD = 1;
	static final int REMOVE = 2;

	static int count = 0;

	static final String path = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/temp/temp.jpg";
	static final String SERVER_IP = "192.168.0.215";
	static final int SERVER_PORT = 33333;
	static String CODE = null;
	static String names = "";
	static String uids = "";

	static HashMap<String, String> namelist = new HashMap<String, String>();

	Button Check_Btn;
	Button Train_Btn;
	Button Add_Btn;
	Button Remove_Btn;
	ImageView Captured_Img;

	static Handler handler = new Handler();

	static AlertDialog WaitDialog;
	static AlertDialog WelcomeDialog;
	static AlertDialog SorryDialog;
	static AlertDialog CodeErrorDialog;

	Bitmap bmp;

	static MyExample FaceRec = new MyExample();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Check_Btn = (Button) findViewById(R.id.check_btn);
		Check_Btn.setOnClickListener(new OnCheckListener());
		Train_Btn = (Button) findViewById(R.id.train_btn);
		Train_Btn.setOnClickListener(new OnTrainListener());
		Add_Btn = (Button) findViewById(R.id.add_btn);
		Add_Btn.setOnClickListener(new OnAddListener());
		Remove_Btn = (Button) findViewById(R.id.remove_btn);
		Remove_Btn.setOnClickListener(new OnRemoveListener());

		Captured_Img = (ImageView) findViewById(R.id.captured_img);
		setImage(true);

		Builder WaitBuilder = new Builder(CheckinHelperActivity.this);
		WaitDialog = WaitBuilder.setTitle(R.string.wait)
				.setMessage(R.string.procing)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create();

		Builder WelcomeBuilder = new Builder(CheckinHelperActivity.this);
		WelcomeDialog = WelcomeBuilder.setTitle(R.string.welcome)
				.setMessage("")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create();

		Builder SorryBuilder = new Builder(CheckinHelperActivity.this);
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
								new UidInputDialog(CheckinHelperActivity.this,
										TRAIN).show();
							}

						}).create();

		Builder CodeErrorBuilder = new Builder(CheckinHelperActivity.this);
		CodeErrorDialog = CodeErrorBuilder.setTitle(R.string.sorry)
				.setMessage(R.string.code_err)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create();

		count = 0;

		updateNamelist();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (RESULT_OK == resultCode) {
			setImage(false);

			if (requestCode == CHECK) {

				new Thread() {

					public void run() {
						handler.post(new ShowWait());
						try {
							HashSet<String> list = FaceRec.recognize(path);
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
				new UidInputDialog(CheckinHelperActivity.this, TRAIN).show();
			}
		}
	}

	class OnCheckListener implements OnClickListener {

		public void onClick(View v) {
			File image = new File(path);
			Uri imageUri = Uri.fromFile(image);
			Intent intent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, CHECK);
		}

	}

	class OnTrainListener implements OnClickListener {

		public void onClick(View v) {
			File image = new File(path);
			Uri imageUri = Uri.fromFile(image);
			Intent intent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, TRAIN);
		}

	}

	class OnAddListener implements OnClickListener {

		public void onClick(View v) {
			new UidInputDialog(CheckinHelperActivity.this, ADD);
		}

	}

	class OnRemoveListener implements OnClickListener {

		public void onClick(View v) {
			new UidInputDialog(CheckinHelperActivity.this, REMOVE);
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
			CheckinHelperActivity.count = 0;
			dismissAll();
			WelcomeDialog.setMessage(names);
			WelcomeDialog.show();

			new Thread() {

				public void run() {
					try {
						Socket socket = new Socket(SERVER_IP, SERVER_PORT);
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
			CheckinHelperActivity.count++;
			if (CheckinHelperActivity.count >= 3) {
				// TODO
			}
			dismissAll();
			SorryDialog.show();
		}

	}

	static void updateNamelist() {

		new Thread() {
			
			public void run() {
				try {
					Socket socket = new Socket(SERVER_IP, SERVER_PORT);
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(socket.getInputStream(), "UTF-8"));
					PrintWriter writer = new PrintWriter(new OutputStreamWriter(
							new BufferedOutputStream(socket.getOutputStream()),
							"UTF-8"));

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

	static void dismissAll() {
		WaitDialog.dismiss();
		WelcomeDialog.dismiss();
		SorryDialog.dismiss();
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
					success = FaceRec.train(path, uid, "");
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
				}

				handler.post(new ShowSorry());
			}

		}.start();

	}

	static void addMember(final String uid, final String name) {
		new Thread() {

			public void run() {
				try {
					Socket socket = new Socket(SERVER_IP, SERVER_PORT);
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
					Socket socket = new Socket(SERVER_IP, SERVER_PORT);
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

	void setImage(boolean first) {
		bmp = null;
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();

		if (!first) {
			Display currentDisplay = getWindowManager().getDefaultDisplay();
			int dw = currentDisplay.getWidth();
			int dh = currentDisplay.getHeight();

			bmpFactoryOptions.inJustDecodeBounds = true;
			bmp = BitmapFactory.decodeFile(path, bmpFactoryOptions);

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
		}

		bmpFactoryOptions.inJustDecodeBounds = false;
		bmp = BitmapFactory.decodeFile(path, bmpFactoryOptions);
		if (bmp != null) {
			Captured_Img.setImageBitmap(bmp);
		}

		if (!first) {
			try {
				FileOutputStream fout = new FileOutputStream(path);
				bmp.compress(Bitmap.CompressFormat.JPEG, 100, fout);
				fout.flush();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}