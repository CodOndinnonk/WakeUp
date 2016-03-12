package com.wakeup;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;

public class LocActivityHelper {
    Context context;
    public static final String ID = "id";
    int alarmId;

    public LocActivityHelper(Context contextGet,int alarmIdGet){
        context = contextGet;
        alarmId = alarmIdGet;
    }

    public void makeWakeActivityFromSleep(){
        //включение экрана
        Activity activity = (Activity) context;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void setComandToRemakeAlarms(){
        Intent setAlarmIntent = new Intent(context, AlarmService.class);
        setAlarmIntent.setAction(AlarmService.DOWHATNEED);
        context.startService(setAlarmIntent);
    }

    public void goToShowContent(){
        Intent goToShowContent = new Intent(context,ShowContent.class);
        goToShowContent.putExtra(ID, alarmId);
        context.startActivity(goToShowContent);
    }

    public void changeAlarmWork(){
        DatabaseHandler db = new DatabaseHandler(context);//переменная для работы с БД
        Alarm needAlarm = db.getAlarmById(alarmId);
        db.updateAlarm(new Alarm(needAlarm.getID(), needAlarm.get_hour(), needAlarm.get_minute(), 0, needAlarm.get_content(), needAlarm.get_everyDay(), needAlarm.get_Sound()));
    }



}
