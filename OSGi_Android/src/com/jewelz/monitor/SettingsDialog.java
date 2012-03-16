package com.jewelz.monitor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsDialog extends Dialog {

	EditText ip_text, port_text;
	Button ok_btn, cancel_btn;
	
	public SettingsDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.settings);
		setTitle(R.string.settings);
		
		String IPAddr = "";
		int port = -1;
		try {
			FileInputStream in = getContext().openFileInput("settings");
			InputStreamReader Inreader = new InputStreamReader(in);
			BufferedReader reader = new BufferedReader(Inreader);
			IPAddr = reader.readLine();
			port = Integer.parseInt(reader.readLine());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ip_text = (EditText) findViewById(R.id.ip_text);
		ip_text.setText(IPAddr);
		port_text = (EditText) findViewById(R.id.port_text);
		if (port != -1) {
			port_text.setText("" + port);
		}
		ok_btn = (Button) findViewById(R.id.ok_btn);
		ok_btn.setOnClickListener(new OnOKListener());
		cancel_btn = (Button) findViewById(R.id.cancel_btn);
		cancel_btn.setOnClickListener(new OnCancelListener());
	}
	
	class OnOKListener implements android.view.View.OnClickListener {

		public void onClick(View v) {
			OutputStreamWriter outwriter = null;
			try {
				outwriter = new OutputStreamWriter(
						getContext().openFileOutput("settings",
								android.content.Context.MODE_PRIVATE));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			PrintWriter writer = new PrintWriter(new BufferedWriter(outwriter));
			writer.println(ip_text.getText());
			writer.println(port_text.getText());
			writer.close();
			SettingsDialog.this.dismiss();
		}
		
	}
	
	class OnCancelListener implements android.view.View.OnClickListener {

		public void onClick(View v) {
			cancel();
		}
		
	}

}
