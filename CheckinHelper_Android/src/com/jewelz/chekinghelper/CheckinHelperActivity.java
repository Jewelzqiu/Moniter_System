package com.jewelz.chekinghelper;

import java.io.File;
import java.util.HashSet;

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
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class CheckinHelperActivity extends Activity {
	
	static final int CHECK = 0;
	static final int TRAIN = 1;
	static int count = 0;
	
	final static String path = Environment.getExternalStorageDirectory().getAbsolutePath() +
			"/temp/temp.jpg";;
	static String names = "";
	
	Button Check_Btn;
	Button Train_Btn;
	ImageView Captured_Img;	
	
	static Handler handler = new Handler();
	
	static AlertDialog WaitDialog;
	static AlertDialog WelcomeDialog;
	static AlertDialog SorryDialog;
	
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
        Captured_Img = (ImageView) findViewById(R.id.captured_img);
        setImage();
        
        Builder WaitBuilder = new Builder(CheckinHelperActivity.this);
        WaitDialog = WaitBuilder.setTitle(R.string.wait)
				.setMessage(R.string.procing)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();							
					}
				})
				.create();
        
        Builder WelcomeBuilder = new Builder(CheckinHelperActivity.this);
		WelcomeDialog = WelcomeBuilder.setTitle(R.string.welcome)
				.setMessage("")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();							
					}
				})
				.create();
		
		Builder SorryBuilder = new Builder(CheckinHelperActivity.this);
		SorryDialog = SorryBuilder.setTitle(R.string.sorry)
				.setMessage(R.string.cant_rec)
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
					
				})
				.setPositiveButton(R.string.train, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						new UidInputDialog(CheckinHelperActivity.this).show();
					}
					
				}).create();
		
		count = 0;
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.d("debug", requestCode + "");
		if (RESULT_OK == resultCode) {
			setImage();
			
			if (requestCode == CHECK) {
				
				new Thread() {
					
					public void run() {
						handler.post(new ShowWait());
						try {
							HashSet<String> list = FaceRec.recognize(path);
							names = "";
							for (String uid : list) {
								names += uid + " ";
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
				new UidInputDialog(CheckinHelperActivity.this).show();
			}
		}
	}
	
	class OnCheckListener implements OnClickListener {

		public void onClick(View v) {
			File image = new File(path);
			Uri imageUri = Uri.fromFile(image);
			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, CHECK);
		}
		
	}
	
	class OnTrainListener implements OnClickListener {

		public void onClick(View v) {
			File image = new File(path);
			Uri imageUri = Uri.fromFile(image);
			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, TRAIN);
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
		}
		
	}
	
	static class ShowSorry implements Runnable {

		public void run() {
			CheckinHelperActivity.count++;
			if (CheckinHelperActivity.count >= 3) {
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
		
	static void train(final String uid) {
		new Thread() {
			
			public void run() {
				boolean success = false;
				handler.post(new ShowWait());
				try {
					Log.d("debug", "train!");
					success = FaceRec.train(path, uid, "");
				} catch (FaceClientException e) {
					e.printStackTrace();
				} catch (FaceServerException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				
				if (success) {
					names = uid;
					// TODO get name
					handler.post(new ShowWelcome());
				} else {
					names = "";
					handler.post(new ShowSorry());
				}
			}
			
		}.start();
		
	}
	
	void setImage() {
		bmp = null;
		Display currentDisplay = getWindowManager().getDefaultDisplay();
		int dw = currentDisplay.getWidth();
		int dh = currentDisplay.getHeight();

		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true; 
        bmp = BitmapFactory.decodeFile(path, bmpFactoryOptions);
		
		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight/(float)dh);  
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth/(float)dw);
        
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
        bmp = BitmapFactory.decodeFile(path, bmpFactoryOptions);
        if (bmp != null) {
        	Captured_Img.setImageBitmap(bmp);
        }
	}
}