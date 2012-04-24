package com.jewelz.checkinhelper;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

	EditTextPreference IP_Preference;
	EditTextPreference Port_Preference;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings);

		IP_Preference = (EditTextPreference) findPreference("ip_p");
		IP_Preference.setSummary(MainActivity.SERVER_IP);
		IP_Preference
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						preference.setSummary(newValue.toString());
						MainActivity.prefs.edit()
								.putString("ip_p", newValue.toString())
								.commit();
						MainActivity.SERVER_IP = newValue.toString();
						return true;
					}
				});

		Port_Preference = (EditTextPreference) findPreference("port_p");
		Port_Preference.setSummary(MainActivity.SERVER_PORT + "");
		Port_Preference
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						preference.setSummary(newValue.toString());
						MainActivity.prefs.edit()
								.putString("port_p", newValue.toString())
								.commit();
						MainActivity.SERVER_PORT = Integer.parseInt(newValue
								.toString());
						return true;
					}
				});
	}

}
