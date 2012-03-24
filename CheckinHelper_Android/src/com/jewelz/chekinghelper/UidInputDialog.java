package com.jewelz.chekinghelper;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UidInputDialog extends Dialog {

	int which;
	
	EditText UidText;
	EditText NameText;
	EditText CodeText;
	TextView NameView;
	Button OK_Btn;
	
	public UidInputDialog(Context context, int which) {
		super(context);
		this.which = which;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.uid_input);
		setTitle(R.string.uit_input);

		UidText = (EditText) findViewById(R.id.uid);
		NameText = (EditText) findViewById(R.id.name);
		CodeText = (EditText) findViewById(R.id.code);
		NameView = (TextView) findViewById(R.id.name_text);
		
		if (which == CheckinHelperActivity.TRAIN || which == CheckinHelperActivity.REMOVE) {
			NameText.setEnabled(false);
			NameView.setEnabled(false);
		}
		
		OK_Btn = (Button) findViewById(R.id.uid_ok);
		OK_Btn.setOnClickListener(new android.view.View.OnClickListener() {
			
			public void onClick(View v) {
				String uid = UidText.getText().toString();
				String code = "" + CodeText.getText().hashCode();
				Log.d("debug", code);
				// TODO
				switch (which) {
				case CheckinHelperActivity.TRAIN:
					CheckinHelperActivity.train(uid);
					break;
				case CheckinHelperActivity.ADD:
					
					break;
				case CheckinHelperActivity.REMOVE:
					
					break;
				}				
				dismiss();
			}
		});
	}
	
	

}
