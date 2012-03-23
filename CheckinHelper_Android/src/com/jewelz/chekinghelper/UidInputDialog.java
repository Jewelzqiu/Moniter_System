package com.jewelz.chekinghelper;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UidInputDialog extends Dialog {

	EditText UidText;
	Button OK_Btn;
	
	public UidInputDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.uid_input);
		setTitle(R.string.uit_input);
		
		UidText = (EditText) findViewById(R.id.uid);
		OK_Btn = (Button) findViewById(R.id.uid_ok);
		OK_Btn.setOnClickListener(new android.view.View.OnClickListener() {
			
			public void onClick(View v) {
				String uid = UidText.getText().toString();
				CheckinHelperActivity.train(uid);
				dismiss();
			}
		});
	}
	
	

}
