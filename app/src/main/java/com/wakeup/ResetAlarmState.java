package com.wakeup;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

public class ResetAlarmState {
    Context context;
    Alarm needAlarm;
    SharedPreferences sharedPreferences;


    public ResetAlarmState(Context contextGet){
        context = contextGet;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    final static String myLog = "myLog";

    public void changeAlarmWork(int alarmId){
        DatabaseHandler db = new DatabaseHandler(context);
        needAlarm = db.getAlarmById(alarmId);
        if(Integer.decode(String.valueOf(needAlarm.get_repetDays().charAt(0))) > 0) {// если стоит любое повторение по дням
            needAlarm.set_active(1);//не выключаем будильник
        }else {// если нет повторений, то выключаем
            needAlarm.set_active(0);
        }
        db.updateAlarm(new Alarm(needAlarm.getID(), needAlarm.get_hour(), needAlarm.get_minute(),
                needAlarm.get_active(), needAlarm.get_content(),
                needAlarm.get_Sound(),needAlarm.get_repetDays()));
    }

    public void changeAlarmWork(Alarm alarm){
        DatabaseHandler db = new DatabaseHandler(context);
        needAlarm = alarm;
        db.updateAlarm(new Alarm(needAlarm.getID(), needAlarm.get_hour(), needAlarm.get_minute(),
                needAlarm.get_active(), needAlarm.get_content(),
                needAlarm.get_Sound(),needAlarm.get_repetDays()));
    }

    public void makeAlarmDelay(int alarmId){
        DatabaseHandler db = new DatabaseHandler(context);
        needAlarm = db.getAlarmById(alarmId);

        Log.d(myLog,"ResetAlarmState  makeAlarmDelay ");

        int delayMinutes = 0;
        try {
            delayMinutes = Integer.decode(sharedPreferences.getString("setDelay", "non"));
            Log.d(myLog, "Delay = " + delayMinutes);
        }catch (Exception e){Log.e(myLog, "ошибка считывания времени задержки " );}

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, needAlarm.get_hour());
        calendar.set(Calendar.MINUTE, needAlarm.get_minute() + delayMinutes);

        db.updateAlarm(new Alarm(needAlarm.getID(), calendar.getTime().getHours(), calendar.getTime().getMinutes(),
                needAlarm.get_active(), needAlarm.get_content(),
                needAlarm.get_Sound(),needAlarm.get_repetDays()));
        }

    public void setComandToRemakeAlarms(){
        Intent setAlarmIntent = new Intent(context, AlarmService.class);
        setAlarmIntent.setAction(AlarmService.DOWHATNEED);
        context.startService(setAlarmIntent);
    }


}
