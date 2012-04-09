package com.jewelz.checkinhelper;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class HistoryFragment extends Fragment {

	static String[] dates = new String[7];
	TextView[] texts = new TextView[7];

	Handler handler = new Handler();
	TableLayout table;
	TableRow firstrow;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.history, container, false);
		texts[0] = (TextView) v.findViewById(R.id.monday);
		texts[1] = (TextView) v.findViewById(R.id.tuesday);
		texts[2] = (TextView) v.findViewById(R.id.wednesday);
		texts[3] = (TextView) v.findViewById(R.id.thursday);
		texts[4] = (TextView) v.findViewById(R.id.friday);
		texts[5] = (TextView) v.findViewById(R.id.saturday);
		texts[6] = (TextView) v.findViewById(R.id.sunday);
		setDates(System.currentTimeMillis());

		table = (TableLayout) v.findViewById(R.id.table);
		firstrow = (TableRow) v.findViewById(R.id.first_row);
		
		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add(R.string.refresh).setIcon(R.drawable.ic_menu_refresh)
				.setOnMenuItemClickListener(new OnRefreshListener())
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(R.string.previous).setIcon(R.drawable.ic_menu_back)
				.setOnMenuItemClickListener(new OnPreviousListener())
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(R.string.next).setIcon(R.drawable.ic_menu_forward)
				.setOnMenuItemClickListener(new OnNextListener())
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	}

	class OnRefreshListener implements OnMenuItemClickListener {

		public boolean onMenuItemClick(MenuItem item) {
			// new Thread(new
			// GetData("select * from radlab order by cdate DESC"))
			// .start();
			return false;
		}

	}

	class OnPreviousListener implements OnMenuItemClickListener {

		public boolean onMenuItemClick(MenuItem item) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	class OnNextListener implements OnMenuItemClickListener {

		public boolean onMenuItemClick(MenuItem item) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	public class SetData implements Runnable {

		ArrayList<String> data;

		public SetData(ArrayList<String> data) {
			this.data = data;
		}

		public void run() {
			table.removeAllViews();
			setDates(System.currentTimeMillis());
			table.addView(firstrow);
			
			for (String line : data) {
				StringTokenizer tokenizer = new StringTokenizer(line);
				String date = tokenizer.nextToken();
				String name = tokenizer.nextToken();
				String time = tokenizer.nextToken() + " - "
						+ tokenizer.nextToken();
				
				int found = -1;
				for (int i = 0; i < 7; i++) {
					if (date.equals(texts[i].getText().toString())) {
						found = i;
						break;
					}
				}
				
				if (found > -1) {
					
				}
			}
//			getPreferenceScreen().removeAll();
//			if (data.size() == 0) {
//				return;
//			}
//
//			for (String line : data) {
//				StringTokenizer tokenizer = new StringTokenizer(line);
//				String date = tokenizer.nextToken();
//				String name = tokenizer.nextToken();
//				String time = tokenizer.nextToken() + " - "
//						+ tokenizer.nextToken();
//				PreferenceCategory category = (PreferenceCategory) getPreferenceScreen()
//						.findPreference(date);
//				if (category == null) {
//					category = new PreferenceCategory(getActivity());
//					category.setKey(date);
//					category.setTitle(date);
//					getPreferenceScreen().addPreference(category);
//				}
//				Preference item = new Preference(getActivity());
//				String Name = CheckInFragment.namelist.get(name);
//				if (Name != null) {
//					int length = Name.length();
//					for (int i = 0; i < 5 - length; i++) {
//						Name += "\t";
//					}
//					Name += time;
//					item.setTitle(Name);
//					category.addPreference(item);
//				}
//			}
		}

	}

	public class GetData implements Runnable {

		String query;

		public GetData(String query) {
			this.query = query;
		}

		public void run() {
			try {
				Socket socket = new Socket(MainActivity.SERVER_IP,
						MainActivity.SERVER_PORT);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(socket.getInputStream(), "UTF-8"));
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(
						new BufferedOutputStream(socket.getOutputStream()),
						"UTF-8"));

				writer.println(this.query);
				writer.flush();
				ArrayList<String> data = new ArrayList<String>();
				String line = reader.readLine();
				while (line != null) {
					System.out.println(line);
					data.add(line);
					line = reader.readLine();
				}

				reader.close();
				writer.close();
				socket.close();
				handler.post(new SetData(data));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}

		}

	}

	void setDates(long time) {
		Calendar calendar = Calendar.getInstance();
		int today = calendar.get(Calendar.DAY_OF_WEEK);
		int offset = today - Calendar.MONDAY;
		int temp = 0;
		while (temp < 7) {
			Date date = new Date(time - offset * 24 * 60 * 60 * 1000);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			dates[temp] = df.format(date);
			texts[0].setText(dates[temp]);
			temp++;
			offset--;
		}
		new Thread(new GetData("select * from radlab order by cdate DESC"))
				.start();
	}
}
