package com.wakeup;


import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.Spinner;

public class PreferenceUser extends PreferenceActivity {
    ListPreference  listLoc;
    public static final String APP_PREFERENCES = "mySettings";// это будет именем файла настроек
    public static final String APP_PREFERENCES_LOGIN = "Login"; // логин


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.pref);
        listLoc = (ListPreference)findPreference("listLocActivity");
        listLoc.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                return false;
            }
        });

    }

    protected void onResume() {
        SetSumarys();
        super.onResume();
    }

    public void SetSumarys(){//задаем отображение выбраных пораметров

    }


}
