package com.wakeup;


import android.content.DialogInterface;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.Spinner;

import java.util.ArrayList;

public class PreferenceUser extends PreferenceActivity {
    ListPreference  listLoc;
    final String myLog = "myLog";




       @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
           listLoc = (ListPreference)findPreference("listLocActivity");

           setSumarys();

    }

    protected void onResume() {
        super.onResume();
        setSumarys();
    }


    public void setSumarys(){//задаем отображение выбраных пораметров
        listLoc.setSummary(listLoc.getValue());

    }
}
