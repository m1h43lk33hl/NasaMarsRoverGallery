package com.example.nasaapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        if(getArguments() != null){
            String key = getArguments().getString("rootKey");
            setPreferencesFromResource(R.xml.preference_nasa, key);
        }else{
            setPreferencesFromResource(R.xml.preference_nasa, s);
        }

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();

        // For each preferenceItem
        for (int i = 0; i < prefScreen.getPreferenceCount(); i++) {
            Preference p = prefScreen.getPreference(i);

            if (!(p instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);

        // check if preference is instance of checkbox
        if (preference!= null) {
            if (!(preference instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);
            }
        }

        // Enable/Disable list
        if(key.equals(getString(R.string.pref_lang_check_key)))
        {
            boolean isEnabled = sharedPreferences.getBoolean(key, true);
            getPreferenceScreen().findPreference(getString(R.string.pref_lang_list_key)).setEnabled(!isEnabled);
        }

    }

    /**
     * Sets summary of preference
     *
     * @param preference
     * @param value
     */
    private void setPreferenceSummary(Preference preference, String value) {
        if (preference instanceof ListPreference) {

            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);

            if (prefIndex >= 0) {
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else if (preference instanceof EditTextPreference) {
            preference.setSummary(value);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        // Enable/Disable list
        boolean isEnabled =  getPreferenceScreen().getSharedPreferences().getBoolean(getString(R.string.pref_lang_check_key), true);
        getPreferenceScreen().findPreference(getString(R.string.pref_lang_list_key)).setEnabled(!isEnabled);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onNavigateToScreen(PreferenceScreen preferenceScreen){
        SettingsFragment applicationPreferencesFragment = new SettingsFragment();
        Bundle args = new Bundle();

        args.putString("rootKey", preferenceScreen.getKey());
        applicationPreferencesFragment.setArguments(args);

        getFragmentManager()
                .beginTransaction()
                .replace(getId(), applicationPreferencesFragment)
                .addToBackStack(null)
                .commit();
    }
}
