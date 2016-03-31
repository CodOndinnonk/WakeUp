package com.wakeup;


import android.content.Context;
import android.content.Intent;

public class ResetAlarmState {
    Context context;
    Alarm needAlarm;


    public ResetAlarmState(Context contextGet){
        context = contextGet;
    }

    public void changeAlarmWork(int alarmId){
        DatabaseHandler db = new DatabaseHandler(context);
        needAlarm = db.getAlarmById(alarmId);
        if(Integer.valueOf(needAlarm.get_repetDays()) > 0) {// если стоит любое повторение по дням
            needAlarm.set_active(1);//не выключаем будильник
        }else {// если нет повторений, то выключаем
            needAlarm.set_active(0);
        }
        db.updateAlarm(new Alarm(needAlarm.getID(), needAlarm.get_hour(), needAlarm.get_minute(),
                needAlarm.get_active(), needAlarm.get_content(), needAlarm.get_everyDay(),
                needAlarm.get_Sound(),needAlarm.get_repetDays()));
    }

    public void changeAlarmWork(Alarm alarm){
        DatabaseHandler db = new DatabaseHandler(context);
        needAlarm = alarm;
        db.updateAlarm(new Alarm(needAlarm.getID(), needAlarm.get_hour(), needAlarm.get_minute(),
                needAlarm.get_active(), needAlarm.get_content(), needAlarm.get_everyDay(),
                needAlarm.get_Sound(),needAlarm.get_repetDays()));    }

    public void setComandToRemakeAlarms(){
        Intent setAlarmIntent = new Intent(context, AlarmService.class);
        setAlarmIntent.setAction(AlarmService.DOWHATNEED);
        context.startService(setAlarmIntent);
    }


}
