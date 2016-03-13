package com.wakeup;


import android.preference.CheckBoxPreference;
import android.preference.ListPreference;

import android.preference.PreferenceActivity;

import android.os.Bundle;

import android.util.Log;

public class PreferenceUser extends PreferenceActivity {
    ListPreference  listLoc;
    CheckBoxPreference checkBoxProverb;
    final String myLog = "myLog";
    String[] namesOfLocActivities;



       @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
           Log.d(myLog, "PreferenceUser onCreate ");
           listLoc = (ListPreference)findPreference("listLocActivity");
           checkBoxProverb = (CheckBoxPreference)findPreference("proverbCheckBox");
           namesOfLocActivities = getResources().getStringArray(R.array.entriesLoc);

           chackForFirstStart();
    }

    protected void onResume() {
        super.onResume();
        setSumarys();
    }


    public void setSumarys(){//задаем отображение выбраных пораметров
        listLoc.setSummary(listLoc.getValue());
    }


    public void chackForFirstStart(){//задаем отображение выбраных параметров
        if(listLoc.getValue() == null){//если небыло выбрано настроек ранее, ставим дефолтовые значения
            listLoc.setValue(namesOfLocActivities[0]);
            checkBoxProverb.setChecked(true);
        }else {
            setSumarys();
        }
    }



}
