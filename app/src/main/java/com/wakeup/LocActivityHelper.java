package com.wakeup;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager;

public class LocActivityHelper {
    Context context;
    public static final String ID = "id";
    public static final String IS_CONTENT = "isContent";
    public static final String IS_PROVERB = "isProverb";
    final String myLog = "myLog";
    int alarmId;
    SharedPreferences sharedPreferences;
    boolean isProverb;
    Alarm needAlarm;

    public LocActivityHelper(Context contextGet,int alarmIdGet){
        context = contextGet;
        alarmId = alarmIdGet;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        isProverb = sharedPreferences.getBoolean("proverbCheckBox", true);
        getNeedAlarm();
    }

    public void makeWakeActivityFromSleep(){
        //включение экрана
        Activity activity = (Activity) context;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }




    public void chackForProperties(boolean isContent, boolean isProverb){
        Intent goToShowContent = new Intent(context,ShowContent.class);
        goToShowContent.putExtra(ID, alarmId);
        goToShowContent.putExtra(IS_CONTENT, isContent);
        goToShowContent.putExtra(IS_PROVERB, isProverb);
        context.startActivity(goToShowContent);
    }

    public void getNeedAlarm(){
        DatabaseHandler db = new DatabaseHandler(context);
        needAlarm = db.getAlarmById(alarmId);
    }



    public void goToShowContent(){
        if(needAlarm.get_content().length() > 0){//если в будильнике есть заметка
            chackForProperties(true, isProverb);//включаем заметку, и передаем взятое из настроек состояние высказывания
        }else if(isProverb){//есль высказывание включено
            chackForProperties(false,isProverb);
        }else {
        }
    }





}
