package com.jewelz.chekinghelper;

import java.security.MessageDigest;

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

		if (which == CheckinHelperActivity.TRAIN
				|| which == CheckinHelperActivity.REMOVE) {
			NameText.setEnabled(false);
			NameView.setEnabled(false);
		}

		OK_Btn = (Button) findViewById(R.id.uid_ok);
		OK_Btn.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View v) {
				String uid = UidText.getText().toString();
				String code = MD5(CodeText.getText().toString());
				Log.d("debug", "code:  " + CheckinHelperActivity.CODE);
				Log.d("debug", "input: " + code);

				if (!code.equals(CheckinHelperActivity.CODE)) {
					CheckinHelperActivity.CodeErrorDialog.show();
					return;
				}

				switch (which) {
				case CheckinHelperActivity.TRAIN:
					CheckinHelperActivity.train(uid);
					break;
				case CheckinHelperActivity.ADD:
					String name = NameText.getText().toString();
					CheckinHelperActivity.addMember(uid, name);
					break;
				case CheckinHelperActivity.REMOVE:
					CheckinHelperActivity.removeMember(uid);
					break;
				}
				dismiss();
			}
		});
	}

	public static String MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];

		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}

}
