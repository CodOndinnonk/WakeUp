package com.wakeup;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;

import android.preference.Preference;
import android.preference.PreferenceActivity;

import android.os.Bundle;

import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class PreferenceUser extends PreferenceActivity {
    ListPreference listLoc;
    CheckBoxPreference checkBoxProverb;
    CheckBoxPreference checkBoxDelay;
    Preference delayTime;
    final String myLog = "myLog";
    String[] namesOfLocActivities;
    public static final String APP_PREFERENCES = "WakeUpSettings";// это будет именем файла настроек
    public static final String LOC_ACTIVITY = "LocActivity";// это будет именем файла настроек
    public static final String DELAY_TIME = "DelayTime";// это будет именем файла настроек


    SharedPreferences mSettings;//создание переменной, необходимой для работы с файлом настройки


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);// инициализация переменной
        Log.d(myLog, "PreferenceUser onCreate ");
        listLoc = (ListPreference) findPreference("listLocActivity");
        checkBoxProverb = (CheckBoxPreference) findPreference("proverbCheckBox");
        namesOfLocActivities = getResources().getStringArray(R.array.entriesLoc);
        checkBoxDelay = (CheckBoxPreference) findPreference("Delay");
        delayTime =  findPreference("setDelay");

        chackForFirstStart();




//        listLoc.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object o) {
//                SharedPreferences.Editor editor = mSettings.edit();// вызов обьекта editor для измненения параметров настроек
//                editor.putString(LOC_ACTIVITY, listLoc.getValue());// добавление в базу поля "Login" и значения, взятого из поля для ввода
//                Log.d(myLog, "LOC_ACTIVITY = " + listLoc.getValue());
//                editor.apply();// метод, сохраняющий добавленные данные
//
//                return false;
//            }
//        });
//
//        delayTime.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object o) {
//                SharedPreferences.Editor editor = mSettings.edit();// вызов обьекта editor для измненения параметров настроек
//                editor.putString(DELAY_TIME, o.toString());// добавление в базу поля "Login" и значения, взятого из поля для ввода
//                Log.d(myLog, "DELAY_TIME = " + o.toString());
//                editor.apply();// метод, сохраняющий добавленные данные
//                return false;
//            }
//        });



    }

    protected void onResume() {
        super.onResume();
        setSumarys();
    }


    public void setSumarys() {//задаем отображение выбраных пораметров
        listLoc.setSummary(listLoc.getValue());
        delayTime.setSummary(delayTime.getSharedPreferences().getString("setDelay",""));


    }


    public void chackForFirstStart() {//задаем отображение выбраных параметров
        if (listLoc.getValue() == null) {//если небыло выбрано настроек ранее, ставим дефолтовые значения
            listLoc.setValue(namesOfLocActivities[0]);
            checkBoxProverb.setChecked(true);
        } else {
            setSumarys();
        }
    }



}
